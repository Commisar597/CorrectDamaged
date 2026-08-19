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
import net.minecraft.resources.ResourceLocation;

/**
 * Слой рендеринга костей игрока.
 * Зачем нужен: Выполняет рендеринг глубокого скелетного слоя при травмах или сгорании кожи/мышц.
 * Поддерживает динамическое переключение между нормальной костью и обгоревшей (isBurntBone).
 */
public class PlayerBonesLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    private final PlayerBonesModel bonesModel;

    public PlayerBonesLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent,
                            EntityModelSet modelSet, boolean isSlim) {
        super(parent);
        ModelLayerLocation layerLoc = isSlim ? ClientEvents.PLAYER_BONES_SLIM_LAYER : ClientEvents.PLAYER_BONES_LAYER;
        this.bonesModel = new PlayerBonesModel(modelSet.bakeLayer(layerLoc));
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight,
                       AbstractClientPlayer player, float limbSwing, float limbSwingAmount,
                       float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        player.getCapability(LimbManager.LIMB_DATA_CAP).ifPresent(data -> {
            PlayerModel<AbstractClientPlayer> parentModel = getParentModel();
            bonesModel.setupAnim(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

            renderArmBones(poseStack, buffer, packedLight, data.getRightArm(), bonesModel.rightArmShoulderBone,
                    bonesModel.rightArmForearmBone, bonesModel.rightArmWristBone, parentModel.rightArm);

            renderArmBones(poseStack, buffer, packedLight, data.getLeftArm(), bonesModel.leftArmShoulderBone,
                    bonesModel.leftArmForearmBone, bonesModel.leftArmWristBone, parentModel.leftArm);

            renderLegBones(poseStack, buffer, packedLight, data.getRightLeg(), bonesModel.rightThighBone,
                    bonesModel.rightCalfBone, bonesModel.rightFootBone, parentModel.rightLeg);

            renderLegBones(poseStack, buffer, packedLight, data.getLeftLeg(), bonesModel.leftThighBone,
                    bonesModel.leftCalfBone, bonesModel.leftFootBone, parentModel.leftLeg);
        });
    }

    /**
     * Отрисовывает кости руки, проверяя флаг обугленности для выбора нужного файла текстуры.
     */
    private void renderArmBones(
            PoseStack poseStack, MultiBufferSource buffer, int packedLight, ArmData arm,
            ModelPart shoulderPart, ModelPart forearmPart, ModelPart wristPart, ModelPart parentPart
    ) {
        int level = arm.getBoneState();
        if (level <= 0) return;

        ResourceLocation tex = arm.isBurntBone() ? PlayerBonesModel.BURNT_BONE : PlayerBonesModel.BONE;

        poseStack.pushPose();
        parentPart.translateAndRotate(poseStack);

        if (level >= 1) renderPart(poseStack, buffer, packedLight, shoulderPart, tex);
        if (level >= 2) renderPart(poseStack, buffer, packedLight, forearmPart, tex);
        if (level >= 3) renderPart(poseStack, buffer, packedLight, wristPart, tex);

        poseStack.popPose();
    }

    /**
     * Отрисовывает кости ноги, проверяя состояние флага isBurntBone.
     */
    private void renderLegBones(
            PoseStack poseStack, MultiBufferSource buffer, int packedLight, LegData leg,
            ModelPart thighPart, ModelPart calfPart, ModelPart footPart, ModelPart parentPart
    ) {
        int level = leg.getBoneState();
        if (level <= 0) return;

        ResourceLocation tex = leg.isBurntBone() ? PlayerBonesModel.BURNT_BONE : PlayerBonesModel.BONE;

        poseStack.pushPose();
        parentPart.translateAndRotate(poseStack);

        if (level >= 1) renderPart(poseStack, buffer, packedLight, thighPart, tex);
        if (level >= 2) renderPart(poseStack, buffer, packedLight, calfPart, tex);
        if (level >= 3) renderPart(poseStack, buffer, packedLight, footPart, tex);

        poseStack.popPose();
    }

    private void renderPart(PoseStack poseStack, MultiBufferSource buffer, int packedLight,
                            ModelPart part, ResourceLocation tex) {
        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(tex));
        part.render(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0f,
                1.0f, 1.0f, 1.0f);
    }
}