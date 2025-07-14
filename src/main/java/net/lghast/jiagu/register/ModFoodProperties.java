package net.lghast.jiagu.register;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

public class ModFoodProperties {
    private static final List<Holder.Reference<MobEffect>> NON_INSTANT_EFFECT_HOLDERS =
            BuiltInRegistries.MOB_EFFECT.holders()
                    .filter(holder -> !holder.value().isInstantenous())
                    .toList();

    private static final Supplier<MobEffectInstance> RANDOM_EFFECT_SUPPLIER = () -> {
        if (NON_INSTANT_EFFECT_HOLDERS.isEmpty()) return null;

        Holder<MobEffect> randomHolder = NON_INSTANT_EFFECT_HOLDERS.get(
                ThreadLocalRandom.current().nextInt(NON_INSTANT_EFFECT_HOLDERS.size())
        );

        return new MobEffectInstance(randomHolder, 160);
    };

    public static final FoodProperties SHADOW_BERRIES = new FoodProperties.Builder().nutrition(2).saturationModifier(0.1f)
            .effect(()->new MobEffectInstance(MobEffects.DARKNESS,150,0), 0.4f).build();

    public static final FoodProperties SOUR_BERRIES = new FoodProperties.Builder().nutrition(2).saturationModifier(0.1F)
            .effect(()->new MobEffectInstance(MobEffects.HUNGER,150,0), 0.75f).build();

    public static final FoodProperties YOLIME_BREAD = new FoodProperties.Builder().nutrition(6).saturationModifier(0.6F)
            .effect(RANDOM_EFFECT_SUPPLIER, 1.0F).build();
}
