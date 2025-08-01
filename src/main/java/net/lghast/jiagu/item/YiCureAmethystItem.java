package net.lghast.jiagu.item;

import net.lghast.jiagu.register.ModEnchantments;
import net.lghast.jiagu.register.ModItems;
import net.lghast.jiagu.utils.ModUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class YiCureAmethystItem extends Item {
    private static final int COOLDOWN_TICKS = 2400;
    private static final int BASE_DURATION = 160;
    private static final int BASE_AMPLIFIER = 0;


    public YiCureAmethystItem(Properties properties) {
        super(properties.rarity(Rarity.EPIC).stacksTo(1).durability(50));
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack repairCandidate) {
        return repairCandidate.is(Items.GHAST_TEAR);
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 12;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);

        if (player.getCooldowns().isOnCooldown(this)) {
            return InteractionResultHolder.fail(stack);
        }

        if (stack.getDamageValue() >= stack.getMaxDamage()) {
            return InteractionResultHolder.fail(stack);
        }

        player.startUsingItem(usedHand);
        return InteractionResultHolder.success(stack);
    }

    @Override
    public void onStopUsing(ItemStack stack, LivingEntity entity, int count) {
        if(!(entity instanceof Player player)) return;

        Level level = player.level();
        ItemStack offHandItem = player.getOffhandItem();
        if(offHandItem.is(ModItems.PRESCRIPTION)){
            Holder<MobEffect> effectHolder = PrescriptionItem.getEffectHolder(offHandItem);
            if(effectHolder!=null && needEffect(getAmplifier(stack, level), getDuration(stack, level), effectHolder, player)){
                player.addEffect(new MobEffectInstance(effectHolder, (int)(getDuration(stack, level) * 1.2), getAmplifier(stack, level)));
                offHandItem.hurtAndBreak(1, player, EquipmentSlot.OFFHAND);
                useSuccess(3, player, stack);
                return;
            }
        }
        normalEffect(player, stack);
    }


    private void normalEffect(Player player, ItemStack stack){
        Holder<MobEffect> regenerationHolder = player.level().registryAccess().registryOrThrow(Registries.MOB_EFFECT)
                .getHolderOrThrow(Objects.requireNonNull(MobEffects.REGENERATION.getKey()));

        Level level = player.level();

        if(needCure(BASE_AMPLIFIER, BASE_DURATION, regenerationHolder, player)) {
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
            useSuccess(1, player, stack);
        }
    }

    private void useSuccess(int damage, Player player,ItemStack stack){
        stack.hurtAndBreak(damage, player, EquipmentSlot.MAINHAND);

        Level level = player.level();
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.AMETHYST_CLUSTER_BREAK, SoundSource.NEUTRAL);
        player.getCooldowns().addCooldown(this, getCooldownTicks(stack, level));

        if(level instanceof ServerLevel serverLevel) {
            ModUtils.spawnParticlesForAll(serverLevel, ParticleTypes.HEART,
                    player.getX(), player.getY() + 0.8, player.getZ(), 0.35, 0.2, 0.35, 5, 0.01);
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

    private int getCooldownTicks(ItemStack stack, Level level){
        Holder<Enchantment> hippocratesHolder = level.registryAccess().registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(ModEnchantments.HIPPOCRATES);
        return (int) Math.max(20, COOLDOWN_TICKS * (1 - stack.getEnchantmentLevel(hippocratesHolder)*0.12));
    }

    private int getAmplifier(ItemStack stack, Level level){
        Holder<Enchantment> panaceaHolder = level.registryAccess().registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(ModEnchantments.PANACEA);
        return BASE_AMPLIFIER + stack.getEnchantmentLevel(panaceaHolder)/3;
    }

    private int getDuration(ItemStack stack, Level level){
        Holder<Enchantment> panaceaHolder = level.registryAccess().registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(ModEnchantments.PANACEA);
        return (int) (BASE_DURATION * (1 + stack.getEnchantmentLevel(panaceaHolder)*0.25));
    }

    @Override
    public int getEnchantmentValue(ItemStack stack) {
        return 10;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return true;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }
}
