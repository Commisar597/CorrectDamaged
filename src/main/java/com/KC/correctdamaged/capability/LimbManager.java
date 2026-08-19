package com.KC.correctdamaged.capability;

import com.KC.correctdamaged.CorrectDamaged;
import com.KC.correctdamaged.capability.visual.LimbCapabilityProvider;
import com.KC.correctdamaged.capability.visual.LimbData;
import com.KC.correctdamaged.network.PacketHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Менеджер управления Капабилити (Capability) повреждений и визуального состояния конечностей игрока.
 * Обрабатывает Forge-события прикрепления, синхронизации и переноса данных при смерти/возрождении.
 */
@Mod.EventBusSubscriber(modid = CorrectDamaged.MODID)
public class LimbManager {

    /** Объект Capability для доступа к данным конечностей (LimbData). */
    public static final Capability<LimbData> LIMB_DATA_CAP = CapabilityManager.get(new CapabilityToken<>() {});
    /** Уникальный идентификатор регистратора Capability. */
    public static final ResourceLocation CAP_ID = new ResourceLocation(CorrectDamaged.MODID, "limb_data");

    /**
     * Прикрепляет Капабилити к сущностям игроков при их спавне/создании.
     */
    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player player) {
            if (!get(player).isPresent()) {
                event.addCapability(CAP_ID, new LimbCapabilityProvider());
            }
        }
    }

    /**
     * Переносит данные конечностей при клонировании игрока (например, после смерти или при переходе из Энда/Незера).
     */
    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        Player oldPlayer = event.getOriginal();
        Player newPlayer = event.getEntity();

        // Временно восстанавливаем капабилити старого игрока для копирования
        oldPlayer.reviveCaps();
        oldPlayer.getCapability(LIMB_DATA_CAP).ifPresent(oldCap -> {
            newPlayer.getCapability(LIMB_DATA_CAP).ifPresent(newCap -> {
                newCap.copyFrom(oldCap);
            });
        });
        oldPlayer.invalidateCaps();

        if (newPlayer instanceof ServerPlayer serverPlayer) {
            PacketHandler.syncToPlayer(serverPlayer);
        }
    }

    /**
     * Синхронизирует данные конечностей с клиентом при входе игрока на сервер.
     */
    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            PacketHandler.syncToPlayer(serverPlayer);
        }
    }

    /**
     * Синхронизирует данные игрока с другими наблюдающими за ним игроками (трекинг).
     */
    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking event) {
        if (event.getTarget() instanceof ServerPlayer targetPlayer && event.getEntity() instanceof ServerPlayer observer) {
            PacketHandler.sendTo(observer, targetPlayer);
        }
    }

    /** Вспомогательный метод для отправки пакета синхронизации на клиенты, если код выполняется на сервере. */
    private static void syncIfServer(Player player) {
        if (!player.level().isClientSide && player instanceof ServerPlayer serverPlayer) {
            PacketHandler.syncToTrackingAndSelf(serverPlayer);
        }
    }

    // --- Правая рука ---

    public static boolean setRightArmShoulderSkin(Player player, int state) {
        return player.getCapability(LIMB_DATA_CAP).map(cap -> {
            cap.getRightArm().setShoulderSkin(state);
            syncIfServer(player);
            return true;
        }).orElse(false);
    }

    public static boolean setRightArmForearmSkin(Player player, int state) {
        return player.getCapability(LIMB_DATA_CAP).map(cap -> {
            cap.getRightArm().setForearmSkin(state);
            syncIfServer(player);
            return true;
        }).orElse(false);
    }

    public static boolean setRightArmWristSkin(Player player, int state) {
        return player.getCapability(LIMB_DATA_CAP).map(cap -> {
            cap.getRightArm().setWristSkin(state);
            syncIfServer(player);
            return true;
        }).orElse(false);
    }

    // --- Левая рука ---

    public static boolean setLeftArmShoulderSkin(Player player, int state) {
        return player.getCapability(LIMB_DATA_CAP).map(cap -> {
            cap.getLeftArm().setShoulderSkin(state);
            syncIfServer(player);
            return true;
        }).orElse(false);
    }

    public static boolean setLeftArmForearmSkin(Player player, int state) {
        return player.getCapability(LIMB_DATA_CAP).map(cap -> {
            cap.getLeftArm().setForearmSkin(state);
            syncIfServer(player);
            return true;
        }).orElse(false);
    }

    public static boolean setLeftArmWristSkin(Player player, int state) {
        return player.getCapability(LIMB_DATA_CAP).map(cap -> {
            cap.getLeftArm().setWristSkin(state);
            syncIfServer(player);
            return true;
        }).orElse(false);
    }

    // --- Правая нога ---

    public static boolean setRightLegThighSkin(Player player, int state) {
        return player.getCapability(LIMB_DATA_CAP).map(cap -> {
            cap.getRightLeg().setThighSkin(state);
            syncIfServer(player);
            return true;
        }).orElse(false);
    }

    public static boolean setRightLegCalfSkin(Player player, int state) {
        return player.getCapability(LIMB_DATA_CAP).map(cap -> {
            cap.getRightLeg().setCalfSkin(state);
            syncIfServer(player);
            return true;
        }).orElse(false);
    }

    public static boolean setRightLegFootSkin(Player player, int state) {
        return player.getCapability(LIMB_DATA_CAP).map(cap -> {
            cap.getRightLeg().setFootSkin(state);
            syncIfServer(player);
            return true;
        }).orElse(false);
    }

    // --- Левая нога ---

    public static boolean setLeftLegThighSkin(Player player, int state) {
        return player.getCapability(LIMB_DATA_CAP).map(cap -> {
            cap.getLeftLeg().setThighSkin(state);
            syncIfServer(player);
            return true;
        }).orElse(false);
    }

    public static boolean setLeftLegCalfSkin(Player player, int state) {
        return player.getCapability(LIMB_DATA_CAP).map(cap -> {
            cap.getLeftLeg().setCalfSkin(state);
            syncIfServer(player);
            return true;
        }).orElse(false);
    }

    public static boolean setLeftLegFootSkin(Player player, int state) {
        return player.getCapability(LIMB_DATA_CAP).map(cap -> {
            cap.getLeftLeg().setFootSkin(state);
            syncIfServer(player);
            return true;
        }).orElse(false);
    }

    // --- Голова и туловище ---

    public static boolean setBodySkinMask(Player player, int state) {
        return player.getCapability(LIMB_DATA_CAP).map(cap -> {
            cap.getBody().setSkinMask(state);
            syncIfServer(player);
            return true;
        }).orElse(false);
    }

    public static boolean setHeadSkinMask(Player player, byte mask) {
        return player.getCapability(LIMB_DATA_CAP).map(cap -> {
            cap.getHead().setSkinMask(mask);
            syncIfServer(player);
            return true;
        }).orElse(false);
    }

    public static boolean setHeadMuscleMask(Player player, byte mask) {
        return player.getCapability(LIMB_DATA_CAP).map(cap -> {
            cap.getHead().setMuscleMask(mask);
            syncIfServer(player);
            return true;
        }).orElse(false);
    }

    public static boolean setHeadSkullMask(Player player, byte mask) {
        return player.getCapability(LIMB_DATA_CAP).map(cap -> {
            cap.getHead().setSkullMask(mask);
            syncIfServer(player);
            return true;
        }).orElse(false);
    }

    public static boolean setHeadBurntSkull(Player player, boolean burnt) {
        return player.getCapability(LIMB_DATA_CAP).map(cap -> {
            cap.getHead().setBurntSkull(burnt);
            syncIfServer(player);
            return true;
        }).orElse(false);
    }

    // --- Кости конечностей ---

    public static boolean setBoneRightArm(Player player, int state) {
        return player.getCapability(LIMB_DATA_CAP).map(cap -> {
            cap.getRightArm().setBoneState(state);
            syncIfServer(player);
            return true;
        }).orElse(false);
    }

    public static boolean setBoneLeftArm(Player player, int state) {
        return player.getCapability(LIMB_DATA_CAP).map(cap -> {
            cap.getLeftArm().setBoneState(state);
            syncIfServer(player);
            return true;
        }).orElse(false);
    }

    public static boolean setBoneRightLeg(Player player, int state) {
        return player.getCapability(LIMB_DATA_CAP).map(cap -> {
            cap.getRightLeg().setBoneState(state);
            syncIfServer(player);
            return true;
        }).orElse(false);
    }

    public static boolean setBoneLeftLeg(Player player, int state) {
        return player.getCapability(LIMB_DATA_CAP).map(cap -> {
            cap.getLeftLeg().setBoneState(state);
            syncIfServer(player);
            return true;
        }).orElse(false);
    }

    public static boolean setShowSkeleton(Player player, int state) {
        return player.getCapability(LIMB_DATA_CAP).map(cap -> {
            cap.getBody().setShowSkeleton(state);
            syncIfServer(player);
            return true;
        }).orElse(false);
    }

    // --- Обугленные кости ---

    public static boolean setBurntBoneRightArm(Player player, boolean burnt) {
        return player.getCapability(LIMB_DATA_CAP).map(cap -> {
            cap.getRightArm().setBurntBone(burnt);
            syncIfServer(player);
            return true;
        }).orElse(false);
    }

    public static boolean setBurntBoneLeftArm(Player player, boolean burnt) {
        return player.getCapability(LIMB_DATA_CAP).map(cap -> {
            cap.getLeftArm().setBurntBone(burnt);
            syncIfServer(player);
            return true;
        }).orElse(false);
    }

    public static boolean setBurntBoneRightLeg(Player player, boolean burnt) {
        return player.getCapability(LIMB_DATA_CAP).map(cap -> {
            cap.getRightLeg().setBurntBone(burnt);
            syncIfServer(player);
            return true;
        }).orElse(false);
    }

    public static boolean setBurntBoneLeftLeg(Player player, boolean burnt) {
        return player.getCapability(LIMB_DATA_CAP).map(cap -> {
            cap.getLeftLeg().setBurntBone(burnt);
            syncIfServer(player);
            return true;
        }).orElse(false);
    }

    // --- Мышцы конечностей и тела ---

    public static boolean setMuscleBody(Player player, int state) {
        return player.getCapability(LIMB_DATA_CAP).map(cap -> {
            cap.getBody().setMuscleBody(state);
            syncIfServer(player);
            return true;
        }).orElse(false);
    }

    public static boolean setMuscleRightArm(Player player, int state) {
        return player.getCapability(LIMB_DATA_CAP).map(cap -> {
            cap.getRightArm().setMuscleState(state);
            syncIfServer(player);
            return true;
        }).orElse(false);
    }

    public static boolean setMuscleLeftArm(Player player, int state) {
        return player.getCapability(LIMB_DATA_CAP).map(cap -> {
            cap.getLeftArm().setMuscleState(state);
            syncIfServer(player);
            return true;
        }).orElse(false);
    }

    public static boolean setMuscleRightLeg(Player player, int state) {
        return player.getCapability(LIMB_DATA_CAP).map(cap -> {
            cap.getRightLeg().setMuscleState(state);
            syncIfServer(player);
            return true;
        }).orElse(false);
    }

    public static boolean setMuscleLeftLeg(Player player, int state) {
        return player.getCapability(LIMB_DATA_CAP).map(cap -> {
            cap.getLeftLeg().setMuscleState(state);
            syncIfServer(player);
            return true;
        }).orElse(false);
    }

    /**
     * Получить LazyOptional с {@link LimbData} для указанного игрока.
     */
    public static LazyOptional<LimbData> get(Player player) {
        return player.getCapability(LIMB_DATA_CAP);
    }

    public static boolean setBurntSkeleton(Player player, boolean burnt) {
        return player.getCapability(LIMB_DATA_CAP).map(cap -> {
            cap.getBody().setBurntSkeleton(burnt);
            syncIfServer(player);
            return true;
        }).orElse(false);
    }

    /**
     * Применяет пресет повреждений к воксельной матрице туловища игрока.
     */
    public static boolean applyBodyVoxelPreset(Player player, String preset) {
        return player.getCapability(LIMB_DATA_CAP).map(cap -> {
            cap.getBody().getBodyVoxelMatrix().applyPreset(preset);
            syncIfServer(player);
            return true;
        }).orElse(false);
    }
}