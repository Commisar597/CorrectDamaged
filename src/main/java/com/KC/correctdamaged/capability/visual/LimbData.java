package com.KC.correctdamaged.capability.visual;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Главный класс-хранилище всех визуальных данных повреждений игрока.
 * Объединяет данные обеих рук, ног, головы и туловища, а также поддерживает сериализацию в NBT.
 */
public class LimbData implements INBTSerializable<CompoundTag> {

    private final ArmData rightArm = new ArmData(LimbSide.RIGHT);
    private final ArmData leftArm = new ArmData(LimbSide.LEFT);
    private final LegData rightLeg = new LegData(LimbSide.RIGHT);
    private final LegData leftLeg = new LegData(LimbSide.LEFT);
    private final HeadData head = new HeadData();
    private final BodyData body = new BodyData();

    /**
     * Копирует состояние из другого экземпляра {@link LimbData}.
     */
    public void copyFrom(LimbData source) {
        this.rightArm.copyFrom(source.rightArm);
        this.leftArm.copyFrom(source.leftArm);
        this.rightLeg.copyFrom(source.rightLeg);
        this.leftLeg.copyFrom(source.leftLeg);
        this.head.copyFrom(source.head);
        this.body.copyFrom(source.body);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.put("RightArm", rightArm.serializeNBT());
        tag.put("LeftArm", leftArm.serializeNBT());
        tag.put("RightLeg", rightLeg.serializeNBT());
        tag.put("LeftLeg", leftLeg.serializeNBT());
        tag.put("Head", head.serializeNBT());
        tag.put("Body", body.serializeNBT());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        if (tag.contains("RightArm")) rightArm.deserializeNBT(tag.getCompound("RightArm"));
        if (tag.contains("LeftArm")) leftArm.deserializeNBT(tag.getCompound("LeftArm"));
        if (tag.contains("RightLeg")) rightLeg.deserializeNBT(tag.getCompound("RightLeg"));
        if (tag.contains("LeftLeg")) leftLeg.deserializeNBT(tag.getCompound("LeftLeg"));
        if (tag.contains("Head")) head.deserializeNBT(tag.getCompound("Head"));
        if (tag.contains("Body")) body.deserializeNBT(tag.getCompound("Body"));
    }

    public ArmData getRightArm() {
        return rightArm;
    }

    public ArmData getLeftArm() {
        return leftArm;
    }

    public LegData getRightLeg() {
        return rightLeg;
    }

    public LegData getLeftLeg() {
        return leftLeg;
    }

    public HeadData getHead() {
        return head;
    }

    public BodyData getBody() {
        return body;
    }
}