package net.lghast.jiagu.block.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.lghast.jiagu.block.EruditeWenchangAltarBlock;
import net.lghast.jiagu.block.entity.EruditeWenchangAltarBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class EruditeWenchangAltarRenderer implements BlockEntityRenderer<EruditeWenchangAltarBlockEntity> {
    private static final float ROTATION_SPEED = 0.12f;
    private static final float[] ITEM_POSITIONS = {1.0f, 1.5f, 2.0f, 2.5f};

    public EruditeWenchangAltarRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(EruditeWenchangAltarBlockEntity blockEntity, float partialTick, @NotNull PoseStack poseStack,
                       @NotNull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if (blockEntity.isRemoved()) return;

        BlockState state = blockEntity.getBlockState();
        int itemCount = state.getValue(EruditeWenchangAltarBlock.ITEM_COUNT);

        if (itemCount == 0) return;

        ItemStack[] items = blockEntity.getItems();
        if (items == null) return;

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        Level level = blockEntity.getLevel();
        if (level == null) return;

        float gameTime = (level.getGameTime() + partialTick) / 20.0f;
        float rotation = gameTime * ROTATION_SPEED * 360.0f;

        for(int i = 0; i<itemCount; i++) {
            if(items[i].isEmpty()){
                continue;
            }
            poseStack.pushPose();
            poseStack.translate(0.5, ITEM_POSITIONS[i], 0.5);
            poseStack.mulPose(Axis.YP.rotationDegrees(rotation));
            poseStack.scale(0.8f, 0.8f, 0.8f);

            itemRenderer.renderStatic(
                    items[i],
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
}