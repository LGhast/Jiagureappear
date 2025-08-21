package net.lghast.jiagu.common.block.entity;

import net.lghast.jiagu.common.block.CangjieMorpherBlock;
import net.lghast.jiagu.common.block.RubbingMachineBlock;
import net.lghast.jiagu.register.ModBlockEntities;
import net.lghast.jiagu.register.ModItems;
import net.lghast.jiagu.register.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;


public class RubbingMachineBlockEntity extends RandomizableContainerBlockEntity {
    private NonNullList<ItemStack> items = NonNullList.withSize(2, ItemStack.EMPTY);
    private int rubbingTicks = 0;
    private static final int RUBBING_TICKS = 6;

    public RubbingMachineBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.RUBBING_MACHINE.get(), pos, state);
    }

    public void startMorphing() {
        this.rubbingTicks = RUBBING_TICKS;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, RubbingMachineBlockEntity blockEntity) {
        if (blockEntity.rubbingTicks > 0) {
            blockEntity.rubbingTicks--;
            if (blockEntity.rubbingTicks == 0) {
                level.setBlock(pos, state.setValue(RubbingMachineBlock.CRAFTING, false), 3);
            }
        }
    }


    @Override
    protected NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    public ItemStack getItem(int slot) {
        return this.getItems().get(slot);
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        this.items = items;
    }

    @Override
    public int getContainerSize() {
        return 2;
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.rubbing_machine");
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return null;
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return (slot == 0 && !stack.is(ModTags.INVALID_TO_CHARACTERS) && !stack.has(DataComponents.CUSTOM_NAME))
                || (slot == 1 && stack.is(ModTags.RUBBING_INKS));
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.putInt("DisassemblingTicks", rubbingTicks);
        ContainerHelper.saveAllItems(tag, items, provider);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        rubbingTicks = tag.getInt("DisassemblingTicks");
        ContainerHelper.loadAllItems(tag, items, provider);
    }
}
