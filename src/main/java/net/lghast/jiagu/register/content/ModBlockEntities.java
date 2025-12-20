package net.lghast.jiagu.register.content;

import net.lghast.jiagu.JiaguReappear;
import net.lghast.jiagu.common.content.blockentity.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, JiaguReappear.MOD_ID);

    public static final Supplier<BlockEntityType<AutoDisassemblerBlockEntity>> AUTO_DISASSEMBLER = BLOCK_ENTITY_TYPES.register(
            "auto_disassembler",
            () -> BlockEntityType.Builder.of(
                            AutoDisassemblerBlockEntity::new,
                            ModBlocks.AUTO_DISASSEMBLER.get()
                    )
                    .build(null)
    );

    public static final Supplier<BlockEntityType<CangjieMorpherBlockEntity>> CANGJIE_MORPHER = BLOCK_ENTITY_TYPES.register(
            "cangjie_morpher",
            () -> BlockEntityType.Builder.of(
                            CangjieMorpherBlockEntity::new,
                            ModBlocks.CANGJIE_MORPHER.get()
                    )
                    .build(null)
    );
    public static final Supplier<BlockEntityType<RubbingMachineBlockEntity>> RUBBING_MACHINE = BLOCK_ENTITY_TYPES.register(
            "rubbing_machine",
            () -> BlockEntityType.Builder.of(
                            RubbingMachineBlockEntity::new,
                            ModBlocks.RUBBING_MACHINE.get()
                    )
                    .build(null)
    );

    public static final Supplier<BlockEntityType<WenchangAltarBlockEntity>> WENCHANG_ALTAR = BLOCK_ENTITY_TYPES.register(
            "wenchang_altar",
            () -> BlockEntityType.Builder.of(
                            WenchangAltarBlockEntity::new,
                            ModBlocks.WENCHANG_ALTAR.get()
                    )
                    .build(null)
    );

    public static final Supplier<BlockEntityType<EruditeWenchangAltarBlockEntity>> ERUDITE_WENCHANG_ALTAR = BLOCK_ENTITY_TYPES.register(
            "erudite_wenchang_altar",
            () -> BlockEntityType.Builder.of(
                            EruditeWenchangAltarBlockEntity::new,
                            ModBlocks.ERUDITE_WENCHANG_ALTAR.get()
                    )
                    .build(null)
    );

    public static final Supplier<BlockEntityType<YaowangGourdBlockEntity>> YAOWANG_GOURD = BLOCK_ENTITY_TYPES.register(
            "yaowang_gourd",
            () -> BlockEntityType.Builder.of(
                            YaowangGourdBlockEntity::new,
                            ModBlocks.YAOWANG_GOURD.get()
                    )
                    .build(null)
    );

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITY_TYPES.register(eventBus);
    }
}
