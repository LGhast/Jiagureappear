package net.lghast.jiagu.character;

import net.lghast.jiagu.common.content.item.CharacterItem;
import net.lghast.jiagu.config.CommonConfig;
import net.lghast.jiagu.register.content.ModItems;
import net.lghast.jiagu.utils.ModUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

public class CharacterInfo {
    public record CharacterData(float floatValue, String identifier) {}
    public record StructureData(CharacterStructure structure, List<String> components) {}

    public static final Map<String, CharacterData> CHARACTER_DATA = new HashMap<>();
    public static final Map<String, StructureData> CHARACTER_COMPONENTS = new HashMap<>();
    public static final Map<String, String> COUNTERPARTS = new HashMap<>();

    private static Map<String,String> extraComponents = new HashMap<>();
    private static Map<String,CharacterData> extraData = new HashMap<>();

    private static final List<String> inscriptionsLucky = new ArrayList<>();
    private static final List<String> inscriptionsLuckyAdvanced = new ArrayList<>();

    public static String LATIN_LETTERS = "ÞþÇçŒœÆæÐðßÄäÖöÜüÀàÂâÉéÈèÊêËëÎîÏïÔôÙùÛûÜüŸÿ";
    public static String GREEK_LETTERS = "ΑΒΓΔΕΖΗΘΙΚΛΜΝΞΟΠΡΣΤΥΦΧΨΩαβγδεζηθικλμνξοπρστυφχψω";
    public static String KANA = "あいうえおかきくけこさしすせそたちつてとなにぬねのはひふへほまみむめもやゆよらりるれろわをんがぎぐげござじずぜぞだぢづでどばびぶべぼぱぴぷぺぽっゃゅょアイウエオカキクケコサシスセソタチツテトナニヌネノハヒフヘホマミムメモヤユヨラリルレロワヲンガギグゲゴザジズゼゾダヂヅデドバビブベボパピプペポッャュョ";
    public static String ARABIC_LETTERS = "ا ب ت ث ج ح خ د ذ ر ز س ش ص ض ط ظ ع غ ف ق ك ل م ن ه و ي";
    public static String CYRILLIC_LETTERS = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюяЄІЇЎҐЂЋЏЉЊђћџљњј";

    static {
        loadCoreData();
    }

    private static void loadCoreData() {
        JsonCharacterLoader.CharacterJsonData data = JsonCharacterLoader.loadAll();
        CHARACTER_DATA.putAll(data.characterData());
        CHARACTER_COMPONENTS.putAll(data.characterComponents());

        COUNTERPARTS.putAll(JsonCounterpartLoader.loadAll());
    }

    public static float getFloatValue(String character) {
        CharacterData extraCharacterData = extraData.get(character);
        if(extraCharacterData != null){
            return extraCharacterData.floatValue;
        }

        CharacterData data = CHARACTER_DATA.get(character);
        if (data == null) {
            if(character.length()==1) {
                if(character.matches("[0-9%]")) return 5000.0f;
                if (character.matches("[A-Za-z]") || LATIN_LETTERS.contains(character)) return 5001.0f;
                if (GREEK_LETTERS.contains(character)) return 5002.0f;
                if (KANA.contains(character)) return 5003.0f;
                if (ARABIC_LETTERS.contains(character)) return 5004.0f;
                if (CYRILLIC_LETTERS.contains(character)) return 5005.0f;
            }
            return 0.0f;
        }
        return data.floatValue();
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

    public static CharacterQuality getQuality(String character){
        float value = getFloatValue(character);
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
                System.err.println("Invalid component mapping: " + mapping);
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
                    float value = Float.parseFloat(parts[1]);
                    newDataMap.put(parts[0], new CharacterData(value, parts[2]));
                }
            } catch (Exception e) {
                System.err.println("Invalid character data mapping: " + mapping);
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
                .sorted(Comparator.comparing(entry -> getFloatValue(entry.getKey())))
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
            float value = entry.getValue().floatValue;
            if(value<1000){
                String key = entry.getKey();
                inscriptionsLuckyAdvanced.add(key);
                if(!CHARACTER_COMPONENTS.containsKey(key)){
                    inscriptionsLucky.add(key);
                }
            }
        }
    }

    public static String getLuckyInscription(){
        if(inscriptionsLucky.isEmpty()){
            inscriptionsLuckyInitialize();
        }
        Random random = new Random();
        int randomIndex = random.nextInt(inscriptionsLucky.size());
        return inscriptionsLucky.get(randomIndex);
    }

    public static String getLuckyInscriptionAdvanced(){
        if(inscriptionsLuckyAdvanced.isEmpty()){
            inscriptionsLuckyInitialize();
        }
        Random random = new Random();
        int randomIndex = random.nextInt(inscriptionsLuckyAdvanced.size());
        return inscriptionsLuckyAdvanced.get(randomIndex);
    }

    public static String getLuckyInscriptionAny(){
        Random random = new Random();
        int randomIndex = random.nextInt(CHARACTER_COMPONENTS.size());
        List<String> keys = new ArrayList<>(CHARACTER_COMPONENTS.keySet());
        return keys.get(randomIndex);
    }

    public static String counterpart(String inscription){
        if(COUNTERPARTS.containsKey(inscription)){
            return COUNTERPARTS.get(inscription);
        }
        return inscription;
    }
}
