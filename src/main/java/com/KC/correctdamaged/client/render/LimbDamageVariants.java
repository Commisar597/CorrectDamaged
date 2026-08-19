package com.KC.correctdamaged.client.render;

import com.KC.correctdamaged.client.render.customRender.CubeUV;
import com.KC.correctdamaged.client.render.customRender.CustomCube;

import static com.KC.correctdamaged.client.render.customRender.FreeUVCubeRenderer.FaceUV;

/**
 * Фабрика процедурных CustomCube для визуализации сегментированных повреждений.
 * Зачем нужен: Позволяет высчитать точные координаты UV-развертки (включая рукава и штанины)
 * для отдельного укороченного или обрезанного блока кожи/одежды, чтобы текстура скина игрока не натягивалась и не искажалась.
 */
public final class LimbDamageVariants {

    private LimbDamageVariants() {}

    /**
     * Типы сегментов конечностей и их внешней одежды (слой куртки/штанов).
     */
    public enum LimbType {
        RIGHT_ARM,
        LEFT_ARM,
        RIGHT_SLEEVE,
        LEFT_SLEEVE,
        RIGHT_LEG,
        LEFT_LEG,
        RIGHT_PANTS,
        LEFT_PANTS
    }

    /**
     * Создает процедурный CustomCube с заданной высотой и смещением Y для фрагмента конечности.
     *
     * @param type Тип конечности или элемента одежды.
     * @param slim Признак тонких рук (3px).
     * @param height Высота отрисовываемого сегмента.
     * @param yOffset Смещение по оси Y от сустава.
     * @return Заполненный объект CustomCube с просчитанными гранями UV.
     */
    public static CustomCube createLimbSegmentCube(LimbType type, boolean slim, float height, float yOffset) {
        return switch (type) {
            case RIGHT_ARM    -> createArmSegment(type, false, slim, height, yOffset, 0.0F, 40F, 16F);
            case RIGHT_SLEEVE -> createArmSegment(type, false, slim, height, yOffset, 0.25F, 40F, 32F);
            case LEFT_ARM     -> createArmSegment(type, true,  slim, height, yOffset, 0.0F,  32F, 48F);
            case LEFT_SLEEVE  -> createArmSegment(type, true,  slim, height, yOffset, 0.25F, 48F, 48F);
            case RIGHT_LEG    -> createLegSegment(type, height, yOffset, 0.0F,  0F,  16F);
            case RIGHT_PANTS  -> createLegSegment(type, height, yOffset, 0.25F, 0F,  32F);
            case LEFT_LEG     -> createLegSegment(type, height, yOffset, 0.0F,  16F, 48F);
            case LEFT_PANTS   -> createLegSegment(type, height, yOffset, 0.25F, 0F,  48F);
        };
    }

    /**
     * Создает геометрию и правильную маппинг-сетку текстуры для сегментов рук и рукавов.
     */
    private static CustomCube createArmSegment(
            LimbType type, boolean isLeft, boolean slim,
            float height, float yOffset, float def, float u0, float v0
    ) {
        float width = slim ? 3.0F : 4.0F;
        float x = isLeft ? -1.0F : (slim ? -2.0F : -3.0F);
        float d = 4.0F;

        // Корректный расчёт смещения UV по Y для рукавов и плечей
        float vShift = yOffset - (-2.0F);

        // Верхняя грань рендерится только на вершине плеча, нижняя — только на самом краю кисти
        FaceUV top = (yOffset == -2.0F) ? FaceUV.of(u0 + d, v0, u0 + d + width, v0 + d) : null;
        FaceUV bottom = (yOffset + height >= 10.0F) ? FaceUV.of(u0 + d + width, v0,
                u0 + d + width + width, v0 + d) : null;

        float vStart = v0 + d + vShift;
        float vEnd = vStart + height;

        FaceUV left  = FaceUV.of(u0, vStart, u0 + d, vEnd);
        FaceUV front = FaceUV.of(u0 + d, vStart, u0 + d + width, vEnd);
        FaceUV right = FaceUV.of(u0 + d + width, vStart, u0 + d + width + d, vEnd);
        FaceUV back  = FaceUV.of(u0 + d + width + d, vStart, u0 + d + width + d + width, vEnd);

        CubeUV uv = new CubeUV(front, back, left, right, top, bottom);

        return new CustomCube(type.name().toLowerCase() + "_segment", x, yOffset,
                -2.0F, width, height, d, def, uv);
    }

    /**
     * Создает геометрию и маппинг-сетку текстуры для сегментов ног и штанин.
     */
    private static CustomCube createLegSegment(
            LimbType type, float height, float yOffset, float def, float u0, float v0
    ) {
        float width = 4.0F;
        float x = -2.0F;
        float d = 4.0F;

        float vShift = yOffset;

        FaceUV top = (yOffset == 0.0F) ? FaceUV.of(u0 + d, v0, u0 + d + width, v0 + d) : null;
        FaceUV bottom = (yOffset + height >= 12.0F) ? FaceUV.of(u0 + d + width, v0,
                u0 + d + width + width, v0 + d) : null;

        float vStart = v0 + d + vShift;
        float vEnd = vStart + height;

        FaceUV left  = FaceUV.of(u0, vStart, u0 + d, vEnd);
        FaceUV front = FaceUV.of(u0 + d, vStart, u0 + d + width, vEnd);
        FaceUV right = FaceUV.of(u0 + d + width, vStart, u0 + d + width + d, vEnd);
        FaceUV back  = FaceUV.of(u0 + d + width + d, vStart, u0 + d + width + d + width, vEnd);

        CubeUV uv = new CubeUV(front, back, left, right, top, bottom);

        return new CustomCube(type.name().toLowerCase() + "_segment", x, yOffset,
                -2.0F, width, height, d, def, uv);
    }
}