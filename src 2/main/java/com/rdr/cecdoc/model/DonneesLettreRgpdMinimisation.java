package com.rdr.cecdoc.model;

import com.rdr.cecdoc.util.NormalisationTexte;

import java.util.Locale;

public final class DonneesLettreRgpdMinimisation {
    private final String prenomsEtatCivil;
    private final String prenomsConnusOrganisme;
    private final String nom;
    private final String adressePostale;
    private final String telephonePortable;
    private final String courriel;
    private final String nomAdresseOrganisme;
    private final String villeRedaction;
    private final String dateRedaction;
    private final String genreDemande;
    private final String dateNaissance;
    private final String lieuNaissance;
    private final String sexeEtatCivil;
    private final String civiliteAffichage;
    private final boolean champsCiviliteGenrePresents;

    public DonneesLettreRgpdMinimisation(String prenomsEtatCivil, String prenomsConnusOrganisme, String nom, String adressePostale, String telephonePortable, String courriel, String nomAdresseOrganisme, String villeRedaction, String dateRedaction, String genreDemande, String dateNaissance, String lieuNaissance, String sexeEtatCivil, String civiliteAffichage, boolean champsCiviliteGenrePresents) {
        this.prenomsEtatCivil = NormalisationTexte.normaliserNomPropre(prenomsEtatCivil);
        this.prenomsConnusOrganisme = NormalisationTexte.normaliserNomPropre(prenomsConnusOrganisme);
        this.nom = NormalisationTexte.normaliserNomPropre(nom);
        this.adressePostale = NormalisationTexte.normaliserTexte(adressePostale);
        this.telephonePortable = NormalisationTexte.normaliserTexte(telephonePortable);
        this.courriel = NormalisationTexte.normaliserTexte(courriel).toLowerCase(Locale.ROOT);
        this.nomAdresseOrganisme = NormalisationTexte.normaliserTexte(nomAdresseOrganisme);
        this.villeRedaction = NormalisationTexte.normaliserNomPropre(villeRedaction);
        this.dateRedaction = NormalisationTexte.normaliserTexte(dateRedaction);
        this.genreDemande = normaliserGenre(genreDemande);
        this.dateNaissance = NormalisationTexte.normaliserTexte(dateNaissance);
        this.lieuNaissance = NormalisationTexte.normaliserNomPropre(lieuNaissance);
        this.sexeEtatCivil = normaliserSexeEtatCivil(sexeEtatCivil);
        this.civiliteAffichage = normaliserCiviliteAffichage(civiliteAffichage);
        this.champsCiviliteGenrePresents = champsCiviliteGenrePresents;
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

    private static String normaliserSexeEtatCivil(String valeur) {
        String texte = NormalisationTexte.normaliserTexte(valeur).toLowerCase(Locale.ROOT);
        if (texte.startsWith("f")) {
            return "féminin";
        }
        return "masculin";
    }

    private static String normaliserCiviliteAffichage(String valeur) {
        String texte = NormalisationTexte.normaliserTexte(valeur).toLowerCase(Locale.ROOT);
        if (texte.contains("pas")) {
            return "pas de civilité";
        }
        if (texte.startsWith("f")) {
            return "féminin";
        }
        return "masculin";
    }

    public String identiteEntete() {
        String identiteReference = FormateurIdentite.assemblerPrenomsNom(prenomsEtatCivil, nomMajuscules());
        String identiteOrganisme = FormateurIdentite.assemblerPrenomsNom(prenomsConnusOrganisme, nomMajuscules());
        if (identiteReference.isBlank()) {
            return identiteOrganisme;
        }
        if (!doitAfficherMentionOrganisme()) {
            return identiteReference;
        }
        String identiteOrganismeFinale = identiteOrganisme.isBlank() ? identiteReference : identiteOrganisme;
        return identiteReference + " (" + identiteOrganismeFinale + " dans vos fichiers)";
    }

    private boolean doitAfficherMentionOrganisme() {
        return !prenomsConnusOrganisme.isBlank();
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

    public String nomAdresseOrganisme() {
        return nomAdresseOrganisme;
    }

    public String villeRedaction() {
        return villeRedaction;
    }

    public String dateRedaction() {
        return dateRedaction;
    }

    public String dateNaissance() {
        return dateNaissance;
    }

    public String lieuNaissance() {
        return lieuNaissance;
    }

    public String prenomsEtatCivil() {
        return prenomsEtatCivil;
    }

    public String prenomEtatCivil() {
        return prenomsEtatCivil;
    }

    public String sexeEtatCivil() {
        return sexeEtatCivil;
    }

    public String civiliteAffichage() {
        return civiliteAffichage;
    }

    public boolean champsCiviliteGenrePresents() {
        return champsCiviliteGenrePresents;
    }

    public String adjectifSousSigne() {
        return switch (genreDemande) {
            case "féminin" -> "soussignée";
            case "non-binaire" -> "soussigné·e";
            default -> "soussigné";
        };
    }

    public String adjectifNe() {
        return switch (genreDemande) {
            case "féminin" -> "née";
            case "non-binaire" -> "né·e";
            default -> "né";
        };
    }

    public String nomMajuscules() {
        return nom.toUpperCase(Locale.ROOT);
    }
}
