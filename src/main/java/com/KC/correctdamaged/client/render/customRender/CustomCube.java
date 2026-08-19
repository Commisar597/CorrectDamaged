package com.KC.correctdamaged.client.render.customRender;

/**
 * Структура данных (Record), описывающая пользовательский куб для рендеринга.
 * Хранит геометрические параметры, расширение/деформацию и карту UV-развертки.
 *
 * @param name        Уникальное или отладочное имя элемента.
 * @param x           Начальная позиция X в локальных координатах.
 * @param y           Начальная позиция Y в локальных координатах.
 * @param z           Начальная позиция Z в локальных координатах.
 * @param width       Ширина куба (по оси X).
 * @param height      Высота куба (по оси Y).
 * @param depth       Глубина куба (по оси Z).
 * @param deformation Значение деформации (расширения/сжатия) куба во все стороны.
 * @param uv          Набор UV-координат для граней этого куба.
 */
public record CustomCube(
        String name,
        float x,
        float y,
        float z,
        float width,
        float height,
        float depth,
        float deformation,
        CubeUV uv
) {
}