package com.KC.correctdamaged.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class LimbCapabilityProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    private final LimbData data = new LimbData();

    private final LazyOptional<LimbData> optional =
            LazyOptional.of(() -> data);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(
            @Nonnull net.minecraftforge.common.capabilities.Capability<T> capability,
            @Nullable Direction side
    ) {
        if (capability == LimbCapability.INSTANCE) {
            return optional.cast();
        }

        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        return data.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        data.deserializeNBT(nbt);
    }
}