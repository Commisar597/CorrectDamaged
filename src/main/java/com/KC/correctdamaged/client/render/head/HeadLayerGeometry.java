package com.KC.correctdamaged.client.render.head;

import com.KC.correctdamaged.capability.HeadData;

public class HeadLayerGeometry {

    public static float getSkinRadius(HeadData data) {
        return 4.0F;
    }

    public static float[] getSkinYBounds(HeadData data) {
        float r = getSkinRadius(data);
        return new float[] {-4.0F - r, -4.0F + r};
    }

    public static float getMuscleRadius(HeadData data) {
        if (data.getSkinMask() == 0) {
            return 4.0F;
        }
        return 3.875F;
    }

    public static float[] getMuscleYBounds(HeadData data) {
        float r = getMuscleRadius(data);
        return new float[] {-4.0F - r, -4.0F + r};
    }

    public static float getSkullRadius(HeadData data) {
        if (data.getSkinMask() == 0 && data.getMuscleMask() == 0) {
            return 4.0F;
        }
        if (data.getSkinMask() == 0) {
            return 3.875F;
        }
        return 3.5F;
    }

    public static float[] getSkullYBounds(HeadData data) {
        float r = getSkullRadius(data);
        return new float[] {-4.0F - r, -4.0F + r};
    }
}