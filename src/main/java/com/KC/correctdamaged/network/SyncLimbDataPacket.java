package com.KC.correctdamaged.network;

import com.KC.correctdamaged.capability.visual.LimbCapability;
import com.KC.correctdamaged.capability.visual.LimbData;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class SyncLimbDataPacket {

    private final UUID playerUUID;
    private final CompoundTag nbt;

    /**
     * Конструктор пакета синхронизации данных о повреждениях/конечностях.
     *
     * @param playerUUID Уникальный идентификатор игрока, чьи данные передаются.
     * @param nbt Структура NBT с сохраненным состоянием конечностей.
     */
    public SyncLimbDataPacket(UUID playerUUID, CompoundTag nbt) {
        this.playerUUID = playerUUID;
        this.nbt = nbt;
    }

    /**
     * Фабричный метод создания пакета напрямую из объекта ServerPlayer на сервере.
     * Зачем нужен: Упрощает создание пакета, автоматически сериализуя Capability игрока в NBT.
     *
     * @param player Игрок, данные которого нужно запаковать.
     * @return Готовый объект SyncLimbDataPacket.
     */
    public static SyncLimbDataPacket from(ServerPlayer player) {
        CompoundTag tag = player.getCapability(LimbCapability.INSTANCE)
                .map(LimbData::serializeNBT)
                .orElseGet(CompoundTag::new);
        return new SyncLimbDataPacket(player.getUUID(), tag);
    }

    /**
     * Метод кодирования (записи) пакета в байтовый буфер.
     * Зачем нужен: Превращает Java-объект пакета в набор байт для передачи по сети.
     *
     * @param packet Объект пакета.
     * @param buffer Сетевой байтовый буфер Forge/Minecraft.
     */
    public static void encode(SyncLimbDataPacket packet, FriendlyByteBuf buffer) {
        buffer.writeUUID(packet.playerUUID);
        buffer.writeNbt(packet.nbt);
    }

    /**
     * Метод декодирования (чтения) пакета из байтового буфера.
     * Зачем нужен: Воссоздает Java-объект SyncLimbDataPacket из полученных по сети байт.
     *
     * @param buffer Сетевой байтовый буфер.
     * @return Расшифрованный объект пакета.
     */
    public static SyncLimbDataPacket decode(FriendlyByteBuf buffer) {
        return new SyncLimbDataPacket(buffer.readUUID(), buffer.readNbt());
    }

    /**
     * Точка входа для обработки пакета на стороне получателя.
     * Зачем нужен: Обеспечивает безопасное исполнение логики пакета в главном потоке клиенте.
     *
     * @param packet Декодированный пакет.
     * @param contextSupplier Поставщик сетевого контекста Forge.
     */
    public static void handle(SyncLimbDataPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        // Передает задачу в поток выполнения клиента (безопасно против крашей)
        context.enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> handleClient(packet));
        });
        context.setPacketHandled(true);
    }

    /**
     * Клиентский обработчик пакета.
     * Зачем нужен: Находит нужного игрока на клиенте по UUID и обновляет его Capability данными из NBT.
     *
     * @param packet Пакет с обновленными данными.
     */
    private static void handleClient(SyncLimbDataPacket packet) {
        if (Minecraft.getInstance().level == null) return;

        Player player = Minecraft.getInstance().level.getPlayerByUUID(packet.playerUUID);
        if (player != null) {
            player.getCapability(LimbCapability.INSTANCE).ifPresent(data -> {
                data.deserializeNBT(packet.nbt);
            });
        }
    }
}