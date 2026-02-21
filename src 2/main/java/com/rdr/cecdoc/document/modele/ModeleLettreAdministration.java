package com.rdr.cecdoc.document.modele;

import com.rdr.cecdoc.document.AlignementTexte;
import com.rdr.cecdoc.document.ModeleDocumentAbstrait;
import com.rdr.cecdoc.document.RedactriceDocument;
import com.rdr.cecdoc.model.DonneesLettreAdministration;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

public final class ModeleLettreAdministration extends ModeleDocumentAbstrait<DonneesLettreAdministration> {
    private static final DateTimeFormatter FORMAT_DATE = DateTimeFormatter.ofPattern("dd/MM/uuuu", Locale.ROOT);

    @Override
    public void rediger(DonneesLettreAdministration donnees, RedactriceDocument redactrice) {
        Objects.requireNonNull(donnees, "donnees");
        Objects.requireNonNull(redactrice, "redactrice");

        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, donnees.identiteEntete(), false);
        ajouterBlocMultiligne(redactrice, AlignementTexte.GAUCHE, donnees.adressePostale(), false);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "Tél : " + donnees.telephonePortable(), false);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, donnees.courriel(), false);
        ajouterParagrapheVide(redactrice, AlignementTexte.GAUCHE);

        ajouterBlocMultiligne(redactrice, AlignementTexte.DROITE, donnees.adresseDestinataire(), false);
        ajouterParagrapheVide(redactrice, AlignementTexte.GAUCHE);

        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "Objet : Changement d’état civil", true);
        ajouterParagrapheVide(redactrice, AlignementTexte.GAUCHE);

        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "Madame, Monsieur,", false);
        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, construireParagraphePrincipal(donnees), false);

        if (donnees.changementPrenom() && !donnees.changementSexe()) {
            ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "Bien que ma mention de sexe n’ait pas encore été modifiée par le tribunal judiciaire, je vous saurais gré de bien vouloir modifier ma civilité également, la mise en adéquation de l’identité sociale et du prénom administratif n’étant pas une simple procédure de reconnaissance mais permettant tout particulièrement la protection de la vie privée de l’usager·e trans, puisque celui/celle-ci n’est alors plus contraint·e à dévoiler sa transidentité, situation enfreignant le droit à la vie privée (article 8 de la Convention Européenne des Droits de l’Homme et 9 du Code civil). De plus, la civilité n'est pas un élément constitutif de l'état civil. Ce fait est rappelé par la décision MLD-2014-058 du Défenseur des Droits ainsi que dans le cadre d’un établissement bancaire, dans le circulaire du Premier Ministre n°5575/SG du 21 février 2012.", false);
            ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "De ce fait, vous comprendrez que l’inadéquation entre mon prénom " + donnees.prenomUsage() + " et la mention M./H. au sein des communications internes de votre organisme me mette dans une situation plus qu’inconfortable, et cela inutilement puisque la civilité n’est légalement pas définie un élément de l’état civil.", false);
        }

        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "Bien cordialement,", false);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "Fait à " + donnees.villeActuelle() + ", le " + LocalDate.now().format(FORMAT_DATE), false);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, donnees.identiteSignature(), false);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "Signature", false);
    }

    private String construireParagraphePrincipal(DonneesLettreAdministration donnees) {
        return donnees.premierParagrapheModifications() + " Cela dans le cadre d’une transition de genre, comme en attestent l’extrait d’acte de naissance et la pièce d’identité ci-joints. Je vous contacte pour " + donnees.objetPronomsObjet() + " modifier également auprès de vos services, conformément " + donnees.objetConformite() + ".";
    }
}
