package com.KC.correctdamaged.client.render;

import com.KC.correctdamaged.client.render.FreeUVCubeRenderer.FaceUV;

public record CubeUV(
        FaceUV front,
        FaceUV back,
        FaceUV left,
        FaceUV right,
        FaceUV top,
        FaceUV bottom
) {}