package net.lghast.jiagu.common.content.item;

import net.lghast.jiagu.utils.LzhWriterUtils;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

public class InfinitePapyrusItem extends Item {
    public InfinitePapyrusItem(Item.Properties properties) {
        super(properties.rarity(Rarity.EPIC).stacksTo(1));
    }

    /*@Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if(level.isClientSide) return InteractionResultHolder.sidedSuccess(player.getMainHandItem(), true);

        LzhWriterUtils.write("sophisticatedbackpacks");

        return InteractionResultHolder.sidedSuccess(player.getMainHandItem(), true);
    }*/
}
