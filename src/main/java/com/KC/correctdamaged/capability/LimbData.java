package com.KC.correctdamaged.capability;

import net.minecraft.nbt.CompoundTag;

public class LimbData {

    /**
     * Состояние конечности:
     *
     * 3 = конечность целиком
     * 2 = осталось 2/3
     * 1 = осталось 1/3
     * 0 = конечность полностью отсутствует
     */

    private int rightArm = 3;
    private int leftArm = 3;
    private int rightLeg = 3;
    private int leftLeg = 3;

    // =========================
    // GETTERS
    // =========================

    public int getRightArm() {
        return rightArm;
    }

    public int getLeftArm() {
        return leftArm;
    }

    public int getRightLeg() {
        return rightLeg;
    }

    public int getLeftLeg() {
        return leftLeg;
    }

    // =========================
    // SETTERS
    // =========================

    public void setRightArm(int state) {
        this.rightArm = normalizeState(state);
    }

    public void setLeftArm(int state) {
        this.leftArm = normalizeState(state);
    }

    public void setRightLeg(int state) {
        this.rightLeg = normalizeState(state);
    }

    public void setLeftLeg(int state) {
        this.leftLeg = normalizeState(state);
    }

    // =========================
    // COPY
    // =========================

    public void copyFrom(LimbData source) {
        this.rightArm = source.rightArm;
        this.leftArm = source.leftArm;
        this.rightLeg = source.rightLeg;
        this.leftLeg = source.leftLeg;
    }

    // =========================
    // NBT
    // =========================

    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();

        tag.putInt("right_arm", rightArm);
        tag.putInt("left_arm", leftArm);
        tag.putInt("right_leg", rightLeg);
        tag.putInt("left_leg", leftLeg);

        return tag;
    }

    public void deserializeNBT(CompoundTag tag) {
        if (tag.contains("right_arm")) {
            rightArm = normalizeState(tag.getInt("right_arm"));
        }

        if (tag.contains("left_arm")) {
            leftArm = normalizeState(tag.getInt("left_arm"));
        }

        if (tag.contains("right_leg")) {
            rightLeg = normalizeState(tag.getInt("right_leg"));
        }

        if (tag.contains("left_leg")) {
            leftLeg = normalizeState(tag.getInt("left_leg"));
        }
    }

    // =========================
    // INTERNAL
    // =========================

    private int normalizeState(int state) {
        return Math.max(0, Math.min(3, state));
    }
}