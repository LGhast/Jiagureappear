package net.lghast.jiagu.network;

import net.lghast.jiagu.common.system.menu.CharacterCrafterMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public record OpenCharacterCrafterPayload() implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<OpenCharacterCrafterPayload> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("jiagureappear", "open_character_crafter"));

    public static final StreamCodec<FriendlyByteBuf, OpenCharacterCrafterPayload> STREAM_CODEC =
            StreamCodec.unit(new OpenCharacterCrafterPayload());

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(OpenCharacterCrafterPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                if (player.containerMenu instanceof CharacterCrafterMenu) return;
                player.openMenu(new SimpleMenuProvider(
                        (windowId, playerInventory, playerEntity) ->
                                new CharacterCrafterMenu(windowId, playerInventory),
                        Component.translatable("gui.jiagureappear.character_crafter.title_assemble")
                ));
            }
        });
    }
}
