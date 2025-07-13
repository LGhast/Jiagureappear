package net.lghast.jiagu.item;

import net.lghast.jiagu.register.ModEnchantments;
import net.lghast.jiagu.utils.ModUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.windcharge.WindCharge;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.ChunkEntities;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.compress.utils.Sets;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class BiaoGaleAmethystItem extends Item {
    private static final int BASE_DISTANCE = 6;
    private static final int WIND_ANGLE = 30;
    private static final double BASE_SPEED = 0.3;


    public BiaoGaleAmethystItem(Properties properties) {
        super(properties.rarity(Rarity.EPIC).stacksTo(1).durability(60));
    }

    @Override
    public boolean isRepairable(ItemStack stack) {
        return true;
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack repairCandidate) {
        return repairCandidate.is(Items.BREEZE_ROD);
    }

    private static final Set<ResourceKey<Enchantment>> supportedEnchantment = Sets.newHashSet(
            Enchantments.MENDING,Enchantments.UNBREAKING,
            Enchantments.VANISHING_CURSE,Enchantments.BINDING_CURSE,
            ModEnchantments.FLURRY,ModEnchantments.WUTHERING
    );

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);

        if (stack.getDamageValue() >= stack.getMaxDamage()) {
            return InteractionResultHolder.fail(stack);
        }


        player.startUsingItem(usedHand);
        return InteractionResultHolder.success(stack);
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    @Override
    public void onUseTick(Level level, LivingEntity user, ItemStack stack, int remainingUseDuration) {
        super.onUseTick(level, user, stack, remainingUseDuration);
        if(!(user instanceof Player player) || !(level instanceof ServerLevel serverLevel)) return;
        if (Math.random() < 0.08) {
            ModUtils.spawnParticlesForAll(serverLevel, ParticleTypes.GUST_EMITTER_SMALL,
                    player.getX(), player.getY() + 0.5, player.getZ(), 0.3, 0.3, 0.3, 1, 0.5);
        }
        if (Math.random() < 0.07) {
            ModUtils.spawnParticlesForAll(serverLevel, ParticleTypes.GUST_EMITTER_LARGE,
                    player.getX(), player.getY() + 0.5, player.getZ(), 0.3, 0.3, 0.3, 1, 0.5);
        }
        Vec3 eyePos = player.getEyePosition();
        Vec3 lookVec = player.getLookAngle();
        Vec3 newEyePos = eyePos.add(lookVec.scale(0.5));


        Holder<Enchantment> wutheringHolder = level.registryAccess().registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(ModEnchantments.WUTHERING);
        Holder<Enchantment> flurryHolder = level.registryAccess().registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(ModEnchantments.FLURRY);

        double windDistance = BASE_DISTANCE * (stack.getEnchantmentLevel(wutheringHolder) * 0.2 + 1);
        double windSpeed = BASE_SPEED * (stack.getEnchantmentLevel(flurryHolder) * 0.25 + 1);

        double yCenter = player.getY();
        AABB area = new AABB(
                player.getX()-windDistance, yCenter-0.5, player.getZ()-windDistance,
                player.getX()+windDistance, yCenter+2, player.getZ()+windDistance
        );

        List<Entity> entities = level.getEntities(
                EntityTypeTest.forClass(Entity.class),
                area,
                e -> e != player
                        && (e.isPushable() || e instanceof ArmorStand || e instanceof ItemEntity)
                        && isInSector(newEyePos, lookVec, e.position(), windDistance, WIND_ANGLE)
        );

        if(entities.isEmpty()) return;

        for (Entity entity : entities) {
            Vec3 targetPos = entity.position();
            Vec3 pushDir = targetPos.subtract(eyePos).normalize();

            BlockHitResult hitResult = level.clip(new ClipContext(
                    eyePos, targetPos,
                    ClipContext.Block.COLLIDER,
                    ClipContext.Fluid.NONE,
                    player
            ));

            if (hitResult.getType() == BlockHitResult.Type.BLOCK) {
                double blockDist = eyePos.distanceTo(hitResult.getLocation());
                double entityDist = eyePos.distanceTo(targetPos);
                if (blockDist < entityDist) continue;
            }

            AABB box = entity.getBoundingBox();
            double volume = entity instanceof ItemEntity itemEntity ?
                    itemEntity.getItem().getCount()/100.0+1 : box.getXsize() * box.getYsize() * box.getZsize();
            double speed = windSpeed / (1 + volume * 1.2);

            Vec3 velocity = entity.getDeltaMovement()
                    .add(pushDir.scale(speed))
                    .scale(0.9);
            entity.setDeltaMovement(velocity);
            entity.hurtMarked = true;
        }

        if (level.getRandom().nextFloat() < 0.04f) {
            stack.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
        }
    }

    private boolean isInSector(Vec3 origin, Vec3 direction, Vec3 target, double maxDist, double angleDeg){
        Vec3 toTarget = target.subtract(origin);
        double distance = toTarget.length();

        if (distance > maxDist || distance < 0.01) return false;

        double dot = direction.dot(toTarget.normalize());
        dot = Math.max(-1.0, Math.min(1.0, dot));
        double angleRad = Math.acos(dot);

        return Math.toDegrees(angleRad) <= angleDeg;
    }


    @Override
    public int getEnchantmentValue(ItemStack stack) {
        return 10;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return true;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Override
    public boolean isPrimaryItemFor(ItemStack stack, Holder<Enchantment> enchantment) {
        if(supportedEnchantment.contains(enchantment.getKey())){
            return true;
        }
        return super.isPrimaryItemFor(stack, enchantment);
    }

    @Override
    public boolean supportsEnchantment(ItemStack stack, Holder<Enchantment> enchantment) {
        if(supportedEnchantment.contains(enchantment.getKey())){
            return true;
        }
        return super.supportsEnchantment(stack, enchantment);
    }
}
