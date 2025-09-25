package net.lghast.jiagu.config;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.Arrays;
import java.util.List;

public class ServerConfig {
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    public static ModConfigSpec.DoubleValue YI_CONFLAGRANT_BASE_DAMAGE;
    public static ModConfigSpec.IntValue YI_CONFLAGRANT_ATTACK_INTERVAL;
    public static ModConfigSpec.BooleanValue YI_CONFLAGRANT_EXTRA_DAMAGE_TO_FUR;

    public static ModConfigSpec.DoubleValue BIAO_GALE_BASE_DISTANCE;
    public static ModConfigSpec.IntValue BIAO_GALE_WIND_ANGLE;
    public static ModConfigSpec.DoubleValue BIAO_GALE_BASE_SPEED;
    public static ModConfigSpec.DoubleValue BIAO_GALE_VOLUME_FACTOR;

    public static ModConfigSpec.DoubleValue JIAN_SWORD_CHARACTER_CHANCE;
    public static ModConfigSpec.BooleanValue JIAN_SWORD_CUSTOM_NAME_CHECK;

    public static ModConfigSpec.IntValue YI_CURE_COOLDOWN_TIME;
    public static ModConfigSpec.IntValue YI_CURE_BASE_DURATION;
    public static ModConfigSpec.IntValue YI_CURE_BASE_AMPLIFIER;

    public static ModConfigSpec.IntValue GU_PARASITE_COOLDOWN_TIME;
    public static ModConfigSpec.IntValue GU_PARASITE_BASE_DURATION;
    public static ModConfigSpec.IntValue GU_PARASITE_BASE_AMPLIFIER;
    public static ModConfigSpec.DoubleValue GU_PARASITE_PROJECTILE_VELOCITY_MAX;
    public static ModConfigSpec.DoubleValue GU_PARASITE_PROJECTILE_VELOCITY_MIN;

    public static ModConfigSpec.IntValue LEI_STONES_COOLDOWN_TIME;
    public static ModConfigSpec.DoubleValue LEI_STONES_BASE_DAMAGE;
    public static ModConfigSpec.DoubleValue LEI_STONES_BASE_RADIUS;
    public static ModConfigSpec.DoubleValue LEI_STONES_PROJECTILE_VELOCITY_MAX;
    public static ModConfigSpec.DoubleValue LEI_STONES_PROJECTILE_VELOCITY_MIN;

    public static ModConfigSpec.DoubleValue YOLIME_BLOCK_BOUNCE_VELOCITY_CAP;
    public static ModConfigSpec.BooleanValue CANGJIE_TRIPOD_CUSTOM_NAME_CHECK;
    public static ModConfigSpec.BooleanValue CANGJIE_MORPHER_CUSTOM_NAME_CHECK;
    public static ModConfigSpec.BooleanValue RUBBING_TABLE_CUSTOM_NAME_CHECK;
    public static ModConfigSpec.BooleanValue RUBBING_MACHINE_CUSTOM_NAME_CHECK;
    public static ModConfigSpec.BooleanValue GOLD_BRICKS_EFFECT;

    public static ModConfigSpec.ConfigValue<List<? extends String>> IDIOM_LIST;
    public static ModConfigSpec.ConfigValue<List<? extends String>> CHARACTER_DISASSEMBLING_MAPPINGS;
    public static ModConfigSpec.ConfigValue<List<? extends String>> CHARACTER_MAPPINGS;

    static {
        BUILDER.push("核心内容 Core Content");

        IDIOM_LIST = BUILDER
                .comment("有效成语列表 Valid idiom list",
                        "列于此处的字符串会被判定为有效成语，但仍需要数据包自定义相关进度。",
                        "Strings listed here will be recognized as valid idioms, though the relevant advancements still need to be customized via a data pack.")
                .defineList("idiom_list",
                        () -> Arrays.asList("小心翼翼", "一石二鳥", "九牛一毛", "水乳交融", "如魚得水",
                                "從善如流", "孤芳自賞", "光明磊落", "自強不息", "一片丹心", "心靈手巧",
                                "大逆不道", "胡說八道", "囊螢映雪", "背井離鄉", "粉墨登場", "求知若渴",
                                "筆耕不輟", "名列前茅", "美中不足", "買櫝還珠"),
                        () -> "成语",
                        o -> o instanceof String);

        CHARACTER_DISASSEMBLING_MAPPINGS = BUILDER
                .comment("汉字拆解配方，例：塊=土,鬼。可覆盖原版配方。",
                        "Character Disassembling Recipe, e.g. 塊=土,鬼. Vanilla recipes can be override.")
                .defineListAllowEmpty(
                        List.of("character_disassembling_mappings"),
                        () -> Arrays.asList(
                                "哈=口,合",
                                "Minecraft=我,的,世,界"
                        ),
                        null,
                        it -> it instanceof String
                );

        CHARACTER_MAPPINGS = BUILDER
                .comment("甲骨文物品额外纹理映射，用于自定义新增甲骨文物品的纹理。除更改此配置外还需添加和修改相应物品模型JSON文件",
                        "Extra Jiagu item texture mapping, used to customize the texture of Jiagu Item. In addition to modifying this configuration, the corresponding item model JSON files must also be added or modified.",
                        "格式为 字符,小数,纹理图片文件名",
                        "Format: character, decimal, texture image filename",
                        "e.g. 舌,139.0,she_tongue",
                        "此处小数也表示物品品质，1~999为石质，1000~1999为铁质，2000~2999为金质，3000~3999为钻石质，6000~6999为铜质",
                        "The decimal value also represents the item's quality: 1-999 corresponds to Stone, 1000-1999 to Iron, 2000-2999 to Gold, 3000-3999 to Diamond, and 6000-6999 to Copper.")
                .defineListAllowEmpty(
                        List.of("character_mappings"),
                        () -> Arrays.asList(
                                "舌,139.0,she_tongue"
                        ),
                        null,
                        it -> it instanceof String && ((String) it).matches("^[^,]+,-?\\d*(?:\\.\\d+)?,[a-z_]+$")
                );

        BUILDER.pop();


        BUILDER.push("紫晶铭刻 Amethyst Inscriptions");

        BUILDER.push(" 燚 Yi-Conflagrant");
        YI_CONFLAGRANT_BASE_DAMAGE = BUILDER
                .comment("基础伤害 Base damage")
                .defineInRange("yi_conflagrant_base_damage", 1.0, 0.0, 999.0);
        YI_CONFLAGRANT_ATTACK_INTERVAL = BUILDER
                .comment("攻击间隔（刻） Interval between attacks (in ticks)")
                .defineInRange("yi_conflagrant_attack_interval", 20, 1, 999);
        YI_CONFLAGRANT_EXTRA_DAMAGE_TO_FUR = BUILDER
                .comment("是否对有皮毛的生物伤害翻倍  Whether to deal double damage to furry mobs")
                .define("yi_conflagrant_extra_damage_to_fur", true);
        BUILDER.pop();

        BUILDER.push(" 飆 Biao-Gale");
        BIAO_GALE_BASE_DISTANCE = BUILDER
                .comment("基础最大推移距离 Base maximum translation distance")
                .defineInRange("biao_gale_base_distance", 6.0, 0.0, 999.0);
        BIAO_GALE_WIND_ANGLE = BUILDER
                .comment("风场半角 Wind field half-angle",
                        "此配置控制风场的角度，实际角度会为此值的两倍。（若设置为30，则实际风场角为60°）",
                        "This configuration controls the angle of the wind field. The actual angle will be twice this value. (If set to 30, the actual wind field angle will be 60°)")
                .defineInRange("biao_gale_wind_angle", 30, 0, 180);
        BIAO_GALE_BASE_SPEED = BUILDER
                .comment("基础风速 Base wind speed")
                .defineInRange("biao_gale_base_wind_speed", 0.3, 0.0, 50.0);
        BIAO_GALE_VOLUME_FACTOR = BUILDER
                .comment("体积影响因子 Volume effect multiplier",
                        "此配置影响实体碰撞体积对风场推动速度的影响。值越大，大体积实体越不易吹动。",
                        "This configuration affects how the collision volume of an entity influences the push force from the wind field. A larger value makes it harder to blow away larger entities. ")
                .defineInRange("biao_gale_volume_factor", 1.2, 0.0, 10.0);
        BUILDER.pop();

        BUILDER.push(" 劍 Jian-Sword");
        JIAN_SWORD_CHARACTER_CHANCE = BUILDER
                .comment("基础甲骨文掉落概率 Base Jiagu inscription drop chance")
                .defineInRange("jian_sword_base_drop_chance", 0.2, 0.0, 1.0);
        JIAN_SWORD_CUSTOM_NAME_CHECK = BUILDER
                .comment("实体自定义名称检测 Entity custom name detection",
                        "是否检测受击杀生物有无自定义命名",
                        "Determines whether to check if the killed mob has a custom name")
                .define("cangjie_tripod_custom_name_check", true);
        BUILDER.pop();

        BUILDER.push(" 醫 Yi-Cure");
        YI_CURE_COOLDOWN_TIME = BUILDER
                .comment("冷却时间（刻） Cooldown (in ticks)")
                .defineInRange("yi_cure_cooldown", 2000, 0, 72000);
        YI_CURE_BASE_DURATION = BUILDER
                .comment("效果基础持续时间（刻） Base effect duration (in ticks)")
                .defineInRange("yi_cure_base_duration", 220, 0, 72000);
        YI_CURE_BASE_AMPLIFIER = BUILDER
                .comment("效果放大器 Effect amplifier")
                .defineInRange("yi_cure_amplifier", 0, 0, 254);
        BUILDER.pop();

        BUILDER.push(" 蠱 Gu-Parasite");
        GU_PARASITE_COOLDOWN_TIME = BUILDER
                .comment("冷却时间（刻） Cooldown (in ticks)")
                .defineInRange("gu_parasite_cooldown", 400, 0, 72000);
        GU_PARASITE_BASE_DURATION = BUILDER
                .comment("效果基础持续时间（刻） Base effect duration (in ticks)")
                .defineInRange("gu_parasite_base_duration", 240, 0, 72000);
        GU_PARASITE_BASE_AMPLIFIER = BUILDER
                .comment("效果放大器 Effect amplifier")
                .defineInRange("gu_parasite_amplifier", 0, 0, 254);
        GU_PARASITE_BASE_AMPLIFIER = BUILDER
                .comment("效果放大器 Effect amplifier")
                .defineInRange("gu_parasite_amplifier", 0, 0, 254);
        GU_PARASITE_PROJECTILE_VELOCITY_MAX = BUILDER
                .comment("弹射物最大蓄力速度加成 Maximum projectile charge velocity bonus",
                        "此配置控制玩家通过蓄力最多能使弹射物发射速度增加多少。",
                        "This configuration controls how much the player can increase the projectile's launch speed at most by charging.")
                .defineInRange("gu_parasite_velocity_bonus_max", 2.5, 0.0, 50.0);
        GU_PARASITE_PROJECTILE_VELOCITY_MIN = BUILDER
                .comment("弹射物最小速度 Minimum projectile velocity")
                .defineInRange("gu_parasite_velocity_min", 0.8, 0.0, 50.0);
        BUILDER.pop();

        BUILDER.push(" 磊 Lei-Stones");
        LEI_STONES_COOLDOWN_TIME = BUILDER
                .comment("冷却时间（刻） Cooldown (in ticks)")
                .defineInRange("lei_stones_cooldown", 40, 0, 72000);
        LEI_STONES_BASE_DAMAGE = BUILDER
                .comment("基础伤害 Base damage")
                .defineInRange("lei_stones_base_damage", 5.0f, 0, 999.0f);
        LEI_STONES_BASE_RADIUS = BUILDER
                .comment("基础范围伤害半径 Base AOE radius")
                .defineInRange("lei_stones_base_radius", 1.2f, 0, 15.0f);
        LEI_STONES_PROJECTILE_VELOCITY_MAX = BUILDER
                .comment("弹射物最大蓄力速度加成 Maximum projectile charge velocity bonus",
                        "此配置控制玩家通过蓄力最多能使弹射物发射速度增加多少。",
                        "This configuration controls how much the player can increase the projectile's launch speed at most by charging.")
                .defineInRange("lei_stones_velocity_bonus_max", 2.3, 0.0, 50.0);
        LEI_STONES_PROJECTILE_VELOCITY_MIN = BUILDER
                .comment("弹射物最小速度 Minimum projectile velocity")
                .defineInRange("lei_stones_velocity_min", 0.8, 0.0, 50.0);
        BUILDER.pop();

        BUILDER.pop();



        BUILDER.push("方块 Blocks");

        YOLIME_BLOCK_BOUNCE_VELOCITY_CAP = BUILDER
                .comment("悠酱块弹跳速度上限 Bounce velocity cap of yolime block")
                .defineInRange("yolime_block_bounce_velocity_cap", 66.6, 0.0, 999.0);
        CANGJIE_TRIPOD_CUSTOM_NAME_CHECK = BUILDER
                .comment("仓颉鼎自定义名称检测 Cangjie Ding-tripod custom name detection",
                        "使用仓颉鼎时是否检测玩家手持物品有无自定义命名",
                        "Determines whether to check if the player's held item has a custom name when using the Cangjie Ding-tripod")
                .define("cangjie_tripod_custom_name_check", true);
        CANGJIE_MORPHER_CUSTOM_NAME_CHECK = BUILDER
                .comment("仓颉造字器自定义名称检测 Cangjie morpher custom name detection")
                .define("cangjie_morpher_custom_name_check", true);
        RUBBING_TABLE_CUSTOM_NAME_CHECK = BUILDER
                .comment("拓印台自定义名称检测 Rubbing table custom name detection")
                .define("rubbing_table_custom_name_check", true);
        RUBBING_MACHINE_CUSTOM_NAME_CHECK = BUILDER
                .comment("拓印器自定义名称检测 Rubbing machine custom name detection")
                .define("rubbing_machine_custom_name_check", true);
        GOLD_BRICKS_EFFECT = BUILDER
                .comment("金砖实际效果 Effect of gold bricks")
                .define("gold_bricks_effect", true);

        BUILDER.pop();


        SPEC = BUILDER.build();
    }
}
