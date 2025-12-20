package net.lghast.jiagu.common.system.menu;

import net.lghast.jiagu.register.system.ModMenus;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class DictionaryMenu extends AbstractContainerMenu {
    private final Player player;

    public DictionaryMenu(int containerId, Inventory playerInventory) {
        super(ModMenus.DICTIONARY_MENU.get(), containerId);
        this.player = playerInventory.player;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
