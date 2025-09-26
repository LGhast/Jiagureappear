package net.lghast.jiagu.common.system.menu;

import net.lghast.jiagu.common.content.item.PrescriptionItem;
import net.lghast.jiagu.register.content.ModItems;
import net.lghast.jiagu.register.system.ModMenus;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;

import java.util.ArrayList;
import java.util.List;

public class PrescriptionMenu extends AbstractContainerMenu {
    private static final int SLOT_COUNT = 3;
    private final Container prescriptionContainer;
    private final ItemStack userStack;

    public PrescriptionMenu(int containerId, Inventory playerInventory, ItemStack userStack) {
        this(containerId, playerInventory, new SimpleContainer(SLOT_COUNT), new SimpleContainerData(2), userStack);
    }

    public PrescriptionMenu(int containerId, Inventory playerInventory, Container prescriptionContainer, ContainerData data, ItemStack userStack) {
        super(ModMenus.PRESCRIPTION_MENU.get(), containerId);
        this.prescriptionContainer = prescriptionContainer;
        this.userStack = userStack;

        loadFromItemStack();

        checkContainerSize(prescriptionContainer, SLOT_COUNT);
        checkContainerDataCount(data, 2);

        for (int i = 0; i < SLOT_COUNT; i++) {
            this.addSlot(new Slot(prescriptionContainer, i, 62 + i * 18, 32) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return stack.is(ModItems.PRESCRIPTION);
                }
            });
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 80 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 138));
        }

        this.addDataSlots(data);
    }

    private void loadFromItemStack() {
        ItemContainerContents containerContents = userStack.get(DataComponents.CONTAINER);
        if (containerContents != null) {
            for (int i = 0; i < Math.min(containerContents.getSlots(), SLOT_COUNT); i++) {
                ItemStack stack = containerContents.getStackInSlot(i);
                if (!stack.isEmpty()) {
                    prescriptionContainer.setItem(i, stack);
                }
            }
        }
    }

    public void saveToItemStack() {
        List<ItemStack> items = new ArrayList<>();
        for (int i = 0; i < SLOT_COUNT; i++) {
            items.add(prescriptionContainer.getItem(i).copy());
        }

        ItemContainerContents containerContents = ItemContainerContents.fromItems(items);
        userStack.set(DataComponents.CONTAINER, containerContents);
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot.hasItem()) {
            ItemStack stack1 = slot.getItem();
            stack = stack1.copy();

            if (index < SLOT_COUNT) {
                if (!this.moveItemStackTo(stack1, SLOT_COUNT, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (stack1.getItem() instanceof PrescriptionItem) {
                if (!this.moveItemStackTo(stack1, 0, SLOT_COUNT, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else {
                return ItemStack.EMPTY;
            }

            if (stack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return stack;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        if (!player.level().isClientSide) {
            saveToItemStack();
        }
    }
}
