package com.KC.correctdamaged.client.render.limbs;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;

public class LimbAnatomyManager {

    public static void renderLimbAnatomy(
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            AbstractClientPlayer player,
            PlayerModel<AbstractClientPlayer> model,
            PlayerBonesRenderer bonesRenderer,
            PlayerMusclesRenderer musclesRenderer,
            StumpRenderer stumpRenderer,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {
        bonesRenderer.render(
                poseStack, buffer, packedLight, player,
                limbSwing, limbSwingAmount, ageInTicks,
                netHeadYaw, headPitch, model
        );

        musclesRenderer.render(
                poseStack, buffer, packedLight, player,
                limbSwing, limbSwingAmount, ageInTicks,
                netHeadYaw, headPitch, model
        );

        stumpRenderer.render(poseStack, buffer, packedLight, player, model);

        LimbDamageRenderer.render(poseStack, buffer, packedLight, player, model);
    }
}