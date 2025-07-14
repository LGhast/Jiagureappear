package net.lghast.jiagu.datagen;

import net.lghast.jiagu.datagen.recipebuilders.AmethystSmithingBuilder;
import net.lghast.jiagu.register.ModBlocks;
import net.lghast.jiagu.datagen.recipebuilders.CharacterAssemblingBuilder;
import net.lghast.jiagu.datagen.recipebuilders.CharacterAssemblingNonMirroredBuilder;
import net.lghast.jiagu.register.ModItems;
import net.lghast.jiagu.item.CharacterItem;
import net.lghast.jiagu.utils.CharacterInfo;
import net.lghast.jiagu.utils.CharacterStructure;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static net.lghast.jiagu.JiaguReappear.MOD_ID;

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

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.YOLIME_BREAD.get(), 1)
                .requires(ModItems.YOLIME).requires(Items.BREAD)
                .unlockedBy("has_yolime", has(ModItems.YOLIME))
                .save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.AMETHYST_UPGRADE_SMITHING_TEMPLATE.get(), 1)
                .requires(Items.AMETHYST_CLUSTER).requires(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE)
                .unlockedBy("has_netherite_upgrade_smithing_template", has(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE))
                .save(recipeOutput,"amethyst_upgrade_smithing_template_from_netherite_one");

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
                .define('D', ModBlocks.CHARACTER_DISASSEMBLER)
                .define('I',Items.IRON_INGOT)
                .define('R', Items.REDSTONE)
                .define('P', Items.DROPPER)
                .unlockedBy("has_cangjie_ding_tripod",
                        has(ModBlocks.CANGJIE_DING_TRIPOD.get()))
                .save(recipeOutput);

        converseRecipe(recipeOutput, Items.SWEET_BERRIES, ModItems.SOUR_BERRIES, "sweet_berries", "sour_berries");
        converseRecipe(recipeOutput, Items.GLOW_BERRIES, ModItems.SHADOW_BERRIES, "glow_berries", "shadow_berries");
        converseRecipe(recipeOutput, Items.SLIME_BALL, ModItems.YOLIME, "slime_ball", "yolime");
        converseRecipe(recipeOutput, Items.SNOWBALL, Items.FIRE_CHARGE, "snowball", "fire_charge");
        converseRecipe(recipeOutput, Items.WHITE_DYE, Items.BLACK_DYE, "white_dye", "black_dye");
        converseRecipe(recipeOutput, Items.MAGMA_BLOCK, Items.SNOW_BLOCK, "magma_block", "snow_block");
        converseRecipe(recipeOutput, Items.SLIME_BLOCK, ModBlocks.YOLIME_BLOCK, "slime_block", "yolime_block");

        amethystSmithing(recipeOutput, "逆", ModItems.NI_CONVERSE_AMETHYST.asItem(), "ni_converse_amethyst_from_smithing");
        amethystSmithing(recipeOutput, "劍", ModItems.JIAN_SWORD_AMETHYST.asItem(), "jian_sword_amethyst");
        amethystSmithing(recipeOutput, "燚", ModItems.YI_CONFLAGRANT_AMETHYST.asItem(), "yi_conflagrant_amethyst");
        amethystSmithing(recipeOutput, "飆", ModItems.BIAO_GALE_AMETHYST.asItem(), "biao_gale_amethyst");

        for (String s : CharacterInfo.CHARACTER_COMPONENTS.keySet()){
            generateFor(recipeOutput, s);
        }
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


    private void generateFor(RecipeOutput output, String characterResult){
        CharacterStructure structure = CharacterInfo.getStructure(characterResult);
        if(structure==null) return;
        List<String> components = CharacterInfo.getComponents(characterResult);
        if(components==null) return;
        switch (structure){
            case HORIZONTAL -> characterHorizontal(output, characterResult, components);
            case HORIZONTAL_LONG -> characterHorizontalLong(output, characterResult, components);
            case VERTICAL -> characterVertical(output, characterResult, components);
            case VERTICAL_LONG -> characterVerticalLong(output, characterResult, components);
            case TRIANGLE -> characterTriangle(output, characterResult, components);
            case TRIANGLE_RIGHT -> characterTriangleRight(output, characterResult, components);
            case TRIANGLE_INVERT -> characterTriangleInvert(output, characterResult, components);
            case TRIANGLE_RIGHT_INVERT -> characterTriangleRightInvert(output, characterResult, components);
            case RHOMBUS -> characterRhombus(output, characterResult, components);
            case T_SHAPE -> characterTShape(output, characterResult, components);
            case T_SHAPE_LONG -> characterTShapeLong(output, characterResult, components);
            case T_SHAPE_INVERT -> characterTShapeInvert(output, characterResult, components);
            case T_SHAPE_HORIZONTAL -> characterTShapeHorizontal(output, characterResult, components);
            case ARROW -> characterArrow(output, characterResult, components);
            case ARROW_INVERT -> characterArrowInvert(output, characterResult, components);
            case L_SHAPE -> characterLShape(output, characterResult, components);
            case BIAS -> characterBias(output, characterResult, components);
            case SQUARE -> characterSquare(output, characterResult, components);
            case RECTANGLE -> characterRectangle(output, characterResult, components);
            case SAME -> characterSame(output, characterResult, components);
        }
    }


    private void characterSame(RecipeOutput output, String characterResult, List<String> ingredients){
        if(ingredients==null || characterResult==null) return;
        generateCharacterRecipeNonMirrored(output, characterResult, builder -> builder
                .pattern("   ")
                .pattern(" M ")
                .pattern("   ")
                .define('M', ingredients.getFirst())
                .unlockedBy("has_character_item",has(ModItems.CHARACTER_ITEM)));
    }

    private void characterHorizontal(RecipeOutput output, String characterResult, List<String> ingredients){
        if(ingredients==null || characterResult==null) return;
        if(ingredients.size()<2) return;
        generateCharacterRecipeNonMirrored(output, characterResult, builder -> builder
                .pattern("   ")
                .pattern("LR ")
                .pattern("   ")
                .define('L', ingredients.getFirst())
                .define('R', ingredients.get(1))
                .unlockedBy("has_character_item",has(ModItems.CHARACTER_ITEM)));
    }

    private void characterVertical(RecipeOutput output, String characterResult, List<String> ingredients){
        if(ingredients==null || characterResult==null) return;
        if(ingredients.size()<2) return;
        generateCharacterRecipe(output, characterResult, builder -> builder
                .pattern(" U ")
                .pattern(" L ")
                .pattern("   ")
                .define('U', ingredients.getFirst())
                .define('L', ingredients.get(1))
                .unlockedBy("has_character_item",has(ModItems.CHARACTER_ITEM)));
    }

    private void characterHorizontalLong(RecipeOutput output, String characterResult, List<String> ingredients){
        if(ingredients==null || characterResult==null) return;
        if(ingredients.size()<3) return;
        generateCharacterRecipeNonMirrored(output, characterResult, builder -> builder
                .pattern("   ")
                .pattern("LMR")
                .pattern("   ")
                .define('L', ingredients.getFirst())
                .define('M', ingredients.get(1))
                .define('R', ingredients.get(2))
                .unlockedBy("has_character_item",has(ModItems.CHARACTER_ITEM)));
    }

    private void characterVerticalLong(RecipeOutput output, String characterResult, List<String> ingredients){
        if(ingredients==null || characterResult==null) return;
        if(ingredients.size()<3) return;
        generateCharacterRecipe(output, characterResult, builder -> builder
                .pattern(" U ")
                .pattern(" M ")
                .pattern(" L ")
                .define('U', ingredients.getFirst())
                .define('M', ingredients.get(1))
                .define('L', ingredients.get(2))
                .unlockedBy("has_character_item",has(ModItems.CHARACTER_ITEM)));
    }

    private void characterTriangle(RecipeOutput output, String characterResult, List<String> ingredients){
        if(ingredients==null || characterResult==null) return;
        if(ingredients.size()<3) return;
        generateCharacterRecipe(output, characterResult, builder -> builder
                .pattern("U  ")
                .pattern("LR ")
                .pattern("   ")
                .define('U', ingredients.getFirst())
                .define('L', ingredients.get(1))
                .define('R', ingredients.get(2))
                .unlockedBy("has_character_item",has(ModItems.CHARACTER_ITEM)));
    }

    private void characterTriangleRight(RecipeOutput output, String characterResult, List<String> ingredients){
        if(ingredients==null || characterResult==null) return;
        if(ingredients.size()<3) return;
        generateCharacterRecipe(output, characterResult, builder -> builder
                .pattern(" U ")
                .pattern("LR ")
                .pattern("   ")
                .define('U', ingredients.getFirst())
                .define('L', ingredients.get(1))
                .define('R', ingredients.get(2))
                .unlockedBy("has_character_item",has(ModItems.CHARACTER_ITEM)));
    }

    private void characterTriangleInvert(RecipeOutput output, String characterResult, List<String> ingredients){
        if(ingredients==null || characterResult==null) return;
        if(ingredients.size()<3) return;
        generateCharacterRecipe(output, characterResult, builder -> builder
                .pattern("   ")
                .pattern("LR ")
                .pattern("D  ")
                .define('L', ingredients.getFirst())
                .define('R', ingredients.get(1))
                .define('D', ingredients.get(2))
                .unlockedBy("has_character_item",has(ModItems.CHARACTER_ITEM)));
    }

    private void characterTriangleRightInvert(RecipeOutput output, String characterResult, List<String> ingredients){
        if(ingredients==null || characterResult==null) return;
        if(ingredients.size()<3) return;
        generateCharacterRecipe(output, characterResult, builder -> builder
                .pattern("   ")
                .pattern("LR ")
                .pattern(" D ")
                .define('L', ingredients.getFirst())
                .define('R', ingredients.get(1))
                .define('D', ingredients.get(2))
                .unlockedBy("has_character_item",has(ModItems.CHARACTER_ITEM)));
    }

    private void characterRhombus(RecipeOutput output, String characterResult, List<String> ingredients){
        if(ingredients==null || characterResult==null) return;
        if(ingredients.size()<4) return;
        generateCharacterRecipe(output, characterResult, builder -> builder
                .pattern(" U ")
                .pattern("L R")
                .pattern(" D ")
                .define('U', ingredients.getFirst())
                .define('L', ingredients.get(1))
                .define('R', ingredients.get(2))
                .define('D', ingredients.get(3))
                .unlockedBy("has_character_item",has(ModItems.CHARACTER_ITEM)));
    }

    private void characterTShape(RecipeOutput output, String characterResult, List<String> ingredients){
        if(ingredients==null || characterResult==null) return;
        if(ingredients.size()<4) return;
        generateCharacterRecipe(output, characterResult, builder -> builder
                .pattern("   ")
                .pattern("LMR")
                .pattern(" D ")
                .define('L', ingredients.getFirst())
                .define('M', ingredients.get(1))
                .define('R', ingredients.get(2))
                .define('D', ingredients.get(3))
                .unlockedBy("has_character_item",has(ModItems.CHARACTER_ITEM)));
    }

    private void characterTShapeInvert(RecipeOutput output, String characterResult, List<String> ingredients){
        if(ingredients==null || characterResult==null) return;
        if(ingredients.size()<4) return;
        generateCharacterRecipe(output, characterResult, builder -> builder
                .pattern(" U ")
                .pattern("LMR")
                .pattern("   ")
                .define('U', ingredients.getFirst())
                .define('L', ingredients.get(1))
                .define('M', ingredients.get(2))
                .define('R', ingredients.get(3))
                .unlockedBy("has_character_item",has(ModItems.CHARACTER_ITEM)));
    }

    private void characterTShapeHorizontal(RecipeOutput output, String characterResult, List<String> ingredients){
        if(ingredients==null || characterResult==null) return;
        if(ingredients.size()<4) return;
        generateCharacterRecipe(output, characterResult, builder -> builder
                .pattern(" U ")
                .pattern(" MR")
                .pattern(" D ")
                .define('U', ingredients.getFirst())
                .define('M', ingredients.get(1))
                .define('R', ingredients.get(2))
                .define('D', ingredients.get(3))
                .unlockedBy("has_character_item",has(ModItems.CHARACTER_ITEM)));
    }

    private void characterSquare(RecipeOutput output, String characterResult, List<String> ingredients){
        if(ingredients==null || characterResult==null) return;
        if(ingredients.size()<4) return;
        generateCharacterRecipe(output, characterResult, builder -> builder
                .pattern("AB ")
                .pattern("CD ")
                .pattern("   ")
                .define('A', ingredients.getFirst())
                .define('B', ingredients.get(1))
                .define('C', ingredients.get(2))
                .define('D', ingredients.get(3))
                .unlockedBy("has_character_item",has(ModItems.CHARACTER_ITEM)));
    }

    private void characterRectangle(RecipeOutput output, String characterResult, List<String> ingredients){
        if(ingredients==null || characterResult==null) return;
        if(ingredients.size()<6) return;
        generateCharacterRecipe(output, characterResult, builder -> builder
                .pattern("ABC")
                .pattern("DEF")
                .pattern("   ")
                .define('A', ingredients.getFirst())
                .define('B', ingredients.get(1))
                .define('C', ingredients.get(2))
                .define('D', ingredients.get(3))
                .define('E', ingredients.get(4))
                .define('F', ingredients.get(5))
                .unlockedBy("has_character_item",has(ModItems.CHARACTER_ITEM)));
    }

    private void characterArrow(RecipeOutput output, String characterResult, List<String> ingredients){
        if(ingredients==null || characterResult==null) return;
        if(ingredients.size()<3) return;
        generateCharacterRecipe(output, characterResult, builder -> builder
                .pattern(" U ")
                .pattern("L R")
                .pattern("   ")
                .define('U', ingredients.getFirst())
                .define('L', ingredients.get(1))
                .define('R', ingredients.get(2))
                .unlockedBy("has_character_item",has(ModItems.CHARACTER_ITEM)));
    }

    private void characterArrowInvert(RecipeOutput output, String characterResult, List<String> ingredients){
        if(ingredients==null || characterResult==null) return;
        if(ingredients.size()<3) return;
        generateCharacterRecipe(output, characterResult, builder -> builder
                .pattern("   ")
                .pattern("L R")
                .pattern(" D ")
                .define('L', ingredients.getFirst())
                .define('R', ingredients.get(1))
                .define('D', ingredients.get(2))
                .unlockedBy("has_character_item",has(ModItems.CHARACTER_ITEM)));
    }

    private void characterBias(RecipeOutput output, String characterResult, List<String> ingredients){
        if(ingredients==null || characterResult==null) return;
        if(ingredients.size()<2) return;
        generateCharacterRecipe(output, characterResult, builder -> builder
                .pattern("   ")
                .pattern("U  ")
                .pattern(" D ")
                .define('U', ingredients.getFirst())
                .define('D', ingredients.get(1))
                .unlockedBy("has_character_item",has(ModItems.CHARACTER_ITEM)));
    }

    private void characterLShape(RecipeOutput output, String characterResult, List<String> ingredients){
        if(ingredients==null || characterResult==null) return;
        if(ingredients.size()<4) return;
        generateCharacterRecipe(output, characterResult, builder -> builder
                .pattern(" U ")
                .pattern(" M ")
                .pattern(" DR")
                .define('U', ingredients.getFirst())
                .define('M', ingredients.get(1))
                .define('D', ingredients.get(2))
                .define('R', ingredients.get(3))
                .unlockedBy("has_character_item",has(ModItems.CHARACTER_ITEM)));
    }

    private void characterTShapeLong(RecipeOutput output, String characterResult, List<String> ingredients){
        if(ingredients==null || characterResult==null) return;
        if(ingredients.size()<5) return;
        generateCharacterRecipe(output, characterResult, builder -> builder
                .pattern("LUR")
                .pattern(" M ")
                .pattern(" D ")
                .define('L', ingredients.getFirst())
                .define('U', ingredients.get(1))
                .define('R', ingredients.get(2))
                .define('M', ingredients.get(3))
                .define('D', ingredients.get(3))
                .unlockedBy("has_character_item",has(ModItems.CHARACTER_ITEM)));
    }

    private void generateCharacterRecipe(RecipeOutput output, String character, Consumer<CharacterAssemblingBuilder> configurator) {
        String identifier = CharacterInfo.getIdentifier(character);
        ItemStack resultStack = new ItemStack(ModItems.CHARACTER_ITEM.get(), 1);
        CharacterItem.setInscription(resultStack, character);
        CharacterAssemblingBuilder builder = CharacterAssemblingBuilder.shaped(
                RecipeCategory.MISC,
                resultStack,
                character,
                identifier
        );
        configurator.accept(builder);
        ResourceLocation recipeId = ResourceLocation.parse(MOD_ID + ":" + identifier);
        builder.save(output, recipeId);
    }

    private void generateCharacterRecipeNonMirrored(RecipeOutput output, String character, Consumer<CharacterAssemblingNonMirroredBuilder> configurator) {
        String identifier = CharacterInfo.getIdentifier(character);
        ItemStack resultStack = new ItemStack(ModItems.CHARACTER_ITEM.get(), 1);
        CharacterItem.setInscription(resultStack, character);
        CharacterAssemblingNonMirroredBuilder builder = CharacterAssemblingNonMirroredBuilder.shaped(
                RecipeCategory.MISC,
                resultStack,
                character,
                identifier
        );
        configurator.accept(builder);
        ResourceLocation recipeId = ResourceLocation.parse(MOD_ID + ":" + identifier);
        builder.save(output, recipeId);
    }
}

