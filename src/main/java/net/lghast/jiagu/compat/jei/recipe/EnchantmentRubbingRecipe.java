package net.lghast.jiagu.compat.jei.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public record EnchantmentRubbingRecipe(List<ItemStack> inputs, ItemStack talisman, Ingredient output) {}
