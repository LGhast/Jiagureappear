package net.lghast.jiagu.client.misc;

import net.lghast.jiagu.JiaguReappear;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ModelEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("unused")
@EventBusSubscriber(modid = JiaguReappear.MOD_ID, value = Dist.CLIENT)
public class CharacterModBusEvent {
    private static final Logger LOGGER = LogManager.getLogger();
    @SubscribeEvent
    public static void modifyBakingResult(ModelEvent.ModifyBakingResult event) {
        ModelResourceLocation key = ModelResourceLocation.inventory(
                ResourceLocation.fromNamespaceAndPath(JiaguReappear.MOD_ID, "character"));
        BakedModel original = event.getModels().get(key);
        if (original == null) {
            LOGGER.error("Character item model not present in baking results");
        }
        if (original != null) {
            event.getModels().put(key, new CharacterItemModel(original));
        }
        LOGGER.info("Character model replaced successfully, original={}", original != null);
    }
}
