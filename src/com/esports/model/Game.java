package com.esports.model;

/**
 * Represents a game type (e.g. CS2, Valorant) loaded from games.csv.
 * Immutable: once created, it cannot change.
 * CSV columns: ID, GameName, BasePointPerRound
 */
public class Game {

    private final int id;
    private final String name;
    private final int basePointPerRound;

    public Game(int id, String name, int basePointPerRound) {
        this.id = id;
        this.name = name;
        this.basePointPerRound = basePointPerRound;
    }

    /** Copy constructor (defensive copy) */
    public Game(Game other) {
        this.id = other.id;
        this.name = other.name;
        this.basePointPerRound = other.basePointPerRound;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getBasePointPerRound() {
        return basePointPerRound;
    }

    @Override
    public String toString() {
        return name + " (id=" + id + ", base=" + basePointPerRound + ")";
    }
}
