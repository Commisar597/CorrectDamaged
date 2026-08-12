package com.KC.correctdamaged.keywords;

import com.KC.correctdamaged.CorrectDamaged;
import net.minecraft.resources.ResourceLocation;

public class FrostbiteKeyword implements IAttackKeyword
{
    public static final FrostbiteKeyword INSTANCE = new FrostbiteKeyword();
    private static final ResourceLocation ID = new ResourceLocation(CorrectDamaged.MODID, "frostbite");

    private FrostbiteKeyword() {

    }

    @Override public ResourceLocation getId() {
        return ID;
    }

    @Override public String getTranslationKey() {
        return "keyword." + CorrectDamaged.MODID + ".frostbite";
    }

    @Override public KeywordCategory getCategory() {
        return KeywordCategory.THERMAL;
    }

    @Override public boolean canSeverLimb() {
        return false;
    }
}