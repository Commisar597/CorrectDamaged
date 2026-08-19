package com.KC.correctdamaged.client.render.octantRender;

import com.KC.correctdamaged.client.render.customRender.CubeUV;
import com.KC.correctdamaged.client.render.customRender.FreeUVCubeRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

/**
 * Вспомогательный класс для отрисовки одиночного октанта.
 * Зачем нужен: Проверяет активность октанта в битовой маске, определяет его внешние грани
 * и передает геометрию в низкоуровневый `FreeUVCubeRenderer`.
 */
public class OctantRenderHelper {

    /**
     * Отрисовывает один октант, если соответствующий бит активности взведён в маске.
     *
     * @param pose Матрица трансформаций.
     * @param consumer Буфер вертексов.
     * @param mask Битовая маска видимости октантов (8 бит).
     * @param octantIndex Индекс текущего октанта (0..7).
     * @param bounds Границы октанта [x0, y0, z0, x1, y1, z1].
     * @param octantUV UV-развертка октанта.
     * @param packedLight Освещение.
     * @param packedOverlay Оверлей (напр. повреждения/краснота).
     * @param texWidth Ширина текстурного атласа.
     * @param texHeight Высота текстурного атласа.
     */
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

    /**
     * Фильтрует UV-грани октанта, оставляя только те, которые выходят на внешнюю поверхность головы,
     * отсекая внутренние перегородки смежных октантов.
     *
     * @param octantIndex Индекс октанта (0..7).
     * @param octantUV Полный UV октанта.
     * @return CubeUV с оставленными внешними гранями (внутренние установлены в null).
     */
    public static CubeUV buildExternalSkinUV(int octantIndex, CubeUV octantUV) {
        boolean iX = (octantIndex & 1) != 0; // true = Левая сторона (+X)
        boolean iY = (octantIndex & 2) != 0; // true = Низ (+Y)
        boolean iZ = (octantIndex & 4) != 0; // true = Затылок (+Z)

        // Так как рендерер рисует `left` на -X, октанты правой стороны головы (iX=false)
        // отвечают за внешнюю грань -X.
        boolean renderLeft   = !iX;
        boolean renderRight  = iX;

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