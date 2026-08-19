package com.KC.correctdamaged.client.render.torso;

import com.KC.correctdamaged.capability.visual.BodyVoxelMatrix;
import com.KC.correctdamaged.client.render.customRender.CubeUV;
import com.KC.correctdamaged.client.render.customRender.FreeUVCubeRenderer;
import com.KC.correctdamaged.client.render.customRender.FreeUVCubeRenderer.FaceUV;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

/**
 * Воксельный рендерер геометрии туловища (Torso).
 * Зачем нужен: Выполняет повоксельную отрисовку разрушаемого туловища игрока (8x12x4).
 * Определяет, является ли воксел наружной кожей или обнаженной мякотью/мясом (Flesh),
 * скрывает изолированные невидимые вокселы и накладывает соответствующий UV-маппинг.
 */
public class VoxelBodyRenderer {

    /** Текстурный атлас мяса/мякоти для срезов ранений. */
    private static final ResourceLocation FLESH_TEXTURE =
            new ResourceLocation("correct_damaged", "textures/entity/flesh_atlas.png");

    private static final int SKIN_TEX_W = 64;
    private static final int SKIN_TEX_H = 64;
    private static final int FLESH_TEX_W = 32;
    private static final int FLESH_TEX_H = 32;

    /**
     * Основной метод отрисовки воксельной матрицы туловища.
     * Зачем нужен: Проходит по всем координатам трехмерной сетки `BodyVoxelMatrix` и рендерит
     * воксели (кубы 1x1x1 пиксель) с динамическим подбором текстуры кожи или мяса на границах ран.
     *
     * @param poseStack Матрица трансформаций.
     * @param buffer Буфер рендеринга.
     * @param packedLight Уровень освещения.
     * @param player Объект игрока.
     * @param matrix Трехмерная воксельная матрица состояния туловища.
     */
    public static void renderVoxelBody(
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            AbstractClientPlayer player,
            BodyVoxelMatrix matrix
    ) {
        VertexConsumer skinConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(player.getSkinTextureLocation()));
        VertexConsumer fleshConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(FLESH_TEXTURE));

        PoseStack.Pose pose = poseStack.last();
        int overlay = OverlayTexture.NO_OVERLAY;

        for (int x = 0; x < BodyVoxelMatrix.WIDTH_X; x++) {
            for (int y = 0; y < BodyVoxelMatrix.HEIGHT_Y; y++) {
                for (int z = 0; z < BodyVoxelMatrix.DEPTH_Z; z++) {

                    // Пропуск пустых (выбитых) вокселей
                    if (!matrix.isSolid(x, y, z)) continue;

                    // Пропуск полностью закрытых со всех сторон вокселей для оптимизации Draw Calls
                    if (isFullyIsolated(matrix, x, y, z)) continue;

                    // Локальные координаты куба воксела относительно центра модели туловища
                    float x0 = (x - 4);
                    float x1 = x0 + 1;
                    float y0 = y;
                    float y1 = y0 + 1;
                    float z0 = (z - 2);
                    float z1 = z0 + 1;

                    boolean borderToDamage = hasDamagedNeighbor(matrix, x, y, z);

                    // Если воксел граничит с разрушенным участком — рендерим текстуру мяса (Flesh)
                    if (borderToDamage) {
                        boolean isSurface = isSurfaceVoxel(x, y, z);

                        CubeUV fleshUV = buildFleshUV(matrix, x, y, z, isSurface);
                        if (hasAnyFace(fleshUV)) {
                            FreeUVCubeRenderer.renderBox(
                                    pose, fleshConsumer,
                                    x0, y0, z0, x1, y1, z1,
                                    FLESH_TEX_W, FLESH_TEX_H,
                                    packedLight, overlay,
                                    1.0F, 1.0F, 1.0F, 1.0F,
                                    fleshUV
                            );
                        }
                    } else {
                        // В противном случае рендерим стандартную кожу игрока с его скина
                        CubeUV skinUV = buildSkinUV(matrix, x, y, z);
                        if (hasAnyFace(skinUV)) {
                            FreeUVCubeRenderer.renderBox(
                                    pose, skinConsumer,
                                    x0, y0, z0, x1, y1, z1,
                                    SKIN_TEX_W, SKIN_TEX_H,
                                    packedLight, overlay,
                                    1.0F, 1.0F, 1.0F, 1.0F,
                                    skinUV
                            );
                        }
                    }
                }
            }
        }
    }

    /**
     * Строит UV-развертку мяса/мякоти для вокселей на срезе раны.
     */
    private static CubeUV buildFleshUV(BodyVoxelMatrix matrix, int x, int y, int z, boolean isBorder) {
        FaceUV fleshFace = isBorder
                ? getTopRightQuadrantUV(x, y, z)
                : getDeepFleshUV(x, y, z);

        FaceUV front  = !matrix.isSolidSafe(x, y, z - 1) ? fleshFace : null;
        FaceUV back   = !matrix.isSolidSafe(x, y, z + 1) ? fleshFace : null;
        FaceUV left   = !matrix.isSolidSafe(x - 1, y, z) ? fleshFace : null;
        FaceUV right  = !matrix.isSolidSafe(x + 1, y, z) ? fleshFace : null;
        FaceUV top    = !matrix.isSolidSafe(x, y - 1, z) ? fleshFace : null;
        FaceUV bottom = !matrix.isSolidSafe(x, y + 1, z) ? fleshFace : null;

        return new CubeUV(front, back, left, right, top, bottom);
    }

    /**
     * Высчитывает квадрант UV для поверхностных слоев повреждения мякоти.
     */
    private static FaceUV getTopRightQuadrantUV(int x, int y, int z) {
        int uOffset = 16 + ((x + y + z) % 2 * 8);
        int vOffset = 0  + ((x * 2 + z) % 2 * 8);
        return FaceUV.of(uOffset, vOffset, uOffset + 8, vOffset + 8);
    }

    /**
     * Высчитывает квадрант UV для глубоких слоев мяса на основе псевдослучайного распределения по координатам.
     */
    private static FaceUV getDeepFleshUV(int x, int y, int z) {
        int quadrant = Math.abs(x * 7 + y * 13 + z * 31) % 3;

        int baseU = 0;
        int baseV = 0;

        switch (quadrant) {
            case 0 -> { baseU = 0;  baseV = 0;  }
            case 1 -> { baseU = 0;  baseV = 16; }
            case 2 -> { baseU = 16; baseV = 16; }
        }

        int uOffset = baseU + ((x + y) % 2 * 8);
        int vOffset = baseV + ((y + z) % 2 * 8);

        return FaceUV.of(uOffset, vOffset, uOffset + 8, vOffset + 8);
    }

    /**
     * Проверяет, расположен ли воксел на внешней границе кубоида туловища.
     */
    private static boolean isSurfaceVoxel(int x, int y, int z) {
        return x == 0 || x == BodyVoxelMatrix.WIDTH_X - 1 ||
                y == 0 || y == BodyVoxelMatrix.HEIGHT_Y - 1 ||
                z == 0 || z == BodyVoxelMatrix.DEPTH_Z - 1;
    }

    /**
     * Проверяет, граничит ли текущий цельный воксел хотя бы с одним разрушенным (пустым) вокселом.
     */
    private static boolean hasDamagedNeighbor(BodyVoxelMatrix matrix, int x, int y, int z) {
        if (BodyVoxelMatrix.isInBounds(x, y, z - 1) && !matrix.isSolid(x, y, z - 1)) return true;
        if (BodyVoxelMatrix.isInBounds(x, y, z + 1) && !matrix.isSolid(x, y, z + 1)) return true;
        if (BodyVoxelMatrix.isInBounds(x - 1, y, z) && !matrix.isSolid(x - 1, y, z)) return true;
        if (BodyVoxelMatrix.isInBounds(x + 1, y, z) && !matrix.isSolid(x + 1, y, z)) return true;
        if (BodyVoxelMatrix.isInBounds(x, y - 1, z) && !matrix.isSolid(x, y - 1, z)) return true;
        if (BodyVoxelMatrix.isInBounds(x, y + 1, z) && !matrix.isSolid(x, y + 1, z)) return true;

        return false;
    }

    /**
     * Проверяет, находится ли воксел в абсолютной изоляции (нет ни одного цельного соседнего воксела).
     */
    private static boolean isFullyIsolated(BodyVoxelMatrix matrix, int x, int y, int z) {
        return !matrix.isSolidSafe(x, y, z - 1) &&
                !matrix.isSolidSafe(x, y, z + 1) &&
                !matrix.isSolidSafe(x - 1, y, z) &&
                !matrix.isSolidSafe(x + 1, y, z) &&
                !matrix.isSolidSafe(x, y - 1, z) &&
                !matrix.isSolidSafe(x, y + 1, z);
    }

    /**
     * Рассчитывает точные координаты UV-развертки скина игрока (в пикселях 64x64) для конкретного внешнего воксела туловища.
     */
    private static CubeUV buildSkinUV(BodyVoxelMatrix matrix, int x, int y, int z) {
        FaceUV front  = (z == 0  && !matrix.isSolidSafe(x, y, z - 1)) ? FaceUV.of(20 + x, 20 + y, 20 + x + 1, 20 + y + 1) : null;
        FaceUV back   = (z == 3  && !matrix.isSolidSafe(x, y, z + 1)) ? FaceUV.of(32 + (7 - x), 20 + y, 32 + (7 - x) + 1, 20 + y + 1) : null;
        FaceUV left   = (x == 0  && !matrix.isSolidSafe(x - 1, y, z)) ? FaceUV.of(16 + z, 20 + y, 16 + z + 1, 20 + y + 1) : null;
        FaceUV right  = (x == 7  && !matrix.isSolidSafe(x + 1, y, z)) ? FaceUV.of(28 + (3 - z), 20 + y, 28 + (3 - z) + 1, 20 + y + 1) : null;
        FaceUV top    = (y == 0  && !matrix.isSolidSafe(x, y - 1, z)) ? FaceUV.of(20 + x, 16 + z, 20 + x + 1, 16 + z + 1) : null;
        FaceUV bottom = (y == 11 && !matrix.isSolidSafe(x, y + 1, z)) ? FaceUV.of(28 + x, 16 + z, 28 + x + 1, 16 + z + 1) : null;

        return new CubeUV(front, back, left, right, top, bottom);
    }

    /**
     * Проверяет, содержит ли CubeUV хотя бы одну видимую грань.
     */
    private static boolean hasAnyFace(CubeUV uv) {
        return uv.front() != null || uv.back() != null || uv.left() != null ||
                uv.right() != null || uv.top() != null || uv.bottom() != null;
    }
}