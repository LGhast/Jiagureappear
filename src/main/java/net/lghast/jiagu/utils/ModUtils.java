package net.lghast.jiagu.utils;

import net.lghast.jiagu.JiaguReappear;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Objects;

public class ModUtils {
    public static void spawnItem(ServerLevel level, double x, double y, double z, ItemStack stack, boolean pickUpDelay) {
        if (stack.isEmpty()) return;
        ItemEntity itemEntity = new ItemEntity(level, x, y, z, stack);
        if(pickUpDelay) itemEntity.setDefaultPickUpDelay();
        level.addFreshEntity(itemEntity);
    }

    public static void spawnItemWithMotion(ServerLevel level, double x, double y, double z, ItemStack stack, boolean pickUpDelay) {
        if (stack.isEmpty()) return;
        ItemEntity itemEntity = new ItemEntity(level, x, y, z, stack,
                level.random.nextGaussian() * 0.05, 0.2, level.random.nextGaussian() * 0.05
        );
        if(pickUpDelay) itemEntity.setDefaultPickUpDelay();
        level.addFreshEntity(itemEntity);
    }

    public static void spawnParticles(Level level, ParticleOptions particle,
                             double x, double y, double z,
                             double dx, double dy, double dz,
                             double speed, int count) {

        if (level.isClientSide) {
            for (int i = 0; i < count; i++) {
                double offsetX = (level.random.nextDouble() - 0.5) * speed;
                double offsetY = (level.random.nextDouble() - 0.5) * speed;
                double offsetZ = (level.random.nextDouble() - 0.5) * speed;

                level.addParticle(particle,
                        x + offsetX,
                        y + offsetY,
                        z + offsetZ,
                        dx, dy, dz);
            }
        }
    }

    public static void spawnParticlesForAll(ServerLevel serverLevel, ParticleOptions particle,
                                   double x, double y, double z,
                                   double dx, double dy, double dz,
                                   int count, double range) {

        serverLevel.sendParticles(particle,
                x, y, z,
                count,
                dx, dy, dz,
                range);

    }

    public static void damage(ItemStack stack){
        if(stack.isDamageableItem()){
            int damage = stack.getDamageValue()+1;
            if(damage>=stack.getMaxDamage()){
                stack.shrink(1);
            }else{
                stack.setDamageValue(damage);
            }
        }
    }


    public static String getCharacters(String characters){
        return characters.replace(" ", "").replace("“", "").replace("”", "");
    }
    public static String getCharacters(ItemStack stack){
        return stack.getHoverName().getString().replace(" ", "").replace("“", "").replace("”", "");
    }
    public static String getCharacters(Holder<Enchantment> holder, int level){
        return Enchantment.getFullname(holder, level).getString().replace(" ", "").replace("“", "").replace("”", "");
    }
    public static String getCharacters(Entity entity){
        return Objects.requireNonNull(entity.getDisplayName()).getString().replace(" ", "").replace("“", "").replace("”", "");
    }

    public static String insertLineBreaks(String input, int interval) {
        if (input == null || input.isEmpty() || interval <= 0) {
            return input;
        }

        StringBuilder result = new StringBuilder();
        int length = input.length();

        for (int i = 0; i < length; i += interval) {
            int endIndex = Math.min(i + interval, length);
            result.append(input, i, endIndex);
            if (endIndex < length) {
                result.append('\n');
            }
        }

        return result.toString();
    }
}
