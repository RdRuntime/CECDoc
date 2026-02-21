package com.rdr.cecdoc.model;

public record InstantaneLettreRelanceMairiePrenom(String prenomsEtatCivil, String prenomsDemandes, String nom,
                                                  String adressePostale, String telephonePortable, String courriel,
                                                  String adresseMairie, String villeRedaction, String dateRedaction,
                                                  String dateDemande, String genreActuel, String dateNaissance,
                                                  String lieuNaissance, String referenceDossier) {

    public InstantaneLettreRelanceMairiePrenom {
        prenomsEtatCivil = nettoyer(prenomsEtatCivil);
        prenomsDemandes = nettoyer(prenomsDemandes);
        nom = nettoyer(nom);
        adressePostale = nettoyer(adressePostale);
        telephonePortable = nettoyer(telephonePortable);
        courriel = nettoyer(courriel);
        adresseMairie = nettoyer(adresseMairie);
        villeRedaction = nettoyer(villeRedaction);
        dateRedaction = nettoyer(dateRedaction);
        dateDemande = nettoyer(dateDemande);
        genreActuel = nettoyer(genreActuel);
        dateNaissance = nettoyer(dateNaissance);
        lieuNaissance = nettoyer(lieuNaissance);
        referenceDossier = nettoyer(referenceDossier);
    }

    public static InstantaneLettreRelanceMairiePrenom vide() {
        return new InstantaneLettreRelanceMairiePrenom("", "", "", "", "", "", "", "", "", "", "", "", "", "");
    }

    private static String nettoyer(String valeur) {
        return valeur == null ? "" : valeur.trim();
    }
}
