package com.KC.correctdamaged.network;

import com.KC.correctdamaged.capability.LimbCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class SyncLimbDataPacket {

    private final UUID playerUUID;

    private final int rightArm;
    private final int leftArm;
    private final int rightLeg;
    private final int leftLeg;
    private final int headState;
    private final int bodyState;

    public SyncLimbDataPacket(
            UUID playerUUID,
            int rightArm,
            int leftArm,
            int rightLeg,
            int leftLeg,
            int headState,
            int bodyState
    ) {
        this.playerUUID = playerUUID;
        this.rightArm = rightArm;
        this.leftArm = leftArm;
        this.rightLeg = rightLeg;
        this.leftLeg = leftLeg;
        this.headState = headState;
        this.bodyState = bodyState;
    }

    public static SyncLimbDataPacket from(ServerPlayer player) {
        return player.getCapability(LimbCapability.INSTANCE)
                .map(data -> new SyncLimbDataPacket(
                        player.getUUID(),
                        data.getRightArm(),
                        data.getLeftArm(),
                        data.getRightLeg(),
                        data.getLeftLeg(),
                        data.getHeadState(),
                        data.getBodyState()
                ))
                .orElse(new SyncLimbDataPacket(
                        player.getUUID(),
                        3, 3, 3, 3, 5, 9
                ));
    }

    public static void encode(
            SyncLimbDataPacket packet,
            FriendlyByteBuf buffer
    ) {
        buffer.writeUUID(packet.playerUUID);

        buffer.writeByte(packet.rightArm);
        buffer.writeByte(packet.leftArm);
        buffer.writeByte(packet.rightLeg);
        buffer.writeByte(packet.leftLeg);
        buffer.writeByte(packet.headState);
        buffer.writeByte(packet.bodyState);
    }

    public static SyncLimbDataPacket decode(
            FriendlyByteBuf buffer
    ) {
        return new SyncLimbDataPacket(
                buffer.readUUID(),
                buffer.readByte(),
                buffer.readByte(),
                buffer.readByte(),
                buffer.readByte(),
                buffer.readByte(),
                buffer.readByte()
        );
    }

    public static void handle(
            SyncLimbDataPacket packet,
            Supplier<NetworkEvent.Context> contextSupplier
    ) {
        NetworkEvent.Context context = contextSupplier.get();

        context.enqueueWork(() -> {
            Minecraft minecraft = Minecraft.getInstance();

            if (minecraft.level == null) {
                return;
            }

            Player player = minecraft.level.getPlayerByUUID(packet.playerUUID);

            if (player == null) {
                return;
            }

            player.getCapability(LimbCapability.INSTANCE).ifPresent(data -> {
                data.setRightArm(packet.rightArm);
                data.setLeftArm(packet.leftArm);
                data.setRightLeg(packet.rightLeg);
                data.setLeftLeg(packet.leftLeg);
                data.setHeadState(packet.headState);
                data.setBodyState(packet.bodyState);
            });
        });

        context.setPacketHandled(true);
    }
}