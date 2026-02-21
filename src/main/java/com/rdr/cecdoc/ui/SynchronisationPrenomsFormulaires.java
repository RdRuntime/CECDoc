package com.rdr.cecdoc.ui;

import com.rdr.cecdoc.util.NormalisationTexte;

final class SynchronisationPrenomsFormulaires {
    private SynchronisationPrenomsFormulaires() {
    }

    static Sortie synchroniser(Entree entree) {
        Entree valeur = entree == null ? Entree.vide() : entree;

        boolean principalPorteLesPrenomsNaissance = valeur.changementPrenomsPrincipal();
        boolean changementPrenomEffectif = valeur.changementPrenomEffectifAdministration();

        String prenomsEtatCivilCanoniques = principalPorteLesPrenomsNaissance ? premiereValeurNonVide(valeur.prenomsEtatCivilAdministration(), valeur.prenomEtatCivilUniversite(), valeur.prenomsUsagePrincipal()) : premiereValeurNonVide(valeur.prenomsEtatCivilPrincipal(), valeur.prenomsEtatCivilAdministration(), valeur.prenomEtatCivilUniversite(), valeur.prenomsUsagePrincipal());

        String prenomsNaissanceCanoniques = principalPorteLesPrenomsNaissance ? premiereValeurNonVide(valeur.prenomsEtatCivilPrincipal(), valeur.prenomsNaissanceAdministration(), prenomsEtatCivilCanoniques) : premiereValeurNonVide(valeur.prenomsNaissanceAdministration(), prenomsEtatCivilCanoniques);

        if (changementPrenomEffectif) {
            prenomsEtatCivilCanoniques = premiereValeurNonVide(prenomsEtatCivilCanoniques, valeur.prenomsUsagePrincipal(), prenomsNaissanceCanoniques);
            prenomsNaissanceCanoniques = premiereValeurNonVide(prenomsNaissanceCanoniques, principalPorteLesPrenomsNaissance ? valeur.prenomsEtatCivilPrincipal() : valeur.prenomsNaissanceAdministration(), prenomsEtatCivilCanoniques);
        } else {
            String baseSansChangement = premiereValeurNonVide(prenomsNaissanceCanoniques, prenomsEtatCivilCanoniques);
            prenomsNaissanceCanoniques = baseSansChangement;
            prenomsEtatCivilCanoniques = baseSansChangement;
        }

        String prenomsUsageCanoniques = premiereValeurNonVide(valeur.prenomsUsagePrincipal(), changementPrenomEffectif ? prenomsEtatCivilCanoniques : prenomsNaissanceCanoniques);

        String prenomsEtatCivilPrincipal = principalPorteLesPrenomsNaissance ? premiereValeurNonVide(prenomsNaissanceCanoniques, valeur.prenomsEtatCivilPrincipal(), prenomsEtatCivilCanoniques) : premiereValeurNonVide(prenomsEtatCivilCanoniques, valeur.prenomsEtatCivilPrincipal(), prenomsNaissanceCanoniques);

        String prenomsEtatCivilAdministration = premiereValeurNonVide(prenomsEtatCivilCanoniques, valeur.prenomsEtatCivilAdministration(), prenomsEtatCivilPrincipal);
        if (!changementPrenomEffectif) {
            prenomsEtatCivilAdministration = premiereValeurNonVide(prenomsNaissanceCanoniques, prenomsEtatCivilAdministration);
        }

        String prenomsNaissanceAdministration = changementPrenomEffectif ? premiereValeurNonVide(prenomsNaissanceCanoniques, valeur.prenomsNaissanceAdministration(), prenomsEtatCivilPrincipal) : premiereValeurNonVide(prenomsNaissanceCanoniques, prenomsEtatCivilAdministration);

        String prenomsUsagePrincipal = premiereValeurNonVide(prenomsUsageCanoniques, valeur.prenomsUsagePrincipal());
        String prenomUsageUniversite = premiereValeurNonVide(premierPrenom(prenomsUsagePrincipal), premierPrenom(prenomsEtatCivilAdministration));
        String prenomUsageAdministration = premiereValeurNonVide(premierPrenom(prenomsUsagePrincipal), premierPrenom(prenomsEtatCivilAdministration));
        if (changementPrenomEffectif) {
            prenomUsageAdministration = premiereValeurNonVide(premierPrenom(prenomsEtatCivilAdministration), prenomUsageAdministration);
        } else {
            prenomUsageAdministration = premiereValeurNonVide(premierPrenom(prenomsUsagePrincipal), prenomUsageAdministration);
        }

        String prenomEtatCivilUniversite = premiereValeurNonVide(premierPrenom(principalPorteLesPrenomsNaissance ? prenomsEtatCivilCanoniques : prenomsEtatCivilPrincipal), premierPrenom(prenomsEtatCivilAdministration), premierPrenom(valeur.prenomEtatCivilUniversite()));

        return new Sortie(prenomsUsagePrincipal, prenomUsageUniversite, prenomUsageAdministration, prenomsEtatCivilPrincipal, prenomEtatCivilUniversite, prenomsEtatCivilAdministration, prenomsNaissanceAdministration);
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

    record Entree(boolean changementPrenomsPrincipal, boolean changementPrenomEffectifAdministration,
                  String prenomsUsagePrincipal, String prenomUsageUniversite, String prenomUsageAdministration,
                  String prenomsEtatCivilPrincipal, String prenomEtatCivilUniversite,
                  String prenomsEtatCivilAdministration, String prenomsNaissanceAdministration) {
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
            return new Entree(false, false, "", "", "", "", "", "", "");
        }
    }

    record Sortie(String prenomsUsagePrincipal, String prenomUsageUniversite, String prenomUsageAdministration,
                  String prenomsEtatCivilPrincipal, String prenomEtatCivilUniversite,
                  String prenomsEtatCivilAdministration, String prenomsNaissanceAdministration) {
    }
}
