package net.lghast.jiagu.register;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.lghast.jiagu.JiaguReappear;
import net.lghast.jiagu.data_component.Prescription;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModDataComponents {
    public static final DeferredRegister.DataComponents REGISTRAR =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, JiaguReappear.MOD_ID);

    public static final Codec<Prescription> PRESCRIPTION_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    MobEffect.CODEC.optionalFieldOf("effect").forGetter(Prescription::effect)
            ).apply(instance, Prescription::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, Prescription> PRESCRIPTION_STREAM_CODEC =
            StreamCodec.composite(
                    MobEffect.STREAM_CODEC.apply(ByteBufCodecs::optional),
                    Prescription::effect,
                    Prescription::new
            );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Prescription>> PRESCRIPTION =
            REGISTRAR.registerComponentType("prescription", builder ->
                    builder.persistent(PRESCRIPTION_CODEC)
                            .networkSynchronized(PRESCRIPTION_STREAM_CODEC)
            );
}

