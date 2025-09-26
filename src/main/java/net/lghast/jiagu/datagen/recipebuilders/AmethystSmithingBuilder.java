package net.lghast.jiagu.datagen.recipebuilders;

import net.lghast.jiagu.JiaguReappear;
import net.lghast.jiagu.common.system.ingredient.CharacterIngredient;
import net.lghast.jiagu.register.content.ModItems;
import net.minecraft.advancements.Criterion;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

public class AmethystSmithingBuilder extends SmithingTransformRecipeBuilder {
    private final String identifier;


    private AmethystSmithingBuilder(Ingredient template, String baseInscription, Ingredient addition, RecipeCategory category, Item result, String identifier) {
        super(template, createCharacterIngredient(baseInscription), addition, category, result);
        this.identifier = identifier;
    }

    public static AmethystSmithingBuilder amethystSmithing(String baseInscription, Ingredient addition, RecipeCategory category, Item result, String identifier) {
        Ingredient template = Ingredient.of(ModItems.AMETHYST_UPGRADE_SMITHING_TEMPLATE.get());
        return new AmethystSmithingBuilder(template, baseInscription, addition, category, result, identifier);
    }

    @Override
    public @NotNull AmethystSmithingBuilder unlocks(@NotNull String key, @NotNull Criterion<?> criterion) {
        super.unlocks(key, criterion);
        return this;
    }

    @Override
    public void save(@NotNull RecipeOutput output, @NotNull ResourceLocation id) {
        ResourceLocation fixedId = ResourceLocation.parse(
                JiaguReappear.MOD_ID + ":" + identifier
        );
        super.save(output, fixedId);
    }

    @Override
    public void save(@NotNull RecipeOutput recipeOutput, @NotNull String recipeId) {
        this.save(recipeOutput, ResourceLocation.parse(recipeId));
    }

    private static Ingredient createCharacterIngredient(String inscription) {
        CharacterIngredient characterIngredient = new CharacterIngredient(inscription);
        return new Ingredient(characterIngredient);
    }
}
