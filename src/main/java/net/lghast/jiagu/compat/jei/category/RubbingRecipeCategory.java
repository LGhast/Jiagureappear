package net.lghast.jiagu.compat.jei.category;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.lghast.jiagu.compat.jei.recipe.RubbingRecipe;
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
public class RubbingRecipeCategory implements IRecipeCategory<RubbingRecipe> {
    public static final RecipeType<RubbingRecipe> TYPE =
            RecipeType.create("jiagureappear", "rubbing", RubbingRecipe.class);

    private static final ResourceLocation BACKGROUND =
            ResourceLocation.fromNamespaceAndPath("jiagureappear", "textures/gui/jei/rubbing.png");

    private static final int WIDTH = 126;
    private static final int HEIGHT = 80;

    private static final int INPUT_START_X = 10;
    private static final int INPUT_START_Y = 9;
    private static final int INK_X = 38;
    private static final int INK_Y = 55;
    private static final int OUTPUT_X = 72;
    private static final int OUTPUT_Y = 55;

    private final IDrawable background;
    private final IDrawable icon;

    public RubbingRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.drawableBuilder(BACKGROUND, 0, 0, WIDTH, HEIGHT)
                .setTextureSize(WIDTH, HEIGHT)
                .build();
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(ModBlocks.RUBBING_TABLE.asItem()));
    }

    @Override
    public @NotNull RecipeType<RubbingRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("jei.jiagureappear.rubbing");
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
    public void setRecipe(IRecipeLayoutBuilder builder, RubbingRecipe recipe, IFocusGroup focuses) {
        for (int i = 0; i < recipe.inputs().size(); i++) {
            ItemStack input = recipe.inputs().get(i);
            if (input.isEmpty()) continue;
            int x = INPUT_START_X + (i % 6) * 18;
            int y = INPUT_START_Y + (i / 6) * 18;
            builder.addSlot(RecipeIngredientRole.INPUT, x, y).addItemStack(input);
        }

        builder.addSlot(RecipeIngredientRole.INPUT, INK_X, INK_Y)
                .addIngredients(Ingredient.of(ModTags.RUBBING_INKS));

        builder.addSlot(RecipeIngredientRole.OUTPUT, OUTPUT_X, OUTPUT_Y)
                .addItemStack(recipe.output());
    }

    @Override
    public void draw(RubbingRecipe recipe, IRecipeSlotsView recipeSlotsView,
                     GuiGraphics guiGraphics, double mouseX, double mouseY) {
        background.draw(guiGraphics, 0, 0);
    }
}
