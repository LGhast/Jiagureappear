package net.lghast.jiagu.common.block.entity;

import net.lghast.jiagu.common.block.EruditeWenchangAltarBlock;
import net.lghast.jiagu.config.ServerConfig;
import net.lghast.jiagu.datagen.ModAdvancementProvider;
import net.lghast.jiagu.common.item.CharacterItem;
import net.lghast.jiagu.register.ModBlockEntities;
import net.lghast.jiagu.register.ModBlocks;
import net.lghast.jiagu.register.ModItems;
import net.lghast.jiagu.register.ModParticles;
import net.lghast.jiagu.utils.CharacterInfo;
import net.lghast.jiagu.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.Arrays;

public class EruditeWenchangAltarBlockEntity extends BlockEntity {
    private final ItemStack[] items = new ItemStack[4];

    public EruditeWenchangAltarBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ERUDITE_WENCHANG_ALTAR.get(), pos, state);
        Arrays.fill(items, ItemStack.EMPTY);
    }

    public ItemStack[] getItems() {
        return items;
    }

    public String getInscriptions(){
        if(level != null) {
            BlockState state = level.getBlockState(worldPosition);
            int count = state.getValue(EruditeWenchangAltarBlock.ITEM_COUNT);
            if(count!=4){
                return null;
            }
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = items.length - 1; i >= 0; i--) {
                ItemStack item = items[i];
                if (item.is(ModItems.CHARACTER_ITEM)) {
                    stringBuilder.append(CharacterItem.getInscription(item));
                } else {
                    return null;
                }
            }
            return stringBuilder.toString();
        }
        return null;
    }

    public void addItem(Level level, BlockPos pos, ItemStack item){
        dropItem(level, pos, 3);
        for(int i = items.length-1; i>0; i--){
            items[i] = items[i-1];
        }
        items[0] = item;
        setChanged();
        updateBlockState();

        if(level instanceof ServerLevel serverLevel && ServerConfig.IDIOM_LIST.get().contains(getInscriptions())){
            ModUtils.spawnParticlesForAll(serverLevel, ModParticles.JIAGU_FLOATING_PARTICLES.get(),
                    pos.getX()+0.5, pos.getY()+0.4, pos.getZ()+0.5, 0.3, 0.2, 0.3, 12, 0.005);
        }
    }

    public ItemStack removeItem() {
        ItemStack removed = ItemStack.EMPTY;
        for(int i = items.length-1; i>=0; i--){
            if(!items[i].isEmpty()){
                removed = items[i].copy();
                items[i] = ItemStack.EMPTY;
                break;
            }
        }
        setChanged();
        updateBlockState();
        return removed;
    }

    public void dropItem(Level level, BlockPos pos, int index){
        if (!items[index].isEmpty()) {
            Containers.dropItemStack(level, pos.getX()+0.5, pos.getY()+1, pos.getZ()+0.5, items[index]);
            items[index] = ItemStack.EMPTY;
            setChanged();
            updateBlockState();
        }
    }

    public void dropAllItems(Level level, BlockPos pos) {
        boolean changed = false;
        for (int i = 0; i < items.length; i++) {
            if (!items[i].isEmpty()) {
                Containers.dropItemStack(level,
                        pos.getX() + 0.5,
                        pos.getY() + 1,
                        pos.getZ() + 0.5,
                        items[i]);

                items[i] = ItemStack.EMPTY;
                changed = true;
            }
        }
        if (changed) {
            setChanged();
            if (!level.isClientSide) {
                BlockState state = level.getBlockState(pos);
                level.sendBlockUpdated(pos, state, state, Block.UPDATE_ALL);
            }
        }
    }

    private void updateBlockState() {
        if (level != null && !level.isClientSide) {
            if (level.getBlockEntity(worldPosition) != this || isRemoved()) {
                return;
            }

            BlockState state = level.getBlockState(worldPosition);
            if (!state.is(ModBlocks.ERUDITE_WENCHANG_ALTAR.get())) {
                return;
            }

            int count = (int) Arrays.stream(items).filter(stack -> !stack.isEmpty()).count();
            if (state.getValue(EruditeWenchangAltarBlock.ITEM_COUNT) != count) {
                level.setBlock(worldPosition,
                        state.setValue(EruditeWenchangAltarBlock.ITEM_COUNT, count),
                        Block.UPDATE_CLIENTS);
            }
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        ListTag itemsTag = tag.getList("Items", Tag.TAG_COMPOUND);
        for (int i = 0; i < Math.min(itemsTag.size(), 4); i++) {
            items[i] = ItemStack.parse(registries, itemsTag.getCompound(i)).orElse(ItemStack.EMPTY);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        ListTag itemsTag = new ListTag();
        for (ItemStack stack : items) {
            if (!stack.isEmpty()) {
                itemsTag.add(stack.save(registries));
            }
        }
        tag.put("Items", itemsTag);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        saveAdditional(tag, registries);
        return tag;
    }


    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider registries) {
        super.onDataPacket(net, pkt, registries);
        if (level != null && level.isClientSide) {
            loadAdditional(pkt.getTag(), registries);
        }
    }
}