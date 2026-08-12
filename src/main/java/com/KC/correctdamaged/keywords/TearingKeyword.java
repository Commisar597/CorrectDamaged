package com.KC.correctdamaged.keywords;

import com.KC.correctdamaged.CorrectDamaged;
import net.minecraft.resources.ResourceLocation;

public class TearingKeyword implements IAttackKeyword {

    public static final TearingKeyword INSTANCE = new TearingKeyword();
    private static final ResourceLocation ID = new ResourceLocation(CorrectDamaged.MODID, "tearing");

    private TearingKeyword() {

    }

    @Override public ResourceLocation getId() {
        return ID;
    }

    @Override public String getTranslationKey() {
        return "keyword." + CorrectDamaged.MODID + ".tearing";
    }

    @Override public KeywordCategory getCategory() {
        return KeywordCategory.PHYSICAL;
    }

    @Override public boolean canSeverLimb() {
        return false;
    }
}