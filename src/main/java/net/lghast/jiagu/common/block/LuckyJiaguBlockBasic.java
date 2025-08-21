package net.lghast.jiagu.common.block;

import net.lghast.jiagu.utils.CharacterInfo;

public class LuckyJiaguBlockBasic extends LuckyJiaguBlock {

    public LuckyJiaguBlockBasic(Properties properties) {
        super(properties);
    }

    @Override
    protected int getSpawnTimes() {
        double random = Math.random();
        if(random<0.7){
            return 1;
        }else if(random<0.9){
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
