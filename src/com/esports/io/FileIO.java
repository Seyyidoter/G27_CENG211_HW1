package com.esports.io;

import com.esports.model.Game;
import com.esports.model.Gamer;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class FileIO {

    // ---------------- games.csv ----------------
    public static Game[] readGames(String path) {
        int count = 0;

        // 1) Count Pass (UTF-8)
        Path gamesPath = Path.of(path);
        try (BufferedReader br = Files.newBufferedReader(gamesPath, StandardCharsets.UTF_8)) {
            br.readLine(); // Header
            String line;
            while ((line = br.readLine()) != null) {
                if (!isBlank(line)) count++;
            }
        } catch (IOException e) {
            System.out.println("ReadGames count error: " + e.getMessage());
            return new Game[0];
        }

        Game[] out = new Game[count];
        int i = 0;

        // 2) Fill Pass (UTF-8)
        try (BufferedReader br = Files.newBufferedReader(gamesPath, StandardCharsets.UTF_8)) {
            br.readLine(); // Header
            String line;
            while ((line = br.readLine()) != null) {
                if (isBlank(line)) continue;

                String[] p = line.split(",", -1);
                if (p.length < 3) continue;

                try {
                    int id   = parseInt(p[0]);
                    String name = trim(p[1]);
                    int base = parseInt(p[2]);

                    // --- Minimal validations ---
                    if (id < 0) continue;           // Do not accept negative ID
                    if (base < 0) continue;         // No negative base points
                    if (name.isEmpty()) continue;   // Skip empty name row

                    out[i++] = new Game(id, name, base);
                } catch (NumberFormatException ex) {
                    // Skip malformed line
                }
            }
        } catch (IOException e) {
            System.out.println("ReadGames fill error: " + e.getMessage());
            return new Game[0];
        }

        // Trim if necessary
        if (i != out.length) {
            Game[] trimmed = new Game[i];
            System.arraycopy(out, 0, trimmed, 0, i);
            return trimmed;
        }
        return out;
    }

    // ---------------- gamers.csv ----------------
    public static Gamer[] readGamers(String path) {
        int count = 0;


        // 1) Count Pass (UTF-8)
        Path gamersPath = Path.of(path);
        try (BufferedReader br = Files.newBufferedReader(gamersPath, StandardCharsets.UTF_8)) {
            br.readLine(); // Header
            String line;
            while ((line = br.readLine()) != null) {
                if (!isBlank(line)) count++;
            }
        } catch (IOException e) {
            System.out.println("readGamers count error: " + e.getMessage());
            return new Gamer[0];
        }

        Gamer[] out = new Gamer[count];
        int i = 0;

        // 2) Fill Pass (UTF-8)
        try (BufferedReader br = Files.newBufferedReader(gamersPath, StandardCharsets.UTF_8)) {
            br.readLine(); // Header
            String line;
            while ((line = br.readLine()) != null) {
                if (isBlank(line)) continue;

                String[] p = line.split(",", -1);
                if (p.length < 5) continue;

                try {
                    int id       = parseInt(p[0]);
                    String nick  = trim(p[1]);
                    String name  = trim(p[2]);
                    String phone = trim(p[3]);
                    int exp      = parseInt(p[4]);

                    // --- Minimal validations ---
                    if (id < 0) continue;          // Do not accept negative ID
                    if (exp < 0) exp = 0;          // Experience cannot be negative
                    if (nick.isEmpty()) continue;  // Skip empty nickname
                    if (name.isEmpty()) continue;  // Skip empty name

                    out[i++] = new Gamer(id, nick, name, phone, exp);
                } catch (NumberFormatException ex) {
                    // Skip malformed line
                }
            }
        } catch (IOException e) {
            System.out.println("readGamers fill error: " + e.getMessage());
            return new Gamer[0];
        }

        if (i != out.length) {
            Gamer[] trimmed = new Gamer[i];
            System.arraycopy(out, 0, trimmed, 0, i);
            return trimmed;
        }
        return out;
    }

    // ---------------- Helpers ----------------
    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private static String trim(String s) {
        return (s == null) ? "" : s.trim();
    }

    private static int parseInt(String s) {
        return Integer.parseInt(trim(s));
    }
}