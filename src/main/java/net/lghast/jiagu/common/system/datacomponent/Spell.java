package net.lghast.jiagu.common.system.datacomponent;

import net.lghast.jiagu.utils.ModUtils;
import net.lghast.jiagu.utils.lzh.LzhMappings;
import net.minecraft.core.Holder;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.Objects;
import java.util.Optional;

public record Spell(Optional<Holder<Enchantment>> enchantment, int level, String spellName) {
    public static final Spell EMPTY = new Spell(Optional.empty(), 0, "");

    public Spell(Holder<Enchantment> enchantment, int level, String spellName) {
        this(Optional.of(enchantment), level, spellName);
    }

    public Spell() {
        this(Optional.empty(), 0, "");
    }

    public Spell(String spellName) {
        this(Optional.empty(), 0, spellName);
    }

    public Spell(Holder<Enchantment> enchantment, String spellName) {
        this(Optional.of(enchantment), 1, spellName);
    }

    public Spell(Holder<Enchantment> enchantment, int level) {
        this(Optional.of(enchantment), level, ModUtils.modifyName(enchantment, level));
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

        return enchantmentEqual &&
                this.level == other.level &&
                Objects.equals(this.spellName, other.spellName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                enchantment.map(Holder::hashCode).orElse(0),
                level,
                spellName
        );
    }

    @Override
    public String toString() {
        return "Spell{enchantment=" + enchantment.map(Holder::getRegisteredName).orElse("empty") +
                ", level=" + level +
                ", spellName='" + spellName + "'}";
    }

    public boolean isEmpty(){
        return enchantment().isEmpty() || level <= 0 || spellName == null || spellName.isEmpty();
    }

    public Holder<Enchantment> getEnchantment(){
        return enchantment.orElse(null);
    }
}
