package com.KC.correctdamaged.keywords;

import com.KC.correctdamaged.CorrectDamaged;
import net.minecraft.resources.ResourceLocation;

public class PoisonKeyword implements IAttackKeyword {

    public static final PoisonKeyword INSTANCE = new PoisonKeyword();
    private static final ResourceLocation ID = new ResourceLocation(CorrectDamaged.MODID, "poison");

    private PoisonKeyword() {

    }

    @Override public ResourceLocation getId() {
        return ID;
    }

    @Override public String getTranslationKey() {
        return "keyword." + CorrectDamaged.MODID + ".poison";
    }

    @Override public KeywordCategory getCategory() {
        return KeywordCategory.BIOLOGICAL;
    }

    @Override public boolean canSeverLimb() {
        return false;
    }
}