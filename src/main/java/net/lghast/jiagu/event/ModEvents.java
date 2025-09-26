package net.lghast.jiagu.event;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.lghast.jiagu.JiaguReappear;
import net.lghast.jiagu.common.content.item.CharacterItem;
import net.lghast.jiagu.common.content.item.PrescriptionItem;
import net.lghast.jiagu.register.content.ModItems;
import net.lghast.jiagu.register.content.ModPotions;
import net.lghast.jiagu.register.content.ModVillagers;
import net.lghast.jiagu.utils.CharacterInfo;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@EventBusSubscriber(modid = JiaguReappear.MOD_ID)
public class ModEvents {
    @SubscribeEvent
    public static void onBrewingRecipeRegister(RegisterBrewingRecipesEvent event) {
        PotionBrewing.Builder builder = event.getBuilder();
        builder.addMix(Potions.AWKWARD, ModItems.SOUR_BERRIES.get(), ModPotions.APPETIZING);
        builder.addMix(ModPotions.APPETIZING, Items.REDSTONE, ModPotions.LONG_APPETIZING);
        builder.addMix(Potions.AWKWARD, ModItems.SHADOW_BERRIES.get(), ModPotions.DARKNESS);
        builder.addMix(ModPotions.DARKNESS, Items.REDSTONE, ModPotions.LONG_DARKNESS);
    }

    private static final List<Holder.Reference<MobEffect>> EFFECT_HOLDERS =
            BuiltInRegistries.MOB_EFFECT.holders()
                    .filter(holder -> !holder.value().isInstantenous())
                    .toList();

    private static Holder<MobEffect> getRandomEffect(){
        if (EFFECT_HOLDERS.isEmpty()) return null;

       return EFFECT_HOLDERS.get(ThreadLocalRandom.current().nextInt(EFFECT_HOLDERS.size()));
    }

    @SubscribeEvent
    public static void addCustomTrades(VillagerTradesEvent event){
        if(event.getType() == ModVillagers.SCHOLAR.value()){
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();

            trades.get(1).add((entity,randomSource) ->{
                if(Math.random()<0.5){
                    return new MerchantOffer(
                            new ItemCost(Items.PAPER, 24),
                            new ItemStack(Items.EMERALD, 1), 20, 3, 0.05f);
                }
                return new MerchantOffer(
                    new ItemCost(Items.EMERALD, 5),
                    new ItemStack(ModItems.BONE_LAMELLA.get(), 2), 20, 2, 0.05f);
            });
            trades.get(1).add((entity,randomSource) -> {
                ItemStack itemStack = new ItemStack(ModItems.CHARACTER_ITEM.get(), 3);
                CharacterItem.setInscription(itemStack, CharacterInfo.getLuckyInscription());
                return new MerchantOffer(
                        new ItemCost(Items.EMERALD, 10),
                        Optional.of(new ItemCost(ModItems.BONE_LAMELLA, 1)),
                        itemStack, 20, 2, 0.05f
                );
            });

            trades.get(2).add((entity,randomSource) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 5),
                    new ItemStack(ModItems.TURTLE_PLASTRON.get(), 1), 10, 10, 0.05f
            ));
            trades.get(2).add((entity,randomSource) -> new MerchantOffer(
                    new ItemCost(Math.random()<0.5 ? Items.INK_SAC : Items.BLACK_DYE, 5),
                    new ItemStack(Items.EMERALD, 1), 12, 10, 0.05f
            ));
            trades.get(2).add((entity,randomSource) -> new MerchantOffer(
                    new ItemCost(Items.CHARCOAL, 8),
                    new ItemStack(Items.EMERALD, 1), 12, 10, 0.05f
            ));

            trades.get(3).add((entity,randomSource) -> {
                ItemStack itemStack = new ItemStack(ModItems.CHARACTER_ITEM.get(), 3);
                CharacterItem.setInscription(itemStack, CharacterInfo.getLuckyInscriptionAdvanced());
                return new MerchantOffer(
                        new ItemCost(Items.EMERALD, 10),
                        Optional.of(new ItemCost(ModItems.BONE_LAMELLA, 1)),
                        itemStack, 20, 12, 0.05f
                );
            });
            trades.get(3).add((entity,randomSource) -> new MerchantOffer(
                    new ItemCost(Items.BAMBOO, 32),
                    new ItemStack(Items.EMERALD, 1), 10, 12, 0.05f
            ));

            trades.get(4).add((entity,randomSource) -> {
                ItemStack itemStack = new ItemStack(ModItems.PRESCRIPTION.get(), 1);
                PrescriptionItem.setEffect(itemStack, getRandomEffect());
                return new MerchantOffer(
                        new ItemCost(Items.EMERALD, 25),
                        itemStack, 5, 16, 0.1f
                );
            });
            trades.get(4).add((entity,randomSource) -> new MerchantOffer(
                    new ItemCost(Items.COPPER_INGOT, 25),
                    new ItemStack(Items.EMERALD, 1), 12, 10, 0.05f
            ));

            trades.get(5).add((entity,randomSource) -> {
                ItemStack itemStack = new ItemStack(ModItems.CHARACTER_ITEM.get(), 1);
                String luckyInscription = CharacterInfo.getLuckyInscriptionAny();
                CharacterItem.setInscription(itemStack, luckyInscription);
                int level = (int)(CharacterInfo.getFloatValue(luckyInscription)/1000);
                int cost = switch (level) {
                    case 0,6 -> 3;
                    case 1 -> 6;
                    case 2 -> 10;
                    case 3 -> 15;
                    case 5 -> 1;
                    case 7 -> 64;
                    default -> 4;
                };
                return new MerchantOffer(
                        new ItemCost(Items.EMERALD, cost),
                        Optional.of(new ItemCost(ModItems.BONE_LAMELLA, 1)),
                        itemStack, 20, 5 + cost, 0.05f
                );
            });
        }

        if(event.getType() == VillagerProfession.BUTCHER) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();

            trades.get(5).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(ModItems.SOUR_BERRIES, 10),
                    new ItemStack(Items.EMERALD, 1), 36, 30, 0.05f
            ));
        }
    }
}
