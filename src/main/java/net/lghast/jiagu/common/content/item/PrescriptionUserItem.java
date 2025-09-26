package net.lghast.jiagu.common.content.item;

import net.lghast.jiagu.register.content.ModItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public abstract class PrescriptionUserItem extends Item {
    int usingTicks = 0;

    public PrescriptionUserItem(Properties properties) {
        super(properties.rarity(Rarity.EPIC).stacksTo(1).durability(60));
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);

        if (player.getCooldowns().isOnCooldown(this)) {
            return InteractionResultHolder.fail(stack);
        }

        if (stack.getDamageValue() >= stack.getMaxDamage()) {
            return InteractionResultHolder.fail(stack);
        }

        usingTicks = 0;
        player.startUsingItem(usedHand);
        return InteractionResultHolder.success(stack);
    }

    protected int getUsingTicks(){
        return usingTicks;
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        usingTicks++;
        super.onUseTick(level, livingEntity, stack, remainingUseDuration);
    }

    @Override
    public void releaseUsing(ItemStack userStack, Level level, LivingEntity entity, int chargedTime) {
        if (level.isClientSide) {
            return;
        }
        if(!(entity instanceof Player player)) {
            return;
        }

        ItemStack prescriptionStack = getFirstPrescriptionFromItemStack(userStack);
        if(!prescriptionStack.isEmpty() && prescriptionStack.is(ModItems.PRESCRIPTION)) {
            prescriptionEffect(prescriptionStack, userStack, level, player);
            return;
        }
        normalEffect(userStack, level, player);
    }

    abstract protected void prescriptionEffect(ItemStack prescriptionStack, ItemStack userStack, Level level, Player player);

    abstract protected void normalEffect(ItemStack userStack, Level level, Player player);

    protected void useSuccess(int damage, Player player,ItemStack stack){
        stack.hurtAndBreak(damage, player, EquipmentSlot.MAINHAND);

        Level level = player.level();
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.AMETHYST_CLUSTER_BREAK, SoundSource.NEUTRAL);
        player.getCooldowns().addCooldown(this, getCooldownTicks(stack, level));
    }

    abstract protected int getCooldownTicks(ItemStack stack, Level level);

    abstract protected int getAmplifier(ItemStack stack, Level level);

    abstract protected int getDuration(ItemStack stack, Level level);

    private ItemStack getFirstPrescriptionFromItemStack(ItemStack stack) {
        ItemContainerContents containerContents = stack.get(DataComponents.CONTAINER);
        if (containerContents != null) {
            for (int i = 0; i < containerContents.getSlots(); i++) {
                ItemStack prescriptionStack = containerContents.getStackInSlot(i);
                if (!prescriptionStack.isEmpty() && prescriptionStack.getItem() instanceof PrescriptionItem) {
                    return prescriptionStack;
                }
            }
        }
        return ItemStack.EMPTY;
    }

    protected void updatePrescriptionInItemStack(ItemStack stack, ItemStack updatedPrescription) {
        ItemContainerContents containerContents = stack.get(DataComponents.CONTAINER);
        if (containerContents != null) {
            List<ItemStack> items = new ArrayList<>();
            boolean updated = false;

            for (int i = 0; i < containerContents.getSlots(); i++) {
                ItemStack prescriptionStack = containerContents.getStackInSlot(i);
                if (!prescriptionStack.isEmpty() && prescriptionStack.getItem() instanceof PrescriptionItem && !updated) {
                    items.add(updatedPrescription.copy());
                    updated = true;
                } else {
                    items.add(prescriptionStack.copy());
                }
            }

            ItemContainerContents newContents = ItemContainerContents.fromItems(items);
            stack.set(DataComponents.CONTAINER, newContents);
        }
    }

    @Override
    public int getEnchantmentValue(ItemStack stack) {
        return 10;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return true;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, components, tooltipFlag);
        ItemStack prescriptionStack = getFirstPrescriptionFromItemStack(stack);
        MobEffect effect = PrescriptionItem.getEffect(prescriptionStack);
        if(effect!= null) {
            components.add(Component.translatable("tooltip.jiagureappear.prescription", effect.getDisplayName()));
        }else{
            components.add(Component.translatable("tooltip.jiagureappear.prescription_no"));
        }
    }
}
