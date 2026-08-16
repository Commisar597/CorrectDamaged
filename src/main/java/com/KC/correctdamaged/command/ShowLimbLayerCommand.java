package com.KC.correctdamaged.command;

import com.KC.correctdamaged.capability.LimbManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class ShowLimbLayerCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("show")
                        .requires(source -> source.hasPermission(2))

                        .then(Commands.literal("bone")
                                .then(Commands.literal("right_arm")
                                        .then(Commands.argument("state", IntegerArgumentType.integer(0, 6))
                                                .executes(ctx -> apply(ctx.getSource(), "bone", "right_arm", IntegerArgumentType.getInteger(ctx, "state")))))
                                .then(Commands.literal("left_arm")
                                        .then(Commands.argument("state", IntegerArgumentType.integer(0, 6))
                                                .executes(ctx -> apply(ctx.getSource(), "bone", "left_arm", IntegerArgumentType.getInteger(ctx, "state")))))
                                .then(Commands.literal("right_leg")
                                        .then(Commands.argument("state", IntegerArgumentType.integer(0, 6))
                                                .executes(ctx -> apply(ctx.getSource(), "bone", "right_leg", IntegerArgumentType.getInteger(ctx, "state")))))
                                .then(Commands.literal("left_leg")
                                        .then(Commands.argument("state", IntegerArgumentType.integer(0, 6))
                                                .executes(ctx -> apply(ctx.getSource(), "bone", "left_leg", IntegerArgumentType.getInteger(ctx, "state")))))
                                .then(Commands.literal("skull")
                                        .then(Commands.argument("state", IntegerArgumentType.integer(0, 2))
                                                .executes(ctx -> apply(ctx.getSource(), "bone", "skull", IntegerArgumentType.getInteger(ctx, "state")))))
                                .then(Commands.literal("skeleton")
                                        .then(Commands.argument("state", IntegerArgumentType.integer(0, 2))
                                                .executes(ctx -> apply(ctx.getSource(), "bone", "skeleton", IntegerArgumentType.getInteger(ctx, "state"))))))

                        .then(Commands.literal("muscle")
                                .then(Commands.literal("head")
                                        .then(Commands.argument("state", IntegerArgumentType.integer(0, 1))
                                                .executes(ctx -> apply(ctx.getSource(), "muscle", "head", IntegerArgumentType.getInteger(ctx, "state")))))
                                .then(Commands.literal("body")
                                        .then(Commands.argument("state", IntegerArgumentType.integer(0, 1))
                                                .executes(ctx -> apply(ctx.getSource(), "muscle", "body", IntegerArgumentType.getInteger(ctx, "state")))))
                                .then(Commands.literal("right_arm")
                                        .then(Commands.argument("state", IntegerArgumentType.integer(0, 3))
                                                .executes(ctx -> apply(ctx.getSource(), "muscle", "right_arm", IntegerArgumentType.getInteger(ctx, "state")))))
                                .then(Commands.literal("left_arm")
                                        .then(Commands.argument("state", IntegerArgumentType.integer(0, 3))
                                                .executes(ctx -> apply(ctx.getSource(), "muscle", "left_arm", IntegerArgumentType.getInteger(ctx, "state")))))
                                .then(Commands.literal("right_leg")
                                        .then(Commands.argument("state", IntegerArgumentType.integer(0, 3))
                                                .executes(ctx -> apply(ctx.getSource(), "muscle", "right_leg", IntegerArgumentType.getInteger(ctx, "state")))))
                                .then(Commands.literal("left_leg")
                                        .then(Commands.argument("state", IntegerArgumentType.integer(0, 3))
                                                .executes(ctx -> apply(ctx.getSource(), "muscle", "left_leg", IntegerArgumentType.getInteger(ctx, "state"))))))
        );
    }

    private static int apply(CommandSourceStack source, String category, String limb, int state) {
        try {
            ServerPlayer player = source.getPlayerOrException();
            boolean changed = false;

            if ("bone".equals(category)) {
                changed = switch (limb) {
                    case "right_arm" -> LimbManager.setBoneRightArm(player, state);
                    case "left_arm" -> LimbManager.setBoneLeftArm(player, state);
                    case "right_leg" -> LimbManager.setBoneRightLeg(player, state);
                    case "left_leg" -> LimbManager.setBoneLeftLeg(player, state);
                    case "skull" -> LimbManager.setShowSkull(player, state);
                    case "skeleton" -> LimbManager.setShowSkeleton(player, state);
                    default -> false;
                };
            } else if ("muscle".equals(category)) {
                changed = switch (limb) {
                    case "head" -> LimbManager.setMuscleHead(player, state);
                    case "body" -> LimbManager.setMuscleBody(player, state);
                    case "right_arm" -> LimbManager.setMuscleRightArm(player, state);
                    case "left_arm" -> LimbManager.setMuscleLeftArm(player, state);
                    case "right_leg" -> LimbManager.setMuscleRightLeg(player, state);
                    case "left_leg" -> LimbManager.setMuscleLeftLeg(player, state);
                    default -> false;
                };
            }

            if (!changed) {
                source.sendFailure(Component.literal("Capability is unavailable.."));
                return 0;
            }

            source.sendSuccess(() -> Component.literal(category + " " + limb + " -> " + state), true);
            return 1;
        } catch (Exception e) {
            source.sendFailure(Component.literal("Command execution error: " + e.getMessage()));
            return 0;
        }
    }
}