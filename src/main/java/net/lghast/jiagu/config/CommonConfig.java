package net.lghast.jiagu.config;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.Arrays;
import java.util.List;

public class CommonConfig {
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;
    private static final String CONFIG_PREFIX = "config.jiagureappear.";

    public static ModConfigSpec.DoubleValue YI_CONFLAGRANT_CENTER_DAMAGE;
    public static ModConfigSpec.DoubleValue YI_CONFLAGRANT_EDGE_DAMAGE;
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
    public static ModConfigSpec.BooleanValue GOLD_BRICKS_EFFECT;

    public static ModConfigSpec.ConfigValue<List<? extends String>> CHARACTER_DISASSEMBLING_MAPPINGS;
    public static ModConfigSpec.ConfigValue<List<? extends String>> CHARACTER_MAPPINGS;

    public static ModConfigSpec.ConfigValue<List<? extends String>> ITEM_LZH;
    public static ModConfigSpec.ConfigValue<List<? extends String>> ENTITY_LZH;
    public static ModConfigSpec.ConfigValue<List<? extends String>> ENCHANTMENT_LZH;
    public static ModConfigSpec.ConfigValue<List<? extends String>> EFFECT_LZH;
    public static ModConfigSpec.ConfigValue<List<? extends String>> POTION_LZH;
    public static ModConfigSpec.ConfigValue<List<? extends String>> RECEIVER_MAPPINGS;

    static {
        BUILDER.push("核心内容 Core Content");

        CHARACTER_DISASSEMBLING_MAPPINGS = BUILDER
                .comment("汉字拆解配方,可覆盖原版配方。",
                        "Character Disassembling Recipe. Vanilla recipes can be override.",
                        "e.g. [\"塊=土,鬼\", \"焱=火,火,火\"]")
                .translation(CONFIG_PREFIX + "character_disassembling_mappings")
                .defineList(
                        List.of("character_disassembling_mappings"),
                        Arrays::asList,
                        null,
                        it -> it instanceof String,
                        null
                );

        CHARACTER_MAPPINGS = BUILDER
                .comment("甲骨文物品额外纹理映射，用于自定义新增甲骨文物品的纹理。除更改此配置外还需添加和修改相应物品模型JSON文件",
                        "Extra Jiagu item texture mapping, used to customize the texture of Jiagu Item. In addition to modifying this configuration, the corresponding item model JSON files must also be added or modified.",
                        "格式为 字符,小数,纹理图片文件名",
                        "Format: character, decimal, texture image filename",
                        "e.g. [\"舌,139.0,she_tongue\", \"言,140.0,yan_speak\"]",
                        "此处小数也表示物品品质，1~999为石质，1000~1999为铁质，2000~2999为金质，3000~3999为钻石质，6000~6999为铜质",
                        "The decimal value also represents the item's quality: 1-999 corresponds to Stone, 1000-1999 to Iron, 2000-2999 to Gold, 3000-3999 to Diamond, and 6000-6999 to Copper.")
                .translation(CONFIG_PREFIX + "character_mappings")
                .defineList(
                        List.of("character_mappings"),
                        Arrays::asList,
                        null,
                        it -> it instanceof String && ((String) it).matches("^[^,]+,-?\\d*(?:\\.\\d+)?,[a-z_]+$"),
                        null
                );

        ITEM_LZH = BUILDER
                .comment("物品文言名称映射，为缺少文言名称的物品添加文言名称，或覆盖已有名称",
                        "Item LZH name mapping, assigns LZH names to items lacking them, or overrides existing names",
                        "e.g. [\"minecraft:apple,林檎\", \"minecraft:iron_axe,鐵斧\"]")
                .translation(CONFIG_PREFIX + "item_lzh")
                .defineList(
                        List.of("item_lzh"),
                        Arrays::asList,
                        null,
                        it -> it instanceof String && ((String) it).matches("^[a-zA-Z0-9_.-]+:[a-zA-Z0-9_.-]+,[\\s\\S]+$"),
                        null
                );

        ENTITY_LZH = BUILDER
                .comment("实体文言名称映射，为缺少文言名称的实体添加文言名称，或覆盖已有名称",
                        "Entity LZH name mapping, assigns LZH names to entities lacking them, or overrides existing names",
                        "e.g. [\"minecraft:zombie,殭屍\", \"minecraft:pig,豕\"]")
                .translation(CONFIG_PREFIX + "entity_lzh")
                .defineList(
                        List.of("entity_lzh"),
                        Arrays::asList,
                        null,
                        it -> it instanceof String && ((String) it).matches("^[a-zA-Z0-9_.-]+:[a-zA-Z0-9_.-]+,[\\s\\S]+$"),
                        null
                );

        ENCHANTMENT_LZH = BUILDER
                .comment("附魔文言名称映射，为缺少文言名称的附魔添加文言名称，或覆盖已有名称",
                        "Enchantment LZH name mapping, assigns LZH names to enchantments lacking them, or overrides existing names",
                        "e.g. [\"minecraft:protection,護\", \"minecraft:channeling,通雷術\"]")
                .translation(CONFIG_PREFIX + "enchantment_lzh")
                .defineList(
                        List.of("enchantment_lzh"),
                        Arrays::asList,
                        null,
                        it -> it instanceof String && ((String) it).matches("^[a-zA-Z0-9_.-]+:[a-zA-Z0-9_.-]+,[\\s\\S]+$"),
                        null
                );

        RECEIVER_MAPPINGS = BUILDER
                .comment("附魔接受者映射，为附魔添加对应物品，用于在字典中显示。若已在配置中添加新的附魔文言名称，必须在此处添加对应物品",
                        "Enchantment receiver mapping, adds corresponding items for enchantments to display in the dictionary. If you have added new LZH names for an enchantment in the configuration, you must add corresponding items here",
                        "e.g. [\"minecraft:sharpness,minecraft:iron_axe\", \"minecraft:power,minecraft:bow\"]")
                .translation(CONFIG_PREFIX + "receiver_mappings")
                .defineList(
                        List.of("receiver_mappings"),
                        Arrays::asList,
                        null,
                        it -> it instanceof String && ((String) it).matches("^[a-zA-Z0-9_.-]+:[a-zA-Z0-9_.-]+,[a-zA-Z0-9_.-]+:[a-zA-Z0-9_.-]+$"),
                        null
                );

        EFFECT_LZH = BUILDER
                .comment("状态效果文言名称映射，为缺少文言名称的状态效果添加文言名称，或覆盖已有名称",
                        "Status effect LZH name mapping, assigns LZH names to effects lacking them, or overrides existing names",
                        "e.g. [\"minecraft:regeneration,復甦\", \"minecraft:health_boost,益壽\"]")
                .translation(CONFIG_PREFIX + "effect_lzh")
                .defineList(
                        List.of("effect_lzh"),
                        Arrays::asList,
                        null,
                        it -> it instanceof String && ((String) it).matches("^[a-zA-Z0-9_.-]+:[a-zA-Z0-9_.-]+,[\\s\\S]+$"),
                        null
                );

        POTION_LZH = BUILDER
                .comment("药水文言名称映射，为缺少文言名称的药水添加文言名称，或覆盖已有名称",
                        "Potion LZH name mapping, assigns LZH names to potions lacking them, or overrides existing names",
                        "e.g. [\"minecraft:long_night_vision,久效夜視劑\", \"minecraft:awkward,粗劑\"]")
                .translation(CONFIG_PREFIX + "potion_lzh")
                .defineList(
                        List.of("potion_lzh"),
                        Arrays::asList,
                        null,
                        it -> it instanceof String && ((String) it).matches("^[a-zA-Z0-9_.-]+:[a-zA-Z0-9_.-]+,[\\s\\S]+$"),
                        null
                );

        BUILDER.pop();


        BUILDER.push("紫晶铭刻 Amethyst Inscriptions");

        BUILDER.push(" 燚 Yi-Conflagrant");
        YI_CONFLAGRANT_CENTER_DAMAGE = BUILDER
                .comment("中心伤害（距离为0） Damage at the center (distance 0)")
                .translation(CONFIG_PREFIX + "yi_conflagrant_center_damage")
                .defineInRange("yi_conflagrant_center_damage", 0.5, 0.0, 999.0);
        YI_CONFLAGRANT_EDGE_DAMAGE = BUILDER
                .comment("边缘伤害（距离最远） Damage at the edge (farthest)")
                .translation(CONFIG_PREFIX + "yi_conflagrant_edge_damage")
                .defineInRange("yi_conflagrant_edge_damage", 1.5, 0.0, 999.0);
        YI_CONFLAGRANT_ATTACK_INTERVAL = BUILDER
                .comment("攻击间隔（刻） Interval between attacks (in ticks)")
                .translation(CONFIG_PREFIX + "yi_conflagrant_attack_interval")
                .defineInRange("yi_conflagrant_attack_interval", 20, 1, 999);
        YI_CONFLAGRANT_EXTRA_DAMAGE_TO_FUR = BUILDER
                .comment("是否对有皮毛的生物伤害翻倍  Whether to deal double damage to furry mobs")
                .translation(CONFIG_PREFIX + "yi_conflagrant_extra_damage_to_fur")
                .define("yi_conflagrant_extra_damage_to_fur", true);
        BUILDER.pop();

        BUILDER.push(" 飆 Biao-Gale");
        BIAO_GALE_BASE_DISTANCE = BUILDER
                .comment("基础最大推移距离 Base maximum translation distance")
                .translation(CONFIG_PREFIX + "biao_gale_base_distance")
                .defineInRange("biao_gale_base_distance", 6.0, 0.0, 999.0);
        BIAO_GALE_WIND_ANGLE = BUILDER
                .comment("风场半角 Wind field half-angle",
                        "此配置控制风场的角度，实际角度会为此值的两倍。（若设置为30，则实际风场角为60°）",
                        "This configuration controls the angle of the wind field. The actual angle will be twice this value. (If set to 30, the actual wind field angle will be 60°)")
                .translation(CONFIG_PREFIX + "biao_gale_wind_angle")
                .defineInRange("biao_gale_wind_angle", 30, 0, 180);
        BIAO_GALE_BASE_SPEED = BUILDER
                .comment("基础风速 Base wind speed")
                .translation(CONFIG_PREFIX + "biao_gale_base_wind_speed")
                .defineInRange("biao_gale_base_wind_speed", 0.3, 0.0, 50.0);
        BIAO_GALE_VOLUME_FACTOR = BUILDER
                .comment("体积影响因子 Volume effect multiplier",
                        "此配置影响实体碰撞体积对风场推动速度的影响。值越大，大体积实体越不易吹动。",
                        "This configuration affects how the collision volume of an entity influences the push force from the wind field. A larger value makes it harder to blow away larger entities. ")
                .translation(CONFIG_PREFIX + "biao_gale_volume_factor")
                .defineInRange("biao_gale_volume_factor", 1.2, 0.0, 10.0);
        BUILDER.pop();

        BUILDER.push(" 劍 Jian-Sword");
        JIAN_SWORD_CHARACTER_CHANCE = BUILDER
                .comment("基础甲骨文掉落概率 Base Jiagu inscription drop chance")
                .translation(CONFIG_PREFIX + "jian_sword_base_drop_chance")
                .defineInRange("jian_sword_base_drop_chance", 0.2, 0.0, 1.0);
        JIAN_SWORD_CUSTOM_NAME_CHECK = BUILDER
                .comment("实体自定义名称检测 Entity custom name detection",
                        "是否检测受击杀生物有无自定义命名",
                        "Determines whether to check if the killed mob has a custom name")
                .translation(CONFIG_PREFIX + "cangjie_tripod_custom_name_check")
                .define("cangjie_tripod_custom_name_check", true);
        BUILDER.pop();

        BUILDER.push(" 醫 Yi-Cure");
        YI_CURE_COOLDOWN_TIME = BUILDER
                .comment("冷却时间（刻） Cooldown (in ticks)")
                .translation(CONFIG_PREFIX + "yi_cure_cooldown")
                .defineInRange("yi_cure_cooldown", 2000, 0, 72000);
        YI_CURE_BASE_DURATION = BUILDER
                .comment("效果基础持续时间（刻） Base effect duration (in ticks)")
                .translation(CONFIG_PREFIX + "yi_cure_base_duration")
                .defineInRange("yi_cure_base_duration", 220, 0, 72000);
        YI_CURE_BASE_AMPLIFIER = BUILDER
                .comment("效果放大器 Effect amplifier")
                .translation(CONFIG_PREFIX + "yi_cure_amplifier")
                .defineInRange("yi_cure_amplifier", 0, 0, 254);
        BUILDER.pop();

        BUILDER.push(" 蠱 Gu-Parasite");
        GU_PARASITE_COOLDOWN_TIME = BUILDER
                .comment("冷却时间（刻） Cooldown (in ticks)")
                .translation(CONFIG_PREFIX + "gu_parasite_cooldown")
                .defineInRange("gu_parasite_cooldown", 400, 0, 72000);
        GU_PARASITE_BASE_DURATION = BUILDER
                .comment("效果基础持续时间（刻） Base effect duration (in ticks)")
                .translation(CONFIG_PREFIX + "gu_parasite_base_duration")
                .defineInRange("gu_parasite_base_duration", 240, 0, 72000);
        GU_PARASITE_BASE_AMPLIFIER = BUILDER
                .comment("效果放大器 Effect amplifier")
                .translation(CONFIG_PREFIX + "gu_parasite_amplifier")
                .defineInRange("gu_parasite_amplifier", 0, 0, 254);
        GU_PARASITE_PROJECTILE_VELOCITY_MAX = BUILDER
                .comment("弹射物最大蓄力速度加成 Maximum projectile charge velocity bonus",
                        "此配置控制玩家通过蓄力最多能使弹射物发射速度增加多少。",
                        "This configuration controls how much the player can increase the projectile's launch speed at most by charging.")
                .translation(CONFIG_PREFIX + "gu_parasite_velocity_bonus_max")
                .defineInRange("gu_parasite_velocity_bonus_max", 2.5, 0.0, 50.0);
        GU_PARASITE_PROJECTILE_VELOCITY_MIN = BUILDER
                .comment("弹射物最小速度 Minimum projectile velocity")
                .translation(CONFIG_PREFIX + "gu_parasite_velocity_min")
                .defineInRange("gu_parasite_velocity_min", 0.8, 0.0, 50.0);
        BUILDER.pop();

        BUILDER.push(" 磊 Lei-Stones");
        LEI_STONES_COOLDOWN_TIME = BUILDER
                .comment("冷却时间（刻） Cooldown (in ticks)")
                .translation(CONFIG_PREFIX + "lei_stones_cooldown")
                .defineInRange("lei_stones_cooldown", 20, 0, 72000);
        LEI_STONES_BASE_DAMAGE = BUILDER
                .comment("基础伤害 Base damage")
                .translation(CONFIG_PREFIX + "lei_stones_base_damage")
                .defineInRange("lei_stones_base_damage", 4.0f, 0, 999.0f);
        LEI_STONES_BASE_RADIUS = BUILDER
                .comment("基础范围伤害半径 Base AOE radius")
                .translation(CONFIG_PREFIX + "lei_stones_base_radius")
                .defineInRange("lei_stones_base_radius", 2f, 0, 15.0f);
        LEI_STONES_PROJECTILE_VELOCITY_MAX = BUILDER
                .comment("弹射物最大蓄力速度加成 Maximum projectile charge velocity bonus",
                        "此配置控制玩家通过蓄力最多能使弹射物发射速度增加多少。",
                        "This configuration controls how much the player can increase the projectile's launch speed at most by charging.")
                .translation(CONFIG_PREFIX + "lei_stones_velocity_bonus_max")
                .defineInRange("lei_stones_velocity_bonus_max", 2.3, 0.0, 50.0);
        LEI_STONES_PROJECTILE_VELOCITY_MIN = BUILDER
                .comment("弹射物最小速度 Minimum projectile velocity")
                .translation(CONFIG_PREFIX + "lei_stones_velocity_min")
                .defineInRange("lei_stones_velocity_min", 0.8, 0.0, 50.0);
        BUILDER.pop();

        BUILDER.pop();



        BUILDER.push("方块 Blocks");

        YOLIME_BLOCK_BOUNCE_VELOCITY_CAP = BUILDER
                .comment("悠酱块弹跳速度上限 Bounce velocity cap of yolime block")
                .translation(CONFIG_PREFIX + "yolime_block_bounce_velocity_cap")
                .defineInRange("yolime_block_bounce_velocity_cap", 66.6, 0.0, 999.0);
        GOLD_BRICKS_EFFECT = BUILDER
                .comment("金砖实际效果 Effect of gold bricks")
                .translation(CONFIG_PREFIX + "gold_bricks_effect")
                .define("gold_bricks_effect", true);

        BUILDER.pop();


        SPEC = BUILDER.build();
    }
}
