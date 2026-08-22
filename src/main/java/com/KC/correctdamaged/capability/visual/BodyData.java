package com.KC.correctdamaged.capability.visual;

import com.KC.correctdamaged.logic.damage.preset.DamagePreset;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public class BodyData implements INBTSerializable<CompoundTag> {

    private final BodyVoxelMatrix bodyVoxelMatrix = new BodyVoxelMatrix();
    private final BodyVoxelMatrix musclesOctalBody = new BodyVoxelMatrix(6, 10, 2, DamagePreset.TargetLayer.BODY_MUSCLE);
    private final BodyVoxelMatrix jacketVoxelMatrix = new BodyVoxelMatrix(4, 6, 2, DamagePreset.TargetLayer.GENERIC);
    private final OrgansData organsData = new OrgansData();

    private int organsVisible = 0;
    private int skinMask = 1;
    private int musclesMask = 0;
    private int jacketMask = 1;
    private int showSkeleton = 0;
    private boolean burntSkeleton = false;

    public void copyFrom(BodyData source) {
        this.skinMask = source.skinMask;
        this.musclesMask = source.musclesMask;
        this.jacketMask = source.jacketMask;
        this.showSkeleton = source.showSkeleton;
        this.burntSkeleton = source.burntSkeleton;
        this.organsVisible = source.organsVisible;
        this.organsData.copyFrom(source.organsData);

        this.bodyVoxelMatrix.fromLongArray(source.bodyVoxelMatrix.toLongArray());
        this.musclesOctalBody.fromLongArray(source.musclesOctalBody.toLongArray());
        this.jacketVoxelMatrix.fromLongArray(source.jacketVoxelMatrix.toLongArray());
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("SkinMask", skinMask);
        tag.putInt("MusclesMask", musclesMask);
        tag.putInt("JacketMask", jacketMask);
        tag.putInt("ShowSkeleton", showSkeleton);
        tag.putBoolean("BurntSkeleton", burntSkeleton);
        tag.putInt("OrgansVisible", organsVisible);
        tag.put("OrgansData", organsData.serializeNBT());

        tag.putLongArray("BodyVoxels", bodyVoxelMatrix.toLongArray());
        tag.putLongArray("MusclesVoxels", musclesOctalBody.toLongArray());
        tag.putLongArray("JacketVoxels", jacketVoxelMatrix.toLongArray());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        if (tag.contains("SkinMask")) skinMask = tag.getInt("SkinMask");
        if (tag.contains("MusclesMask")) musclesMask = tag.getInt("MusclesMask");
        if (tag.contains("JacketMask")) jacketMask = tag.getInt("JacketMask");
        if (tag.contains("ShowSkeleton")) showSkeleton = tag.getInt("ShowSkeleton");
        if (tag.contains("BurntSkeleton")) burntSkeleton = tag.getBoolean("BurntSkeleton");
        if (tag.contains("OrgansVisible")) organsVisible = tag.getInt("OrgansVisible");
        if (tag.contains("OrgansData")) organsData.deserializeNBT(tag.getCompound("OrgansData"));

        if (tag.contains("BodyVoxels")) bodyVoxelMatrix.fromLongArray(tag.getLongArray("BodyVoxels"));
        if (tag.contains("MusclesVoxels")) musclesOctalBody.fromLongArray(tag.getLongArray("MusclesVoxels"));
        if (tag.contains("JacketVoxels")) jacketVoxelMatrix.fromLongArray(tag.getLongArray("JacketVoxels"));
    }

    public OrgansData getOrgansData() {
        return organsData;
    }

    public int getOrgansVisible() {
        return organsVisible;
    }

    public void setOrgansVisible(int organsVisible) {
        this.organsVisible = organsVisible;
    }

    public void setSkinMask(int skinMask) {
        this.skinMask = skinMask;
    }

    public void setMusclesMask(int musclesMask) {
        this.musclesMask = musclesMask;
    }

    public void setJacketMask(int jacketMask) {
        this.jacketMask = jacketMask;
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

    public BodyVoxelMatrix getJacketVoxelMatrix() {
        return this.jacketVoxelMatrix;
    }

    public BodyVoxelMatrix getMuscleVoxelMatrix() {
        return this.musclesOctalBody;
    }

    public int getMusclesMask() {
        return musclesMask;
    }

    public int getJacketMask() {
        return jacketMask;
    }
}