package net.lghast.jiagu.client.misc;

import com.mojang.blaze3d.vertex.PoseStack;
import net.lghast.jiagu.JiaguReappear;
import net.lghast.jiagu.character.CharacterInfo;
import net.lghast.jiagu.common.content.item.CharacterItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ParametersAreNonnullByDefault
public class CharacterItemModel implements BakedModel {
    private static final ModelState IDENTITY_MODEL_STATE = new ModelState() {
    };

    private final BakedModel original;
    private final Map<String, BakedModel> cache = new ConcurrentHashMap<>();

    private volatile TextureAtlasSprite sprite;
    private volatile List<BakedQuad> quads;

    public CharacterItemModel(BakedModel original) {
        this.original = original;
    }

    private CharacterItemModel(BakedModel original, String identifier) {
        this.original = original;
        this.sprite = getAtlasSprite(identifier);
        this.quads = createQuads(this.sprite);
    }

    private static TextureAtlas getBlockAtlas() {
        return (TextureAtlas) Minecraft.getInstance()
                .getTextureManager()
                .getTexture(InventoryMenu.BLOCK_ATLAS);
    }

    private static TextureAtlasSprite getAtlasSprite(String identifier) {
        ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(
                JiaguReappear.MOD_ID, "item/characters/" + identifier);
        return getBlockAtlas().getSprite(texture);
    }

    private TextureAtlasSprite getOrCreateSprite() {
        TextureAtlasSprite result = sprite;
        if (result == null) {
            synchronized (this) {
                result = sprite;
                if (result == null) {
                    String identifier = CharacterInfo.getIdentifier(CharacterItem.DEFAULT_INSCRIPTION);
                    if (!identifier.matches("[a-z0-9_.-]+")) {
                        identifier = "default";
                    }
                    result = getAtlasSprite(identifier);
                    sprite = result;
                }
            }
        }
        return result;
    }

    private List<BakedQuad> getOrCreateQuads() {
        List<BakedQuad> result = quads;
        if (result == null) {
            synchronized (this) {
                result = quads;
                if (result == null) {
                    result = createQuads(getOrCreateSprite());
                    quads = result;
                }
            }
        }
        return result;
    }

    @SuppressWarnings("deprecation")
    private static List<BakedQuad> createQuads(TextureAtlasSprite sprite) {
        BlockModel source = BlockModel.fromString(
                "{\"textures\":{\"layer0\":\"minecraft:missing\"}}");
        BlockModel generated = new ItemModelGenerator()
                .generateBlockModel(material -> sprite, source);
        List<BakedQuad> quads = new ArrayList<>();
        for (BlockElement element : generated.getElements()) {
            for (Map.Entry<Direction, BlockElementFace> entry : element.faces.entrySet()) {
                quads.add(BlockModel.bakeFace(element, entry.getValue(), sprite,
                        entry.getKey(), IDENTITY_MODEL_STATE));
            }
        }
        return quads;
    }

    @Override
    @Deprecated
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand) {
        return getOrCreateQuads();
    }

    @Override
    public @NotNull BakedModel applyTransform(ItemDisplayContext transformType, PoseStack poseStack, boolean applyLeftHandTransform) {
        if (original != null) {
            original.applyTransform(transformType, poseStack, applyLeftHandTransform);
        }
        return this;
    }

    @Override
    public boolean useAmbientOcclusion() {
        return original != null && original.useAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return original != null && original.isGui3d();
    }

    @Override
    public boolean usesBlockLight() {
        return original != null && original.usesBlockLight();
    }

    @Override
    public boolean isCustomRenderer() {
        return original != null && original.isCustomRenderer();
    }

    @Override
    @Deprecated
    public @NotNull TextureAtlasSprite getParticleIcon() {
        return getOrCreateSprite();
    }

    @Override
    public @NotNull ItemOverrides getOverrides() {
        return new ItemOverrides() {
            @Override
            public BakedModel resolve(BakedModel originalModel, ItemStack stack,
                                      @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed) {
                String identifier = CharacterInfo.getIdentifier(CharacterItem.getInscription(stack));
                if (!identifier.matches("[a-z0-9_.-]+")) {
                    return originalModel;
                }
                return cache.computeIfAbsent(identifier, id -> new CharacterItemModel(original, id) {
                    @Override
                    public @NotNull ItemOverrides getOverrides() {
                        return ItemOverrides.EMPTY;
                    }
                });
            }
        };
    }
}
