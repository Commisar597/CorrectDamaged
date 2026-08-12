package com.KC.correctdamaged.keywords;

import com.KC.correctdamaged.CorrectDamaged;
import net.minecraft.resources.ResourceLocation;

public class BlastKeyword implements IAttackKeyword {

    public static final BlastKeyword INSTANCE = new BlastKeyword();
    private static final ResourceLocation ID = new ResourceLocation(CorrectDamaged.MODID, "blast");

    private BlastKeyword() {

    }

    @Override public ResourceLocation getId() {
        return ID;
    }

    @Override public String getTranslationKey() {
        return "keyword." + CorrectDamaged.MODID + ".blast";
    }

    @Override public KeywordCategory getCategory() {
        return KeywordCategory.KINETIC;
    }

    @Override public boolean canSeverLimb() {
        return true;
    }
}