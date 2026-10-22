package net.lghast.jiagu.common.system.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.lghast.jiagu.common.content.item.PrescriptionItem;
import net.lghast.jiagu.register.content.ModItems;
import net.lghast.jiagu.utils.ModUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
@EventBusSubscriber
public class PrescribeCommand {
    private static final String ALL_KEY = "all";

    @SubscribeEvent
    public static void registerCommand(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(Commands.literal("prescribe")
                .requires(source -> source.hasPermission(2))
                .then(Commands.argument("effect", ResourceLocationArgument.id())
                        .suggests(PrescribeCommand::suggestEffects)
                        .executes(context -> prescribe(
                                context,
                                ResourceLocationArgument.getId(context, "effect"),
                                Collections.singleton(context.getSource().getPlayerOrException())
                        ))
                        .then(Commands.argument("targets", EntityArgument.players())
                                .executes(context -> prescribe(
                                        context,
                                        ResourceLocationArgument.getId(context, "effect"),
                                        EntityArgument.getPlayers(context, "targets")
                                ))
                        )));
    }

    private static CompletableFuture<Suggestions> suggestEffects(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        List<String> names = new ArrayList<>();
        names.add(ALL_KEY);
        BuiltInRegistries.MOB_EFFECT.keySet().stream()
                .map(ResourceLocation::toString)
                .forEach(names::add);
        return SharedSuggestionProvider.suggest(names, builder);
    }

    private static int prescribe(CommandContext<CommandSourceStack> context, ResourceLocation effectId, Collection<ServerPlayer> targets) {
        CommandSourceStack source = context.getSource();

        if (effectId.getNamespace().equals(ResourceLocation.DEFAULT_NAMESPACE) && effectId.getPath().equals(ALL_KEY)) {
            List<Holder.Reference<MobEffect>> effects = BuiltInRegistries.MOB_EFFECT.holders()
                    .filter(holder -> ModUtils.modifyName(holder.value()) != null)
                    .toList();
            for (ServerPlayer player : targets) {
                for (Holder.Reference<MobEffect> holder : effects) {
                    givePrescription(player, holder);
                }
            }
            if (targets.size() == 1) {
                source.sendSuccess(() -> Component.translatable("commands.prescribe.all.single",
                        effects.size(), targets.iterator().next().getDisplayName()), true);
            } else {
                source.sendSuccess(() -> Component.translatable("commands.prescribe.all.multiple",
                        targets.size(), effects.size()), true);
            }
            return effects.size() * targets.size();
        }

        Holder<MobEffect> holder = resolveEffect(effectId);
        if (holder == null) {
            source.sendFailure(Component.translatable("commands.prescribe.invalid", effectId.toString()));
            return 0;
        }

        for (ServerPlayer player : targets) {
            givePrescription(player, holder);
        }

        Component effectName = holder.value().getDisplayName();
        if (targets.size() == 1) {
            source.sendSuccess(() -> Component.translatable("commands.prescribe.success.single",
                    effectName, targets.iterator().next().getDisplayName()), true);
        } else {
            source.sendSuccess(() -> Component.translatable("commands.prescribe.success.multiple",
                    targets.size(), effectName), true);
        }
        return targets.size();
    }

    private static Holder<MobEffect> resolveEffect(ResourceLocation location) {
        MobEffect effect = BuiltInRegistries.MOB_EFFECT.get(location);
        return effect == null ? null : BuiltInRegistries.MOB_EFFECT.wrapAsHolder(effect);
    }

    private static void givePrescription(ServerPlayer player, Holder<MobEffect> holder) {
        ItemStack stack = new ItemStack(ModItems.PRESCRIPTION.get());
        PrescriptionItem.setEffect(stack, holder);
        if (!player.addItem(stack) && !stack.isEmpty()) {
            player.drop(stack, false);
        }
    }
}
