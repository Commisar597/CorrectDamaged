package com.KC.correctdamaged.client.render.torso;

import com.KC.correctdamaged.capability.LimbManager;
import com.KC.correctdamaged.capability.visual.BodyData;
import com.KC.correctdamaged.capability.visual.BodyVoxelMatrix;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;

public class BodyAnatomyManager {

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

            if (bodyData.isBodyIntact() && bodyData.getShowSkeleton() == 0 && bodyData.getMuscleBody() == 0) {
                return;
            }

            poseStack.pushPose();
            parentModel.body.translateAndRotate(poseStack);

            int showSkeleton = bodyData.getShowSkeleton();
            if (showSkeleton > 0) {
                boolean isBurnt = bodyData.isBurntSkeleton();
                bodyModel.renderSkeleton(poseStack, buffer, packedLight, isBurnt);
            }

            if (bodyData.getMuscleBody() > 0) {
                bodyModel.renderMuscles(poseStack, buffer, packedLight);
            }

            BodyVoxelMatrix matrix = bodyData.getBodyVoxelMatrix();
            if (matrix != null && matrix.hasDamage()) {
                VoxelBodyRenderer.renderVoxelBody(poseStack, buffer, packedLight, player, matrix);
            }

            poseStack.popPose();
        });
    }
}