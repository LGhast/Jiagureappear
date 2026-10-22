package net.lghast.jiagu.common.system.datacomponent;

import net.lghast.jiagu.utils.ModUtils;
import net.minecraft.core.Holder;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.Objects;
import java.util.Optional;

public record Spell(Optional<Holder<Enchantment>> enchantment, int level) {
    public static final Spell EMPTY = new Spell(Optional.empty(), 0);

    public Spell() {
        this(Optional.empty(), 0);
    }

    public Spell(Holder<Enchantment> enchantment, int level) {
        this(Optional.of(enchantment), level);
    }

    public String spellName() {
        return enchantment.map(holder -> ModUtils.modifyName(holder, level)).orElse("");
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Spell other)) return false;

        boolean enchantmentEqual;
        if (this.enchantment.isPresent() && other.enchantment.isPresent()) {
            enchantmentEqual = Objects.equals(this.enchantment.get(), other.enchantment.get());
        } else {
            enchantmentEqual = this.enchantment.isEmpty() && other.enchantment.isEmpty();
        }

        return enchantmentEqual && this.level == other.level;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                enchantment.map(Holder::hashCode).orElse(0),
                level
        );
    }

    @Override
    public String toString() {
        return "Spell{enchantment=" + enchantment.map(Holder::getRegisteredName).orElse("empty") +
                ", level=" + level + "}";
    }

    public boolean isEmpty() {
        return enchantment().isEmpty() || level <= 0;
    }

    public Holder<Enchantment> getEnchantment() {
        return enchantment.orElse(null);
    }
}
