package net.lghast.jiagu.datagen;

import net.lghast.jiagu.JiaguReappear;
import net.lghast.jiagu.register.ModItems;
import net.lghast.jiagu.utils.CharacterInfo;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.Comparator;
import java.util.List;

public class ModItemModelProvider extends ItemModelProvider {

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, JiaguReappear.MOD_ID, existingFileHelper);
    }

    private static final List<InscriptionModel> INSCRIPTIONS =
            CharacterInfo.CHARACTER_DATA.entrySet().stream()
                    .map(entry -> new InscriptionModel(
                            entry.getKey(),
                            entry.getValue().floatValue(),
                            entry.getValue().identifier()
                    ))
                    .sorted(Comparator.comparingDouble(InscriptionModel::predicateValue))
                    .toList();

    @Override
    protected void registerModels() {
        basicItem(ModItems.INFINITE_PAPYRUS.get());
        basicItem(ModItems.BONE_LAMELLA.get());
        basicItem(ModItems.TURTLE_PLASTRON.get());
        basicItem(ModItems.YELLOW_PAPER.get());
        basicItem(ModItems.TAOIST_TALISMAN.get());
        basicItem(ModItems.PRESCRIPTION.get());
        basicItem(ModItems.YOLIME.get());
        basicItem(ModItems.YOLIME_BREAD.get());
        basicItem(ModItems.SHADOW_BERRIES.get());
        basicItem(ModItems.SOUR_BERRIES.get());
        basicItem(ModItems.NI_CONVERSE_AMETHYST.get());
        basicItem(ModItems.NI_CONVERSE_AMETHYST_DORMANT.get());
        basicItem(ModItems.JIAN_SWORD_AMETHYST.get());
        basicItem(ModItems.BIAO_GALE_AMETHYST.get());
        basicItem(ModItems.AMETHYST_UPGRADE_SMITHING_TEMPLATE.get());

        withExistingParent("item/characters/default", "minecraft:item/generated")
                .texture("layer0", modLoc("item/characters/default"));

        for (InscriptionModel model : INSCRIPTIONS) {
            withExistingParent("item/characters/" + model.modelName(), "minecraft:item/generated")
                    .texture("layer0", modLoc("item/characters/" + model.modelName()));
        }

        ItemModelBuilder characterBuilder = withExistingParent("item/character", "minecraft:item/generated")
                .texture("layer0", modLoc("item/characters/default"));

        ResourceLocation predicateId = ResourceLocation.parse("jiagureappear:inscription");

        for (InscriptionModel model : INSCRIPTIONS) {
            characterBuilder = characterBuilder.override()
                    .predicate(predicateId, model.predicateValue())
                    .model(getExistingFile(modLoc("item/characters/" + model.modelName())))
                    .end();
        }
    }

    private record InscriptionModel(String character, float predicateValue, String modelName) {}
}
