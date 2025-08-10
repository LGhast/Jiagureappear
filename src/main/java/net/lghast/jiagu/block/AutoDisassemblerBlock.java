package net.lghast.jiagu.block;

import com.mojang.serialization.MapCodec;
import net.lghast.jiagu.block.entity.AutoDisassemblerBlockEntity;
import net.lghast.jiagu.item.CharacterItem;
import net.lghast.jiagu.register.ModBlockEntities;
import net.lghast.jiagu.register.ModItems;
import net.lghast.jiagu.register.ModParticles;
import net.lghast.jiagu.utils.CharacterInfo;
import net.lghast.jiagu.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.FrontAndTop;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CrafterBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;

public class AutoDisassemblerBlock extends BaseEntityBlock {
    public static final MapCodec<AutoDisassemblerBlock> CODEC = simpleCodec(AutoDisassemblerBlock::new);
    public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;
    public static final BooleanProperty CRAFTING = BooleanProperty.create("crafting");
    private static final EnumProperty<FrontAndTop> ORIENTATION = BlockStateProperties.ORIENTATION;

    public AutoDisassemblerBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(ORIENTATION, FrontAndTop.NORTH_UP)
                        .setValue(TRIGGERED, false)
                        .setValue(CRAFTING, false)
        );
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ORIENTATION, TRIGGERED, CRAFTING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction direction = context.getNearestLookingDirection().getOpposite();
        Direction direction1 = switch (direction) {
            case DOWN -> context.getHorizontalDirection().getOpposite();
            case UP -> context.getHorizontalDirection();
            case NORTH, SOUTH, WEST, EAST -> Direction.UP;
        };
        return this.defaultBlockState()
                .setValue(ORIENTATION, FrontAndTop.fromFrontAndTop(direction, direction1))
                .setValue(TRIGGERED, context.getLevel().hasNeighborSignal(context.getClickedPos()));
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        boolean hasSignal = level.hasNeighborSignal(pos);
        boolean isTriggered = state.getValue(TRIGGERED);

        if (hasSignal && !isTriggered) {
            level.scheduleTick(pos, this, 4);
            level.setBlock(pos, state.setValue(TRIGGERED, true), 2);
        } else if (!hasSignal && isTriggered) {
            level.setBlock(pos, state.setValue(TRIGGERED, false), 2);
        }
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(TRIGGERED)) {
            disassembleItem(state, level, pos);
        }
    }

    private void disassembleItem(BlockState state, ServerLevel level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof AutoDisassemblerBlockEntity blockEntity) {
            ItemStack content = blockEntity.getItem(0);

            if (content.is(ModItems.CHARACTER_ITEM) && !state.getValue(CRAFTING)) {
                level.setBlock(pos, state.setValue(CRAFTING, true), 3);
                blockEntity.startDisassembling();

                List<String> components = CharacterInfo.getComponents(CharacterItem.getInscription(content));
                int count = content.getCount();

                float value = CharacterInfo.getFloatValue(CharacterItem.getInscription(content));
                SoundEvent sound = getSoundEvents(value);
                level.playSound(null, pos, sound, SoundSource.BLOCKS, 1.0F, 1.0F);

                Direction direction = state.getValue(ORIENTATION).front();
                Vec3 spawnPos = Vec3.atCenterOf(pos).relative(direction, 0.7);

                if(components!=null) {
                    for (String c : components) {
                        ItemStack result = new ItemStack(ModItems.CHARACTER_ITEM.get());
                        result.setCount(count);
                        CharacterItem.setInscription(result, c);
                        DefaultDispenseItemBehavior.spawnItem(level, result, 6, direction, spawnPos);
                    }
                    ModUtils.spawnParticlesForAll(level, ModParticles.JIAGU_PARTICLES.get(),
                            pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5,
                            0.3, 0.4, 0.3, 10, 0.2);
                }else{
                    DefaultDispenseItemBehavior.spawnItem(level, content, 6, direction, spawnPos);
                    ModUtils.spawnParticlesForAll(level, ParticleTypes.SMOKE,
                            pos.getX() + 0.5, pos.getY() + 0.7, pos.getZ() + 0.5,
                            0.3, 0.4, 0.3, 10, 0.05);
                }

                blockEntity.setItem(0, ItemStack.EMPTY);
                blockEntity.setChanged();;
            }
        }
    }

    private SoundEvent getSoundEvents(float value) {
        if (value > 999 && value < 3000) return SoundEvents.CHAIN_BREAK;
        if (value > 5999) return SoundEvents.COPPER_BREAK;
        return SoundEvents.STONE_BREAK;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof AutoDisassemblerBlockEntity blockEntity) {
            ItemStack heldItem = player.getMainHandItem();
            ItemStack slotItem = blockEntity.getItem(0);

            if (player.isShiftKeyDown()) {
                if (!slotItem.isEmpty()) {
                    if(!level.isClientSide) {
                        popOutItem(level, pos, state, slotItem);
                        blockEntity.setItem(0, ItemStack.EMPTY);
                    }
                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
                return InteractionResult.PASS;
            }

            if(!level.isClientSide) {
                boolean success = ModUtils.tryPutOrSupplement(player, heldItem, slotItem,
                        itemStack -> itemStack.is(ModItems.CHARACTER_ITEM),
                        newStack -> blockEntity.setItem(0, newStack)
                );

                if(success){
                    level.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS);
                    return InteractionResult.SUCCESS;
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }

    private void popOutItem(Level level, BlockPos pos, BlockState state, ItemStack stack) {
        Direction direction = state.getValue(ORIENTATION).front();
        Vec3 spawnPos = Vec3.atCenterOf(pos).relative(direction, 0.7);
        DefaultDispenseItemBehavior.spawnItem(level, stack, 6, direction, spawnPos);
        level.playSound(null, pos, SoundEvents.DISPENSER_DISPENSE, SoundSource.BLOCKS);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        Containers.dropContentsOnDestroy(state, newState, level, pos);
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AutoDisassemblerBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : createTickerHelper(type, ModBlockEntities.AUTO_DISASSEMBLER.get(), AutoDisassemblerBlockEntity::serverTick);
    }

    @Override
    protected boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof AutoDisassemblerBlockEntity blockEntity) {
            ItemStack content = blockEntity.getItem(0);
            return content.isEmpty() ? 0 : 15;
        }
        return 0;
    }
}
