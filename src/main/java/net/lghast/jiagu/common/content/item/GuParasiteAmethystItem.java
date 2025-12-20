package net.lghast.jiagu.common.content.item;

import net.lghast.jiagu.common.content.entity.ParasiteSporeEntity;
import net.lghast.jiagu.config.ServerConfig;
import net.lghast.jiagu.register.content.ModEnchantments;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

@ParametersAreNonnullByDefault
public class GuParasiteAmethystItem extends PrescriptionUserItem {
    public GuParasiteAmethystItem(Properties properties) {
        super(properties.rarity(Rarity.EPIC).stacksTo(1).durability(60));
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack repairCandidate) {
        return repairCandidate.is(Items.FERMENTED_SPIDER_EYE);
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    private void shootProjectile(Player player, Level level, MobEffectInstance mobEffectInstance) {
        if (!level.isClientSide) {
            ParasiteSporeEntity projectile = new ParasiteSporeEntity(level, player);
            projectile.setEffect(mobEffectInstance);

            Vec3 look = player.getLookAngle();

            float velocity = getVelocityMin() + Math.min(getVelocityBonusMax(), getUsingTicks()/100f);
            projectile.shoot(look.x, look.y, look.z, velocity, 1.0f);

            level.addFreshEntity(projectile);
        }
    }

    @Override
    protected void prescriptionEffect(ItemStack prescriptionStack, ItemStack stack, Level level, Player player) {
        Holder<MobEffect> effectHolder = PrescriptionItem.getEffectHolder(prescriptionStack);
        if(effectHolder != null){
            int amplifier = getAmplifier(stack, level);
            int duration = getDuration(stack, level);

            shootProjectile(player, level, new MobEffectInstance(effectHolder, duration, amplifier));
            prescriptionStack.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);

            updatePrescriptionInItemStack(stack, prescriptionStack);
            useSuccess(2, player, stack);
        }
    }

    @Override
    protected void normalEffect(ItemStack stack, Level level, Player player) {
        Holder<MobEffect> poisonHolder = level.registryAccess().registryOrThrow(Registries.MOB_EFFECT)
                .getHolderOrThrow(Objects.requireNonNull(MobEffects.POISON.getKey()));

        int amplifier = getAmplifier(stack, level);
        int duration = getDuration(stack, level);
        shootProjectile(player, level, new MobEffectInstance(poisonHolder, duration, amplifier));
        useSuccess(1, player, stack);
    }

    @Override
    protected int getCooldownTicks(ItemStack stack, Level level) {
        int cooldown = ServerConfig.GU_PARASITE_COOLDOWN_TIME.get();
        Holder<Enchantment> maladyHolder = level.registryAccess().registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(ModEnchantments.MALADY);
        return (int) Math.max(15, cooldown * (1 - stack.getEnchantmentLevel(maladyHolder) * 0.12));
    }

    @Override
    protected int getAmplifier(ItemStack stack, Level level) {
        int amplifier = ServerConfig.GU_PARASITE_BASE_AMPLIFIER.get();
        Holder<Enchantment> panaceaHolder = level.registryAccess().registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(ModEnchantments.PANACEA);
        return amplifier + stack.getEnchantmentLevel(panaceaHolder) / 3;
    }

    @Override
    protected int getDuration(ItemStack stack, Level level) {
        int duration = ServerConfig.GU_PARASITE_BASE_DURATION.get();
        Holder<Enchantment> panaceaHolder = level.registryAccess().registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(ModEnchantments.PANACEA);
        return (int) (duration * (1 + stack.getEnchantmentLevel(panaceaHolder) * 0.35));
    }

    private float getVelocityMin() {
        return ServerConfig.GU_PARASITE_PROJECTILE_VELOCITY_MIN.get().floatValue();
    }

    private float getVelocityBonusMax() {
        return ServerConfig.GU_PARASITE_PROJECTILE_VELOCITY_MAX.get().floatValue();
    }
}
