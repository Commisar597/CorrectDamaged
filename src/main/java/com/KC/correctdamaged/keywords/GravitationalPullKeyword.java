package com.KC.correctdamaged.keywords;

import com.KC.correctdamaged.CorrectDamaged;
import net.minecraft.resources.ResourceLocation;

public class GravitationalPullKeyword implements IAttackKeyword {

    public static final GravitationalPullKeyword INSTANCE = new GravitationalPullKeyword();
    private static final ResourceLocation ID = new ResourceLocation(CorrectDamaged.MODID, "gravitational_pull");

    private GravitationalPullKeyword() {

    }

    @Override public ResourceLocation getId() {
        return ID;
    }

    @Override public String getTranslationKey() {
        return "keyword." + CorrectDamaged.MODID + ".gravitational_pull";
    }

    @Override public KeywordCategory getCategory() {
        return KeywordCategory.SPATIAL;
    }

    @Override public boolean canSeverLimb() {
        return false;
    }
}