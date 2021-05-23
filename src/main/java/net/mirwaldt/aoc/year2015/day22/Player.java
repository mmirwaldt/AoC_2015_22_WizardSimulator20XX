package net.mirwaldt.aoc.year2015.day22;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static java.lang.Math.max;

public class Player {
    protected final String name;
    protected int hitPoints;
    protected int damage;
    protected int armor;

    public Player(Player player) {
        this(player.name, player.hitPoints, player.damage, player.armor);
    }

    public Player(String name, int hitPoints, int damage, int armor) {
        this.name = name;
        this.hitPoints = hitPoints;
        this.damage = damage;
        this.armor = armor;
    }

    public List<String> attack(Player enemy) {
        return enemy.beingAttackedBy(this);
    }

    public List<String> beingAttackedBy(Player enemy) {
        final int effectiveDamage = ((0 < enemy.damage) ? max(1, enemy.damage - armor) : 0);
        final String attackString = enemy.name + " attacks " + name + " with damage " + effectiveDamage;
        if(Wizard.DEBUG_MODE) {
            System.out.println(attackString);
        }
        hitPoints = max(0, hitPoints - effectiveDamage);
        return Collections.singletonList(attackString);
    }

    public boolean hasLost() {
        return hitPoints == 0;
    }

    @Override
    public String toString() {
        return "- " + name + " has " + hitPoints + " hit points";
    }

    /**
     * for part two
     */
    public void loseHitpoint() {
        hitPoints = max(0, hitPoints - 1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return hitPoints == player.hitPoints && damage == player.damage && armor == player.armor && Objects.equals(name, player.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, hitPoints, damage, armor);
    }
}
