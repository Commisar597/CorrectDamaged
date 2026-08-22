package com.KC.correctdamaged.event;

import com.KC.correctdamaged.CorrectDamaged;
import com.KC.correctdamaged.client.render.head.*;
import com.KC.correctdamaged.client.render.limbs.*;
import com.KC.correctdamaged.client.render.torso.BodyAnatomyLayer;
import com.KC.correctdamaged.client.render.torso.BodyModel;
import com.KC.correctdamaged.client.render.torso.organs.PlayerOrgansModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CorrectDamaged.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {

    public static final ModelLayerLocation PLAYER_BONES_LAYER = new ModelLayerLocation(
            new ResourceLocation(CorrectDamaged.MODID, "player_bones"), "main");
    public static final ModelLayerLocation PLAYER_BONES_SLIM_LAYER = new ModelLayerLocation(
            new ResourceLocation(CorrectDamaged.MODID, "player_bones_slim"), "main");

    public static final ModelLayerLocation PLAYER_MUSCLES_LAYER = new ModelLayerLocation(
            new ResourceLocation(CorrectDamaged.MODID, "player_muscles"), "main");
    public static final ModelLayerLocation PLAYER_MUSCLES_SLIM_LAYER = new ModelLayerLocation(
            new ResourceLocation(CorrectDamaged.MODID, "player_muscles_slim"), "main");

    public static final ModelLayerLocation BODY_LAYER = new ModelLayerLocation(
            new ResourceLocation(CorrectDamaged.MODID, "body"), "main");

    public static final ModelLayerLocation ORGANS_LAYER = new ModelLayerLocation(
            new ResourceLocation(CorrectDamaged.MODID, "organs"), "main");

    @SubscribeEvent
    public static void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(
                PLAYER_BONES_LAYER,
                () -> PlayerBonesModel.createBodyLayer(false)
        );
        event.registerLayerDefinition(
                PLAYER_BONES_SLIM_LAYER,
                () -> PlayerBonesModel.createBodyLayer(true)
        );

        event.registerLayerDefinition(
                PLAYER_MUSCLES_LAYER,
                () -> PlayerMusclesModel.createBodyLayer(false)
        );
        event.registerLayerDefinition(
                PLAYER_MUSCLES_SLIM_LAYER,
                () -> PlayerMusclesModel.createBodyLayer(true)
        );

        event.registerLayerDefinition(
                BODY_LAYER,
                BodyModel::createBodyLayer
        );

        event.registerLayerDefinition(
                ORGANS_LAYER,
                PlayerOrgansModel::createBodyLayer
        );
    }

    @SubscribeEvent
    public static void onAddLayers(EntityRenderersEvent.AddLayers event) {
        for (String skinName : event.getSkins()) {
            PlayerRenderer renderer = event.getSkin(skinName);
            if (renderer != null) {
                renderer.addLayer(new LimbAnatomyLayer(renderer, event.getEntityModels()));
                renderer.addLayer(new BodyAnatomyLayer(renderer, event.getEntityModels()));
                renderer.addLayer(new HeadAnatomyLayer(renderer));
            }
        }
    }
}