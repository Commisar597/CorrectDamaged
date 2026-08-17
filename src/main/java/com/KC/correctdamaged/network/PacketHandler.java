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

    public static void init() {
        CHANNEL = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(CorrectDamaged.MODID, "main"),
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

    public static void sendToPlayer(SyncLimbDataPacket packet, ServerPlayer player) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), packet);
    }

    public static void sendToTrackingAndSelf(SyncLimbDataPacket packet, ServerPlayer player) {
        CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player), packet);
    }

    public static void syncToPlayer(ServerPlayer player) {
        sendToPlayer(SyncLimbDataPacket.from(player), player);
    }

    public static void syncToTrackingAndSelf(ServerPlayer player) {
        sendToTrackingAndSelf(SyncLimbDataPacket.from(player), player);
    }

    public static void sendTo(ServerPlayer receiver, ServerPlayer dataOwner) {
        sendToPlayer(SyncLimbDataPacket.from(dataOwner), receiver);
    }
}