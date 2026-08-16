package com.KC.correctdamaged.client.render.head;

import com.KC.correctdamaged.capability.HeadData;
import com.KC.correctdamaged.capability.LimbManager;
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
import net.minecraft.resources.ResourceLocation;

public class OctalHatDamageLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    private static final CubeUV HAT_UV = new CubeUV(
            FreeUVCubeRenderer.FaceUV.of(40F, 8F, 48F, 16F),
            FreeUVCubeRenderer.FaceUV.of(56F, 8F, 64F, 16F),
            FreeUVCubeRenderer.FaceUV.of(32F, 8F, 40F, 16F),
            FreeUVCubeRenderer.FaceUV.of(48F, 8F, 56F, 16F),
            FreeUVCubeRenderer.FaceUV.of(40F, 0F, 48F, 8F),
            FreeUVCubeRenderer.FaceUV.of(48F, 0F, 56F, 8F)
    );

    public OctalHatDamageLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent) {
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
            byte headMask = headData.getSkinMask();

            if (headMask == 0 || (headMask & 0xFF) == 0xFF) {
                return;
            }

            poseStack.pushPose();
            getParentModel().head.translateAndRotate(poseStack);

            PoseStack.Pose pose = poseStack.last();

            ResourceLocation skinTex = player.getSkinTextureLocation();
            VertexConsumer hatConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(skinTex));

            renderHatOctants(pose, hatConsumer, headMask, packedLight);

            poseStack.popPose();
        });
    }

    private void renderHatOctants(
            PoseStack.Pose pose, VertexConsumer hatConsumer,
            byte headMask, int packedLight
    ) {
        float x0 = -4.25F, y0 = -8.25F, z0 = -4.25F;
        float x1 = 4.25F,  y1 =  0.25F,  z1 = 4.25F;

        for (int i = 0; i < 8; i++) {
            if ((headMask & (1 << i)) == 0) continue;

            float[] bounds = OctreeMeshSplitter.getOctantBounds(x0, y0, z0, x1, y1, z1, i);
            CubeUV octantUV = OctreeMeshSplitter.getOctantUV(HAT_UV, i);

            OctantRenderHelper.renderOctant(
                    pose, hatConsumer,
                    headMask, i, bounds,
                    octantUV,
                    packedLight, OverlayTexture.NO_OVERLAY,
                    64, 64
            );
        }
    }
}