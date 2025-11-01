package com.esports.io;

import com.esports.model.Game;
import com.esports.model.Gamer;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.IOException;

public final class FileIO {

    // ---------------- games.csv ----------------
    public static Game[] readGames(String path) {
        int count = 0;

        // 1) Count Pass
        //    - open reader
        //    - skip header
        //    -count non-blank data rows
        try (BufferedReader br =
                     new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"))) {
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

        // 2) Fill Pass
        //    - open reader again
        //    - skip header
        //    - parse each row; minimal validation; build Game[]
        try (BufferedReader br =
                     new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"))) {
            br.readLine(); // Header
            String line;
            while ((line = br.readLine()) != null) {
                if (isBlank(line)) continue;

                String[] p = line.split(",", -1); // ID, Name, BasePoint
                if (p.length < 3) continue;

                try {
                    int id   = parseInt(p[0]);
                    String name = trim(p[1]);
                    int base = parseInt(p[2]);

                    // --- Minimal validations defensive ---
                    if (id < 0) continue;         // no negative ID
                    if (base < 0) continue;       // no negative base points
                    if (name.isEmpty()) continue; // skip empty name

                    out[i++] = new Game(id, name, base);
                } catch (NumberFormatException ex) {
                    // Skip malformed line
                }
            }
        } catch (IOException e) {
            System.out.println("ReadGames fill error: " + e.getMessage());
            return new Game[0];
        }

        // Trim if needed (some rows may be skipped while filling)
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

        // 1) Count Pass 
        //    - open reader
        //    - skip header
        //    - count non-blank data rows
        try (BufferedReader br =
                     new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"))) {
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

        // 2) Fill Pass 
        //    - open reader again
        //    - skip header
        //    - parse each row; minimal validation; build Gamer[]
        try (BufferedReader br =
                     new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"))) {
            br.readLine(); // Header
            String line;
            while ((line = br.readLine()) != null) {
                if (isBlank(line)) continue;

                String[] p = line.split(",", -1); // ID, Nickname, Name, Phone, ExperienceYears
                if (p.length < 5) continue;

                try {
                    int id       = parseInt(p[0]);
                    String nick  = trim(p[1]);
                    String name  = trim(p[2]);
                    String phone = trim(p[3]);
                    int exp      = parseInt(p[4]);

                    // --- Minimal validations defensive ---
                    if (id < 0) continue;        // no negative ID
                    if (exp < 0) exp = 0;        // negative exp -> clamp to 0
                    if (nick.isEmpty()) continue;
                    if (name.isEmpty()) continue;

                    out[i++] = new Gamer(id, nick, name, phone, exp);
                } catch (NumberFormatException ex) {
                    // Skip malformed line
                }
            }
        } catch (IOException e) {
            System.out.println("readGamers fill error: " + e.getMessage());
            return new Gamer[0];
        }

        // Trim if needed (some rows may be skipped while filling)
        if (i != out.length) {
            Gamer[] trimmed = new Gamer[i];
            System.arraycopy(out, 0, trimmed, 0, i);
            return trimmed;
        }
        return out;
    }

    // ---------------- Helpers ----------------
    // blank line check
    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    // safe trim (null -> "")
    private static String trim(String s) {
        return (s == null) ? "" : s.trim();
    }

    // parse int after trim
    private static int parseInt(String s) {
        return Integer.parseInt(trim(s));
    }
}
