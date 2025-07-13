package net.lghast.jiagu.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public class InfinitePapyrusItem extends Item {
    public InfinitePapyrusItem(Item.Properties properties) {
        super(properties.rarity(Rarity.EPIC).stacksTo(1));
    }

}
