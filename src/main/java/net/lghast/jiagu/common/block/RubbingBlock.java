package net.lghast.jiagu.common.block;

import com.mojang.serialization.MapCodec;
import net.lghast.jiagu.common.item.CharacterItem;
import net.lghast.jiagu.common.item.TaoistTalismanItem;
import net.lghast.jiagu.config.ServerConfig;
import net.lghast.jiagu.register.ModItems;
import net.lghast.jiagu.register.ModParticles;
import net.lghast.jiagu.register.ModTags;
import net.lghast.jiagu.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.*;
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

public class RubbingBlock extends HorizontalDirectionalBlock{
    public static final MapCodec<RubbingBlock> CODEC = simpleCodec(RubbingBlock::new);
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    protected static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 16.0);

    public RubbingBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }


    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        ItemStack itemToRub = player.getOffhandItem();
        ItemStack mainHandItem = player.getMainHandItem();

        if(itemToRub.isEmpty()) {
            player.displayClientMessage(Component.translatable("block.jiagureappear.rubbing_table.wrong_item_offhand"),true);
            return InteractionResult.SUCCESS;
        }

        if(itemToRub.is(ModTags.INVALID_TO_BE_RUBBED)) {
            player.displayClientMessage(Component.translatable("block.jiagureappear.rubbing_table.wrong_item_mainhand"),true);
            return InteractionResult.SUCCESS;
        }

        if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer && level instanceof ServerLevel serverLevel) {
            if(!serverPlayer.getLanguage().equals("lzh")){
                player.displayClientMessage(Component.translatable("tips.jiagureappear.wrong_language"),true);
                return InteractionResult.SUCCESS;
            }

            if(Objects.equals(TaoistTalismanItem.getSpell(itemToRub), null)){
                if(ServerConfig.RUBBING_TABLE_CUSTOM_NAME_CHECK.get()
                        && itemToRub.has(DataComponents.CUSTOM_NAME)) {
                    player.displayClientMessage(Component.translatable("block.jiagureappear.rubbing_table.named_item"),true);
                    return InteractionResult.SUCCESS;
                }
                if(!mainHandItem.is(ModTags.RUBBING_INKS)) {
                    player.displayClientMessage(Component.translatable("block.jiagureappear.rubbing_table.wrong_item"),true);
                    return InteractionResult.SUCCESS;
                }
                normalRub(serverLevel, pos, player, itemToRub);
                return InteractionResult.SUCCESS;
            }

            ItemEnchantments enchantments = itemToRub.getTagEnchantments();
            if(enchantments.isEmpty()){
                player.displayClientMessage(Component.translatable("block.jiagureappear.rubbing_table.talisman_without_enchantment"),true);
                return InteractionResult.SUCCESS;
            }

            var entry = enchantments.entrySet().iterator().next();
            Holder<Enchantment> holder = entry.getKey();
            int enchantLevel = enchantments.getLevel(holder);
            if(!mainHandItem.supportsEnchantment(holder)){
                player.displayClientMessage(Component.translatable("block.jiagureappear.rubbing_table.not_support_enchantment"),true);
                return InteractionResult.SUCCESS;
            }

            enchantRub(serverLevel, pos, player, itemToRub, holder, enchantLevel);
        }
        return InteractionResult.SUCCESS;
    }

    private void normalRub(ServerLevel serverLevel,BlockPos pos, Player player, ItemStack itemToRub){
        List<ItemStack> characters = new ArrayList<>();
        String rubbingName = itemToRub.getHoverName().getString();
        int targetSize = rubbingName.length();
        for(int i=0; i<player.getInventory().getContainerSize(); i++){
            ItemStack checkedItem = player.getInventory().getItem(i);
            if(checkedItem.is(ModItems.CHARACTER_ITEM)) {
                String inscription = CharacterItem.getInscription(checkedItem);
                if(inscription.length()!=1) continue;
                if(rubbingName.contains(inscription)) {
                    characters.add(player.getInventory().getItem(i));
                    rubbingName=rubbingName.replace(inscription, "");
                }
            }
        }
        if(targetSize==characters.size()){
            if(!player.isCreative()) {
                for (ItemStack c : characters) {
                    c.shrink(1);
                }
                player.getMainHandItem().shrink(1);
            }
            ItemStack rubbedItem = new ItemStack(itemToRub.getItem(),1);
            ModUtils.spawnItem(serverLevel, pos.getX()+0.5, pos.getY()+1.1, pos.getZ()+0.5, rubbedItem, false);
            ModUtils.spawnParticlesForAll(serverLevel, ModParticles.JIAGU_PARTICLES.get(),
                    pos.getX()+0.5, pos.getY()+1.1, pos.getZ()+0.5, 0.4, 0.5, 0.4, 10, 0.1);

            serverLevel.playSound(null, pos, SoundEvents.CHISELED_BOOKSHELF_PICKUP, SoundSource.BLOCKS);
        }else{
            player.displayClientMessage(Component.translatable("block.jiagureappear.rubbing_table.lack_characters"),true);
        }
    }

    private void enchantRub(ServerLevel serverLevel,BlockPos pos, Player player, ItemStack itemToRub, Holder<Enchantment> holder,int enchantLevel){
        List<ItemStack> characters = new ArrayList<>();
        String rubbingName = TaoistTalismanItem.getSpell(itemToRub);;
        if(rubbingName==null) return;
        int targetSize = rubbingName.length();
        for(int i=0; i<player.getInventory().getContainerSize(); i++){
            ItemStack checkedItem = player.getInventory().getItem(i);
            if(checkedItem.is(ModItems.CHARACTER_ITEM)) {
                String inscription = CharacterItem.getInscription(checkedItem);
                if(inscription.length()!=1) continue;
                if(rubbingName.contains(inscription)) {
                    characters.add(player.getInventory().getItem(i));
                    rubbingName=rubbingName.replace(inscription, "");
                }
            }
        }
        if(targetSize==characters.size()){
            if(!player.isCreative()) {
                for (ItemStack c : characters) {
                    c.shrink(1);
                }
                ModUtils.damage(itemToRub);
            }
            player.getMainHandItem().enchant(holder, enchantLevel);

            ModUtils.spawnParticlesForAll(serverLevel, ModParticles.JIAGU_PARTICLES.get(),
                    pos.getX()+0.5, pos.getY()+1.1, pos.getZ()+0.5, 0.4, 0.5, 0.4, 10, 0.1);

            serverLevel.playSound(null, pos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS);
        }else{
            player.displayClientMessage(Component.translatable("block.jiagureappear.rubbing_table.lack_characters"),true);
        }
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
