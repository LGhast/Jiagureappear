package net.lghast.jiagu.block;

import net.lghast.jiagu.item.CharacterItem;
import net.lghast.jiagu.register.ModEnchantments;
import net.lghast.jiagu.register.ModItems;
import net.lghast.jiagu.utils.CharacterInfo;
import net.lghast.jiagu.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

import java.util.List;
import java.util.Random;

abstract public class LuckyJiaguBlock extends Block {
    public LuckyJiaguBlock(Properties properties) {
        super(properties);
    }

    abstract protected int getSpawnTimes();

    abstract protected String getRandomInscription();

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        if(willHarvest && level instanceof ServerLevel serverLevel){
            float times = getSpawnTimes();
            for(int i = 0; i<times ;i++) {
                ItemStack itemStack = new ItemStack(ModItems.CHARACTER_ITEM.asItem());
                CharacterItem.setInscription(itemStack, getRandomInscription());
                ModUtils.spawnItemWithMotion(serverLevel, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, itemStack, true);
            }
        }
        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }
}
