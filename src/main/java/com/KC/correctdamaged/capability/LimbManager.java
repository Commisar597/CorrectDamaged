package com.KC.correctdamaged.capability;

import com.KC.correctdamaged.network.PacketHandler;

import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

public final class LimbManager {

    private LimbManager() {
    }

    public static Optional<LimbData> get(Player player) {
        return player
                .getCapability(LimbCapability.INSTANCE)
                .resolve();
    }

    public static boolean setRightArm(
            Player player,
            int state
    ) {
        return get(player).map(data -> {

            int oldState = data.getRightArm();

            data.setRightArm(state);

            if (oldState != data.getRightArm()) {
                sync(player);
            }

            return true;

        }).orElse(false);
    }

    public static boolean setLeftArm(
            Player player,
            int state
    ) {
        return get(player).map(data -> {

            int oldState = data.getLeftArm();

            data.setLeftArm(state);

            if (oldState != data.getLeftArm()) {
                sync(player);
            }

            return true;

        }).orElse(false);
    }

    public static boolean setRightLeg(
            Player player,
            int state
    ) {
        return get(player).map(data -> {

            int oldState = data.getRightLeg();

            data.setRightLeg(state);

            if (oldState != data.getRightLeg()) {
                sync(player);
            }

            return true;

        }).orElse(false);
    }

    public static boolean setLeftLeg(
            Player player,
            int state
    ) {
        return get(player).map(data -> {

            int oldState = data.getLeftLeg();

            data.setLeftLeg(state);

            if (oldState != data.getLeftLeg()) {
                sync(player);
            }

            return true;

        }).orElse(false);
    }

    public static int getRightArm(Player player) {
        return get(player)
                .map(LimbData::getRightArm)
                .orElse(3);
    }

    public static int getLeftArm(Player player) {
        return get(player)
                .map(LimbData::getLeftArm)
                .orElse(3);
    }

    public static int getRightLeg(Player player) {
        return get(player)
                .map(LimbData::getRightLeg)
                .orElse(3);
    }

    public static int getLeftLeg(Player player) {
        return get(player)
                .map(LimbData::getLeftLeg)
                .orElse(3);
    }

    public static boolean damageRightArm(Player player) {

        return get(player).map(data -> {

            int oldState = data.getRightArm();

            if (oldState <= 0) {
                return false;
            }

            data.setRightArm(oldState - 1);

            sync(player);

            return true;

        }).orElse(false);
    }

    public static boolean damageLeftArm(Player player) {

        return get(player).map(data -> {

            int oldState = data.getLeftArm();

            if (oldState <= 0) {
                return false;
            }

            data.setLeftArm(oldState - 1);

            sync(player);

            return true;

        }).orElse(false);
    }

    public static boolean damageRightLeg(Player player) {

        return get(player).map(data -> {

            int oldState = data.getRightLeg();

            if (oldState <= 0) {
                return false;
            }

            data.setRightLeg(oldState - 1);

            sync(player);

            return true;

        }).orElse(false);
    }

    public static boolean damageLeftLeg(Player player) {

        return get(player).map(data -> {

            int oldState = data.getLeftLeg();

            if (oldState <= 0) {
                return false;
            }

            data.setLeftLeg(oldState - 1);

            sync(player);

            return true;

        }).orElse(false);
    }

    private static void sync(Player player) {

        if (!(player instanceof ServerPlayer serverPlayer)) {
            return;
        }

        PacketHandler.syncToTrackingAndSelf(serverPlayer);
    }
}