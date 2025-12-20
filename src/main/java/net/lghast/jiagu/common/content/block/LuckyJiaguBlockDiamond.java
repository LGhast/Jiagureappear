package net.lghast.jiagu.common.content.block;

import net.lghast.jiagu.utils.lzh.CharacterInfo;

public class LuckyJiaguBlockDiamond extends LuckyJiaguBlock {

    public LuckyJiaguBlockDiamond(Properties properties) {
        super(properties);
    }

    @Override
    protected int getSpawnTimes() {
        double random = Math.random();
        if(random<0.5){
            return 3;
        }else if(random<0.85){
            return 4;
        }else{
            return 5;
        }
    }

    @Override
    protected String getRandomInscription() {
        return CharacterInfo.getLuckyInscriptionAdvanced();
    }
}
