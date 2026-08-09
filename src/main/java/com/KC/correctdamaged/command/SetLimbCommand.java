package com.KC.correctdamaged.command;

import com.KC.correctdamaged.capability.LimbManager;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class SetLimbCommand {

    public static void register(
            CommandDispatcher<CommandSourceStack> dispatcher
    ) {

        dispatcher.register(
                Commands.literal("limb")
                        .requires(source -> source.hasPermission(2))

                        .then(
                                Commands.argument(
                                                "part",
                                                StringArgumentType.word()
                                        )
                                        .then(
                                                Commands.argument(
                                                                "state",
                                                                IntegerArgumentType.integer(0, 3)
                                                        )
                                                        .executes(context -> {

                                                            ServerPlayer player =
                                                                    context.getSource()
                                                                            .getPlayerOrException();

                                                            String part =
                                                                    StringArgumentType.getString(
                                                                            context,
                                                                            "part"
                                                                    ).toLowerCase();

                                                            int state =
                                                                    IntegerArgumentType.getInteger(
                                                                            context,
                                                                            "state"
                                                                    );

                                                            boolean changed = switch (part) {

                                                                case "right_arm" ->
                                                                        LimbManager.setRightArm(
                                                                                player,
                                                                                state
                                                                        );

                                                                case "left_arm" ->
                                                                        LimbManager.setLeftArm(
                                                                                player,
                                                                                state
                                                                        );

                                                                case "right_leg" ->
                                                                        LimbManager.setRightLeg(
                                                                                player,
                                                                                state
                                                                        );

                                                                case "left_leg" ->
                                                                        LimbManager.setLeftLeg(
                                                                                player,
                                                                                state
                                                                        );

                                                                default -> {
                                                                    context.getSource().sendFailure(
                                                                            Component.literal(
                                                                                    "Неизвестная конечность: "
                                                                                            + part
                                                                            )
                                                                    );

                                                                    yield false;
                                                                }
                                                            };

                                                            if (!changed) {
                                                                context.getSource().sendFailure(
                                                                        Component.literal(
                                                                                "Capability конечностей недоступна."
                                                                        )
                                                                );

                                                                return 0;
                                                            }

                                                            context.getSource().sendSuccess(
                                                                    () -> Component.literal(
                                                                            "Установлено состояние "
                                                                                    + part
                                                                                    + ": "
                                                                                    + state
                                                                    ),
                                                                    true
                                                            );

                                                            return 1;
                                                        })
                                        )
                        )
        );
    }
}