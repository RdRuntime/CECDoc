package com.rdr.cecdoc.model;

public record InstantaneLettreRgpdMinimisation(String prenomsEtatCivil, String prenomsConnusOrganisme, String nom,
                                               String adressePostale, String telephonePortable, String courriel,
                                               String nomAdresseOrganisme, String villeRedaction, String dateRedaction,
                                               String genreDemande, String dateNaissance, String lieuNaissance,
                                               String sexeEtatCivil, String civiliteAffichage,
                                               boolean champsCiviliteGenrePresents) {

    public InstantaneLettreRgpdMinimisation {
        prenomsEtatCivil = nettoyer(prenomsEtatCivil);
        prenomsConnusOrganisme = nettoyer(prenomsConnusOrganisme);
        nom = nettoyer(nom);
        adressePostale = nettoyer(adressePostale);
        telephonePortable = nettoyer(telephonePortable);
        courriel = nettoyer(courriel);
        nomAdresseOrganisme = nettoyer(nomAdresseOrganisme);
        villeRedaction = nettoyer(villeRedaction);
        dateRedaction = nettoyer(dateRedaction);
        genreDemande = nettoyer(genreDemande);
        dateNaissance = nettoyer(dateNaissance);
        lieuNaissance = nettoyer(lieuNaissance);
        sexeEtatCivil = nettoyer(sexeEtatCivil);
        civiliteAffichage = nettoyer(civiliteAffichage);
    }

    public static InstantaneLettreRgpdMinimisation vide() {
        return new InstantaneLettreRgpdMinimisation("", "", "", "", "", "", "", "", "", "", "", "", "", "", true);
    }

    private static String nettoyer(String valeur) {
        return valeur == null ? "" : valeur.trim();
    }
}
