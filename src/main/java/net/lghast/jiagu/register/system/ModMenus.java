package net.lghast.jiagu.register.system;

import net.lghast.jiagu.JiaguReappear;
import net.lghast.jiagu.common.content.blockentity.YaowangGourdBlockEntity;
import net.lghast.jiagu.common.system.menu.DictionaryMenu;
import net.lghast.jiagu.common.system.menu.PrescriptionMenu;
import net.lghast.jiagu.common.system.menu.YaowangGourdMenu;
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

    public static final Supplier<MenuType<PrescriptionMenu>> PRESCRIPTION_MENU =
            MENUS.register("prescription_menu", () ->
                    IMenuTypeExtension.create((windowId, inv, data) ->
                            new PrescriptionMenu(windowId, inv, ItemStack.EMPTY)));

    public static final Supplier<MenuType<DictionaryMenu>> DICTIONARY_MENU =
            MENUS.register("dictionary_menu", () ->
                    IMenuTypeExtension.create((windowId, inv, data) ->
                            new DictionaryMenu(windowId, inv)));

    public static final Supplier<MenuType<YaowangGourdMenu>> YAOWANG_GOURD_MENU =
            MENUS.register("yaowang_gourd_menu", () ->
                    IMenuTypeExtension.create((windowId, inv, data) ->
                            new YaowangGourdMenu(windowId, inv, (YaowangGourdBlockEntity) inv.player.level().getBlockEntity(data.readBlockPos()))));

    public static void register(IEventBus eventBus){
        MENUS.register(eventBus);
    }
}
