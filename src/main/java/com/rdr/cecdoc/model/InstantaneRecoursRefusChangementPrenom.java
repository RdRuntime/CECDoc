package com.rdr.cecdoc.model;

public record InstantaneRecoursRefusChangementPrenom(String prenom, String nom, String adressePostale,
                                                     String telephonePortable, String courriel, String villeMairie,
                                                     String adresseMairie, String villeRedaction, String dateRedaction,
                                                     String genreDemande, String dateNaissance, String lieuNaissance,
                                                     String prenomsInscrits, String prenomsDemandes,
                                                     boolean plusieursPrenomsInscrits, boolean plusieursPrenomsDemandes,
                                                     String qualiteAvocat, String nomAvocat, String barreauAvocat,
                                                     String adresseAvocat, String telephoneAvocat,
                                                     String courrielAvocat, String dateNotificationRefus,
                                                     String villeTribunalJudiciaire, String motifRefusNotifie,
                                                     boolean usageFamilial, boolean usageAmical,
                                                     boolean usageProfessionnel, boolean usageScolaire,
                                                     boolean usageAssociatif, String anecdotesDifficultes,
                                                     String raisonsContestation, String dateRecepisseDepot,
                                                     String dateDecisionRefus) {

    public InstantaneRecoursRefusChangementPrenom {
        prenom = nettoyer(prenom);
        nom = nettoyer(nom);
        adressePostale = nettoyer(adressePostale);
        telephonePortable = nettoyer(telephonePortable);
        courriel = nettoyer(courriel);
        villeMairie = nettoyer(villeMairie);
        adresseMairie = nettoyer(adresseMairie);
        villeRedaction = nettoyer(villeRedaction);
        dateRedaction = nettoyer(dateRedaction);
        genreDemande = nettoyer(genreDemande);
        dateNaissance = nettoyer(dateNaissance);
        lieuNaissance = nettoyer(lieuNaissance);
        prenomsInscrits = nettoyer(prenomsInscrits);
        prenomsDemandes = nettoyer(prenomsDemandes);
        qualiteAvocat = nettoyer(qualiteAvocat);
        nomAvocat = nettoyer(nomAvocat);
        barreauAvocat = nettoyer(barreauAvocat);
        adresseAvocat = nettoyer(adresseAvocat);
        telephoneAvocat = nettoyer(telephoneAvocat);
        courrielAvocat = nettoyer(courrielAvocat);
        dateNotificationRefus = nettoyer(dateNotificationRefus);
        villeTribunalJudiciaire = nettoyer(villeTribunalJudiciaire);
        motifRefusNotifie = nettoyer(motifRefusNotifie);
        anecdotesDifficultes = nettoyer(anecdotesDifficultes);
        raisonsContestation = nettoyer(raisonsContestation);
        dateRecepisseDepot = nettoyer(dateRecepisseDepot);
        dateDecisionRefus = nettoyer(dateDecisionRefus);
    }

    public static InstantaneRecoursRefusChangementPrenom vide() {
        return new InstantaneRecoursRefusChangementPrenom("", "", "", "", "", "", "", "", "", "", "", "", "", "", false, false, "avocat", "", "", "", "", "", "", "", "", false, false, false, false, false, "", "", "", "");
    }

    private static String nettoyer(String valeur) {
        return valeur == null ? "" : valeur.trim();
    }
}
