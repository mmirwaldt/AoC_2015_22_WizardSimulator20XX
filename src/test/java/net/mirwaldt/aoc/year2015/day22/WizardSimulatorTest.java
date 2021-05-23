package net.mirwaldt.aoc.year2015.day22;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Math.max;
import static java.util.stream.Collectors.joining;
import static net.mirwaldt.aoc.year2015.day22.Spell.MAGIC_MISSILE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WizardSimulatorTest {
    @Test
    public void testFirstExample() {
        Wizard.DEBUG_MODE = true;

        final Player boss = new Player("Boss", 13, 8, 0);
        final Wizard player = new Wizard("Player", 10, 0, 0, 250);

        System.out.println("-- Player turn --");
        System.out.println(player.toString());
        System.out.println(boss.toString());
        player.castSpell(Spell.POISON);
        player.attack(boss);
        System.out.println();

        System.out.println("-- Boss turn --");
        System.out.println(player.toString());
        System.out.println(boss.toString());
        boss.attack(player);
        System.out.println();

        System.out.println("-- Player turn --");
        System.out.println(player.toString());
        System.out.println(boss.toString());
        player.castSpell(MAGIC_MISSILE);
        player.attack(boss);
        System.out.println();

        System.out.println("-- Boss turn --");
        System.out.println(player.toString());
        System.out.println(boss.toString());
        boss.attack(player);
        System.out.println();

        assertTrue(boss.hasLost());
    }

    @Test
    public void testSecondExample() {
        Wizard.DEBUG_MODE = true;

        final Player boss = new Player("Boss", 14, 8, 0);
        final Wizard player = new Wizard("Player", 10, 0, 0, 250);

        System.out.println("-- Player turn --");
        System.out.println(player.toString());
        System.out.println(boss.toString());
        player.castSpell(Spell.RECHARGE);
        player.attack(boss);
        System.out.println();

        System.out.println("-- Boss turn --");
        System.out.println(player.toString());
        System.out.println(boss.toString());
        boss.attack(player);
        System.out.println();

        System.out.println("-- Player turn --");
        System.out.println(player.toString());
        System.out.println(boss.toString());
        player.castSpell(Spell.SHIELD);
        player.attack(boss);
        System.out.println();

        System.out.println("-- Boss turn --");
        System.out.println(player.toString());
        System.out.println(boss.toString());
        boss.attack(player);
        System.out.println();


        System.out.println("-- Player turn --");
        System.out.println(player.toString());
        System.out.println(boss.toString());
        player.castSpell(Spell.DRAIN);
        player.attack(boss);
        System.out.println();

        System.out.println("-- Boss turn --");
        System.out.println(player.toString());
        System.out.println(boss.toString());
        boss.attack(player);
        System.out.println();


        System.out.println("-- Player turn --");
        System.out.println(player.toString());
        System.out.println(boss.toString());
        player.castSpell(Spell.POISON);
        player.attack(boss);
        System.out.println();

        System.out.println("-- Boss turn --");
        System.out.println(player.toString());
        System.out.println(boss.toString());
        boss.attack(player);
        System.out.println();

        System.out.println("-- Player turn --");
        System.out.println(player.toString());
        System.out.println(boss.toString());
        player.castSpell(MAGIC_MISSILE);
        player.attack(boss);
        System.out.println();

        System.out.println("-- Boss turn --");
        System.out.println(player.toString());
        System.out.println(boss.toString());
        boss.attack(player);
        System.out.println();

        assertTrue(boss.hasLost());
    }

    @Test
    public void test_900() throws IOException {
        Wizard.DEBUG_MODE = true;

        final List<String> lines = Files.readAllLines(Path.of("900"), StandardCharsets.US_ASCII);

        for (String line : lines) {
            if (line.startsWith("#")) {
                continue;
            }
            String upperCase = line.replace("Magic Missile", MAGIC_MISSILE.name()).toUpperCase();
            final String[] spellsSequences = upperCase.split("->");
            final List<Spell> spells = Stream.of(spellsSequences)
                    .map(String::trim)
                    .map(Spell::valueOf)
                    .collect(Collectors.toList());
            testSpellSequence(spells, 900, false);
        }
    }

    @Test
    public void test_1216() throws IOException {
        Wizard.DEBUG_MODE = true;

        final List<String> lines = Files.readAllLines(Path.of("1216"), StandardCharsets.US_ASCII);

        for (String line : lines) {
            if (line.startsWith("#")) {
                continue;
            }
            String upperCase = line.replace("Magic Missile", MAGIC_MISSILE.name()).toUpperCase();
            final String[] spellsSequences = upperCase.split("->");
            final List<Spell> spells = Stream.of(spellsSequences)
                    .map(String::trim)
                    .map(Spell::valueOf)
                    .collect(Collectors.toList());
            System.out.println(spells.stream().mapToInt(spell -> spell.costs).sum()
                    + ":" + spells.stream().map(Spell::name).collect(joining("->")));
//            testSpellSequence(spells, 1216, true);
        }
    }

    private Player createBoss() {
        return new Player("Boss", 51, 9, 0);
    }

    private Wizard createPlayer() {
        return new Wizard("Player", 50, 0, 0, 500);
    }

    private void testSpellSequence(List<Spell> spells, int expectedPayedManas, boolean hardMode) {
        final Wizard player = createPlayer();
        final Player boss = createBoss();

        for (Spell spell : spells) {
            playPlayer(hardMode, player, boss, spell);

            if (boss.hasLost() && hardModeCondition(hardMode, player)) {
                break;
            }

            playBoss(player, boss);

            if (boss.hasLost() && hardModeCondition(hardMode, player)) {
                break;
            }
        }

        if (!(boss.hasLost() && hardModeCondition(hardMode, player))) {
            System.out.println();
        }
        assertTrue(boss.hasLost() && hardModeCondition(hardMode, player));
        assertEquals(expectedPayedManas, player.payedMana,
                spells.stream().mapToInt(spell -> spell.costs).sum() + "/"
                        + spells.stream().mapToInt(spell -> max(spell.turns, 1) * spell.damage).sum()
                        + ":" + spells.stream().map(Spell::name).collect(joining("->")));
    }

    private void playBoss(Wizard player, Player boss) {
        System.out.println("-- Boss turn --");
        System.out.println(player.toString());
        System.out.println(boss.toString());
        boss.attack(player);
        System.out.println();
    }

    private void playPlayer(boolean hardMode, Wizard player, Player boss, Spell spell) {
        System.out.println("-- Player turn --");
        if (hardMode) {
            player.loseHitpoint();
            System.out.println("Player loses one hitpoint");
        }
        System.out.println(player.toString());
        System.out.println(boss.toString());
        try {
            player.castSpell(spell);
        } catch (IllegalArgumentException e) {
            System.out.println();
        }
        player.attack(boss);
        System.out.println();
    }

    private boolean hardModeCondition(boolean hardMode, Wizard player) {
        return !hardMode || !player.hasLost();
    }
}
