package com.KC.correctdamaged.capability.visual;

import java.util.BitSet;

/**
 * Трёхмерная сетка вокселей (8x12x4), представляющая объёмное тело (туловище) игрока.
 * Используется для процедурного вырезания отверстий, ранений и сквозных повреждений.
 */
public class BodyVoxelMatrix {

    public static final int WIDTH_X = 8;
    public static final int HEIGHT_Y = 12;
    public static final int DEPTH_Z = 4;
    public static final int TOTAL_VOXELS = WIDTH_X * HEIGHT_Y * DEPTH_Z; // 384 вокселя

    /** Битовое хранилище цельности вокселей (true = воксель цел, false = разрушен/вырезан). */
    private final BitSet voxels;

    public BodyVoxelMatrix() {
        this.voxels = new BitSet(TOTAL_VOXELS);
        fillAll();
    }

    /** Проверяет, цела ли матрица полностью. */
    public boolean isFullyIntact() {
        return voxels.cardinality() == TOTAL_VOXELS;
    }

    /** Проверяет, есть ли хотя бы одно повреждение. */
    public boolean hasDamage() {
        return voxels.cardinality() < TOTAL_VOXELS;
    }

    /** Вычисляет плоский индекс 1D-массива из 3D-координат. */
    public static int getIndex(int x, int y, int z) {
        return x + (y * WIDTH_X) + (z * WIDTH_X * HEIGHT_Y);
    }

    /** Проверяет, находятся ли координаты в пределах матрицы. */
    public static boolean isInBounds(int x, int y, int z) {
        return x >= 0 && x < WIDTH_X && y >= 0 && y < HEIGHT_Y && z >= 0 && z < DEPTH_Z;
    }

    /** Возвращает статус вокселя (true — цел, false — разрушен). */
    public boolean isSolid(int x, int y, int z) {
        if (!isInBounds(x, y, z)) {
            return false;
        }
        return voxels.get(getIndex(x, y, z));
    }

    /** Безопасная версия получения состояния вокселя с проверкой границ. */
    public boolean isSolidSafe(int x, int y, int z) {
        if (!isInBounds(x, y, z)) {
            return false;
        }
        return voxels.get(getIndex(x, y, z));
    }

    /** Устанавливает состояние вокселя. */
    public void setSolid(int x, int y, int z, boolean solid) {
        if (isInBounds(x, y, z)) {
            voxels.set(getIndex(x, y, z), solid);
        }
    }

    /** Заполняет весь объем (полное восстановление). */
    public void fillAll() {
        voxels.set(0, TOTAL_VOXELS, true);
    }

    /** Сериализует BitSet в массив long для передачи или сохранения NBT. */
    public long[] toLongArray() {
        return voxels.toLongArray();
    }

    /** Десериализует состояние из массива long. */
    public void fromLongArray(long[] data) {
        voxels.clear();
        BitSet loaded = BitSet.valueOf(data);
        voxels.or(loaded);
    }

    /** Сбрасывает матрицу в полностью целое состояние. */
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