package com.KC.correctdamaged.capability.visual;

import java.util.BitSet;

public class BodyVoxelMatrix {

    public static final int WIDTH_X = 8;
    public static final int HEIGHT_Y = 12;
    public static final int DEPTH_Z = 4;
    public static final int TOTAL_VOXELS = WIDTH_X * HEIGHT_Y * DEPTH_Z; // 384

    private final BitSet voxels;

    public BodyVoxelMatrix() {
        this.voxels = new BitSet(TOTAL_VOXELS);
        fillAll();
    }

    public boolean isFullyIntact() {
        return voxels.cardinality() == TOTAL_VOXELS;
    }

    public boolean hasDamage() {
        return voxels.cardinality() < TOTAL_VOXELS;
    }

    public boolean isAnySkinDestroyed() {
        for (int y = 0; y < HEIGHT_Y; y++) {
            for (int x = 0; x < WIDTH_X; x++) {
                if (!isSolid(x, y, 0) || !isSolid(x, y, DEPTH_Z - 1)) return true;
            }
            for (int z = 0; z < DEPTH_Z; z++) {
                if (!isSolid(0, y, z) || !isSolid(WIDTH_X - 1, y, z)) return true;
            }
        }
        return false;
    }

    public static int getIndex(int x, int y, int z) {
        return x + (y * WIDTH_X) + (z * WIDTH_X * HEIGHT_Y);
    }

    public static boolean isInBounds(int x, int y, int z) {
        return x >= 0 && x < WIDTH_X && y >= 0 && y < HEIGHT_Y && z >= 0 && z < DEPTH_Z;
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
        voxels.set(0, TOTAL_VOXELS, true);
    }

    public void clearAll() {
        voxels.clear(0, TOTAL_VOXELS);
    }

    public boolean isBoneZoneDestroyed() {
        for (int y = 0; y < HEIGHT_Y; y++) {
            if (!isSolid(3, y, 2) || !isSolid(4, y, 2)) {
                return true; // Позвоночник пробит
            }
        }
        return false;
    }

    public void removeBox(int xMin, int yMin, int zMin, int xMax, int yMax, int zMax) {
        for (int x = xMin; x <= xMax; x++) {
            for (int y = yMin; y <= yMax; y++) {
                for (int z = zMin; z <= zMax; z++) {
                    setSolid(x, y, z, false);
                }
            }
        }
    }

    public void applyThroughHole(float centerX, float centerY, float radius) {
        float rSq = radius * radius;
        for (int x = 0; x < WIDTH_X; x++) {
            for (int y = 0; y < HEIGHT_Y; y++) {
                float dx = (x + 0.5f) - centerX;
                float dy = (y + 0.5f) - centerY;
                if (dx * dx + dy * dy <= rSq) {
                    for (int z = 0; z < DEPTH_Z; z++) {
                        setSolid(x, y, z, false);
                    }
                }
            }
        }
    }

    public void applySphericalDamage(float centerX, float centerY, float centerZ, float radius) {
        float rSq = radius * radius;
        for (int x = 0; x < WIDTH_X; x++) {
            for (int y = 0; y < HEIGHT_Y; y++) {
                for (int z = 0; z < DEPTH_Z; z++) {
                    float dx = (x + 0.5f) - centerX;
                    float dy = (y + 0.5f) - centerY;
                    float dz = (z + 0.5f) - centerZ;
                    if (dx * dx + dy * dy + dz * dz <= rSq) {
                        setSolid(x, y, z, false);
                    }
                }
            }
        }
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