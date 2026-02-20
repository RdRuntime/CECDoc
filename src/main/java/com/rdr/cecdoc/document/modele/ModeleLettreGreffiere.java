package com.rdr.cecdoc.document.modele;

import com.rdr.cecdoc.document.AlignementTexte;
import com.rdr.cecdoc.document.ModeleDocumentAbstrait;
import com.rdr.cecdoc.document.RedactriceDocument;
import com.rdr.cecdoc.model.DonneesDossier;
import com.rdr.cecdoc.util.NormalisationTexte;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

public final class ModeleLettreGreffiere extends ModeleDocumentAbstrait<DonneesDossier> {
    private static final DateTimeFormatter FORMAT_DATE_LONGUE = DateTimeFormatter.ofPattern("d MMMM uuuu", Locale.FRENCH);

    @Override
    public void rediger(DonneesDossier donneesDossier, RedactriceDocument redactrice) {
        Objects.requireNonNull(donneesDossier, "donneesDossier");
        Objects.requireNonNull(redactrice, "redactrice");
        ajouterBlocExpediteur(redactrice, donneesDossier);
        ajouterBlocDestinataire(redactrice, donneesDossier);
        ajouterObjet(redactrice, donneesDossier);
        ajouterCorps(redactrice, donneesDossier);
        ajouterBlocSignature(redactrice);
    }

    private void ajouterBlocExpediteur(RedactriceDocument redactrice, DonneesDossier donneesDossier) {
        String ligneNom = identiteAffichee(donneesDossier);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, ligneNom, false);
        ajouterBlocMultiligne(redactrice, AlignementTexte.GAUCHE, donneesDossier.adresse(), false);
        ajouterParagrapheVide(redactrice, AlignementTexte.GAUCHE);
        ajouterParagrapheVide(redactrice, AlignementTexte.GAUCHE);
    }

    private void ajouterBlocDestinataire(RedactriceDocument redactrice, DonneesDossier donneesDossier) {
        ajouterParagraphe(redactrice, AlignementTexte.DROITE, "Monsieur/Madame le/la Greffier·e en Chef,", false);
        ajouterLignesTribunalEntete(redactrice, donneesDossier.tribunal());
        ajouterParagrapheVide(redactrice, AlignementTexte.DROITE);

        String dateEtVille = nettoyer(donneesDossier.villeActuelle()) + ", le " + LocalDate.now().format(FORMAT_DATE_LONGUE);
        ajouterParagraphe(redactrice, AlignementTexte.DROITE, dateEtVille, false);
        ajouterParagrapheVide(redactrice, AlignementTexte.GAUCHE);
    }

    private void ajouterObjet(RedactriceDocument redactrice, DonneesDossier donneesDossier) {
        if (donneesDossier.changementPrenoms()) {
            ajouterParagraphe(redactrice, AlignementTexte.CENTRE, "Objet : Requête aux fins de modification des mentions du sexe et des prénoms à l'état civil", true);
            return;
        }
        ajouterParagraphe(redactrice, AlignementTexte.CENTRE, "Objet : Requête aux fins de modification des mentions du sexe à l'état civil", true);
    }

    private void ajouterCorps(RedactriceDocument redactrice, DonneesDossier donneesDossier) {
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "Monsieur ou Madame le/la Greffier·e en Chef,", false);

        String blocIdentite = identiteAffichee(donneesDossier);
        String ligneProfession = NormalisationTexte.minusculerPourMilieuPhrase(donneesDossier.professionExercee());
        String ligneAdresse = NormalisationTexte.aplatirLignes(donneesDossier.adresse());
        String tribunalPremiereLigne = tribunalPremiereLigne(donneesDossier.tribunal());

        StringBuilder premierParagraphe = new StringBuilder("Je ").append(donneesDossier.adjectifSousSigne()).append(", ").append(blocIdentite);
        if (!ligneProfession.isBlank()) {
            premierParagraphe.append(", ").append(ligneProfession);
        }

        if (donneesDossier.changementPrenoms()) {
            premierParagraphe.append(", demeurant au ").append(ligneAdresse).append(", souhaite par la présente saisir le ").append(tribunalPremiereLigne).append(" afin qu'il statue sur ma requête aux fins de modification des mentions du sexe et des prénoms à l'état civil.");
        } else {
            premierParagraphe.append(", demeurant au ").append(ligneAdresse).append(", souhaite par la présente saisir le ").append(tribunalPremiereLigne).append(" afin qu'il statue sur ma requête aux fins de modification des mentions du sexe à l'état civil.");
        }

        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, premierParagraphe.toString(), false);

        String sexeNaissance = normaliserSexe(donneesDossier.sexeEtatCivil());
        String sexeDemande = normaliserSexe(donneesDossier.sexeDemandeEtatCivil());
        String secondParagraphe = "En effet, je suis " + identiteGenreTrans(donneesDossier) + ", " + donneesDossier.adjectifNe() + " de sexe " + sexeNaissance + " et " + donneesDossier.adjectifInscrit() + " sur les registres de l'état civil comme " + formeTel(donneesDossier) + ". Cependant, je suis " + donneesDossier.adjectifConnu() + " comme " + identiteSociale(donneesDossier) + " par tous mes proches et je me présente publiquement comme étant de ce sexe.";

        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, secondParagraphe, false);
        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "Au titre de l'article 61-5 du Code Civil, je vous prie donc de bien vouloir statuer sur les éléments suivants :", false);

        if (donneesDossier.changementPrenoms()) {
            ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "• Dire et juger recevable et fondée ma demande de changement de la mention de sexe et de prénoms ;", false);
            ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "• Ordonner que mon acte de naissance soit rectifié en ce sens que la mention « " + sexeNaissance + " » soit remplacée par la mention « " + sexeDemande + " » et que mes prénoms « " + nettoyer(donneesDossier.prenomsEtatCivil()) + " » soient remplacés par les prénoms « " + prenomsDemandes(donneesDossier) + " » ;", false);
        } else {
            ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "• Dire et juger recevable et fondée ma demande de changement de la mention de sexe ;", false);
            ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "• Ordonner que mon acte de naissance soit rectifié en ce sens que la mention « " + sexeNaissance + " » soit remplacée par la mention « " + sexeDemande + " » ;", false);
        }

        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "• Ordonner la retranscription du dispositif du jugement à intervenir en marge des registres de l'État Civil, en marge de mon acte de naissance et de tout autre document officiel me concernant ;", false);
        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "• Ordonner qu'aucune expédition des actes d'État Civil sans la mention desdites rectifications ne soit délivrée.", false);
        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "Je vous remercie de bien vouloir me communiquer dès que possible la date de l'audience.", false);
        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "En vous remerciant de l'intérêt que vous porterez à ma requête, Monsieur/Madame le/la Greffier·e en Chef, veuillez recevoir mes salutations les plus respectueuses.", false);
    }

    private void ajouterBlocSignature(RedactriceDocument redactrice) {
        ajouterParagrapheVide(redactrice, AlignementTexte.DROITE);
        ajouterParagrapheVide(redactrice, AlignementTexte.DROITE);
        ajouterParagraphe(redactrice, AlignementTexte.DROITE, "Signature", false);
        ajouterParagrapheVide(redactrice, AlignementTexte.JUSTIFIE);
    }

    private void ajouterLignesTribunalEntete(RedactriceDocument redactrice, String tribunal) {
        String[] lignes = nettoyer(tribunal).split("\\R");
        boolean auMoinsUneLigne = false;
        for (String ligne : lignes) {
            String candidate = ligne.trim();
            if (candidate.isEmpty()) {
                continue;
            }
            ajouterParagraphe(redactrice, AlignementTexte.DROITE, candidate, true);
            auMoinsUneLigne = true;
        }
        if (!auMoinsUneLigne) {
            ajouterParagraphe(redactrice, AlignementTexte.DROITE, "", true);
        }
    }

    private String identiteGenreTrans(DonneesDossier donneesDossier) {
        if (donneesDossier.pronomNeutre()) {
            return "une personne non-binaire";
        }
        return donneesDossier.sexeDemandeFeminin() ? "une femme transgenre" : "un homme transgenre";
    }

    private String identiteSociale(DonneesDossier donneesDossier) {
        if (!donneesDossier.pronomNeutre()) {
            return donneesDossier.sexeDemandeFeminin() ? "une femme" : "un homme";
        }
        String sexeEtatCivil = normaliserSexe(donneesDossier.sexeEtatCivil());
        if (sexeEtatCivil.startsWith("masc")) {
            return "une personne non-binaire socialement femme (à l'opposé du sexe de naissance)";
        }
        return "une personne non-binaire socialement homme (à l'opposé du sexe de naissance)";
    }

    private String formeTel(DonneesDossier donneesDossier) {
        if (donneesDossier.pronomNeutre()) {
            return "tel·le";
        }
        String sexeEtatCivil = normaliserSexe(donneesDossier.sexeEtatCivil());
        return sexeEtatCivil.startsWith("f") ? "telle" : "tel";
    }

    private String normaliserSexe(String valeur) {
        String nettoye = nettoyer(valeur).toLowerCase(Locale.ROOT);
        if (nettoye.startsWith("f")) {
            return "féminin";
        }
        if (nettoye.startsWith("m")) {
            return "masculin";
        }
        return nettoye;
    }

    private String identiteAffichee(DonneesDossier donneesDossier) {
        String nom = nettoyer(donneesDossier.nomFamilleMajuscules());
        String prenomsEtatCivil = nettoyer(donneesDossier.prenomsEtatCivil());
        String identiteEtatCivil = assemblerNomComplet(nom, prenomsEtatCivil);
        if (!donneesDossier.changementPrenoms()) {
            return identiteEtatCivil;
        }
        String prenomsChoisis = nettoyer(donneesDossier.prenomsUsage());
        if (prenomsChoisis.isBlank()) {
            prenomsChoisis = prenomsEtatCivil;
        }
        String identiteChoisie = assemblerNomComplet(nom, prenomsChoisis);
        return identiteChoisie + " (" + identiteEtatCivil + " pour l'état civil)";
    }

    private String assemblerNomComplet(String nom, String prenoms) {
        String partieNom = nettoyer(nom);
        String partiePrenoms = nettoyer(prenoms);
        if (partieNom.isBlank()) {
            return partiePrenoms;
        }
        if (partiePrenoms.isBlank()) {
            return partieNom;
        }
        return partieNom + " " + partiePrenoms;
    }

    private String tribunalPremiereLigne(String valeur) {
        String[] lignes = nettoyer(valeur).split("\\R");
        for (String ligne : lignes) {
            String candidate = ligne.trim();
            if (!candidate.isEmpty()) {
                return candidate;
            }
        }
        return "";
    }

    private String prenomsDemandes(DonneesDossier donneesDossier) {
        String prenomsUsage = nettoyer(donneesDossier.prenomsUsage());
        if (!prenomsUsage.isBlank()) {
            return prenomsUsage;
        }
        return nettoyer(donneesDossier.prenomsEtatCivil());
    }
}
