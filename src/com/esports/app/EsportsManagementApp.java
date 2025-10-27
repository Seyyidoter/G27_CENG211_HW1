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
 * This class performs the following steps:
 * 1. Loads data from CSV files.
 * 2. Initializes the MatchManagement and PointsBoard.
 * 3. Runs the tournament simulation.
 * 4. Calculates the final season results.
 * 5. Creates a Query object and prints all results. 
 */
public class EsportsManagementApp {

    
    private static final String GAMES_FILE_PATH = "games.csv";
    private static final String GAMERS_FILE_PATH = "gamers.csv";

    
    public static void main(String[] args) {
        
        System.out.println("--- The E-Sports Tournament Challenge Simulation ---");

        // Load Data from CSV files 
        System.out.println("Loading " + GAMES_FILE_PATH + "...");
        Game[] allGames = FileIO.readGames(GAMES_FILE_PATH);
        
        System.out.println("Loading " + GAMERS_FILE_PATH + "...");
        Gamer[] allGamers = FileIO.readGamers(GAMERS_FILE_PATH);

        // --- Data Validation Checks ---
        if (allGamers.length == 0) {
            System.out.println("Error: Could not load gamers or gamers.csv is empty.");
            System.out.println("Please check the file path: " + GAMERS_FILE_PATH);
            return; // Stop execution
        }
        if (allGames.length < 3) {
            // A match requires 3 different games 
            System.out.println("Error: At least 3 games are required in games.csv to run a match.");
            System.out.println("Please check the file path: " + GAMES_FILE_PATH);
            return; // Stop execution
        }

        System.out.println("Loaded " + allGamers.length + " gamers and " + allGames.length + " games.");

        // Initialize Management Classes
        MatchManagement matchManagement = new MatchManagement(allGamers, allGames); 
        PointsBoard pointsBoard = new PointsBoard(allGamers); 

        // Run Simulation
        int totalMatchCount = allGamers.length * 15; // 15 matches per gamer 
        System.out.println("Simulating all " + totalMatchCount + " matches...");
        matchManagement.simulateTournament();
        System.out.println("Simulation complete.");

        // Calculate Season Results
        System.out.println("Calculating season results...");
        
        // Get the (defensive copy) 2D array of all simulated matches
        Match[][] simulatedMatches = matchManagement.getAllGamerMatches();
        
        // Calculate totals, averages, and medals based on these matches
        pointsBoard.calculateSeasonResults(simulatedMatches); 

        // Run Queries and Print Results
        System.out.println("\n--- Tournament Results ---");
        
        // Create the Query object, passing it all the data it needs
        Query query = new Query(
            simulatedMatches,
            pointsBoard.getGamers(), // Get the gamer array copy from the points board
            pointsBoard              // Pass the board itself for stats
        );

        // Print all 6 queries 
        query.printAllQueries();
        
        System.out.println("\n--- Simulation Finished ---");
    }
}
