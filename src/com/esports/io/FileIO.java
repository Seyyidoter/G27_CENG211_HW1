package com.esports.io;

import com.esports.model.Game;
import com.esports.model.Gamer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileIO {

    // ---------------- games.csv ----------------
    public static Game[] readGames(String path) {
        int count = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
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

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.readLine(); // Skip header
            String line;
            while ((line = br.readLine()) != null) {
                if (isBlank(line)) continue;
                String[] p = line.split(",", -1);
                if (p.length < 3) continue;

                try {
                    int id = parseInt(p[0]);
                    String name = trim(p[1]);
                    int base = parseInt(p[2]);
                    out[i++] = new Game(id, name, base);
                } catch (NumberFormatException ex) {
                    // Skip row
                }
            }
        } catch (IOException e) {
            System.out.println("ReadGames fill error: " + e.getMessage());
            return new Game[0];
        }

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

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
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

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.readLine(); // Skip header
            String line;
            while ((line = br.readLine()) != null) {
                if (isBlank(line)) continue;
                String[] p = line.split(",", -1);
                if (p.length < 5) continue;

                try {
                    int id = parseInt(p[0]);
                    String nick = trim(p[1]);
                    String name = trim(p[2]);
                    String phone = trim(p[3]);
                    int exp = parseInt(p[4]);
                    out[i++] = new Gamer(id, nick, name, phone, exp);
                } catch (NumberFormatException ex) {
                    // Skip row
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
        return s == null ? "" : s.trim();
    }

    private static int parseInt(String s) {
        return Integer.parseInt(trim(s));
    }
}
