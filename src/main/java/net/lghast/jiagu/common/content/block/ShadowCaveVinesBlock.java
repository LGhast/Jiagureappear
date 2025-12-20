package net.lghast.jiagu.common.content.block;

import net.lghast.jiagu.register.content.ModBlocks;
import net.lghast.jiagu.register.content.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CaveVinesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ShadowCaveVinesBlock extends CaveVinesBlock {
    public ShadowCaveVinesBlock(Properties p_152959_) {
        super(p_152959_);
    }

    @Override
    public @NotNull ItemStack getCloneItemStack(LevelReader p_304444_, BlockPos p_153008_, BlockState p_153009_) {
        return new ItemStack(ModItems.SHADOW_BERRIES.get());
    }

    @Override
    protected @NotNull Block getBodyBlock() {
        return ModBlocks.SHADOW_CAVE_VINES_PLANT.get();
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(BlockState p_153021_, Level p_153022_, BlockPos p_153023_, Player p_153024_, BlockHitResult p_153026_) {
        return ShadowCaveVinesBlock.use(p_153024_, p_153021_, p_153022_, p_153023_);
    }

    protected static InteractionResult use(@Nullable Entity entity, BlockState state, Level level, BlockPos pos) {
        if (state.getValue(BlockStateProperties.BERRIES)) {
            Block.popResource(level, pos, new ItemStack(ModItems.SHADOW_BERRIES.get(), 1));
            float f = Mth.randomBetween(level.random, 0.8F, 1.2F);
            level.playSound(null, pos, SoundEvents.CAVE_VINES_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, f);

            BlockState blockstate = state.setValue(BlockStateProperties.BERRIES, Boolean.FALSE);
            level.setBlock(pos, blockstate, 2);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(entity, blockstate));
            return InteractionResult.sidedSuccess(level.isClientSide);
        } else {
            return InteractionResult.PASS;
        }
    }
}
