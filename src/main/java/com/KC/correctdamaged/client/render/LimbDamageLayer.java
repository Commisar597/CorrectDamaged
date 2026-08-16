package com.KC.correctdamaged.client.render;

import com.KC.correctdamaged.capability.LimbManager;
import com.KC.correctdamaged.client.render.customRender.CustomCube;
import com.KC.correctdamaged.client.render.customRender.FreeUVCubeRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class LimbDamageLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    public LimbDamageLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent) {
        super(parent);
    }

    @Override
    public void render(
            PoseStack poseStack, MultiBufferSource buffer, int packedLight,
            AbstractClientPlayer player, float limbSwing, float limbSwingAmount,
            float partialTicks, float ageInTicks, float netHeadYaw, float headPitch
    ) {
        LimbManager.get(player).ifPresent(data -> {
            boolean slim = player.getModelName().equals("slim");
            ResourceLocation texture = player.getSkinTextureLocation();
            VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(texture));

            PlayerModel<AbstractClientPlayer> model = getParentModel();

            renderLimb(poseStack, consumer, packedLight, model.rightArm, LimbDamageVariants.LimbType.RIGHT_ARM, LimbDamageVariants.LimbType.RIGHT_SLEEVE, data.getRightArm(), slim);
            renderLimb(poseStack, consumer, packedLight, model.leftArm, LimbDamageVariants.LimbType.LEFT_ARM, LimbDamageVariants.LimbType.LEFT_SLEEVE, data.getLeftArm(), slim);

            renderLimb(poseStack, consumer, packedLight, model.rightLeg, LimbDamageVariants.LimbType.RIGHT_LEG, LimbDamageVariants.LimbType.RIGHT_PANTS, data.getRightLeg(), false);
            renderLimb(poseStack, consumer, packedLight, model.leftLeg, LimbDamageVariants.LimbType.LEFT_LEG, LimbDamageVariants.LimbType.LEFT_PANTS, data.getLeftLeg(), false);
        });
    }

    private void renderLimb(
            PoseStack poseStack, VertexConsumer consumer, int packedLight,
            ModelPart parentPart, LimbDamageVariants.LimbType baseType, LimbDamageVariants.LimbType layerType,
            int state, boolean slim
    ) {
        if (state <= 0 || state >= 3) return;

        float height = state == 2 ? 10.0F : 6.0F;

        poseStack.pushPose();
        parentPart.translateAndRotate(poseStack);

        CustomCube baseCube = LimbDamageVariants.createLimbStageCube(baseType, slim, height);
        drawCustomCube(poseStack, consumer, packedLight, baseCube);

        CustomCube layerCube = LimbDamageVariants.createLimbStageCube(layerType, slim, height);
        drawCustomCube(poseStack, consumer, packedLight, layerCube);

        poseStack.popPose();
    }

    private void drawCustomCube(PoseStack poseStack, VertexConsumer consumer, int packedLight, CustomCube cube) {
        float def = cube.deformation();

        float x0 = cube.x() - def;
        float y0 = cube.y() - def;
        float z0 = cube.z() - def;

        float x1 = cube.x() + cube.width() + def;
        float y1 = cube.y() + cube.height() + def;
        float z1 = cube.z() + cube.depth() + def;

        FreeUVCubeRenderer.renderBox(
                poseStack.last(), consumer,
                x0, y0, z0,
                x1, y1, z1,
                64, 64,
                packedLight, OverlayTexture.NO_OVERLAY,
                1.0F, 1.0F, 1.0F, 1.0F,
                cube.uv()
        );
    }
}