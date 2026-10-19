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

public class JsonCounterpartLoader {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = new Gson();

    public static Map<String, String> loadAll() {
        Map<String, String> result = new HashMap<>();

        Path directory = findResource("data/jiagureappear/counterpart");
        if (directory != null && Files.isDirectory(directory)) {
            try (DirectoryStream<Path> files = Files.newDirectoryStream(directory,
                    path -> path.getFileName().toString().endsWith(".json"))) {
                for (Path file : files) {
                    parseFile(file, result);
                }
            } catch (IOException e) {
                LOGGER.error("Error reading counterpart JSON directory", e);
            }
        }

        Path file = findResource("data/jiagureappear/counterpart.json");
        if (file != null && Files.isRegularFile(file)) {
            parseFile(file, result);
        }

        if (result.isEmpty()) {
            LOGGER.warn("No counterpart JSON data loaded.");
        }
        return result;
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

    private static void parseFile(Path file, Map<String, String> result) {
        try (Reader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            JsonObject root = GSON.fromJson(reader, JsonObject.class);
            if (root == null) return;

            for (Map.Entry<String, JsonElement> entry : root.entrySet()) {
                try {
                    String simplified = entry.getKey();
                    String traditional = entry.getValue().getAsString();
                    if (simplified == null || simplified.isEmpty()
                            || traditional == null || traditional.isEmpty()) continue;
                    result.put(simplified, traditional);
                } catch (Exception e) {
                    LOGGER.warn("Invalid counterpart entry '{}' in file {}", entry.getKey(), file);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Failed to parse counterpart JSON file: {}", file, e);
        }
    }
}
