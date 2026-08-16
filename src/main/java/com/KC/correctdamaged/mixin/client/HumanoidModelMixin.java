package com.KC.correctdamaged.mixin.client;

import com.KC.correctdamaged.capability.HeadData;
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

                // Получаем маску кожи головы
                byte headMask = data.getHead().getSkinMask();

                // Если маска полная (255), возвращаем ванильную голову и шляпу
                boolean isFullHead = (headMask & 0xFF) == 0xFF;
                playerModel.head.visible = isFullHead;
                playerModel.hat.visible = isFullHead;

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

    private static void correctDamaged$applyBodyState(ModelPart body, ModelPart jacket, int state) {
        if (state == 9) {
            body.visible = true;
            body.skipDraw = false;
            jacket.visible = true;
            jacket.skipDraw = false;
            return;
        }

        if (state == 0) {
            body.visible = false;
            jacket.visible = false;
            return;
        }

        body.visible = true;
        body.skipDraw = true;
        jacket.visible = true;
        jacket.skipDraw = true;
    }
}