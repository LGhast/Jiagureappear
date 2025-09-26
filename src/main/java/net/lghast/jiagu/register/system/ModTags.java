package net.lghast.jiagu.register.system;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
    //ITEMS
    public static final TagKey<Item> FOODS_BREAD = TagKey.create(
            Registries.ITEM,
            ResourceLocation.parse("c:foods/bread")
    );

    public static final TagKey<Item> FOODS_BERRY = TagKey.create(
            Registries.ITEM,
            ResourceLocation.parse("c:foods/berry")
    );

    public static final TagKey<Item> FOODS_VEGETABLE = TagKey.create(
            Registries.ITEM,
            ResourceLocation.parse("c:foods/vegetable")
    );

    public static final TagKey<Item> FOODS = TagKey.create(
            Registries.ITEM,
            ResourceLocation.parse("c:foods")
    );

    public static final TagKey<Item> FOODS_SOUP = TagKey.create(
            Registries.ITEM,
            ResourceLocation.parse("c:foods/soup")
    );

    public static final TagKey<Item> CROPS_ZUCCHINI = TagKey.create(
            Registries.ITEM,
            ResourceLocation.parse("c:crops/zucchini")
    );

    public static final TagKey<Item> CROPS = TagKey.create(
            Registries.ITEM,
            ResourceLocation.parse("c:crops")
    );

    public static final TagKey<Item> CONCRETES = TagKey.create(
            Registries.ITEM,
            ResourceLocation.parse("c:concretes")
    );

    public static final TagKey<Item> VILLAGER_JOB_SITES = TagKey.create(
            Registries.ITEM,
            ResourceLocation.parse("c:villager_job_sites")
    );

    public static final TagKey<Item> INVALID_TO_CHARACTERS = TagKey.create(
            Registries.ITEM,
            ResourceLocation.parse("jiagureappear:invalid_to_characters")
    );

    public static final TagKey<Item> INVALID_TO_BE_RUBBED = TagKey.create(
            Registries.ITEM,
            ResourceLocation.parse("jiagureappear:invalid_to_be_rubbed")
    );

    public static final TagKey<Item> JIAGU_LUCKY_BLOCK_ITEMS = TagKey.create(
            Registries.ITEM,
            ResourceLocation.parse("jiagureappear:jiagu_lucky_blocks")
    );

    public static final TagKey<Item> RUBBING_INKS = TagKey.create(
            Registries.ITEM,
            ResourceLocation.parse("jiagureappear:rubbing_inks")
    );

    public static final TagKey<Item> HERBS = TagKey.create(
            Registries.ITEM,
            ResourceLocation.parse("jiagureappear:herbs")
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
            ResourceLocation.parse("jiagureappear:enchantable/igniting")
    );

    public static final TagKey<Item> CREMATION_ENCHANTABLE = TagKey.create(
            Registries.ITEM,
            ResourceLocation.parse("jiagureappear:enchantable/cremation")
    );

    public static final TagKey<Item> INQUISITIVENESS_ENCHANTABLE = TagKey.create(
            Registries.ITEM,
            ResourceLocation.parse("jiagureappear:enchantable/inquisitiveness")
    );

    public static final TagKey<Item> FLURRY_ENCHANTABLE = TagKey.create(
            Registries.ITEM,
            ResourceLocation.parse("jiagureappear:enchantable/flurry")
    );

    public static final TagKey<Item> WUTHERING_ENCHANTABLE = TagKey.create(
            Registries.ITEM,
            ResourceLocation.parse("jiagureappear:enchantable/wuthering")
    );

    public static final TagKey<Item> BENEVOLENCE_ENCHANTABLE = TagKey.create(
            Registries.ITEM,
            ResourceLocation.parse("jiagureappear:enchantable/benevolence")
    );

    public static final TagKey<Item> HIPPOCRATES_ENCHANTABLE = TagKey.create(
            Registries.ITEM,
            ResourceLocation.parse("jiagureappear:enchantable/hippocrates")
    );

    public static final TagKey<Item> PANACEA_ENCHANTABLE = TagKey.create(
            Registries.ITEM,
            ResourceLocation.parse("jiagureappear:enchantable/panacea")
    );

    public static final TagKey<Item> MALADY_ENCHANTABLE = TagKey.create(
            Registries.ITEM,
            ResourceLocation.parse("jiagureappear:enchantable/malady")
    );

    public static final TagKey<Item> MASSIVENESS_ENCHANTABLE = TagKey.create(
            Registries.ITEM,
            ResourceLocation.parse("jiagureappear:enchantable/massiveness")
    );

    public static final TagKey<Item> AFTERSHOCK_ENCHANTABLE = TagKey.create(
            Registries.ITEM,
            ResourceLocation.parse("jiagureappear:enchantable/aftershock")
    );

    public static final TagKey<Item> WENCHANG_ALTAR_DISPLAYABLE = TagKey.create(
            Registries.ITEM,
            ResourceLocation.parse("jiagureappear:wenchang_altar_displayable")
    );

    public static final TagKey<Item> SHOOTABLE_STONES = TagKey.create(
            Registries.ITEM,
            ResourceLocation.parse("jiagureappear:shootable_stones")
    );

    //BLOCKS
    public static final TagKey<Block> JIAGU_LUCKY_BLOCKS = TagKey.create(
            Registries.BLOCK,
            ResourceLocation.parse("jiagureappear:jiagu_lucky_blocks")
    );

    public static final TagKey<Block> WENCHANG_ALTARS = TagKey.create(
            Registries.BLOCK,
            ResourceLocation.parse("jiagureappear:wenchang_altars")
    );

    public static final TagKey<Block> VILLAGER_JOB_SITES_BLOCK = TagKey.create(
            Registries.BLOCK,
            ResourceLocation.parse("c:villager_job_sites")
    );

    //ENTITIES
    public static final TagKey<EntityType<?>> MOBS_WITH_FUR = TagKey.create(
            Registries.ENTITY_TYPE,
            ResourceLocation.parse("jiagureappear:mobs_with_fur")
    );

    public static final TagKey<EntityType<?>> OVIS = TagKey.create(
            Registries.ENTITY_TYPE,
            ResourceLocation.parse("jiagureappear:ovis")
    );
}
