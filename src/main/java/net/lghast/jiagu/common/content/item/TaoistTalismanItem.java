package net.lghast.jiagu.common.content.item;

import net.lghast.jiagu.register.content.ModItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.CustomData;

import java.util.List;
import java.util.Objects;

public class TaoistTalismanItem extends Item {
    public static final String SPELL = "spell";

    public TaoistTalismanItem(Properties properties) {
        super(properties.rarity(Rarity.RARE).stacksTo(1).durability(3));
    }

    public static String getSpell(ItemStack stack){
        if(stack.is(ModItems.TAOIST_TALISMAN)) {
            CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
            if (customData != null) {
                CompoundTag tag = customData.copyTag();
                if (tag.contains(SPELL)) {
                    return tag.getString(SPELL);
                }
            }
        }
        return null;
    }

    public static void setSpell(ItemStack stack, String inscription){
        CompoundTag tag =new CompoundTag();
        tag.putString(SPELL, inscription);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag tooltipFlag) {
        String spell = getSpell(stack);
        if(Objects.equals(spell, null)) return;
        components.add(Component.translatable("tooltip.jiagureappear.taoist_talisman", spell));
        super.appendHoverText(stack,context,components,tooltipFlag);
    }
}

