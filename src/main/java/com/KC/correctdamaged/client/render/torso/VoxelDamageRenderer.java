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
import net.minecraft.resources.ResourceLocation;

public class VoxelDamageRenderer {

    private static final ResourceLocation FLESH_TEXTURE =
            new ResourceLocation("correct_damaged", "textures/entity/flesh_atlas.png");

    private static final int FLESH_TEX_W = 32;
    private static final int FLESH_TEX_H = 32;

    public static void renderVoxelBody(
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            AbstractClientPlayer player,
            BodyVoxelMatrix matrix
    ) {
        VertexConsumer fleshConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(FLESH_TEXTURE));

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

                    if (borderToDamage) {
                        CubeUV fleshUV = buildFleshUV(matrix, x, y, z);
                        if (hasAnyFace(fleshUV)) {
                            FreeUVCubeRenderer.renderBox(
                                    pose, fleshConsumer,
                                    x0, y0, z0, x1, y1, z1,
                                    FLESH_TEX_W, FLESH_TEX_H,
                                    packedLight, overlay,
                                    1.0F, 1.0F, 1.0F, 1.0F,
                                    fleshUV
                            );
                        }
                    }
                }
            }
        }
    }

    private static CubeUV buildFleshUV(BodyVoxelMatrix matrix, int x, int y, int z) {
        FaceUV front = null;
        FaceUV back = null;
        FaceUV left = null;
        FaceUV right = null;
        FaceUV top = null;
        FaceUV bottom = null;

        boolean isSurface = isSurfaceVoxel(x, y, z, matrix);
        FaceUV fleshTexture;
        if (isSurface) {
            fleshTexture = getTopRightQuadrantUV(x, y, z);
        } else {
            fleshTexture = getDeepFleshUV(x, y, z);
        }

        if (!matrix.isInBounds(x, y, z - 1) || !matrix.isSolid(x, y, z - 1)) {
            front = fleshTexture;
        }

        if (!matrix.isInBounds(x, y, z + 1) || !matrix.isSolid(x, y, z + 1)) {
            back = fleshTexture;
        }

        if (!matrix.isInBounds(x - 1, y, z) || !matrix.isSolid(x - 1, y, z)) {
            left = fleshTexture;
        }

        if (!matrix.isInBounds(x + 1, y, z) || !matrix.isSolid(x + 1, y, z)) {
            right = fleshTexture;
        }

        if (!matrix.isInBounds(x, y - 1, z) || !matrix.isSolid(x, y - 1, z)) {
            top = fleshTexture;
        }

        if (!matrix.isInBounds(x, y + 1, z) || !matrix.isSolid(x, y + 1, z)) {
            bottom = fleshTexture;
        }

        if (matrix.isInBounds(x, y + 1, z) && !matrix.isSolid(x, y + 1, z)) {
            bottom = fleshTexture;
        }

        return new CubeUV(front, back, left, right, top, bottom);
    }

    private static FaceUV getTopRightQuadrantUV(int x, int y, int z) {
        int uOffset = (x * 31 + y * 17 + z) & 15;
        int vOffset = (x * 13 + y * 7 + z * 31) & 15;
        return FaceUV.of(uOffset, vOffset, uOffset + 1, vOffset + 1);
    }

    private static FaceUV getDeepFleshUV(int x, int y, int z) {
        int uOffset = ((x * 31 + y * 17 + z) & 15) + 16;
        int vOffset = ((x * 13 - y * 7 + z * 31) & 15) + 16;

        return FaceUV.of(uOffset, vOffset, uOffset + 1, vOffset + 1);
    }

    private static boolean isSurfaceVoxel(int x, int y, int z, BodyVoxelMatrix matrix) {
        return x == 0 || x == matrix.getWidthX() - 1 ||
                y == 0 || y == matrix.getHeightY() - 1 ||
                z == 0 || z == matrix.getDepthZ() - 1;
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