package com.KC.correctdamaged.client.render;

import com.KC.correctdamaged.CorrectDamaged;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;

public class PlayerBonesModel extends PlayerModel<AbstractClientPlayer> {
    public static final ResourceLocation BONE = new ResourceLocation(CorrectDamaged.MODID, "textures/entity/bone_texture.png");
    public static final ResourceLocation BURNT_BONE = new ResourceLocation(CorrectDamaged.MODID, "textures/entity/burnt_bone_texture.png");

    public final ModelPart rightFootBone;
    public final ModelPart rightCalfBone;
    public final ModelPart rightThighBone;

    public final ModelPart leftFootBone;
    public final ModelPart leftCalfBone;
    public final ModelPart leftThighBone;

    public final ModelPart rightArmWristBone;
    public final ModelPart rightArmForearmBone;
    public final ModelPart rightArmShoulderBone;

    public final ModelPart leftArmWristBone;
    public final ModelPart leftArmForearmBone;
    public final ModelPart leftArmShoulderBone;

    public final ModelPart skeleton;

    public PlayerBonesModel(ModelPart root) {
        super(root, false);

        ModelPart body = root.getChild("body");
        ModelPart rightLeg = root.getChild("right_leg");
        ModelPart leftLeg = root.getChild("left_leg");
        ModelPart rightArm = root.getChild("right_arm");
        ModelPart leftArm = root.getChild("left_arm");

        this.rightThighBone = rightLeg.getChild("rightThighBone");
        this.rightCalfBone = rightLeg.getChild("rightCalfBone");
        this.rightFootBone = rightLeg.getChild("rightFootBone");

        this.leftThighBone = leftLeg.getChild("leftThighBone");
        this.leftCalfBone = leftLeg.getChild("leftCalfBone");
        this.leftFootBone = leftLeg.getChild("leftFootBone");

        this.rightArmShoulderBone = rightArm.getChild("rightArmShoulderBone");
        this.rightArmForearmBone = rightArm.getChild("rightArmForearmBone");
        this.rightArmWristBone = rightArm.getChild("rightArmWristBone");

        this.leftArmShoulderBone = leftArm.getChild("leftArmShoulderBone");
        this.leftArmForearmBone = leftArm.getChild("leftArmForearmBone");
        this.leftArmWristBone = leftArm.getChild("leftArmWristBone");

        this.skeleton = body.getChild("skeleton");
    }

    public static LayerDefinition createBodyLayer(boolean slim) {
        MeshDefinition meshdefinition = PlayerModel.createMesh(CubeDeformation.NONE, slim);
        PartDefinition root = meshdefinition.getRoot();

        PartDefinition body = root.getChild("body");
        PartDefinition rightLeg = root.getChild("right_leg");
        PartDefinition leftLeg = root.getChild("left_leg");
        PartDefinition rightArm = root.getChild("right_arm");
        PartDefinition leftArm = root.getChild("left_arm");

        rightLeg.addOrReplaceChild("rightThighBone",
                CubeListBuilder.create().texOffs(0, 6).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F), PartPose.ZERO);
        rightLeg.addOrReplaceChild("rightCalfBone",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 6.0F, -1.0F, 2.0F, 4.0F, 2.0F), PartPose.ZERO);
        rightLeg.addOrReplaceChild("rightFootBone",
                CubeListBuilder.create().texOffs(0, 14).addBox(-1.0F, 10.0F, -1.0F, 2.0F, 2.0F, 2.0F), PartPose.ZERO);

        leftLeg.addOrReplaceChild("leftThighBone",
                CubeListBuilder.create().texOffs(0, 6).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F), PartPose.ZERO);
        leftLeg.addOrReplaceChild("leftCalfBone",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 6.0F, -1.0F, 2.0F, 4.0F, 2.0F), PartPose.ZERO);
        leftLeg.addOrReplaceChild("leftFootBone",
                CubeListBuilder.create().texOffs(0, 14).addBox(-1.0F, 10.0F, -1.0F, 2.0F, 2.0F, 2.0F), PartPose.ZERO);

        rightArm.addOrReplaceChild("rightArmShoulderBone",
                CubeListBuilder.create().texOffs(0, 6).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 6.0F, 2.0F), PartPose.offset(-0.1F, 0.0F, 0.0F));
        rightArm.addOrReplaceChild("rightArmForearmBone",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 4.0F, -1.0F, 2.0F, 4.0F, 2.0F), PartPose.offset(-0.1F, 0.0F, 0.0F));
        rightArm.addOrReplaceChild("rightArmWristBone",
                CubeListBuilder.create().texOffs(0, 14).addBox(-1.0F, 8.0F, -1.0F, 2.0F, 2.0F, 2.0F), PartPose.offset(-0.1F, 0.0F, 0.0F));

        leftArm.addOrReplaceChild("leftArmShoulderBone",
                CubeListBuilder.create().texOffs(0, 6).mirror().addBox(-1.0F, -2.0F, -1.0F, 2.0F, 6.0F, 2.0F), PartPose.offset(0.1F, 0.0F, 0.0F));
        leftArm.addOrReplaceChild("leftArmForearmBone",
                CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, 4.0F, -1.0F, 2.0F, 4.0F, 2.0F), PartPose.offset(0.1F, 0.0F, 0.0F));
        leftArm.addOrReplaceChild("leftArmWristBone",
                CubeListBuilder.create().texOffs(0, 14).mirror().addBox(-1.0F, 8.0F, -1.0F, 2.0F, 2.0F, 2.0F), PartPose.offset(0.1F, 0.0F, 0.0F));

        body.addOrReplaceChild("skeleton",
                CubeListBuilder.create().texOffs(40, 0).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(-0.250F)), PartPose.ZERO);

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(AbstractClientPlayer entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        setAllPartsVisible(true);
    }

    public void setAllPartsVisible(boolean visible) {
        this.rightFootBone.visible = visible;
        this.rightCalfBone.visible = visible;
        this.rightThighBone.visible = visible;
        this.leftFootBone.visible = visible;
        this.leftCalfBone.visible = visible;
        this.leftThighBone.visible = visible;

        this.rightArmWristBone.visible = visible;
        this.rightArmForearmBone.visible = visible;
        this.rightArmShoulderBone.visible = visible;
        this.leftArmWristBone.visible = visible;
        this.leftArmForearmBone.visible = visible;
        this.leftArmShoulderBone.visible = visible;

        this.skeleton.visible = visible;
    }
}