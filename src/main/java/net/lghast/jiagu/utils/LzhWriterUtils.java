package net.lghast.jiagu.utils;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.block.Block;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LzhWriterUtils {
    private static final Path itemFile = Paths.get("item_names.txt").toAbsolutePath();
    private static final Path entityFile = Paths.get("entity_names.txt").toAbsolutePath();
    private static final Path blockFile = Paths.get("block_names.txt").toAbsolutePath();
    private static final Path effectFile = Paths.get("effect_names.txt").toAbsolutePath();
    private static final Path potionFile = Paths.get("potion_names.txt").toAbsolutePath();
    
    public static void write(String nameSpace){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(itemFile.toFile()))) {

            for (Item item : BuiltInRegistries.ITEM) {
                ItemStack stack = new ItemStack(item);
                if (stack.isEmpty()) {
                    continue;
                }
                ResourceLocation location = BuiltInRegistries.ITEM.getKey(item);
                if(!location.getNamespace().equals(nameSpace)) continue;
                String id = location.toString();

                String displayName = stack.getHoverName().getString();

                String line = String.format("ITEM_MAP.put(\"%s\", \"%s\");", id, displayName);
                writer.write(line);
                writer.newLine();
            }

            System.out.println("物品名称映射已成功导出至: " + itemFile);

        } catch (IOException e) {
            System.err.println("导出物品名称映射时发生错误: " + e.getMessage());
            e.printStackTrace();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(entityFile.toFile()))) {

            for (EntityType<?> entityType : BuiltInRegistries.ENTITY_TYPE) {
                if (entityType.getCategory() == MobCategory.MISC) {
                    continue;
                }
                ResourceLocation location = BuiltInRegistries.ENTITY_TYPE.getKey(entityType);
                if(!location.getNamespace().equals(nameSpace)) continue;
                String id = location.toString();

                String displayName = entityType.getDescription().getString();

                String line = String.format("ENTITY_MAP.put(\"%s\", \"%s\");", id, displayName);
                writer.write(line);
                writer.newLine();
            }

            System.out.println("实体名称映射已成功导出至: " + entityFile);

        } catch (IOException e) {
            System.err.println("实体名称映射时发生错误: " + e.getMessage());
            e.printStackTrace();
        }
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(blockFile.toFile()))) {

            for (Block block : BuiltInRegistries.BLOCK) {
                ResourceLocation location = BuiltInRegistries.BLOCK.getKey(block);
                if(!location.getNamespace().equals(nameSpace)) continue;
                String id = location.toString();

                String displayName = block.getName().getString();

                String line = String.format("BLOCK_MAP.put(\"%s\", \"%s\");", id, displayName);
                writer.write(line);
                writer.newLine();
            }

            System.out.println("方块名称映射已成功导出至: " + blockFile);

        } catch (IOException e) {
            System.err.println("方块名称映射时发生错误: " + e.getMessage());
            e.printStackTrace();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(effectFile.toFile()))) {

            for (MobEffect effect : BuiltInRegistries.MOB_EFFECT) {
                ResourceLocation location = BuiltInRegistries.MOB_EFFECT.getKey(effect);
                if(location == null) continue;
                
                if(!location.getNamespace().equals(nameSpace)) continue;
                String id = location.toString();

                String displayName = effect.getDisplayName().getString();

                String line = String.format("EFFECT_MAP.put(\"%s\", \"%s\");", id, displayName);
                writer.write(line);
                writer.newLine();
            }

            System.out.println("状态效果名称映射已成功导出至: " + effectFile);

        } catch (IOException e) {
            System.err.println("状态效果名称映射时发生错误: " + e.getMessage());
            e.printStackTrace();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(potionFile.toFile()))) {

            for (Holder.Reference<Potion> potion : BuiltInRegistries.POTION.holders().toList()) {
                ResourceLocation location = BuiltInRegistries.POTION.getKey(potion.value());
                if(location == null) continue;

                if(!location.getNamespace().equals(nameSpace)) continue;
                String id = location.toString();

                ItemStack stack = new ItemStack(Items.POTION);
                stack.set(DataComponents.POTION_CONTENTS, new PotionContents(potion));
                String displayName = stack.getHoverName().getString();

                if(location.getNamespace().startsWith("long_")){
                    displayName = "久效" + displayName;
                }else if(location.getNamespace().startsWith("strong_")){
                    displayName = "強效" + displayName;
                }

                String line = String.format("POTION_MAP.put(\"%s\", \"%s\");", id, displayName);
                writer.write(line);
                writer.newLine();
            }

            System.out.println("药水名称映射已成功导出至: " + potionFile);

        } catch (IOException e) {
            System.err.println("药水效果名称映射时发生错误: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
