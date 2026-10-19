package net.lghast.jiagu.lzh;

import net.lghast.jiagu.common.content.item.PrescriptionItem;
import net.lghast.jiagu.config.CommonConfig;
import net.lghast.jiagu.register.content.ModItems;
import net.lghast.jiagu.utils.ModUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.block.Block;

import java.util.*;

public class LzhMappings {
    public static final Map<String, String> ITEM_MAP = new HashMap<>();
    public static final Map<String, String> ENTITY_MAP = new HashMap<>();
    public static final Map<String, String> BLOCK_MAP = new HashMap<>();
    public static final Map<String, String> ENCHANTMENT_MAP = new HashMap<>();
    public static final Map<String, String> POTION_MAP = new HashMap<>();
    public static final Map<String, String> EFFECT_MAP = new HashMap<>();
    public static final Map<String, String> RECEIVER_MAP = new HashMap<>();

    private static final Map<ResourceLocation, String> ITEM_CACHE = new HashMap<>();
    private static final Map<ResourceLocation, String> ENTITY_CACHE = new HashMap<>();
    private static final Map<ResourceLocation, String> BLOCK_CACHE = new HashMap<>();
    private static final Map<ResourceLocation, String> ENCHANTMENT_CACHE = new HashMap<>();
    private static final Map<ResourceLocation, String> POTION_CACHE = new HashMap<>();
    private static final Map<ResourceLocation, String> EFFECT_CACHE = new HashMap<>();
    private static final Map<ResourceLocation, String> EGG_CACHE = new HashMap<>();
    private static final Map<ResourceLocation, ResourceLocation> RECEIVER_CACHE = new HashMap<>();

    private static boolean initialized = false;

    public static void initialize() {
        if (initialized) return;

        loadJsonMappings();
        loadConfigMappings();
        precomputeCaches();

        initialized = true;
    }

    private static void loadJsonMappings() {
        Map<JsonLzhLoader.Category, Map<String, String>> loaded = JsonLzhLoader.loadAll();
        ITEM_MAP.putAll(loaded.get(JsonLzhLoader.Category.ITEM));
        BLOCK_MAP.putAll(loaded.get(JsonLzhLoader.Category.BLOCK));
        ENTITY_MAP.putAll(loaded.get(JsonLzhLoader.Category.ENTITY));
        ENCHANTMENT_MAP.putAll(loaded.get(JsonLzhLoader.Category.ENCHANTMENT));
        EFFECT_MAP.putAll(loaded.get(JsonLzhLoader.Category.EFFECT));
        POTION_MAP.putAll(loaded.get(JsonLzhLoader.Category.POTION));

        RECEIVER_MAP.putAll(JsonReceiverLoader.loadAll());
    }

    private static void precomputeCaches() {
        ITEM_CACHE.clear();
        ENTITY_CACHE.clear();
        BLOCK_CACHE.clear();
        ENCHANTMENT_CACHE.clear();
        POTION_CACHE.clear();
        EFFECT_CACHE.clear();
        EGG_CACHE.clear();
        RECEIVER_CACHE.clear();

        for (Map.Entry<String, String> entry : ITEM_MAP.entrySet()) {
            ResourceLocation key = ResourceLocation.tryParse(entry.getKey());
            if (key != null) {
                ITEM_CACHE.put(key, entry.getValue());
                if (entry.getValue().startsWith("孳") && entry.getValue().endsWith("之卵")) {
                    EGG_CACHE.put(key, entry.getValue().replace("孳", "").replace("之卵", ""));
                }
            }
        }
        for (Map.Entry<String, String> entry : ENTITY_MAP.entrySet()) {
            ResourceLocation key = ResourceLocation.tryParse(entry.getKey());
            if (key != null) {
                ENTITY_CACHE.put(key, entry.getValue());
            }
        }
        for (Map.Entry<String, String> entry : BLOCK_MAP.entrySet()) {
            ResourceLocation key = ResourceLocation.tryParse(entry.getKey());
            if (key != null) {
                BLOCK_CACHE.put(key, entry.getValue());
            }
        }
        for (Map.Entry<String, String> entry : ENCHANTMENT_MAP.entrySet()) {
            ResourceLocation key = ResourceLocation.tryParse(entry.getKey());
            if (key != null) {
                ENCHANTMENT_CACHE.put(key, entry.getValue());
            }
        }
        for (Map.Entry<String, String> entry : POTION_MAP.entrySet()) {
            ResourceLocation key = ResourceLocation.tryParse(entry.getKey());
            if (key != null) {
                POTION_CACHE.put(key, entry.getValue());
            }
        }
        for (Map.Entry<String, String> entry : EFFECT_MAP.entrySet()) {
            ResourceLocation key = ResourceLocation.tryParse(entry.getKey());
            if (key != null) {
                EFFECT_CACHE.put(key, entry.getValue());
            }
        }
        for (Map.Entry<String, String> entry : RECEIVER_MAP.entrySet()) {
            ResourceLocation key = ResourceLocation.tryParse(entry.getKey());
            ResourceLocation receiver = ResourceLocation.tryParse(entry.getValue());
            if (key != null && receiver != null) {
                RECEIVER_CACHE.put(key, receiver);
            }
        }
    }

    private static void loadConfigMappings() {
        for (String mapping : CommonConfig.ITEM_LZH.get()) {
            parseAndPutMapping(mapping, ITEM_MAP);
        }
        for (String mapping : CommonConfig.BLOCK_LZH.get()) {
            parseAndPutMapping(mapping, BLOCK_MAP);
        }
        for (String mapping : CommonConfig.ENTITY_LZH.get()) {
            parseAndPutMapping(mapping, ENTITY_MAP);
        }
        for (String mapping : CommonConfig.ENCHANTMENT_LZH.get()) {
            parseAndPutMapping(mapping, ENCHANTMENT_MAP);
        }
        for (String mapping : CommonConfig.EFFECT_LZH.get()) {
            parseAndPutMapping(mapping, EFFECT_MAP);
        }
        for (String mapping : CommonConfig.POTION_LZH.get()) {
            parseAndPutMapping(mapping, POTION_MAP);
        }
        for (String mapping : CommonConfig.RECEIVER_MAPPINGS.get()) {
            parseAndPutMapping(mapping, RECEIVER_MAP);
        }
    }

    private static void parseAndPutMapping(String mapping, Map<String, String> targetMap) {
        if (mapping == null || mapping.trim().isEmpty()) return;

        String[] parts = mapping.split(",", 2);
        if (parts.length == 2) {
            String key = parts[0].trim();
            String value = parts[1].trim();
            if (!key.isEmpty() && !value.isEmpty()) {
                targetMap.put(key, value);
            }
        }
    }

    public static void reloadFromConfig() {
        initialized = false;

        ITEM_MAP.clear();
        ENTITY_MAP.clear();
        BLOCK_MAP.clear();
        ENCHANTMENT_MAP.clear();
        POTION_MAP.clear();
        EFFECT_MAP.clear();
        RECEIVER_MAP.clear();

        initialize();
    }

    private static void ensureInitialized() {
        if (!initialized) {
            initialize();
        }
    }

    public static String getLzhName(ItemStack stack) {
        ensureInitialized();
        if (stack.isEmpty()) {
            return null;
        }

        if (isPotion(stack)) {
            return getPotionLzhName(stack);
        }

        ResourceLocation itemKey = BuiltInRegistries.ITEM.getKey(stack.getItem());
        return ITEM_CACHE.get(itemKey);
    }

    private static String getPotionLzhName(ItemStack stack) {
        ensureInitialized();
        PotionContents potionContents = stack.getComponents().get(DataComponents.POTION_CONTENTS);
        if (potionContents == null) return null;
        Optional<Holder<Potion>> potionHolder = potionContents.potion();
        if (potionHolder.isEmpty()) return null;

        Potion potion = potionHolder.get().value();

        ResourceLocation potionKey = BuiltInRegistries.POTION.getKey(potion);
        if (potionKey != null) {
            String name = POTION_CACHE.get(potionKey);
            if (name == null) return null;

            if (stack.is(Items.SPLASH_POTION)) {
                return name.replace("之", "").replace("劑", "噴劑");
            }
            if (stack.is(Items.LINGERING_POTION)) {
                return name.replace("之", "").replace("劑", "滯劑");
            }
            if (stack.is(Items.TIPPED_ARROW)) {
                return name.replace("之", "").replace("劑", "之矢");
            }
            return POTION_CACHE.get(potionKey);
        }

        return null;
    }

    private static boolean isPotion(ItemStack stack) {
        PotionContents potionContents = stack.getComponents().get(DataComponents.POTION_CONTENTS);
        if (potionContents == null) return false;
        Optional<Holder<Potion>> potionHolder = potionContents.potion();
        return potionHolder.isPresent();
    }

    public static String getLzhName(Entity entity) {
        ensureInitialized();
        EntityType<?> entityType = entity.getType();
        ResourceLocation entityKey = BuiltInRegistries.ENTITY_TYPE.getKey(entityType);

        return ENTITY_CACHE.get(entityKey);
    }

    public static String getLzhName(EntityType<?> entityType) {
        ensureInitialized();
        ResourceLocation entityKey = BuiltInRegistries.ENTITY_TYPE.getKey(entityType);

        return ENTITY_CACHE.get(entityKey);
    }

    public static String getLzhName(Block block) {
        ensureInitialized();
        ResourceLocation blockKey = BuiltInRegistries.BLOCK.getKey(block);

        return BLOCK_CACHE.get(blockKey);
    }

    public static String getLzhName(Holder<Enchantment> enchantment) {
        ensureInitialized();
        try {
            ResourceLocation enchantId = getEnchantmentId(enchantment);
            String result = ENCHANTMENT_CACHE.get(enchantId);
            if (result != null) {
                return result;
            }
        } catch (Exception ignored) {}

        return null;
    }

    public static List<String> getEnchantmentLzhNames(ItemStack stack) {
        List<String> enchantmentNames = new ArrayList<>();

        ItemEnchantments enchantments = stack.get(DataComponents.ENCHANTMENTS);
        if (enchantments != null) {
            for (var entry : enchantments.entrySet()) {
                String lzhName = getLzhName(entry.getKey());
                enchantmentNames.add(lzhName);
            }
        }

        return enchantmentNames;
    }

    public static String getLzhName(MobEffect effect) {
        ensureInitialized();
        ResourceLocation effectKey = BuiltInRegistries.MOB_EFFECT.getKey(effect);

        if (effectKey != null) {
            return EFFECT_CACHE.get(effectKey);
        }

        return null;
    }

    public static List<ItemStack> getItemStacksWith(String inscription) {
        if (inscription.length() != 1) return new ArrayList<>();

        ensureInitialized();
        List<ResourceLocation> rlList = new ArrayList<>();

        for (Map.Entry<ResourceLocation, String> entry : ITEM_CACHE.entrySet()) {
            if (entry.getValue().contains(inscription)) {
                rlList.add(entry.getKey());
            }
        }

        List<ItemStack> itemList = new ArrayList<>();

        if (!rlList.isEmpty()) {
            ModUtils.sortResourceLocation(rlList);

            for (ResourceLocation location : rlList) {
                if (location.getPath().equals("character")) continue;
                itemList.add(new ItemStack(BuiltInRegistries.ITEM.get(location)));
            }
        }

        if (!inscription.equals("劑")) {
            List<ResourceLocation> potionList = new ArrayList<>();
            for (Map.Entry<ResourceLocation, String> entry : POTION_CACHE.entrySet()) {
                if (entry.getValue().contains(inscription)) {
                    potionList.add(entry.getKey());
                }
            }
            if (!potionList.isEmpty()) {
                ModUtils.sortResourceLocation(potionList);
                for (ResourceLocation location : potionList) {
                    Potion potion = BuiltInRegistries.POTION.get(location);
                    if (potion == null) break;

                    ItemStack potionStack = new ItemStack(Items.POTION);
                    potionStack.set(DataComponents.POTION_CONTENTS,
                            new PotionContents(BuiltInRegistries.POTION.wrapAsHolder(potion)));
                    itemList.add(potionStack);
                }
            }
        }
        return itemList;
    }

    public static List<ItemStack> getSpawnEggsWith(String inscription) {
        if (inscription.length() != 1) return new ArrayList<>();

        ensureInitialized();
        List<ResourceLocation> rlList = new ArrayList<>();

        for (Map.Entry<ResourceLocation, String> entry : EGG_CACHE.entrySet()) {
            if (entry.getValue().contains(inscription)) {
                rlList.add(entry.getKey());
            }
        }

        if (rlList.isEmpty()) return new ArrayList<>();
        ModUtils.sortResourceLocation(rlList);

        List<ItemStack> itemList = new ArrayList<>();
        for (ResourceLocation location : rlList) {
            itemList.add(new ItemStack(BuiltInRegistries.ITEM.get(location)));
        }

        return itemList;
    }

    public static List<ItemStack> getPrescriptionsWith(String inscription) {
        if (inscription.length() != 1) return new ArrayList<>();

        ensureInitialized();
        List<ResourceLocation> rlList = new ArrayList<>();

        for (Map.Entry<ResourceLocation, String> entry : EFFECT_CACHE.entrySet()) {
            if (entry.getValue().contains(inscription)) {
                rlList.add(entry.getKey());
            }
        }

        if (rlList.isEmpty()) return new ArrayList<>();
        ModUtils.sortResourceLocation(rlList);

        List<ItemStack> itemList = new ArrayList<>();
        for (ResourceLocation location : rlList) {
            MobEffect effect = BuiltInRegistries.MOB_EFFECT.get(location);
            if (effect == null) continue;

            ItemStack stack = new ItemStack(ModItems.PRESCRIPTION.asItem());
            PrescriptionItem.setEffect(stack, BuiltInRegistries.MOB_EFFECT.wrapAsHolder(effect));
            itemList.add(stack);
        }

        return itemList;
    }

    public static List<ItemStack> getReceiverItemsWith(String inscription) {
        if (inscription.length() != 1) return new ArrayList<>();

        ensureInitialized();
        List<ResourceLocation> rlList = new ArrayList<>();

        for (Map.Entry<ResourceLocation, String> entry : ENCHANTMENT_CACHE.entrySet()) {
            if (entry.getValue().contains(inscription)) {
                rlList.add(entry.getKey());
            }
        }

        if (rlList.isEmpty()) return new ArrayList<>();
        ModUtils.sortResourceLocation(rlList);

        List<ItemStack> itemList = new ArrayList<>();
        for (ResourceLocation location : rlList) {
            ResourceLocation receiverLocation = RECEIVER_CACHE.get(location);
            if (receiverLocation == null) continue;

            ItemStack stack = new ItemStack(BuiltInRegistries.ITEM.get(receiverLocation));
            stack.set(DataComponents.CUSTOM_NAME, Component.literal(ENCHANTMENT_CACHE.get(location)));
            itemList.add(stack);
        }

        return itemList;
    }

    public static ItemStack getPrescriptionWith(String name) {
        ensureInitialized();

        ResourceLocation location = null;
        for (Map.Entry<ResourceLocation, String> entry : EFFECT_CACHE.entrySet()) {
            if (Objects.equals(entry.getValue(), name)) {
                location = entry.getKey();
                break;
            }
        }

        if (location == null) return ItemStack.EMPTY;

        MobEffect effect = BuiltInRegistries.MOB_EFFECT.get(location);
        if (effect == null) return ItemStack.EMPTY;

        ItemStack stack = new ItemStack(ModItems.PRESCRIPTION.asItem());
        PrescriptionItem.setEffect(stack, BuiltInRegistries.MOB_EFFECT.wrapAsHolder(effect));

        return stack;
    }

    public static ResourceLocation getEnchantmentId(Holder<Enchantment> enchantmentHolder) {
        return enchantmentHolder.unwrapKey()
                .map(ResourceKey::location)
                .orElse(null);
    }
}
