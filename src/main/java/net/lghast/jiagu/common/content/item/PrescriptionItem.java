package net.lghast.jiagu.common.content.item;

import net.lghast.jiagu.common.system.datacomponent.Prescription;
import net.lghast.jiagu.register.system.ModDataComponents;
import net.lghast.jiagu.utils.ModUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.*;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class PrescriptionItem extends Item {

    public PrescriptionItem(Properties properties) {
        super(properties.rarity(Rarity.RARE).stacksTo(1).durability(5)
                .component(ModDataComponents.PRESCRIPTION.value(), new Prescription()));
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
            components.add(Component.translatable("tooltip.jiagureappear.prescription", ModUtils.modifyName(effect), effect.getDisplayName())
                    .withStyle(ChatFormatting.GRAY));
        }else{
            components.add(Component.translatable("tooltip.jiagureappear.prescription_no")
                    .withStyle(ChatFormatting.DARK_GRAY));
        }
    }
}
