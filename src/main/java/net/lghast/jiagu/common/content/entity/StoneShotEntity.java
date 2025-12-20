package net.lghast.jiagu.common.content.entity;

import net.lghast.jiagu.register.content.ModEntityTypes;
import net.lghast.jiagu.utils.ModUtils;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class StoneShotEntity extends ThrowableItemProjectile {
    private final float explosion_radius;
    private final float damage;

    public StoneShotEntity(EntityType<? extends StoneShotEntity> type, Level level) {
        super(type, level);
        this.explosion_radius = 1.3f;
        this.damage = 4.0f;
    }

    public StoneShotEntity(Level level, LivingEntity shooter, float explosion_radius, float damage) {
        super(ModEntityTypes.STONE_SHOT.get(), shooter, level);
        this.explosion_radius = explosion_radius;
        this.damage = damage;
    }

    private float getRadius() {
        return explosion_radius;
    }

    private float getDamage() {
        if(getItem().getItem() instanceof BlockItem blockItem){
            float destroyTime = blockItem.getBlock().defaultDestroyTime();
            return damage * getMultiplier(destroyTime);
        }
        return damage;
    }

    private float getMultiplier(float destroyTime){
        if(destroyTime < 0 ) {
            return 20f;
        }
        double multiplier;
        if(destroyTime <= 10) {
            multiplier = -0.0174*destroyTime*destroyTime+0.534*destroyTime+0.4;
        }else{
            multiplier = 1.864 * Math.log(destroyTime) - 0.29;
        }
        return (float) Math.round(multiplier);
    }

    @Override
    protected @NotNull Item getDefaultItem() {
        return Blocks.STONE.asItem();
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);

        if (!this.level().isClientSide) {
            shotExplosionEffect(result.getLocation());
            this.discard();
        }
    }

    private void shotExplosionEffect(Vec3 explosionPos) {
        AABB area = new AABB(
                explosionPos.x - getRadius(), explosionPos.y - getRadius(), explosionPos.z - getRadius(),
                explosionPos.x + getRadius(), explosionPos.y + getRadius(), explosionPos.z + getRadius()
        );
        playExplosionEffects(explosionPos);

        for (LivingEntity entity : this.level().getEntitiesOfClass(LivingEntity.class, area)) {
            if (entity != this.getOwner()) {
                double distance = entity.distanceToSqr(explosionPos);
                double maxDistanceSqr = getRadius() * getRadius();

                if (distance <= maxDistanceSqr) {
                    entity.hurt(this.damageSources().thrown(this, this.getOwner()), getDamage());
                    shotKnockback(entity, explosionPos, distance, maxDistanceSqr);
                }
            }
        }
    }

    private void shotKnockback(LivingEntity entity, Vec3 explosionPos, double distanceSqr, double maxDistanceSqr) {
        Vec3 direction = entity.position().subtract(explosionPos).normalize();
        float distanceFactor = 1.0f - (float)(distanceSqr / maxDistanceSqr);
        float knockbackStrength = 2.0f * distanceFactor;

        entity.setDeltaMovement(
                entity.getDeltaMovement().add(
                        direction.x * knockbackStrength,
                        direction.y * knockbackStrength,
                        direction.z * knockbackStrength
                )
        );
        entity.hurtMarked = true;
    }

    private void playExplosionEffects(Vec3 pos) {
        this.level().playSound(null, pos.x, pos.y, pos.z,
                SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 1.0f, 1.0f);
        if (this.level() instanceof ServerLevel serverLevel) {
            ModUtils.spawnParticles(serverLevel, ParticleTypes.EXPLOSION, pos.x, pos.y, pos.z,
                    getRadius()/2, getRadius()/2, getRadius()/2, 3, 0.03);
        }
        spawnParticles();
    }

    private void spawnParticles() {
        if (this.level() instanceof ServerLevel serverLevel) {
            ParticleOptions particleOptions;
            Item item = getItem().getItem();

            if (item instanceof BlockItem blockItem) {
                Block block = blockItem.getBlock();
                particleOptions = new BlockParticleOption(ParticleTypes.BLOCK, block.defaultBlockState());
            } else {
                particleOptions = new ItemParticleOption(ParticleTypes.ITEM, getItem());
            }

            for (int i = 0; i < 25; ++i) {
                serverLevel.sendParticles(particleOptions,
                        this.getX(), this.getY(), this.getZ(),
                        1,
                        this.random.nextGaussian() * 0.5,
                        this.random.nextGaussian() * 0.5,
                        this.random.nextGaussian() * 0.5,
                        0.04);
            }
        }
    }
}
