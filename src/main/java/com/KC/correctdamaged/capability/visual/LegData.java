package com.KC.correctdamaged.capability.visual;

import net.minecraft.nbt.CompoundTag;

/**
 * Хранит состояние слоя кожи, мышц и костей для конкретной ноги игрока.
 */
public class LegData {
    private final LimbSide side;

    private int thighSkin = 1;
    private int calfSkin = 1;
    private int footSkin = 1;

    private int muscleState = 0;
    private int boneState = 0;
    private boolean burntBone = false;

    public LegData(LimbSide side) {
        this.side = side;
    }

    public int getThighSkin() { return thighSkin; }
    public void setThighSkin(int state) { this.thighSkin = clamp(state, 0, 1); }

    public int getCalfSkin() { return calfSkin; }
    public void setCalfSkin(int state) { this.calfSkin = clamp(state, 0, 1); }

    public int getFootSkin() { return footSkin; }
    public void setFootSkin(int state) { this.footSkin = clamp(state, 0, 1); }

    public boolean hasThighSkin() { return thighSkin == 1; }
    public boolean hasCalfSkin() { return calfSkin == 1; }
    public boolean hasFootSkin() { return footSkin == 1; }

    public int getMuscleState() { return muscleState; }
    public void setMuscleState(int state) { this.muscleState = clamp(state, 0, 3); }

    public int getBoneState() { return boneState; }
    public void setBoneState(int state) { this.boneState = clamp(state, 0, 3); }

    public boolean isBurntBone() { return burntBone; }
    public void setBurntBone(boolean burntBone) { this.burntBone = burntBone; }

    public void copyFrom(LegData source) {
        this.thighSkin = source.thighSkin;
        this.calfSkin = source.calfSkin;
        this.footSkin = source.footSkin;
        this.muscleState = source.muscleState;
        this.boneState = source.boneState;
        this.burntBone = source.burntBone;
    }

    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("thigh_skin", thighSkin);
        tag.putInt("calf_skin", calfSkin);
        tag.putInt("foot_skin", footSkin);
        tag.putInt("muscle_state", muscleState);
        tag.putInt("bone_state", boneState);
        tag.putBoolean("burnt_bone", burntBone);
        return tag;
    }

    public void deserializeNBT(CompoundTag tag) {
        if (tag.contains("thigh_skin")) thighSkin = clamp(tag.getInt("thigh_skin"), 0, 1);
        if (tag.contains("calf_skin")) calfSkin = clamp(tag.getInt("calf_skin"), 0, 1);
        if (tag.contains("foot_skin")) footSkin = clamp(tag.getInt("foot_skin"), 0, 1);
        if (tag.contains("muscle_state")) muscleState = clamp(tag.getInt("muscle_state"), 0, 3);
        if (tag.contains("bone_state")) boneState = clamp(tag.getInt("bone_state"), 0, 3);
        if (tag.contains("burnt_bone")) burntBone = tag.getBoolean("burnt_bone");
    }

    private int clamp(int val, int min, int max) {
        return Math.max(min, Math.min(max, val));
    }
}