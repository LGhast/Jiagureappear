package net.lghast.jiagu.client.screen;

public enum DictionaryTab {
    LOOKUP("gui.jiagureappear.dictionary.tab.lookup"),
    LIST("gui.jiagureappear.dictionary.tab.list"),
    IDIOM("gui.jiagureappear.dictionary.tab.idiom");

    private final String translationKey;

    DictionaryTab(String translationKey) {
        this.translationKey = translationKey;
    }

    public String getTranslationKey() {
        return translationKey;
    }
}
