package net.lghast.jiagu.register.system;

import net.lghast.jiagu.common.system.advancement.*;
import net.neoforged.bus.api.IEventBus;

public class ModAdvancementTriggers {
    public static void register(IEventBus modEventBus){
        IdiomFormedTrigger.TRIGGER_TYPES.register(modEventBus);
        DisassembleTrigger.TRIGGER_TYPES.register(modEventBus);
        RubTrigger.TRIGGER_TYPES.register(modEventBus);
        RubEnchantmentTrigger.TRIGGER_TYPES.register(modEventBus);
        YaowangTrigger.TRIGGER_TYPES.register(modEventBus);
    }
}
