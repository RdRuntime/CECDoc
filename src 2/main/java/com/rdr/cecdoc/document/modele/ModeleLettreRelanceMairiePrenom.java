package com.rdr.cecdoc.document.modele;

import com.rdr.cecdoc.document.AlignementTexte;
import com.rdr.cecdoc.document.ModeleDocumentAbstrait;
import com.rdr.cecdoc.document.RedactriceDocument;
import com.rdr.cecdoc.model.DonneesLettreRelanceMairiePrenom;

import java.util.Objects;

public final class ModeleLettreRelanceMairiePrenom extends ModeleDocumentAbstrait<DonneesLettreRelanceMairiePrenom> {
    @Override
    public void rediger(DonneesLettreRelanceMairiePrenom donnees, RedactriceDocument redactrice) {
        Objects.requireNonNull(donnees, "donnees");
        Objects.requireNonNull(redactrice, "redactrice");

        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, donnees.identiteEntete(), false);
        ajouterBlocMultiligne(redactrice, AlignementTexte.GAUCHE, donnees.adressePostale(), false);
        if (aDuTexte(donnees.ligneContact())) {
            ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, donnees.ligneContact(), false);
        }
        ajouterParagrapheVide(redactrice, AlignementTexte.GAUCHE);

        ajouterParagraphe(redactrice, AlignementTexte.DROITE, "À l’attention de Madame/Monsieur l’Officier·e de l’état civil", false);
        ajouterBlocMultiligne(redactrice, AlignementTexte.DROITE, donnees.adresseMairie(), false);
        ajouterParagraphe(redactrice, AlignementTexte.DROITE, "À " + donnees.villeRedaction() + ", le " + dateOuAujourdhui(donnees.dateRedaction()), false);
        ajouterParagrapheVide(redactrice, AlignementTexte.GAUCHE);

        ajouterParagraphe(redactrice, AlignementTexte.CENTRE, "Objet : Relance + accusé de réception – demande de changement de prénoms (article 60 du Code civil) / CRPA (article L.112-3)", true);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "Madame, Monsieur,", false);

        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "Je, " + donnees.adjectifSousSigne() + " " + donnees.identiteEnteteAvecEtatCivil() + ", fais suite à ma demande de changement de prénoms déposée / adressée le " + dateOuAujourdhui(donnees.dateDemande()) + " sur le fondement de l’article 60 du Code civil, de " + donnees.prenomsEtatCivil() + " vers " + donnees.prenomsDemandes() + ".", false);

        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "1. Accusé de réception", true);
        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "Conformément à l’article L.112-3 du Code des relations entre le public et l’administration, une demande adressée à l’administration fait l’objet d’un accusé de réception (sauf exceptions prévues par le texte).", false);

        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "2. Demande", true);
        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "À ce jour, je n’ai pas reçu" + (!donnees.referenceDossierRenseignee() ? " d’accusé de réception ni " : " ") + "d’information sur l’instruction. Je vous remercie de bien vouloir :", false);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "1) me transmettre l’accusé de réception (ou confirmer la date de réception)", false);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "2) m’indiquer l’état d’avancement et, le cas échéant, les pièces complémentaires nécessaires", false);

        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "3. Éléments d’identification", true);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "- Identité : " + donnees.identiteEnteteAvecEtatCivil(), false);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "- Prénoms à l’état civil : " + donnees.prenomsEtatCivil(), false);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "- Prénoms demandés : " + donnees.prenomsDemandes(), false);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "- Date et lieu de naissance : " + dateOuAujourdhui(donnees.dateNaissance()) + " – " + donnees.lieuNaissance(), false);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "- Adresse : " + donnees.adresseInline(), false);
        if (donnees.referenceDossierRenseignee()) {
            ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "- Référence de dossier : " + donnees.referenceDossier(), false);
        }

        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "Je vous prie d’agréer, Madame, Monsieur, l’expression de ma considération distinguée.", false);
        ajouterParagrapheVide(redactrice, AlignementTexte.GAUCHE);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, donnees.signature(), false);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "Signature", false);
    }
}
