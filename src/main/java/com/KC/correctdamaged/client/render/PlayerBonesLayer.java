package com.KC.correctdamaged.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;

public class PlayerBonesLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    private final PlayerBonesModel defaultBonesModel;
    private final PlayerBonesModel slimBonesModel;

    public PlayerBonesLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderer) {
        this(renderer, Minecraft.getInstance().getEntityModels());
    }

    public PlayerBonesLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderer, EntityModelSet modelSet) {
        super(renderer);
        this.defaultBonesModel = new PlayerBonesModel(modelSet.bakeLayer(PlayerBonesModel.LAYER_LOCATION), false);
        this.slimBonesModel = new PlayerBonesModel(modelSet.bakeLayer(PlayerBonesModel.SLIM_LAYER_LOCATION), true);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, AbstractClientPlayer player,
                       float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {

        if (player.isInvisible()) {
            return;
        }

        boolean isSlim = player.getModelName().equals("slim");
        PlayerBonesModel activeModel = isSlim ? this.slimBonesModel : this.defaultBonesModel;

        this.getParentModel().copyPropertiesTo(activeModel);
        activeModel.setupAnim(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        boolean isBurnt = player.isOnFire();

        activeModel.renderWithTextures(poseStack, buffer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F, isBurnt);
    }
}