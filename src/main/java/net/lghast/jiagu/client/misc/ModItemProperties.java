package net.lghast.jiagu.client.misc;

import net.lghast.jiagu.common.content.item.CharacterItem;
import net.lghast.jiagu.common.content.item.PrescriptionItem;
import net.lghast.jiagu.register.content.ModItems;
import net.lghast.jiagu.utils.lzh.CharacterInfo;
import net.lghast.jiagu.utils.PrescriptionInfo;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Player;

public class ModItemProperties {
    private static void registerCoreProperties(){
        ResourceLocation propertyIdInscription = ResourceLocation.parse("jiagureappear:inscription");
        ItemProperties.register(ModItems.CHARACTER_ITEM.get(),
                propertyIdInscription,
                (stack, level, entity, seed) -> {
                    String inscription = CharacterItem.getInscription(stack);
                    return CharacterInfo.getFloatValue(inscription);
                });

        PrescriptionInfo.reloadFromConfig();
        ResourceLocation propertyIdPrescription = ResourceLocation.parse("jiagureappear:prescription");
        ItemProperties.register(ModItems.PRESCRIPTION.get(),
                propertyIdPrescription,
                (stack, level, entity, seed) -> {
                    Holder<MobEffect> holder = PrescriptionItem.getEffectHolder(stack);
                    return PrescriptionInfo.getFloatValue(holder);
                });
    }

    private static void registerUsingProperty(){
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
        ItemProperties.register(ModItems.LEI_STONES_AMETHYST.get(),
                propertyIdUsing,
                (stack, level, entity, seed) ->
                        entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F
        );
    }

    private static void registerDormantProperty(){
        ResourceLocation propertyIdDormant = ResourceLocation.parse("jiagureappear:dormant");
        ItemProperties.register(ModItems.YI_CURE_AMETHYST.get(),
                propertyIdDormant,
                (stack, level, entity, seed) ->
                        entity instanceof Player player && player.getCooldowns().isOnCooldown(stack.getItem()) ? 1.0F : 0.0F
        );
        ItemProperties.register(ModItems.GU_PARASITE_AMETHYST.get(),
                propertyIdDormant,
                (stack, level, entity, seed) ->
                        entity instanceof Player player && player.getCooldowns().isOnCooldown(stack.getItem()) ? 1.0F : 0.0F
        );
    }

    public static void register(){
        registerCoreProperties();
        registerUsingProperty();
        registerDormantProperty();
    }
}
