package net.lghast.jiagu.item;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public enum CharacterTier implements Tier {
    AMETHYST(3.0f, 1234, 8.0F, 18, Ingredient.of(Items.AMETHYST_SHARD), BlockTags.INCORRECT_FOR_DIAMOND_TOOL);

    private final int uses;
    private final float speed;
    private final float attackDamageBonus;
    private final int enchantmentValue;
    private final TagKey<Block> incorrectBlocksForDrops;
    private final Ingredient repairIngredient;

    CharacterTier(float attackDamageBonus, int durability, float speed, int enchantmentValue, Ingredient repairIngredient, TagKey<Block> incorrectBlocksForDrops) {
        this.attackDamageBonus = attackDamageBonus;
        this.uses = durability;
        this.speed = speed;
        this.enchantmentValue = enchantmentValue;
        this.repairIngredient = repairIngredient;
        this.incorrectBlocksForDrops = incorrectBlocksForDrops;
    }

    @Override
    public int getUses() {
        return uses;
    }

    @Override
    public float getSpeed() {
        return speed;
    }

    @Override
    public float getAttackDamageBonus() {
        return attackDamageBonus;
    }

    @Override
    public TagKey<Block> getIncorrectBlocksForDrops() {
        return incorrectBlocksForDrops;
    }

    @Override
    public int getEnchantmentValue() {
        return enchantmentValue;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return repairIngredient;
    }
}
