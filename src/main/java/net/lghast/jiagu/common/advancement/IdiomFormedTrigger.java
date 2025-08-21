package net.lghast.jiagu.common.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.lghast.jiagu.JiaguReappear;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Optional;

public class IdiomFormedTrigger extends SimpleCriterionTrigger<IdiomFormedTrigger.Instance> {

    public static class Instance implements SimpleCriterionTrigger.SimpleInstance {
        private final Optional<ContextAwarePredicate> playerPredicate;
        private final String idiom;

        public Instance(Optional<ContextAwarePredicate> playerPredicate, String idiom) {
            this.playerPredicate = playerPredicate;
            this.idiom = idiom;
        }

        public boolean matches(String formedIdiom) {
            return this.idiom.equals(formedIdiom);
        }

        @Override
        public Optional<ContextAwarePredicate> player() {
            return playerPredicate;
        }

        public static final Codec<Instance> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(inst -> inst.playerPredicate),
                        Codec.STRING.fieldOf("idiom").forGetter(inst -> inst.idiom)
                ).apply(instance, Instance::new)
        );
    }

    @Override
    public Codec<Instance> codec() {
        return Instance.CODEC;
    }

    public void trigger(ServerPlayer player, String idiom) {
        this.trigger(player, instance -> instance.matches(idiom));
    }

    public static Criterion<Instance> formedIdiom(String idiom) {
        return new Criterion<>(
                TRIGGER.get(),
                new Instance(Optional.empty(), idiom)
        );
    }

    public static final DeferredRegister<CriterionTrigger<?>> TRIGGER_TYPES =
            DeferredRegister.create(Registries.TRIGGER_TYPE, "jiagureappear");

    public static final DeferredHolder<CriterionTrigger<?>, IdiomFormedTrigger> TRIGGER =
            TRIGGER_TYPES.register("idiom_formed", IdiomFormedTrigger::new);
}