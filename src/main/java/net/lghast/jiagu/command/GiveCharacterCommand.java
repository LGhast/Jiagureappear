package net.lghast.jiagu.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.lghast.jiagu.item.CharacterItem;
import net.lghast.jiagu.register.ModItems;
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

@EventBusSubscriber
public class GiveCharacterCommand {
    @SubscribeEvent
    public static void registerCommand(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(Commands.literal("givecharacter")
                .requires(source -> source.hasPermission(2))
                .then(Commands.argument("inscription", StringArgumentType.string())
                        .executes(context -> giveItem(
                                context,
                                Collections.singleton(context.getSource().getPlayerOrException()),
                                StringArgumentType.getString(context, "inscription"),
                                1
                        ))

                        .then(Commands.argument("targets", EntityArgument.players())
                                .executes(context -> giveItem(
                                        context,
                                        EntityArgument.getPlayers(context, "targets"),
                                        StringArgumentType.getString(context, "inscription"),
                                        1
                                ))

                                .then(Commands.argument("count", IntegerArgumentType.integer(1, 64))
                                        .executes(context -> giveItem(
                                                context,
                                                EntityArgument.getPlayers(context, "targets"),
                                                StringArgumentType.getString(context, "inscription"),
                                                IntegerArgumentType.getInteger(context, "count")
                                        ))
                                )

                                .then(Commands.argument("count", IntegerArgumentType.integer(1, 64))
                                        .executes(context -> giveItem(
                                                context,
                                                Collections.singleton(context.getSource().getPlayerOrException()),
                                                StringArgumentType.getString(context, "inscription"),
                                                IntegerArgumentType.getInteger(context, "count")
                                        ))
                                )
                        )));
    }

    private static int giveItem(CommandContext<CommandSourceStack> context,
                                Collection<ServerPlayer> targets,
                                String inscription,
                                int count) {
        CommandSourceStack source = context.getSource();
        int successCount = 0;

        for (ServerPlayer player : targets) {
            ItemStack stack = new ItemStack(ModItems.CHARACTER_ITEM.get(), count);
            CharacterItem.setInscription(stack, inscription);

            boolean added = player.addItem(stack);
            if (added) {
                successCount++;
                if (targets.size() == 1) {
                    source.sendSuccess(() ->
                                    Component.translatable("commands.givecharacter.success.single",
                                            count,
                                            inscription,
                                            player.getDisplayName()
                                    ),
                            true
                    );
                }
            }
        }

        if (targets.size() > 1) {
            int finalSuccessCount = successCount;
            source.sendSuccess(() ->
                            Component.translatable("commands.givecharacter.success.multiple",
                                    finalSuccessCount,
                                    count,
                                    inscription
                            ),
                    true
            );
        }
        return successCount;
    }
}
