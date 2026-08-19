package com.KC.correctdamaged.capability.visual;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Хранит данные о состоянии туловища игрока (торса), включая целостность кожи,
 * слой мышц, скелет, обугливание и воксельную матрицу глубоких повреждений.
 */
public class BodyData implements INBTSerializable<CompoundTag> {

    /** Трёхмерная воксельная сетка для процедурного вырезания повреждений туловища. */
    private final BodyVoxelMatrix bodyVoxelMatrix = new BodyVoxelMatrix();

    /** Состояние внешнего слоя кожи туловища (1 = целая, 0 = повреждена/отсутствует). */
    private int skinMask = 1;
    /** Состояние мышц туловища (0 = норма, 1+ = повреждение или оголение). */
    private int muscleBody = 0;
    /** Режим отображения скелета туловища (0 = скрыт, 1 = обычный, 2 = обугленный). */
    private int showSkeleton = 0;
    /** Флаг обугливания костей туловища. */
    private boolean burntSkeleton = false;

    /**
     * Копирует данные состояния туловища из другого объекта {@link BodyData}.
     *
     * @param source Источник данных для копирования.
     */
    public void copyFrom(BodyData source) {
        this.skinMask = source.skinMask;
        this.muscleBody = source.muscleBody;
        this.showSkeleton = source.showSkeleton;
        this.burntSkeleton = source.burntSkeleton;
        this.bodyVoxelMatrix.fromLongArray(source.bodyVoxelMatrix.toLongArray());
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("SkinMask", skinMask);
        tag.putInt("MuscleBody", muscleBody);
        tag.putInt("ShowSkeleton", showSkeleton);
        tag.putBoolean("BurntSkeleton", burntSkeleton);
        tag.putLongArray("BodyVoxels", bodyVoxelMatrix.toLongArray());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        if (tag.contains("SkinMask")) skinMask = tag.getInt("SkinMask");
        if (tag.contains("MuscleBody")) muscleBody = tag.getInt("MuscleBody");
        if (tag.contains("ShowSkeleton")) showSkeleton = tag.getInt("ShowSkeleton");
        if (tag.contains("BurntSkeleton")) burntSkeleton = tag.getBoolean("BurntSkeleton");
        if (tag.contains("BodyVoxels")) {
            bodyVoxelMatrix.fromLongArray(tag.getLongArray("BodyVoxels"));
        }
    }

    /** @return Маска состояния кожи туловища. */
    public int getSkinMask() { return skinMask; }
    /** @param skinMask Состояние кожи туловища. */
    public void setSkinMask(int skinMask) { this.skinMask = skinMask; }

    /** @return Состояние мышц туловища. */
    public int getMuscleBody() { return muscleBody; }
    /** @param muscleBody Состояние мышц туловища. */
    public void setMuscleBody(int muscleBody) { this.muscleBody = muscleBody; }

    /** @return Режим отображения скелета туловища. */
    public int getShowSkeleton() { return showSkeleton; }
    /** @param showSkeleton Режим отображения скелета. */
    public void setShowSkeleton(int showSkeleton) { this.showSkeleton = showSkeleton; }

    /**
     * Проверяет, обуглен ли скелет туловища (по флагу или по режиму {@code showSkeleton == 2}).
     *
     * @return {@code true}, если скелет обуглен.
     */
    public boolean isBurntSkeleton() {
        return this.burntSkeleton || this.showSkeleton == 2;
    }

    /** @param burntSkeleton Устанавливает флаг обугливания скелета. */
    public void setBurntSkeleton(boolean burntSkeleton) {
        this.burntSkeleton = burntSkeleton;
    }

    /**
     * Проверяет, обуглено ли туловище.
     *
     * @return {@code true}, если туловище/скелет обуглены.
     */
    public boolean isBurntBody() {
        return isBurntSkeleton();
    }

    /**
     * Проверяет, находится ли туловище в полностью неповреждённом состоянии.
     *
     * @return {@code true}, если кожа цела и воксельная матрица не имеет разрушений.
     */
    public boolean isBodyIntact() {
        return this.skinMask == 1 && this.bodyVoxelMatrix.isFullyIntact();
    }

    /** @return Воксельная матрица туловища {@link BodyVoxelMatrix}. */
    public BodyVoxelMatrix getBodyVoxelMatrix() {
        return this.bodyVoxelMatrix;
    }

    /**
     * Псевдоним для {@link #getBodyVoxelMatrix()}.
     *
     * @return Воксельная матрица туловища {@link BodyVoxelMatrix}.
     */
    public BodyVoxelMatrix getVoxelMatrix() {
        return this.bodyVoxelMatrix;
    }
}