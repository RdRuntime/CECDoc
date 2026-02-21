package com.rdr.cecdoc.model;

import com.rdr.cecdoc.util.NormalisationTexte;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class DonneesRecoursRefusChangementSexe {
    private final String prenom;
    private final String nom;
    private final String adressePostale;
    private final String telephonePortable;
    private final String courriel;
    private final String villeCourAppel;
    private final String adresseCourAppel;
    private final String villeRedaction;
    private final String dateRedaction;
    private final String genreRevendique;
    private final String dateNaissance;
    private final String lieuNaissance;
    private final String qualiteAvocat;
    private final String nomAvocat;
    private final String barreauAvocat;
    private final String adresseAvocat;
    private final String telephoneAvocat;
    private final String courrielAvocat;
    private final String villeTribunal;
    private final String dateJugement;
    private final String motifRefus;
    private final boolean changementPrenoms;
    private final String numeroJugement;
    private final String prenomsEtatCivil;
    private final String prenomsDemandes;

    public DonneesRecoursRefusChangementSexe(String prenom, String nom, String adressePostale, String telephonePortable, String courriel, String villeCourAppel, String adresseCourAppel, String villeRedaction, String dateRedaction, String genreRevendique, String dateNaissance, String lieuNaissance, String qualiteAvocat, String nomAvocat, String barreauAvocat, String adresseAvocat, String telephoneAvocat, String courrielAvocat, String villeTribunal, String dateJugement, String motifRefus, boolean changementPrenoms, String numeroJugement, String prenomsEtatCivil, String prenomsDemandes) {
        this.prenom = NormalisationTexte.normaliserNomPropre(prenom);
        this.nom = NormalisationTexte.normaliserNomPropre(nom);
        this.adressePostale = NormalisationTexte.normaliserTexte(adressePostale);
        this.telephonePortable = NormalisationTexte.normaliserTexte(telephonePortable);
        this.courriel = NormalisationTexte.normaliserTexte(courriel).toLowerCase(Locale.ROOT);
        this.villeCourAppel = NormalisationTexte.normaliserNomPropre(villeCourAppel);
        this.adresseCourAppel = NormalisationTexte.normaliserTexte(adresseCourAppel);
        this.villeRedaction = NormalisationTexte.normaliserNomPropre(villeRedaction);
        this.dateRedaction = NormalisationTexte.normaliserTexte(dateRedaction);
        this.genreRevendique = normaliserGenre(genreRevendique);
        this.dateNaissance = NormalisationTexte.normaliserTexte(dateNaissance);
        this.lieuNaissance = NormalisationTexte.normaliserNomPropre(lieuNaissance);
        this.qualiteAvocat = normaliserQualiteAvocat(qualiteAvocat);
        this.nomAvocat = NormalisationTexte.normaliserNomPropre(nomAvocat);
        this.barreauAvocat = NormalisationTexte.normaliserTexte(barreauAvocat);
        this.adresseAvocat = NormalisationTexte.normaliserTexte(adresseAvocat);
        this.telephoneAvocat = NormalisationTexte.normaliserTexte(telephoneAvocat);
        this.courrielAvocat = NormalisationTexte.normaliserTexte(courrielAvocat).toLowerCase(Locale.ROOT);
        this.villeTribunal = NormalisationTexte.normaliserNomPropre(villeTribunal);
        this.dateJugement = NormalisationTexte.normaliserTexte(dateJugement);
        this.motifRefus = NormalisationTexte.normaliserTexte(motifRefus);
        this.changementPrenoms = changementPrenoms;
        this.numeroJugement = NormalisationTexte.normaliserTexte(numeroJugement);
        this.prenomsEtatCivil = NormalisationTexte.normaliserTexte(prenomsEtatCivil);
        this.prenomsDemandes = NormalisationTexte.normaliserTexte(prenomsDemandes);
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

    private static String normaliserQualiteAvocat(String valeur) {
        String texte = NormalisationTexte.normaliserTexte(valeur).toLowerCase(Locale.ROOT);
        if (texte.startsWith("avocat·e")) {
            return "avocat·e";
        }
        if (texte.startsWith("avocate")) {
            return "avocate";
        }
        return "avocat";
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
        return FormateurIdentite.identiteAvecEtatCivil(prenomsDemandesEffectifs(), prenomsEtatCivilEffectifs(), nomMajuscules());
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

    public String villeCourAppel() {
        return villeCourAppel;
    }

    public String adresseCourAppel() {
        return adresseCourAppel;
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

    public String ligneAvocat() {
        List<String> segments = new ArrayList<>();
        segments.add("Agissant par l’intermédiaire de " + qualiteAvocatAvecAccord() + " Me " + nomAvocat);
        if (!barreauAvocat.isBlank()) {
            segments.add(barreauAvocat);
        }
        if (!adresseAvocat.isBlank()) {
            segments.add(NormalisationTexte.aplatirLignes(adresseAvocat));
        }
        if (!telephoneAvocat.isBlank()) {
            segments.add(telephoneAvocat);
        }
        if (!courrielAvocat.isBlank()) {
            segments.add(courrielAvocat);
        }
        return String.join(", ", segments);
    }

    private String qualiteAvocatAvecAccord() {
        return switch (qualiteAvocat) {
            case "avocate" -> "mon avocate";
            case "avocat·e" -> "mon·a avocat·e";
            default -> "mon avocat";
        };
    }

    public String nomAvocat() {
        return nomAvocat;
    }

    public String numeroJugement() {
        return numeroJugement;
    }

    public String dateJugement() {
        return dateJugement;
    }

    public String motifRefusMilieuPhrase() {
        if (motifRefus.isBlank()) {
            return "";
        }
        if (motifRefus.length() == 1) {
            return motifRefus.toLowerCase(Locale.ROOT);
        }
        return Character.toLowerCase(motifRefus.charAt(0)) + motifRefus.substring(1);
    }

    public String genreRevendique() {
        return genreRevendique;
    }

    public String adjectifSousSigne() {
        return switch (genreRevendique) {
            case "féminin" -> "soussignée";
            case "non-binaire" -> "soussigné·e";
            default -> "soussigné";
        };
    }

    public String adjectifNe() {
        return switch (genreRevendique) {
            case "féminin" -> "née";
            case "non-binaire" -> "né·e";
            default -> "né";
        };
    }

    public String adjectifConnu() {
        return switch (genreRevendique) {
            case "féminin" -> "connue";
            case "non-binaire" -> "connu·e";
            default -> "connu";
        };
    }

    public boolean changementPrenoms() {
        return changementPrenoms;
    }

    public String objetRecours() {
        return "Objet : Appel (recours) contre le jugement refusant la modification " + complementModificationEtatCivil() + " dans les actes de l’état civil – articles 61-5 à 61-8 du Code civil et articles 1055-5 à 1055-10 du Code de procédure civile";
    }

    public String descriptionRequeteRefusee() {
        return "en modification " + complementModificationEtatCivil() + " à l’état civil";
    }

    public String descriptionProcedureChangement() {
        if (changementPrenoms) {
            return "la procédure de changement de la mention du sexe et, le cas échéant, des prénoms";
        }
        return "la procédure de changement de la mention du sexe";
    }

    public String ligneDemandeModification() {
        if (changementPrenoms) {
            return "3. Ordonner la modification de la mention du sexe dans mes actes de l’état civil et le changement de mes prénoms de " + prenomsEtatCivilEffectifs() + " en " + prenomsDemandesEffectifs() + " ;";
        }
        return "3. Ordonner la modification de la mention du sexe dans mes actes de l’état civil ;";
    }

    public String signature() {
        return identiteEntete();
    }

    public String referenceJugementEtTribunal() {
        String tribunal = "Tribunal judiciaire compétent";
        if (!villeTribunal.isBlank()) {
            String texte = villeTribunal.trim();
            tribunal = texte.toLowerCase(Locale.ROOT).startsWith("tribunal") ? texte : "Tribunal judiciaire de " + texte;
        }
        String numero = numeroJugement.isBlank() ? "" : " n° " + numeroJugement;
        return "du " + tribunal + numero;
    }

    public String mentionPrenomsDansMotivation() {
        if (!changementPrenoms) {
            return "";
        }
        return " Ma demande inclut également la mise à jour de mes prénoms de " + prenomsEtatCivilEffectifs() + " en " + prenomsDemandesEffectifs() + ".";
    }

    public String nomMajuscules() {
        return nom.toUpperCase(Locale.ROOT);
    }

    private String complementModificationEtatCivil() {
        if (changementPrenoms) {
            return "de la mention du sexe et des prénoms";
        }
        return "de la mention du sexe";
    }

    private String prenomsEtatCivilEffectifs() {
        if (!prenomsEtatCivil.isBlank()) {
            return prenomsEtatCivil;
        }
        return prenom;
    }

    private String prenomsDemandesEffectifs() {
        if (!prenomsDemandes.isBlank()) {
            return prenomsDemandes;
        }
        return prenom;
    }
}
