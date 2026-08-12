package com.KC.correctdamaged.keywords;

import com.KC.correctdamaged.CorrectDamaged;
import net.minecraft.resources.ResourceLocation;

public class EntropyKeyword implements IAttackKeyword {

    public static final EntropyKeyword INSTANCE = new EntropyKeyword();
    private static final ResourceLocation ID = new ResourceLocation(CorrectDamaged.MODID, "entropy");

    private EntropyKeyword() {

    }

    @Override public ResourceLocation getId() {
        return ID;
    }

    @Override public String getTranslationKey() {
        return "keyword." + CorrectDamaged.MODID + ".entropy";
    }

    @Override public KeywordCategory getCategory() {
        return KeywordCategory.SPIRITUAL;
    }

    @Override public boolean canSeverLimb() {
        return true;
    }
}