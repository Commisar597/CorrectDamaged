package com.KC.correctdamaged.capability.visual;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public class BodyData implements INBTSerializable<CompoundTag> {

    private final BodyVoxelMatrix bodyVoxelMatrix = new BodyVoxelMatrix();

    private int skinMask = 1;
    private int muscleBody = 0;
    private int showSkeleton = 0;
    private boolean burntSkeleton = false;

    public void copyFrom(BodyData source) {
        this.skinMask = source.skinMask;
        this.muscleBody = source.muscleBody;
        this.showSkeleton = source.showSkeleton;
        this.burntSkeleton = source.burntSkeleton;
        this.bodyVoxelMatrix.fromLongArray(source.bodyVoxelMatrix.toLongArray());
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("SkinMask", skinMask);
        tag.putInt("MuscleBody", muscleBody);
        tag.putInt("ShowSkeleton", showSkeleton);
        tag.putBoolean("BurntSkeleton", burntSkeleton);
        tag.putLongArray("BodyVoxels", bodyVoxelMatrix.toLongArray());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        if (tag.contains("SkinMask")) skinMask = tag.getInt("SkinMask");
        if (tag.contains("MuscleBody")) muscleBody = tag.getInt("MuscleBody");
        if (tag.contains("ShowSkeleton")) showSkeleton = tag.getInt("ShowSkeleton");
        if (tag.contains("BurntSkeleton")) burntSkeleton = tag.getBoolean("BurntSkeleton");
        if (tag.contains("BodyVoxels")) {
            bodyVoxelMatrix.fromLongArray(tag.getLongArray("BodyVoxels"));
        }
    }

    public int getSkinMask() { return skinMask; }
    public void setSkinMask(int skinMask) { this.skinMask = skinMask; }

    public int getMuscleBody() { return muscleBody; }
    public void setMuscleBody(int muscleBody) { this.muscleBody = muscleBody; }

    public int getShowSkeleton() { return showSkeleton; }
    public void setShowSkeleton(int showSkeleton) { this.showSkeleton = showSkeleton; }

    public boolean isBurntSkeleton() {
        return this.burntSkeleton || this.showSkeleton == 2;
    }
    public void setBurntSkeleton(boolean burntSkeleton) {
        this.burntSkeleton = burntSkeleton;
    }

    public boolean isBurntBody() {
        return isBurntSkeleton();
    }

    public boolean isBodyIntact() {
        return this.skinMask == 1 && this.bodyVoxelMatrix.isFullyIntact();
    }

    public BodyVoxelMatrix getBodyVoxelMatrix() {
        return this.bodyVoxelMatrix;
    }

    public BodyVoxelMatrix getVoxelMatrix() {
        return this.bodyVoxelMatrix;
    }
}