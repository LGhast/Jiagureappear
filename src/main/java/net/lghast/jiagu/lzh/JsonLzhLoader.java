package net.lghast.jiagu.lzh;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.lghast.jiagu.JiaguReappear;
import net.lghast.jiagu.utils.ModUtils;
import net.neoforged.fml.ModList;
import net.neoforged.neoforgespi.language.IModFileInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class JsonLzhLoader {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = new Gson();

    public enum Category {
        ITEM,
        ENTITY,
        ENCHANTMENT,
        EFFECT,
        POTION
    }

    public static Map<Category, Map<String, String>> loadAll() {
        Map<Category, Map<String, String>> result = new EnumMap<>(Category.class);
        for (Category category : Category.values()) {
            result.put(category, new HashMap<>());
        }

        IModFileInfo modFileInfo = ModList.get().getModFileById(JiaguReappear.MOD_ID);
        if (modFileInfo == null) return result;

        Path root = modFileInfo.getFile().findResource("data/jiagureappear/lzh");
        if (root == null || !Files.exists(root)) {
            LOGGER.warn("LZH mapping folder not found.");
            return result;
        }

        try (DirectoryStream<Path> namespaceDirs = Files.newDirectoryStream(root)) {
            for (Path namespaceDir : namespaceDirs) {
                if (!Files.isDirectory(namespaceDir)) continue;

                String namespace = namespaceDir.getFileName().toString();
                if (!isNamespaceAvailable(namespace)) continue;

                try (DirectoryStream<Path> files = Files.newDirectoryStream(namespaceDir,
                        path -> path.getFileName().toString().endsWith(".json"))) {
                    for (Path file : files) {
                        parseFile(file, namespace, result);
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("Error reading LZH mapping directory", e);
        }
        return result;
    }

    private static boolean isNamespaceAvailable(String namespace) {
        return "minecraft".equals(namespace) || ModUtils.isModLoaded(namespace);
    }

    private static void parseFile(Path file, String namespace,
                                  Map<Category, Map<String, String>> result) {
        String fileName = file.getFileName().toString();
        String categoryName = fileName.substring(0, fileName.length() - 5);
        Category category;
        try {
            category = Category.valueOf(categoryName.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            LOGGER.warn("Unknown LZH mapping category file: {}", file);
            return;
        }

        try (Reader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            JsonObject root = GSON.fromJson(reader, JsonObject.class);
            if (root == null) return;

            for (Map.Entry<String, JsonElement> entry : root.entrySet()) {
                try {
                    String path = entry.getKey();
                    String name = entry.getValue().getAsString();
                    if (path == null || path.trim().isEmpty()
                            || name == null || name.trim().isEmpty()) continue;

                    String id = path.contains(":") ? path : namespace + ":" + path;
                    result.get(category).put(id, name);
                } catch (Exception e) {
                    LOGGER.warn("Invalid LZH mapping entry '{}' in file {}", entry.getKey(), file);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Failed to parse LZH mapping file: {}", file, e);
        }
    }
}