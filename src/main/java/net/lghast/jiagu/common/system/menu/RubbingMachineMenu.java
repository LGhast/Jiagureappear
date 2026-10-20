package net.lghast.jiagu.common.system.menu;

import net.lghast.jiagu.common.content.blockentity.RubbingMachineBlockEntity;
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
public class RubbingMachineMenu extends AbstractContainerMenu {
    public static final int SLOT_COUNT = 3;

    private static final int[] INK_SLOT_X = {52, 70};
    private static final int INK_SLOT_Y = 25;

    private static final int TEMPLATE_SLOT_X = 109;
    private static final int TEMPLATE_SLOT_Y = 25;

    private final RubbingMachineBlockEntity blockEntity;

    public RubbingMachineMenu(int containerId, Inventory playerInventory, RubbingMachineBlockEntity blockEntity) {
        super(ModMenus.RUBBING_MACHINE_MENU.get(), containerId);
        this.blockEntity = blockEntity;

        checkContainerSize(blockEntity, SLOT_COUNT);

        for (int i = 0; i < RubbingMachineBlockEntity.INK_SLOTS; i++) {
            this.addSlot(new Slot(blockEntity, i, INK_SLOT_X[i], INK_SLOT_Y) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return stack.is(ModTags.RUBBING_INKS);
                }
            });
        }

        this.addSlot(new Slot(blockEntity, RubbingMachineBlockEntity.TEMPLATE_SLOT,
                TEMPLATE_SLOT_X, TEMPLATE_SLOT_Y) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return !stack.is(ModTags.INVALID_TO_CHARACTERS);
            }
            @Override
            public int getMaxStackSize() {
                return 1;
            }
        });

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
