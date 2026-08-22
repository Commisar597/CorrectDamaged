package com.KC.correctdamaged.logic.damage.preset;

import com.KC.correctdamaged.CorrectDamaged;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = CorrectDamaged.MODID)
public class DamagePresetManager extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static final Map<String, DamagePreset> PRESETS = new HashMap<>();

    public DamagePresetManager() {
        super(GSON, "damage_presets");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> objects, ResourceManager resourceManager, ProfilerFiller profiler) {
        PRESETS.clear();

        objects.forEach((location, json) -> {
            try {
                DamagePreset preset = GSON.fromJson(json, DamagePreset.class);
                String key = location.getPath().toLowerCase();
                PRESETS.put(key, preset);
                CorrectDamaged.LOGGER.info("Loaded damage preset: {} -> {}", location, key);
            } catch (Exception e) {
                CorrectDamaged.LOGGER.error("Failed to parse damage preset JSON at: {}", location, e);
            }
        });
    }

    public static DamagePreset getPreset(String name) {
        return PRESETS.get(name.toLowerCase());
    }

    public static Map<String, DamagePreset> getAllPresets() {
        return Collections.unmodifiableMap(PRESETS);
    }

    @SubscribeEvent
    public static void onAddReloadListeners(AddReloadListenerEvent event) {
        event.addListener(new DamagePresetManager());
    }
}