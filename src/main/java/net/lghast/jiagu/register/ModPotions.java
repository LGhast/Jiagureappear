package net.lghast.jiagu.register;

import net.lghast.jiagu.JiaguReappear;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModPotions {
    public static final DeferredRegister<Potion> POTIONS =
            DeferredRegister.create(BuiltInRegistries.POTION, JiaguReappear.MOD_ID);

    public static final Holder<Potion> APPETIZING = POTIONS.register("appetizing",
            ()->new Potion(new MobEffectInstance(MobEffects.HUNGER, 1200, 0)));

    public static final Holder<Potion> LONG_APPETIZING = POTIONS.register("long_appetizing",
            ()->new Potion(new MobEffectInstance(MobEffects.HUNGER, 2500, 0)));

    public static final Holder<Potion> DARKNESS = POTIONS.register("darkness",
            ()->new Potion(new MobEffectInstance(MobEffects.DARKNESS, 1800, 0)));

    public static final Holder<Potion> LONG_DARKNESS = POTIONS.register("long_darkness",
            ()->new Potion(new MobEffectInstance(MobEffects.DARKNESS, 4800, 0)));

    public static void register(IEventBus eventBus){
        POTIONS.register(eventBus);
    }
}
