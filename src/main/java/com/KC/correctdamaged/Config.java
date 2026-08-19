package com.KC.correctdamaged;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = CorrectDamaged.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    static final ForgeConfigSpec SPEC = BUILDER.build();

    /**
     * Обработчик события загрузки или перезагрузки файла конфигурации (ModConfigEvent).
     * Зачем нужен: Позволяет синхронизировать Java-переменные с изменениями в config-файле .toml.
     * Что делает: Сейчас пуст, так как в билдере нет прописанных параметров.
     *
     * @param event Контекст события конфигурации.
     */
    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
    }
}