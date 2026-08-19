package com.KC.correctdamaged.client.render;

import com.KC.correctdamaged.capability.visual.ArmData;
import com.KC.correctdamaged.capability.visual.LegData;
import com.KC.correctdamaged.capability.LimbManager;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class StumpLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    private final ModelPart cap4x4;
    private final ModelPart cap3x4;

    /**
     * Конструктор слоя рендера обрубков.
     * Зачем нужен: Инициализирует и запекает (bake) 3D-модели «заглушек» (плоских полигонов-спилов)
     * размерами 4x4 (стандарт) и 3x4 (для тонких рук скинов Slim/Alex).
     *
     * @param parent Родительский рендерер игрока.
     */
    public StumpLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent) {
        super(parent);

        this.cap4x4 = createCap(-2.0F, -2.0F, 4.0F, 4.0F, 0, 0, 4, 4);
        this.cap3x4 = createCap(-1.5F, -2.0F, 3.0F, 4.0F, 2, 0, 3, 4);
    }

    /**
     * Фабричный метод для создания отдельного ModelPart плоской заглушки (спила).
     * Зачем нужен: Создает полигон заданной ширины и глубины с очень малой высотой (0.001F) для закрытия среды среза ампутированной руки или ноги.
     *
     * @param x Смещение по X.
     * @param z Смещение по Z.
     * @param width Ширина заглушки.
     * @param depth Глубина заглушки.
     * @param texU Смещение UV по U.
     * @param texV Смещение UV по V.
     * @param texW Ширина текстуры.
     * @param texH Высота текстуры.
     * @return Готовая часть модели ModelPart.
     */
    private ModelPart createCap(float x, float z, float width, float depth, int texU, int texV, int texW, int texH) {
        MeshDefinition mesh = new MeshDefinition();
        mesh.getRoot().addOrReplaceChild("cap",
                CubeListBuilder.create()
                        .texOffs(texU, texV)
                        .addBox(x, 0.0F, z, width, 0.001F, depth),
                PartPose.ZERO);
        return LayerDefinition.create(mesh, texW, texH).bakeRoot().getChild("cap");
    }

    /**
     * Главный метод рендера слоя. Вызывается каждый кадр для каждого отрисовываемого игрока.
     * Зачем нужен: Читает состояние Capability конечностей и рендерит заглушки-обрубки в местах ампутации или частичной потери сегментов рук и ног.
     */
    @Override
    public void render(
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            AbstractClientPlayer player,
            float limbSwing,
            float limbSwingAmount,
            float partialTicks,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {
        player.getCapability(LimbManager.LIMB_DATA_CAP).ifPresent(data -> {
            boolean isSlim = player.getModelName().equals("slim");

            ArmData rightArm = data.getRightArm();
            ArmData leftArm = data.getLeftArm();
            LegData rightLeg = data.getRightLeg();
            LegData leftLeg = data.getLeftLeg();

            // Отрисовка заглушек для рук
            renderArmStumps(poseStack, buffer, packedLight, player, getParentModel().rightArm, rightArm, isSlim, 0, StumpTextureResolver.LimbType.RIGHT_ARM);
            renderArmStumps(poseStack, buffer, packedLight, player, getParentModel().leftArm, leftArm, isSlim, 1, StumpTextureResolver.LimbType.LEFT_ARM);

            // Отрисовка заглушек для ног
            renderLegStumps(poseStack, buffer, packedLight, player, getParentModel().rightLeg, rightLeg, 2, StumpTextureResolver.LimbType.RIGHT_LEG);
            renderLegStumps(poseStack, buffer, packedLight, player, getParentModel().leftLeg, leftLeg, 3, StumpTextureResolver.LimbType.LEFT_LEG);
        });
    }

    /**
     * Логика проверки и рендера спилов для конкретной руки.
     * Зачем нужен: Определяет, какие именно сегменты руки отсутствуют (плечо, предплечье, кисть),
     * и отрисовывает заглушку на соответствующей высоте кости.
     */
    private void renderArmStumps(
            PoseStack poseStack, MultiBufferSource buffer, int packedLight, AbstractClientPlayer player,
            ModelPart parentLimb, ArmData arm, boolean isSlim, int limbId, StumpTextureResolver.LimbType limbType
    ) {
        boolean s = arm.hasShoulderSkin();
        boolean f = arm.hasForearmSkin();
        boolean w = arm.hasWristSkin();

        // Если все сегменты на месте — заглушки не нужны
        if (s && f && w) return;

        // Если плеча нет — рендерим заглушку в самом основании (у сустава с туловищем)
        if (!s) {
            renderCapAtPosition(poseStack, buffer, packedLight, player, parentLimb, cap4x4, "stump_fresh_4x4", 0.0F, 0.0F, limbId, false, limbType, true);
        }

        ModelPart cap = isSlim ? cap3x4 : cap4x4;
        String texName = isSlim ? "stump_fresh_4x3" : "stump_fresh_4x4";
        float centerX = isSlim ? (limbId == 0 ? -0.5F : 0.5F) : -1.0F;

        // Граница между плечом и предплечьем
        if (s != f) {
            renderCapAtPosition(poseStack, buffer, packedLight, player, parentLimb, cap, texName, 4.0F, centerX, limbId, isSlim, limbType, false);
        }

        // Граница между предплечьем и кистью
        if (f != w) {
            renderCapAtPosition(poseStack, buffer, packedLight, player, parentLimb, cap, texName, 8.0F, centerX, limbId, isSlim, limbType, false);
        }
    }

    /**
     * Логика проверки и рендера спилов для конкретной ноги.
     * Зачем нужен: Проверяет наличие бедра, голени и стопы, рассчитывая позиции спилов.
     */
    private void renderLegStumps(
            PoseStack poseStack, MultiBufferSource buffer, int packedLight, AbstractClientPlayer player,
            ModelPart parentLimb, LegData leg, int limbId, StumpTextureResolver.LimbType limbType
    ) {
        boolean t = leg.hasThighSkin();
        boolean c = leg.hasCalfSkin();
        boolean f = leg.hasFootSkin();

        // Если нога целая — выходим
        if (t && c && f) return;

        ModelPart cap = cap4x4;
        String texName = "stump_fresh_4x4";

        // Если нет бедра — спил у самого таза
        if (!t) {
            renderCapAtPosition(poseStack, buffer, packedLight, player, parentLimb, cap, texName, 0.0F, 0.0F, limbId, false, limbType, true);
        }

        // Граница бедро-голень (колено)
        if (t != c) {
            renderCapAtPosition(poseStack, buffer, packedLight, player, parentLimb, cap, texName, 6.0F, 0.0F, limbId, false, limbType, false);
        }

        // Граница голень-стопа (лодыжка)
        if (c != f) {
            renderCapAtPosition(poseStack, buffer, packedLight, player, parentLimb, cap, texName, 10.0F, 0.0F, limbId, false, limbType, false);
        }
    }

    /**
     * Низкоуровневая отрисовка модели заглушки в пространстве с применением матричных трансформаций.
     * Зачем нужен: Перемещает PoseStack в нужную точку модели игрока, поворачивает заглушку на рандомный/фиксированный угол,
     * накладывает текстуру и передает геометрию в буфер рендера.
     */
    private void renderCapAtPosition(
            PoseStack poseStack, MultiBufferSource buffer, int packedLight, AbstractClientPlayer player,
            ModelPart parentLimb, ModelPart capModel, String baseTexName, float yOffset, float centerX,
            int limbId, boolean isSlim, StumpTextureResolver.LimbType limbType, boolean isRoot
    ) {
        poseStack.pushPose();

        // Привязка позиции: к корню (туловищу), если конечности нет вообще, или к родительской кости
        if (isRoot) {
            getParentModel().body.translateAndRotate(poseStack);
            if (limbId == 0 || limbId == 1) {
                float armX = (limbId == 0) ? -4.01F : 4.01F;
                poseStack.translate(armX / 16.0D, 2.0D / 16.0D, 0.0D);
                poseStack.mulPose(Axis.ZP.rotationDegrees(limbId == 0 ? -90.0F : 90.0F));
            } else {
                float legX = (limbId == 2) ? -2.0F : 2.0F;
                poseStack.translate(legX / 16.0D, 12.01D / 16.0D, 0.0D);
            }
        } else {
            parentLimb.translateAndRotate(poseStack);
            poseStack.translate(centerX / 16.0D, yOffset / 16.0D, 0.0D);
        }

        // Случайный поворот заглушки для разнообразия вида среза
        float angle = getRotationAngle(player, limbId, isSlim);
        if (angle != 0.0F) {
            poseStack.mulPose(Axis.YP.rotationDegrees(angle));
        }

        ResourceLocation texture = StumpTextureResolver.getStumpTexture(player, baseTexName, limbType);
        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(texture));
        capModel.render(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        poseStack.popPose();
    }

    /**
     * Генерирует детерминированный угол поворота текстуры заглушки на основе UUID игрока и ID конечности.
     * Зачем нужен: Создает визуальное разнообразие срезов, чтобы они не выглядели у всех одинаково,
     * но при этом поворот остается постоянным для одного и того же игрока (не мерцает каждый кадр).
     *
     * @param player Игрок.
     * @param limbId Числовой индекс конечности (0, 1 — руки; 2, 3 — ноги).
     * @param isSlim Признак тонкой модели.
     * @return Угол поворота в градусах (0, 90, 180, 270).
     */
    private float getRotationAngle(AbstractClientPlayer player, int limbId, boolean isSlim) {
        long uuidHash = player.getUUID().getLeastSignificantBits() ^ player.getUUID().getMostSignificantBits();
        int seed = Math.abs((int) (uuidHash ^ (limbId * 31L)));

        if (isSlim && (limbId == 0 || limbId == 1)) {
            return (seed % 2) * 180.0F; // Для тонких рук только 2 возможных поворота (из-за асимметрии 3х4)
        } else {
            return (seed % 4) * 90.0F;  // Для квадратных заглушек 4x4 доступно 4 поворота
        }
    }
}