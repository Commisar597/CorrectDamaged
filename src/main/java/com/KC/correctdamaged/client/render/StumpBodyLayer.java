package com.KC.correctdamaged.client.render;

import com.KC.correctdamaged.CorrectDamaged;
import com.KC.correctdamaged.capability.LimbManager;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class StumpBodyLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    private static final ResourceLocation DAMAGED_LEFT_2x4 = tex("damaged_left_2x4.png");
    private static final ResourceLocation DAMAGED_RIGHT_2x4 = tex("damaged_right_2x4.png");

    private static final ResourceLocation DAMAGED_1x4_1_P = tex("damaged_1x4_1_p.png");
    private static final ResourceLocation DAMAGED_1x4_1_N = tex("damaged_1x4_1_n.png");

    private static final ResourceLocation HOLE_DAMAGE_3x4 = tex("hole_3x4.png");
    private static final ResourceLocation HOLE_4x4 = tex("hole_4x4.png");

    private static final ResourceLocation BODY_STUMP_12x4_LEFT = tex("body_stump_12x4_left.png");
    private static final ResourceLocation BODY_STUMP_12x4_RIGHT = tex("body_stump_12x4_right.png");

    private static ResourceLocation tex(String name) {
        return new ResourceLocation(CorrectDamaged.MODID, "textures/entity/" + name);
    }

    public StumpBodyLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent) {
        super(parent);
    }

    @Override
    public void render(
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            AbstractClientPlayer player,
            float limbSwing,
            float limbSwingAmount,
            float partialTicks,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {
        LimbManager.get(player).ifPresent(data -> {
            int bodyState = data.getBodyState();
            if (bodyState == 9 || bodyState == 0) return;

            poseStack.pushPose();
            getParentModel().body.translateAndRotate(poseStack);

            switch (bodyState) {
                case 1 -> renderCavity(poseStack, buffer, packedLight, 2F, 6F);
                case 2 -> renderCavity(poseStack, buffer, packedLight, 7F, 11F);

                case 3 -> renderNotch(poseStack, buffer, packedLight, true, 6F, 7F, 10F, 11F, false);
                case 4 -> renderNotch(poseStack, buffer, packedLight, false, 6F, 7F, 10F, 11F, false);

                case 5 -> renderNotch(poseStack, buffer, packedLight, false, 3F, 4F, 8F, 9F, true);
                case 6 -> renderNotch(poseStack, buffer, packedLight, true, 3F, 4F, 8F, 9F, true);

                case 7 -> renderBodyHalfStump(poseStack, buffer, packedLight, BODY_STUMP_12x4_RIGHT, true);
                case 8 -> renderBodyHalfStump(poseStack, buffer, packedLight, BODY_STUMP_12x4_LEFT, false);
            }

            poseStack.popPose();
        });
    }

    private void renderNotch(
            PoseStack poseStack, MultiBufferSource buffer, int packedLight, boolean isLeft,
            float y0, float y1, float y2, float y3, boolean isBig
    ) {
        float xSign = isLeft ? 1F : -1F;

        ResourceLocation tex = isLeft ? DAMAGED_RIGHT_2x4 : DAMAGED_LEFT_2x4;

        ResourceLocation tex_Vertical = DAMAGED_1x4_1_P;
        ResourceLocation tex_Ledge = DAMAGED_1x4_1_N;

        ResourceLocation tex4;
        int texH;
        if (isBig) {
            tex4 = HOLE_4x4;
            texH = 4;
        } else {
            tex4 = HOLE_DAMAGE_3x4;
            texH = 3;
        }

        renderLedge(poseStack, buffer.getBuffer(RenderType.entityCutoutNoCull(tex)), packedLight, xSign, 2F, 4F, y0, false, 2, 4);

        renderLedge(poseStack, buffer.getBuffer(RenderType.entityCutoutNoCull(tex)), packedLight, xSign, 2F, 4F, y3, true, 2, 4);

        renderNotchWall(poseStack, buffer.getBuffer(RenderType.entityCutoutNoCull(tex_Vertical)), packedLight, xSign * 2F, y0, y1, isLeft, 1, 4);

        renderLedge(poseStack, buffer.getBuffer(RenderType.entityCutoutNoCull(tex_Ledge)), packedLight, xSign, 1F, 2F, y1, false, 1, 4);

        renderLedge(poseStack, buffer.getBuffer(RenderType.entityCutoutNoCull(tex_Ledge)), packedLight, xSign, 1F, 2F, y2, true, 1, 4);

        renderNotchWall(poseStack, buffer.getBuffer(RenderType.entityCutoutNoCull(tex_Vertical)), packedLight, xSign * 2F, y2, y3, isLeft, 1, 4);

        renderNotchWall(poseStack, buffer.getBuffer(RenderType.entityCutoutNoCull(tex4)), packedLight, xSign * 1F, y1, y2, isLeft, 4, texH);
    }

    private void renderCavity(PoseStack poseStack, MultiBufferSource buffer, int packedLight, float yTop, float yBottom) {
        VertexConsumer buf = buffer.getBuffer(RenderType.entityCutoutNoCull(HOLE_4x4));
        float h = yBottom - yTop;
        float d = 4F;
        int texW = 4;
        int texH = 4;

        poseStack.pushPose();
        poseStack.translate(-2.0D / 16.0D, (yTop + yBottom) / 2.0D / 16.0D, 0.0D);
        renderHoleWall(poseStack, buf, packedLight, h, d, texW, texH);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.translate(2.0D / 16.0D, (yTop + yBottom) / 2.0D / 16.0D, 0.0D);
        renderHoleWall(poseStack, buf, packedLight, h, d, texW, texH);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.translate(0.0D, yTop / 16.0D, 0.0D);
        poseStack.mulPose(Axis.ZP.rotationDegrees(90.0F));
        renderHoleWall(poseStack, buf, packedLight, 4F, d, texW, texH);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.translate(0.0D, yBottom / 16.0D, 0.0D);
        poseStack.mulPose(Axis.ZP.rotationDegrees(90.0F));
        renderHoleWall(poseStack, buf, packedLight, 4F, d, texW, texH);
        poseStack.popPose();
    }

    private void renderHoleWall(
            PoseStack poseStack, VertexConsumer buf, int packedLight,
            float height, float depth, int realTexW, int realTexH
    ) {
        FreeUVCubeRenderer.FaceUV uv = FreeUVCubeRenderer.FaceUV.of(0F, 0F, realTexW, realTexH);
        CubeUV boxUV = new CubeUV(null, null, uv, uv, null, null);

        FreeUVCubeRenderer.renderBox(
                poseStack.last(), buf,
                -0.001F, -height / 2F, -depth / 2F,
                0.001F, height / 2F, depth / 2F,
                realTexW, realTexH, packedLight, OverlayTexture.NO_OVERLAY,
                1f, 1f, 1f, 1f, boxUV
        );
    }

    private void renderLedge(
            PoseStack poseStack, VertexConsumer buf, int packedLight,
            float xSign, float xInner, float xOuter, float y, boolean facingUp,
            int realTexW, int realTexH
    ) {
        float width = xOuter - xInner;
        float xCenter = xSign * (xInner + xOuter) / 2F;
        float depth = 4F;

        FreeUVCubeRenderer.FaceUV uv = FreeUVCubeRenderer.FaceUV.of(0F, 0F, realTexW, realTexH);
        CubeUV cubeUv = facingUp
                ? new CubeUV(null, null, null, null, uv, null)
                : new CubeUV(null, null, null, null, null, uv);

        poseStack.pushPose();
        poseStack.translate(xCenter / 16.0D, y / 16.0D, 0.0D);

        FreeUVCubeRenderer.renderBox(
                poseStack.last(), buf,
                -width / 2F, -0.001F, -depth / 2F,
                width / 2F, 0.001F, depth / 2F,
                realTexW, realTexH, packedLight, OverlayTexture.NO_OVERLAY,
                1f, 1f, 1f, 1f, cubeUv
        );

        poseStack.popPose();
    }

    private void renderNotchWall(
            PoseStack poseStack, VertexConsumer buf, int packedLight,
            float x, float yTop, float yBottom, boolean isLeft,
            int realTexW, int realTexH
    ) {
        float height = yBottom - yTop;
        float depth = 4F;

        FreeUVCubeRenderer.FaceUV uv = FreeUVCubeRenderer.FaceUV.of(0F, 0F, realTexW, realTexH);
        CubeUV cubeUv = isLeft
                ? new CubeUV(null, null, null, uv, null, null)
                : new CubeUV(null, null, uv, null, null, null);

        poseStack.pushPose();
        poseStack.translate(x / 16.0D, (yTop + yBottom) / 2.0D / 16.0D, 0.0D);

        FreeUVCubeRenderer.renderBox(
                poseStack.last(), buf,
                -0.001F, -height / 2F, -depth / 2F,
                0.001F, height / 2F, depth / 2F,
                realTexW, realTexH, packedLight, OverlayTexture.NO_OVERLAY,
                1f, 1f, 1f, 1f, cubeUv
        );

        poseStack.popPose();
    }

    private void renderBodyHalfStump(
            PoseStack poseStack, MultiBufferSource buffer, int packedLight,
            ResourceLocation texture, boolean isRight
    ) {
        VertexConsumer buf = buffer.getBuffer(RenderType.entityCutoutNoCull(texture));
        PoseStack.Pose pose = poseStack.last();

        FreeUVCubeRenderer.FaceUV uv = FreeUVCubeRenderer.FaceUV.of(0F, 0F, 4F, 12F);

        float xPos = isRight ? 2.0F : -2.0F;

        poseStack.pushPose();
        poseStack.translate(xPos / 16.0D, 0.0D, 0.0D);

        FreeUVCubeRenderer.renderBox(
                pose, buf,
                -0.001F, 0.0F, -2.0F,
                0.001F, 12.0F, 2.0F,
                4, 12, packedLight, OverlayTexture.NO_OVERLAY,
                1f, 1f, 1f, 1f,
                new CubeUV(
                        null,
                        null,
                        isRight ? null : uv,
                        isRight ? uv : null,
                        null,
                        null
                )
        );

        poseStack.popPose();
    }
}