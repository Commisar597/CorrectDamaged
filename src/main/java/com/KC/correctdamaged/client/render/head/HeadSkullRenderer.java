package com.KC.correctdamaged.client.render.head;

import com.KC.correctdamaged.capability.visual.HeadData;
import com.KC.correctdamaged.client.render.customRender.CubeUV;
import com.KC.correctdamaged.client.render.customRender.FreeUVCubeRenderer;
import com.KC.correctdamaged.client.render.octantRender.OctantRenderHelper;
import com.KC.correctdamaged.client.render.octantRender.OctreeMeshSplitter;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class HeadSkullRenderer {

    private static final ResourceLocation SKULL_TEXTURE = new ResourceLocation("correct_damaged", "textures/entity/bone_texture.png");
    private static final ResourceLocation BURNT_SKULL_TEXTURE = new ResourceLocation("correct_damaged", "textures/entity/burnt_bone_texture.png");

    private static final CubeUV HEAD_SKULL_UV = new CubeUV(
            FreeUVCubeRenderer.FaceUV.of(40F, 56F, 48F, 64F),
            FreeUVCubeRenderer.FaceUV.of(56F, 56F, 64F, 64F),
            FreeUVCubeRenderer.FaceUV.of(32F, 56F, 40F, 64F),
            FreeUVCubeRenderer.FaceUV.of(48F, 56F, 56F, 64F),
            FreeUVCubeRenderer.FaceUV.of(40F, 48F, 48F, 56F),
            FreeUVCubeRenderer.FaceUV.of(48F, 48F, 56F, 56F)
    );

    public static void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, HeadData headData) {
        byte skullMask = headData.getSkullMask();
        if (skullMask == 0) return;

        PoseStack.Pose pose = poseStack.last();
        ResourceLocation skullTex = headData.isBurntSkull() ? BURNT_SKULL_TEXTURE : SKULL_TEXTURE;
        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(skullTex));

        float radius = HeadLayerGeometry.getSkullRadius(headData);
        float[] yBounds = HeadLayerGeometry.getSkullYBounds(headData);

        if ((skullMask & 0xFF) == 0xFF) {
            FreeUVCubeRenderer.renderBox(
                    pose, consumer,
                    -radius, yBounds[0], -radius,
                    radius, yBounds[1], radius,
                    64, 64,
                    packedLight, OverlayTexture.NO_OVERLAY,
                    1.0F, 1.0F, 1.0F, 1.0F,
                    HEAD_SKULL_UV
            );
        } else {
            float x0 = -radius, y0 = yBounds[0], z0 = -radius;
            float x1 = radius,  y1 = yBounds[1], z1 = radius;

            for (int i = 0; i < 8; i++) {
                if ((skullMask & (1 << i)) == 0) continue;
                float[] bounds = OctreeMeshSplitter.getOctantBounds(x0, y0, z0, x1, y1, z1, i);
                CubeUV octantUV = OctreeMeshSplitter.getOctantUV(HEAD_SKULL_UV, i);

                OctantRenderHelper.renderOctant(
                        pose, consumer,
                        skullMask, i, bounds,
                        octantUV,
                        packedLight, OverlayTexture.NO_OVERLAY,
                        64, 64
                );
            }
        }
    }
}