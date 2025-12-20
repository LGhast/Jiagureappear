package net.lghast.jiagu.utils;

import net.lghast.jiagu.config.ClientConfig;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;

import java.util.*;

public class PrescriptionInfo {
    private static Map<Holder<MobEffect>, Float> prescriptionMap = new HashMap<>();

    public static void reloadFromConfig() {
        Map<Holder<MobEffect>, Float> newMap = new HashMap<>();
        List<? extends String> mappings = ClientConfig.PRESCRIPTION_MAPPINGS.get();

        for (String mapping : mappings) {
            try {
                String[] parts = mapping.split("=");
                if (parts.length != 2) continue;

                ResourceLocation effectId = ResourceLocation.tryParse(parts[0]);
                float value = Float.parseFloat(parts[1]);

                if (effectId != null) {
                    Optional<Holder.Reference<MobEffect>> effectHolder = BuiltInRegistries.MOB_EFFECT.getHolder(effectId);
                    effectHolder.ifPresent(holder -> newMap.put(holder, value));
                }
            } catch (Exception e) {
                System.err.println("Invalid prescription mapping: " + mapping);
            }
        }
        prescriptionMap = newMap;
    }

    public static float getFloatValue(Holder<MobEffect> effectHolder) {
        return prescriptionMap.getOrDefault(effectHolder, 0.0f);
    }
}
