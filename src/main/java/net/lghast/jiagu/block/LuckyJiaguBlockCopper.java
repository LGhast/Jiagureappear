package net.lghast.jiagu.block;

import net.lghast.jiagu.item.CharacterItem;
import net.lghast.jiagu.register.ModItems;
import net.lghast.jiagu.utils.CharacterInfo;
import net.lghast.jiagu.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

public class LuckyJiaguBlockCopper extends LuckyJiaguBlock {

    public LuckyJiaguBlockCopper(Properties properties) {
        super(properties);
    }

    @Override
    protected int getSpawnTimes() {
        double random = Math.random();
        if(random<0.5){
            return 1;
        }else if(random<0.8){
            return 2;
        }else{
            return 3;
        }
    }

    @Override
    protected String getRandomInscription() {
        double random = Math.random();
        if(random<0.5){
            return "simple";
        }else if(random<0.85){
            return "reversal";
        }else if(random<0.99){
            return "complex";
        }else if(random<0.995){
            return "LGhast";
        }else{
            return "Minecraft";
        }
    }
}
