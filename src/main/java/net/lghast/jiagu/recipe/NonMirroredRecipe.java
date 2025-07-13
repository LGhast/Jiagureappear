package net.lghast.jiagu.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.lghast.jiagu.register.ModSerializers;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.NotNull;

public class NonMirroredRecipe implements CraftingRecipe {
    public final ShapedRecipePattern pattern;
    public final ItemStack result;
    private final String group;
    public final CraftingBookCategory category;
    public final boolean showNotification;

    public NonMirroredRecipe(String group, CraftingBookCategory category,
                             ShapedRecipePattern pattern, ItemStack result,
                             boolean showNotification) {
        this.group = group;
        this.category = category;
        this.pattern = pattern;
        this.result = result;
        this.showNotification = showNotification;
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        return pattern.ingredients();
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        if (input.width() < pattern.width() || input.height() < pattern.height()) {
            return false;
        }
        for (int startX = 0; startX <= input.width() - pattern.width(); startX++) {
            for (int startY = 0; startY <= input.height() - pattern.height(); startY++) {
                if (matchesPattern(input, startX, startY)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean matchesPattern(CraftingInput input, int startX, int startY) {
        for (int patternX = 0; patternX < pattern.width(); patternX++) {
            for (int patternY = 0; patternY < pattern.height(); patternY++) {
                int inputX = startX + patternX;
                int inputY = startY + patternY;

                ItemStack itemstack = input.getItem(inputX, inputY);
                int ingredientIndex = patternY * pattern.width() + patternX;
                Ingredient ingredient = pattern.ingredients().get(ingredientIndex);

                if (!ingredient.test(itemstack)) {
                    return false;
                }
            }
        }
        for (int x = 0; x < input.width(); x++) {
            for (int y = 0; y < input.height(); y++) {
                if (x >= startX && x < startX + pattern.width() &&
                        y >= startY && y < startY + pattern.height()) {
                    continue;
                }
                if (!input.getItem(x, y).isEmpty()) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModSerializers.NON_MIRRORED_SHAPED.get();
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width >= pattern.width() && height >= pattern.height();
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider pRegistries) {
        return result;
    }

    @Override
    public CraftingBookCategory category() {
        return CraftingBookCategory.MISC;
    }

    public static class Serializer implements RecipeSerializer<NonMirroredRecipe> {
        public static final MapCodec<NonMirroredRecipe> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        Codec.STRING.optionalFieldOf("group", "").forGetter(recipe -> recipe.group),
                        CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(recipe -> recipe.category),
                        ShapedRecipePattern.MAP_CODEC.forGetter(recipe -> recipe.pattern),
                        ItemStack.STRICT_CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
                        Codec.BOOL.optionalFieldOf("show_notification", true).forGetter(recipe -> recipe.showNotification)
                ).apply(instance, NonMirroredRecipe::new)
        );

        @Override
        public MapCodec<NonMirroredRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, NonMirroredRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        public static final StreamCodec<RegistryFriendlyByteBuf, NonMirroredRecipe> STREAM_CODEC =
                StreamCodec.of(Serializer::toNetwork, Serializer::fromNetwork);

        private static NonMirroredRecipe fromNetwork(RegistryFriendlyByteBuf buf) {
            String group = buf.readUtf();
            CraftingBookCategory category = buf.readEnum(CraftingBookCategory.class);
            ShapedRecipePattern pattern = ShapedRecipePattern.STREAM_CODEC.decode(buf);
            ItemStack result = ItemStack.STREAM_CODEC.decode(buf);
            boolean showNotification = buf.readBoolean();
            return new NonMirroredRecipe(group, category, pattern, result, showNotification);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buf, NonMirroredRecipe recipe) {
            buf.writeUtf(recipe.group);
            buf.writeEnum(recipe.category);
            ShapedRecipePattern.STREAM_CODEC.encode(buf, recipe.pattern);
            ItemStack.STREAM_CODEC.encode(buf, recipe.result);
            buf.writeBoolean(recipe.showNotification);
        }
    }

    @Override
    public boolean isSpecial() {
        return false;
    }
}

