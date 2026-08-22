package com.KC.correctdamaged.capability.visual;

import com.KC.correctdamaged.logic.damage.preset.DamagePreset;
import com.KC.correctdamaged.logic.damage.preset.DamagePresetManager;

import java.util.BitSet;

public class BodyVoxelMatrix {

    public final int widthX;
    public final int heightY;
    public final int depthZ;
    public final int totalVoxels;
    private final DamagePreset.TargetLayer layerType;

    private final BitSet voxels;

    public BodyVoxelMatrix(int widthX, int heightY, int depthZ, DamagePreset.TargetLayer layerType) {
        this.widthX = widthX;
        this.heightY = heightY;
        this.depthZ = depthZ;
        this.layerType = layerType;
        this.totalVoxels = widthX * heightY * depthZ;
        this.voxels = new BitSet(totalVoxels);
        fillAll();
    }

    public BodyVoxelMatrix() {
        this(8, 12, 4, DamagePreset.TargetLayer.BODY_OUTER);
    }

    public DamagePreset.TargetLayer getLayerType() {
        return layerType;
    }

    public int getWidthX() {
        return widthX;
    }

    public int getHeightY() {
        return heightY;
    }

    public int getDepthZ() {
        return depthZ;
    }

    public int getTotalVoxels() {
        return totalVoxels;
    }

    public boolean isFullyIntact() {
        return voxels.cardinality() == totalVoxels;
    }

    public boolean hasDamage() {
        return voxels.cardinality() < totalVoxels;
    }

    public int getIndex(int x, int y, int z) {
        return x + (y * widthX) + (z * widthX * heightY);
    }

    public boolean isInBounds(int x, int y, int z) {
        return x >= 0 && x < widthX && y >= 0 && y < heightY && z >= 0 && z < depthZ;
    }

    public boolean isSolid(int x, int y, int z) {
        if (!isInBounds(x, y, z)) {
            return false;
        }
        return voxels.get(getIndex(x, y, z));
    }

    public boolean isSolidSafe(int x, int y, int z) {
        if (!isInBounds(x, y, z)) {
            return false;
        }
        return voxels.get(getIndex(x, y, z));
    }

    public void setSolid(int x, int y, int z, boolean solid) {
        if (isInBounds(x, y, z)) {
            voxels.set(getIndex(x, y, z), solid);
        }
    }

    public void fillAll() {
        voxels.set(0, totalVoxels, true);
    }

    public long[] toLongArray() {
        return voxels.toLongArray();
    }

    public void fromLongArray(long[] data) {
        voxels.clear();
        BitSet loaded = BitSet.valueOf(data);
        voxels.or(loaded);
    }

    public void reset() {
        fillAll();
    }

    public boolean applyVoxelPreset(String presetName) {
        if ("reset".equalsIgnoreCase(presetName)) {
            reset();
            return true;
        }

        DamagePreset preset = DamagePresetManager.getPreset(presetName);
        if (preset == null) {
            return false;
        }

        if (preset.getTargetLayer() != DamagePreset.TargetLayer.GENERIC && preset.getTargetLayer() != this.layerType) {
            return false;
        }

        reset();
        for (DamagePreset.VoxelCoord coord : preset.getRemovedVoxels()) {
            setSolid(coord.x, coord.y, coord.z, false);
        }
        return true;
    }
}