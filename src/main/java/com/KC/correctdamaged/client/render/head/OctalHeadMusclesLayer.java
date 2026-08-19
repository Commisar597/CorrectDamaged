package com.KC.correctdamaged.client.render.head;

import com.KC.correctdamaged.capability.visual.HeadData;
import com.KC.correctdamaged.capability.LimbManager;
import com.KC.correctdamaged.client.render.PlayerMusclesModel;
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

/**
 * Слой рендеринга мышечного слоя головы.
 * Зачем нужен: Отображает промежуточный мышечный слой головы по октантам,
 * располагающийся между кожей и черепом.
 */
public class OctalHeadMusclesLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    private static final CubeUV HEAD_MUSCLE_UV = new CubeUV(
            FreeUVCubeRenderer.FaceUV.of(40F, 56F, 48F, 64F),
            FreeUVCubeRenderer.FaceUV.of(56F, 56F, 64F, 64F),
            FreeUVCubeRenderer.FaceUV.of(32F, 56F, 40F, 64F),
            FreeUVCubeRenderer.FaceUV.of(48F, 56F, 56F, 64F),
            FreeUVCubeRenderer.FaceUV.of(40F, 48F, 48F, 56F),
            FreeUVCubeRenderer.FaceUV.of(48F, 48F, 56F, 56F)
    );

    public OctalHeadMusclesLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent) {
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
            byte muscleMask = headData.getMuscleMask();

            if (muscleMask == 0) {
                return;
            }

            poseStack.pushPose();
            getParentModel().head.translateAndRotate(poseStack);

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
                renderMuscleOctants(pose, consumer, muscleMask, headData, packedLight);
            }

            poseStack.popPose();
        });
    }

    /**
     * Отрисовывает мышцы головы по октантам.
     */
    private void renderMuscleOctants(
            PoseStack.Pose pose, VertexConsumer consumer,
            byte muscleMask, HeadData headData, int packedLight
    ) {
        float radius = HeadLayerGeometry.getMuscleRadius(headData);
        float[] yBounds = HeadLayerGeometry.getMuscleYBounds(headData);

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