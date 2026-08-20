package com.KC.correctdamaged.capability.visual;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public class BodyData implements INBTSerializable<CompoundTag> {

    private final BodyVoxelMatrix bodyVoxelMatrix = new BodyVoxelMatrix();

    private int skinMask = 1;
    private byte muscleOctalBody = (byte) 0;
    private byte jacketOctalBody = (byte) 0xFF;
    private int showSkeleton = 0;
    private boolean burntSkeleton = false;

    public void copyFrom(BodyData source) {
        this.skinMask = source.skinMask;
        this.muscleOctalBody = source.muscleOctalBody;
        this.jacketOctalBody = source.jacketOctalBody;
        this.showSkeleton = source.showSkeleton;
        this.burntSkeleton = source.burntSkeleton;
        this.bodyVoxelMatrix.fromLongArray(source.bodyVoxelMatrix.toLongArray());
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("SkinMask", skinMask);
        tag.putInt("MuscleBody", muscleOctalBody);
        tag.putByte("JacketBody", jacketOctalBody);
        tag.putInt("ShowSkeleton", showSkeleton);
        tag.putBoolean("BurntSkeleton", burntSkeleton);
        tag.putLongArray("BodyVoxels", bodyVoxelMatrix.toLongArray());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        if (tag.contains("SkinMask")) skinMask = tag.getInt("SkinMask");
        if (tag.contains("MuscleBody")) muscleOctalBody = tag.getByte("MuscleBody");
        if (tag.contains("JacketBody")) jacketOctalBody = tag.getByte("JacketBody");
        if (tag.contains("ShowSkeleton")) showSkeleton = tag.getInt("ShowSkeleton");
        if (tag.contains("BurntSkeleton")) burntSkeleton = tag.getBoolean("BurntSkeleton");
        if (tag.contains("BodyVoxels")) {
            bodyVoxelMatrix.fromLongArray(tag.getLongArray("BodyVoxels"));
        }
    }

    public void setSkinMask(int skinMask) {
        this.skinMask = skinMask;
    }

    public void setMuscleOctalBody(byte muscleOctalBody) {
        this.muscleOctalBody = muscleOctalBody;
    }

    public byte getMuscleOctantBody() {
        return muscleOctalBody;
    }

    public void setJacketOctalBody(byte jacketOctalBody) {
        this.jacketOctalBody = jacketOctalBody;
    }

    public byte getJacketOctantBody() {
        return jacketOctalBody;
    }

    public int getShowSkeleton() {
        return showSkeleton;
    }

    public void setShowSkeleton(int showSkeleton) {
        this.showSkeleton = showSkeleton;
    }

    public boolean isBurntSkeleton() {
        return this.burntSkeleton || this.showSkeleton == 2;
    }

    public void setBurntSkeleton(boolean burntSkeleton) {
        this.burntSkeleton = burntSkeleton;
    }

    public boolean isBodyIntact() {
        return this.skinMask == 1 && this.bodyVoxelMatrix.isFullyIntact();
    }

    public BodyVoxelMatrix getBodyVoxelMatrix() {
        return this.bodyVoxelMatrix;
    }
}