package com.KC.correctdamaged.keywords;

import com.KC.correctdamaged.CorrectDamaged;
import net.minecraft.resources.ResourceLocation;

public class CorrosiveKeyword implements IAttackKeyword {

    public static final CorrosiveKeyword INSTANCE = new CorrosiveKeyword();
    private static final ResourceLocation ID = new ResourceLocation(CorrectDamaged.MODID, "corrosive");

    private CorrosiveKeyword() {

    }

    @Override public ResourceLocation getId() {
        return ID;
    }

    @Override public String getTranslationKey() {
        return "keyword." + CorrectDamaged.MODID + ".corrosive";
    }

    @Override public KeywordCategory getCategory() {
        return KeywordCategory.CHEMICAL;
    }

    @Override public boolean canSeverLimb() {
        return false;
    }
}