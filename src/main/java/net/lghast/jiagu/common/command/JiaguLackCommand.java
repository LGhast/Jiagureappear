package net.lghast.jiagu.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.lghast.jiagu.register.ModTags;
import net.lghast.jiagu.utils.CharacterInfo;
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
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber
public class JiaguLackCommand {
    private static Set<String> namespacesCache = null;
    private static long lastCacheUpdate = 0;
    private static final long CACHE_DURATION = 5000;

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
        return SharedSuggestionProvider.suggest(namespacesCache, builder);
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
        HashSet<String> lacks = new HashSet<>();

        List<Holder.Reference<MobEffect>> effectLists = BuiltInRegistries.MOB_EFFECT.holders()
                .filter(holder -> filterByNamespace(holder, namespace))
                .toList();

        List<Holder.Reference<Item>> itemLists = BuiltInRegistries.ITEM.holders()
                .filter(holder -> filterByNamespace(holder, namespace))
                .toList();

        List<Holder.Reference<EntityType<?>>> entityLists = BuiltInRegistries.ENTITY_TYPE.holders()
                .filter(holder -> filterByNamespace(holder, namespace))
                .toList();

        List<Holder.Reference<Potion>> potionLists = BuiltInRegistries.POTION.holders()
                .filter(holder -> filterByNamespace(holder, namespace))
                .toList();

        for (Holder.Reference<MobEffect> effectList : effectLists) {
            MobEffect effect = effectList.value();
            char[] chars = effect.getDisplayName().getString().toCharArray();
            lackFilter(chars, lacks);
        }

        for (Holder.Reference<Item> itemList : itemLists) {
            ItemStack stack = new ItemStack(itemList.value());
            if(stack.is(ModTags.INVALID_TO_CHARACTERS)){
                continue;
            }
            char[] chars = stack.getHoverName().getString().toCharArray();
            lackFilter(chars, lacks);
        }

        for (Holder.Reference<EntityType<?>> entityList : entityLists) {
            EntityType<?> entity = entityList.value();
            if (entity.getCategory() == MobCategory.MISC) {
                continue;
            }
            char[] chars = entity.getDescription().getString().toCharArray();
            lackFilter(chars, lacks);
        }

        for (Holder.Reference<Potion> potionList : potionLists) {
            ItemStack stack = new ItemStack(Items.POTION);
            stack.set(DataComponents.POTION_CONTENTS, new PotionContents(potionList));
            char[] chars = stack.getHoverName().getString().toCharArray();
            lackFilter(chars, lacks);
        }

        int size = lacks.size();
        if(size > 0) {
            StringBuilder builder = new StringBuilder();
            for (String s : lacks) {
                builder.append(s);
            }
            String output = ModUtils.insertLineBreaks(builder.toString(), 35);
            source.sendSuccess(() -> Component.literal(output), false);
        }

        source.sendSuccess(() -> Component.literal("共：" + size), false);

        return 1;
    }

    private static boolean filterByNamespace(Holder.Reference<?> holder, String namespace) {
        if (namespace == null) {
            return true;
        }
        return holder.key().location().getNamespace().equals(namespace);
    }

    private static void lackFilter(char[] chars, HashSet<String> lacks){
        for (char c : chars) {
            String inscription = String.valueOf(c);
            if(inscription.matches("[a-zA-Z()\"“”（） ]")){
                continue;
            }
            if (!CharacterInfo.CHARACTER_DATA.containsKey(inscription)) {
                lacks.add(inscription);
            }
        }
    }
}
