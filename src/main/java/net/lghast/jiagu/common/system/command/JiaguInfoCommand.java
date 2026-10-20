package net.lghast.jiagu.common.system.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.lghast.jiagu.character.CharacterInfo;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import java.util.List;

@EventBusSubscriber
@SuppressWarnings("unused")
public class JiaguInfoCommand {
    @SubscribeEvent
    public static void registerCommand(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(Commands.literal("jiaguinfo")
                .then(Commands.argument("inscription", StringArgumentType.greedyString())
                        .executes(context -> executeInfo(
                                context.getSource(),
                                StringArgumentType.getString(context, "inscription")
                        ))
                ));
    }

    public static int executeInfo(CommandSourceStack source, String input) {
        if (!CharacterInfo.CHARACTER_DATA.containsKey(input)) {
            source.sendFailure(
                    Component.literal("“" + input + "”")
                            .append(Component.translatable("commands.jiaguinfo.not_found"))
                            .withStyle(ChatFormatting.RED)
            );
            return 0;
        }

        source.sendSuccess(() ->
                Component.literal("“" + input + "”:"), false
        );
        source.sendSuccess(() ->
                Component.translatable("commands.jiaguinfo.recorded_name")
                        .append(Component.literal(CharacterInfo.getIdentifier(input))
                                .withStyle(ChatFormatting.GRAY)), false
        );

        List<String> list = CharacterInfo.getComponents(input);
        if (list == null || list.isEmpty()) {
            source.sendSuccess(() ->
                    Component.translatable("commands.jiaguinfo.disassembly")
                            .append(Component.translatable("commands.jiaguinfo.nothing")
                                    .withStyle(ChatFormatting.GRAY)), false
            );
        } else {
            String disassembly = CharacterInfo.getCharacterDisassembly(input);
            String text = disassembly != null
                    ? disassembly
                    : Component.translatable("commands.jiaguinfo.nothing").getString();
            source.sendSuccess(() ->
                    Component.translatable("commands.jiaguinfo.disassembly")
                            .append(Component.literal(text)
                                    .withStyle(ChatFormatting.GRAY)), false
            );
        }

        String assemblable = CharacterInfo.getCharacterAssembly(input);
        if (assemblable.isEmpty()) {
            source.sendSuccess(() ->
                    Component.translatable("commands.jiaguinfo.assembly")
                            .append(Component.translatable("commands.jiaguinfo.nothing")
                                    .withStyle(ChatFormatting.GRAY)), false
            );
        } else {
            Component assemblyComponent;
            if (assemblable.length() > 12) {
                assemblyComponent = Component.translatable("commands.jiaguinfo.assembly")
                        .append(Component.literal("\n" + assemblable).withStyle(ChatFormatting.GRAY));
            } else {
                assemblyComponent = Component.translatable("commands.jiaguinfo.assembly")
                        .append(Component.literal(assemblable).withStyle(ChatFormatting.GRAY));
            }
            source.sendSuccess(() -> assemblyComponent, false);
        }

        return 1;
    }
}
