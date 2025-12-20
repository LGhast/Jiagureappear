package net.lghast.jiagu.common.content.item;

import net.lghast.jiagu.config.ServerConfig;
import net.lghast.jiagu.register.content.ModEnchantments;
import net.lghast.jiagu.utils.ModUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ParametersAreNonnullByDefault
public class YiCureAmethystItem extends PrescriptionUserItem{

    public YiCureAmethystItem(Properties properties) {
        super(properties.rarity(Rarity.EPIC).stacksTo(1).durability(60));
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack repairCandidate) {
        return repairCandidate.is(Items.GHAST_TEAR);
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 200;
    }

    @Override
    protected void prescriptionEffect(ItemStack prescriptionStack, ItemStack stack, Level level, Player player) {
        if(getUsingTicks() < 20){
            return;
        }
        Holder<MobEffect> effectHolder = PrescriptionItem.getEffectHolder(prescriptionStack);
        if(effectHolder == null){
            normalEffect(stack, level, player);
            return;
        }
        if(needEffect(getAmplifier(stack, level), getDuration(stack, level), effectHolder, player)) {
            int duration = (int)(getDuration(stack, level) * 1.2);
            int amplifier = getAmplifier(stack, level);
            MobEffectInstance effectInstance = new MobEffectInstance(effectHolder, duration, amplifier);

            player.addEffect(effectInstance);
            prescriptionStack.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);

            updatePrescriptionInItemStack(stack, prescriptionStack);
            if(level instanceof ServerLevel serverLevel) {
               spawnParticles(serverLevel, effectHolder, player);
            }
            useSuccess(2, player, stack);
        }
    }

    @Override
    protected void normalEffect(ItemStack stack, Level level, Player player){
        if(getUsingTicks() < 20){
            return;
        }
        Holder<MobEffect> regenerationHolder = player.level().registryAccess().registryOrThrow(Registries.MOB_EFFECT)
                .getHolderOrThrow(Objects.requireNonNull(MobEffects.REGENERATION.getKey()));

        if(needCure(getAmplifier(stack, level), getDuration(stack, level), regenerationHolder, player)) {
            player.addEffect(new MobEffectInstance(regenerationHolder, getDuration(stack, level), getAmplifier(stack, level)));

            Holder<Enchantment> benevolenceHolder = level.registryAccess().registryOrThrow(Registries.ENCHANTMENT)
                    .getHolderOrThrow(ModEnchantments.BENEVOLENCE);
            int benevolenceLevel = stack.getEnchantmentLevel(benevolenceHolder);
            if(benevolenceLevel>0){
                List<MobEffectInstance> effects = new ArrayList<>(player.getActiveEffects());
                if(!effects.isEmpty()) {

                    for (MobEffectInstance e : effects) {
                        Holder<MobEffect> effectHolder = e.getEffect();

                        if (!effectHolder.value().isBeneficial() && e.getAmplifier() <= benevolenceLevel) {
                            player.removeEffect(effectHolder);
                        }
                    }
                }
            }
            if(level instanceof ServerLevel serverLevel) {
                spawnParticles(serverLevel, regenerationHolder, player);
            }
            useSuccess(1, player, stack);
        }
    }

    private void spawnParticles(ServerLevel serverLevel, Holder<MobEffect> holder, Player player){
        if(holder.value().isBeneficial()){
            ModUtils.spawnParticles(serverLevel, ParticleTypes.HEART,
                    player.getX(), player.getY() + 0.9, player.getZ(), 0.35, 0.3, 0.35, 8, 0.01);
        }else{
            ModUtils.spawnParticles(serverLevel, ParticleTypes.WITCH,
                    player.getX(), player.getY() + 0.9, player.getZ(), 0.35, 0.3, 0.35, 8, 0.01);
        }
    }

    private boolean needEffect(int amplifier, int duration, Holder<MobEffect> effectHolder, Player player){
        MobEffectInstance effect = player.getEffect(effectHolder);
        if(effect==null) return true;

        if(effect.getAmplifier() > amplifier) return false;
        if(effect.getAmplifier() == amplifier) return effect.getDuration() < duration;
        return true;
    }

    private boolean needCure(int amplifier, int duration, Holder<MobEffect> regenerationHolder, Player player){
        if(!(player.isCreative()) && player.getHealth()>=player.getMaxHealth()) return false;
        return needEffect(amplifier, duration, regenerationHolder, player);
    }

    @Override
    protected int getCooldownTicks(@NotNull ItemStack stack, @NotNull Level level){
        int cooldown = ServerConfig.YI_CURE_COOLDOWN_TIME.get();
        Holder<Enchantment> hippocratesHolder = level.registryAccess().registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(ModEnchantments.HIPPOCRATES);
        return (int) Math.max(20, cooldown * (1 - stack.getEnchantmentLevel(hippocratesHolder)*0.12));
    }

    @Override
    protected int getAmplifier(@NotNull ItemStack stack, @NotNull Level level){
        int amplifier = ServerConfig.YI_CURE_BASE_AMPLIFIER.get();
        Holder<Enchantment> panaceaHolder = level.registryAccess().registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(ModEnchantments.PANACEA);
        return amplifier + stack.getEnchantmentLevel(panaceaHolder)/3;
    }

    @Override
    protected int getDuration(@NotNull ItemStack stack, @NotNull Level level){
        int duration = ServerConfig.YI_CURE_BASE_DURATION.get();
        Holder<Enchantment> panaceaHolder = level.registryAccess().registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(ModEnchantments.PANACEA);
        return (int) (duration * (1 + stack.getEnchantmentLevel(panaceaHolder)*0.35));
    }
}
