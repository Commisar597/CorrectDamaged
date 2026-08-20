package com.KC.correctdamaged.client.render.head;

import com.KC.correctdamaged.capability.visual.HeadData;
import com.KC.correctdamaged.client.render.PlayerMusclesModel;
import com.KC.correctdamaged.client.render.customRender.CubeUV;
import com.KC.correctdamaged.client.render.customRender.FreeUVCubeRenderer;
import com.KC.correctdamaged.client.render.octantRender.OctantRenderHelper;
import com.KC.correctdamaged.client.render.octantRender.OctreeMeshSplitter;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;

public class HeadMusclesRenderer {

    private static final CubeUV HEAD_MUSCLE_UV = new CubeUV(
            FreeUVCubeRenderer.FaceUV.of(40F, 56F, 48F, 64F),
            FreeUVCubeRenderer.FaceUV.of(56F, 56F, 64F, 64F),
            FreeUVCubeRenderer.FaceUV.of(32F, 56F, 40F, 64F),
            FreeUVCubeRenderer.FaceUV.of(48F, 56F, 56F, 64F),
            FreeUVCubeRenderer.FaceUV.of(40F, 48F, 48F, 56F),
            FreeUVCubeRenderer.FaceUV.of(48F, 48F, 56F, 56F)
    );

    public static void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, HeadData headData) {
        byte muscleMask = headData.getMuscleMask();
        if (muscleMask == 0) return;

        PoseStack.Pose pose = poseStack.last();
        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(PlayerMusclesModel.MUSCLE));

        float radius = HeadLayerGeometry.getMuscleRadius(headData);
        float[] yBounds = HeadLayerGeometry.getMuscleYBounds(headData);

        if ((muscleMask & 0xFF) == 0xFF) {
            FreeUVCubeRenderer.renderBox(
                    pose, consumer,
                    -radius, yBounds[0], -radius,
                    radius, yBounds[1], radius,
                    64, 64,
                    packedLight, OverlayTexture.NO_OVERLAY,
                    1.0F, 1.0F, 1.0F, 1.0F,
                    HEAD_MUSCLE_UV
            );
        } else {
            float x0 = -radius, y0 = yBounds[0], z0 = -radius;
            float x1 = radius,  y1 = yBounds[1], z1 = radius;

            for (int i = 0; i < 8; i++) {
                if ((muscleMask & (1 << i)) == 0) continue;
                float[] bounds = OctreeMeshSplitter.getOctantBounds(x0, y0, z0, x1, y1, z1, i);
                CubeUV octantUV = OctreeMeshSplitter.getOctantUV(HEAD_MUSCLE_UV, i);

                OctantRenderHelper.renderOctant(
                        pose, consumer,
                        muscleMask, i, bounds,
                        octantUV,
                        packedLight, OverlayTexture.NO_OVERLAY,
                        64, 64
                );
            }
        }
    }
}