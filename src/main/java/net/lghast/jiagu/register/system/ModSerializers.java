package net.lghast.jiagu.register.system;

import net.lghast.jiagu.common.system.recipe.CharacterAssembling;
import net.lghast.jiagu.common.system.recipe.CharacterAssemblingNonMirrored;
import net.lghast.jiagu.common.system.recipe.NonMirroredRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, "jiagureappear");

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<NonMirroredRecipe>>
            NON_MIRRORED_SHAPED = RECIPE_SERIALIZERS.register(
            "non_mirrored_shaped",
            NonMirroredRecipe.Serializer::new
    );

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<CharacterAssemblingNonMirrored>>
            CHARACTER_ASSEMBLING_NON_MIRRORED = RECIPE_SERIALIZERS.register(
            "character_assembling_non_mirrored",
            CharacterAssemblingNonMirrored.Serializer::new
    );

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<CharacterAssembling>>
            CHARACTER_ASSEMBLING = RECIPE_SERIALIZERS.register(
            "character_assembling",
            CharacterAssembling.Serializer::new
    );

    public static void register(IEventBus eventBus) {
        RECIPE_SERIALIZERS.register(eventBus);
    }
}