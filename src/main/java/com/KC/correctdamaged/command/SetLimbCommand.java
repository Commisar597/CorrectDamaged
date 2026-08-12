package com.KC.correctdamaged.command;

import com.KC.correctdamaged.capability.LimbManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class SetLimbCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("limb")
                        .requires(source -> source.hasPermission(2))

                        // Команды для рук и ног (состояния 0..3)
                        .then(Commands.literal("right_arm")
                                .then(Commands.argument("state", IntegerArgumentType.integer(0, 3))
                                        .executes(ctx -> applyLimb(ctx.getSource(), "right_arm", IntegerArgumentType.getInteger(ctx, "state")))))

                        .then(Commands.literal("left_arm")
                                .then(Commands.argument("state", IntegerArgumentType.integer(0, 3))
                                        .executes(ctx -> applyLimb(ctx.getSource(), "left_arm", IntegerArgumentType.getInteger(ctx, "state")))))

                        .then(Commands.literal("right_leg")
                                .then(Commands.argument("state", IntegerArgumentType.integer(0, 3))
                                        .executes(ctx -> applyLimb(ctx.getSource(), "right_leg", IntegerArgumentType.getInteger(ctx, "state")))))

                        .then(Commands.literal("left_leg")
                                .then(Commands.argument("state", IntegerArgumentType.integer(0, 3))
                                        .executes(ctx -> applyLimb(ctx.getSource(), "left_leg", IntegerArgumentType.getInteger(ctx, "state")))))

                        // Команда для головы (состояния 0..5)
                        .then(Commands.literal("head")
                                .then(Commands.argument("state", IntegerArgumentType.integer(0, 5))
                                        .executes(ctx -> applyLimb(ctx.getSource(), "head", IntegerArgumentType.getInteger(ctx, "state")))))

                        // Команда для туловища (состояния 0..9)
                        .then(Commands.literal("body")
                                .then(Commands.argument("state", IntegerArgumentType.integer(0, 9))
                                        .executes(ctx -> applyLimb(ctx.getSource(), "body", IntegerArgumentType.getInteger(ctx, "state")))))
        );
    }

    private static int applyLimb(CommandSourceStack source, String part, int state) {
        try {
            ServerPlayer player = source.getPlayerOrException();
            boolean changed = switch (part) {
                case "right_arm" -> LimbManager.setRightArm(player, state);
                case "left_arm" -> LimbManager.setLeftArm(player, state);
                case "right_leg" -> LimbManager.setRightLeg(player, state);
                case "left_leg" -> LimbManager.setLeftLeg(player, state);
                case "head" -> LimbManager.setHeadState(player, state);
                case "body" -> LimbManager.setBodyState(player, state);
                default -> false;
            };

            if (!changed) {
                source.sendFailure(Component.literal("Capability недоступна."));
                return 0;
            }

            source.sendSuccess(() -> Component.literal("Установлено состояние " + part + ": " + state), true);
            return 1;
        } catch (Exception e) {
            source.sendFailure(Component.literal("Ошибка выполнения команды: " + e.getMessage()));
            return 0;
        }
    }
}