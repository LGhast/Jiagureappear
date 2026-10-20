package net.lghast.jiagu.register.system;

import net.lghast.jiagu.JiaguReappear;
import net.lghast.jiagu.common.content.blockentity.*;
import net.lghast.jiagu.register.content.ModBlockEntities;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

@SuppressWarnings("unused")
@EventBusSubscriber(modid = JiaguReappear.MOD_ID)
public class ModCapabilities {
    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.AUTO_DISASSEMBLER.get(),
                AutoDisassemblerBlockEntity::getItemHandler
        );
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.CANGJIE_MORPHER.get(),
                CangjieMorpherBlockEntity::getItemHandler
        );
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.RUBBING_MACHINE.get(),
                RubbingMachineBlockEntity::getItemHandler
        );
    }
}
