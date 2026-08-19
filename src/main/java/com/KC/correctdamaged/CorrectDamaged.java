package com.KC.correctdamaged;

import com.KC.correctdamaged.network.PacketHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

@Mod(CorrectDamaged.MODID)
public class CorrectDamaged {

    public static final String MODID = "correct_damaged";
    public static final Logger LOGGER = LogUtils.getLogger();

    /**
     * Конструктор главного класса мода. Вызывается Forge при загрузке.
     * Зачем нужен: Регистрирует подписки на события Forge, конфигурации мода
     * и инициализирует сетевую систему пакетов.
     */
    public CorrectDamaged() {
        // Регистрирует этот класс на основном шине событий (FORGE bus) для отслеживания игровых событий
        MinecraftForge.EVENT_BUS.register(this);

        // Регистрирует общие настройки мода (Common Config)
        ModLoadingContext.get().registerConfig(
                ModConfig.Type.COMMON,
                Config.SPEC
        );

        // Инициализирует регистрацию сетевых пакетов
        PacketHandler.init();

        LOGGER.info("Correct Damaged initialized.");
    }

    /**
     * Обработчик события запуска сервера (ServerStartingEvent).
     * Зачем нужен: Выполняет логику, которая должна сработать сразу при старте игрового мира/сервера.
     * Что делает: На данный момент пуст, служит заготовкой под будущий функционал.
     *
     * @param event Контекст события запуска сервера.
     */
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }
}