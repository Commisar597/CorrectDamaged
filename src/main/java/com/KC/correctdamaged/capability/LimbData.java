package com.KC.correctdamaged.capability;

import net.minecraft.nbt.CompoundTag;

public class LimbData {

    private int rightArm = 3;
    private int leftArm = 3;
    private int rightLeg = 3;
    private int leftLeg = 3;
    private int headState = 5;
    private int bodyState = 9;

    private int boneRightArm = 0;
    private int boneLeftArm = 0;
    private int boneRightLeg = 0;
    private int boneLeftLeg = 0;
    private int showSkull = 0;
    private int showSkeleton = 0;

    private int muscleRightArm = 0;
    private int muscleLeftArm = 0;
    private int muscleRightLeg = 0;
    private int muscleLeftLeg = 0;
    private int muscleHead = 0;
    private int muscleBody = 0;

    public int getRightArm() { return rightArm; }
    public int getLeftArm() { return leftArm; }
    public int getRightLeg() { return rightLeg; }
    public int getLeftLeg() { return leftLeg; }
    public int getHeadState() { return headState; }
    public int getBodyState() { return bodyState; }

    public int getBoneRightArm() { return boneRightArm; }
    public int getBoneLeftArm() { return boneLeftArm; }
    public int getBoneRightLeg() { return boneRightLeg; }
    public int getBoneLeftLeg() { return boneLeftLeg; }
    public int getShowSkull() { return showSkull; }
    public int getShowSkeleton() { return showSkeleton; }

    public int getMuscleRightArm() { return muscleRightArm; }
    public int getMuscleLeftArm() { return muscleLeftArm; }
    public int getMuscleRightLeg() { return muscleRightLeg; }
    public int getMuscleLeftLeg() { return muscleLeftLeg; }
    public int getMuscleHead() { return muscleHead; }
    public int getMuscleBody() { return muscleBody; }

    public void setRightArm(int state) { this.rightArm = normalizeState(state, 0, 3); }
    public void setLeftArm(int state) { this.leftArm = normalizeState(state, 0, 3); }
    public void setRightLeg(int state) { this.rightLeg = normalizeState(state, 0, 3); }
    public void setLeftLeg(int state) { this.leftLeg = normalizeState(state, 0, 3); }
    public void setHeadState(int state) { this.headState = normalizeState(state, 0, 5); }
    public void setBodyState(int state) { this.bodyState = normalizeState(state, 0, 9); }

    public void setBoneRightArm(int state) { this.boneRightArm = normalizeState(state, 0, 6); }
    public void setBoneLeftArm(int state) { this.boneLeftArm = normalizeState(state, 0, 6); }
    public void setBoneRightLeg(int state) { this.boneRightLeg = normalizeState(state, 0, 6); }
    public void setBoneLeftLeg(int state) { this.boneLeftLeg = normalizeState(state, 0, 6); }
    public void setShowSkull(int state) { this.showSkull = normalizeState(state, 0, 2); }
    public void setShowSkeleton(int state) { this.showSkeleton = normalizeState(state, 0, 2); }

    public void setMuscleRightArm(int state) { this.muscleRightArm = normalizeState(state, 0, 3); }
    public void setMuscleLeftArm(int state) { this.muscleLeftArm = normalizeState(state, 0, 3); }
    public void setMuscleRightLeg(int state) { this.muscleRightLeg = normalizeState(state, 0, 3); }
    public void setMuscleLeftLeg(int state) { this.muscleLeftLeg = normalizeState(state, 0, 3); }
    public void setMuscleHead(int state) { this.muscleHead = normalizeState(state, 0, 1); }
    public void setMuscleBody(int state) { this.muscleBody = normalizeState(state, 0, 1); }

    public void copyFrom(LimbData source) {
        this.rightArm = source.rightArm;
        this.leftArm = source.leftArm;
        this.rightLeg = source.rightLeg;
        this.leftLeg = source.leftLeg;
        this.headState = source.headState;
        this.bodyState = source.bodyState;

        this.boneRightArm = source.boneRightArm;
        this.boneLeftArm = source.boneLeftArm;
        this.boneRightLeg = source.boneRightLeg;
        this.boneLeftLeg = source.boneLeftLeg;
        this.showSkull = source.showSkull;
        this.showSkeleton = source.showSkeleton;

        this.muscleRightArm = source.muscleRightArm;
        this.muscleLeftArm = source.muscleLeftArm;
        this.muscleRightLeg = source.muscleRightLeg;
        this.muscleLeftLeg = source.muscleLeftLeg;
        this.muscleHead = source.muscleHead;
        this.muscleBody = source.muscleBody;
    }

    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("right_arm", rightArm);
        tag.putInt("left_arm", leftArm);
        tag.putInt("right_leg", rightLeg);
        tag.putInt("left_leg", leftLeg);
        tag.putInt("head_state", headState);
        tag.putInt("body_state", bodyState);

        tag.putInt("bone_right_arm", boneRightArm);
        tag.putInt("bone_left_arm", boneLeftArm);
        tag.putInt("bone_right_leg", boneRightLeg);
        tag.putInt("bone_left_leg", boneLeftLeg);
        tag.putInt("show_skull", showSkull);
        tag.putInt("show_skeleton", showSkeleton);

        tag.putInt("muscle_right_arm", muscleRightArm);
        tag.putInt("muscle_left_arm", muscleLeftArm);
        tag.putInt("muscle_right_leg", muscleRightLeg);
        tag.putInt("muscle_left_leg", muscleLeftLeg);
        tag.putInt("muscle_head", muscleHead);
        tag.putInt("muscle_body", muscleBody);
        return tag;
    }

    public void deserializeNBT(CompoundTag tag) {
        if (tag.contains("right_arm")) rightArm = normalizeState(tag.getInt("right_arm"), 0, 3);
        if (tag.contains("left_arm")) leftArm = normalizeState(tag.getInt("left_arm"), 0, 3);
        if (tag.contains("right_leg")) rightLeg = normalizeState(tag.getInt("right_leg"), 0, 3);
        if (tag.contains("left_leg")) leftLeg = normalizeState(tag.getInt("left_leg"), 0, 3);
        if (tag.contains("head_state")) headState = normalizeState(tag.getInt("head_state"), 0, 5);
        if (tag.contains("body_state")) bodyState = normalizeState(tag.getInt("body_state"), 0, 9);

        if (tag.contains("bone_right_arm")) boneRightArm = normalizeState(tag.getInt("bone_right_arm"), 0, 6);
        if (tag.contains("bone_left_arm")) boneLeftArm = normalizeState(tag.getInt("bone_left_arm"), 0, 6);
        if (tag.contains("bone_right_leg")) boneRightLeg = normalizeState(tag.getInt("bone_right_leg"), 0, 6);
        if (tag.contains("bone_left_leg")) boneLeftLeg = normalizeState(tag.getInt("bone_left_leg"), 0, 6);
        if (tag.contains("show_skull")) showSkull = normalizeState(tag.getInt("show_skull"), 0, 2);
        if (tag.contains("show_skeleton")) showSkeleton = normalizeState(tag.getInt("show_skeleton"), 0, 2);

        if (tag.contains("muscle_right_arm")) muscleRightArm = normalizeState(tag.getInt("muscle_right_arm"), 0, 3);
        if (tag.contains("muscle_left_arm")) muscleLeftArm = normalizeState(tag.getInt("muscle_left_arm"), 0, 3);
        if (tag.contains("muscle_right_leg")) muscleRightLeg = normalizeState(tag.getInt("muscle_right_leg"), 0, 3);
        if (tag.contains("muscle_left_leg")) muscleLeftLeg = normalizeState(tag.getInt("muscle_left_leg"), 0, 3);
        if (tag.contains("muscle_body")) muscleBody = normalizeState(tag.getInt("muscle_body"), 0, 1);
    }

    private int normalizeState(int state, int min, int max) {
        return Math.max(min, Math.min(max, state));
    }
}