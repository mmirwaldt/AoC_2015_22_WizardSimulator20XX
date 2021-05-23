package net.mirwaldt.aoc.year2015.day22;

import static net.mirwaldt.aoc.year2015.day22.Spell.Stage.*;

public enum Spell {
    MAGIC_MISSILE(INSTANT,53, 4, 0, 0,0, 0),
    DRAIN(INSTANT, 73, 2, 0, 2,0, 0),
    SHIELD(LATE, 113, 0, 7, 0,6, 0),
    POISON(LATE, 173, 3, 0, 0,6, 0),
    RECHARGE(LATE, 229, 0, 0, 0,5, 101);

    enum Stage {
        INSTANT, EARLY, LATE
    }

    final Stage stage;
    final int costs;
    final int damage;
    final int armor;
    final int heal;
    final int turns;
    final int profit;

    Spell(Stage stage, int costs, int damage, int armor, int heal, int turns, int profit) {
        this.stage = stage;
        this.costs = costs;
        this.damage = damage;
        this.armor = armor;
        this.heal = heal;
        this.turns = turns;
        this.profit = profit;
    }
}
