package com.rdr.cecdoc.docx;

import com.rdr.cecdoc.model.DonneesDossier;
import com.rdr.cecdoc.util.NormalisationTexte;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public final class GenerateurLettreGreffiere extends GenerateurLettreAbstrait {
    private static final DateTimeFormatter FORMAT_DATE_LONGUE = DateTimeFormatter.ofPattern("d MMMM uuuu", Locale.FRENCH);

    public void generer(DonneesDossier donneesDossier, File destination) throws IOException {
        Objects.requireNonNull(donneesDossier, "donneesDossier");
        Objects.requireNonNull(destination, "destination");
        ecrireDocument(destination, document -> {
            ajouterBlocExpediteur(document, donneesDossier);
            ajouterBlocDestinataire(document, donneesDossier);
            ajouterObjet(document, donneesDossier);
            ajouterCorps(document, donneesDossier);
            ajouterBlocSignature(document);
        });
    }

    private void ajouterBlocExpediteur(XWPFDocument document, DonneesDossier donneesDossier) {
        String ligneNom = identiteAffichee(donneesDossier);
        ajouterLigne(document, ParagraphAlignment.LEFT, ligneNom, false);
        ajouterBlocMultiligne(document, ParagraphAlignment.LEFT, donneesDossier.adresse(), false);
        ajouterParagrapheVide(document, ParagraphAlignment.LEFT);
        ajouterParagrapheVide(document, ParagraphAlignment.LEFT);
    }

    private void ajouterBlocDestinataire(XWPFDocument document, DonneesDossier donneesDossier) {
        ajouterLigne(document, ParagraphAlignment.RIGHT, "Monsieur/Madame le/la Greffier·e en Chef,", false);
        ajouterLignesTribunalEntete(document, donneesDossier.tribunal());
        ajouterParagrapheVide(document, ParagraphAlignment.RIGHT);

        String dateEtVille = nettoyer(donneesDossier.villeActuelle()) + ", le " + LocalDate.now().format(FORMAT_DATE_LONGUE);
        ajouterLigne(document, ParagraphAlignment.RIGHT, dateEtVille, false);

        ajouterParagrapheVide(document, ParagraphAlignment.LEFT);
    }

    private void ajouterObjet(XWPFDocument document, DonneesDossier donneesDossier) {
        if (donneesDossier.changementPrenoms()) {
            ajouterLigne(document, ParagraphAlignment.CENTER, "Objet : Requête aux fins de modification des mentions du sexe et des prénoms à l'état civil", true);
            return;
        }
        ajouterLigne(document, ParagraphAlignment.CENTER, "Objet : Requête aux fins de modification des mentions du sexe à l'état civil", true);
    }

    private void ajouterCorps(XWPFDocument document, DonneesDossier donneesDossier) {
        ajouterLigne(document, ParagraphAlignment.LEFT, "Monsieur ou Madame le/la Greffier·e en Chef,", false);

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

        ajouterLigne(document, ParagraphAlignment.BOTH, premierParagraphe.toString(), false);

        String sexeNaissance = normaliserSexe(donneesDossier.sexeEtatCivil());
        String sexeDemande = normaliserSexe(donneesDossier.sexeDemandeEtatCivil());

        String secondParagraphe = "En effet, je suis " + identiteGenreTrans(donneesDossier) + ", " + donneesDossier.adjectifNe() + " de sexe " + sexeNaissance + " et " + donneesDossier.adjectifInscrit() + " sur les registres de l'état civil comme " + formeTel(donneesDossier) + ". Cependant, je suis " + donneesDossier.adjectifConnu() + " comme " + identiteSociale(donneesDossier) + " par tous mes proches et je me présente publiquement comme étant de ce sexe.";

        ajouterLigne(document, ParagraphAlignment.BOTH, secondParagraphe, false);
        ajouterLigne(document, ParagraphAlignment.BOTH, "Au titre de l'article 61-5 du Code Civil, je vous prie donc de bien vouloir statuer sur les éléments suivants :", false);

        if (donneesDossier.changementPrenoms()) {
            ajouterLigne(document, ParagraphAlignment.BOTH, "• Dire et juger recevable et fondée ma demande de changement de la mention de sexe et de prénoms ;", false);
            ajouterLigne(document, ParagraphAlignment.BOTH, "• Ordonner que mon acte de naissance soit rectifié en ce sens que la mention « " + sexeNaissance + " » soit remplacée par la mention « " + sexeDemande + " » et que mes prénoms « " + nettoyer(donneesDossier.prenomsEtatCivil()) + " » soient remplacés par les prénoms « " + prenomsDemandes(donneesDossier) + " » ;", false);
        } else {
            ajouterLigne(document, ParagraphAlignment.BOTH, "• Dire et juger recevable et fondée ma demande de changement de la mention de sexe ;", false);
            ajouterLigne(document, ParagraphAlignment.BOTH, "• Ordonner que mon acte de naissance soit rectifié en ce sens que la mention « " + sexeNaissance + " » soit remplacée par la mention « " + sexeDemande + " » ;", false);
        }

        ajouterLigne(document, ParagraphAlignment.BOTH, "• Ordonner la retranscription du dispositif du jugement à intervenir en marge des registres de l'État Civil, en marge de mon acte de naissance et de tout autre document officiel me concernant ;", false);
        ajouterLigne(document, ParagraphAlignment.BOTH, "• Ordonner qu'aucune expédition des actes d'État Civil sans la mention desdites rectifications ne soit délivrée.", false);
        ajouterLigne(document, ParagraphAlignment.BOTH, "Je vous remercie de bien vouloir me communiquer dès que possible la date de l'audience.", false);
        ajouterLigne(document, ParagraphAlignment.BOTH, "En vous remerciant de l'intérêt que vous porterez à ma requête, Monsieur/Madame le/la Greffier·e en Chef, veuillez recevoir mes salutations les plus respectueuses.", false);
    }

    private void ajouterBlocSignature(XWPFDocument document) {
        ajouterParagrapheVide(document, ParagraphAlignment.RIGHT);
        ajouterParagrapheVide(document, ParagraphAlignment.RIGHT);
        ajouterLigne(document, ParagraphAlignment.RIGHT, "Signature", false);
        ajouterParagrapheVide(document, ParagraphAlignment.BOTH);
    }

    private void ajouterLignesTribunalEntete(XWPFDocument document, String tribunal) {
        String[] lignes = nettoyer(tribunal).split("\\R");
        boolean auMoinsUneLigne = false;
        for (String ligne : lignes) {
            String candidate = ligne.trim();
            if (candidate.isEmpty()) {
                continue;
            }
            ajouterLigne(document, ParagraphAlignment.RIGHT, candidate, true);
            auMoinsUneLigne = true;
        }
        if (!auMoinsUneLigne) {
            ajouterLigne(document, ParagraphAlignment.RIGHT, "", true);
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
