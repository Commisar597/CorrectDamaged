package com.KC.correctdamaged.capability.visual;

import net.minecraft.nbt.CompoundTag;

/**
 * Хранит маски состояний кожи, мышц и черепа для головы игрока (битовые маски/флаги).
 */
public class HeadData {

    private byte skinMask = (byte) 0xFF;
    private byte muscleMask = (byte) 0;
    private byte skullMask = (byte) 0;
    private boolean burntSkull = false;

    public byte getSkinMask() {
        return skinMask;
    }

    public void setSkinMask(byte skinMask) {
        this.skinMask = skinMask;
    }

    public byte getMuscleMask() {
        return muscleMask;
    }

    public void setMuscleMask(byte muscleMask) {
        this.muscleMask = muscleMask;
    }

    public byte getSkullMask() {
        return skullMask;
    }

    public void setSkullMask(byte skullMask) {
        this.skullMask = skullMask;
    }

    public boolean isBurntSkull() {
        return burntSkull;
    }

    public void setBurntSkull(boolean burntSkull) {
        this.burntSkull = burntSkull;
    }

    public void copyFrom(HeadData source) {
        this.skinMask = source.skinMask;
        this.muscleMask = source.muscleMask;
        this.skullMask = source.skullMask;
        this.burntSkull = source.burntSkull;
    }

    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putByte("skin_mask", skinMask);
        tag.putByte("muscle_mask", muscleMask);
        tag.putByte("skull_mask", skullMask);
        tag.putBoolean("burnt_skull", burntSkull);
        return tag;
    }

    public void deserializeNBT(CompoundTag tag) {
        if (tag.contains("skin_mask")) skinMask = tag.getByte("skin_mask");
        if (tag.contains("muscle_mask")) muscleMask = tag.getByte("muscle_mask");
        if (tag.contains("skull_mask")) skullMask = tag.getByte("skull_mask");
        if (tag.contains("burnt_skull")) burntSkull = tag.getBoolean("burnt_skull");
    }
}