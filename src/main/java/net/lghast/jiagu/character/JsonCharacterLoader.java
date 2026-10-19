package net.lghast.jiagu.character;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.lghast.jiagu.JiaguReappear;
import net.neoforged.fml.ModList;
import net.neoforged.neoforgespi.language.IModFileInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class JsonCharacterLoader {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = new Gson();

    public record CharacterJsonData(
            Map<String, CharacterInfo.CharacterData> characterData,
            Map<String, CharacterInfo.StructureData> characterComponents) {}

    public static CharacterJsonData loadAll() {
        Map<String, CharacterInfo.CharacterData> dataMap = new HashMap<>();
        Map<String, CharacterInfo.StructureData> componentMap = new HashMap<>();

        Path directory = findResource("data/jiagureappear/character");
        if (directory != null && Files.isDirectory(directory)) {
            try (DirectoryStream<Path> files = Files.newDirectoryStream(directory,
                    path -> path.getFileName().toString().endsWith(".json"))) {
                for (Path file : files) {
                    parseFile(file, dataMap, componentMap);
                }
            } catch (IOException e) {
                LOGGER.error("Error reading character JSON directory", e);
            }
        }

        Path file = findResource("data/jiagureappear/character.json");
        if (file != null && Files.isRegularFile(file)) {
            parseFile(file, dataMap, componentMap);
        }

        if (dataMap.isEmpty() && componentMap.isEmpty()) {
            LOGGER.warn("No character JSON data loaded.");
        }
        return new CharacterJsonData(dataMap, componentMap);
    }

    private static Path findResource(String resourcePath) {
        IModFileInfo modFileInfo = ModList.get().getModFileById(JiaguReappear.MOD_ID);
        if (modFileInfo != null) {
            Path path = modFileInfo.getFile().findResource(resourcePath);
            if (path != null && Files.exists(path)) {
                return path;
            }
        }

        try {
            URL url = JiaguReappear.class.getClassLoader().getResource(resourcePath);
            if (url != null) {
                return Paths.get(url.toURI());
            }
        } catch (Exception ignored) {}

        return null;
    }

    private static void parseFile(Path file,
                                  Map<String, CharacterInfo.CharacterData> dataMap,
                                  Map<String, CharacterInfo.StructureData> componentMap) {
        try (Reader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            JsonObject root = GSON.fromJson(reader, JsonObject.class);
            if (root == null) return;

            for (Map.Entry<String, JsonElement> entry : root.entrySet()) {
                try {
                    parseEntry(entry.getKey(), entry.getValue().getAsJsonObject(),
                            dataMap, componentMap);
                } catch (Exception e) {
                    LOGGER.warn("Invalid character entry '{}' in file {}", entry.getKey(), file);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Failed to parse character JSON file: {}", file, e);
        }
    }

    private static void parseEntry(String character, JsonObject obj,
                                   Map<String, CharacterInfo.CharacterData> dataMap,
                                   Map<String, CharacterInfo.StructureData> componentMap) {
        if (character == null || character.isEmpty()) return;

        JsonElement valueElem = obj.get("value");
        JsonElement identifierElem = obj.get("identifier");
        if (valueElem != null && identifierElem != null) {
            float value = valueElem.getAsFloat();
            String identifier = identifierElem.getAsString();
            if (identifier != null && !identifier.isEmpty()) {
                dataMap.put(character, new CharacterInfo.CharacterData(value, identifier));
            }
        }

        JsonElement structureElem = obj.get("structure");
        JsonElement componentsElem = obj.get("components");
        if (structureElem != null && componentsElem != null && componentsElem.isJsonArray()) {
            try {
                CharacterStructure structure = CharacterStructure.valueOf(
                        structureElem.getAsString().toUpperCase(Locale.ROOT));

                List<String> components = new ArrayList<>();
                for (JsonElement element : componentsElem.getAsJsonArray()) {
                    components.add(element.getAsString());
                }

                if (!components.isEmpty()) {
                    componentMap.put(character, new CharacterInfo.StructureData(structure, components));
                }
            } catch (IllegalArgumentException e) {
                LOGGER.warn("Unknown structure '{}' for character '{}'",
                        structureElem.getAsString(), character);
            }
        }
    }
}
