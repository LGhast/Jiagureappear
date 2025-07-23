package net.lghast.jiagu.entity;

import net.lghast.jiagu.register.ModEntityTypes;
import net.lghast.jiagu.register.ModItems;
import net.lghast.jiagu.utils.ModUtils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class ParasiteSporeEntity extends ThrowableItemProjectile {
    private MobEffectInstance effectInstance;

    public ParasiteSporeEntity(EntityType<? extends ParasiteSporeEntity> type, Level level) {
        super(type, level);
    }

    public ParasiteSporeEntity(Level level, LivingEntity shooter) {
        super(ModEntityTypes.PARASITE_SPORE.get(), shooter, level);
    }

    public void setEffect(MobEffectInstance effectInstance) {
        this.effectInstance = effectInstance;
    }

    @Override
    public void tick() {
        super.tick();
        Level level = level();
        if(level instanceof ServerLevel serverLevel){
            ModUtils.spawnParticlesForAll(serverLevel, ParticleTypes.WITCH,
                    getX(), getY(), getZ(), 0.2, 0.2, 0.2, 1, 0.01);
        }
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.PARASITE_SPORE.get();
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        super.onHitEntity(hitResult);
        Entity entity = hitResult.getEntity();
        if (effectInstance != null && entity instanceof LivingEntity livingEntity) {
            livingEntity.addEffect(new MobEffectInstance(effectInstance));
        }
        this.discard();
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if(level() instanceof ServerLevel serverLevel){
            if(effectInstance.getEffect().value().isBeneficial()){
                ModUtils.spawnParticlesForAll(serverLevel, ParticleTypes.HAPPY_VILLAGER,
                        getX(), getY(), getZ(), 0.5, 0.5, 0.5, 10, 0.01);
            }else{
                ModUtils.spawnParticlesForAll(serverLevel, ParticleTypes.WITCH,
                        getX(), getY(), getZ(), 0.5, 0.5, 0.5, 5, 0.01);
                ModUtils.spawnParticlesForAll(serverLevel, ParticleTypes.SCULK_SOUL,
                        getX(), getY(), getZ(), 0.5, 0.5, 0.5, 5, 0.01);
            }
        }
    }
}
