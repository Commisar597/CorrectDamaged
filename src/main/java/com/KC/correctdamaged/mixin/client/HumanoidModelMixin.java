package com.KC.correctdamaged.mixin.client;

import com.KC.correctdamaged.capability.visual.ArmData;
import com.KC.correctdamaged.capability.visual.LegData;
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
            // Получаем объекты конечностей
            ArmData rightArmData = data.getRightArm();
            ArmData leftArmData = data.getLeftArm();
            LegData rightLegData = data.getRightLeg();
            LegData leftLegData = data.getLeftLeg();

            int rightArmState = correctDamaged$getCutState(new int[]{ rightArmData.getShoulderSkin(), rightArmData.getForearmSkin(), rightArmData.getWristSkin() });
            int leftArmState  = correctDamaged$getCutState(new int[]{ leftArmData.getShoulderSkin(), leftArmData.getForearmSkin(), leftArmData.getWristSkin() });

            int rightLegState = correctDamaged$getCutState(new int[]{ rightLegData.getThighSkin(), rightLegData.getCalfSkin(), rightLegData.getFootSkin() });
            int leftLegState  = correctDamaged$getCutState(new int[]{ leftLegData.getThighSkin(), leftLegData.getCalfSkin(), leftLegData.getFootSkin() });

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

                byte headMask = data.getHead().getSkinMask();

                boolean isFullHead = (headMask & 0xFF) == 0xFF;
                playerModel.head.visible = isFullHead;
                playerModel.hat.visible = isFullHead;

                correctDamaged$applyBodyState(playerModel.body, playerModel.jacket, bodyState);
            }
        });
    }

    private static int correctDamaged$getCutState(int[] skinSegments) {
        if (skinSegments[0] == 1) {
            if (skinSegments[1] == 1) {
                return skinSegments[2] == 1 ? 3 : 2;
            }
            return 1;
        }
        return 0;
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