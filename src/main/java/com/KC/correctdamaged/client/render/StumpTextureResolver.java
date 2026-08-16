package com.KC.correctdamaged.client.render;

import com.KC.correctdamaged.CorrectDamaged;
import com.KC.correctdamaged.capability.LimbData;
import com.KC.correctdamaged.capability.LimbManager;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;

public final class StumpTextureResolver {

    private StumpTextureResolver() {}

    public enum LimbType {
        HEAD,
        BODY,
        RIGHT_ARM,
        LEFT_ARM,
        RIGHT_LEG,
        LEFT_LEG
    }

    private static ResourceLocation tex(String name) {
        return new ResourceLocation(CorrectDamaged.MODID, "textures/entity/" + name);
    }

    public static ResourceLocation getStumpTexture(AbstractClientPlayer player, String baseName, LimbType type) {
        boolean isBurnt = LimbManager.get(player)
                .map(data -> checkIsBurnt(data, type))
                .orElse(false);

        if (isBurnt) {
            return tex(baseName + "_burnt_bone.png");
        }

        return tex(baseName + ".png");
    }

    private static boolean checkIsBurnt(LimbData data, LimbType type) {
        return switch (type) {
            case HEAD -> data.getHead().getSkinMask() == 2;
            case BODY -> data.getBodyState() == 2 || data.getShowSkeleton() == 2;
            case RIGHT_ARM -> data.getBoneRightArm() > 3;
            case LEFT_ARM -> data.getBoneLeftArm() > 3;
            case RIGHT_LEG -> data.getBoneRightLeg() > 3;
            case LEFT_LEG -> data.getBoneLeftLeg() > 3;
        };
    }
}