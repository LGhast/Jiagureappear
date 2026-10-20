package net.lghast.jiagu.common.content.block;

import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.NotNull;

public class LuckyJiaguBlockCopper extends LuckyJiaguBlock {
    public LuckyJiaguBlockCopper(Properties properties) {
        super(properties);
    }

    @Override
    protected int getSpawnTimes(@NotNull RandomSource random) {
        float roll = random.nextFloat();
        if(roll < 0.5f){
            return 1;
        }else if(roll < 0.8f){
            return 2;
        }else{
            return 3;
        }
    }

    @Override
    protected String getRandomInscription(@NotNull RandomSource random) {
        float roll = random.nextFloat();
        if(roll < 0.5f){
            return "simple";
        }else if(roll < 0.85f){
            return "reversal";
        }else if(roll < 0.99f){
            return "complex";
        }else if(roll < 0.995f){
            return "LGhast";
        }else{
            return "Minecraft";
        }
    }
}
