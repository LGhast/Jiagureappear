package net.lghast.jiagu.client.screen;

import net.lghast.jiagu.common.content.item.CharacterItem;
import net.lghast.jiagu.common.content.item.PrescriptionItem;
import net.lghast.jiagu.utils.ModUtils;
import net.lghast.jiagu.utils.lzh.LzhMappings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@OnlyIn(Dist.CLIENT)
@ParametersAreNonnullByDefault
public class DictionaryListWidget extends AbstractWidget {
    private static final int ITEMS_PER_PAGE = 7;
    private static final int ITEM_HEIGHT = 15;
    private static final float ITEM_SCALE = 0.8f;

    private DisplayMode mode = DisplayMode.ASSEMBLY_RECIPE;
    private List<ItemStack> items = new ArrayList<>();
    private int currentPage = 0;
    private Runnable onPageChange;
    private Consumer<String> onItemClick;

    public DictionaryListWidget(int x, int y, int width, int height) {
        super(x, y, width, height, Component.empty());
    }

    public void setOnPageChange(Runnable onPageChange) {
        this.onPageChange = onPageChange;
    }

    public void setMode(DisplayMode mode){
        this.mode = mode;
    }

    public void setItems(List<ItemStack> newItems) {
        this.items = new ArrayList<>(newItems);
        this.currentPage = 0;
        if (onPageChange != null) {
            onPageChange.run();
        }
    }

    public void setOnItemClick(Consumer<String> onItemClick) {
        this.onItemClick = onItemClick;
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        if(mode == DisplayMode.ASSEMBLY_RECIPE) return;
        if(mode == DisplayMode.DEFAULT) return;

        Minecraft minecraft = Minecraft.getInstance();

        if (items.isEmpty()) {
            String text = Component.translatable("gui.jiagureappear.dictionary.no_items").getString();
            int textWidth = minecraft.font.width(text);
            graphics.drawString(minecraft.font, text,
                    getX() + (width - textWidth) / 2,
                    getY() + height / 2 - 7,
                    0xf8f6f0, false);
            return;
        }

        int startIndex = currentPage * ITEMS_PER_PAGE;
        int endIndex = Math.min(startIndex + ITEMS_PER_PAGE, items.size());
        int hoveredIndex = getHoveredItemIndex(mouseX, mouseY);

        for (int i = startIndex; i < endIndex; i++) {
            int displayIndex = i - startIndex;
            int itemY = getY() + displayIndex * ITEM_HEIGHT;

            if (displayIndex % 2 == 0) {
                graphics.fill(getX(), itemY, getX() + width - 1, itemY + ITEM_HEIGHT, 0x507a4c02);
            }else{
                graphics.fill(getX(), itemY, getX() + width - 1, itemY + ITEM_HEIGHT, 0x307a4c02);
            }

            if (i == hoveredIndex) {
                graphics.fill(getX(), itemY, getX() + width - 1, itemY + ITEM_HEIGHT, 0x44FFFFFF);
            }

            ItemStack stack = items.get(i);
            if (!stack.isEmpty()) {
                graphics.pose().pushPose();

                int itemX = getX() + 4;
                int itemRenderY = itemY - 2;

                graphics.pose().translate(itemX, itemRenderY, 0);
                graphics.pose().scale(ITEM_SCALE, ITEM_SCALE, 1.0f);

                graphics.renderItem(stack, 0, 4, i);
                graphics.renderItemDecorations(minecraft.font, stack, 0, 4);

                graphics.pose().popPose();

                String renderedText = getRenderedText(stack);
                String truncatedText = ModUtils.truncateToWidth(minecraft.font, renderedText, width - 20);
                graphics.drawString(minecraft.font, truncatedText, getX() + 21, itemY+4, 0xFFFFFF, false);
            }
        }
    }

    private String getRenderedText(ItemStack stack){
        String text = switch (mode){
            case ASSEMBLY_RECIPE, DEFAULT -> null;
            case ASSEMBLY_LIST -> CharacterItem.getConvertedInscription(stack);
            case RELATED_ITEMS -> LzhMappings.getLzhName(stack);
            case RELATED_ENCHANTMENTS -> stack.getDisplayName().getString().replace("[", "").replace("]", "");
            case RELATED_EFFECTS -> ModUtils.modifyName(PrescriptionItem.getEffect(stack));
            case RELATED_ENTITIES -> ModUtils.modifyName(stack).replace("孳", "").replace("之卵", "");
        };
        return text == null ? Component.translatable("gui.jiagureappear.dictionary.unknown").getString() : text;
    }

    private int getHoveredItemIndex(double mouseX, double mouseY) {
        if (!isMouseOver(mouseX, mouseY)) {
            return -1;
        }

        int relativeY = (int) (mouseY - getY());
        int displayIndex = relativeY / ITEM_HEIGHT;

        if (displayIndex >= 0 && displayIndex < ITEMS_PER_PAGE) {
            int itemIndex = currentPage * ITEMS_PER_PAGE + displayIndex;
            if (itemIndex >= 0 && itemIndex < items.size()) {
                return itemIndex;
            }
        }
        return -1;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(mode == DisplayMode.ASSEMBLY_RECIPE) return false;
        if(mode == DisplayMode.DEFAULT) return false;
        if (!isMouseOver(mouseX, mouseY)) return false;

        int index = getHoveredItemIndex(mouseX, mouseY);
        if (index >= 0) {
            ItemStack stack = items.get(index);

            if (mode == DisplayMode.ASSEMBLY_LIST) {
                String inscription = CharacterItem.getInscription(stack);
                if (inscription != null && !inscription.isEmpty() && onItemClick != null) {
                    onItemClick.accept(inscription);
                    return true;
                }
            }
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }


    public void nextPage() {
        int totalPages = getTotalPages();
        if (totalPages <= 1) return;
        currentPage++;
        if (currentPage >= totalPages) {
            currentPage = 0;
        }
        if (onPageChange != null) {
            onPageChange.run();
        }
    }

    public void previousPage() {
        int totalPages = getTotalPages();
        if (totalPages <= 1) return;

        currentPage--;
        if (currentPage < 0) {
            currentPage = 0;
        }
        if (onPageChange != null) {
            onPageChange.run();
        }
    }

    public boolean hasPreviousPage() {
        return currentPage > 0;
    }

    public boolean hasNextPage() {
        int totalPages = getTotalPages();
        return totalPages > 1;
    }

    private int getTotalPages() {
        return Math.max(1, (int) Math.ceil((double) items.size() / ITEMS_PER_PAGE));
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        narrationElementOutput.add(NarratedElementType.TITLE, Component.translatable("gui.jiagureappear.dictionary"));
    }
}
