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

    /**
     * Событие прикрепления возможностей (Capabilities) к сущностям.
     * Зачем нужен: Добавляет хранилище данных мода (LimbCapabilityProvider) каждому созданному игроку.
     *
     * @param event Контекст события AttachCapabilitiesEvent.
     */
    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            event.addCapability(
                    new ResourceLocation(CorrectDamaged.MODID, "limbs"),
                    new LimbCapabilityProvider()
            );
        }
    }

    /**
     * Событие клонирования игрока (возникает при смерти или возвращении из Энда).
     * Зачем нужен: Переносит сохраненное состояние конечностей со старого (умершего) объекта игрока на нового.
     *
     * @param event Контекст события PlayerEvent.Clone.
     */
    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
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

    /**
     * Событие входа игрока на сервер.
     * Зачем нужен: Отправляет подключившемуся игроку пакет с текущими данными о его конечностях.
     *
     * @param event Контекст события PlayerLoggedInEvent.
     */
    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            PacketHandler.syncToPlayer(player);
        }
    }

    /**
     * Событие возрождения игрока после смерти.
     * Зачем нужен: Повторно синхронизирует состояние конечностей с клиентом игрока после возрождения.
     *
     * @param event Контекст события PlayerRespawnEvent.
     */
    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            PacketHandler.syncToPlayer(player);
        }
    }

    /**
     * Событие, когда один игрок начинает отображаться у другого (заходит в зону видимости / рендера).
     * Зачем нужен: Отправляет наблюдателю (trackingPlayer) актуальное состояние конечностей наблюдаемого (targetPlayer).
     *
     * @param event Контекст события StartTracking.
     */
    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking event) {
        if (!(event.getEntity() instanceof ServerPlayer trackingPlayer)) {
            return;
        }

        if (!(event.getTarget() instanceof ServerPlayer targetPlayer)) {
            return;
        }

        PacketHandler.sendTo(trackingPlayer, targetPlayer);
    }

    /**
     * Событие регистрации команд Forge/Minecraft.
     * Зачем нужен: Регистрирует внутриигровые команды мода (например, для проверки или выдачи ран конечностям).
     *
     * @param event Контекст события RegisterCommandsEvent.
     */
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        LimbCommands.register(event.getDispatcher());
    }
}