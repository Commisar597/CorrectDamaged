package com.KC.correctdamaged.client.render;

import com.KC.correctdamaged.client.ClientSetup;
import com.KC.correctdamaged.capability.LimbManager;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;

public class PlayerBonesLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    private final PlayerBonesModel bonesModel;

    public PlayerBonesLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent, EntityModelSet modelSet, boolean isSlim) {
        super(parent);
        ModelLayerLocation layerLoc = isSlim ? ClientSetup.PLAYER_BONES_SLIM_LAYER : ClientSetup.PLAYER_BONES_LAYER;
        this.bonesModel = new PlayerBonesModel(modelSet.bakeLayer(layerLoc));
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        int boneRightArm = LimbManager.getBoneRightArm(player);
        int boneLeftArm = LimbManager.getBoneLeftArm(player);
        int boneRightLeg = LimbManager.getBoneRightLeg(player);
        int boneLeftLeg = LimbManager.getBoneLeftLeg(player);

        if (boneRightArm == 0 && boneLeftArm == 0 && boneRightLeg == 0 && boneLeftLeg == 0) return;

        PlayerModel<AbstractClientPlayer> parentModel = this.getParentModel();
        this.bonesModel.setupAnim(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        renderLimb(poseStack, buffer, packedLight, boneRightLeg, bonesModel.rightThighBone, bonesModel.rightCalfBone, bonesModel.rightFootBone, parentModel.rightLeg);
        renderLimb(poseStack, buffer, packedLight, boneLeftLeg, bonesModel.leftThighBone, bonesModel.leftCalfBone, bonesModel.leftFootBone, parentModel.leftLeg);
        renderLimb(poseStack, buffer, packedLight, boneRightArm, bonesModel.rightArmShoulderBone, bonesModel.rightArmForearmBone, bonesModel.rightArmWristBone, parentModel.rightArm);
        renderLimb(poseStack, buffer, packedLight, boneLeftArm, bonesModel.leftArmShoulderBone, bonesModel.leftArmForearmBone, bonesModel.leftArmWristBone, parentModel.leftArm);
    }

    private void renderLimb(
            PoseStack poseStack, MultiBufferSource buffer, int packedLight, int state,
            ModelPart innerPart, ModelPart midPart, ModelPart outerPart,
            ModelPart parentBodyPart
    ) {
        if (state <= 0) return;

        boolean burnt = state > 3;
        int level = burnt ? state - 3 : state;

        ResourceLocation shoulderTex = burnt ? PlayerBonesModel.BURNT_BONE : PlayerBonesModel.BONE;
        ResourceLocation forearmTex = burnt ? PlayerBonesModel.BURNT_BONE : PlayerBonesModel.BONE;
        ResourceLocation wristTex = burnt ? PlayerBonesModel.BURNT_BONE : PlayerBonesModel.BONE;

        poseStack.pushPose();
        parentBodyPart.translateAndRotate(poseStack);

        if (level >= 1) {
            VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(shoulderTex));
            innerPart.render(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
        }

        if (level >= 2) {
            VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(forearmTex));
            midPart.render(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
        }

        if (level >= 3) {
            VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(wristTex));
            outerPart.render(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
        }

        poseStack.popPose();
    }
}