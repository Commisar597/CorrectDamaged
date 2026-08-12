package com.KC.correctdamaged.keywords;

import com.KC.correctdamaged.CorrectDamaged;
import net.minecraft.resources.ResourceLocation;

public class BluntKeyword implements IAttackKeyword {

    public static final BluntKeyword INSTANCE = new BluntKeyword();
    private static final ResourceLocation ID = new ResourceLocation(CorrectDamaged.MODID, "blunt");

    private BluntKeyword() {

    }

    @Override public ResourceLocation getId() {
        return ID;
    }

    @Override public String getTranslationKey() {
        return "keyword." + CorrectDamaged.MODID + ".blunt";
    }

    @Override public KeywordCategory getCategory() {
        return KeywordCategory.PHYSICAL;
    }

    @Override public boolean canSeverLimb() {
        return false;
    }
}