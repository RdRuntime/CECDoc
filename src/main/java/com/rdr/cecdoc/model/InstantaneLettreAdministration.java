package com.rdr.cecdoc.model;

public record InstantaneLettreAdministration(String prenomUsage, String prenomsEtatCivil, String nom,
                                             String adressePostale, String telephonePortable, String courriel,
                                             String adresseDestinataire, boolean changementPrenom,
                                             String prenomNaissance, boolean changementSexe, String sexeAvant,
                                             String sexeApres, boolean changementPrenomFaitEnMairie,
                                             String numeroDecisionMairie, String dateDecisionMairie,
                                             String tribunalCompetent, String numeroJugement, String villeActuelle) {

    public InstantaneLettreAdministration {
        prenomUsage = nettoyer(prenomUsage);
        prenomsEtatCivil = nettoyer(prenomsEtatCivil);
        nom = nettoyer(nom);
        adressePostale = nettoyer(adressePostale);
        telephonePortable = nettoyer(telephonePortable);
        courriel = nettoyer(courriel);
        adresseDestinataire = nettoyer(adresseDestinataire);
        prenomNaissance = nettoyer(prenomNaissance);
        sexeAvant = nettoyer(sexeAvant);
        sexeApres = nettoyer(sexeApres);
        numeroDecisionMairie = nettoyer(numeroDecisionMairie);
        dateDecisionMairie = nettoyer(dateDecisionMairie);
        tribunalCompetent = nettoyer(tribunalCompetent);
        numeroJugement = nettoyer(numeroJugement);
        villeActuelle = nettoyer(villeActuelle);
    }

    public static InstantaneLettreAdministration vide() {
        return new InstantaneLettreAdministration("", "", "", "", "", "", "", false, "", false, "", "", false, "", "", "", "", "");
    }

    private static String nettoyer(String valeur) {
        return valeur == null ? "" : valeur.trim();
    }
}
