package net.lghast.jiagu;

import net.lghast.jiagu.block.renderer.WenchangAltarRenderer;
import net.lghast.jiagu.data_component.Prescription;
import net.lghast.jiagu.item.CharacterItem;
import net.lghast.jiagu.item.PrescriptionItem;
import net.lghast.jiagu.particle.JiaguFloatingParticles;
import net.lghast.jiagu.particle.JiaguParticles;
import net.lghast.jiagu.register.*;
import net.lghast.jiagu.utils.CharacterInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
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

        ModDataComponents.REGISTRAR.register(modEventBus);
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
            ItemProperties.register(ModItems.YI_CURE_AMETHYST.get(),
                    propertyIdUsing,
                    (stack, level, entity, seed) ->
                            entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F
            );
            ItemProperties.register(ModItems.GU_PARASITE_AMETHYST.get(),
                    propertyIdUsing,
                    (stack, level, entity, seed) ->
                            entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F
            );

            ResourceLocation propertyIdDormant = ResourceLocation.parse("jiagureappear:dormant");
            ItemProperties.register(ModItems.YI_CURE_AMETHYST.get(),
                    propertyIdDormant,
                    (stack, level, entity, seed) ->
                            entity instanceof Player player && player.getCooldowns().isOnCooldown(stack.getItem()) ? 1.0F : 0.0F
            );

            ResourceLocation propertyIdPrescription = ResourceLocation.parse("jiagureappear:prescription");
            ItemProperties.register(ModItems.PRESCRIPTION.get(),
                    propertyIdPrescription,
                    (stack, level, entity, seed) -> {
                        Holder<MobEffect> holder = PrescriptionItem.getEffectHolder(stack);
                        return CharacterInfo.getFloatValue(holder);
                    });

            ItemBlockRenderTypes.setRenderLayer(ModBlocks.CHARACTER_DISASSEMBLER.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.WENCHANG_ALTAR.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.YAOWANG_GOURD.get(), RenderType.cutout());

            BlockEntityRenderers.register(
                    ModBlockEntities.WENCHANG_ALTAR.get(),
                    WenchangAltarRenderer::new
            );

            EntityRenderers.register(
                    ModEntityTypes.PARASITE_SPORE.get(),
                    ThrownItemRenderer::new
            );
        });
    }

    @SubscribeEvent
    public void onBrewingRecipeRegister(RegisterBrewingRecipesEvent event) {
        PotionBrewing.Builder builder = event.getBuilder();
        builder.addMix(Potions.AWKWARD, ModItems.SOUR_BERRIES.get(), ModPotions.APPETIZING);
        builder.addMix(ModPotions.APPETIZING, Items.REDSTONE, ModPotions.LONG_APPETIZING);
        builder.addMix(Potions.AWKWARD, ModItems.SHADOW_BERRIES.get(), ModPotions.DARKNESS);
        builder.addMix(ModPotions.DARKNESS, Items.REDSTONE, ModPotions.LONG_DARKNESS);
    }

}