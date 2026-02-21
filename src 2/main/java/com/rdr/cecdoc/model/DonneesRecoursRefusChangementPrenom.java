package com.rdr.cecdoc.model;

import com.rdr.cecdoc.util.NormalisationTexte;
import com.rdr.cecdoc.util.ParseursDate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class DonneesRecoursRefusChangementPrenom {
    private static final DateTimeFormatter FORMAT_DATE_LONGUE = DateTimeFormatter.ofPattern("d MMMM uuuu", Locale.FRENCH);

    private final String prenom;
    private final String nom;
    private final String adressePostale;
    private final String telephonePortable;
    private final String courriel;
    private final String villeMairie;
    private final String adresseMairie;
    private final String villeRedaction;
    private final String dateRedaction;
    private final String genreDemande;
    private final String dateNaissance;
    private final String lieuNaissance;
    private final String prenomsInscrits;
    private final String prenomsDemandes;
    private final boolean plusieursPrenomsInscrits;
    private final String qualiteAvocat;
    private final String nomAvocat;
    private final String barreauAvocat;
    private final String adresseAvocat;
    private final String telephoneAvocat;
    private final String courrielAvocat;
    private final String dateNotificationRefus;
    private final String villeTribunalJudiciaire;
    private final String motifRefusNotifie;
    private final boolean usageFamilial;
    private final boolean usageAmical;
    private final boolean usageProfessionnel;
    private final boolean usageScolaire;
    private final boolean usageAssociatif;
    private final String anecdotesDifficultes;
    private final String raisonsContestation;
    private final String dateRecepisseDepot;
    private final String dateDecisionRefus;

    public DonneesRecoursRefusChangementPrenom(String prenom, String nom, String adressePostale, String telephonePortable, String courriel, String villeMairie, String adresseMairie, String villeRedaction, String dateRedaction, String genreDemande, String dateNaissance, String lieuNaissance, String prenomsInscrits, String prenomsDemandes, boolean plusieursPrenomsInscrits, boolean plusieursPrenomsDemandes, String qualiteAvocat, String nomAvocat, String barreauAvocat, String adresseAvocat, String telephoneAvocat, String courrielAvocat, String dateNotificationRefus, String villeTribunalJudiciaire, String motifRefusNotifie, boolean usageFamilial, boolean usageAmical, boolean usageProfessionnel, boolean usageScolaire, boolean usageAssociatif, String anecdotesDifficultes, String raisonsContestation, String dateRecepisseDepot, String dateDecisionRefus) {
        this.prenom = NormalisationTexte.normaliserNomPropre(prenom);
        this.nom = NormalisationTexte.normaliserNomPropre(nom);
        this.adressePostale = NormalisationTexte.normaliserTexte(adressePostale);
        this.telephonePortable = NormalisationTexte.normaliserTexte(telephonePortable);
        this.courriel = NormalisationTexte.normaliserTexte(courriel).toLowerCase(Locale.ROOT);
        this.villeMairie = NormalisationTexte.normaliserNomPropre(villeMairie);
        this.adresseMairie = NormalisationTexte.normaliserTexte(adresseMairie);
        this.villeRedaction = NormalisationTexte.normaliserNomPropre(villeRedaction);
        this.dateRedaction = NormalisationTexte.normaliserTexte(dateRedaction);
        this.genreDemande = normaliserGenre(genreDemande);
        this.dateNaissance = NormalisationTexte.normaliserTexte(dateNaissance);
        this.lieuNaissance = NormalisationTexte.normaliserNomPropre(lieuNaissance);
        this.prenomsInscrits = NormalisationTexte.normaliserTexte(prenomsInscrits);
        this.prenomsDemandes = NormalisationTexte.normaliserTexte(prenomsDemandes);
        this.plusieursPrenomsInscrits = plusieursPrenomsInscrits;
        this.qualiteAvocat = normaliserQualiteAvocat(qualiteAvocat);
        this.nomAvocat = NormalisationTexte.normaliserNomPropre(nomAvocat);
        this.barreauAvocat = NormalisationTexte.normaliserTexte(barreauAvocat);
        this.adresseAvocat = NormalisationTexte.normaliserTexte(adresseAvocat);
        this.telephoneAvocat = NormalisationTexte.normaliserTexte(telephoneAvocat);
        this.courrielAvocat = NormalisationTexte.normaliserTexte(courrielAvocat).toLowerCase(Locale.ROOT);
        this.dateNotificationRefus = NormalisationTexte.normaliserTexte(dateNotificationRefus);
        this.villeTribunalJudiciaire = NormalisationTexte.normaliserNomPropre(villeTribunalJudiciaire);
        this.motifRefusNotifie = NormalisationTexte.normaliserTexte(motifRefusNotifie);
        this.usageFamilial = usageFamilial;
        this.usageAmical = usageAmical;
        this.usageProfessionnel = usageProfessionnel;
        this.usageScolaire = usageScolaire;
        this.usageAssociatif = usageAssociatif;
        this.anecdotesDifficultes = NormalisationTexte.normaliserTexte(anecdotesDifficultes);
        this.raisonsContestation = NormalisationTexte.normaliserTexte(raisonsContestation);
        this.dateRecepisseDepot = NormalisationTexte.normaliserTexte(dateRecepisseDepot);
        this.dateDecisionRefus = NormalisationTexte.normaliserTexte(dateDecisionRefus);
    }

    public DonneesRecoursRefusChangementPrenom(String prenom, String nom, String adressePostale, String telephonePortable, String courriel, String villeMairie, String adresseMairie, String villeRedaction, String dateRedaction, String genreDemande, String dateNaissance, String lieuNaissance, String prenomsInscrits, String prenomsDemandes, boolean plusieursPrenomsInscrits, boolean plusieursPrenomsDemandes) {
        this(prenom, nom, adressePostale, telephonePortable, courriel, villeMairie, adresseMairie, villeRedaction, dateRedaction, genreDemande, dateNaissance, lieuNaissance, prenomsInscrits, prenomsDemandes, plusieursPrenomsInscrits, plusieursPrenomsDemandes, "avocat", "", "", "", "", "", "", "", "", false, false, false, false, false, "", "", "", "");
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

    private static String formaterDateLongue(String date) {
        if (ParseursDate.dateSaisieValide(date)) {
            return ParseursDate.parserDateSaisie(date).format(FORMAT_DATE_LONGUE);
        }
        return LocalDate.now().format(FORMAT_DATE_LONGUE);
    }

    private static String assemblerIdentite(String prenoms, String nom) {
        String blocPrenoms = NormalisationTexte.normaliserTexte(prenoms);
        String blocNom = NormalisationTexte.normaliserTexte(nom);
        if (blocPrenoms.isBlank()) {
            return blocNom;
        }
        if (blocNom.isBlank()) {
            return blocPrenoms;
        }
        return blocPrenoms + " " + blocNom;
    }

    public String identiteEnteteAvecEtatCivil() {
        return prenomsDemandes() + " (" + prenomsInscrits() + " pour l'état civil) " + nomMajuscules();
    }

    public String identiteIntroductionAvecEtatCivil() {
        return identiteChoisie() + " (" + prenomsInscrits() + " " + nomMajuscules() + " pour l'état civil)";
    }

    public String identiteChoisie() {
        return assemblerIdentite(prenomsDemandes(), nomMajuscules());
    }

    public String identiteSimple() {
        return assemblerIdentite(premierPrenomUsage(), nomMajuscules());
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

    public String adressePostale() {
        return adressePostale;
    }

    public String adresseTribunal() {
        return adresseMairie;
    }

    public String villeRedaction() {
        return villeRedaction;
    }

    public String villeTribunalJudiciaire() {
        return villeTribunalJudiciaire;
    }

    public String villeMairie() {
        return villeMairie;
    }

    public String dateRedactionLongue() {
        return formaterDateLongue(dateRedaction);
    }

    public String dateNaissance() {
        return dateNaissance;
    }

    public String dateNaissanceLongue() {
        return formaterDateLongue(dateNaissance);
    }

    public String dateNotificationRefus() {
        return dateOuAujourdhui(dateNotificationRefus);
    }

    public String dateRecepisseDepot() {
        return dateOuAujourdhui(dateRecepisseDepot);
    }

    public String dateDecisionRefus() {
        return dateOuAujourdhui(dateDecisionRefus);
    }

    public String dateRefusDiscussion() {
        return dateOuAujourdhui(dateNotificationRefus);
    }

    public String lieuNaissance() {
        return lieuNaissance;
    }

    public String motifRefusNotifie() {
        return motifRefusNotifie;
    }

    public String motifRefusMilieuPhrase() {
        if (motifRefusNotifie.isBlank()) {
            return "";
        }
        if (motifRefusNotifie.length() == 1) {
            return motifRefusNotifie.toLowerCase(Locale.ROOT);
        }
        return Character.toLowerCase(motifRefusNotifie.charAt(0)) + motifRefusNotifie.substring(1);
    }

    public String prenomsDemandes() {
        if (!prenomsDemandes.isBlank()) {
            return prenomsDemandes;
        }
        return prenom;
    }

    public String prenomsInscrits() {
        if (!prenomsInscrits.isBlank()) {
            return prenomsInscrits;
        }
        return prenom;
    }

    public String premierPrenomUsage() {
        return NormalisationTexte.extrairePremierPrenom(prenomsDemandes());
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

    public String adjectifAppele() {
        return switch (genreDemande) {
            case "féminin" -> "appelée";
            case "non-binaire" -> "appelé·e";
            default -> "appelé";
        };
    }

    public String adjectifConnu() {
        return switch (genreDemande) {
            case "féminin" -> "connue";
            case "non-binaire" -> "connu·e";
            default -> "connu";
        };
    }

    public String adjectifIdentifie() {
        return switch (genreDemande) {
            case "féminin" -> "identifiée";
            case "non-binaire" -> "identifié·e";
            default -> "identifié";
        };
    }

    public String locutionMonMesPrenomsInscrits() {
        return plusieursPrenomsInscrits ? "mes prénoms inscrits" : "mon prénom inscrit";
    }

    public String nomMajuscules() {
        return nom.toUpperCase(Locale.ROOT);
    }

    public boolean usageFamilial() {
        return usageFamilial;
    }

    public boolean usageAmical() {
        return usageAmical;
    }

    public boolean usageProfessionnel() {
        return usageProfessionnel;
    }

    public boolean usageScolaire() {
        return usageScolaire;
    }

    public boolean usageAssociatif() {
        return usageAssociatif;
    }

    public String anecdotesDifficultes() {
        return anecdotesDifficultes;
    }

    public boolean aAnecdotes() {
        return !anecdotesDifficultes.isBlank();
    }

    public List<String> raisonsContestationListe() {
        if (raisonsContestation.isBlank()) {
            return List.of("L’appréciation du dossier est erronée au regard des pièces déjà produites.");
        }
        List<String> raisons = new ArrayList<>();
        for (String ligne : raisonsContestation.split("\\R")) {
            String raison = ligne == null ? "" : ligne.trim();
            if (raison.isEmpty()) {
                continue;
            }
            while (raison.startsWith("-") || raison.startsWith("•")) {
                raison = raison.substring(1).trim();
            }
            if (!raison.isEmpty()) {
                raisons.add(raison);
            }
        }
        if (raisons.isEmpty()) {
            return List.of("L’appréciation du dossier est erronée au regard des pièces déjà produites.");
        }
        return raisons;
    }

    public String signature() {
        return identiteSimple();
    }

    private String dateOuAujourdhui(String date) {
        if (ParseursDate.dateSaisieValide(date)) {
            return date;
        }
        return LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/uuuu", Locale.ROOT));
    }
}
