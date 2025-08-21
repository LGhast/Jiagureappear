package net.lghast.jiagu.common.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class InfinitePapyrusItem extends Item {
    public InfinitePapyrusItem(Item.Properties properties) {
        super(properties.rarity(Rarity.EPIC).stacksTo(1));
    }

}
