package net.lghast.jiagu.idiom;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.lghast.jiagu.JiaguReappear;
import net.neoforged.fml.ModList;
import net.neoforged.neoforgespi.language.IModFileInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class JsonIdiomLoader {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = new Gson();

    public static Map<String, IdiomInfo> loadAll() {
        Map<String, IdiomInfo> map = new HashMap<>();
        Path file = findResource();
        if (file != null && Files.isRegularFile(file)) {
            try (Reader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
                JsonObject root = GSON.fromJson(reader, JsonObject.class);
                if (root != null) {
                    for (Map.Entry<String, JsonElement> entry : root.entrySet()) {
                        try {
                            JsonObject obj = entry.getValue().getAsJsonObject();
                            String characters = obj.get("characters").getAsString();
                            String icon = obj.get("icon").getAsString();
                            map.put(entry.getKey(), new IdiomInfo(characters, icon));
                        } catch (Exception e) {
                            LOGGER.warn("Invalid idiom entry '{}'", entry.getKey());
                        }
                    }
                }
            } catch (Exception e) {
                LOGGER.error("Failed to parse idiom JSON file", e);
            }
        } else {
            LOGGER.warn("No idiom JSON data loaded.");
        }
        return map;
    }

    private static Path findResource() {
        IModFileInfo modFileInfo = ModList.get().getModFileById(JiaguReappear.MOD_ID);
        if (modFileInfo != null) {
            Path path = modFileInfo.getFile().findResource("data/jiagureappear/idiom/idioms.json");
            if (path != null && Files.exists(path)) {
                return path;
            }
        }

        try {
            URL url = JiaguReappear.class.getClassLoader().getResource("data/jiagureappear/idiom/idioms.json");
            if (url != null) {
                return Paths.get(url.toURI());
            }
        } catch (Exception ignored) {}

        return null;
    }
}
