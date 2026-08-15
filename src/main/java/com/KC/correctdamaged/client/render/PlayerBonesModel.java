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

    public PlayerBonesModel(ModelPart root) {
        super(root, false);
        this.rightThighBone = root.getChild("rightThighBone");
        this.rightCalfBone = root.getChild("rightCalfBone");
        this.rightFootBone = root.getChild("rightFootBone");

        this.leftThighBone = root.getChild("leftThighBone");
        this.leftCalfBone = root.getChild("leftCalfBone");
        this.leftFootBone = root.getChild("leftFootBone");

        this.rightArmShoulderBone = root.getChild("rightArmShoulderBone");
        this.rightArmForearmBone = root.getChild("rightArmForearmBone");
        this.rightArmWristBone = root.getChild("rightArmWristBone");

        this.leftArmShoulderBone = root.getChild("leftArmShoulderBone");
        this.leftArmForearmBone = root.getChild("leftArmForearmBone");
        this.leftArmWristBone = root.getChild("leftArmWristBone");
    }

    public static LayerDefinition createBodyLayer(boolean slim) {
        MeshDefinition meshdefinition = PlayerModel.createMesh(CubeDeformation.NONE, slim);
        PartDefinition partdefinition = meshdefinition.getRoot();

        // --- НОГИ ---
        partdefinition.addOrReplaceChild("rightThighBone",
                CubeListBuilder.create().texOffs(0, 6).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F), PartPose.ZERO);
        partdefinition.addOrReplaceChild("rightCalfBone",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 6.0F, -1.0F, 2.0F, 4.0F, 2.0F), PartPose.ZERO);
        partdefinition.addOrReplaceChild("rightFootBone",
                CubeListBuilder.create().texOffs(0, 14).addBox(-1.0F, 10.0F, -1.0F, 2.0F, 2.0F, 2.0F), PartPose.ZERO);

        partdefinition.addOrReplaceChild("leftThighBone",
                CubeListBuilder.create().texOffs(0, 6).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F), PartPose.ZERO);
        partdefinition.addOrReplaceChild("leftCalfBone",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 6.0F, -1.0F, 2.0F, 4.0F, 2.0F), PartPose.ZERO);
        partdefinition.addOrReplaceChild("leftFootBone",
                CubeListBuilder.create().texOffs(0, 14).addBox(-1.0F, 10.0F, -1.0F, 2.0F, 2.0F, 2.0F), PartPose.ZERO);

        // --- РУКИ ---
        partdefinition.addOrReplaceChild("rightArmShoulderBone",
                CubeListBuilder.create().texOffs(0, 6).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 6.0F, 2.0F), PartPose.offset(-0.1F, 0.0F, 0.0F));
        partdefinition.addOrReplaceChild("rightArmForearmBone",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 4.0F, -1.0F, 2.0F, 4.0F, 2.0F), PartPose.offset(-0.1F, 0.0F, 0.0F));
        partdefinition.addOrReplaceChild("rightArmWristBone",
                CubeListBuilder.create().texOffs(0, 14).addBox(-1.0F, 8.0F, -1.0F, 2.0F, 2.0F, 2.0F), PartPose.offset(-0.1F, 0.0F, 0.0F));

        partdefinition.addOrReplaceChild("leftArmShoulderBone",
                CubeListBuilder.create().texOffs(0, 6).mirror().addBox(-1.0F, -2.0F, -1.0F, 2.0F, 6.0F, 2.0F), PartPose.offset(0.1F, 0.0F, 0.0F));
        partdefinition.addOrReplaceChild("leftArmForearmBone",
                CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, 4.0F, -1.0F, 2.0F, 4.0F, 2.0F), PartPose.offset(0.1F, 0.0F, 0.0F));
        partdefinition.addOrReplaceChild("leftArmWristBone",
                CubeListBuilder.create().texOffs(0, 14).mirror().addBox(-1.0F, 8.0F, -1.0F, 2.0F, 2.0F, 2.0F), PartPose.offset(0.1F, 0.0F, 0.0F));

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
    }
}