package com.KC.correctdamaged.client.render.head;

import com.KC.correctdamaged.capability.LimbManager;
import com.KC.correctdamaged.capability.visual.HeadData;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;

public class HeadAnatomyLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    public HeadAnatomyLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent) {
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

            poseStack.pushPose();
            getParentModel().head.translateAndRotate(poseStack);

            HeadManager.renderHeadAnatomy(poseStack, buffer, packedLight, player, headData);

            poseStack.popPose();
        });
    }
}