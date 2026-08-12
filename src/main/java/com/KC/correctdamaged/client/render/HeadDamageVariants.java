package com.KC.correctdamaged.client.render;

import java.util.Map;

import static com.KC.correctdamaged.client.render.FreeUVCubeRenderer.FaceUV;

public final class HeadDamageVariants {

    private HeadDamageVariants() {
    }

    public static final Map<Integer, BodyDamageVariant> VARIANTS = Map.of(
            1, createHeadDown(),
            2, createHeadTop(),
            3, createHeadRightHalf(),
            4, createHeadLeftHalf()
    );

    private static BodyDamageVariant createHeadDown() {
        return new BodyDamageVariant(
                new CustomCube("head_down", -4F, -4F, -4F, 8F, 4F, 8F, 0F, new CubeUV(
                        FaceUV.of(8F, 12F, 16F, 16F),
                        FaceUV.of(24F, 12F, 32F, 16F),
                        FaceUV.of(0F, 12F, 8F, 16F),
                        FaceUV.of(16F, 12F, 24F, 16F),
                        null,
                        FaceUV.of(16F, 0F, 24F, 8F)
                )),
                new CustomCube("hat_down", -4F, -4F, -4F, 8F, 4F, 8F, 0.5F, new CubeUV(
                        FaceUV.of(40F, 12F, 48F, 16F),
                        FaceUV.of(56F, 12F, 64F, 16F),
                        FaceUV.of(32F, 12F, 40F, 16F),
                        FaceUV.of(48F, 12F, 56F, 16F),
                        null,
                        FaceUV.of(48F, 0F, 56F, 8F)
                ))
        );
    }

    private static BodyDamageVariant createHeadTop() {
        return new BodyDamageVariant(
                new CustomCube("head_top", -4F, -8F, -4F, 8F, 4F, 8F, 0F, new CubeUV(
                        FaceUV.of(8F, 8F, 16F, 12F),
                        FaceUV.of(24F, 8F, 32F, 12F),
                        FaceUV.of(0F, 8F, 8F, 12F),
                        FaceUV.of(16F, 8F, 24F, 12F),
                        FaceUV.of(8F, 0F, 16F, 8F),
                        null
                )),
                new CustomCube("hat_top", -4F, -8F, -4F, 8F, 4F, 8F, 0.5F, new CubeUV(
                        FaceUV.of(40F, 8F, 48F, 12F),
                        FaceUV.of(56F, 8F, 64F, 12F),
                        FaceUV.of(32F, 8F, 40F, 12F),
                        FaceUV.of(48F, 8F, 56F, 12F),
                        FaceUV.of(40F, 0F, 48F, 8F),
                        null
                ))
        );
    }

    private static BodyDamageVariant createHeadRightHalf() {
        return new BodyDamageVariant(
                new CustomCube("head_right", -4F, -8F, -4F, 4F, 8F, 8F, 0F, new CubeUV(
                        FaceUV.of(8F, 8F, 12F, 16F),
                        FaceUV.of(28F, 8F, 32F, 16F),
                        FaceUV.of(0F, 8F, 8F, 16F),
                        null,
                        FaceUV.of(8F, 0F, 12F, 8F),
                        FaceUV.of(16F, 0F, 20F, 8F)
                )),
                new CustomCube("hat_right", -4F, -8F, -4F, 4F, 8F, 8F, 0.5F, new CubeUV(
                        FaceUV.of(40F, 8F, 44F, 16F),
                        FaceUV.of(60F, 8F, 64F, 16F),
                        FaceUV.of(32F, 8F, 40F, 16F),
                        null,
                        FaceUV.of(40F, 0F, 44F, 8F),
                        FaceUV.of(48F, 0F, 52F, 8F)
                ))
        );
    }

    private static BodyDamageVariant createHeadLeftHalf() {
        return new BodyDamageVariant(
                new CustomCube("head_left", 0F, -8F, -4F, 4F, 8F, 8F, 0F, new CubeUV(
                        FaceUV.of(12F, 8F, 16F, 16F),
                        FaceUV.of(24F, 8F, 28F, 16F),
                        null,
                        FaceUV.of(16F, 8F, 24F, 16F),
                        FaceUV.of(12F, 0F, 16F, 8F),
                        FaceUV.of(20F, 0F, 24F, 8F)
                )),
                new CustomCube("hat_left", 0F, -8F, -4F, 4F, 8F, 8F, 0.5F, new CubeUV(
                        FaceUV.of(44F, 8F, 48F, 16F),
                        FaceUV.of(56F, 8F, 60F, 16F),
                        null,
                        FaceUV.of(48F, 8F, 56F, 16F),
                        FaceUV.of(44F, 0F, 48F, 8F),
                        FaceUV.of(52F, 0F, 56F, 8F)
                ))
        );
    }
}