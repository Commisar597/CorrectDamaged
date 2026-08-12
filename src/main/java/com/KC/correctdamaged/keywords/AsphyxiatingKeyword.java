package com.KC.correctdamaged.keywords;

import com.KC.correctdamaged.CorrectDamaged;
import net.minecraft.resources.ResourceLocation;

public class AsphyxiatingKeyword implements IAttackKeyword {

    public static final AsphyxiatingKeyword INSTANCE = new AsphyxiatingKeyword();
    private static final ResourceLocation ID = new ResourceLocation(CorrectDamaged.MODID, "asphyxiating");

    private AsphyxiatingKeyword()
    {}

    @Override public ResourceLocation getId() {
        return ID;
    }

    @Override public String getTranslationKey() {
        return "keyword." + CorrectDamaged.MODID + ".asphyxiating";
    }

    @Override public KeywordCategory getCategory() {
        return KeywordCategory.BIOLOGICAL;
    }

    @Override public boolean canSeverLimb() {
        return false;
    }
}