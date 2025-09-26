package net.lghast.jiagu.common.system.datacomponent;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;

import java.util.Objects;
import java.util.Optional;

public record Prescription(Optional<Holder<MobEffect>> effect) {
    public static final Prescription EMPTY = new Prescription(Optional.empty());

    public Prescription(Holder<MobEffect> effect) {
        this(Optional.of(effect));
    }

    public Prescription() {
        this(Optional.empty());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Prescription other)) return false;

        if (this.effect.isPresent() && other.effect.isPresent()) {
            return Objects.equals(this.effect.get(), other.effect.get());
        }
        return this.effect.isEmpty() && other.effect.isEmpty();
    }

    @Override
    public int hashCode() {
        return effect.map(Holder::hashCode).orElse(0);
    }
}
