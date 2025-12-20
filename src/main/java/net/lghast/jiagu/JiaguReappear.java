package net.lghast.jiagu;

import net.lghast.jiagu.client.misc.ModItemProperties;
import net.lghast.jiagu.client.particle.ModParticles;
import net.lghast.jiagu.client.render.ModRenders;
import net.lghast.jiagu.common.system.advancement.IdiomFormedTrigger;
import net.lghast.jiagu.config.ClientConfig;
import net.lghast.jiagu.config.ServerConfig;
import net.lghast.jiagu.register.content.*;
import net.lghast.jiagu.register.system.*;
import net.lghast.jiagu.utils.lzh.LzhMappings;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@Mod(JiaguReappear.MOD_ID)
public class JiaguReappear
{
    public static final String MOD_ID = "jiagureappear";
    private static final Logger LOGGER = LogUtils.getLogger();

    public JiaguReappear(IEventBus modEventBus, ModContainer modContainer)
    {
        ModDataComponents.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModEntityTypes.register(modEventBus);

        ModCreativeModeTabs.register(modEventBus);
        ModIngredientTypes.register(modEventBus);
        ModRecipes.register(modEventBus);
        ModSerializers.register(modEventBus);
        ModParticles.register(modEventBus);
        ModPotions.register(modEventBus);
        ModLootModifiers.register(modEventBus);
        ModMenus.register(modEventBus);
        ModVillagers.register(modEventBus);
        ModAdvancementTriggers.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.SERVER, ServerConfig.SPEC);
        modContainer.registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);

        modEventBus.addListener(this::onClientSetup);
    }

    public void onClientSetup(FMLClientSetupEvent event){
        event.enqueueWork(()->{
            ModItemProperties.register();
            ModRenders.register();
        });
    }
}