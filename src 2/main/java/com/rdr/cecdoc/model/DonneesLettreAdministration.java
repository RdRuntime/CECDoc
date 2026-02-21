package com.rdr.cecdoc.model;

import com.rdr.cecdoc.util.NormalisationTexte;

import java.util.Locale;

public final class DonneesLettreAdministration {
    private final String prenomUsage;
    private final String prenomsEtatCivil;
    private final String nom;
    private final String adressePostale;
    private final String telephonePortable;
    private final String courriel;
    private final String adresseDestinataire;
    private final boolean changementPrenom;
    private final String prenomNaissance;
    private final boolean changementSexe;
    private final String sexeAvant;
    private final String sexeApres;
    private final boolean changementPrenomFaitEnMairie;
    private final String numeroDecisionMairie;
    private final String dateDecisionMairie;
    private final String tribunalCompetent;
    private final String numeroJugement;
    private final String villeActuelle;

    public DonneesLettreAdministration(String prenomUsage, String prenomsEtatCivil, String nom, String adressePostale, String telephonePortable, String courriel, String adresseDestinataire, boolean changementPrenom, String prenomNaissance, boolean changementSexe, String sexeAvant, String sexeApres, boolean changementPrenomFaitEnMairie, String numeroDecisionMairie, String dateDecisionMairie, String tribunalCompetent, String numeroJugement, String villeActuelle) {
        this.prenomUsage = NormalisationTexte.normaliserNomPropre(prenomUsage);
        this.prenomsEtatCivil = NormalisationTexte.normaliserNomPropre(prenomsEtatCivil);
        this.nom = NormalisationTexte.normaliserNomPropre(nom);
        this.adressePostale = NormalisationTexte.normaliserTexte(adressePostale);
        this.telephonePortable = NormalisationTexte.normaliserTexte(telephonePortable);
        this.courriel = NormalisationTexte.normaliserTexte(courriel).toLowerCase(Locale.ROOT);
        this.adresseDestinataire = NormalisationTexte.normaliserTexte(adresseDestinataire);
        this.changementPrenom = changementPrenom;
        this.prenomNaissance = NormalisationTexte.normaliserNomPropre(prenomNaissance);
        this.changementSexe = changementSexe;
        this.sexeAvant = normaliserSexe(sexeAvant);
        this.sexeApres = normaliserSexe(sexeApres);
        this.changementPrenomFaitEnMairie = changementPrenomFaitEnMairie;
        this.numeroDecisionMairie = NormalisationTexte.normaliserTexte(numeroDecisionMairie);
        this.dateDecisionMairie = NormalisationTexte.normaliserTexte(dateDecisionMairie);
        this.tribunalCompetent = NormalisationTexte.normaliserNomPropre(tribunalCompetent);
        this.numeroJugement = NormalisationTexte.normaliserTexte(numeroJugement);
        this.villeActuelle = NormalisationTexte.normaliserNomPropre(villeActuelle);
    }

    private static String valeurOuInconnue(String valeur) {
        return valeur == null || valeur.isBlank() ? "XXXX" : valeur;
    }

    private static String normaliserSexe(String valeur) {
        String propre = NormalisationTexte.normaliserTexte(valeur).toLowerCase(Locale.ROOT);
        if (propre.startsWith("f")) {
            return "féminin";
        }
        return "masculin";
    }

    public String prenomUsage() {
        return prenomUsage;
    }

    public String prenomsEtatCivil() {
        return prenomsEtatCivil;
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

    public String adresseDestinataire() {
        return adresseDestinataire;
    }

    public boolean changementPrenom() {
        return changementPrenom;
    }

    public boolean changementSexe() {
        return changementSexe;
    }

    public String villeActuelle() {
        return villeActuelle;
    }

    public boolean changementDouble() {
        return changementPrenom && changementSexe;
    }

    public String identiteEntete() {
        return FormateurIdentite.identiteAvecEtatCivil(prenomUsage, prenomsEtatCivil, nomMajuscules());
    }

    public String premierParagrapheModifications() {
        StringBuilder resultat = new StringBuilder();
        if (changementPrenom) {
            resultat.append("Mes prénoms ont été modifié de ").append(prenomNaissance).append(" ").append(nomMajuscules()).append(" à ").append(prenomsEtatCivil).append(" ").append(nomMajuscules()).append(". ");
        }
        if (changementSexe) {
            resultat.append("Mon sexe à l'état civil a été modifié d'un sexe ").append(sexeAvant).append(" à un sexe ").append(sexeApres).append(". ");
        }
        return resultat.toString().trim();
    }

    public String objetConformite() {
        if (changementDouble() && changementPrenomFaitEnMairie) {
            return "à la décision de l’officier·e d’état civil n° " + valeurOuInconnue(numeroDecisionMairie) + " du " + valeurOuInconnue(dateDecisionMairie) + " et au jugement du " + valeurOuInconnue(tribunalCompetent) + " numéro " + valeurOuInconnue(numeroJugement) + ", en annexe de la présente lettre";
        }
        if (changementSexe) {
            return "au jugement du " + valeurOuInconnue(tribunalCompetent) + " numéro " + valeurOuInconnue(numeroJugement) + ", en annexe de la présente lettre";
        }
        return "à la décision de l’officier·e d’état civil n° " + valeurOuInconnue(numeroDecisionMairie) + " du " + valeurOuInconnue(dateDecisionMairie) + ", en annexe de la présente lettre";
    }

    public String objetPronomsObjet() {
        if (changementDouble()) {
            return "les";
        }
        if (changementPrenom) {
            return "le";
        }
        return "la";
    }

    public String identiteSignature() {
        String premierPrenom = NormalisationTexte.extrairePremierPrenom(prenomUsage);
        if (premierPrenom.isBlank()) {
            premierPrenom = NormalisationTexte.extrairePremierPrenom(prenomsEtatCivil);
        }
        if (premierPrenom.isBlank()) {
            return nomMajuscules();
        }
        return premierPrenom + " " + nomMajuscules();
    }
}
