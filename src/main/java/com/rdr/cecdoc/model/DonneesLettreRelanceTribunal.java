package com.rdr.cecdoc.model;

import com.rdr.cecdoc.util.NormalisationTexte;

import java.util.Locale;

public final class DonneesLettreRelanceTribunal {
    private final String prenom;
    private final String nom;
    private final String adressePostale;
    private final String telephonePortable;
    private final String courriel;
    private final String adresseTribunal;
    private final String villeRedaction;
    private final String dateRedaction;
    private final String dateDepotEnvoi;
    private final String informationAttendue;
    private final String genreRevendique;
    private final String dateNaissance;
    private final String lieuNaissance;
    private final String referenceDossier;
    private final boolean changementPrenoms;
    private final String prenomsEtatCivil;
    public DonneesLettreRelanceTribunal(String prenom, String nom, String adressePostale, String telephonePortable, String courriel, String adresseTribunal, String villeRedaction, String dateRedaction, String dateDepotEnvoi, String informationAttendue, String genreRevendique, String dateNaissance, String lieuNaissance, String referenceDossier, boolean changementPrenoms, String prenomsEtatCivil) {
        this.prenom = NormalisationTexte.normaliserNomPropre(prenom);
        this.nom = NormalisationTexte.normaliserNomPropre(nom);
        this.adressePostale = NormalisationTexte.normaliserTexte(adressePostale);
        this.telephonePortable = NormalisationTexte.normaliserTexte(telephonePortable);
        this.courriel = NormalisationTexte.normaliserTexte(courriel).toLowerCase(Locale.ROOT);
        this.adresseTribunal = NormalisationTexte.normaliserTexte(adresseTribunal);
        this.villeRedaction = NormalisationTexte.normaliserNomPropre(villeRedaction);
        this.dateRedaction = NormalisationTexte.normaliserTexte(dateRedaction);
        this.dateDepotEnvoi = NormalisationTexte.normaliserTexte(dateDepotEnvoi);
        this.informationAttendue = normaliserInformation(informationAttendue);
        this.genreRevendique = normaliserGenre(genreRevendique);
        this.dateNaissance = NormalisationTexte.normaliserTexte(dateNaissance);
        this.lieuNaissance = NormalisationTexte.normaliserNomPropre(lieuNaissance);
        this.referenceDossier = NormalisationTexte.normaliserTexte(referenceDossier);
        this.changementPrenoms = changementPrenoms;
        this.prenomsEtatCivil = NormalisationTexte.normaliserTexte(prenomsEtatCivil);
    }

    public DonneesLettreRelanceTribunal(String prenom, String nom, String adressePostale, String telephonePortable, String courriel, String adresseTribunal, String villeRedaction, String dateRedaction, String dateDepotEnvoi, String informationAttendue, String genreRevendique, String dateNaissance, String lieuNaissance, String referenceDossier, boolean changementPrenoms) {
        this(prenom, nom, adressePostale, telephonePortable, courriel, adresseTribunal, villeRedaction, dateRedaction, dateDepotEnvoi, informationAttendue, genreRevendique, dateNaissance, lieuNaissance, referenceDossier, changementPrenoms, "");
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

    private static String normaliserInformation(String valeur) {
        String texte = NormalisationTexte.normaliserTexte(valeur).toLowerCase(Locale.ROOT);
        if (texte.contains("audience")) {
            return "date d’audience";
        }
        if (texte.contains("accus")) {
            return "accusé d’enregistrement";
        }
        return "information sur l’instruction";
    }

    public String identiteEntete() {
        if (prenom.isBlank()) {
            return nomMajuscules();
        }
        if (nom.isBlank()) {
            return prenom;
        }
        return prenom + " " + nomMajuscules();
    }

    public String identiteAvecEtatCivilSiNecessaire() {
        if (!changementPrenoms) {
            return identiteEntete();
        }
        return identiteEntete() + " (" + identiteEtatCivil() + " à l'état civil)";
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

    public String adresseTribunal() {
        return adresseTribunal;
    }

    public String villeRedaction() {
        return villeRedaction;
    }

    public String dateRedaction() {
        return dateRedaction;
    }

    public String dateDepotEnvoi() {
        return dateDepotEnvoi;
    }

    public String complementInformationAttendue() {
        return switch (typeInformationAttendue()) {
            case ACCUSE_ENREGISTREMENT -> "d'accusé d'enregistrement";
            case DATE_AUDIENCE -> "de date audience";
            case INFORMATION_INSTRUCTION -> "d'information sur l'instruction";
        };
    }

    public String demandePrincipaleInformationAttendue() {
        return switch (typeInformationAttendue()) {
            case ACCUSE_ENREGISTREMENT -> "me transmettre un accusé d’enregistrement ;";
            case DATE_AUDIENCE -> "m’indiquer une date d’audience ;";
            case INFORMATION_INSTRUCTION -> "me communiquer une information sur l’instruction ;";
        };
    }

    public String demandeSecondaireInformationAttendue() {
        return switch (typeInformationAttendue()) {
            case ACCUSE_ENREGISTREMENT -> "m’indiquer l’état d’avancement du dossier ;";
            case DATE_AUDIENCE -> "me confirmer que la requête est bien enregistrée ;";
            case INFORMATION_INSTRUCTION -> "me confirmer l’enregistrement de la requête ;";
        };
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

    public String suffixeObjetChangementPrenoms() {
        return changementPrenoms ? " et de prénoms" : "";
    }

    public String adjectifSousSigne() {
        return switch (genreRevendique) {
            case "féminin" -> "soussignée";
            case "non-binaire" -> "soussigné·e";
            default -> "soussigné";
        };
    }

    public String signature() {
        return identiteAvecEtatCivilSiNecessaire();
    }

    public String nomMajuscules() {
        return nom.toUpperCase(Locale.ROOT);
    }

    private String identiteEtatCivil() {
        String prenomsEtatCivilEffectifs = prenomsEtatCivil.isBlank() ? prenom : prenomsEtatCivil;
        if (prenomsEtatCivilEffectifs.isBlank()) {
            return nomMajuscules();
        }
        if (nom.isBlank()) {
            return prenomsEtatCivilEffectifs;
        }
        return prenomsEtatCivilEffectifs + " " + nomMajuscules();
    }

    private TypeInformationAttendue typeInformationAttendue() {
        if (informationAttendue.contains("audience")) {
            return TypeInformationAttendue.DATE_AUDIENCE;
        }
        if (informationAttendue.contains("accus")) {
            return TypeInformationAttendue.ACCUSE_ENREGISTREMENT;
        }
        return TypeInformationAttendue.INFORMATION_INSTRUCTION;
    }

    private enum TypeInformationAttendue {
        ACCUSE_ENREGISTREMENT, DATE_AUDIENCE, INFORMATION_INSTRUCTION
    }
}
