package com.KC.correctdamaged.keywords;

import com.KC.correctdamaged.CorrectDamaged;
import net.minecraft.resources.ResourceLocation;

public class DisintegrationKeyword implements IAttackKeyword {

    public static final DisintegrationKeyword INSTANCE = new DisintegrationKeyword();
    private static final ResourceLocation ID = new ResourceLocation(CorrectDamaged.MODID, "disintegration");

    private DisintegrationKeyword() {

    }

    @Override public ResourceLocation getId() {
        return ID;
    }

    @Override public String getTranslationKey() {
        return "keyword." + CorrectDamaged.MODID + ".disintegration";
    }

    @Override public KeywordCategory getCategory() {
        return KeywordCategory.SPATIAL;
    }

    @Override public boolean canSeverLimb() {
        return true;
    }
}