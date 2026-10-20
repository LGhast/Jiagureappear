package net.lghast.jiagu.register.system;

import net.lghast.jiagu.JiaguReappear;
import net.lghast.jiagu.common.content.blockentity.*;
import net.lghast.jiagu.common.system.menu.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, JiaguReappear.MOD_ID);

    public static final Supplier<MenuType<CharacterCrafterMenu>> CHARACTER_CRAFTER_MENU =
            MENUS.register("character_crafter_menu", () ->
                    IMenuTypeExtension.create((windowId, inv, data) ->
                            new CharacterCrafterMenu(windowId, inv)));

    public static final Supplier<MenuType<PrescriptionMenu>> PRESCRIPTION_MENU =
            MENUS.register("prescription_menu", () ->
                    IMenuTypeExtension.create((windowId, inv, data) ->
                            new PrescriptionMenu(windowId, inv, ItemStack.EMPTY)));

    public static final Supplier<MenuType<DictionaryMenu>> DICTIONARY_MENU =
            MENUS.register("dictionary_menu", () ->
                    IMenuTypeExtension.create((windowId, inv, data) ->
                            new DictionaryMenu(windowId)));

    public static final Supplier<MenuType<AutoDisassemblerMenu>> AUTO_DISASSEMBLER_MENU =
            MENUS.register("auto_disassembler_menu", () ->
                    IMenuTypeExtension.create((windowId, inv, data) ->
                            new AutoDisassemblerMenu(windowId, inv,
                                    (AutoDisassemblerBlockEntity) inv.player.level().getBlockEntity(data.readBlockPos()))));

    public static final Supplier<MenuType<CangjieMorpherMenu>> CANGJIE_MORPHER_MENU =
            MENUS.register("cangjie_morpher_menu", () ->
                    IMenuTypeExtension.create((windowId, inv, data) ->
                            new CangjieMorpherMenu(windowId, inv,
                                    (CangjieMorpherBlockEntity) inv.player.level().getBlockEntity(data.readBlockPos()))));

    public static final Supplier<MenuType<RubbingMachineMenu>> RUBBING_MACHINE_MENU =
            MENUS.register("rubbing_machine_menu", () ->
                    IMenuTypeExtension.create((windowId, inv, data) ->
                            new RubbingMachineMenu(windowId, inv,
                                    (RubbingMachineBlockEntity) inv.player.level().getBlockEntity(data.readBlockPos()))));

    public static final Supplier<MenuType<YaowangGourdMenu>> YAOWANG_GOURD_MENU =
            MENUS.register("yaowang_gourd_menu", () ->
                    IMenuTypeExtension.create((windowId, inv, data) ->
                            new YaowangGourdMenu(windowId, inv, (YaowangGourdBlockEntity) inv.player.level().getBlockEntity(data.readBlockPos()))));

    public static void register(IEventBus eventBus){
        MENUS.register(eventBus);
    }
}
