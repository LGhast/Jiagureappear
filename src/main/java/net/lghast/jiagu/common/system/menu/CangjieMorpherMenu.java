package net.lghast.jiagu.common.system.menu;

import net.lghast.jiagu.common.content.blockentity.CangjieMorpherBlockEntity;
import net.lghast.jiagu.register.system.ModMenus;
import net.lghast.jiagu.register.system.ModTags;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class CangjieMorpherMenu extends AbstractContainerMenu {
    public static final int SLOT_COUNT = 4;

    private static final int[] MATERIAL_SLOT_X = {43, 61};
    private static final int MATERIAL_SLOT_Y = 25;

    private static final int[] CONTENT_SLOT_X = {100, 118};
    private static final int CONTENT_SLOT_Y = 25;

    private final CangjieMorpherBlockEntity blockEntity;

    public CangjieMorpherMenu(int containerId, Inventory playerInventory, CangjieMorpherBlockEntity blockEntity) {
        super(ModMenus.CANGJIE_MORPHER_MENU.get(), containerId);
        this.blockEntity = blockEntity;

        checkContainerSize(blockEntity, SLOT_COUNT);

        for (int i = 0; i < CangjieMorpherBlockEntity.MATERIAL_SLOTS; i++) {
            this.addSlot(new Slot(blockEntity, i, MATERIAL_SLOT_X[i], MATERIAL_SLOT_Y) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return stack.is(ModTags.JIAGU_MATERIALS);
                }
            });
        }

        for (int i = 0; i < CangjieMorpherBlockEntity.CONTENT_SLOTS; i++) {
            int slotIndex = CangjieMorpherBlockEntity.CONTENT_SLOT_OFFSET + i;
            this.addSlot(new Slot(blockEntity, slotIndex, CONTENT_SLOT_X[i], CONTENT_SLOT_Y) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return !stack.is(ModTags.INVALID_TO_CHARACTERS) && !stack.is(ModTags.JIAGU_MATERIALS);
                }
            });
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 59 + i * 18));
            }
        }

        for (int k = 0; k < 9; k++) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 117));
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return blockEntity.stillValid(player);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(Player player, int index) {
        ItemStack original = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            original = slotStack.copy();

            if (index < SLOT_COUNT) {
                if (!this.moveItemStackTo(slotStack, SLOT_COUNT, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(slotStack, 0, SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }

            if (slotStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return original;
    }
}