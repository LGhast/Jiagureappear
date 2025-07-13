package net.lghast.jiagu.block;

import net.lghast.jiagu.utils.CharacterInfo;

public class LuckyJiaguBlockIron extends LuckyJiaguBlock {

    public LuckyJiaguBlockIron(Properties properties) {
        super(properties);
    }

    @Override
    protected int getSpawnTimes() {
        double random = Math.random();
        if(random<0.5){
            return 1;
        }else if(random<0.85){
            return 2;
        }else{
            return 3;
        }
    }

    @Override
    protected String getRandomInscription() {
        return CharacterInfo.getLuckyInscription();
    }
}
