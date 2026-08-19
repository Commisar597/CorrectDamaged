package com.KC.correctdamaged.client.render.customRender;

import com.KC.correctdamaged.client.render.customRender.FreeUVCubeRenderer.FaceUV;

/**
 * Контейнер UV-координат для всех шести сторон куба.
 * Если для конкретной грани передано значение {@code null}, эта грань рендериться не будет.
 *
 * @param front  UV передней грани.
 * @param back   UV задней грани.
 * @param left   UV левой грани.
 * @param right  UV правой грани.
 * @param top    UV верхней грани.
 * @param bottom UV нижней грани.
 */
public record CubeUV(
        FaceUV front,
        FaceUV back,
        FaceUV left,
        FaceUV right,
        FaceUV top,
        FaceUV bottom
) {}