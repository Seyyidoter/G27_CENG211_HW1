package com.esports.logic;

import com.esports.model.Game;
import com.esports.model.Gamer;
import com.esports.model.Match;

import java.util.Random;

/**
 * Orchestrates tournament simulation.
 * Holds a 2D grid of matches: [gamerIndex][matchIndex], with 15 matches per gamer.
 * Notes:
 * Gamer and Game are mutable, so we deep-copy them into this class in the constructor.
 * getAllGamerMatches() returns deep copies of Match objects to avoid privacy leaks
 * Optional constructor allows injecting a Random for deterministic tests.
 */
public class MatchManagement {

    private static final int MATCHES_PER_GAMER = 15;

    private final Match[][] allGamerMatches; // [gamer][0..14]
    private final Gamer[] allGamers;         // Mutable
    private final Game[] availableGames;     // Mutable
    private int nextMatchID = 1;
    private final Random random;

    public MatchManagement(Gamer[] gamers, Game[] games) {
        this(gamers, games, new Random());
    }

    public MatchManagement(Gamer[] gamers, Game[] games, Random rng) {
        if (gamers == null || games == null || games.length < 3) {
            this.allGamers = new Gamer[0];
            this.availableGames = new Game[0];
            this.allGamerMatches = new Match[0][0];
            this.random = (rng == null) ? new Random() : rng;
        } else {
            // Deep-copy gamers array (element by element) using Gamer copy ctor
            this.allGamers = new Gamer[gamers.length];
            for (int i = 0; i < gamers.length; i++) {
                this.allGamers[i] = new Gamer(gamers[i]);
            }

            // Deep-copy games array using Game copy ctor
            this.availableGames = new Game[games.length];
            for (int i = 0; i < games.length; i++) {
                this.availableGames[i] = new Game(games[i]);
            }
            this.allGamerMatches = new Match[gamers.length][MATCHES_PER_GAMER];
            this.random = (rng == null) ? new Random() : rng;
        }
    }

    /** Simulates 15 matches per gamer. Each match has 3 distinct games and 1..10 rounds per game. */
    public void simulateTournament() {
        if (allGamers.length == 0) return;

        for (int i = 0; i < allGamers.length; i++) {
            Gamer currentGamer = allGamers[i];

            for (int j = 0; j < MATCHES_PER_GAMER; j++) {
                Match newMatch = generateRandomMatch(nextMatchID++);
                newMatch.computePointsFor(currentGamer);
                // Storing the match as-is is fine; we return defensive copies to callers.
                allGamerMatches[i][j] = newMatch;
            }
        }
    }

    /** Generates a random match with 3 different games and random rounds in [1..10]. */
    private Match generateRandomMatch(int id) {
        Game[] selectedGames = new Game[3];
        int[] selectedRounds = new int[3];

        int count = 0;
        while (count < 3) {
            Game candidate = availableGames[random.nextInt(availableGames.length)];

            boolean alreadyChosen = false;
            for (int j = 0; j < count; j++) {
                if (selectedGames[j].getId() == candidate.getId()) {
                    alreadyChosen = true;
                    break;
                }
            }

            if (!alreadyChosen) {
                selectedGames[count] = candidate;
                selectedRounds[count] = random.nextInt(10) + 1; // [1..10]
                count++;
            }
        }
        return new Match(id, selectedGames, selectedRounds);
    }

    /**
     * Returns a deep defensive copy of the matches grid.
     * The outer and inner arrays are cloned.
     * Each Match is copied using its copy constructor.
     */
    public Match[][] getAllGamerMatches() {
        Match[][] copy = new Match[allGamerMatches.length][MATCHES_PER_GAMER];
        for (int i = 0; i < allGamerMatches.length; i++) {
            for (int j = 0; j < MATCHES_PER_GAMER; j++) {
                Match m = allGamerMatches[i][j];
                if (m != null) copy[i][j] = new Match(m); // deep copy via Match copy-ctor
            }
        }
        return copy;
    }

    public int getNumberOfGamers() {
        return allGamers.length;
    }
}
