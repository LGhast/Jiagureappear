package net.lghast.jiagu.utils;

import net.lghast.jiagu.register.system.ModTags;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.Objects;
import java.util.function.Predicate;

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

    public static void spawnParticlesForAll(ServerLevel serverLevel, ParticleOptions particle,
                                   double x, double y, double z,
                                   double dx, double dy, double dz,
                                   int count, double speed) {

        serverLevel.sendParticles(particle,
                x, y, z,
                count,
                dx, dy, dz,
                speed);

    }

    public static void damage(ItemStack stack){
        damage(stack, 1);
    }

    public static void damage(ItemStack stack,int count){
        if(stack.isDamageableItem()){
            int damage = stack.getDamageValue()+count;
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
        return getCharacters(stack.getHoverName().getString());
    }
    public static String getCharacters(Holder<Enchantment> holder, int level){
        return getCharacters(Enchantment.getFullname(holder, level).getString());
    }
    public static String getCharacters(Entity entity){
        String base = Objects.requireNonNull(entity.getDisplayName()).getString();
        if(entity.getType().is(ModTags.OVIS) && entity instanceof AgeableMob mob && mob.isBaby()){
            base = base.replace("羊", "羔");
        }
        return getCharacters(base);
    }
    public static String getCharacters(MobEffect effect){
        return getCharacters(effect.getDisplayName().getString());
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

    public static boolean tryPutOrSupplement(Player player, ItemStack heldItem, ItemStack slotItem,
                                                 Predicate<ItemStack> validator, SlotSetter setSlot) {

        if (player.isSpectator() || heldItem.isEmpty() || !validator.test(heldItem)) {
            return false;
        }
        if (slotItem.isEmpty()) {
            setSlot.set(heldItem.copy());
            if (!player.isCreative()) {
                heldItem.setCount(0);
            }
            return true;
        }

        if (canSupplement(slotItem, heldItem, validator)) {
            int spaceLeft = slotItem.getMaxStackSize() - slotItem.getCount();
            int toTransfer = Math.min(spaceLeft, heldItem.getCount());

            if (toTransfer > 0) {
                ItemStack newSlotItem = slotItem.copy();
                newSlotItem.grow(toTransfer);
                setSlot.set(newSlotItem);

                if (!player.isCreative()) {
                    heldItem.shrink(toTransfer);
                }
                return true;
            }
        }
        return false;
    }

    public static boolean canSupplement(ItemStack slotItem, ItemStack heldItem, Predicate<ItemStack> validator) {
        return !heldItem.isEmpty() &&
                ItemStack.isSameItemSameComponents(slotItem, heldItem) &&
                validator.test(heldItem);
    }

    public static boolean canSupplement(ItemStack slotItem, ItemStack heldItem) {
        return !heldItem.isEmpty() && ItemStack.isSameItemSameComponents(slotItem, heldItem);
    }

    @FunctionalInterface
    public interface SlotSetter {
        void set(ItemStack stack);
    }
}
