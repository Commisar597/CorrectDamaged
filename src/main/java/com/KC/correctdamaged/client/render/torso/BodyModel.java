package com.KC.correctdamaged.client.render.torso;

import com.KC.correctdamaged.CorrectDamaged;
import com.KC.correctdamaged.client.render.customRender.CubeUV;
import com.KC.correctdamaged.client.render.customRender.FreeUVCubeRenderer.FaceUV;
import com.KC.correctdamaged.client.render.octantRender.OctantRenderHelper;
import com.KC.correctdamaged.client.render.torso.TorsoGridRenderHelper;
import com.KC.correctdamaged.client.render.octantRender.psevdo.TorsoGridSplitter;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class BodyModel extends PlayerModel<AbstractClientPlayer> {

    public static final ResourceLocation BONE = new ResourceLocation(CorrectDamaged.MODID,
            "textures/entity/bone_texture.png");
    public static final ResourceLocation BURNT_BONE = new ResourceLocation(CorrectDamaged.MODID,
            "textures/entity/burnt_bone_texture.png");
    public static final ResourceLocation MUSCLE = new ResourceLocation(CorrectDamaged.MODID,
            "textures/entity/muscles_texture.png");

    public final ModelPart skeleton;
    public final ModelPart bodyMuscle;

    public BodyModel(ModelPart root) {
        super(root, false);
        ModelPart body = root.getChild("body");
        this.skeleton = body.getChild("skeleton");
        this.bodyMuscle = body.getChild("bodyMuscle");
    }


    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = PlayerModel.createMesh(CubeDeformation.NONE, false);
        PartDefinition root = meshdefinition.getRoot();

        PartDefinition body = root.getChild("body");

        body.addOrReplaceChild("skeleton",
                CubeListBuilder.create().texOffs(40, 0).addBox(-4.0F,
                        0.0F, -2.0F, 8.0F, 12.0F, 4.0F,
                        new CubeDeformation(-0.15F)), PartPose.ZERO);

        body.addOrReplaceChild("bodyMuscle",
                CubeListBuilder.create().texOffs(40, 0).addBox(-4.0F,
                        0.0F, -2.0F, 8.0F, 12.0F, 4.0F,
                        new CubeDeformation(-0.125F)), PartPose.ZERO);

        body.addOrReplaceChild("jacket",
                CubeListBuilder.create().texOffs(40, 0).addBox(-4.0F,
                        0.0F, -2.0F, 8.0F, 12.0F, 4.0F,
                        new CubeDeformation(0.125F)), PartPose.ZERO);

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(AbstractClientPlayer entity, float limbSwing, float limbSwingAmount,
                          float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }

    public void renderSkeleton(PoseStack poseStack, MultiBufferSource buffer, int packedLight, boolean isBurnt) {
        ResourceLocation tex = isBurnt ? BURNT_BONE : BONE;
        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(tex));
        this.skeleton.render(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F,
                1.0F, 1.0F, 1.0F);
    }


    public void renderOctalMuscles(PoseStack poseStack, MultiBufferSource buffer, int muscleMask, int packedLight) {
        if (muscleMask == 0) return;

        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(MUSCLE));

        poseStack.pushPose();
        this.body.translateAndRotate(poseStack);

        float def = -0.125f;
        float x0 = -4.0f - def, y0 = 0.0f - def, z0 = -2.0f - def;
        float x1 =  4.0f + def, y1 = 12.0f + def, z1 =  2.0f + def;

        for (int i = 0; i < TorsoGridSplitter.TOTAL_BLOCKS; i++) {
            if ((muscleMask & (1 << i)) == 0) continue;

            float[] bounds = TorsoGridSplitter.getBlockBounds(x0, y0, z0, x1, y1, z1, i);
            CubeUV blockUV = TorsoGridSplitter.getBlockUV(new CubeUV(
                    FaceUV.of(20F, 20F, 28F, 32F),
                    FaceUV.of(32F, 20F, 40F, 32F),
                    FaceUV.of(16F, 20F, 20F, 32F),
                    FaceUV.of(28F, 20F, 32F, 32F),
                    FaceUV.of(20F, 16F, 28F, 20F),
                    FaceUV.of(28F, 16F, 36F, 20F)
            ), i);

            TorsoGridRenderHelper.renderSegmentBlock(
                    poseStack.last(), consumer,
                    muscleMask, i, bounds,
                    blockUV, packedLight, OverlayTexture.NO_OVERLAY, 64, 64
            );
        }

        poseStack.popPose();
    }

    public void renderOctalJacket(PoseStack poseStack, VertexConsumer consumer, int jacketMask, int packedLight) {
        if (jacketMask == 0) return;

        poseStack.pushPose();
        this.body.translateAndRotate(poseStack);

        float extra = 0.25f;
        float x0 = -4.0f - extra, y0 = 0.0f - extra, z0 = -2.0f - extra;
        float x1 =  4.0f + extra, y1 = 12.0f + extra, z1 =  2.0f + extra;

        for (int i = 0; i < TorsoGridSplitter.TOTAL_BLOCKS; i++) {
            if ((jacketMask & (1 << i)) == 0) continue;

            float[] bounds = TorsoGridSplitter.getBlockBounds(x0, y0, z0, x1, y1, z1, i);
            CubeUV blockUV = TorsoGridSplitter.getBlockUV(new CubeUV(
                    FaceUV.of(20F, 36F, 28F, 48F),
                    FaceUV.of(32F, 36F, 40F, 48F),
                    FaceUV.of(16F, 36F, 20F, 48F),
                    FaceUV.of(28F, 36F, 32F, 48F),
                    FaceUV.of(20F, 32F, 28F, 36F),
                    FaceUV.of(28F, 32F, 36F, 36F)
            ), i);

            TorsoGridRenderHelper.renderSegmentBlock(
                    poseStack.last(), consumer,
                    jacketMask, i, bounds,
                    blockUV, packedLight, OverlayTexture.NO_OVERLAY, 64, 64
            );
        }

        poseStack.popPose();
    }
}