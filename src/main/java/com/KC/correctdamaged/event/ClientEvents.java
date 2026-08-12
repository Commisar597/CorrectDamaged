package com.KC.correctdamaged.client.event;

import com.KC.correctdamaged.CorrectDamaged;
import com.KC.correctdamaged.client.render.BodyDamageLayer;
import com.KC.correctdamaged.client.render.HeadDamageLayer;
import com.KC.correctdamaged.client.render.LimbDamageLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(
        modid = CorrectDamaged.MODID,
        bus = Mod.EventBusSubscriber.Bus.MOD,
        value = Dist.CLIENT
)
public class ClientEvents {

    @SubscribeEvent
    public static void onAddLayers(EntityRenderersEvent.AddLayers event) {
        for (String skinName : event.getSkins()) {
            PlayerRenderer renderer = event.getSkin(skinName);
            if (renderer != null) {
                renderer.addLayer(new BodyDamageLayer(renderer));
                renderer.addLayer(new HeadDamageLayer(renderer));
                renderer.addLayer(new LimbDamageLayer(renderer));
            }
        }
    }
}