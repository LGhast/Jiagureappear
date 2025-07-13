package net.lghast.jiagu.register;

import net.lghast.jiagu.JiaguReappear;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, JiaguReappear.MOD_ID);

    public static final Supplier<SimpleParticleType> JIAGU_PARTICLES =
            PARTICLE_TYPES.register("jiagu_particles", () -> new SimpleParticleType(true));

    public static final Supplier<SimpleParticleType> JIAGU_FLOATING_PARTICLES =
            PARTICLE_TYPES.register("jiagu_floating_particles", () -> new SimpleParticleType(true));

    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }
}
