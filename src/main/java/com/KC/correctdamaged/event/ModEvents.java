package com.KC.correctdamaged.event;

import com.KC.correctdamaged.CorrectDamaged;
import com.KC.correctdamaged.capability.visual.LimbCapability;
import com.KC.correctdamaged.capability.visual.LimbCapabilityProvider;
import com.KC.correctdamaged.command.LimbCommands;
import com.KC.correctdamaged.network.PacketHandler;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import net.minecraft.server.level.ServerPlayer;

@Mod.EventBusSubscriber(
        modid = CorrectDamaged.MODID,
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public class ModEvents {

    @SubscribeEvent
    public static void onAttachCapabilities(
            AttachCapabilitiesEvent<Entity> event
    ) {

        if (event.getObject() instanceof Player) {

            event.addCapability(
                    new ResourceLocation(
                            CorrectDamaged.MODID,
                            "limbs"
                    ),
                    new LimbCapabilityProvider()
            );
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(
            PlayerEvent.Clone event
    ) {

        event.getOriginal()
                .getCapability(LimbCapability.INSTANCE)
                .ifPresent(oldData -> {

                    event.getEntity()
                            .getCapability(LimbCapability.INSTANCE)
                            .ifPresent(newData -> {

                                newData.copyFrom(oldData);

                            });
                });
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(
            PlayerEvent.PlayerLoggedInEvent event
    ) {

        if (event.getEntity() instanceof ServerPlayer player) {

            PacketHandler.syncToPlayer(player);
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawn(
            PlayerEvent.PlayerRespawnEvent event
    ) {

        if (event.getEntity() instanceof ServerPlayer player) {

            PacketHandler.syncToPlayer(player);
        }
    }

    @SubscribeEvent
    public static void onStartTracking(
            PlayerEvent.StartTracking event
    ) {

        if (!(event.getEntity() instanceof ServerPlayer trackingPlayer)) {
            return;
        }

        if (!(event.getTarget() instanceof ServerPlayer targetPlayer)) {
            return;
        }

        PacketHandler.sendTo(trackingPlayer, targetPlayer);
    }

    @SubscribeEvent
    public static void onRegisterCommands(
            RegisterCommandsEvent event
    ) {

        LimbCommands.register(
                event.getDispatcher()
        );
    }
}