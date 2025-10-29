package com.esports.model;

/**
 * Represents a fixed set of medal types for gamers.
 * There are four possible medals:
 *  - GOLD
 *  - SILVER
 *  - BRONZE
 *  - NONE
 * Each medal type corresponds to a specific total point range.
 *   GOLD   → 2000 and above
 *   SILVER → 1200–1999
 *   BRONZE → 700–1199
 *   NONE   → below 700
 */

public enum Medal {
    GOLD, SILVER, BRONZE, NONE;

    public static Medal fromTotalPoints(int totalPoints) {
        if (totalPoints >= 4400) return GOLD;
        if (totalPoints >= 3800) return SILVER;
        if (totalPoints >= 3200)  return BRONZE;
        return NONE;
    }

    public String displayName() {
        return switch (this) {
            case GOLD -> "GOLD";
            case SILVER -> "SILVER";
            case BRONZE -> "BRONZE";
            default -> "NONE";
        };
    }
}
