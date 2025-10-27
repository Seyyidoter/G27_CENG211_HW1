package com.esports.logic;

import com.esports.model.Gamer;
import com.esports.model.Match;
import com.esports.model.Medal;

/**
 * Computes and stores total points, average per match, and medals for each gamer.
 * Uses parallel arrays corresponding to gamer indices.
 */
public class PointsBoard {

    private final Gamer[] gamers;
    private final int[] totalPoints;
    private final double[] averagePerMatch;
    private final Medal[] medals;

    private static final int MATCHES_PER_GAMER = 15;

    public PointsBoard(Gamer[] allGamers) {
        if (allGamers == null) {
            this.gamers = new Gamer[0];
            this.totalPoints = new int[0];
            this.averagePerMatch = new double[0];
            this.medals = new Medal[0];
        } else {
            // Defensive copy of gamers array
            this.gamers = new Gamer[allGamers.length];
            for (int i = 0; i < allGamers.length; i++) {
                this.gamers[i] = new Gamer(allGamers[i]); // Copy constructor
            }

            int size = allGamers.length;
            this.totalPoints = new int[size];
            this.averagePerMatch = new double[size];
            this.medals = new Medal[size];
        }
    }

    /** Calculates the season stats using the MatchManagement 2D array */
    public void calculateSeasonResults(Match[][] allGamerMatches) {
        if (gamers.length == 0 || allGamerMatches == null || gamers.length != allGamerMatches.length) return;

        for (int i = 0; i < gamers.length; i++) {
            Match[] gamerMatches = allGamerMatches[i];
            int total = 0;

            for (Match match : gamerMatches) {
                if (match != null) total += match.getMatchPoints();
            }

            totalPoints[i] = total;
            averagePerMatch[i] = (double) total / MATCHES_PER_GAMER;
            medals[i] = Medal.fromTotalPoints(total);
        }
    }

    /** Returns a defensive copy of the highest-scoring gamer */
    public Gamer getHighestScoringGamer() {
        if (totalPoints.length == 0) return null;

        int bestIndex = 0;
        for (int i = 1; i < totalPoints.length; i++) {
            if (totalPoints[i] > totalPoints[bestIndex]) bestIndex = i;
        }

        return new Gamer(gamers[bestIndex]);
    }

    // --- Getters (safe) ---
    public Gamer[] getGamers() {
        Gamer[] copy = new Gamer[gamers.length];
        for (int i = 0; i < gamers.length; i++) copy[i] = new Gamer(gamers[i]);
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
        int[] copy = new int[totalPoints.length];
        System.arraycopy(totalPoints, 0, copy, 0, totalPoints.length);
        return copy;
    }

    public Medal[] getAllMedals() {
        Medal[] copy = new Medal[medals.length];
        System.arraycopy(medals, 0, copy, 0, medals.length);
        return copy;
    }
}
