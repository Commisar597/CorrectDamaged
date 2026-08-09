package com.KC.correctdamaged.mixin.client;

import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerModel.class)
public abstract class PlayerModelMixin {

    private static final float STAGE_2_3_HEIGHT = 10.0F;
    private static final float STAGE_1_3_HEIGHT = 6.0F;

    @Inject(method = "createMesh", at = @At("RETURN"))
    private static void correctDamaged$addLimbStages(
            CubeDeformation cubeDeformation,
            boolean slim,
            CallbackInfoReturnable<MeshDefinition> cir
    ) {
        PartDefinition root = cir.getReturnValue().getRoot();
        CubeDeformation layerDeformation = new CubeDeformation(0.25F);

        addArmStages(root.getChild("right_arm"), slim, false, cubeDeformation, 40, 16);
        addArmStages(root.getChild("left_arm"), slim, true, cubeDeformation, 32, 48);
        addLegStages(root.getChild("right_leg"), false, cubeDeformation, 0, 16);
        addLegStages(root.getChild("left_leg"), true, cubeDeformation, 16, 48);

        addArmStages(root.getChild("right_sleeve"), slim, false, layerDeformation, 40, 32);
        addArmStages(root.getChild("left_sleeve"), slim, true, layerDeformation, 48, 48);
        addLegStages(root.getChild("right_pants"), false, layerDeformation, 0, 32);
        addLegStages(root.getChild("left_pants"), true, layerDeformation, 0, 48);
    }

    private static void addArmStages(
            PartDefinition arm,
            boolean slim,
            boolean isLeft,
            CubeDeformation deformation,
            int texU,
            int texV
    ) {
        float width = slim ? 3.0F : 4.0F;
        float depth = 4.0F;
        float x = isLeft ? -1.0F : (slim ? -2.0F : -3.0F);

        arm.addOrReplaceChild(
                "cd_stage_2_3",
                CubeListBuilder.create()
                        .texOffs(texU, texV)
                        .addBox(x, -2.0F, -2.0F, width, STAGE_2_3_HEIGHT, depth, deformation),
                PartPose.ZERO
        );

        arm.addOrReplaceChild(
                "cd_stage_1_3",
                CubeListBuilder.create()
                        .texOffs(texU, texV)
                        .addBox(x, -2.0F, -2.0F, width, STAGE_1_3_HEIGHT, depth, deformation),
                PartPose.ZERO
        );
    }

    private static void addLegStages(
            PartDefinition leg,
            boolean isLeft,
            CubeDeformation deformation,
            int texU,
            int texV
    ) {
        leg.addOrReplaceChild(
                "cd_stage_2_3",
                CubeListBuilder.create()
                        .texOffs(texU, texV)
                        .addBox(-2.0F, 0.0F, -2.0F, 4.0F, STAGE_2_3_HEIGHT, 4.0F, deformation),
                PartPose.ZERO
        );

        leg.addOrReplaceChild(
                "cd_stage_1_3",
                CubeListBuilder.create()
                        .texOffs(texU, texV)
                        .addBox(-2.0F, 0.0F, -2.0F, 4.0F, STAGE_1_3_HEIGHT, 4.0F, deformation),
                PartPose.ZERO
        );
    }
}