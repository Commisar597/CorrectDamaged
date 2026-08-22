package com.KC.correctdamaged.logic.damage.preset;

import java.util.Collections;
import java.util.List;

public class DamagePreset {

    public enum TargetLayer {
        BODY_OUTER,
        BODY_MUSCLE,
        GENERIC
    }

    private String name;
    private TargetLayer targetLayer = TargetLayer.BODY_OUTER;
    private List<VoxelCoord> removedVoxels = Collections.emptyList();

    public DamagePreset() {}

    public DamagePreset(String name, TargetLayer targetLayer, List<VoxelCoord> removedVoxels) {
        this.name = name;
        this.targetLayer = targetLayer;
        this.removedVoxels = removedVoxels;
    }

    public String getName() {
        return name;
    }

    public TargetLayer getTargetLayer() {
        return targetLayer;
    }

    public List<VoxelCoord> getRemovedVoxels() {
        return removedVoxels;
    }

    public static class VoxelCoord {
        public int x;
        public int y;
        public int z;

        public VoxelCoord() {}

        public VoxelCoord(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }
}