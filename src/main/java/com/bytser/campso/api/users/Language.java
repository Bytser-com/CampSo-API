package com.bytser.campso.api.users;

public enum Language {
    NL("Dutch", true),
    EN("English", true),
    DE("German", false),
    FR("French", false),
    ES("Spanish", false),
    IT("Italian", false);

    private final String displayName;
    private boolean supported = false;

    Language(String displayName, boolean supported) {
        this.displayName = displayName;
        this.supported = supported;
    }

    public String getDisplayName() {
        return displayName;
    }
    public boolean isSupported() {
        return supported;
    }
}
