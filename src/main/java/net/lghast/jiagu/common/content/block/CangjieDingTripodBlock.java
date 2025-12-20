package net.lghast.jiagu.common.content.block;

import com.mojang.serialization.MapCodec;
import net.lghast.jiagu.common.content.item.PrescriptionItem;
import net.lghast.jiagu.common.content.item.TaoistTalismanItem;
import net.lghast.jiagu.common.system.datacomponent.Spell;
import net.lghast.jiagu.register.content.ModItems;
import net.lghast.jiagu.common.content.item.CharacterItem;
import net.lghast.jiagu.client.particle.ModParticles;
import net.lghast.jiagu.register.system.ModTags;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class CangjieDingTripodBlock extends HorizontalDirectionalBlock {
    public static final MapCodec<CangjieDingTripodBlock> CODEC = simpleCodec(CangjieDingTripodBlock::new);
    private static final VoxelShape SHAPE = Block.box(2, 0, 2, 14, 9, 14);


    public CangjieDingTripodBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult result) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        if (!(player instanceof ServerPlayer) || !(level instanceof ServerLevel serverLevel)) {
            return InteractionResult.FAIL;
        }

        ItemStack mainHandItem = player.getMainHandItem();
        ItemStack offHandItem = player.getOffhandItem();

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

        normalTransfer(serverLevel, pos, player, mainHandItem, offHandItem);

        return InteractionResult.SUCCESS;
    }

    private void normalTransfer(ServerLevel serverLevel, BlockPos pos, Player player,ItemStack mainHandItem,ItemStack offHandItem){
        String morpherResult = getMorpherResult(mainHandItem);
        if(morpherResult == null) {
            player.displayClientMessage(Component.translatable("block.jiagureappear.cangjie_ding_tripod.wrong_item"),true);
            return;
        }

        spawnCharacters(morpherResult, serverLevel, pos);

        if(!player.isCreative()) {
            consumeItems(player, mainHandItem, offHandItem);
        }
        spawnParticles(serverLevel, pos);
        serverLevel.playSound(null, pos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS);
    }

    private String getMorpherResult(ItemStack mainHandItem) {
        if (mainHandItem.is(ModItems.TAOIST_TALISMAN)) {
            Spell spell = TaoistTalismanItem.getSpell(mainHandItem);
            return spell.isEmpty() ? ModUtils.modifyName(mainHandItem) : spell.spellName();
        } else if (mainHandItem.is(ModItems.PRESCRIPTION)) {
            MobEffect effect = PrescriptionItem.getEffect(mainHandItem);
            return effect != null ? ModUtils.modifyName(effect) : ModUtils.modifyName(mainHandItem);
        } else {
            return ModUtils.modifyName(mainHandItem);
        }
    }

    private void consumeItems(Player player, ItemStack mainHandItem, ItemStack offHandItem) {
        if(mainHandItem.is(ModItems.TAOIST_TALISMAN) || mainHandItem.is(ModItems.PRESCRIPTION)) {
            mainHandItem.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
        }else {
            mainHandItem.shrink(1);
        }

        if (offHandItem.isDamageableItem()) {
            offHandItem.hurtAndBreak(5, player, EquipmentSlot.OFFHAND);
        } else if (!offHandItem.is(ModItems.INFINITE_PAPYRUS)) {
            offHandItem.shrink(1);
        }
    }

    private void enchantmentTransfer(ServerLevel serverLevel, BlockPos pos, Player player, ItemStack mainHandItem, ItemStack offHandItem){
        var componentType = EnchantmentHelper.getComponentType(mainHandItem);
        ItemEnchantments enchantments = mainHandItem.getOrDefault(componentType, ItemEnchantments.EMPTY);

        if(enchantments.isEmpty()){
            player.displayClientMessage(Component.translatable("block.jiagureappear.cangjie_ding_tripod.no_enchantment"),true);
            return;
        }

        ItemStack talisman = new ItemStack(ModItems.TAOIST_TALISMAN.get());
        boolean isFirst = true;
        for (var entry : enchantments.entrySet()) {
            Holder<Enchantment> holder = entry.getKey();
            int level = enchantments.getLevel(holder);

            String enchantName = ModUtils.modifyName(holder, level);
            if(enchantName == null) {
                if(isFirst){
                    player.displayClientMessage(Component.translatable("block.jiagureappear.cangjie_ding_tripod.wrong_item"),true);
                    return;
                }
                continue;
            }

            if(isFirst){
                TaoistTalismanItem.setSpell(talisman, holder, level);
                isFirst = false;
            }else {
                spawnCharacters(enchantName, serverLevel, pos);
            }
        }
        if(!player.isCreative()){
            consumeItemsEnchantment(player, mainHandItem, offHandItem);
        }
        ModUtils.spawnItemWithMotion(serverLevel, pos.getX()+0.5, pos.getY()+0.8, pos.getZ()+0.5, talisman, false);
        spawnParticles(serverLevel, pos);
        serverLevel.playSound(null, pos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS);
        serverLevel.playSound(null, pos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS);
    }

    private void consumeItemsEnchantment(Player player, ItemStack mainHandItem, ItemStack offHandItem) {
        offHandItem.shrink(1);

        if(mainHandItem.is(Items.ENCHANTED_BOOK)){
            ItemStack bookItem = new ItemStack(Items.BOOK);
            player.setItemInHand(InteractionHand.MAIN_HAND, bookItem);
            player.containerMenu.setRemoteSlot(player.getInventory().selected, bookItem);
        }else {
            ItemStack modifiedItem = mainHandItem.copy();
            modifiedItem.set(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
            player.setItemInHand(InteractionHand.MAIN_HAND, modifiedItem);
            player.containerMenu.setRemoteSlot(player.getInventory().selected, modifiedItem);
        }
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
        ModUtils.spawnParticles(serverLevel, ModParticles.JIAGU_PARTICLES.get(),
                pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, 0.3, 0.4, 0.3, 5, 0.1);
        ModUtils.spawnParticles(serverLevel, ParticleTypes.LAVA,
                pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, 0.2, 0.3, 0.2, 3, 0.1);
    }

    @Override
    protected @NotNull VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    protected @NotNull MapCodec<? extends HorizontalDirectionalBlock> codec() {
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
