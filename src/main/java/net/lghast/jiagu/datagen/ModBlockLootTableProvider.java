package net.lghast.jiagu.datagen;

import net.lghast.jiagu.register.ModBlocks;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.neoforged.fml.common.Mod;

import java.util.Set;

public class ModBlockLootTableProvider extends BlockLootSubProvider {
    protected  ModBlockLootTableProvider(HolderLookup.Provider registries){
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(),registries);
    }
    @Override
    protected void generate() {
        dropSelf(ModBlocks.GOLDEN_BRICKS.get());
        dropSelf(ModBlocks.CHARACTER_DISASSEMBLER.get());
        dropSelf(ModBlocks.CANGJIE_DING_TRIPOD.get());
        dropSelf(ModBlocks.RUBBING_TABLE.get());
        dropSelf(ModBlocks.YOLIME_BLOCK.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks(){
        return ModBlocks.BLOCKS.getEntries().stream()
                .map(Holder::value)
                .filter(block -> !shouldSkipBlock(block)) // 自定义过滤逻辑
                ::iterator;
    }

    private boolean shouldSkipBlock(Block block) {
        return block == ModBlocks.SOUR_BERRY_BUSH.get() ||
                block == ModBlocks.SHADOW_CAVE_VINES_PLANT.get() ||
                block == ModBlocks.SHADOW_CAVE_VINES.get();
    }
}
