package com.rdr.cecdoc.model;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

public record InstantaneLettreMiseAJourActesLies(String prenom, String nom, String adressePostale,
                                                 String telephonePortable, String courriel, String typeDestinataire,
                                                 String villeAutoriteDestinataire, String adresseDestinataire,
                                                 String villeRedaction, String dateRedaction, String genreAccords,
                                                 String dateNaissance, String lieuNaissance, String autoriteDecision,
                                                 String dateDecision, String dateDecisionDefinitive,
                                                 boolean changementPrenoms, boolean changementSexe,
                                                 boolean acteNaissanceRequerant, String communeNaissanceRequerant,
                                                 String anneeNaissanceRequerant, boolean concernePartenaire,
                                                 String lienPartenaire, String genrePartenaire, boolean acteMariage,
                                                 String communeMariage, String dateMariage,
                                                 boolean acteNaissancePartenaire, String prenomPartenaire,
                                                 String nomPartenaire, String communeNaissancePartenaire,
                                                 String anneeNaissancePartenaire, boolean mentionPacs,
                                                 String autoritePacs, String optionLivret,
                                                 List<InstantaneEnfantActe> enfants) {

    public InstantaneLettreMiseAJourActesLies {
        prenom = nettoyer(prenom);
        nom = nettoyer(nom);
        adressePostale = nettoyer(adressePostale);
        telephonePortable = nettoyer(telephonePortable);
        courriel = nettoyer(courriel);
        typeDestinataire = nettoyer(typeDestinataire);
        villeAutoriteDestinataire = nettoyer(villeAutoriteDestinataire);
        adresseDestinataire = nettoyer(adresseDestinataire);
        villeRedaction = nettoyer(villeRedaction);
        dateRedaction = nettoyer(dateRedaction);
        genreAccords = nettoyer(genreAccords);
        dateNaissance = nettoyer(dateNaissance);
        lieuNaissance = nettoyer(lieuNaissance);
        autoriteDecision = nettoyer(autoriteDecision);
        if (estAutoriteOfficierEtatCivil(autoriteDecision)) {
            changementSexe = false;
        }
        dateDecision = nettoyer(dateDecision);
        dateDecisionDefinitive = nettoyer(dateDecisionDefinitive);
        communeNaissanceRequerant = nettoyer(communeNaissanceRequerant);
        anneeNaissanceRequerant = nettoyer(anneeNaissanceRequerant);
        lienPartenaire = nettoyer(lienPartenaire);
        genrePartenaire = nettoyer(genrePartenaire);
        communeMariage = nettoyer(communeMariage);
        dateMariage = nettoyer(dateMariage);
        prenomPartenaire = nettoyer(prenomPartenaire);
        nomPartenaire = nettoyer(nomPartenaire);
        communeNaissancePartenaire = nettoyer(communeNaissancePartenaire);
        anneeNaissancePartenaire = nettoyer(anneeNaissancePartenaire);
        autoritePacs = nettoyer(autoritePacs);
        optionLivret = nettoyer(optionLivret);
        enfants = enfants == null ? List.of() : enfants.stream().filter(Objects::nonNull).toList();
    }

    public static InstantaneLettreMiseAJourActesLies vide() {
        return new InstantaneLettreMiseAJourActesLies("", "", "", "", "", "Officier·e de l’état civil", "", "", "", "", "Masculin", "", "", "Officier·e de l’état civil", "", "", true, false, true, "", "", false, "Époux·se", "Non-binaire", false, "", "", false, "", "", "", "", false, "", "Aucune demande", List.of());
    }

    private static boolean estAutoriteOfficierEtatCivil(String valeur) {
        return nettoyer(valeur).toLowerCase(Locale.ROOT).contains("officier");
    }

    private static String nettoyer(String valeur) {
        return valeur == null ? "" : valeur.trim();
    }
}
