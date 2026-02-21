package com.rdr.cecdoc.model;

public record InstantaneLettreRelanceTribunal(String prenom, String nom, String adressePostale,
                                              String telephonePortable, String courriel, String adresseTribunal,
                                              String villeRedaction, String dateRedaction, String dateDepotEnvoi,
                                              String informationAttendue, String genreRevendique, String dateNaissance,
                                              String lieuNaissance, String referenceDossier, boolean changementPrenoms,
                                              String prenomsEtatCivil) {

    public InstantaneLettreRelanceTribunal {
        prenom = nettoyer(prenom);
        nom = nettoyer(nom);
        adressePostale = nettoyer(adressePostale);
        telephonePortable = nettoyer(telephonePortable);
        courriel = nettoyer(courriel);
        adresseTribunal = nettoyer(adresseTribunal);
        villeRedaction = nettoyer(villeRedaction);
        dateRedaction = nettoyer(dateRedaction);
        dateDepotEnvoi = nettoyer(dateDepotEnvoi);
        informationAttendue = nettoyer(informationAttendue);
        genreRevendique = nettoyer(genreRevendique);
        dateNaissance = nettoyer(dateNaissance);
        lieuNaissance = nettoyer(lieuNaissance);
        referenceDossier = nettoyer(referenceDossier);
        prenomsEtatCivil = nettoyer(prenomsEtatCivil);
    }

    public static InstantaneLettreRelanceTribunal vide() {
        return new InstantaneLettreRelanceTribunal("", "", "", "", "", "", "", "", "", "", "", "", "", "", false, "");
    }

    private static String nettoyer(String valeur) {
        return valeur == null ? "" : valeur.trim();
    }
}
