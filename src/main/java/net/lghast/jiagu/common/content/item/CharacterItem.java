package net.lghast.jiagu.common.content.item;

import net.lghast.jiagu.config.ClientConfig;
import net.lghast.jiagu.utils.CharacterInfo;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;

import java.util.List;

public class CharacterItem extends Item{

    public static final String INSCRIPTION = "inscription";

    public static final String DEFAULT_INSCRIPTION = "default";
    public static final String SIMPLE_INSCRIPTION = "simple";
    public static final String SIMPLE_INSCRIPTION_2 = "simple2";
    public static final String REVERSAL_INSCRIPTION = "reversal";
    public static final String COMPLEX_INSCRIPTION = "complex";

    public CharacterItem(Properties properties) {
        super(properties.stacksTo(64));
    }

    public static String getInscription(ItemStack stack){
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        if (customData != null) {
            CompoundTag tag = customData.copyTag();
            if (tag.contains(INSCRIPTION)) {
                return tag.getString(INSCRIPTION);
            }
        }
        return DEFAULT_INSCRIPTION;
    }

    public static void setInscription(ItemStack stack, String inscription){
        CompoundTag tag =new CompoundTag();
        tag.putString(INSCRIPTION, inscription);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    public static void setInscription(ItemStack stack, char inscription){
        CompoundTag tag =new CompoundTag();
        tag.putString(INSCRIPTION, String.valueOf(inscription));
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag tooltipFlag) {
        String inscription = getInscription(stack);
        boolean isNormalInscription = false;
        switch (inscription) {
            case DEFAULT_INSCRIPTION:
                components.add(Component.translatable("tooltip.jiagureappear.character_item_default", inscription));
                break;
            case SIMPLE_INSCRIPTION:
                components.add(Component.translatable("tooltip.jiagureappear.character_item_simple", inscription));
                break;
            case SIMPLE_INSCRIPTION_2:
                components.add(Component.translatable("tooltip.jiagureappear.character_item_simple2", inscription));
                break;
            case REVERSAL_INSCRIPTION:
                components.add(Component.translatable("tooltip.jiagureappear.character_item_reversal", inscription));
                break;
            case COMPLEX_INSCRIPTION:
                components.add(Component.translatable("tooltip.jiagureappear.character_item_complex", inscription));
                break;
            default:
                components.add(Component.translatable("tooltip.jiagureappear.character_item", inscription));
                isNormalInscription = true;
                break;
        }
        if(isNormalInscription) {
            if(ClientConfig.DISPLAY_DISASSEMBLY_INFO.get()) {
                String from = CharacterInfo.getCharacterDisassembly(inscription);
                if (from != null) {
                    components.add(Component.translatable("item.jiagureappear.character.from", from));
                }
            }

            if(ClientConfig.DISPLAY_ASSEMBLY_INFO.get()) {
                String to = CharacterInfo.getCharacterAssemblySimple(inscription);
                if (!to.isEmpty()) {
                    char[] toChars = to.toCharArray();
                    if (toChars.length <= 9) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (char c : toChars) {
                            stringBuilder.append(c).append(" ");
                        }
                        components.add(Component.translatable("item.jiagureappear.character.to", stringBuilder.toString().trim()));
                    } else {
                        components.add(Component.translatable("item.jiagureappear.character.to_long"));
                        int groupCount = (toChars.length + 8) / 9;

                        for (int i = 0; i < groupCount; i++) {
                            StringBuilder stringBuilder = new StringBuilder();
                            int start = i * 9;
                            int end = Math.min(start + 9, toChars.length);
                            for (int j = start; j < end; j++) {
                                stringBuilder.append(toChars[j]).append(" ");
                            }
                            components.add(Component.literal(stringBuilder.toString().trim()));
                        }
                    }
                }
            }
        }
        super.appendHoverText(stack,context,components,tooltipFlag);
    }
}
