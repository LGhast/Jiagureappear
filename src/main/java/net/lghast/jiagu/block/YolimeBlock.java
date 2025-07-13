package net.lghast.jiagu.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SlimeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.concurrent.ThreadLocalRandom;

public class YolimeBlock extends SlimeBlock {
    public YolimeBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void updateEntityAfterFallOn(BlockGetter level, Entity entity) {
        if (entity.isSuppressingBounce()) {
            super.updateEntityAfterFallOn(level, entity);
        } else {
            this.bounceUp(entity);
        }
    }

    private void bounceUp(Entity entity) {
        Vec3 vec3 = entity.getDeltaMovement();
        if (vec3.y < 0.0) {
            double d0 = ThreadLocalRandom.current().nextDouble(0.6, 2.5);
            entity.setDeltaMovement(vec3.x, Math.min(-vec3.y * d0, 66.6), vec3.z);
        }
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        double d0 = Math.abs(entity.getDeltaMovement().y);
        if (d0 < 0.1 && !entity.isSteppingCarefully()) {
            double d1 = 1.4 + d0 * ThreadLocalRandom.current().nextDouble(0.5, 2.5);
            entity.setDeltaMovement(entity.getDeltaMovement().multiply(d1, 1.0, d1));
        }

        super.stepOn(level, pos, state, entity);
    }
}
