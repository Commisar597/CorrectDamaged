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

            correctDamaged$applyState(rightArm, rightArmState);
            correctDamaged$applyState(leftArm, leftArmState);
            correctDamaged$applyState(rightLeg, rightLegState);
            correctDamaged$applyState(leftLeg, leftLegState);

            if ((Object) this instanceof PlayerModel<?> playerModel) {
                correctDamaged$applyState(playerModel.rightSleeve, rightArmState);
                correctDamaged$applyState(playerModel.leftSleeve, leftArmState);
                correctDamaged$applyState(playerModel.rightPants, rightLegState);
                correctDamaged$applyState(playerModel.leftPants, leftLegState);
            }
        });
    }

    private static void correctDamaged$applyState(ModelPart limb, int state) {

        if (state == 3) {
            limb.visible = true;
            limb.skipDraw = false;
            limb.getChild("cd_stage_2_3").visible = false;
            limb.getChild("cd_stage_1_3").visible = false;
            return;
        }

        if (state == 0) {
            limb.visible = false;
            return;
        }

        limb.visible = true;
        limb.skipDraw = true;
        limb.getChild("cd_stage_2_3").visible = (state == 2);
        limb.getChild("cd_stage_1_3").visible = (state == 1);
    }
}