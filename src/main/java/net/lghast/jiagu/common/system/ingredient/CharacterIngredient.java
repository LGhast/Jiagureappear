package net.lghast.jiagu.common.system.ingredient;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.lghast.jiagu.register.system.ModIngredientTypes;
import net.lghast.jiagu.register.content.ModItems;
import net.lghast.jiagu.common.content.item.CharacterItem;
import net.minecraft.world.item.ItemStack;
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
        return INSCRIPTION.equals(CharacterItem.getInscription(stack));
    }

    @Override
    public @NotNull Stream<ItemStack> getItems() {
        ItemStack stack = new ItemStack(ModItems.CHARACTER_ITEM.get());
        CharacterItem.setInscription(stack, INSCRIPTION);
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
    public @NotNull IngredientType<?> getType() {
        return ModIngredientTypes.CHARACTER_INGREDIENT.get();
    }

    public String getInscription() {
        return this.INSCRIPTION;
    }
}
