package com.KC.correctdamaged.client.render.octantRender.psevdo;

import com.KC.correctdamaged.client.render.customRender.CubeUV;
import com.KC.correctdamaged.client.render.customRender.FreeUVCubeRenderer;

public class TorsoGridSplitter {

    public static final int TOTAL_BLOCKS = 12;

    public static float[] getBlockBounds(float x0, float y0, float z0, float x1, float y1, float z1, int index) {
        int ix = index % 2;
        int iy = (index / 2) % 3;
        int iz = index / 6;

        float stepX = (x1 - x0) / 2.0F;
        float stepY = (y1 - y0) / 3.0F;
        float stepZ = (z1 - z0) / 2.0F;

        float bx0 = x0 + ix * stepX;
        float by0 = y0 + iy * stepY;
        float bz0 = z0 + iz * stepZ;

        return new float[]{bx0, by0, bz0, bx0 + stepX, by0 + stepY, bz0 + stepZ};
    }

    public static CubeUV getBlockUV(CubeUV fullUV, int index) {
        int ix = index % 2;
        int iy = (index / 2) % 3;
        int iz = index / 6;

        boolean offsetX = (ix == 1);
        boolean offsetZ = (iz == 1);

        FreeUVCubeRenderer.FaceUV front = subUV3Y(fullUV.front(), offsetX, iy);
        FreeUVCubeRenderer.FaceUV back  = subUV3Y(fullUV.back(), !offsetX, iy);
        FreeUVCubeRenderer.FaceUV left  = subUV3Y(fullUV.left(), !offsetZ, iy);
        FreeUVCubeRenderer.FaceUV right = subUV3Y(fullUV.right(), offsetZ, iy);

        FreeUVCubeRenderer.FaceUV top    = subUV2XZ(fullUV.top(), offsetX, !offsetZ);
        FreeUVCubeRenderer.FaceUV bottom = subUV2XZ(fullUV.bottom(), offsetX, offsetZ);

        return new CubeUV(front, back, left, right, top, bottom);
    }

    private static FreeUVCubeRenderer.FaceUV subUV3Y(FreeUVCubeRenderer.FaceUV uv, boolean offsetX, int stepY) {
        if (uv == null) return null;
        float uMid = (uv.u0() + uv.u1()) / 2.0F;
        float u0 = offsetX ? uMid : uv.u0();
        float u1 = offsetX ? uv.u1() : uMid;

        float vHeight = (uv.v1() - uv.v0()) / 3.0F;
        float v0 = uv.v0() + stepY * vHeight;
        float v1 = v0 + vHeight;

        return FreeUVCubeRenderer.FaceUV.of(u0, v0, u1, v1);
    }

    private static FreeUVCubeRenderer.FaceUV subUV2XZ(FreeUVCubeRenderer.FaceUV uv, boolean offsetX, boolean offsetZ) {
        if (uv == null) return null;
        float uMid = (uv.u0() + uv.u1()) / 2.0F;
        float u0 = offsetX ? uMid : uv.u0();
        float u1 = offsetX ? uv.u1() : uMid;

        float vMid = (uv.v0() + uv.v1()) / 2.0F;
        float v0 = offsetZ ? vMid : uv.v0();
        float v1 = offsetZ ? uv.v1() : vMid;

        return FreeUVCubeRenderer.FaceUV.of(u0, v0, u1, v1);
    }
}