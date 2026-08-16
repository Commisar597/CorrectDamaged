package com.KC.correctdamaged.network;

import com.KC.correctdamaged.capability.HeadData;
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
    private final int bodyState;

    private final int boneRightArm;
    private final int boneLeftArm;
    private final int boneRightLeg;
    private final int boneLeftLeg;
    private final int showSkeleton;

    private final int muscleRightArm;
    private final int muscleLeftArm;
    private final int muscleRightLeg;
    private final int muscleLeftLeg;
    private final int muscleBody;

    private final int headSkinMask;
    private final int headMuscleMask;
    private final int headSkullMask;

    public SyncLimbDataPacket(
            UUID playerUUID,
            int rightArm,
            int leftArm,
            int rightLeg,
            int leftLeg,
            int bodyState,
            int boneRightArm,
            int boneLeftArm,
            int boneRightLeg,
            int boneLeftLeg,
            int showSkeleton,
            int muscleRightArm,
            int muscleLeftArm,
            int muscleRightLeg,
            int muscleLeftLeg,
            int muscleBody,
            int headSkinMask,
            int headMuscleMask,
            int headSkullMask
    ) {
        this.playerUUID = playerUUID;
        this.rightArm = rightArm;
        this.leftArm = leftArm;
        this.rightLeg = rightLeg;
        this.leftLeg = leftLeg;
        this.bodyState = bodyState;
        this.boneRightArm = boneRightArm;
        this.boneLeftArm = boneLeftArm;
        this.boneRightLeg = boneRightLeg;
        this.boneLeftLeg = boneLeftLeg;
        this.showSkeleton = showSkeleton;
        this.muscleRightArm = muscleRightArm;
        this.muscleLeftArm = muscleLeftArm;
        this.muscleRightLeg = muscleRightLeg;
        this.muscleLeftLeg = muscleLeftLeg;
        this.muscleBody = muscleBody;
        this.headSkinMask = headSkinMask;
        this.headMuscleMask = headMuscleMask;
        this.headSkullMask = headSkullMask;
    }

    public static SyncLimbDataPacket from(ServerPlayer player) {
        return player.getCapability(LimbCapability.INSTANCE)
                .map(data -> {
                    HeadData head = data.getHead();
                    return new SyncLimbDataPacket(
                            player.getUUID(),
                            data.getRightArm(),
                            data.getLeftArm(),
                            data.getRightLeg(),
                            data.getLeftLeg(),
                            data.getBodyState(),
                            data.getBoneRightArm(),
                            data.getBoneLeftArm(),
                            data.getBoneRightLeg(),
                            data.getBoneLeftLeg(),
                            data.getShowSkeleton(),
                            data.getMuscleRightArm(),
                            data.getMuscleLeftArm(),
                            data.getMuscleRightLeg(),
                            data.getMuscleLeftLeg(),
                            data.getMuscleBody(),
                            head.getSkinMask() & 0xFF,
                            head.getMuscleMask() & 0xFF,
                            head.getSkullMask() & 0xFF
                    );
                })
                .orElse(new SyncLimbDataPacket(
                        player.getUUID(),
                        3, 3, 3, 3, 9,
                        0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0,
                        255, 255, 255
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
        buffer.writeByte(packet.bodyState);

        buffer.writeByte(packet.boneRightArm);
        buffer.writeByte(packet.boneLeftArm);
        buffer.writeByte(packet.boneRightLeg);
        buffer.writeByte(packet.boneLeftLeg);
        buffer.writeByte(packet.showSkeleton);

        buffer.writeByte(packet.muscleRightArm);
        buffer.writeByte(packet.muscleLeftArm);
        buffer.writeByte(packet.muscleRightLeg);
        buffer.writeByte(packet.muscleLeftLeg);
        buffer.writeByte(packet.muscleBody);

        buffer.writeByte(packet.headSkinMask);
        buffer.writeByte(packet.headMuscleMask);
        buffer.writeByte(packet.headSkullMask);
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
                buffer.readByte(),
                buffer.readByte(),
                buffer.readByte(),
                buffer.readByte(),
                buffer.readByte(),
                buffer.readByte(),
                buffer.readByte(),
                buffer.readByte(),
                buffer.readByte(),
                buffer.readByte(),
                buffer.readByte(),
                buffer.readByte(),
                buffer.readByte()
        );
    }

    public static void handle(SyncLimbDataPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();

        context.enqueueWork(() -> {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.level == null) return;

            Player player = minecraft.level.getPlayerByUUID(packet.playerUUID);
            if (player == null && minecraft.player != null && minecraft.player.getUUID().equals(packet.playerUUID)) {
                player = minecraft.player;
            }

            if (player == null) return;

            player.getCapability(LimbCapability.INSTANCE).ifPresent(data -> {
                data.setRightArm(packet.rightArm);
                data.setLeftArm(packet.leftArm);
                data.setRightLeg(packet.rightLeg);
                data.setLeftLeg(packet.leftLeg);
                data.setBodyState(packet.bodyState);

                data.setBoneRightArm(packet.boneRightArm);
                data.setBoneLeftArm(packet.boneLeftArm);
                data.setBoneRightLeg(packet.boneRightLeg);
                data.setBoneLeftLeg(packet.boneLeftLeg);
                data.setShowSkeleton(packet.showSkeleton);

                data.setMuscleRightArm(packet.muscleRightArm);
                data.setMuscleLeftArm(packet.muscleLeftArm);
                data.setMuscleRightLeg(packet.muscleRightLeg);
                data.setMuscleLeftLeg(packet.muscleLeftLeg);
                data.setMuscleBody(packet.muscleBody);

                HeadData head = data.getHead();
                head.setSkinMask((byte) packet.headSkinMask);
                head.setMuscleMask((byte) packet.headMuscleMask);
                head.setSkullMask((byte) packet.headSkullMask);
            });
        });

        context.setPacketHandled(true);
    }
}