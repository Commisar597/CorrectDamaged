package com.KC.correctdamaged.command;

import com.KC.correctdamaged.capability.LimbManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

/**
 * /show bone <limb> <state>    — state 0-6 (0=скрыть, 1-3=кумулятивно от кисти/стопы к плечу/бедру,
 *                                            4-6=то же самое, но обожжённая текстура)
 * /show muscle <limb> <state>  — state 0-3 (0=скрыть, 1-3=кумулятивно, обожжённого варианта нет)
 */
public class ShowLimbLayerCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("show")
                        .requires(source -> source.hasPermission(2))

                        .then(Commands.literal("bone")
                                .then(Commands.literal("right_arm")
                                        .then(Commands.argument("state", IntegerArgumentType.integer(0, 6))
                                                .executes(ctx -> apply(ctx.getSource(), true, "right_arm", IntegerArgumentType.getInteger(ctx, "state")))))
                                .then(Commands.literal("left_arm")
                                        .then(Commands.argument("state", IntegerArgumentType.integer(0, 6))
                                                .executes(ctx -> apply(ctx.getSource(), true, "left_arm", IntegerArgumentType.getInteger(ctx, "state")))))
                                .then(Commands.literal("right_leg")
                                        .then(Commands.argument("state", IntegerArgumentType.integer(0, 6))
                                                .executes(ctx -> apply(ctx.getSource(), true, "right_leg", IntegerArgumentType.getInteger(ctx, "state")))))
                                .then(Commands.literal("left_leg")
                                        .then(Commands.argument("state", IntegerArgumentType.integer(0, 6))
                                                .executes(ctx -> apply(ctx.getSource(), true, "left_leg", IntegerArgumentType.getInteger(ctx, "state"))))))

                        .then(Commands.literal("muscle")
                                .then(Commands.literal("right_arm")
                                        .then(Commands.argument("state", IntegerArgumentType.integer(0, 3))
                                                .executes(ctx -> apply(ctx.getSource(), false, "right_arm", IntegerArgumentType.getInteger(ctx, "state")))))
                                .then(Commands.literal("left_arm")
                                        .then(Commands.argument("state", IntegerArgumentType.integer(0, 3))
                                                .executes(ctx -> apply(ctx.getSource(), false, "left_arm", IntegerArgumentType.getInteger(ctx, "state")))))
                                .then(Commands.literal("right_leg")
                                        .then(Commands.argument("state", IntegerArgumentType.integer(0, 3))
                                                .executes(ctx -> apply(ctx.getSource(), false, "right_leg", IntegerArgumentType.getInteger(ctx, "state")))))
                                .then(Commands.literal("left_leg")
                                        .then(Commands.argument("state", IntegerArgumentType.integer(0, 3))
                                                .executes(ctx -> apply(ctx.getSource(), false, "left_leg", IntegerArgumentType.getInteger(ctx, "state"))))))
        );
    }

    private static int apply(CommandSourceStack source, boolean isBone, String limb, int state) {
        try {
            ServerPlayer player = source.getPlayerOrException();
            boolean changed = isBone
                    ? switch (limb) {
                case "right_arm" -> LimbManager.setBoneRightArm(player, state);
                case "left_arm" -> LimbManager.setBoneLeftArm(player, state);
                case "right_leg" -> LimbManager.setBoneRightLeg(player, state);
                case "left_leg" -> LimbManager.setBoneLeftLeg(player, state);
                default -> false;
            }
                    : switch (limb) {
                case "right_arm" -> LimbManager.setMuscleRightArm(player, state);
                case "left_arm" -> LimbManager.setMuscleLeftArm(player, state);
                case "right_leg" -> LimbManager.setMuscleRightLeg(player, state);
                case "left_leg" -> LimbManager.setMuscleLeftLeg(player, state);
                default -> false;
            };

            if (!changed) {
                source.sendFailure(Component.literal("Capability недоступна."));
                return 0;
            }

            source.sendSuccess(() -> Component.literal(
                    (isBone ? "bone " : "muscle ") + limb + " -> " + state), true);
            return 1;
        } catch (Exception e) {
            source.sendFailure(Component.literal("Ошибка выполнения команды: " + e.getMessage()));
            return 0;
        }
    }
}