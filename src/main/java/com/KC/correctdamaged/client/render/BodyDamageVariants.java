package com.KC.correctdamaged.client.render;

import java.util.Map;

import static com.KC.correctdamaged.client.render.FreeUVCubeRenderer.FaceUV;

public final class BodyDamageVariants {

    private BodyDamageVariants() {}

    public static final Map<Integer, BodyDamageVariant> VARIANTS = Map.of(
            1, createChestHole(),
            2, createStomachHole(),
            3, createDestroyedLeftStomach(),
            4, createDestroyedRightStomach(),
            5, createDestroyedLeftSide(),
            6, createDestroyedRightSide(),
            7, createRightBodyHalf(),
            8, createLeftBodyHalf()
    );

    private static BodyDamageVariant createChestHole() {
        return new BodyDamageVariant(
                new CustomCube("body1", -4F, 0F, -2F, 8F, 2F, 4F, 0F, new CubeUV(
                        FaceUV.of(20F, 20F, 28F, 22F),
                        FaceUV.of(32F, 20F, 40F, 22F),
                        FaceUV.of(16F, 20F, 20F, 22F),
                        FaceUV.of(28F, 20F, 32F, 22F),
                        FaceUV.of(20F, 16F, 28F, 20F),
                        null
                )),

                new CustomCube("body2", -4F, 2F, -2F, 2F, 4F, 4F, 0F, new CubeUV(
                        FaceUV.of(20F, 22F, 22F, 26F),
                        FaceUV.of(38F, 22F, 40F, 26F),
                        FaceUV.of(16F, 22F, 20F, 26F),
                        null,
                        null,
                        null
                )),

                new CustomCube("body3", 2F, 2F, -2F, 2F, 4F, 4F, 0F, new CubeUV(
                        FaceUV.of(26F, 22F, 28F, 26F),
                        FaceUV.of(32F, 22F, 34F, 26F),
                        null,
                        FaceUV.of(28F, 22F, 32F, 26F),
                        null,
                        null
                )),

                new CustomCube("body4", -4F, 6F, -2F, 8F, 6F, 4F, 0F, new CubeUV(
                        FaceUV.of(20F, 26F, 28F, 32F),
                        FaceUV.of(32F, 26F, 40F, 32F),
                        FaceUV.of(16F, 26F, 20F, 32F),
                        FaceUV.of(28F, 26F, 32F, 32F),
                        null,
                        FaceUV.of(28F, 16F, 36F, 20F)
                )),

                new CustomCube("bodyLayer1", -4.5F, -0.5F, -2.5F, 9F, 2F, 5F, 0F, new CubeUV(
                        FaceUV.of(20F, 36F, 28F, 38F),
                        FaceUV.of(32F, 36F, 40F, 38F),
                        FaceUV.of(16F, 36F, 20F, 38F),
                        FaceUV.of(28F, 36F, 32F, 38F),
                        FaceUV.of(20F, 32F, 28F, 36F),
                        null
                )),

                new CustomCube("bodyLayer2", -4.5F, 1.5F, -2.5F, 2F, 4F, 5F, 0F, new CubeUV( // H=4
                        FaceUV.of(20F, 38F, 22F, 42F),
                        FaceUV.of(38F, 38F, 40F, 42F),
                        FaceUV.of(16F, 38F, 20F, 42F),
                        null, null, null
                )),

                new CustomCube("bodyLayer3", 2.5F, 1.5F, -2.5F, 2F, 4F, 5F, 0F, new CubeUV( // H=4
                        FaceUV.of(26F, 38F, 28F, 42F),
                        FaceUV.of(32F, 38F, 34F, 42F),
                        null,
                        FaceUV.of(28F, 38F, 32F, 42F),
                        null, null
                )),

                new CustomCube("bodyLayer4", -4.5F, 5.5F, -2.5F, 9F, 6F, 5F, 0F, new CubeUV( // Смещение Y и H исправлены
                        FaceUV.of(20F, 42F, 28F, 48F),
                        FaceUV.of(32F, 42F, 40F, 48F),
                        FaceUV.of(16F, 42F, 20F, 48F),
                        FaceUV.of(28F, 42F, 32F, 48F),
                        null,
                        FaceUV.of(28F, 32F, 36F, 36F)
                ))
        );
    }

    private static BodyDamageVariant createStomachHole() {
        return new BodyDamageVariant(
                new CustomCube("body1", -4F, 0F, -2F, 8F, 7F, 4F, 0F, new CubeUV(
                        FaceUV.of(20F, 20F, 28F, 27F),
                        FaceUV.of(32F, 20F, 40F, 27F),
                        FaceUV.of(16F, 20F, 20F, 27F),
                        FaceUV.of(28F, 20F, 32F, 27F),
                        FaceUV.of(20F, 16F, 28F, 20F),
                        null
                )),

                new CustomCube("body2", -4F, 7F, -2F, 2F, 4F, 4F, 0F, new CubeUV(
                        FaceUV.of(20F, 27F, 22F, 31F),
                        FaceUV.of(38F, 27F, 40F, 31F),
                        FaceUV.of(16F, 27F, 20F, 31F),
                        null, null, null
                )),

                new CustomCube("body3", 2F, 7F, -2F, 2F, 4F, 4F, 0F, new CubeUV(
                        FaceUV.of(26F, 27F, 28F, 31F),
                        FaceUV.of(32F, 27F, 34F, 31F),
                        null,
                        FaceUV.of(28F, 27F, 32F, 31F),
                        null, null
                )),

                new CustomCube("body4", -4F, 11F, -2F, 8F, 1F, 4F, 0F, new CubeUV(
                        FaceUV.of(20F, 31F, 28F, 32F),
                        FaceUV.of(32F, 31F, 40F, 32F),
                        FaceUV.of(16F, 31F, 20F, 32F),
                        FaceUV.of(28F, 31F, 32F, 32F),
                        null,
                        FaceUV.of(28F, 16F, 36F, 20F)
                )),

                new CustomCube("bodyLayer1", -4.5F, -0.5F, -2.5F, 9F, 7F, 5F, 0F, new CubeUV(
                        FaceUV.of(20F, 36F, 28F, 43F),
                        FaceUV.of(32F, 36F, 40F, 43F),
                        FaceUV.of(16F, 36F, 20F, 43F),
                        FaceUV.of(28F, 36F, 32F, 43F),
                        FaceUV.of(20F, 32F, 28F, 36F),
                        null
                )),

                new CustomCube("bodyLayer2", -4.5F, 6.5F, -2.5F, 2F, 4F, 5F, 0F, new CubeUV( // H=4
                        FaceUV.of(20F, 43F, 22F, 47F),
                        FaceUV.of(38F, 43F, 40F, 47F),
                        FaceUV.of(16F, 43F, 20F, 47F),
                        null, null, null
                )),

                new CustomCube("bodyLayer3", 2.5F, 6.5F, -2.5F, 2F, 4F, 5F, 0F, new CubeUV( // H=4
                        FaceUV.of(26F, 43F, 28F, 47F),
                        FaceUV.of(32F, 43F, 34F, 47F),
                        null,
                        FaceUV.of(28F, 43F, 32F, 47F),
                        null, null
                )),

                new CustomCube("bodyLayer4", -4.5F, 10.5F, -2.5F, 9F, 1F, 4F, 0F, new CubeUV( // Смещение Y и H исправлены
                        FaceUV.of(20F, 47F, 28F, 48F),
                        FaceUV.of(32F, 47F, 40F, 48F),
                        FaceUV.of(16F, 47F, 20F, 48F),
                        FaceUV.of(28F, 47F, 32F, 48F),
                        null,
                        FaceUV.of(28F, 32F, 36F, 36F)
                ))
        );
    }

    private static BodyDamageVariant createDestroyedRightStomach() {
        return new BodyDamageVariant(
                new CustomCube("body1", -4F, 0F, -2F, 8F, 6F, 4F, 0F, new CubeUV(
                        FaceUV.of(20F, 20F, 28F, 26F),
                        FaceUV.of(32F, 20F, 40F, 26F),
                        FaceUV.of(16F, 20F, 20F, 26F),
                        FaceUV.of(28F, 20F, 32F, 26F),
                        FaceUV.of(20F, 16F, 28F, 20F),
                        null
                )),

                new CustomCube("body2", -2F, 6F, -2F, 6F, 1F, 4F, 0F, new CubeUV(
                        FaceUV.of(20F, 26F, 26F, 27F),
                        FaceUV.of(34F, 26F, 26F, 27F),
                        null,
                        FaceUV.of(28F, 26F, 32F, 27F),
                        null,
                        null
                )),

                new CustomCube("body3", -1F, 7F, -2F, 5F, 3F, 4F, 0F, new CubeUV(
                        FaceUV.of(23F, 27F, 28F, 30F),
                        FaceUV.of(32F, 27F, 37F, 30F),
                        null,
                        FaceUV.of(28F, 27F, 32F, 30F),
                        null,
                        null
                )),

                new CustomCube("body4", -2F, 10F, -2F, 6F, 1F, 4F, 0F, new CubeUV(
                        FaceUV.of(20F, 30F, 28F, 31F),
                        FaceUV.of(34F, 30F, 37F, 31F),
                        null,
                        FaceUV.of(28F, 30F, 32F, 31F),
                        null,
                        null
                )),

                new CustomCube("body5", -4F, 11F, -2F, 8F, 1F, 4F, 0F, new CubeUV(
                        FaceUV.of(20F, 31F, 28F, 32F),
                        FaceUV.of(32F, 31F, 40F, 32F),
                        FaceUV.of(16F, 31F, 20F, 32F),
                        FaceUV.of(28F, 31F, 32F, 32F),
                        null,
                        FaceUV.of(28F, 16F, 36F, 20F)
                )),

                new CustomCube("bodyLayer1", -4.5F, -0.5F, -2.5F, 9F, 6F, 5F, 0F, new CubeUV(
                        FaceUV.of(20F, 36F, 28F, 42F),
                        FaceUV.of(32F, 36F, 40F, 42F),
                        FaceUV.of(16F, 36F, 20F, 42F),
                        FaceUV.of(28F, 36F, 32F, 42F),
                        FaceUV.of(20F, 32F, 28F, 36F),
                        null
                )),

                new CustomCube("bodyLayer2", -1.5F, 5.5F, -2.5F, 6F, 1F, 5F, 0F, new CubeUV(
                        FaceUV.of(20F, 42F, 26F, 43F),
                        FaceUV.of(34F, 42F, 26F, 43F),
                        null,
                        FaceUV.of(28F, 42F, 32F, 43F),
                        null,
                        null
                )),

                new CustomCube("bodyLayer3", -0.5F, 6.5F, -2.5F, 5F, 4F, 5F, 0F, new CubeUV(
                        FaceUV.of(23F, 43F, 28F, 46F),
                        FaceUV.of(32F, 43F, 37F, 46F),
                        null,
                        FaceUV.of(28F, 42F, 32F, 46F),
                        null,
                        null
                )),

                new CustomCube("bodyLayer4", -1.5F, 10.5F, -2.5F, 6F, 1F, 5F, 0F, new CubeUV(
                        FaceUV.of(20F, 46F, 28F, 47F),
                        FaceUV.of(34F, 46F, 37F, 47F),
                        null,
                        FaceUV.of(28F, 46F, 32F, 47F),
                        null,
                        null
                )),

                new CustomCube("bodyLayer5", -4.5F, 11.5F, -2.5F, 9F, 1F, 5F, 0F, new CubeUV(
                        FaceUV.of(20F, 47F, 28F, 48F),
                        FaceUV.of(32F, 47F, 40F, 48F),
                        FaceUV.of(16F, 47F, 20F, 48F),
                        FaceUV.of(28F, 47F, 32F, 48F),
                        null,
                        FaceUV.of(28F, 32F, 36F, 36F)
                ))
        );
    }


    private static BodyDamageVariant createDestroyedLeftStomach() {
        return new BodyDamageVariant(
                new CustomCube("body1", -4F, 0F, -2F, 8F, 6F, 4F, 0F, new CubeUV(
                        FaceUV.of(20F, 20F, 28F, 26F),
                        FaceUV.of(32F, 20F, 40F, 26F),
                        FaceUV.of(16F, 20F, 20F, 26F),
                        FaceUV.of(28F, 20F, 32F, 26F),
                        FaceUV.of(20F, 16F, 28F, 20F),
                        null
                )),

                new CustomCube("body2", -4F, 6F, -2F, 6F, 1F, 4F, 0F, new CubeUV(
                        FaceUV.of(20F, 26F, 26F, 27F),
                        FaceUV.of(34F, 26F, 40F, 27F),
                        FaceUV.of(16F, 26F, 20F, 27F),
                        null,
                        null,
                        null
                )),

                new CustomCube("body3", -4F, 7F, -2F, 5F, 3F, 4F, 0F, new CubeUV(
                        FaceUV.of(20F, 27F, 25F, 30F),
                        FaceUV.of(35F, 27F, 40F, 30F),
                        FaceUV.of(16F, 27F, 20F, 30F),
                        null,
                        null,
                        null
                )),

                new CustomCube("body4", -4F, 10F, -2F, 6F, 1F, 4F, 0F, new CubeUV(
                        FaceUV.of(20F, 30F, 26F, 31F),
                        FaceUV.of(34F, 30F, 40F, 31F),
                        FaceUV.of(16F, 30F, 20F, 31F),
                        null,
                        null,
                        null
                )),

                new CustomCube("body5", -4F, 11F, -2F, 8F, 1F, 4F, 0F, new CubeUV(
                        FaceUV.of(20F, 31F, 28F, 32F),
                        FaceUV.of(32F, 31F, 40F, 32F),
                        FaceUV.of(16F, 31F, 20F, 32F),
                        FaceUV.of(28F, 31F, 32F, 32F),
                        null,
                        FaceUV.of(28F, 16F, 36F, 20F)
                )),

                new CustomCube("bodyLayer1", -4.5F, -0.5F, -2.5F, 9F, 6F, 5F, 0F, new CubeUV(
                        FaceUV.of(20F, 36F, 28F, 42F),
                        FaceUV.of(32F, 36F, 40F, 42F),
                        FaceUV.of(16F, 36F, 20F, 42F),
                        FaceUV.of(28F, 36F, 32F, 42F),
                        FaceUV.of(20F, 32F, 28F, 36F),
                        null
                )),

                new CustomCube("bodyLayer2", -4.5F, 5.5F, -2.5F, 6F, 1F, 5F, 0F, new CubeUV(
                        FaceUV.of(20F, 42F, 26F, 43F),
                        FaceUV.of(34F, 42F, 40F, 43F),
                        FaceUV.of(16F, 42F, 20F, 43F),
                        null,
                        null,
                        null
                )),

                new CustomCube("bodyLayer3", -4.5F, 6.5F, -2.5F, 5F, 4F, 5F, 0F, new CubeUV(
                        FaceUV.of(20F, 43F, 25F, 46F),
                        FaceUV.of(35F, 43F, 40F, 46F),
                        FaceUV.of(16F, 43F, 20F, 46F),
                        null,
                        null,
                        null
                )),

                new CustomCube("bodyLayer4", -4.5F, 10.5F, -2.5F, 6F, 1F, 5F, 0F, new CubeUV(
                        FaceUV.of(20F, 46F, 26F, 47F),
                        FaceUV.of(34F, 46F, 40F, 47F),
                        FaceUV.of(16F, 46F, 20F, 47F),
                        null,
                        null,
                        null
                )),

                new CustomCube("bodyLayer5", -4.5F, 11.5F, -2.5F, 9F, 1F, 5F, 0F, new CubeUV(
                        FaceUV.of(20F, 47F, 28F, 48F),
                        FaceUV.of(32F, 47F, 40F, 48F),
                        FaceUV.of(16F, 47F, 20F, 48F),
                        FaceUV.of(28F, 47F, 32F, 48F),
                        null,
                        FaceUV.of(28F, 32F, 36F, 36F)
                ))
        );
    }

    private static BodyDamageVariant createDestroyedLeftSide() {
        return new BodyDamageVariant(
                new CustomCube("body1", -4F, 0F, -2F, 8F, 3F, 4F, 0F, new CubeUV(
                        FaceUV.of(20F, 20F, 28F, 23F),
                        FaceUV.of(32F, 20F, 40F, 23F),
                        FaceUV.of(16F, 20F, 20F, 23F),
                        FaceUV.of(28F, 20F, 32F, 23F),
                        FaceUV.of(20F, 16F, 28F, 20F),
                        null
                )),

                new CustomCube("body2", -2F, 3F, -2F, 6F, 1F, 4F, 0F, new CubeUV(
                        FaceUV.of(22F, 23F, 28F, 24F),
                        FaceUV.of(32F, 23F, 38F, 24F),
                        null,
                        FaceUV.of(28F, 23F, 32F, 24F),
                        null,
                        null
                )),

                new CustomCube("body3", -1F, 4F, -2F, 5F, 4F, 4F, 0F, new CubeUV(
                        FaceUV.of(23F, 24F, 28F, 28F),
                        FaceUV.of(32F, 24F, 37F, 28F),
                        null,
                        FaceUV.of(28F, 24F, 32F, 28F),
                        null,
                        null
                )),

                new CustomCube("body4", -2F, 8F, -2F, 6F, 1F, 4F, 0F, new CubeUV(
                        FaceUV.of(22F, 28F, 28F, 29F),
                        FaceUV.of(32F, 28F, 38F, 29F),
                        null,
                        FaceUV.of(28F, 28F, 32F, 29F),
                        null,
                        null
                )),

                new CustomCube("body5", -4F, 9F, -2F, 8F, 3F, 4F, 0F, new CubeUV(
                        FaceUV.of(20F, 29F, 28F, 32F),
                        FaceUV.of(32F, 29F, 40F, 32F),
                        FaceUV.of(16F, 29F, 20F, 32F),
                        FaceUV.of(28F, 29F, 32F, 32F),
                        null,
                        FaceUV.of(28F, 16F, 36F, 20F)
                )),

                new CustomCube("bodyLayer1", -4.5F, -0.5F, -2.5F, 9F, 3F, 5F, 0F, new CubeUV(
                        FaceUV.of(20F, 36F, 28F, 39F),
                        FaceUV.of(32F, 36F, 40F, 39F),
                        FaceUV.of(16F, 36F, 20F, 39F),
                        FaceUV.of(28F, 36F, 32F, 39F),
                        FaceUV.of(20F, 32F, 28F, 36F),
                        null
                )),

                new CustomCube("bodyLayer2", -1.5F, 2.5F, -2.5F, 6F, 1F, 5F, 0F, new CubeUV(
                        FaceUV.of(22F, 39F, 28F, 40F),
                        FaceUV.of(32F, 39F, 38F, 40F),
                        null,
                        FaceUV.of(28F, 39F, 32F, 40F),
                        null,
                        null
                )),

                new CustomCube("bodyLayer3", -0.5F, 3.5F, -2.5F, 5F, 4F, 5F, 0F, new CubeUV(
                        FaceUV.of(23F, 40F, 28F, 44F),
                        FaceUV.of(32F, 40F, 37F, 44F),
                        null,
                        FaceUV.of(28F, 40F, 32F, 44F),
                        null,
                        null
                )),

                new CustomCube("bodyLayer4", -1.5F, 7.5F, -2.5F, 6F, 1F, 5F, 0F, new CubeUV(
                        FaceUV.of(22F, 44F, 28F, 45F),
                        FaceUV.of(32F, 44F, 38F, 45F),
                        null,
                        FaceUV.of(28F, 44F, 32F, 45F),
                        null,
                        null
                )),

                new CustomCube("bodyLayer5", -4.5F, 8.5F, -2.5F, 9F, 3F, 5F, 0F, new CubeUV(
                        FaceUV.of(20F, 45F, 28F, 48F),
                        FaceUV.of(32F, 45F, 40F, 48F),
                        FaceUV.of(16F, 45F, 20F, 48F),
                        FaceUV.of(28F, 45F, 32F, 48F),
                        null,
                        FaceUV.of(28F, 32F, 36F, 36F)
                ))
        );
    }

    private static BodyDamageVariant createDestroyedRightSide() {
        return new BodyDamageVariant(
                new CustomCube("body1", -4F, 0F, -2F, 8F, 3F, 4F, 0F, new CubeUV(
                        FaceUV.of(20F, 20F, 28F, 23F),
                        FaceUV.of(32F, 20F, 40F, 23F),
                        FaceUV.of(16F, 20F, 20F, 23F),
                        FaceUV.of(28F, 20F, 32F, 23F),
                        FaceUV.of(20F, 16F, 28F, 20F),
                        null
                )),

                new CustomCube("body2", -4F, 3F, -2F, 6F, 1F, 4F, 0F, new CubeUV(
                        FaceUV.of(20F, 23F, 26F, 24F),
                        FaceUV.of(34F, 23F, 40F, 24F),
                        FaceUV.of(16F, 23F, 20F, 24F),
                        null,
                        null,
                        null
                )),

                new CustomCube("body3", -4F, 4F, -2F, 5F, 4F, 4F, 0F, new CubeUV(
                        FaceUV.of(20F, 24F, 25F, 28F),
                        FaceUV.of(35F, 24F, 40F, 28F),
                        FaceUV.of(16F, 24F, 20F, 28F),
                        null,
                        null,
                        null
                )),

                new CustomCube("body4", -4F, 8F, -2F, 6F, 1F, 4F, 0F, new CubeUV(
                        FaceUV.of(20F, 28F, 26F, 29F),
                        FaceUV.of(34F, 28F, 40F, 29F),
                        FaceUV.of(16F, 28F, 20F, 29F),
                        null,
                        null,
                        null
                )),

                new CustomCube("body5", -4F, 9F, -2F, 8F, 3F, 4F, 0F, new CubeUV(
                        FaceUV.of(20F, 29F, 28F, 32F),
                        FaceUV.of(32F, 29F, 40F, 32F),
                        FaceUV.of(16F, 29F, 20F, 32F),
                        FaceUV.of(28F, 29F, 32F, 32F),
                        null,
                        FaceUV.of(28F, 16F, 36F, 20F)
                )),

                new CustomCube("bodyLayer1", -4.5F, -0.5F, -2.5F, 9F, 3F, 5F, 0F, new CubeUV(
                        FaceUV.of(20F, 36F, 28F, 39F),
                        FaceUV.of(32F, 36F, 40F, 39F),
                        FaceUV.of(16F, 36F, 20F, 39F),
                        FaceUV.of(28F, 36F, 32F, 39F),
                        FaceUV.of(20F, 32F, 28F, 36F),
                        null
                )),

                new CustomCube("bodyLayer2", -4.5F, 2.5F, -2.5F, 6F, 1F, 5F, 0F, new CubeUV(
                        FaceUV.of(20F, 39F, 26F, 40F),
                        FaceUV.of(34F, 39F, 40F, 40F),
                        FaceUV.of(16F, 39F, 20F, 40F),
                        null,
                        null,
                        null
                )),

                new CustomCube("bodyLayer3", -4.5F, 3.5F, -2.5F, 5F, 4F, 5F, 0F, new CubeUV(
                        FaceUV.of(20F, 40F, 25F, 44F),
                        FaceUV.of(35F, 40F, 40F, 44F),
                        FaceUV.of(16F, 40F, 20F, 44F),
                        null,
                        null,
                        null
                )),

                new CustomCube("bodyLayer4", -4.5F, 7.5F, -2.5F, 6F, 1F, 5F, 0F, new CubeUV(
                        FaceUV.of(20F, 44F, 26F, 45F),
                        FaceUV.of(34F, 44F, 40F, 45F),
                        FaceUV.of(16F, 44F, 20F, 45F),
                        null,
                        null,
                        null
                )),

                new CustomCube("bodyLayer5", -4.5F, 8.5F, -2.5F, 9F, 3F, 5F, 0F, new CubeUV(
                        FaceUV.of(20F, 45F, 28F, 48F),
                        FaceUV.of(32F, 45F, 40F, 48F),
                        FaceUV.of(16F, 45F, 20F, 48F),
                        FaceUV.of(28F, 45F, 32F, 48F),
                        null,
                        FaceUV.of(28F, 32F, 36F, 36F)
                ))
        );
    }

    private static BodyDamageVariant createRightBodyHalf() {
        return new BodyDamageVariant(
                new CustomCube("bodyRH", -4F, 0F, -2F, 4F, 12F, 4F, 0F, new CubeUV(
                        FaceUV.of(20F, 20F, 24F, 32F),
                        FaceUV.of(36F, 20F, 40F, 32F),
                        FaceUV.of(16F, 20F, 20F, 32F),
                        null,
                        FaceUV.of(20F, 16F, 24F, 20F),
                        FaceUV.of(28F, 16F, 32F, 20F)
                )),

                new CustomCube("bodyLayerRH", -4.5F, -0.5F, -2.5F, 5F, 13F, 5F, 0F, new CubeUV(
                        FaceUV.of(20F, 36F, 24F, 48F),
                        FaceUV.of(36F, 36F, 40F, 48F),
                        FaceUV.of(16F, 36F, 20F, 48F),
                        null,
                        FaceUV.of(20F, 32F, 24F, 36F),
                        FaceUV.of(28F, 32F, 32F, 36F)
                ))
        );
    }

    private static BodyDamageVariant createLeftBodyHalf() {
        return new BodyDamageVariant(
                new CustomCube("bodyLH", 0F, 0F, -2F, 4F, 12F, 4F, 0F, new CubeUV(
                        FaceUV.of(24F, 20F, 28F, 32F),
                        FaceUV.of(32F, 20F, 36F, 32F),
                        null,
                        FaceUV.of(28F, 20F, 32F, 32F),
                        FaceUV.of(24F, 16F, 28F, 20F),
                        FaceUV.of(32F, 16F, 36F, 20F)
                )),

                new CustomCube("bodyLayerLH", -0.5F, -0.5F, -2.5F, 4F, 13F, 5F, 0F, new CubeUV(
                        FaceUV.of(24F, 36F, 28F, 48F),
                        FaceUV.of(32F, 36F, 36F, 48F),
                        null,
                        FaceUV.of(28F, 36F, 32F, 48F),
                        FaceUV.of(24F, 32F, 28F, 36F),
                        FaceUV.of(32F, 32F, 36F, 36F)
                ))
        );
    }
}