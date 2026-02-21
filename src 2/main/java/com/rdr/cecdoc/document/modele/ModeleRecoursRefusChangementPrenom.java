package com.rdr.cecdoc.document.modele;

import com.rdr.cecdoc.document.AlignementTexte;
import com.rdr.cecdoc.document.ModeleDocumentAbstrait;
import com.rdr.cecdoc.document.RedactriceDocument;
import com.rdr.cecdoc.model.DonneesRecoursRefusChangementPrenom;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class ModeleRecoursRefusChangementPrenom extends ModeleDocumentAbstrait<DonneesRecoursRefusChangementPrenom> {
    private static String domainesUsage(DonneesRecoursRefusChangementPrenom donnees) {
        List<String> domaines = new ArrayList<>();
        if (donnees.usageFamilial()) {
            domaines.add("familiale");
        }
        if (donnees.usageAmical()) {
            domaines.add("amicale");
        }
        if (donnees.usageProfessionnel()) {
            domaines.add("professionnelle");
        }
        if (donnees.usageScolaire()) {
            domaines.add("scolaire");
        }
        if (donnees.usageAssociatif()) {
            domaines.add("associative");
        }
        if (domaines.isEmpty()) {
            return "familiale, amicale, professionnelle, scolaire, associative";
        }
        return String.join(", ", domaines);
    }

    @Override
    public void rediger(DonneesRecoursRefusChangementPrenom donnees, RedactriceDocument redactrice) {
        Objects.requireNonNull(donnees, "donnees");
        Objects.requireNonNull(redactrice, "redactrice");

        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, donnees.identiteEnteteAvecEtatCivil(), false);
        ajouterBlocMultiligne(redactrice, AlignementTexte.GAUCHE, donnees.adressePostale(), false);
        if (aDuTexte(donnees.ligneContact())) {
            ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, donnees.ligneContact(), false);
        }
        ajouterParagrapheVide(redactrice, AlignementTexte.GAUCHE);

        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, donnees.ligneAvocat(), false);
        ajouterParagrapheVide(redactrice, AlignementTexte.GAUCHE);

        ajouterParagraphe(redactrice, AlignementTexte.DROITE, "À l’attention de Madame/Monsieur le/la Juge aux affaires familiales", false);
        ajouterBlocMultiligne(redactrice, AlignementTexte.DROITE, donnees.adresseTribunal(), false);
        ajouterParagraphe(redactrice, AlignementTexte.DROITE, "À " + donnees.villeRedaction() + ", le " + donnees.dateRedactionLongue(), false);
        ajouterParagrapheVide(redactrice, AlignementTexte.GAUCHE);

        ajouterParagraphe(redactrice, AlignementTexte.CENTRE, "Objet : Recours contre un refus de changement de prénom – saisine du/de la Juge aux affaires familiales", true);
        ajouterParagraphe(redactrice, AlignementTexte.CENTRE, "(Code civil, art. 60 ; Code de procédure civile, art. 1055-2, 1055-3 et 1055-4)", false);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "Madame/Monsieur le/la Juge aux affaires familiales,", false);

        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "Je, " + donnees.adjectifSousSigne() + " " + donnees.identiteIntroductionAvecEtatCivil() + ", " + donnees.adjectifNe() + " le " + donnees.dateNaissanceLongue() + " à " + donnees.lieuNaissance() + ", sollicite, conformément à l’article 60 du Code civil, l’autorisation de changer " + donnees.locutionMonMesPrenomsInscrits() + " à l’état civil de " + donnees.prenomsInscrits() + " en " + donnees.prenomsDemandes() + " dans le cadre d'une transition de genre.", false);

        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "La présente demande est formée à la suite d’un refus notifié le " + donnees.dateNotificationRefus() + " par Madame/Monsieur le/la Procureur·e de la République près le Tribunal judiciaire de " + donnees.villeTribunalJudiciaire() + ", après transmission de mon dossier par l’officier·e de l’état civil de la mairie de " + donnees.villeMairie() + ", au motif suivant : " + donnees.motifRefusMilieuPhrase() + ".", false);

        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "Je rappelle que, lorsque le/la procureur·e de la République s’oppose au changement de prénoms en application du dernier alinéa de l’article 60 du Code civil, la demande est portée contre lui/elle devant le/la juge aux affaires familiales du tribunal judiciaire auprès duquel il/elle exerce ses fonctions (Code de procédure civile, article 1055-2). La procédure applicable est la procédure écrite ordinaire (Code de procédure civile, article 1055-3).", false);

        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "1. Rappel du cadre juridique (Code civil, article 60)", true);
        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "En vertu de l’article 60 du Code civil, toute personne peut demander le changement de ses prénoms si la demande revêt un intérêt légitime. L’officier·e de l’état civil saisit sans délai le/la procureur·e de la République s’il/elle estime que la demande ne revêt pas un intérêt légitime, notamment lorsqu’elle est contraire à l’intérêt de l’enfant ou aux droits des tiers à voir protéger leur nom de famille. En cas d’opposition ou de refus du/de la procureur·e de la République, la personne demanderesse peut saisir le/la juge aux affaires familiales.", false);

        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "2. Exposé de la demande et de l’intérêt légitime", true);

        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "2.1 Prénom(s) sollicité(s)", true);
        ajouterPuce(redactrice, AlignementTexte.GAUCHE, 1, "Prénoms actuellement inscrits : " + donnees.prenomsInscrits(), false);
        ajouterPuce(redactrice, AlignementTexte.GAUCHE, 1, "Prénoms demandés : " + donnees.prenomsDemandes(), false);

        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "2.2 Faits établissant l’intérêt légitime", true);
        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "La demande est fondée sur un intérêt légitime, au sens de l’article 60 du Code civil, pour les raisons suivantes :", false);

        String domainesUsage = domainesUsage(donnees);
        ajouterPuce(redactrice, AlignementTexte.GAUCHE, 1, "Usage constant et notoire : je suis " + donnees.adjectifAppele() + " et " + donnees.adjectifConnu() + " sous le prénom " + donnees.premierPrenomUsage() + " dans ma vie : " + domainesUsage + ".", false);
        ajouterPuce(redactrice, AlignementTexte.GAUCHE, 1, "Cohérence identitaire et sociale (parcours de transition) : je suis une personne transgenre ; les prénoms sollicités correspondent à mon identité vécue et sociale. Ils sont ceux sous lesquels je suis " + donnees.adjectifIdentifie() + " dans la vie quotidienne, et leur usage est stable et vérifiable par les pièces produites.", false);
        ajouterPuce(redactrice, AlignementTexte.GAUCHE, 1, "Protection de la vie privée et prévention des difficultés : le maintien des anciens prénoms sur des documents, correspondances et démarches génère des difficultés répétées (incompréhensions, erreurs administratives, outing, divulgations non souhaitées d’éléments de vie privée, mise en danger ou exposition involontaire, atteintes à la dignité, risques de discrimination), lesquelles sont directement évitées par la concordance entre mon état civil et mon identité d’usage.", false);
        if (donnees.aAnecdotes()) {
            ajouterPuce(redactrice, AlignementTexte.GAUCHE, 2, donnees.anecdotesDifficultes(), false);
        }
        ajouterPuce(redactrice, AlignementTexte.GAUCHE, 1, "Absence d’atteinte aux droits des tiers : la demande ne porte pas atteinte aux droits des tiers à voir protéger leur nom de famille ; elle ne poursuit aucun but frauduleux et n’a d’autre finalité que d’aligner mes prénoms d’état civil avec l’usage constant, l’identité de genre et l’identification sociale effective.", false);

        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "2.3 Discussion du motif de refus", true);
        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "Le refus opposé le " + donnees.dateRefusDiscussion() + " est motivé par : " + donnees.motifRefusNotifie() + ".", false);
        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "Je conteste ce motif pour les raisons suivantes :", false);
        for (String raison : donnees.raisonsContestationListe()) {
            ajouterPuce(redactrice, AlignementTexte.GAUCHE, 1, raison, false);
        }

        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "3. Demande au tribunal", true);
        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "Par ces motifs et tous autres à produire, déduire ou suppléer, je demande à Madame/Monsieur le/la Juge aux affaires familiales de :", false);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "1) Dire la demande recevable comme formée contre Madame/Monsieur le/la Procureur·e de la République près le Tribunal judiciaire de " + donnees.villeTribunalJudiciaire() + " (CPC, art. 1055-2) ;", false);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "2) Dire que la demande revêt un intérêt légitime au sens de l’article 60 du Code civil ;", false);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "3) Ordonner le changement de mes prénoms à l’état civil de " + donnees.prenomsInscrits() + " en " + donnees.prenomsDemandes() + " ;", false);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "4) Dire que le dispositif de la décision ordonnant la modification du prénom sera transmis sans délai, par le/la procureur·e de la République, à l’officier·e de l’état civil dépositaire des actes concernés pour mention en marge (CPC, art. 1055-4) ;", false);

        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "4. Pièces justificatives (bordereau indicatif)", true);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "1. Copie intégrale de l’acte de naissance ;", false);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "2. Copie de la pièce d’identité ;", false);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "3. Justificatif de domicile ;", false);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "4. Copie du récépissé de dépôt en mairie du " + donnees.dateRecepisseDepot() + " ;", false);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "5. Copie de la décision de refus ou d’opposition du/de la procureur·e de la République, notifiée le " + donnees.dateDecisionRefus() + " ;", false);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "6. Justificatifs d’usage du prénom " + donnees.premierPrenomUsage() + " (documents scolaires/professionnels, correspondances, cartes, comptes, etc.) ;", false);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "7. Attestations de proches (témoignages) ;", false);

        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "Je vous prie d’agréer, Madame/Monsieur le/la Juge aux affaires familiales, l’expression de ma considération distinguée.", false);
        ajouterParagrapheVide(redactrice, AlignementTexte.GAUCHE);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, donnees.signature(), false);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "Signature", false);
    }
}
