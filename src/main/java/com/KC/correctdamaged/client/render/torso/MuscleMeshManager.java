package com.KC.correctdamaged.client.render.torso;

import com.KC.correctdamaged.capability.visual.BodyVoxelMatrix;

public class MuscleMeshManager {

    public static final int MUSCLE_GRID_X = 4;
    public static final int MUSCLE_GRID_Y = 6;
    public static final int TOTAL_MUSCLE_SUBMESHES = MUSCLE_GRID_X * MUSCLE_GRID_Y;

    public static boolean shouldRenderSubMesh(BodyVoxelMatrix matrix, int gridX, int gridY, boolean isFront) {
        int startX = gridX * 2;
        int startY = gridY * 2;
        int outerZ = isFront ? 0 : 3;
        int innerZ = isFront ? 1 : 2;

        for (int dx = 0; dx < 2; dx++) {
            for (int dy = 0; dy < 2; dy++) {
                int vx = startX + dx;
                int vy = startY + dy;

                boolean skinSolid = matrix.isSolid(vx, vy, outerZ);
                boolean fleshSolid = matrix.isSolid(vx, vy, innerZ);

                if (!skinSolid && fleshSolid) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean isMuscleLayerSplit(BodyVoxelMatrix matrix) {
        for (int gx = 0; gx < MUSCLE_GRID_X; gx++) {
            for (int gy = 0; gy < MUSCLE_GRID_Y; gy++) {
                int startX = gx * 2;
                int startY = gy * 2;

                for (int dx = 0; dx < 2; dx++) {
                    for (int dy = 0; dy < 2; dy++) {
                        int vx = startX + dx;
                        int vy = startY + dy;

                        if (!matrix.isSolid(vx, vy, 1) || !matrix.isSolid(vx, vy, 2)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}