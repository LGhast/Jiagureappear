package net.lghast.jiagu.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.lghast.jiagu.item.CharacterItem;
import net.lghast.jiagu.utils.CharacterInfo;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.synchronization.brigadier.StringArgumentSerializer;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.util.FakePlayerFactory;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import java.util.List;

@EventBusSubscriber
public class JiaguInfoCommand {
    @SubscribeEvent
    public static void registerCommand(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(Commands.literal("jiaguinfo")
                .then(Commands.argument("inscription", StringArgumentType.greedyString())
                        .executes(context -> {
                            CommandSourceStack source = context.getSource();
                            String input = StringArgumentType.getString(context, "inscription");

                            if(!CharacterInfo.CHARACTER_DATA.containsKey(input)){
                                source.sendFailure(
                                        Component.literal("§c“" + input + "”未為所錄")
                                );
                                return 1;
                            }

                            source.sendSuccess(() ->
                                            Component.literal("“"+input+"”:"), false
                            );
                            List<String> list = CharacterInfo.getComponents(input);
                            if(list == null || list.isEmpty()){
                                source.sendSuccess(() ->
                                                Component.literal("可製自：§7無"), false
                                );
                            }else{
                                source.sendSuccess(() ->
                                                Component.literal("可製自：§3" + CharacterInfo.getCharactersDisassemblable(input)), false
                                );
                            }

                            String assemblable = CharacterInfo.getCharactersAssemblable(input);
                            if(assemblable.isEmpty()){
                                source.sendSuccess(() ->
                                                Component.literal("可製為：§7無\n"), false
                                );
                            }else if(assemblable.length()>12){
                                source.sendSuccess(() ->
                                                Component.literal("可製為：\n§3" + assemblable + "\n"), false
                                );
                            }else{
                                source.sendSuccess(() ->
                                        Component.literal("可製為：§3" + assemblable + "\n"), false
                                );
                            }

                            return 1;
                        })
                ));
    }
}
