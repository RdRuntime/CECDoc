package com.rdr.cecdoc.model;

public record InstantaneRecoursRefusChangementSexe(String prenom, String nom, String adressePostale,
                                                   String telephonePortable, String courriel, String villeCourAppel,
                                                   String adresseCourAppel, String villeRedaction, String dateRedaction,
                                                   String genreRevendique, String dateNaissance, String lieuNaissance,
                                                   String qualiteAvocat, String nomAvocat, String barreauAvocat,
                                                   String adresseAvocat, String telephoneAvocat, String courrielAvocat,
                                                   String villeTribunal, String dateJugement, String motifRefus,
                                                   boolean changementPrenoms, String numeroJugement,
                                                   String prenomsEtatCivil, String prenomsDemandes) {

    public InstantaneRecoursRefusChangementSexe {
        prenom = nettoyer(prenom);
        nom = nettoyer(nom);
        adressePostale = nettoyer(adressePostale);
        telephonePortable = nettoyer(telephonePortable);
        courriel = nettoyer(courriel);
        villeCourAppel = nettoyer(villeCourAppel);
        adresseCourAppel = nettoyer(adresseCourAppel);
        villeRedaction = nettoyer(villeRedaction);
        dateRedaction = nettoyer(dateRedaction);
        genreRevendique = nettoyer(genreRevendique);
        dateNaissance = nettoyer(dateNaissance);
        lieuNaissance = nettoyer(lieuNaissance);
        qualiteAvocat = nettoyer(qualiteAvocat);
        nomAvocat = nettoyer(nomAvocat);
        barreauAvocat = nettoyer(barreauAvocat);
        adresseAvocat = nettoyer(adresseAvocat);
        telephoneAvocat = nettoyer(telephoneAvocat);
        courrielAvocat = nettoyer(courrielAvocat);
        villeTribunal = nettoyer(villeTribunal);
        dateJugement = nettoyer(dateJugement);
        motifRefus = nettoyer(motifRefus);
        numeroJugement = nettoyer(numeroJugement);
        prenomsEtatCivil = nettoyer(prenomsEtatCivil);
        prenomsDemandes = nettoyer(prenomsDemandes);
    }

    public static InstantaneRecoursRefusChangementSexe vide() {
        return new InstantaneRecoursRefusChangementSexe("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", false, "", "", "");
    }

    private static String nettoyer(String valeur) {
        return valeur == null ? "" : valeur.trim();
    }
}
