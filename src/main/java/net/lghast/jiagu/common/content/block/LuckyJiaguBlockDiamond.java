package net.lghast.jiagu.common.content.block;

import net.lghast.jiagu.character.CharacterInfo;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.NotNull;

public class LuckyJiaguBlockDiamond extends LuckyJiaguBlock {
    public LuckyJiaguBlockDiamond(Properties properties) {
        super(properties);
    }

    @Override
    protected int getSpawnTimes(@NotNull RandomSource random) {
        float roll = random.nextFloat();
        if(roll < 0.5f){
            return 3;
        }else if(roll < 0.85f){
            return 4;
        }else{
            return 5;
        }
    }

    @Override
    protected String getRandomInscription(@NotNull RandomSource random) {
        return CharacterInfo.getLuckyInscriptionAdvanced(random);
    }
}
