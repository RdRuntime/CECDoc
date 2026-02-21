package com.rdr.cecdoc.document.modele;

import com.rdr.cecdoc.document.AlignementTexte;
import com.rdr.cecdoc.document.ModeleDocumentAbstrait;
import com.rdr.cecdoc.document.RedactriceDocument;
import com.rdr.cecdoc.model.DonneesRecoursRefusChangementSexe;

import java.util.Objects;

public final class ModeleRecoursRefusChangementSexe extends ModeleDocumentAbstrait<DonneesRecoursRefusChangementSexe> {
    @Override
    public void rediger(DonneesRecoursRefusChangementSexe donnees, RedactriceDocument redactrice) {
        Objects.requireNonNull(donnees, "donnees");
        Objects.requireNonNull(redactrice, "redactrice");

        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, donnees.identiteAvecEtatCivilSiNecessaire(), false);
        ajouterBlocMultiligne(redactrice, AlignementTexte.GAUCHE, donnees.adressePostale(), false);
        if (aDuTexte(donnees.ligneContact())) {
            ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, donnees.ligneContact(), false);
        }
        ajouterParagrapheVide(redactrice, AlignementTexte.GAUCHE);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, donnees.ligneAvocat(), false);
        ajouterParagrapheVide(redactrice, AlignementTexte.GAUCHE);

        ajouterParagraphe(redactrice, AlignementTexte.DROITE, "À l’attention de Madame/Monsieur le/la Premier·e Président·e", false);
        ajouterParagraphe(redactrice, AlignementTexte.DROITE, "Cour d’appel de " + donnees.villeCourAppel(), false);
        ajouterBlocMultiligne(redactrice, AlignementTexte.DROITE, donnees.adresseCourAppel(), false);
        ajouterParagraphe(redactrice, AlignementTexte.DROITE, "À " + donnees.villeRedaction() + ", le " + dateOuAujourdhui(donnees.dateRedaction()), false);
        ajouterParagrapheVide(redactrice, AlignementTexte.GAUCHE);

        ajouterParagraphe(redactrice, AlignementTexte.CENTRE, donnees.objetRecours(), true);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "Madame/Monsieur le/la Premier·e Président·e,", false);

        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "Je, " + donnees.adjectifSousSigne() + " " + donnees.identiteAvecEtatCivilSiNecessaire() + ", " + donnees.adjectifNe() + " le " + dateOuAujourdhui(donnees.dateNaissance()) + " à " + donnees.lieuNaissance() + ", ai l’honneur de former appel du jugement " + donnees.referenceJugementEtTribunal() + " rendu le " + dateOuAujourdhui(donnees.dateJugement()) + ", qui a rejeté ma requête " + donnees.descriptionRequeteRefusee() + ".", false);

        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "1. Rappel du cadre juridique", true);
        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "Les articles 61-5 à 61-8 du Code civil organisent " + donnees.descriptionProcedureChangement() + ". Ils exigent de démontrer par une réunion suffisante de faits que la mention relative au sexe dans les actes de l’état civil ne correspond pas à celle dans laquelle je me présente et dans laquelle je suis " + donnees.adjectifConnu() + ". L’article 61-5 précise qu’il n’est pas nécessaire de suivre un traitement médical ou d’avoir subi une opération chirurgicale pour demander cette modification ; la demande ne peut être refusée en l’absence de ces éléments. La procédure est gratuite et la représentation par avocat n’est pas obligatoire en première instance. En appel, la voie de recours est ouverte et l’avocat est obligatoire.", false);
        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "Les articles 1055-5 à 1055-10 du Code de procédure civile prévoient que la demande relève de la matière gracieuse et fixent les modalités d’appel.", false);

        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "2. Motivation de l’appel", true);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "• Je me présente publiquement comme appartenant au sexe " + donnees.genreRevendique() + " et je suis " + donnees.adjectifConnu() + " de mon entourage sous ce sexe.", false);
        if (!donnees.changementPrenoms()) {
            ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "• J’ai obtenu la modification de mes prénoms pour qu’ils correspondent à mon identité de genre.", false);
        }
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "• Divers témoignages et pièces, joints à la requête initiale, démontrent la réunion suffisante de faits exigée par l’article 61-5.", false);
        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "Le Tribunal judiciaire a néanmoins rejeté ma demande au motif suivant : " + donnees.motifRefusMilieuPhrase() + ", méconnaissant ainsi les dispositions précitées et la jurisprudence constante. Ce refus porte atteinte à mon droit au respect de la vie privée et à mon identité de genre." + donnees.mentionPrenomsDansMotivation(), false);

        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "3. Demande", true);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "Par ces motifs et tous autres à produire, il est demandé à la Cour d’appel de :", false);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "1. Réformer le jugement " + donnees.referenceJugementEtTribunal() + " en date du " + dateOuAujourdhui(donnees.dateJugement()) + " ;", false);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "2. Constater que je satisfais aux conditions des articles 61-5 et suivants du Code civil ;", false);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, donnees.ligneDemandeModification(), false);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "4. Dire que la décision sera transmise au/à la procureur·e de la République et aux officier·es d’état civil pour mise à jour des actes concernés.", false);

        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "4. Pièces jointes", true);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "1. Copie intégrale du jugement attaqué ;", false);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "2. Copie de ma requête initiale et de son bordereau de pièces ;", false);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "3. Attestations de témoins, pièces d’identité et justificatifs de l’utilisation sociale de mon identité ;", false);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "4. Autres pièces pertinentes.", false);

        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "Je vous prie d’agréer, Madame/Monsieur le/la Premier·e Président·e, l’expression de ma considération distinguée.", false);
        ajouterParagrapheVide(redactrice, AlignementTexte.GAUCHE);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, donnees.signature(), false);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "Signature", false);
    }
}
