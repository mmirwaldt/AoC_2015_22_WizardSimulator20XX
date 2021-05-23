package net.mirwaldt.aoc.year2015.day22;

import java.util.*;

public class WizardSimulator {
    public static void main(String[] args) {
        final Player boss = new Player("Boss", 51, 9, 0);
        final Wizard player = new Wizard("Player", 50, 0, 0, 500);

        Wizard.DEBUG_MODE = false;
        RecursiveSimulator recursiveSimulatorPartOne = new RecursiveSimulator(false);
        recursiveSimulatorPartOne.simulateRecursive(player, boss, true);
        System.out.println(recursiveSimulatorPartOne.minPayedMana); // result : 900

        RecursiveSimulator recursiveSimulatorPartTwo = new RecursiveSimulator(true);
        recursiveSimulatorPartTwo.simulateRecursive(player, boss, true);
        System.out.println(recursiveSimulatorPartTwo.minPayedMana); // result : 1216
    }

    static class RecursiveSimulator {
        private final boolean loseHitpointAtEveryTurn;
        private int minPayedMana = Integer.MAX_VALUE;
        private final Deque<Spell> spellStack = new ArrayDeque<>();
        private final Deque<List<String>> stringStack = new ArrayDeque<>();
        private List<List<String>> outputs = Collections.emptyList();
        private final Set<String> spellSequences = new HashSet<>();
        int winCounter = 0;
        int loseCounter = 0;

        public RecursiveSimulator(boolean loseHitpointAtEveryTurn) {
            this.loseHitpointAtEveryTurn = loseHitpointAtEveryTurn;
        }

        public void simulateRecursive(Wizard player, Player boss, boolean isWizardsTurn) {
            if (boss.hasLost()){ //  && !player.hasLost()) {
                winCounter++;
                if (player.payedMana <= minPayedMana) {
                    minPayedMana = player.payedMana;
                    outputs = new ArrayList<>(stringStack);
                }
            } else if (!player.hasLost() && player.payedMana < minPayedMana) {
                if (isWizardsTurn) {
                    final EnumSet<Spell> spells = player.getAvailableSpells();
                    if (!spells.isEmpty()) {
                        for (Spell spell : spells) {
                            final Wizard newPlayer = new Wizard(player);
                            final Player newBoss = new Player(boss);

                            if (loseHitpointAtEveryTurn) {
                                newPlayer.loseHitpoint();
                                if (newPlayer.hasLost()) {
                                    continue;
                                }
                            }

                            final List<String> output = new ArrayList<>();
                            output.add("-- Player turn --");
                            if (loseHitpointAtEveryTurn) {
                                output.add("One hit point removed from player");
                            }
                            output.add(newPlayer.toString());
                            output.add(player.turnsBySpells.keySet().toString());
                            output.add(spells.toString());
                            output.add(newBoss.toString());
                            output.addAll(newPlayer.activeSpellsToString());
                            output.add(newPlayer.spellCastToString(spell));
                            newPlayer.castSpell(spell);
                            output.addAll(newPlayer.attack(newBoss));
                            stringStack.addLast(output);
                            spellStack.addLast(spell);

                            simulateRecursive(newPlayer, newBoss, false);

                            spellStack.removeLast();
                            stringStack.removeLast();
                        }
                    }
                } else {
                    final Wizard newPlayer = new Wizard(player);
                    final Player newBoss = new Player(boss);

                    final List<String> output = new ArrayList<>();
                    output.add("-- Boss turn --");
                    output.add(newPlayer.toString());
                    output.add(player.turnsBySpells.keySet().toString());
                    output.add(player.getAvailableSpells().toString());
                    output.add(newBoss.toString());
                    output.addAll(newPlayer.activeSpellsToString());
                    output.addAll(newBoss.attack(newPlayer));
                    stringStack.addLast(output);

                    simulateRecursive(newPlayer, newBoss, true);

                    stringStack.removeLast();
                }
            } else if(player.hasLost()) {
                loseCounter++;
            }
        }
    }
}
