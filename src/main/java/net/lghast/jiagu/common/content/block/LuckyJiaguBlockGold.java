package net.lghast.jiagu.common.content.block;

import net.lghast.jiagu.character.CharacterInfo;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.NotNull;

public class LuckyJiaguBlockGold extends LuckyJiaguBlock {
    public LuckyJiaguBlockGold(Properties properties) {
        super(properties);
    }

    @Override
    protected int getSpawnTimes(@NotNull RandomSource random) {
        float roll = random.nextFloat();
        if(roll < 0.5f){
            return 1;
        }else if(roll < 0.85f){
            return 2;
        }else{
            return 3;
        }
    }

    @Override
    protected String getRandomInscription(@NotNull RandomSource random) {
        return CharacterInfo.getLuckyInscriptionAdvanced(random);
    }
}
