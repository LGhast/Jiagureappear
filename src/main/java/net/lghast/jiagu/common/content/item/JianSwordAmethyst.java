package net.lghast.jiagu.common.content.item;

import net.lghast.jiagu.JiaguReappear;
import net.lghast.jiagu.config.ServerConfig;
import net.lghast.jiagu.register.content.ModEnchantments;
import net.lghast.jiagu.register.content.ModItems;
import net.lghast.jiagu.client.particle.ModParticles;
import net.lghast.jiagu.utils.ModUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

public class JianSwordAmethyst extends SwordItem {
    public JianSwordAmethyst(Tier tier, Properties properties) {
        super(tier, properties);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @EventBusSubscriber(modid = JiaguReappear.MOD_ID)
    public static class CharacterHuntingEvents
    {
        @SubscribeEvent
        public static void onEntityKilled(LivingDeathEvent event) {
            LivingEntity entityKilled = event.getEntity();

            if (entityKilled.level().isClientSide) return;
            if(!(entityKilled.level() instanceof ServerLevel serverLevel)) return;

            if(ServerConfig.JIAN_SWORD_CUSTOM_NAME_CHECK.get() && !(entityKilled instanceof Player)) {
                if (entityKilled.hasCustomName()) return;
            }

            if(event.getSource().getDirectEntity() instanceof LivingEntity source){
                ItemStack mainHandItem = source.getMainHandItem();
                if(mainHandItem.isEmpty()) return;

                if(mainHandItem.is(ModItems.JIAN_SWORD_AMETHYST.asItem())){
                    Holder<Enchantment> inquisitivenessHolder = serverLevel.registryAccess().registryOrThrow(Registries.ENCHANTMENT)
                            .getHolderOrThrow(ModEnchantments.INQUISITIVENESS);
                    ItemEnchantments enchantments = mainHandItem.getTagEnchantments();

                    float baseChance = ServerConfig.JIAN_SWORD_CHARACTER_CHANCE.get().floatValue();
                    float chance = baseChance * (1 + enchantments.getLevel(inquisitivenessHolder) * 0.5f);
                    if(Math.random() < chance){
                        String entityName = entityKilled instanceof Player? "戲者" : ModUtils.getCharacters(entityKilled);
                        for (int i = 0; i < entityName.length(); i++) {
                            char c = entityName.charAt(i);
                            ItemStack characterItem = new ItemStack(ModItems.CHARACTER_ITEM.get());
                            CharacterItem.setInscription(characterItem, c);
                            ModUtils.spawnItemWithMotion(serverLevel, entityKilled.getX(), entityKilled.getY(), entityKilled.getZ(), characterItem, false);
                        }
                        ModUtils.spawnParticlesForAll(serverLevel, ModParticles.JIAGU_FLOATING_PARTICLES.get(),
                                entityKilled.getX(), entityKilled.getY()+0.2, entityKilled.getZ(), 0.6, 0.5, 0.6, 15, 0.04);
                    }
                }
            }
        }
    }
}
