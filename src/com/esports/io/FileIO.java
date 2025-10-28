package com.esports.io;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.IOException;

public final class FileIO {

    // ---------------- games.csv ----------------
    public static Game[] readGames(String path) {
        // if path comes null than use default
        if (path == null || path.trim().isEmpty()) {
            path = "games.csv";
        }

        int count = 0;
        int skipped = 0; //skipped rows for defensive check

        // count rows
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"))) {
            br.readLine(); // header
            String line;
            // increase count until find empty row
            while ((line = br.readLine()) != null) {
                if (!isBlank(line)) {
                    // min column control
                    String[] p = line.split(",", -1);
                    if (p.length >= 3) {
                        // try to parse numeric values
                        if (isInt(p[0]) && isInt(p[2])) {
                            count++;
                        } else {
                            skipped++;
                        }
                    } else {
                        skipped++;
                    }
                } else {
                    // empty row
                    skipped++;
                }
            }
        } catch (IOException e) {
            System.out.println("readGames count error: " + e.getMessage());
            return new Game[0];
        }

        if (count == 0) {
            // if there is no valid
            System.out.println("readGames: loaded=0, skipped=" + skipped);
            return new Game[0];
        }

        // fill the array
        Game[] out = new Game[count];
        int i = 0;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"))) {
            String line = br.readLine(); // header
            //read until the row is empty
            while ((line = br.readLine()) != null) {
                //empty-row control
                if (isBlank(line)) {
                    continue;}
                // split line and store it in a temp list called p
                String[] p = line.split(",", -1); // ID,Name,BasePoint
                //if the number of items are less than 3, continue
                if (p.length < 3) continue;

                // assign values and create new game object with calling constructor
                try {
                    int id   = parseInt(p[0]);
                    String name = trim(p[1]);
                    int base = parseInt(p[2]);
                    out[i] = new Game(id, name, base);
                    i++; 
                } catch (NumberFormatException ex) {
                    // continue if row has an error
                }
            }
        } catch (IOException e) {
            System.out.println("readGames fill error: " + e.getMessage());
            return new Game[0];
        }

        // if i < count (if number format exception happens) than trim it and copy to new array
        if (i != out.length) {
            Game[] trimmed = new Game[i];
            System.arraycopy(out, 0, trimmed, 0, i);
            System.out.println("readGames: loaded=" + i + ", skipped=" + (skipped + (count - i)));
            return trimmed;
        }
        //return final output 
        System.out.println("readGames: loaded=" + i + ", skipped=" + skipped);
        return out;
    }

    // ---------------- gamers.csv ----------------
    public static Gamer[] readGamers(String path) {
        // if path comes null than use default
        if (path == null || path.trim().isEmpty()) {
            path = "gamers.csv";
        }

        int count = 0;
        int skipped = 0; // skipped row number

        // count rows
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"))) {
            br.readLine(); // header
            String line;
            //increase count until find empty row
            while ((line = br.readLine()) != null) {
                if (!isBlank(line)){
                    // if it's valid count
                    String[] p = line.split(",", -1);
                    if (p.length >= 5 && isInt(p[0]) && isInt(p[4])) {
                        count++;
                    } else {
                        skipped++;
                    }
                } else {
                    skipped++;
                }
            }
        } catch (IOException e) {
            System.out.println("readGamers count error: " + e.getMessage());
            return new Gamer[0];
        }

        if (count == 0) {
            System.out.println("readGamers: loaded=0, skipped=" + skipped);
            return new Gamer[0];
        }

        // create and fill the array with obtained size
        Gamer[] out = new Gamer[count];
        int i = 0;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"))) {
            String line = br.readLine(); // header
            //read until the row is empty
            while ((line = br.readLine()) != null) {
                //empty row control
                if (isBlank(line)) {
                    continue;}

                //store the data in temp list 
                String[] p = line.split(",", -1); // ID,Nickname,Name,Phone,ExperienceYears

                //skip if there is no enough data
                if (p.length < 5) {
                    continue;}
                //assign them and create gamer objects
                try {
                    int id     = parseInt(p[0]);
                    String nick= trim(p[1]);
                    String name= trim(p[2]);
                    String phone = trim(p[3]);
                    int exp    = parseInt(p[4]);
                    out[i++] = new Gamer(id, nick, name, phone, exp);
                } catch (NumberFormatException ex) {
                    // skip rows that throws this error
                }
            }
        } catch (IOException e) {
            System.out.println("readGamers fill error: " + e.getMessage());
            return new Gamer[0];
        }

        // if i < count happens than trim it and copy to new array
        if (i != out.length) {
            Gamer[] trimmed = new Gamer[i];
            System.arraycopy(out, 0, trimmed, 0, i);
            System.out.println("readGamers: loaded=" + i + ", skipped=" + (skipped + (count - i)));
            return trimmed;
        }
        System.out.println("readGamers: loaded=" + i + ", skipped=" + skipped);
        return out;
    }

    // ---------------- helpers ----------------
    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private static String trim(String s) {
        return s == null ? "" : s.trim();
    }

    private static int parseInt(String s) {
        return Integer.parseInt(trim(s));
    }

    // to check parse control faster
    private static boolean isInt(String s) {
        if (s == null) return false;
        String t = s.trim();
        if (t.isEmpty()) return false;
        int start = (t.charAt(0) == '-' || t.charAt(0) == '+') ? 1 : 0;
        if (start == t.length()) return false;
        for (int i = start; i < t.length(); i++) {
            char c = t.charAt(i);
            if (c < '0' || c > '9') return false;
        }
        return true;
    }
}


    private static int parseInt(String s) {
        return Integer.parseInt(trim(s));
    }
}
