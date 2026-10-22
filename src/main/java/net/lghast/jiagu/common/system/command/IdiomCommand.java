package net.lghast.jiagu.common.system.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.lghast.jiagu.common.content.item.CharacterItem;
import net.lghast.jiagu.idiom.IdiomInfo;
import net.lghast.jiagu.idiom.IdiomRegistry;
import net.lghast.jiagu.network.IdiomCollectedPayload;
import net.lghast.jiagu.register.content.ModItems;
import net.lghast.jiagu.register.system.ModAttachments;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
@EventBusSubscriber
public class IdiomCommand {
    @SubscribeEvent
    public static void registerCommand(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(Commands.literal("idiom")
                .requires(source -> source.hasPermission(2))
                .then(Commands.argument("targets", EntityArgument.players())
                        .then(Commands.literal("grant")
                                .then(Commands.argument("idiom", StringArgumentType.string())
                                        .suggests(IdiomCommand::suggestIdioms)
                                        .executes(context -> grantIdiom(
                                                context,
                                                EntityArgument.getPlayers(context, "targets"),
                                                StringArgumentType.getString(context, "idiom")
                                        ))))
                        .then(Commands.literal("remove")
                                .then(Commands.argument("idiom", StringArgumentType.string())
                                        .suggests(IdiomCommand::suggestIdioms)
                                        .executes(context -> removeIdiom(
                                                context,
                                                EntityArgument.getPlayers(context, "targets"),
                                                StringArgumentType.getString(context, "idiom")
                                        ))))
                        .then(Commands.literal("give")
                                .then(Commands.argument("idiom", StringArgumentType.string())
                                        .suggests(IdiomCommand::suggestIdioms)
                                        .executes(context -> giveIdiom(
                                                context,
                                                EntityArgument.getPlayers(context, "targets"),
                                                StringArgumentType.getString(context, "idiom")
                                        ))))));
    }

    private static CompletableFuture<Suggestions> suggestIdioms(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggest(IdiomRegistry.getIdioms().keySet(), builder);
    }

    private static int grantIdiom(CommandContext<CommandSourceStack> context, Collection<ServerPlayer> targets, String idiomId) {
        CommandSourceStack source = context.getSource();
        IdiomInfo info = IdiomRegistry.getById(idiomId);
        if (info == null) {
            source.sendFailure(Component.translatable("commands.idiom.invalid", idiomId));
            return 0;
        }
        int grantedCount = 0;
        for (ServerPlayer player : targets) {
            Set<String> collected = player.getData(ModAttachments.COLLECTED_IDIOMS.get());
            if (collected.add(idiomId)) {
                player.setData(ModAttachments.COLLECTED_IDIOMS.get(), collected);
                PacketDistributor.sendToPlayer(player, new IdiomCollectedPayload(idiomId, info.characters()));
                grantedCount++;
            }
        }
        int finalGrantedCount = grantedCount;
        source.sendSuccess(() -> Component.translatable("commands.idiom.grant", info.characters(), finalGrantedCount), true);
        return grantedCount;
    }

    private static int removeIdiom(CommandContext<CommandSourceStack> context, Collection<ServerPlayer> targets, String idiomId) {
        CommandSourceStack source = context.getSource();
        IdiomInfo info = IdiomRegistry.getById(idiomId);
        if (info == null) {
            source.sendFailure(Component.translatable("commands.idiom.invalid", idiomId));
            return 0;
        }
        int removedCount = 0;
        for (ServerPlayer player : targets) {
            Set<String> collected = player.getData(ModAttachments.COLLECTED_IDIOMS.get());
            if (collected.remove(idiomId)) {
                player.setData(ModAttachments.COLLECTED_IDIOMS.get(), collected);
                removedCount++;
            }
        }
        if (removedCount == 0) {
            source.sendFailure(Component.translatable("commands.idiom.not_collected", info.characters()));
            return 0;
        }
        int finalRemovedCount = removedCount;
        source.sendSuccess(() -> Component.translatable("commands.idiom.remove", info.characters(), finalRemovedCount), true);
        return removedCount;
    }

    private static int giveIdiom(CommandContext<CommandSourceStack> context, Collection<ServerPlayer> targets, String idiomId) {
        CommandSourceStack source = context.getSource();
        IdiomInfo info = IdiomRegistry.getById(idiomId);
        if (info == null) {
            source.sendFailure(Component.translatable("commands.idiom.invalid", idiomId));
            return 0;
        }
        int successCount = 0;
        for (ServerPlayer player : targets) {
            for (char c : info.characters().toCharArray()) {
                ItemStack stack = new ItemStack(ModItems.CHARACTER_ITEM.get(), 1);
                CharacterItem.setInscription(stack, String.valueOf(c));
                if (player.addItem(stack)) {
                    successCount++;
                }
            }
        }
        int finalSuccessCount = successCount;
        source.sendSuccess(() -> Component.translatable("commands.idiom.give", info.characters(), finalSuccessCount), true);
        return successCount;
    }
}
