package net.lghast.jiagu.common.system.menu;

import net.lghast.jiagu.character.CharacterInfo;
import net.lghast.jiagu.character.CharacterStructure;
import net.lghast.jiagu.common.content.item.CharacterItem;
import net.lghast.jiagu.common.system.advancement.DisassembleTrigger;
import net.lghast.jiagu.config.CommonConfig;
import net.lghast.jiagu.register.content.ModItems;
import net.lghast.jiagu.register.system.ModMenus;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

@ParametersAreNonnullByDefault
public class CharacterCrafterMenu extends AbstractContainerMenu {
    public static final int MODE_ASSEMBLE = 0;
    public static final int MODE_DISASSEMBLE = 1;

    public static final int GRID_START = 0;
    public static final int GRID_COUNT = 9;
    public static final int RESULT_SLOT = 9;
    public static final int INPUT_SLOT = 10;
    public static final int OUTPUT_START = 11;
    public static final int OUTPUT_COUNT = 9;
    public static final int PLAYER_START = 20;
    public static final int PLAYER_COUNT = 36;

    private final SimpleContainer craftContainer;
    private final ContainerData data;
    private boolean refreshing;
    private boolean clearing;

    public CharacterCrafterMenu(int containerId, Inventory playerInventory) {
        super(ModMenus.CHARACTER_CRAFTER_MENU.get(), containerId);
        this.data = new SimpleContainerData(1);
        this.data.set(0, MODE_ASSEMBLE);
        this.craftContainer = new SimpleContainer(20) {
            @Override
            public void setChanged() {
                super.setChanged();
                CharacterCrafterMenu.this.slotsChanged(this);
            }
        };

        checkContainerSize(this.craftContainer, 20);
        checkContainerDataCount(this.data, 1);

        for (int i = 0; i < GRID_COUNT; i++) {
            this.addSlot(new GridSlot(i, 43 + (i % 3) * 18, 28 + (i / 3) * 18));
        }
        this.addSlot(new ResultSlot(RESULT_SLOT, 118, 46));
        this.addSlot(new InputSlot(INPUT_SLOT, 42, 46));
        for (int i = 0; i < OUTPUT_COUNT; i++) {
            this.addSlot(new OutputSlot(OUTPUT_START + i, 81 + (i % 3) * 18, 28 + (i / 3) * 18));
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 103 + i * 18));
            }
        }
        for (int k = 0; k < 9; k++) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 161));
        }

        this.addDataSlots(this.data);
        refreshResult();
    }

    public int getMode() {
        return this.data.get(0);
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public boolean clickMenuButton(Player player, int button) {
        if (button != 0) return false;
        if (player.level().isClientSide) return true;
        if (getMode() == MODE_ASSEMBLE) {
            for (int i = GRID_START; i < GRID_START + GRID_COUNT; i++) {
                ItemStack stack = this.craftContainer.getItem(i);
                if (!stack.isEmpty()) {
                    this.craftContainer.setItem(i, ItemStack.EMPTY);
                    returnToPlayer(player, stack);
                }
            }
        } else {
            ItemStack stack = this.craftContainer.getItem(INPUT_SLOT);
            if (!stack.isEmpty()) {
                this.craftContainer.setItem(INPUT_SLOT, ItemStack.EMPTY);
                returnToPlayer(player, stack);
            }
        }
        this.data.set(0, getMode() == MODE_ASSEMBLE ? MODE_DISASSEMBLE : MODE_ASSEMBLE);
        refreshResult();
        broadcastChanges();
        return true;
    }

    @Override
    public void clicked(int slotId, int button, ClickType clickType, Player player) {
        if (getMode() == MODE_DISASSEMBLE
                && slotId >= OUTPUT_START && slotId < OUTPUT_START + OUTPUT_COUNT) {
            Slot slot = this.slots.get(slotId);
            if (!slot.hasItem()) {
                super.clicked(slotId, button, clickType, player);
                return;
            }
            if (player.level().isClientSide) return;

            if (clickType == ClickType.QUICK_MOVE) {
                while (this.craftContainer.getItem(INPUT_SLOT).getCount() > 0) {
                    completeDisassembly(player);
                }
            } else {
                completeDisassembly(player);
            }
            broadcastChanges();
            return;
        }
        super.clicked(slotId, button, clickType, player);
    }

    @Override
    public void slotsChanged(Container container) {
        super.slotsChanged(container);
        if (this.clearing || this.refreshing) return;
        if (container == this.craftContainer) {
            refreshResult();
        }
    }

    private void refreshResult() {
        this.refreshing = true;
        try {
            if (getMode() == MODE_ASSEMBLE) {
                String candidate = findAssemblyMatch();
                this.craftContainer.setItem(RESULT_SLOT,
                        candidate == null ? ItemStack.EMPTY : createCharacterStack(candidate));
            } else {
                this.craftContainer.setItem(RESULT_SLOT, ItemStack.EMPTY);
                ItemStack input = this.craftContainer.getItem(INPUT_SLOT);
                String inscription = input.isEmpty() ? null : CharacterItem.getInscription(input);
                List<String> components = inscription == null ? null : CharacterInfo.getComponents(inscription);
                if (components == null || components.isEmpty()) {
                    for (int i = 0; i < OUTPUT_COUNT; i++) {
                        this.craftContainer.setItem(OUTPUT_START + i, ItemStack.EMPTY);
                    }
                } else {
                    List<String> layout = getLayout(inscription, components);
                    for (int i = 0; i < OUTPUT_COUNT; i++) {
                        String component = layout.get(i);
                        this.craftContainer.setItem(OUTPUT_START + i,
                                component == null ? ItemStack.EMPTY : createCharacterStack(component));
                    }
                }
            }
        } finally {
            this.refreshing = false;
        }
    }

    @Nullable
    private String findAssemblyMatch() {
        Set<String> candidates = new LinkedHashSet<>(CharacterInfo.CHARACTER_COMPONENTS.keySet());
        for (String mapping : CommonConfig.CHARACTER_DISASSEMBLING_MAPPINGS.get()) {
            String[] parts = mapping.split("=", 2);
            if (parts.length == 2 && !parts[0].isBlank()) {
                candidates.add(parts[0]);
            }
        }

        String best = null;
        int bestSize = -1;
        for (String candidate : candidates) {
            List<String> components = CharacterInfo.getComponents(candidate);
            if (components == null || components.isEmpty()) continue;
            if (components.size() <= bestSize) continue;
            List<String> layout = getLayout(candidate, components);
            if (matchesGridTranslated(layout)) {
                best = candidate;
                bestSize = components.size();
            }
        }
        return best;
    }

    private boolean matchesGridTranslated(List<String> layout) {
        for (int dx = -2; dx <= 2; dx++) {
            for (int dy = -2; dy <= 2; dy++) {
                if (matchesAt(layout, dx, dy)) return true;
            }
        }
        return false;
    }

    private boolean matchesAt(List<String> layout, int dx, int dy) {
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                String expected = layout.get(y * 3 + x);
                int gx = x + dx;
                int gy = y + dy;
                if (expected == null) {
                    if (gx < 0 || gx >= 3 || gy < 0 || gy >= 3) continue;
                    ItemStack stack = this.craftContainer.getItem(gy * 3 + gx);
                    if (!stack.isEmpty()) return false;
                } else {
                    if (gx < 0 || gx >= 3 || gy < 0 || gy >= 3) return false;
                    ItemStack stack = this.craftContainer.getItem(gy * 3 + gx);
                    if (stack.isEmpty()) return false;
                    if (!expected.equals(CharacterItem.getInscription(stack))) return false;
                }
            }
        }
        return true;
    }

    private static List<String> getLayout(String inscription, List<String> components) {
        CharacterStructure structure = CharacterInfo.getStructure(inscription);
        if (structure != null) {
            List<String> layout = structure.getComponentList(components);
            if (layout.size() == 9) return layout;
        }
        List<String> layout = new ArrayList<>(Collections.nCopies(9, null));
        int count = Math.min(components.size(), 3);
        int startCol = (3 - count) / 2;
        for (int i = 0; i < count; i++) {
            layout.set(3 + startCol + i, components.get(i));
        }
        return layout;
    }

    private static ItemStack createCharacterStack(String inscription) {
        ItemStack stack = new ItemStack(ModItems.CHARACTER_ITEM.get());
        CharacterItem.setInscription(stack, inscription);
        return stack;
    }

    private static void returnToPlayer(Player player, ItemStack stack) {
        if (!player.isAlive()) {
            player.drop(stack, false);
            return;
        }
        player.getInventory().placeItemBackInInventory(stack);
        if (!stack.isEmpty()) {
            player.drop(stack, false);
        }
    }

    private void consumeAssembleInputs() {
        for (int i = GRID_START; i < GRID_START + GRID_COUNT; i++) {
            if (!this.craftContainer.getItem(i).isEmpty()) {
                this.craftContainer.removeItem(i, 1);
            }
        }
    }

    private void completeDisassembly(Player player) {
        for (int i = OUTPUT_START; i < OUTPUT_START + OUTPUT_COUNT; i++) {
            ItemStack stack = this.craftContainer.getItem(i);
            if (stack.isEmpty()) continue;
            this.craftContainer.setItem(i, ItemStack.EMPTY);
            returnToPlayer(player, stack);
        }

        String inscription = null;
        ItemStack input = this.craftContainer.getItem(INPUT_SLOT);
        if (!input.isEmpty()) {
            inscription = CharacterItem.getInscription(input);
            this.craftContainer.removeItem(INPUT_SLOT, 1);
        }
        refreshResult();
        if (player instanceof ServerPlayer serverPlayer && inscription != null) {
            DisassembleTrigger.TRIGGER.get().trigger(serverPlayer, inscription);
        }
    }

    @Override
    public @NotNull ItemStack quickMoveStack(Player player, int index) {
        Slot slot = this.slots.get(index);
        if (!slot.hasItem()) return ItemStack.EMPTY;
        ItemStack slotStack = slot.getItem();
        ItemStack original = slotStack.copy();

        if (index == RESULT_SLOT) {
            boolean movedAny = false;
            while (true) {
                ItemStack current = this.craftContainer.getItem(RESULT_SLOT);
                if (current.isEmpty()) break;
                ItemStack single = current.copyWithCount(1);
                if (!this.moveItemStackTo(single, PLAYER_START, PLAYER_START + PLAYER_COUNT, true)) {
                    break;
                }
                consumeAssembleInputs();
                refreshResult();
                movedAny = true;
            }
            if (!movedAny) return ItemStack.EMPTY;
            if (this.craftContainer.getItem(RESULT_SLOT).isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            broadcastChanges();
            return original;
        }

        if (index >= OUTPUT_START && index < OUTPUT_START + OUTPUT_COUNT) {
            if (!player.level().isClientSide) {
                while (this.craftContainer.getItem(INPUT_SLOT).getCount() > 0) {
                    completeDisassembly(player);
                }
                broadcastChanges();
            }
            return ItemStack.EMPTY;
        }

        if (index < PLAYER_START) {
            if (!this.moveItemStackTo(slotStack, PLAYER_START, PLAYER_START + PLAYER_COUNT, true)) {
                return ItemStack.EMPTY;
            }
        } else {
            if (slotStack.getItem() == ModItems.CHARACTER_ITEM.get()) {
                boolean moved;
                if (getMode() == MODE_ASSEMBLE) {
                    moved = this.moveItemStackTo(slotStack, GRID_START, GRID_START + GRID_COUNT, false);
                } else {
                    moved = this.moveItemStackTo(slotStack, INPUT_SLOT, INPUT_SLOT + 1, false);
                }
                if (!moved) return ItemStack.EMPTY;
            } else if (index < PLAYER_START + 27) {
                if (!this.moveItemStackTo(slotStack, PLAYER_START + 27, PLAYER_START + PLAYER_COUNT, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(slotStack, PLAYER_START, PLAYER_START + 27, false)) {
                return ItemStack.EMPTY;
            }
        }

        if (slotStack.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }
        return original;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        if (player.level().isClientSide) return;
        this.clearing = true;
        try {
            for (int i = 0; i < this.craftContainer.getContainerSize(); i++) {
                if (i == RESULT_SLOT) continue;
                if (i >= OUTPUT_START && i < OUTPUT_START + OUTPUT_COUNT) continue;
                ItemStack stack = this.craftContainer.removeItemNoUpdate(i);
                if (!stack.isEmpty()) {
                    returnToPlayer(player, stack);
                }
            }
        } finally {
            this.clearing = false;
        }
    }

    private class GridSlot extends Slot {
        GridSlot(int index, int x, int y) {
            super(CharacterCrafterMenu.this.craftContainer, index, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return stack.is(ModItems.CHARACTER_ITEM);
        }

        @Override
        public boolean isActive() {
            return getMode() == MODE_ASSEMBLE;
        }
    }

    private class InputSlot extends Slot {
        InputSlot(int index, int x, int y) {
            super(CharacterCrafterMenu.this.craftContainer, index, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return stack.is(ModItems.CHARACTER_ITEM);
        }

        @Override
        public boolean isActive() {
            return getMode() == MODE_DISASSEMBLE;
        }
    }

    private class ResultSlot extends Slot {
        ResultSlot(int index, int x, int y) {
            super(CharacterCrafterMenu.this.craftContainer, index, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }

        @Override
        public boolean mayPickup(Player player) {
            return getMode() == MODE_ASSEMBLE && hasItem();
        }

        @Override
        public boolean isActive() {
            return getMode() == MODE_ASSEMBLE;
        }

        @Override
        public void onTake(Player player, ItemStack stack) {
            if (!player.level().isClientSide) {
                consumeAssembleInputs();
                refreshResult();
                broadcastChanges();
            }
            super.onTake(player, stack);
        }
    }

    private class OutputSlot extends Slot {
        OutputSlot(int index, int x, int y) {
            super(CharacterCrafterMenu.this.craftContainer, index, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }

        @Override
        public boolean mayPickup(Player player) {
            return getMode() == MODE_DISASSEMBLE && hasItem();
        }

        @Override
        public boolean isActive() {
            return getMode() == MODE_DISASSEMBLE;
        }
    }
}
