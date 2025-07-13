package net.lghast.jiagu.item;

import net.lghast.jiagu.register.ModItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class NiConverseAmethystItem extends Item {
    public NiConverseAmethystItem(Properties properties) {
        super(properties.rarity(Rarity.EPIC).stacksTo(1).craftRemainder(ModItems.NI_CONVERSE_AMETHYST_DORMANT.asItem()));
    }
}
