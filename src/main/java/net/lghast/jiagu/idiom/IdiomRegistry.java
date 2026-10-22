package net.lghast.jiagu.idiom;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class IdiomRegistry {
    private static final Map<String, IdiomInfo> IDIOMS = new LinkedHashMap<>();

    static {
        IDIOMS.putAll(JsonIdiomLoader.loadAll());
    }

    public static Map<String, IdiomInfo> getIdioms() {
        return IDIOMS;
    }

    public static IdiomInfo getById(String id) {
        return IDIOMS.get(id);
    }

    public static String getIdByCharacters(String characters) {
        for (Map.Entry<String, IdiomInfo> entry : IDIOMS.entrySet()) {
            if (entry.getValue().characters().equals(characters)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static boolean isValid(String characters) {
        return getIdByCharacters(characters) != null;
    }

    public static List<Map.Entry<String, IdiomInfo>> sortedById() {
        List<Map.Entry<String, IdiomInfo>> list = new ArrayList<>(IDIOMS.entrySet());
        list.sort(Map.Entry.comparingByKey());
        return list;
    }

    public static ItemStack getIconStack(String iconId) {
        Item item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(iconId));
        return item == Items.AIR ? ItemStack.EMPTY : new ItemStack(item);
    }
}
