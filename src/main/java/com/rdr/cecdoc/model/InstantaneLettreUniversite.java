package com.rdr.cecdoc.model;

public record InstantaneLettreUniversite(String genreActuel, String civiliteSouhaitee, String prenomUsage,
                                         String prenomEtatCivil, String nom, String adressePostale,
                                         String telephonePortable, String courriel, String ine, String nomUniversite,
                                         String explicationParcours, String villeActuelle) {

    public InstantaneLettreUniversite {
        genreActuel = nettoyer(genreActuel);
        civiliteSouhaitee = nettoyer(civiliteSouhaitee);
        prenomUsage = nettoyer(prenomUsage);
        prenomEtatCivil = nettoyer(prenomEtatCivil);
        nom = nettoyer(nom);
        adressePostale = nettoyer(adressePostale);
        telephonePortable = nettoyer(telephonePortable);
        courriel = nettoyer(courriel);
        ine = nettoyer(ine);
        nomUniversite = nettoyer(nomUniversite);
        explicationParcours = nettoyer(explicationParcours);
        villeActuelle = nettoyer(villeActuelle);
    }

    public static InstantaneLettreUniversite vide() {
        return new InstantaneLettreUniversite("", "", "", "", "", "", "", "", "", "", "", "");
    }

    private static String nettoyer(String valeur) {
        return valeur == null ? "" : valeur.trim();
    }
}
