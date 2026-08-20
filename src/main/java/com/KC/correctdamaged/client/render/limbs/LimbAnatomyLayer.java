package com.KC.correctdamaged.client.render.limbs;

import com.KC.correctdamaged.capability.LimbManager;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;

public class LimbAnatomyLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    private final PlayerBonesRenderer defaultBonesRenderer;
    private final PlayerBonesRenderer slimBonesRenderer;
    private final PlayerMusclesRenderer defaultMusclesRenderer;
    private final PlayerMusclesRenderer slimMusclesRenderer;
    private final StumpRenderer stumpRenderer;

    public LimbAnatomyLayer(
            RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent,
            EntityModelSet modelSet
    ) {
        super(parent);
        this.defaultBonesRenderer = new PlayerBonesRenderer(modelSet, false);
        this.slimBonesRenderer = new PlayerBonesRenderer(modelSet, true);
        this.defaultMusclesRenderer = new PlayerMusclesRenderer(modelSet, false);
        this.slimMusclesRenderer = new PlayerMusclesRenderer(modelSet, true);
        this.stumpRenderer = new StumpRenderer();
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight,
                       AbstractClientPlayer player, float limbSwing, float limbSwingAmount,
                       float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        LimbManager.get(player).ifPresent(data -> {
            PlayerModel<AbstractClientPlayer> model = getParentModel();
            boolean isSlim = player.getModelName().equals("slim");
            PlayerBonesRenderer bonesRenderer = isSlim ? slimBonesRenderer : defaultBonesRenderer;
            PlayerMusclesRenderer musclesRenderer = isSlim ? slimMusclesRenderer : defaultMusclesRenderer;
            LimbAnatomyManager.renderLimbAnatomy(
                    poseStack, buffer, packedLight, player, model,
                    bonesRenderer, musclesRenderer, stumpRenderer, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch
            );
        });
    }
}
