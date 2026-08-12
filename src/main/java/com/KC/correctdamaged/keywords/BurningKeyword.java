package com.KC.correctdamaged.keywords;

import com.KC.correctdamaged.CorrectDamaged;
import net.minecraft.resources.ResourceLocation;

public class BurningKeyword implements IAttackKeyword {

    public static final BurningKeyword INSTANCE = new BurningKeyword();
    private static final ResourceLocation ID = new ResourceLocation(CorrectDamaged.MODID, "burning");

    private BurningKeyword() {}

    @Override public ResourceLocation getId() {
        return ID;
    }

    @Override public String getTranslationKey() {
        return "keyword." + CorrectDamaged.MODID + ".burning";
    }

    @Override public KeywordCategory getCategory() {
        return KeywordCategory.THERMAL;
    }

    @Override public boolean canSeverLimb() {
        return false;
    }
}