package net.lghast.jiagu.utils;

import net.minecraft.core.Direction;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class SidedInvHandler implements IItemHandler {
    private final WorldlyContainer container;
    @Nullable
    private final Direction side;

    public SidedInvHandler(WorldlyContainer container, @Nullable Direction side) {
        this.container = container;
        this.side = side;
    }

    @Override
    public int getSlots() {
        return container.getContainerSize();
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int slot) {
        return slot >= 0 && slot < getSlots() ? container.getItem(slot) : ItemStack.EMPTY;
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (stack.isEmpty() || isInvalidSlot(slot, true)) {
            return stack;
        }

        if (!container.canPlaceItem(slot, stack)) {
            return stack;
        }

        ItemStack existing = container.getItem(slot);
        int limit = Math.min(container.getMaxStackSize(), stack.getMaxStackSize());
        if (!existing.isEmpty()) {
            if (!ItemStack.isSameItemSameComponents(existing, stack)) {
                return stack;
            }
            limit -= existing.getCount();
        }
        if (limit <= 0) {
            return stack;
        }

        int toInsert = Math.min(limit, stack.getCount());
        if (!simulate) {
            if (existing.isEmpty()) {
                container.setItem(slot, stack.copyWithCount(toInsert));
            } else {
                existing.grow(toInsert);
            }
            container.setChanged();
        }

        return stack.copyWithCount(stack.getCount() - toInsert);
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (amount <= 0 || isInvalidSlot(slot, false)) {
            return ItemStack.EMPTY;
        }

        ItemStack existing = container.getItem(slot);
        if (existing.isEmpty()) {
            return ItemStack.EMPTY;
        }

        int toExtract = Math.min(amount, existing.getCount());
        ItemStack result = existing.copyWithCount(toExtract);
        if (!simulate) {
            if (toExtract == existing.getCount()) {
                container.setItem(slot, ItemStack.EMPTY);
            } else {
                existing.shrink(toExtract);
            }
            container.setChanged();
        }
        return result;
    }

    @Override
    public int getSlotLimit(int slot) {
        return container.getMaxStackSize();
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return slot >= 0 && slot < getSlots() && container.canPlaceItem(slot, stack);
    }

    private boolean isInvalidSlot(int slot, boolean insert) {
        if (side != null) {
            for (int allowed : container.getSlotsForFace(side)) {
                if (allowed == slot) {
                    if (insert) {
                        return !container.canPlaceItemThroughFace(slot, container.getItem(slot), side);
                    }
                    return !container.canTakeItemThroughFace(slot, container.getItem(slot), side);
                }
            }
        }
        return true;
    }
}
