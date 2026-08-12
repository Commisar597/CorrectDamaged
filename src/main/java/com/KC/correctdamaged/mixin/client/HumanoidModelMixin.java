package com.KC.correctdamaged.mixin.client;

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
        if (!(entity instanceof Player player)) {
            return;
        }

        LimbManager.get(player).ifPresent(data -> {
            int rightArmState = data.getRightArm();
            int leftArmState = data.getLeftArm();
            int rightLegState = data.getRightLeg();
            int leftLegState = data.getLeftLeg();
            int headState = data.getHeadState();
            int bodyState = data.getBodyState();

            correctDamaged$applyState(rightArm, rightArmState);
            correctDamaged$applyState(leftArm, leftArmState);
            correctDamaged$applyState(rightLeg, rightLegState);
            correctDamaged$applyState(leftLeg, leftLegState);

            if ((Object) this instanceof PlayerModel<?> playerModel) {
                correctDamaged$applyState(playerModel.rightSleeve, rightArmState);
                correctDamaged$applyState(playerModel.leftSleeve, leftArmState);
                correctDamaged$applyState(playerModel.rightPants, rightLegState);
                correctDamaged$applyState(playerModel.leftPants, leftLegState);

                correctDamaged$applyHeadState(playerModel.head, playerModel.hat, headState);
                correctDamaged$applyBodyState(playerModel.body, playerModel.jacket, bodyState);
            }
        });
    }

    private static void correctDamaged$applyState(ModelPart limb, int state) {
        if (state == 3) {
            limb.visible = true;
            limb.skipDraw = false;
            return;
        }

        if (state == 0) {
            limb.visible = false;
            return;
        }

        limb.visible = true;
        limb.skipDraw = true;
    }

    private static void correctDamaged$applyHeadState(ModelPart head, ModelPart hat, int state) {
        if (state == 5) {
            head.visible = true;
            head.skipDraw = false;
            hat.visible = true;
            hat.skipDraw = false;
            return;
        }

        if (state == 0) {
            head.visible = false;
            hat.visible = false;
            return;
        }

        head.visible = true;
        head.skipDraw = true;
        hat.visible = true;
        hat.skipDraw = true;
    }

    private static void correctDamaged$applyBodyState(ModelPart body, ModelPart jacket, int state) {
        // 9: Цельное тело (стандартный ванильный рендер)
        if (state == 9) {
            body.visible = true;
            body.skipDraw = false;
            jacket.visible = true;
            jacket.skipDraw = false;
            return;
        }

        // 0: Тело отсутствует
        if (state == 0) {
            body.visible = false;
            jacket.visible = false;
            return;
        }

        // 1..8: Вариации повреждения (скрываем стандартное тело, BodyDamageLayer отрендерит кастомное)
        body.visible = true;
        body.skipDraw = true;
        jacket.visible = true;
        jacket.skipDraw = true;
    }
}