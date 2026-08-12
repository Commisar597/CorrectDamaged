package com.KC.correctdamaged.capability;

import com.KC.correctdamaged.network.PacketHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

public final class LimbManager {

    private LimbManager() {}

    public static Optional<LimbData> get(Player player) {
        return player.getCapability(LimbCapability.INSTANCE).resolve();
    }

    public static boolean setRightArm(Player player, int state) {
        return get(player).map(data -> {
            int oldState = data.getRightArm();
            data.setRightArm(state);
            if (oldState != data.getRightArm()) sync(player);
            return true;
        }).orElse(false);
    }

    public static boolean setLeftArm(Player player, int state) {
        return get(player).map(data -> {
            int oldState = data.getLeftArm();
            data.setLeftArm(state);
            if (oldState != data.getLeftArm()) sync(player);
            return true;
        }).orElse(false);
    }

    public static boolean setRightLeg(Player player, int state) {
        return get(player).map(data -> {
            int oldState = data.getRightLeg();
            data.setRightLeg(state);
            if (oldState != data.getRightLeg()) sync(player);
            return true;
        }).orElse(false);
    }

    public static boolean setLeftLeg(Player player, int state) {
        return get(player).map(data -> {
            int oldState = data.getLeftLeg();
            data.setLeftLeg(state);
            if (oldState != data.getLeftLeg()) sync(player);
            return true;
        }).orElse(false);
    }

    public static boolean setHeadState(Player player, int state) {
        return get(player).map(data -> {
            int oldState = data.getHeadState();
            data.setHeadState(state);
            if (oldState != data.getHeadState()) sync(player);
            return true;
        }).orElse(false);
    }

    public static boolean setBodyState(Player player, int state) {
        return get(player).map(data -> {
            int oldState = data.getBodyState();
            data.setBodyState(state);
            if (oldState != data.getBodyState()) sync(player);
            return true;
        }).orElse(false);
    }

    public static int getRightArm(Player player) { return get(player).map(LimbData::getRightArm).orElse(3); }
    public static int getLeftArm(Player player) { return get(player).map(LimbData::getLeftArm).orElse(3); }
    public static int getRightLeg(Player player) { return get(player).map(LimbData::getRightLeg).orElse(3); }
    public static int getLeftLeg(Player player) { return get(player).map(LimbData::getLeftLeg).orElse(3); }
    public static int getHeadState(Player player) { return get(player).map(LimbData::getHeadState).orElse(5); }
    public static int getBodyState(Player player) { return get(player).map(LimbData::getBodyState).orElse(9); }

    private static void sync(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            PacketHandler.syncToTrackingAndSelf(serverPlayer);
        }
    }
}