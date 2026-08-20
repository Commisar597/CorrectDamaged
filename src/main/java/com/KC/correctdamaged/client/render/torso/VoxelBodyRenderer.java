package com.KC.correctdamaged.client.render.torso;

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

public class VoxelBodyRenderer {

    private static final int SKIN_TEX_W = 64;
    private static final int SKIN_TEX_H = 64;

    public static void renderVoxelBody(
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            AbstractClientPlayer player,
            BodyVoxelMatrix matrix
    ) {
        VertexConsumer skinConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(player.getSkinTextureLocation()));

        PoseStack.Pose pose = poseStack.last();
        int overlay = OverlayTexture.NO_OVERLAY;

        for (int x = 0; x < matrix.getWidthX(); x++) {
            for (int y = 0; y < matrix.getHeightY(); y++) {
                for (int z = 0; z < matrix.getDepthZ(); z++) {

                    if (!matrix.isSolid(x, y, z)) continue;

                    if (isLonely(matrix, x, y, z)) continue;

                    float x0 = (x - 4);
                    float x1 = x0 + 1;
                    float y0 = y;
                    float y1 = y0 + 1;
                    float z0 = (z - 2);
                    float z1 = z0 + 1;

                    boolean borderToDamage = hasDamagedNeighbor(matrix, x, y, z);

                    if (!borderToDamage) {
                        CubeUV skinUV = buildSkinUV(x, y, z, matrix);
                        if (hasAnyFace(skinUV)) {
                            FreeUVCubeRenderer.renderBox(
                                    pose, skinConsumer,
                                    x0, y0, z0, x1, y1, z1,
                                    SKIN_TEX_W, SKIN_TEX_H,
                                    packedLight, overlay,
                                    1.0F, 1.0F, 1.0F, 1.0F,
                                    skinUV
                            );
                        }
                    }
                }
            }
        }
    }

    private static CubeUV buildSkinUV(int x, int y, int z, BodyVoxelMatrix matrix) {
        FaceUV front = null;
        FaceUV back = null;
        FaceUV left = null;
        FaceUV right = null;
        FaceUV top = null;
        FaceUV bottom = null;

        if (!matrix.isInBounds(x, y, z - 1)) {
            int uStart = 20 + x;
            int vStart = 20 + y;
            front = FaceUV.of(uStart, vStart, uStart + 1, vStart + 1);
        }

        if (!matrix.isInBounds(x, y, z + 1)) {
            int uStart = 32 + (7 - x);
            int vStart = 20 + y;
            back = FaceUV.of(uStart, vStart, uStart + 1, vStart + 1);
        }

        if (!matrix.isInBounds(x - 1, y, z)) {
            int uStart = 16 + z;
            int vStart = 20 + y;
            left = FaceUV.of(uStart, vStart, uStart + 1, vStart + 1);
        }

        if (!matrix.isInBounds(x + 1, y, z)) {
            int uStart = 28 + (3 - z);
            int vStart = 20 + y;
            right = FaceUV.of(uStart, vStart, uStart + 1, vStart + 1);
        }

        if (!matrix.isInBounds(x, y - 1, z)) {
            int uStart = 20 + x;
            int vStart = 16 + z;
            top = FaceUV.of(uStart, vStart, uStart + 1, vStart + 1);
        }

        if (!matrix.isInBounds(x, y + 1, z)) {
            int uStart = 28 + x;
            int vStart = 16 + z;
            bottom = FaceUV.of(uStart, vStart, uStart + 1, vStart + 1);
        }

        return new CubeUV(front, back, left, right, top, bottom);
    }

    private static boolean hasDamagedNeighbor(BodyVoxelMatrix matrix, int x, int y, int z) {
        if (matrix.isInBounds(x, y, z - 1) && !matrix.isSolid(x, y, z - 1)) return true;
        if (matrix.isInBounds(x, y, z + 1) && !matrix.isSolid(x, y, z + 1)) return true;
        if (matrix.isInBounds(x - 1, y, z) && !matrix.isSolid(x - 1, y, z)) return true;
        if (matrix.isInBounds(x + 1, y, z) && !matrix.isSolid(x + 1, y, z)) return true;
        if (matrix.isInBounds(x, y - 1, z) && !matrix.isSolid(x, y - 1, z)) return true;
        if (matrix.isInBounds(x, y + 1, z) && !matrix.isSolid(x, y + 1, z)) return true;

        return false;
    }


    private static boolean isLonely(BodyVoxelMatrix matrix, int x, int y, int z) {
        return !matrix.isSolidSafe(x, y, z - 1) &&
                !matrix.isSolidSafe(x, y, z + 1) &&
                !matrix.isSolidSafe(x - 1, y, z) &&
                !matrix.isSolidSafe(x + 1, y, z) &&
                !matrix.isSolidSafe(x, y - 1, z) &&
                !matrix.isSolidSafe(x, y + 1, z);
    }

    private static boolean hasAnyFace(CubeUV uv) {
        return uv.front() != null || uv.back() != null || uv.left() != null ||
                uv.right() != null || uv.top() != null || uv.bottom() != null;
    }
}