package net.lghast.jiagu.common.block;

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
