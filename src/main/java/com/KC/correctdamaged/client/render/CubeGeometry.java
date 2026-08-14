package com.KC.correctdamaged.client.render;

public record CubeGeometry(
        float x0,
        float y0,
        float z0,

        float x1,
        float y1,
        float z1
) {

    public static CubeGeometry box(
            float x0, float y0, float z0,
            float x1, float y1, float z1
    ) {
        return new CubeGeometry(
                x0, y0, z0,
                x1, y1, z1);
    }
}