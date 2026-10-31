package net.lghast.jiagu.common.system.command;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.lghast.jiagu.lzh.LzhMappings;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforgespi.language.IModInfo;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
@EventBusSubscriber
public class LzhCommand {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String LANGUAGE = "lzh";

    @SubscribeEvent
    public static void registerCommand(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(Commands.literal("lzh")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("supply")
                        .then(Commands.argument("namespace", StringArgumentType.string())
                                .suggests(LzhCommand::suggestNamespaces)
                                .executes(context -> supply(context, StringArgumentType.getString(context, "namespace")))))
                .then(Commands.literal("declare")
                        .then(Commands.argument("namespace", StringArgumentType.string())
                                .suggests(LzhCommand::suggestNamespaces)
                                .executes(context -> declare(context, StringArgumentType.getString(context, "namespace"))))));
    }

    private static CompletableFuture<Suggestions> suggestNamespaces(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        Set<String> namespaces = new TreeSet<>();
        BuiltInRegistries.ITEM.keySet().forEach(rl -> namespaces.add(rl.getNamespace()));
        BuiltInRegistries.ENTITY_TYPE.keySet().forEach(rl -> namespaces.add(rl.getNamespace()));
        BuiltInRegistries.MOB_EFFECT.keySet().forEach(rl -> namespaces.add(rl.getNamespace()));
        BuiltInRegistries.POTION.keySet().forEach(rl -> namespaces.add(rl.getNamespace()));
        return SharedSuggestionProvider.suggest(namespaces, builder);
    }

    private static int supply(CommandContext<CommandSourceStack> context, String namespace) {
        CommandSourceStack source = context.getSource();
        if (!(source.getEntity() instanceof ServerPlayer player)) {
            source.sendFailure(Component.literal("该命令只能由玩家执行。"));
            return 0;
        }
        if (!LANGUAGE.equals(player.clientInformation().language())) {
            source.sendFailure(Component.literal("当前语言不是文言文，无法使用 supply。"));
            return 0;
        }
        return export(context, namespace, true);
    }

    private static int declare(CommandContext<CommandSourceStack> context, String namespace) {
        return export(context, namespace, false);
    }

    private static int export(CommandContext<CommandSourceStack> context, String namespace, boolean withNames) {
        CommandSourceStack source = context.getSource();
        Registry<Enchantment> enchantments = source.getLevel().registryAccess().registryOrThrow(Registries.ENCHANTMENT);
        Map<String, String> lzh = withNames ? loadLzhTranslations() : Map.of();

        Map<String, Map<String, String>> result = new LinkedHashMap<>();
        result.put("item", collectItems(namespace, withNames, lzh));
        result.put("entity", collectEntities(namespace, withNames, lzh));
        result.put("effect", collectEffects(namespace, withNames, lzh));
        result.put("enchantment", collectEnchantments(enchantments, namespace, withNames, lzh));
        result.put("potion", collectPotions(namespace, withNames, lzh));

        result.entrySet().removeIf(entry -> entry.getValue().isEmpty());
        if (result.isEmpty()) {
            source.sendFailure(Component.literal("没有可导出的内容。"));
            return 0;
        }

        Path dir = source.getLevel().getServer().getServerDirectory()
                .resolve("lzh_export").resolve(namespace);
        try {
            Files.createDirectories(dir);
            for (Map.Entry<String, Map<String, String>> entry : result.entrySet()) {
                Files.writeString(dir.resolve(entry.getKey() + ".json"),
                        GSON.toJson(entry.getValue()), StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            source.sendFailure(Component.literal("导出失败：" + e.getMessage()));
            return 0;
        }

        int total = result.values().stream().mapToInt(Map::size).sum();
        source.sendSuccess(() -> Component.literal("已导出 " + total + " 条到 " + dir), true);
        return total;
    }

    private static Map<String, String> collectItems(String namespace, boolean withNames, Map<String, String> lzh) {
        Map<String, String> map = new LinkedHashMap<>();
        for (Holder.Reference<Item> holder : BuiltInRegistries.ITEM.holders().toList()) {
            ResourceLocation rl = holder.key().location();
            if (!rl.getNamespace().equals(namespace)) continue;
            ItemStack stack = new ItemStack(holder.value());
            if (LzhMappings.getLzhName(stack) != null) continue;
            if (isPotion(stack)) continue;
            map.put(rl.getPath(), displayName(stack.getItem().getDescriptionId(),
                    stack.getHoverName().getString(), withNames, lzh));
        }
        return map;
    }

    private static Map<String, String> collectEntities(String namespace, boolean withNames, Map<String, String> lzh) {
        Map<String, String> map = new LinkedHashMap<>();
        for (Holder.Reference<EntityType<?>> holder : BuiltInRegistries.ENTITY_TYPE.holders().toList()) {
            ResourceLocation rl = holder.key().location();
            if (!rl.getNamespace().equals(namespace)) continue;
            if (holder.value().getCategory() == MobCategory.MISC) continue;
            if (LzhMappings.getLzhName(holder.value()) != null) continue;
            map.put(rl.getPath(), displayName(holder.value().getDescriptionId(),
                    holder.value().getDescription().getString(), withNames, lzh));
        }
        return map;
    }

    private static Map<String, String> collectEffects(String namespace, boolean withNames, Map<String, String> lzh) {
        Map<String, String> map = new LinkedHashMap<>();
        for (Holder.Reference<MobEffect> holder : BuiltInRegistries.MOB_EFFECT.holders().toList()) {
            ResourceLocation rl = holder.key().location();
            if (!rl.getNamespace().equals(namespace)) continue;
            if (LzhMappings.getLzhName(holder.value()) != null) continue;
            map.put(rl.getPath(), displayName(holder.value().getDescriptionId(),
                    holder.value().getDisplayName().getString(), withNames, lzh));
        }
        return map;
    }

    private static Map<String, String> collectEnchantments(Registry<Enchantment> enchantments, String namespace, boolean withNames, Map<String, String> lzh) {
        Map<String, String> map = new LinkedHashMap<>();
        for (Holder.Reference<Enchantment> holder : enchantments.holders().toList()) {
            ResourceLocation rl = holder.key().location();
            if (!rl.getNamespace().equals(namespace)) continue;
            if (LzhMappings.getLzhName(holder) != null) continue;
            Component description = holder.value().description();
            String key = getTranslationKey(description);
            map.put(rl.getPath(), displayName(key, description.getString(), withNames, lzh));
        }
        return map;
    }

    private static Map<String, String> collectPotions(String namespace, boolean withNames, Map<String, String> lzh) {
        Map<String, String> map = new LinkedHashMap<>();
        for (Holder.Reference<Potion> holder : BuiltInRegistries.POTION.holders().toList()) {
            ResourceLocation rl = holder.key().location();
            if (!rl.getNamespace().equals(namespace)) continue;
            ItemStack stack = new ItemStack(Items.POTION);
            stack.set(DataComponents.POTION_CONTENTS, new PotionContents(holder));
            if (LzhMappings.getLzhName(stack) != null) continue;
            String key = "item.minecraft.potion.effect." + rl.getPath();
            map.put(rl.getPath(), displayName(key, stack.getHoverName().getString(), withNames, lzh));
        }
        return map;
    }

    private static String displayName(String translationKey, String fallback, boolean withNames, Map<String, String> lzh) {
        if (!withNames) return "";
        if (translationKey != null) {
            String lzhName = lzh.get(translationKey);
            if (lzhName != null) return lzhName;
        }
        return fallback;
    }

    private static String getTranslationKey(Component component) {
        if (component.getContents() instanceof TranslatableContents translatable) {
            return translatable.getKey();
        }
        return null;
    }

    private static boolean isPotion(ItemStack stack) {
        PotionContents contents = stack.get(DataComponents.POTION_CONTENTS);
        return contents != null && contents.potion().isPresent();
    }

    private static Map<String, String> loadLzhTranslations() {
        Map<String, String> map = new HashMap<>();
        for (IModInfo mod : ModList.get().getMods()) {
            Path file = mod.getOwningFile().getFile()
                    .findResource("assets/" + mod.getModId() + "/lang/lzh.json");
            if (file == null || !Files.isRegularFile(file)) continue;
            try (Reader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
                JsonObject root = GSON.fromJson(reader, JsonObject.class);
                if (root != null) {
                    for (Map.Entry<String, JsonElement> entry : root.entrySet()) {
                        map.put(entry.getKey(), entry.getValue().getAsString());
                    }
                }
            } catch (Exception ignored) {}
        }
        return map;
    }
}
