package net.lghast.jiagu.block;

import com.mojang.serialization.MapCodec;
import net.lghast.jiagu.item.PrescriptionItem;
import net.lghast.jiagu.item.TaoistTalismanItem;
import net.lghast.jiagu.register.ModItems;
import net.lghast.jiagu.item.CharacterItem;
import net.lghast.jiagu.register.ModParticles;
import net.lghast.jiagu.register.ModTags;
import net.lghast.jiagu.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Item;
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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class CangjieDingTripodBlock extends HorizontalDirectionalBlock {
    public static final MapCodec<CangjieDingTripodBlock> CODEC = simpleCodec(CangjieDingTripodBlock::new);
    private static final VoxelShape SHAPE = Block.box(2, 0, 2, 14, 9, 14);


    public CangjieDingTripodBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult result) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        if (player instanceof ServerPlayer serverPlayer && level instanceof ServerLevel serverLevel) {
            ItemStack mainHandItem = player.getMainHandItem();
            ItemStack offHandItem = player.getOffhandItem();

            if(!serverPlayer.getLanguage().equals("lzh")){
                player.displayClientMessage(Component.translatable("tips.jiagureappear.wrong_language"),true);
                return InteractionResult.SUCCESS;
            }

            if(mainHandItem.isEmpty() || mainHandItem.is(ModTags.INVALID_TO_CHARACTERS)){
                player.displayClientMessage(Component.translatable("block.jiagureappear.cangjie_ding_tripod.wrong_item"),true);
                return InteractionResult.SUCCESS;
            }

            if(offHandItem.isEmpty()){
                player.displayClientMessage(Component.translatable("block.jiagureappear.cangjie_ding_tripod.lack_materials"),true);
                return InteractionResult.SUCCESS;
            }

            if(!offHandItem.is(ModTags.JIAGU_MATERIALS)){
                player.displayClientMessage(Component.translatable("block.jiagureappear.cangjie_ding_tripod.lack_materials"),true);
                return InteractionResult.SUCCESS;
            }

            if(offHandItem.is(ModItems.YELLOW_PAPER)){
                if(mainHandItem.is(ModItems.TAOIST_TALISMAN)){
                    player.displayClientMessage(Component.translatable("block.jiagureappear.cangjie_ding_tripod.wrong_item"),true);
                    return InteractionResult.SUCCESS;
                }

                enchantmentTransfer(serverLevel, pos, player, mainHandItem, offHandItem);
                return InteractionResult.SUCCESS;
            }

            boolean hasCustomName = mainHandItem.has(DataComponents.CUSTOM_NAME);
            if(hasCustomName){
                player.displayClientMessage(Component.translatable("block.jiagureappear.cangjie_ding_tripod.named_item"),true);
                return InteractionResult.SUCCESS;
            }
            normalTransfer(serverLevel, pos, player, mainHandItem, offHandItem);

        }
        return InteractionResult.SUCCESS;
    }

    private void normalTransfer(ServerLevel serverLevel, BlockPos pos, Player player,ItemStack mainHandItem,ItemStack offHandItem){
        String morpherResult;
        if(mainHandItem.is(ModItems.TAOIST_TALISMAN)){
            String spell = TaoistTalismanItem.getSpell(mainHandItem);
            morpherResult = Objects.equals(spell, null) ? ModUtils.getCharacters(mainHandItem) : ModUtils.getCharacters(spell);
        }else if(mainHandItem.is(ModItems.PRESCRIPTION)){
            MobEffect effect = PrescriptionItem.getEffect(mainHandItem);
            morpherResult = effect==null ? ModUtils.getCharacters(mainHandItem) : ModUtils.getCharacters(effect);
        }else{
            morpherResult = ModUtils.getCharacters(mainHandItem);
        }

        spawnCharacters(morpherResult, serverLevel, pos);
        if(!player.isCreative()) {
            mainHandItem.shrink(1);
            if (offHandItem.isDamageableItem()) {
                offHandItem.hurtAndBreak(5, player, EquipmentSlot.OFFHAND);
            } else if(!offHandItem.is(ModItems.INFINITE_PAPYRUS)){
                offHandItem.shrink(1);
            }
        }
        spawnParticles(serverLevel, pos);
        serverLevel.playSound(null, pos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS);
    }

    private void enchantmentTransfer(ServerLevel serverLevel, BlockPos pos, Player player, ItemStack mainHandItem, ItemStack offHandItem){
        ItemEnchantments enchantments = mainHandItem.getTagEnchantments();

        if(enchantments.isEmpty()){
            player.displayClientMessage(Component.translatable("block.jiagureappear.cangjie_ding_tripod.no_enchantment"),true);
            return;
        }
        ItemStack talisman = new ItemStack(ModItems.TAOIST_TALISMAN.get());
        boolean isFirst = true;
        for (var entry : enchantments.entrySet()) {
            Holder<Enchantment> holder = entry.getKey();
            int level = enchantments.getLevel(holder);
            String enchantName = ModUtils.getCharacters(holder, level);
            if(isFirst){
                TaoistTalismanItem.setSpell(talisman, enchantName);
                talisman.enchant(holder, level);
                isFirst = false;
            }else {
                spawnCharacters(enchantName, serverLevel, pos);
            }
        }
        if(!player.isCreative()){
            offHandItem.shrink(1);
            ItemStack modifiedItem = mainHandItem.copy();
            modifiedItem.set(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
            player.setItemInHand(InteractionHand.MAIN_HAND, modifiedItem);
            player.containerMenu.setRemoteSlot(player.getInventory().selected, modifiedItem);
        }
        ModUtils.spawnItemWithMotion(serverLevel, pos.getX()+0.5, pos.getY()+0.8, pos.getZ()+0.5, talisman, false);
        spawnParticles(serverLevel, pos);
        serverLevel.playSound(null, pos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS);
        serverLevel.playSound(null, pos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS);
    }

    private void spawnCharacters(String characters,ServerLevel serverLevel,BlockPos pos){
        for (int i = 0; i < characters.length(); i++) {
            char c = characters.charAt(i);
            ItemStack characterItem = new ItemStack(ModItems.CHARACTER_ITEM.get());
            CharacterItem.setInscription(characterItem, c);
            ModUtils.spawnItemWithMotion(serverLevel, pos.getX()+0.5, pos.getY()+0.65, pos.getZ()+0.5, characterItem, false);
        }
    }

    private void spawnParticles(ServerLevel serverLevel,BlockPos pos){
        ModUtils.spawnParticlesForAll(serverLevel, ModParticles.JIAGU_PARTICLES.get(),
                pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, 0.3, 0.4, 0.3, 5, 0.1);
        ModUtils.spawnParticlesForAll(serverLevel, ParticleTypes.LAVA,
                pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, 0.2, 0.3, 0.2, 3, 0.1);
    }

    @Override
    protected VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}
