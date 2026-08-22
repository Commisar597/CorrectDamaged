package com.KC.correctdamaged.command;

import com.KC.correctdamaged.capability.LimbManager;
import com.KC.correctdamaged.logic.damage.preset.DamagePresetManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;

public class LimbCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {

        dispatcher.register(
                Commands.literal("correct")
                        .requires(source -> source.hasPermission(2))

                        .then(Commands.literal("limb")
                                .then(Commands.literal("right_arm")
                                        .then(Commands.literal("shoulder")
                                                .then(Commands.argument("state", IntegerArgumentType.integer(0, 1))
                                                        .executes(ctx -> applySkinSegment(ctx.getSource(), "right_arm_shoulder", IntegerArgumentType.getInteger(ctx, "state")))))
                                        .then(Commands.literal("forearm")
                                                .then(Commands.argument("state", IntegerArgumentType.integer(0, 1))
                                                        .executes(ctx -> applySkinSegment(ctx.getSource(), "right_arm_forearm", IntegerArgumentType.getInteger(ctx, "state")))))
                                        .then(Commands.literal("wrist")
                                                .then(Commands.argument("state", IntegerArgumentType.integer(0, 1))
                                                        .executes(ctx -> applySkinSegment(ctx.getSource(), "right_arm_wrist", IntegerArgumentType.getInteger(ctx, "state"))))))

                                .then(Commands.literal("left_arm")
                                        .then(Commands.literal("shoulder")
                                                .then(Commands.argument("state", IntegerArgumentType.integer(0, 1))
                                                        .executes(ctx -> applySkinSegment(ctx.getSource(), "left_arm_shoulder", IntegerArgumentType.getInteger(ctx, "state")))))
                                        .then(Commands.literal("forearm")
                                                .then(Commands.argument("state", IntegerArgumentType.integer(0, 1))
                                                        .executes(ctx -> applySkinSegment(ctx.getSource(), "left_arm_forearm", IntegerArgumentType.getInteger(ctx, "state")))))
                                        .then(Commands.literal("wrist")
                                                .then(Commands.argument("state", IntegerArgumentType.integer(0, 1))
                                                        .executes(ctx -> applySkinSegment(ctx.getSource(), "left_arm_wrist", IntegerArgumentType.getInteger(ctx, "state"))))))

                                .then(Commands.literal("right_leg")
                                        .then(Commands.literal("thigh")
                                                .then(Commands.argument("state", IntegerArgumentType.integer(0, 1))
                                                        .executes(ctx -> applySkinSegment(ctx.getSource(), "right_leg_thigh", IntegerArgumentType.getInteger(ctx, "state")))))
                                        .then(Commands.literal("calf")
                                                .then(Commands.argument("state", IntegerArgumentType.integer(0, 1))
                                                        .executes(ctx -> applySkinSegment(ctx.getSource(), "right_leg_calf", IntegerArgumentType.getInteger(ctx, "state")))))
                                        .then(Commands.literal("foot")
                                                .then(Commands.argument("state", IntegerArgumentType.integer(0, 1))
                                                        .executes(ctx -> applySkinSegment(ctx.getSource(), "right_leg_foot", IntegerArgumentType.getInteger(ctx, "state"))))))

                                .then(Commands.literal("left_leg")
                                        .then(Commands.literal("thigh")
                                                .then(Commands.argument("state", IntegerArgumentType.integer(0, 1))
                                                        .executes(ctx -> applySkinSegment(ctx.getSource(), "left_leg_thigh", IntegerArgumentType.getInteger(ctx, "state")))))
                                        .then(Commands.literal("calf")
                                                .then(Commands.argument("state", IntegerArgumentType.integer(0, 1))
                                                        .executes(ctx -> applySkinSegment(ctx.getSource(), "left_leg_calf", IntegerArgumentType.getInteger(ctx, "state")))))
                                        .then(Commands.literal("foot")
                                                .then(Commands.argument("state", IntegerArgumentType.integer(0, 1))
                                                        .executes(ctx -> applySkinSegment(ctx.getSource(), "left_leg_foot", IntegerArgumentType.getInteger(ctx, "state"))))))

                                .then(Commands.literal("body")
                                        .then(Commands.argument("state", IntegerArgumentType.integer(0, 1))
                                                .executes(ctx -> applySkinSegment(ctx.getSource(), "body", IntegerArgumentType.getInteger(ctx, "state"))))))

                        .then(Commands.literal("damage")
                                .then(Commands.literal("voxel")
                                        .then(Commands.literal("body")
                                                .then(Commands.argument("preset", StringArgumentType.word())
                                                        .suggests((ctx, builder) -> {
                                                            List<String> names = new ArrayList<>(DamagePresetManager.getAllPresets().keySet());
                                                            names.add("reset");
                                                            return SharedSuggestionProvider.suggest(names, builder);
                                                        })
                                                        .executes(ctx -> applyVoxelPreset(ctx.getSource(),
                                                                StringArgumentType.getString(ctx, "preset")))))))

                        .then(Commands.literal("show")
                                .then(Commands.literal("organs")
                                        .then(Commands.literal("visible")
                                                .then(Commands.argument("state", IntegerArgumentType.integer(0, 1))
                                                        .executes(ctx -> applyOrgansVisible(ctx.getSource(), IntegerArgumentType.getInteger(ctx, "state")))))
                                        .then(Commands.literal("heart")
                                                .then(Commands.argument("state", IntegerArgumentType.integer(0, 1))
                                                        .executes(ctx -> applyOrganState(ctx.getSource(), "heart", IntegerArgumentType.getInteger(ctx, "state")))))
                                        .then(Commands.literal("left_lung")
                                                .then(Commands.argument("state", IntegerArgumentType.integer(0, 1))
                                                        .executes(ctx -> applyOrganState(ctx.getSource(), "left_lung", IntegerArgumentType.getInteger(ctx, "state")))))
                                        .then(Commands.literal("right_lung")
                                                .then(Commands.argument("state", IntegerArgumentType.integer(0, 1))
                                                        .executes(ctx -> applyOrganState(ctx.getSource(), "right_lung", IntegerArgumentType.getInteger(ctx, "state")))))
                                        .then(Commands.literal("liver")
                                                .then(Commands.argument("state", IntegerArgumentType.integer(0, 1))
                                                        .executes(ctx -> applyOrganState(ctx.getSource(), "liver", IntegerArgumentType.getInteger(ctx, "state")))))
                                        .then(Commands.literal("git")
                                                .then(Commands.argument("state", IntegerArgumentType.integer(0, 1))
                                                        .executes(ctx -> applyOrganState(ctx.getSource(), "git", IntegerArgumentType.getInteger(ctx, "state"))))))

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

                                .then(Commands.literal("bone")
                                        .then(Commands.literal("right_arm")
                                                .then(Commands.argument("state", IntegerArgumentType.integer(0, 3))
                                                        .executes(ctx -> applyBoneOrMuscle(ctx.getSource(), "bone", "right_arm", IntegerArgumentType.getInteger(ctx, "state")))))
                                        .then(Commands.literal("left_arm")
                                                .then(Commands.argument("state", IntegerArgumentType.integer(0, 3))
                                                        .executes(ctx -> applyBoneOrMuscle(ctx.getSource(), "bone", "left_arm", IntegerArgumentType.getInteger(ctx, "state")))))
                                        .then(Commands.literal("right_leg")
                                                .then(Commands.argument("state", IntegerArgumentType.integer(0, 3))
                                                        .executes(ctx -> applyBoneOrMuscle(ctx.getSource(), "bone", "right_leg", IntegerArgumentType.getInteger(ctx, "state")))))
                                        .then(Commands.literal("left_leg")
                                                .then(Commands.argument("state", IntegerArgumentType.integer(0, 3))
                                                        .executes(ctx -> applyBoneOrMuscle(ctx.getSource(), "bone", "left_leg", IntegerArgumentType.getInteger(ctx, "state")))))
                                        .then(Commands.literal("skeleton")
                                                .then(Commands.argument("state", IntegerArgumentType.integer(0, 2))
                                                        .executes(ctx -> applyBoneOrMuscle(ctx.getSource(), "bone", "skeleton", IntegerArgumentType.getInteger(ctx, "state")))))

                                        .then(Commands.literal("burnt")
                                                .then(Commands.literal("skeleton")
                                                        .then(Commands.argument("burnt", BoolArgumentType.bool())
                                                                .executes(ctx -> applyBurntSkeleton(ctx.getSource(), BoolArgumentType.getBool(ctx, "burnt")))))
                                                .then(Commands.literal("right_arm")
                                                        .then(Commands.argument("burnt", BoolArgumentType.bool())
                                                                .executes(ctx -> applyBurntBone(ctx.getSource(), "right_arm", BoolArgumentType.getBool(ctx, "burnt")))))
                                                .then(Commands.literal("left_arm")
                                                        .then(Commands.argument("burnt", BoolArgumentType.bool())
                                                                .executes(ctx -> applyBurntBone(ctx.getSource(), "left_arm", BoolArgumentType.getBool(ctx, "burnt")))))
                                                .then(Commands.literal("right_leg")
                                                        .then(Commands.argument("burnt", BoolArgumentType.bool())
                                                                .executes(ctx -> applyBurntBone(ctx.getSource(), "right_leg", BoolArgumentType.getBool(ctx, "burnt")))))
                                                .then(Commands.literal("left_leg")
                                                        .then(Commands.argument("burnt", BoolArgumentType.bool())
                                                                .executes(ctx -> applyBurntBone(ctx.getSource(), "left_leg", BoolArgumentType.getBool(ctx, "burnt")))))))

                                .then(Commands.literal("muscle")
                                        .then(Commands.literal("body")
                                                .then(Commands.argument("state", IntegerArgumentType.integer(0, 1))
                                                        .executes(ctx -> applyBoneOrMuscle(ctx.getSource(), "muscle", "body", IntegerArgumentType.getInteger(ctx, "state")))))
                                        .then(Commands.literal("right_arm")
                                                .then(Commands.argument("state", IntegerArgumentType.integer(0, 3))
                                                        .executes(ctx -> applyBoneOrMuscle(ctx.getSource(), "muscle", "right_arm", IntegerArgumentType.getInteger(ctx, "state")))))
                                        .then(Commands.literal("left_arm")
                                                .then(Commands.argument("state", IntegerArgumentType.integer(0, 3))
                                                        .executes(ctx -> applyBoneOrMuscle(ctx.getSource(), "muscle", "left_arm", IntegerArgumentType.getInteger(ctx, "state")))))
                                        .then(Commands.literal("right_leg")
                                                .then(Commands.argument("state", IntegerArgumentType.integer(0, 3))
                                                        .executes(ctx -> applyBoneOrMuscle(ctx.getSource(), "muscle", "right_leg", IntegerArgumentType.getInteger(ctx, "state")))))
                                        .then(Commands.literal("left_leg")
                                                .then(Commands.argument("state", IntegerArgumentType.integer(0, 3))
                                                        .executes(ctx -> applyBoneOrMuscle(ctx.getSource(), "muscle", "left_leg", IntegerArgumentType.getInteger(ctx, "state")))))))
        );
    }

    private static int applyOrgansVisible(CommandSourceStack source, int state) {
        try {
            ServerPlayer player = source.getPlayerOrException();
            boolean changed = LimbManager.setOrgansVisible(player, state);

            if (!changed) {
                source.sendFailure(Component.literal("Capability is unavailable."));
                return 0;
            }

            source.sendSuccess(() -> Component.literal("Organs visibility -> " + state), true);
            return 1;
        } catch (Exception e) {
            source.sendFailure(Component.literal("Command execution error: " + e.getMessage()));
            return 0;
        }
    }

    private static int applyOrganState(CommandSourceStack source, String organ, int state) {
        try {
            ServerPlayer player = source.getPlayerOrException();
            boolean changed = switch (organ) {
                case "heart" -> LimbManager.setOrganHeart(player, state);
                case "left_lung" -> LimbManager.setOrganLeftLung(player, state);
                case "right_lung" -> LimbManager.setOrganRightLung(player, state);
                case "liver" -> LimbManager.setOrganLiver(player, state);
                case "git" -> LimbManager.setOrganGit(player, state);
                default -> false;
            };

            if (!changed) {
                source.sendFailure(Component.literal("Capability is unavailable."));
                return 0;
            }

            source.sendSuccess(() -> Component.literal("Organ " + organ + " state -> " + state), true);
            return 1;
        } catch (Exception e) {
            source.sendFailure(Component.literal("Command execution error: " + e.getMessage()));
            return 0;
        }
    }

    private static int applyVoxelPreset(CommandSourceStack source, String preset) {
        try {
            ServerPlayer player = source.getPlayerOrException();
            boolean changed = LimbManager.applyBodyVoxelPreset(player, preset);

            if (!changed) {
                source.sendFailure(Component.literal("Capability is unavailable."));
                return 0;
            }

            source.sendSuccess(() -> Component.literal("Applied voxel preset: " + preset), true);
            return 1;
        } catch (Exception e) {
            source.sendFailure(Component.literal("Command execution error: " + e.getMessage()));
            return 0;
        }
    }

    private static int applySkinSegment(CommandSourceStack source, String segment, int state) {
        try {
            ServerPlayer player = source.getPlayerOrException();
            boolean changed = switch (segment) {
                case "right_arm_shoulder" -> LimbManager.setRightArmShoulderSkin(player, state);
                case "right_arm_forearm" -> LimbManager.setRightArmForearmSkin(player, state);
                case "right_arm_wrist" -> LimbManager.setRightArmWristSkin(player, state);

                case "left_arm_shoulder" -> LimbManager.setLeftArmShoulderSkin(player, state);
                case "left_arm_forearm" -> LimbManager.setLeftArmForearmSkin(player, state);
                case "left_arm_wrist" -> LimbManager.setLeftArmWristSkin(player, state);

                case "right_leg_thigh" -> LimbManager.setRightLegThighSkin(player, state);
                case "right_leg_calf" -> LimbManager.setRightLegCalfSkin(player, state);
                case "right_leg_foot" -> LimbManager.setRightLegFootSkin(player, state);

                case "left_leg_thigh" -> LimbManager.setLeftLegThighSkin(player, state);
                case "left_leg_calf" -> LimbManager.setLeftLegCalfSkin(player, state);
                case "left_leg_foot" -> LimbManager.setLeftLegFootSkin(player, state);

                case "body" -> LimbManager.setBodySkinMask(player, state);
                default -> false;
            };

            if (!changed) {
                source.sendFailure(Component.literal("Capability is unavailable."));
                return 0;
            }

            source.sendSuccess(() -> Component.literal("Segment " + segment + " -> " + state), true);
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

            source.sendSuccess(() -> Component.literal("Head burnt skull set to: " + burnt), true);
            return 1;
        } catch (Exception e) {
            source.sendFailure(Component.literal("Command execution error: " + e.getMessage()));
            return 0;
        }
    }

    private static int applyBoneOrMuscle(CommandSourceStack source, String category, String limb, int state) {
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

    private static int applyBurntBone(CommandSourceStack source, String limb, boolean burnt) {
        try {
            ServerPlayer player = source.getPlayerOrException();
            boolean changed = switch (limb) {
                case "right_arm" -> LimbManager.setBurntBoneRightArm(player, burnt);
                case "left_arm" -> LimbManager.setBurntBoneLeftArm(player, burnt);
                case "right_leg" -> LimbManager.setBurntBoneRightLeg(player, burnt);
                case "left_leg" -> LimbManager.setBurntBoneLeftLeg(player, burnt);
                default -> false;
            };

            if (!changed) {
                source.sendFailure(Component.literal("Capability is unavailable."));
                return 0;
            }

            source.sendSuccess(() -> Component.literal("Burnt bone " + limb + " -> " + burnt), true);
            return 1;
        } catch (Exception e) {
            source.sendFailure(Component.literal("Command execution error: " + e.getMessage()));
            return 0;
        }
    }

    private static int applyBurntSkeleton(CommandSourceStack source, boolean burnt) {
        try {
            ServerPlayer player = source.getPlayerOrException();
            boolean changed = LimbManager.setBurntSkeleton(player, burnt);

            if (!changed) {
                source.sendFailure(Component.literal("Capability is unavailable."));
                return 0;
            }

            source.sendSuccess(() -> Component.literal("Burnt skeleton -> " + burnt), true);
            return 1;
        } catch (Exception e) {
            source.sendFailure(Component.literal("Command execution error: " + e.getMessage()));
            return 0;
        }
    }
}