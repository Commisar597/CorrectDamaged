package com.KC.correctdamaged.client.render;

import com.KC.correctdamaged.capability.visual.ArmData;
import com.KC.correctdamaged.capability.visual.LegData;
import com.KC.correctdamaged.capability.LimbManager;
import com.KC.correctdamaged.event.ClientEvents;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;

public class PlayerMusclesLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    private final PlayerMusclesModel musclesModel;

    private static final float OFFSET_X = -0.0125F;

    public PlayerMusclesLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent, EntityModelSet modelSet, boolean isSlim) {
        super(parent);
        ModelLayerLocation layerLoc = isSlim ? ClientEvents.PLAYER_MUSCLES_SLIM_LAYER : ClientEvents.PLAYER_MUSCLES_LAYER;
        this.musclesModel = new PlayerMusclesModel(modelSet.bakeLayer(layerLoc));
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        player.getCapability(LimbManager.LIMB_DATA_CAP).ifPresent(data -> {
            PlayerModel<AbstractClientPlayer> parentModel = getParentModel();
            musclesModel.setupAnim(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

            renderArmMuscles(poseStack, buffer, packedLight, data.getRightArm(),
                    musclesModel.rightArmShoulderMuscle, musclesModel.rightArmForearmMuscle, musclesModel.rightArmWristMuscle,
                    parentModel.rightArm, OFFSET_X);

            renderArmMuscles(poseStack, buffer, packedLight, data.getLeftArm(),
                    musclesModel.leftArmShoulderMuscle, musclesModel.leftArmForearmMuscle, musclesModel.leftArmWristMuscle,
                    parentModel.leftArm, -OFFSET_X);

            renderLegMuscles(poseStack, buffer, packedLight, data.getRightLeg(), musclesModel.rightThighMuscle, musclesModel.rightCalfMuscle, musclesModel.rightFootMuscle, parentModel.rightLeg);
            renderLegMuscles(poseStack, buffer, packedLight, data.getLeftLeg(), musclesModel.leftThighMuscle, musclesModel.leftCalfMuscle, musclesModel.leftFootMuscle, parentModel.leftLeg);
        });
    }

    private void renderArmMuscles(
            PoseStack poseStack, MultiBufferSource buffer, int packedLight, ArmData arm,
            ModelPart shoulderPart, ModelPart forearmPart, ModelPart wristPart, ModelPart parentPart,
            float offsetX
    ) {
        int level = arm.getMuscleState();
        if (level <= 0) return;

        poseStack.pushPose();

        parentPart.translateAndRotate(poseStack);

        poseStack.translate(offsetX, 0.0D, 0.0D);

        if (level >= 1) renderPart(poseStack, buffer, packedLight, shoulderPart);
        if (level >= 2) renderPart(poseStack, buffer, packedLight, forearmPart);
        if (level >= 3) renderPart(poseStack, buffer, packedLight, wristPart);

        poseStack.popPose();
    }

    private void renderLegMuscles(
            PoseStack poseStack, MultiBufferSource buffer, int packedLight, LegData leg,
            ModelPart thighPart, ModelPart calfPart, ModelPart footPart, ModelPart parentPart
    ) {
        int level = leg.getMuscleState();
        if (level <= 0) return;

        poseStack.pushPose();
        parentPart.translateAndRotate(poseStack);

        if (level >= 1) renderPart(poseStack, buffer, packedLight, thighPart);
        if (level >= 2) renderPart(poseStack, buffer, packedLight, calfPart);
        if (level >= 3) renderPart(poseStack, buffer, packedLight, footPart);

        poseStack.popPose();
    }

    private void renderPart(PoseStack poseStack, MultiBufferSource buffer, int packedLight, ModelPart part) {
        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(PlayerMusclesModel.MUSCLE));
        part.render(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
    }
}