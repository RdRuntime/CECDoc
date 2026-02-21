package com.rdr.cecdoc.document.modele;

import com.rdr.cecdoc.document.AlignementTexte;
import com.rdr.cecdoc.document.ModeleDocumentAbstrait;
import com.rdr.cecdoc.document.RedactriceDocument;
import com.rdr.cecdoc.model.DonneesLettreRelanceTribunal;

import java.util.Objects;

public final class ModeleLettreRelanceTribunal extends ModeleDocumentAbstrait<DonneesLettreRelanceTribunal> {
    @Override
    public void rediger(DonneesLettreRelanceTribunal donnees, RedactriceDocument redactrice) {
        Objects.requireNonNull(donnees, "donnees");
        Objects.requireNonNull(redactrice, "redactrice");

        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, donnees.identiteAvecEtatCivilSiNecessaire(), false);
        ajouterBlocMultiligne(redactrice, AlignementTexte.GAUCHE, donnees.adressePostale(), false);
        if (aDuTexte(donnees.ligneContact())) {
            ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, donnees.ligneContact(), false);
        }
        ajouterParagrapheVide(redactrice, AlignementTexte.GAUCHE);

        ajouterParagraphe(redactrice, AlignementTexte.DROITE, "À l’attention du Greffe", false);
        ajouterBlocMultiligne(redactrice, AlignementTexte.DROITE, donnees.adresseTribunal(), false);
        ajouterParagraphe(redactrice, AlignementTexte.DROITE, "À " + donnees.villeRedaction() + ", le " + dateOuAujourdhui(donnees.dateRedaction()), false);
        ajouterParagrapheVide(redactrice, AlignementTexte.GAUCHE);

        ajouterParagraphe(redactrice, AlignementTexte.CENTRE, "Objet : Relance – requête en modification de la mention du sexe à l’état civil (articles 61-5 et s. du Code civil / articles 1055-5 et s. du CPC)", true);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "Madame, Monsieur,", false);

        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "Je, " + donnees.adjectifSousSigne() + " " + donnees.identiteAvecEtatCivilSiNecessaire() + ", fais suite au dépôt, en date du " + dateOuAujourdhui(donnees.dateDepotEnvoi()) + ", de ma requête aux fins de modification de la mention du sexe" + donnees.suffixeObjetChangementPrenoms() + " à l’état civil.", false);

        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "1. Rappel du cadre juridique", true);
        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "La demande relève de la matière gracieuse et est régie par les articles 61-5 et suivants du Code civil ainsi que par les articles 1055-5 et suivants du Code de procédure civile.", false);

        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "2. Demande", true);
        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "À ce jour, je n’ai pas reçu " + donnees.complementInformationAttendue() + ". Je vous remercie de bien vouloir :", false);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "• " + donnees.demandePrincipaleInformationAttendue(), false);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "• " + donnees.demandeSecondaireInformationAttendue(), false);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "• et, le cas échéant, les modalités de convocation.", false);

        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "3. Références utiles", true);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "• Nom : " + donnees.identiteAvecEtatCivilSiNecessaire(), false);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "• Date/Lieu de naissance : " + dateOuAujourdhui(donnees.dateNaissance()) + " – " + donnees.lieuNaissance(), false);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "• Adresse : " + donnees.adresseInline(), false);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "• Téléphone/E-mail : " + donnees.ligneContact(), false);
        if (donnees.referenceDossierRenseignee()) {
            ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "• Référence dossier : " + donnees.referenceDossier(), false);
        }

        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "4. Pièces jointes", true);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "1. Copie de la requête ;", false);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "2. Bordereau des pièces ;", false);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "3. Preuve de dépôt/envoi ;", false);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "4. Autres pièces pertinentes.", false);

        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "Je vous prie d’agréer, Madame, Monsieur, l’expression de ma considération distinguée.", false);
        ajouterParagrapheVide(redactrice, AlignementTexte.GAUCHE);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, donnees.signature(), false);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "Signature", false);
    }
}
