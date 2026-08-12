package com.KC.correctdamaged.keywords;

import com.KC.correctdamaged.CorrectDamaged;
import net.minecraft.resources.ResourceLocation;

public class InternalImplosionKeyword implements IAttackKeyword {

    public static final InternalImplosionKeyword INSTANCE = new InternalImplosionKeyword();
    private static final ResourceLocation ID = new ResourceLocation(CorrectDamaged.MODID, "internal_implosion");

    private InternalImplosionKeyword() {

    }

    @Override public ResourceLocation getId() {
        return ID;
    }

    @Override public String getTranslationKey() {
        return "keyword." + CorrectDamaged.MODID + ".internal_implosion";
    }

    @Override public KeywordCategory getCategory() {
        return KeywordCategory.SPIRITUAL;
    }

    @Override public boolean canSeverLimb() {
        return false;
    }
}