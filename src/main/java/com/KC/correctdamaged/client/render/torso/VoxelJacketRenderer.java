package com.KC.correctdamaged.client.render.torso;

import com.KC.correctdamaged.capability.LimbManager;
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

public class VoxelJacketRenderer {

    private static final int SKIN_TEX_W = 64;
    private static final int SKIN_TEX_H = 64;

    public static void renderVoxelJacket(
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            AbstractClientPlayer player,
            BodyVoxelMatrix jacketMatrix,
            BodyVoxelMatrix bodyMatrix
    ) {
        if (jacketMatrix == null || bodyMatrix == null) return;

        boolean isBodyIntact = LimbManager.get(player)
                .map(data -> data.getBody().isBodyIntact())
                .orElse(true);

        if (isBodyIntact) {
            return;
        }

        if (!isValidGridRatio(jacketMatrix, bodyMatrix)) {
            return;
        }

        VertexConsumer skinConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(player.getSkinTextureLocation()));

        PoseStack.Pose pose = poseStack.last();
        int overlay = OverlayTexture.NO_OVERLAY;

        float extra = 0.25f;

        for (int x = 0; x < jacketMatrix.getWidthX(); x++) {
            for (int y = 0; y < jacketMatrix.getHeightY(); y++) {
                for (int z = 0; z < jacketMatrix.getDepthZ(); z++) {

                    if (!jacketMatrix.isSolid(x, y, z)) continue;

                    if (!isBodyFullyIntactUnderJacket(bodyMatrix, x, y, z)) continue;

                    float x0 = (x * 2) - 4.0f - extra;
                    float x1 = x0 + 2.0f + (extra * 2);
                    float y0 = (y * 2) - extra;
                    float y1 = y0 + 2.0f + (extra * 2);
                    float z0 = (z * 2) - 2.0f - extra;
                    float z1 = z0 + 2.0f + (extra * 2);

                    CubeUV jacketUV = buildJacketUV(x, y, z, jacketMatrix);
                    FreeUVCubeRenderer.renderBox(
                            pose, skinConsumer,
                            x0, y0, z0, x1, y1, z1,
                            SKIN_TEX_W, SKIN_TEX_H,
                            packedLight, overlay,
                            1.0F, 1.0F, 1.0F, 1.0F,
                            jacketUV
                    );
                }
            }
        }
    }

    private static boolean isValidGridRatio(BodyVoxelMatrix jacket, BodyVoxelMatrix body) {
        return body.getWidthX() == jacket.getWidthX() * 2
                && body.getHeightY() == jacket.getHeightY() * 2
                && body.getDepthZ() == jacket.getDepthZ() * 2;
    }

    private static boolean isBodyFullyIntactUnderJacket(BodyVoxelMatrix bodyMatrix, int jX, int jY, int jZ) {
        int startX = jX * 2;
        int startY = jY * 2;
        int startZ = jZ * 2;

        for (int dx = 0; dx < 2; dx++) {
            for (int dy = 0; dy < 2; dy++) {
                for (int dz = 0; dz < 2; dz++) {
                    if (!bodyMatrix.isSolidSafe(startX + dx, startY + dy, startZ + dz)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private static CubeUV buildJacketUV(int x, int y, int z, BodyVoxelMatrix matrix) {
        FaceUV front = null;
        FaceUV back = null;
        FaceUV left = null;
        FaceUV right = null;
        FaceUV top = null;
        FaceUV bottom = null;

        int uOffset = 0;
        int vOffset = 16;

        if (!matrix.isSolidSafe(x, y, z - 1)) {
            int uStart = 20 + (x * 2) + uOffset;
            int vStart = 20 + (y * 2) + vOffset;
            front = FaceUV.of(uStart, vStart, uStart + 2, vStart + 2);
        }

        if (!matrix.isSolidSafe(x, y, z + 1)) {
            int uStart = 32 + (7 - (x * 2 + 1)) + uOffset;
            int vStart = 20 + (y * 2) + vOffset;
            back = FaceUV.of(uStart, vStart, uStart + 2, vStart + 2);
        }

        if (!matrix.isSolidSafe(x - 1, y, z)) {
            int uStart = 16 + (z * 2) + uOffset;
            int vStart = 20 + (y * 2) + vOffset;
            left = FaceUV.of(uStart, vStart, uStart + 2, vStart + 2);
        }

        if (!matrix.isSolidSafe(x + 1, y, z)) {
            int uStart = 28 + (3 - (z * 2 + 1)) + uOffset;
            int vStart = 20 + (y * 2) + vOffset;
            right = FaceUV.of(uStart, vStart, uStart + 2, vStart + 2);
        }

        if (!matrix.isSolidSafe(x, y - 1, z)) {
            int uStart = 20 + (x * 2) + uOffset;
            int vStart = 16 + (3 - (z * 2 + 1)) + vOffset;
            top = FaceUV.of(uStart, vStart, uStart + 2, vStart + 2);
        }

        if (!matrix.isSolidSafe(x, y + 1, z)) {
            int uStart = 28 + (x * 2) + uOffset;
            int vStart = 16 + (z * 2) + vOffset;
            bottom = FaceUV.of(uStart, vStart, uStart + 2, vStart + 2);
        }

        return new CubeUV(front, back, left, right, top, bottom);
    }
}