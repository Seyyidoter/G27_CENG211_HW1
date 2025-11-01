package com.esports.app;

import com.esports.io.FileIO;
import com.esports.logic.MatchManagement;
import com.esports.logic.PointsBoard;
import com.esports.logic.Query;
import com.esports.model.Game;
import com.esports.model.Gamer;
import com.esports.model.Match;

/**
 * Main application class for the E-Sports Tournament Challenge.
 * Orchestrates the loading, simulation, and reporting steps.
 */
public class EsportsManagementApp {

    private static final String GAMES_FILE_PATH = "src/resources/games.csv";
    private static final String GAMERS_FILE_PATH = "src/resources/gamers.csv";

    public static void main(String[] args) {

        // 1. Load Data
        Game[] allGames = FileIO.readGames(GAMES_FILE_PATH);
        Gamer[] allGamers = FileIO.readGamers(GAMERS_FILE_PATH);

        // 2. Validate data (null/size checks)
        if (!isDataValid(allGamers, allGames)) {
            return; // Stop execution if validation fails
        }

        // 3. Initialize Management Classes
        MatchManagement matchManagement = new MatchManagement(allGamers, allGames);
        PointsBoard pointsBoard = new PointsBoard(allGamers);

        // 4. Run Simulation
        matchManagement.simulateTournament();

        // 5. Calculate Season Results
        Match[][] simulatedMatches = matchManagement.getAllGamerMatches();
        pointsBoard.calculateSeasonResults(simulatedMatches);

        // 6. Run queries and print results
        runAndPrintQueries(simulatedMatches, pointsBoard);
    }

    /**
     * Checks if the loaded data meets the minimum requirements to run the simulation.
     * Prints error messages if validation fails.
     * @param allGamers The loaded array of gamers.
     * @param allGames The loaded array of games.
     * @return true if data is valid, false otherwise.
     */
    private static boolean isDataValid(Gamer[] allGamers, Game[] allGames) {
        if (allGamers == null || allGamers.length == 0) {
            System.out.println("Error: Could not load gamers or gamers.csv is empty.");
            System.out.println("Please check the file path: " + GAMERS_FILE_PATH);
            return false;
        }
        if (allGames == null || allGames.length < 3) {
            // A match requires 3 different games
            System.out.println("Error: At least 3 games are required in games.csv to run a match.");
            System.out.println("Please check the file path: " + GAMES_FILE_PATH);
            return false;
        }
        return true;
    }

    /**
     * Creates a Query object and prints all tournament results to the console.
     * @param simulatedMatches The 2D array of all completed matches.
     * @param pointsBoard The PointsBoard containing calculated season totals.
     */
    private static void runAndPrintQueries(Match[][] simulatedMatches, PointsBoard pointsBoard) {
        Query query = new Query(
                simulatedMatches,
                pointsBoard.getGamers(), // Get the gamer array copy from the points board
                pointsBoard              // Pass the board itself for stats
        );

        query.printAllQueries();
    }
}