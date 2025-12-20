package net.lghast.jiagu.client.screen;

import net.minecraft.network.chat.Component;

public enum DisplayMode {
    ASSEMBLY_RECIPE("gui.jiagureappear.dictionary.assembly_recipe"),
    ASSEMBLY_LIST("gui.jiagureappear.dictionary.assembly_list"),
    RELATED_ITEMS("gui.jiagureappear.dictionary.related_items"),
    RELATED_ENCHANTMENTS("gui.jiagureappear.related_enchantments"),
    RELATED_EFFECTS("gui.jiagureappear.dictionary.related_effects"),
    RELATED_ENTITIES("gui.jiagureappear.dictionary.related_entities"),
    DEFAULT("");

    private final String displayName;

    DisplayMode(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return Component.translatable(displayName).getString();
    }
}
