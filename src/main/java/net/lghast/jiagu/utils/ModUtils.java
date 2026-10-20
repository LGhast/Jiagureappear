package net.lghast.jiagu.utils;

import net.lghast.jiagu.register.system.ModTags;
import net.lghast.jiagu.lzh.LzhMappings;
import net.minecraft.client.gui.Font;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.neoforged.fml.ModList;

import java.util.Comparator;
import java.util.List;

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

    public static void spawnParticles(ServerLevel serverLevel, ParticleOptions particle, double x, double y, double z,
                                      double dx, double dy, double dz, int count, double speed) {
        serverLevel.sendParticles(particle, x, y, z, count, dx, dy, dz, speed);
    }

    public static String modifyName(String name){
        if(name == null){
            return null;
        }
        return name.replace(" ", "").replace("“", "").replace("”", "").replace("·", "");
    }

    public static String modifyName(ItemStack stack){
        return modifyName(LzhMappings.getLzhName(stack));
    }

    public static String modifyName(Block block){
        return modifyName(LzhMappings.getLzhName(block));
    }

    public static String modifyName(MobEffect effect){
        return modifyName(LzhMappings.getLzhName(effect));
    }

    public static String modifyName(EntityType<?> entity){
        return modifyName(LzhMappings.getLzhName(entity));
    }

    public static String modifyName(Entity entity){
        String base = LzhMappings.getLzhName(entity);
        if(entity.getType().is(ModTags.OVIS) && entity instanceof AgeableMob mob && mob.isBaby()){
            base = base.replace("羊", "羔");
        }
        return modifyName(base);
    }

    public static String modifyName(Holder<Enchantment> holder, int level){
        String base = LzhMappings.getLzhName(holder);
        if(base == null) return null;

        String levelString = switch (level){
            case 1 -> "";
            case 2 -> "二階";
            case 3 -> "三階";
            case 4 -> "四階";
            case 5 -> "五階";
            case 6 -> "六階";
            case 7 -> "七階";
            case 8 -> "八階";
            case 9 -> "九階";
            case 10 -> "十階";
            default -> level + "階";
        };
        return modifyName(base + levelString);
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

    public static String truncateToWidth(Font font, String text, int maxWidth) {
        if (font.width(text) <= maxWidth) {
            return text;
        }

        String ellipsis = "...";
        int ellipsisWidth = font.width(ellipsis);
        int targetWidth = maxWidth - ellipsisWidth;

        if (targetWidth <= 0) {
            return ellipsis;
        }

        int low = 0;
        int high = text.length();
        int bestPos = 0;

        while (low <= high) {
            int mid = (low + high) / 2;
            String substring = text.substring(0, mid);
            int width = font.width(substring);

            if (width <= targetWidth) {
                bestPos = mid;
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        if (bestPos == 0) {
            bestPos = 1;
        }

        return text.substring(0, bestPos).trim() + ellipsis;
    }

    public static void sortResourceLocation(List<ResourceLocation> list) {
        list.sort(Comparator.comparing(ResourceLocation::getPath));
    }

    public static boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }
}
