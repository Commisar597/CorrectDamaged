package com.KC.correctdamaged.capability.visual;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public class LimbData implements INBTSerializable<CompoundTag> {

    private final ArmData rightArm = new ArmData(LimbSide.RIGHT);
    private final ArmData leftArm = new ArmData(LimbSide.LEFT);
    private final LegData rightLeg = new LegData(LimbSide.RIGHT);
    private final LegData leftLeg = new LegData(LimbSide.LEFT);
    private final HeadData head = new HeadData();

    private int bodyState = 9;
    private int muscleBody = 0;
    private int showSkeleton = 0;

    public void copyFrom(LimbData source) {
        this.rightArm.copyFrom(source.rightArm);
        this.leftArm.copyFrom(source.leftArm);
        this.rightLeg.copyFrom(source.rightLeg);
        this.leftLeg.copyFrom(source.leftLeg);
        this.head.copyFrom(source.head);
        this.bodyState = source.bodyState;
        this.muscleBody = source.muscleBody;
        this.showSkeleton = source.showSkeleton;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.put("RightArm", rightArm.serializeNBT());
        tag.put("LeftArm", leftArm.serializeNBT());
        tag.put("RightLeg", rightLeg.serializeNBT());
        tag.put("LeftLeg", leftLeg.serializeNBT());
        tag.put("Head", head.serializeNBT());
        tag.putInt("BodyState", bodyState);
        tag.putInt("MuscleBody", muscleBody);
        tag.putInt("ShowSkeleton", showSkeleton);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        if (tag.contains("RightArm")) rightArm.deserializeNBT(tag.getCompound("RightArm"));
        if (tag.contains("LeftArm")) leftArm.deserializeNBT(tag.getCompound("LeftArm"));
        if (tag.contains("RightLeg")) rightLeg.deserializeNBT(tag.getCompound("RightLeg"));
        if (tag.contains("LeftLeg")) leftLeg.deserializeNBT(tag.getCompound("LeftLeg"));
        if (tag.contains("Head")) head.deserializeNBT(tag.getCompound("Head"));
        if (tag.contains("BodyState")) bodyState = tag.getInt("BodyState");
        if (tag.contains("MuscleBody")) muscleBody = tag.getInt("MuscleBody");
        if (tag.contains("ShowSkeleton")) showSkeleton = tag.getInt("ShowSkeleton");
    }

    public ArmData getRightArm() { return rightArm; }
    public ArmData getLeftArm() { return leftArm; }
    public LegData getRightLeg() { return rightLeg; }
    public LegData getLeftLeg() { return leftLeg; }
    public HeadData getHead() { return head; }

    public int getBodyState() { return bodyState; }
    public void setBodyState(int bodyState) { this.bodyState = bodyState; }

    public int getMuscleBody() { return muscleBody; }
    public void setMuscleBody(int muscleBody) { this.muscleBody = muscleBody; }

    public int getShowSkeleton() { return showSkeleton; }
    public void setShowSkeleton(int showSkeleton) { this.showSkeleton = showSkeleton; }
}