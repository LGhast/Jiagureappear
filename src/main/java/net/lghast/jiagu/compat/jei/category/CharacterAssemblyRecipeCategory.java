package net.lghast.jiagu.compat.jei.category;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.lghast.jiagu.common.content.item.CharacterItem;
import net.lghast.jiagu.compat.jei.recipe.CharacterAssemblyRecipe;
import net.lghast.jiagu.register.content.ModItems;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@OnlyIn(Dist.CLIENT)
@ParametersAreNonnullByDefault
public class CharacterAssemblyRecipeCategory implements IRecipeCategory<CharacterAssemblyRecipe> {
    public static final RecipeType<CharacterAssemblyRecipe> TYPE =
            RecipeType.create("jiagureappear", "character_assembly", CharacterAssemblyRecipe.class);

    private static final ResourceLocation BACKGROUND =
            ResourceLocation.fromNamespaceAndPath("jiagureappear", "textures/gui/jei/assembling.png");

    private static final int WIDTH = 126;
    private static final int HEIGHT = 80;

    private static final int INPUT_START_X = 18;
    private static final int INPUT_START_Y = 15;
    private static final int OUTPUT_X = 93;
    private static final int OUTPUT_Y = 33;

    private final IDrawable background;
    private final IDrawable icon;

    public CharacterAssemblyRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.drawableBuilder(BACKGROUND, 0, 0, WIDTH, HEIGHT)
                .setTextureSize(WIDTH, HEIGHT)
                .build();
        ItemStack iconStack = new ItemStack(ModItems.CHARACTER_ITEM.get());
        CharacterItem.setInscription(iconStack, "合");
        this.icon = guiHelper.createDrawableItemStack(iconStack);
    }

    @Override
    public @NotNull RecipeType<CharacterAssemblyRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("jei.jiagureappear.character_assembly");
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
    public void setRecipe(IRecipeLayoutBuilder builder, CharacterAssemblyRecipe recipe, IFocusGroup focuses) {
        for (int i = 0; i < 9; i++) {
            ItemStack input = recipe.inputs().get(i);
            if (input.isEmpty()) continue;

            int x = INPUT_START_X + (i % 3) * 18;
            int y = INPUT_START_Y + (i / 3) * 18;
            builder.addSlot(RecipeIngredientRole.INPUT, x, y).addItemStack(input);
        }

        builder.addSlot(RecipeIngredientRole.OUTPUT, OUTPUT_X, OUTPUT_Y)
                .addItemStack(recipe.output());
    }

    @Override
    public void draw(CharacterAssemblyRecipe recipe, IRecipeSlotsView recipeSlotsView,
                     GuiGraphics guiGraphics, double mouseX, double mouseY) {
        background.draw(guiGraphics, 0, 0);
    }
}
