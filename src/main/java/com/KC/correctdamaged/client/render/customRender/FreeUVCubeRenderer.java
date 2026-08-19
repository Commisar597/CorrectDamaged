package com.KC.correctdamaged.client.render.customRender;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import org.joml.Vector3f;
import org.joml.Vector4f;

/**
 * Низкоуровневый утилитный рендерер кубов с произвольной (произвольными UV-координатами) разверткой текстуры.
 * Позволяет отрисовывать отдельные грани с заданной сеткой текстурных координат.
 */
public final class FreeUVCubeRenderer {

    private FreeUVCubeRenderer() {}

    /**
     * Данные текстурных координат (UV) для конкретной грани куба.
     *
     * @param u0 Начальная координата U (в пикселях текстуры).
     * @param v0 Начальная координата V (в пикселях текстуры).
     * @param u1 Конечная координата U (в пикселях текстуры).
     * @param v1 Конечная координата V (в пикселях текстуры).
     */
    public record FaceUV(float u0, float v0, float u1, float v1) {
        /**
         * Фабричный метод для удобной инициализации {@link FaceUV}.
         */
        public static FaceUV of(float u0, float v0, float u1, float v1) {
            return new FaceUV(u0, v0, u1, v1);
        }
    }

    /**
     * Отрисовывает параллелепипед (куб) по заданным границам и параметрам развертки.
     *
     * @param pose          Текущая матрица трансформации (PoseStack).
     * @param consumer      Буфер вершин для записи (VertexConsumer).
     * @param x0            Минимальная X-координата локальной модели.
     * @param y0            Минимальная Y-координата локальной модели.
     * @param z0            Минимальная Z-координата локальной модели.
     * @param x1            Максимальная X-координата локальной модели.
     * @param y1            Максимальная Y-координата локальной модели.
     * @param z1            Максимальная Z-координата локальной модели.
     * @param texWidth      Ширина текстуры в пикселях (например, 64).
     * @param texHeight     Высота текстуры в пикселях (например, 64).
     * @param packedLight   Упакованное освещение (Block/Sky Light).
     * @param packedOverlay Упакованный оверлей (красный оттенок при получении урона и т.д.).
     * @param red           Канал красного цвета (0.0F - 1.0F).
     * @param green         Канал зеленого цвета (0.0F - 1.0F).
     * @param blue          Канал синего цвета (0.0F - 1.0F).
     * @param alpha         Прозрачность (0.0F - 1.0F).
     * @param uv            Контейнер UV-координат для всех 6 граней.
     */
    public static void renderBox(
            PoseStack.Pose pose, VertexConsumer consumer,
            float x0, float y0, float z0,
            float x1, float y1, float z1,
            int texWidth, int texHeight,
            int packedLight, int packedOverlay,
            float red, float green, float blue, float alpha,
            CubeUV uv
    ) {
        // Локальные вершины передней стенки (z0)
        Vector3f a = new Vector3f(x0, y0, z0);
        Vector3f b = new Vector3f(x1, y0, z0);
        Vector3f c = new Vector3f(x1, y1, z0);
        Vector3f d = new Vector3f(x0, y1, z0);

        // Локальные вершины задней стенки (z1)
        Vector3f e = new Vector3f(x0, y0, z1);
        Vector3f f = new Vector3f(x1, y0, z1);
        Vector3f g = new Vector3f(x1, y1, z1);
        Vector3f h = new Vector3f(x0, y1, z1);

        // Передняя грань (Front)
        if (uv.front() != null) {
            quad(pose, consumer, a, b, c, d, uv.front(), texWidth, texHeight, packedLight, packedOverlay, red, green, blue, alpha, 0, 0, -1);
        }
        // Задняя грань (Back)
        if (uv.back() != null) {
            quad(pose, consumer, f, e, h, g, uv.back(), texWidth, texHeight, packedLight, packedOverlay, red, green, blue, alpha, 0, 0, 1);
        }
        // Левая грань (Left)
        if (uv.left() != null) {
            quad(pose, consumer, e, a, d, h, uv.left(), texWidth, texHeight, packedLight, packedOverlay, red, green, blue, alpha, -1, 0, 0);
        }
        // Правая грань (Right)
        if (uv.right() != null) {
            quad(pose, consumer, b, f, g, c, uv.right(), texWidth, texHeight, packedLight, packedOverlay, red, green, blue, alpha, 1, 0, 0);
        }
        // Верхняя грань (Top)
        if (uv.top() != null) {
            quad(pose, consumer, e, f, b, a, uv.top(), texWidth, texHeight, packedLight, packedOverlay, red, green, blue, alpha, 0, -1, 0);
        }
        // Нижняя грань (Bottom)
        if (uv.bottom() != null) {
            quad(pose, consumer, d, c, g, h, uv.bottom(), texWidth, texHeight, packedLight, packedOverlay, red, green, blue, alpha, 0, 1, 0);
        }
    }

    /**
     * Отрисовывает одну плоскую четырёхугольную грань (Quad) из 4-х вершин.
     */
    private static void quad(
            PoseStack.Pose pose, VertexConsumer consumer,
            Vector3f p0, Vector3f p1, Vector3f p2, Vector3f p3,
            FaceUV uv, int texWidth, int texHeight,
            int packedLight, int packedOverlay,
            float red, float green, float blue, float alpha,
            float nx, float ny, float nz
    ) {
        // Трансформируем вектор нормали в соответствии с текущей матрицей
        Vector3f normal = new Vector3f(nx, ny, nz);
        pose.normal().transform(normal);

        // Нормализация UV в диапазон 0.0 - 1.0
        float u0 = uv.u0() / texWidth;
        float v0 = uv.v0() / texHeight;
        float u1 = uv.u1() / texWidth;
        float v1 = uv.v1() / texHeight;

        // Генерация 4 вершин для четырёхугольника
        vertex(pose, consumer, p0, u0, v0, packedLight, packedOverlay, red, green, blue, alpha, normal);
        vertex(pose, consumer, p1, u1, v0, packedLight, packedOverlay, red, green, blue, alpha, normal);
        vertex(pose, consumer, p2, u1, v1, packedLight, packedOverlay, red, green, blue, alpha, normal);
        vertex(pose, consumer, p3, u0, v1, packedLight, packedOverlay, red, green, blue, alpha, normal);
    }

    /**
     * Записывает одну конкретную вершину в буфер отрисовки.
     */
    private static void vertex(
            PoseStack.Pose pose, VertexConsumer consumer,
            Vector3f position, float u, float v,
            int packedLight, int packedOverlay,
            float red, float green, float blue, float alpha,
            Vector3f normal
    ) {
        // Переводим пиксельные/модельные координаты в блоки (деление на 16.0)
        Vector4f pos = new Vector4f(
                position.x() / 16.0F,
                position.y() / 16.0F,
                position.z() / 16.0F,
                1.0F
        );
        // Применяем позиционную матрицу трансформации
        pos.mul(pose.pose());

        // Передаём данные вершины в VertexConsumer
        consumer.vertex(
                pos.x(), pos.y(), pos.z(),
                red, green, blue, alpha,
                u, v,
                packedOverlay, packedLight,
                normal.x(), normal.y(), normal.z()
        );
    }
}