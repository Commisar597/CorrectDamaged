package com.KC.correctdamaged.client.render;

public record CustomCube(
        String name,
        float x,
        float y,
        float z,
        float width,
        float height,
        float depth,
        float deformation,
        CubeUV uv
) {
}