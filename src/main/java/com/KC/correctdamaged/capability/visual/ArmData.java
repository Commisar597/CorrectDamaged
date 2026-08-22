package com.KC.correctdamaged.capability.visual;

import net.minecraft.nbt.CompoundTag;

public class ArmData {

    private final LimbSide side;

    private int shoulderSkin = 1;
    private int forearmSkin = 1;
    private int wristSkin = 1;

    private int muscleState = 0;
    private int boneState = 0;
    private boolean burntBone = false;

    public ArmData(LimbSide side) {
        this.side = side;
    }

    public void setShoulderSkin(int state) {
        this.shoulderSkin = clamp(state, 0, 1);
    }

    public void setForearmSkin(int state) {
        this.forearmSkin = clamp(state, 0, 1);
    }

    public void setWristSkin(int state) {
        this.wristSkin = clamp(state, 0, 1);
    }

    public boolean hasShoulderSkin() {
        return shoulderSkin == 1;
    }

    public boolean hasForearmSkin() {
        return forearmSkin == 1;
    }

    public boolean hasWristSkin() {
        return wristSkin == 1;
    }

    public int getMuscleState() {
        return muscleState;
    }

    public void setMuscleState(int state) {
        this.muscleState = clamp(state, 0, 3);
    }

    public int getBoneState() {
        return boneState;
    }

    public void setBoneState(int state) {
        this.boneState = clamp(state, 0, 3);
    }

    public boolean isBurntBone() {
        return burntBone;
    }

    public void setBurntBone(boolean burntBone) {
        this.burntBone = burntBone;
    }

    public void copyFrom(ArmData source) {
        this.shoulderSkin = source.shoulderSkin;
        this.forearmSkin = source.forearmSkin;
        this.wristSkin = source.wristSkin;
        this.muscleState = source.muscleState;
        this.boneState = source.boneState;
        this.burntBone = source.burntBone;
    }

    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("shoulder_skin", shoulderSkin);
        tag.putInt("forearm_skin", forearmSkin);
        tag.putInt("wrist_skin", wristSkin);
        tag.putInt("muscle_state", muscleState);
        tag.putInt("bone_state", boneState);
        tag.putBoolean("burnt_bone", burntBone);
        return tag;
    }

    public void deserializeNBT(CompoundTag tag) {
        if (tag.contains("shoulder_skin")) shoulderSkin = clamp(tag.getInt("shoulder_skin"), 0, 1);
        if (tag.contains("forearm_skin")) forearmSkin = clamp(tag.getInt("forearm_skin"), 0, 1);
        if (tag.contains("wrist_skin")) wristSkin = clamp(tag.getInt("wrist_skin"), 0, 1);
        if (tag.contains("muscle_state")) muscleState = clamp(tag.getInt("muscle_state"), 0, 3);
        if (tag.contains("bone_state")) boneState = clamp(tag.getInt("bone_state"), 0, 3);
        if (tag.contains("burnt_bone")) burntBone = tag.getBoolean("burnt_bone");
    }

    private int clamp(int val, int min, int max) {
        return Math.max(min, Math.min(max, val));
    }
}