package net.lghast.jiagu.block.entity;

import net.lghast.jiagu.block.AutoDisassemblerBlock;
import net.lghast.jiagu.register.ModBlockEntities;
import net.lghast.jiagu.register.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;


public class AutoDisassemblerBlockEntity extends RandomizableContainerBlockEntity {
    private NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);
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
        }
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        this.items = items;
    }

    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.auto_disassembler");
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return null;
    }

    // 漏斗支持
    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return slot == 0 && stack.is(ModItems.CHARACTER_ITEM);
    }
}
