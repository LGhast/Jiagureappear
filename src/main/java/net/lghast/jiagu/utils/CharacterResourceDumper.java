package net.lghast.jiagu.utils;

import com.google.gson.*;
import com.mojang.serialization.JsonOps;
import net.lghast.jiagu.character.CharacterInfo;
import net.lghast.jiagu.datagen.ModRecipeProvider;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.neoforged.neoforge.common.conditions.ICondition;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class CharacterResourceDumper {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void dump() {
        Path root = Path.of("jiagu_character_resources_dump");
        try {
            dumpRecipes(root.resolve("data"));
            dumpModels(root.resolve("assets"));
            LOGGER.info("Character resource dump completed at {}", root.toAbsolutePath());
        } catch (Exception e) {
            LOGGER.error("Character resource dump failed", e);
        }
    }

    private static void dumpRecipes(Path dataRoot) {
        RecipeExtractor extractor = new RecipeExtractor();
        extractor.generate(new DirectRecipeOutput(dataRoot));
    }

    private static void dumpModels(Path assetsRoot) {
        Path modelsRoot = assetsRoot.resolve("jiagureappear")
                .resolve("models").resolve("item");

        writeGeneratedModel(modelsRoot.resolve("characters").resolve("default.json"),
                "jiagureappear:item/characters/default");

        List<Map.Entry<String, CharacterInfo.CharacterData>> sorted =
                new ArrayList<>(CharacterInfo.CHARACTER_DATA.entrySet());
        sorted.sort(Comparator.comparingDouble(entry -> entry.getValue().floatValue()));

        JsonObject characterModel = new JsonObject();
        characterModel.addProperty("parent", "minecraft:item/generated");

        JsonObject textures = new JsonObject();
        textures.addProperty("layer0", "jiagureappear:item/characters/default");
        characterModel.add("textures", textures);

        JsonArray overrides = new JsonArray();
        for (Map.Entry<String, CharacterInfo.CharacterData> entry : sorted) {
            String modelName = entry.getValue().identifier();

            writeGeneratedModel(modelsRoot.resolve("characters")
                            .resolve(modelName + ".json"),
                    "jiagureappear:item/characters/" + modelName);

            JsonObject override = new JsonObject();
            JsonObject predicate = new JsonObject();
            predicate.addProperty("jiagureappear:inscription", entry.getValue().floatValue());
            override.add("predicate", predicate);
            override.addProperty("model", "jiagureappear:item/characters/" + modelName);
            overrides.add(override);
        }
        characterModel.add("overrides", overrides);
        writeJson(modelsRoot.resolve("character.json"), characterModel);
    }

    private static void writeGeneratedModel(Path file, String layer0) {
        JsonObject model = new JsonObject();
        model.addProperty("parent", "minecraft:item/generated");
        JsonObject textures = new JsonObject();
        textures.addProperty("layer0", layer0);
        model.add("textures", textures);
        writeJson(file, model);
    }

    private static void writeJson(Path file, JsonElement json) {
        try {
            Files.createDirectories(file.getParent());
            Files.writeString(file, GSON.toJson(json), StandardCharsets.UTF_8);
        } catch (IOException e) {
            LOGGER.error("Failed to write JSON file {}", file, e);
        }
    }

    private static class RecipeExtractor extends ModRecipeProvider {
        RecipeExtractor() {
            super(new PackOutput(Path.of("jiagu_character_resources_dump").resolve("tmp_pack")),
                    CompletableFuture.completedFuture(
                            RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY)));
        }

        void generate(RecipeOutput output) {
            buildRecipes(output);
        }
    }

    private static class DirectRecipeOutput implements RecipeOutput {
        private final Path dataRoot;

        DirectRecipeOutput(Path dataRoot) {
            this.dataRoot = dataRoot;
        }

        @Override
        public void accept(ResourceLocation id, Recipe<?> recipe,
                           @Nullable AdvancementHolder advancement, ICondition... conditions) {
            if (!id.getPath().startsWith("character_assembling/")) {
                return;
            }
            try {
                JsonElement recipeJson = Recipe.CODEC.encodeStart(JsonOps.INSTANCE, recipe)
                        .getOrThrow(IllegalStateException::new);

                Path recipeFile = dataRoot.resolve(id.getNamespace()).resolve("recipe")
                        .resolve(id.getPath() + ".json");
                writeJson(recipeFile, recipeJson);
            } catch (Exception e) {
                LOGGER.error("Failed to write recipe {}", id, e);
            }
        }

        @Override
        public Advancement.Builder advancement() {
            return Advancement.Builder.advancement();
        }
    }
}
