package com.rdr.cecdoc.model;

import com.rdr.cecdoc.util.NormalisationTexte;
import com.rdr.cecdoc.util.ParseursDate;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;

public class DonneesDossier {
    private final boolean changementPrenoms;
    private final String prenomsEtatCivil;
    private final String prenomsUsage;
    private final String nomFamille;
    private final String dateNaissance;
    private final String lieuNaissance;
    private final String sexeEtatCivil;
    private final String adresse;
    private final String tribunal;
    private final String recit;
    private final String villeActuelle;
    private final List<String> piecesJustificatives;
    private final List<PieceJustificative> piecesJustificativesDetaillees;
    private final String nationalite;
    private final String profession;
    private final String situationMatrimoniale;
    private final String situationEnfants;
    private final boolean pacsContracte;
    private final boolean pronomNeutre;

    public DonneesDossier(boolean changementPrenoms, String prenomsEtatCivil, String prenomsUsage, String nomFamille, String dateNaissance, String lieuNaissance, String sexeEtatCivil, String adresse, String tribunal, String recit, String villeActuelle, List<String> piecesJustificatives, String nationalite, String profession, String situationMatrimoniale, String situationEnfants, boolean pacsContracte, boolean pronomNeutre) {
        this(changementPrenoms, prenomsEtatCivil, prenomsUsage, nomFamille, dateNaissance, lieuNaissance, sexeEtatCivil, adresse, tribunal, recit, villeActuelle, piecesJustificatives, nationalite, profession, situationMatrimoniale, situationEnfants, pacsContracte, pronomNeutre, List.of());
    }

    public DonneesDossier(boolean changementPrenoms, String prenomsEtatCivil, String prenomsUsage, String nomFamille, String dateNaissance, String lieuNaissance, String sexeEtatCivil, String adresse, String tribunal, String recit, String villeActuelle, List<String> piecesJustificatives, String nationalite, String profession, String situationMatrimoniale, String situationEnfants, boolean pacsContracte, boolean pronomNeutre, List<PieceJustificative> piecesJustificativesDetaillees) {
        this.changementPrenoms = changementPrenoms;
        this.prenomsEtatCivil = normalizeName(prenomsEtatCivil);
        this.prenomsUsage = normalizeName(prenomsUsage);
        this.nomFamille = normalizeName(nomFamille);
        this.dateNaissance = normalize(dateNaissance);
        this.lieuNaissance = normalizeProperNoun(lieuNaissance);
        this.sexeEtatCivil = normalize(sexeEtatCivil);
        this.adresse = normalize(adresse);
        this.tribunal = normalize(tribunal);
        this.recit = normalize(recit);
        this.villeActuelle = normalizeProperNoun(villeActuelle);

        List<String> titres = normaliserTitres(piecesJustificatives);
        List<PieceJustificative> details = normaliserDetails(piecesJustificativesDetaillees);
        if (!details.isEmpty()) {
            titres = details.stream().map(PieceJustificative::intitule).toList();
        } else if (!titres.isEmpty()) {
            details = titres.stream().map(titre -> new PieceJustificative(titre, List.of())).toList();
        }
        this.piecesJustificatives = titres;
        this.piecesJustificativesDetaillees = details;

        this.nationalite = normalizeLowerCase(nationalite);
        this.profession = normalize(profession);
        this.situationMatrimoniale = normalizeSentenceCase(situationMatrimoniale);
        this.situationEnfants = normalizeSentenceCase(situationEnfants);
        this.pacsContracte = pacsContracte;
        this.pronomNeutre = pronomNeutre;
    }

    private static List<String> normaliserTitres(List<String> titres) {
        if (titres == null) {
            return List.of();
        }
        return titres.stream().filter(Objects::nonNull).map(String::trim).filter(value -> !value.isEmpty()).toList();
    }

    private static List<PieceJustificative> normaliserDetails(List<PieceJustificative> details) {
        if (details == null || details.isEmpty()) {
            return List.of();
        }
        List<PieceJustificative> normalized = new ArrayList<>(details.size());
        for (PieceJustificative detail : details) {
            if (detail == null || detail.intitule().isBlank()) {
                continue;
            }
            normalized.add(new PieceJustificative(detail.intitule(), detail.piecesJointes()));
        }
        return normalized;
    }

    private static String normalize(String value) {
        return value == null ? "" : value.trim();
    }

    private static String normalizeLowerCase(String value) {
        return normalize(value).toLowerCase(Locale.ROOT);
    }

    private static String normalizeSentenceCase(String value) {
        String cleaned = normalize(value);
        if (cleaned.isEmpty()) return cleaned;
        String lower = cleaned.toLowerCase(Locale.ROOT);

        int firstLetterIdx = -1;
        for (int i = 0; i < lower.length(); i++) {
            if (Character.isLetter(lower.charAt(i))) {
                firstLetterIdx = i;
                break;
            }
        }
        if (firstLetterIdx < 0) return lower;

        char first = Character.toUpperCase(lower.charAt(firstLetterIdx));
        if (firstLetterIdx == 0) {
            return first + lower.substring(1);
        }
        return lower.substring(0, firstLetterIdx) + first + lower.substring(firstLetterIdx + 1);
    }

    private static String normalizeName(String value) {
        String cleaned = normalize(value);
        if (cleaned.isEmpty()) return cleaned;
        if (estTexteMajuscule(cleaned)) {
            return cleaned.toUpperCase(Locale.ROOT);
        }

        StringBuilder out = new StringBuilder(cleaned.length());
        boolean upperNext = true;
        for (int i = 0; i < cleaned.length(); i++) {
            char ch = cleaned.charAt(i);
            if (Character.isLetter(ch)) {
                out.append(upperNext ? Character.toUpperCase(ch) : Character.toLowerCase(ch));
                upperNext = false;
            } else {
                out.append(ch);
                upperNext = ch == ' ' || ch == '-' || ch == '\'' || ch == '’';
            }
        }
        return out.toString();
    }

    private static String normalizeProperNoun(String value) {
        return NormalisationTexte.normaliserNomPropre(value);
    }

    private static boolean estTexteMajuscule(String texte) {
        boolean contientLettre = false;
        for (int i = 0; i < texte.length(); i++) {
            char caractere = texte.charAt(i);
            if (!Character.isLetter(caractere)) {
                continue;
            }
            contientLettre = true;
            if (Character.isLowerCase(caractere)) {
                return false;
            }
        }
        return contientLettre;
    }

    public boolean changementPrenoms() {
        return changementPrenoms;
    }

    public String prenomsEtatCivil() {
        return prenomsEtatCivil;
    }

    public String prenomsUsage() {
        return prenomsUsage;
    }

    public String nomFamilleMajuscules() {
        return nomFamille.toUpperCase(Locale.ROOT);
    }

    public String dateNaissance() {
        return dateNaissance;
    }

    public String lieuNaissance() {
        return lieuNaissance;
    }

    public String sexeEtatCivil() {
        return sexeEtatCivil;
    }

    public String adresse() {
        return adresse;
    }

    public String tribunal() {
        return tribunal;
    }

    public String recit() {
        return recit;
    }

    public String villeActuelle() {
        return villeActuelle;
    }

    public List<String> piecesJustificatives() {
        return Collections.unmodifiableList(piecesJustificatives);
    }

    public List<PieceJustificative> piecesJustificativesDetaillees() {
        return Collections.unmodifiableList(piecesJustificativesDetaillees);
    }

    public String nationalite() {
        return nationalite;
    }

    public String nationaliteDebutPhrase() {
        if (nationalite.isEmpty()) return nationalite;
        if (nationalite.length() == 1) return nationalite.toUpperCase(Locale.ROOT);
        return Character.toUpperCase(nationalite.charAt(0)) + nationalite.substring(1);
    }

    public String professionExercee() {
        return profession;
    }

    public String situationMatrimoniale() {
        return situationMatrimoniale;
    }

    public String situationEnfants() {
        return situationEnfants;
    }

    public boolean pacsContracte() {
        return pacsContracte;
    }

    public boolean pronomNeutre() {
        return pronomNeutre;
    }

    public String sexeDemande() {
        if (pronomNeutre) return "Non-binaire";
        String s = sexeEtatCivil.toLowerCase(Locale.ROOT);
        if (s.startsWith("m")) return "Féminin";
        if (s.startsWith("f")) return "Masculin";
        return "";
    }

    public String sexeDemandeEtatCivil() {
        String s = sexeEtatCivil.toLowerCase(Locale.ROOT);
        if (s.startsWith("m")) return "Féminin";
        if (s.startsWith("f")) return "Masculin";
        return "";
    }

    public String age() {
        try {
            LocalDate dob = ParseursDate.parserDateSaisie(dateNaissance);
            LocalDate today = LocalDate.now();
            if (dob.isAfter(today)) return "";
            return Integer.toString(Period.between(dob, today).getYears());
        } catch (RuntimeException ex) {
            return "";
        }
    }

    public boolean sexeDemandeFeminin() {
        String t = sexeDemande();
        return t.toLowerCase(Locale.ROOT).startsWith("f");
    }

    public String civiliteDemande() {
        if (pronomNeutre) return "Mx";
        return sexeDemandeFeminin() ? "Madame" : "Monsieur";
    }

    public String civiliteEtatCivil() {
        String s = sexeEtatCivil.trim().toLowerCase(Locale.ROOT);
        return s.startsWith("f") ? "Madame" : "Monsieur";
    }

    public String nomRequerant() {
        if (pronomNeutre) return "la personne requérante";
        return sexeDemandeFeminin() ? "la requérante" : "le requérant";
    }

    public String nomRequerantMajuscule() {
        if (pronomNeutre) return "La personne requérante";
        return sexeDemandeFeminin() ? "La requérante" : "Le requérant";
    }

    public String deNomRequerant() {
        if (pronomNeutre) return "de la personne requérante";
        return sexeDemandeFeminin() ? "de la requérante" : "du requérant";
    }

    public String nomDeclarant() {
        if (pronomNeutre) return "la personne déclarante";
        return sexeDemandeFeminin() ? "la déclarante" : "le déclarant";
    }

    public String adjectifAge() {
        if (pronomNeutre) return "âgé·e";
        return sexeDemandeFeminin() ? "âgée" : "âgé";
    }

    public String adjectifNe() {
        if (pronomNeutre) return "né·e";
        return sexeDemandeFeminin() ? "née" : "né";
    }

    public String adjectifInscrit() {
        if (pronomNeutre) return "inscrit·e";
        return sexeDemandeFeminin() ? "inscrite" : "inscrit";
    }

    public String adjectifSousSigne() {
        if (pronomNeutre) return "soussigné·e";
        return sexeDemandeFeminin() ? "soussignée" : "soussigné";
    }

    public String nomGenreDemande() {
        if (pronomNeutre) return "personne non-binaire";
        return sexeDemandeFeminin() ? "femme" : "homme";
    }

    public String adjectifGenreDemande() {
        if (pronomNeutre) return "non-binaire";
        return sexeDemandeFeminin() ? "féminin" : "masculin";
    }

    public String adjectifGenreEtatCivil() {
        if (pronomNeutre) return "actuel";
        String s = sexeEtatCivil.trim().toLowerCase(Locale.ROOT);
        return s.startsWith("f") ? "féminin" : "masculin";
    }

    public String pronomQuIel() {
        if (pronomNeutre) return "qu'iel";
        return sexeDemandeFeminin() ? "qu’elle" : "qu’il";
    }

    public String pronomIel() {
        if (pronomNeutre) return "iel";
        return sexeDemandeFeminin() ? "elle" : "il";
    }

    public String adjectifConnu() {
        if (pronomNeutre) return "connu·e";
        return sexeDemandeFeminin() ? "connue" : "connu";
    }
}
