package com.KC.correctdamaged.keywords;

import com.KC.correctdamaged.CorrectDamaged;
import net.minecraft.resources.ResourceLocation;

public class SoulDamageKeyword implements IAttackKeyword {

    public static final SoulDamageKeyword INSTANCE = new SoulDamageKeyword();
    private static final ResourceLocation ID = new ResourceLocation(CorrectDamaged.MODID, "soul_damage");

    private SoulDamageKeyword() {

    }

    @Override public ResourceLocation getId() {
        return ID;
    }

    @Override public String getTranslationKey() {
        return "keyword." + CorrectDamaged.MODID + ".soul_damage";
    }

    @Override public KeywordCategory getCategory() {
        return KeywordCategory.SPIRITUAL;
    }

    @Override public boolean canSeverLimb() {
        return false;
    }
}