package com.KC.correctdamaged.client.render.octantRender;

import com.KC.correctdamaged.client.render.customRender.CubeUV;
import com.KC.correctdamaged.client.render.customRender.FreeUVCubeRenderer;

/**
 * Утилитарный класс для математического деления (субделения) меша и UV-координат на октанты.
 * Зачем нужен: Высчитывает пространственные границы (AABB) и подразбивает UV-текстурные координаты
 * куба 8x8x8 на 8 меньших октантов (кубиков 4x4x4) для пооктантного разрушения головы.
 */
public class OctreeMeshSplitter {

    /**
     * Высчитывает вершины (AABB) конкретного октанта на основе битовой маски индекса.
     *
     * @param x0 Минимальный X исходного куба.
     * @param y0 Минимальный Y исходного куба.
     * @param z0 Минимальный Z исходного куба.
     * @param x1 Максимальный X исходного куба.
     * @param y1 Максимальный Y исходного куба.
     * @param z1 Максимальный Z исходного куба.
     * @param octantIndex Индекс октанта (0..7), где биты 1, 2, 4 отвечают за X, Y, Z соответственно.
     * @return Массив из 6 значений float: [ox0, oy0, oz0, ox1, oy1, oz1].
     */
    public static float[] getOctantBounds(float x0, float y0, float z0, float x1, float y1, float z1, int octantIndex) {
        float xMid = (x0 + x1) / 2.0F;
        float yMid = (y0 + y1) / 2.0F;
        float zMid = (z0 + z1) / 2.0F;

        boolean iX = (octantIndex & 1) != 0;
        boolean iY = (octantIndex & 2) != 0;
        boolean iZ = (octantIndex & 4) != 0;

        float ox0 = iX ? xMid : x0;
        float ox1 = iX ? x1   : xMid;

        float oy0 = iY ? yMid : y0;
        float oy1 = iY ? y1   : yMid;

        float oz0 = iZ ? zMid : z0;
        float oz1 = iZ ? z1   : zMid;

        return new float[]{ox0, oy0, oz0, ox1, oy1, oz1};
    }

    /**
     * Подразбивает полные UV-координаты шести граней куба для выбранного октанта.
     *
     * @param fullUV Исходная UV-развертка целого куба.
     * @param octantIndex Индекс октанта (0..7).
     * @return Новый объект CubeUV, содержащий усеченные UV для всех 6 граней октанта.
     */
    public static CubeUV getOctantUV(CubeUV fullUV, int octantIndex) {
        boolean iX = (octantIndex & 1) != 0;
        boolean iY = (octantIndex & 2) != 0;
        boolean iZ = (octantIndex & 4) != 0;

        FreeUVCubeRenderer.FaceUV front  = subUV(fullUV.front(),  iX,  iY);
        FreeUVCubeRenderer.FaceUV back   = subUV(fullUV.back(),  !iX,  iY);

        // Для левого поля (правое ухо 0..8) развёртка идёт спереди назад от 8 к 0.
        // Задняя часть (iZ = true) считывает начало текстуры (offsetX = false).
        FreeUVCubeRenderer.FaceUV left   = subUV(fullUV.left(),  !iZ,  iY);

        // Для правого поля (левое ухо 16..24) развёртка идёт спереди назад от 16 к 24.
        // Задняя часть (iZ = true) считывает конец текстуры (offsetX = true).
        FreeUVCubeRenderer.FaceUV right  = subUV(fullUV.right(),  iZ,  iY);

        FreeUVCubeRenderer.FaceUV top    = subUV(fullUV.top(),    iX, !iZ);
        FreeUVCubeRenderer.FaceUV bottom = subUV(fullUV.bottom(), iX,  iZ);

        return new CubeUV(front, back, left, right, top, bottom);
    }

    /**
     * Делит прямоугольник UV пополам по горизонтали и/или вертикали.
     */
    private static FreeUVCubeRenderer.FaceUV subUV(FreeUVCubeRenderer.FaceUV uv, boolean offsetX, boolean offsetY) {
        if (uv == null) return null;

        float uMid = (uv.u0() + uv.u1()) / 2.0F;
        float vMid = (uv.v0() + uv.v1()) / 2.0F;

        float u0 = offsetX ? uMid : uv.u0();
        float u1 = offsetX ? uv.u1() : uMid;

        float v0 = offsetY ? vMid : uv.v0();
        float v1 = offsetY ? uv.v1() : vMid;

        return FreeUVCubeRenderer.FaceUV.of(u0, v0, u1, v1);
    }
}