package net.lghast.jiagu.item;

import net.lghast.jiagu.register.ModEnchantments;
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
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class YiConflagrantAmethystItem extends Item {
    private int cooldownTicks = 0;
    private static final int INTERVAL = 20;
    private static final float DAMAGE = 1;

    public YiConflagrantAmethystItem(Properties properties) {
        super(properties.rarity(Rarity.EPIC).stacksTo(1).durability(60));
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
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);

        if (stack.getDamageValue() >= stack.getMaxDamage()) {
            return InteractionResultHolder.fail(stack);
        }

        cooldownTicks = 0;

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
        if (level.isClientSide)  return;

        cooldownTicks++;

        if (cooldownTicks < getInterval(level, stack))  return;
        cooldownTicks = 0;

        double rangeXZ = 4;
        double rangeY = 1.5;
        AABB area = new AABB(
                user.getX() - rangeXZ, user.getY() - rangeY, user.getZ() - rangeXZ,
                user.getX() + rangeXZ, user.getY() + rangeY, user.getZ() + rangeXZ
        );

        for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class, area,
                e -> e != user && e.isAlive())) {

            Holder<DamageType> damageTypeHolder = level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE)
                    .getHolderOrThrow(DamageTypes.ON_FIRE);
            DamageSource source = new DamageSource(damageTypeHolder, user);

            if (target.hurt(source, DAMAGE)) {
                if (level instanceof ServerLevel serverLevel) {
                    ModUtils.spawnParticlesForAll(serverLevel, ParticleTypes.FLAME,
                            target.getX(), target.getY() + 1.0, target.getZ(), 0.5, 0.5, 0.5, 20, 0.02);
                    serverLevel.playSound(null, target.getOnPos(), SoundEvents.GENERIC_BURN, SoundSource.NEUTRAL);
                }

                Holder<Enchantment> ignitingHolder = level.registryAccess().registryOrThrow(Registries.ENCHANTMENT)
                        .getHolderOrThrow(ModEnchantments.IGNITING);
                if(stack.getEnchantmentLevel(ignitingHolder)>0){
                    target.igniteForSeconds(5);
                }

                if (level.getRandom().nextFloat() < 0.4f) {
                    if (level.getRandom().nextFloat() < 0.04f) {
                        stack.hurtAndBreak(1, user, EquipmentSlot.MAINHAND);
                    }
                    if (stack.isEmpty()) break;
                }
            }
        }
    }

    private float getInterval(Level level, ItemStack stack){
        Holder<Enchantment> cremationHolder = level.registryAccess().registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(ModEnchantments.CREMATION);
        return Math.max(1, INTERVAL * (1.0f - (stack.getEnchantmentLevel(cremationHolder) * 0.17f)));
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
