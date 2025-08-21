package net.lghast.jiagu.register;

import net.lghast.jiagu.JiaguReappear;
import net.lghast.jiagu.common.entity.ParasiteSporeEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(Registries.ENTITY_TYPE, JiaguReappear.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<ParasiteSporeEntity>> PARASITE_SPORE =
            ENTITY_TYPES.register("parasite_spore",
                    () -> EntityType.Builder.<ParasiteSporeEntity> of (ParasiteSporeEntity::new, MobCategory.MISC)
                            .sized(0.25F, 0.25F)
                            .clientTrackingRange(4)
                            .updateInterval(10)
                            .build("parasite_spore"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
