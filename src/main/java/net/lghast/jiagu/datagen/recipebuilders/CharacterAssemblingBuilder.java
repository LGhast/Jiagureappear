package net.lghast.jiagu.datagen.recipebuilders;

import net.lghast.jiagu.common.system.ingredient.CharacterIngredient;
import net.lghast.jiagu.register.content.ModItems;
import net.lghast.jiagu.common.content.item.CharacterItem;
import net.lghast.jiagu.common.system.recipe.CharacterAssembling;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import org.jetbrains.annotations.Nullable;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Objects;

public class CharacterAssemblingBuilder implements RecipeBuilder {
    private final RecipeCategory category;
    private final ItemStack result;
    private final String resultInscription;
    private final List<String> rows = Lists.newArrayList();
    private final Map<Character, Ingredient> key = Maps.newLinkedHashMap();
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
    @Nullable
    private String group;
    private boolean showNotification = true;
    private final String identifier;

    public CharacterAssemblingBuilder(RecipeCategory category, ItemStack result,
                                      String resultInscription, String identifier) {
        this.category = category;
        this.result = result;
        this.resultInscription = resultInscription;
        this.identifier = identifier;
    }

    public static CharacterAssemblingBuilder shaped(RecipeCategory category, ItemStack result,
                                                    String resultInscription, String identifier) {
        return new CharacterAssemblingBuilder(category, result, resultInscription, identifier);
    }

    public CharacterAssemblingBuilder define(Character symbol, String inscription) {
        if (this.key.containsKey(symbol)) {
            throw new IllegalArgumentException("Symbol '" + symbol + "' is already defined!");
        } else if (symbol == ' ') {
            throw new IllegalArgumentException("Symbol ' ' (whitespace) is reserved");
        } else {
            CharacterIngredient customIngredient = new CharacterIngredient(inscription);
            this.key.put(symbol, new Ingredient(customIngredient));
            return this;
        }
    }

    public CharacterAssemblingBuilder pattern(String pattern) {
        if (!this.rows.isEmpty() && pattern.length() != this.rows.get(0).length()) {
            throw new IllegalArgumentException("Pattern must be the same width on every line!");
        } else {
            this.rows.add(pattern);
            return this;
        }
    }

    @Override
    public CharacterAssemblingBuilder unlockedBy(String name, Criterion<?> criterion) {
        this.criteria.put(name, criterion);
        return this;
    }

    @Override
    public CharacterAssemblingBuilder group(@Nullable String groupName) {
        this.group = groupName;
        return this;
    }

    public CharacterAssemblingBuilder showNotification(boolean show) {
        this.showNotification = show;
        return this;
    }

    @Override
    public Item getResult() {
        return this.result.getItem();
    }

    @Override
    public void save(RecipeOutput output, ResourceLocation id) {
        if (this.criteria.isEmpty()) {
            throw new IllegalStateException("No unlock criteria defined for recipe " + id);
        }
        ShapedRecipePattern pattern = ShapedRecipePattern.of(this.key, this.rows);

        Advancement.Builder advancementBuilder = output.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                .rewards(AdvancementRewards.Builder.recipe(id))
                .requirements(AdvancementRequirements.Strategy.OR);

        this.criteria.forEach(advancementBuilder::addCriterion);
        ResourceLocation recipeId = ResourceLocation.parse(
                id.getNamespace() + ":character_assembling/" + this.identifier
        );
        CharacterAssembling recipe = new CharacterAssembling(
                Objects.requireNonNullElse(this.group, ""),
                RecipeBuilder.determineBookCategory(this.category),
                pattern,
                this.result.copy(),
                this.resultInscription,
                this.showNotification
        );
        if(recipe.result.is(ModItems.CHARACTER_ITEM.get())) {
            CharacterItem.setInscription(recipe.result, this.resultInscription);
        }

        output.accept(recipeId, recipe, advancementBuilder.build(recipeId.withPrefix("recipes/" + this.category.getFolderName() + "/")));
    }
}
