package com.KC.correctdamaged.client.render;

import com.KC.correctdamaged.capability.LimbManager;
import com.KC.correctdamaged.client.ClientSetup;
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

public class PlayerMusclesLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    private final PlayerMusclesModel musclesModel;

    public PlayerMusclesLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent, EntityModelSet modelSet, boolean isSlim) {
        super(parent);
        ModelLayerLocation layerLoc = isSlim ? ClientSetup.PLAYER_MUSCLES_SLIM_LAYER : ClientSetup.PLAYER_MUSCLES_LAYER;
        this.musclesModel = new PlayerMusclesModel(modelSet.bakeLayer(layerLoc));
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        int muscleRightArm = LimbManager.getMuscleRightArm(player);
        int muscleLeftArm = LimbManager.getMuscleLeftArm(player);
        int muscleRightLeg = LimbManager.getMuscleRightLeg(player);
        int muscleLeftLeg = LimbManager.getMuscleLeftLeg(player);

        if (muscleRightArm == 0 && muscleLeftArm == 0 && muscleRightLeg == 0 && muscleLeftLeg == 0) return;

        PlayerModel<AbstractClientPlayer> parentModel = this.getParentModel();
        this.musclesModel.setupAnim(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        renderLimb(poseStack, buffer, packedLight, muscleRightLeg, musclesModel.rightThighMuscle, musclesModel.rightCalfMuscle, musclesModel.rightFootMuscle, parentModel.rightLeg);
        renderLimb(poseStack, buffer, packedLight, muscleLeftLeg, musclesModel.leftThighMuscle, musclesModel.leftCalfMuscle, musclesModel.leftFootMuscle, parentModel.leftLeg);
        renderLimb(poseStack, buffer, packedLight, muscleRightArm, musclesModel.rightArmShoulderMuscle, musclesModel.rightArmForearmMuscle, musclesModel.rightArmWristMuscle, parentModel.rightArm);
        renderLimb(poseStack, buffer, packedLight, muscleLeftArm, musclesModel.leftArmShoulderMuscle, musclesModel.leftArmForearmMuscle, musclesModel.leftArmWristMuscle, parentModel.leftArm);
    }

    private void renderLimb(
            PoseStack poseStack, MultiBufferSource buffer, int packedLight, int state,
            ModelPart innerPart, ModelPart midPart, ModelPart outerPart,
            ModelPart parentBodyPart
    ) {
        if (state <= 0) return;

        ResourceLocation muscleTex = PlayerMusclesModel.MUSCLE;

        poseStack.pushPose();
        parentBodyPart.translateAndRotate(poseStack);

        if (state >= 1) {
            VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(muscleTex));
            innerPart.render(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 1f);
        }

        if (state >= 2) {
            VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(muscleTex));
            midPart.render(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 1f);
        }

        if (state >= 3) {
            VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(muscleTex));
            outerPart.render(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 1f);
        }

        poseStack.popPose();
    }
}