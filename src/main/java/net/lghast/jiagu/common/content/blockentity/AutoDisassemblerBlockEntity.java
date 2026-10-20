package net.lghast.jiagu.common.content.blockentity;

import net.lghast.jiagu.common.content.block.AutoDisassemblerBlock;
import net.lghast.jiagu.common.system.menu.AutoDisassemblerMenu;
import net.lghast.jiagu.register.content.ModBlockEntities;
import net.lghast.jiagu.register.content.ModItems;
import net.lghast.jiagu.utils.SidedInvHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class AutoDisassemblerBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer {
    public static final int SLOT_COUNT = 3;

    private NonNullList<ItemStack> items = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);
    private int disassemblingTicks = 0;
    private static final int DISASSEMBLING_TICKS = 6;

    public AutoDisassemblerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.AUTO_DISASSEMBLER.get(), pos, state);
    }

    public void startDisassembling() {
        this.disassemblingTicks = DISASSEMBLING_TICKS;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, AutoDisassemblerBlockEntity blockEntity) {
        if (blockEntity.disassemblingTicks > 0) {
            blockEntity.disassemblingTicks--;
            if (blockEntity.disassemblingTicks == 0) {
                level.setBlock(pos, state.setValue(AutoDisassemblerBlock.CRAFTING, false), 3);
            }
            blockEntity.setChanged();
        }
    }

    @Override
    protected @NotNull NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        this.items = items;
    }

    @Override
    protected @NotNull Component getDefaultName() {
        return Component.translatable("container.auto_disassembler");
    }

    @Override
    protected @NotNull AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new AutoDisassemblerMenu(containerId, inventory, this);
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return stack.is(ModItems.CHARACTER_ITEM);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.putInt("DisassemblingTicks", disassemblingTicks);
        ContainerHelper.saveAllItems(tag, items, provider);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        disassemblingTicks = tag.getInt("DisassemblingTicks");
        ContainerHelper.loadAllItems(tag, items, provider);
    }

    @Override
    public int @NotNull [] getSlotsForFace(Direction side) {
        return new int[]{0, 1, 2};
    }

    @Override
    public int getContainerSize() {
        return SLOT_COUNT;
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack stack, @Nullable Direction direction) {
        return canPlaceItem(index, stack);
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return false;
    }

    public IItemHandler getItemHandler(@Nullable Direction side) {
        return new SidedInvHandler(this, side);
    }
}
