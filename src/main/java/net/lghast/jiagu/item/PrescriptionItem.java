package net.lghast.jiagu.item;

import net.lghast.jiagu.data_component.Prescription;
import net.lghast.jiagu.register.ModDataComponents;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.*;

import java.util.List;

public class PrescriptionItem extends Item {

    public PrescriptionItem(Properties properties) {
        super(properties.rarity(Rarity.RARE).stacksTo(1).durability(5));
    }

    public static MobEffect getEffect(ItemStack stack){
        if(stack.getItem() instanceof PrescriptionItem){
            Prescription prescription = stack.get(ModDataComponents.PRESCRIPTION);
            if(prescription != null && prescription.effect().isPresent()) {
                return prescription.effect().get().value();
            }
        }
        return null;
    }

    public static Holder<MobEffect> getEffectHolder(ItemStack stack){
        if(stack.getItem() instanceof PrescriptionItem){
            Prescription prescription = stack.get(ModDataComponents.PRESCRIPTION);
            if(prescription != null && prescription.effect().isPresent()) {
                return prescription.effect().get();
            }
        }
        return null;
    }

    public static void setEffect(ItemStack stack, Holder<MobEffect> effectHolder){
        if(stack.getItem() instanceof PrescriptionItem){
            stack.set(ModDataComponents.PRESCRIPTION, new Prescription(effectHolder));
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, components, tooltipFlag);
        MobEffect effect = getEffect(stack);
        if(effect!= null) {
            components.add(Component.translatable("tooltip.jiagureappear.prescription", effect.getDisplayName()));
        }else{
            components.add(Component.translatable("tooltip.jiagureappear.prescription_no"));
        }
    }
}
