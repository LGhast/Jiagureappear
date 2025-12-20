package net.lghast.jiagu.client.screen;

import net.lghast.jiagu.common.content.item.CharacterItem;
import net.lghast.jiagu.common.system.menu.DictionaryMenu;
import net.lghast.jiagu.register.content.ModItems;
import net.lghast.jiagu.utils.lzh.CharacterInfo;
import net.lghast.jiagu.utils.lzh.CharacterStructure;
import net.lghast.jiagu.utils.lzh.LzhMappings;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.lwjgl.glfw.GLFW;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@OnlyIn(Dist.CLIENT)
@ParametersAreNonnullByDefault
public class DictionaryScreen extends AbstractContainerScreen<DictionaryMenu> {
    private static final ResourceLocation DEFAULT_TEXTURE =
            ResourceLocation.fromNamespaceAndPath("jiagureappear", "textures/gui/dictionary.png");
    private static final ResourceLocation CRAFT_TEXTURE =
            ResourceLocation.fromNamespaceAndPath("jiagureappear", "textures/gui/dictionary_craft.png");
    private static final ResourceLocation LIST_TEXTURE =
            ResourceLocation.fromNamespaceAndPath("jiagureappear", "textures/gui/dictionary_list.png");

    private static final int GUI_WIDTH = 292;
    private static final int GUI_HEIGHT = 180;

    private static final int EDITBOX_X = 46;
    private static final int EDITBOX_Y = 26;
    private static final int EDITBOX_WIDTH = 75;
    private static final int EDITBOX_HEIGHT = 9;

    private static final int ITEM_PREVIEW_X = 212;
    private static final int ITEM_PREVIEW_Y = 19;

    private static final int WIDGET_X = 174;
    private static final int WIDGET_Y = 56;
    private static final int WIDGET_WIDTH = 103;
    private static final int WIDGET_HEIGHT = 112;

    private static final int PREV_BUTTON_X = 162;
    private static final int PREV_BUTTON_Y = 55;
    private static final int NEXT_BUTTON_X = 162;
    private static final int NEXT_BUTTON_Y = 150;
    private static final int BUTTON_SIZE = 12;

    private static final int MODE_BUTTON_START_X = 32;
    private static final int MODE_BUTTON_START_Y = 52;
    private static final int MODE_BUTTON_SPACING = 18;
    private static final int TEXT_OFFSET_X = 16;

    private static final int RECIPE_TITLE_X = 179;
    private static final int RECIPE_TITLE_Y = 64;
    private static final int RECIPE_X = 180;
    private static final int RECIPE_Y = 76;
    private static final int RECIPE_SPACING_X = 32;
    private static final int RECIPE_SPACING_Y = 29;

    private EditBox searchBox;
    private DictionaryListWidget listWidget;
    private Button prevPageButton;
    private Button nextPageButton;
    private final List<ClickableItem> clickableItems = new ArrayList<>();

    private final List<Button> modeButtons = new ArrayList<>();
    private DisplayMode currentMode = DisplayMode.DEFAULT;

    private String currentSearchText = "";
    private ItemStack previewStack = ItemStack.EMPTY;

    private static class ClickableItem {
        private final Rect2i area;
        private final String character;

        public ClickableItem(int x, int y, String character) {
            this.area = new Rect2i(x, y, 16, 16);
            this.character = character;
        }

        public boolean contains(double mouseX, double mouseY) {
            return area.contains((int) mouseX, (int) mouseY);
        }
    }

    public DictionaryScreen(DictionaryMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = GUI_WIDTH;
        this.imageHeight = GUI_HEIGHT;
        this.titleLabelY = 10000;
    }

    @Override
    protected void init() {
        super.init();

        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;
        this.inventoryLabelY = 10000;

        this.searchBox = new EditBox(this.font,
                leftPos + EDITBOX_X, topPos + EDITBOX_Y,
                EDITBOX_WIDTH, EDITBOX_HEIGHT,
                Component.translatable("gui.jiagureappear.dictionary.search").withStyle(ChatFormatting.BLACK));
        this.searchBox.setMaxLength(10);
        this.searchBox.setBordered(false);
        this.searchBox.setValue(currentSearchText);
        this.searchBox.setResponder(this::onSearchTextChanged);
        this.addRenderableWidget(searchBox);

        createModeButtons();

        this.listWidget = new DictionaryListWidget(
                leftPos + WIDGET_X,
                topPos + WIDGET_Y,
                WIDGET_WIDTH,
                WIDGET_HEIGHT
        );
        this.listWidget.setOnItemClick(this::setSearchedCharacter);
        this.addRenderableWidget(listWidget);

        this.prevPageButton = Button.builder(Component.literal("←"), button -> {
            listWidget.previousPage();
            updateButtonStates();
        }).bounds(leftPos + PREV_BUTTON_X, topPos + PREV_BUTTON_Y, BUTTON_SIZE, BUTTON_SIZE).build();
        this.addRenderableWidget(prevPageButton);

        this.nextPageButton = Button.builder(Component.literal("→"), button -> {
            listWidget.nextPage();
            updateButtonStates();
        }).bounds(leftPos + NEXT_BUTTON_X, topPos + NEXT_BUTTON_Y, BUTTON_SIZE, BUTTON_SIZE).build();
        this.addRenderableWidget(nextPageButton);

        listWidget.setOnPageChange(this::updateButtonStates);
        updateButtonStates();
        for(Button button : modeButtons){
            button.active = false;
        }

        updatePreviewItem();
    }

    private void createModeButtons() {
        DisplayMode[] modes = DisplayMode.values();

        for (int i = 0; i < modes.length - 1; i++) {
            final DisplayMode mode = modes[i];
            final int buttonY = MODE_BUTTON_START_Y + i * MODE_BUTTON_SPACING;

            Button button = Button.builder(Component.empty(), btn -> {
                currentMode = mode;
                onDisplayModeChanged();
            }).bounds(
                    leftPos + MODE_BUTTON_START_X,
                    topPos + buttonY,
                    BUTTON_SIZE,
                    BUTTON_SIZE
            ).build();

            this.modeButtons.add(button);
            this.addRenderableWidget(button);
        }
    }

    private void setSearchedCharacter(String character) {
        searchBox.setValue(character);
        onSearchTextChanged(character);
    }

    private void onSearchTextChanged(String newText) {
        this.currentSearchText = newText;
        updatePreviewItem();
        updateItemList();

        if(currentSearchText.trim().isEmpty()){
            currentMode = DisplayMode.DEFAULT;
        }
        updateButtonStates();
    }

    private void onDisplayModeChanged() {
        updatePreviewItem();
        updateButtonStates();
        listWidget.setMode(currentMode);
        updateItemList();
    }

    private void updateItemList(){
        String modifiedText = CharacterInfo.counterpart(currentSearchText.trim());
        if(Objects.equals(modifiedText, "")){
            listWidget.setMode(DisplayMode.DEFAULT);
            return;
        }

        switch (currentMode){
            case ASSEMBLY_LIST:
                listWidget.setItems(CharacterInfo.getCharacterItemAssembly(modifiedText));
                break;
            case RELATED_ITEMS:
                listWidget.setItems(LzhMappings.getItemStacksWith(modifiedText));
                break;
            case RELATED_ENTITIES:
                listWidget.setItems(LzhMappings.getSpawnEggsWith(modifiedText));
                break;
            case RELATED_EFFECTS:
                listWidget.setItems(LzhMappings.getPrescriptionsWith(modifiedText));
                break;
            case RELATED_ENCHANTMENTS:
                listWidget.setItems(LzhMappings.getReceiverItemsWith(modifiedText));
                break;
            default:
                listWidget.setItems(new ArrayList<>());
                break;
        }
    }

    private void updatePreviewItem() {
        String modifiedText = CharacterInfo.counterpart(currentSearchText.trim());
        if (modifiedText.isEmpty()) {
            previewStack = ItemStack.EMPTY;
        } else {
            previewStack = new ItemStack(ModItems.CHARACTER_ITEM.get());
            CharacterItem.setInscription(previewStack, modifiedText);
        }
    }

    private void updateButtonStates() {
        boolean isListMode = currentMode != DisplayMode.ASSEMBLY_RECIPE && currentMode != DisplayMode.DEFAULT;

        prevPageButton.visible = isListMode;
        nextPageButton.visible = isListMode;

        if (isListMode) {
            prevPageButton.active = listWidget.hasPreviousPage();
            nextPageButton.active = listWidget.hasNextPage();
        }

        for(Button button : modeButtons){
            button.active = !currentSearchText.trim().isEmpty();
        }
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        ResourceLocation texture = switch (currentMode){
            case DEFAULT -> DEFAULT_TEXTURE;
            case ASSEMBLY_RECIPE -> CRAFT_TEXTURE;
            default -> LIST_TEXTURE;
        };
        graphics.blit(texture, leftPos, topPos, 0, 0, GUI_WIDTH, GUI_HEIGHT, GUI_WIDTH, GUI_HEIGHT);
    }

    private void renderRecipe(GuiGraphics graphics){
        clickableItems.clear();

        String modifiedText = CharacterInfo.counterpart(currentSearchText.trim());
        String key = CharacterInfo.canBeDisassembled(modifiedText) ? "gui.jiagureappear.dictionary.recipe" : "gui.jiagureappear.dictionary.no_recipe";
        Component text = Component.translatable(key);
        graphics.drawString(this.font, text,
                leftPos + RECIPE_TITLE_X,
                topPos + RECIPE_TITLE_Y,
                0xFF000000, false);

        CharacterStructure structure = CharacterInfo.getStructure(modifiedText);
        if(structure == null) return;

        List<String> list = structure.getComponentList(CharacterInfo.getComponents(modifiedText));
        if(list.size() != 9) return;

        for(int i = 0; i < 3; i++) {
            for (int j = i * 3; j < i * 3 + 3; j++) {
                ItemStack stack = new ItemStack(ModItems.CHARACTER_ITEM.asItem());
                if(list.get(j) == null) continue;

                CharacterItem.setInscription(stack, list.get(j));
                int x = RECIPE_X + (j - i * 3) * RECIPE_SPACING_X;
                int y = RECIPE_Y + i * RECIPE_SPACING_Y;

                graphics.renderItem(stack, leftPos + x, topPos + y);
                graphics.renderItemDecorations(this.font, stack, leftPos + x, topPos + y);

                String displayText = CharacterItem.getConvertedInscription(stack);
                if (displayText != null) {
                    String realText = displayText.length() < 3 ? displayText : "/";
                    int textWidth = this.font.width(realText);
                    graphics.drawString(this.font, realText, leftPos + x + 8 - textWidth / 2, topPos + y + 18, 0x000000, false);
                }

                ClickableItem clickableItem = new ClickableItem(
                        leftPos + x,
                        topPos + y,
                        list.get(j)
                );
                clickableItems.add(clickableItem);
            }
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics, mouseX, mouseY, partialTick);
        super.render(graphics, mouseX, mouseY, partialTick);
        this.renderTooltip(graphics, mouseX, mouseY);

        renderModeButtonTexts(graphics);

        if (currentMode == DisplayMode.ASSEMBLY_RECIPE && !currentSearchText.trim().isEmpty()) {
            renderRecipe(graphics);
        }

        if (!previewStack.isEmpty()) {
            graphics.renderItem(previewStack, leftPos + ITEM_PREVIEW_X, topPos + ITEM_PREVIEW_Y);
            graphics.renderItemDecorations(this.font, previewStack, leftPos + ITEM_PREVIEW_X, topPos + ITEM_PREVIEW_Y);

            String displayText = CharacterItem.getConvertedInscription(previewStack);
            if(displayText != null) {
                int textWidth = this.font.width(displayText);
                graphics.drawString(this.font, displayText,
                        leftPos + ITEM_PREVIEW_X + 8 - textWidth / 2,
                        topPos + ITEM_PREVIEW_Y + 20,
                        0x000000, false);
            }
        }

        if (this.searchBox.getValue().isEmpty() && !this.searchBox.isFocused()) {
            graphics.drawString(this.font,
                    Component.translatable("gui.jiagureappear.dictionary.search_hint").withStyle(ChatFormatting.ITALIC),
                    leftPos + EDITBOX_X, topPos + EDITBOX_Y,
                    0x737270, false);
        }
    }

    private void renderModeButtonTexts(GuiGraphics graphics) {
        DisplayMode[] modes = DisplayMode.values();

        for (int i = 0; i < modes.length; i++) {
            DisplayMode mode = modes[i];
            int textY = topPos + MODE_BUTTON_START_Y + i * MODE_BUTTON_SPACING;

            int textX = leftPos + MODE_BUTTON_START_X + TEXT_OFFSET_X;

            boolean isSelected = mode == currentMode;
            Component textComponent = Component.literal(mode.getDisplayName()).withColor(0x4e4e4d);
            if (isSelected) {
                textComponent = textComponent.copy().withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.BLACK);
            }

            graphics.drawString(this.font, textComponent, textX, textY + 2, 0xFFFFFF, false);
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE && this.minecraft != null && this.minecraft.player != null) {
            this.minecraft.player.closeContainer();
            return true;
        }

        return this.searchBox.keyPressed(keyCode, scanCode, modifiers) ||
                this.searchBox.canConsumeInput() ||
                super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            for (ClickableItem clickableItem : clickableItems) {
                if (clickableItem.contains(mouseX, mouseY)) {
                    setSearchedCharacter(clickableItem.character);
                    if(minecraft != null && minecraft.player != null) {
                        minecraft.player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 0.5F, 1.0F);
                    }
                    return true;
                }
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }
}