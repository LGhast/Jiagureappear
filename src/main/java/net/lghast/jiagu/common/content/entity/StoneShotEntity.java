package net.lghast.jiagu.common.content.entity;

import net.lghast.jiagu.register.content.ModEntityTypes;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
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

public class StoneShotEntity extends ThrowableItemProjectile {
    private final float explosion_radius;
    private final float damage;

    public StoneShotEntity(EntityType<? extends StoneShotEntity> type, Level level) {
        super(type, level);
        this.explosion_radius = 1.5f;
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
            if(destroyTime<0){
                return damage * 15;
            }
            return damage * (float) Math.log(1+destroyTime);
        }
        return damage;
    }

    @Override
    protected Item getDefaultItem() {
        return Blocks.STONE.asItem();
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);

        if (!this.level().isClientSide) {
            this.level().explode(this, this.getX(), this.getY(), this.getZ(),
                    getRadius(), false, Level.ExplosionInteraction.NONE);

            AABB area = new AABB(
                    this.getX() - getRadius(), this.getY() - getRadius(), this.getZ() - getRadius(),
                    this.getX() + getRadius(), this.getY() + getRadius(), this.getZ() + getRadius()
            );

            for (LivingEntity entity : this.level().getEntitiesOfClass(LivingEntity.class, area)) {
                if (entity != this.getOwner()) {
                    entity.hurt(this.damageSources().thrown(this, this.getOwner()), getDamage());
                }
            }

            spawnParticles();

            this.discard();
        }
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
