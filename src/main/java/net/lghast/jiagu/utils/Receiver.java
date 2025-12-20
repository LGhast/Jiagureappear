package net.lghast.jiagu.utils;

public enum Receiver {
    COMMON("minecraft:enchanted_book"),
    MELEE("minecraft:iron_axe"),
    MINING_TOOL("minecraft:iron_shovel"),
    ARMOR("minecraft:iron_chestplate"),
    CURSE("minecraft:ominous_bottle"),
    HELMET("minecraft:iron_helmet"),
    LEGGINGS("minecraft:iron_leggings"),
    BOOTS("minecraft:iron_boots"),
    SWORD("minecraft:iron_sword"),
    BOW("minecraft:bow"),
    CROSSBOW("minecraft:crossbow"),
    TRIDENT("minecraft:trident"),
    FISHING_ROD("minecraft:fishing_rod"),
    MACE("minecraft:mace"),
    AMETHYST_INSCRIPTION("jiagureappear:amethyst_upgrade_smithing_template");

    private final String item;

    Receiver(String item) {
        this.item = item;
    }

    public String getItem() {
        return item;
    }
}
