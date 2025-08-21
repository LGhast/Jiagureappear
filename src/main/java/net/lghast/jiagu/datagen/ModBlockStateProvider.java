package net.lghast.jiagu.datagen;

import net.lghast.jiagu.JiaguReappear;
import net.lghast.jiagu.register.ModBlocks;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper){
        super(output, JiaguReappear.MOD_ID,exFileHelper);
    }
    @Override
    protected void registerStatesAndModels() {
        blockWithItem(ModBlocks.GOLDEN_BRICKS);
        blockWithItem(ModBlocks.LUCKY_JIAGU_BLOCK);
        blockWithItem(ModBlocks.LUCKY_JIAGU_BLOCK_IRON);
        blockWithItem(ModBlocks.LUCKY_JIAGU_BLOCK_COPPER);
        blockWithItem(ModBlocks.LUCKY_JIAGU_BLOCK_GOLD);
        blockWithItem(ModBlocks.LUCKY_JIAGU_BLOCK_DIAMOND);
        blockItem(ModBlocks.RUBBING_TABLE);
    }

    private void blockWithItem(DeferredBlock<?> deferredBlock){
        simpleBlockWithItem(deferredBlock.get(), cubeAll(deferredBlock.get()));
    }

    private void blockItem(DeferredBlock<?> deferredBlock){
        simpleBlockItem(deferredBlock.get(), new ModelFile.UncheckedModelFile("jiagureappear:block/"+deferredBlock.getId().getPath()));
    }

    private void blockItem(DeferredBlock<?> deferredBlock,String appendix){
        simpleBlockItem(deferredBlock.get(), new ModelFile.UncheckedModelFile("jiagureappear:block/"+deferredBlock.getId().getPath()+appendix));
    }
}
