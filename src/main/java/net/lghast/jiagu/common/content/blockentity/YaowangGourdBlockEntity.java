package net.lghast.jiagu.common.content.blockentity;

import net.lghast.jiagu.common.content.item.CharacterItem;
import net.lghast.jiagu.common.system.menu.YaowangGourdMenu;
import net.lghast.jiagu.register.content.ModBlockEntities;
import net.lghast.jiagu.register.content.ModItems;
import net.lghast.jiagu.utils.lzh.LzhMappings;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class YaowangGourdBlockEntity extends BaseContainerBlockEntity {
    private static final int PRESCRIPTION_SLOT = 0;
    private static final int FIRST_CHARACTER_SLOT = 1;
    private static final int SLOT_COUNT = 7;

    private NonNullList<ItemStack> items = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);
    private String currentInscription = "";

    public YaowangGourdBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.YAOWANG_GOURD.get(), pos, state);
    }

    @Override
    protected @NotNull Component getDefaultName() {
        return Component.translatable("container.jiagureappear.yaowang_gourd");
    }

    @Override
    protected @NotNull NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        this.items = items;
        updateInscription();
    }

    @Override
    protected @NotNull AbstractContainerMenu createMenu(int containerId, Inventory playerInventory) {
        return new YaowangGourdMenu(containerId, playerInventory, this);
    }

    private void updateInscription() {
        StringBuilder sb = new StringBuilder();
        boolean valid = true;

        for (int i = FIRST_CHARACTER_SLOT; i < SLOT_COUNT; i++) {
            ItemStack stack = items.get(i);
            if (!stack.isEmpty() && stack.getItem() == ModItems.CHARACTER_ITEM.get()) {
                String inscription = CharacterItem.getInscription(stack);
                if (inscription.length() != 1) {
                    valid = false;
                    break;
                }
                sb.append(inscription);
            }
        }

        if (valid) {
            currentInscription = sb.toString();
        } else {
            currentInscription = "";
        }

        setChanged();
    }

    public String getCurrentInscription() {
        return currentInscription;
    }

    public boolean canCraftPrescription() {
        if (currentInscription.isEmpty()) return false;
        if (items.get(PRESCRIPTION_SLOT).isEmpty()) return false;
        if (items.get(PRESCRIPTION_SLOT).getItem() != ModItems.EMPTY_PRESCRIPTION.get()) return false;

        ItemStack result = LzhMappings.getPrescriptionWith(currentInscription);
        return !result.isEmpty();
    }

    public ItemStack craftPrescription() {
        if (!canCraftPrescription()) return ItemStack.EMPTY;

        ItemStack result = LzhMappings.getPrescriptionWith(currentInscription);
        if (result.isEmpty()) return ItemStack.EMPTY;

        items.get(PRESCRIPTION_SLOT).shrink(1);
        for (int i = FIRST_CHARACTER_SLOT; i < SLOT_COUNT; i++) {
            if (!items.get(i).isEmpty()) {
                items.get(i).shrink(1);
            }
        }

        updateInscription();
        setChanged();
        return result;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        super.setItem(slot, stack);
        if (slot >= FIRST_CHARACTER_SLOT && slot < SLOT_COUNT) {
            updateInscription();
        }
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        ContainerHelper.loadAllItems(tag, items, provider);
        currentInscription = tag.getString("CurrentInscription");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        ContainerHelper.saveAllItems(tag, items, provider);
        tag.putString("CurrentInscription", currentInscription);
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if (slot == PRESCRIPTION_SLOT) {
            return stack.getItem() == ModItems.EMPTY_PRESCRIPTION.get();
        } else if (slot >= FIRST_CHARACTER_SLOT && slot < SLOT_COUNT) {
            return stack.getItem() == ModItems.CHARACTER_ITEM.get();
        }
        return false;
    }

    @Override
    public int getContainerSize() {
        return SLOT_COUNT;
    }
}