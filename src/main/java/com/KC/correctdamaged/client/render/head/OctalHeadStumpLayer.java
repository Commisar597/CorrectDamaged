package com.KC.correctdamaged.client.render.head;

import com.KC.correctdamaged.capability.visual.HeadData;
import com.KC.correctdamaged.capability.LimbManager;
import com.KC.correctdamaged.client.render.customRender.CubeUV;
import com.KC.correctdamaged.client.render.customRender.FreeUVCubeRenderer;
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

/**
 * Слой рендеринга внутренних срезов (пеньков/ран) головы.
 * Зачем нужен: Рисует внутреннюю текстуру мяса/черепа на границе отрубленных или разрушенных октантов головы.
 */
public class OctalHeadStumpLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    private static final ResourceLocation NORMAL_STUMP = new ResourceLocation("correct_damaged",
            "textures/entity/head_stump_8x8.png");
    private static final ResourceLocation BURNT_STUMP = new ResourceLocation("correct_damaged",
            "textures/entity/head_stump_8x8_burnt_bone.png");
    private static final ResourceLocation SCULL_STUMP = new ResourceLocation("correct_damaged",
            "textures/entity/head_stump_8x8_scull.png");
    private static final ResourceLocation BURNT_SCULL_STUMP = new ResourceLocation("correct_damaged",
            "textures/entity/head_stump_8x8_burnt_bone_scull.png");

    public OctalHeadStumpLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent) {
        super(parent);
    }

    @Override
    public void render(
            PoseStack poseStack, MultiBufferSource buffer, int packedLight,
            AbstractClientPlayer player, float limbSwing, float limbSwingAmount,
            float partialTicks, float ageInTicks, float netHeadYaw, float headPitch
    ) {
        LimbManager.get(player).ifPresent(limbData -> {
            HeadData headData = limbData.getHead();
            byte skinMask = headData.getSkinMask();
            byte muscleMask = headData.getMuscleMask();

            if ((skinMask & 0xFF) == 0xFF) {
                return;
            }

            poseStack.pushPose();
            getParentModel().head.translateAndRotate(poseStack);

            PoseStack.Pose pose = poseStack.last();

            ResourceLocation stumpTex;
            boolean isSkullOnly = (skinMask == 0 && muscleMask == 0);
            boolean isMuscleOnly = (skinMask == 0 && (muscleMask & 0xFF) != 0);

            if (isSkullOnly) {
                stumpTex = headData.isBurntSkull() ? BURNT_SCULL_STUMP : SCULL_STUMP;
            } else if (isMuscleOnly) {
                stumpTex = (skinMask == 2) ? BURNT_STUMP : NORMAL_STUMP;
            } else {
                stumpTex = (skinMask == 2) ? BURNT_STUMP : NORMAL_STUMP;
            }

            VertexConsumer stumpConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(stumpTex));

            byte activeMask = skinMask;
            if (skinMask == 0) {
                activeMask = (muscleMask == 0) ? headData.getSkullMask() : muscleMask;
            }

            renderStumpOctants(pose, stumpConsumer, activeMask, headData, packedLight);

            poseStack.popPose();
        });
    }

    /**
     * Перебирает октанты активного слоя и рендерит внутренние спилы/срезы для отсутствующих соседей.
     */
    private void renderStumpOctants(
            PoseStack.Pose pose, VertexConsumer consumer,
            byte activeMask, HeadData headData, int packedLight
    ) {
        float radius;
        float[] yBounds;

        if (headData.getSkinMask() == 0) {
            if (headData.getMuscleMask() == 0) {
                radius = HeadLayerGeometry.getSkullRadius(headData);
                yBounds = HeadLayerGeometry.getSkullYBounds(headData);
            } else {
                radius = HeadLayerGeometry.getMuscleRadius(headData);
                yBounds = HeadLayerGeometry.getMuscleYBounds(headData);
            }
        } else {
            radius = HeadLayerGeometry.getSkinRadius(headData);
            yBounds = HeadLayerGeometry.getSkinYBounds(headData);
        }

        float x0 = -radius, y0 = yBounds[0], z0 = -radius;
        float x1 = radius,  y1 = yBounds[1], z1 = radius;

        for (int i = 0; i < 8; i++) {
            if ((activeMask & (1 << i)) == 0) continue;

            float[] bounds = OctreeMeshSplitter.getOctantBounds(x0, y0, z0, x1, y1, z1, i);
            CubeUV stumpUV = getStumpUV(activeMask, i, bounds);

            if (stumpUV != null) {
                FreeUVCubeRenderer.renderBox(
                        pose, consumer,
                        bounds[0], bounds[1], bounds[2],
                        bounds[3], bounds[4], bounds[5],
                        8, 8,
                        packedLight, OverlayTexture.NO_OVERLAY,
                        1.0F, 1.0F, 1.0F, 1.0F,
                        stumpUV
                );
            }
        }
    }

    /**
     * Высчитывает UV внутренних граней среза (stump) для октанта на основе отсутствия соседних октантов.
     */
    private CubeUV getStumpUV(byte headMask, int octantIndex, float[] b) {
        float minX = b[0], minY = b[1], minZ = b[2];
        float maxX = b[3], maxY = b[4], maxZ = b[5];

        float uX0 = minX + 4.0F, uX1 = maxX + 4.0F;
        float vY0 = minY + 8.0F, vY1 = maxY + 8.0F;
        float uZ0 = minZ + 4.0F, uZ1 = maxZ + 4.0F;

        boolean neighborX = (headMask & (1 << (octantIndex ^ 1))) != 0;
        boolean neighborY = (headMask & (1 << (octantIndex ^ 2))) != 0;
        boolean neighborZ = (headMask & (1 << (octantIndex ^ 4))) != 0;

        FreeUVCubeRenderer.FaceUV leftFace = null;
        FreeUVCubeRenderer.FaceUV rightFace = null;
        FreeUVCubeRenderer.FaceUV topFace = null;
        FreeUVCubeRenderer.FaceUV bottomFace = null;
        FreeUVCubeRenderer.FaceUV frontFace = null;
        FreeUVCubeRenderer.FaceUV backFace = null;

        boolean hasStump = false;

        if (!neighborX) {
            hasStump = true;
            if ((octantIndex & 1) != 0) {
                leftFace = FreeUVCubeRenderer.FaceUV.of(uZ1, vY0, uZ0, vY1);
            } else {
                rightFace = FreeUVCubeRenderer.FaceUV.of(uZ0, vY0, uZ1, vY1);
            }
        }

        if (!neighborY) {
            hasStump = true;
            if ((octantIndex & 2) != 0) {
                topFace = FreeUVCubeRenderer.FaceUV.of(uX0, uZ1, uX1, uZ0);
            } else {
                bottomFace = FreeUVCubeRenderer.FaceUV.of(uX0, uZ0, uX1, uZ1);
            }
        }

        if (!neighborZ) {
            hasStump = true;
            if ((octantIndex & 4) != 0) {
                frontFace = FreeUVCubeRenderer.FaceUV.of(uX0, vY0, uX1, vY1);
            } else {
                backFace = FreeUVCubeRenderer.FaceUV.of(uX1, vY0, uX0, vY1);
            }
        }

        if (!hasStump) return null;

        return new CubeUV(frontFace, backFace, leftFace, rightFace, topFace, bottomFace);
    }
}