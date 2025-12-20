package net.lghast.jiagu.network;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber()
public class NetworkRegistry {

    @SubscribeEvent
    public static void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1.0");

        registrar.playToServer(
                OpenPrescriptionGuiPayload.TYPE,
                OpenPrescriptionGuiPayload.STREAM_CODEC,
                OpenPrescriptionGuiPayload::handle
        );

        registrar.playToServer(
                JiaguInfoSearchPayload.TYPE,
                JiaguInfoSearchPayload.STREAM_CODEC,
                JiaguInfoSearchPayload::handle
        );

        registrar.playToServer(
                CraftPrescriptionPayload.TYPE,
                CraftPrescriptionPayload.STREAM_CODEC,
                CraftPrescriptionPayload::handle
        );
    }
}
