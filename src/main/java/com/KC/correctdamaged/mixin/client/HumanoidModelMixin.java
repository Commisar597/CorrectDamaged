package com.KC.correctdamaged.mixin.client;

import com.KC.correctdamaged.capability.visual.ArmData;
import com.KC.correctdamaged.capability.visual.LegData;
import com.KC.correctdamaged.capability.visual.BodyData;
import com.KC.correctdamaged.capability.LimbManager;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public abstract class HumanoidModelMixin<T extends LivingEntity> {

    @Shadow public ModelPart head;
    @Shadow public ModelPart hat;
    @Shadow public ModelPart body;
    @Shadow public ModelPart rightArm;
    @Shadow public ModelPart leftArm;
    @Shadow public ModelPart rightLeg;
    @Shadow public ModelPart leftLeg;

    /**
     * Внедряет логику в конец метода setupAnim класса HumanoidModel.
     * Зачем нужен: Корректирует видимость стандартных частей модели игрока (рук, ног, головы, одежды)
     * в зависимости от того, насколько повреждены или ампутированы его конечности.
     *
     * @param entity Сущность, модель которой рендерится.
     * @param ci Контекст вызова Mixin (CallbackInfo).
     */
    @Inject(method = "setupAnim", at = @At("TAIL"))
    private void correctDamaged$applyLimbStates(
            T entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch,
            CallbackInfo ci
    ) {
        // Проверяем, является ли сущность игроком (мод обрабатывает только игроков)
        if (!(entity instanceof Player player)) {
            return;
        }

        // Получаем Capability конечностей и настраиваем видимость стандартной текстуры/кости skin
        LimbManager.get(player).ifPresent(data -> {
            ArmData rightArmData = data.getRightArm();
            ArmData leftArmData  = data.getLeftArm();
            LegData rightLegData = data.getRightLeg();
            LegData leftLegData  = data.getLeftLeg();

            // Проверка: целостность кожи всех подчастей конечностей
            boolean rightArmFull = rightArmData.hasShoulderSkin() && rightArmData.hasForearmSkin() && rightArmData.hasWristSkin();
            boolean leftArmFull  = leftArmData.hasShoulderSkin()  && leftArmData.hasForearmSkin()  && leftArmData.hasWristSkin();
            boolean rightLegFull = rightLegData.hasThighSkin()    && rightLegData.hasCalfSkin()    && rightLegData.hasFootSkin();
            boolean leftLegFull  = leftLegData.hasThighSkin()     && leftLegData.hasCalfSkin()     && leftLegData.hasFootSkin();

            // Если конечность повреждена/отсутствует — скрываем её стандартную часть модели
            rightArm.visible = rightArmFull;
            rightArm.skipDraw = !rightArmFull;

            leftArm.visible = leftArmFull;
            leftArm.skipDraw = !leftArmFull;

            rightLeg.visible = rightLegFull;
            rightLeg.skipDraw = !rightLegFull;

            leftLeg.visible = leftLegFull;
            leftLeg.skipDraw = !leftLegFull;

            // Если модель — конкретно модель игрока ( PlayerModel ), скрываем еще и внешние слои одежды (рукава, штанины, куртку)
            if ((Object) this instanceof PlayerModel<?> playerModel) {
                playerModel.rightSleeve.visible = rightArmFull;
                playerModel.leftSleeve.visible  = leftArmFull;
                playerModel.rightPants.visible  = rightLegFull;
                playerModel.leftPants.visible   = leftLegFull;

                byte headMask = data.getHead().getSkinMask();
                boolean isFullHead = (headMask & 0xFF) == 0xFF;
                playerModel.head.visible = isFullHead;
                playerModel.hat.visible = isFullHead;

                BodyData bodyData = data.getBody();
                boolean isFullBody = bodyData.isBodyIntact();

                playerModel.body.visible = isFullBody;
                playerModel.body.skipDraw = !isFullBody;
                playerModel.jacket.visible = isFullBody;
                playerModel.jacket.skipDraw = !isFullBody;
            }
        });
    }
}