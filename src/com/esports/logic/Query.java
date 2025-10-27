package com.esports.logic;

import com.esports.model.Game;
import com.esports.model.Gamer;
import com.esports.model.Match;
import com.esports.model.Medal;

/**
 * Contains methods required to answer the 6 queries.
 * This class is instantiated after the simulation is complete and
 * receives data (or copies) from MatchManagement and PointsBoard.
 */
public class Query {

    private final Match[][] allGamerMatches;
    private final Gamer[] allGamers;
    private final PointsBoard pointsBoard;

    /**
     * Constructs the Query object.
     *
     * @param allGamerMatches The 2D array of matches from MatchManagement
     * @param allGamers       The 1D array of gamers from PointsBoard
     * @param pointsBoard     The PointsBoard object (which has calculated results)
     */
    public Query(Match[][] allGamerMatches, Gamer[] allGamers, PointsBoard pointsBoard) {
        
        this.allGamerMatches = allGamerMatches;
        this.allGamers = allGamers;
        this.pointsBoard = pointsBoard;
    }

    /**
     * Executes and prints all 6 queries in order to the console.
     */
    public void printAllQueries() {
        printHighestScoringMatch();
        System.out.println(); // Spacer between queries
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

    // --- Query 1: Highest-Scoring Match ---
    /**
     * Query 1: Finds and prints the details of the highest-scoring match. 
     */
    public void printHighestScoringMatch() {
        System.out.println("1. Highest-Scoring Match");

        if (allGamerMatches.length == 0 || allGamerMatches[0].length == 0) {
            System.out.println("No matches found.");
            return;
        }

        Match bestMatch = allGamerMatches[0][0];
        int maxPoints = bestMatch.getMatchPoints();

        for (int i = 0; i < allGamerMatches.length; i++) {
            for (int j = 0; j < allGamerMatches[i].length; j++) {
                Match currentMatch = allGamerMatches[i][j];
                if (currentMatch != null && currentMatch.getMatchPoints() > maxPoints) {
                    maxPoints = currentMatch.getMatchPoints();
                    bestMatch = currentMatch;
                }
            }
        }

        System.out.println("Highest-Scoring Match:");
        printMatchDetails(bestMatch);
    }

    // --- Query 2: Lowest-Scoring Match & Most Contributing Game ---
    /**
     * Query 2: Finds the lowest-scoring match and the most contributing game within it. 
     */
    public void printLowestScoringMatch() {
        System.out.println("2. Lowest-Scoring Match & Most Contributing Game");

        if (allGamerMatches.length == 0 || allGamerMatches[0].length == 0) {
            System.out.println("No matches found.");
            return;
        }

        Match worstMatch = allGamerMatches[0][0];
        int minPoints = worstMatch.getMatchPoints();

        for (int i = 0; i < allGamerMatches.length; i++) {
            for (int j = 0; j < allGamerMatches[i].length; j++) {
                Match currentMatch = allGamerMatches[i][j];
                if (currentMatch != null && currentMatch.getMatchPoints() < minPoints) {
                    minPoints = currentMatch.getMatchPoints();
                    worstMatch = currentMatch;
                }
            }
        }

        System.out.println("Lowest-Scoring Match:");
        printMatchDetails(worstMatch);

        // Now, find the most contributing game in this match
        Game[] games = worstMatch.getGames();
        int[] rounds = worstMatch.getRounds();
        
        int maxContribution = -1;
        int bestGameIndex = -1;

        for (int i = 0; i < 3; i++) {
            int contribution = rounds[i] * games[i].getBasePointPerRound();
            if (contribution > maxContribution) {
                maxContribution = contribution;
                bestGameIndex = i;
            }
        }

        Game bestGame = games[bestGameIndex];
        int bestRounds = rounds[bestGameIndex];

        System.out.println("Most Contributing Game in this Match:");
        System.out.println(" Game: " + bestGame.getName());
        System.out.println(" Contribution: " + bestRounds + " rounds × " 
                         + bestGame.getBasePointPerRound() + " points = " + maxContribution);
    }

    // --- Query 3: Match with the Lowest Bonus Points ---
    /**
     * Query 3: Finds the match that awarded the lowest bonus points. 
     */
    public void printLowestBonusMatch() {
        System.out.println("3. Match with the Lowest Bonus Points");

        if (allGamerMatches.length == 0 || allGamerMatches[0].length == 0) {
            System.out.println("No matches found.");
            return;
        }

        Match lowestBonusMatch = allGamerMatches[0][0];
        int minBonus = lowestBonusMatch.getBonusPoints();

        for (int i = 0; i < allGamerMatches.length; i++) {
            for (int j = 0; j < allGamerMatches[i].length; j++) {
                Match currentMatch = allGamerMatches[i][j];
                if (currentMatch != null && currentMatch.getBonusPoints() < minBonus) {
                    minBonus = currentMatch.getBonusPoints();
                    lowestBonusMatch = currentMatch;
                }
            }
        }
        
        System.out.println("Match with Lowest Bonus Points:");
        
        Game[] games = lowestBonusMatch.getGames();
        System.out.println(" Match ID: " + lowestBonusMatch.getId());
        System.out.println(" Games: [" + games[0].getName() + ", " + games[1].getName() + ", " + games[2].getName() + "]");
        System.out.println(" Skill Points: " + lowestBonusMatch.getSkillPoints());
        System.out.println(" Bonus Points: " + lowestBonusMatch.getBonusPoints());
        System.out.println(" Match Points: " + lowestBonusMatch.getMatchPoints());
    }

    // --- Query 4: Highest-Scoring Gamer ---
    /**
     * Query 4: Finds the gamer with the highest total points for the season. 
     */
    public void printHighestScoringGamer() {
        System.out.println("4. Highest-Scoring Gamer");

        int[] totalPoints = pointsBoard.getAllTotalPoints();
        if (totalPoints.length == 0) {
            System.out.println("No gamers found.");
            return;
        }

        // We need to find the *index* of the highest-scoring gamer
        int bestIndex = 0;
        for (int i = 1; i < totalPoints.length; i++) {
            if (totalPoints[i] > totalPoints[bestIndex]) {
                bestIndex = i;
            }
        }

        // Get all info based on this index
        Gamer bestGamer = allGamers[bestIndex];
        int total = pointsBoard.getTotalPoints(bestIndex);
        double avg = pointsBoard.getAveragePerMatch(bestIndex);
        Medal medal = pointsBoard.getMedal(bestIndex);

        System.out.println("Highest-Scoring Gamer:");
        System.out.println(" Nickname: " + bestGamer.getNickname());
        System.out.println(" Name: " + bestGamer.getRealName());
        System.out.println(" Total Points: " + total);
        
        System.out.printf(" Average Per Match: %.2f\n", avg);
        System.out.println(" Medal: " + medal.toString()); // .toString() returns "GOLD", etc.
    }

    // --- Query 5: Total Tournament Points ---
    /**
     * Query 5: Calculates the sum of all match points across all matches. 
     */
    public void printTotalTournamentPoints() {
        System.out.println("5. Total Tournament Points");
        
        // It's more efficient to sum the gamer totals from PointsBoard
        int[] gamerTotals = pointsBoard.getAllTotalPoints();
        
        int tournamentTotal = 0; 
        for (int i = 0; i < gamerTotals.length; i++) {
            tournamentTotal += gamerTotals[i];
        }

        int totalMatches = allGamers.length * 15; 

        System.out.println("Total Tournament Points across " + totalMatches + " matches: " + tournamentTotal);
    }

    // --- Query 6: Medal Distribution ---
    /**
     * Query 6: Calculates and prints the medal distribution (count and percentage). 
     */
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

        for (int i = 0; i < totalGamers; i++) {
            Medal m = allMedals[i];
            
            // Compare using the static final objects from the Medal class
            if (m.equals(Medal.GOLD)) {
                goldCount++;
            } else if (m.equals(Medal.SILVER)) {
                silverCount++;
            } else if (m.equals(Medal.BRONZE)) {
                bronzeCount++;
            } else { 
                noneCount++;
            }
        }
        
        // Calculate percentages
        double goldPct = (double) goldCount / totalGamers * 100.0;
        double silverPct = (double) silverCount / totalGamers * 100.0;
        double bronzePct = (double) bronzeCount / totalGamers * 100.0;
        double nonePct = (double) noneCount / totalGamers * 100.0;

        System.out.println("Medal Distribution:");
        // Sample output [cite: 143]
        System.out.printf(" GOLD:   %d gamers (%.1f%%)\n", goldCount, goldPct);
        System.out.printf(" SILVER: %d gamers (%.1f%%)\n", silverCount, silverPct);
        System.out.printf(" BRONZE: %d gamers (%.1f%%)\n", bronzeCount, bronzePct);
        System.out.printf(" NONE:   %d gamers (%.1f%%)\n", noneCount, nonePct);
    }


    // --- Helper Method ---
    /**
     * A helper method to print common match details for Queries 1 and 2.
     * @param m The Match object to print details for.
     */
    private void printMatchDetails(Match m) {
        Game[] games = m.getGames();
        int[] rounds = m.getRounds();
        
        System.out.println(" Match ID: " + m.getId());
        System.out.println(" Games: [" + games[0].getName() + ", " + games[1].getName() + ", " + games[2].getName() + "]");
        System.out.println(" Rounds: [" + rounds[0] + ", " + rounds[1] + ", " + rounds[2] + "]");
        System.out.println(" Raw Points: " + m.getRawPoints());
        System.out.println(" Skill Points: " + m.getSkillPoints());
        System.out.println(" Bonus Points: " + m.getBonusPoints());
        System.out.println(" Match Points: " + m.getMatchPoints());
    }
}