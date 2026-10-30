package net.lghast.jiagu.common.system.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.lghast.jiagu.register.system.ModTags;
import net.lghast.jiagu.character.CharacterInfo;
import net.lghast.jiagu.utils.ModUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
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
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
@EventBusSubscriber
public class JiaguLackCommand {
    private static Set<String> namespacesCache = null;
    private static long lastCacheUpdate = 0;
    private static final long CACHE_DURATION = 5000;

    private static final int LINE_BREAK_INTERVAL = 35;

    @SubscribeEvent
    public static void registerCommand(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        var command = Commands.literal("jiagulack")
                .then(Commands.argument("namespace", StringArgumentType.string())
                        .suggests(JiaguLackCommand::suggestNamespaces)
                        .executes(context -> {
                            String namespace = StringArgumentType.getString(context, "namespace");
                            return executeCommand(context, namespace);
                        }))
                .executes(context -> executeCommand(context, null));

        dispatcher.register(command);
    }

    private static CompletableFuture<Suggestions> suggestNamespaces(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        updateNamespacesCache();
        Set<String> all = new HashSet<>(namespacesCache);
        try {
            context.getSource().getLevel().registryAccess().registryOrThrow(Registries.ENCHANTMENT)
                    .keySet().stream().map(ResourceLocation::getNamespace).forEach(all::add);
        } catch (Exception ignored) {}
        return SharedSuggestionProvider.suggest(all, builder);
    }

    private static void updateNamespacesCache() {
        long currentTime = System.currentTimeMillis();

        if (namespacesCache == null || currentTime - lastCacheUpdate > CACHE_DURATION) {
            namespacesCache = new HashSet<>();

            BuiltInRegistries.ITEM.keySet().stream()
                    .map(ResourceLocation::getNamespace)
                    .forEach(namespacesCache::add);

            BuiltInRegistries.ENTITY_TYPE.keySet().stream()
                    .map(ResourceLocation::getNamespace)
                    .forEach(namespacesCache::add);

            BuiltInRegistries.MOB_EFFECT.keySet().stream()
                    .map(ResourceLocation::getNamespace)
                    .forEach(namespacesCache::add);

            BuiltInRegistries.POTION.keySet().stream()
                    .map(ResourceLocation::getNamespace)
                    .forEach(namespacesCache::add);

            lastCacheUpdate = currentTime;
        }
    }

    private static int executeCommand(CommandContext<CommandSourceStack> context, String namespace) {
        CommandSourceStack source = context.getSource();

        Map<String, Integer> total = new HashMap<>();
        Map<String, Integer> missing = new HashMap<>();

        for (Holder.Reference<MobEffect> effectHolder : BuiltInRegistries.MOB_EFFECT.holders()
                .filter(holder -> filterByNamespace(holder, namespace)).toList()) {
            String name = ModUtils.modifyName(effectHolder.value());
            if (name != null) countChars(name, total, missing);
        }

        for (Holder.Reference<Item> itemHolder : BuiltInRegistries.ITEM.holders()
                .filter(holder -> filterByNamespace(holder, namespace)).toList()) {
            ItemStack stack = new ItemStack(itemHolder.value());
            if (stack.is(ModTags.INVALID_TO_CHARACTERS)) continue;
            String name = ModUtils.modifyName(stack);
            if (name != null) countChars(name, total, missing);
        }

        for (Holder.Reference<EntityType<?>> entityHolder : BuiltInRegistries.ENTITY_TYPE.holders()
                .filter(holder -> filterByNamespace(holder, namespace)).toList()) {
            EntityType<?> entity = entityHolder.value();
            if (entity.getCategory() == MobCategory.MISC) continue;
            String name = ModUtils.modifyName(entity);
            if (name != null) countChars(name, total, missing);
        }

        for (Holder.Reference<Potion> potionHolder : BuiltInRegistries.POTION.holders()
                .filter(holder -> filterByNamespace(holder, namespace)).toList()) {
            ItemStack stack = new ItemStack(Items.POTION);
            stack.set(DataComponents.POTION_CONTENTS, new PotionContents(potionHolder));
            String name = ModUtils.modifyName(stack);
            if (name != null) countChars(name, total, missing);
        }

        for (Holder.Reference<Enchantment> enchantmentHolder : source.getLevel().registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT).holders()
                .filter(holder -> filterByNamespace(holder, namespace)).toList()) {
            String name = ModUtils.modifyName(enchantmentHolder, 1);
            if (name != null) countChars(name, total, missing);
        }

        int totalCount = total.size();
        int missingCount = missing.size();

        if (totalCount == 0) {
            source.sendSuccess(() -> Component.literal("此模组无汉字。"), false);
            return 0;
        }

        if (namespace != null) {
            source.sendSuccess(() -> Component.literal(namespace + ":"), false);
        }

        if (missingCount > 0) {
            StringBuilder builder = new StringBuilder();
            missing.entrySet().stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .forEach(entry -> builder.append(entry.getKey()));

            source.sendSuccess(() -> Component.literal(
                    ModUtils.insertLineBreaks(builder.toString(), LINE_BREAK_INTERVAL)), false);
        }

        source.sendSuccess(() -> Component.literal("共缺失汉字：" + missingCount + "/" + totalCount), false);
        return missingCount;
    }

    private static boolean filterByNamespace(Holder.Reference<?> holder, String namespace) {
        if (namespace == null) {
            return true;
        }
        return holder.key().location().getNamespace().equals(namespace);
    }

    private static void countChars(String name, Map<String, Integer> total, Map<String, Integer> missing) {
        for (char c : name.toCharArray()) {
            String inscription = String.valueOf(c);
            if (inscription.matches("[a-zA-Z()\"“”（） ]")) {
                continue;
            }
            total.merge(inscription, 1, Integer::sum);
            if (!CharacterInfo.CHARACTER_DATA.containsKey(inscription)) {
                missing.merge(inscription, 1, Integer::sum);
            }
        }
    }
}
