package com.KC.correctdamaged.client.render.torso;

import com.KC.correctdamaged.CorrectDamaged;
import com.KC.correctdamaged.capability.visual.BodyVoxelMatrix;
import com.KC.correctdamaged.client.render.customRender.CubeUV;
import com.KC.correctdamaged.client.render.customRender.FreeUVCubeRenderer;
import com.KC.correctdamaged.client.render.customRender.FreeUVCubeRenderer.FaceUV;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class VoxelMusclesRenderer {

    public static final ResourceLocation MUSCLE = new ResourceLocation(CorrectDamaged.MODID,
            "textures/entity/muscles_texture.png");

    private static final int MUSCLES_W = 64;
    private static final int MUSCLES_H = 64;

    public static void renderVoxelMuscles(
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            AbstractClientPlayer player,
            BodyVoxelMatrix muscleMatrix,
            BodyVoxelMatrix bodyMatrix
    ) {
        if (muscleMatrix == null || bodyMatrix == null) return;

        if (!isValidInnerGridRatio(muscleMatrix, bodyMatrix)) {
            return;
        }

        VertexConsumer skinConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(MUSCLE));

        PoseStack.Pose pose = poseStack.last();
        int overlay = OverlayTexture.NO_OVERLAY;

        float smoller = -0.125f;

        for (int x = 0; x < muscleMatrix.getWidthX(); x++) {
            for (int y = 0; y < muscleMatrix.getHeightY(); y++) {
                for (int z = 0; z < muscleMatrix.getDepthZ(); z++) {

                    if (!muscleMatrix.isSolid(x, y, z)) continue;

                    int bx = x + 1;
                    int by = y + 1;
                    int bz = z + 1;

                    if (!bodyMatrix.isSolidSafe(bx, by, bz)) continue;

                    float x0 = bx - 4.0f - smoller;
                    float x1 = x0 + 1.0f + (smoller * 2);
                    float y0 = by - smoller;
                    float y1 = y0 + 1.0f + (smoller * 2);
                    float z0 = bz - 2.0f - smoller;
                    float z1 = z0 + 1.0f + (smoller * 2);

                    CubeUV muscleUV = buildMuscleUV(x, y, z, muscleMatrix);
                    FreeUVCubeRenderer.renderBox(
                            pose, skinConsumer,
                            x0, y0, z0, x1, y1, z1,
                            MUSCLES_W, MUSCLES_H,
                            packedLight, overlay,
                            1.0F, 1.0F, 1.0F, 1.0F,
                            muscleUV
                    );
                }
            }
        }
    }

    private static boolean isValidInnerGridRatio(BodyVoxelMatrix muscle, BodyVoxelMatrix body) {
        return body.getWidthX() == muscle.getWidthX() + 2
                && body.getHeightY() == muscle.getHeightY() + 2
                && body.getDepthZ() == muscle.getDepthZ() + 2;
    }

    private static CubeUV buildMuscleUV(int x, int y, int z, BodyVoxelMatrix matrix) {
        FaceUV front = null;
        FaceUV back = null;
        FaceUV left = null;
        FaceUV right = null;
        FaceUV top = null;
        FaceUV bottom = null;

        int uOffset = 0;
        int vOffset = 16;

        if (!matrix.isSolidSafe(x, y, z - 1)) {
            int uStart = 20 + (x + 1) + uOffset;
            int vStart = 20 + (y + 1) + vOffset;
            front = FaceUV.of(uStart, vStart, uStart + 1, vStart + 1);
        }

        if (!matrix.isSolidSafe(x, y, z + 1)) {
            int uStart = 32 + (7 - (x + 1)) + uOffset;
            int vStart = 20 + (y + 1) + vOffset;
            back = FaceUV.of(uStart, vStart, uStart + 1, vStart + 1);
        }

        if (!matrix.isSolidSafe(x - 1, y, z)) {
            int uStart = 16 + (z + 1) + uOffset;
            int vStart = 20 + (y + 1) + vOffset;
            left = FaceUV.of(uStart, vStart, uStart + 1, vStart + 1);
        }

        if (!matrix.isSolidSafe(x + 1, y, z)) {
            int uStart = 28 + (3 - (z + 1)) + uOffset;
            int vStart = 20 + (y + 1) + vOffset;
            right = FaceUV.of(uStart, vStart, uStart + 1, vStart + 1);
        }

        if (!matrix.isSolidSafe(x, y - 1, z)) {
            int uStart = 20 + (x + 1) + uOffset;
            int vStart = 16 + (z + 1) + vOffset;
            top = FaceUV.of(uStart, vStart, uStart + 1, vStart + 1);
        }

        if (!matrix.isSolidSafe(x, y + 1, z)) {
            int uStart = 28 + (x + 1) + uOffset;
            int vStart = 16 + (z + 1) + vOffset;
            bottom = FaceUV.of(uStart, vStart, uStart + 1, vStart + 1);
        }

        return new CubeUV(front, back, left, right, top, bottom);
    }
}