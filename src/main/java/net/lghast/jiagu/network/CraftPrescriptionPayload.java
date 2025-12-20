package net.lghast.jiagu.network;

import net.lghast.jiagu.common.system.advancement.YaowangTrigger;
import net.lghast.jiagu.common.system.menu.YaowangGourdMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record CraftPrescriptionPayload(BlockPos pos) implements CustomPacketPayload {
    public static final Type<CraftPrescriptionPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath("jiagureappear", "craft_prescription"));

    public static final StreamCodec<FriendlyByteBuf, CraftPrescriptionPayload> STREAM_CODEC = StreamCodec.of(
            (buf, payload) -> buf.writeBlockPos(payload.pos),
            buf -> new CraftPrescriptionPayload(buf.readBlockPos())
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(CraftPrescriptionPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                if (player.containerMenu instanceof YaowangGourdMenu menu) {
                    ItemStack result = menu.getBlockEntity().craftPrescription();
                    if (!result.isEmpty() && player.containerMenu.getCarried().isEmpty()) {
                        player.containerMenu.setCarried(result);
                        YaowangTrigger.TRIGGER.get().trigger(player);
                    }
                }
            }
        });
    }
}
