package com.rdr.cecdoc.document.modele;

import com.rdr.cecdoc.document.AlignementTexte;
import com.rdr.cecdoc.document.ModeleDocumentAbstrait;
import com.rdr.cecdoc.document.RedactriceDocument;
import com.rdr.cecdoc.model.DonneesEnfantActe;
import com.rdr.cecdoc.model.DonneesLettreMiseAJourActesLies;

import java.util.Objects;

public final class ModeleLettreMiseAJourActesLies extends ModeleDocumentAbstrait<DonneesLettreMiseAJourActesLies> {
    @Override
    public void rediger(DonneesLettreMiseAJourActesLies donnees, RedactriceDocument redactrice) {
        Objects.requireNonNull(donnees, "donnees");
        Objects.requireNonNull(redactrice, "redactrice");

        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, donnees.identiteEntete(), false);
        ajouterBlocMultiligne(redactrice, AlignementTexte.GAUCHE, donnees.adressePostale(), false);
        if (aDuTexte(donnees.ligneContact())) {
            ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, donnees.ligneContact(), false);
        }
        ajouterParagrapheVide(redactrice, AlignementTexte.GAUCHE);

        ajouterParagraphe(redactrice, AlignementTexte.DROITE, donnees.ligneDestinataireTitre(), false);
        ajouterParagraphe(redactrice, AlignementTexte.DROITE, donnees.ligneDestinataireSousTitre(), false);
        ajouterBlocMultiligne(redactrice, AlignementTexte.DROITE, donnees.adresseDestinataire(), false);
        ajouterParagraphe(redactrice, AlignementTexte.DROITE, "À " + donnees.villeRedaction() + ", le " + dateOuAujourdhui(donnees.dateRedaction()), false);
        ajouterParagrapheVide(redactrice, AlignementTexte.GAUCHE);

        ajouterParagraphe(redactrice, AlignementTexte.CENTRE, "Objet : " + donnees.objetMiseAJourActes() + " à la suite " + donnees.formuleSuiteActeDecisionDefinitif() + " – " + donnees.objetTypeChangements(), true);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "Madame/Monsieur,", false);

        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "Je, " + donnees.adjectifSousSigne() + " " + donnees.identiteEntete() + ", " + donnees.adjectifNe() + " le " + dateOuAujourdhui(donnees.dateNaissance()) + " à " + donnees.lieuNaissance() + ", sollicite " + donnees.formuleDemandeInitiale() + ", à la suite " + donnees.formuleSuiteActeDecisionRenduPar() + " en date du " + dateOuAujourdhui(donnees.dateDecision()) + ", " + donnees.formuleActeDecisionDevenuDefinitif() + " le " + dateOuAujourdhui(donnees.dateDecisionDefinitive()) + ".", false);

        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "1. Rappel de l’objet de la demande", true);
        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, donnees.formuleActeDecisionPrecite() + " a ordonné : " + descriptionDecision(donnees) + ". Je vous demande d’ordonner ou de faire procéder à " + donnees.formuleRappelDemande() + ", selon ma situation, ainsi que, le cas échéant, la délivrance ou mise à jour du livret de famille.", false);

        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "2. Actes à mettre à jour", true);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "A) Actes me concernant", true);
        if (donnees.acteNaissanceRequerant()) {
            ajouterPuce(redactrice, AlignementTexte.GAUCHE, 1, donnees.ligneActeNaissanceRequerant(), false);
        }

        if (donnees.concernePartenaire()) {
            ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "B) Actes concernant mon " + donnees.designationPartenaire(), true);
            if (donnees.acteMariage()) {
                ajouterPuce(redactrice, AlignementTexte.GAUCHE, 1, donnees.ligneActeMariage(), false);
            }
            if (donnees.acteNaissancePartenaire()) {
                ajouterPuce(redactrice, AlignementTexte.GAUCHE, 1, donnees.ligneActeNaissancePartenaire(), false);
            }
            if (donnees.mentionPacs()) {
                ajouterPuce(redactrice, AlignementTexte.GAUCHE, 1, donnees.ligneMentionPacs(), false);
            }
        }

        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "C) Actes concernant mes enfants", true);
        if (donnees.aDesEnfants()) {
            for (DonneesEnfantActe enfant : donnees.enfants()) {
                ajouterPuce(redactrice, AlignementTexte.GAUCHE, 1, enfant.ligneActeNaissance(), false);
            }
        }

        if (donnees.optionLivretRenseignee()) {
            ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "D) Livret de famille", true);
            ajouterPuce(redactrice, AlignementTexte.GAUCHE, 1, donnees.ligneLivret(), false);
        }

        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "3. Consentements requis", true);
        ajouterPuce(redactrice, AlignementTexte.GAUCHE, 1, donnees.determinantPartenaire() + " " + donnees.designationPartenaire() + " (mise à jour liée à l’acte de mariage ou à la mention de PACS) ;", false);
        ajouterPuce(redactrice, AlignementTexte.GAUCHE, 1, "chaque enfant majeur·e concerné·e (mise à jour de son acte de naissance) ;", false);
        ajouterPuce(redactrice, AlignementTexte.GAUCHE, 1, "l’autre représentant·e légal·e de chaque enfant mineur·e concerné·e.", false);
        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "Je joins les consentements écrits nécessaires, ou, à défaut, je sollicite les modalités permettant de les recueillir et de finaliser la mise à jour.", false);

        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "4. Pièces jointes", true);
        ajouterPuce(redactrice, AlignementTexte.GAUCHE, 1, "Copie intégrale " + donnees.formuleActeDecisionPieceJointe() + " du " + dateOuAujourdhui(donnees.dateDecision()) + " ;", false);
        ajouterPuce(redactrice, AlignementTexte.GAUCHE, 1, "Consentement écrit de " + donnees.determinantPartenaire() + " " + donnees.designationPartenaire() + " pour la mise à jour des actes concernés ;", false);
        ajouterPuce(redactrice, AlignementTexte.GAUCHE, 1, "Consentement écrit de chaque enfant majeur·e concerné·e ;", false);
        ajouterPuce(redactrice, AlignementTexte.GAUCHE, 1, "Consentement de l’autre représentant·e légal·e pour chaque enfant mineur·e concerné·e ;", false);
        ajouterPuce(redactrice, AlignementTexte.GAUCHE, 1, "Copies intégrales des actes à mettre à jour (mariage, naissances, mention de PACS, autres actes) ;", false);
        ajouterPuce(redactrice, AlignementTexte.GAUCHE, 1, "Copie d’une pièce d’identité ;", false);
        ajouterPuce(redactrice, AlignementTexte.GAUCHE, 1, "Tout autre document utile.", false);

        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "5. Demande", true);
        ajouterPuce(redactrice, AlignementTexte.GAUCHE, 1, "ordonner la mise à jour des actes listés ci-dessus, conformément à " + donnees.formuleActeDecisionDevenueDefinitive() + " ;", false);
        ajouterPuce(redactrice, AlignementTexte.GAUCHE, 1, "me confirmer par écrit la prise en charge de la demande et, le cas échéant, la liste des pièces complémentaires nécessaires ;", false);
        ajouterPuce(redactrice, AlignementTexte.GAUCHE, 1, "m’indiquer les modalités pour obtenir " + donnees.formuleDemandeLivret() + " auprès de la mairie compétente.", false);

        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "Je vous prie d’agréer, Madame/Monsieur, l’expression de ma considération distinguée.", false);
        ajouterParagrapheVide(redactrice, AlignementTexte.GAUCHE);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, donnees.signature(), false);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "Signature", false);
    }

    private String descriptionDecision(DonneesLettreMiseAJourActesLies donnees) {
        if (donnees.changementPrenoms() && donnees.changementSexe()) {
            return "la modification de la mention du sexe à l’état civil et le changement de mes prénoms";
        }
        if (donnees.changementPrenoms()) {
            return "le changement de mes prénoms";
        }
        if (donnees.changementSexe()) {
            return "la modification de la mention du sexe à l’état civil";
        }
        return "la mise à jour de mes actes liés";
    }
}
