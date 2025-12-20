package net.lghast.jiagu.common.system.menu;

import net.lghast.jiagu.common.content.blockentity.YaowangGourdBlockEntity;
import net.lghast.jiagu.register.content.ModItems;
import net.lghast.jiagu.register.system.ModMenus;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class YaowangGourdMenu extends AbstractContainerMenu {
    private final YaowangGourdBlockEntity blockEntity;

    public YaowangGourdMenu(int containerId, Inventory playerInventory, YaowangGourdBlockEntity blockEntity) {
        super(ModMenus.YAOWANG_GOURD_MENU.get(), containerId);
        this.blockEntity = blockEntity;

        addSlots(playerInventory);
    }

    private void addSlots(Inventory playerInventory) {
        this.addSlot(new Slot(blockEntity, 0, 35, 28) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.getItem() == ModItems.EMPTY_PRESCRIPTION.get();
            }
        });

        for (int i = 0; i < 6; i++) {
            this.addSlot(new Slot(blockEntity, i + 1, 35 + i * 18, 62) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return stack.getItem() == ModItems.CHARACTER_ITEM.get();
                }
            });
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 94 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 152));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack originalStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            originalStack = slotStack.copy();

            if (index < 7) {
                if (!this.moveItemStackTo(slotStack, 7, 43, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (slotStack.getItem() == ModItems.EMPTY_PRESCRIPTION.get()) {
                    if (!this.moveItemStackTo(slotStack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slotStack.getItem() == ModItems.CHARACTER_ITEM.get()) {
                    if (!this.moveItemStackTo(slotStack, 1, 7, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            }

            if (slotStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return originalStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return blockEntity.stillValid(player);
    }

    public YaowangGourdBlockEntity getBlockEntity() {
        return blockEntity;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);

        if (!player.level().isClientSide) {
            for (int i = 0; i < blockEntity.getContainerSize(); i++) {
                ItemStack stack = blockEntity.getItem(i);
                if (!stack.isEmpty()) {
                    if (player.isAlive()) {
                        player.getInventory().placeItemBackInInventory(stack);
                    } else {
                        player.drop(stack, false);
                    }
                    blockEntity.setItem(i, ItemStack.EMPTY);
                }
            }
            blockEntity.setChanged();
        }
    }
}
