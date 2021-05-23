package net.mirwaldt.aoc.year2015.day22;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static net.mirwaldt.aoc.year2015.day22.Spell.Stage.*;

public class Wizard extends Player {
    public static final int CONTINUE = -1;
    public static boolean DEBUG_MODE = true;
    protected int mana;
    protected int payedMana = 0;
    protected EnumMap<Spell, Integer> turnsBySpells = new EnumMap<>(Spell.class);

    public Wizard(Wizard wizard) {
        super(wizard.name, wizard.hitPoints, wizard.damage, wizard.armor);
        this.mana = wizard.mana;
        this.payedMana = wizard.payedMana;
        this.turnsBySpells = new EnumMap<>(wizard.turnsBySpells);
    }

    public Wizard(String name, int hitPoints, int damage, int armor, int mana) {
        super(name, hitPoints, damage, armor);
        this.mana = mana;
    }

    public void castSpell(Spell spell) {
//        if (turnsBySpells.containsKey(spell)) {
//
////            throw new IllegalArgumentException("Spell " + spell.name()
////                    + " is still active for " + turnsBySpells.get(spell) + " turn(s)!");
//        } else {
            if (spell.costs <= mana) {
                payedMana += spell.costs;
                mana -= spell.costs;
                if (spell.stage == INSTANT) {
                    damage += spell.damage;
                    hitPoints += spell.heal;
                }
                if (DEBUG_MODE) {
                    String s = String.join("\n", activeSpellsToString());
                    if(!s.isEmpty()) {
                        System.out.println(s);
                    }
                }
                turnsBySpells.computeIfPresent(spell, (key, val) -> CONTINUE);
                turnsBySpells.putIfAbsent(spell, spell.turns);
                if (DEBUG_MODE) {
                    System.out.println(spellCastToString(spell));
                }
            } else {
                throw new IllegalArgumentException("Spell " + spell.name() + " costs "
                        + spell.costs + " mana but only " + mana + " mana is available!");
            }
//        }
    }

    public String spellCastToString(Spell spell) {
        return "Player casts " + spell.name();
    }

    public List<String> activeSpellsToString() {
        return turnsBySpells.keySet().stream().map(activeSpell -> activeSpell + " active.").collect(toList());
    }

    private void evaluateLateSpells() {
        Iterator<Map.Entry<Spell, Integer>> entryIterator = turnsBySpells.entrySet().iterator();
        while (entryIterator.hasNext()) {
            final Map.Entry<Spell, Integer> spellEntry = entryIterator.next();
            final Spell spell = spellEntry.getKey();
            int turn = spellEntry.getValue();
            if (spell.stage == INSTANT) {
                damage -= spell.damage;
                entryIterator.remove();
            } else if (spell.stage == LATE) {
                if (turn == CONTINUE) {
                    turn = spell.turns;
                } else if (turn == spell.turns) {
                    damage += spell.damage;
                    armor += spell.armor;
                }

                if (0 <= turn && turn < spell.turns) {
                    mana += spell.profit;
                }

                if(0 == turn) {
                    damage -= spell.damage;
                    armor -= spell.armor;
                    entryIterator.remove();
                } else {
                    spellEntry.setValue(--turn);
                }
            }
        }
    }

    @Override
    public List<String> attack(Player enemy) {
        final List<String> attackStrings = enemy.beingAttackedBy(this);
        evaluateLateSpells();
        return attackStrings;
    }

    @Override
    public List<String> beingAttackedBy(Player enemy) {
        if (DEBUG_MODE) {
            String s = String.join("\n", activeSpellsToString());
            if(!s.isEmpty()) {
                System.out.println(s);
            }
        }
        final List<String> attackStrings = new ArrayList<>();
        attackStrings.addAll(enemy.beingAttackedBy(this));
        attackStrings.addAll(super.beingAttackedBy(enemy));
        evaluateLateSpells();
        return attackStrings;
    }

    @Override
    public String toString() {
        return "- " + name + " has " + hitPoints + " hit points, " + armor + " armor, " + mana + " mana, " + payedMana + " payedMana";
    }

    public EnumSet<Spell> getAvailableSpells() {
        return Arrays.stream(Spell.values())
                .filter(spell -> !turnsBySpells.containsKey(spell) || turnsBySpells.get(spell) ==0)
                .filter(spell -> spell.costs <= mana)
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(Spell.class)));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Wizard wizard = (Wizard) o;
        return mana == wizard.mana && payedMana == wizard.payedMana && Objects.equals(turnsBySpells, wizard.turnsBySpells);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mana, payedMana, turnsBySpells);
    }
}
