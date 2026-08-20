package com.KC.correctdamaged.client.render.torso;

import com.KC.correctdamaged.client.render.customRender.CubeUV;
import com.KC.correctdamaged.client.render.customRender.FreeUVCubeRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

public class TorsoGridRenderHelper {

    public static void renderSegmentBlock(
            PoseStack.Pose pose, VertexConsumer consumer,
            int mask, int blockIndex, float[] bounds,
            CubeUV blockUV,
            int packedLight, int packedOverlay,
            int texWidth, int texHeight
    ) {
        if ((mask & (1 << blockIndex)) == 0) {
            return;
        }

        CubeUV externalUV = buildExternalSkinUV(blockIndex, blockUV);

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

    public static CubeUV buildExternalSkinUV(int blockIndex, CubeUV blockUV) {
        // Раскладываем индекс (0..11) по сетке 2x3x2
        int ix = blockIndex % 2;            // 0 = Лево (-X), 1 = Право (+X)
        int iy = (blockIndex / 2) % 3;     // 0 = Верх (-Y), 1 = Середина, 2 = Низ (+Y)
        int iz = blockIndex / 6;            // 0 = Перед (-Z), 1 = Зад (+Z)

        // Грань рендерится только если блок находится на внешнем краю торса
        boolean renderLeft   = (ix == 0);
        boolean renderRight  = (ix == 1);

        boolean renderTop    = (iy == 0);
        boolean renderBottom = (iy == 2);

        boolean renderFront  = (iz == 0);
        boolean renderBack   = (iz == 1);

        return new CubeUV(
                renderFront  ? blockUV.front()  : null,
                renderBack   ? blockUV.back()   : null,
                renderLeft   ? blockUV.left()   : null,
                renderRight  ? blockUV.right()  : null,
                renderTop    ? blockUV.top()    : null,
                renderBottom ? blockUV.bottom() : null
        );
    }
}