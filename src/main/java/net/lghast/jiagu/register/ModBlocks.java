package net.lghast.jiagu.register;

import net.lghast.jiagu.JiaguReappear;
import net.lghast.jiagu.block.*;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(JiaguReappear.MOD_ID);

    public static final DeferredBlock<Block> GOLDEN_BRICKS = registerBlock("golden_bricks",
            ()-> new GoldBricksBlock(BlockBehaviour.Properties.of().mapColor(MapColor.GOLD)
                    .strength(3.0f,6.0f).sound(SoundType.METAL).instrument(NoteBlockInstrument.BELL).requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> CHARACTER_DISASSEMBLER = registerBlock("character_disassembler",
            ()-> new CharacterDisassemblerBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE)
                    .strength(3.5f,3.5f).sound(SoundType.COPPER).requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> CANGJIE_DING_TRIPOD = registerBlock("cangjie_ding_tripod",
            ()-> new CangjieDingTripodBlock(BlockBehaviour.Properties.of().mapColor(MapColor.GOLD)
                    .strength(3.5f,6f).sound(SoundType.COPPER).requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> RUBBING_TABLE = registerBlock("rubbing_table",
            ()-> new RubbingBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_GRAY)
                    .strength(2.5f,2.5f).sound(SoundType.WOOD)));

    public static final DeferredBlock<Block> SOUR_BERRY_BUSH = registerBlockWithoutItem("sour_berry_bush",
            ()-> new SourBerryBushBlock(BlockBehaviour.Properties.of()
                    .sound(SoundType.SWEET_BERRY_BUSH).mapColor(MapColor.PLANT)
                    .randomTicks().noCollission().pushReaction(PushReaction.DESTROY)
            ));

    public static final DeferredBlock<Block> SHADOW_CAVE_VINES = registerBlockWithoutItem("shadow_cave_vines",
            ()-> new ShadowCaveVinesBlock(BlockBehaviour.Properties.of()
                    .sound(SoundType.CAVE_VINES).mapColor(MapColor.PLANT)
                    .randomTicks().noCollission().pushReaction(PushReaction.DESTROY).instabreak()
            ));

    public static final DeferredBlock<Block> SHADOW_CAVE_VINES_PLANT = registerBlockWithoutItem("shadow_cave_vines_plant",
            ()-> new ShadowCaveVinesPlantBlock(BlockBehaviour.Properties.of()
                    .sound(SoundType.CAVE_VINES).mapColor(MapColor.PLANT)
                    .randomTicks().noCollission().pushReaction(PushReaction.DESTROY).instabreak()
            ));

    public static final DeferredBlock<Block> YOLIME_BLOCK = registerBlock("yolime_block",
            ()-> new YolimeBlock(BlockBehaviour.Properties.of()
                    .sound(SoundType.SLIME_BLOCK).noOcclusion().mapColor(MapColor.COLOR_PINK)));

    public static final DeferredBlock<Block> LUCKY_JIAGU_BLOCK = registerBlock("lucky_jiagu_block",
            ()-> new LuckyJiaguBlockBasic(BlockBehaviour.Properties.of().mapColor(MapColor.STONE)
                    .sound(SoundType.BONE_BLOCK).noLootTable()));

    public static final DeferredBlock<Block> LUCKY_JIAGU_BLOCK_IRON = registerBlock("lucky_jiagu_block_iron",
            ()-> new LuckyJiaguBlockIron(BlockBehaviour.Properties.of().mapColor(MapColor.METAL)
                    .sound(SoundType.METAL).noLootTable()));

    public static final DeferredBlock<Block> LUCKY_JIAGU_BLOCK_COPPER = registerBlock("lucky_jiagu_block_copper",
            ()-> new LuckyJiaguBlockCopper(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE)
                    .sound(SoundType.COPPER).noLootTable()));

    public static final DeferredBlock<Block> LUCKY_JIAGU_BLOCK_GOLD = registerBlock("lucky_jiagu_block_gold",
            ()-> new LuckyJiaguBlockGold(BlockBehaviour.Properties.of().mapColor(MapColor.GOLD)
                    .sound(SoundType.METAL).noLootTable()));

    public static final DeferredBlock<Block> AUTO_DISASSEMBLER = registerBlock("auto_disassembler",
            ()-> new AutoDisassemblerBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE)
                    .sound(SoundType.COPPER).strength(3.5f,3.5f).requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> CANGJIE_MORPHER = registerBlock("cangjie_morpher",
            ()-> new CangjieMorpherBlock(BlockBehaviour.Properties.of().mapColor(MapColor.GOLD)
                    .strength(3.5f,6f).sound(SoundType.COPPER).requiresCorrectToolForDrops()));

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block){
        DeferredBlock<T> toReturn = BLOCKS.register(name,block);
        registerBlockItem(name,toReturn);
        return toReturn;
    }

    private static <T extends Block> DeferredBlock<T> registerBlockWithoutItem(String name, Supplier<T> block){
        DeferredBlock<T> toReturn = BLOCKS.register(name,block);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block){
        ModItems.ITEMS.register(name,()-> new BlockItem(block.get(),new Item.Properties()));
    }

    public static void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }
}
