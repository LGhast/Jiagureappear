package net.lghast.jiagu.compat.jei.category;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.lghast.jiagu.compat.jei.recipe.EnchantmentRubbingRecipe;
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
public class EnchantmentRubbingRecipeCategory implements IRecipeCategory<EnchantmentRubbingRecipe> {
    public static final RecipeType<EnchantmentRubbingRecipe> TYPE =
            RecipeType.create("jiagureappear", "enchantment_rubbing", EnchantmentRubbingRecipe.class);

    private static final ResourceLocation BACKGROUND =
            ResourceLocation.fromNamespaceAndPath("jiagureappear", "textures/gui/jei/rubbing_enchantment.png");

    private static final int WIDTH = 126;
    private static final int HEIGHT = 80;

    private static final int INPUT_START_X = 10;
    private static final int INPUT_START_Y = 9;
    private static final int TALISMAN_X = 38;
    private static final int TALISMAN_Y = 55;
    private static final int OUTPUT_X = 72;
    private static final int OUTPUT_Y = 55;

    private final IDrawable background;
    private final IDrawable icon;

    public EnchantmentRubbingRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.drawableBuilder(BACKGROUND, 0, 0, WIDTH, HEIGHT)
                .setTextureSize(WIDTH, HEIGHT)
                .build();
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(ModItems.TAOIST_TALISMAN.get()));
    }

    @Override
    public @NotNull RecipeType<EnchantmentRubbingRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("jei.jiagureappear.enchantment_rubbing");
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
    public void setRecipe(IRecipeLayoutBuilder builder, EnchantmentRubbingRecipe recipe, IFocusGroup focuses) {
        for (int i = 0; i < recipe.inputs().size(); i++) {
            ItemStack input = recipe.inputs().get(i);
            if (input.isEmpty()) continue;
            int x = INPUT_START_X + (i % 6) * 18;
            int y = INPUT_START_Y + (i / 6) * 18;
            builder.addSlot(RecipeIngredientRole.INPUT, x, y).addItemStack(input);
        }

        builder.addSlot(RecipeIngredientRole.INPUT, TALISMAN_X, TALISMAN_Y)
                .addItemStack(recipe.talisman());

        builder.addSlot(RecipeIngredientRole.OUTPUT, OUTPUT_X, OUTPUT_Y)
                .addIngredients(recipe.output());
    }

    @Override
    public void draw(EnchantmentRubbingRecipe recipe, IRecipeSlotsView recipeSlotsView,
                     GuiGraphics guiGraphics, double mouseX, double mouseY) {
        background.draw(guiGraphics, 0, 0);
    }
}
