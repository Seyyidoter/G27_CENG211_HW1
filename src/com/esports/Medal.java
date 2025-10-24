package com.esports;

/**
 * Represents a fixed set of medal types for gamers.
 * There are four possible medals:
 *  - GOLD
 *  - SILVER
 *  - BRONZE
 *  - NONE
 *
 * Each medal type corresponds to a specific total point range.
 *   GOLD   → 2000 and above
 *   SILVER → 1200–1999
 *   BRONZE → 700–1199
 *   NONE   → below 700
 */
public class Medal {

    private final String name;

    // Constructor is private to prevent creating new medal types.
    private Medal(String name) {
        this.name = name;
    }

    // Predefined medal objects.
    public static final Medal GOLD   = new Medal("GOLD");
    public static final Medal SILVER = new Medal("SILVER");
    public static final Medal BRONZE = new Medal("BRONZE");
    public static final Medal NONE   = new Medal("NONE");

    /**
     * Determines the appropriate medal based on the given total points.
     *
     * @param totalPoints total score of a gamer
     * @return the corresponding Medal object
     */
    public static Medal fromTotalPoints(int totalPoints) {
        if (totalPoints >= 2000) {
            return GOLD;
        } else if (totalPoints >= 1200) {
            return SILVER;
        } else if (totalPoints >= 700) {
            return BRONZE;
        } else {
            return NONE;
        }
    }

    /**
     * Returns the name of the medal as text (e.g., "GOLD").
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * Compares two Medal objects by their names.
     *
     * @param obj another object to compare
     * @return true if both represent the same medal type
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Medal other = (Medal) obj;
        return this.name.equals(other.name);
    }

}
