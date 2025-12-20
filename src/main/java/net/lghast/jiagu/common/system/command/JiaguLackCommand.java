package net.lghast.jiagu.common.system.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.lghast.jiagu.register.system.ModTags;
import net.lghast.jiagu.utils.lzh.CharacterInfo;
import net.lghast.jiagu.utils.ModUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

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
        HashSet<String> gross = new HashSet<>();

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
            String name = ModUtils.modifyName(effect);
            if(name == null) continue;
            char[] chars = name.toCharArray();
            lackFilter(chars, lacks, gross);
        }

        for (Holder.Reference<Item> itemList : itemLists) {
            ItemStack stack = new ItemStack(itemList.value());
            if(stack.is(ModTags.INVALID_TO_CHARACTERS)){
                continue;
            }
            String name = ModUtils.modifyName(stack);
            if(name == null) continue;
            char[] chars = name.toCharArray();
            lackFilter(chars, lacks, gross);
        }

        for (Holder.Reference<EntityType<?>> entityList : entityLists) {
            EntityType<?> entity = entityList.value();
            if (entity.getCategory() == MobCategory.MISC) {
                continue;
            }
            String name = ModUtils.modifyName(entity);
            if(name == null) continue;
            char[] chars = name.toCharArray();
            lackFilter(chars, lacks, gross);
        }

        for (Holder.Reference<Potion> potionList : potionLists) {
            ItemStack stack = new ItemStack(Items.POTION);
            stack.set(DataComponents.POTION_CONTENTS, new PotionContents(potionList));
            String name = ModUtils.modifyName(stack);
            if(name == null) continue;
            char[] chars = name.toCharArray();
            lackFilter(chars, lacks, gross);
        }

        int size = lacks.size();
        int sum = gross.size();

        if(sum > 0){
            if(size > 0) {
                StringBuilder builder = new StringBuilder();
                for (String s : lacks) {
                    builder.append(s);
                }
                String output = ModUtils.insertLineBreaks(builder.toString(), 35);
                source.sendSuccess(() -> Component.literal(output), false);

                StringBuilder lackBuilder = new StringBuilder();
                for(String s : lacks){
                    lackBuilder.append(s);
                }
                System.out.println("缺失汉字列表：" + lackBuilder);
            }

            source.sendSuccess(() -> Component.literal("共缺失汉字：" + size + "/" + sum), false);
        }else{
            source.sendSuccess(() -> Component.literal("此模组无汉字。"), false);
        }

        return 1;
    }

    private static boolean filterByNamespace(Holder.Reference<?> holder, String namespace) {
        if (namespace == null) {
            return true;
        }
        return holder.key().location().getNamespace().equals(namespace);
    }

    private static void lackFilter(char[] chars, HashSet<String> lacks, HashSet<String> gross){
        for (char c : chars) {
            String inscription = String.valueOf(c);
            if(inscription.matches("[a-zA-Z()\"“”（） ]")){
                continue;
            }
            gross.add(inscription);
            if (!CharacterInfo.CHARACTER_DATA.containsKey(inscription)) {
                lacks.add(inscription);
            }
        }
    }
}
