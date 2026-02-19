package com.rdr.cecdoc.docx;

import com.rdr.cecdoc.model.DonneesLettreAdministration;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public final class GenerateurLettreAdministration extends GenerateurLettreAbstrait {
    private static final DateTimeFormatter FORMAT_DATE = DateTimeFormatter.ofPattern("dd/MM/uuuu", Locale.ROOT);

    public void generer(DonneesLettreAdministration donneesLettreAdministration, File destination) throws IOException {
        Objects.requireNonNull(donneesLettreAdministration, "donneesLettreAdministration");
        ecrireDocument(destination, document -> rediger(document, donneesLettreAdministration));
    }

    private void rediger(XWPFDocument document, DonneesLettreAdministration donnees) {
        ajouterLigne(document, ParagraphAlignment.LEFT, donnees.identiteEntete(), false);
        ajouterBlocMultiligne(document, ParagraphAlignment.LEFT, donnees.adressePostale(), false);
        ajouterLigne(document, ParagraphAlignment.LEFT, "Tél : " + donnees.telephonePortable(), false);
        ajouterLigne(document, ParagraphAlignment.LEFT, donnees.courriel(), false);
        ajouterParagrapheVide(document, ParagraphAlignment.LEFT);

        ajouterBlocMultiligne(document, ParagraphAlignment.RIGHT, donnees.adresseDestinataire(), false);
        ajouterParagrapheVide(document, ParagraphAlignment.LEFT);

        ajouterLigne(document, ParagraphAlignment.LEFT, "Objet : Changement d’état civil", true);
        ajouterParagrapheVide(document, ParagraphAlignment.LEFT);

        ajouterLigne(document, ParagraphAlignment.LEFT, "Madame, Monsieur,", false);
        ajouterLigne(document, ParagraphAlignment.BOTH, construireParagraphePrincipal(donnees), false);

        if (donnees.changementPrenom() && !donnees.changementSexe()) {
            ajouterLigne(document, ParagraphAlignment.BOTH, "Bien que ma mention de sexe n’ait pas encore été modifiée par le tribunal judiciaire, je vous saurais gré de bien vouloir modifier ma civilité également, la mise en adéquation de l’identité sociale et du prénom administratif n’étant pas une simple procédure de reconnaissance mais permettant tout particulièrement la protection de la vie privée de l’usager·e trans, puisque celui/celle-ci n’est alors plus contraint·e à dévoiler sa transidentité, situation enfreignant le droit à la vie privée (article 8 de la Convention Européenne des Droits de l’Homme et 9 du Code civil). De plus, la civilité n'est pas un élément constitutif de l'état civil. Ce fait est rappelé par la décision MLD-2014-058 du Défenseur des Droits ainsi que dans le cadre d’un établissement bancaire, dans le circulaire du Premier Ministre n°5575/SG du 21 février 2012.", false);
            ajouterLigne(document, ParagraphAlignment.BOTH, "De ce fait, vous comprendrez que l’inadéquation entre mon prénom " + donnees.prenomUsage() + " et la mention M./H. au sein des communications internes de votre organisme me mette dans une situation plus qu’inconfortable, et cela inutilement puisque la civilité n’est légalement pas définie un élément de l’état civil.", false);
        }

        ajouterLigne(document, ParagraphAlignment.LEFT, "Bien cordialement,", false);
        ajouterLigne(document, ParagraphAlignment.LEFT, "Fait à " + donnees.villeActuelle() + ", le " + LocalDate.now().format(FORMAT_DATE), false);
        ajouterLigne(document, ParagraphAlignment.LEFT, donnees.identiteSignature(), false);
        ajouterLigne(document, ParagraphAlignment.LEFT, "Signature", false);
    }

    private String construireParagraphePrincipal(DonneesLettreAdministration donnees) {
        return donnees.premierParagrapheModifications() + " Cela dans le cadre d’une transition de genre, comme en attestent l’extrait d’acte de naissance et la pièce d’identité ci-joints. Je vous contacte pour " + donnees.objetPronomsObjet() + " modifier également auprès de vos services, conformément " + donnees.objetConformite() + ".";
    }
}
