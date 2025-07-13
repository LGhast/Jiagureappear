package net.lghast.jiagu.utils;

import net.lghast.jiagu.item.CharacterItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.*;

public class CharacterInfo {
    public record CharacterData(float floatValue, String identifier) {}
    public record StructureData(CharacterStructure structure, List<String> components) {}

    public static final Map<String, StructureData> CHARACTER_COMPONENTS = new HashMap<>(Map.ofEntries(
            Map.entry("版", newData(CharacterStructure.HORIZONTAL,"片", "反")),
            Map.entry("才", newData(CharacterStructure.VERTICAL,"simple","屮")),
            Map.entry("材", newData(CharacterStructure.HORIZONTAL,"木", "才")),
            Map.entry("艸", newData(CharacterStructure.HORIZONTAL,"屮", "屮")),
            Map.entry("呈", newData(CharacterStructure.VERTICAL,"口", "土")),
            Map.entry("舂", newData(CharacterStructure.T_SHAPE,"又", "午", "又", "臼")),
            Map.entry("从", newData(CharacterStructure.HORIZONTAL,"人", "人")),
            Map.entry("錠", newData(CharacterStructure.HORIZONTAL,"金", "定")),
            Map.entry("定", newData(CharacterStructure.VERTICAL,"宀", "正")),
            Map.entry("屵", newData(CharacterStructure.VERTICAL,"山", "厂")),
            Map.entry("反", newData(CharacterStructure.HORIZONTAL,"厂", "又")),
            Map.entry("方", newData(CharacterStructure.VERTICAL,"simple", "刀")),
            Map.entry("敢", newData(CharacterStructure.TRIANGLE,"豕", "甘", "又")),
            Map.entry("甘", newData(CharacterStructure.VERTICAL,"口", "simple")),
            Map.entry("岡", newData(CharacterStructure.VERTICAL,"网", "山")),
            Map.entry("剛", newData(CharacterStructure.HORIZONTAL,"岡", "刀")),
            Map.entry("灰", newData(CharacterStructure.TRIANGLE_INVERT,"simple", "又", "火")),
            Map.entry("亼", newData(CharacterStructure.VERTICAL,"口", "reversal")),
            Map.entry("皆", newData(CharacterStructure.VERTICAL,"从", "曰")),
            Map.entry("階", newData(CharacterStructure.HORIZONTAL,"阜", "皆")),
            Map.entry("金", newData(CharacterStructure.TRIANGLE_RIGHT,"亼", "呂", "王")),
            Map.entry("林", newData(CharacterStructure.HORIZONTAL,"木", "木")),
            Map.entry("霝", newData(CharacterStructure.ARROW,"雨", "口", "口")),
            Map.entry("靈", newData(CharacterStructure.VERTICAL,"霝", "巫")),
            Map.entry("喦", newData(CharacterStructure.RHOMBUS,"口", "口", "口", "simple")),
            Map.entry("砂", newData(CharacterStructure.HORIZONTAL,"石", "少")),
            Map.entry("沙", newData(CharacterStructure.HORIZONTAL,"水", "少")),
            Map.entry("少", newData(CharacterStructure.VERTICAL,"小", "simple")),
            Map.entry("石", newData(CharacterStructure.HORIZONTAL,"厂", "口")),
            Map.entry("炭", newData(CharacterStructure.VERTICAL,"屵", "火")),
            Map.entry("鐵", newData(CharacterStructure.HORIZONTAL,"金", "鐵右")),
            Map.entry("銅", newData(CharacterStructure.HORIZONTAL,"金", "同")),
            Map.entry("同", newData(CharacterStructure.VERTICAL,"凡", "口")),
            Map.entry("嚴", newData(CharacterStructure.VERTICAL,"喦", "敢")),
            Map.entry("巖", newData(CharacterStructure.VERTICAL,"山", "嚴")),
            Map.entry("曰", newData(CharacterStructure.VERTICAL,"simple", "口")),
            Map.entry("乍", newData(CharacterStructure.VERTICAL,"simple", "耒")),
            Map.entry("正", newData(CharacterStructure.VERTICAL,"丁", "止")),
            Map.entry("鐵右", newData(CharacterStructure.TRIANGLE,"大", "呈", "戈")),
            Map.entry("之", newData(CharacterStructure.VERTICAL,"止", "simple")),
            Map.entry("樁", newData(CharacterStructure.HORIZONTAL,"木", "舂")),
            Map.entry("柞", newData(CharacterStructure.HORIZONTAL,"木", "乍")),
            Map.entry("生", newData(CharacterStructure.VERTICAL,"屮", "simple")),
            Map.entry("君", newData(CharacterStructure.VERTICAL,"尹", "口")),
            Map.entry("青", newData(CharacterStructure.VERTICAL,"生", "井")),
            Map.entry("群", newData(CharacterStructure.VERTICAL,"君", "羊")),
            Map.entry("央", newData(CharacterStructure.VERTICAL_LONG,"simple", "simple", "大")),
            Map.entry("戔", newData(CharacterStructure.HORIZONTAL_LONG,"戈", "reversal", "戈")),
            Map.entry("此", newData(CharacterStructure.HORIZONTAL_LONG,"止", "reversal", "人")),
            Map.entry("秧", newData(CharacterStructure.HORIZONTAL,"禾", "央")),
            Map.entry("淺", newData(CharacterStructure.HORIZONTAL,"水", "戔")),
            Map.entry("綫", newData(CharacterStructure.HORIZONTAL,"糸", "戔")),
            Map.entry("殘", newData(CharacterStructure.HORIZONTAL,"歺", "戔")),
            Map.entry("胔", newData(CharacterStructure.VERTICAL,"此", "肉")),
            Map.entry("森", newData(CharacterStructure.ARROW,"木", "木", "木")),
            Map.entry("焱", newData(CharacterStructure.ARROW,"火", "火", "火")),
            Map.entry("卒", newData(CharacterStructure.VERTICAL,"simple", "衣")),
            Map.entry("板", newData(CharacterStructure.HORIZONTAL,"木", "反")),
            Map.entry("檎", newData(CharacterStructure.HORIZONTAL,"木", "禽")),
            Map.entry("淬", newData(CharacterStructure.HORIZONTAL,"水", "卒")),
            Map.entry("染", newData(CharacterStructure.TRIANGLE_RIGHT,"九", "水", "木")),
            Map.entry("界", newData(CharacterStructure.VERTICAL,"田", "介")),
            Map.entry("炎", newData(CharacterStructure.VERTICAL,"火", "火")),
            Map.entry("尹", newData(CharacterStructure.HORIZONTAL,"simple", "又")),
            Map.entry("赤", newData(CharacterStructure.VERTICAL,"大", "火")),
            Map.entry("案", newData(CharacterStructure.VERTICAL,"安", "木")),
            Map.entry("安", newData(CharacterStructure.VERTICAL,"宀", "女")),
            Map.entry("墨", newData(CharacterStructure.VERTICAL,"黑", "土")),
            Map.entry("綠", newData(CharacterStructure.HORIZONTAL,"糸", "彔")),
            Map.entry("鈺", newData(CharacterStructure.HORIZONTAL,"金", "玉")),
            Map.entry("古", newData(CharacterStructure.VERTICAL,"甲", "口")),
            Map.entry("粒", newData(CharacterStructure.HORIZONTAL,"米", "立")),
            Map.entry("眇", newData(CharacterStructure.HORIZONTAL,"目", "少")),
            Map.entry("星", newData(CharacterStructure.VERTICAL,"晶", "生")),
            Map.entry("紫", newData(CharacterStructure.HORIZONTAL,"此", "糸")),
            Map.entry("立", newData(CharacterStructure.VERTICAL,"大", "simple")),
            Map.entry("二", newData(CharacterStructure.VERTICAL,"simple", "一")),
            Map.entry("三", newData(CharacterStructure.VERTICAL,"simple", "二")),
            Map.entry("骨", newData(CharacterStructure.VERTICAL,"冎", "肉")),
            Map.entry("祖", newData(CharacterStructure.HORIZONTAL,"示", "且")),
            Map.entry("黑", newData(CharacterStructure.ARROW_INVERT,"simple", "simple", "大")),
            Map.entry("母", newData(CharacterStructure.HORIZONTAL_LONG,"simple", "女", "simple")),
            Map.entry("斧", newData(CharacterStructure.HORIZONTAL_LONG,"父", "reversal", "斤")),
            Map.entry("鋤", newData(CharacterStructure.HORIZONTAL,"金", "助")),
            Map.entry("鎬", newData(CharacterStructure.HORIZONTAL,"金", "高")),
            Map.entry("劍", newData(CharacterStructure.HORIZONTAL,"金", "僉")),
            Map.entry("鍁", newData(CharacterStructure.HORIZONTAL,"金", "欣")),
            Map.entry("海", newData(CharacterStructure.HORIZONTAL,"水", "每")),
            Map.entry("欣", newData(CharacterStructure.HORIZONTAL,"斤", "欠")),
            Map.entry("助", newData(CharacterStructure.HORIZONTAL,"且", "力")),
            Map.entry("兄", newData(CharacterStructure.VERTICAL,"口", "人")),
            Map.entry("父", newData(CharacterStructure.BIAS,"simple", "又")),
            Map.entry("僉", newData(CharacterStructure.ARROW,"亼", "兄", "兄")),
            Map.entry("高", newData(CharacterStructure.VERTICAL,"complex", "口")),
            Map.entry("每", newData(CharacterStructure.VERTICAL,"complex", "女")),
            Map.entry("complex", newData(CharacterStructure.SQUARE,"simple", "simple", "simple", "simple")),
            Map.entry("般", newData(CharacterStructure.HORIZONTAL,"凡", "攴")),
            Map.entry("留", newData(CharacterStructure.VERTICAL,"卯", "田")),
            Map.entry("盤", newData(CharacterStructure.VERTICAL,"般", "皿")),
            Map.entry("攴", newData(CharacterStructure.TRIANGLE_RIGHT,"simple", "simple", "又")),
            Map.entry("殸", newData(CharacterStructure.TRIANGLE,"simple", "厂", "殳")),
            Map.entry("殳", newData(CharacterStructure.HORIZONTAL,"complex", "又")),
            Map.entry("陶", newData(CharacterStructure.TRIANGLE_RIGHT,"人", "阜", "人")),
            Map.entry("聲", newData(CharacterStructure.VERTICAL_LONG,"殸", "耳", "口")),
            Map.entry("亍", newData(CharacterStructure.HORIZONTAL,"彳", "reversal")),
            Map.entry("辵", newData(CharacterStructure.VERTICAL,"彳", "止")),
            Map.entry("段", newData(CharacterStructure.HORIZONTAL,"厂", "殳")),
            Map.entry("鍛", newData(CharacterStructure.HORIZONTAL,"金", "段")),
            Map.entry("廣", newData(CharacterStructure.VERTICAL,"广", "黃")),
            Map.entry("礦", newData(CharacterStructure.HORIZONTAL,"石", "廣")),
            Map.entry("莫", newData(CharacterStructure.VERTICAL_LONG,"艸", "日", "艸")),
            Map.entry("模", newData(CharacterStructure.HORIZONTAL,"木", "莫")),
            Map.entry("逆", newData(CharacterStructure.HORIZONTAL,"辵", "屰")),
            Map.entry("行", newData(CharacterStructure.HORIZONTAL,"彳", "亍")),
            Map.entry("屰", newData(CharacterStructure.VERTICAL,"大", "reversal")),
            Map.entry("黃", newData(CharacterStructure.VERTICAL,"大", "complex")),
            Map.entry("塊", newData(CharacterStructure.HORIZONTAL,"土", "鬼")),
            Map.entry("麥", newData(CharacterStructure.VERTICAL_LONG,"來", "止", "reversal")),
            Map.entry("雪", newData(CharacterStructure.VERTICAL,"雨", "羽")),
            Map.entry("桶", newData(CharacterStructure.HORIZONTAL,"木", "甬")),
            Map.entry("勇", newData(CharacterStructure.VERTICAL,"甬", "力")),
            Map.entry("湧", newData(CharacterStructure.HORIZONTAL,"水", "勇")),
            Map.entry("甬", newData(CharacterStructure.VERTICAL,"simple", "用")),
            Map.entry("炙", newData(CharacterStructure.VERTICAL,"肉", "火")),
            Map.entry("籽", newData(CharacterStructure.HORIZONTAL,"米", "子")),
            Map.entry("楷", newData(CharacterStructure.HORIZONTAL,"木", "皆")),
            Map.entry("旗", newData(CharacterStructure.VERTICAL,"㫃", "其")),
            Map.entry("朱", newData(CharacterStructure.HORIZONTAL,"simple", "木")),
            Map.entry("蛛", newData(CharacterStructure.HORIZONTAL,"虫", "朱")),
            Map.entry("冰", newData(CharacterStructure.HORIZONTAL,"仌", "水")),
            Map.entry("蟲", newData(CharacterStructure.ARROW,"虫", "虫", "虫")),
            Map.entry("從", newData(CharacterStructure.HORIZONTAL,"辵", "从")),
            Map.entry("骸", newData(CharacterStructure.HORIZONTAL,"骨", "亥")),
            Map.entry("檻", newData(CharacterStructure.HORIZONTAL,"木", "監")),
            Map.entry("監", newData(CharacterStructure.TRIANGLE_RIGHT,"目", "皿", "卩")),
            Map.entry("藍", newData(CharacterStructure.VERTICAL,"艸", "監")),
            Map.entry("蘭", newData(CharacterStructure.VERTICAL,"艸", "闌")),
            Map.entry("闌", newData(CharacterStructure.VERTICAL,"門", "柬")),
            Map.entry("深", newData(CharacterStructure.HORIZONTAL,"水", "罙")),
            Map.entry("罙", newData(CharacterStructure.VERTICAL,"穴", "又")),
            Map.entry("燚", newData(CharacterStructure.SQUARE,"火", "火", "火", "火")),
            Map.entry("終", newData(CharacterStructure.HORIZONTAL,"糸", "冬")),
            Map.entry("樅", newData(CharacterStructure.HORIZONTAL,"木", "從")),
            Map.entry("門", newData(CharacterStructure.HORIZONTAL,"戶", "戶")),
            Map.entry("靛", newData(CharacterStructure.HORIZONTAL,"青", "定")),
            Map.entry("緋", newData(CharacterStructure.HORIZONTAL,"糸", "非")),
            Map.entry("扉", newData(CharacterStructure.VERTICAL,"戶", "非")),
            Map.entry("莓", newData(CharacterStructure.VERTICAL,"艸", "每")),
            Map.entry("非", newData(CharacterStructure.HORIZONTAL_LONG,"人", "reversal", "人")),
            Map.entry("欠", newData(CharacterStructure.VERTICAL,"simple", "卩")),
            Map.entry("鬼", newData(CharacterStructure.VERTICAL,"complex", "卩")),
            Map.entry("沒", newData(CharacterStructure.VERTICAL,"回", "又")),
            Map.entry("葉", newData(CharacterStructure.VERTICAL,"艸", "枼")),
            Map.entry("玥", newData(CharacterStructure.HORIZONTAL,"玉", "月")),
            Map.entry("重", newData(CharacterStructure.HORIZONTAL_LONG,"東", "reversal", "人")),
            Map.entry("種", newData(CharacterStructure.HORIZONTAL,"禾", "重")),
            Map.entry("朝", newData(CharacterStructure.T_SHAPE_HORIZONTAL,"屮", "日", "月", "屮")),
            Map.entry("倉", newData(CharacterStructure.VERTICAL_LONG,"亼", "戶", "口")),
            Map.entry("蒼", newData(CharacterStructure.VERTICAL,"艸", "倉")),
            Map.entry("橙", newData(CharacterStructure.HORIZONTAL,"木", "登")),
            Map.entry("赬", newData(CharacterStructure.HORIZONTAL,"赤", "貞")),
            Map.entry("翠", newData(CharacterStructure.VERTICAL,"羽", "卒")),
            Map.entry("黛", newData(CharacterStructure.VERTICAL,"代", "黑")),
            Map.entry("代", newData(CharacterStructure.HORIZONTAL,"人", "弋")),
            Map.entry("登", newData(CharacterStructure.T_SHAPE,"止", "reversal", "止", "豆")),
            Map.entry("荳", newData(CharacterStructure.VERTICAL,"艸", "豆")),
            Map.entry("丐", newData(CharacterStructure.HORIZONTAL,"亡", "刀")),
            Map.entry("褐", newData(CharacterStructure.HORIZONTAL,"衣", "曷")),
            Map.entry("曷", newData(CharacterStructure.VERTICAL,"日", "丐")),
            Map.entry("紅", newData(CharacterStructure.HORIZONTAL,"糸", "工")),
            Map.entry("渴", newData(CharacterStructure.HORIZONTAL,"水", "曷")),
            Map.entry("縹", newData(CharacterStructure.HORIZONTAL,"糸", "票")),
            Map.entry("票", newData(CharacterStructure.T_SHAPE,"又", "囟", "又", "火")),
            Map.entry("英", newData(CharacterStructure.VERTICAL,"艸", "央")),
            Map.entry("貞", newData(CharacterStructure.VERTICAL,"卜", "鼎")),
            Map.entry("介", newData(CharacterStructure.HORIZONTAL_LONG,"simple", "人", "simple")),
            Map.entry("床", newData(CharacterStructure.HORIZONTAL,"爿", "木")),
            Map.entry("戒", newData(CharacterStructure.T_SHAPE_INVERT,"戈", "又", "reversal", "又")),
            Map.entry("械", newData(CharacterStructure.HORIZONTAL,"木", "戒")),
            Map.entry("機", newData(CharacterStructure.HORIZONTAL,"木", "幾")),
            Map.entry("戎", newData(CharacterStructure.HORIZONTAL,"甲", "戈")),
            Map.entry("戍", newData(CharacterStructure.HORIZONTAL,"人", "戈")),
            Map.entry("幾", newData(CharacterStructure.ARROW_INVERT,"幺", "幺", "戍")),
            Map.entry("爿", newData(CharacterStructure.HORIZONTAL,"reversal", "片")),
            Map.entry("制", newData(CharacterStructure.HORIZONTAL,"木", "刀")),
            Map.entry("製", newData(CharacterStructure.VERTICAL,"制", "衣")),
            Map.entry("奴", newData(CharacterStructure.HORIZONTAL,"女", "又")),
            Map.entry("探", newData(CharacterStructure.HORIZONTAL,"手", "罙")),
            Map.entry("弩", newData(CharacterStructure.VERTICAL,"奴", "弓")),
            Map.entry("㭜", newData(CharacterStructure.HORIZONTAL,"木", "戎")),
            Map.entry("櫻", newData(CharacterStructure.HORIZONTAL,"木", "嬰")),
            Map.entry("嬰", newData(CharacterStructure.ARROW_INVERT,"貝", "貝", "女")),
            Map.entry("兌", newData(CharacterStructure.ARROW_INVERT,"simple", "simple", "兄")),
            Map.entry("銳", newData(CharacterStructure.HORIZONTAL,"金", "兌")),
            Map.entry("殼", newData(CharacterStructure.HORIZONTAL,"complex", "殳")),
            Map.entry("扡", newData(CharacterStructure.HORIZONTAL,"手", "也")),
            Map.entry("既", newData(CharacterStructure.HORIZONTAL,"complex", "旡")),
            Map.entry("旡", newData(CharacterStructure.HORIZONTAL,"欠", "reversal")),
            Map.entry("叉", newData(CharacterStructure.VERTICAL,"simple", "又")),
            Map.entry("旦", newData(CharacterStructure.VERTICAL,"日", "simple")),
            Map.entry("戟", newData(CharacterStructure.TRIANGLE,"㫃", "旦", "戈")),
            Map.entry("樺", newData(CharacterStructure.HORIZONTAL,"木", "華")),
            Map.entry("爐", newData(CharacterStructure.HORIZONTAL,"火", "盧")),
            Map.entry("苗", newData(CharacterStructure.VERTICAL,"艸", "田")),
            Map.entry("貓", newData(CharacterStructure.HORIZONTAL,"豸", "苗")),
            Map.entry("潮", newData(CharacterStructure.HORIZONTAL,"水", "朝")),
            Map.entry("罔", newData(CharacterStructure.VERTICAL,"网", "亡")),
            Map.entry("網", newData(CharacterStructure.HORIZONTAL,"糸", "罔")),
            Map.entry("崗", newData(CharacterStructure.VERTICAL,"山", "岡")),
            Map.entry("燈", newData(CharacterStructure.HORIZONTAL,"火", "登")),
            Map.entry("善", newData(CharacterStructure.ARROW,"羊", "言", "言")),
            Map.entry("舌", newData(CharacterStructure.VERTICAL,"complex", "口")),
            Map.entry("言", newData(CharacterStructure.VERTICAL,"simple", "舌")),
            Map.entry("修", newData(CharacterStructure.VERTICAL,"攸", "彡")),
            Map.entry("鸚", newData(CharacterStructure.HORIZONTAL,"嬰", "鳥")),
            Map.entry("攸", newData(CharacterStructure.HORIZONTAL,"人", "攴")),
            Map.entry("埴", newData(CharacterStructure.HORIZONTAL,"土", "直")),
            Map.entry("悠", newData(CharacterStructure.VERTICAL,"攸", "心")),
            Map.entry("直", newData(CharacterStructure.VERTICAL,"simple", "目")),
            Map.entry("圭", newData(CharacterStructure.VERTICAL,"土", "土")),
            Map.entry("雞", newData(CharacterStructure.HORIZONTAL,"奚", "鳥")),
            Map.entry("鼃", newData(CharacterStructure.VERTICAL,"圭", "黽")),
            Map.entry("奚", newData(CharacterStructure.TRIANGLE_RIGHT_INVERT,"爪", "糸", "大")),
            Map.entry("蕈", newData(CharacterStructure.VERTICAL,"艸", "覃")),
            Map.entry("字", newData(CharacterStructure.VERTICAL,"宀", "子")),
            Map.entry("璧", newData(CharacterStructure.VERTICAL,"辟", "玉")),
            Map.entry("辟", newData(CharacterStructure.HORIZONTAL,"卩", "辛")),
            Map.entry("容", newData(CharacterStructure.VERTICAL,"穴", "simple")),
            Map.entry("閃", newData(CharacterStructure.VERTICAL,"門", "人")),
            Map.entry("熔", newData(CharacterStructure.HORIZONTAL,"火", "容")),
            Map.entry("完", newData(CharacterStructure.VERTICAL,"宀", "元")),
            Map.entry("箱", newData(CharacterStructure.VERTICAL,"竹", "相")),
            Map.entry("相", newData(CharacterStructure.HORIZONTAL,"木", "目")),
            Map.entry("翌", newData(CharacterStructure.HORIZONTAL,"立", "翼")),
            Map.entry("桿", newData(CharacterStructure.HORIZONTAL,"木", "旱")),
            Map.entry("胡", newData(CharacterStructure.HORIZONTAL,"古", "肉")),
            Map.entry("旱", newData(CharacterStructure.VERTICAL,"日", "干")),
            Map.entry("蘆", newData(CharacterStructure.VERTICAL,"艸", "盧")),
            Map.entry("洋", newData(CharacterStructure.HORIZONTAL,"水", "羊")),
            Map.entry("芋", newData(CharacterStructure.VERTICAL,"艸", "于")),
            Map.entry("黯", newData(CharacterStructure.HORIZONTAL,"黑", "音")),
            Map.entry("雕", newData(CharacterStructure.HORIZONTAL,"周", "隹")),
            Map.entry("糕", newData(CharacterStructure.HORIZONTAL,"米", "羔")),
            Map.entry("羔", newData(CharacterStructure.HORIZONTAL_LONG,"simple", "羊", "simple")),
            Map.entry("羹", newData(CharacterStructure.VERTICAL,"羔", "鬲")),
            Map.entry("瑚", newData(CharacterStructure.HORIZONTAL,"玉", "胡")),
            Map.entry("韁", newData(CharacterStructure.HORIZONTAL,"革", "畺")),
            Map.entry("殭", newData(CharacterStructure.HORIZONTAL,"歹", "畺")),
            Map.entry("狼", newData(CharacterStructure.HORIZONTAL,"良", "犬")),
            Map.entry("珊", newData(CharacterStructure.HORIZONTAL,"玉", "冊")),
            Map.entry("屍", newData(CharacterStructure.VERTICAL,"尸", "死")),
            Map.entry("死", newData(CharacterStructure.HORIZONTAL,"歹", "人")),
            Map.entry("音", newData(CharacterStructure.VERTICAL,"言", "simple")),
            Map.entry("柵", newData(CharacterStructure.HORIZONTAL,"木", "冊")),
            Map.entry("椎", newData(CharacterStructure.HORIZONTAL,"木", "隹")),
            Map.entry("奪", newData(CharacterStructure.L_SHAPE,"衣", "小", "隹", "又")),
            Map.entry("畺", newData(CharacterStructure.T_SHAPE_LONG,"simple", "simple", "simple", "田", "田")),
            Map.entry("猋", newData(CharacterStructure.ARROW,"犬", "犬", "犬")),
            Map.entry("飆", newData(CharacterStructure.HORIZONTAL,"猋", "風")),
            Map.entry("贆", newData(CharacterStructure.HORIZONTAL,"貝", "猋")),
            Map.entry("風", newData(CharacterStructure.HORIZONTAL,"鳳", "凡")),
            Map.entry("竿", newData(CharacterStructure.VERTICAL,"竹", "干")),
            Map.entry("枯", newData(CharacterStructure.HORIZONTAL,"木", "古")),
            Map.entry("魄", newData(CharacterStructure.HORIZONTAL,"白", "鬼")),
            Map.entry("扇", newData(CharacterStructure.VERTICAL,"戶", "羽")),
            Map.entry("物", newData(CharacterStructure.VERTICAL,"牛", "勿")),
            Map.entry("核", newData(CharacterStructure.VERTICAL,"木", "亥")),
            Map.entry("洄", newData(CharacterStructure.VERTICAL,"水", "回")),
            Map.entry("饢", newData(CharacterStructure.VERTICAL,"食", "囊")),
            Map.entry("鮭", newData(CharacterStructure.VERTICAL,"魚", "圭")),
            Map.entry("鱈", newData(CharacterStructure.VERTICAL,"魚", "雪")),
            Map.entry("囊", newData(CharacterStructure.T_SHAPE_INVERT,"橐", "土", "complex", "又")),
            Map.entry("勿", newData(CharacterStructure.T_SHAPE_HORIZONTAL,"simple", "simple", "刀", "simple")),
            Map.entry("寒", newData(CharacterStructure.RECTANGLE, "simple", "宀", "simple", "simple", "人", "simple")),
            Map.entry("幺", newData(CharacterStructure.SAME,"玄")),
            Map.entry("玄", newData(CharacterStructure.SAME,"幺"))

            //No Textures
            /*

           */
    ));

    private static List<String> componentList(String... components){
        List<String> list = new ArrayList<>();
        Collections.addAll(list, components);
        return list;
    }

    private static StructureData newData(CharacterStructure structure, String... components){
        return new StructureData(structure,componentList(components));
    }

    public static List<String> getComponents(String inscription){
        StructureData data = CHARACTER_COMPONENTS.get(inscription);
        if (data == null) return null;
        return data.components;
    }

    public static CharacterStructure getStructure(String inscription){
        StructureData data = CHARACTER_COMPONENTS.get(inscription);
        if (data == null) return null;
        return data.structure;
    }

    public static String getCharactersAssemblable(String searchValue){
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, StructureData> entry : CHARACTER_COMPONENTS.entrySet()) {
            String key = entry.getKey();
            List<String> valueList = entry.getValue().components;
            if(valueList == null) continue;
            if (valueList.contains(searchValue)) {
                result.append(key).append(" ");
            }
        }
        return ModUtils.insertLineBreaks(result.toString().trim(), 14);
    }

    public static String getCharactersAssemblableContinuous(String searchValue){
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, StructureData> entry : CHARACTER_COMPONENTS.entrySet()) {
            String key = entry.getKey();
            if(key.length()>1) continue;
            List<String> valueList = entry.getValue().components;
            if(valueList == null) continue;
            if (valueList.contains(searchValue)) {
                result.append(key);
            }
        }
        return result.toString();
    }

    public static String getCharactersDisassemblable(String searchValue){
        List<String> list = CharacterInfo.getComponents(searchValue);
        if(list == null || list.isEmpty()) return null;
        StringBuilder stringBuilder = new StringBuilder();
        for(String component : list){
            if(component.length()==1){
                stringBuilder.append(component).append("+");
                continue;
            }
            String finalComponent = switch (component) {
                case CharacterItem.SIMPLE_INSCRIPTION ->
                        Component.translatable("tooltip.jiagureappear.character_item_simple_simplified").getString();
                case CharacterItem.COMPLEX_INSCRIPTION ->
                        Component.translatable("tooltip.jiagureappear.character_item_complex_simplified").getString();
                case CharacterItem.REVERSAL_INSCRIPTION ->
                        Component.translatable("tooltip.jiagureappear.character_item_reversal_simplified").getString();
                default -> component;
            };
            stringBuilder.append(finalComponent).append("+");
        }
        stringBuilder.deleteCharAt(stringBuilder.length()-1);
        return stringBuilder.toString();
    }

    public static final Map<String, CharacterData> CHARACTER_DATA = new HashMap<>(Map.ofEntries(
            //Stone Level
            Map.entry("土", new CharacterData(1.0f, "tu_soil")),
            Map.entry("水", new CharacterData(2.0f, "shui_water")),
            Map.entry("屮", new CharacterData(3.0f, "cao_grass")),
            Map.entry("木", new CharacterData(4.0f, "mu_tree")),
            Map.entry("口", new CharacterData(5.0f, "kou_mouth")),
            Map.entry("人", new CharacterData(6.0f, "ren_human")),
            Map.entry("又", new CharacterData(7.0f, "you_hand")),
            Map.entry("山", new CharacterData(8.0f, "shan_mountain")),
            Map.entry("豕", new CharacterData(9.0f, "shi_pig")),
            Map.entry("甘", new CharacterData(10.0f, "gan_sweet")),
            Map.entry("午", new CharacterData(11.0f, "wu_pestle")),
            Map.entry("乍", new CharacterData(12.0f, "zha_sudden")),
            Map.entry("耒", new CharacterData(13.0f, "lei_plough")),
            Map.entry("臼", new CharacterData(14.0f, "jiu_mortar")),
            Map.entry("才", new CharacterData(15.0f, "cai_talent")),
            Map.entry("呂", new CharacterData(16.0f, "lv_metalstack")),
            Map.entry("王", new CharacterData(17.0f, "wang_king")),
            Map.entry("亼", new CharacterData(18.0f, "ji_mouth")),
            Map.entry("止", new CharacterData(19.0f, "zhi_toe")),
            Map.entry("丁", new CharacterData(20.0f, "ding_nail")),
            Map.entry("宀", new CharacterData(21.0f, "mian_house")),
            Map.entry("刀", new CharacterData(22.0f, "dao_knife")),
            Map.entry("方", new CharacterData(23.0f, "fang_square")),
            Map.entry("戈", new CharacterData(24.0f, "ge_daggeraxe")),
            Map.entry("大", new CharacterData(25.0f, "da_big")),
            Map.entry("厂", new CharacterData(26.0f, "han_cliff")),
            Map.entry("火", new CharacterData(27.0f, "huo_fire")),
            Map.entry("戶", new CharacterData(28.0f, "hu_door")),
            Map.entry("片", new CharacterData(29.0f, "pian_slice")),
            Map.entry("小", new CharacterData(30.0f, "xiao_small")),
            Map.entry("少", new CharacterData(31.0f, "shao_few")),
            Map.entry("凡", new CharacterData(32.0f, "fan_normal")),
            Map.entry("曰", new CharacterData(33.0f, "yue_speak")),
            Map.entry("阜", new CharacterData(34.0f, "fu_mount")),
            Map.entry("网", new CharacterData(35.0f, "wang_net")),
            Map.entry("之", new CharacterData(36.0f, "zhi_goto")),
            Map.entry("雨", new CharacterData(37.0f, "yu_rain")),
            Map.entry("巫", new CharacterData(38.0f, "wu_wizard")),
            Map.entry("羊", new CharacterData(39.0f, "yang_goat")),
            Map.entry("井", new CharacterData(40.0f, "jing_well")),
            Map.entry("生", new CharacterData(41.0f, "sheng_birth")),
            Map.entry("尹", new CharacterData(42.0f, "yin_manage")),
            Map.entry("禾", new CharacterData(43.0f, "he_crops")),
            Map.entry("糸", new CharacterData(44.0f, "mi_silk")),
            Map.entry("肉", new CharacterData(45.0f, "rou_meat")),
            Map.entry("央", new CharacterData(46.0f, "yang_center")),
            Map.entry("禽", new CharacterData(47.0f, "qin_bird")),
            Map.entry("衣", new CharacterData(48.0f, "yi_clothes")),
            Map.entry("卒", new CharacterData(49.0f, "zu_end")),
            Map.entry("歺", new CharacterData(50.0f, "e_bone")),
            Map.entry("九", new CharacterData(51.0f, "jiu_nine")),
            Map.entry("介", new CharacterData(52.0f, "jie_medium")),
            Map.entry("田", new CharacterData(53.0f, "tian_field")),
            Map.entry("女", new CharacterData(54.0f, "nv_female")),
            Map.entry("玉", new CharacterData(55.0f, "yu_jade")),
            Map.entry("玄", new CharacterData(56.0f, "xuan_deep")),
            Map.entry("黑", new CharacterData(57.0f, "hei_black")),
            Map.entry("彔", new CharacterData(58.0f, "lu_winch")),
            Map.entry("甲", new CharacterData(59.0f, "jia_armour")),
            Map.entry("晶", new CharacterData(60.0f, "jing_stars")),
            Map.entry("立", new CharacterData(61.0f, "li_stand")),
            Map.entry("米", new CharacterData(62.0f, "mi_rice")),
            Map.entry("上", new CharacterData(63.0f, "shang_up")),
            Map.entry("十", new CharacterData(64.0f, "shi_ten")),
            Map.entry("下", new CharacterData(65.0f, "xia_down")),
            Map.entry("目", new CharacterData(66.0f, "mu_eye")),
            Map.entry("冎", new CharacterData(67.0f, "gua_bone")),
            Map.entry("母", new CharacterData(68.0f, "mu_mum")),
            Map.entry("且", new CharacterData(69.0f, "qie_and")),
            Map.entry("示", new CharacterData(70.0f, "shi_show")),
            Map.entry("一", new CharacterData(71.0f, "yi_one")),
            Map.entry("二", new CharacterData(72.0f, "er_two")),
            Map.entry("三", new CharacterData(73.0f, "san_three")),
            Map.entry("父", new CharacterData(74.0f, "fu_father")),
            Map.entry("高", new CharacterData(75.0f, "gao_high")),
            Map.entry("斤", new CharacterData(76.0f, "jin_axe")),
            Map.entry("力", new CharacterData(77.0f, "li_strength")),
            Map.entry("每", new CharacterData(78.0f, "mei_each")),
            Map.entry("欠", new CharacterData(79.0f, "qian_yawn")),
            Map.entry("耳", new CharacterData(80.0f, "er_ear")),
            Map.entry("卯", new CharacterData(81.0f, "mao_mao")),
            Map.entry("皿", new CharacterData(82.0f, "min_vessel")),
            Map.entry("攴", new CharacterData(83.0f, "pu_tap")),
            Map.entry("殳", new CharacterData(84.0f, "shu_weapon")),
            Map.entry("四", new CharacterData(85.0f, "si_four")),
            Map.entry("五", new CharacterData(86.0f, "wu_five")),
            Map.entry("彳", new CharacterData(87.0f, "chi_walk")),
            Map.entry("亍", new CharacterData(88.0f, "chu_walk")),
            Map.entry("黃", new CharacterData(89.0f, "huang_yellow")),
            Map.entry("屰", new CharacterData(90.0f, "ni_reverse")),
            Map.entry("日", new CharacterData(91.0f, "ri_sun")),
            Map.entry("广", new CharacterData(92.0f, "yan_house")),
            Map.entry("鬼", new CharacterData(93.0f, "gui_ghost")),
            Map.entry("來", new CharacterData(94.0f, "lai_come")),
            Map.entry("甬", new CharacterData(95.0f, "yong_bell")),
            Map.entry("用", new CharacterData(96.0f, "yong_use")),
            Map.entry("羽", new CharacterData(97.0f, "yu_feather")),
            Map.entry("子", new CharacterData(98.0f, "zi_kid")),
            Map.entry("革", new CharacterData(99.0f, "ge_leather")),
            Map.entry("龜", new CharacterData(100.0f, "gui_turtle")),
            Map.entry("虫", new CharacterData(101.0f, "hui_snake")),
            Map.entry("馬", new CharacterData(102.0f, "ma_horse")),
            Map.entry("其", new CharacterData(103.0f, "qi_it")),
            Map.entry("犬", new CharacterData(104.0f, "quan_dog")),
            Map.entry("心", new CharacterData(105.0f, "xin_heart")),
            Map.entry("㫃", new CharacterData(106.0f, "yan_flag")),
            Map.entry("朱", new CharacterData(107.0f, "zhu_red")),
            Map.entry("仌", new CharacterData(108.0f, "bing_ice")),
            Map.entry("冬", new CharacterData(109.0f, "dong_winter")),
            Map.entry("亥", new CharacterData(110.0f, "hai_hai")),
            Map.entry("柬", new CharacterData(111.0f, "jian_note")),
            Map.entry("穴", new CharacterData(112.0f, "xue_cave")),
            Map.entry("東", new CharacterData(113.0f, "dong_east")),
            Map.entry("回", new CharacterData(114.0f, "hui_return")),
            Map.entry("枼", new CharacterData(115.0f, "ye_leaf")),
            Map.entry("月", new CharacterData(116.0f, "yue_moon")),
            Map.entry("白", new CharacterData(117.0f, "bai_white")),
            Map.entry("卜", new CharacterData(118.0f, "bu_divination")),
            Map.entry("鼎", new CharacterData(119.0f, "ding_ding")),
            Map.entry("豆", new CharacterData(120.0f, "dou_vessel")),
            Map.entry("弓", new CharacterData(121.0f, "gong_bow")),
            Map.entry("工", new CharacterData(122.0f, "gong_work")),
            Map.entry("亡", new CharacterData(123.0f, "wang_escape")),
            Map.entry("囟", new CharacterData(124.0f, "xin_fontanel")),
            Map.entry("弋", new CharacterData(125.0f, "yi_arrow")),
            Map.entry("爿", new CharacterData(126.0f, "pan_wall")),
            Map.entry("幺", new CharacterData(127.0f, "yao_small")),
            Map.entry("貝", new CharacterData(128.0f, "bei_shell")),
            Map.entry("手", new CharacterData(129.0f, "shou_hand")),
            Map.entry("叉", new CharacterData(130.0f, "cha_fork")),
            Map.entry("旦", new CharacterData(131.0f, "dan_morning")),
            Map.entry("華", new CharacterData(132.0f, "hua_flower")),
            Map.entry("旡", new CharacterData(133.0f, "ji_burp")),
            Map.entry("盧", new CharacterData(134.0f, "lu_black")),
            Map.entry("也", new CharacterData(135.0f, "ye_too")),
            Map.entry("豸", new CharacterData(136.0f, "zhi_beast")),
            Map.entry("鳥", new CharacterData(137.0f, "niao_bird")),
            Map.entry("彡", new CharacterData(138.0f, "shan_fur")),
            Map.entry("舌", new CharacterData(139.0f, "she_tongue")),
            Map.entry("言", new CharacterData(140.0f, "yan_speak")),
            Map.entry("直", new CharacterData(141.0f, "zhi_straight")),
            Map.entry("黽", new CharacterData(142.0f, "meng_frog")),
            Map.entry("覃", new CharacterData(143.0f, "tan_saltbag")),
            Map.entry("爪", new CharacterData(144.0f, "zhua_claw")),
            Map.entry("卩", new CharacterData(145.0f, "jie_kneel")),
            Map.entry("毛", new CharacterData(146.0f, "mao_hair")),
            Map.entry("辛", new CharacterData(147.0f, "xin_spicy")),
            Map.entry("翼", new CharacterData(148.0f, "yi_wing")),
            Map.entry("元", new CharacterData(149.0f, "yuan_one")),
            Map.entry("竹", new CharacterData(150.0f, "zhu_bamboo")),
            Map.entry("容", new CharacterData(151.0f, "rong_contain")),
            Map.entry("干", new CharacterData(152.0f, "gan_shield")),
            Map.entry("于", new CharacterData(153.0f, "yu_yu")),
            Map.entry("冊", new CharacterData(154.0f, "ce_books")),
            Map.entry("歹", new CharacterData(155.0f, "dai_die")),
            Map.entry("羔", new CharacterData(156.0f, "gao_lamb")),
            Map.entry("鬲", new CharacterData(157.0f, "ge_vessel")),
            Map.entry("良", new CharacterData(158.0f, "liang_good")),
            Map.entry("尸", new CharacterData(159.0f, "shi_human")),
            Map.entry("音", new CharacterData(160.0f, "yin_sound")),
            Map.entry("周", new CharacterData(161.0f, "zhou_zhou")),
            Map.entry("隹", new CharacterData(162.0f, "zhui_bird")),
            Map.entry("鳳", new CharacterData(163.0f, "feng_phoenix")),
            Map.entry("瓜", new CharacterData(164.0f, "gua_melon")),
            Map.entry("南", new CharacterData(165.0f, "nan_south")),
            Map.entry("牛", new CharacterData(166.0f, "niu_ox")),
            Map.entry("矢", new CharacterData(167.0f, "shi_arrow")),
            Map.entry("世", new CharacterData(168.0f, "shi_world")),
            Map.entry("勿", new CharacterData(169.0f, "wu_not")),
            Map.entry("食", new CharacterData(170.0f, "shi_food")),
            Map.entry("橐", new CharacterData(171.0f, "tuo_pocket")),
            Map.entry("魚", new CharacterData(172.0f, "yu_fish")),


            //Iron Level
            Map.entry("林", new CharacterData(1000.0f, "lin_woods")),
            Map.entry("从", new CharacterData(1001.0f, "cong_follow")),
            Map.entry("艸", new CharacterData(1002.0f, "cao_herbs")),
            Map.entry("喦", new CharacterData(1003.0f, "nie_talkative")),
            Map.entry("敢", new CharacterData(1004.0f, "gan_brave")),
            Map.entry("柞", new CharacterData(1005.0f, "zuo_oak")),
            Map.entry("舂", new CharacterData(1006.0f, "chong_pestle")),
            Map.entry("材", new CharacterData(1007.0f, "cai_timber")),
            Map.entry("金", new CharacterData(1008.0f, "jin_metal")),
            Map.entry("正", new CharacterData(1009.0f, "zheng_justice")),
            Map.entry("呈", new CharacterData(1010.0f, "cheng_submit")),
            Map.entry("屵", new CharacterData(1011.0f, "e_cliff")),
            Map.entry("反", new CharacterData(1012.0f, "fan_reverse")),
            Map.entry("石", new CharacterData(1013.0f, "shi_stone")),
            Map.entry("沙", new CharacterData(1014.0f, "sha_sand")),
            Map.entry("同", new CharacterData(1015.0f, "tong_same")),
            Map.entry("岡", new CharacterData(1016.0f, "gang_mount")),
            Map.entry("灰", new CharacterData(1017.0f, "hui_ash")),
            Map.entry("霝", new CharacterData(1018.0f, "ling_rain")),
            Map.entry("君", new CharacterData(1019.0f, "jun_ruler")),
            Map.entry("青", new CharacterData(1020.0f, "qing_cyan")),
            Map.entry("戔", new CharacterData(1021.0f, "can_damage")),
            Map.entry("此", new CharacterData(1022.0f, "ci_this")),
            Map.entry("秧", new CharacterData(1023.0f, "yang_seedling")),
            Map.entry("森", new CharacterData(1024.0f, "sen_forest")),
            Map.entry("檎", new CharacterData(1025.0f, "qin_apple")),
            Map.entry("淬", new CharacterData(1026.0f, "cui_quench")),
            Map.entry("染", new CharacterData(1027.0f, "ran_dye")),
            Map.entry("界", new CharacterData(1028.0f, "jie_scope")),
            Map.entry("炎", new CharacterData(1029.0f, "yan_hot")),
            Map.entry("赤", new CharacterData(1030.0f, "chi_red")),
            Map.entry("安", new CharacterData(1031.0f, "an_safe")),
            Map.entry("墨", new CharacterData(1032.0f, "mo_ink")),
            Map.entry("綠", new CharacterData(1033.0f, "lv_green")),
            Map.entry("焱", new CharacterData(1034.0f, "yan_flames")),
            Map.entry("古", new CharacterData(1035.0f, "gu_ancient")),
            Map.entry("粒", new CharacterData(1036.0f, "li_grain")),
            Map.entry("星", new CharacterData(1037.0f, "xing_star")),
            Map.entry("眇", new CharacterData(1038.0f, "miao_blindeye")),
            Map.entry("骨", new CharacterData(1039.0f, "gu_bone")),
            Map.entry("祖", new CharacterData(1040.0f, "zu_ancestor")),
            Map.entry("斧", new CharacterData(1041.0f, "fu_axe")),
            Map.entry("海", new CharacterData(1042.0f, "hai_sea")),
            Map.entry("欣", new CharacterData(1043.0f, "xin_happy")),
            Map.entry("兄", new CharacterData(1044.0f, "xiong_brother")),
            Map.entry("助", new CharacterData(1045.0f, "zhu_help")),
            Map.entry("般", new CharacterData(1046.0f, "ban_plate")),
            Map.entry("留", new CharacterData(1047.0f, "liu_leave")),
            Map.entry("殸", new CharacterData(1048.0f, "qing_qing")),
            Map.entry("陶", new CharacterData(1049.0f, "tao_pottery")),
            Map.entry("辵", new CharacterData(1050.0f, "chuo_walk")),
            Map.entry("段", new CharacterData(1051.0f, "duan_section")),
            Map.entry("廣", new CharacterData(1052.0f, "guang_vast")),
            Map.entry("行", new CharacterData(1053.0f, "xing_walk")),
            Map.entry("塊", new CharacterData(1054.0f, "kuai_block")),
            Map.entry("麥", new CharacterData(1055.0f, "mai_wheat")),
            Map.entry("桶", new CharacterData(1056.0f, "tong_barrel")),
            Map.entry("雪", new CharacterData(1057.0f, "xue_snow")),
            Map.entry("勇", new CharacterData(1058.0f, "yong_brave")),
            Map.entry("炙", new CharacterData(1059.0f, "zhi_roast")),
            Map.entry("籽", new CharacterData(1060.0f, "zi_seed")),
            Map.entry("旗", new CharacterData(1061.0f, "qi_flag")),
            Map.entry("蛛", new CharacterData(1062.0f, "zhu_spider")),
            Map.entry("冰", new CharacterData(1063.0f, "bing_ices")),
            Map.entry("蟲", new CharacterData(1064.0f, "chong_insect")),
            Map.entry("監", new CharacterData(1065.0f, "jian_monitor")),
            Map.entry("門", new CharacterData(1066.0f, "men_door")),
            Map.entry("罙", new CharacterData(1067.0f, "shen_shen")),
            Map.entry("燚", new CharacterData(1068.0f, "yi_conflagrant")),
            Map.entry("終", new CharacterData(1069.0f, "zhong_end")),
            Map.entry("非", new CharacterData(1070.0f, "fei_not")),
            Map.entry("沒", new CharacterData(1071.0f, "mo_sink")),
            Map.entry("玥", new CharacterData(1072.0f, "yue_pearl")),
            Map.entry("朝", new CharacterData(1073.0f, "zhao_morning")),
            Map.entry("重", new CharacterData(1074.0f, "zhong_heavy")),
            Map.entry("倉", new CharacterData(1075.0f, "cang_granary")),
            Map.entry("翠", new CharacterData(1076.0f, "cui_green")),
            Map.entry("代", new CharacterData(1077.0f, "dai_generation")),
            Map.entry("登", new CharacterData(1078.0f, "deng_climb")),
            Map.entry("丐", new CharacterData(1079.0f, "gai_beggar")),
            Map.entry("紅", new CharacterData(1080.0f, "hong_red")),
            Map.entry("票", new CharacterData(1081.0f, "piao_ticket")),
            Map.entry("貞", new CharacterData(1082.0f, "zhen_ding")),
            Map.entry("床", new CharacterData(1083.0f, "chuang_bed")),
            Map.entry("戒", new CharacterData(1084.0f, "jie_guard")),
            Map.entry("戍", new CharacterData(1085.0f, "shu_guard")),
            Map.entry("罔", new CharacterData(1086.0f, "wang_deceive")),
            Map.entry("制", new CharacterData(1087.0f, "zhi_policy")),
            Map.entry("奴", new CharacterData(1088.0f, "nu_slave")),
            Map.entry("戎", new CharacterData(1089.0f, "rong_weapon")),
            Map.entry("嬰", new CharacterData(1090.0f, "ying_baby")),
            Map.entry("扡", new CharacterData(1091.0f, "chi_strip")),
            Map.entry("兌", new CharacterData(1092.0f, "dui_convert")),
            Map.entry("樺", new CharacterData(1093.0f, "hua_birch")),
            Map.entry("既", new CharacterData(1094.0f, "ji_already")),
            Map.entry("戟", new CharacterData(1095.0f, "ji_halberd")),
            Map.entry("殼", new CharacterData(1096.0f, "ke_shell")),
            Map.entry("爐", new CharacterData(1097.0f, "lu_stove")),
            Map.entry("善", new CharacterData(1098.0f, "shan_kind")),
            Map.entry("攸", new CharacterData(1099.0f, "you_flow")),
            Map.entry("埴", new CharacterData(1100.0f, "zhi_clay")),
            Map.entry("圭", new CharacterData(1101.0f, "gui_jade")),
            Map.entry("奚", new CharacterData(1102.0f, "xi_slave")),
            Map.entry("字", new CharacterData(1103.0f, "zi_character")),
            Map.entry("辟", new CharacterData(1104.0f, "pi_break")),
            Map.entry("完", new CharacterData(1105.0f, "wan_finish")),
            Map.entry("相", new CharacterData(1106.0f, "xiang_phase")),
            Map.entry("翌", new CharacterData(1107.0f, "yi_tomorrow")),
            Map.entry("熔", new CharacterData(1108.0f, "rong_melt")),
            Map.entry("旱", new CharacterData(1109.0f, "han_dry")),
            Map.entry("洋", new CharacterData(1110.0f, "yang_ocean")),
            Map.entry("黯", new CharacterData(1121.0f, "an_dark")),
            Map.entry("雕", new CharacterData(1122.0f, "diao_carve")),
            Map.entry("奪", new CharacterData(1123.0f, "duo_rob")),
            Map.entry("糕", new CharacterData(1124.0f, "gao_cake")),
            Map.entry("羹", new CharacterData(1125.0f, "geng_soup")),
            Map.entry("畺", new CharacterData(1126.0f, "jiang_jiang")),
            Map.entry("狼", new CharacterData(1127.0f, "lang_wolf")),
            Map.entry("珊", new CharacterData(1128.0f, "shan_coral")),
            Map.entry("死", new CharacterData(1129.0f, "si_die")),
            Map.entry("柵", new CharacterData(1130.0f, "zha_fence")),
            Map.entry("猋", new CharacterData(1131.0f, "biao_dogs")),
            Map.entry("風", new CharacterData(1132.0f, "feng_wind")),
            Map.entry("竿", new CharacterData(1133.0f, "gan_pole")),
            Map.entry("寒", new CharacterData(1134.0f, "han_cold")),
            Map.entry("魄", new CharacterData(1135.0f, "po_soul")),
            Map.entry("扇", new CharacterData(1136.0f, "shan_fan")),
            Map.entry("物", new CharacterData(1137.0f, "wu_thing")),
            Map.entry("核", new CharacterData(1138.0f, "he_core")),
            Map.entry("洄", new CharacterData(1139.0f, "hui_backwash")),
            Map.entry("囊", new CharacterData(1140.0f, "nang_pocket")),


            //Gold Level
            Map.entry("嚴", new CharacterData(2000.0f, "yan_strict")),
            Map.entry("樁", new CharacterData(2001.0f, "zhuang_stump")),
            Map.entry("定", new CharacterData(2002.0f, "ding_stable")),
            Map.entry("鐵右", new CharacterData(2003.0f, "zhi_big")),
            Map.entry("炭", new CharacterData(2004.0f, "tan_charcoal")),
            Map.entry("版", new CharacterData(2005.0f, "ban_plank")),
            Map.entry("砂", new CharacterData(2006.0f, "sha_grit")),
            Map.entry("銅", new CharacterData(2007.0f, "tong_copper")),
            Map.entry("皆", new CharacterData(2008.0f, "jie_all")),
            Map.entry("靈", new CharacterData(2009.0f, "ling_soul")),
            Map.entry("群", new CharacterData(2010.0f, "qun_group")),
            Map.entry("淺", new CharacterData(2011.0f, "qian_shallow")),
            Map.entry("綫", new CharacterData(2012.0f, "xian_string")),
            Map.entry("胔", new CharacterData(2013.0f, "zi_rottenmeat")),
            Map.entry("板", new CharacterData(2014.0f, "ban_board")),
            Map.entry("殘", new CharacterData(2015.0f, "can_disable")),
            Map.entry("案", new CharacterData(2016.0f, "an_table")),
            Map.entry("鈺", new CharacterData(2017.0f, "yu_treasure")),
            Map.entry("紫", new CharacterData(2018.0f, "zi_purple")),
            Map.entry("鋤", new CharacterData(2019.0f, "chu_hoe")),
            Map.entry("鎬", new CharacterData(2020.0f, "gao_pickaxe")),
            Map.entry("僉", new CharacterData(2021.0f, "qian_all")),
            Map.entry("鍁", new CharacterData(2022.0f, "xian_shovel")),
            Map.entry("劍", new CharacterData(2023.0f, "jian_sword")),
            Map.entry("盤", new CharacterData(2024.0f, "pan_plate")),
            Map.entry("聲", new CharacterData(2025.0f, "sheng_sound")),
            Map.entry("鍛", new CharacterData(2026.0f, "duan_smith")),
            Map.entry("礦", new CharacterData(2027.0f, "kuang_ore")),
            Map.entry("莫", new CharacterData(2028.0f, "mo_not")),
            Map.entry("逆", new CharacterData(2029.0f, "ni_converse")),
            Map.entry("湧", new CharacterData(2030.0f, "yong_surge")),
            Map.entry("從", new CharacterData(2031.0f, "cong_follows")),
            Map.entry("骸", new CharacterData(2032.0f, "hai_skeleton")),
            Map.entry("檻", new CharacterData(2033.0f, "jian_fence")),
            Map.entry("藍", new CharacterData(2034.0f, "lan_blue")),
            Map.entry("闌", new CharacterData(2035.0f, "lan_fence")),
            Map.entry("深", new CharacterData(2036.0f, "shen_deep")),
            Map.entry("緋", new CharacterData(2037.0f, "fei_crimson")),
            Map.entry("扉", new CharacterData(2038.0f, "fei_door")),
            Map.entry("種", new CharacterData(2039.0f, "zhong_plant")),
            Map.entry("莓", new CharacterData(2040.0f, "mei_berry")),
            Map.entry("葉", new CharacterData(2041.0f, "ye_leaves")),
            Map.entry("蒼", new CharacterData(2042.0f, "cang_gray")),
            Map.entry("橙", new CharacterData(2043.0f, "cheng_orange")),
            Map.entry("赬", new CharacterData(2044.0f, "cheng_red")),
            Map.entry("黛", new CharacterData(2045.0f, "dai_cyan")),
            Map.entry("荳", new CharacterData(2046.0f, "dou_bean")),
            Map.entry("曷", new CharacterData(2047.0f, "he_why")),
            Map.entry("縹", new CharacterData(2048.0f, "piao_lightcyan")),
            Map.entry("英", new CharacterData(2049.0f, "ying_flower")),
            Map.entry("幾", new CharacterData(2050.0f, "ji_several")),
            Map.entry("網", new CharacterData(2051.0f, "wang_web")),
            Map.entry("械", new CharacterData(2052.0f, "xie_machine")),
            Map.entry("製", new CharacterData(2053.0f, "zhi_make")),
            Map.entry("弩", new CharacterData(2054.0f, "nu_crossbow")),
            Map.entry("㭜", new CharacterData(2055.0f, "rong_acacia")),
            Map.entry("探", new CharacterData(2056.0f, "tan_explore")),
            Map.entry("櫻", new CharacterData(2057.0f, "ying_cherry")),
            Map.entry("潮", new CharacterData(2058.0f, "chao_tide")),
            Map.entry("苗", new CharacterData(2059.0f, "miao_seedling")),
            Map.entry("銳", new CharacterData(2060.0f, "rui_sharp")),
            Map.entry("剛", new CharacterData(2061.0f, "gang_hard")),
            Map.entry("崗", new CharacterData(2062.0f, "gang_mountain")),
            Map.entry("燈", new CharacterData(2063.0f, "deng_lamp")),
            Map.entry("繕", new CharacterData(2064.0f, "shan_mend")),
            Map.entry("修", new CharacterData(2065.0f, "xiu_repair")),
            Map.entry("鸚", new CharacterData(2066.0f, "ying_parrot")),
            Map.entry("悠", new CharacterData(2067.0f, "you_remote")),
            Map.entry("雞", new CharacterData(2068.0f, "ji_chicken")),
            Map.entry("鼃", new CharacterData(2069.0f, "wa_frog")),
            Map.entry("蕈", new CharacterData(2070.0f, "xun_mushroom")),
            Map.entry("璧", new CharacterData(2071.0f, "bi_jade")),
            Map.entry("閃", new CharacterData(2072.0f, "shan_spy")),
            Map.entry("箱", new CharacterData(2073.0f, "xiang_chest")),
            Map.entry("桿", new CharacterData(2074.0f, "gan_rod")),
            Map.entry("胡", new CharacterData(2075.0f, "hu_why")),
            Map.entry("蘆", new CharacterData(2076.0f, "lu_reed")),
            Map.entry("芋", new CharacterData(2077.0f, "yu_potato")),
            Map.entry("韁", new CharacterData(2078.0f, "jiang_rein")),
            Map.entry("殭", new CharacterData(2079.0f, "jiang_zombie")),
            Map.entry("屍", new CharacterData(2080.0f, "shi_corpse")),
            Map.entry("飆", new CharacterData(2081.0f, "biao_gale")),
            Map.entry("贆", new CharacterData(2082.0f, "biao_shellfish")),
            Map.entry("枯", new CharacterData(2083.0f, "ku_wither")),
            Map.entry("鮭", new CharacterData(2084.0f, "gui_salmon")),
            Map.entry("饢", new CharacterData(2085.0f, "nang_bread")),
            Map.entry("鱈", new CharacterData(2086.0f, "xue_cod")),


            //Diamond Level
            Map.entry("巖", new CharacterData(3000.0f, "yan_rock")),
            Map.entry("錠", new CharacterData(3001.0f, "ding_ingot")),
            Map.entry("鐵", new CharacterData(3002.0f, "tie_iron")),
            Map.entry("階", new CharacterData(3003.0f, "jie_stairs")),
            Map.entry("模", new CharacterData(3004.0f, "mu_model")),
            Map.entry("楷", new CharacterData(3005.0f, "kai_pattern")),
            Map.entry("樅", new CharacterData(3006.0f, "zong_fir")),
            Map.entry("蘭", new CharacterData(3007.0f, "lan_orchid")),
            Map.entry("靛", new CharacterData(3008.0f, "dian_indigo")),
            Map.entry("褐", new CharacterData(3009.0f, "he_brown")),
            Map.entry("渴", new CharacterData(3010.0f, "ke_thirst")),
            Map.entry("機", new CharacterData(3011.0f, "ji_machine")),
            Map.entry("貓", new CharacterData(3012.0f, "mao_cat")),
            Map.entry("瑚", new CharacterData(3013.0f, "hu_coral")),


            //Special Level
            Map.entry("1", new CharacterData(5000.0f, "numbers")),
            Map.entry("A", new CharacterData(5001.0f, "latin_letters")),
            Map.entry("Α", new CharacterData(5002.0f, "greek_letters")),
            Map.entry("あ", new CharacterData(5003.0f, "kana")),
            Map.entry("ي", new CharacterData(5004.0f, "arabic_letters")),
            Map.entry("Д", new CharacterData(5005.0f, "cyrillic_letters")),

            Map.entry("LGhast", new CharacterData(7000.0f, "lghast")),
            Map.entry("Minecraft", new CharacterData(7001.0f, "minecraft")),


            //Copper Level
            Map.entry(CharacterItem.SIMPLE_INSCRIPTION, new CharacterData(6000.0f, "simple_noncharacter_component")),
            Map.entry(CharacterItem.REVERSAL_INSCRIPTION, new CharacterData(6001.0f, "character_reversal")),
            Map.entry(CharacterItem.COMPLEX_INSCRIPTION, new CharacterData(6002.0f, "complex_character_component"))
    ));

    public static float getFloatValue(String character) {
        CharacterData data = CHARACTER_DATA.get(character);
        if (data == null) {
            if(character.length()==1) {
                if (NUMBERS.contains(character)) return 5000.0f;
                if (LATIN_LETTERS.contains(character)) return 5001.0f;
                if (GREEK_LETTERS.contains(character)) return 5002.0f;
                if (KANA.contains(character)) return 5003.0f;
                if (ARABIC_LETTERS.contains(character)) return 5004.0f;
                if (CYRILLIC_LETTERS.contains(character)) return 5005.0f;
            }
            return 0.0f;
        }
        return data.floatValue();
    }

    public static String NUMBERS = "1234567890%";
    public static String LATIN_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzÞþÇçŒœÆæÐðßÄäÖöÜüÀàÂâÉéÈèÊêËëÎîÏïÔôÙùÛûÜüŸÿ";
    public static String GREEK_LETTERS = "ΑΒΓΔΕΖΗΘΙΚΛΜΝΞΟΠΡΣΤΥΦΧΨΩαβγδεζηθικλμνξοπρστυφχψω";
    public static String KANA = "あいうえおかきくけこさしすせそたちつてとなにぬねのはひふへほまみむめもやゆよらりるれろわをんがぎぐげござじずぜぞだぢづでどばびぶべぼぱぴぷぺぽっゃゅょアイウエオカキクケコサシスセソタチツテトナニヌネノハヒフヘホマミムメモヤユヨラリルレロワヲンガギグゲゴザジズゼゾダヂヅデドバビブベボパピプペポッャュョ";
    public static String ARABIC_LETTERS = "ا ب ت ث ج ح خ د ذ ر ز س ش ص ض ط ظ ع غ ف ق ك ل م ن ه و ي";
    public static String CYRILLIC_LETTERS = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюяЄІЇЎҐЂЋЏЉЊђћџљњј";

    public static String getIdentifier(String character) {
        CharacterData data = CHARACTER_DATA.get(character);
        if (data == null) return CharacterItem.DEFAULT_INSCRIPTION;
        return data.identifier();
    }

    private static final List<String> inscriptionsLucky = new ArrayList<>();
    private static final List<String> inscriptionsLuckyAdvanced = new ArrayList<>();

    private static void inscriptionsLuckyInitialize(){
        for (Map.Entry<String, CharacterData> entry : CHARACTER_DATA.entrySet()) {
            float value = entry.getValue().floatValue;
            if(value<2000){
                String key = entry.getKey();
                if(!CHARACTER_COMPONENTS.containsKey(key)){
                    inscriptionsLucky.add(key);
                }
            }
        }
    }

    private static void inscriptionsLuckyAdvancedInitialize(){
        for (Map.Entry<String, CharacterData> entry : CHARACTER_DATA.entrySet()) {
            float value = entry.getValue().floatValue;
            if(value<1000){
                String key = entry.getKey();
                inscriptionsLuckyAdvanced.add(key);
            }
        }
    }

    public static String getLuckyInscription(){
        if(inscriptionsLucky.isEmpty()){
            inscriptionsLuckyInitialize();
        }
        Random random = new Random();
        int randomIndex = random.nextInt(inscriptionsLucky.size());
        return inscriptionsLucky.get(randomIndex);
    }

    public static String getLuckyInscriptionAdvanced(){
        if(inscriptionsLuckyAdvanced.isEmpty()){
            inscriptionsLuckyAdvancedInitialize();
        }
        Random random = new Random();
        int randomIndex = random.nextInt(inscriptionsLuckyAdvanced.size());
        return inscriptionsLuckyAdvanced.get(randomIndex);
    }
}
