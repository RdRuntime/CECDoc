package com.rdr.cecdoc.model;

import com.rdr.cecdoc.util.NormalisationTexte;

import java.util.Locale;

public final class DonneesLettreRelanceMairiePrenom {
    private final String prenomsEtatCivil;
    private final String prenomsDemandes;
    private final String nom;
    private final String adressePostale;
    private final String telephonePortable;
    private final String courriel;
    private final String adresseMairie;
    private final String villeRedaction;
    private final String dateRedaction;
    private final String dateDemande;
    private final String genreActuel;
    private final String dateNaissance;
    private final String lieuNaissance;
    private final String referenceDossier;

    public DonneesLettreRelanceMairiePrenom(String prenomsEtatCivil, String prenomsDemandes, String nom, String adressePostale, String telephonePortable, String courriel, String adresseMairie, String villeRedaction, String dateRedaction, String dateDemande, String genreActuel, String dateNaissance, String lieuNaissance, String referenceDossier) {
        this.prenomsEtatCivil = NormalisationTexte.normaliserTexte(prenomsEtatCivil);
        this.prenomsDemandes = NormalisationTexte.normaliserTexte(prenomsDemandes);
        this.nom = NormalisationTexte.normaliserNomPropre(nom);
        this.adressePostale = NormalisationTexte.normaliserTexte(adressePostale);
        this.telephonePortable = NormalisationTexte.normaliserTexte(telephonePortable);
        this.courriel = NormalisationTexte.normaliserTexte(courriel).toLowerCase(Locale.ROOT);
        this.adresseMairie = NormalisationTexte.normaliserTexte(adresseMairie);
        this.villeRedaction = NormalisationTexte.normaliserNomPropre(villeRedaction);
        this.dateRedaction = NormalisationTexte.normaliserTexte(dateRedaction);
        this.dateDemande = NormalisationTexte.normaliserTexte(dateDemande);
        this.genreActuel = normaliserGenre(genreActuel);
        this.dateNaissance = NormalisationTexte.normaliserTexte(dateNaissance);
        this.lieuNaissance = NormalisationTexte.normaliserNomPropre(lieuNaissance);
        this.referenceDossier = NormalisationTexte.normaliserTexte(referenceDossier);
    }

    private static String normaliserGenre(String valeur) {
        String texte = NormalisationTexte.normaliserTexte(valeur).toLowerCase(Locale.ROOT);
        if (texte.startsWith("f")) {
            return "féminin";
        }
        if (texte.startsWith("n")) {
            return "non-binaire";
        }
        return "masculin";
    }

    public String identiteEntete() {
        return assemblerIdentite(prenomsDemandes(), nomMajuscules());
    }

    public String identiteEnteteAvecEtatCivil() {
        if (prenomsDemandes().equalsIgnoreCase(prenomsEtatCivil())) {
            return identiteEntete();
        }
        return identiteEntete() + " (" + prenomsEtatCivil() + " " + nomMajuscules() + " pour l'état civil)";
    }

    public String prenomsEtatCivil() {
        if (prenomsEtatCivil.isBlank()) {
            return prenomsDemandes;
        }
        return prenomsEtatCivil;
    }

    public String prenomsDemandes() {
        if (prenomsDemandes.isBlank()) {
            return prenomsEtatCivil;
        }
        return prenomsDemandes;
    }

    public String ligneContact() {
        if (telephonePortable.isBlank() && courriel.isBlank()) {
            return "";
        }
        if (telephonePortable.isBlank()) {
            return courriel;
        }
        if (courriel.isBlank()) {
            return telephonePortable;
        }
        return telephonePortable + " – " + courriel;
    }

    public String adressePostale() {
        return adressePostale;
    }

    public String adresseMairie() {
        return adresseMairie;
    }

    public String villeRedaction() {
        return villeRedaction;
    }

    public String dateRedaction() {
        return dateRedaction;
    }

    public String dateDemande() {
        return dateDemande;
    }

    public String dateNaissance() {
        return dateNaissance;
    }

    public String lieuNaissance() {
        return lieuNaissance;
    }

    public String adresseInline() {
        return adressePostale.replaceAll("\\s*\\R\\s*", " ").replaceAll("\\s{2,}", " ").trim();
    }

    public boolean referenceDossierRenseignee() {
        return !referenceDossier.isBlank();
    }

    public String referenceDossier() {
        return referenceDossier;
    }

    public String adjectifSousSigne() {
        return switch (genreActuel) {
            case "féminin" -> "soussignée";
            case "non-binaire" -> "soussigné·e";
            default -> "soussigné";
        };
    }

    public String signature() {
        return identiteEntete();
    }

    public String nomMajuscules() {
        return nom.toUpperCase(Locale.ROOT);
    }

    private String assemblerIdentite(String prenoms, String nomValeur) {
        if (prenoms == null || prenoms.isBlank()) {
            return nomValeur == null ? "" : nomValeur;
        }
        if (nomValeur == null || nomValeur.isBlank()) {
            return prenoms;
        }
        return prenoms + " " + nomValeur;
    }
}
