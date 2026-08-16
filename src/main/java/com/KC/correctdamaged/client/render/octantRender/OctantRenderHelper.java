package com.KC.correctdamaged.client.render.octantRender;

import com.KC.correctdamaged.client.render.customRender.CubeUV;
import com.KC.correctdamaged.client.render.customRender.FreeUVCubeRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

public class OctantRenderHelper {

    public static void renderOctant(
            PoseStack.Pose pose, VertexConsumer consumer,
            byte mask, int octantIndex, float[] bounds,
            CubeUV octantUV,
            int packedLight, int packedOverlay,
            int texWidth, int texHeight
    ) {
        if ((mask & (1 << octantIndex)) == 0) {
            return;
        }

        CubeUV externalUV = buildExternalSkinUV(octantIndex, octantUV);

        FreeUVCubeRenderer.renderBox(
                pose, consumer,
                bounds[0], bounds[1], bounds[2],
                bounds[3], bounds[4], bounds[5],
                texWidth, texHeight,
                packedLight, packedOverlay,
                1.0F, 1.0F, 1.0F, 1.0F,
                externalUV
        );
    }

    public static CubeUV buildExternalSkinUV(int octantIndex, CubeUV octantUV) {
        boolean iX = (octantIndex & 1) != 0; // true = Левая половина головы (+X)
        boolean iY = (octantIndex & 2) != 0; // true = Нижняя половина (+Y)
        boolean iZ = (octantIndex & 4) != 0; // true = Затылок (+Z)

        // ИСПРАВЛЕНО: Так как рендерер рисует `left` на стороне -X,
        // внешний край октанта iX=false находится на -X, следовательно ему нужен renderLeft.
        boolean renderLeft   = !iX;  // Октанты правой стороны головы (отвечают за внешнюю грань -X)
        boolean renderRight  = iX;   // Октанты левой стороны головы (отвечают за внешнюю грань +X)

        boolean renderBottom = iY;
        boolean renderTop    = !iY;
        boolean renderFront  = !iZ;
        boolean renderBack   = iZ;

        return new CubeUV(
                renderFront  ? octantUV.front()  : null,
                renderBack   ? octantUV.back()   : null,
                renderLeft   ? octantUV.left()   : null,
                renderRight  ? octantUV.right()  : null,
                renderTop    ? octantUV.top()    : null,
                renderBottom ? octantUV.bottom() : null
        );
    }
}