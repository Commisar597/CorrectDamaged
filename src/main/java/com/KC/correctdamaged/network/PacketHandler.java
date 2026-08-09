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

    // =========================================================
    // INITIALIZATION
    // =========================================================

    public static void init() {

        CHANNEL = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(
                        CorrectDamaged.MODID,
                        "main"
                ),
                () -> PROTOCOL_VERSION,
                PROTOCOL_VERSION::equals,
                PROTOCOL_VERSION::equals
        );

        CHANNEL.registerMessage(
                id++,
                SyncLimbDataPacket.class,
                SyncLimbDataPacket::encode,
                SyncLimbDataPacket::decode,
                SyncLimbDataPacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT)
        );
    }

    // =========================================================
    // SYNC: игрок -> себе и всем, кто его трекает
    // =========================================================

    public static void syncToTrackingAndSelf(
            ServerPlayer player
    ) {

        SyncLimbDataPacket packet =
                SyncLimbDataPacket.from(player);

        CHANNEL.send(
                PacketDistributor.TRACKING_ENTITY_AND_SELF.with(
                        () -> player
                ),
                packet
        );
    }

    // =========================================================
    // SYNC: только самому игроку (например, при логине/респавне)
    // =========================================================

    public static void syncToPlayer(
            ServerPlayer player
    ) {

        SyncLimbDataPacket packet =
                SyncLimbDataPacket.from(player);

        CHANNEL.send(
                PacketDistributor.PLAYER.with(
                        () -> player
                ),
                packet
        );
    }

    // =========================================================
    // SYNC: данные конкретного игрока (dataOwner) отправить
    // конкретному получателю (receiver) — нужно для StartTracking
    // =========================================================

    public static void sendTo(
            ServerPlayer receiver,
            ServerPlayer dataOwner
    ) {

        SyncLimbDataPacket packet =
                SyncLimbDataPacket.from(dataOwner);

        CHANNEL.send(
                PacketDistributor.PLAYER.with(
                        () -> receiver
                ),
                packet
        );
    }
}