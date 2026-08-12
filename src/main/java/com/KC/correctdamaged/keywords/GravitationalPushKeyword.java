package com.KC.correctdamaged.keywords;

import com.KC.correctdamaged.CorrectDamaged;
import net.minecraft.resources.ResourceLocation;

public class GravitationalPushKeyword implements IAttackKeyword {

    public static final GravitationalPushKeyword INSTANCE = new GravitationalPushKeyword();
    private static final ResourceLocation ID = new ResourceLocation(CorrectDamaged.MODID, "gravitational_push");

    private GravitationalPushKeyword() {

    }

    @Override public ResourceLocation getId() {
        return ID;
    }

    @Override public String getTranslationKey() {
        return "keyword." + CorrectDamaged.MODID + ".gravitational_push";
    }

    @Override public KeywordCategory getCategory() {
        return KeywordCategory.SPATIAL;
    }

    @Override public boolean canSeverLimb() {
        return false;
    }
}