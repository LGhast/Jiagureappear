package net.lghast.jiagu.common.block;

import com.mojang.serialization.MapCodec;
import net.lghast.jiagu.common.item.CharacterItem;
import net.lghast.jiagu.common.item.PrescriptionItem;
import net.lghast.jiagu.common.item.TaoistTalismanItem;
import net.lghast.jiagu.register.ModEnchantments;
import net.lghast.jiagu.register.ModItems;
import net.lghast.jiagu.register.ModParticles;
import net.lghast.jiagu.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class YaowangGourdBlock extends HorizontalDirectionalBlock{
    public static final MapCodec<YaowangGourdBlock> CODEC = simpleCodec(YaowangGourdBlock::new);
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    protected static final VoxelShape SHAPE = Block.box(3.0, 0.0, 3.0, 13.0, 14.0, 13.0);

    public YaowangGourdBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }


    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if(!level.isClientSide() && player instanceof ServerPlayer serverPlayer && level instanceof ServerLevel serverLevel){
            if(!serverPlayer.getLanguage().equals("lzh")){
                player.displayClientMessage(Component.translatable("tips.jiagureappear.wrong_language"),true);
                return InteractionResult.SUCCESS;
            }

            ItemStack heldItem = player.getMainHandItem();
            if(!heldItem.is(ModItems.EMPTY_PRESCRIPTION)) {
                player.displayClientMessage(Component.translatable("block.jiagureappear.yaowang_gourd.wrong_item"),true);
                return InteractionResult.SUCCESS;
            }

            List<MobEffectInstance> effects = new ArrayList<>(player.getActiveEffects());
            if(effects.isEmpty()){
                player.displayClientMessage(Component.translatable("block.jiagureappear.yaowang_gourd.no_effect"),true);
                return InteractionResult.SUCCESS;
            }

            prescribe(serverLevel, pos, player, heldItem, effects);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    private void prescribe(ServerLevel serverLevel,  BlockPos pos, Player player, ItemStack heldItem, List<MobEffectInstance> effects){
        Holder<MobEffect> effectHolder = serverLevel.registryAccess().registryOrThrow(Registries.MOB_EFFECT)
                .getHolderOrThrow(Objects.requireNonNull(effects.getFirst().getEffect().getKey()));

        List<ItemStack> characters = new ArrayList<>();
        String prescribingName = ModUtils.getCharacters(effectHolder.value());
        int targetSize = prescribingName.length();
        for(int i=0; i<player.getInventory().getContainerSize(); i++){
            ItemStack checkedItem = player.getInventory().getItem(i);
            if(checkedItem.is(ModItems.CHARACTER_ITEM)) {
                String inscription = CharacterItem.getInscription(checkedItem);
                if(inscription.length()!=1) continue;
                if(prescribingName.contains(inscription)) {
                    characters.add(player.getInventory().getItem(i));
                    prescribingName=prescribingName.replace(inscription, "");
                }
            }
        }
        if(targetSize==characters.size()){
            if(!player.isCreative()) {
                for (ItemStack c : characters) {
                    c.shrink(1);
                }
                heldItem.shrink(1);
            }
            ItemStack prescription = new ItemStack(ModItems.PRESCRIPTION.asItem());
            PrescriptionItem.setEffect(prescription, effectHolder);
            ModUtils.spawnItem(serverLevel, pos.getX()+0.5, pos.getY()+1.1, pos.getZ()+0.5, prescription, false);

            player.removeEffect(effectHolder);

            spawnParticles(serverLevel, pos);
            serverLevel.playSound(null, pos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS);
        }else{
            player.displayClientMessage(Component.translatable("block.jiagureappear.rubbing_table.lack_characters"),true);
        }
    }


    private void spawnParticles(ServerLevel serverLevel,BlockPos pos){
        ModUtils.spawnParticlesForAll(serverLevel, ParticleTypes.HAPPY_VILLAGER,
                pos.getX()+0.5, pos.getY()+0.8, pos.getZ()+0.5, 0.3, 0.4, 0.3, 8, 0.1);
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
        builder.add(FACING);

    }
}
