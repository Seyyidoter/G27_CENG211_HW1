package com.esports.model;

/**
 * Represents a game type (e.g. CS2, Valorant) loaded from games.csv.
 * CSV columns: ID, GameName, BasePointPerRound
 * Matches the homework spec:
 * - has constructors, getters, and setters
 * - supports copy constructor
 */
public class Game {

    private int id;
    private String name;
    private int basePointPerRound;

    // Main constructor
    public Game(int id, String name, int basePointPerRound) {
        setId(id);
        setName(name);
        setBasePointPerRound(basePointPerRound);
    }

    // Copy constructor
    public Game(Game other) {
        if (other == null) {
            throw new IllegalArgumentException("Game copy constructor: other cannot be null");
        }
        this.id = other.id;
        this.name = other.name;
        this.basePointPerRound = other.basePointPerRound;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getBasePointPerRound() {
        return basePointPerRound;
    }

    // Setters (validated)
    public void setId(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("Game id must be >= 0");
        }
        this.id = id;
    }

    public void setName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Game name cannot be null");
        }
        String t = name.trim();
        if (t.isEmpty()) {
            throw new IllegalArgumentException("Game name cannot be empty");
        }
        this.name = t;
    }

    public void setBasePointPerRound(int basePointPerRound) {
        if (basePointPerRound < 0) {
            throw new IllegalArgumentException("Base point per round must be >= 0");
        }
        this.basePointPerRound = basePointPerRound;
    }

    @Override
    public String toString() {
        return name + " (id=" + id + ", base=" + basePointPerRound + ")";
    }
}
