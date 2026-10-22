package net.lghast.jiagu.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.lghast.jiagu.character.CharacterInfo;
import net.lghast.jiagu.character.CharacterStructure;
import net.lghast.jiagu.common.content.item.CharacterItem;
import net.lghast.jiagu.common.content.item.PrescriptionItem;
import net.lghast.jiagu.common.content.item.TaoistTalismanItem;
import net.lghast.jiagu.common.system.datacomponent.Spell;
import net.lghast.jiagu.compat.jei.category.CharacterAssemblyRecipeCategory;
import net.lghast.jiagu.compat.jei.category.CharacterDisassemblyRecipeCategory;
import net.lghast.jiagu.compat.jei.category.CharacterMorphingRecipeCategory;
import net.lghast.jiagu.compat.jei.category.EnchantmentRubbingRecipeCategory;
import net.lghast.jiagu.compat.jei.category.PrescribingRecipeCategory;
import net.lghast.jiagu.compat.jei.category.RubbingRecipeCategory;
import net.lghast.jiagu.compat.jei.recipe.CharacterAssemblyRecipe;
import net.lghast.jiagu.compat.jei.recipe.CharacterDisassemblyRecipe;
import net.lghast.jiagu.compat.jei.recipe.CharacterMorphingRecipe;
import net.lghast.jiagu.compat.jei.recipe.EnchantmentRubbingRecipe;
import net.lghast.jiagu.compat.jei.recipe.PrescribingRecipe;
import net.lghast.jiagu.compat.jei.recipe.RubbingRecipe;
import net.lghast.jiagu.register.content.ModItems;
import net.lghast.jiagu.register.system.ModTags;
import net.lghast.jiagu.utils.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@JeiPlugin
@SuppressWarnings("unused")
@ParametersAreNonnullByDefault
public class JiaguJeiPlugin implements IModPlugin {
    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath("jiagureappear", "jei_plugin");
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        registration.registerSubtypeInterpreter(ModItems.CHARACTER_ITEM.get(), new ISubtypeInterpreter<>() {
            @Override
            public String getSubtypeData(ItemStack ingredient, UidContext context) {
                return CharacterItem.getInscription(ingredient);
            }

            @Override
            public @NotNull String getLegacyStringSubtypeInfo(ItemStack ingredient, UidContext context) {
                return CharacterItem.getInscription(ingredient);
            }
        });

        registration.registerSubtypeInterpreter(ModItems.PRESCRIPTION.get(), new ISubtypeInterpreter<>() {
            @Override
            public String getSubtypeData(ItemStack ingredient, UidContext context) {
                return getPrescriptionSubtype(ingredient);
            }

            @Override
            public @NotNull String getLegacyStringSubtypeInfo(ItemStack ingredient, UidContext context) {
                return getPrescriptionSubtype(ingredient);
            }
        });

        registration.registerSubtypeInterpreter(ModItems.TAOIST_TALISMAN.get(), new ISubtypeInterpreter<>() {
            @Override
            public String getSubtypeData(ItemStack ingredient, UidContext context) {
                return getTalismanSubtype(ingredient);
            }

            @Override
            public @NotNull String getLegacyStringSubtypeInfo(ItemStack ingredient, UidContext context) {
                return getTalismanSubtype(ingredient);
            }
        });
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(
                new CharacterAssemblyRecipeCategory(registration.getJeiHelpers().getGuiHelper()),
                new CharacterDisassemblyRecipeCategory(registration.getJeiHelpers().getGuiHelper()),
                new CharacterMorphingRecipeCategory(registration.getJeiHelpers().getGuiHelper()),
                new EnchantmentRubbingRecipeCategory(registration.getJeiHelpers().getGuiHelper()),
                new PrescribingRecipeCategory(registration.getJeiHelpers().getGuiHelper()),
                new RubbingRecipeCategory(registration.getJeiHelpers().getGuiHelper())
        );
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(CharacterAssemblyRecipeCategory.TYPE, buildAssemblyRecipes());
        registration.addRecipes(CharacterDisassemblyRecipeCategory.TYPE, buildDisassemblyRecipes());
        registration.addRecipes(CharacterMorphingRecipeCategory.TYPE, buildCharacterMorphingRecipes());
        registration.addRecipes(EnchantmentRubbingRecipeCategory.TYPE, buildEnchantmentRubbingRecipes());
        registration.addRecipes(PrescribingRecipeCategory.TYPE, buildPrescriptionRecipes());
        registration.addRecipes(RubbingRecipeCategory.TYPE, buildRubbingRecipes());
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModItems.CHARACTER_ITEM.get()),
                CharacterAssemblyRecipeCategory.TYPE, CharacterDisassemblyRecipeCategory.TYPE,
                CharacterMorphingRecipeCategory.TYPE, RubbingRecipeCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModItems.PRESCRIPTION.get()),
                PrescribingRecipeCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModItems.TAOIST_TALISMAN.get()),
                EnchantmentRubbingRecipeCategory.TYPE);
    }

    private static List<CharacterAssemblyRecipe> buildAssemblyRecipes() {
        List<CharacterAssemblyRecipe> recipes = new ArrayList<>();
        for (Map.Entry<String, CharacterInfo.StructureData> entry : CharacterInfo.CHARACTER_COMPONENTS.entrySet()) {
            String character = entry.getKey();
            List<String> components = entry.getValue().components();
            if (components == null || components.isEmpty()) continue;

            List<String> layout = buildLayout(character, components);
            List<ItemStack> inputs = new ArrayList<>(9);
            for (String component : layout) {
                inputs.add(component == null ? ItemStack.EMPTY : makeCharacterStack(component));
            }
            recipes.add(new CharacterAssemblyRecipe(inputs, makeCharacterStack(character)));
        }
        return recipes;
    }

    private static List<CharacterDisassemblyRecipe> buildDisassemblyRecipes() {
        List<CharacterDisassemblyRecipe> recipes = new ArrayList<>();
        for (Map.Entry<String, CharacterInfo.StructureData> entry : CharacterInfo.CHARACTER_COMPONENTS.entrySet()) {
            String character = entry.getKey();
            List<String> components = entry.getValue().components();
            if (components == null || components.isEmpty()) continue;

            List<String> layout = buildLayout(character, components);
            List<ItemStack> outputs = new ArrayList<>(9);
            for (String component : layout) {
                outputs.add(component == null ? ItemStack.EMPTY : makeCharacterStack(component));
            }
            recipes.add(new CharacterDisassemblyRecipe(makeCharacterStack(character), outputs));
        }
        return recipes;
    }

    private static List<CharacterMorphingRecipe> buildCharacterMorphingRecipes() {
        List<CharacterMorphingRecipe> recipes = new ArrayList<>();

        for (Holder.Reference<Item> itemHolder : BuiltInRegistries.ITEM.holders().toList()) {
            ItemStack stack = new ItemStack(itemHolder.value());
            if (stack.is(ModTags.INVALID_TO_CHARACTERS)) continue;
            if (stack.is(ModItems.TAOIST_TALISMAN.get())) continue;
            if (stack.is(ModItems.PRESCRIPTION.get())) continue;
            addMorphingRecipe(recipes, stack, ModUtils.modifyName(stack));
        }

        ItemStack defaultTalisman = new ItemStack(ModItems.TAOIST_TALISMAN.get());
        addMorphingRecipe(recipes, defaultTalisman, ModUtils.modifyName(defaultTalisman));

        ItemStack defaultPrescription = new ItemStack(ModItems.PRESCRIPTION.get());
        addMorphingRecipe(recipes, defaultPrescription, ModUtils.modifyName(defaultPrescription));

        for (Holder.Reference<MobEffect> effectHolder : BuiltInRegistries.MOB_EFFECT.holders().toList()) {
            String name = ModUtils.modifyName(effectHolder.value());
            if (name == null || name.isEmpty() || name.length() > 12) continue;
            ItemStack prescription = new ItemStack(ModItems.PRESCRIPTION.get());
            PrescriptionItem.setEffect(prescription, effectHolder);
            addMorphingRecipe(recipes, prescription, name);
        }

        for (Holder.Reference<Enchantment> enchantmentHolder : getEnchantments()) {
            String name = ModUtils.modifyName(enchantmentHolder, 1);
            if (name == null || name.isEmpty() || name.length() > 12) continue;
            ItemStack talisman = new ItemStack(ModItems.TAOIST_TALISMAN.get());
            TaoistTalismanItem.setSpell(talisman, enchantmentHolder, 1);
            addMorphingRecipe(recipes, talisman, name);
        }

        return recipes;
    }

    private static void addMorphingRecipe(List<CharacterMorphingRecipe> recipes, ItemStack input, @Nullable String name) {
        if (name == null || name.isEmpty() || name.length() > 12) return;
        List<ItemStack> outputs = new ArrayList<>();
        for (char c : name.toCharArray()) {
            outputs.add(makeCharacterStack(String.valueOf(c)));
        }
        recipes.add(new CharacterMorphingRecipe(input, outputs));
    }

    private static List<EnchantmentRubbingRecipe> buildEnchantmentRubbingRecipes() {
        List<EnchantmentRubbingRecipe> recipes = new ArrayList<>();
        for (Holder.Reference<Enchantment> enchantmentHolder : getEnchantments()) {
            TagKey<Item> tag = getEnchantableTag(enchantmentHolder);
            if (tag == null) continue;

            String name = ModUtils.modifyName(enchantmentHolder, 1);
            if (name == null || name.isEmpty() || name.length() > 12) continue;

            List<ItemStack> inputs = new ArrayList<>();
            for (char c : name.toCharArray()) {
                inputs.add(makeCharacterStack(String.valueOf(c)));
            }

            ItemStack talisman = new ItemStack(ModItems.TAOIST_TALISMAN.get());
            TaoistTalismanItem.setSpell(talisman, enchantmentHolder, 1);

            recipes.add(new EnchantmentRubbingRecipe(inputs, talisman, Ingredient.of(tag)));
        }
        return recipes;
    }

    private static List<PrescribingRecipe> buildPrescriptionRecipes() {
        List<PrescribingRecipe> recipes = new ArrayList<>();
        for (Holder.Reference<MobEffect> effectHolder : BuiltInRegistries.MOB_EFFECT.holders().toList()) {
            String name = ModUtils.modifyName(effectHolder.value());
            if (name == null || name.isEmpty() || name.length() > 6) continue;

            List<ItemStack> inputs = new ArrayList<>();
            for (char c : name.toCharArray()) {
                inputs.add(makeCharacterStack(String.valueOf(c)));
            }

            ItemStack output = new ItemStack(ModItems.PRESCRIPTION.get());
            PrescriptionItem.setEffect(output, effectHolder);

            recipes.add(new PrescribingRecipe(inputs, output));
        }
        return recipes;
    }

    private static List<RubbingRecipe> buildRubbingRecipes() {
        List<RubbingRecipe> recipes = new ArrayList<>();
        for (Holder.Reference<Item> itemHolder : BuiltInRegistries.ITEM.holders().toList()) {
            ItemStack stack = new ItemStack(itemHolder.value());
            if (stack.is(ModTags.INVALID_TO_BE_RUBBED)) continue;

            String name = ModUtils.modifyName(stack);
            if (name == null || name.isEmpty() || name.length() > 12) continue;

            List<ItemStack> inputs = new ArrayList<>();
            for (char c : name.toCharArray()) {
                inputs.add(makeCharacterStack(String.valueOf(c)));
            }
            recipes.add(new RubbingRecipe(inputs, stack));
        }
        return recipes;
    }

    private static List<String> buildLayout(String inscription, List<String> components) {
        CharacterStructure structure = CharacterInfo.getStructure(inscription);
        if (structure != null) {
            List<String> layout = structure.getComponentList(components);
            if (layout.size() == 9) return layout;
        }

        List<String> layout = new ArrayList<>(Collections.nCopies(9, null));
        int count = Math.min(components.size(), 3);
        int startCol = (3 - count) / 2;
        for (int i = 0; i < count; i++) {
            layout.set(3 + startCol + i, components.get(i));
        }
        return layout;
    }

    private static ItemStack makeCharacterStack(String inscription) {
        ItemStack stack = new ItemStack(ModItems.CHARACTER_ITEM.get());
        CharacterItem.setInscription(stack, inscription);
        return stack;
    }

    private static String getPrescriptionSubtype(ItemStack stack) {
        MobEffect effect = PrescriptionItem.getEffect(stack);
        if (effect == null) return "";
        ResourceLocation key = BuiltInRegistries.MOB_EFFECT.getKey(effect);
        return key == null ? "" : key.toString();
    }

    private static String getTalismanSubtype(ItemStack stack) {
        Spell spell = TaoistTalismanItem.getSpell(stack);
        if (spell.isEmpty()) return "";
        Holder<Enchantment> holder = spell.getEnchantment();
        if (holder == null) return "";
        ResourceLocation key = holder.unwrapKey().map(ResourceKey::location).orElse(null);
        return key == null ? "" : key + ":" + spell.level();
    }

    private static List<Holder.Reference<Enchantment>> getEnchantments() {
        Level level = Minecraft.getInstance().level;
        if (level == null) return List.of();
        return level.registryAccess().registryOrThrow(Registries.ENCHANTMENT).holders().toList();
    }

    private static TagKey<Item> getEnchantableTag(Holder<Enchantment> holder) {
        ResourceLocation id = holder.unwrapKey().map(ResourceKey::location).orElse(null);
        if (id == null) return null;
        return switch (id.getPath()) {
            case "igniting" -> ModTags.IGNITING_ENCHANTABLE;
            case "cremation" -> ModTags.CREMATION_ENCHANTABLE;
            case "inquisitiveness" -> ModTags.INQUISITIVENESS_ENCHANTABLE;
            case "flurry" -> ModTags.FLURRY_ENCHANTABLE;
            case "wuthering" -> ModTags.WUTHERING_ENCHANTABLE;
            case "benevolence" -> ModTags.BENEVOLENCE_ENCHANTABLE;
            case "hippocrates" -> ModTags.HIPPOCRATES_ENCHANTABLE;
            case "panacea" -> ModTags.PANACEA_ENCHANTABLE;
            case "malady" -> ModTags.MALADY_ENCHANTABLE;
            case "massiveness" -> ModTags.MASSIVENESS_ENCHANTABLE;
            case "aftershock" -> ModTags.AFTERSHOCK_ENCHANTABLE;
            case "sharpness" -> ItemTags.SHARP_WEAPON_ENCHANTABLE;
            case "smite", "bane_of_arthropods", "knockback", "looting" -> ItemTags.WEAPON_ENCHANTABLE;
            case "fire_aspect" -> ItemTags.FIRE_ASPECT_ENCHANTABLE;
            case "sweeping_edge" -> ItemTags.SWORD_ENCHANTABLE;
            case "protection", "fire_protection", "blast_protection", "projectile_protection", "thorns" -> ItemTags.ARMOR_ENCHANTABLE;
            case "respiration", "aqua_affinity" -> ItemTags.HEAD_ARMOR_ENCHANTABLE;
            case "depth_strider", "frost_walker", "soul_speed", "feather_falling" -> ItemTags.FOOT_ARMOR_ENCHANTABLE;
            case "swift_sneak" -> ItemTags.LEG_ARMOR_ENCHANTABLE;
            case "binding_curse" -> ItemTags.EQUIPPABLE_ENCHANTABLE;
            case "efficiency" -> ItemTags.MINING_ENCHANTABLE;
            case "silk_touch", "fortune" -> ItemTags.MINING_LOOT_ENCHANTABLE;
            case "unbreaking", "mending" -> ItemTags.DURABILITY_ENCHANTABLE;
            case "power", "punch", "flame", "infinity" -> ItemTags.BOW_ENCHANTABLE;
            case "luck_of_the_sea", "lure" -> ItemTags.FISHING_ENCHANTABLE;
            case "loyalty", "impaling", "riptide", "channeling" -> ItemTags.TRIDENT_ENCHANTABLE;
            case "multishot", "quick_charge", "piercing" -> ItemTags.CROSSBOW_ENCHANTABLE;
            case "vanishing_curse" -> ItemTags.VANISHING_ENCHANTABLE;
            case "density", "breach", "wind_burst" -> ItemTags.MACE_ENCHANTABLE;
            case "backstabbing" -> ModTags.KNIFE_ENCHANTABLE;
            default -> null;
        };
    }
}
