package net.lghast.jiagu.register.system;

import com.mojang.serialization.Codec;
import net.lghast.jiagu.JiaguReappear;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class ModAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, JiaguReappear.MOD_ID);

    private static final StreamCodec<RegistryFriendlyByteBuf, Set<String>> IDIOM_SET_STREAM_CODEC = StreamCodec.of(
            (buf, set) -> {
                buf.writeVarInt(set.size());
                for (String s : set) {
                    buf.writeUtf(s);
                }
            },
            buf -> {
                int size = buf.readVarInt();
                Set<String> set = new HashSet<>();
                for (int i = 0; i < size; i++) {
                    set.add(buf.readUtf());
                }
                return set;
            }
    );

    public static final Supplier<AttachmentType<Set<String>>> COLLECTED_IDIOMS = ATTACHMENT_TYPES.register("collected_idioms",
            () -> AttachmentType.<Set<String>>builder(() -> new HashSet<>())
                    .serialize(Codec.STRING.listOf().xmap(HashSet::new, ArrayList::new))
                    .copyOnDeath()
                    .sync(IDIOM_SET_STREAM_CODEC)
                    .build());

    public static final Supplier<AttachmentType<Boolean>> WELCOME_SHOWN = ATTACHMENT_TYPES.register("welcome_shown",
            () -> AttachmentType.<Boolean>builder(() -> false)
                    .serialize(Codec.BOOL)
                    .copyOnDeath()
                    .build());

    public static void register(IEventBus eventBus) {
        ATTACHMENT_TYPES.register(eventBus);
    }
}
