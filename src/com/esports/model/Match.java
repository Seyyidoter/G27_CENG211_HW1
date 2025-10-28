package com.esports.model;
package main.java;

/**
 * Each match has exactly 3 different games.
 * For each game, the number of rounds is randomly chosen between 1 and 10.
 
 * Match.java calculates:
 *  - rawPoints
 *  - skillPoints (depends on gamer experience)
 *  - bonusPoints (based on rawPoints range)
 *  - matchPoints = skill + bonus
 */
public class Match {

    // --- FIELDS (DATA) -----------------------------------------------------

    private static final int GAME_COUNT = 3;
    private static final int MIN_ROUND = 1;
    private static final int MAX_ROUND = 10;

    private final int id;          // match ID
    private final Game[] games;    // exactly 3 games in each match
    private final int[] rounds;    // how many rounds played for each game 

    private int rawPoints;         // total raw points for this match
    private int skillPoints;       // after experience multiplier
    private int bonusPoints;       // bonus depending on rawPoints
    private int matchPoints;       // final score = skill + bonus

    // constructor

    public Match(int id, Game[] games, int[] rounds) {
        if (games == null || rounds == null)
            throw new IllegalArgumentException("games/rounds cannot be null.");
        if (games.length != GAME_COUNT || rounds.length != GAME_COUNT)
            throw new IllegalArgumentException("A match must have exactly 3 games and 3 round values.");

        // distinct control
        if (games[0] == null || games[1] == null || games[2] == null)
            throw new IllegalArgumentException("Games cannot be null.");
        if (games[0] == games[1] || games[0] == games[2] || games[1] == games[2])
            throw new IllegalArgumentException("Games in a match must be distinct.");

        // round interval control
        for (int r : rounds) {
            if (r < MIN_ROUND || r > MAX_ROUND)
                throw new IllegalArgumentException("Round must be in [1..10].");
        }

        this.id = id;
        // defensive copy
        this.games  = new Game[] { games[0], games[1], games[2] };
        this.rounds = new int[]  { rounds[0], rounds[1], rounds[2] };
    }

    // --- CALCULATE POINTS ------------------------------------

    /**
     * Compute all points for this match.
     * The gamer’s experience effects the skill multiplier.
     */
    public void computePointsFor(Gamer gamer) {
        if (gamer == null) {
            throw new IllegalArgumentException("Gamer cannot be null.");
        }

        // 1. RAW POINTS = sum(rounds[i] * basePointPerRound[i])
        int raw = 0;
        for (int i = 0; i < 3; i++) {
            int base = games[i].getBasePointPerRound();
            raw += rounds[i] * base;
        }
        this.rawPoints = raw;

        // 2. SKILL POINTS = floor(raw * (1 + min(experience,10)*0.02))
        int exp = gamer.getExperienceYears();
        if (exp > 10) exp = 10;                 // maximum experience used is 10
        double multiplier = 1.0 + exp * 0.02;   // each year gives +2%
        this.skillPoints = (int) Math.floor(raw * multiplier);

        // 3. BONUS POINTS = depends on rawPoints
        this.bonusPoints = computeBonus(raw);
        

        // 4. MATCH POINTS = skill + bonus
        this.matchPoints = this.skillPoints + this.bonusPoints;
    }

    // --- BONUS CALCULATION -------------------------------------------------
    // According to homework table:
    // 0–199 → +10
    // 200–399 → +25
    // 400–599 → +50
    // ≥600 → +100
    private int computeBonus(int raw) {
        if (raw >= 600) return 100;
        if (raw >= 400) return 50;
        if (raw >= 200) return 25;
        if (raw >= 0)   return 10;
        return 0; // (raw cannot be negative, but just in case)
    }

    // --- GETTERS -----------------------------------------------------------
    public int getId() { 
        return id; 
    }

    public int getRawPoints() { 
        return rawPoints; 
    }

    public int getSkillPoints() { 
        return skillPoints; 
    }

    public int getBonusPoints() { 
        return bonusPoints; 
    }

    public int getMatchPoints() { 
        return matchPoints; 
    }

    // defensive copies to avoid mutation
    public Game[] getGames()  { 
        return new Game[] { games[0], games[1], games[2] }; 
    }

    public int[]  getRounds() { 
        return new int[]  { rounds[0], rounds[1], rounds[2] }; 
    }




    // --- toString() for printing

    @Override
    public String toString() {
        // for readable text show names, rounds, and all points
        String g0 = games[0].getName();
        String g1 = games[1].getName();
        String g2 = games[2].getName();

        return "Match ID: " + id +
                "\nGames: [" + g0 + ", " + g1 + ", " + g2 + "]" +
                "\nRounds: [" + rounds[0] + ", " + rounds[1] + ", " + rounds[2] + "]" +
                "\nRaw Points: " + rawPoints +
                "\nSkill Points: " + skillPoints +
                "\nBonus Points: " + bonusPoints +
                "\nMatch Points: " + matchPoints + "\n";
    }
}

