package net.lghast.jiagu.client.misc;

import net.lghast.jiagu.JiaguReappear;
import net.lghast.jiagu.client.misc.ModKeyBindings;
import net.lghast.jiagu.client.particle.JiaguFloatingParticles;
import net.lghast.jiagu.client.particle.JiaguParticles;
import net.lghast.jiagu.client.particle.ModParticles;
import net.lghast.jiagu.client.screen.PrescriptionScreen;
import net.lghast.jiagu.common.content.item.PrescriptionUserItem;
import net.lghast.jiagu.network.OpenPrescriptionGuiPayload;
import net.lghast.jiagu.register.system.ModMenus;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

import java.util.Objects;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = JiaguReappear.MOD_ID)
public class ClientEventHandler {

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.level == null) return;

        while (ModKeyBindings.OPEN_PRESCRIPTION_GUI.consumeClick()) {
            if (minecraft.player.getMainHandItem().getItem() instanceof PrescriptionUserItem) {
                Objects.requireNonNull(minecraft.getConnection()).send(new OpenPrescriptionGuiPayload());
            }
        }
    }

    @SubscribeEvent
    public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticles.JIAGU_PARTICLES.get(), JiaguParticles.Provider::new);
        event.registerSpriteSet(ModParticles.JIAGU_FLOATING_PARTICLES.get(), JiaguFloatingParticles.Provider::new);
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenus.PRESCRIPTION_MENU.get(), PrescriptionScreen::new);
    }
}
