package com.esports.model;

/**
 * Represents a single match played by a gamer.
 * Each match consists of exactly 3 different games, each played for 1–10 rounds.
 * Stores:
 * - ID
 * - the games played (3 distinct Game objects)
 * - the number of rounds for each game
 * - computed scores: rawPoints, skillPoints, bonusPoints, matchPoints
 * Includes:
 * - Copy constructor
 * - NPE/illegal-state checks
 * - Privacy-leak prevention via defensive copies
 */
public class Match {

    private static final int GAME_COUNT = 3;

    private int id;

    // Core per-match data
    private Game[] games;   // Length 3, all distinct IDs
    private int[] rounds;   // Length 3, each in [1..10]

    // Calculated
    private int rawPoints;
    private int skillPoints;
    private int bonusPoints;
    private int matchPoints;

    // Main constructor delegates to validation logic
    public Match(int id, Game[] games, int[] rounds) {
        setId(id);
        setGamesAndRounds(games, rounds); // Validates and deep-copies
    }

    // Copy constructor (deep defensive copy)
    public Match(Match other) {
        if (other == null) {
            throw new IllegalArgumentException("Match copy constructor: other cannot be null");
        }
        this.id = other.id;

        // Deep copy for games
        this.games = new Game[GAME_COUNT];
        for (int i = 0; i < GAME_COUNT; i++) {
            this.games[i] = new Game(other.games[i]); // Use Game copy constructor
        }

        // Defensive copy for rounds
        this.rounds = other.rounds.clone();

        // Copy calculated values
        this.rawPoints   = other.rawPoints;
        this.skillPoints = other.skillPoints;
        this.bonusPoints = other.bonusPoints;
        this.matchPoints = other.matchPoints;
    }

    /**
     * Computes rawPoints, skillPoints, bonusPoints, matchPoints for a given gamer,
     */
    public void computePointsFor(Gamer gamer) {
        if (gamer == null) {
            throw new IllegalArgumentException("Gamer cannot be null when computing points.");
        }

        // 1) Raw Points = sum(rounds[i] * basePointPerRound[i])
        int raw = 0;
        for (int i = 0; i < GAME_COUNT; i++) {
            raw += rounds[i] * games[i].getBasePointPerRound();
        }
        this.rawPoints = raw;

        // 2) Skill Points = floor(raw * (1 + min(exp,10) * 0.02))
        int cappedExp = gamer.getCappedExperienceForScoring(); // 0..10
        double multiplier = 1.0 + cappedExp * 0.02;
        this.skillPoints = (int) Math.floor(raw * multiplier);

        // 3) Bonus Points by raw score table
        this.bonusPoints = computeBonus(raw);

        // 4) Match Points = skillPoints + bonusPoints
        this.matchPoints = this.skillPoints + this.bonusPoints;
    }

    private int computeBonus(int raw) {
        if (raw >= 600) return 100;
        if (raw >= 400) return 50;
        if (raw >= 200) return 25;
        return 10;
    }

    // -------- Getters --------

    public int getId() {
        return id;
    }

    /**
     * Returns a defensive deep copy of the games array.
     * Each Game is copied using its copy constructor.
     * This prevents callers from modifying internal state (privacy leak prevention).
     */
    public Game[] getGames() {
        Game[] copy = new Game[GAME_COUNT];
        for (int i = 0; i < GAME_COUNT; i++) {
            copy[i] = new Game(games[i]);
        }
        return copy;
    }

    /**
     * Returns a defensive copy of the rounds array.
     */
    public int[] getRounds() {
        return rounds.clone();
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

    // -------- Setters for core identity / configuration --------

    public void setId(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("Match id must be >= 0");
        }
        this.id = id;
    }

    /**
     * Sets both games[] and rounds[] together so we can validate consistency.
     * This enforces:
     * - non-null arrays
     * - length == 3
     * - each game != null
     * - each rounds[i] in [1..10]
     * - all 3 game IDs must be distinct
     * We deep-copy into internal arrays, so caller's arrays can't mutate us later.
     */
    public void setGamesAndRounds(Game[] gamesIn, int[] roundsIn) {
        if (gamesIn == null || roundsIn == null) {
            throw new IllegalArgumentException("games/rounds cannot be null");
        }
        if (gamesIn.length != GAME_COUNT || roundsIn.length != GAME_COUNT) {
            throw new IllegalArgumentException("A match must have exactly 3 games and 3 round values.");
        }

        Game[] tempGames = new Game[GAME_COUNT];
        int[] tempRounds = new int[GAME_COUNT];

        for (int i = 0; i < GAME_COUNT; i++) {
            if (gamesIn[i] == null) {
                throw new IllegalArgumentException("Game at index " + i + " is null.");
            }

            int r = roundsIn[i];
            if (r < 1 || r > 10) {
                throw new IllegalArgumentException("Round count must be in [1..10], got " + r + " at index " + i);
            }

            // Deep copy of Game so caller can't later mutate original and affect this Match.
            tempGames[i] = new Game(gamesIn[i]);
            tempRounds[i] = r;
        }

        // Distinct game IDs
        int g0 = tempGames[0].getId();
        int g1 = tempGames[1].getId();
        int g2 = tempGames[2].getId();
        if (g0 == g1 || g0 == g2 || g1 == g2) {
            throw new IllegalArgumentException("Match must contain three different games.");
        }

        this.games = tempGames;
        this.rounds = tempRounds;
    }

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
