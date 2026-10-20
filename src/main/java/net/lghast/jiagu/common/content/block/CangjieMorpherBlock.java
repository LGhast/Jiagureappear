package net.lghast.jiagu.common.content.block;

import com.mojang.serialization.MapCodec;
import net.lghast.jiagu.common.content.blockentity.CangjieMorpherBlockEntity;
import net.lghast.jiagu.common.content.item.CharacterItem;
import net.lghast.jiagu.common.content.item.PrescriptionItem;
import net.lghast.jiagu.common.content.item.TaoistTalismanItem;
import net.lghast.jiagu.common.system.datacomponent.Spell;
import net.lghast.jiagu.register.content.ModBlockEntities;
import net.lghast.jiagu.register.content.ModItems;
import net.lghast.jiagu.client.particle.ModParticles;
import net.lghast.jiagu.register.system.ModTags;
import net.lghast.jiagu.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.FrontAndTop;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
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
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class CangjieMorpherBlock extends BaseEntityBlock {
    public static final MapCodec<CangjieMorpherBlock> CODEC = simpleCodec(CangjieMorpherBlock::new);
    public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;
    public static final BooleanProperty CRAFTING = BooleanProperty.create("crafting");
    private static final EnumProperty<FrontAndTop> ORIENTATION = BlockStateProperties.ORIENTATION;

    public CangjieMorpherBlock(Properties properties) {
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
            morphing(state, level, pos);
        }
    }

    private void morphing(BlockState state, ServerLevel level, BlockPos pos) {
        if (state.getValue(CRAFTING)) return;

        if (level.getBlockEntity(pos) instanceof CangjieMorpherBlockEntity blockEntity) {
            ItemStack material0 = blockEntity.getItem(0);
            ItemStack material1 = blockEntity.getItem(1);
            if (material0.isEmpty() && material1.isEmpty()) {
                morphingFailure(level, pos);
                return;
            }

            if (isMaterialInvalid(material0) || isMaterialInvalid(material1)) {
                morphingFailure(level, pos);
                return;
            }

            boolean hasContent = false;
            boolean contentValid = true;
            for (int i = CangjieMorpherBlockEntity.CONTENT_SLOT_OFFSET;
                 i < CangjieMorpherBlockEntity.SLOT_COUNT; i++) {
                ItemStack content = blockEntity.getItem(i);
                if (content.isEmpty()) continue;
                hasContent = true;
                if (content.is(ModTags.INVALID_TO_CHARACTERS) || content.is(ModTags.JIAGU_MATERIALS)) {
                    contentValid = false;
                    break;
                }
            }

            if (!hasContent || !contentValid) {
                morphingFailure(level, pos);
                return;
            }

            boolean hasInfinitePapyrus = material0.is(ModItems.INFINITE_PAPYRUS) || material1.is(ModItems.INFINITE_PAPYRUS);
            int remainingMaterials = material0.getCount() + material1.getCount();
            boolean processed = false;
            for (int i = CangjieMorpherBlockEntity.CONTENT_SLOT_OFFSET;
                 i < CangjieMorpherBlockEntity.SLOT_COUNT; i++) {
                ItemStack content = blockEntity.getItem(i);
                if (content.isEmpty()) continue;

                String morpherResult = getMorpherResult(content);
                if (morpherResult == null) continue;

                int count = hasInfinitePapyrus ? content.getCount() : Math.min(content.getCount(), remainingMaterials);
                if (count <= 0) continue;

                spawnCharacters(level, count, morpherResult, state, pos);
                if (content.is(ModItems.PRESCRIPTION) || content.is(ModItems.TAOIST_TALISMAN)) {
                    content.hurtAndBreak(1, level, null, item -> {});
                } else {
                    content.shrink(count);
                }
                if (!hasInfinitePapyrus) {
                    remainingMaterials -= consumeMaterials(blockEntity, count);
                }
                processed = true;
            }
            if (!processed) {
                morphingFailure(level, pos);
                return;
            }

            level.setBlock(pos, state.setValue(CRAFTING, true), 3);
            blockEntity.startMorphing();
            spawnParticles(level, pos);
            level.playSound(null, pos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS);
            blockEntity.setChanged();
        }
    }

    private void morphingFailure(ServerLevel level, BlockPos pos){
        level.playSound(null, pos, SoundEvents.DISPENSER_FAIL, SoundSource.BLOCKS);
        ModUtils.spawnParticles(level, ParticleTypes.SMOKE,
                pos.getX() + 0.5, pos.getY() + 0.7, pos.getZ() + 0.5,
                0.3, 0.4, 0.3, 10, 0.05);
    }

    private String getMorpherResult(ItemStack content){
        if (content.is(ModItems.TAOIST_TALISMAN)) {
            Spell spell = TaoistTalismanItem.getSpell(content);
            return spell.isEmpty() ? ModUtils.modifyName(content) : spell.spellName();
        } else if (content.is(ModItems.PRESCRIPTION)) {
            MobEffect effect = PrescriptionItem.getEffect(content);
            return effect != null ? ModUtils.modifyName(effect) : ModUtils.modifyName(content);
        } else {
            return ModUtils.modifyName(content);
        }
    }

    private void spawnCharacters(ServerLevel level, int count, String morpherResult, BlockState state, BlockPos pos) {
        Direction direction = state.getValue(ORIENTATION).front();
        Vec3 spawnPos = Vec3.atCenterOf(pos).relative(direction, 0.7);

        for (char c : morpherResult.toCharArray()) {
            ItemStack result = new ItemStack(ModItems.CHARACTER_ITEM.get());
            result.setCount(count);
            CharacterItem.setInscription(result, c);
            DefaultDispenseItemBehavior.spawnItem(level, result, 6, direction, spawnPos);
        }
    }

    private boolean isMaterialInvalid(ItemStack material) {
        return !material.isEmpty() && (!material.is(ModTags.JIAGU_MATERIALS) || material.is(ModItems.YELLOW_PAPER));
    }

    private int consumeMaterials(CangjieMorpherBlockEntity blockEntity, int count) {
        int toConsume = count;
        for (int i = 0; i < CangjieMorpherBlockEntity.MATERIAL_SLOTS && toConsume > 0; i++) {
            ItemStack material = blockEntity.getItem(i);
            if (material.isEmpty() || material.is(ModItems.INFINITE_PAPYRUS)) continue;
            int take = Math.min(toConsume, material.getCount());
            material.shrink(take);
            toConsume -= take;
        }
        return count - toConsume;
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof CangjieMorpherBlockEntity blockEntity) {
            player.openMenu(blockEntity, buf -> buf.writeBlockPos(pos));
        }
        return InteractionResult.SUCCESS;
    }

    private void spawnParticles(ServerLevel serverLevel,BlockPos pos){
        ModUtils.spawnParticles(serverLevel, ModParticles.JIAGU_PARTICLES.get(),
                pos.getX()+0.5, pos.getY()+1, pos.getZ()+0.5, 0.3, 0.4, 0.3, 5, 0.1);
        ModUtils.spawnParticles(serverLevel, ParticleTypes.LAVA,
                pos.getX()+0.5, pos.getY()+1, pos.getZ()+0.5, 0.2, 0.3, 0.2, 3, 0.1);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        Containers.dropContentsOnDestroy(state, newState, level, pos);
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CangjieMorpherBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : createTickerHelper(type, ModBlockEntities.CANGJIE_MORPHER.get(), CangjieMorpherBlockEntity::serverTick);
    }

    @Override
    protected boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof CangjieMorpherBlockEntity blockEntity) {
            int filled = 0;
            for (int i = 0; i < CangjieMorpherBlockEntity.SLOT_COUNT; i++) {
                if (!blockEntity.getItem(i).isEmpty()) {
                    filled++;
                }
            }
            return filled * (16 / CangjieMorpherBlockEntity.SLOT_COUNT);
        }
        return 0;
    }
}
