package com.KC.correctdamaged.keywords;

import com.KC.correctdamaged.CorrectDamaged;
import net.minecraft.resources.ResourceLocation;

public class PiercingKeyword implements IAttackKeyword {

    public static final PiercingKeyword INSTANCE = new PiercingKeyword();
    private static final ResourceLocation ID = new ResourceLocation(CorrectDamaged.MODID, "piercing");

    private PiercingKeyword() {}

    @Override public ResourceLocation getId() {
        return ID;
    }

    @Override public String getTranslationKey() {
        return "keyword." + CorrectDamaged.MODID + ".piercing";
    }

    @Override public KeywordCategory getCategory() {
        return KeywordCategory.PHYSICAL;
    }

    @Override public boolean canSeverLimb() {
        return false;
    }
}