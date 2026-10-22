package net.lghast.jiagu.client.screen;

import net.lghast.jiagu.common.content.item.CharacterItem;
import net.lghast.jiagu.common.system.menu.DictionaryMenu;
import net.lghast.jiagu.register.content.ModItems;
import net.lghast.jiagu.character.CharacterInfo;
import net.lghast.jiagu.character.CharacterQuality;
import net.lghast.jiagu.character.CharacterStructure;
import net.lghast.jiagu.config.ClientConfig;
import net.lghast.jiagu.idiom.IdiomDescriptionLoader;
import net.lghast.jiagu.idiom.IdiomInfo;
import net.lghast.jiagu.idiom.IdiomRegistry;
import net.lghast.jiagu.lzh.LzhMappings;
import net.lghast.jiagu.register.system.ModAttachments;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
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

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@OnlyIn(Dist.CLIENT)
@ParametersAreNonnullByDefault
public class DictionaryScreen extends AbstractContainerScreen<DictionaryMenu> {
    private static final ResourceLocation LOOKUP_DEFAULT_TEXTURE =
            ResourceLocation.fromNamespaceAndPath("jiagureappear", "textures/gui/dictionary/dictionary_lookup_default.png");
    private static final ResourceLocation LOOKUP_CRAFT_TEXTURE =
            ResourceLocation.fromNamespaceAndPath("jiagureappear", "textures/gui/dictionary/dictionary_lookup_craft.png");
    private static final ResourceLocation LOOKUP_ENTRIES_TEXTURE =
            ResourceLocation.fromNamespaceAndPath("jiagureappear", "textures/gui/dictionary/dictionary_lookup_entries.png");
    private static final ResourceLocation LIST_TAB_TEXTURE =
            ResourceLocation.fromNamespaceAndPath("jiagureappear", "textures/gui/dictionary/dictionary_list.png");
    private static final ResourceLocation IDIOM_TAB_TEXTURE =
            ResourceLocation.fromNamespaceAndPath("jiagureappear", "textures/gui/dictionary/dictionary_chengyu_default.png");
    private static final ResourceLocation IDIOM_INTRO_TEXTURE =
            ResourceLocation.fromNamespaceAndPath("jiagureappear", "textures/gui/dictionary/dictionary_chengyu_intro.png");
    private static final ResourceLocation BOOKMARKS_TEXTURE =
            ResourceLocation.fromNamespaceAndPath("jiagureappear", "textures/gui/dictionary/bookmarks.png");

    private static final int GUI_WIDTH = 292;
    private static final int GUI_HEIGHT = 180;

    private static final int EDITBOX_X = 46;
    private static final int EDITBOX_Y = 26;
    private static final int EDITBOX_WIDTH = 75;
    private static final int EDITBOX_HEIGHT = 9;

    private static final int ITEM_PREVIEW_X = 212;
    private static final int ITEM_PREVIEW_Y = 19;

    private static final int WIDGET_X = 173;
    private static final int WIDGET_Y = 55;
    private static final int WIDGET_WIDTH = 103;
    private static final int WIDGET_HEIGHT = 112;

    private static final int PREV_BUTTON_X = 161;
    private static final int PREV_BUTTON_Y = 54;
    private static final int NEXT_BUTTON_X = 161;
    private static final int NEXT_BUTTON_Y = 149;
    private static final int BUTTON_SIZE = 12;

    private static final int SECTION_BUTTON_START_X = 32;
    private static final int SECTION_BUTTON_START_Y = 52;
    private static final int SECTION_BUTTON_SPACING = 18;
    private static final int TEXT_OFFSET_X = 16;

    private static final int RECIPE_TITLE_X = 179;
    private static final int RECIPE_TITLE_Y = 64;
    private static final int RECIPE_X = 180;
    private static final int RECIPE_Y = 76;
    private static final int RECIPE_SPACING_X = 32;
    private static final int RECIPE_SPACING_Y = 29;

    private static final int BOOKMARK_X = 2;
    private static final int BOOKMARK_FIRST_Y = 17;
    private static final int BOOKMARK_SPACING = 11;
    private static final int BOOKMARK_WIDTH = 7;
    private static final int BOOKMARK_HEIGHT = 8;
    private static final int BOOKMARKS_TEXTURE_HEIGHT = 24;

    private static final int LIST_MODE_X = 173;
    private static final int LIST_MODE_Y = 22;
    private static final int LIST_MODE_ITEM_WIDTH = 103;
    private static final int LIST_MODE_ITEM_HEIGHT = 15;
    private static final int LIST_MODE_VISIBLE_COUNT = 9;
    private static final float ITEM_SCALE = 0.8f;

    private static final int SCROLLBAR_X = 162;
    private static final int SCROLLBAR_Y = 21;
    private static final int SCROLLBAR_WIDTH = 10;
    private static final int SCROLLBAR_HEIGHT = 137;
    private static final int SCROLLBAR_THUMB_HEIGHT = 8;

    private static final int QUALITY_CHECKBOX_X = 32;
    private static final int QUALITY_CHECKBOX_Y = 52;
    private static final int QUALITY_CHECKBOX_SPACING = 22;

    private static final int LIST_TITLE_Y = 11;
    private static final int LIST_TITLE_CENTER_X = 219;

    private static final String[] QUALITY_KEYS = {
            "gui.jiagureappear.dictionary.quality.stone",
            "gui.jiagureappear.dictionary.quality.iron",
            "gui.jiagureappear.dictionary.quality.gold",
            "gui.jiagureappear.dictionary.quality.diamond",
            "gui.jiagureappear.dictionary.quality.copper"
    };

    private static final int IDIOM_PREV_X = 16;
    private static final int IDIOM_PREV_Y = 52;
    private static final int IDIOM_NEXT_X = 16;
    private static final int IDIOM_NEXT_Y = 147;
    private static final int IDIOM_LIST_X = 28;
    private static final int IDIOM_LIST_Y = 53;
    private static final int IDIOM_LIST_WIDTH = 103;
    private static final int IDIOMS_PER_PAGE = 7;
    private static final int IDIOM_ITEM_HEIGHT = 15;
    private static final int IDIOM_FILTER_SIZE = 8;
    private static final int IDIOM_FILTER_Y = 41;
    private static final int IDIOM_FILTER_LEFT_X = 17;
    private static final int IDIOM_FILTER_RIGHT_X = 29;
    private static final int IDIOM_DETAIL_X = 187;
    private static final int IDIOM_DETAIL_Y = 22;
    private static final int IDIOM_DETAIL_SPACING = 17;
    private static final int IDIOM_DESCRIPTION_X = 165;
    private static final int IDIOM_DESCRIPTION_Y = 53;
    private static final int IDIOM_DESCRIPTION_WIDTH = 111;
    private static final float IDIOM_DESCRIPTION_SCALE = 0.9f;
    private static final int IDIOM_DESCRIPTION_PAGE_BUTTON_X = 163;
    private static final int IDIOM_DESCRIPTION_PAGE_BUTTON_Y = 157;

    private EditBox searchBox;
    private DictionaryListWidget listWidget;
    private Button prevPageButton;
    private Button nextPageButton;
    private final List<ClickableItem> clickableItems = new ArrayList<>();

    private final List<Button> sectionButtons = new ArrayList<>();
    private final Map<LookupSection, Boolean> sectionHasContent = new EnumMap<>(LookupSection.class);
    private LookupSection currentSection = LookupSection.DEFAULT;

    private DictionaryTab currentTab = DictionaryTab.LOOKUP;

    private String currentSearchText = "";
    private ItemStack previewStack = ItemStack.EMPTY;

    private final List<String> listModeCharacters = new ArrayList<>();
    private int listModeScroll = 0;
    private final List<QualityCheckbox> qualityCheckboxes = new ArrayList<>();
    private final boolean[] qualityFilters = new boolean[]{true, true, true, true, true};
    private boolean draggingScrollbar = false;

    private final List<Map.Entry<String, IdiomInfo>> idiomEntries = new ArrayList<>();
    private int idiomPage = 0;
    private Button idiomPrevButton;
    private Button idiomNextButton;
    private final List<IdiomFilterCheckbox> idiomFilterCheckboxes = new ArrayList<>();
    private boolean filterCollectedOnly = false;
    private boolean filterUncollectedOnly = false;
    private String selectedIdiomId = null;
    private final List<ClickableItem> idiomCharacterClickables = new ArrayList<>();
    private int idiomDescriptionPage = 0;
    private Button idiomDescriptionPageButton;

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

        createSectionButtons();
        createBookmarkButtons();
        createQualityCheckboxes();
        createIdiomButtons();

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
        updateSectionContent();
        updateButtonStates();
        for(Button button : sectionButtons){
            button.active = false;
        }

        rebuildListMode();
        rebuildIdiomEntries();
        updatePreviewItem();
        updateTabVisibility();
    }

    private void createSectionButtons() {
        LookupSection[] sections = LookupSection.values();

        for (int i = 0; i < sections.length - 1; i++) {
            final LookupSection section = sections[i];
            final int buttonY = SECTION_BUTTON_START_Y + i * SECTION_BUTTON_SPACING;

            Button button = Button.builder(Component.empty(), btn -> {
                currentSection = section;
                onSectionChanged();
            }).bounds(
                    leftPos + SECTION_BUTTON_START_X,
                    topPos + buttonY,
                    BUTTON_SIZE,
                    BUTTON_SIZE
            ).build();

            this.sectionButtons.add(button);
            this.addRenderableWidget(button);
        }
    }

    private void createBookmarkButtons() {
        DictionaryTab[] tabs = DictionaryTab.values();
        for (int i = 0; i < tabs.length; i++) {
            DictionaryTab tab = tabs[i];
            int y = BOOKMARK_FIRST_Y + i * BOOKMARK_SPACING;
            BookmarkButton button = new BookmarkButton(leftPos + BOOKMARK_X, topPos + y, tab);
            this.addRenderableWidget(button);
        }
    }

    private void createQualityCheckboxes() {
        for (int i = 0; i < QUALITY_KEYS.length; i++) {
            int y = QUALITY_CHECKBOX_Y + i * QUALITY_CHECKBOX_SPACING;
            QualityCheckbox checkbox = new QualityCheckbox(leftPos + QUALITY_CHECKBOX_X, topPos + y, i);
            this.qualityCheckboxes.add(checkbox);
            this.addRenderableWidget(checkbox);
        }
    }

    private void createIdiomButtons() {
        this.idiomPrevButton = Button.builder(Component.literal("←"), button -> {
            idiomPrevPage();
            updateIdiomButtonStates();
        }).bounds(leftPos + IDIOM_PREV_X, topPos + IDIOM_PREV_Y, BUTTON_SIZE, BUTTON_SIZE).build();
        this.addRenderableWidget(idiomPrevButton);

        this.idiomNextButton = Button.builder(Component.literal("→"), button -> {
            idiomNextPage();
            updateIdiomButtonStates();
        }).bounds(leftPos + IDIOM_NEXT_X, topPos + IDIOM_NEXT_Y, BUTTON_SIZE, BUTTON_SIZE).build();
        this.addRenderableWidget(idiomNextButton);

        this.idiomFilterCheckboxes.add(new IdiomFilterCheckbox(
                leftPos + IDIOM_FILTER_LEFT_X, topPos + IDIOM_FILTER_Y, true));
        this.idiomFilterCheckboxes.add(new IdiomFilterCheckbox(
                leftPos + IDIOM_FILTER_RIGHT_X, topPos + IDIOM_FILTER_Y, false));
        for (IdiomFilterCheckbox checkbox : idiomFilterCheckboxes) {
            this.addRenderableWidget(checkbox);
        }

        this.idiomDescriptionPageButton = Button.builder(Component.literal("→"), button -> {
            idiomDescriptionPage = 1 - idiomDescriptionPage;
        }).bounds(leftPos + IDIOM_DESCRIPTION_PAGE_BUTTON_X, topPos + IDIOM_DESCRIPTION_PAGE_BUTTON_Y,
                BUTTON_SIZE, BUTTON_SIZE).build();
        this.addRenderableWidget(idiomDescriptionPageButton);
    }

    private void rebuildIdiomEntries() {
        idiomEntries.clear();
        Set<String> collected = getCollectedIdioms();
        String search = CharacterInfo.counterpart(currentSearchText.trim());
        List<Map.Entry<String, IdiomInfo>> candidates = new ArrayList<>();
        for (Map.Entry<String, IdiomInfo> entry : IdiomRegistry.sortedById()) {
            boolean isCollected = collected.contains(entry.getKey());
            if (filterCollectedOnly && !isCollected) continue;
            if (filterUncollectedOnly && isCollected) continue;
            if (!search.isEmpty() && !entry.getValue().characters().contains(search)) continue;
            candidates.add(entry);
        }
        candidates.sort((a, b) -> {
            boolean aCollected = collected.contains(a.getKey());
            boolean bCollected = collected.contains(b.getKey());
            if (aCollected != bCollected) return aCollected ? -1 : 1;
            return a.getKey().compareTo(b.getKey());
        });
        idiomEntries.addAll(candidates);
        idiomPage = 0;
        updateIdiomButtonStates();
    }

    private Set<String> getCollectedIdioms() {
        if (minecraft != null && minecraft.player != null) {
            return minecraft.player.getData(ModAttachments.COLLECTED_IDIOMS.get());
        }
        return Set.of();
    }

    private void idiomPrevPage() {
        if (idiomPage > 0) {
            idiomPage--;
            updateIdiomButtonStates();
        }
    }

    private void idiomNextPage() {
        int total = getIdiomTotalPages();
        if (total > 1) {
            idiomPage = (idiomPage + 1) % total;
            updateIdiomButtonStates();
        }
    }

    private int getIdiomTotalPages() {
        return Math.max(1, (int) Math.ceil((double) idiomEntries.size() / IDIOMS_PER_PAGE));
    }

    private void updateIdiomButtonStates() {
        idiomPrevButton.active = idiomPage > 0;
        idiomNextButton.active = getIdiomTotalPages() > 1;
    }

    private void renderIdiomMode(GuiGraphics graphics, int mouseX, int mouseY) {
        if (idiomEntries.isEmpty()) {
            String noItems = Component.translatable("gui.jiagureappear.dictionary.no_items").getString();
            int noItemsWidth = this.font.width(noItems);
            graphics.drawString(this.font, noItems,
                    leftPos + IDIOM_LIST_X + (IDIOM_LIST_WIDTH - noItemsWidth) / 2,
                    topPos + IDIOM_LIST_Y + 30,
                    0xf8f6f0, false);
            return;
        }

        Set<String> collected = getCollectedIdioms();
        int start = idiomPage * IDIOMS_PER_PAGE;
        int end = Math.min(start + IDIOMS_PER_PAGE, idiomEntries.size());
        int hoveredIndex = getIdiomHoveredIndex(mouseX, mouseY);

        for (int i = start; i < end; i++) {
            int displayIndex = i - start;
            int itemY = topPos + IDIOM_LIST_Y + displayIndex * IDIOM_ITEM_HEIGHT;
            Map.Entry<String, IdiomInfo> entry = idiomEntries.get(i);
            IdiomInfo info = entry.getValue();
            int itemX = leftPos + IDIOM_LIST_X;

            if (displayIndex % 2 == 0) {
                graphics.fill(itemX, itemY, itemX + IDIOM_LIST_WIDTH - 1, itemY + IDIOM_ITEM_HEIGHT, 0x507a4c02);
            } else {
                graphics.fill(itemX, itemY, itemX + IDIOM_LIST_WIDTH - 1, itemY + IDIOM_ITEM_HEIGHT, 0x307a4c02);
            }

            if (entry.getKey().equals(selectedIdiomId) || i == hoveredIndex) {
                graphics.fill(itemX, itemY, itemX + IDIOM_LIST_WIDTH - 1, itemY + IDIOM_ITEM_HEIGHT, 0x44FFFFFF);
            }

            ItemStack icon = IdiomRegistry.getIconStack(info.icon());
            if (!icon.isEmpty()) {
                graphics.pose().pushPose();
                graphics.pose().translate(itemX + 4, itemY - 2, 0);
                graphics.pose().scale(ITEM_SCALE, ITEM_SCALE, 1.0f);
                graphics.renderItem(icon, 0, 4, i);
                graphics.renderItemDecorations(this.font, icon, 0, 4);
                graphics.pose().popPose();
            }

            int color = collected.contains(entry.getKey()) ? 0xFFFFFF : 0xFFd0d3d9;
            graphics.drawString(this.font, info.characters(), itemX + 21, itemY + 4, color, false);
        }
    }

    private void renderIdiomDetail(GuiGraphics graphics, int mouseX, int mouseY) {
        idiomCharacterClickables.clear();
        if (selectedIdiomId == null) return;
        IdiomInfo info = IdiomRegistry.getById(selectedIdiomId);
        if (info == null) return;

        String characters = info.characters();
        for (int i = 0; i < characters.length() && i < 4; i++) {
            String ch = String.valueOf(characters.charAt(i));
            int x = leftPos + IDIOM_DETAIL_X + i * IDIOM_DETAIL_SPACING;
            int y = topPos + IDIOM_DETAIL_Y;
            ItemStack stack = makeCharacterStack(ch);
            graphics.renderItem(stack, x, y);
            graphics.renderItemDecorations(this.font, stack, x, y);
            idiomCharacterClickables.add(new ClickableItem(x, y, ch));
            if (isMouseOverItem(mouseX, mouseY, x, y)) {
                renderInscriptionHover(graphics, stack, mouseX, mouseY);
            }
        }
    }

    private void renderIdiomDescription(GuiGraphics graphics) {
        if (selectedIdiomId == null) return;
        boolean collected = getCollectedIdioms().contains(selectedIdiomId);

        String description;
        if (!collected && ClientConfig.IDIOM_DESCRIPTION_REQUIRES_COLLECTION.get()) {
            description = Component.translatable("gui.jiagureappear.dictionary.idiom_locked").getString();
        } else {
            String lang = Minecraft.getInstance().getLanguageManager().getSelected();
            description = IdiomDescriptionLoader.load(selectedIdiomId, lang);
        }
        if (description == null || description.isEmpty()) {
            updateDescriptionPageButton(false);
            return;
        }

        description = description.replace("\r\n", "\n").replace('\r', '\n');

        String[] pages = description.split("\n===\n", -1);
        String page1 = pages[0];
        String page2 = pages.length > 1 ? pages[1] : null;
        boolean hasNextPage = page2 != null;

        String currentPageText = idiomDescriptionPage == 0 ? page1 : (page2 != null ? page2 : page1);

        float scale = IDIOM_DESCRIPTION_SCALE;
        List<Component> lines;
        while (true) {
            int maxWidth = (int) (IDIOM_DESCRIPTION_WIDTH / scale);
            lines = buildDescriptionLines(currentPageText, maxWidth);
            int lineStep = (int) (this.font.lineHeight * scale) + 1;
            int totalHeight = lines.size() * lineStep;
            int maxHeight = IDIOM_DESCRIPTION_PAGE_BUTTON_Y - IDIOM_DESCRIPTION_Y;
            if (totalHeight <= maxHeight || scale <= 0.5f) {
                break;
            }
            scale -= 0.05f;
        }

        graphics.pose().pushPose();
        graphics.pose().translate(leftPos + IDIOM_DESCRIPTION_X, topPos + IDIOM_DESCRIPTION_Y, 0);
        graphics.pose().scale(scale, scale, 1.0f);
        int y = 0;
        for (Component line : lines) {
            graphics.drawString(this.font, line, 0, y, 0xFF000000, false);
            y += this.font.lineHeight + 1;
        }
        graphics.pose().popPose();

        updateDescriptionPageButton(hasNextPage);
    }

    private List<Component> buildDescriptionLines(String text, int maxWidth) {
        List<Component> lines = new ArrayList<>();
        String[] paragraphs = text.split("\n", -1);
        for (String paragraph : paragraphs) {
            if (paragraph.isEmpty()) {
                lines.add(Component.literal(""));
            } else {
                lines.addAll(wrapDescription(paragraph, maxWidth));
            }
        }
        return lines;
    }

    private List<Component> wrapDescription(String text, int maxWidth) {
        List<Component> lines = new ArrayList<>();
        StringBuilder line = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (!line.isEmpty() && this.font.width(line.toString() + c) > maxWidth) {
                if (isPunctuation(c)) {
                    line.append(c);
                } else {
                    lines.add(Component.literal(line.toString()));
                    line = new StringBuilder();
                    line.append(c);
                }
            } else {
                line.append(c);
            }
        }
        if (!line.isEmpty()) {
            lines.add(Component.literal(line.toString()));
        }
        return lines;
    }

    private static boolean isPunctuation(char c) {
        return "，。！？；：、”’）》】〉…,.!?;:)]}".indexOf(c) >= 0;
    }

    private void updateDescriptionPageButton(boolean hasNextPage) {
        if (idiomDescriptionPageButton == null) return;
        idiomDescriptionPageButton.visible = hasNextPage && currentTab == DictionaryTab.IDIOM;
        idiomDescriptionPageButton.setMessage(Component.literal(idiomDescriptionPage == 0 ? "→" : "←"));
    }

    private int getIdiomHoveredIndex(double mouseX, double mouseY) {
        int x = leftPos + IDIOM_LIST_X;
        int y = topPos + IDIOM_LIST_Y;
        if (mouseX < x || mouseX >= x + IDIOM_LIST_WIDTH) return -1;
        if (mouseY < y || mouseY >= y + IDIOMS_PER_PAGE * IDIOM_ITEM_HEIGHT) return -1;
        int displayIndex = (int) (mouseY - y) / IDIOM_ITEM_HEIGHT;
        int index = idiomPage * IDIOMS_PER_PAGE + displayIndex;
        if (index >= 0 && index < idiomEntries.size()) return index;
        return -1;
    }

    private void switchTab(DictionaryTab tab) {
        this.currentTab = tab;
        this.searchBox.setValue("");
        if (tab == DictionaryTab.LIST) {
            rebuildListMode();
        } else if (tab == DictionaryTab.IDIOM) {
            selectedIdiomId = null;
            idiomDescriptionPage = 0;
            rebuildIdiomEntries();
        }
        updateTabVisibility();
    }

    private void updateTabVisibility() {
        boolean lookup = currentTab == DictionaryTab.LOOKUP;
        boolean idiom = currentTab == DictionaryTab.IDIOM;
        this.searchBox.visible = true;
        this.listWidget.visible = lookup;
        for (Button button : sectionButtons) {
            button.visible = lookup;
        }
        for (QualityCheckbox checkbox : qualityCheckboxes) {
            checkbox.visible = currentTab == DictionaryTab.LIST;
        }
        this.idiomPrevButton.visible = idiom;
        this.idiomNextButton.visible = idiom;
        for (IdiomFilterCheckbox checkbox : idiomFilterCheckboxes) {
            checkbox.visible = idiom;
        }
        if (idiomDescriptionPageButton != null) {
            idiomDescriptionPageButton.visible = false;
        }
        updateButtonStates();
    }

    private void setSearchedCharacter(String character) {
        searchBox.setValue(character);
        onSearchTextChanged(character);
    }

    private void onSearchTextChanged(String newText) {
        this.currentSearchText = newText;
        updatePreviewItem();
        updateItemList();
        updateSectionContent();

        if(currentSearchText.trim().isEmpty()){
            currentSection = LookupSection.DEFAULT;
        }
        updateButtonStates();

        if (currentTab == DictionaryTab.LIST) {
            rebuildListMode();
        } else if (currentTab == DictionaryTab.IDIOM) {
            rebuildIdiomEntries();
        }
    }

    private void onSectionChanged() {
        updatePreviewItem();
        updateButtonStates();
        listWidget.setSection(currentSection);
        updateItemList();
    }

    private void updateItemList(){
        String modifiedText = CharacterInfo.counterpart(currentSearchText.trim());
        if(Objects.equals(modifiedText, "")){
            listWidget.setSection(LookupSection.DEFAULT);
            return;
        }

        switch (currentSection){
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

    private void updateSectionContent() {
        String modifiedText = CharacterInfo.counterpart(currentSearchText.trim());
        for (LookupSection section : LookupSection.values()) {
            sectionHasContent.put(section, hasContentFor(section, modifiedText));
        }
    }

    private boolean hasContentFor(LookupSection section, @Nullable String character) {
        if (character == null || character.isEmpty()) {
            return false;
        }
        return switch (section) {
            case ASSEMBLY_RECIPE -> CharacterInfo.canBeDisassembled(character);
            case ASSEMBLY_LIST -> !CharacterInfo.getCharacterItemAssembly(character).isEmpty();
            case RELATED_ITEMS -> !LzhMappings.getItemStacksWith(character).isEmpty();
            case RELATED_ENCHANTMENTS -> !LzhMappings.getReceiverItemsWith(character).isEmpty();
            case RELATED_EFFECTS -> !LzhMappings.getPrescriptionsWith(character).isEmpty();
            case RELATED_ENTITIES -> !LzhMappings.getSpawnEggsWith(character).isEmpty();
            case DEFAULT -> false;
        };
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
        boolean isSectionList = currentTab == DictionaryTab.LOOKUP
                && currentSection != LookupSection.ASSEMBLY_RECIPE
                && currentSection != LookupSection.DEFAULT;

        prevPageButton.visible = isSectionList;
        nextPageButton.visible = isSectionList;

        if (isSectionList) {
            prevPageButton.active = listWidget.hasPreviousPage();
            nextPageButton.active = listWidget.hasNextPage();
        }

        for(Button button : sectionButtons){
            button.active = !currentSearchText.trim().isEmpty();
        }
    }

    private void rebuildListMode() {
        listModeCharacters.clear();
        String search = CharacterInfo.counterpart(currentSearchText.trim());

        for (Map.Entry<String, CharacterInfo.CharacterData> entry : CharacterInfo.CHARACTER_DATA.entrySet()) {
            String inscription = entry.getKey();
            int value = entry.getValue().intValue();
            if (value >= 5000 && value < 6000) continue;

            CharacterQuality quality = CharacterInfo.getQuality(inscription);
            if (quality == CharacterQuality.RUST) continue;

            int qIndex = quality.ordinal();
            if (qIndex >= qualityFilters.length || !qualityFilters[qIndex]) continue;

            if (!search.isEmpty()) {
                if (!inscription.equals(search) && !isMadeFrom(inscription, search)) {
                    continue;
                }
            }

            listModeCharacters.add(inscription);
        }

        listModeCharacters.sort(Comparator.comparingInt(CharacterInfo::getIntValue));
        listModeScroll = 0;
    }

    private boolean isMadeFrom(String inscription, String component) {
        List<String> components = CharacterInfo.getComponents(inscription);
        return components != null && components.contains(component);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        ResourceLocation texture = switch (currentTab) {
            case LOOKUP -> switch (currentSection) {
                case DEFAULT -> LOOKUP_DEFAULT_TEXTURE;
                case ASSEMBLY_RECIPE -> LOOKUP_CRAFT_TEXTURE;
                default -> LOOKUP_ENTRIES_TEXTURE;
            };
            case LIST -> LIST_TAB_TEXTURE;
            case IDIOM -> selectedIdiomId == null ? IDIOM_TAB_TEXTURE : IDIOM_INTRO_TEXTURE;
        };
        graphics.blit(texture, leftPos, topPos, 0, 0, GUI_WIDTH, GUI_HEIGHT, GUI_WIDTH, GUI_HEIGHT);
    }

    private void renderRecipe(GuiGraphics graphics, int mouseX, int mouseY){
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

                if (isMouseOverItem(mouseX, mouseY, leftPos + x, topPos + y)) {
                    renderInscriptionHover(graphics, stack, mouseX, mouseY);
                }
            }
        }
    }

    private void renderListMode(GuiGraphics graphics, int mouseX, int mouseY) {
        Component title = Component.translatable("gui.jiagureappear.dictionary.list_title");
        float titleScale = 0.8f;
        int titleWidth = (int) (this.font.width(title) * titleScale);
        graphics.pose().pushPose();
        graphics.pose().translate(leftPos + LIST_TITLE_CENTER_X - titleWidth / 2.0f, topPos + LIST_TITLE_Y, 0);
        graphics.pose().scale(titleScale, titleScale, 1.0f);
        graphics.drawString(this.font, title, 0, 0, 0x000000, false);
        graphics.pose().popPose();

        if (listModeCharacters.isEmpty()) {
            String noItems = Component.translatable("gui.jiagureappear.dictionary.no_items").getString();
            int noItemsWidth = this.font.width(noItems);
            graphics.drawString(this.font, noItems,
                    leftPos + LIST_MODE_X + (LIST_MODE_ITEM_WIDTH - noItemsWidth) / 2,
                    topPos + LIST_MODE_Y + 30,
                    0xf8f6f0, false);
            return;
        }

        int start = listModeScroll;
        int end = Math.min(start + LIST_MODE_VISIBLE_COUNT, listModeCharacters.size());
        int hoveredIndex = getListModeHoveredIndex(mouseX, mouseY);

        for (int i = start; i < end; i++) {
            int displayIndex = i - start;
            int itemY = topPos + LIST_MODE_Y + displayIndex * LIST_MODE_ITEM_HEIGHT;
            String inscription = listModeCharacters.get(i);

            int itemX = leftPos + LIST_MODE_X;

            if (displayIndex % 2 == 0) {
                graphics.fill(itemX, itemY, itemX + LIST_MODE_ITEM_WIDTH - 1, itemY + LIST_MODE_ITEM_HEIGHT, 0x507a4c02);
            } else {
                graphics.fill(itemX, itemY, itemX + LIST_MODE_ITEM_WIDTH - 1, itemY + LIST_MODE_ITEM_HEIGHT, 0x307a4c02);
            }

            if (i == hoveredIndex) {
                graphics.fill(itemX, itemY, itemX + LIST_MODE_ITEM_WIDTH - 1, itemY + LIST_MODE_ITEM_HEIGHT, 0x44FFFFFF);
            }

            ItemStack stack = makeCharacterStack(inscription);
            graphics.pose().pushPose();
            graphics.pose().translate(itemX + 4, itemY - 2, 0);
            graphics.pose().scale(ITEM_SCALE, ITEM_SCALE, 1.0f);
            graphics.renderItem(stack, 0, 4, i);
            graphics.renderItemDecorations(this.font, stack, 0, 4);
            graphics.pose().popPose();

            String displayText = CharacterItem.getConvertedInscription(stack);
            if (displayText != null) {
                graphics.drawString(this.font, displayText, itemX + 21, itemY + 4, 0xFFFFFF, false);
            }
        }

        int maxScroll = Math.max(0, listModeCharacters.size() - LIST_MODE_VISIBLE_COUNT);
        if (maxScroll > 0) {
            int thumbY = topPos + SCROLLBAR_Y
                    + (int) ((double) listModeScroll / maxScroll * (SCROLLBAR_HEIGHT - SCROLLBAR_THUMB_HEIGHT));
            graphics.fill(leftPos + SCROLLBAR_X, thumbY,
                    leftPos + SCROLLBAR_X + SCROLLBAR_WIDTH, thumbY + SCROLLBAR_THUMB_HEIGHT, 0xFF90764a);
        }
    }

    private ItemStack makeCharacterStack(String inscription) {
        ItemStack stack = new ItemStack(ModItems.CHARACTER_ITEM.get());
        CharacterItem.setInscription(stack, inscription);
        return stack;
    }

    private int getListModeHoveredIndex(double mouseX, double mouseY) {
        int x = leftPos + LIST_MODE_X;
        int y = topPos + LIST_MODE_Y;
        if (mouseX < x || mouseX >= x + LIST_MODE_ITEM_WIDTH) return -1;
        if (mouseY < y || mouseY >= y + LIST_MODE_VISIBLE_COUNT * LIST_MODE_ITEM_HEIGHT) return -1;

        int displayIndex = (int) (mouseY - y) / LIST_MODE_ITEM_HEIGHT;
        int index = listModeScroll + displayIndex;
        if (index >= 0 && index < listModeCharacters.size()) {
            return index;
        }
        return -1;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics, mouseX, mouseY, partialTick);
        super.render(graphics, mouseX, mouseY, partialTick);
        this.renderTooltip(graphics, mouseX, mouseY);

        if (currentTab == DictionaryTab.LOOKUP) {
            renderSectionButtonTexts(graphics);

            if (currentSection == LookupSection.ASSEMBLY_RECIPE && !currentSearchText.trim().isEmpty()) {
                renderRecipe(graphics, mouseX, mouseY);
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

                if (isMouseOverItem(mouseX, mouseY, leftPos + ITEM_PREVIEW_X, topPos + ITEM_PREVIEW_Y)) {
                    renderInscriptionHover(graphics, previewStack, mouseX, mouseY);
                }
            }

        } else if (currentTab == DictionaryTab.LIST) {
            renderListMode(graphics, mouseX, mouseY);
            renderQualityCheckboxTexts(graphics);
        } else if (currentTab == DictionaryTab.IDIOM) {
            renderIdiomMode(graphics, mouseX, mouseY);
            renderIdiomDetail(graphics, mouseX, mouseY);
            renderIdiomDescription(graphics);
        }

        if (this.searchBox.getValue().isEmpty() && !this.searchBox.isFocused()) {
            graphics.drawString(this.font,
                    Component.translatable("gui.jiagureappear.dictionary.search_hint").withStyle(ChatFormatting.ITALIC),
                    leftPos + EDITBOX_X, topPos + EDITBOX_Y,
                    0x737270, false);
        }
    }

    private void renderSectionButtonTexts(GuiGraphics graphics) {
        LookupSection[] sections = LookupSection.values();

        for (int i = 0; i < sections.length; i++) {
            LookupSection section = sections[i];
            int textY = topPos + SECTION_BUTTON_START_Y + i * SECTION_BUTTON_SPACING;

            int textX = leftPos + SECTION_BUTTON_START_X + TEXT_OFFSET_X;

            boolean isSelected = section == currentSection;
            boolean hasContent = sectionHasContent.getOrDefault(section, false);

            Component textComponent = Component.literal(section.getDisplayName());
            if (isSelected) {
                textComponent = textComponent.copy().withStyle(ChatFormatting.ITALIC);
            }
            textComponent = textComponent.copy().withStyle(hasContent ? ChatFormatting.BLACK : ChatFormatting.GRAY);

            graphics.drawString(this.font, textComponent, textX, textY + 2, 0xFFFFFF, false);
        }
    }

    private void renderQualityCheckboxTexts(GuiGraphics graphics) {
        for (int i = 0; i < QUALITY_KEYS.length; i++) {
            int textY = topPos + QUALITY_CHECKBOX_Y + i * QUALITY_CHECKBOX_SPACING;
            int textX = leftPos + QUALITY_CHECKBOX_X + TEXT_OFFSET_X;
            graphics.drawString(this.font, Component.translatable(QUALITY_KEYS[i]).withStyle(ChatFormatting.BLACK), textX, textY + 2, 0xFFFFFF, false);
        }
    }

    private boolean isMouseOverItem(double mouseX, double mouseY, int x, int y) {
        return mouseX >= x && mouseX < x + 16 && mouseY >= y && mouseY < y + 16;
    }

    private void renderInscriptionHover(GuiGraphics graphics, ItemStack stack, int mouseX, int mouseY) {
        String text = CharacterItem.getConvertedInscription(stack);
        if (text != null && !text.isEmpty()) {
            graphics.renderTooltip(this.font, Component.literal(text), mouseX, mouseY);
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

            if (currentTab == DictionaryTab.LIST) {
                if (isMouseOverScrollbar(mouseX, mouseY)) {
                    draggingScrollbar = true;
                    updateScrollFromMouseY(mouseY);
                    return true;
                }
                int index = getListModeHoveredIndex(mouseX, mouseY);
                if (index >= 0) {
                    String inscription = listModeCharacters.get(index);
                    currentTab = DictionaryTab.LOOKUP;
                    setSearchedCharacter(inscription);
                    updateTabVisibility();
                    return true;
                }
            }
            if (currentTab == DictionaryTab.IDIOM) {
                for (ClickableItem clickableItem : idiomCharacterClickables) {
                    if (clickableItem.contains(mouseX, mouseY)) {
                        currentTab = DictionaryTab.LOOKUP;
                        setSearchedCharacter(clickableItem.character);
                        updateTabVisibility();
                        return true;
                    }
                }
                int index = getIdiomHoveredIndex(mouseX, mouseY);
                if (index >= 0) {
                    selectedIdiomId = idiomEntries.get(index).getKey();
                    idiomDescriptionPage = 0;
                    return true;
                }
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (currentTab == DictionaryTab.LIST) {
            int maxScroll = Math.max(0, listModeCharacters.size() - LIST_MODE_VISIBLE_COUNT);
            if (scrollY > 0) {
                listModeScroll = Math.max(0, listModeScroll - 1);
            } else if (scrollY < 0) {
                listModeScroll = Math.min(maxScroll, listModeScroll + 1);
            }
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    private boolean isMouseOverScrollbar(double mouseX, double mouseY) {
        int x = leftPos + SCROLLBAR_X;
        int y = topPos + SCROLLBAR_Y;
        return mouseX >= x && mouseX < x + SCROLLBAR_WIDTH
                && mouseY >= y && mouseY < y + SCROLLBAR_HEIGHT;
    }

    private void updateScrollFromMouseY(double mouseY) {
        int maxScroll = Math.max(0, listModeCharacters.size() - LIST_MODE_VISIBLE_COUNT);
        if (maxScroll == 0) {
            listModeScroll = 0;
            return;
        }
        double relativeY = mouseY - (topPos + SCROLLBAR_Y);
        double ratio = relativeY / (SCROLLBAR_HEIGHT - SCROLLBAR_THUMB_HEIGHT);
        ratio = Math.max(0, Math.min(1, ratio));
        listModeScroll = (int) Math.round(ratio * maxScroll);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (draggingScrollbar && currentTab == DictionaryTab.LIST) {
            updateScrollFromMouseY(mouseY);
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (draggingScrollbar) {
            draggingScrollbar = false;
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    private class BookmarkButton extends AbstractButton {
        private final DictionaryTab tab;

        public BookmarkButton(int x, int y, DictionaryTab tab) {
            super(x, y, BOOKMARK_WIDTH, BOOKMARK_HEIGHT, Component.empty());
            this.tab = tab;
        }

        @Override
        public void onPress() {
            switchTab(tab);
        }

        @Override
        protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
            int v = tab.ordinal() * BOOKMARK_HEIGHT;
            graphics.blit(BOOKMARKS_TEXTURE, getX(), getY(), 0, v, BOOKMARK_WIDTH, BOOKMARK_HEIGHT,
                    BOOKMARK_WIDTH, BOOKMARKS_TEXTURE_HEIGHT);

            if (isHovered()) {
                graphics.fill(getX(), getY(), getX() + BOOKMARK_WIDTH, getY() + BOOKMARK_HEIGHT, 0x66FFFFFF);
                graphics.renderTooltip(Minecraft.getInstance().font,
                        Component.translatable(tab.getTranslationKey()), mouseX, mouseY);
            }
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
            narrationElementOutput.add(NarratedElementType.TITLE, Component.translatable(tab.getTranslationKey()));
        }
    }

    private class QualityCheckbox extends AbstractButton {
        private final int index;

        public QualityCheckbox(int x, int y, int index) {
            super(x, y, BUTTON_SIZE, BUTTON_SIZE, Component.empty());
            this.index = index;
        }

        @Override
        public void onPress() {
            qualityFilters[index] = !qualityFilters[index];
            rebuildListMode();
        }

        @Override
        protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
            graphics.fill(getX(), getY(), getX() + BUTTON_SIZE, getY() + BUTTON_SIZE, 0xFF90764a);
            graphics.fill(getX() + 1, getY() + 1, getX() + BUTTON_SIZE - 1, getY() + BUTTON_SIZE - 1, 0xFFbea884);
            if (qualityFilters[index]) {
                graphics.fill(getX() + 3, getY() + 3, getX() + BUTTON_SIZE - 3, getY() + BUTTON_SIZE - 3, 0xFFb0925f);
            }
            if (isHovered()) {
                graphics.fill(getX(), getY(), getX() + BUTTON_SIZE, getY() + BUTTON_SIZE, 0x33FFFFFF);
            }
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
            narrationElementOutput.add(NarratedElementType.TITLE, Component.translatable(QUALITY_KEYS[index]));
        }
    }

    private class IdiomFilterCheckbox extends AbstractButton {
        private final boolean collectedFilter;

        public IdiomFilterCheckbox(int x, int y, boolean collectedFilter) {
            super(x, y, IDIOM_FILTER_SIZE, IDIOM_FILTER_SIZE, Component.empty());
            this.collectedFilter = collectedFilter;
        }

        @Override
        public void onPress() {
            if (collectedFilter) {
                filterCollectedOnly = !filterCollectedOnly;
                if (filterCollectedOnly) filterUncollectedOnly = false;
            } else {
                filterUncollectedOnly = !filterUncollectedOnly;
                if (filterUncollectedOnly) filterCollectedOnly = false;
            }
            rebuildIdiomEntries();
        }

        @Override
        protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
            boolean checked = collectedFilter ? filterCollectedOnly : filterUncollectedOnly;
            graphics.fill(getX(), getY(), getX() + IDIOM_FILTER_SIZE, getY() + IDIOM_FILTER_SIZE, 0xFF90764a);
            graphics.fill(getX() + 1, getY() + 1, getX() + IDIOM_FILTER_SIZE - 1, getY() + IDIOM_FILTER_SIZE - 1, 0xFFbea884);
            if (checked) {
                graphics.fill(getX() + 2, getY() + 2, getX() + IDIOM_FILTER_SIZE - 2, getY() + IDIOM_FILTER_SIZE - 2, 0xFFb0925f);
            }
            if (isHovered()) {
                graphics.renderTooltip(Minecraft.getInstance().font,
                        Component.translatable(collectedFilter
                                ? "gui.jiagureappear.dictionary.idiom_filter_collected"
                                : "gui.jiagureappear.dictionary.idiom_filter_uncollected"),
                        mouseX, mouseY);
            }
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
            narrationElementOutput.add(NarratedElementType.TITLE, Component.empty());
        }
    }
}
