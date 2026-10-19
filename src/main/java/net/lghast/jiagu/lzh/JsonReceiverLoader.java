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
import java.util.HashMap;
import java.util.Map;

public class JsonReceiverLoader {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = new Gson();

    public static Map<String, String> loadAll() {
        Map<String, String> result = new HashMap<>();

        IModFileInfo modFileInfo = ModList.get().getModFileById(JiaguReappear.MOD_ID);
        if (modFileInfo == null) return result;

        Path root = modFileInfo.getFile().findResource("data/jiagureappear/receiver");
        if (root == null || !Files.exists(root)) {
            LOGGER.warn("Receiver mapping folder not found.");
            return result;
        }

        try (DirectoryStream<Path> files = Files.newDirectoryStream(root,
                path -> path.getFileName().toString().endsWith(".json"))) {
            for (Path file : files) {
                parseFile(file, result);
            }
        } catch (IOException e) {
            LOGGER.error("Error reading receiver mapping directory", e);
        }
        return result;
    }

    private static void parseFile(Path file, Map<String, String> result) {
        String fileName = file.getFileName().toString();
        String namespace = fileName.substring(0, fileName.length() - 5);
        if (!"minecraft".equals(namespace) && !ModUtils.isModLoaded(namespace)) {
            return;
        }

        try (Reader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            JsonObject root = GSON.fromJson(reader, JsonObject.class);
            if (root == null) return;

            for (Map.Entry<String, JsonElement> entry : root.entrySet()) {
                try {
                    String enchantPath = entry.getKey();
                    String receiverId = entry.getValue().getAsString();
                    if (enchantPath == null || enchantPath.trim().isEmpty()
                            || receiverId == null || receiverId.trim().isEmpty()) continue;

                    String enchantId = enchantPath.contains(":")
                            ? enchantPath
                            : namespace + ":" + enchantPath;
                    result.put(enchantId, receiverId);
                } catch (Exception e) {
                    LOGGER.warn("Invalid receiver mapping entry '{}' in file {}", entry.getKey(), file);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Failed to parse receiver mapping file: {}", file, e);
        }
    }
}
