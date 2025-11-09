package com.bytser.campso.api.users;

public enum CountryCode {
    NL("Netherlands", Language.NL),
    BE("Belgium", Language.NL),
    DE("Germany", Language.DE),
    FR("France", Language.FR),
    ES("Spain", Language.ES),
    IT("Italy", Language.IT),
    UK("United Kingdom", Language.EN);

    private final String displayName;
    private final Language countryLanguage;

    CountryCode(String displayName, Language countryLanguage) {
        this.displayName = displayName;
        this.countryLanguage = countryLanguage;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Language getCountryLanguage() {
        return countryLanguage;
    }
}
