package net.lghast.jiagu.compat.jei.category;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.lghast.jiagu.compat.jei.recipe.CharacterMorphingRecipe;
import net.lghast.jiagu.register.content.ModBlocks;
import net.lghast.jiagu.register.system.ModTags;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@OnlyIn(Dist.CLIENT)
@ParametersAreNonnullByDefault
public class CharacterMorphingRecipeCategory implements IRecipeCategory<CharacterMorphingRecipe> {
    public static final RecipeType<CharacterMorphingRecipe> TYPE =
            RecipeType.create("jiagureappear", "character_morphing", CharacterMorphingRecipe.class);

    private static final ResourceLocation BACKGROUND =
            ResourceLocation.fromNamespaceAndPath("jiagureappear", "textures/gui/jei/character_morphing.png");

    private static final int WIDTH = 126;
    private static final int HEIGHT = 80;

    private static final int MATERIAL_X = 38;
    private static final int MATERIAL_Y = 11;
    private static final int INPUT_X = 72;
    private static final int INPUT_Y = 11;
    private static final int OUTPUT_START_X = 10;
    private static final int OUTPUT_START_Y = 40;

    private final IDrawable background;
    private final IDrawable icon;

    public CharacterMorphingRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.drawableBuilder(BACKGROUND, 0, 0, WIDTH, HEIGHT)
                .setTextureSize(WIDTH, HEIGHT)
                .build();
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(ModBlocks.CANGJIE_DING_TRIPOD.asItem()));
    }

    @Override
    public @NotNull RecipeType<CharacterMorphingRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("jei.jiagureappear.character_morphing");
    }

    @Override
    public int getWidth() {
        return WIDTH;
    }

    @Override
    public int getHeight() {
        return HEIGHT;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CharacterMorphingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, MATERIAL_X, MATERIAL_Y)
                .addIngredients(Ingredient.of(ModTags.JIAGU_MATERIALS));

        builder.addSlot(RecipeIngredientRole.INPUT, INPUT_X, INPUT_Y)
                .addItemStack(recipe.input());

        for (int i = 0; i < recipe.outputs().size(); i++) {
            ItemStack output = recipe.outputs().get(i);
            if (output.isEmpty()) continue;
            int x = OUTPUT_START_X + (i % 6) * 18;
            int y = OUTPUT_START_Y + (i / 6) * 18;
            builder.addSlot(RecipeIngredientRole.OUTPUT, x, y).addItemStack(output);
        }
    }

    @Override
    public void draw(CharacterMorphingRecipe recipe, IRecipeSlotsView recipeSlotsView,
                     GuiGraphics guiGraphics, double mouseX, double mouseY) {
        background.draw(guiGraphics, 0, 0);
    }
}
