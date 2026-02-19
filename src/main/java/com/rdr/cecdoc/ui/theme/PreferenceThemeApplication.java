package com.rdr.cecdoc.ui.theme;

import java.util.Locale;

public enum PreferenceThemeApplication {
    DEFAUT("defaut", "Par défaut"), TRANS("trans", "Drapeau trans"), NON_BINAIRE("non_binaire", "Drapeau non-binaire"), LESBIEN("lesbien", "Drapeau lesbien"), INTERSEXE("intersexe", "Drapeau intersexe"), RAINBOW("rainbow", "Drapeau arc-en-ciel");

    private final String code;
    private final String libelle;

    PreferenceThemeApplication(String code, String libelle) {
        this.code = code;
        this.libelle = libelle;
    }

    public String code() {
        return code;
    }

    public boolean estForce() {
        return this != DEFAUT;
    }

    public ModeTheme modeForce() {
        return switch (this) {
            case TRANS -> ModeTheme.TRANS;
            case NON_BINAIRE -> ModeTheme.NON_BINAIRE;
            case LESBIEN -> ModeTheme.LESBIEN;
            case INTERSEXE -> ModeTheme.INTERSEXE;
            case RAINBOW -> ModeTheme.RAINBOW;
            case DEFAUT -> null;
        };
    }

    public static PreferenceThemeApplication depuisCode(String code) {
        if (code == null) {
            return DEFAUT;
        }
        String normalise = code.trim().toLowerCase(Locale.ROOT);
        for (PreferenceThemeApplication valeur : values()) {
            if (valeur.code.equals(normalise)) {
                return valeur;
            }
        }
        return DEFAUT;
    }

    @Override
    public String toString() {
        return libelle;
    }
}
