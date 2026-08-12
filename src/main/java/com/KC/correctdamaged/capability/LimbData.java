package com.KC.correctdamaged.capability;

import net.minecraft.nbt.CompoundTag;

public class LimbData {

    private int rightArm = 3;
    private int leftArm = 3;
    private int rightLeg = 3;
    private int leftLeg = 3;
    private int headState = 5;
    private int bodyState = 9;

    public int getRightArm() { return rightArm; }
    public int getLeftArm() { return leftArm; }
    public int getRightLeg() { return rightLeg; }
    public int getLeftLeg() { return leftLeg; }
    public int getHeadState() { return headState; }
    public int getBodyState() { return bodyState; }

    public void setRightArm(int state) { this.rightArm = normalizeState(state, 0, 3); }
    public void setLeftArm(int state) { this.leftArm = normalizeState(state, 0, 3); }
    public void setRightLeg(int state) { this.rightLeg = normalizeState(state, 0, 3); }
    public void setLeftLeg(int state) { this.leftLeg = normalizeState(state, 0, 3); }
    public void setHeadState(int state) { this.headState = normalizeState(state, 0, 5); }
    public void setBodyState(int state) { this.bodyState = normalizeState(state, 0, 9); }

    public void copyFrom(LimbData source) {
        this.rightArm = source.rightArm;
        this.leftArm = source.leftArm;
        this.rightLeg = source.rightLeg;
        this.leftLeg = source.leftLeg;
        this.headState = source.headState;
        this.bodyState = source.bodyState;
    }

    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("right_arm", rightArm);
        tag.putInt("left_arm", leftArm);
        tag.putInt("right_leg", rightLeg);
        tag.putInt("left_leg", leftLeg);
        tag.putInt("head_state", headState);
        tag.putInt("body_state", bodyState);
        return tag;
    }

    public void deserializeNBT(CompoundTag tag) {
        if (tag.contains("right_arm")) rightArm = normalizeState(tag.getInt("right_arm"), 0, 3);
        if (tag.contains("left_arm")) leftArm = normalizeState(tag.getInt("left_arm"), 0, 3);
        if (tag.contains("right_leg")) rightLeg = normalizeState(tag.getInt("right_leg"), 0, 3);
        if (tag.contains("left_leg")) leftLeg = normalizeState(tag.getInt("left_leg"), 0, 3);
        if (tag.contains("head_state")) headState = normalizeState(tag.getInt("head_state"), 0, 5);
        if (tag.contains("body_state")) bodyState = normalizeState(tag.getInt("body_state"), 0, 9);
    }

    private int normalizeState(int state, int min, int max) {
        return Math.max(min, Math.min(max, state));
    }
}