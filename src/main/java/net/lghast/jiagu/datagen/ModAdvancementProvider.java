package net.lghast.jiagu.datagen;

import net.lghast.jiagu.common.content.item.CharacterItem;
import net.lghast.jiagu.common.system.advancement.*;
import net.lghast.jiagu.register.content.ModBlocks;
import net.lghast.jiagu.register.content.ModItems;
import net.lghast.jiagu.register.system.ModTags;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@ParametersAreNonnullByDefault
public class ModAdvancementProvider extends AdvancementProvider {
    public ModAdvancementProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, existingFileHelper, List.of(new JiaguAdvancementGenerator()));
    }

    private static class JiaguAdvancementGenerator implements AdvancementProvider.AdvancementGenerator {
        @Override
        public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> saver, ExistingFileHelper existingFileHelper) {
            AdvancementHolder rootAdvancement = Advancement.Builder.advancement()
                    .display(
                            ModItems.BONE_LAMELLA,
                            Component.translatable("advancements.jiagureappear.root.title"),
                            Component.translatable("advancements.jiagureappear.root.description"),
                            ResourceLocation.parse("textures/block/bamboo_block.png"),
                            AdvancementType.TASK,
                            false,
                            false,
                            false
                    )
                    .addCriterion("get_bone_lamella", InventoryChangeTrigger.TriggerInstance.hasItems(
                            ItemPredicate.Builder.item().of(ModItems.BONE_LAMELLA.get()).build()
                    ))
                    .save(saver, ResourceLocation.parse("jiagureappear:jiagu/root"), existingFileHelper);

            AdvancementHolder obtainDictionary = Advancement.Builder.advancement()
                    .parent(rootAdvancement)
                    .display(
                            ModItems.DICTIONARY,
                            Component.translatable("advancements.jiagureappear.obtain_dictionary.title"),
                            Component.translatable("advancements.jiagureappear.obtain_dictionary.description"),
                            null,
                            AdvancementType.GOAL,
                            true,
                            true,
                            false
                    )
                    .addCriterion("get_dictionary", InventoryChangeTrigger.TriggerInstance.hasItems(
                            ItemPredicate.Builder.item().of(ModItems.DICTIONARY.get()).build()
                    ))
                    .save(saver, ResourceLocation.parse("jiagureappear:jiagu/obtain_dictionary"), existingFileHelper);

            AdvancementHolder obtainCangjieDing = Advancement.Builder.advancement()
                    .parent(rootAdvancement)
                    .display(
                            ModBlocks.CANGJIE_DING_TRIPOD,
                            Component.translatable("advancements.jiagureappear.obtain_cangjie_ding.title"),
                            Component.translatable("advancements.jiagureappear.obtain_cangjie_ding.description"),
                            null,
                            AdvancementType.TASK,
                            true,
                            true,
                            false
                    )
                    .addCriterion("get_cangjie_ding_tripod", InventoryChangeTrigger.TriggerInstance.hasItems(
                            ItemPredicate.Builder.item().of(ModBlocks.CANGJIE_DING_TRIPOD.get()).build()
                    ))
                    .save(saver, ResourceLocation.parse("jiagureappear:jiagu/obtain_cangjie_ding"), existingFileHelper);


            ItemStack wenStack = new ItemStack(ModItems.CHARACTER_ITEM.asItem());
            CharacterItem.setInscription(wenStack, "文");
            AdvancementHolder obtainJiagu = Advancement.Builder.advancement()
                    .parent(obtainCangjieDing)
                    .display(
                            wenStack,
                            Component.translatable("advancements.jiagureappear.obtain_jiagu.title"),
                            Component.translatable("advancements.jiagureappear.obtain_jiagu.description"),
                            null,
                            AdvancementType.TASK,
                            true,
                            true,
                            false
                    )
                    .addCriterion("get_character", InventoryChangeTrigger.TriggerInstance.hasItems(
                            ItemPredicate.Builder.item().of(ModItems.CHARACTER_ITEM.get()).build()
                    ))
                    .save(saver, ResourceLocation.parse("jiagureappear:jiagu/obtain_jiagu"), existingFileHelper);

            AdvancementHolder obtainCangjieMorpher = Advancement.Builder.advancement()
                    .parent(obtainCangjieDing)
                    .display(
                            ModBlocks.CANGJIE_MORPHER,
                            Component.translatable("advancements.jiagureappear.obtain_cangjie_morpher.title"),
                            Component.translatable("advancements.jiagureappear.obtain_cangjie_morpher.description"),
                            null,
                            AdvancementType.TASK,
                            true,
                            true,
                            false
                    )
                    .addCriterion("get_cangjie_morpher", InventoryChangeTrigger.TriggerInstance.hasItems(
                            ItemPredicate.Builder.item().of(ModBlocks.CANGJIE_MORPHER.get()).build()
                    ))
                    .save(saver, ResourceLocation.parse("jiagureappear:jiagu/obtain_cangjie_morpher"), existingFileHelper);

            AdvancementHolder disassemble = Advancement.Builder.advancement()
                    .parent(obtainJiagu)
                    .display(
                            ModBlocks.CHARACTER_DISASSEMBLER,
                            Component.translatable("advancements.jiagureappear.disassemble.title"),
                            Component.translatable("advancements.jiagureappear.disassemble.description"),
                            null,
                            AdvancementType.TASK,
                            true,
                            true,
                            false
                    )
                    .addCriterion("disassemble", DisassembleTrigger.disassemble())
                    .save(saver, ResourceLocation.parse("jiagureappear:jiagu/disassemble"), existingFileHelper);

            ItemStack reversalStack = new ItemStack(ModItems.CHARACTER_ITEM.asItem());
            CharacterItem.setInscription(reversalStack, CharacterItem.REVERSAL_INSCRIPTION);
            AdvancementHolder obtainAllCopperJiagu = Advancement.Builder.advancement()
                    .parent(disassemble)
                    .display(
                            reversalStack,
                            Component.translatable("advancements.jiagureappear.obtain_all_copper_characters.title"),
                            Component.translatable("advancements.jiagureappear.obtain_all_copper_characters.description"),
                            null,
                            AdvancementType.CHALLENGE,
                            true,
                            true,
                            false
                    )
                    .addCriterion("simple", DisassembleTrigger.disassemble(CharacterItem.SIMPLE_INSCRIPTION))
                    .addCriterion("simple2", DisassembleTrigger.disassemble(CharacterItem.SIMPLE_INSCRIPTION_2))
                    .addCriterion("reversal", DisassembleTrigger.disassemble(CharacterItem.REVERSAL_INSCRIPTION))
                    .addCriterion("complex", DisassembleTrigger.disassemble(CharacterItem.COMPLEX_INSCRIPTION))
                    .save(saver, ResourceLocation.parse("jiagureappear:jiagu/obtain_all_copper_characters"), existingFileHelper);

            AdvancementHolder obtainAutoDisassembler = Advancement.Builder.advancement()
                    .parent(disassemble)
                    .display(
                            ModBlocks.AUTO_DISASSEMBLER,
                            Component.translatable("advancements.jiagureappear.obtain_auto_disassembler.title"),
                            Component.translatable("advancements.jiagureappear.obtain_auto_disassembler.description"),
                            null,
                            AdvancementType.TASK,
                            true,
                            true,
                            false
                    )
                    .addCriterion("get_auto_disassembler", InventoryChangeTrigger.TriggerInstance.hasItems(
                            ItemPredicate.Builder.item().of(ModBlocks.AUTO_DISASSEMBLER.get()).build()
                    ))
                    .save(saver, ResourceLocation.parse("jiagureappear:jiagu/obtain_auto_disassembler"), existingFileHelper);

            AdvancementHolder rub = Advancement.Builder.advancement()
                    .parent(obtainJiagu)
                    .display(
                            ModBlocks.RUBBING_TABLE,
                            Component.translatable("advancements.jiagureappear.rub.title"),
                            Component.translatable("advancements.jiagureappear.rub.description"),
                            null,
                            AdvancementType.TASK,
                            true,
                            true,
                            false
                    )
                    .addCriterion("rub", RubTrigger.rub())
                    .save(saver, ResourceLocation.parse("jiagureappear:jiagu/rub"), existingFileHelper);

            AdvancementHolder obtainRubbingMachine = Advancement.Builder.advancement()
                    .parent(rub)
                    .display(
                            ModBlocks.RUBBING_MACHINE,
                            Component.translatable("advancements.jiagureappear.obtain_rubbing_machine.title"),
                            Component.translatable("advancements.jiagureappear.obtain_rubbing_machine.description"),
                            null,
                            AdvancementType.TASK,
                            true,
                            true,
                            false
                    )
                    .addCriterion("get_rubbing_machine", InventoryChangeTrigger.TriggerInstance.hasItems(
                            ItemPredicate.Builder.item().of(ModBlocks.RUBBING_MACHINE.get()).build()
                    ))
                    .save(saver, ResourceLocation.parse("jiagureappear:jiagu/obtain_rubbing_machine"), existingFileHelper);

            AdvancementHolder obtainYaowangGourd = Advancement.Builder.advancement()
                    .parent(rootAdvancement)
                    .display(
                            ModBlocks.YAOWANG_GOURD,
                            Component.translatable("advancements.jiagureappear.obtain_yaowang_gourd.title"),
                            Component.translatable("advancements.jiagureappear.obtain_yaowang_gourd.description"),
                            null,
                            AdvancementType.TASK,
                            true,
                            true,
                            false
                    )
                    .addCriterion("get_yaowang_gourd", InventoryChangeTrigger.TriggerInstance.hasItems(
                            ItemPredicate.Builder.item().of(ModBlocks.YAOWANG_GOURD.get()).build()
                    ))
                    .save(saver, ResourceLocation.parse("jiagureappear:jiagu/obtain_yaowang_gourd"), existingFileHelper);

            AdvancementHolder obtainPrescription = Advancement.Builder.advancement()
                    .parent(obtainYaowangGourd)
                    .display(
                            ModItems.PRESCRIPTION,
                            Component.translatable("advancements.jiagureappear.obtain_prescription.title"),
                            Component.translatable("advancements.jiagureappear.obtain_prescription.description"),
                            null,
                            AdvancementType.TASK,
                            true,
                            true,
                            false
                    )
                    .addCriterion("yaowang", YaowangTrigger.yaowang())
                    .save(saver, ResourceLocation.parse("jiagureappear:jiagu/obtain_prescription"), existingFileHelper);

            AdvancementHolder obtainTaoistTalisman = Advancement.Builder.advancement()
                    .parent(obtainCangjieDing)
                    .display(
                            ModItems.YELLOW_PAPER,
                            Component.translatable("advancements.jiagureappear.obtain_taoist_talisman.title"),
                            Component.translatable("advancements.jiagureappear.obtain_taoist_talisman.description"),
                            null,
                            AdvancementType.TASK,
                            true,
                            true,
                            false
                    )
                    .addCriterion("get_taoist_talisman", InventoryChangeTrigger.TriggerInstance.hasItems(
                            ItemPredicate.Builder.item().of(ModItems.TAOIST_TALISMAN.get()).build()
                    ))
                    .save(saver, ResourceLocation.parse("jiagureappear:jiagu/obtain_taoist_talisman"), existingFileHelper);

            AdvancementHolder rubEnchantment = Advancement.Builder.advancement()
                    .parent(obtainTaoistTalisman)
                    .display(
                            ModItems.TAOIST_TALISMAN,
                            Component.translatable("advancements.jiagureappear.rub_enchantment.title"),
                            Component.translatable("advancements.jiagureappear.rub_enchantment.description"),
                            null,
                            AdvancementType.GOAL,
                            true,
                            true,
                            false
                    )
                    .addCriterion("rub_enchantment", RubEnchantmentTrigger.rub())
                    .save(saver, ResourceLocation.parse("jiagureappear:jiagu/rub_enchantment"), existingFileHelper);

            AdvancementHolder obtainWenchangAltar = Advancement.Builder.advancement()
                    .parent(obtainDictionary)
                    .display(
                            ModBlocks.WENCHANG_ALTAR,
                            Component.translatable("advancements.jiagureappear.obtain_wenchang_altar.title"),
                            Component.translatable("advancements.jiagureappear.obtain_wenchang_altar.description"),
                            null,
                            AdvancementType.TASK,
                            true,
                            true,
                            false
                    )
                    .addCriterion("get_wenchang_altar", InventoryChangeTrigger.TriggerInstance.hasItems(
                            ItemPredicate.Builder.item().of(ModBlocks.WENCHANG_ALTAR.get()).build()
                    ))
                    .save(saver, ResourceLocation.parse("jiagureappear:jiagu/obtain_wenchang_altar"), existingFileHelper);

            AdvancementHolder obtainEruditeWenchangAltar = Advancement.Builder.advancement()
                    .parent(obtainWenchangAltar)
                    .display(
                            ModBlocks.ERUDITE_WENCHANG_ALTAR,
                            Component.translatable("advancements.jiagureappear.obtain_erudite_wenchang_altar.title"),
                            Component.translatable("advancements.jiagureappear.obtain_erudite_wenchang_altar.description"),
                            null,
                            AdvancementType.GOAL,
                            true,
                            true,
                            false
                    )
                    .addCriterion("get_erudite_wenchang_altar", InventoryChangeTrigger.TriggerInstance.hasItems(
                            ItemPredicate.Builder.item().of(ModBlocks.ERUDITE_WENCHANG_ALTAR.get()).build()
                    ))
                    .save(saver, ResourceLocation.parse("jiagureappear:jiagu/obtain_erudite_wenchang_altar"), existingFileHelper);

            AdvancementHolder obtainAmethystInscription = Advancement.Builder.advancement()
                    .parent(obtainJiagu)
                    .display(
                            ModItems.AMETHYST_UPGRADE_SMITHING_TEMPLATE,
                            Component.translatable("advancements.jiagureappear.obtain_amethyst_upgrade.title"),
                            Component.translatable("advancements.jiagureappear.obtain_amethyst_upgrade.description"),
                            null,
                            AdvancementType.CHALLENGE,
                            true,
                            true,
                            false
                    )
                    .addCriterion("get_amethyst_inscription", InventoryChangeTrigger.TriggerInstance.hasItems(
                            ItemPredicate.Builder.item().of(ModTags.AMETHYST_INSCRIPTIONS).build()
                    ))
                    .save(saver, ResourceLocation.parse("jiagureappear:jiagu/obtain_amethyst_inscription"), existingFileHelper);

            AdvancementHolder obtainPrescriptionUser = Advancement.Builder.advancement()
                    .parent(obtainPrescription)
                    .display(
                            Items.FERMENTED_SPIDER_EYE,
                            Component.translatable("advancements.jiagureappear.obtain_prescription_users.title"),
                            Component.translatable("advancements.jiagureappear.obtain_prescription_users.description"),
                            null,
                            AdvancementType.CHALLENGE,
                            true,
                            true,
                            false
                    )
                    .addCriterion("get_prescription_user", InventoryChangeTrigger.TriggerInstance.hasItems(
                            ItemPredicate.Builder.item().of(ModTags.PRESCRIPTION_USERS).build()
                    ))
                    .save(saver, ResourceLocation.parse("jiagureappear:jiagu/obtain_prescription_users"), existingFileHelper);
        }
    }
}

