package net.lghast.jiagu.register.system;

import net.lghast.jiagu.JiaguReappear;
import net.lghast.jiagu.character.CharacterInfo;
import net.lghast.jiagu.common.content.item.CharacterItem;
import net.lghast.jiagu.common.content.item.PrescriptionItem;
import net.lghast.jiagu.common.content.item.TaoistTalismanItem;
import net.lghast.jiagu.register.content.ModBlocks;
import net.lghast.jiagu.register.content.ModItems;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, JiaguReappear.MOD_ID);

    public static final Supplier<CreativeModeTab> JIAGU_ITEMS_TAB = CREATIVE_MODE_TAB.register("jiagu_items_tab",
            ()-> CreativeModeTab.builder().icon(()->new ItemStack(ModItems.BONE_LAMELLA.get()))
                    .title(Component.translatable("creativetab.jiagureappear.jiagu_items"))
                    .displayItems((itemDisplayParameters,output)->{
                        output.accept(ModItems.CHARACTER_ITEM);
                        output.accept(ModItems.BONE_LAMELLA);
                        output.accept(ModItems.TURTLE_PLASTRON);
                        output.accept(ModItems.DICTIONARY);
                        output.accept(ModItems.EMPTY_PRESCRIPTION);
                        output.accept(ModItems.PRESCRIPTION);
                        output.accept(ModItems.YELLOW_PAPER);
                        output.accept(ModItems.TAOIST_TALISMAN);
                        output.accept(ModBlocks.CHARACTER_DISASSEMBLER);
                        output.accept(ModBlocks.CANGJIE_DING_TRIPOD);
                        output.accept(ModBlocks.RUBBING_TABLE);
                        output.accept(ModBlocks.AUTO_DISASSEMBLER);
                        output.accept(ModBlocks.CANGJIE_MORPHER);
                        output.accept(ModBlocks.RUBBING_MACHINE);
                        output.accept(ModBlocks.YAOWANG_GOURD);
                        output.accept(ModBlocks.WENCHANG_ALTAR);
                        output.accept(ModBlocks.ERUDITE_WENCHANG_ALTAR);
                        output.accept(ModItems.AMETHYST_UPGRADE_SMITHING_TEMPLATE);
                        output.accept(ModItems.NI_CONVERSE_AMETHYST);
                        output.accept(ModItems.NI_CONVERSE_AMETHYST_DORMANT);
                        output.accept(ModItems.JIAN_SWORD_AMETHYST);
                        output.accept(ModItems.YI_CONFLAGRANT_AMETHYST);
                        output.accept(ModItems.BIAO_GALE_AMETHYST);
                        output.accept(ModItems.YI_CURE_AMETHYST);
                        output.accept(ModItems.GU_PARASITE_AMETHYST);
                        output.accept(ModItems.LEI_STONES_AMETHYST);
                        output.accept(ModItems.SOUR_BERRIES);
                        output.accept(ModItems.SHADOW_BERRIES);
                        output.accept(ModItems.ZUCCHINI);
                        output.accept(ModItems.COOKED_ZUCCHINI);
                        output.accept(ModItems.ZUCCHINI_SOUP);
                        output.accept(ModItems.YOLIME);
                        output.accept(ModItems.YOLIME_BREAD);
                        output.accept(ModBlocks.YOLIME_BLOCK);
                        output.accept(ModBlocks.GOLDEN_BRICKS);
                        output.accept(ModItems.INFINITE_PAPYRUS);
                        output.accept(ModItems.CANGJIE_JADE_BURIN);
                        output.accept(ModBlocks.LUCKY_JIAGU_BLOCK);
                        output.accept(ModBlocks.LUCKY_JIAGU_BLOCK_IRON);
                        output.accept(ModBlocks.LUCKY_JIAGU_BLOCK_COPPER);
                        output.accept(ModBlocks.LUCKY_JIAGU_BLOCK_GOLD);
                        output.accept(ModBlocks.LUCKY_JIAGU_BLOCK_DIAMOND);
                    }).build());

    public static final Supplier<CreativeModeTab> JIAGU_CHARACTERS_TAB = CREATIVE_MODE_TAB.register("jiagu_characters_tab",
            ()-> CreativeModeTab.builder().icon(()->{
                        ItemStack icon = new ItemStack(ModItems.CHARACTER_ITEM.get());
                        CharacterItem.setInscription(icon, "土");
                        return icon;
                    })
                    .title(Component.translatable("creativetab.jiagureappear.jiagu_characters"))
                    .displayItems((itemDisplayParameters,output)->{
                        List<String> stone = new ArrayList<>();
                        List<String> copper = new ArrayList<>();
                        for (Map.Entry<String, CharacterInfo.CharacterData> entry : CharacterInfo.CHARACTER_DATA.entrySet()) {
                            int value = entry.getValue().intValue();
                            if (value < 1000) {
                                if (!CharacterInfo.canBeDisassembled(entry.getKey())) {
                                    stone.add(entry.getKey());
                                }
                            } else if (value >= 6000 && value < 7000) {
                                copper.add(entry.getKey());
                            }
                        }
                        stone.sort(Comparator.comparingInt(CharacterInfo::getIntValue));
                        copper.sort(Comparator.comparingInt(CharacterInfo::getIntValue));

                        for (String inscription : stone) {
                            output.accept(makeCharacterStack(inscription));
                        }
                        for (String inscription : copper) {
                            output.accept(makeCharacterStack(inscription));
                        }
                    }).build());

    public static final Supplier<CreativeModeTab> PRESCRIPTION_TALISMAN_TAB = CREATIVE_MODE_TAB.register("prescription_talisman_tab",
            ()-> CreativeModeTab.builder().icon(()->{
                        ItemStack icon = new ItemStack(ModItems.PRESCRIPTION.get());
                        MobEffect saturation = BuiltInRegistries.MOB_EFFECT.get(
                                ResourceLocation.parse("minecraft:saturation"));
                        if (saturation != null) {
                            PrescriptionItem.setEffect(icon, BuiltInRegistries.MOB_EFFECT.wrapAsHolder(saturation));
                        }
                        return icon;
                    })
                    .title(Component.translatable("creativetab.jiagureappear.prescriptions_talismans"))
                    .displayItems((itemDisplayParameters,output)->{
                        for (Holder.Reference<MobEffect> effectHolder : BuiltInRegistries.MOB_EFFECT.holders().toList()) {
                            ItemStack stack = new ItemStack(ModItems.PRESCRIPTION.get());
                            PrescriptionItem.setEffect(stack, effectHolder);
                            output.accept(stack);
                        }

                        var enchantments = itemDisplayParameters.holders().lookupOrThrow(Registries.ENCHANTMENT);
                        for (Holder.Reference<Enchantment> enchantmentHolder : enchantments.listElements().toList()) {
                            ItemStack stack = new ItemStack(ModItems.TAOIST_TALISMAN.get());
                            TaoistTalismanItem.setSpell(stack, enchantmentHolder, 1);
                            output.accept(stack);
                        }
                    }).build());

    private static ItemStack makeCharacterStack(String inscription) {
        ItemStack stack = new ItemStack(ModItems.CHARACTER_ITEM.get());
        CharacterItem.setInscription(stack, inscription);
        return stack;
    }

    public static void register(IEventBus eventBus){
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
