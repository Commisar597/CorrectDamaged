package com.KC.correctdamaged.client.render.torso;

import com.KC.correctdamaged.capability.LimbManager;
import com.KC.correctdamaged.capability.visual.BodyData;
import com.KC.correctdamaged.capability.visual.BodyVoxelMatrix;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;

/**
 * Менеджер отрисовки анатомии туловища.
 * Зачем нужен: Координирует последовательность рендеринга внутренних слоев туловища
 * (костей, мышц и разрушаемой воксельной матрицы) на основе флагов из `BodyData`.
 */
public class BodyAnatomyManager {

    /**
     * Связывает позицию с родительской моделью игрока и поочередно рендерит открытые слои анатомии туловища.
     */
    public static void renderBody(
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            AbstractClientPlayer player,
            PlayerModel<AbstractClientPlayer> parentModel,
            BodyModel bodyModel
    ) {
        LimbManager.get(player).ifPresent(limbData -> {
            BodyData bodyData = limbData.getBody();

            // Если туловище полностью целое и нет флагов показа скелета/мышц — пропуск
            if (bodyData.isBodyIntact() && bodyData.getShowSkeleton() == 0 && bodyData.getMuscleBody() == 0) {
                return;
            }

            poseStack.pushPose();
            parentModel.body.translateAndRotate(poseStack);

            // 1. Рендеринг скелета (при наличии повреждений или глубоком ранении)
            int showSkeleton = bodyData.getShowSkeleton();
            if (showSkeleton > 0) {
                boolean isBurnt = bodyData.isBurntSkeleton();
                bodyModel.renderSkeleton(poseStack, buffer, packedLight, isBurnt);
            }

            // 2. Рендеринг мышц
            if (bodyData.getMuscleBody() > 0) {
                bodyModel.renderMuscles(poseStack, buffer, packedLight);
            }

            // 3. Рендеринг разрушаемой воксельной сетки срезов ран
            BodyVoxelMatrix matrix = bodyData.getBodyVoxelMatrix();
            if (matrix != null && matrix.hasDamage()) {
                VoxelBodyRenderer.renderVoxelBody(poseStack, buffer, packedLight, player, matrix);
            }

            poseStack.popPose();
        });
    }
}