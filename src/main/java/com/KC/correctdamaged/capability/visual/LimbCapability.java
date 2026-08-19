package com.KC.correctdamaged.capability.visual;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

/**
 * Класс-держатель ссылки на экземпляр Capability для {@link LimbData}.
 */
public final class LimbCapability {

    public static final Capability<LimbData> INSTANCE =
            CapabilityManager.get(new CapabilityToken<>() {});

    private LimbCapability() {
    }
}