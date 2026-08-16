package com.KC.correctdamaged.command;

import com.KC.correctdamaged.capability.LimbManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class LimbCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("limb")
                        .requires(source -> source.hasPermission(2))

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

                        .then(Commands.literal("body")
                                .then(Commands.argument("state", IntegerArgumentType.integer(0, 9))
                                        .executes(ctx -> applyLimb(ctx.getSource(), "body", IntegerArgumentType.getInteger(ctx, "state")))))
        );

        dispatcher.register(
                Commands.literal("show")
                        .requires(source -> source.hasPermission(2))

                        // Управление слоями головы: /show head <skin|muscle|skull> <mask>
                        // И флаг обугленности: /show head burnt <true|false>
                        .then(Commands.literal("head")
                                .then(Commands.literal("skin")
                                        .then(Commands.argument("mask", IntegerArgumentType.integer(0, 255))
                                                .executes(ctx -> applyHeadMask(ctx.getSource(), "skin", IntegerArgumentType.getInteger(ctx, "mask")))))
                                .then(Commands.literal("muscle")
                                        .then(Commands.argument("mask", IntegerArgumentType.integer(0, 255))
                                                .executes(ctx -> applyHeadMask(ctx.getSource(), "muscle", IntegerArgumentType.getInteger(ctx, "mask")))))
                                .then(Commands.literal("skull")
                                        .then(Commands.argument("mask", IntegerArgumentType.integer(0, 255))
                                                .executes(ctx -> applyHeadMask(ctx.getSource(), "skull", IntegerArgumentType.getInteger(ctx, "mask")))))
                                .then(Commands.literal("burnt")
                                        .then(Commands.argument("burnt", BoolArgumentType.bool())
                                                .executes(ctx -> applyHeadBurnt(ctx.getSource(), BoolArgumentType.getBool(ctx, "burnt"))))))

                        // Ветка костей (без головы)
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
                                .then(Commands.literal("skeleton")
                                        .then(Commands.argument("state", IntegerArgumentType.integer(0, 2))
                                                .executes(ctx -> apply(ctx.getSource(), "bone", "skeleton", IntegerArgumentType.getInteger(ctx, "state"))))))

                        // Ветка мышц (без головы)
                        .then(Commands.literal("muscle")
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

    private static int applyLimb(CommandSourceStack source, String part, int state) {
        try {
            ServerPlayer player = source.getPlayerOrException();
            boolean changed = switch (part) {
                case "right_arm" -> LimbManager.setRightArm(player, state);
                case "left_arm" -> LimbManager.setLeftArm(player, state);
                case "right_leg" -> LimbManager.setRightLeg(player, state);
                case "left_leg" -> LimbManager.setLeftLeg(player, state);
                case "body" -> LimbManager.setBodyState(player, state);
                default -> false;
            };

            if (!changed) {
                source.sendFailure(Component.literal("Capability unavailable."));
                return 0;
            }

            source.sendSuccess(() -> Component.literal("The state has been established " + part + ": " + state), true);
            return 1;
        } catch (Exception e) {
            source.sendFailure(Component.literal("Command execution error: " + e.getMessage()));
            return 0;
        }
    }

    private static int applyHeadMask(CommandSourceStack source, String target, int mask) {
        try {
            ServerPlayer player = source.getPlayerOrException();
            byte byteMask = (byte) mask;

            boolean changed = switch (target) {
                case "skin" -> LimbManager.setHeadSkinMask(player, byteMask);
                case "muscle" -> LimbManager.setHeadMuscleMask(player, byteMask);
                case "skull" -> LimbManager.setHeadSkullMask(player, byteMask);
                default -> false;
            };

            if (!changed) {
                source.sendFailure(Component.literal("Capability is unavailable."));
                return 0;
            }

            source.sendSuccess(() -> Component.literal("Head " + target + " mask -> " + mask + " (0b" + Integer.toBinaryString(mask) + ")"), true);
            return 1;
        } catch (Exception e) {
            source.sendFailure(Component.literal("Command execution error: " + e.getMessage()));
            return 0;
        }
    }

    private static int applyHeadBurnt(CommandSourceStack source, boolean burnt) {
        try {
            ServerPlayer player = source.getPlayerOrException();
            boolean changed = LimbManager.setHeadBurntSkull(player, burnt);

            if (!changed) {
                source.sendFailure(Component.literal("Capability is unavailable."));
                return 0;
            }

            source.sendSuccess(() -> Component.literal("Head burnt skull status set to: " + burnt), true);
            return 1;
        } catch (Exception e) {
            source.sendFailure(Component.literal("Command execution error: " + e.getMessage()));
            return 0;
        }
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
                    case "skeleton" -> LimbManager.setShowSkeleton(player, state);
                    default -> false;
                };
            } else if ("muscle".equals(category)) {
                changed = switch (limb) {
                    case "body" -> LimbManager.setMuscleBody(player, state);
                    case "right_arm" -> LimbManager.setMuscleRightArm(player, state);
                    case "left_arm" -> LimbManager.setMuscleLeftArm(player, state);
                    case "right_leg" -> LimbManager.setMuscleRightLeg(player, state);
                    case "left_leg" -> LimbManager.setMuscleLeftLeg(player, state);
                    default -> false;
                };
            }

            if (!changed) {
                source.sendFailure(Component.literal("Capability is unavailable."));
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