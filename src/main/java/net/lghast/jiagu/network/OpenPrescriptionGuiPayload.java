package net.lghast.jiagu.network;

import net.lghast.jiagu.common.content.item.PrescriptionUserItem;
import net.lghast.jiagu.common.system.menu.PrescriptionMenu;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record OpenPrescriptionGuiPayload() implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<OpenPrescriptionGuiPayload> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("jiagureappear", "open_prescription_gui"));

    public static final StreamCodec<FriendlyByteBuf, OpenPrescriptionGuiPayload> STREAM_CODEC =
            StreamCodec.unit(new OpenPrescriptionGuiPayload());

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(OpenPrescriptionGuiPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                ItemStack mainHandItem = player.getMainHandItem();
                if (mainHandItem.getItem() instanceof PrescriptionUserItem) {
                    player.openMenu(new SimpleMenuProvider(
                            (windowId, playerInventory, playerEntity) ->
                                    new PrescriptionMenu(windowId, playerInventory, mainHandItem),
                            Component.translatable("gui.jiagureappear.prescription")
                    ));
                }
            }
        });
    }
}
