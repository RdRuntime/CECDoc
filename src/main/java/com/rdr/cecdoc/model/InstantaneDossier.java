package com.rdr.cecdoc.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record InstantaneDossier(boolean changementPrenoms, boolean pronomNeutre, String prenomsEtatCivil, String prenomsUsage,
                                String nomFamille, String dateNaissance, String lieuNaissance, String sexeEtatCivil, String adresse,
                                String tribunal, String recit, String villeActuelle, List<String> piecesJustificatives,
                                String nationalite, String profession, String situationMatrimoniale, String situationEnfants,
                                boolean pacsContracte, List<PieceJustificative> piecesJustificativesDetaillees) {

    public InstantaneDossier(boolean changementPrenoms, boolean pronomNeutre, String prenomsEtatCivil, String prenomsUsage,
                             String nomFamille, String dateNaissance, String lieuNaissance, String sexeEtatCivil, String adresse,
                             String tribunal, String recit, String villeActuelle, List<String> piecesJustificatives,
                             String nationalite, String profession, String situationMatrimoniale, String situationEnfants,
                             boolean pacsContracte) {
        this(changementPrenoms, pronomNeutre, prenomsEtatCivil, prenomsUsage, nomFamille, dateNaissance, lieuNaissance,
                sexeEtatCivil, adresse, tribunal, recit, villeActuelle, piecesJustificatives, nationalite, profession,
                situationMatrimoniale, situationEnfants, pacsContracte, List.of());
    }

    public InstantaneDossier {
        prenomsEtatCivil = sanitize(prenomsEtatCivil);
        prenomsUsage = sanitize(prenomsUsage);
        nomFamille = sanitize(nomFamille);
        dateNaissance = sanitize(dateNaissance);
        lieuNaissance = sanitize(lieuNaissance);
        sexeEtatCivil = sanitize(sexeEtatCivil);
        adresse = sanitize(adresse);
        tribunal = sanitize(tribunal);
        recit = sanitize(recit);
        villeActuelle = sanitize(villeActuelle);
        nationalite = sanitize(nationalite);
        profession = sanitize(profession);
        situationMatrimoniale = sanitize(situationMatrimoniale);
        situationEnfants = sanitize(situationEnfants);

        List<String> titres = normaliserTitres(piecesJustificatives);
        List<PieceJustificative> details = normaliserDetails(piecesJustificativesDetaillees);
        if (!details.isEmpty()) {
            titres = details.stream().map(PieceJustificative::intitule).toList();
        } else if (!titres.isEmpty()) {
            details = titres.stream().map(title -> new PieceJustificative(title, List.of())).toList();
        }

        piecesJustificatives = List.copyOf(titres);
        piecesJustificativesDetaillees = List.copyOf(details);
    }

    private static List<String> normaliserTitres(List<String> titles) {
        if (titles == null) {
            return List.of();
        }
        return titles.stream().filter(Objects::nonNull).map(String::trim).filter(value -> !value.isEmpty()).toList();
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

    private static String sanitize(String value) {
        return value == null ? "" : value.trim();
    }
}
