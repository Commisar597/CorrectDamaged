package com.KC.correctdamaged.client.render;

import com.KC.correctdamaged.capability.visual.ArmData;
import com.KC.correctdamaged.capability.visual.LegData;
import com.KC.correctdamaged.capability.LimbManager;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class StumpLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    private final ModelPart cap4x4;
    private final ModelPart cap3x4;

    public StumpLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent) {
        super(parent);

        this.cap4x4 = createCap(-2.0F, -2.0F, 4.0F, 4.0F, 0, 0, 4, 4);
        this.cap3x4 = createCap(-1.5F, -2.0F, 3.0F, 4.0F, 2, 0, 3, 4);
    }

    private ModelPart createCap(float x, float z, float width, float depth, int texU, int texV, int texW, int texH) {
        MeshDefinition mesh = new MeshDefinition();
        mesh.getRoot().addOrReplaceChild("cap",
                CubeListBuilder.create()
                        .texOffs(texU, texV)
                        .addBox(x, 0.0F, z, width, 0.001F, depth),
                PartPose.ZERO);
        return LayerDefinition.create(mesh, texW, texH).bakeRoot().getChild("cap");
    }

    @Override
    public void render(
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            AbstractClientPlayer player,
            float limbSwing,
            float limbSwingAmount,
            float partialTicks,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {
        player.getCapability(LimbManager.LIMB_DATA_CAP).ifPresent(data -> {
            boolean isSlim = player.getModelName().equals("slim");

            ArmData rightArm = data.getRightArm();
            ArmData leftArm = data.getLeftArm();
            LegData rightLeg = data.getRightLeg();
            LegData leftLeg = data.getLeftLeg();

            renderArmStumps(poseStack, buffer, packedLight, player, getParentModel().rightArm, rightArm, isSlim, 0, StumpTextureResolver.LimbType.RIGHT_ARM);
            renderArmStumps(poseStack, buffer, packedLight, player, getParentModel().leftArm, leftArm, isSlim, 1, StumpTextureResolver.LimbType.LEFT_ARM);

            renderLegStumps(poseStack, buffer, packedLight, player, getParentModel().rightLeg, rightLeg, 2, StumpTextureResolver.LimbType.RIGHT_LEG);
            renderLegStumps(poseStack, buffer, packedLight, player, getParentModel().leftLeg, leftLeg, 3, StumpTextureResolver.LimbType.LEFT_LEG);
        });
    }

    private void renderArmStumps(
            PoseStack poseStack, MultiBufferSource buffer, int packedLight, AbstractClientPlayer player,
            ModelPart parentLimb, ArmData arm, boolean isSlim, int limbId, StumpTextureResolver.LimbType limbType
    ) {
        boolean s = arm.hasShoulderSkin();
        boolean f = arm.hasForearmSkin();
        boolean w = arm.hasWristSkin();

        if (s && f && w) return;

        if (!s) {
            renderCapAtPosition(poseStack, buffer, packedLight, player, parentLimb, cap4x4, "stump_fresh_4x4", 0.0F, 0.0F, limbId, false, limbType, true);
        }

        ModelPart cap = isSlim ? cap3x4 : cap4x4;
        String texName = isSlim ? "stump_fresh_4x3" : "stump_fresh_4x4";
        float centerX = isSlim ? (limbId == 0 ? -0.5F : 0.5F) : -1.0F;

        if (s != f) {
            renderCapAtPosition(poseStack, buffer, packedLight, player, parentLimb, cap, texName, 4.0F, centerX, limbId, isSlim, limbType, false);
        }

        if (f != w) {
            renderCapAtPosition(poseStack, buffer, packedLight, player, parentLimb, cap, texName, 8.0F, centerX, limbId, isSlim, limbType, false);
        }
    }

    private void renderLegStumps(
            PoseStack poseStack, MultiBufferSource buffer, int packedLight, AbstractClientPlayer player,
            ModelPart parentLimb, LegData leg, int limbId, StumpTextureResolver.LimbType limbType
    ) {
        boolean t = leg.hasThighSkin();
        boolean c = leg.hasCalfSkin();
        boolean f = leg.hasFootSkin();

        if (t && c && f) return;

        ModelPart cap = cap4x4;
        String texName = "stump_fresh_4x4";

        if (!t) {
            renderCapAtPosition(poseStack, buffer, packedLight, player, parentLimb, cap, texName, 0.0F, 0.0F, limbId, false, limbType, true);
        }

        if (t != c) {
            renderCapAtPosition(poseStack, buffer, packedLight, player, parentLimb, cap, texName, 6.0F, 0.0F, limbId, false, limbType, false);
        }

        if (c != f) {
            renderCapAtPosition(poseStack, buffer, packedLight, player, parentLimb, cap, texName, 10.0F, 0.0F, limbId, false, limbType, false);
        }
    }

    private void renderCapAtPosition(
            PoseStack poseStack, MultiBufferSource buffer, int packedLight, AbstractClientPlayer player,
            ModelPart parentLimb, ModelPart capModel, String baseTexName, float yOffset, float centerX,
            int limbId, boolean isSlim, StumpTextureResolver.LimbType limbType, boolean isRoot
    ) {
        poseStack.pushPose();

        if (isRoot) {
            getParentModel().body.translateAndRotate(poseStack);
            if (limbId == 0 || limbId == 1) {
                float armX = (limbId == 0) ? -4.01F : 4.01F;
                poseStack.translate(armX / 16.0D, 2.0D / 16.0D, 0.0D);
                poseStack.mulPose(Axis.ZP.rotationDegrees(limbId == 0 ? -90.0F : 90.0F));
            } else {
                float legX = (limbId == 2) ? -2.0F : 2.0F;
                poseStack.translate(legX / 16.0D, 12.01D / 16.0D, 0.0D);
            }
        } else {
            parentLimb.translateAndRotate(poseStack);
            poseStack.translate(centerX / 16.0D, yOffset / 16.0D, 0.0D);
        }

        float angle = getRotationAngle(player, limbId, isSlim);
        if (angle != 0.0F) {
            poseStack.mulPose(Axis.YP.rotationDegrees(angle));
        }

        ResourceLocation texture = StumpTextureResolver.getStumpTexture(player, baseTexName, limbType);
        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(texture));
        capModel.render(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        poseStack.popPose();
    }

    private float getRotationAngle(AbstractClientPlayer player, int limbId, boolean isSlim) {
        long uuidHash = player.getUUID().getLeastSignificantBits() ^ player.getUUID().getMostSignificantBits();
        int seed = Math.abs((int) (uuidHash ^ (limbId * 31L)));

        if (isSlim && (limbId == 0 || limbId == 1)) {
            return (seed % 2) * 180.0F;
        } else {
            return (seed % 4) * 90.0F;
        }
    }
}