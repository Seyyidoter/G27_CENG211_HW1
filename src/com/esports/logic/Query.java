package com.esports.logic;

import com.esports.model.Game;
import com.esports.model.Gamer;
import com.esports.model.Match;
import com.esports.model.Medal;

/**
 * Query list:
 * 1- The highest-scoring match (by Match Points).
 * 2- In the lowest-scoring match, the most contributing game and its contribution value.
 * 3- The match with the lowest bonus points.
 * 4- The highest-scoring gamer (Nickname, Name, Total Points, Average Per Match, Medal).
 * 5- The total tournament points across all 1500 matches.
 * 6- The medal distribution (counts and percentages).
 * Notes for safety/robustness:
 * - We defensively handle empty / null inputs to avoid NPEs.
 */
public class Query {

    private final Match[][] allGamerMatches;
    private final Gamer[] allGamers;
    private final PointsBoard pointsBoard;

    /**
     * Constructs the Query object with already-simulated data.
     *
     * @param allGamerMatches 2D match grid: [gamerIndex][matchIndex]
     * @param allGamers       gamer array (same ordering as in PointsBoard)
     * @param pointsBoard     the computed season stats per gamer
     */
    public Query(Match[][] allGamerMatches, Gamer[] allGamers, PointsBoard pointsBoard) {
        // Store references. We assume caller already gave us the "safe" / copied versions.
        // (MatchManagement.getAllGamerMatches() already does deep copy; PointsBoard.getGamers() clones.)
        this.allGamerMatches = allGamerMatches;
        this.allGamers = allGamers;
        this.pointsBoard = pointsBoard;
    }

    /**
     * Executes and prints all 6 queries to stdout in the exact order required by the assignment.
     */
    public void printAllQueries() {
        printHighestScoringMatch();
        System.out.println(); // blank line between queries

        printLowestScoringMatch();
        System.out.println();

        printLowestBonusMatch();
        System.out.println();

        printHighestScoringGamer();
        System.out.println();

        printTotalTournamentPoints();
        System.out.println();

        printMedalDistribution();
    }

    /* -------------------------------------------------
       Query 1: Highest-Scoring Match (by Match Points)
       ------------------------------------------------- */
    public void printHighestScoringMatch() {
        System.out.println("1. Highest-Scoring Match");

        Match bestMatch = findFirstNonNullMatch();
        if (bestMatch == null) {
            System.out.println("No matches found.");
            return;
        }

        int maxPoints = bestMatch.getMatchPoints();

        for (Match[] row : allGamerMatches) {
            if (row == null) continue;
            for (Match currentMatch : row) {
                if (currentMatch != null && currentMatch.getMatchPoints() > maxPoints) {
                    maxPoints = currentMatch.getMatchPoints();
                    bestMatch = currentMatch;
                }
            }
        }

        System.out.println("Highest-Scoring Match:");
        printMatchDetails(bestMatch);
    }

    /* -------------------------------------------------
       Query 2: Lowest-Scoring Match & Most Contributing Game
     * Outputs the lowest-scoring match (by Match Points) and the most contributing game within it.
       ------------------------------------------------- */
    public void printLowestScoringMatch() {
        System.out.println("2. Lowest-Scoring Match & Most Contributing Game");

        Match worstMatch = findFirstNonNullMatch();
        if (worstMatch == null) {
            System.out.println("No matches found.");
            return;
        }

        int minPoints = worstMatch.getMatchPoints();

        for (Match[] row : allGamerMatches) {
            if (row == null) continue;
            for (Match currentMatch : row) {
                if (currentMatch != null && currentMatch.getMatchPoints() < minPoints) {
                    minPoints = currentMatch.getMatchPoints();
                    worstMatch = currentMatch;
                }
            }
        }

        System.out.println("Lowest-Scoring Match:");
        printMatchDetails(worstMatch);

        // Now find the most contributing game in that worst match
        Game[] games = worstMatch.getGames();
        int[] rounds = worstMatch.getRounds();

        int maxContribution = -1;
        int bestGameIndex = -1;

        for (int i = 0; i < games.length; i++) {
            int contribution = rounds[i] * games[i].getBasePointPerRound();
            if (contribution > maxContribution) {
                maxContribution = contribution;
                bestGameIndex = i;
            }
        }

        Game bestGame = games[bestGameIndex];
        int bestRounds = rounds[bestGameIndex];

        System.out.println("Most Contributing Game in this Match:");
        System.out.println("Game: " + bestGame.getName());
        System.out.println("Contribution: " + bestRounds + " rounds × "
                + bestGame.getBasePointPerRound() + " points = " + maxContribution);
    }

    /* -------------------------------------------------
       Query 3: Match with the Lowest Bonus Points
       We match the sample output structure
       ------------------------------------------------- */
    public void printLowestBonusMatch() {
        System.out.println("3. Match with the Lowest Bonus Points");

        Match lowestBonusMatch = findFirstNonNullMatch();
        if (lowestBonusMatch == null) {
            System.out.println("No matches found.");
            return;
        }

        int minBonus = lowestBonusMatch.getBonusPoints();

        for (Match[] row : allGamerMatches) {
            if (row == null) continue;
            for (Match currentMatch : row) {
                if (currentMatch != null && currentMatch.getBonusPoints() < minBonus) {
                    minBonus = currentMatch.getBonusPoints();
                    lowestBonusMatch = currentMatch;
                }
            }
        }

        System.out.println("Match with Lowest Bonus Points:");
        Game[] games = lowestBonusMatch.getGames();
        System.out.println("Match ID: " + lowestBonusMatch.getId());
        System.out.println("Games: [" + games[0].getName() + ", "
                + games[1].getName() + ", "
                + games[2].getName() + "]");
        System.out.println("Skill Points: " + lowestBonusMatch.getSkillPoints());
        System.out.println("Bonus Points: " + lowestBonusMatch.getBonusPoints());
        System.out.println("Match Points: " + lowestBonusMatch.getMatchPoints());
    }

    /* -------------------------------------------------
       Query 4: Highest-Scoring Gamer
       We print:
       - Nickname
       - Name
       - Total Points
       - Average Per Match
       - Medal
       ------------------------------------------------- */
    public void printHighestScoringGamer() {
        System.out.println("4. Highest-Scoring Gamer");

        int[] totals = pointsBoard.getAllTotalPoints();
        if (totals.length == 0) {
            System.out.println("No gamers found.");
            return;
        }

        int bestIndex = 0;
        for (int i = 1; i < totals.length; i++) {
            if (totals[i] > totals[bestIndex]) {
                bestIndex = i;
            }
        }

        Gamer bestGamer = allGamers[bestIndex];
        int total = pointsBoard.getTotalPoints(bestIndex);
        double avg = pointsBoard.getAveragePerMatch(bestIndex);
        Medal medal = pointsBoard.getMedal(bestIndex);

        System.out.println("Highest-Scoring Gamer:");
        System.out.println("Nickname: " + bestGamer.getNickname());
        System.out.println("Name: " + bestGamer.getRealName());
        System.out.println("Total Points: " + total);
        System.out.printf("Average Per Match: %.2f\n", avg);
        System.out.println("Medal: " + medal.displayName());
    }

    /* -------------------------------------------------
       Query 5: Total Tournament Points
       We should sum Match Points of every match in the entire season.
       ------------------------------------------------- */
    public void printTotalTournamentPoints() {
        System.out.println("5. Total Tournament Points");

        // Sum all gamers' totals to get the global tournament total.
        int[] gamerTotals = pointsBoard.getAllTotalPoints();
        int tournamentTotal = 0;
        for (int v : gamerTotals) {
            tournamentTotal += v;
        }

        // Print in the exact sample style:
        System.out.println("Total Tournament Points across 1500 matches: " + tournamentTotal);
    }

    /* -------------------------------------------------
       Query 6: Medal Distribution
       We print count and percentage of each medal type.
       ------------------------------------------------- */
    public void printMedalDistribution() {
        System.out.println("6. Medal Distribution");

        Medal[] allMedals = pointsBoard.getAllMedals();
        int totalGamers = allMedals.length;

        if (totalGamers == 0) {
            System.out.println("No gamers found.");
            return;
        }

        int goldCount = 0;
        int silverCount = 0;
        int bronzeCount = 0;
        int noneCount = 0;

        for (Medal m : allMedals) {
            if (m == Medal.GOLD) {
                goldCount++;
            } else if (m == Medal.SILVER) {
                silverCount++;
            } else if (m == Medal.BRONZE) {
                bronzeCount++;
            } else {
                noneCount++;
            }
        }

        double goldPct   = (double) goldCount   * 100.0 / totalGamers;
        double silverPct = (double) silverCount * 100.0 / totalGamers;
        double bronzePct = (double) bronzeCount * 100.0 / totalGamers;
        double nonePct   = (double) noneCount   * 100.0 / totalGamers;

        System.out.println("Medal Distribution:");
        System.out.printf("GOLD:   %d gamers (%.1f%%)\n",   goldCount,   goldPct);
        System.out.printf("SILVER: %d gamers (%.1f%%)\n", silverCount, silverPct);
        System.out.printf("BRONZE: %d gamers (%.1f%%)\n", bronzeCount, bronzePct);
        System.out.printf("NONE:   %d gamers (%.1f%%)\n",   noneCount,   nonePct);
    }

    /* =================================================
       Internal helpers
       ================================================= */

    /**
     * Prints detailed info about a single match in the standard report format.
     */
    private void printMatchDetails(Match m) {
        Game[] games = m.getGames();
        int[] rounds = m.getRounds();

        System.out.println("Match ID: " + m.getId());
        System.out.println("Games: [" + games[0].getName() + ", "
                + games[1].getName() + ", "
                + games[2].getName() + "]");
        System.out.println("Rounds: [" + rounds[0] + ", "
                + rounds[1] + ", "
                + rounds[2] + "]");
        System.out.println("Raw Points: " + m.getRawPoints());
        System.out.println("Skill Points: " + m.getSkillPoints());
        System.out.println("Bonus Points: " + m.getBonusPoints());
        System.out.println("Match Points: " + m.getMatchPoints());
    }

    /**
     * Safely finds the first non-null Match in the allGamerMatches grid.
     * If no matches exist, returns null instead of throwing.
     * Prevents null pointer exceptions in edge cases.
     */
    private Match findFirstNonNullMatch() {
        if (allGamerMatches == null) return null;
        for (Match[] row : allGamerMatches) {
            if (row == null) continue;
            for (Match m : row) {
                if (m != null) {
                    return m;
                }
            }
        }
        return null;
    }
}
