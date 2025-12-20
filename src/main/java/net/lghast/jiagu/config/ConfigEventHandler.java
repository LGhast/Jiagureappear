package net.lghast.jiagu.config;

import net.lghast.jiagu.JiaguReappear;
import net.lghast.jiagu.utils.lzh.CharacterInfo;
import net.lghast.jiagu.utils.PrescriptionInfo;
import net.lghast.jiagu.utils.lzh.LzhMappings;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;

@EventBusSubscriber(modid = JiaguReappear.MOD_ID, value = Dist.CLIENT)
public class ConfigEventHandler {
    @SubscribeEvent
    public static void onLoadConfig(ModConfigEvent.Loading event) {
        if (event.getConfig().getSpec() == ClientConfig.SPEC) {
            PrescriptionInfo.reloadFromConfig();
        }
        if (event.getConfig().getSpec() == ServerConfig.SPEC) {
            CharacterInfo.reloadFromConfig();
            LzhMappings.initialize();
        }
    }

    @SubscribeEvent
    public static void onReloadConfig(ModConfigEvent.Reloading event) {
        if (event.getConfig().getSpec() == ClientConfig.SPEC) {
            PrescriptionInfo.reloadFromConfig();
        }
        if (event.getConfig().getSpec() == ServerConfig.SPEC) {
            CharacterInfo.reloadFromConfig();
            LzhMappings.reloadFromConfig();
        }
    }
}
