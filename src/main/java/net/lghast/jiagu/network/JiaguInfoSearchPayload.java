package net.lghast.jiagu.network;

import net.lghast.jiagu.common.content.item.CharacterItem;
import net.lghast.jiagu.common.system.command.JiaguInfoCommand;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public record JiaguInfoSearchPayload() implements CustomPacketPayload {

    public static final Type<JiaguInfoSearchPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath("jiagureappear", "jiagu_info_search"));

    public static final StreamCodec<FriendlyByteBuf, JiaguInfoSearchPayload> STREAM_CODEC =
            StreamCodec.unit(new JiaguInfoSearchPayload());

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(JiaguInfoSearchPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                String inscription = CharacterItem.getInscription(player.getMainHandItem());
                if (!inscription.isEmpty()) {
                    JiaguInfoCommand.executeInfo(player.createCommandSourceStack(), inscription);
                }
            }
        });
    }
}
