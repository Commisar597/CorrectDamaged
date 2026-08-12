package com.KC.correctdamaged.keywords;

import net.minecraft.resources.ResourceLocation;

public interface IAttackKeyword {

    ResourceLocation getId();

    String getTranslationKey();

    KeywordCategory getCategory();

    boolean canSeverLimb();
}