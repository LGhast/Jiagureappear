package net.lghast.jiagu.register;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static final TagKey<Item> FOODS_BREAD = TagKey.create(
            Registries.ITEM,
            ResourceLocation.parse("c:foods/bread")
    );

    public static final TagKey<Item> FOODS_BERRY = TagKey.create(
            Registries.ITEM,
            ResourceLocation.parse("c:foods/berry")
    );

    public static final TagKey<Item> INVALID_TO_CHARACTERS = TagKey.create(
            Registries.ITEM,
            ResourceLocation.parse("jiagureappear:invalid_to_characters")
    );

    public static final TagKey<Item> JIAGU_LUCKY_BLOCK_ITEMS = TagKey.create(
            Registries.ITEM,
            ResourceLocation.parse("jiagureappear:jiagu_lucky_blocks")
    );

    public static final TagKey<Block> JIAGU_LUCKY_BLOCKS = TagKey.create(
            Registries.BLOCK,
            ResourceLocation.parse("jiagureappear:jiagu_lucky_blocks")
    );

    public static final TagKey<Item> RUBBING_INKS = TagKey.create(
            Registries.ITEM,
            ResourceLocation.parse("jiagureappear:rubbing_inks")
    );

    public static final TagKey<Item> JIAGU_MATERIALS = TagKey.create(
            Registries.ITEM,
            ResourceLocation.parse("jiagureappear:jiagu_materials")
    );

    public static final TagKey<Item> AMETHYST_INSCRIPTIONS = TagKey.create(
            Registries.ITEM,
            ResourceLocation.parse("jiagureappear:amethyst_inscriptions")
    );

    public static final TagKey<Item> IGNITING_ENCHANTABLE = TagKey.create(
            Registries.ITEM,
            ResourceLocation.parse("jiagureappear:igniting_enchantable")
    );

    public static final TagKey<Item> CREMATION_ENCHANTABLE = TagKey.create(
            Registries.ITEM,
            ResourceLocation.parse("jiagureappear:cremation_enchantable")
    );

    public static final TagKey<Item> INQUISITIVENESS_ENCHANTABLE = TagKey.create(
            Registries.ITEM,
            ResourceLocation.parse("jiagureappear:inquisitiveness_enchantable")
    );

    public static final TagKey<Item> FLURRY_ENCHANTABLE = TagKey.create(
            Registries.ITEM,
            ResourceLocation.parse("jiagureappear:flurry_enchantable")
    );

    public static final TagKey<Item> WUTHERING_ENCHANTABLE = TagKey.create(
            Registries.ITEM,
            ResourceLocation.parse("jiagureappear:wuthering_enchantable")
    );

    public static final TagKey<Item> WENCHANG_ALTAR_DISPLAYABLE = TagKey.create(
            Registries.ITEM,
            ResourceLocation.parse("jiagureappear:wenchang_altar_displayable")
    );
}
