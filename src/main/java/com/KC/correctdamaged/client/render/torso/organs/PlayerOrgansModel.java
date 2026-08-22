package com.KC.correctdamaged.client.render.torso.organs;

import com.KC.correctdamaged.CorrectDamaged;
import com.KC.correctdamaged.capability.visual.OrgansData;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class PlayerOrgansModel extends PlayerModel<AbstractClientPlayer> {
    public static final ResourceLocation ORGANS_TEXTURE = new ResourceLocation(CorrectDamaged.MODID,"textures/entity/organs_texture.png");

    private static final RenderType ORGANS_RENDER_TYPE = RenderType.entityCutoutNoCull(ORGANS_TEXTURE);

    public final ModelPart heart;
    public final ModelPart leftLung;
    public final ModelPart rightLung;
    public final ModelPart liver;
    public final ModelPart git;

    public PlayerOrgansModel(ModelPart root) {
        super(root, false);
        ModelPart body = root.getChild("body");
        ModelPart organs = body.getChild("organs");

        this.heart = organs.getChild("heart");
        this.leftLung = organs.getChild("left_lung");
        this.rightLung = organs.getChild("right_lung");
        this.liver = organs.getChild("liver");
        this.git = organs.getChild("g_i_t");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = PlayerModel.createMesh(CubeDeformation.NONE, false);
        PartDefinition root = meshdefinition.getRoot();

        PartDefinition body = root.getChild("body");
        PartDefinition organs = body.addOrReplaceChild("organs", CubeListBuilder.create(), PartPose.ZERO);

        organs.addOrReplaceChild("heart", CubeListBuilder.create().texOffs(24, 28)
                .addBox(0.0F, 2.0F, -1.0F, 2.0F, 2.0F, 2.0F), PartPose.ZERO);

        organs.addOrReplaceChild("left_lung", CubeListBuilder.create()
                .texOffs(26, 21).addBox(2.0F, 2.0F, -1.0F, 1.0F, 2.0F, 2.0F)
                .texOffs(8, 29).addBox(1.0F, 1.0F, -1.0F, 2.0F, 1.0F, 2.0F)
                .texOffs(20, 23).addBox(1.0F, 0.0F, -1.0F, 1.0F, 1.0F, 2.0F)
                .texOffs(12, 23).addBox(1.0F, 4.0F, -1.0F, 2.0F, 1.0F, 2.0F), PartPose.ZERO);

        organs.addOrReplaceChild("right_lung", CubeListBuilder.create()
                .texOffs(16, 26).addBox(-3.0F, 1.0F, -1.0F, 2.0F, 4.0F, 2.0F)
                .texOffs(26, 25).addBox(-2.0F, 0.0F, -1.0F, 1.0F, 1.0F, 2.0F), PartPose.ZERO);

        organs.addOrReplaceChild("liver", CubeListBuilder.create()
                .texOffs(0, 26).addBox(-3.0F, 5.0F, -1.0F, 3.0F, 1.0F, 2.0F)
                .texOffs(0, 29).addBox(-3.0F, 6.0F, -1.0F, 2.0F, 1.0F, 2.0F), PartPose.ZERO);

        organs.addOrReplaceChild("g_i_t", CubeListBuilder.create()
                .texOffs(24, 18).addBox(0.0F, 7.0F, -1.0F, 2.0F, 1.0F, 2.0F)
                .texOffs(0, 23).addBox(-1.0F, 8.0F, -1.0F, 4.0F, 1.0F, 2.0F)
                .texOffs(16, 15).addBox(-3.0F, 9.0F, -1.0F, 6.0F, 1.0F, 2.0F)
                .texOffs(12, 20).addBox(-2.0F, 10.0F, -1.0F, 4.0F, 1.0F, 2.0F), PartPose.ZERO);

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    public void renderOrgans(PoseStack poseStack, MultiBufferSource buffer, int packedLight, OrgansData organs) {
        VertexConsumer consumer = buffer.getBuffer(ORGANS_RENDER_TYPE);

        if (organs.getHeart() == 1) this.heart.render(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY);
        if (organs.getLeft_lung() == 1) this.leftLung.render(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY);
        if (organs.getRight_lung() == 1) this.rightLung.render(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY);
        if (organs.getLiver() == 1) this.liver.render(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY);
        if (organs.getG_i_t() == 1) this.git.render(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY);
    }
}