package com.rdr.cecdoc.ui;

import com.rdr.cecdoc.util.NormalisationTexte;

final class SynchronisationPrenomsFormulaires {
    private SynchronisationPrenomsFormulaires() {
    }

    static Sortie synchroniser(Entree entree) {
        Entree valeur = entree == null ? Entree.vide() : entree;

        String prenomsUsagePartages = premiereValeurNonVide(
                valeur.prenomsUsagePrincipal(),
                valeur.prenomUsageAdministration(),
                valeur.prenomUsageUniversite()
        );
        String prenomUsagePartageSimple = premierPrenom(prenomsUsagePartages);
        String prenomsUsagePrincipal = prenomsUsagePartages.isBlank() ? valeur.prenomsUsagePrincipal() : prenomsUsagePartages;
        String prenomUsageAdministration = prenomUsagePartageSimple.isBlank() ? valeur.prenomUsageAdministration() : prenomUsagePartageSimple;
        String prenomUsageUniversite = prenomUsagePartageSimple.isBlank() ? valeur.prenomUsageUniversite() : prenomUsagePartageSimple;

        String prenomsEtatCivilPrincipal = valeur.prenomsEtatCivilPrincipal();
        String prenomEtatCivilUniversite = valeur.prenomEtatCivilUniversite();
        String prenomsEtatCivilAdministration = valeur.prenomsEtatCivilAdministration();
        String prenomsNaissanceAdministration = valeur.prenomsNaissanceAdministration();

        if (valeur.changementPrenomsPrincipal()) {
            String prenomsNaissancePartages = premiereValeurNonVide(
                    valeur.prenomsEtatCivilPrincipal(),
                    valeur.prenomsNaissanceAdministration()
            );
            if (!prenomsNaissancePartages.isBlank()) {
                prenomsEtatCivilPrincipal = prenomsNaissancePartages;
                prenomsNaissanceAdministration = prenomsNaissancePartages;
            }

            String prenomsEtatCivilSecondaires = premiereValeurNonVide(
                    valeur.prenomsEtatCivilAdministration(),
                    valeur.prenomEtatCivilUniversite()
            );
            if (!prenomsEtatCivilSecondaires.isBlank()) {
                prenomsEtatCivilAdministration = prenomsEtatCivilSecondaires;
                prenomEtatCivilUniversite = premierPrenom(prenomsEtatCivilSecondaires);
            }
        } else {
            String prenomsEtatCivilPartages = premiereValeurNonVide(
                    valeur.prenomsEtatCivilPrincipal(),
                    valeur.prenomsEtatCivilAdministration(),
                    valeur.prenomEtatCivilUniversite()
            );
            if (!prenomsEtatCivilPartages.isBlank()) {
                prenomsEtatCivilPrincipal = prenomsEtatCivilPartages;
                prenomsEtatCivilAdministration = prenomsEtatCivilPartages;
                prenomEtatCivilUniversite = premierPrenom(prenomsEtatCivilPartages);
            }
        }

        return new Sortie(
                prenomsUsagePrincipal,
                prenomUsageUniversite,
                prenomUsageAdministration,
                prenomsEtatCivilPrincipal,
                prenomEtatCivilUniversite,
                prenomsEtatCivilAdministration,
                prenomsNaissanceAdministration
        );
    }

    private static String premierPrenom(String valeur) {
        return NormalisationTexte.extrairePremierPrenom(valeur);
    }

    private static String premiereValeurNonVide(String... valeurs) {
        if (valeurs == null) {
            return "";
        }
        for (String valeur : valeurs) {
            String texte = NormalisationTexte.normaliserTexte(valeur);
            if (!texte.isBlank()) {
                return texte;
            }
        }
        return "";
    }

    record Entree(
            boolean changementPrenomsPrincipal,
            String prenomsUsagePrincipal,
            String prenomUsageUniversite,
            String prenomUsageAdministration,
            String prenomsEtatCivilPrincipal,
            String prenomEtatCivilUniversite,
            String prenomsEtatCivilAdministration,
            String prenomsNaissanceAdministration
    ) {
        Entree {
            prenomsUsagePrincipal = NormalisationTexte.normaliserTexte(prenomsUsagePrincipal);
            prenomUsageUniversite = NormalisationTexte.normaliserTexte(prenomUsageUniversite);
            prenomUsageAdministration = NormalisationTexte.normaliserTexte(prenomUsageAdministration);
            prenomsEtatCivilPrincipal = NormalisationTexte.normaliserTexte(prenomsEtatCivilPrincipal);
            prenomEtatCivilUniversite = NormalisationTexte.normaliserTexte(prenomEtatCivilUniversite);
            prenomsEtatCivilAdministration = NormalisationTexte.normaliserTexte(prenomsEtatCivilAdministration);
            prenomsNaissanceAdministration = NormalisationTexte.normaliserTexte(prenomsNaissanceAdministration);
        }

        static Entree vide() {
            return new Entree(false, "", "", "", "", "", "", "");
        }
    }

    record Sortie(
            String prenomsUsagePrincipal,
            String prenomUsageUniversite,
            String prenomUsageAdministration,
            String prenomsEtatCivilPrincipal,
            String prenomEtatCivilUniversite,
            String prenomsEtatCivilAdministration,
            String prenomsNaissanceAdministration
    ) {
    }
}
