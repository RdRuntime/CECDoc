package com.rdr.cecdoc.service.config;

import com.rdr.cecdoc.model.InstantaneDossier;
import com.rdr.cecdoc.model.InstantaneLettreAdministration;
import com.rdr.cecdoc.model.InstantaneLettreUniversite;

import java.util.Objects;

public record EtatDossierPersistant(InstantaneDossier instantane, boolean effacerApresExport,
                                    boolean confirmerQuitterAvecDonnees, boolean memoriserDonneesSaisies,
                                    String themeApplication, String dossierSortieParDefaut,
                                    InstantaneLettreUniversite instantaneLettreUniversite,
                                    InstantaneLettreAdministration instantaneLettreAdministration) {
    public EtatDossierPersistant {
        Objects.requireNonNull(instantane, "instantane");
        themeApplication = normaliserTheme(themeApplication);
        dossierSortieParDefaut = normaliserDossierSortieParDefaut(dossierSortieParDefaut);
        instantaneLettreUniversite = instantaneLettreUniversite == null ? InstantaneLettreUniversite.vide() : instantaneLettreUniversite;
        instantaneLettreAdministration = instantaneLettreAdministration == null ? InstantaneLettreAdministration.vide() : instantaneLettreAdministration;
    }

    public EtatDossierPersistant(InstantaneDossier instantane, boolean effacerApresExport) {
        this(instantane, effacerApresExport, true, true, "defaut", "", InstantaneLettreUniversite.vide(), InstantaneLettreAdministration.vide());
    }

    public EtatDossierPersistant(InstantaneDossier instantane, boolean effacerApresExport, boolean confirmerQuitterAvecDonnees, boolean memoriserDonneesSaisies, String themeApplication, InstantaneLettreUniversite instantaneLettreUniversite, InstantaneLettreAdministration instantaneLettreAdministration) {
        this(instantane, effacerApresExport, confirmerQuitterAvecDonnees, memoriserDonneesSaisies, themeApplication, "", instantaneLettreUniversite, instantaneLettreAdministration);
    }

    public EtatDossierPersistant(InstantaneDossier instantane, boolean effacerApresExport, boolean confirmerQuitterAvecDonnees, boolean memoriserDonneesSaisies, String themeApplication, String dossierSortieParDefaut) {
        this(instantane, effacerApresExport, confirmerQuitterAvecDonnees, memoriserDonneesSaisies, themeApplication, dossierSortieParDefaut, InstantaneLettreUniversite.vide(), InstantaneLettreAdministration.vide());
    }

    private static String normaliserTheme(String valeur) {
        if (valeur == null || valeur.isBlank()) {
            return "defaut";
        }
        String theme = valeur.trim().toLowerCase();
        return switch (theme) {
            case "defaut", "trans", "non_binaire", "lesbien", "intersexe", "rainbow" -> theme;
            default -> "defaut";
        };
    }

    private static String normaliserDossierSortieParDefaut(String valeur) {
        if (valeur == null) {
            return "";
        }
        return valeur.trim();
    }
}
