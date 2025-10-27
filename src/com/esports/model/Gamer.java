package com.esports.model;

/**
 * Represents a gamer loaded from gamers.csv.
 */
public class Gamer {

    private final int id;
    private final String nickname;
    private final String realName;
    private String phoneNumber;
    private int experienceYears;

    public Gamer(int id,
                 String nickname,
                 String realName,
                 String phoneNumber,
                 int experienceYears) {

        this.id = id;
        this.nickname = nickname;
        this.realName = realName;
        this.phoneNumber = phoneNumber;
        this.experienceYears = experienceYears;
    }

    /** Copy constructor for safety */
    public Gamer(Gamer other) {
        this.id = other.id;
        this.nickname = other.nickname;
        this.realName = other.realName;
        this.phoneNumber = other.phoneNumber;
        this.experienceYears = other.experienceYears;
    }

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

    /** We allow phone update because it's personal info */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(int experienceYears) {
        this.experienceYears = Math.max(experienceYears, 0);
    }

    /**
     * Tournament rule: if experienceYears > 10, treat as 10 in multiplier.
     * We'll expose a helper because match scoring needs this.
     */
    public int getCappedExperienceForScoring() {
        return Math.min(experienceYears, 10);
    }

    @Override
    public String toString() {
        return nickname + " (" + realName + ")";
    }
}
