package net.lghast.jiagu.compat.jei.category;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.lghast.jiagu.compat.jei.recipe.CharacterDisassemblyRecipe;
import net.lghast.jiagu.register.content.ModBlocks;
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
public class CharacterDisassemblyRecipeCategory implements IRecipeCategory<CharacterDisassemblyRecipe> {
    public static final RecipeType<CharacterDisassemblyRecipe> TYPE =
            RecipeType.create("jiagureappear", "character_disassembly", CharacterDisassemblyRecipe.class);

    private static final ResourceLocation BACKGROUND =
            ResourceLocation.fromNamespaceAndPath("jiagureappear", "textures/gui/jei/disassembling.png");

    private static final int WIDTH = 126;
    private static final int HEIGHT = 80;

    private static final int INPUT_X = 18;
    private static final int INPUT_Y = 32;
    private static final int OUTPUT_START_X = 57;
    private static final int OUTPUT_START_Y = 14;

    private final IDrawable background;
    private final IDrawable icon;

    public CharacterDisassemblyRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.drawableBuilder(BACKGROUND, 0, 0, WIDTH, HEIGHT)
                .setTextureSize(WIDTH, HEIGHT)
                .build();
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(ModBlocks.CHARACTER_DISASSEMBLER.asItem()));
    }

    @Override
    public @NotNull RecipeType<CharacterDisassemblyRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("jei.jiagureappear.character_disassembly");
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
    public void setRecipe(IRecipeLayoutBuilder builder, CharacterDisassemblyRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, INPUT_X, INPUT_Y)
                .addItemStack(recipe.input());

        for (int i = 0; i < 9; i++) {
            ItemStack output = recipe.outputs().get(i);
            if (output.isEmpty()) continue;

            int x = OUTPUT_START_X + (i % 3) * 18;
            int y = OUTPUT_START_Y + (i / 3) * 18;
            builder.addSlot(RecipeIngredientRole.OUTPUT, x, y).addItemStack(output);
        }
    }

    @Override
    public void draw(CharacterDisassemblyRecipe recipe, IRecipeSlotsView recipeSlotsView,
                     GuiGraphics guiGraphics, double mouseX, double mouseY) {
        background.draw(guiGraphics, 0, 0);
    }
}
