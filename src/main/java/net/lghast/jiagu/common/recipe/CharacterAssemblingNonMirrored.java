package net.lghast.jiagu.common.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.lghast.jiagu.register.ModItems;
import net.lghast.jiagu.common.item.CharacterItem;
import net.lghast.jiagu.register.ModSerializers;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipePattern;

public class CharacterAssemblingNonMirrored extends NonMirroredRecipe {
    public final String result_inscription;

    public CharacterAssemblingNonMirrored(String group, CraftingBookCategory category, ShapedRecipePattern pattern, ItemStack result, String result_inscription, boolean showNotification) {
        super(group, category, pattern, result, showNotification);
        this.result_inscription = result_inscription;
        if(result.is(ModItems.CHARACTER_ITEM.get())){
            CharacterItem.setInscription(result, result_inscription);
        }
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModSerializers.CHARACTER_ASSEMBLING_NON_MIRRORED.get();
    }

    public static class Serializer implements RecipeSerializer<CharacterAssemblingNonMirrored> {
        public static final MapCodec<CharacterAssemblingNonMirrored> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        Codec.STRING.optionalFieldOf("group", "").forGetter(recipe -> recipe.getGroup()),
                        CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(recipe -> recipe.category),
                        ShapedRecipePattern.MAP_CODEC.forGetter(recipe -> recipe.pattern),
                        ItemStack.STRICT_CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
                        Codec.STRING.fieldOf("result_inscription").forGetter(recipe -> recipe.result_inscription),
                        Codec.BOOL.optionalFieldOf("show_notification", true).forGetter(recipe -> recipe.showNotification)
                ).apply(instance, CharacterAssemblingNonMirrored::new)
        );

        @Override
        public MapCodec<CharacterAssemblingNonMirrored> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, CharacterAssemblingNonMirrored> streamCodec() {
            return STREAM_CODEC;
        }

        public static final StreamCodec<RegistryFriendlyByteBuf, CharacterAssemblingNonMirrored> STREAM_CODEC =
                StreamCodec.of(Serializer::toNetwork, Serializer::fromNetwork);

        private static CharacterAssemblingNonMirrored fromNetwork(RegistryFriendlyByteBuf buf) {
            String group = buf.readUtf();
            CraftingBookCategory category = buf.readEnum(CraftingBookCategory.class);
            ShapedRecipePattern pattern = ShapedRecipePattern.STREAM_CODEC.decode(buf);
            ItemStack result = ItemStack.STREAM_CODEC.decode(buf);
            String inscription = buf.readUtf();
            boolean showNotification = buf.readBoolean();
            return new CharacterAssemblingNonMirrored(group, category, pattern, result, inscription, showNotification);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buf, CharacterAssemblingNonMirrored recipe) {
            buf.writeUtf(recipe.getGroup());
            buf.writeEnum(recipe.category);
            ShapedRecipePattern.STREAM_CODEC.encode(buf, recipe.pattern);
            ItemStack.STREAM_CODEC.encode(buf, recipe.result);
            buf.writeUtf(recipe.result_inscription);
            buf.writeBoolean(recipe.showNotification);
        }
    }
}
