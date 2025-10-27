package com.esports.logic;

import com.esports.model.Game;
import com.esports.model.Gamer;
import com.esports.model.Match;

import java.util.Random;

/**
 * Manages all matches in the tournament.
 * Stores a 2D array of Match objects [GamerIndex][MatchIndex 0–14].
 * Simulates 15 matches for each gamer.
 */
public class MatchManagement {

    private static final int MATCHES_PER_GAMER = 15;
    private final Match[][] allGamerMatches;
    private final Gamer[] allGamers;
    private final Game[] availableGames;
    private int nextMatchID = 1;
    private final Random random = new Random();

    public MatchManagement(Gamer[] gamers, Game[] games) {
        if (gamers == null || games == null || games.length < 3) {
            this.allGamers = new Gamer[0];
            this.availableGames = new Game[0];
            this.allGamerMatches = new Match[0][0];
        } else {
            // Defensive copy for gamers
            this.allGamers = new Gamer[gamers.length];
            for (int i = 0; i < gamers.length; i++) {
                this.allGamers[i] = new Gamer(gamers[i]); // Copy constructor
            }

            // Defensive copy for games
            this.availableGames = new Game[games.length];
            for (int i = 0; i < games.length; i++) {
                this.availableGames[i] = new Game(games[i]); // Copy constructor
            }

            // Internal match storage (fresh)
            this.allGamerMatches = new Match[gamers.length][MATCHES_PER_GAMER];
        }
    }

    /** Creates and simulates all 15 matches for each gamer */
    public void simulateTournament() {
        if (allGamers.length == 0) return;

        for (int i = 0; i < allGamers.length; i++) {
            Gamer currentGamer = allGamers[i];

            for (int j = 0; j < MATCHES_PER_GAMER; j++) {
                Match newMatch = generateRandomMatch(nextMatchID++);
                newMatch.computePointsFor(currentGamer);
                allGamerMatches[i][j] = newMatch;
            }
        }
    }

    /** Generates a random match with 3 different games and random round counts (1–10). */
    private Match generateRandomMatch(int id) {
        Game[] selectedGames = new Game[3];
        int[] selectedRounds = new int[3];

        int count = 0;

        while (count < 3) {
            // Pick a random game candidate
            Game candidate = availableGames[random.nextInt(availableGames.length)];

            // Check if we already picked this game before
            boolean alreadyChosen = false;
            for (int j = 0; j < count; j++) {
                if (selectedGames[j].getId() == candidate.getId()) {
                    alreadyChosen = true;
                    break;
                }
            }

            // If it's not a duplicate, accept it
            if (!alreadyChosen) {
                selectedGames[count] = candidate;
                selectedRounds[count] = random.nextInt(10) + 1; // 1-10 rounds
                count++;
            }
            // else: duplicate -> loop again and try another random game
        }

        return new Match(id, selectedGames, selectedRounds);
    }

    /** Returns a defensive copy of all matches */
    public Match[][] getAllGamerMatches() {
        Match[][] copy = new Match[allGamerMatches.length][MATCHES_PER_GAMER];
        for (int i = 0; i < allGamerMatches.length; i++) {
            for (int j = 0; j < MATCHES_PER_GAMER; j++) {
                Match m = allGamerMatches[i][j];
                if (m != null) copy[i][j] = new Match(m);
            }
        }
        return copy;
    }

    public int getNumberOfGamers() {
        return allGamers.length;
    }
}
