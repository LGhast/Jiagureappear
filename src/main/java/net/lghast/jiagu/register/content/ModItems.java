package net.lghast.jiagu.register.content;

import net.lghast.jiagu.JiaguReappear;
import net.lghast.jiagu.common.system.datacomponent.Prescription;
import net.lghast.jiagu.common.content.item.*;
import net.lghast.jiagu.register.system.ModDataComponents;
import net.lghast.jiagu.register.system.ModFoodProperties;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(JiaguReappear.MOD_ID);

    public static final DeferredItem<Item> INFINITE_PAPYRUS = ITEMS.register("infinite_papyrus",
            ()-> new InfinitePapyrusItem(new Item.Properties()));

    public static final DeferredItem<Item> CHARACTER_ITEM = ITEMS.register("character",
            ()-> new CharacterItem(new Item.Properties()));

    public static final DeferredItem<Item> CANGJIE_JADE_BURIN = ITEMS.register("cangjie_jade_burin",
            ()-> new CangjieJadeBurinItem(new Item.Properties()));

    public static final DeferredItem<Item> YELLOW_PAPER = ITEMS.register("yellow_paper",
            ()-> new Item(new Item.Properties()));

    public static final DeferredItem<Item> EMPTY_PRESCRIPTION = ITEMS.register("empty_prescription",
            ()-> new Item(new Item.Properties()));

    public static final DeferredItem<Item> YOLIME = ITEMS.register("yolime",
            ()-> new Item(new Item.Properties()));

    public static final DeferredItem<Item> YOLIME_BREAD = ITEMS.register("yolime_bread",
            ()-> new Item(new Item.Properties().food(ModFoodProperties.YOLIME_BREAD)));

    public static final DeferredItem<Item> SHADOW_BERRIES = ITEMS.register("shadow_berries",
            ()-> new ItemNameBlockItem(ModBlocks.SHADOW_CAVE_VINES.get(),
                    new Item.Properties().food(ModFoodProperties.SHADOW_BERRIES)));

    public static final DeferredItem<Item> SOUR_BERRIES = ITEMS.register("sour_berries",
            ()-> new ItemNameBlockItem(ModBlocks.SOUR_BERRY_BUSH.get(),
                    new Item.Properties().food(ModFoodProperties.SOUR_BERRIES)));

    public static final DeferredItem<Item> ZUCCHINI = ITEMS.register("zucchini",
            ()-> new Item(new Item.Properties().food(ModFoodProperties.ZUCCHINI)));

    public static final DeferredItem<Item> COOKED_ZUCCHINI = ITEMS.register("cooked_zucchini",
            ()-> new Item(new Item.Properties().food(ModFoodProperties.COOKED_ZUCCHINI)));

    public static final DeferredItem<Item> ZUCCHINI_SOUP = ITEMS.register("zucchini_soup",
            ()-> new Item(new Item.Properties().food(ModFoodProperties.ZUCCHINI_SOUP).stacksTo(1)));

    public static final DeferredItem<Item> TAOIST_TALISMAN = ITEMS.register("taoist_talisman",
            ()-> new TaoistTalismanItem(new Item.Properties()));

    public static final DeferredItem<PrescriptionItem> PRESCRIPTION =
            ITEMS.register("prescription", () -> new PrescriptionItem(new Item.Properties()));

    public static final DeferredItem<Item> BONE_LAMELLA = ITEMS.register("bone_lamella",
            ()-> new Item(new Item.Properties()));

    public static final DeferredItem<Item> TURTLE_PLASTRON = ITEMS.register("turtle_plastron",
            ()-> new Item(new Item.Properties()));

    public static final DeferredItem<Item> PARASITE_SPORE = ITEMS.register("parasite_spore",
            ()-> new Item(new Item.Properties()));

    public static final DeferredItem<Item> AMETHYST_UPGRADE_SMITHING_TEMPLATE = ITEMS.register("amethyst_upgrade_smithing_template",
            ()-> new Item(new Item.Properties()));

    public static final DeferredItem<Item> NI_CONVERSE_AMETHYST_DORMANT = ITEMS.register("ni_converse_amethyst_dormant",
            ()-> new Item(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> NI_CONVERSE_AMETHYST = ITEMS.register("ni_converse_amethyst",
            ()-> new NiConverseAmethystItem(new Item.Properties()));

    public static final DeferredItem<Item> JIAN_SWORD_AMETHYST = ITEMS.register("jian_sword_amethyst",
            ()-> new JianSwordAmethyst(CharacterTier.AMETHYST, new Item.Properties().rarity(Rarity.EPIC)
                    .attributes(SwordItem.createAttributes(CharacterTier.AMETHYST, 3.0F, -2.4F))));

    public static final DeferredItem<Item> YI_CONFLAGRANT_AMETHYST = ITEMS.register("yi_conflagrant_amethyst",
            ()-> new YiConflagrantAmethystItem(new Item.Properties()));

    public static final DeferredItem<Item> BIAO_GALE_AMETHYST = ITEMS.register("biao_gale_amethyst",
            ()-> new BiaoGaleAmethystItem(new Item.Properties()));

    public static final DeferredItem<Item> YI_CURE_AMETHYST = ITEMS.register("yi_cure_amethyst",
            ()-> new YiCureAmethystItem(new Item.Properties()));

    public static final DeferredItem<Item> GU_PARASITE_AMETHYST = ITEMS.register("gu_parasite_amethyst",
            ()-> new GuParasiteAmethystItem(new Item.Properties()));

    public static final DeferredItem<Item> LEI_STONES_AMETHYST = ITEMS.register("lei_stones_amethyst",
            ()-> new LeiStonesAmethystItem(new Item.Properties()));

    public static final DeferredItem<Item> DICTIONARY = ITEMS.register("dictionary",
            ()-> new DictionaryItem(new Item.Properties()));

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
