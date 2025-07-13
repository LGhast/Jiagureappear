package net.lghast.jiagu.datagen.recipebuilders;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.lghast.jiagu.JiaguReappear;
import net.lghast.jiagu.ingredient.CharacterIngredient;
import net.lghast.jiagu.item.CharacterItem;
import net.lghast.jiagu.recipe.CharacterAssembling;
import net.lghast.jiagu.register.ModItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.neoforged.neoforge.common.crafting.ICustomIngredient;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    public AmethystSmithingBuilder unlocks(String key, Criterion<?> criterion) {
        super.unlocks(key, criterion);
        return this;
    }

    @Override
    public void save(RecipeOutput output, ResourceLocation id) {
        ResourceLocation fixedId = ResourceLocation.parse(
                JiaguReappear.MOD_ID + ":" + identifier
        );
        super.save(output, fixedId);
    }

    @Override
    public void save(RecipeOutput recipeOutput, String recipeId) {
        this.save(recipeOutput, ResourceLocation.parse(recipeId));
    }

    private static Ingredient createCharacterIngredient(String inscription) {
        CharacterIngredient characterIngredient = new CharacterIngredient(inscription);
        return new Ingredient(characterIngredient);
    }
}
