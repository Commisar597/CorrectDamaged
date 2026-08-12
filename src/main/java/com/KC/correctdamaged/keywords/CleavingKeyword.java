package com.KC.correctdamaged.keywords;

import com.KC.correctdamaged.CorrectDamaged;
import net.minecraft.resources.ResourceLocation;

public class CleavingKeyword implements IAttackKeyword {

    public static final CleavingKeyword INSTANCE = new CleavingKeyword();
    private static final ResourceLocation ID = new ResourceLocation(CorrectDamaged.MODID, "cleaving");

    private CleavingKeyword() {

    }

    @Override public ResourceLocation getId() {
        return ID;
    }

    @Override public String getTranslationKey() {
        return "keyword." + CorrectDamaged.MODID + ".cleaving";
    }

    @Override public KeywordCategory getCategory() {
        return KeywordCategory.PHYSICAL;
    }

    @Override public boolean canSeverLimb() {
        return true;
    }
}