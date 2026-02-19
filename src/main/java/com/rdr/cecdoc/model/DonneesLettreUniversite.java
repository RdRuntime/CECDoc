package com.rdr.cecdoc.model;

import com.rdr.cecdoc.util.NormalisationTexte;

import java.util.Locale;

public final class DonneesLettreUniversite {
    private final String civiliteSouhaitee;
    private final String prenomUsage;
    private final String prenomEtatCivil;
    private final String nom;
    private final String adressePostale;
    private final String telephonePortable;
    private final String courriel;
    private final String ine;
    private final String nomAdresseUniversite;
    private final String explicationParcours;
    private final String genreActuel;
    private final String villeActuelle;

    public DonneesLettreUniversite(String civiliteSouhaitee, String prenomUsage, String prenomEtatCivil, String nom, String adressePostale, String telephonePortable, String courriel, String ine, String nomAdresseUniversite, String explicationParcours, String genreActuel, String villeActuelle) {
        this.civiliteSouhaitee = normaliserCivilite(civiliteSouhaitee);
        this.prenomUsage = NormalisationTexte.normaliserNomPropre(prenomUsage);
        this.prenomEtatCivil = NormalisationTexte.normaliserNomPropre(prenomEtatCivil);
        this.nom = NormalisationTexte.normaliserNomPropre(nom);
        this.adressePostale = NormalisationTexte.normaliserTexte(adressePostale);
        this.telephonePortable = NormalisationTexte.normaliserTexte(telephonePortable);
        this.courriel = NormalisationTexte.normaliserTexte(courriel).toLowerCase(Locale.ROOT);
        this.ine = NormalisationTexte.normaliserTexte(ine).toUpperCase(Locale.ROOT);
        this.nomAdresseUniversite = NormalisationTexte.normaliserTexte(nomAdresseUniversite);
        this.explicationParcours = NormalisationTexte.normaliserTexte(explicationParcours);
        this.genreActuel = normaliserGenre(genreActuel);
        this.villeActuelle = NormalisationTexte.normaliserNomPropre(villeActuelle);
    }

    public String prenomUsage() {
        return prenomUsage;
    }

    public String prenomEtatCivil() {
        return prenomEtatCivil;
    }

    public String nomMajuscules() {
        return nom.toUpperCase(Locale.ROOT);
    }

    public String adressePostale() {
        return adressePostale;
    }

    public String telephonePortable() {
        return telephonePortable;
    }

    public String courriel() {
        return courriel;
    }

    public String ine() {
        return ine;
    }

    public String nomAdresseUniversite() {
        return nomAdresseUniversite;
    }

    public String explicationParcoursMilieuPhrase() {
        return NormalisationTexte.minusculerPourMilieuPhrase(explicationParcours);
    }

    public String villeActuelle() {
        return villeActuelle;
    }

    public boolean genreNonBinaire() {
        return "non-binaire".equals(genreActuel);
    }

    public boolean genreFeminin() {
        return "féminin".equals(genreActuel);
    }

    public String civiliteEntete() {
        if (genreNonBinaire()) {
            return "Mx";
        }
        if (genreFeminin()) {
            return "Madame";
        }
        return "Monsieur";
    }

    public String identiteEntete() {
        String parenthese = prenomEtatCivil.isBlank() ? "" : " (" + prenomEtatCivil + ")";
        return civiliteEntete() + " " + prenomUsage + parenthese + " " + nomMajuscules();
    }

    public String marqueurEtatCivilOppose() {
        if (genreNonBinaire()) {
            return "";
        }
        if (genreFeminin()) {
            return "masculin";
        }
        return "féminin";
    }

    public String phraseCiviliteDemandee() {
        if (genreNonBinaire()) {
            return "Ne fasse pas figurer la civilité, conformément à la décision du Défenseur des Droits.";
        }
        return "Fassent uniquement figurer la civilité « " + civiliteEntete() + " », conformément à la décision du Défenseur des Droits.";
    }

    private static String normaliserCivilite(String valeur) {
        String propre = NormalisationTexte.normaliserTexte(valeur).toLowerCase(Locale.ROOT);
        if (propre.startsWith("mad")) {
            return "Madame";
        }
        if (propre.startsWith("mx")) {
            return "Mx";
        }
        return "Monsieur";
    }

    private static String normaliserGenre(String valeur) {
        String propre = NormalisationTexte.normaliserTexte(valeur).toLowerCase(Locale.ROOT);
        if (propre.startsWith("f")) {
            return "féminin";
        }
        if (propre.startsWith("m")) {
            return "masculin";
        }
        return "non-binaire";
    }
}
