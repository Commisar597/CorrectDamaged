package com.KC.correctdamaged.client.render.torso;

import com.KC.correctdamaged.capability.LimbManager;
import com.KC.correctdamaged.capability.visual.BodyData;
import com.KC.correctdamaged.capability.visual.BodyVoxelMatrix;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.player.PlayerModelPart;

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

            if (bodyData.isBodyIntact() && bodyData.getShowSkeleton() == 0 && bodyData.getMuscleOctantBody() == 0) {
                return;
            }

            poseStack.pushPose();
            parentModel.body.translateAndRotate(poseStack);

            int showSkeleton = bodyData.getShowSkeleton();
            if (showSkeleton > 0) {
                boolean isBurnt = bodyData.isBurntSkeleton();
                bodyModel.renderSkeleton(poseStack, buffer, packedLight, isBurnt);
            }

            int muscleMask = bodyData.getMuscleOctantBody();

            if (bodyData.getMuscleOctantBody() > 0) {
                bodyModel.renderOctalMuscles(poseStack, buffer, muscleMask, packedLight);
            }

            BodyVoxelMatrix matrix = bodyData.getBodyVoxelMatrix();
            if (matrix != null && matrix.hasDamage()) {
                VoxelBodyRenderer.renderVoxelBody(poseStack, buffer, packedLight, player, matrix);
                VoxelDamageRenderer.renderVoxelBody(poseStack, buffer, packedLight, player, matrix);
            }

            if (player.isModelPartShown(PlayerModelPart.JACKET)) {
                int jacketMask = bodyData.getJacketOctantBody();
                if (jacketMask != 0) {
                    VertexConsumer jacketConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(player.getSkinTextureLocation()));
                    bodyModel.renderOctalJacket(poseStack, jacketConsumer, jacketMask, packedLight);
                }
            }

            poseStack.popPose();
        });
    }
}