package com.KC.correctdamaged.client.render.torso;

import com.KC.correctdamaged.capability.LimbManager;
import com.KC.correctdamaged.capability.visual.BodyData;
import com.KC.correctdamaged.capability.visual.BodyVoxelMatrix;
import com.KC.correctdamaged.client.render.torso.organs.PlayerOrgansModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.player.PlayerModelPart;

public class BodyAnatomyManager {

    public static void renderBody(
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            AbstractClientPlayer player,
            PlayerModel<AbstractClientPlayer> parentModel,
            BodyModel bodyModel,
            PlayerOrgansModel organsModel
    ) {
        var capOpt = LimbManager.get(player).resolve();
        if (capOpt.isEmpty()) return;

        BodyData bodyData = capOpt.get().getBody();

        poseStack.pushPose();
        parentModel.body.translateAndRotate(poseStack);

        int showSkeleton = bodyData.getShowSkeleton();
        if (showSkeleton > 0) {
            boolean isBurnt = bodyData.isBurntSkeleton();
            bodyModel.renderSkeleton(poseStack, buffer, packedLight, isBurnt);
        }

        BodyVoxelMatrix bodyMatrix = bodyData.getBodyVoxelMatrix();
        int muscleMask = bodyData.getMusclesMask();

        if (muscleMask == 1) {
            bodyModel.renderMuscles(poseStack, buffer, packedLight);
        } else if (muscleMask == 2) {
            BodyVoxelMatrix muscleMatrix = bodyData.getMuscleVoxelMatrix();
            if (muscleMatrix != null) {
                VoxelMusclesRenderer.renderVoxelMuscles(
                        poseStack, buffer, packedLight, muscleMatrix, bodyMatrix
                );
            }
        }

        if (bodyData.getOrgansVisible() == 1 && bodyData.getOrgansData() != null) {
            organsModel.renderOrgans(poseStack, buffer, packedLight, bodyData.getOrgansData());
        }

        if (bodyMatrix != null && bodyMatrix.hasDamage()) {
            VoxelBodyRenderer.renderVoxelBody(poseStack, buffer, packedLight, player, bodyMatrix);
            VoxelDamageRenderer.renderVoxelBody(poseStack, buffer, packedLight, player, bodyMatrix);
        }

        if (player.isModelPartShown(PlayerModelPart.JACKET)) {
            int jacketMask = bodyData.getJacketMask();
            if (jacketMask != 0) {
                BodyVoxelMatrix jacketMatrix = bodyData.getJacketVoxelMatrix();
                if (jacketMatrix != null) {
                    VoxelJacketRenderer.renderVoxelJacket(
                            poseStack, buffer, packedLight, player, jacketMatrix, bodyMatrix
                    );
                }
            }
        }

        poseStack.popPose();
    }
}