package net.lghast.jiagu.common.system.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.lghast.jiagu.common.content.item.CharacterItem;
import net.lghast.jiagu.register.content.ModItems;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import java.util.Collection;
import java.util.Collections;

@SuppressWarnings("unused")
@EventBusSubscriber
public class GiveCharactersCommand {
    @SubscribeEvent
    public static void registerCommand(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(Commands.literal("givecharacters")
                .requires(source -> source.hasPermission(2))
                .then(Commands.argument("characters", StringArgumentType.string())
                        .executes(context -> giveCharacters(
                                context,
                                Collections.singleton(context.getSource().getPlayerOrException()),
                                StringArgumentType.getString(context, "characters")
                        ))
                        .then(Commands.argument("targets", EntityArgument.players())
                                .executes(context -> giveCharacters(
                                        context,
                                        EntityArgument.getPlayers(context, "targets"),
                                        StringArgumentType.getString(context, "characters")
                                )))));
    }

    private static int giveCharacters(CommandContext<CommandSourceStack> context,
                                      Collection<ServerPlayer> targets,
                                      String characters) {
        CommandSourceStack source = context.getSource();
        int successCount = 0;

        for (ServerPlayer player : targets) {
            for (char c : characters.toCharArray()) {
                ItemStack stack = new ItemStack(ModItems.CHARACTER_ITEM.get(), 1);
                CharacterItem.setInscription(stack, String.valueOf(c));
                if (player.addItem(stack)) {
                    successCount++;
                }
            }
        }

        int finalSuccessCount = successCount;
        source.sendSuccess(() -> Component.translatable("commands.givecharacters.success",
                characters, finalSuccessCount), true);
        return successCount;
    }
}
