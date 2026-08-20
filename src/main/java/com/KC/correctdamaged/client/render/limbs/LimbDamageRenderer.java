package com.KC.correctdamaged.client.render.limbs;

import com.KC.correctdamaged.capability.visual.ArmData;
import com.KC.correctdamaged.capability.visual.LegData;
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
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class LimbDamageRenderer {

    public static void render(
            PoseStack poseStack, MultiBufferSource buffer, int packedLight,
            AbstractClientPlayer player, PlayerModel<AbstractClientPlayer> model
    ) {
        player.getCapability(LimbManager.LIMB_DATA_CAP).ifPresent(data -> {
            boolean slim = player.getModelName().equals("slim");
            ResourceLocation texture = player.getSkinTextureLocation();
            VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(texture));

            renderArm(poseStack, consumer, packedLight, model.rightArm, LimbDamageVariants.LimbType.RIGHT_ARM,
                    LimbDamageVariants.LimbType.RIGHT_SLEEVE, data.getRightArm(), slim);

            renderArm(poseStack, consumer, packedLight, model.leftArm, LimbDamageVariants.LimbType.LEFT_ARM,
                    LimbDamageVariants.LimbType.LEFT_SLEEVE, data.getLeftArm(), slim);

            renderLeg(poseStack, consumer, packedLight, model.rightLeg, LimbDamageVariants.LimbType.RIGHT_LEG,
                    LimbDamageVariants.LimbType.RIGHT_PANTS, data.getRightLeg());

            renderLeg(poseStack, consumer, packedLight, model.leftLeg, LimbDamageVariants.LimbType.LEFT_LEG,
                    LimbDamageVariants.LimbType.LEFT_PANTS, data.getLeftLeg());
        });
    }

    private static void renderArm(
            PoseStack poseStack, VertexConsumer consumer, int packedLight,
            ModelPart parentPart, LimbDamageVariants.LimbType baseType, LimbDamageVariants.LimbType layerType,
            ArmData arm, boolean slim
    ) {
        if (arm.hasShoulderSkin() && arm.hasForearmSkin() && arm.hasWristSkin()) {
            return;
        }

        poseStack.pushPose();
        parentPart.translateAndRotate(poseStack);

        if (arm.hasShoulderSkin()) {
            renderSegment(poseStack, consumer, packedLight, baseType, layerType, slim, 6.0F, -2.0F);
        }
        if (arm.hasForearmSkin()) {
            renderSegment(poseStack, consumer, packedLight, baseType, layerType, slim, 4.0F, 4.0F);
        }
        if (arm.hasWristSkin()) {
            renderSegment(poseStack, consumer, packedLight, baseType, layerType, slim, 2.0F, 8.0F);
        }

        poseStack.popPose();
    }

    private static void renderLeg(
            PoseStack poseStack, VertexConsumer consumer, int packedLight,
            ModelPart parentPart, LimbDamageVariants.LimbType baseType, LimbDamageVariants.LimbType layerType,
            LegData leg
    ) {
        if (leg.hasThighSkin() && leg.hasCalfSkin() && leg.hasFootSkin()) {
            return;
        }

        poseStack.pushPose();
        parentPart.translateAndRotate(poseStack);

        if (leg.hasThighSkin()) {
            renderSegment(poseStack, consumer, packedLight, baseType, layerType,
                    false, 6.0F, 0.0F);
        }

        if (leg.hasCalfSkin()) {
            renderSegment(poseStack, consumer, packedLight, baseType, layerType,
                    false, 4.0F, 6.0F);
        }

        if (leg.hasFootSkin()) {
            renderSegment(poseStack, consumer, packedLight, baseType, layerType,
                    false, 2.0F, 10.0F);
        }

        poseStack.popPose();
    }

    private static void renderSegment(
            PoseStack poseStack, VertexConsumer consumer, int packedLight,
            LimbDamageVariants.LimbType baseType, LimbDamageVariants.LimbType layerType,
            boolean slim, float height, float yOffset
    ) {
        CustomCube baseCube = LimbDamageVariants.createLimbSegmentCube(baseType, slim, height, yOffset);
        drawCustomCube(poseStack, consumer, packedLight, baseCube);

        CustomCube layerCube = LimbDamageVariants.createLimbSegmentCube(layerType, slim, height, yOffset);
        drawCustomCube(poseStack, consumer, packedLight, layerCube);
    }

    private static void drawCustomCube(PoseStack poseStack, VertexConsumer consumer, int packedLight, CustomCube cube) {
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