package com.KC.correctdamaged.capability.visual;

import com.KC.correctdamaged.capability.LimbManager;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class LimbCapabilityProvider implements ICapabilitySerializable<CompoundTag> {

    private final LimbData limbData = new LimbData();
    private final LazyOptional<LimbData> optionalData = LazyOptional.of(() -> limbData);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == LimbManager.LIMB_DATA_CAP) {
            return optionalData.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        return limbData.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        limbData.deserializeNBT(nbt);
    }
}