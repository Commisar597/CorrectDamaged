package com.KC.correctdamaged;

import com.KC.correctdamaged.network.PacketHandler;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import com.mojang.logging.LogUtils;

import org.slf4j.Logger;

@Mod(CorrectDamaged.MODID)
public class CorrectDamaged {

    public static final String MODID = "correct_damaged";

    public static final Logger LOGGER =
            LogUtils.getLogger();

    public CorrectDamaged() {

        IEventBus modEventBus =
                FMLJavaModLoadingContext.get()
                        .getModEventBus();

        MinecraftForge.EVENT_BUS.register(this);

        ModLoadingContext.get().registerConfig(
                ModConfig.Type.COMMON,
                Config.SPEC
        );

        PacketHandler.init();

        LOGGER.info("Correct Damaged initialized.");
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }
}