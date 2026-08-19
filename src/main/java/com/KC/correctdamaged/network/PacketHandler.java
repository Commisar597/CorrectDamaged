package com.KC.correctdamaged.network;

import com.KC.correctdamaged.CorrectDamaged;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;

public final class PacketHandler {

    private static final String PROTOCOL_VERSION = "1";
    private static SimpleChannel CHANNEL;
    private static int id = 0;

    private PacketHandler() {
    }

    /**
     * Создает сетевой канал мода и регистрирует все типы сетевых сообщений (пакетов).
     * Зачем нужен: Позволяет Forge знать, как кодировать, декодировать и обрабатывать каждый пакет.
     */
    public static void init() {
        CHANNEL = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(CorrectDamaged.MODID, "main"),
                () -> PROTOCOL_VERSION,
                PROTOCOL_VERSION::equals,
                PROTOCOL_VERSION::equals
        );

        // Регистрация пакета синхронизации конечностей
        CHANNEL.registerMessage(
                id++,
                SyncLimbDataPacket.class,
                SyncLimbDataPacket::encode,
                SyncLimbDataPacket::decode,
                SyncLimbDataPacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT)
        );
    }

    /**
     * Отправляет пакет конкретному игроку на серверной стороне.
     *
     * @param packet Пакет для отправки.
     * @param player Игрок-получатель.
     */
    public static void sendToPlayer(SyncLimbDataPacket packet, ServerPlayer player) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), packet);
    }

    /**
     * Отправляет пакет всем игрокам, которые видят данного игрока (трекают его), а также самому этому игроку.
     * Зачем нужен: Чтобы изменения внешнего вида игрока были видны и ему, и окружающим.
     *
     * @param packet Пакет для отправки.
     * @param player Игрок-источник.
     */
    public static void sendToTrackingAndSelf(SyncLimbDataPacket packet, ServerPlayer player) {
        CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player), packet);
    }

    /**
     * Хелпер: читает свежие Capability данные у игрока и отправляет их лично ему.
     *
     * @param player Игрок для синхронизации.
     */
    public static void syncToPlayer(ServerPlayer player) {
        sendToPlayer(SyncLimbDataPacket.from(player), player);
    }

    /**
     * Хелпер: читает свежие Capability данные у игрока и рассылает ему и всем наблюдателям.
     *
     * @param player Игрок для синхронизации.
     */
    public static void syncToTrackingAndSelf(ServerPlayer player) {
        sendToTrackingAndSelf(SyncLimbDataPacket.from(player), player);
    }

    /**
     * Отправляет пакет с данными о конечностях одного игрока (dataOwner) другому игроку (receiver).
     * Зачем нужен: Например, когда новый игрок подключается и начинает «видеть» другого игрока.
     *
     * @param receiver Получатель пакета.
     * @param dataOwner Владелец данных о конечностях.
     */
    public static void sendTo(ServerPlayer receiver, ServerPlayer dataOwner) {
        sendToPlayer(SyncLimbDataPacket.from(dataOwner), receiver);
    }
}