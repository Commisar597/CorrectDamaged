package com.KC.correctdamaged.client.render;

import com.KC.correctdamaged.capability.LimbManager;
import com.KC.correctdamaged.client.render.customRender.CustomCube;
import com.KC.correctdamaged.client.render.customRender.FreeUVCubeRenderer;
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

public class BodyDamageLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    public BodyDamageLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent) {
        super(parent);
    }

    @Override
    public void render(
            PoseStack poseStack, MultiBufferSource buffer, int packedLight,
            AbstractClientPlayer player, float limbSwing, float limbSwingAmount,
            float partialTicks, float ageInTicks, float netHeadYaw, float headPitch
    ) {
        LimbManager.get(player).ifPresent(data -> {
            int bodyState = data.getBodyState();

            if (bodyState == 0 || bodyState == 9) return;

            BodyDamageVariant variant = BodyDamageVariants.VARIANTS.get(bodyState);
            if (variant == null) return;

            poseStack.pushPose();

            getParentModel().body.translateAndRotate(poseStack);

            ResourceLocation texture = player.getSkinTextureLocation();
            VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(texture));

            renderVariant(variant, poseStack, consumer, packedLight);

            poseStack.popPose();
        });
    }

    private void renderVariant(BodyDamageVariant variant, PoseStack poseStack, VertexConsumer consumer, int packedLight) {
        for (CustomCube cube : variant.cubes()) {
            float def = cube.deformation();

            float x0 = cube.x() - def;
            float y0 = cube.y() - def;
            float z0 = cube.z() - def;

            float x1 = cube.x() + cube.width() + def;
            float y1 = cube.y() + cube.height() + def;
            float z1 = cube.z() + cube.depth() + def;

            FreeUVCubeRenderer.renderBox(
                    poseStack.last(), consumer,
                    x0, y0, z0,
                    x1, y1, z1,
                    64, 64,
                    packedLight, OverlayTexture.NO_OVERLAY,
                    1.0F, 1.0F, 1.0F, 1.0F,
                    cube.uv()
            );
        }
    }
}