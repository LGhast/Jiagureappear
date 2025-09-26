package net.lghast.jiagu.common.content.item;

import net.lghast.jiagu.register.content.ModItems;
import net.lghast.jiagu.client.particle.ModParticles;
import net.lghast.jiagu.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class CangjieJadeBurinItem extends Item {

    public CangjieJadeBurinItem(Properties properties) {
        super(properties.rarity(Rarity.EPIC).stacksTo(1));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();

        if(level instanceof ServerLevel serverLevel){
            BlockState state = level.getBlockState(pos);
            if(state.isAir()){
                return InteractionResult.FAIL;
            }
            String blockName = state.getBlock().getName().getString();
            if(!blockName.isEmpty()){
                char[] chars = blockName.toCharArray();
                for(char inscription : chars){
                    ItemStack stack = new ItemStack(ModItems.CHARACTER_ITEM.get());
                    CharacterItem.setInscription(stack, inscription);
                    ModUtils.spawnItemWithMotion(serverLevel, pos.getX()+0.5, pos.getY()+0.2, pos.getZ()+0.5, stack, true);
                }
                level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);

                level.playSound(null, pos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
                ModUtils.spawnParticlesForAll(serverLevel, ModParticles.JIAGU_FLOATING_PARTICLES.get(),
                        pos.getX()+0.5, pos.getY()+0.2, pos.getZ()+0.5, 0.4, 0.3, 0.4, 12, 0.03);
            }
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
