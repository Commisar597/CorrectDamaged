package com.KC.correctdamaged.client.render.torso;

import com.KC.correctdamaged.capability.visual.BodyVoxelMatrix;

/**
 * Менеджер подсеток (SubMeshes) мышечного слоя туловища.
 * Зачем нужен: Делит поверхность торса на сетку 4x6 мышечных зон. Высчитывает,
 * какие участки мышц необходимо отображать, если внешний слой кожи разрушен,
 * но внутренний мышечный слой остался целым.
 */
public class MuscleMeshManager {

    /** Разрешение сетки мышц по горизонтали. */
    public static final int MUSCLE_GRID_X = 4;
    /** Разрешение сетки мышц по вертикали. */
    public static final int MUSCLE_GRID_Y = 6;
    /** Общее количество подсеток мышц на передней/задней стороне. */
    public static final int TOTAL_MUSCLE_SUBMESHES = MUSCLE_GRID_X * MUSCLE_GRID_Y;

    /**
     * Определяет, нужно ли рендерить конкретную подсетку мышц (2x2 воксела).
     *
     * @param matrix Воксельная матрица туловища.
     * @param gridX Индекс сетки по X (0..3).
     * @param gridY Индекс сетки по Y (0..5).
     * @param isFront Флаг передней/задней стороны торса.
     * @return true, если кожа отсутствует, но мышечная ткань цела.
     */
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

                // Если кожи нет (!skinSolid), но мясо/мышцы есть (fleshSolid) — отображаем мышцу
                if (!skinSolid && fleshSolid) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Проверяет, имеет ли мышечный слой сквозные разрывы или повреждения по всей площади.
     */
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