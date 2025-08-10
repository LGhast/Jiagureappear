package net.lghast.jiagu.register;

import net.lghast.jiagu.block.renderer.EruditeWenchangAltarRenderer;
import net.lghast.jiagu.block.renderer.WenchangAltarRenderer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

public class ModBlockEntityRenders {
    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(
                ModBlockEntities.WENCHANG_ALTAR.get(),
                WenchangAltarRenderer::new
        );

        event.registerBlockEntityRenderer(
                ModBlockEntities.ERUDITE_WENCHANG_ALTAR.get(),
                EruditeWenchangAltarRenderer::new
        );
    }
}
