package net.lghast.jiagu.common.block.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.lghast.jiagu.common.block.entity.WenchangAltarBlockEntity;
import net.lghast.jiagu.config.ClientConfig;
import net.lghast.jiagu.config.ServerConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class WenchangAltarRenderer implements BlockEntityRenderer<WenchangAltarBlockEntity> {
    public WenchangAltarRenderer(BlockEntityRendererProvider.Context context) {}

    private static float getRotationSpeed(){
        return ClientConfig.WENCHANG_ALTAR_ROTATION_SPEED.get().floatValue();
    }

    @Override
    public void render(WenchangAltarBlockEntity blockEntity, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {

        ItemStack item = blockEntity.getItem();
        if (item.isEmpty()) return;

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        Level level = blockEntity.getLevel();

        if (level == null) return;

        float gameTime = (level.getGameTime() + partialTick) / 20.0f;
        float rotation = gameTime * getRotationSpeed() * 360.0f;

        poseStack.pushPose();

        poseStack.translate(0.5, 1, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(rotation));
        poseStack.scale(0.8f, 0.8f, 0.8f);

        itemRenderer.renderStatic(
                item,
                ItemDisplayContext.GROUND,
                packedLight,
                OverlayTexture.NO_OVERLAY,
                poseStack,
                bufferSource,
                level,
                (int) blockEntity.getBlockPos().asLong()
        );

        poseStack.popPose();
    }
}
