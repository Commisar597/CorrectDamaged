package com.KC.correctdamaged.keywords;

import com.KC.correctdamaged.CorrectDamaged;
import net.minecraft.resources.ResourceLocation;

public class BallisticKeyword implements IAttackKeyword
{
    public static final BallisticKeyword INSTANCE = new BallisticKeyword();
    private static final ResourceLocation ID = new ResourceLocation(CorrectDamaged.MODID, "ballistic");

    private BallisticKeyword() {

    }

    @Override public ResourceLocation getId() {
        return ID;

    }
    @Override public String getTranslationKey() {
        return "keyword." + CorrectDamaged.MODID + ".ballistic";
    }

    @Override public KeywordCategory getCategory() {
        return KeywordCategory.KINETIC;
    }

    @Override public boolean canSeverLimb() {
        return false;
    }
}