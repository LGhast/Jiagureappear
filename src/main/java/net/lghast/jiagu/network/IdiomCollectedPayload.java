package net.lghast.jiagu.network;

import net.lghast.jiagu.client.toast.IdiomToast;
import net.lghast.jiagu.idiom.IdiomInfo;
import net.lghast.jiagu.idiom.IdiomRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record IdiomCollectedPayload(String idiomId, String characters) implements CustomPacketPayload {
    public static final Type<IdiomCollectedPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath("jiagureappear", "idiom_collected"));

    public static final StreamCodec<FriendlyByteBuf, IdiomCollectedPayload> STREAM_CODEC = StreamCodec.of(
            (buf, payload) -> {
                buf.writeUtf(payload.idiomId);
                buf.writeUtf(payload.characters);
            },
            buf -> new IdiomCollectedPayload(buf.readUtf(), buf.readUtf())
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(IdiomCollectedPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> showToast(payload));
    }

    @OnlyIn(Dist.CLIENT)
    private static void showToast(IdiomCollectedPayload payload) {
        IdiomInfo info = IdiomRegistry.getById(payload.idiomId());
        ItemStack icon = info != null ? IdiomRegistry.getIconStack(info.icon()) : ItemStack.EMPTY;
        Minecraft.getInstance().getToasts().addToast(new IdiomToast(
                Component.literal(payload.characters()),
                Component.translatable("idiom.jiagureappear.collected"),
                icon
        ));
    }
}
