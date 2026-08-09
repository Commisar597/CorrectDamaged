package com.KC.correctdamaged.capability;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public final class LimbCapability {

    public static final Capability<LimbData> INSTANCE =
            CapabilityManager.get(new CapabilityToken<>() {});

    private LimbCapability() {
    }
}