package net.lghast.jiagu.compat.jei.recipe;

import net.minecraft.world.item.ItemStack;

import java.util.List;

public record CharacterAssemblyRecipe(List<ItemStack> inputs, ItemStack output) {}
