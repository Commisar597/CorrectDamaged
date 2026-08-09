package com.KC.correctdamaged.client.render;

import com.KC.correctdamaged.CorrectDamaged;
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

    private static final ResourceLocation STUMP_4x4 = new ResourceLocation(CorrectDamaged.MODID, "textures/entity/stump_fresh_4x4.png");
    private static final ResourceLocation STUMP_4x3 = new ResourceLocation(CorrectDamaged.MODID, "textures/entity/stump_fresh_4x3.png");

    private final ModelPart rightArmCap;
    private final ModelPart rightArmSlimCap;
    private final ModelPart leftArmCap;
    private final ModelPart leftArmSlimCap;
    private final ModelPart legCap;

    public StumpLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent) {
        super(parent);

        this.rightArmCap = createCap(-3.0F, -2.0F, 4.0F, 0, 0, 4, 4);
        this.leftArmCap = createCap(-1.0F, -2.0F, 4.0F, 0, 0, 4, 4);

        this.rightArmSlimCap = createCap(-2.0F, -2.0F, 3.0F, 2, 0, 3, 4);
        this.leftArmSlimCap = createCap(-1.0F, -2.0F, 3.0F, 2, 0, 3, 4);

        this.legCap = createCap(-2.0F, -2.0F, 4.0F, 0, 0, 4, 4);
    }

    private ModelPart createCap(float x, float z, float width, int texU, int texV, int texW, int texH) {
        MeshDefinition mesh = new MeshDefinition();
        mesh.getRoot().addOrReplaceChild("cap",
                CubeListBuilder.create()
                        .texOffs(texU, texV)
                        .addBox(x, 0.0F, z, width, 0.001F, 4.0F),
                PartPose.ZERO);
        return LayerDefinition.create(mesh, texW, texH).bakeRoot().getChild("cap");
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        LimbManager.get(player).ifPresent(data -> {
            boolean isSlim = player.getModelName().equals("slim");

            renderLimbCap(poseStack, buffer, packedLight, player, getParentModel().rightArm, data.getRightArm(),
                    isSlim ? rightArmSlimCap : rightArmCap, isSlim ? STUMP_4x3 : STUMP_4x4, -2.0F,
                    isSlim ? -0.5F : -1.0F, 0, isSlim);

            renderLimbCap(poseStack, buffer, packedLight, player, getParentModel().leftArm, data.getLeftArm(),
                    isSlim ? leftArmSlimCap : leftArmCap, isSlim ? STUMP_4x3 : STUMP_4x4, -2.0F,
                    isSlim ? 0.5F : 1.0F, 1, isSlim);

            renderLimbCap(poseStack, buffer, packedLight, player, getParentModel().rightLeg, data.getRightLeg(),
                    legCap, STUMP_4x4, 0.0F, 0.0F, 2, false);

            renderLimbCap(poseStack, buffer, packedLight, player, getParentModel().leftLeg, data.getLeftLeg(),
                    legCap, STUMP_4x4, 0.0F, 0.0F, 3, false);
        });
    }

    private void renderLimbCap(
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            AbstractClientPlayer player,
            ModelPart parentLimb,
            int state,
            ModelPart capModel,
            ResourceLocation texture,
            float startY,
            float centerX,
            int limbId,
            boolean isSlim
    ) {
        if (state == 3 || state == 0) return;

        float cutHeight = (state == 2) ? 10.0F : 6.0F;
        float yOffset = startY + cutHeight + 0.01F;

        poseStack.pushPose();

        parentLimb.translateAndRotate(poseStack);

        poseStack.translate(centerX / 16.0D, yOffset / 16.0D, 0.0D);

        float angle = getRotationAngle(player, limbId, isSlim);
        if (angle != 0.0F) {
            poseStack.mulPose(Axis.YP.rotationDegrees(angle));
        }

        poseStack.translate(-centerX / 16.0D, 0.0D, 0.0D);

        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(texture));
        capModel.render(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        poseStack.popPose();
    }

    private float getRotationAngle(AbstractClientPlayer player, int limbId, boolean isSlim) {
        long uuidHash = player.getUUID().getLeastSignificantBits() ^ player.getUUID().getMostSignificantBits();
        int seed = Math.abs((int) (uuidHash ^ (limbId * 31L)));

        if (isSlim && (limbId == 0 || limbId == 1)) {
            int step = seed % 2;
            return step * 180.0F;
        } else {
            int step = seed % 4;
            return step * 90.0F;
        }
    }
}