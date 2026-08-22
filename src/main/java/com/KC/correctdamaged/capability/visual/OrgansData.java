package com.KC.correctdamaged.capability.visual;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public class OrgansData implements INBTSerializable<CompoundTag> {

    private int heart = 0;
    private int left_lung = 0;
    private int right_lung = 0;
    private int liver = 0;
    private int g_i_t = 0;

    public OrgansData() {}

    public void copyFrom(OrgansData source) {
        this.heart = source.heart;
        this.left_lung = source.left_lung;
        this.right_lung = source.right_lung;
        this.liver = source.liver;
        this.g_i_t = source.g_i_t;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("Heart", heart);
        tag.putInt("LeftLung", left_lung);
        tag.putInt("RightLung", right_lung);
        tag.putInt("Liver", liver);
        tag.putInt("GIT", g_i_t);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        if (tag.contains("Heart")) heart = tag.getInt("Heart");
        if (tag.contains("LeftLung")) left_lung = tag.getInt("LeftLung");
        if (tag.contains("RightLung")) right_lung = tag.getInt("RightLung");
        if (tag.contains("Liver")) liver = tag.getInt("Liver");
        if (tag.contains("GIT")) g_i_t = tag.getInt("GIT");
    }

    public int getHeart() {
        return heart;
    }

    public void setHeart(int heart) {
        this.heart = heart;
    }

    public int getLeft_lung() {
        return left_lung;
    }

    public void setLeft_lung(int left_lung) {
        this.left_lung = left_lung;
    }

    public int getRight_lung() {
        return right_lung;
    }

    public void setRight_lung(int right_lung) {
        this.right_lung = right_lung;
    }

    public int getLiver() {
        return liver;
    }

    public void setLiver(int liver) {
        this.liver = liver;
    }

    public int getG_i_t() {
        return g_i_t;
    }

    public void setG_i_t(int g_i_t) {
        this.g_i_t = g_i_t;
    }
}