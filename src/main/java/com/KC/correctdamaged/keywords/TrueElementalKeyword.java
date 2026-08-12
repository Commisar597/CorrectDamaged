package com.KC.correctdamaged.keywords;

import com.KC.correctdamaged.CorrectDamaged;
import net.minecraft.resources.ResourceLocation;

public class TrueElementalKeyword implements IAttackKeyword {

    public static final TrueElementalKeyword INSTANCE = new TrueElementalKeyword();
    private static final ResourceLocation ID = new ResourceLocation(CorrectDamaged.MODID, "true_elemental");

    private TrueElementalKeyword() {

    }

    @Override public ResourceLocation getId() {
        return ID;
    }

    @Override public String getTranslationKey() {
        return "keyword." + CorrectDamaged.MODID + ".true_elemental";
    }

    @Override public KeywordCategory getCategory() {
        return KeywordCategory.SPIRITUAL;
    }

    @Override public boolean canSeverLimb() {
        return false;
    }
}