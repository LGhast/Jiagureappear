package net.lghast.jiagu.item;

import net.lghast.jiagu.register.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.List;

public class PrescriptionItem extends Item {
    public PrescriptionItem(Properties properties) {
        super(properties.rarity(Rarity.RARE).stacksTo(1));
    }
}
