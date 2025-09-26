package net.lghast.jiagu.common.content.block;

import net.lghast.jiagu.register.content.ModBlocks;
import net.lghast.jiagu.register.content.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.CaveVinesPlantBlock;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class ShadowCaveVinesPlantBlock extends CaveVinesPlantBlock {
    public ShadowCaveVinesPlantBlock(Properties p_153000_) {
        super(p_153000_);
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader p_304444_, BlockPos p_153008_, BlockState p_153009_) {
        return new ItemStack(ModItems.SHADOW_BERRIES.get());
    }

    @Override
    protected GrowingPlantHeadBlock getHeadBlock() {
        return (GrowingPlantHeadBlock) ModBlocks.SHADOW_CAVE_VINES.get();
    }


    @Override
    protected InteractionResult useWithoutItem(BlockState p_153021_, Level p_153022_, BlockPos p_153023_, Player p_153024_, BlockHitResult p_153026_) {
        return ShadowCaveVinesBlock.use(p_153024_, p_153021_, p_153022_, p_153023_);
    }
}
