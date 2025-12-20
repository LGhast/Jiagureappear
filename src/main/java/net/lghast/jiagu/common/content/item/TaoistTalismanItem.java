package net.lghast.jiagu.common.content.item;

import net.lghast.jiagu.common.system.datacomponent.Spell;
import net.lghast.jiagu.register.content.ModItems;
import net.lghast.jiagu.register.system.ModDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class TaoistTalismanItem extends Item {
    public TaoistTalismanItem(Properties properties) {
        super(properties.rarity(Rarity.RARE).stacksTo(1).durability(3)
                .component(ModDataComponents.SPELL.value(), new Spell()));
    }

    public static Spell getSpell(ItemStack stack){
        if(!stack.is(ModItems.TAOIST_TALISMAN)) return new Spell();

        Spell spell = stack.get(ModDataComponents.SPELL);
        if(spell == null) return new Spell();

        return spell;
    }

    public static void setSpell(ItemStack stack, Holder<Enchantment> enchantment, int level){
        if(!stack.is(ModItems.TAOIST_TALISMAN)) return;

        Spell spell = new Spell(enchantment, level);
        stack.set(ModDataComponents.SPELL, spell);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag tooltipFlag) {
        Spell spell = getSpell(stack);
        if(spell.isEmpty()) return;

        components.add(Component.translatable("tooltip.jiagureappear.taoist_talisman", spell.spellName())
                .withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack,context,components,tooltipFlag);
    }
}

