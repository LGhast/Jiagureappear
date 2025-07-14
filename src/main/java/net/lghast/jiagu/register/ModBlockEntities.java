package net.lghast.jiagu.register;

import com.mojang.datafixers.types.Type;
import net.lghast.jiagu.JiaguReappear;
import net.lghast.jiagu.block.entity.AutoDisassemblerBlockEntity;
import net.lghast.jiagu.block.entity.CangjieMorpherBlockEntity;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.CrafterBlockEntity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

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

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITY_TYPES.register(eventBus);
    }
}
