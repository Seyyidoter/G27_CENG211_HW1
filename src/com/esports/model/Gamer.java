package com.esports.model;

/**
 * Represents a gamer loaded from gamers.csv.
 * CSV columns: ID, Nickname, Name, Phone, ExperienceYears.
 * Satisfies:
 * - copy constructor
 * - safe validation to avoid null pointer issues / illegal states.
 */
public class Gamer {

    private int id;
    private String nickname;
    private String realName;
    private String phoneNumber;
    private int experienceYears; // stored >= 0

    // Main constructor
    public Gamer(int id,
                 String nickname,
                 String realName,
                 String phoneNumber,
                 int experienceYears) {

        setId(id);
        setNickname(nickname);
        setRealName(realName);
        setPhoneNumber(phoneNumber);
        setExperienceYears(experienceYears);
    }

    // Copy constructor
    public Gamer(Gamer other) {
        if (other == null) {
            throw new IllegalArgumentException("Gamer copy constructor: other cannot be null");
        }
        this.id = other.id;
        this.nickname = other.nickname;
        this.realName = other.realName;
        this.phoneNumber = other.phoneNumber;
        this.experienceYears = other.experienceYears;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public String getRealName() {
        return realName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public int getExperienceYears() {
        return experienceYears;
    }

    /**
     * For scoring:
     */
    public int getCappedExperienceForScoring() {
        return Math.min(experienceYears, 10);
    }

    // Setters (validated)
    public void setId(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("Gamer id must be >= 0");
        }
        this.id = id;
    }

    public void setNickname(String nickname) {
        if (nickname == null) {
            throw new IllegalArgumentException("Nickname cannot be null");
        }
        String t = nickname.trim();
        if (t.isEmpty()) {
            throw new IllegalArgumentException("Nickname cannot be empty");
        }
        this.nickname = t;
    }

    public void setRealName(String realName) {
        if (realName == null) {
            throw new IllegalArgumentException("Real name cannot be null");
        }
        String t = realName.trim();
        if (t.isEmpty()) {
            throw new IllegalArgumentException("Real name cannot be empty");
        }
        this.realName = t;
    }

    public void setPhoneNumber(String phoneNumber) {
        // Phone can be blank in CSV, so normalize instead of rejecting
        if (phoneNumber == null) {
            this.phoneNumber = "";
        } else {
            this.phoneNumber = phoneNumber.trim();
        }
    }

    public void setExperienceYears(int experienceYears) {
        this.experienceYears = Math.max(experienceYears, 0);
    }

    @Override
    public String toString() {
        return nickname + " (" + realName + ")";
    }
}
