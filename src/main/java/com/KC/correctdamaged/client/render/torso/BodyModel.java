package com.KC.correctdamaged.client.render.torso;

import com.KC.correctdamaged.CorrectDamaged;
import com.KC.correctdamaged.client.render.customRender.CubeUV;
import com.KC.correctdamaged.client.render.customRender.FreeUVCubeRenderer.FaceUV;
import com.KC.correctdamaged.client.render.octantRender.OctantRenderHelper;
import com.KC.correctdamaged.client.render.octantRender.OctreeMeshSplitter;
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

/**
 * 3D-модель внутреннего анатомического строения туловища (скелет и мышцы).
 * Зачем нужен: Хранит геометрию грудной клетки/позвоночника (skeleton) и глубокого мышечного корсета (bodyMuscle)
 * с деформациями масштаба для предотвращения Z-fighting с кожей игрока.
 */
public class BodyModel extends PlayerModel<AbstractClientPlayer> {

    public static final ResourceLocation BONE = new ResourceLocation(CorrectDamaged.MODID,
            "textures/entity/bone_texture.png");
    public static final ResourceLocation BURNT_BONE = new ResourceLocation(CorrectDamaged.MODID,
            "textures/entity/burnt_bone_texture.png");
    public static final ResourceLocation MUSCLE = new ResourceLocation(CorrectDamaged.MODID,
            "textures/entity/muscles_texture.png");

    public final ModelPart skeleton;
    public final ModelPart bodyMuscle;

    /**
     * Конструктор модели анатомии туловища.
     */
    public BodyModel(ModelPart root) {
        super(root, false);
        ModelPart body = root.getChild("body");
        this.skeleton = body.getChild("skeleton");
        this.bodyMuscle = body.getChild("bodyMuscle");
    }

    /**
     * Создает базовую геометрию и разметку слоя для скелета и мышц туловища.
     */
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

    /**
     * Выполняет отрисовку скелета туловища с учётом флага обугленности костей.
     */
    public void renderSkeleton(PoseStack poseStack, MultiBufferSource buffer, int packedLight, boolean isBurnt) {
        ResourceLocation tex = isBurnt ? BURNT_BONE : BONE;
        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(tex));
        this.skeleton.render(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F,
                1.0F, 1.0F, 1.0F);
    }

    /**
     * Выполняет отрисовку мышечного слоя туловища.
     */
    public void renderOctalMuscles(PoseStack poseStack,  MultiBufferSource buffer, byte muscleMask, int packedLight) {
        if (muscleMask == 0) return;

        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(MUSCLE));

        poseStack.pushPose();
        this.body.translateAndRotate(poseStack);

        float def = -0.125f;
        float x0 = -4.0f - def, y0 = 0.0f - def, z0 = -2.0f - def;
        float x1 =  4.0f + def, y1 = 12.0f + def, z1 =  2.0f + def;

        for (int i = 0; i < 8; i++) {
            if ((muscleMask & (1 << i)) == 0) continue;

            float[] bounds = OctreeMeshSplitter.getOctantBounds(x0, y0, z0, x1, y1, z1, i);
            CubeUV octantUV = OctreeMeshSplitter.getOctantUV(new CubeUV(
                    FaceUV.of(20F, 20F, 28F, 32F),
                    FaceUV.of(32F, 20F, 40F, 32F),
                    FaceUV.of(16F, 20F, 20F, 32F),
                    FaceUV.of(28F, 20F, 32F, 32F),
                    FaceUV.of(20F, 16F, 28F, 20F),
                    FaceUV.of(28F, 16F, 36F, 20F)
            ), i);

            OctantRenderHelper.renderOctant(
                    poseStack.last(), consumer,
                    muscleMask, i, bounds,
                    octantUV, packedLight, OverlayTexture.NO_OVERLAY, 64, 64
            );
        }

        poseStack.popPose();
    }

    public void renderOctalJacket(PoseStack poseStack, VertexConsumer consumer, byte jacketMask, int packedLight) {
        if (jacketMask == 0) return;

        poseStack.pushPose();
        this.body.translateAndRotate(poseStack);

        float extra = 0.25f;
        float x0 = -4.0f - extra, y0 = 0.0f - extra, z0 = -2.0f - extra;
        float x1 =  4.0f + extra, y1 = 12.0f + extra, z1 =  2.0f + extra;

        for (int i = 0; i < 8; i++) {
            if ((jacketMask & (1 << i)) == 0) continue;

            float[] bounds = OctreeMeshSplitter.getOctantBounds(x0, y0, z0, x1, y1, z1, i);
            CubeUV octantUV = OctreeMeshSplitter.getOctantUV(new CubeUV(
                    FaceUV.of(20F, 36F, 28F, 48F),
                    FaceUV.of(32F, 36F, 40F, 48F),
                    FaceUV.of(16F, 36F, 20F, 48F),
                    FaceUV.of(28F, 36F, 32F, 48F),
                    FaceUV.of(20F, 32F, 28F, 36F),
                    FaceUV.of(28F, 32F, 36F, 36F)
            ), i);

            OctantRenderHelper.renderOctant(
                    poseStack.last(), consumer,
                    jacketMask, i, bounds,
                    octantUV, packedLight, OverlayTexture.NO_OVERLAY, 64, 64
            );
        }

        poseStack.popPose();
    }
}