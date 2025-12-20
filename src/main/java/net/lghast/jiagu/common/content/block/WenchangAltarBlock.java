package net.lghast.jiagu.common.content.block;

import com.mojang.serialization.MapCodec;
import net.lghast.jiagu.common.content.blockentity.WenchangAltarBlockEntity;
import net.lghast.jiagu.common.content.item.CharacterItem;
import net.lghast.jiagu.register.content.ModItems;
import net.lghast.jiagu.register.system.ModTags;
import net.lghast.jiagu.utils.lzh.CharacterInfo;
import net.lghast.jiagu.utils.lzh.CharacterQuality;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
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
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class WenchangAltarBlock extends BaseEntityBlock {
    public static final MapCodec<WenchangAltarBlock> CODEC = simpleCodec(WenchangAltarBlock::new);
    public static final BooleanProperty HAS_ITEM = BooleanProperty.create("has_item");
    private static final VoxelShape SHAPE = Block.box(1, 0, 1, 15, 13, 15);

    public WenchangAltarBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(HAS_ITEM, false));
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HAS_ITEM);
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState state) {
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
    protected @NotNull InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult result) {
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
        if (!(level.getBlockEntity(pos) instanceof WenchangAltarBlockEntity blockEntity)) {
            return 0;
        }
        ItemStack content = blockEntity.getItem();
        if(content.is(ItemStack.EMPTY.getItem())){
            return 0;
        }
        if(!content.is(ModItems.CHARACTER_ITEM)) {
            return 15;
        }
        String inscription = CharacterItem.getInscription(content);
        CharacterQuality quality = CharacterInfo.getQuality(inscription);

        return switch (quality){
            case STONE,COPPER -> 2;
            case IRON -> 5;
            case GOLD -> 10;
            case DIAMOND,RUST -> 15;
        };
    }
}
