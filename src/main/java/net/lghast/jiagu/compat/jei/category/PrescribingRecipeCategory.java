package net.lghast.jiagu.compat.jei.category;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.lghast.jiagu.compat.jei.recipe.PrescribingRecipe;
import net.lghast.jiagu.register.content.ModBlocks;
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
public class PrescribingRecipeCategory implements IRecipeCategory<PrescribingRecipe> {
    public static final RecipeType<PrescribingRecipe> TYPE =
            RecipeType.create("jiagureappear", "prescription", PrescribingRecipe.class);

    private static final ResourceLocation BACKGROUND =
            ResourceLocation.fromNamespaceAndPath("jiagureappear", "textures/gui/jei/prescribing.png");

    private static final int WIDTH = 126;
    private static final int HEIGHT = 80;

    private static final int EMPTY_PRESCRIPTION_X = 10;
    private static final int EMPTY_PRESCRIPTION_Y = 15;
    private static final int OUTPUT_X = 100;
    private static final int OUTPUT_Y = 15;
    private static final int INPUT_START_X = 10;
    private static final int INPUT_START_Y = 49;

    private final IDrawable background;
    private final IDrawable icon;

    public PrescribingRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.drawableBuilder(BACKGROUND, 0, 0, WIDTH, HEIGHT)
                .setTextureSize(WIDTH, HEIGHT)
                .build();
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(ModBlocks.YAOWANG_GOURD.asItem()));
    }

    @Override
    public @NotNull RecipeType<PrescribingRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("jei.jiagureappear.prescription");
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
    public void setRecipe(IRecipeLayoutBuilder builder, PrescribingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, EMPTY_PRESCRIPTION_X, EMPTY_PRESCRIPTION_Y)
                .addItemStack(new ItemStack(ModItems.EMPTY_PRESCRIPTION.get()));

        builder.addSlot(RecipeIngredientRole.OUTPUT, OUTPUT_X, OUTPUT_Y)
                .addItemStack(recipe.output());

        for (int i = 0; i < recipe.inputs().size(); i++) {
            ItemStack input = recipe.inputs().get(i);
            if (input.isEmpty()) continue;
            builder.addSlot(RecipeIngredientRole.INPUT, INPUT_START_X + i * 18, INPUT_START_Y)
                    .addItemStack(input);
        }
    }

    @Override
    public void draw(PrescribingRecipe recipe, IRecipeSlotsView recipeSlotsView,
                     GuiGraphics guiGraphics, double mouseX, double mouseY) {
        background.draw(guiGraphics, 0, 0);
    }
}
