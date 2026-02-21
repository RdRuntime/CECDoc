package com.rdr.cecdoc.model;

public record InstantaneEnfantActe(String prenom, String nom, String communeNaissance, String dateNaissance,
                                   String genre) {

    public InstantaneEnfantActe {
        prenom = nettoyer(prenom);
        nom = nettoyer(nom);
        communeNaissance = nettoyer(communeNaissance);
        dateNaissance = nettoyer(dateNaissance);
        genre = nettoyer(genre);
    }

    public static InstantaneEnfantActe vide() {
        return new InstantaneEnfantActe("", "", "", "", "Non-binaire");
    }

    private static String nettoyer(String valeur) {
        return valeur == null ? "" : valeur.trim();
    }
}
