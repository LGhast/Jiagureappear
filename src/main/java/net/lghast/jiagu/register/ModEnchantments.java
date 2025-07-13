package net.lghast.jiagu.register;

import net.lghast.jiagu.JiaguReappear;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.enchantment.Enchantment;

public class ModEnchantments {
    public static final ResourceKey<Enchantment> IGNITING = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(JiaguReappear.MOD_ID, "igniting"));

    public static final ResourceKey<Enchantment> CREMATION = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(JiaguReappear.MOD_ID, "cremation"));

    public static final ResourceKey<Enchantment> INQUISITIVENESS = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(JiaguReappear.MOD_ID, "inquisitiveness"));

    public static final ResourceKey<Enchantment> FLURRY = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(JiaguReappear.MOD_ID, "flurry"));

    public static final ResourceKey<Enchantment> WUTHERING = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(JiaguReappear.MOD_ID, "wuthering"));

    public static void bootstrap(BootstrapContext<Enchantment> context){
        var enchantments = context.lookup(Registries.ENCHANTMENT);
        var item = context.lookup(Registries.ITEM);

        register(context, IGNITING, Enchantment.enchantment(Enchantment.definition(
                item.getOrThrow(ModTags.IGNITING_ENCHANTABLE),
                4,
                1,
                Enchantment.constantCost(20),
                Enchantment.constantCost(50),
                4,
                EquipmentSlotGroup.MAINHAND
        )));

        register(context, CREMATION, Enchantment.enchantment(Enchantment.definition(
                item.getOrThrow(ModTags.CREMATION_ENCHANTABLE),
                3,
                3,
                Enchantment.dynamicCost(15, 8),
                Enchantment.dynamicCost(60, 8),
                4,
                EquipmentSlotGroup.MAINHAND
        )));

        register(context, INQUISITIVENESS, Enchantment.enchantment(Enchantment.definition(
                item.getOrThrow(ModTags.INQUISITIVENESS_ENCHANTABLE),
                4,
                3,
                Enchantment.dynamicCost(15, 8),
                Enchantment.dynamicCost(60, 8),
                4,
                EquipmentSlotGroup.MAINHAND
        )));

        register(context, FLURRY, Enchantment.enchantment(Enchantment.definition(
                item.getOrThrow(ModTags.FLURRY_ENCHANTABLE),
                3,
                3,
                Enchantment.dynamicCost(16, 8),
                Enchantment.dynamicCost(60, 8),
                4,
                EquipmentSlotGroup.MAINHAND
        )));

        register(context, WUTHERING, Enchantment.enchantment(Enchantment.definition(
                item.getOrThrow(ModTags.WUTHERING_ENCHANTABLE),
                3,
                3,
                Enchantment.dynamicCost(16, 8),
                Enchantment.dynamicCost(60, 8),
                4,
                EquipmentSlotGroup.MAINHAND
        )));
    }


    private static void register(BootstrapContext<Enchantment> registry, ResourceKey<Enchantment> key, Enchantment.Builder builder){
        registry.register(key, builder.build(key.location()));
    }
}
