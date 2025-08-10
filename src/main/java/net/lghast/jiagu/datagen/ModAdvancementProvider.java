package net.lghast.jiagu.datagen;

import net.lghast.jiagu.advancement.IdiomFormedTrigger;
import net.lghast.jiagu.utils.CharacterInfo;
import net.lghast.jiagu.utils.CharacterStructure;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ModAdvancementProvider extends AdvancementProvider {
    public ModAdvancementProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, existingFileHelper, List.of(new IdiomAdvancementGenerator()));
    }

    private static class IdiomAdvancementGenerator implements AdvancementProvider.AdvancementGenerator {
        @Override
        public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> saver, ExistingFileHelper existingFileHelper) {
            createIdiomAdvancement(saver, existingFileHelper, "xiaoxinyiyi", "小心翼翼", new ItemStack(Items.BRUSH));
            createIdiomAdvancement(saver, existingFileHelper, "yishierniao", "一石二鳥", new ItemStack(Items.CROSSBOW));
            createIdiomAdvancement(saver, existingFileHelper, "jiuniuyimao", "九牛一毛", new ItemStack(Items.LEATHER));
            createIdiomAdvancement(saver, existingFileHelper, "shuirujiaorong", "水乳交融", new ItemStack(Items.MILK_BUCKET));
            createIdiomAdvancement(saver, existingFileHelper, "ruyudeshui", "如魚得水", new ItemStack(Items.TROPICAL_FISH_BUCKET));
            createIdiomAdvancement(saver, existingFileHelper, "congshanruliu", "從善如流", new ItemStack(Items.KNOWLEDGE_BOOK));
            createIdiomAdvancement(saver, existingFileHelper, "gufangzishang", "孤芳自賞", new ItemStack(Items.POPPY));
            createIdiomAdvancement(saver, existingFileHelper, "guangmingleiluo", "光明磊落", new ItemStack(Items.LANTERN));
            createIdiomAdvancement(saver, existingFileHelper, "ziqiangbuxi", "自強不息", new ItemStack(Items.BLAZE_POWDER));
        }

        private void createIdiomAdvancement(
                Consumer<AdvancementHolder> saver,
                ExistingFileHelper existingFileHelper,
                String advancementId,
                String idiom,
                ItemStack icon
        ) {
            Advancement.Builder builder = Advancement.Builder.advancement()
                    .parent(AdvancementSubProvider.createPlaceholder("jiagureappear:idiom/root"))
                    .display(
                            icon,
                            Component.translatable("advancements.jiagureappear.idiom."+advancementId+".title"),
                            Component.translatable("advancements.jiagureappear.idiom."+advancementId+".description"),
                            null,
                            AdvancementType.GOAL,
                            true,
                            true,
                            false
                    )
                    .addCriterion(
                            "formed_idiom",
                            IdiomFormedTrigger.formedIdiom(idiom)
                    )
                    .sendsTelemetryEvent();

            ResourceLocation advancementLoc = ResourceLocation.parse("jiagureappear:idiom/" + advancementId);
            builder.save(saver, advancementLoc, existingFileHelper);
        }
    }
}
