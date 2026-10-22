package net.lghast.jiagu.character;

import net.lghast.jiagu.common.content.item.CharacterItem;
import net.lghast.jiagu.config.CommonConfig;
import net.lghast.jiagu.register.content.ModItems;
import net.lghast.jiagu.utils.ModUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Central registry of jiagu inscription data loaded from JSON.
 * Provides numeric value, quality, disassembly components, assembly relations
 * and random "lucky" inscriptions.
 */
public class CharacterInfo {
    private static final Logger LOGGER = LogManager.getLogger();

    public record CharacterData(int intValue, String identifier) {}
    public record StructureData(CharacterStructure structure, List<String> components) {}

    public static final Map<String, CharacterData> CHARACTER_DATA = new HashMap<>();
    public static final Map<String, StructureData> CHARACTER_COMPONENTS = new HashMap<>();
    public static final Map<String, String> COUNTERPARTS = new HashMap<>();

    private static Map<String,String> extraComponents = new HashMap<>();
    private static Map<String,CharacterData> extraData = new HashMap<>();

    private static final List<String> inscriptionsLucky = new ArrayList<>();
    private static final List<String> inscriptionsLuckyAdvanced = new ArrayList<>();

    static {
        loadCoreData();
    }

    private static void loadCoreData() {
        JsonCharacterLoader.CharacterJsonData data = JsonCharacterLoader.loadAll();
        CHARACTER_DATA.putAll(data.characterData());
        CHARACTER_COMPONENTS.putAll(data.characterComponents());

        COUNTERPARTS.putAll(JsonCounterpartLoader.loadAll());
    }

    /**
     * Numeric value used for sorting and quality. All characters, including
     * foreign scripts, are registered in character.json; unknown ones return 0.
     */
    public static int getIntValue(String character) {
        CharacterData extraCharacterData = extraData.get(character);
        if(extraCharacterData != null){
            return extraCharacterData.intValue;
        }

        CharacterData data = CHARACTER_DATA.get(character);
        return data == null ? 0 : data.intValue();
    }

    public static String getIdentifier(String character) {
        CharacterData extraCharacterData = extraData.get(character);
        if (extraCharacterData != null){
            return extraCharacterData.identifier;
        }

        CharacterData data = CHARACTER_DATA.get(character);
        if (data == null) {
            return CharacterItem.DEFAULT_INSCRIPTION;
        }
        return data.identifier();
    }

    /**
     * Maps the numeric value to a quality tier.
     * Note: values in [4000, 6000) intentionally fall through to STONE.
     */
    public static CharacterQuality getQuality(String character){
        int value = getIntValue(character);
        if(value>=0 && value<1000){
            return CharacterQuality.STONE;
        }
        if(value<2000){
            return CharacterQuality.IRON;
        }
        if(value<3000){
            return CharacterQuality.GOLD;
        }
        if(value<4000){
            return CharacterQuality.DIAMOND;
        }
        if(value>=6000 && value<7000){
            return CharacterQuality.COPPER;
        }
        if(value>=7000){
            return CharacterQuality.RUST;
        }
        return CharacterQuality.STONE;
    }

    public static boolean canBeDisassembled(String inscription){
        return CHARACTER_COMPONENTS.containsKey(inscription);
    }

    public static List<String> getComponents(String inscription){
        String extraString = extraComponents.get(inscription);
        if(extraString != null){
            List<String> extraList = Arrays.stream(extraString.split(",")).toList();
            if(!extraList.isEmpty()){
                return extraList;
            }
        }

        StructureData data = CHARACTER_COMPONENTS.get(inscription);
        if(data !=null){
            return data.components;
        }
        return null;
    }

    public static CharacterStructure getStructure(String inscription){
        StructureData data = CHARACTER_COMPONENTS.get(inscription);
        if (data == null) return null;
        return data.structure;
    }

    /**
     * Rebuilds extra mappings from config strings.
     * Component format: "字=a,b". Data format: "字,int,identifier".
     */
    public static void reloadFromConfig() {
        Map<String, String> newComponentMap = new HashMap<>();
        List<? extends String> componentMappings = CommonConfig.CHARACTER_DISASSEMBLING_MAPPINGS.get();
        for (String mapping : componentMappings) {
            try {
                String[] parts = mapping.split("=");
                if (parts.length != 2) continue;
                if (parts[0] != null && parts[1] !=null) {
                    newComponentMap.put(parts[0], parts[1]);
                }
            } catch (Exception e) {
                LOGGER.warn("Invalid component mapping: {}", mapping);
            }
        }
        extraComponents = newComponentMap;
        Map<String, CharacterData> newDataMap = new HashMap<>();
        List<? extends String> dataMappings = CommonConfig.CHARACTER_MAPPINGS.get();
        for (String mapping : dataMappings) {
            try {
                String[] parts = mapping.split(",");
                if (parts.length != 3) continue;
                if (parts[0] != null && parts[1] !=null && parts[2] !=null) {
                    int value = Integer.parseInt(parts[1]);
                    newDataMap.put(parts[0], new CharacterData(value, parts[2]));
                }
            } catch (Exception e) {
                LOGGER.warn("Invalid character data mapping: {}", mapping);
            }
        }
        extraData = newDataMap;
    }

    public static String getCharacterAssembly(String searchValue){
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, StructureData> entry : CHARACTER_COMPONENTS.entrySet()) {
            String key = entry.getKey();
            List<String> valueList = entry.getValue().components;
            if(valueList == null) continue;
            if (valueList.contains(searchValue)) {
                result.append(key).append(" ");
            }
        }
        return ModUtils.insertLineBreaks(result.toString().trim(), 14);
    }

    public static List<ItemStack> getCharacterItemAssembly(String searchValue) {
        return CHARACTER_COMPONENTS.entrySet().stream()
                .filter(entry -> entry.getValue().components.contains(searchValue))
                .sorted(Comparator.comparing(entry -> getIntValue(entry.getKey())))
                .map(entry -> {
                    ItemStack stack = new ItemStack(ModItems.CHARACTER_ITEM.asItem());
                    CharacterItem.setInscription(stack, entry.getKey());
                    return stack;
                })
                .collect(Collectors.toList());
    }

    public static String getCharacterAssemblySimple(String searchValue){
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, StructureData> entry : CHARACTER_COMPONENTS.entrySet()) {
            String key = entry.getKey();
            if(key.length()>1) continue;
            List<String> valueList = entry.getValue().components;
            if(valueList == null) continue;
            if (valueList.contains(searchValue)) {
                result.append(key);
            }
        }
        return result.toString();
    }

    /**
     * Builds a display string such as "木+口×2".
     *
     * @return null when the character cannot be disassembled
     */
    public static String getCharacterDisassembly(String searchValue) {
        List<String> list = CharacterInfo.getComponents(searchValue);
        if (list == null || list.isEmpty()) return null;
        List<String> convertedComponents = getConvertedComponents(list);

        Map<String, Integer> componentCount = new LinkedHashMap<>();
        for (String component : convertedComponents) {
            componentCount.put(component, componentCount.getOrDefault(component, 0) + 1);
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, Integer> entry : componentCount.entrySet()) {
            String component = entry.getKey();
            int count = entry.getValue();
            if (!stringBuilder.isEmpty()) {
                stringBuilder.append("+");
            }
            stringBuilder.append(component);
            if (count > 1) {
                stringBuilder.append("×").append(count);
            }
        }

        return stringBuilder.toString();
    }

    /**
     * Replaces special component placeholders (simple/simple2/complex/reversal)
     * with their localized names; single characters pass through unchanged.
     */
    private static List<String> getConvertedComponents(List<String> list) {
        List<String> convertedComponents = new ArrayList<>();
        for (String component : list) {
            if (component.length() == 1) {
                convertedComponents.add(component);
                continue;
            }
            String finalComponent = switch (component) {
                case CharacterItem.SIMPLE_INSCRIPTION ->
                        Component.translatable("tooltip.jiagureappear.character_item_simple_simplified").getString();
                case CharacterItem.SIMPLE_INSCRIPTION_2 ->
                        Component.translatable("tooltip.jiagureappear.character_item_simple2_simplified").getString();
                case CharacterItem.COMPLEX_INSCRIPTION ->
                        Component.translatable("tooltip.jiagureappear.character_item_complex_simplified").getString();
                case CharacterItem.REVERSAL_INSCRIPTION ->
                        Component.translatable("tooltip.jiagureappear.character_item_reversal_simplified").getString();
                default -> component;
            };
            convertedComponents.add(finalComponent);
        }
        return convertedComponents;
    }

    private static void inscriptionsLuckyInitialize(){
        for (Map.Entry<String, CharacterData> entry : CHARACTER_DATA.entrySet()) {
            int value = entry.getValue().intValue;
            if(value<1000){
                String key = entry.getKey();
                inscriptionsLuckyAdvanced.add(key);
                if(!CHARACTER_COMPONENTS.containsKey(key)){
                    inscriptionsLucky.add(key);
                }
            }
        }
    }

    public static String getLuckyInscription(RandomSource random){
        if(inscriptionsLucky.isEmpty()){
            inscriptionsLuckyInitialize();
        }
        return inscriptionsLucky.get(random.nextInt(inscriptionsLucky.size()));
    }

    public static String getLuckyInscriptionAdvanced(RandomSource random){
        if(inscriptionsLuckyAdvanced.isEmpty()){
            inscriptionsLuckyInitialize();
        }
        return inscriptionsLuckyAdvanced.get(random.nextInt(inscriptionsLuckyAdvanced.size()));
    }

    public static String getLuckyInscriptionAny(RandomSource random){
        List<String> keys = new ArrayList<>(CHARACTER_COMPONENTS.keySet());
        if (keys.isEmpty()) return CharacterItem.DEFAULT_INSCRIPTION;
        return keys.get(random.nextInt(keys.size()));
    }

    public static String counterpart(String inscription){
        if(COUNTERPARTS.containsKey(inscription)){
            return COUNTERPARTS.get(inscription);
        }
        return inscription;
    }
}
