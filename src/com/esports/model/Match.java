package com.esports.model;

/**
 * Represents a single match played by a gamer.
 * Each match has exactly 3 games, each with 1–10 random rounds.
 *
 * Calculates:
 *  - rawPoints
 *  - skillPoints (depends on experience)
 *  - bonusPoints (based on rawPoints range)
 *  - matchPoints = skill + bonus
 */
public class Match {

    private final int id;
    private final Game[] games;
    private final int[] rounds;

    private int rawPoints;
    private int skillPoints;
    private int bonusPoints;
    private int matchPoints;

    // Constructor
    public Match(int id, Game[] games, int[] rounds) {
        if (games == null || rounds == null || games.length != 3 || rounds.length != 3) {
            throw new IllegalArgumentException("A match must have exactly 3 games and 3 round values.");
        }

        this.id = id;

        // Defensive copy of arrays
        this.games = new Game[3];
        this.rounds = new int[3];
        for (int i = 0; i < 3; i++) {
            this.games[i] = new Game(games[i]);
            this.rounds[i] = rounds[i];
        }
    }

    /** Copy constructor (defensive copy) */
    public Match(Match other) {
        this.id = other.id;

        this.games = new Game[other.games.length];
        for (int i = 0; i < other.games.length; i++) {
            this.games[i] = new Game(other.games[i]);
        }

        this.rounds = new int[other.rounds.length];
        for (int i = 0; i < other.rounds.length; i++) {
            this.rounds[i] = other.rounds[i];
        }

        this.rawPoints = other.rawPoints;
        this.skillPoints = other.skillPoints;
        this.bonusPoints = other.bonusPoints;
        this.matchPoints = other.matchPoints;
    }

    /** Calculates all point values for the given gamer */
    public void computePointsFor(Gamer gamer) {
        if (gamer == null) throw new IllegalArgumentException("Gamer cannot be null.");

        // 1. Raw Points
        int raw = 0;
        for (int i = 0; i < 3; i++) {
            raw += rounds[i] * games[i].getBasePointPerRound();
        }
        this.rawPoints = raw;

        // 2. Skill Points
        int exp = gamer.getCappedExperienceForScoring();
        double multiplier = 1.0 + exp * 0.02;
        this.skillPoints = (int) Math.floor(raw * multiplier);

        // 3. Bonus Points
        this.bonusPoints = computeBonus(raw);

        // 4. Match Points
        this.matchPoints = skillPoints + bonusPoints;
    }

    private int computeBonus(int raw) {
        if (raw >= 600) return 100;
        if (raw >= 400) return 50;
        if (raw >= 200) return 25;
        return 10;
    }

    // --- Getters ---
    public int getId() { return id; }

    public Game[] getGames() {
        Game[] copy = new Game[games.length];
        for (int i = 0; i < games.length; i++) copy[i] = new Game(games[i]);
        return copy;
    }

    public int[] getRounds() {
        int[] copy = new int[rounds.length];
        System.arraycopy(rounds, 0, copy, 0, rounds.length);
        return copy;
    }

    public int getRawPoints() { return rawPoints; }
    public int getSkillPoints() { return skillPoints; }
    public int getBonusPoints() { return bonusPoints; }
    public int getMatchPoints() { return matchPoints; }

    @Override
    public String toString() {
        return "Match ID: " + id +
                "\nGames: [" + games[0].getName() + ", " + games[1].getName() + ", " + games[2].getName() + "]" +
                "\nRounds: [" + rounds[0] + ", " + rounds[1] + ", " + rounds[2] + "]" +
                "\nRaw Points: " + rawPoints +
                "\nSkill Points: " + skillPoints +
                "\nBonus Points: " + bonusPoints +
                "\nMatch Points: " + matchPoints + "\n";
    }
}
