package net.lghast.jiagu.common.content.block;

import com.mojang.serialization.MapCodec;
import net.lghast.jiagu.common.content.blockentity.RubbingMachineBlockEntity;
import net.lghast.jiagu.common.content.item.CharacterItem;
import net.lghast.jiagu.register.content.ModBlockEntities;
import net.lghast.jiagu.register.content.ModItems;
import net.lghast.jiagu.client.particle.ModParticles;
import net.lghast.jiagu.register.system.ModTags;
import net.lghast.jiagu.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.FrontAndTop;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
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
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

@ParametersAreNonnullByDefault
public class RubbingMachineBlock extends BaseEntityBlock {
    public static final MapCodec<RubbingMachineBlock> CODEC = simpleCodec(RubbingMachineBlock::new);
    public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;
    public static final BooleanProperty CRAFTING = BooleanProperty.create("crafting");
    private static final EnumProperty<FrontAndTop> ORIENTATION = BlockStateProperties.ORIENTATION;

    public RubbingMachineBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(ORIENTATION, FrontAndTop.NORTH_UP)
                        .setValue(TRIGGERED, false)
                        .setValue(CRAFTING, false)
        );
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState state) {
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
            rubbing(state, level, pos);
        }
    }

    private void rubbing(BlockState state, ServerLevel level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof RubbingMachineBlockEntity blockEntity) {
            ItemStack reference = blockEntity.getItem(0);
            ItemStack ink = blockEntity.getItem(1);

            if (!reference.is(ModTags.INVALID_TO_BE_RUBBED) && ink.is(ModTags.RUBBING_INKS)
                    && !state.getValue(CRAFTING)) {

                level.setBlock(pos, state.setValue(CRAFTING, true), 3);
                blockEntity.startMorphing();
                Direction direction = state.getValue(ORIENTATION).front();

                BlockEntity backEntity = getBackBlockEntity(level, pos, direction);
                if(!(backEntity instanceof RandomizableContainerBlockEntity chestEntity)){
                    rubbingFailure(level, pos);
                    return;
                }

                String rubbingName = ModUtils.modifyName(reference);
                if(rubbingName == null){
                    rubbingFailure(level, pos);
                    return;
                }

                Map<Character, Integer> requiredChars = countCharacters(rubbingName);
                if (requiredChars.isEmpty()) {
                    rubbingFailure(level, pos);
                    return;
                }

                Map<Character, Integer> availableChars = findAvailableCharacters(chestEntity, requiredChars.keySet());
                int maxCount = getMaxCount(requiredChars, availableChars, ink.getCount());

                if (maxCount > 0) {
                    ItemStack result = new ItemStack(reference.getItem(), maxCount);
                    Vec3 spawnPos = Vec3.atCenterOf(pos).relative(direction, 0.7);

                    DefaultDispenseItemBehavior.spawnItem(level, result, 6, direction, spawnPos);
                    level.playSound(null, pos, SoundEvents.CHISELED_BOOKSHELF_PICKUP, SoundSource.BLOCKS, 1.0F, 1.0F);
                    spawnParticles(level, pos);

                    consumeMaterials(ink, chestEntity, requiredChars, maxCount);
                    chestEntity.setChanged();
                } else {
                    rubbingFailure(level, pos);
                }
            }
        }
    }

    private BlockEntity getBackBlockEntity(ServerLevel level, BlockPos pos, Direction direction) {
        return switch (direction) {
            case DOWN -> level.getBlockEntity(pos.above());
            case UP -> level.getBlockEntity(pos.below());
            case EAST -> level.getBlockEntity(pos.west());
            case WEST -> level.getBlockEntity(pos.east());
            case SOUTH -> level.getBlockEntity(pos.north());
            case NORTH -> level.getBlockEntity(pos.south());
        };
    }

    private void rubbingFailure(ServerLevel level, BlockPos pos){
        level.playSound(null, pos, SoundEvents.LEVER_CLICK, SoundSource.BLOCKS);
        ModUtils.spawnParticles(level, ParticleTypes.SMOKE,
                pos.getX() + 0.5, pos.getY() + 0.7, pos.getZ() + 0.5,
                0.3, 0.4, 0.3, 10, 0.05);
    }

    private Map<Character, Integer> countCharacters(String text) {
        Map<Character, Integer> charCount = new HashMap<>();
        for (char c : text.toCharArray()) {
            charCount.put(c, charCount.getOrDefault(c, 0) + 1);
        }
        return charCount;
    }

    private Map<Character, Integer> findAvailableCharacters(RandomizableContainerBlockEntity chest, Set<Character> requiredChars) {
        Map<Character, Integer> availableChars = new HashMap<>();

        for (int i = 0; i < chest.getContainerSize(); i++) {
            ItemStack item = chest.getItem(i);
            if (item.is(ModItems.CHARACTER_ITEM)) {
                String inscription = CharacterItem.getInscription(item);
                if (inscription.length() == 1) {
                    char c = inscription.charAt(0);
                    if (requiredChars.contains(c)) {
                        availableChars.put(c, availableChars.getOrDefault(c, 0) + item.getCount());
                    }
                }
            }
        }

        return availableChars;
    }

    private int getMaxCount(Map<Character, Integer> required, Map<Character, Integer> available, int inkCount) {
        if (required.isEmpty() || available.isEmpty()) {
            return 0;
        }
        int finalCount = inkCount;
        for (Map.Entry<Character, Integer> entry : required.entrySet()) {
            char c = entry.getKey();
            int requiredCountPerItem = entry.getValue();
            int availableCount = available.getOrDefault(c, 0);
            int maxCount = availableCount / requiredCountPerItem;
            finalCount = Math.min(finalCount, maxCount);
        }
        return finalCount;
    }

    private void consumeMaterials(ItemStack ink, RandomizableContainerBlockEntity chest, Map<Character, Integer> requiredChars, int count) {
        ink.shrink(count);

        for (Map.Entry<Character, Integer> entry : requiredChars.entrySet()) {
            char c = entry.getKey();
            int consumption = entry.getValue() * count;

            for (int i = 0; i < chest.getContainerSize() && consumption > 0; i++) {
                ItemStack item = chest.getItem(i);
                if (item.is(ModItems.CHARACTER_ITEM)) {
                    String inscription = CharacterItem.getInscription(item);
                    if (inscription.length() == 1 && inscription.charAt(0) == c) {
                        int consumeAmount = Math.min(consumption, item.getCount());
                        item.shrink(consumeAmount);
                        consumption -= consumeAmount;
                    }
                }
            }
        }
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof RubbingMachineBlockEntity blockEntity) {
            ItemStack heldItem = player.getMainHandItem();
            ItemStack referenceSlotItem = blockEntity.getItem(0);
            ItemStack inkSlotItem = blockEntity.getItem(1);

            if (player.isShiftKeyDown()) {
                if(referenceSlotItem.isEmpty() && inkSlotItem.isEmpty()){
                    return InteractionResult.PASS;
                }
                if (level.isClientSide) {
                    return InteractionResult.SUCCESS;
                }
                if (!referenceSlotItem.isEmpty()) {
                    popOutItem(level, pos, state, referenceSlotItem);
                    blockEntity.setItem(0, ItemStack.EMPTY);
                }
                if (!inkSlotItem.isEmpty()) {
                    popOutItem(level, pos, state, inkSlotItem);
                    blockEntity.setItem(1, ItemStack.EMPTY);
                }
                return InteractionResult.SUCCESS;
            }

            if (!level.isClientSide) {
                if (inkSlotItem.isEmpty() || ModUtils.canSupplement(inkSlotItem, heldItem)) {

                    boolean inkSuccess = ModUtils.tryPutOrSupplement(
                            player,
                            heldItem,
                            inkSlotItem,
                            itemStack -> itemStack.is(ModTags.RUBBING_INKS),
                            newStack -> blockEntity.setItem(1, newStack)
                    );

                    if (inkSuccess) {
                        level.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS);
                        return InteractionResult.SUCCESS;
                    }
                }

                if (referenceSlotItem.isEmpty() || ModUtils.canSupplement(referenceSlotItem, heldItem)) {
                    boolean referenceSuccess = ModUtils.tryPutOrSupplement(
                            player,
                            heldItem,
                            referenceSlotItem,
                            itemStack -> !itemStack.is(ModTags.INVALID_TO_CHARACTERS) &&
                                    !itemStack.has(DataComponents.CUSTOM_NAME),
                            newStack -> blockEntity.setItem(0, newStack)
                    );

                    if (referenceSuccess) {
                        level.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS);
                        return InteractionResult.SUCCESS;
                    }
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

    private void spawnParticles(ServerLevel serverLevel,BlockPos pos){
        ModUtils.spawnParticles(serverLevel, ParticleTypes.SQUID_INK,
                pos.getX()+0.5, pos.getY()+1, pos.getZ()+0.5, 0.3, 0.3, 0.3, 3, 0.1);
        ModUtils.spawnParticles(serverLevel, ModParticles.JIAGU_FLOATING_PARTICLES.get(),
                pos.getX()+0.5, pos.getY()+1, pos.getZ()+0.5, 0.3, 0.3, 0.3, 5, 0.2);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        Containers.dropContentsOnDestroy(state, newState, level, pos);
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new RubbingMachineBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : createTickerHelper(type, ModBlockEntities.RUBBING_MACHINE.get(), RubbingMachineBlockEntity::serverTick);
    }

    @Override
    protected boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof RubbingMachineBlockEntity blockEntity) {
            if(!blockEntity.getItem(0).isEmpty() && !blockEntity.getItem(1).isEmpty()){
                return 15;
            }
        }
        return 0;
    }
}
