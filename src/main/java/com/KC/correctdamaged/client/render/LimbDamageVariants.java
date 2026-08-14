package com.KC.correctdamaged.client.render;

import static com.KC.correctdamaged.client.render.FreeUVCubeRenderer.FaceUV;

public final class LimbDamageVariants {

    private LimbDamageVariants() {}

    public enum LimbType {
        RIGHT_ARM,
        LEFT_ARM,
        RIGHT_SLEEVE,
        LEFT_SLEEVE,
        RIGHT_LEG,
        LEFT_LEG,
        RIGHT_PANTS,
        LEFT_PANTS
    }

    public static CustomCube createLimbStageCube(LimbType type, boolean slim, float height) {
        return switch (type) {
            case RIGHT_ARM    -> createArm(type, false, slim, height, 0.0F,  40F, 16F);
            case RIGHT_SLEEVE -> createArm(type, false, slim, height, 0.25F, 40F, 32F);
            case LEFT_ARM     -> createArm(type, true,  slim, height, 0.0F,  32F, 48F);
            case LEFT_SLEEVE  -> createArm(type, true,  slim, height, 0.25F, 48F, 48F);
            case RIGHT_LEG    -> createLeg(type, height, 0.0F,  0F,  16F);
            case RIGHT_PANTS  -> createLeg(type, height, 0.25F, 0F,  32F);
            case LEFT_LEG     -> createLeg(type, height, 0.0F,  16F, 48F);
            case LEFT_PANTS   -> createLeg(type, height, 0.25F, 0F,  48F);
        };
    }

    private static CustomCube createArm(LimbType type, boolean isLeft, boolean slim, float height, float def, float u0, float v0) {
        float width = slim ? 3.0F : 4.0F;
        float x = isLeft ? -1.0F : (slim ? -2.0F : -3.0F);
        float d = 4.0F;

        FaceUV top = FaceUV.of(u0 + d, v0, u0 + d + width, v0 + d);

        FaceUV left  = FaceUV.of(u0, v0 + d, u0 + d, v0 + d + height);
        FaceUV front = FaceUV.of(u0 + d, v0 + d, u0 + d + width, v0 + d + height);
        FaceUV right = FaceUV.of(u0 + d + width, v0 + d, u0 + d + width + d, v0 + d + height);
        FaceUV back  = FaceUV.of(u0 + d + width + d, v0 + d, u0 + d + width + d + width, v0 + d + height);

        CubeUV uv = new CubeUV(front, back, left, right, top, null);

        return new CustomCube(type.name().toLowerCase() + "_stage", x, -2.0F, -2.0F, width, height, d, def, uv);
    }

    private static CustomCube createLeg(LimbType type, float height, float def, float u0, float v0) {
        float width = 4.0F;
        float x = -2.0F;
        float d = 4.0F;

        FaceUV top = FaceUV.of(u0 + d, v0, u0 + d + width, v0 + d);

        FaceUV left  = FaceUV.of(u0, v0 + d, u0 + d, v0 + d + height);
        FaceUV front = FaceUV.of(u0 + d, v0 + d, u0 + d + width, v0 + d + height);
        FaceUV right = FaceUV.of(u0 + d + width, v0 + d, u0 + d + width + d, v0 + d + height);
        FaceUV back  = FaceUV.of(u0 + d + width + d, v0 + d, u0 + d + width + d + width, v0 + d + height);

        CubeUV uv = new CubeUV(front, back, left, right, top, null);

        return new CustomCube(type.name().toLowerCase() + "_stage", x, 0.0F, -2.0F, width, height, d, def, uv);
    }
}