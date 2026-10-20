package net.lghast.jiagu.common.content.block;

import net.lghast.jiagu.character.CharacterInfo;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.NotNull;

public class LuckyJiaguBlockBasic extends LuckyJiaguBlock {
    public LuckyJiaguBlockBasic(Properties properties) {
        super(properties);
    }

    @Override
    protected int getSpawnTimes(@NotNull RandomSource random) {
        float roll = random.nextFloat();
        if(roll < 0.7f){
            return 1;
        }else if(roll < 0.9f){
            return 2;
        }else{
            return 3;
        }
    }

    @Override
    protected String getRandomInscription(@NotNull RandomSource random) {
        return CharacterInfo.getLuckyInscription(random);
    }
}
