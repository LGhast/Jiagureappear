package net.lghast.jiagu.common.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public class CangjieJadeBurinItem extends Item {

    public CangjieJadeBurinItem(Properties properties) {
        super(properties.rarity(Rarity.EPIC).stacksTo(1));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isFoil(@NotNull ItemStack itemStack) {
        return true;
    }
}
