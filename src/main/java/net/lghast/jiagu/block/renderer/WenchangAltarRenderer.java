package net.lghast.jiagu.block.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.lghast.jiagu.block.entity.WenchangAltarBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class WenchangAltarRenderer implements BlockEntityRenderer<WenchangAltarBlockEntity> {
    public WenchangAltarRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(WenchangAltarBlockEntity blockEntity, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {

        ItemStack item = blockEntity.getItem();
        if (item.isEmpty()) return;

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        poseStack.pushPose();

        poseStack.translate(0.5, 1 + blockEntity.bobOffset, 0.5);

        poseStack.mulPose(Axis.YP.rotationDegrees(blockEntity.rotation));

        poseStack.scale(0.8f, 0.8f, 0.8f);

        itemRenderer.renderStatic(
                item,
                ItemDisplayContext.GROUND,
                packedLight,
                OverlayTexture.NO_OVERLAY,
                poseStack,
                bufferSource,
                blockEntity.getLevel(),
                0
        );

        poseStack.popPose();
    }
}
