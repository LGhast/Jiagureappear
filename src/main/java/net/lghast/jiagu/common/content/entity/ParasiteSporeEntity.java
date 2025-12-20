package net.lghast.jiagu.common.content.entity;

import net.lghast.jiagu.register.content.ModEntityTypes;
import net.lghast.jiagu.register.content.ModItems;
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
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
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
            ModUtils.spawnParticles(serverLevel, ParticleTypes.WITCH,
                    getX(), getY(), getZ(), 0.2, 0.2, 0.2, 1, 0.01);
        }
    }

    @Override
    protected @NotNull Item getDefaultItem() {
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
                ModUtils.spawnParticles(serverLevel, ParticleTypes.HAPPY_VILLAGER,
                        getX(), getY(), getZ(), 0.5, 0.5, 0.5, 11, 0.01);
            }else{
                ModUtils.spawnParticles(serverLevel, ParticleTypes.WITCH,
                        getX(), getY(), getZ(), 0.5, 0.5, 0.5, 6, 0.01);
                ModUtils.spawnParticles(serverLevel, ParticleTypes.SCULK_SOUL,
                        getX(), getY(), getZ(), 0.5, 0.5, 0.5, 5, 0.01);
            }
        }
    }
}
