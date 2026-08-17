package com.KC.correctdamaged.client.render.head;

import com.KC.correctdamaged.capability.visual.HeadData;
import com.KC.correctdamaged.capability.LimbManager;
import com.KC.correctdamaged.client.render.customRender.CubeUV;
import com.KC.correctdamaged.client.render.customRender.FreeUVCubeRenderer;
import com.KC.correctdamaged.client.render.octantRender.OctantRenderHelper;
import com.KC.correctdamaged.client.render.octantRender.OctreeMeshSplitter;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class OctalHeadDamageLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    private static final CubeUV HEAD_UV = new CubeUV(
            FreeUVCubeRenderer.FaceUV.of(8F, 8F, 16F, 16F),
            FreeUVCubeRenderer.FaceUV.of(24F, 8F, 32F, 16F),
            FreeUVCubeRenderer.FaceUV.of(0F, 8F, 8F, 16F),
            FreeUVCubeRenderer.FaceUV.of(16F, 8F, 24F, 16F),
            FreeUVCubeRenderer.FaceUV.of(8F, 0F, 16F, 8F),
            FreeUVCubeRenderer.FaceUV.of(16F, 0F, 24F, 8F)
    );

    public OctalHeadDamageLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent) {
        super(parent);
    }

    @Override
    public void render(
            PoseStack poseStack, MultiBufferSource buffer, int packedLight,
            AbstractClientPlayer player, float limbSwing, float limbSwingAmount,
            float partialTicks, float ageInTicks, float netHeadYaw, float headPitch
    ) {
        LimbManager.get(player).ifPresent(data -> {
            HeadData headData = data.getHead();
            byte headMask = headData.getSkinMask();

            if (headMask == 0 || (headMask & 0xFF) == 0xFF) {
                return;
            }

            poseStack.pushPose();
            getParentModel().head.translateAndRotate(poseStack);

            PoseStack.Pose pose = poseStack.last();

            ResourceLocation skinTex = player.getSkinTextureLocation();
            VertexConsumer skinConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(skinTex));

            renderSkinOctants(pose, skinConsumer, headMask, headData, packedLight);

            poseStack.popPose();
        });
    }

    private void renderSkinOctants(
            PoseStack.Pose pose, VertexConsumer skinConsumer,
            byte headMask, HeadData headData, int packedLight
    ) {
        float radius = HeadLayerGeometry.getSkinRadius(headData);
        float[] yBounds = HeadLayerGeometry.getSkinYBounds(headData);

        float x0 = -radius, y0 = yBounds[0], z0 = -radius;
        float x1 = radius,  y1 = yBounds[1], z1 = radius;

        for (int i = 0; i < 8; i++) {
            if ((headMask & (1 << i)) == 0) continue;

            float[] bounds = OctreeMeshSplitter.getOctantBounds(x0, y0, z0, x1, y1, z1, i);
            CubeUV octantUV = OctreeMeshSplitter.getOctantUV(HEAD_UV, i);

            OctantRenderHelper.renderOctant(
                    pose, skinConsumer,
                    headMask, i, bounds,
                    octantUV,
                    packedLight, OverlayTexture.NO_OVERLAY,
                    64, 64
            );
        }
    }
}