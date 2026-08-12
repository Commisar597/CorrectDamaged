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
    private static final ResourceLocation HEAD_STUMP_8x8 = new ResourceLocation(CorrectDamaged.MODID, "textures/entity/head_stump_8x8.png");

    private final ModelPart cap4x4;
    private final ModelPart cap3x4;
    private final ModelPart cap8x8;

    public StumpLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent) {
        super(parent);

        this.cap4x4 = createCap(-2.0F, -2.0F, 4.0F, 4.0F, 0, 0, 4, 4);
        this.cap3x4 = createCap(-1.5F, -2.0F, 3.0F, 4.0F, 2, 0, 3, 4);
        this.cap8x8 = createCap(-4.0F, -4.0F, 8.0F, 8.0F, 0, 0, 8, 8);
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
        LimbManager.get(player).ifPresent(data -> {
            boolean isSlim = player.getModelName().equals("slim");

            int rightArmState = data.getRightArm();
            int leftArmState = data.getLeftArm();
            int headState = data.getHeadState();

            boolean rightArmSlim = isSlim && rightArmState != 0;
            boolean leftArmSlim = isSlim && leftArmState != 0;

            // --- РУКИ И НОГИ ---
            renderLimbCap(poseStack, buffer, packedLight, player, getParentModel().rightArm, rightArmState,
                    rightArmSlim ? cap3x4 : cap4x4,
                    rightArmSlim ? STUMP_4x3 : STUMP_4x4,
                    -2.0F, rightArmSlim ? -0.5F : -1.0F, 0, rightArmSlim);

            renderLimbCap(poseStack, buffer, packedLight, player, getParentModel().leftArm, leftArmState,
                    leftArmSlim ? cap3x4 : cap4x4,
                    leftArmSlim ? STUMP_4x3 : STUMP_4x4,
                    -2.0F, leftArmSlim ? 0.5F : 1.0F, 1, leftArmSlim);

            renderLimbCap(poseStack, buffer, packedLight, player, getParentModel().rightLeg, data.getRightLeg(),
                    cap4x4, STUMP_4x4, 0.0F, 0.0F, 2, false);

            renderLimbCap(poseStack, buffer, packedLight, player, getParentModel().leftLeg, data.getLeftLeg(),
                    cap4x4, STUMP_4x4, 0.0F, 0.0F, 3, false);

            // --- ГОЛОВА ---
            renderHeadCap(poseStack, buffer, packedLight, player, headState);
        });
    }

    private void renderHeadCap(
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            AbstractClientPlayer player,
            int state
    ) {
        if (state == 5) return; // 5 = целая голова

        poseStack.pushPose();

        if (state == 0) {
            // Обезглавливание: сдвигаем чуть вверх над шеей (+0.02 пикселя)
            getParentModel().body.translateAndRotate(poseStack);
            poseStack.translate(0.0D, -0.02D / 16.0D, 0.0D);
        } else {
            getParentModel().head.translateAndRotate(poseStack);

            switch (state) {
                case 1 ->
                    // Нижняя половина головы (срез сверху на Y = -4): сдвигаем заглушку чуть ВВЕРХ (-3.98)
                        poseStack.translate(0.0D, -3.98D / 16.0D, 0.0D);
                case 2 ->
                    // Верхняя половина головы (срез снизу на Y = -4): сдвигаем заглушку чуть ВНИЗ (-4.02)
                        poseStack.translate(0.0D, -4.02D / 16.0D, 0.0D);
                case 3 -> {
                    // Вертикальный распил (срез справа X = 0): выносим заглушку на +0.02 пикселя вправо
                    poseStack.translate(0.02D / 16.0D, -4.0D / 16.0D, 0.0D);
                    poseStack.mulPose(Axis.ZP.rotationDegrees(90.0F));
                }
                case 4 -> {
                    // Вертикальный распил (срез слева X = 0): выносим заглушку на -0.02 пикселя влево
                    poseStack.translate(-0.02D / 16.0D, -4.0D / 16.0D, 0.0D);
                    poseStack.mulPose(Axis.ZP.rotationDegrees(90.0F));
                }
            }
        }

        // Случайный поворот текстуры среза (0°, 90°, 180°, 270°)
        float angle = getRotationAngle(player, 4, false);
        if (angle != 0.0F) {
            poseStack.mulPose(Axis.YP.rotationDegrees(angle));
        }

        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(HEAD_STUMP_8x8));
        cap8x8.render(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        poseStack.popPose();
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
        if (state == 3) return;

        poseStack.pushPose();

        if (state == 0) {
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
            float cutHeight = (state == 2) ? 10.0F : 6.0F;
            float yOffset = startY + cutHeight + 0.01F;

            parentLimb.translateAndRotate(poseStack);
            poseStack.translate(centerX / 16.0D, yOffset / 16.0D, 0.0D);
        }

        float angle = getRotationAngle(player, limbId, isSlim);
        if (angle != 0.0F) {
            poseStack.mulPose(Axis.YP.rotationDegrees(angle));
        }

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