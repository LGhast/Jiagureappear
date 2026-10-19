package net.lghast.jiagu.datagen;

import net.lghast.jiagu.register.content.ModBlocks;
import net.lghast.jiagu.register.content.ModItems;
import net.lghast.jiagu.register.system.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.concurrent.CompletableFuture;

import static net.lghast.jiagu.JiaguReappear.MOD_ID;

@ParametersAreNonnullByDefault
public class ModRecipeProvider extends RecipeProvider {
    private static final String CONVERSE_MULTI = "_from_conversion_multi";
    private static final String CONVERSE_SINGLE = "_from_conversion_single";

    public ModRecipeProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pRegistries) {
        super(pOutput, pRegistries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput){
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.YELLOW_PAPER.get(), 1)
                .requires(Items.YELLOW_DYE).requires(Items.PAPER)
                .unlockedBy("has_paper", has(Items.PAPER))
                .save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.DICTIONARY.get(), 1)
                .requires(Items.BLUE_DYE).requires(Items.INK_SAC).requires(Items.BOOK)
                .unlockedBy("has_bone_lamella", has(ModItems.BONE_LAMELLA))
                .unlockedBy("has_turtle_plastron", has(ModItems.TURTLE_PLASTRON))
                .save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.EMPTY_PRESCRIPTION.get(), 1)
                .requires(Items.INK_SAC).requires(Items.PAPER).requires(ModTags.HERBS)
                .unlockedBy("has_paper", has(Items.PAPER))
                .save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.YOLIME_BREAD.get(), 1)
                .requires(ModItems.YOLIME).requires(Items.BREAD)
                .unlockedBy("has_yolime", has(ModItems.YOLIME))
                .save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.AMETHYST_UPGRADE_SMITHING_TEMPLATE.get(), 1)
                .requires(Items.AMETHYST_CLUSTER).requires(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE)
                .unlockedBy("has_netherite_upgrade_smithing_template", has(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE))
                .save(recipeOutput,"amethyst_upgrade_smithing_template_from_netherite_one");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.ZUCCHINI_SOUP.get(), 1)
                .requires(ModItems.COOKED_ZUCCHINI).requires(Items.EGG).requires(Items.BOWL)
                .unlockedBy("has_cooked_zucchini", has(ModItems.COOKED_ZUCCHINI))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,ModItems.BONE_LAMELLA.get())
                .pattern(" BB")
                .pattern(" BB")
                .pattern(" BB")
                .define('B', Items.BONE_MEAL)
                .unlockedBy("has_bone_meal", has(Items.BONE_MEAL))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,ModBlocks.YOLIME_BLOCK.get())
                .pattern("BBB")
                .pattern("BBB")
                .pattern("BBB")
                .define('B', ModItems.YOLIME)
                .unlockedBy("has_yolime_block", has(ModBlocks.YOLIME_BLOCK))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,ModItems.YOLIME.get(), 9)
                .pattern("   ")
                .pattern(" B ")
                .pattern("   ")
                .define('B', ModBlocks.YOLIME_BLOCK)
                .unlockedBy("has_yolime", has(ModItems.YOLIME))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,ModBlocks.CHARACTER_DISASSEMBLER.get())
                .pattern(" I ")
                .pattern("CRC")
                .pattern("CBC")
                .define('I', Items.IRON_INGOT)
                .define('C', Items.COPPER_INGOT)
                .define('R',Items.REDSTONE)
                .define('B', Blocks.COPPER_BLOCK.asItem())
                .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,ModBlocks.CANGJIE_DING_TRIPOD.get())
                .pattern("G G")
                .pattern("CBC")
                .pattern("CDC")
                .define('G', Items.GOLD_INGOT)
                .define('C', Items.COPPER_INGOT)
                .define('D',Items.DIAMOND)
                .define('B', Blocks.COPPER_BLOCK.asItem())
                .unlockedBy("has_diamond", has(Items.DIAMOND))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,ModItems.AMETHYST_UPGRADE_SMITHING_TEMPLATE.get(), 2)
                .pattern("DAD")
                .pattern("DSD")
                .pattern("DDD")
                .define('A', ModItems.AMETHYST_UPGRADE_SMITHING_TEMPLATE)
                .define('D',Items.DIAMOND)
                .define('S', Items.AMETHYST_SHARD)
                .unlockedBy("has_amethyst_upgrade_smithing_template",
                        has(ModItems.AMETHYST_UPGRADE_SMITHING_TEMPLATE.get()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,ModItems.NI_CONVERSE_AMETHYST.get(), 1)
                .pattern(" A ")
                .pattern("ANA")
                .pattern(" A ")
                .define('A', Items.AMETHYST_SHARD)
                .define('N', ModItems.NI_CONVERSE_AMETHYST_DORMANT)
                .unlockedBy("has_ni_converse_amethyst_dormant",
                        has(ModItems.NI_CONVERSE_AMETHYST_DORMANT.get()))
                .save(recipeOutput);


        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,ModBlocks.GOLDEN_BRICKS.get(), 1)
                .pattern(" G ")
                .pattern("GBG")
                .pattern(" G ")
                .define('G', Items.GOLD_INGOT)
                .define('B', Blocks.POLISHED_BLACKSTONE_BRICKS)
                .unlockedBy("has_polished_blackstone_bricks",
                        has(Blocks.POLISHED_BLACKSTONE_BRICKS.asItem()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,ModBlocks.LUCKY_JIAGU_BLOCK_IRON.get(), 1)
                .pattern(" A ")
                .pattern("ANA")
                .pattern(" A ")
                .define('A', Items.IRON_INGOT)
                .define('N', ModBlocks.LUCKY_JIAGU_BLOCK)
                .unlockedBy("has_lucky_jiagu_block",
                        has(ModBlocks.LUCKY_JIAGU_BLOCK.get()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,ModBlocks.LUCKY_JIAGU_BLOCK_COPPER.get(), 1)
                .pattern(" A ")
                .pattern("ANA")
                .pattern(" A ")
                .define('A', Items.COPPER_INGOT)
                .define('N', ModBlocks.LUCKY_JIAGU_BLOCK)
                .unlockedBy("has_lucky_jiagu_block",
                        has(ModBlocks.LUCKY_JIAGU_BLOCK.get()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,ModBlocks.LUCKY_JIAGU_BLOCK_GOLD.get(), 1)
                .pattern(" A ")
                .pattern("ANA")
                .pattern(" A ")
                .define('A', Items.GOLD_INGOT)
                .define('N', ModBlocks.LUCKY_JIAGU_BLOCK_IRON)
                .unlockedBy("has_lucky_jiagu_block_iron",
                        has(ModBlocks.LUCKY_JIAGU_BLOCK_IRON.get()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,ModBlocks.LUCKY_JIAGU_BLOCK_DIAMOND.get(), 1)
                .pattern("   ")
                .pattern("ANA")
                .pattern("   ")
                .define('A', Items.DIAMOND)
                .define('N', ModBlocks.LUCKY_JIAGU_BLOCK_GOLD)
                .unlockedBy("has_lucky_jiagu_block_iron",
                        has(ModBlocks.LUCKY_JIAGU_BLOCK_GOLD.get()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,ModBlocks.RUBBING_TABLE.get())
                .pattern("PI ")
                .pattern("MM ")
                .pattern("WW ")
                .define('P', Items.PAPER)
                .define('I', Items.INK_SAC)
                .define('M',Items.IRON_INGOT)
                .define('W', ItemTags.PLANKS)
                .unlockedBy("has_ink_sac", has(Items.INK_SAC))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE,ModBlocks.AUTO_DISASSEMBLER.get(), 1)
                .pattern("III")
                .pattern("IDI")
                .pattern("CPC")
                .define('D', ModBlocks.CHARACTER_DISASSEMBLER)
                .define('I',Items.IRON_INGOT)
                .define('C', Items.COPPER_INGOT)
                .define('P', Items.DROPPER)
                .unlockedBy("has_character_disassembler",
                        has(ModBlocks.CHARACTER_DISASSEMBLER.get()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE,ModBlocks.CANGJIE_MORPHER.get(), 1)
                .pattern("III")
                .pattern("IDI")
                .pattern("RPR")
                .define('D', ModBlocks.CANGJIE_DING_TRIPOD)
                .define('I',Items.IRON_INGOT)
                .define('R', Items.REDSTONE)
                .define('P', Items.DROPPER)
                .unlockedBy("has_cangjie_ding_tripod",
                        has(ModBlocks.CANGJIE_DING_TRIPOD.get()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE,ModBlocks.RUBBING_MACHINE.get(), 1)
                .pattern("III")
                .pattern("IDI")
                .pattern("RPR")
                .define('D', ModBlocks.RUBBING_TABLE)
                .define('I',Items.IRON_INGOT)
                .define('R', Items.REDSTONE)
                .define('P', Items.DROPPER)
                .unlockedBy("has_rubbing_table",
                        has(ModBlocks.RUBBING_TABLE.get()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,ModBlocks.WENCHANG_ALTAR.get(), 1)
                .pattern("EDE")
                .pattern(" Q ")
                .pattern("EEE")
                .define('Q', Items.QUARTZ)
                .define('E', Items.EMERALD)
                .define('D', ModItems.DICTIONARY)
                .unlockedBy("has_dictionary",
                        has(ModItems.DICTIONARY))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,ModBlocks.ERUDITE_WENCHANG_ALTAR.get(), 1)
                .pattern(" D ")
                .pattern(" L ")
                .pattern(" W ")
                .define('D', Items.DIAMOND)
                .define('L', Items.LECTERN)
                .define('W', ModBlocks.WENCHANG_ALTAR)
                .unlockedBy("has_wenchang_altar",
                        has(ModBlocks.WENCHANG_ALTAR))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,ModBlocks.YAOWANG_GOURD.get(), 1)
                .pattern("RS ")
                .pattern("WGW")
                .pattern("WNW")
                .define('R', Items.RED_DYE)
                .define('S', Items.STRING)
                .define('G', Items.GHAST_TEAR)
                .define('W', ItemTags.PLANKS)
                .define('N', Items.NETHERITE_SCRAP)
                .unlockedBy("has_ghast_tear",
                        has(Items.GHAST_TEAR))
                .save(recipeOutput);

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(ModItems.ZUCCHINI.get()), RecipeCategory.FOOD, ModItems.COOKED_ZUCCHINI.get(), 0.35f, 200)
                .unlockedBy("has_zucchini", has(ModItems.ZUCCHINI.get()))
                .save(recipeOutput);

        SimpleCookingRecipeBuilder.smoking(Ingredient.of(ModItems.ZUCCHINI.get()), RecipeCategory.FOOD, ModItems.COOKED_ZUCCHINI.get(), 0.35f, 100)
                .unlockedBy("has_zucchini", has(ModItems.ZUCCHINI.get()))
                .save(recipeOutput, MOD_ID + ":cooked_zucchini_from_smoking");

        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(ModItems.ZUCCHINI.get()), RecipeCategory.FOOD, ModItems.COOKED_ZUCCHINI.get(), 0.35f, 600)
                .unlockedBy("has_zucchini", has(ModItems.ZUCCHINI.get()))
                .save(recipeOutput, MOD_ID + ":cooked_zucchini_from_campfire");

        converseRecipe(recipeOutput, Items.SWEET_BERRIES, ModItems.SOUR_BERRIES, "sweet_berries", "sour_berries");
        converseRecipe(recipeOutput, Items.GLOW_BERRIES, ModItems.SHADOW_BERRIES, "glow_berries", "shadow_berries");
        converseRecipe(recipeOutput, Items.SLIME_BALL, ModItems.YOLIME, "slime_ball", "yolime");
        converseRecipe(recipeOutput, Items.SNOWBALL, Items.FIRE_CHARGE, "snowball", "fire_charge");
        converseRecipe(recipeOutput, Items.WHITE_DYE, Items.BLACK_DYE, "white_dye", "black_dye");
        converseRecipe(recipeOutput, Items.MAGMA_BLOCK, Items.SNOW_BLOCK, "magma_block", "snow_block");
        converseRecipe(recipeOutput, Items.DEAD_BUSH, Items.OAK_SAPLING, "dead_bush", "oak_sapling");
        converseRecipe(recipeOutput, Items.SLIME_BLOCK, ModBlocks.YOLIME_BLOCK, "slime_block", "yolime_block");
        converseRecipe(recipeOutput, Items.PUMPKIN, ModItems.ZUCCHINI, "pumpkin", "zucchini");

        amethystSmithing(recipeOutput, "逆", ModItems.NI_CONVERSE_AMETHYST.asItem(), "ni_converse_amethyst_from_smithing");
        amethystSmithing(recipeOutput, "劍", ModItems.JIAN_SWORD_AMETHYST.asItem(), "jian_sword_amethyst");
        amethystSmithing(recipeOutput, "燚", ModItems.YI_CONFLAGRANT_AMETHYST.asItem(), "yi_conflagrant_amethyst");
        amethystSmithing(recipeOutput, "飆", ModItems.BIAO_GALE_AMETHYST.asItem(), "biao_gale_amethyst");
        amethystSmithing(recipeOutput, "醫", ModItems.YI_CURE_AMETHYST.asItem(), "yi_cure_amethyst");
        amethystSmithing(recipeOutput, "蠱", ModItems.GU_PARASITE_AMETHYST.asItem(), "gu_parasite_amethyst");
        amethystSmithing(recipeOutput, "磊", ModItems.LEI_STONES_AMETHYST.asItem(), "lei_stones_amethyst");
    }


    protected static void amethystSmithing(RecipeOutput recipeOutput, String inscription,
                                           Item resultItem, String name) {
        AmethystSmithingBuilder.amethystSmithing(
                        inscription,
                        Ingredient.of(Items.AMETHYST_SHARD),
                        RecipeCategory.MISC,
                        resultItem,
                        name
                )
                .unlocks("has_amethyst_upgrade_smithing_template",
                        has(ModItems.AMETHYST_UPGRADE_SMITHING_TEMPLATE.get()))
                .save(recipeOutput, "dummy_path");
    }


    private void converseRecipe(RecipeOutput recipeOutput, ItemLike positive, ItemLike negative, String namePositive, String nameNegative){
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, negative, 1)
                .requires(positive).requires(ModItems.NI_CONVERSE_AMETHYST)
                .unlockedBy("has_ni_converse_amethyst", has(ModItems.NI_CONVERSE_AMETHYST))
                .save(recipeOutput, nameNegative+CONVERSE_SINGLE);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, negative, 8)
                .pattern("MMM")
                .pattern("MNM")
                .pattern("MMM")
                .define('M', positive)
                .define('N', ModItems.NI_CONVERSE_AMETHYST)
                .unlockedBy("has_ni_converse_amethyst", has(ModItems.NI_CONVERSE_AMETHYST))
                .save(recipeOutput, nameNegative+CONVERSE_MULTI);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, positive, 1)
                .requires(negative).requires(ModItems.NI_CONVERSE_AMETHYST)
                .unlockedBy("has_ni_converse_amethyst", has(ModItems.NI_CONVERSE_AMETHYST))
                .save(recipeOutput, namePositive+CONVERSE_SINGLE);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, positive, 8)
                .pattern("MMM")
                .pattern("MNM")
                .pattern("MMM")
                .define('M', negative)
                .define('N', ModItems.NI_CONVERSE_AMETHYST)
                .unlockedBy("has_ni_converse_amethyst", has(ModItems.NI_CONVERSE_AMETHYST))
                .save(recipeOutput, namePositive+CONVERSE_MULTI);
    }
}

