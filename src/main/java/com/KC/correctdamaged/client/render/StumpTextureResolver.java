package com.KC.correctdamaged.client.render;

import com.KC.correctdamaged.CorrectDamaged;
import com.KC.correctdamaged.capability.visual.LimbData;
import com.KC.correctdamaged.capability.LimbManager;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;

public final class StumpTextureResolver {

    /**
     * Приватный конструктор.
     * Зачем нужен: Предотвращает создание экземпляров класса, так как класс содержит только статические утилиты.
     */
    private static StumpTextureResolver() {}

    /**
     * Перечисление типов конечностей.
     * Зачем нужен: Позволяет безопасно передавать тип проверяемого органа/конечности без использования строк.
     */
    public enum LimbType {
        HEAD,
        BODY,
        RIGHT_ARM,
        LEFT_ARM,
        RIGHT_LEG,
        LEFT_LEG
    }

    /**
     * Вспомогательный метод для формирования ResourceLocation к файлу текстуры в ресурсах мода.
     *
     * @param path Относительный путь от папки textures/entity/.
     * @return Объект ResourceLocation с полной ссылкой на текстуру.
     */
    private static ResourceLocation tex(String path) {
        return new ResourceLocation(CorrectDamaged.MODID, "textures/entity/" + path);
    }

    /**
     * Возвращает правильный ResourceLocation текстуры «спила»/«обрубка» (stump) для конечности.
     * Зачем нужен: Динамически выбирает между стандартной текстурой обрубка и текстурой с обугленной костью,
     * основываясь на состоянии Capability игрока.
     *
     * @param player Клиентский игрок.
     * @param baseName Базовое имя текстуры (например "stump_fresh_4x4").
     * @param type Тип конечности.
     * @return Ссылка на итоговый файл текстуры .png.
     */
    public static ResourceLocation getStumpTexture(AbstractClientPlayer player, String baseName, LimbType type) {
        boolean isBurnt = LimbManager.get(player)
                .map(data -> checkIsBurnt(data, type))
                .orElse(false);

        if (isBurnt) {
            return tex(baseName + "_burnt_bone.png");
        }

        return tex(baseName + ".png");
    }

    /**
     * Проверяет, является ли кость конкретной конечности обугленной.
     * Зачем нужен: Извлекает флаг isBurntBone из соответствующего объекта данных (ArmData/LegData).
     *
     * @param data Объект данных Capability конечностей.
     * @param type Перечисление типа конечности.
     * @return true, если кость обуглена, иначе false.
     */
    private static boolean checkIsBurnt(LimbData data, LimbType type) {
        return switch (type) {
            case HEAD -> false;
            case BODY -> false;
            case RIGHT_ARM -> data.getRightArm().isBurntBone();
            case LEFT_ARM  -> data.getLeftArm().isBurntBone();
            case RIGHT_LEG -> data.getRightLeg().isBurntBone();
            case LEFT_LEG  -> data.getLeftLeg().isBurntBone();
        };
    }
}