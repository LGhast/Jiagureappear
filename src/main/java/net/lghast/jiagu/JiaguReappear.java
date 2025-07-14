package net.lghast.jiagu;

import net.lghast.jiagu.item.CharacterItem;
import net.lghast.jiagu.particle.JiaguFloatingParticles;
import net.lghast.jiagu.particle.JiaguParticles;
import net.lghast.jiagu.register.*;
import net.lghast.jiagu.utils.CharacterInfo;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
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
        modEventBus.addListener(this::commonSetup);

        NeoForge.EVENT_BUS.register(this);

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);

        ModCreativeModeTabs.register(modEventBus);
        ModIngredientTypes.register(modEventBus);
        ModRecipes.register(modEventBus);
        ModSerializers.register(modEventBus);
        ModParticles.register(modEventBus);

        modEventBus.addListener(this::addCreative);
        modEventBus.addListener(this::onClientSetup);
        modEventBus.addListener(this::commonSetup);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if(event.getTabKey() == CreativeModeTabs.INGREDIENTS){
            event.accept(ModItems.BONE_LAMELLA);
            event.accept(ModItems.TURTLE_PLASTRON);
            event.accept(ModItems.YELLOW_PAPER);
            event.accept(ModItems.INFINITE_PAPYRUS);
            event.accept(ModItems.YOLIME);
        }
        if(event.getTabKey() == CreativeModeTabs.FOOD_AND_DRINKS){
            event.accept(ModItems.SOUR_BERRIES);
            event.accept(ModItems.SHADOW_BERRIES);
            event.accept(ModItems.YOLIME_BREAD);
        }
        if(event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS){
            event.accept(ModBlocks.GOLDEN_BRICKS);
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

    }



    @EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
            event.registerSpriteSet(ModParticles.JIAGU_PARTICLES.get(), JiaguParticles.Provider::new);
            event.registerSpriteSet(ModParticles.JIAGU_FLOATING_PARTICLES.get(), JiaguFloatingParticles.Provider::new);
        }
    }

    public void onClientSetup(FMLClientSetupEvent event){
        event.enqueueWork(()->{
            ResourceLocation propertyIdInscription = ResourceLocation.parse("jiagureappear:inscription");
            ItemProperties.register(ModItems.CHARACTER_ITEM.get(),
                    propertyIdInscription,
                    (stack, level, entity, seed) -> {
                        String inscription = CharacterItem.getInscription(stack);
                        return CharacterInfo.getFloatValue(inscription);
                    });

            ResourceLocation propertyIdUsing = ResourceLocation.parse("jiagureappear:using");
            ItemProperties.register(ModItems.YI_CONFLAGRANT_AMETHYST.get(),
                    propertyIdUsing,
                    (stack, level, entity, seed) ->
                       entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F
                    );

            ItemBlockRenderTypes.setRenderLayer(ModBlocks.CHARACTER_DISASSEMBLER.get(), RenderType.cutout());
        });
    }
}