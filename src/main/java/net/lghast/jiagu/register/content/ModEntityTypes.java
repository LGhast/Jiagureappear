package net.lghast.jiagu.register.content;

import net.lghast.jiagu.JiaguReappear;
import net.lghast.jiagu.common.content.entity.ParasiteSporeEntity;
import net.lghast.jiagu.common.content.entity.StoneShotEntity;
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
                            .sized(0.2F, 0.2F)
                            .clientTrackingRange(4)
                            .updateInterval(10)
                            .build("parasite_spore"));

    public static final DeferredHolder<EntityType<?>, EntityType<StoneShotEntity>> STONE_SHOT =
            ENTITY_TYPES.register("stone_shot",
                    () -> EntityType.Builder.<StoneShotEntity> of (StoneShotEntity::new, MobCategory.MISC)
                            .sized(0.2F, 0.2F)
                            .clientTrackingRange(4)
                            .updateInterval(10)
                            .build("stone_shot"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
