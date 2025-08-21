package net.lghast.jiagu.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ClientConfig {
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    public static ModConfigSpec.DoubleValue WENCHANG_ALTAR_ROTATION_SPEED;
    public static ModConfigSpec.DoubleValue ERUDITE_WENCHANG_ALTAR_ROTATION_SPEED;

    public static ModConfigSpec.BooleanValue DISPLAY_DISASSEMBLY_INFO;
    public static ModConfigSpec.BooleanValue DISPLAY_ASSEMBLY_INFO;

    static {
        BUILDER.push("方块 Blocks");

        WENCHANG_ALTAR_ROTATION_SPEED = BUILDER
                .comment("文昌坛展示物旋转速度 Wenchang altar display item rotation speed")
                .defineInRange("wenchang_altar_rotation_speed", 0.15, 0.0, 10.0);
        ERUDITE_WENCHANG_ALTAR_ROTATION_SPEED = BUILDER
                .comment("文昌坛展示物旋转速度 Erudite Wenchang altar display item rotation speed")
                .defineInRange("erudite_wenchang_altar_rotation_speed", 0.12, 0.0, 10.0);

        BUILDER.pop();

        BUILDER.push("物品 Items");

        DISPLAY_DISASSEMBLY_INFO = BUILDER
                .comment("甲骨文物品显示解字信息 Jiagu inscription items display disassembly information")
                        .define("display_disassembly_info", true);
        DISPLAY_ASSEMBLY_INFO = BUILDER
                .comment("甲骨文物品显示合文信息 Jiagu inscription items display assembly information")
                .define("display_assembly_info", true);

        BUILDER.pop();

        SPEC = BUILDER.build();
    }
}
