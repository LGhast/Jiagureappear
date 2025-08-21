package net.lghast.jiagu.common.ingredient;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.lghast.jiagu.register.ModIngredientTypes;
import net.lghast.jiagu.register.ModItems;
import net.lghast.jiagu.common.item.CharacterItem;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.neoforge.common.crafting.ICustomIngredient;
import net.neoforged.neoforge.common.crafting.IngredientType;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

public class CharacterIngredient implements ICustomIngredient {
    private final String INSCRIPTION;

    public static final MapCodec<CharacterIngredient> MAP_CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.STRING.fieldOf("inscription").forGetter(ci -> ci.INSCRIPTION)
            ).apply(instance, CharacterIngredient::new)
    );

    public CharacterIngredient(String inscription) {
        this.INSCRIPTION = inscription;
    }

    @Override
    public boolean test(ItemStack stack) {
        if(stack.isEmpty()) return false;
        if(!stack.is(ModItems.CHARACTER_ITEM.get())) return false;
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        if (customData != null) {
            CompoundTag tag = customData.copyTag();
            if (tag.contains(CharacterItem.INSCRIPTION)) {
                String itemInscription = tag.getString(CharacterItem.INSCRIPTION);
                return itemInscription.equals(INSCRIPTION);
            }
        }
        return false;
    }

    @Override
    public @NotNull Stream<ItemStack> getItems() {
        ItemStack stack = new ItemStack(ModItems.CHARACTER_ITEM.get());
        CompoundTag tag =new CompoundTag();
        tag.putString(CharacterItem.INSCRIPTION, INSCRIPTION);
        CharacterItem.setInscription(stack,INSCRIPTION);
        return Stream.of(stack);
    }

    @Override
    public boolean isSimple() {
        return false;
    }

    public static final Codec<CharacterIngredient> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.STRING.fieldOf("inscription").forGetter(ci -> ci.INSCRIPTION)
            ).apply(instance, CharacterIngredient::new)
    );

    @Override
    public IngredientType<?> getType() {
        return ModIngredientTypes.CHARACTER_INGREDIENT.get();
    }

    public String getInscription() {
        return this.INSCRIPTION;
    }


}
