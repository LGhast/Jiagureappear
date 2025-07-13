package net.lghast.jiagu.register;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.food.Foods;

public class ModFoodProperties {
    public static final FoodProperties SHADOW_BERRIES = new FoodProperties.Builder().nutrition(2).saturationModifier(0.1f)
            .effect(()->new MobEffectInstance(MobEffects.DARKNESS,150,0), 0.4f).build();

    public static final FoodProperties SOUR_BERRIES = new FoodProperties.Builder().nutrition(2).saturationModifier(0.1F)
            .effect(()->new MobEffectInstance(MobEffects.HUNGER,150,0), 0.75f).build();

    public static final FoodProperties YOLIME_BREAD = new FoodProperties.Builder().nutrition(6).saturationModifier(0.6F).build();
}
