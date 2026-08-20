package com.KC.correctdamaged.client.render.head;

import com.KC.correctdamaged.capability.visual.HeadData;
import com.KC.correctdamaged.client.render.customRender.CubeUV;
import com.KC.correctdamaged.client.render.customRender.FreeUVCubeRenderer;
import com.KC.correctdamaged.client.render.octantRender.OctantRenderHelper;
import com.KC.correctdamaged.client.render.octantRender.OctreeMeshSplitter;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;

public class HeadSkinAndHatRenderer {

    private static final CubeUV HEAD_UV = new CubeUV(
            FreeUVCubeRenderer.FaceUV.of(8F, 8F, 16F, 16F),
            FreeUVCubeRenderer.FaceUV.of(24F, 8F, 32F, 16F),
            FreeUVCubeRenderer.FaceUV.of(0F, 8F, 8F, 16F),
            FreeUVCubeRenderer.FaceUV.of(16F, 8F, 24F, 16F),
            FreeUVCubeRenderer.FaceUV.of(8F, 0F, 16F, 8F),
            FreeUVCubeRenderer.FaceUV.of(16F, 0F, 24F, 8F)
    );

    private static final CubeUV HAT_UV = new CubeUV(
            FreeUVCubeRenderer.FaceUV.of(40F, 8F, 48F, 16F),
            FreeUVCubeRenderer.FaceUV.of(56F, 8F, 64F, 16F),
            FreeUVCubeRenderer.FaceUV.of(32F, 8F, 40F, 16F),
            FreeUVCubeRenderer.FaceUV.of(48F, 8F, 56F, 16F),
            FreeUVCubeRenderer.FaceUV.of(40F, 0F, 48F, 8F),
            FreeUVCubeRenderer.FaceUV.of(48F, 0F, 56F, 8F)
    );

    public static void renderSkin(PoseStack poseStack, MultiBufferSource buffer, int packedLight, AbstractClientPlayer player, HeadData headData) {
        byte headMask = headData.getSkinMask();
        if (headMask == 0 || (headMask & 0xFF) == 0xFF) return;

        PoseStack.Pose pose = poseStack.last();
        VertexConsumer skinConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(player.getSkinTextureLocation()));

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

    public static void renderHat(PoseStack poseStack, MultiBufferSource buffer, int packedLight, AbstractClientPlayer player, HeadData headData) {
        byte headMask = headData.getSkinMask();
        if (headMask == 0 || (headMask & 0xFF) == 0xFF) return;

        PoseStack.Pose pose = poseStack.last();
        VertexConsumer hatConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(player.getSkinTextureLocation()));

        float x0 = -4.25F, y0 = -8.25F, z0 = -4.25F;
        float x1 = 4.25F,  y1 =  0.25F,  z1 = 4.25F;

        for (int i = 0; i < 8; i++) {
            if ((headMask & (1 << i)) == 0) continue;
            float[] bounds = OctreeMeshSplitter.getOctantBounds(x0, y0, z0, x1, y1, z1, i);
            CubeUV octantUV = OctreeMeshSplitter.getOctantUV(HAT_UV, i);

            OctantRenderHelper.renderOctant(
                    pose, hatConsumer,
                    headMask, i, bounds,
                    octantUV,
                    packedLight, OverlayTexture.NO_OVERLAY,
                    64, 64
            );
        }
    }
}