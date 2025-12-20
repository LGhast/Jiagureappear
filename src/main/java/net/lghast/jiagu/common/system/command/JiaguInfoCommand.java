package net.lghast.jiagu.common.system.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.lghast.jiagu.utils.lzh.CharacterInfo;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
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
                                        Component.literal("§c“" + input + "”"+ Component.translatable("commands.jiaguinfo.not_found").getString())
                                );
                                return 1;
                            }

                            source.sendSuccess(() ->
                                            Component.literal("“"+input+"”:"), false
                            );
                            source.sendSuccess(() ->
                                    Component.literal(Component.translatable("commands.jiaguinfo.recorded_name").getString()
                                            + "§7" + CharacterInfo.getIdentifier(input)), false
                            );
                            List<String> list = CharacterInfo.getComponents(input);
                            if(list == null || list.isEmpty()){
                                source.sendSuccess(() ->
                                                Component.literal(Component.translatable("commands.jiaguinfo.disassembly").getString()
                                                        + "§7" + Component.translatable("commands.jiaguinfo.nothing").getString()), false
                                );
                            }else{
                                source.sendSuccess(() ->
                                                Component.literal(Component.translatable("commands.jiaguinfo.disassembly").getString()
                                                        + "§7" + CharacterInfo.getCharacterDisassembly(input)), false
                                );
                            }

                            String assemblable = CharacterInfo.getCharacterAssembly(input);
                            if(assemblable.isEmpty()){
                                source.sendSuccess(() ->
                                        Component.literal(Component.translatable("commands.jiaguinfo.assembly").getString()
                                                + "§7" + Component.translatable("commands.jiaguinfo.nothing").getString()), false
                                );
                            }else if(assemblable.length()>12){
                                source.sendSuccess(() ->
                                                Component.literal(Component.translatable("commands.jiaguinfo.assembly").getString()
                                                        + "\n§7" + assemblable), false
                                );
                            }else{
                                source.sendSuccess(() ->
                                        Component.literal(Component.translatable("commands.jiaguinfo.assembly").getString()
                                                + "§7" + assemblable), false
                                );
                            }

                            return 1;
                        })
                ));
    }
}
