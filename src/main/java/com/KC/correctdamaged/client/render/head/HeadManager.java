package com.KC.correctdamaged.client.render.head;

import com.KC.correctdamaged.capability.visual.HeadData;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;

public class HeadManager {

    public static void renderHeadAnatomy(
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            AbstractClientPlayer player,
            HeadData headData
    ) {
        HeadSkullRenderer.render(poseStack, buffer, packedLight, headData);

        HeadMusclesRenderer.render(poseStack, buffer, packedLight, headData);

        HeadStumpRenderer.render(poseStack, buffer, packedLight, headData);

        HeadSkinAndHatRenderer.renderSkin(poseStack, buffer, packedLight, player, headData);

        HeadSkinAndHatRenderer.renderHat(poseStack, buffer, packedLight, player, headData);
    }
}