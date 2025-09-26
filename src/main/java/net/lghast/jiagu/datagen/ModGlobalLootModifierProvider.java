package net.lghast.jiagu.datagen;

import net.lghast.jiagu.JiaguReappear;
import net.lghast.jiagu.common.system.loot.AddItemModifier;
import net.lghast.jiagu.register.content.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithEnchantedBonusCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.LootTableIdCondition;

import java.util.concurrent.CompletableFuture;

public class ModGlobalLootModifierProvider extends GlobalLootModifierProvider {
    public ModGlobalLootModifierProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, JiaguReappear.MOD_ID);
    }

    @Override
    protected void start() {
        this.add("bone_lamella_from_skeleton", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(ResourceLocation.withDefaultNamespace("entities/skeleton")).build(),
                LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries,0.2f,0.2f).build()
        }, ModItems.BONE_LAMELLA.get()));

        this.add("bone_lamella_from_stray", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(ResourceLocation.withDefaultNamespace("entities/stray")).build(),
                LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries,0.2f,0.2f).build()
        }, ModItems.BONE_LAMELLA.get()));

        this.add("bone_lamella_from_skeleton_horse", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(ResourceLocation.withDefaultNamespace("entities/skeleton_horse")).build(),
                LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries,0.4f,0.2f).build()
        }, ModItems.BONE_LAMELLA.get()));

        this.add("bone_lamella_from_bogged", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(ResourceLocation.withDefaultNamespace("entities/bogged")).build(),
                LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries,0.2f,0.2f).build()
        }, ModItems.BONE_LAMELLA.get()));

        this.add("turtle_plastron_from_turtle", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(ResourceLocation.withDefaultNamespace("entities/turtle")).build(),
                LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries,0.2f,0.2f).build()
        }, ModItems.TURTLE_PLASTRON.get()));
    }
}
