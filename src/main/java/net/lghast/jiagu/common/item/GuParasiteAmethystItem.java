package net.lghast.jiagu.common.item;

import net.lghast.jiagu.common.entity.ParasiteSporeEntity;
import net.lghast.jiagu.config.ServerConfig;
import net.lghast.jiagu.register.ModEnchantments;
import net.lghast.jiagu.register.ModItems;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
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
import net.minecraft.world.phys.Vec3;

import java.util.Objects;

public class GuParasiteAmethystItem extends Item {
    private int usingTicks = 0;

    public GuParasiteAmethystItem(Properties properties) {
        super(properties.rarity(Rarity.EPIC).stacksTo(1).durability(50));
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack repairCandidate) {
        return repairCandidate.is(Items.FERMENTED_SPIDER_EYE);
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
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

        usingTicks = 0;
        player.startUsingItem(usedHand);
        return InteractionResultHolder.success(stack);
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        usingTicks++;
        super.onUseTick(level, livingEntity, stack, remainingUseDuration);
    }

    private void shootProjectile(Player player, Level level, Holder<MobEffect> effect, int duration, int amplifier) {
        if (!level.isClientSide) {
            ParasiteSporeEntity projectile = new ParasiteSporeEntity(level, player);
            projectile.setEffect(new MobEffectInstance(effect, duration, amplifier));

            Vec3 look = player.getLookAngle();

            float velocity = getVelocityMin() + Math.min(getVelocityBonusMax(), usingTicks/100f);
            projectile.shoot(look.x, look.y, look.z, velocity, 1.0f);

            level.addFreshEntity(projectile);
        }
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeCharged) {
        if(entity.level().isClientSide){
            return;
        }
        if (!(entity instanceof Player player)) {
            return;
        }

        ItemStack offHandItem = player.getOffhandItem();
        if (offHandItem.is(ModItems.PRESCRIPTION)) {
            Holder<MobEffect> effectHolder = PrescriptionItem.getEffectHolder(offHandItem);
            if (effectHolder != null) {
                int amplifier = getAmplifier(stack, level);
                int duration = getDuration(stack, level);
                shootProjectile(player, level, effectHolder, duration, amplifier);
                offHandItem.hurtAndBreak(1, player, EquipmentSlot.OFFHAND);
                useSuccess(3, player, stack);
                return;
            }
        }
        normalEffect(player, stack);
    }

    private void normalEffect(Player player, ItemStack stack) {
        Level level = player.level();
        Holder<MobEffect> poisonHolder = level.registryAccess().registryOrThrow(Registries.MOB_EFFECT)
                .getHolderOrThrow(Objects.requireNonNull(MobEffects.POISON.getKey()));

        int amplifier = getAmplifier(stack, level);
        int duration = getDuration(stack, level);
        shootProjectile(player, level, poisonHolder, duration, amplifier);
        useSuccess(1, player, stack);
    }

    private void useSuccess(int damage, Player player, ItemStack stack) {
        stack.hurtAndBreak(damage, player, EquipmentSlot.MAINHAND);

        player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.AMETHYST_CLUSTER_BREAK, SoundSource.NEUTRAL);
        player.getCooldowns().addCooldown(this, getCooldownTicks(stack, player.level()));
    }

    private int getCooldownTicks(ItemStack stack, Level level) {
        int cooldown = ServerConfig.GU_PARASITE_COOLDOWN_TIME.get();
        Holder<Enchantment> maladyHolder = level.registryAccess().registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(ModEnchantments.MALADY);
        return (int) Math.max(15, cooldown * (1 - stack.getEnchantmentLevel(maladyHolder) * 0.12));
    }

    private int getAmplifier(ItemStack stack, Level level) {
        int amplifier = ServerConfig.GU_PARASITE_BASE_AMPLIFIER.get();
        Holder<Enchantment> panaceaHolder = level.registryAccess().registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(ModEnchantments.PANACEA);
        return amplifier + stack.getEnchantmentLevel(panaceaHolder) / 3;
    }

    private int getDuration(ItemStack stack, Level level) {
        int duration = ServerConfig.GU_PARASITE_BASE_DURATION.get();
        Holder<Enchantment> panaceaHolder = level.registryAccess().registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(ModEnchantments.PANACEA);
        return (int) (duration * (1 + stack.getEnchantmentLevel(panaceaHolder) * 0.25));
    }

    private float getVelocityMin() {
        return ServerConfig.GU_PARASITE_PROJECTILE_VELOCITY_MIN.get().floatValue();
    }

    private float getVelocityBonusMax() {
        return ServerConfig.GU_PARASITE_PROJECTILE_VELOCITY_MAX.get().floatValue();
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
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
