package net.lghast.jiagu.common.menu;

import net.lghast.jiagu.common.item.PrescriptionItem;
import net.lghast.jiagu.register.ModItems;
import net.lghast.jiagu.register.ModMenus;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class PrescriptionMenu extends AbstractContainerMenu {
    private static final int SLOT_COUNT = 3;
    private final Container prescriptionContainer;

    public PrescriptionMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new SimpleContainer(SLOT_COUNT), new SimpleContainerData(2));
    }

    public PrescriptionMenu(int containerId, Inventory playerInventory, Container prescriptionContainer, ContainerData data) {
        super(ModMenus.PRESCRIPTION_MENU.get(), containerId);
        this.prescriptionContainer = prescriptionContainer;

        checkContainerSize(prescriptionContainer, SLOT_COUNT);
        checkContainerDataCount(data, 2);

        for (int i = 0; i < SLOT_COUNT; i++) {
            this.addSlot(new Slot(prescriptionContainer, i, 62 + i * 18, 17) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return stack.is(ModItems.PRESCRIPTION);
                }
            });
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 48 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 106));
        }

        this.addDataSlots(data);
    }

    public Container getPrescriptionContainer() {
        return prescriptionContainer;
    }

    public ItemStack getFirstPrescription() {
        for (int i = 0; i < SLOT_COUNT; i++) {
            ItemStack stack = prescriptionContainer.getItem(i);
            if (!stack.isEmpty() && stack.getItem() instanceof PrescriptionItem) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    public boolean hasPrescription() {
        return !getFirstPrescription().isEmpty();
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
}
