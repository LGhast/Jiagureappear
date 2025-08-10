package net.lghast.jiagu.datagen;

import net.lghast.jiagu.JiaguReappear;
import net.lghast.jiagu.register.ModBlocks;
import net.lghast.jiagu.register.ModItems;
import net.lghast.jiagu.register.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends ItemTagsProvider {
    public ModItemTagProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, CompletableFuture<TagLookup<Block>> pBlockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, pLookupProvider, pBlockTags, JiaguReappear.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider pProvider) {
        tag(ItemTags.PIGLIN_LOVED).add(ModBlocks.GOLDEN_BRICKS.asItem());

        tag(ItemTags.FOX_FOOD).add(ModItems.SOUR_BERRIES.asItem(),ModItems.SHADOW_BERRIES.asItem());


        tag(ModTags.FOODS_BREAD).add(ModItems.YOLIME_BREAD.asItem());

        tag(ModTags.FOODS_BERRY).add(ModItems.SOUR_BERRIES.asItem(),ModItems.SHADOW_BERRIES.asItem());

        tag(ModTags.INVALID_TO_CHARACTERS)
                .add(ModItems.CHARACTER_ITEM.asItem())
                .addTag(ModTags.AMETHYST_INSCRIPTIONS)
        ;

        tag(ModTags.INVALID_TO_BE_RUBBED)
                .add(ModItems.CHARACTER_ITEM.asItem())
                .add(ModItems.TAOIST_TALISMAN.asItem())
                .add(ModItems.PRESCRIPTION.asItem())
                .addTag(ModTags.AMETHYST_INSCRIPTIONS)
                .add(Items.POTION)
                .add(Items.TIPPED_ARROW)
                .add(Items.ENCHANTED_BOOK)
                .add(Items.OMINOUS_BOTTLE)
        ;

        tag(ModTags.RUBBING_INKS)
                .add(Items.INK_SAC)
                .add(Items.GLOW_INK_SAC)
                .add(Items.BLACK_DYE)
                .add(Items.CHARCOAL)
        ;

        tag(ModTags.JIAGU_MATERIALS)
                .add(ModItems.INFINITE_PAPYRUS.asItem())
                .add(ModItems.BONE_LAMELLA.asItem())
                .add(ModItems.TURTLE_PLASTRON.asItem())
                .add(Items.TURTLE_HELMET)
                .add(ModItems.YELLOW_PAPER.asItem())
        ;

        tag(ModTags.JIAGU_LUCKY_BLOCK_ITEMS)
                .add(ModBlocks.LUCKY_JIAGU_BLOCK.asItem())
                .add(ModBlocks.LUCKY_JIAGU_BLOCK_IRON.asItem())
                .add(ModBlocks.LUCKY_JIAGU_BLOCK_GOLD.asItem())
                .add(ModBlocks.LUCKY_JIAGU_BLOCK_COPPER.asItem())
        ;

        tag(ModTags.AMETHYST_INSCRIPTIONS)
                .add(ModItems.NI_CONVERSE_AMETHYST.asItem())
                .add(ModItems.NI_CONVERSE_AMETHYST_DORMANT.asItem())
                .add(ModItems.JIAN_SWORD_AMETHYST.asItem())
                .add(ModItems.YI_CONFLAGRANT_AMETHYST.asItem())
                .add(ModItems.BIAO_GALE_AMETHYST.asItem())
                .add(ModItems.YI_CURE_AMETHYST.asItem())
        ;

        tag(ModTags.WENCHANG_ALTAR_DISPLAYABLE)
                .add(ModItems.CHARACTER_ITEM.asItem())
                .add(ModItems.TAOIST_TALISMAN.asItem())
                .add(ModItems.PRESCRIPTION.asItem())
                .addTag(ModTags.AMETHYST_INSCRIPTIONS)
        ;

        tag(ModTags.HERBS)
                .addTag(ItemTags.FLOWERS)
                .addTag(ItemTags.LEAVES)
                .addTag(ItemTags.SAPLINGS)
                .add(Items.SHORT_GRASS)
                .add(Items.TALL_GRASS)
                .add(Items.FERN)
                .add(Items.LARGE_FERN)
                .add(Items.SMALL_DRIPLEAF)
                .add(Items.BIG_DRIPLEAF)
                .add(Items.KELP)
                .add(Items.SEAGRASS)
                .add(Items.GLOW_LICHEN)
                .add(Items.CACTUS)
                .add(Items.SUGAR_CANE)
                .add(Items.LILY_PAD)
                .addTag(ModTags.FOODS_BERRY)
        ;

        tag(ModTags.IGNITING_ENCHANTABLE).add(ModItems.YI_CONFLAGRANT_AMETHYST.asItem());
        tag(ModTags.CREMATION_ENCHANTABLE).add(ModItems.YI_CONFLAGRANT_AMETHYST.asItem());
        tag(ModTags.INQUISITIVENESS_ENCHANTABLE).add(ModItems.JIAN_SWORD_AMETHYST.asItem());
        tag(ModTags.FLURRY_ENCHANTABLE).add(ModItems.BIAO_GALE_AMETHYST.asItem());
        tag(ModTags.WUTHERING_ENCHANTABLE).add(ModItems.BIAO_GALE_AMETHYST.asItem());
        tag(ModTags.BENEVOLENCE_ENCHANTABLE).add(ModItems.YI_CURE_AMETHYST.asItem());
        tag(ModTags.HIPPOCRATES_ENCHANTABLE).add(ModItems.YI_CURE_AMETHYST.asItem());
        tag(ModTags.PANACEA_ENCHANTABLE)
                .add(ModItems.YI_CURE_AMETHYST.asItem())
                .add(ModItems.GU_PARASITE_AMETHYST.asItem());
        tag(ModTags.MALADY_ENCHANTABLE).add(ModItems.GU_PARASITE_AMETHYST.asItem());
    }
}
