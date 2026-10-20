package net.lghast.jiagu.client.misc;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

@OnlyIn(Dist.CLIENT)
@SuppressWarnings("unused")
@EventBusSubscriber(modid = "jiagureappear", value = Dist.CLIENT)
public class ModKeyBindings {
    public static final KeyMapping OPEN_CHARACTER_CRAFTER = new KeyMapping(
            "key.jiagureappear.open_character_crafter",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_R,
            "key.categories.jiagureappear"
    );

    public static final KeyMapping OPEN_PRESCRIPTION_GUI = new KeyMapping(
            "key.jiagureappear.open_prescription_gui",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_X,
            "key.categories.jiagureappear"
    );


    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(OPEN_CHARACTER_CRAFTER);
        event.register(OPEN_PRESCRIPTION_GUI);
    }
}
