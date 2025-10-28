package com.esports.logic;

import com.esports.model.Gamer;
import com.esports.model.Match;
import com.esports.model.Medal;

/**
 * Aggregates season results per gamer:
 * - total points
 * - averagePerMatch: Total Points / 15.0
 * - medal (derived from total points)
 * Notes:
 * Gamer objects are mutable, so we store deep copies of them.
 * All getters return defensive copies where needed to avoid leaking internal state.
 * Arrays we expose are cloned so callers cannot modify our internal arrays.
 */
public class PointsBoard {

    private final Gamer[] gamers;          // internal deep copies of gamers; never exposed directly
    private final int[] totalPoints;       // Season totals per gamer
    private final double[] averagePerMatch;// Season averages per gamer (15)
    private final Medal[] medals;          // Medal per gamer (derived from total)

    // Each gamer is expected to play exactly 15 matches in the season (PDF requirement).
    private static final int MATCHES_PER_GAMER = 15;

    public PointsBoard(Gamer[] allGamers) {
        if (allGamers == null) {
            this.gamers = new Gamer[0];
            this.totalPoints = new int[0];
            this.averagePerMatch = new double[0];
            this.medals = new Medal[0];
        } else {
            this.gamers = new Gamer[allGamers.length];
            for (int i = 0; i < allGamers.length; i++) {
                this.gamers[i] = new Gamer(allGamers[i]); // copy ctor
            }
            int size = allGamers.length;
            this.totalPoints = new int[size];
            this.averagePerMatch = new double[size];
            this.medals = new Medal[size];
        }
    }

    /**
     * Computes totals, averages, and medals using the 2D match grid.
     * Rules (from the assignment):
     * - Total Points = sum of matchPoints over that gamer's 15 matches.
     * - Average Per Match = Total Points / 15.0 (always divide by 15, not by "played").
     * - Medal is assigned from Total Points.
     * Null safety:
     * - If allGamerMatches or a row is null, we safely assign 0 totals instead of throwing NPE.
     */
    public void calculateSeasonResults(Match[][] allGamerMatches) {
        if (gamers.length == 0 || allGamerMatches == null) return;

        // Be robust to length mismatches: compute up to the shortest bound.
        int bound = Math.min(gamers.length, allGamerMatches.length);

        for (int i = 0; i < bound; i++) {
            Match[] gamerMatches = allGamerMatches[i];
            if (gamerMatches == null) {
                totalPoints[i] = 0;
                averagePerMatch[i] = 0.0;
                medals[i] = Medal.NONE;
                continue;
            }

            int total = 0;
            for (Match match : gamerMatches) {
                if (match != null) {
                    total += match.getMatchPoints();
                }
            }
            totalPoints[i] = total;
            averagePerMatch[i] = total / (double) MATCHES_PER_GAMER;
            medals[i] = Medal.fromTotalPoints(total);
        }

        // If there are more gamers than rows in allGamerMatches, zero-fill the rest.
        for (int i = bound; i < gamers.length; i++) {
            totalPoints[i] = 0;
            averagePerMatch[i] = 0.0;
            medals[i] = Medal.NONE;
        }
    }

    /** Returns a defensive copy of the highest-scoring gamer. */
    public Gamer getHighestScoringGamer() {
        if (totalPoints.length == 0) return null;

        int bestIndex = 0;
        for (int i = 1; i < totalPoints.length; i++) {
            if (totalPoints[i] > totalPoints[bestIndex]) bestIndex = i;
        }
        return new Gamer(gamers[bestIndex]); //Defensive
    }

    // --- Safe getters ---
    public Gamer[] getGamers() {
        Gamer[] copy = new Gamer[gamers.length];
        for (int i = 0; i < gamers.length; i++) {
            copy[i] = new Gamer(gamers[i]);
        }
        return copy;
    }

    public int getTotalPoints(int index) {
        return (index >= 0 && index < totalPoints.length) ? totalPoints[index] : 0;
    }

    public double getAveragePerMatch(int index) {
        return (index >= 0 && index < averagePerMatch.length) ? averagePerMatch[index] : 0.0;
    }

    public Medal getMedal(int index) {
        return (index >= 0 && index < medals.length) ? medals[index] : Medal.NONE;
    }

    public int[] getAllTotalPoints() {
        return totalPoints.clone();
    }

    public Medal[] getAllMedals() {
        return medals.clone();
    }
}
