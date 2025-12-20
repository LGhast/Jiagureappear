package net.lghast.jiagu.client.misc;

import net.lghast.jiagu.JiaguReappear;
import net.lghast.jiagu.client.particle.JiaguFloatingParticles;
import net.lghast.jiagu.client.particle.JiaguParticles;
import net.lghast.jiagu.client.particle.ModParticles;
import net.lghast.jiagu.client.screen.DictionaryScreen;
import net.lghast.jiagu.client.screen.PrescriptionScreen;
import net.lghast.jiagu.client.screen.YaowangGourdScreen;
import net.lghast.jiagu.common.content.item.PrescriptionUserItem;
import net.lghast.jiagu.network.OpenPrescriptionGuiPayload;
import net.lghast.jiagu.register.system.ModMenus;
import net.lghast.jiagu.register.system.ModTags;
import net.lghast.jiagu.utils.ModUtils;
import net.lghast.jiagu.utils.lzh.LzhMappings;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import java.util.List;
import java.util.Objects;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = JiaguReappear.MOD_ID)
public class ClientEventHandler {

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.level == null) return;

        while (ModKeyBindings.OPEN_PRESCRIPTION_GUI.consumeClick()) {
            if (minecraft.player.getMainHandItem().getItem() instanceof PrescriptionUserItem) {
                Objects.requireNonNull(minecraft.getConnection()).send(new OpenPrescriptionGuiPayload());
            }
        }
    }

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();

        if(!stack.is(ModTags.WENCHANG_ALTAR_DISPLAYABLE)) {
            List<Component> tooltip = event.getToolTip();
            int insertIndex = findInsertIndex(tooltip);

            String name = LzhMappings.getLzhName(stack);
            if(name == null) return;

            if(stack.is(Items.ENCHANTED_BOOK)){
                var componentType = EnchantmentHelper.getComponentType(stack);
                ItemEnchantments enchantments = stack.getOrDefault(componentType, ItemEnchantments.EMPTY);

                if(!enchantments.isEmpty()) {
                    var entry = enchantments.entrySet().iterator().next();
                    Holder<Enchantment> enchantmentHolder = entry.getKey();
                    int level = entry.getIntValue();

                    String enchantName = ModUtils.modifyName(enchantmentHolder, level);
                    if(enchantName == null) {
                        tooltip.add(insertIndex, Component.translatable("tooltip.jiagureappear.lzh", name)
                                .withStyle(ChatFormatting.GRAY));
                        return;
                    };

                    tooltip.add(insertIndex, Component.translatable("tooltip.jiagureappear.lzh_appended", name, enchantName)
                            .withStyle(ChatFormatting.GRAY));
                    return;
                }
            }

            tooltip.add(insertIndex, Component.translatable("tooltip.jiagureappear.lzh", name)
                    .withStyle(ChatFormatting.GRAY));
        }
    }

    private static int findInsertIndex(List<Component> tooltip) {
        for (int i = 0; i < tooltip.size(); i++) {
            Component line = tooltip.get(i);
            String content = line.getString();

            if (content.trim().isEmpty()) {
                continue;
            }

            if (content.contains(Component.translatable("item.durability").getString())) {
                return i;
            }
        }

        return Math.max(1, tooltip.size());
    }

    @SubscribeEvent
    public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticles.JIAGU_PARTICLES.get(), JiaguParticles.Provider::new);
        event.registerSpriteSet(ModParticles.JIAGU_FLOATING_PARTICLES.get(), JiaguFloatingParticles.Provider::new);
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenus.PRESCRIPTION_MENU.get(), PrescriptionScreen::new);
        event.register(ModMenus.DICTIONARY_MENU.get(), DictionaryScreen::new);
        event.register(ModMenus.YAOWANG_GOURD_MENU.get(), YaowangGourdScreen::new);
    }
}
