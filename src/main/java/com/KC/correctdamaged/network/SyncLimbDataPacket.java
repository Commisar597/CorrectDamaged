package com.KC.correctdamaged.network;

import com.KC.correctdamaged.capability.visual.LimbCapability;
import com.KC.correctdamaged.capability.visual.LimbData;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class SyncLimbDataPacket {

    private final UUID playerUUID;
    private final CompoundTag nbt;

    public SyncLimbDataPacket(UUID playerUUID, CompoundTag nbt) {
        this.playerUUID = playerUUID;
        this.nbt = nbt;
    }

    public static SyncLimbDataPacket from(ServerPlayer player) {
        CompoundTag tag = player.getCapability(LimbCapability.INSTANCE)
                .map(LimbData::serializeNBT)
                .orElseGet(CompoundTag::new);
        return new SyncLimbDataPacket(player.getUUID(), tag);
    }

    public static void encode(SyncLimbDataPacket packet, FriendlyByteBuf buffer) {
        buffer.writeUUID(packet.playerUUID);
        buffer.writeNbt(packet.nbt);
    }

    public static SyncLimbDataPacket decode(FriendlyByteBuf buffer) {
        return new SyncLimbDataPacket(buffer.readUUID(), buffer.readNbt());
    }

    public static void handle(SyncLimbDataPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> handleClient(packet));
        });
        context.setPacketHandled(true);
    }

    private static void handleClient(SyncLimbDataPacket packet) {
        if (Minecraft.getInstance().level == null) return;

        Player player = Minecraft.getInstance().level.getPlayerByUUID(packet.playerUUID);
        if (player != null) {
            player.getCapability(LimbCapability.INSTANCE).ifPresent(data -> {
                data.deserializeNBT(packet.nbt);
            });
        }
    }
}