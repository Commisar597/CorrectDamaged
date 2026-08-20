package com.KC.correctdamaged.client.render.torso;

import com.KC.correctdamaged.CorrectDamaged;
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
                        new CubeDeformation(-0.21F)), PartPose.ZERO);

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

    public void renderMuscles(PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(MUSCLE));
        this.bodyMuscle.render(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }
}