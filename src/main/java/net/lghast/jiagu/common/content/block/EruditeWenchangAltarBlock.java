package net.lghast.jiagu.common.content.block;

import com.mojang.serialization.MapCodec;
import net.lghast.jiagu.common.system.advancement.IdiomFormedTrigger;
import net.lghast.jiagu.common.content.blockentity.EruditeWenchangAltarBlockEntity;
import net.lghast.jiagu.register.system.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class EruditeWenchangAltarBlock extends BaseEntityBlock {
    public static final MapCodec<EruditeWenchangAltarBlock> CODEC = simpleCodec(EruditeWenchangAltarBlock::new);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final IntegerProperty ITEM_COUNT = IntegerProperty.create("item_count", 0, 4);
    private static final VoxelShape SHAPE = Block.box(1, 0, 1, 15, 13, 15);

    public EruditeWenchangAltarBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(ITEM_COUNT, 0));
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
        builder.add(ITEM_COUNT, FACING);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(ITEM_COUNT, 0).setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    private boolean isDisplayable(ItemStack itemStack){
        return itemStack.is(ModTags.WENCHANG_ALTAR_DISPLAYABLE);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult result) {
        if (level.getBlockEntity(pos) instanceof EruditeWenchangAltarBlockEntity blockEntity) {
            ItemStack heldItem = player.getMainHandItem();
            int itemCount = state.getValue(ITEM_COUNT);

            if (player.isCrouching()) {
                if (itemCount > 0) {
                    if (!level.isClientSide) {
                        blockEntity.dropAllItems(level, pos);
                    }
                    level.setBlock(pos, state.setValue(ITEM_COUNT, 0), 3);
                    return InteractionResult.sidedSuccess(level.isClientSide());
                }
                return InteractionResult.PASS;
            }

            if (heldItem.isEmpty() && itemCount > 0) {
                ItemStack topItem = blockEntity.removeItem();
                if (!topItem.isEmpty()) {
                    if (!player.addItem(topItem)) {
                        player.drop(topItem, false);
                    }
                    level.setBlock(pos, state.setValue(ITEM_COUNT, itemCount-1), 3);
                    return InteractionResult.sidedSuccess(level.isClientSide());
                }
                return InteractionResult.PASS;
            }

            if (isDisplayable(heldItem)) {
                blockEntity.addItem(level, pos, heldItem.copyWithCount(1));

                if (!player.isCreative()) {
                    heldItem.shrink(1);
                }

                level.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS);
                level.setBlock(pos, state.setValue(ITEM_COUNT, Math.min(itemCount+1,4)), 3);

                String inscriptions = blockEntity.getInscriptions();
                if (inscriptions != null && !level.isClientSide() && player instanceof ServerPlayer) {
                    IdiomFormedTrigger.TRIGGER.get().trigger((ServerPlayer) player, inscriptions);
                }

                return InteractionResult.sidedSuccess(level.isClientSide());
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof EruditeWenchangAltarBlockEntity blockEntity) {
                blockEntity.dropAllItems(level, pos);
                blockEntity.setRemoved();
            }
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new EruditeWenchangAltarBlockEntity(pos, state);
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
        if (level.getBlockEntity(pos) instanceof EruditeWenchangAltarBlockEntity blockEntity) {
            int itemCount = state.getValue(ITEM_COUNT);
            return switch (itemCount){
                case 0 -> 0;
                case 1 -> 4;
                case 2 -> 8;
                case 3 -> 12;
                default -> 16;
            };
        }
        return 0;
    }
}
