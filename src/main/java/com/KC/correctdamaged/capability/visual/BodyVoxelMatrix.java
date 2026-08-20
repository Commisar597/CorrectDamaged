package com.KC.correctdamaged.capability.visual;

import java.util.BitSet;

public class BodyVoxelMatrix {

    public final int widthX;
    public final int heightY;
    public final int depthZ;
    public final int totalVoxels;

    private final BitSet voxels;

    public BodyVoxelMatrix(int widthX, int heightY, int depthZ) {
        this.widthX = widthX;
        this.heightY = heightY;
        this.depthZ = depthZ;
        this.totalVoxels = widthX * heightY * depthZ;
        this.voxels = new BitSet(totalVoxels);
        fillAll();
    }

    public BodyVoxelMatrix() {
        this(8, 12, 4);
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

    /**
     * Применяет готовую конфигурацию (пресет) ранений.
     *
     * @param presetName Название пресета ("bullet_center", "slash_diagonal", "heavy_blast", "reset").
     */
    public void applyPreset(String presetName) {
        switch (presetName.toLowerCase()) {
            case "bullet_center" -> {
                reset();
                setSolid(3, 4, 1, false);
                setSolid(4, 4, 1, false);
                setSolid(3, 5, 1, false);
                setSolid(4, 5, 1, false);
                setSolid(3, 4, 2, false);
                setSolid(4, 4, 2, false);
                setSolid(3, 5, 2, false);
                setSolid(4, 5, 2, false);
            }
            case "slash_diagonal" -> {
                reset();
                for (int i = 0; i < 6; i++) {
                    setSolid(i + 1, i + 2, 0, false);
                    setSolid(i + 1, i + 2, 1, false);
                }
            }
            case "heavy_blast" -> {
                reset();
                for (int x = 0; x < 4; x++) {
                    for (int y = 2; y < 10; y++) {
                        for (int z = 0; z < 4; z++) {
                            setSolid(x, y, z, false);
                        }
                    }
                }
            }
            case "reset" -> reset();
        }
    }
}