package com.KC.correctdamaged.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class PlayerBonesModel extends PlayerModel<AbstractClientPlayer> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            new ResourceLocation("correct_damaged", "player_bones"), "main");
    public static final ModelLayerLocation SLIM_LAYER_LOCATION = new ModelLayerLocation(
            new ResourceLocation("correct_damaged", "player_bones_slim"), "main");

    public static final ResourceLocation BONE_SHOULDER = new ResourceLocation("correct_damaged", "textures/entity/bone_shoulder_texture_2.png");
    public static final ResourceLocation BONE_FOREARM = new ResourceLocation("correct_damaged", "textures/entity/bone_forearm_texture_2.png");
    public static final ResourceLocation BONE_WRIST = new ResourceLocation("correct_damaged", "textures/entity/bone_wrist_texture_2.png");

    public static final ResourceLocation BURNT_BONE_SHOULDER = new ResourceLocation("correct_damaged", "textures/entity/burnt_bone_shoulder_texture_2.png");
    public static final ResourceLocation BURNT_BONE_FOREARM = new ResourceLocation("correct_damaged", "textures/entity/burnt_bone_forearm_texture_2.png");
    public static final ResourceLocation BURNT_BONE_WRIST = new ResourceLocation("correct_damaged", "textures/entity/burnt_bone_wrist_texture_2.png");

    private final ModelPart rightFootBone;
    private final ModelPart rightCalfBone;
    private final ModelPart rightThighBone;
    private final ModelPart leftFootBone;
    private final ModelPart leftCalfBone;
    private final ModelPart leftThighBone;

    private final ModelPart rightArmWristBone;
    private final ModelPart rightArmForearmBone;
    private final ModelPart rightArmShoulderBone;
    private final ModelPart leftArmWristBone;
    private final ModelPart leftArmForearmBone;
    private final ModelPart leftArmShoulderBone;

    public PlayerBonesModel(ModelPart root, boolean slim) {
        super(root, slim);

        this.rightFootBone = root.getChild("rightFootBone");
        this.rightCalfBone = root.getChild("rightCalfBone");
        this.rightThighBone = root.getChild("rightThighBone");
        this.leftFootBone = root.getChild("leftFootBone");
        this.leftCalfBone = root.getChild("leftCalfBone");
        this.leftThighBone = root.getChild("leftThighBone");

        this.rightArmWristBone = root.getChild("rightArmWristBone");
        this.rightArmForearmBone = root.getChild("rightArmForearmBone");
        this.rightArmShoulderBone = root.getChild("rightArmShoulderBone");
        this.leftArmWristBone = root.getChild("leftArmWristBone");
        this.leftArmForearmBone = root.getChild("leftArmForearmBone");
        this.leftArmShoulderBone = root.getChild("leftArmShoulderBone");
    }

    public static LayerDefinition createBodyLayer(boolean slim) {
        MeshDefinition meshdefinition = PlayerModel.createMesh(CubeDeformation.NONE, slim);
        PartDefinition partdefinition = meshdefinition.getRoot();

        float armOffsetX = slim ? 0.5F : 0.0F;

        partdefinition.addOrReplaceChild("rightThighBone",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -12.0F, -1.0F, 2.0F, 6.0F, 2.0F),
                PartPose.offset(2.0F, 24.0F, 0.0F));
        partdefinition.addOrReplaceChild("rightCalfBone",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 4.0F, 2.0F),
                PartPose.offset(2.0F, 24.0F, 0.0F));
        partdefinition.addOrReplaceChild("rightFootBone",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(2.0F, 24.0F, 0.0F));

        partdefinition.addOrReplaceChild("leftThighBone",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -12.0F, -1.0F, 2.0F, 6.0F, 2.0F),
                PartPose.offset(-2.0F, 24.0F, 0.0F));
        partdefinition.addOrReplaceChild("leftCalfBone",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 4.0F, 2.0F),
                PartPose.offset(-2.0F, 24.0F, 0.0F));
        partdefinition.addOrReplaceChild("leftFootBone",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(-2.0F, 24.0F, 0.0F));

        partdefinition.addOrReplaceChild("rightArmShoulderBone",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -10.0F, -1.0F, 2.0F, 6.0F, 2.0F),
                PartPose.offset(5.0F - armOffsetX, 12.0F, 0.0F));
        partdefinition.addOrReplaceChild("rightArmForearmBone",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F),
                PartPose.offset(5.0F - armOffsetX, 12.0F, 0.0F));
        partdefinition.addOrReplaceChild("rightArmWristBone",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(5.0F - armOffsetX, 12.0F, 0.0F));

        partdefinition.addOrReplaceChild("leftArmShoulderBone",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -10.0F, -1.0F, 2.0F, 6.0F, 2.0F),
                PartPose.offset(-5.0F + armOffsetX, 12.0F, 0.0F));
        partdefinition.addOrReplaceChild("leftArmForearmBone",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F),
                PartPose.offset(-5.0F + armOffsetX, 12.0F, 0.0F));
        partdefinition.addOrReplaceChild("leftArmWristBone",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(-5.0F + armOffsetX, 12.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(AbstractClientPlayer entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        this.rightFootBone.copyFrom(this.rightLeg);
        this.rightCalfBone.copyFrom(this.rightLeg);
        this.rightThighBone.copyFrom(this.rightLeg);

        this.leftFootBone.copyFrom(this.leftLeg);
        this.leftCalfBone.copyFrom(this.leftLeg);
        this.leftThighBone.copyFrom(this.leftLeg);

        this.rightArmWristBone.copyFrom(this.rightArm);
        this.rightArmForearmBone.copyFrom(this.rightArm);
        this.rightArmShoulderBone.copyFrom(this.rightArm);

        this.leftArmWristBone.copyFrom(this.leftArm);
        this.leftArmForearmBone.copyFrom(this.leftArm);
        this.leftArmShoulderBone.copyFrom(this.leftArm);
    }

    public void renderWithTextures(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, float red, float green, float blue, float alpha, boolean isBurnt) {
        ResourceLocation shoulderTex = isBurnt ? BURNT_BONE_SHOULDER : BONE_SHOULDER;
        ResourceLocation forearmTex  = isBurnt ? BURNT_BONE_FOREARM  : BONE_FOREARM;
        ResourceLocation wristTex    = isBurnt ? BURNT_BONE_WRIST    : BONE_WRIST;

        VertexConsumer shoulderConsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(shoulderTex));
        VertexConsumer forearmConsumer  = bufferSource.getBuffer(RenderType.entityCutoutNoCull(forearmTex));
        VertexConsumer wristConsumer    = bufferSource.getBuffer(RenderType.entityCutoutNoCull(wristTex));

        rightThighBone.render(poseStack, shoulderConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        leftThighBone.render(poseStack, shoulderConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        rightArmShoulderBone.render(poseStack, shoulderConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        leftArmShoulderBone.render(poseStack, shoulderConsumer, packedLight, packedOverlay, red, green, blue, alpha);

        rightCalfBone.render(poseStack, forearmConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        leftCalfBone.render(poseStack, forearmConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        rightArmForearmBone.render(poseStack, forearmConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        leftArmForearmBone.render(poseStack, forearmConsumer, packedLight, packedOverlay, red, green, blue, alpha);

        rightFootBone.render(poseStack, wristConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        leftFootBone.render(poseStack, wristConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        rightArmWristBone.render(poseStack, wristConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        leftArmWristBone.render(poseStack, wristConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        rightThighBone.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        leftThighBone.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        rightArmShoulderBone.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        leftArmShoulderBone.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);

        rightCalfBone.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        leftCalfBone.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        rightArmForearmBone.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        leftArmForearmBone.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);

        rightFootBone.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        leftFootBone.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        rightArmWristBone.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        leftArmWristBone.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}