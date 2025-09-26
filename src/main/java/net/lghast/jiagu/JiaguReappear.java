package net.lghast.jiagu;

import net.lghast.jiagu.client.misc.ModItemProperties;
import net.lghast.jiagu.client.particle.ModParticles;
import net.lghast.jiagu.client.render.ModRenders;
import net.lghast.jiagu.common.system.advancement.IdiomFormedTrigger;
import net.lghast.jiagu.common.content.item.CharacterItem;
import net.lghast.jiagu.common.content.item.PrescriptionItem;
import net.lghast.jiagu.client.particle.JiaguFloatingParticles;
import net.lghast.jiagu.client.particle.JiaguParticles;
import net.lghast.jiagu.client.screen.PrescriptionScreen;
import net.lghast.jiagu.config.ClientConfig;
import net.lghast.jiagu.config.ServerConfig;
import net.lghast.jiagu.register.content.*;
import net.lghast.jiagu.register.system.*;
import net.lghast.jiagu.utils.CharacterInfo;
import net.lghast.jiagu.utils.PrescriptionInfo;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

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
        IdiomFormedTrigger.TRIGGER_TYPES.register(modEventBus);

        modEventBus.addListener(this::onClientSetup);

        modContainer.registerConfig(ModConfig.Type.COMMON, CommonConfig.SPEC);
        modContainer.registerConfig(ModConfig.Type.SERVER, ServerConfig.SPEC);
        modContainer.registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);
    }

    public void onClientSetup(FMLClientSetupEvent event){
        event.enqueueWork(()->{
            ModItemProperties.register();
            ModRenders.register();
        });
    }
}