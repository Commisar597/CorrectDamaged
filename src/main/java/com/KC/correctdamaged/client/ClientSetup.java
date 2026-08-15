package com.KC.correctdamaged.client;

import com.KC.correctdamaged.CorrectDamaged;

import com.KC.correctdamaged.client.render.*;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CorrectDamaged.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {

    public static final ModelLayerLocation PLAYER_BONES_LAYER = new ModelLayerLocation(
            new ResourceLocation(CorrectDamaged.MODID, "player_bones"), "main");
    public static final ModelLayerLocation PLAYER_BONES_SLIM_LAYER = new ModelLayerLocation(
            new ResourceLocation(CorrectDamaged.MODID, "player_bones_slim"), "main");

    public static final ModelLayerLocation PLAYER_MUSCLES_LAYER = new ModelLayerLocation(
            new ResourceLocation(CorrectDamaged.MODID, "player_muscles"), "main");
    public static final ModelLayerLocation PLAYER_MUSCLES_SLIM_LAYER = new ModelLayerLocation(
            new ResourceLocation(CorrectDamaged.MODID, "player_muscles_slim"), "main");

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
    }

    @SubscribeEvent
    public static void onAddLayers(EntityRenderersEvent.AddLayers event) {

        PlayerRenderer defaultRenderer = event.getSkin("default");
        if (defaultRenderer != null) {
            defaultRenderer.addLayer(new StumpLayer(defaultRenderer));
            defaultRenderer.addLayer(new StumpBodyLayer(defaultRenderer));
            defaultRenderer.addLayer(new BodyDamageLayer(defaultRenderer));
            defaultRenderer.addLayer(new PlayerBonesLayer(defaultRenderer, event.getEntityModels(), false));
            defaultRenderer.addLayer(new PlayerMusclesLayer(defaultRenderer, event.getEntityModels(), false));
        }

        // Slim скин (Alex - тонкие руки)
        PlayerRenderer slimRenderer = event.getSkin("slim");
        if (slimRenderer != null) {
            slimRenderer.addLayer(new StumpLayer(slimRenderer));
            slimRenderer.addLayer(new StumpBodyLayer(slimRenderer));
            slimRenderer.addLayer(new BodyDamageLayer(slimRenderer));
            slimRenderer.addLayer(new PlayerBonesLayer(slimRenderer, event.getEntityModels(), true));
            slimRenderer.addLayer(new PlayerMusclesLayer(slimRenderer, event.getEntityModels(), true));
        }
    }
}