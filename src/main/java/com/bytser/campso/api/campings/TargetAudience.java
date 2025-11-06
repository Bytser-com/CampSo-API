package com.bytser.campso.api.campings;

public enum TargetAudience {
    Families("Families"),
    Couples("Couples"),
    YoungAdults("Young Adults"),
    Students("Students"),
    Seniors("Seniors"),
    Disabled("Disabled"),
    Mixed("Mixed");

    private final String displayName;

    TargetAudience(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}