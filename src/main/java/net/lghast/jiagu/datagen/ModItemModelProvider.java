package net.lghast.jiagu.datagen;

import net.lghast.jiagu.JiaguReappear;
import net.lghast.jiagu.register.content.ModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, JiaguReappear.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ModItems.INFINITE_PAPYRUS.get());
        basicItem(ModItems.BONE_LAMELLA.get());
        basicItem(ModItems.TURTLE_PLASTRON.get());
        basicItem(ModItems.YELLOW_PAPER.get());
        basicItem(ModItems.TAOIST_TALISMAN.get());
        basicItem(ModItems.EMPTY_PRESCRIPTION.get());
        basicItem(ModItems.YOLIME.get());
        basicItem(ModItems.YOLIME_BREAD.get());
        basicItem(ModItems.SHADOW_BERRIES.get());
        basicItem(ModItems.SOUR_BERRIES.get());
        basicItem(ModItems.ZUCCHINI.get());
        basicItem(ModItems.COOKED_ZUCCHINI.get());
        basicItem(ModItems.ZUCCHINI_SOUP.get());
        basicItem(ModItems.PARASITE_SPORE.get());
        basicItem(ModItems.NI_CONVERSE_AMETHYST.get());
        basicItem(ModItems.NI_CONVERSE_AMETHYST_DORMANT.get());
        basicItem(ModItems.JIAN_SWORD_AMETHYST.get());
        basicItem(ModItems.BIAO_GALE_AMETHYST.get());
        basicItem(ModItems.AMETHYST_UPGRADE_SMITHING_TEMPLATE.get());
        basicItem(ModItems.DICTIONARY.get());
    }
}
