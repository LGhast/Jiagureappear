package net.lghast.jiagu.register.content;

import com.google.common.collect.ImmutableSet;
import net.lghast.jiagu.JiaguReappear;
import net.lghast.jiagu.register.content.ModBlocks;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModVillagers {
    public static final DeferredRegister<PoiType> POI_TYPES =
            DeferredRegister.create(BuiltInRegistries.POINT_OF_INTEREST_TYPE, JiaguReappear.MOD_ID);
    public static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSIONS =
            DeferredRegister.create(BuiltInRegistries.VILLAGER_PROFESSION, JiaguReappear.MOD_ID);

    public static final Holder<PoiType> SCHOLAR_POI = POI_TYPES.register("scholar_poi",
            ()->new PoiType(ImmutableSet.copyOf(ModBlocks.RUBBING_TABLE.get().getStateDefinition().getPossibleStates()), 1, 1));

    public static final Holder<VillagerProfession> SCHOLAR = VILLAGER_PROFESSIONS.register("scholar",
            ()->new VillagerProfession("scholar", holder->holder.value() == SCHOLAR_POI.value(),
                    poiTypeHolder -> poiTypeHolder.value() == SCHOLAR_POI.value(),
                    ImmutableSet.of(Items.BOOK, Items.ENCHANTED_BOOK, Items.BOOKSHELF, Items.WRITABLE_BOOK, Items.WRITTEN_BOOK, Items.KNOWLEDGE_BOOK),
                    ImmutableSet.of(),
                    SoundEvents.CHISELED_BOOKSHELF_PICKUP));


    public static void register(IEventBus eventBus){
        POI_TYPES.register(eventBus);
        VILLAGER_PROFESSIONS.register(eventBus);
    }
}
