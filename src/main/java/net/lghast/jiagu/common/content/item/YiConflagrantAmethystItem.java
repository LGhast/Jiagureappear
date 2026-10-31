package net.lghast.jiagu.common.content.item;

import net.lghast.jiagu.config.CommonConfig;
import net.lghast.jiagu.register.content.ModEnchantments;
import net.lghast.jiagu.register.system.ModTags;
import net.lghast.jiagu.utils.ModUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class YiConflagrantAmethystItem extends Item {
    private static final double RANGE_XZ = 4;
    private static final double RANGE_Y = 1.5;

    public YiConflagrantAmethystItem(Properties properties) {
        super(properties.rarity(Rarity.EPIC).stacksTo(1).durability(80));
    }

    @Override
    public boolean isRepairable(ItemStack stack) {
        return true;
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack repairCandidate) {
        return repairCandidate.is(Items.BLAZE_ROD);
    }

    @Override
    public @NotNull UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.NONE;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        if (stack.getDamageValue() >= stack.getMaxDamage()) {
            return InteractionResultHolder.fail(stack);
        }
        player.startUsingItem(usedHand);
        return InteractionResultHolder.success(stack);
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    @Override
    public void onUseTick(Level level, LivingEntity user, ItemStack stack, int remainingUseDuration) {
        super.onUseTick(level, user, stack, remainingUseDuration);

        if (level.isClientSide) {
            spawnRangeParticles(level, user);
            return;
        }

        int interval = Math.max(1, Math.round(getInterval(level, stack)));
        int elapsed = getUseDuration(stack, user) - remainingUseDuration;
        if (elapsed <= 0 || elapsed % interval != 0) return;

        AABB area = new AABB(
                user.getX() - RANGE_XZ, user.getY() - RANGE_Y, user.getZ() - RANGE_XZ,
                user.getX() + RANGE_XZ, user.getY() + RANGE_Y, user.getZ() + RANGE_XZ
        );
        for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class, area, e -> e != user && e.isAlive())) {
            target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 0, false, false));

            double dx = target.getX() - user.getX();
            double dz = target.getZ() - user.getZ();
            double distance = Math.sqrt(dx * dx + dz * dz);

            Holder<DamageType> damageTypeHolder = level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE)
                    .getHolderOrThrow(DamageTypes.IN_FIRE);
            DamageSource source = new DamageSource(damageTypeHolder, user);
            if (target.hurt(source, getDamage(level, target, distance))) {
                Holder<Enchantment> ignitingHolder = level.registryAccess().registryOrThrow(Registries.ENCHANTMENT)
                        .getHolderOrThrow(ModEnchantments.IGNITING);
                if (stack.getEnchantmentLevel(ignitingHolder) > 0) {
                    target.igniteForSeconds(5);
                }
                if (level.getRandom().nextFloat() < 0.1f) {
                    stack.hurtAndBreak(1, user, EquipmentSlot.MAINHAND);
                    if (stack.isEmpty()) break;
                }
                burnVisualEffect(level, target);
            }
        }
    }

    private void spawnRangeParticles(Level level, LivingEntity user) {
        double y = user.getY();
        for (int i = 0; i < 24; i++) {
            double angle = i * (2 * Math.PI / 24);
            double x = user.getX() + Math.cos(angle) * RANGE_XZ;
            double z = user.getZ() + Math.sin(angle) * RANGE_XZ;
            level.addParticle(ParticleTypes.FLAME, x, y, z, 0, 0, 0);
        }
    }

    private void burnVisualEffect(Level level, LivingEntity target) {
        if (level instanceof ServerLevel serverLevel) {
            ModUtils.spawnParticles(serverLevel, ParticleTypes.FLAME,
                    target.getX(), target.getY() + 1.0, target.getZ(), 0.5, 0.5, 0.5, 20, 0.02);
            serverLevel.playSound(null, target.getOnPos(), SoundEvents.GENERIC_BURN, SoundSource.NEUTRAL);
        }
    }

    private float getInterval(Level level, ItemStack stack) {
        Holder<Enchantment> cremationHolder = level.registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(ModEnchantments.CREMATION);
        int baseInterval = CommonConfig.YI_CONFLAGRANT_ATTACK_INTERVAL.get();
        return Math.max(1, baseInterval * (1.0f - (stack.getEnchantmentLevel(cremationHolder) * 0.17f)));
    }

    private float getDamage(Level level, LivingEntity target, double distance) {
        float centerDamage = CommonConfig.YI_CONFLAGRANT_CENTER_DAMAGE.get().floatValue();
        float edgeDamage = CommonConfig.YI_CONFLAGRANT_EDGE_DAMAGE.get().floatValue();

        float ratio = (float) Math.clamp(distance / RANGE_XZ, 0.0, 1.0);
        float damage = centerDamage + (edgeDamage - centerDamage) * ratio;

        boolean extraDamageToFur = CommonConfig.YI_CONFLAGRANT_EXTRA_DAMAGE_TO_FUR.get();
        if (extraDamageToFur && target.getType().is(ModTags.MOBS_WITH_FUR)) {
            return level.getRandom().nextFloat() < 0.4f ? damage * 2 : damage;
        }
        return damage;
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
