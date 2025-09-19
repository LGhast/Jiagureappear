package net.lghast.jiagu.register;

import net.lghast.jiagu.JiaguReappear;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;

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

    public static final ResourceKey<Enchantment> BENEVOLENCE = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(JiaguReappear.MOD_ID, "benevolence"));

    public static final ResourceKey<Enchantment> HIPPOCRATES = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(JiaguReappear.MOD_ID, "hippocrates"));

    public static final ResourceKey<Enchantment> PANACEA = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(JiaguReappear.MOD_ID, "panacea"));

    public static final ResourceKey<Enchantment> MALADY = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(JiaguReappear.MOD_ID, "malady"));

    public static final ResourceKey<Enchantment> MASSIVENESS = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(JiaguReappear.MOD_ID, "massiveness"));

    public static final ResourceKey<Enchantment> AFTERSHOCK = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(JiaguReappear.MOD_ID, "aftershock"));

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
                4,
                3,
                Enchantment.dynamicCost(15, 8),
                Enchantment.dynamicCost(60, 8),
                4,
                EquipmentSlotGroup.MAINHAND
        )));

        register(context, INQUISITIVENESS, Enchantment.enchantment(Enchantment.definition(
                item.getOrThrow(ModTags.INQUISITIVENESS_ENCHANTABLE),
                8,
                3,
                Enchantment.dynamicCost(15, 8),
                Enchantment.dynamicCost(60, 8),
                4,
                EquipmentSlotGroup.MAINHAND
        )));

        register(context, FLURRY, Enchantment.enchantment(Enchantment.definition(
                item.getOrThrow(ModTags.FLURRY_ENCHANTABLE),
                4,
                3,
                Enchantment.dynamicCost(16, 8),
                Enchantment.dynamicCost(60, 8),
                4,
                EquipmentSlotGroup.MAINHAND
        )));

        register(context, WUTHERING, Enchantment.enchantment(Enchantment.definition(
                item.getOrThrow(ModTags.WUTHERING_ENCHANTABLE),
                4,
                3,
                Enchantment.dynamicCost(16, 8),
                Enchantment.dynamicCost(60, 8),
                4,
                EquipmentSlotGroup.MAINHAND
        )));

        register(context, BENEVOLENCE, Enchantment.enchantment(Enchantment.definition(
                item.getOrThrow(ModTags.BENEVOLENCE_ENCHANTABLE),
                4,
                2,
                Enchantment.constantCost(20),
                Enchantment.constantCost(30),
                4,
                EquipmentSlotGroup.MAINHAND
        )));

        register(context, HIPPOCRATES, Enchantment.enchantment(Enchantment.definition(
                item.getOrThrow(ModTags.HIPPOCRATES_ENCHANTABLE),
                6,
                3,
                Enchantment.dynamicCost(16, 10),
                Enchantment.dynamicCost(60, 10),
                4,
                EquipmentSlotGroup.MAINHAND
        )));

        register(context, PANACEA, Enchantment.enchantment(Enchantment.definition(
                item.getOrThrow(ModTags.PANACEA_ENCHANTABLE),
                10,
                6,
                Enchantment.dynamicCost(1, 11),
                Enchantment.dynamicCost(20, 11),
                4,
                EquipmentSlotGroup.MAINHAND
        )));

        register(context, MALADY, Enchantment.enchantment(Enchantment.definition(
                item.getOrThrow(ModTags.MALADY_ENCHANTABLE),
                6,
                3,
                Enchantment.dynamicCost(16, 10),
                Enchantment.dynamicCost(60, 10),
                4,
                EquipmentSlotGroup.MAINHAND
        )));

        register(context, MASSIVENESS, Enchantment.enchantment(Enchantment.definition(
                item.getOrThrow(ModTags.MASSIVENESS_ENCHANTABLE),
                6,
                3,
                Enchantment.dynamicCost(16, 10),
                Enchantment.dynamicCost(60, 10),
                4,
                EquipmentSlotGroup.MAINHAND
        )));

        register(context, AFTERSHOCK, Enchantment.enchantment(Enchantment.definition(
                item.getOrThrow(ModTags.AFTERSHOCK_ENCHANTABLE),
                6,
                3,
                Enchantment.dynamicCost(16, 10),
                Enchantment.dynamicCost(60, 10),
                4,
                EquipmentSlotGroup.MAINHAND
        )));
    }


    private static void register(BootstrapContext<Enchantment> registry, ResourceKey<Enchantment> key, Enchantment.Builder builder){
        registry.register(key, builder.build(key.location()));
    }
}
