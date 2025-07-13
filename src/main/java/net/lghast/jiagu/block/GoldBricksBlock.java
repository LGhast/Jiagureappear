package net.lghast.jiagu.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class GoldBricksBlock extends Block {

    private static final int EFFECT_DURATION = 100;
    private static final int CHECK_INTERVAL = 20;

    public GoldBricksBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void stepOn(Level pLevel, BlockPos pPos, BlockState pState, Entity pEntity) {
        super.stepOn(pLevel, pPos, pState, pEntity);
        if(pLevel.isClientSide()) return;
        if(pEntity instanceof Piglin || pEntity instanceof PiglinBrute){
            ((AbstractPiglin) pEntity).addEffect(new MobEffectInstance(
                    MobEffects.REGENERATION, EFFECT_DURATION, 1, false, true, true
            ));
        }
    }

    @Override
    protected void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pMovedByPiston) {
        super.onPlace(pState, pLevel, pPos, pOldState, pMovedByPiston);
        if (!pLevel.isClientSide) {
            pLevel.scheduleTick(pPos, this, CHECK_INTERVAL);
        }
    }

    @Override
    protected void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        super.tick(pState, pLevel, pPos, pRandom);
        if (pLevel.isClientSide) return;
        List<Entity> entities = pLevel.getEntities(null, new AABB(pPos));
        for (Entity entity : entities) {
            if ((entity instanceof Piglin || entity instanceof PiglinBrute) &&
                    entity.getBlockPosBelowThatAffectsMyMovement().equals(pPos)) {

                if (((AbstractPiglin) entity).getEffect(MobEffects.REGENERATION) == null) {
                    ((AbstractPiglin) entity).addEffect(new MobEffectInstance(
                            MobEffects.REGENERATION, EFFECT_DURATION, 1, false, true, true
                    ));
                }
            }
        }
        pLevel.scheduleTick(pPos, this, CHECK_INTERVAL);
    }
}
