package com.rdr.cecdoc.model;

import com.rdr.cecdoc.util.NormalisationTexte;
import com.rdr.cecdoc.util.ParseursDate;

import java.time.LocalDate;
import java.util.Locale;

public final class DonneesEnfantActe {
    private final String prenom;
    private final String nom;
    private final String communeNaissance;
    private final String dateNaissance;
    private final String genre;

    public DonneesEnfantActe(String prenom, String nom, String communeNaissance, String dateNaissance, String genre) {
        this.prenom = NormalisationTexte.normaliserNomPropre(prenom);
        this.nom = NormalisationTexte.normaliserNomPropre(nom);
        this.communeNaissance = NormalisationTexte.normaliserNomPropre(communeNaissance);
        this.dateNaissance = NormalisationTexte.normaliserTexte(dateNaissance);
        this.genre = normaliserGenre(genre);
    }

    public String ligneActeNaissance() {
        return "acte de naissance de l’enfant : " + identite() + " (commune : " + communeNaissance + ", date : " + dateNaissance + ") – " + statutMajoriteAccorde();
    }

    public String identite() {
        if (prenom.isBlank()) {
            return nom.toUpperCase(Locale.ROOT);
        }
        if (nom.isBlank()) {
            return prenom;
        }
        return prenom + " " + nom.toUpperCase(Locale.ROOT);
    }

    public boolean estMajeur() {
        if (!ParseursDate.dateSaisieValide(dateNaissance)) {
            return false;
        }
        LocalDate date = ParseursDate.parserDateSaisie(dateNaissance);
        return !date.plusYears(18).isAfter(LocalDate.now());
    }

    private String statutMajoriteAccorde() {
        if (estMajeur()) {
            return switch (genre) {
                case "féminin" -> "majeure";
                case "non-binaire" -> "majeur·e";
                default -> "majeur";
            };
        }
        return switch (genre) {
            case "féminin" -> "mineure";
            case "non-binaire" -> "mineur·e";
            default -> "mineur";
        };
    }

    private String normaliserGenre(String valeur) {
        String texte = NormalisationTexte.normaliserTexte(valeur).toLowerCase();
        if (texte.startsWith("f")) {
            return "féminin";
        }
        if (texte.startsWith("n")) {
            return "non-binaire";
        }
        return "masculin";
    }
}
