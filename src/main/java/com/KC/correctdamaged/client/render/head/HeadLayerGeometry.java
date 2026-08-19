package com.KC.correctdamaged.client.render.head;

import com.KC.correctdamaged.capability.visual.HeadData;

/**
 * Хранилище геометрических параметров (радиусов и вертикальных границ) для слоёв головы.
 * Зачем нужен: Динамически высчитывает размеры кубов (кожа, мышцы, череп) с небольшим смещением
 * масштаба для предотвращения эффекта Z-fighting при наслоении анатомических компонентов.
 */
public class HeadLayerGeometry {

    /**
     * Возвращает радиус внешней кожи головы.
     */
    public static float getSkinRadius(HeadData data) {
        return 4.0F;
    }

    /**
     * Возвращает Y-границы [-Y, +Y] для кожи головы.
     */
    public static float[] getSkinYBounds(HeadData data) {
        float r = getSkinRadius(data);
        return new float[] {-4.0F - r, -4.0F + r};
    }

    /**
     * Возвращает радиус слоя мышц с учётом наличия внешнего слоя кожи.
     */
    public static float getMuscleRadius(HeadData data) {
        if (data.getSkinMask() == 0) {
            return 4.0F;
        }
        return 3.875F;
    }

    /**
     * Возвращает Y-границы для слоя мышц.
     */
    public static float[] getMuscleYBounds(HeadData data) {
        float r = getMuscleRadius(data);
        return new float[] {-4.0F - r, -4.0F + r};
    }

    /**
     * Возвращает радиус черепа с учетом оголенности (отсутствия кожи/мышц).
     */
    public static float getSkullRadius(HeadData data) {
        if (data.getSkinMask() == 0 && data.getMuscleMask() == 0) {
            return 4.0F;
        }
        if (data.getSkinMask() == 0) {
            return 3.875F;
        }
        return 3.5F;
    }

    /**
     * Возвращает Y-границы для черепа.
     */
    public static float[] getSkullYBounds(HeadData data) {
        float r = getSkullRadius(data);
        return new float[] {-4.0F - r, -4.0F + r};
    }
}