package com.KC.correctdamaged.keywords;

import com.KC.correctdamaged.CorrectDamaged;
import net.minecraft.resources.ResourceLocation;

public class ElectricalKeyword implements IAttackKeyword {

    public static final ElectricalKeyword INSTANCE = new ElectricalKeyword();
    private static final ResourceLocation ID = new ResourceLocation(CorrectDamaged.MODID, "electrical");

    private ElectricalKeyword() {

    }

    @Override public ResourceLocation getId() {
        return ID;
    }

    @Override public String getTranslationKey() {
        return "keyword." + CorrectDamaged.MODID + ".electrical";
    }

    @Override public KeywordCategory getCategory() {
        return KeywordCategory.BIOLOGICAL;
    }

    @Override public boolean canSeverLimb() {
        return false;
    }
}