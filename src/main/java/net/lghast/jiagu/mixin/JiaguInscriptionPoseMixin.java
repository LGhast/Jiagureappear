package net.lghast.jiagu.mixin;

import net.lghast.jiagu.common.content.item.BiaoGaleAmethystItem;
import net.lghast.jiagu.common.content.item.LeiStonesAmethystItem;
import net.lghast.jiagu.common.content.item.PrescriptionUserItem;
import net.lghast.jiagu.common.content.item.YiConflagrantAmethystItem;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public abstract class JiaguInscriptionPoseMixin {
    @Final
    @Shadow
    public ModelPart head;

    @Final
    @Shadow
    public ModelPart rightArm;

    @Final
    @Shadow
    public ModelPart leftArm;

    @Inject(method = "setupAnim*", at = @At("TAIL"))
    private void jiagureappear$applyInscriptionPose(LivingEntity entity, float limbSwing, float limbSwingAmount,
                                                    float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        if (!(entity instanceof AbstractClientPlayer player)) {
            return;
        }
        if (player.getUseItemRemainingTicks() <= 0 || !jiagureappear$isChargeableInscription(player.getUseItem().getItem())) {
            return;
        }

        ModelPart arm = player.getMainArm() == HumanoidArm.LEFT ? this.leftArm : this.rightArm;
        arm.xRot = this.head.xRot - (float) (Math.PI / 2.0);
        arm.yRot = this.head.yRot;
        arm.zRot = 0.0F;
    }

    @Unique
    private static boolean jiagureappear$isChargeableInscription(Item item) {
        return item instanceof PrescriptionUserItem
                || item instanceof YiConflagrantAmethystItem
                || item instanceof LeiStonesAmethystItem
                || item instanceof BiaoGaleAmethystItem;
    }
}
