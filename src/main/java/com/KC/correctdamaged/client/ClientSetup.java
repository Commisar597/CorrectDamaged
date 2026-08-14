package com.KC.correctdamaged.client;

import com.KC.correctdamaged.CorrectDamaged;
import com.KC.correctdamaged.client.render.StumpBodyLayer;
import com.KC.correctdamaged.client.render.StumpLayer;

import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CorrectDamaged.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {

    @SubscribeEvent
    public static void onAddLayers(EntityRenderersEvent.AddLayers event) {

        PlayerRenderer defaultRenderer = event.getSkin("default");
        if (defaultRenderer != null) {
            defaultRenderer.addLayer(new StumpLayer(defaultRenderer));
            defaultRenderer.addLayer(new StumpBodyLayer(defaultRenderer));
        }

        PlayerRenderer slimRenderer = event.getSkin("slim");
        if (slimRenderer != null) {
            slimRenderer.addLayer(new StumpLayer(slimRenderer));
            slimRenderer.addLayer(new StumpBodyLayer(slimRenderer));
        }
    }
}