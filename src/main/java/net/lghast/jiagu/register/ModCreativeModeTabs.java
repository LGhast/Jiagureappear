package net.lghast.jiagu.register;

import net.lghast.jiagu.JiaguReappear;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, JiaguReappear.MOD_ID);

    public static final Supplier<CreativeModeTab> JIAGU_ITEMS_TAB = CREATIVE_MODE_TAB.register("jiagu_items_tab",
            ()-> CreativeModeTab.builder().icon(()->new ItemStack(ModItems.INFINITE_PAPYRUS.get()))
                    .title(Component.translatable("creativetab.jiagureappear.jiagu_items"))
                    .displayItems((itemDisplayParameters,output)->{
                        output.accept(ModItems.BONE_LAMELLA);
                        output.accept(ModItems.TURTLE_PLASTRON);
                        output.accept(ModItems.INFINITE_PAPYRUS);
                        output.accept(ModItems.CHARACTER_ITEM);
                        output.accept(ModItems.YELLOW_PAPER);
                        output.accept(ModItems.TAOIST_TALISMAN);
                        output.accept(ModItems.EMPTY_PRESCRIPTION);
                        output.accept(ModItems.PRESCRIPTION);
                        output.accept(ModItems.AMETHYST_UPGRADE_SMITHING_TEMPLATE);
                        output.accept(ModItems.NI_CONVERSE_AMETHYST);
                        output.accept(ModItems.NI_CONVERSE_AMETHYST_DORMANT);
                        output.accept(ModItems.JIAN_SWORD_AMETHYST);
                        output.accept(ModItems.YI_CONFLAGRANT_AMETHYST);
                        output.accept(ModItems.BIAO_GALE_AMETHYST);
                        output.accept(ModItems.YI_CURE_AMETHYST);
                        output.accept(ModItems.GU_PARASITE_AMETHYST);
                        output.accept(ModItems.YOLIME);
                        output.accept(ModItems.YOLIME_BREAD);
                        output.accept(ModBlocks.YOLIME_BLOCK);
                        output.accept(ModItems.SHADOW_BERRIES);
                        output.accept(ModItems.SOUR_BERRIES);
                        output.accept(ModBlocks.CHARACTER_DISASSEMBLER);
                        output.accept(ModBlocks.AUTO_DISASSEMBLER);
                        output.accept(ModBlocks.CANGJIE_DING_TRIPOD);
                        output.accept(ModBlocks.CANGJIE_MORPHER);
                        output.accept(ModBlocks.RUBBING_TABLE);
                        output.accept(ModBlocks.RUBBING_MACHINE);
                        output.accept(ModBlocks.WENCHANG_ALTAR);
                        output.accept(ModBlocks.ERUDITE_WENCHANG_ALTAR);
                        output.accept(ModBlocks.YAOWANG_GOURD);
                        output.accept(ModBlocks.GOLDEN_BRICKS);
                        output.accept(ModItems.CANGJIE_JADE_BURIN);
                        output.accept(ModBlocks.LUCKY_JIAGU_BLOCK);
                        output.accept(ModBlocks.LUCKY_JIAGU_BLOCK_IRON);
                        output.accept(ModBlocks.LUCKY_JIAGU_BLOCK_COPPER);
                        output.accept(ModBlocks.LUCKY_JIAGU_BLOCK_GOLD);
                    }).build());

    public static void register(IEventBus eventBus){
        CREATIVE_MODE_TAB.register(eventBus);
    }
}

