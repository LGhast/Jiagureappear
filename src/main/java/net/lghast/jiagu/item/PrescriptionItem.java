package net.lghast.jiagu.item;

import net.minecraft.world.item.*;

public class PrescriptionItem extends Item {
    public PrescriptionItem(Properties properties) {
        super(properties.rarity(Rarity.RARE).stacksTo(1));
    }
}
