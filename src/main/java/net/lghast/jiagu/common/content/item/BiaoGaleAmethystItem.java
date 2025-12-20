package net.lghast.jiagu.common.content.item;

import net.lghast.jiagu.config.ServerConfig;
import net.lghast.jiagu.register.content.ModEnchantments;
import net.lghast.jiagu.utils.ModUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class BiaoGaleAmethystItem extends Item {
    private int usingTicks = 0;

    public BiaoGaleAmethystItem(Properties properties) {
        super(properties.rarity(Rarity.EPIC).stacksTo(1).durability(60));
    }

    @Override
    public boolean isRepairable(ItemStack stack) {
        return true;
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack repairCandidate) {
        return repairCandidate.is(Items.BREEZE_ROD);
    }

    @Override
    public @NotNull UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);

        if (stack.getDamageValue() >= stack.getMaxDamage()) {
            return InteractionResultHolder.fail(stack);
        }

        usingTicks = 0;
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
        if(!(user instanceof Player player) || !(level instanceof ServerLevel serverLevel)) return;

        usingTicks ++;
        spawnParticles(serverLevel, player);
        if (usingTicks % 15 == 0) {
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.BREEZE_IDLE_GROUND, SoundSource.NEUTRAL);
        }

        Vec3 eyePos = player.getEyePosition();
        Vec3 lookVec = player.getLookAngle();
        Vec3 newEyePos = eyePos.add(lookVec.scale(0.5));

        double windDistance = getWindDistance(level, stack);
        double yCenter = player.getY();
        AABB area = new AABB(
                player.getX()-windDistance, yCenter-0.5, player.getZ()-windDistance,
                player.getX()+windDistance, yCenter+2, player.getZ()+windDistance
        );

        List<Entity> entities = level.getEntities(
                EntityTypeTest.forClass(Entity.class),
                area,
                e -> e != player
                        && (e.isPushable() || e instanceof ArmorStand || e instanceof ItemEntity)
                        && isInSector(newEyePos, lookVec, e.position(), windDistance, getWindAngle())
        );

        if(entities.isEmpty()) return;

        for (Entity entity : entities) {
            Vec3 targetPos = entity.position();
            Vec3 pushDir = targetPos.subtract(eyePos).normalize();

            BlockHitResult hitResult = level.clip(new ClipContext(
                    eyePos, targetPos,
                    ClipContext.Block.COLLIDER,
                    ClipContext.Fluid.NONE,
                    player
            ));

            if (hitResult.getType() == BlockHitResult.Type.BLOCK) {
                double blockDist = eyePos.distanceTo(hitResult.getLocation());
                double entityDist = eyePos.distanceTo(targetPos);
                if (blockDist < entityDist) continue;
            }

            AABB box = entity.getBoundingBox();
            double volume = entity instanceof ItemEntity itemEntity ?
                    itemEntity.getItem().getCount()/100.0+1 : box.getXsize() * box.getYsize() * box.getZsize();
            double speed = getWindSpeed(level, stack) / (1 + volume * getVolumeFactor());

            Vec3 velocity = entity.getDeltaMovement()
                    .add(pushDir.scale(speed))
                    .scale(0.9);
            entity.setDeltaMovement(velocity);
            entity.hurtMarked = true;
        }

        if (level.getRandom().nextFloat() < 0.04f) {
            stack.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
        }
    }

    private boolean isInSector(Vec3 origin, Vec3 direction, Vec3 target, double maxDist, double angleDeg){
        Vec3 toTarget = target.subtract(origin);
        double distance = toTarget.length();

        if (distance > maxDist || distance < 0.01) return false;

        double dot = direction.dot(toTarget.normalize());
        dot = Math.max(-1.0, Math.min(1.0, dot));
        double angleRad = Math.acos(dot);

        return Math.toDegrees(angleRad) <= angleDeg;
    }

    private double getWindDistance(Level level, ItemStack stack){
        double baseDistance = ServerConfig.BIAO_GALE_BASE_DISTANCE.get();
        Holder<Enchantment> wutheringHolder = level.registryAccess().registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(ModEnchantments.WUTHERING);
        return baseDistance * (stack.getEnchantmentLevel(wutheringHolder) * 0.2 + 1);
    }

    private double getWindSpeed(Level level, ItemStack stack){
        double windSpeed = ServerConfig.BIAO_GALE_BASE_SPEED.get();
        Holder<Enchantment> flurryHolder = level.registryAccess().registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(ModEnchantments.FLURRY);
        return windSpeed * (stack.getEnchantmentLevel(flurryHolder) * 0.25 + 1);
    }

    private int getWindAngle(){
        return ServerConfig.BIAO_GALE_WIND_ANGLE.get();
    }

    private double getVolumeFactor(){
        return ServerConfig.BIAO_GALE_VOLUME_FACTOR.get();
    }

    private void spawnParticles(ServerLevel serverLevel, Player player){
        if (usingTicks % 10 == 0) {
            ModUtils.spawnParticles(serverLevel, ParticleTypes.GUST_EMITTER_SMALL,
                    player.getX(), player.getY() + 0.5, player.getZ(), 0.25, 0.25, 0.25, 1, 0.25);
        }
        if (usingTicks % 15 == 0) {
            ModUtils.spawnParticles(serverLevel, ParticleTypes.GUST_EMITTER_LARGE,
                    player.getX(), player.getY() + 0.5, player.getZ(), 0.25, 0.25, 0.25, 1, 0.25);
        }
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
