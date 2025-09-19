package net.lghast.jiagu.common.block;

import net.lghast.jiagu.register.ModItems;
import net.lghast.jiagu.common.item.CharacterItem;
import net.lghast.jiagu.register.ModParticles;
import net.lghast.jiagu.utils.CharacterInfo;
import net.lghast.jiagu.utils.CharacterQuality;
import net.lghast.jiagu.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CharacterDisassemblerBlock extends Block {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    protected static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 9.0, 16.0);
    public static final BooleanProperty ACTIVATED = BooleanProperty.create("activated");

    public CharacterDisassemblerBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(ACTIVATED, false));
    }


    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean isMoving) {
        if (level.isClientSide) return;

        boolean hasRedstonePower = level.hasNeighborSignal(pos) || level.hasNeighborSignal(pos.below());
        boolean isActivated = state.getValue(ACTIVATED);

        if (hasRedstonePower && !isActivated) {
            level.setBlock(pos, state.setValue(ACTIVATED, true), Block.UPDATE_ALL);
            level.playSound(null, pos, SoundEvents.STONE_BUTTON_CLICK_ON, SoundSource.BLOCKS, 0.3F, 0.6F);
        }
        else if (!hasRedstonePower && isActivated) {
            level.setBlock(pos, state.setValue(ACTIVATED, false), Block.UPDATE_ALL);
            level.playSound(null, pos, SoundEvents.STONE_BUTTON_CLICK_OFF, SoundSource.BLOCKS, 0.3F, 0.5F);
        }
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult result) {
        if (!level.isClientSide() && level instanceof ServerLevel serverLevel) {
            ItemStack heldItem = player.getItemInHand(InteractionHand.MAIN_HAND);
            if(heldItem.is(ModItems.CHARACTER_ITEM)){
                String inscription = CharacterItem.getInscription(heldItem);
                List<String> components = CharacterInfo.getComponents(inscription);
                int count = state.getValue(ACTIVATED) ? heldItem.getCount() : 1;
                spawnDisassembledItems(player, count, components, heldItem, serverLevel, pos);
                return InteractionResult.sidedSuccess(level.isClientSide());
            }
        }
        return InteractionResult.PASS;
    }

    private void spawnDisassembledItems(Player player, int count, List<String> components, ItemStack itemToDisassemble, ServerLevel serverLevel, BlockPos pos){
        if(components != null && !components.isEmpty()) {
            for (String c : components) {
                ItemStack characterItem = new ItemStack(ModItems.CHARACTER_ITEM.get());
                characterItem.setCount(count);
                CharacterItem.setInscription(characterItem, c);
                ModUtils.spawnItemWithMotion(serverLevel, pos.getX()+0.5, pos.getY()+0.6, pos.getZ()+0.5, characterItem, false);
            }
            if(!player.isCreative()) {
                itemToDisassemble.shrink(count);
            }
            serverLevel.playSound(null, pos, getSoundEvents(CharacterItem.getInscription(itemToDisassemble)), SoundSource.BLOCKS);
            ModUtils.spawnParticlesForAll(serverLevel, ModParticles.JIAGU_PARTICLES.get(),
                    pos.getX()+0.5, pos.getY()+0.7, pos.getZ()+0.5, 0.3, 0.4, 0.3, 10, 0.2);
        }else{
            serverLevel.playSound(null, pos, SoundEvents.DISPENSER_FAIL, SoundSource.BLOCKS);
            ModUtils.spawnParticlesForAll(serverLevel, ParticleTypes.SMOKE,
                    pos.getX()+0.5, pos.getY()+0.7, pos.getZ()+0.5, 0.2, 0.4, 0.2, 8, 0.01);
        }
    }

    private SoundEvent getSoundEvents(String inscription){
        CharacterQuality quality = CharacterInfo.getQuality(inscription);
        return switch (quality){
            case STONE -> SoundEvents.STONE_BREAK;
            case IRON -> SoundEvents.CHAIN_BREAK;
            case GOLD -> SoundEvents.METAL_BREAK;
            case COPPER, RUST -> SoundEvents.COPPER_BREAK;
            case DIAMOND -> SoundEvents.CALCITE_BREAK;
        };
    }

    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, @Nullable Direction direction) {
        return true;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    @Override
    protected @NotNull VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    protected @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected @NotNull BlockState rotate(BlockState pState, Rotation pRotation) {
        return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
    }

    @Override
    protected @NotNull BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING).add(ACTIVATED);

    }

    @Override
    protected boolean isPathfindable(@NotNull BlockState pState, @NotNull PathComputationType pPathComputationType) {
        return false;
    }
}
