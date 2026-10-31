package net.lghast.jiagu.config;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.Arrays;
import java.util.List;

public class ClientConfig {
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;
    private static final String CONFIG_PREFIX = "config.jiagureappear.";

    public static ModConfigSpec.DoubleValue WENCHANG_ALTAR_ROTATION_SPEED;
    public static ModConfigSpec.DoubleValue ERUDITE_WENCHANG_ALTAR_ROTATION_SPEED;

    public static ModConfigSpec.BooleanValue DISPLAY_STRUCTURE_INFO_WHEN_SHIFT;
    public static ModConfigSpec.BooleanValue DISPLAY_DISASSEMBLY_INFO;
    public static ModConfigSpec.BooleanValue DISPLAY_ASSEMBLY_INFO;
    public static ModConfigSpec.BooleanValue IDIOM_DESCRIPTION_REQUIRES_COLLECTION;

    public static ModConfigSpec.ConfigValue<List<? extends String>> PRESCRIPTION_MAPPINGS;

    static {
        BUILDER.push("方块 Blocks");

        WENCHANG_ALTAR_ROTATION_SPEED = BUILDER
                .comment("文昌坛展示物旋转速度 Wenchang altar display item rotation speed")
                .translation(CONFIG_PREFIX + "wenchang_altar_rotation_speed")
                .defineInRange("wenchang_altar_rotation_speed", 0.15, 0.0, 10.0);
        ERUDITE_WENCHANG_ALTAR_ROTATION_SPEED = BUILDER
                .comment("文昌坛展示物旋转速度 Erudite Wenchang altar display item rotation speed")
                .translation(CONFIG_PREFIX + "erudite_wenchang_altar_rotation_speed")
                .defineInRange("erudite_wenchang_altar_rotation_speed", 0.12, 0.0, 10.0);

        BUILDER.pop();

        BUILDER.push("物品 Items");

        DISPLAY_STRUCTURE_INFO_WHEN_SHIFT = BUILDER
                .comment("甲骨文物品的解字合文信息是否仅在按住Shift键时显示",
                        "Oracle bone structure information (assembled from / assembles into) only shows while holding Shift")
                .translation(CONFIG_PREFIX + "display_structure_info_when_shift")
                .define("display_structure_info_when_shift", true);
        DISPLAY_DISASSEMBLY_INFO = BUILDER
                .comment("甲骨文物品显示解字信息 Jiagu inscription items display disassembly information")
                .translation(CONFIG_PREFIX + "display_disassembly_info")
                .define("display_disassembly_info", true);
        DISPLAY_ASSEMBLY_INFO = BUILDER
                .comment("甲骨文物品显示合文信息 Jiagu inscription items display assembly information")
                .translation(CONFIG_PREFIX + "display_assembly_info")
                .define("display_assembly_info", true);
        IDIOM_DESCRIPTION_REQUIRES_COLLECTION = BUILDER
                .comment("未收集的成语是否需要先收集才能查看简介",
                        "Whether uncollected idioms require collection before viewing their description")
                .translation(CONFIG_PREFIX + "idiom_description_requires_collection")
                .define("idiom_description_requires_collection", false);

        PRESCRIPTION_MAPPINGS = BUILDER
                .comment("药方状态效果映射，用于自定义储存某个状态效果的药方的纹理。除更改此配置外还需添加和修改相应物品模型JSON文件",
                        "Prescription status effect mapping, used to customize the texture of a prescription that stores a specific status effect. In addition to modifying this configuration, the corresponding item model JSON files must also be added or modified.")
                .translation(CONFIG_PREFIX + "prescription_mappings")
                .defineList(
                        List.of("prescription_mappings"),
                        () -> Arrays.asList(
                                "minecraft:poison=1.0",
                                "minecraft:strength=2.0",
                                "minecraft:speed=3.0",
                                "minecraft:weakness=4.0",
                                "minecraft:night_vision=5.0",
                                "minecraft:fire_resistance=6.0",
                                "minecraft:hunger=7.0",
                                "minecraft:darkness=8.0",
                                "minecraft:water_breathing=9.0",
                                "minecraft:invisibility=10.0",
                                "minecraft:resistance=11.0",
                                "minecraft:regeneration=12.0",
                                "minecraft:slowness=13.0",
                                "minecraft:wither=14.0",
                                "minecraft:jump_boost=15.0",
                                "minecraft:slow_falling=16.0",
                                "minecraft:luck=17.0",
                                "minecraft:unluck=18.0",
                                "minecraft:bad_omen=19.0",
                                "minecraft:blindness=20.0",
                                "minecraft:mining_fatigue=21.0",
                                "minecraft:glowing=22.0",
                                "minecraft:levitation=23.0",
                                "minecraft:nausea=24.0",
                                "minecraft:conduit_power=25.0",
                                "minecraft:weaving=26.0",
                                "minecraft:oozing=27.0",
                                "minecraft:infested=28.0",
                                "minecraft:saturation=29.0"
                        ),
                        null,
                        it -> it instanceof String && ((String) it).matches("[a-z0-9_.]+:[a-z0-9_.]+=[0-9]+(\\.[0-9]+)?"),
                        null
                );

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
