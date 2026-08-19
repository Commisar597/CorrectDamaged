package com.KC.correctdamaged.client.render.torso;

import com.KC.correctdamaged.event.ClientEvents;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;

/**
 * Слой рендеринга анатомии туловища для рендерера игрока (PlayerRenderer).
 * Зачем нужен: Интегрирует анатомический рендер туловища в ванильный конвейер Minecraft RenderLayer,
 * синхронизируя анимации тела и вызывая `BodyAnatomyManager`.
 */
public class BodyAnatomyLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    private final BodyModel bodyModel;

    /**
     * Конструктор слоя анатомии туловища.
     */
    public BodyAnatomyLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent, EntityModelSet modelSet) {
        super(parent);
        this.bodyModel = new BodyModel(modelSet.bakeLayer(ClientEvents.BODY_LAYER));
    }

    /**
     * Метод отрисовки слоя. Синхронизирует углы поворота и вызывает логику рендера анатомии.
     */
    @Override
    public void render(
            PoseStack poseStack, MultiBufferSource buffer, int packedLight,
            AbstractClientPlayer player, float limbSwing, float limbSwingAmount,
            float partialTicks, float ageInTicks, float netHeadYaw, float headPitch
    ) {
        bodyModel.setupAnim(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        BodyAnatomyManager.renderBody(
                poseStack, buffer, packedLight, player,
                getParentModel(), bodyModel
        );
    }
}