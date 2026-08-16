package com.KC.correctdamaged.client.render.customRender;

import com.KC.correctdamaged.client.render.customRender.FreeUVCubeRenderer.FaceUV;

public record CubeUV(
        FaceUV front,
        FaceUV back,
        FaceUV left,
        FaceUV right,
        FaceUV top,
        FaceUV bottom
) {}