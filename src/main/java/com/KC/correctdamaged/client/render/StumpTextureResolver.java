package com.KC.correctdamaged.client.render;

import com.KC.correctdamaged.CorrectDamaged;
import com.KC.correctdamaged.capability.visual.LimbData;
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

    private static ResourceLocation tex(String path) {
        return new ResourceLocation(CorrectDamaged.MODID, "textures/entity/" + path);
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
            case HEAD -> false;
            case BODY -> false;
            case RIGHT_ARM -> data.getRightArm().isBurntBone();
            case LEFT_ARM  -> data.getLeftArm().isBurntBone();
            case RIGHT_LEG -> data.getRightLeg().isBurntBone();
            case LEFT_LEG  -> data.getLeftLeg().isBurntBone();
        };
    }
}