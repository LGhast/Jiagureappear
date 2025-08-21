package net.lghast.jiagu.register;

import net.lghast.jiagu.JiaguReappear;
import net.lghast.jiagu.common.recipe.CharacterAssembling;
import net.lghast.jiagu.common.recipe.CharacterAssemblingNonMirrored;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModRecipes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, JiaguReappear.MOD_ID);

    public static final Supplier<RecipeType<CharacterAssembling>> CHARACTER_ASSEMBLING =
            RECIPE_TYPES.register("character_assembling", () -> new RecipeType<>() {});

    public static final Supplier<RecipeType<CharacterAssemblingNonMirrored>> CHARACTER_ASSEMBLING_NON_MIRRORED =
            RECIPE_TYPES.register("character_assembling_non_mirrored", () -> new RecipeType<>() {});

    public static void register(IEventBus eventBus) {
        RECIPE_TYPES.register(eventBus);
    }
}
