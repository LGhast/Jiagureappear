package net.lghast.jiagu.compat.jei.recipe;

import net.minecraft.world.item.ItemStack;

import java.util.List;

public record CharacterDisassemblyRecipe(ItemStack input, List<ItemStack> outputs) {}
