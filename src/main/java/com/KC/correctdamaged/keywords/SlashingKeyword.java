package com.KC.correctdamaged.keywords;

import com.KC.correctdamaged.CorrectDamaged;
import net.minecraft.resources.ResourceLocation;

public class SlashingKeyword implements IAttackKeyword {

    public static final SlashingKeyword INSTANCE = new SlashingKeyword();
    private static final ResourceLocation ID = new ResourceLocation(CorrectDamaged.MODID, "slashing");

    private SlashingKeyword() {

    }

    @Override public ResourceLocation getId() {
        return ID;
    }

    @Override public String getTranslationKey() {
        return "keyword." + CorrectDamaged.MODID + ".slashing";
    }

    @Override public KeywordCategory getCategory() {
        return KeywordCategory.PHYSICAL;
    }

    @Override public boolean canSeverLimb() {
        return true;
    }
}