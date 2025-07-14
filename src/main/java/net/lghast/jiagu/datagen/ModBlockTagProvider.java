package net.lghast.jiagu.datagen;

import net.lghast.jiagu.JiaguReappear;
import net.lghast.jiagu.register.ModBlocks;
import net.lghast.jiagu.register.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {
    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper){
        super(output,lookupProvider, JiaguReappear.MOD_ID,existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        tag(BlockTags.MINEABLE_WITH_AXE)
                .add(ModBlocks.RUBBING_TABLE.get());

        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.GOLDEN_BRICKS.get())
                .add(ModBlocks.CANGJIE_DING_TRIPOD.get())
                .add(ModBlocks.CHARACTER_DISASSEMBLER.get())
                .add(ModBlocks.AUTO_DISASSEMBLER.get())
                .add(ModBlocks.CANGJIE_MORPHER.get())
        ;

        tag(BlockTags.NEEDS_IRON_TOOL)
                .add(ModBlocks.GOLDEN_BRICKS.get());

        tag(ModTags.JIAGU_LUCKY_BLOCKS)
                .add(ModBlocks.LUCKY_JIAGU_BLOCK.get())
                .add(ModBlocks.LUCKY_JIAGU_BLOCK_IRON.get())
        ;

        tag(BlockTags.GUARDED_BY_PIGLINS).add(ModBlocks.GOLDEN_BRICKS.get());
    }
}
