package net.lghast.jiagu.common.block;

import com.mojang.serialization.MapCodec;
import net.lghast.jiagu.common.block.entity.AutoDisassemblerBlockEntity;
import net.lghast.jiagu.common.block.entity.WenchangAltarBlockEntity;
import net.lghast.jiagu.common.block.renderer.WenchangAltarRenderer;
import net.lghast.jiagu.common.item.CharacterItem;
import net.lghast.jiagu.register.ModBlockEntities;
import net.lghast.jiagu.register.ModItems;
import net.lghast.jiagu.register.ModTags;
import net.lghast.jiagu.utils.CharacterInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class WenchangAltarBlock extends BaseEntityBlock {
    public static final MapCodec<WenchangAltarBlock> CODEC = simpleCodec(WenchangAltarBlock::new);
    public static final BooleanProperty HAS_ITEM = BooleanProperty.create("has_item");
    private static final VoxelShape SHAPE = Block.box(1, 0, 1, 15, 13, 15);

    public WenchangAltarBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(HAS_ITEM, false));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HAS_ITEM);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(HAS_ITEM, false);
    }

    private boolean isDisplayable(ItemStack itemStack){
        return itemStack.is(ModTags.WENCHANG_ALTAR_DISPLAYABLE);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult result) {
        if (level.getBlockEntity(pos) instanceof WenchangAltarBlockEntity blockEntity) {
            ItemStack heldItem = player.getMainHandItem();

            if (heldItem.isEmpty()) {
                ItemStack storedItem = blockEntity.removeItem();
                if (!storedItem.isEmpty()) {
                    if (!player.addItem(storedItem)) {
                        player.drop(storedItem, false);
                    }
                    level.setBlock(pos, state.setValue(HAS_ITEM, false), 3);
                    return InteractionResult.sidedSuccess(level.isClientSide());
                }
                return InteractionResult.PASS;
            }

            if (isDisplayable(heldItem)) {
                if (blockEntity.getItem().isEmpty()) {
                    ItemStack toPlace = heldItem.copyWithCount(1);
                    blockEntity.setItem(toPlace);

                    if (!player.isCreative()) {
                        heldItem.shrink(1);
                    }

                    level.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS);

                    level.setBlock(pos, state.setValue(HAS_ITEM, true), 3);
                    return InteractionResult.sidedSuccess(level.isClientSide());
                }
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            if (level.getBlockEntity(pos) instanceof WenchangAltarBlockEntity blockEntity) {
                Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), blockEntity.getItem());
            }
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new WenchangAltarBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return null;
    }

    @Override
    protected boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof WenchangAltarBlockEntity blockEntity) {
            ItemStack content = blockEntity.getItem();
            if(content.is(ItemStack.EMPTY.getItem())){
                return 0;
            }
            if(content.is(ModItems.CHARACTER_ITEM)) {
                float value = CharacterInfo.getFloatValue(CharacterItem.getInscription(content));
                if (value < 1000) {
                    return 2;
                }
                if (value < 2000) {
                    return 5;
                }
                if (value < 3000) {
                    return 10;
                }
                if (value < 4000) {
                    return 15;
                }
                if (value < 6000) {
                    return 2;
                }
                return 15;
            }
            return 15;
        }
        return 0;
    }
}
