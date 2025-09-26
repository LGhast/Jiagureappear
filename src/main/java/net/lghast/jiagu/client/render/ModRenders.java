package net.lghast.jiagu.client.render;

import net.lghast.jiagu.client.render.block.EruditeWenchangAltarRenderer;
import net.lghast.jiagu.client.render.block.WenchangAltarRenderer;
import net.lghast.jiagu.client.render.entity.StoneShotRenderer;
import net.lghast.jiagu.register.content.ModBlockEntities;
import net.lghast.jiagu.register.content.ModBlocks;
import net.lghast.jiagu.register.content.ModEntityTypes;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;

public class ModRenders {
    private static void registerEntityRenders(){
        BlockEntityRenderers.register(
                ModBlockEntities.WENCHANG_ALTAR.get(),
                WenchangAltarRenderer::new
        );

        BlockEntityRenderers.register(
                ModBlockEntities.ERUDITE_WENCHANG_ALTAR.get(),
                EruditeWenchangAltarRenderer::new
        );

        EntityRenderers.register(
                ModEntityTypes.PARASITE_SPORE.get(),
                ThrownItemRenderer::new
        );

        EntityRenderers.register(
                ModEntityTypes.STONE_SHOT.get(),
                StoneShotRenderer::new
        );
    }

    private static void setItemBlockRenders(){
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.CHARACTER_DISASSEMBLER.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.WENCHANG_ALTAR.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.ERUDITE_WENCHANG_ALTAR.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.YAOWANG_GOURD.get(), RenderType.cutout());
    }

    public static void register(){
        registerEntityRenders();
        setItemBlockRenders();
    }
}
