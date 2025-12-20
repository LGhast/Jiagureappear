package net.lghast.jiagu.common.content.item;

import net.lghast.jiagu.common.content.entity.StoneShotEntity;
import net.lghast.jiagu.config.ServerConfig;
import net.lghast.jiagu.register.content.ModEnchantments;
import net.lghast.jiagu.register.system.ModTags;
import net.lghast.jiagu.utils.ModUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class LeiStonesAmethystItem extends Item {
    private int usingTicks = 0;

    public LeiStonesAmethystItem(Properties properties) {
        super(properties.rarity(Rarity.EPIC).stacksTo(1).durability(50));
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack repairCandidate) {
        return repairCandidate.is(Items.OBSIDIAN) || repairCandidate.is(Items.CRYING_OBSIDIAN);
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
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

    private void shootProjectile(Player player, Level level, ItemStack stoneShot, ItemStack leiStones) {
        if (!level.isClientSide) {
            StoneShotEntity projectile = new StoneShotEntity(level, player, getRadius(level, leiStones), getDamage(level, leiStones));
            projectile.setItem(stoneShot);

            Vec3 look = player.getLookAngle();

            float velocity = getVelocityMin() + Math.min(getVelocityBonusMax(), usingTicks/100f);
            projectile.shoot(look.x, look.y, look.z, velocity, 1.0f);

            level.addFreshEntity(projectile);

            if(!player.isCreative()){
              stoneShot.shrink(1);
            }
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
        ItemStack stoneShot = getStoneStack(player);
        if(stoneShot.isEmpty()){
            useFailure(player);
        }else{
            shootProjectile(player, level, stoneShot, stack);
            useSuccess(player, stack);
        }
    }

    private void useSuccess(Player player, ItemStack stack) {
        stack.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);

        player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.SNOW_GOLEM_SHOOT, SoundSource.NEUTRAL);
        player.getCooldowns().addCooldown(this, getCooldownTicks());
    }

    private void useFailure(Player player) {
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.DISPENSER_FAIL, SoundSource.NEUTRAL);
        if(player.level() instanceof ServerLevel serverLevel) {
            ModUtils.spawnParticles(serverLevel, ParticleTypes.SMOKE, player.getX(), player.getY()+1, player.getZ(),
                    0.5, 0.5, 0.5, 15, 0.01);
        }
    }

    private int getCooldownTicks() {
        return ServerConfig.LEI_STONES_COOLDOWN_TIME.get();
    }

    private float getDamage(Level level, ItemStack stack) {
        Holder<Enchantment> massivenessHolder = level.registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(ModEnchantments.MASSIVENESS);
        float baseDamage = ServerConfig.LEI_STONES_BASE_DAMAGE.get().floatValue();
        int massivenessLevel = stack.getEnchantmentLevel(massivenessHolder);
        return baseDamage * (1 + massivenessLevel * 0.3f);
    }

    private float getRadius(Level level, ItemStack stack) {
        Holder<Enchantment> aftershockHolder = level.registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(ModEnchantments.AFTERSHOCK);
        float baseRadius = ServerConfig.LEI_STONES_BASE_RADIUS.get().floatValue();
        int aftershockLevel = stack.getEnchantmentLevel(aftershockHolder);
        return baseRadius + aftershockLevel * 0.4f;
    }

    private float getVelocityMin() {
        return ServerConfig.LEI_STONES_PROJECTILE_VELOCITY_MIN.get().floatValue();
    }

    private float getVelocityBonusMax() {
        return ServerConfig.LEI_STONES_PROJECTILE_VELOCITY_MAX.get().floatValue();
    }

    private ItemStack getStoneStack(Player player) {
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (isStone(stack)) {
                return stack;
            }
        }
        return player.isCreative()? new ItemStack(Items.STONE) : ItemStack.EMPTY;
    }

    private boolean isStone(ItemStack stack) {
        if (stack.isEmpty()) return false;
        return stack.is(ModTags.SHOOTABLE_STONES);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag tooltipFlag) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if (player != null) {
            ItemStack stoneStack = getStoneStack(player);
            if(stoneStack.isEmpty()){
                components.add(Component.translatable("tooltip.jiagureappear.shot_no")
                        .withStyle(ChatFormatting.DARK_GRAY));
            }else {
                String shotName = stoneStack.getHoverName().getString();
                components.add(Component.translatable("tooltip.jiagureappear.shot", shotName)
                        .withStyle(ChatFormatting.GRAY));
            }
        }
        super.appendHoverText(stack, context, components, tooltipFlag);
    }

    @Override
    public @NotNull UseAnim getUseAnimation(ItemStack stack) {
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
