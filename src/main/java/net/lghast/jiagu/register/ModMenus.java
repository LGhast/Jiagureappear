package net.lghast.jiagu.register;

import net.lghast.jiagu.JiaguReappear;
import net.lghast.jiagu.common.menu.PrescriptionMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
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
                            new PrescriptionMenu(windowId, inv)));

    public static void register(IEventBus eventBus){
        MENUS.register(eventBus);
    }
}
