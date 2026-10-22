package net.lghast.jiagu.idiom;

import net.lghast.jiagu.JiaguReappear;
import net.neoforged.fml.ModList;
import net.neoforged.neoforgespi.language.IModFileInfo;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class IdiomDescriptionLoader {
    public static String load(String idiomId, String lang) {
        for (String candidate : getLanguageCandidates(lang)) {
            Path file = findResource("assets/jiagureappear/idioms/" + candidate + "/" + idiomId + ".txt");
            if (file != null && Files.isRegularFile(file)) {
                try {
                    return Files.readString(file, StandardCharsets.UTF_8);
                } catch (IOException e) {
                    return null;
                }
            }
        }
        return null;
    }

    private static List<String> getLanguageCandidates(String lang) {
        List<String> candidates = new ArrayList<>();
        if (!"lzh".equals(lang)) {
            candidates.add(lang);
        }
        candidates.add("zh_tw");
        candidates.add("zh_cn");
        return candidates.stream().distinct().toList();
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
}
