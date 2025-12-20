package net.lghast.jiagu.common.system.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

public class DisassembleTrigger extends SimpleCriterionTrigger<DisassembleTrigger.Instance> {

    public static class Instance implements SimpleInstance {
        private final Optional<ContextAwarePredicate> playerPredicate;
        private final Optional<String> requiredInscriptions;

        public Instance(Optional<ContextAwarePredicate> playerPredicate, Optional<String> requiredInscriptions) {
            this.playerPredicate = playerPredicate;
            this.requiredInscriptions = requiredInscriptions;
        }

        @Override
        public Optional<ContextAwarePredicate> player() {
            return playerPredicate;
        }

        boolean test(String inscription) {
            return requiredInscriptions.isEmpty() || requiredInscriptions.get().equals(inscription);
        }

        public static final Codec<Instance> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(inst -> inst.playerPredicate),
                        Codec.STRING.optionalFieldOf("inscription").forGetter(inst -> inst.requiredInscriptions)
                ).apply(instance, Instance::new)
        );
    }

    @Override
    public @NotNull Codec<Instance> codec() {
        return Instance.CODEC;
    }

    public void trigger(ServerPlayer player) {
        this.trigger(player, instance -> true);
    }

    public void trigger(ServerPlayer player, String inscription) {
        this.trigger(player, instance -> instance.test(inscription));
    }

    public static Criterion<Instance> disassemble() {
        return new Criterion<>(
                TRIGGER.get(),
                new Instance(Optional.empty(), Optional.empty())
        );
    }

    public static Criterion<Instance> disassemble(String message) {
        return new Criterion<>(
                TRIGGER.get(),
                new Instance(Optional.empty(), Optional.ofNullable(message))
        );
    }

    public static final DeferredRegister<CriterionTrigger<?>> TRIGGER_TYPES =
            DeferredRegister.create(Registries.TRIGGER_TYPE, "jiagureappear");

    public static final DeferredHolder<CriterionTrigger<?>, DisassembleTrigger> TRIGGER =
            TRIGGER_TYPES.register("disassemble", DisassembleTrigger::new);
}
