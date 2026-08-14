package com.KC.correctdamaged.client.render;

import net.minecraft.resources.ResourceLocation;

public record CubeTexture(
        ResourceLocation texture,
        FreeUVCubeRenderer.FaceUV uv,
        int width,
        int height
) {

    public static CubeTexture full(ResourceLocation texture, int width, int height) {
        return new CubeTexture(texture, FreeUVCubeRenderer.FaceUV.of(0F, 0F, width, height), width, height);
    }
}