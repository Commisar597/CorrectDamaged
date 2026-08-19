package com.KC.correctdamaged.client.render;

import com.KC.correctdamaged.CorrectDamaged;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;

/**
 * 3D-модель мышечного слоя игрока (руки и ноги).
 * Зачем нужен: Определяет геометрию кубов (кубоидов) мышц для бедер, голеней, стоп, плеч, предплечий и кистей,
 * подгоняя их под анатомическое строение и размеры игрока (включая поддержку Slim-модели Alex).
 */
public class PlayerMusclesModel extends PlayerModel<AbstractClientPlayer> {
    /** Путь к файла текстуры мышечной ткани в ресурсах мода. */
    public static final ResourceLocation MUSCLE = new ResourceLocation(CorrectDamaged.MODID, "textures/entity/muscles_texture.png");

    public final ModelPart rightFootMuscle;
    public final ModelPart rightCalfMuscle;
    public final ModelPart rightThighMuscle;

    public final ModelPart leftFootMuscle;
    public final ModelPart leftCalfMuscle;
    public final ModelPart leftThighMuscle;

    public final ModelPart rightArmWristMuscle;
    public final ModelPart rightArmForearmMuscle;
    public final ModelPart rightArmShoulderMuscle;

    public final ModelPart leftArmWristMuscle;
    public final ModelPart leftArmForearmMuscle;
    public final ModelPart leftArmShoulderMuscle;

    /**
     * Конструктор модели мышц.
     * Зачем нужен: Связывает локальные переменные с иерархией ModelPart, собранной движком Minecraft из LayerDefinition.
     *
     * @param root Корневой элемент запеченной модели.
     */
    public PlayerMusclesModel(ModelPart root) {
        super(root, false);

        ModelPart rightLeg = root.getChild("right_leg");
        ModelPart leftLeg = root.getChild("left_leg");
        ModelPart rightArm = root.getChild("right_arm");
        ModelPart leftArm = root.getChild("left_arm");

        this.rightThighMuscle = rightLeg.getChild("rightThighMuscle");
        this.rightCalfMuscle = rightLeg.getChild("rightCalfMuscle");
        this.rightFootMuscle = rightLeg.getChild("rightFootMuscle");

        this.leftThighMuscle = leftLeg.getChild("leftThighMuscle");
        this.leftCalfMuscle = leftLeg.getChild("leftCalfMuscle");
        this.leftFootMuscle = leftLeg.getChild("leftFootMuscle");

        this.rightArmShoulderMuscle = rightArm.getChild("rightArmShoulderMuscle");
        this.rightArmForearmMuscle = rightArm.getChild("rightArmForearmMuscle");
        this.rightArmWristMuscle = rightArm.getChild("rightArmWristMuscle");

        this.leftArmShoulderMuscle = leftArm.getChild("leftArmShoulderMuscle");
        this.leftArmForearmMuscle = leftArm.getChild("leftArmForearmMuscle");
        this.leftArmWristMuscle = leftArm.getChild("leftArmWristMuscle");
    }

    /**
     * Создает определение сетки (MeshDefinition) и UV-развертки для мышц.
     * Зачем нужен: Формирует размеры кубоидов мышц (по умолчанию чуть меньше стандартных костей/кожи, 3x3 по ширине)
     * и привязывает их к точкам трансформации суставов.
     *
     * @param slim Признак тонкой модели Alex (3px руки).
     * @return Скомпонованное определение слоя LayerDefinition с UV-сеткой 64x64.
     */
    public static LayerDefinition createBodyLayer(boolean slim) {
        MeshDefinition meshdefinition = PlayerModel.createMesh(CubeDeformation.NONE, slim);
        PartDefinition root = meshdefinition.getRoot();

        PartDefinition rightLeg = root.getChild("right_leg");
        PartDefinition leftLeg = root.getChild("left_leg");
        PartDefinition rightArm = root.getChild("right_arm");
        PartDefinition leftArm = root.getChild("left_arm");

        // Мышцы правой ноги (Бедро, Голень, Стопа)
        rightLeg.addOrReplaceChild("rightThighMuscle",
                CubeListBuilder.create().texOffs(0, 7).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 6.0F, 3.0F), PartPose.ZERO);
        rightLeg.addOrReplaceChild("rightCalfMuscle",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 6.0F, -1.5F, 3.0F, 4.0F, 3.0F), PartPose.ZERO);
        rightLeg.addOrReplaceChild("rightFootMuscle",
                CubeListBuilder.create().texOffs(0, 16).addBox(-1.5F, 10.0F, -1.5F, 3.0F, 2.0F, 3.0F), PartPose.ZERO);

        // Мышцы левой ноги
        leftLeg.addOrReplaceChild("leftThighMuscle",
                CubeListBuilder.create().texOffs(0, 7).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 6.0F, 3.0F), PartPose.ZERO);
        leftLeg.addOrReplaceChild("leftCalfMuscle",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 6.0F, -1.5F, 3.0F, 4.0F, 3.0F), PartPose.ZERO);
        leftLeg.addOrReplaceChild("leftFootMuscle",
                CubeListBuilder.create().texOffs(0, 16).addBox(-1.5F, 10.0F, -1.5F, 3.0F, 2.0F, 3.0F), PartPose.ZERO);

        // Мышцы правой руки (Плечо, Предплечье, Кисть)
        rightArm.addOrReplaceChild("rightArmShoulderMuscle",
                CubeListBuilder.create().texOffs(0, 7).addBox(-1.5F, -2.0F, -1.5F, 3.0F, 6.0F, 3.0F), PartPose.offset(-0.3F, 0.0F, 0.0F));
        rightArm.addOrReplaceChild("rightArmForearmMuscle",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 4.0F, -1.5F, 3.0F, 4.0F, 3.0F), PartPose.offset(-0.3F, 0.0F, 0.0F));
        rightArm.addOrReplaceChild("rightArmWristMuscle",
                CubeListBuilder.create().texOffs(0, 16).addBox(-1.5F, 8.0F, -1.5F, 3.0F, 2.0F, 3.0F), PartPose.offset(-0.3F, 0.0F, 0.0F));

        // Мышцы левой руки (с отзеркаливанием текстур)
        leftArm.addOrReplaceChild("leftArmShoulderMuscle",
                CubeListBuilder.create().texOffs(0, 7).mirror().addBox(-1.5F, -2.0F, -1.5F, 3.0F, 6.0F, 3.0F), PartPose.offset(0.3F, 0.0F, 0.0F));
        leftArm.addOrReplaceChild("leftArmForearmMuscle",
                CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.5F, 4.0F, -1.5F, 3.0F, 4.0F, 3.0F), PartPose.offset(0.3F, 0.0F, 0.0F));
        leftArm.addOrReplaceChild("leftArmWristMuscle",
                CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-1.5F, 8.0F, -1.5F, 3.0F, 2.0F, 3.0F), PartPose.offset(0.3F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    /**
     * Настраивает анимацию модели.
     * Зачем нужен: Переключает видимость частей перед обновлением углов поворота из базовой PlayerModel.
     */
    @Override
    public void setupAnim(AbstractClientPlayer entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        setAllPartsVisible(true);
    }

    /**
     * Управляет флагом видимости (visible) всех мышечных сегментов сразу.
     *
     * @param visible true — сделать все сегменты видимыми, false — скрыть.
     */
    public void setAllPartsVisible(boolean visible) {
        this.rightFootMuscle.visible = visible;
        this.rightCalfMuscle.visible = visible;
        this.rightThighMuscle.visible = visible;
        this.leftFootMuscle.visible = visible;
        this.leftCalfMuscle.visible = visible;
        this.leftThighMuscle.visible = visible;

        this.rightArmWristMuscle.visible = visible;
        this.rightArmForearmMuscle.visible = visible;
        this.rightArmShoulderMuscle.visible = visible;
        this.leftArmWristMuscle.visible = visible;
        this.leftArmForearmMuscle.visible = visible;
        this.leftArmShoulderMuscle.visible = visible;
    }
}