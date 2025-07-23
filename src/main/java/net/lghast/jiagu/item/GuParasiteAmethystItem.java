package net.lghast.jiagu.item;

import net.lghast.jiagu.entity.ParasiteSporeEntity;
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
    private static final int COOLDOWN_TICKS = 2000;
    private static final int BASE_DURATION = 160;
    private static final int BASE_AMPLIFIER = 0;

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

            float velocity = 0.5f + Math.min(1.8f, usingTicks/100f);
            projectile.shoot(look.x, look.y, look.z, velocity, 1.0f);

            level.addFreshEntity(projectile);
        }
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeCharged) {
        if (!(entity instanceof Player player)) return;

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
        Holder<Enchantment> maladyHolder = level.registryAccess().registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(ModEnchantments.MALADY);
        return (int) Math.max(15, COOLDOWN_TICKS * (1 - stack.getEnchantmentLevel(maladyHolder) * 0.12));
    }

    private int getAmplifier(ItemStack stack, Level level) {
        Holder<Enchantment> panaceaHolder = level.registryAccess().registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(ModEnchantments.PANACEA);
        return BASE_AMPLIFIER + stack.getEnchantmentLevel(panaceaHolder) / 3;
    }

    private int getDuration(ItemStack stack, Level level) {
        Holder<Enchantment> panaceaHolder = level.registryAccess().registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(ModEnchantments.PANACEA);
        return (int) (BASE_DURATION * (1 + stack.getEnchantmentLevel(panaceaHolder) * 0.25));
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
