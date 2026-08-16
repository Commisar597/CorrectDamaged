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

public class PlayerMusclesModel extends PlayerModel<AbstractClientPlayer> {
    public static final ResourceLocation MUSCLE = new ResourceLocation(CorrectDamaged.MODID, "textures/entity/muscles_texture.png");

    public final ModelPart bodyMuscle;

    public final ModelPart rightFootMuscle;
    public final ModelPart rightCalfMuscle;
    public final ModelPart rightThighMuscle;

    public final ModelPart leftFootMuscle;
    public final ModelPart leftCalfMuscle;
    public final ModelPart leftThighMuscle;

    public final ModelPart rightArmWristMuscle;
    public final ModelPart rightArmForearmMuscle;
    public final ModelPart rightArmShoulderMuscle;

    public final ModelPart leftArmWristMuscle;
    public final ModelPart leftArmForearmMuscle;
    public final ModelPart leftArmShoulderMuscle;

    public PlayerMusclesModel(ModelPart root) {
        super(root, false);

        ModelPart body = root.getChild("body");
        ModelPart rightLeg = root.getChild("right_leg");
        ModelPart leftLeg = root.getChild("left_leg");
        ModelPart rightArm = root.getChild("right_arm");
        ModelPart leftArm = root.getChild("left_arm");

        this.bodyMuscle = body.getChild("bodyMuscle");

        this.rightThighMuscle = rightLeg.getChild("rightThighMuscle");
        this.rightCalfMuscle = rightLeg.getChild("rightCalfMuscle");
        this.rightFootMuscle = rightLeg.getChild("rightFootMuscle");

        this.leftThighMuscle = leftLeg.getChild("leftThighMuscle");
        this.leftCalfMuscle = leftLeg.getChild("leftCalfMuscle");
        this.leftFootMuscle = leftLeg.getChild("leftFootMuscle");

        this.rightArmShoulderMuscle = rightArm.getChild("rightArmShoulderMuscle");
        this.rightArmForearmMuscle = rightArm.getChild("rightArmForearmMuscle");
        this.rightArmWristMuscle = rightArm.getChild("rightArmWristMuscle");

        this.leftArmShoulderMuscle = leftArm.getChild("leftArmShoulderMuscle");
        this.leftArmForearmMuscle = leftArm.getChild("leftArmForearmMuscle");
        this.leftArmWristMuscle = leftArm.getChild("leftArmWristMuscle");
    }

    public static LayerDefinition createBodyLayer(boolean slim) {
        MeshDefinition meshdefinition = PlayerModel.createMesh(CubeDeformation.NONE, slim);
        PartDefinition root = meshdefinition.getRoot();

        PartDefinition body = root.getChild("body");
        PartDefinition rightLeg = root.getChild("right_leg");
        PartDefinition leftLeg = root.getChild("left_leg");
        PartDefinition rightArm = root.getChild("right_arm");
        PartDefinition leftArm = root.getChild("left_arm");

        body.addOrReplaceChild("bodyMuscle",
                CubeListBuilder.create().texOffs(40, 0).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(-0.125F)), PartPose.ZERO);

        rightLeg.addOrReplaceChild("rightThighMuscle",
                CubeListBuilder.create().texOffs(0, 7).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 6.0F, 3.0F), PartPose.ZERO);
        rightLeg.addOrReplaceChild("rightCalfMuscle",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 6.0F, -1.5F, 3.0F, 4.0F, 3.0F), PartPose.ZERO);
        rightLeg.addOrReplaceChild("rightFootMuscle",
                CubeListBuilder.create().texOffs(0, 16).addBox(-1.5F, 10.0F, -1.5F, 3.0F, 2.0F, 3.0F), PartPose.ZERO);

        leftLeg.addOrReplaceChild("leftThighMuscle",
                CubeListBuilder.create().texOffs(0, 7).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 6.0F, 3.0F), PartPose.ZERO);
        leftLeg.addOrReplaceChild("leftCalfMuscle",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 6.0F, -1.5F, 3.0F, 4.0F, 3.0F), PartPose.ZERO);
        leftLeg.addOrReplaceChild("leftFootMuscle",
                CubeListBuilder.create().texOffs(0, 16).addBox(-1.5F, 10.0F, -1.5F, 3.0F, 2.0F, 3.0F), PartPose.ZERO);

        rightArm.addOrReplaceChild("rightArmShoulderMuscle",
                CubeListBuilder.create().texOffs(0, 7).addBox(-1.5F, -2.0F, -1.5F, 3.0F, 6.0F, 3.0F), PartPose.offset(-0.3F, 0.0F, 0.0F));
        rightArm.addOrReplaceChild("rightArmForearmMuscle",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 4.0F, -1.5F, 3.0F, 4.0F, 3.0F), PartPose.offset(-0.3F, 0.0F, 0.0F));
        rightArm.addOrReplaceChild("rightArmWristMuscle",
                CubeListBuilder.create().texOffs(0, 16).addBox(-1.5F, 8.0F, -1.5F, 3.0F, 2.0F, 3.0F), PartPose.offset(-0.3F, 0.0F, 0.0F));

        leftArm.addOrReplaceChild("leftArmShoulderMuscle",
                CubeListBuilder.create().texOffs(0, 7).mirror().addBox(-1.5F, -2.0F, -1.5F, 3.0F, 6.0F, 3.0F), PartPose.offset(0.3F, 0.0F, 0.0F));
        leftArm.addOrReplaceChild("leftArmForearmMuscle",
                CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.5F, 4.0F, -1.5F, 3.0F, 4.0F, 3.0F), PartPose.offset(0.3F, 0.0F, 0.0F));
        leftArm.addOrReplaceChild("leftArmWristMuscle",
                CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-1.5F, 8.0F, -1.5F, 3.0F, 2.0F, 3.0F), PartPose.offset(0.3F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(AbstractClientPlayer entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        setAllPartsVisible(true);
    }

    public void setAllPartsVisible(boolean visible) {
        this.bodyMuscle.visible = visible;

        this.rightFootMuscle.visible = visible;
        this.rightCalfMuscle.visible = visible;
        this.rightThighMuscle.visible = visible;
        this.leftFootMuscle.visible = visible;
        this.leftCalfMuscle.visible = visible;
        this.leftThighMuscle.visible = visible;

        this.rightArmWristMuscle.visible = visible;
        this.rightArmForearmMuscle.visible = visible;
        this.rightArmShoulderMuscle.visible = visible;
        this.leftArmWristMuscle.visible = visible;
        this.leftArmForearmMuscle.visible = visible;
        this.leftArmShoulderMuscle.visible = visible;
    }
}