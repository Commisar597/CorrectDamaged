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

public class PlayerMusclesModel extends PlayerModel<AbstractClientPlayer> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            new ResourceLocation("correct_damaged", "player_muscles"), "main");
    public static final ModelLayerLocation SLIM_LAYER_LOCATION = new ModelLayerLocation(
            new ResourceLocation("correct_damaged", "player_muscles_slim"), "main");

    public static final ResourceLocation TEXTURE_SHOULDER = new ResourceLocation("correct_damaged", "textures/entity/muscles_shoulder_texture_2.png");
    public static final ResourceLocation TEXTURE_FOREARM = new ResourceLocation("correct_damaged", "textures/entity/muscles_forearm_texture_2.png");
    public static final ResourceLocation TEXTURE_WRIST = new ResourceLocation("correct_damaged", "textures/entity/muscles_wrist_texture_2.png");

    private final ModelPart rightFoot;
    private final ModelPart rightCalf;
    private final ModelPart rightThigh;
    private final ModelPart leftFoot;
    private final ModelPart leftCalf;
    private final ModelPart leftThigh;

    private final ModelPart rightArmWrist;
    private final ModelPart rightArmForearm;
    private final ModelPart rightArmShoulder;
    private final ModelPart leftArmWrist;
    private final ModelPart leftArmForearm;
    private final ModelPart leftArmShoulder;

    public PlayerMusclesModel(ModelPart root, boolean slim) {
        super(root, slim);

        this.rightFoot = root.getChild("rightFoot");
        this.rightCalf = root.getChild("rightCalf");
        this.rightThigh = root.getChild("rightThigh");
        this.leftFoot = root.getChild("leftFoot");
        this.leftCalf = root.getChild("leftCalf");
        this.leftThigh = root.getChild("leftThigh");

        this.rightArmWrist = root.getChild("rightArmWrist");
        this.rightArmForearm = root.getChild("rightArmForearm");
        this.rightArmShoulder = root.getChild("rightArmShoulder");
        this.leftArmWrist = root.getChild("leftArmWrist");
        this.leftArmForearm = root.getChild("leftArmForearm");
        this.leftArmShoulder = root.getChild("leftArmShoulder");
    }

    public static LayerDefinition createBodyLayer(boolean slim) {
        MeshDefinition meshdefinition = PlayerModel.createMesh(CubeDeformation.NONE, slim);
        PartDefinition partdefinition = meshdefinition.getRoot();

        float armOffsetX = slim ? 0.5F : 0.0F;

        // Ноги (Соответствие: Thigh -> Shoulder, Calf -> Forearm, Foot -> Wrist)
        partdefinition.addOrReplaceChild("rightThigh",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -6.0F, -2.0F, 3.0F, 6.0F, 3.0F),
                PartPose.offset(1.5F, 18.0F, 0.5F));
        partdefinition.addOrReplaceChild("rightCalf",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -6.0F, -2.0F, 3.0F, 4.0F, 3.0F),
                PartPose.offset(1.5F, 24.0F, 0.5F));
        partdefinition.addOrReplaceChild("rightFoot",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -4.0F, -2.0F, 3.0F, 2.0F, 3.0F),
                PartPose.offset(1.5F, 26.0F, 0.5F));

        partdefinition.addOrReplaceChild("leftThigh",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -6.0F, -2.0F, 3.0F, 6.0F, 3.0F),
                PartPose.offset(-2.5F, 18.0F, 0.5F));
        partdefinition.addOrReplaceChild("leftCalf",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -6.0F, -2.0F, 3.0F, 4.0F, 3.0F),
                PartPose.offset(-2.5F, 24.0F, 0.5F));
        partdefinition.addOrReplaceChild("leftFoot",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -4.0F, -2.0F, 3.0F, 2.0F, 3.0F),
                PartPose.offset(-2.5F, 26.0F, 0.5F));

        // Руки
        partdefinition.addOrReplaceChild("rightArmShoulder",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -6.0F, -2.0F, 3.0F, 6.0F, 3.0F),
                PartPose.offset(5.5F - armOffsetX, 6.0F, 0.5F));
        partdefinition.addOrReplaceChild("rightArmForearm",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -6.0F, -2.0F, 3.0F, 4.0F, 3.0F),
                PartPose.offset(5.5F - armOffsetX, 12.0F, 0.5F));
        partdefinition.addOrReplaceChild("rightArmWrist",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -4.0F, -2.0F, 3.0F, 2.0F, 3.0F),
                PartPose.offset(5.5F - armOffsetX, 14.0F, 0.5F));

        partdefinition.addOrReplaceChild("leftArmShoulder",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -6.0F, -2.0F, 3.0F, 6.0F, 3.0F),
                PartPose.offset(-5.5F + armOffsetX, 6.0F, 0.5F));
        partdefinition.addOrReplaceChild("leftArmForearm",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -6.0F, -2.0F, 3.0F, 4.0F, 3.0F),
                PartPose.offset(-5.5F + armOffsetX, 12.0F, 0.5F));
        partdefinition.addOrReplaceChild("leftArmWrist",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -4.0F, -2.0F, 3.0F, 2.0F, 3.0F),
                PartPose.offset(-5.5F + armOffsetX, 14.0F, 0.5F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(AbstractClientPlayer entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        this.rightFoot.copyFrom(this.rightLeg);
        this.rightCalf.copyFrom(this.rightLeg);
        this.rightThigh.copyFrom(this.rightLeg);

        this.leftFoot.copyFrom(this.leftLeg);
        this.leftCalf.copyFrom(this.leftLeg);
        this.leftThigh.copyFrom(this.leftLeg);

        this.rightArmWrist.copyFrom(this.rightArm);
        this.rightArmForearm.copyFrom(this.rightArm);
        this.rightArmShoulder.copyFrom(this.rightArm);

        this.leftArmWrist.copyFrom(this.leftArm);
        this.leftArmForearm.copyFrom(this.leftArm);
        this.leftArmShoulder.copyFrom(this.leftArm);
    }

    public void renderWithTextures(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        VertexConsumer shoulderConsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(TEXTURE_SHOULDER));
        VertexConsumer forearmConsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(TEXTURE_FOREARM));
        VertexConsumer wristConsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(TEXTURE_WRIST));

        rightThigh.render(poseStack, shoulderConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        leftThigh.render(poseStack, shoulderConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        rightArmShoulder.render(poseStack, shoulderConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        leftArmShoulder.render(poseStack, shoulderConsumer, packedLight, packedOverlay, red, green, blue, alpha);

        rightCalf.render(poseStack, forearmConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        leftCalf.render(poseStack, forearmConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        rightArmForearm.render(poseStack, forearmConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        leftArmForearm.render(poseStack, forearmConsumer, packedLight, packedOverlay, red, green, blue, alpha);

        rightFoot.render(poseStack, wristConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        leftFoot.render(poseStack, wristConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        rightArmWrist.render(poseStack, wristConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        leftArmWrist.render(poseStack, wristConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        rightThigh.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        leftThigh.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        rightArmShoulder.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        leftArmShoulder.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);

        rightCalf.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        leftCalf.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        rightArmForearm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        leftArmForearm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);

        rightFoot.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        leftFoot.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        rightArmWrist.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        leftArmWrist.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}