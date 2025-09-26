package net.lghast.jiagu.common.content.block;

import com.mojang.serialization.MapCodec;
import net.lghast.jiagu.common.content.item.CharacterItem;
import net.lghast.jiagu.common.content.item.TaoistTalismanItem;
import net.lghast.jiagu.config.ServerConfig;
import net.lghast.jiagu.register.content.ModItems;
import net.lghast.jiagu.client.particle.ModParticles;
import net.lghast.jiagu.register.system.ModTags;
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

import java.util.*;

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
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        if (!(player instanceof ServerPlayer serverPlayer) || !(level instanceof ServerLevel serverLevel)) {
            return InteractionResult.FAIL;
        }

        ItemStack offHandItem = player.getOffhandItem();
        ItemStack mainHandItem = player.getMainHandItem();

        if(offHandItem.isEmpty()) {
            player.displayClientMessage(Component.translatable("block.jiagureappear.rubbing_table.wrong_item_offhand"),true);
            return InteractionResult.SUCCESS;
        }

        if (TaoistTalismanItem.getSpell(offHandItem) == null) {
            if(offHandItem.is(ModTags.INVALID_TO_BE_RUBBED)) {
                player.displayClientMessage(Component.translatable("block.jiagureappear.rubbing_table.wrong_item_mainhand"),true);
                return InteractionResult.SUCCESS;
            }
            if (!mainHandItem.is(ModTags.RUBBING_INKS)) {
                player.displayClientMessage(Component.translatable("block.jiagureappear.rubbing_table.wrong_item"), true);
                return InteractionResult.SUCCESS;
            }
        }

        if(!serverPlayer.getLanguage().equals("lzh")){
            player.displayClientMessage(Component.translatable("tips.jiagureappear.wrong_language"),true);
            return InteractionResult.SUCCESS;
        }

        if (TaoistTalismanItem.getSpell(offHandItem) == null) {
            if (ServerConfig.RUBBING_TABLE_CUSTOM_NAME_CHECK.get() && offHandItem.has(DataComponents.CUSTOM_NAME)) {
                player.displayClientMessage(Component.translatable("block.jiagureappear.rubbing_table.named_item"), true);
                return InteractionResult.SUCCESS;
            }

            normalRub(serverLevel, pos, player, offHandItem, mainHandItem);
            return InteractionResult.SUCCESS;
        }

        ItemEnchantments enchantments = offHandItem.getTagEnchantments();
        if (enchantments.isEmpty()) {
            player.displayClientMessage(Component.translatable("block.jiagureappear.rubbing_table.talisman_without_enchantment"), true);
            return InteractionResult.SUCCESS;
        }

        var entry = enchantments.entrySet().iterator().next();
        Holder<Enchantment> holder = entry.getKey();
        int enchantLevel = enchantments.getLevel(holder);

        if (!mainHandItem.supportsEnchantment(holder)) {
            player.displayClientMessage(Component.translatable("block.jiagureappear.rubbing_table.not_support_enchantment"), true);
            return InteractionResult.SUCCESS;
        }

        enchantRub(serverLevel, pos, player, offHandItem, mainHandItem, holder, enchantLevel);

        return InteractionResult.SUCCESS;
    }

    private void normalRub(ServerLevel serverLevel, BlockPos pos, Player player, ItemStack offHandItem, ItemStack mainHandItem){
        String rubbingName = offHandItem.getHoverName().getString();
        Map<Character, Integer> requiredChars = getRequiredCharacters(rubbingName);
        Map<Character, Integer> availableChars = findAvailableCharacters(player, requiredChars.keySet());

        if (hasEnoughCharacters(requiredChars, availableChars)) {
            if (!player.isCreative()) {
                consumeCharacters(player, requiredChars);
                mainHandItem.shrink(1);
            }

            ItemStack rubbedItem = new ItemStack(offHandItem.getItem(), 1);
            ModUtils.spawnItem(serverLevel, pos.getX()+0.5, pos.getY()+1.1, pos.getZ()+0.5, rubbedItem, false);
            ModUtils.spawnParticlesForAll(serverLevel, ModParticles.JIAGU_PARTICLES.get(),
                    pos.getX()+0.5, pos.getY()+1.1, pos.getZ()+0.5, 0.4, 0.5, 0.4, 10, 0.1);
            serverLevel.playSound(null, pos, SoundEvents.CHISELED_BOOKSHELF_PICKUP, SoundSource.BLOCKS, 1.0F, 1.0F);
        } else {
            player.displayClientMessage(Component.translatable("block.jiagureappear.rubbing_table.lack_characters"), true);
        }
    }

    private void enchantRub(ServerLevel serverLevel, BlockPos pos, Player player, ItemStack talisman,
                            ItemStack targetItem, Holder<Enchantment> holder, int enchantLevel) {
        String spell = TaoistTalismanItem.getSpell(talisman);
        if (spell == null) {
            return;
        }

        Map<Character, Integer> requiredChars = getRequiredCharacters(spell);
        Map<Character, Integer> availableChars = findAvailableCharacters(player, requiredChars.keySet());

        if (hasEnoughCharacters(requiredChars, availableChars)) {
            if (!player.isCreative()) {
                consumeCharacters(player, requiredChars);
                ModUtils.damage(talisman);
            }

            targetItem.enchant(holder, enchantLevel);

            ModUtils.spawnParticlesForAll(serverLevel, ModParticles.JIAGU_PARTICLES.get(),
                    pos.getX()+0.5, pos.getY()+1.1, pos.getZ()+0.5, 0.4, 0.5, 0.4, 10, 0.1);
            serverLevel.playSound(null, pos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
        } else {
            player.displayClientMessage(Component.translatable("block.jiagureappear.rubbing_table.lack_characters"), true);
        }
    }

    private Map<Character, Integer> getRequiredCharacters(String text) {
        Map<Character, Integer> charWithFrequency = new HashMap<>();
        for (char c : text.toCharArray()) {
            charWithFrequency.put(c, charWithFrequency.getOrDefault(c, 0) + 1);
        }
        return charWithFrequency;
    }

    private Map<Character, Integer> findAvailableCharacters(Player player, Set<Character> requiredChars) {
        Map<Character, Integer> availableChars = new HashMap<>();

        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack item = player.getInventory().getItem(i);
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

    private boolean hasEnoughCharacters(Map<Character, Integer> required, Map<Character, Integer> available) {
        for (Map.Entry<Character, Integer> entry : required.entrySet()) {
            char c = entry.getKey();
            int requiredCount = entry.getValue();
            int availableCount = available.getOrDefault(c, 0);

            if (availableCount < requiredCount) {
                return false;
            }
        }
        return true;
    }

    private void consumeCharacters(Player player, Map<Character, Integer> requiredChars) {
        for (Map.Entry<Character, Integer> entry : requiredChars.entrySet()) {
            char c = entry.getKey();
            int count = entry.getValue();

            for (int i = 0; i < player.getInventory().getContainerSize() && count > 0; i++) {
                ItemStack item = player.getInventory().getItem(i);
                if (item.is(ModItems.CHARACTER_ITEM)) {
                    String inscription = CharacterItem.getInscription(item);
                    if (inscription.length() == 1 && inscription.charAt(0) == c) {
                        int consumeAmount = Math.min(count, item.getCount());
                        item.shrink(consumeAmount);
                        count -= consumeAmount;
                    }
                }
            }
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
