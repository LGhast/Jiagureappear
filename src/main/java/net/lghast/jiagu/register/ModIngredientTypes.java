package net.lghast.jiagu.register;

import net.lghast.jiagu.ingredient.CharacterIngredient;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.crafting.IngredientType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

import static net.lghast.jiagu.JiaguReappear.MOD_ID;

public class ModIngredientTypes {
    public static final DeferredRegister<IngredientType<?>> INGREDIENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.Keys.INGREDIENT_TYPES, MOD_ID);

    public static final Supplier<IngredientType<CharacterIngredient>> CHARACTER_INGREDIENT =
            INGREDIENT_TYPES.register("character_ingredient",
                    () -> new IngredientType<>(CharacterIngredient.MAP_CODEC));

    public static void register(IEventBus eventBus) {
        INGREDIENT_TYPES.register(eventBus);
    }
}
