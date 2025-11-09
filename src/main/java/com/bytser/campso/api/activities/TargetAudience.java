package com.bytser.campso.api.activities;

public enum TargetAudience {
    FAMILIES("Families"),
    COUPLES("Couples"),
    YOUNG_ADULTS("Young Adults"),
    STUDENTS("Students"),
    SENIORS("Seniors"),
    DISABLED("Disabled"),
    MIXED("Mixed");

    private final String displayName;

    TargetAudience(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}