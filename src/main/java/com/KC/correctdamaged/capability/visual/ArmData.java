package com.KC.correctdamaged.capability.visual;

import net.minecraft.nbt.CompoundTag;

/**
 * Хранит состояние слоёв кожи (плечо, предплечье, запястье), мышц и костей для конкретной руки игрока.
 */
public class ArmData {

    /** Сторона конечности (левая или правая). */
    private final LimbSide side;

    /** Состояние кожи плеча (1 = целая, 0 = отсутствует). */
    private int shoulderSkin = 1;
    /** Состояние кожи предплечья (1 = целая, 0 = отсутствует). */
    private int forearmSkin = 1;
    /** Состояние кожи запястья/кисти (1 = целая, 0 = отсутствует). */
    private int wristSkin = 1;

    /** Состояние мышечного слоя (0-3). */
    private int muscleState = 0;
    /** Состояние костного слоя (0-3). */
    private int boneState = 0;
    /** Флаг обугливания кости руки. */
    private boolean burntBone = false;

    /**
     * Создает экземпляр данных руки для указанной стороны.
     *
     * @param side Сторона руки (левая или правая).
     */
    public ArmData(LimbSide side) {
        this.side = side;
    }

    /** @return Состояние кожи плеча. */
    public int getShoulderSkin() { return shoulderSkin; }
    /** @param state Состояние кожи плеча (ограничивается диапазоном 0..1). */
    public void setShoulderSkin(int state) { this.shoulderSkin = clamp(state, 0, 1); }

    /** @return Состояние кожи предплечья. */
    public int getForearmSkin() { return forearmSkin; }
    /** @param state Состояние кожи предплечья (ограничивается диапазоном 0..1). */
    public void setForearmSkin(int state) { this.forearmSkin = clamp(state, 0, 1); }

    /** @return Состояние кожи запястья. */
    public int getWristSkin() { return wristSkin; }
    /** @param state Состояние кожи запястья (ограничивается диапазоном 0..1). */
    public void setWristSkin(int state) { this.wristSkin = clamp(state, 0, 1); }

    /** @return {@code true}, если кожа плеча цела. */
    public boolean hasShoulderSkin() { return shoulderSkin == 1; }
    /** @return {@code true}, если кожа предплечья цела. */
    public boolean hasForearmSkin() { return forearmSkin == 1; }
    /** @return {@code true}, если кожа запястья цела. */
    public boolean hasWristSkin() { return wristSkin == 1; }

    /** @return Уровень повреждения/отображения мышц (0-3). */
    public int getMuscleState() { return muscleState; }
    /** @param state Уровень повреждения мышц (ограничивается диапазоном 0..3). */
    public void setMuscleState(int state) { this.muscleState = clamp(state, 0, 3); }

    /** @return Уровень повреждения/отображения костей (0-3). */
    public int getBoneState() { return boneState; }
    /** @param state Уровень повреждения костей (ограничивается диапазоном 0..3). */
    public void setBoneState(int state) { this.boneState = clamp(state, 0, 3); }

    /** @return {@code true}, если кость руки обуглена. */
    public boolean isBurntBone() { return burntBone; }
    /** @param burntBone Флаг обугливания кости. */
    public void setBurntBone(boolean burntBone) { this.burntBone = burntBone; }

    /**
     * Копирует состояние из другого объекта {@link ArmData}.
     *
     * @param source Источник данных для копирования.
     */
    public void copyFrom(ArmData source) {
        this.shoulderSkin = source.shoulderSkin;
        this.forearmSkin = source.forearmSkin;
        this.wristSkin = source.wristSkin;
        this.muscleState = source.muscleState;
        this.boneState = source.boneState;
        this.burntBone = source.burntBone;
    }

    /**
     * Сериализует состояние руки в {@link CompoundTag}.
     *
     * @return NBT-тег с сохранёнными данными.
     */
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("shoulder_skin", shoulderSkin);
        tag.putInt("forearm_skin", forearmSkin);
        tag.putInt("wrist_skin", wristSkin);
        tag.putInt("muscle_state", muscleState);
        tag.putInt("bone_state", boneState);
        tag.putBoolean("burnt_bone", burntBone);
        return tag;
    }

    /**
     * Десериализует состояние руки из {@link CompoundTag}.
     *
     * @param tag NBT-тег для чтения данных.
     */
    public void deserializeNBT(CompoundTag tag) {
        if (tag.contains("shoulder_skin")) shoulderSkin = clamp(tag.getInt("shoulder_skin"), 0, 1);
        if (tag.contains("forearm_skin")) forearmSkin = clamp(tag.getInt("forearm_skin"), 0, 1);
        if (tag.contains("wrist_skin")) wristSkin = clamp(tag.getInt("wrist_skin"), 0, 1);
        if (tag.contains("muscle_state")) muscleState = clamp(tag.getInt("muscle_state"), 0, 3);
        if (tag.contains("bone_state")) boneState = clamp(tag.getInt("bone_state"), 0, 3);
        if (tag.contains("burnt_bone")) burntBone = tag.getBoolean("burnt_bone");
    }

    /**
     * Вспомогательный метод для ограничения значения в заданных границах [min, max].
     */
    private int clamp(int val, int min, int max) {
        return Math.max(min, Math.min(max, val));
    }
}