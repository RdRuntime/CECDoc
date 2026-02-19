package com.rdr.cecdoc.docx;

import com.rdr.cecdoc.model.DonneesLettreUniversite;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public final class GenerateurLettreUniversite extends GenerateurLettreAbstrait {
    private static final DateTimeFormatter FORMAT_DATE = DateTimeFormatter.ofPattern("dd/MM/uuuu", Locale.ROOT);

    public void generer(DonneesLettreUniversite donneesLettreUniversite, File destination) throws IOException {
        Objects.requireNonNull(donneesLettreUniversite, "donneesLettreUniversite");
        ecrireDocument(destination, document -> rediger(document, donneesLettreUniversite));
    }

    private void rediger(XWPFDocument document, DonneesLettreUniversite donnees) {
        ajouterLigne(document, ParagraphAlignment.LEFT, donnees.identiteEntete(), false);
        ajouterBlocMultiligne(document, ParagraphAlignment.LEFT, donnees.adressePostale(), false);
        ajouterLigne(document, ParagraphAlignment.LEFT, "Tél : " + donnees.telephonePortable(), false);
        ajouterLigne(document, ParagraphAlignment.LEFT, "Courriel : " + donnees.courriel(), false);
        ajouterLigne(document, ParagraphAlignment.LEFT, "INE : " + donnees.ine(), false);
        ajouterParagrapheVide(document, ParagraphAlignment.LEFT);

        ajouterBlocMultiligne(document, ParagraphAlignment.RIGHT, donnees.nomAdresseUniversite(), false);
        ajouterParagrapheVide(document, ParagraphAlignment.RIGHT);
        ajouterLigne(document, ParagraphAlignment.RIGHT, "Fait à " + donnees.villeActuelle() + ", le " + LocalDate.now().format(FORMAT_DATE), false);
        ajouterLigne(document, ParagraphAlignment.RIGHT, "Sous toutes réserves", false);
        ajouterLigne(document, ParagraphAlignment.CENTER, "Objet : Changement de mon prénom d'usage à l'université dans le cadre d'une transition de genre", true);
        ajouterParagrapheVide(document, ParagraphAlignment.LEFT);

        ajouterLigne(document, ParagraphAlignment.LEFT, "Madame, Monsieur,", false);
        ajouterLigne(document, ParagraphAlignment.LEFT, "Je vous écris pour vous faire part de ma situation,", false);

        String premierParagraphe = "Je suis une personne transgenre (cf. personne dont le sexe assigné ne correspond pas au genre), " + ponctuer(donnees.explicationParcoursMilieuPhrase());
        if (donnees.genreNonBinaire()) {
            ajouterLigne(document, ParagraphAlignment.BOTH, premierParagraphe, false);
        } else {
            ajouterLigne(document, ParagraphAlignment.BOTH, premierParagraphe + " Mon marqueur de sexe à l’état civil est toujours " + donnees.marqueurEtatCivilOppose() + ".", false);
        }

        ajouterLigne(document, ParagraphAlignment.BOTH, "Mon prénom à l’état-civil est toujours " + donnees.prenomEtatCivil() + ", néanmoins, celui-ci n’est pas mon prénom d’usage, qui est " + donnees.prenomUsage() + ".", false);
        ajouterLigne(document, ParagraphAlignment.BOTH, "La lettre de Madame la Ministre de l’enseignement supérieur en date du 17 avril 2019, ayant pour objet « Recommandations pour favoriser l’inclusion des personnes transgenres dans la vie étudiante et dans les établissements d’enseignement supérieur et de recherche » indique :", false);
        ajouterLigne(document, ParagraphAlignment.BOTH, "« Dans cette optique, et en lien avec la politique gouvernement de prévention des violences sexistes et sexuelles et de lutte contre la haine et les discriminations anti-LGBT, le ministère invite l’ensemble des établissements d’enseignement supérieur et de recherche à faciliter l’utilisation du prénom d’usage sur les documents et pièces internes à l’établissement pour les personnes transgenres, tout au long de leur scolarité ou de leur carrière professionnelle. » (Page 2)", false);

        ajouterLigne(document, ParagraphAlignment.BOTH, "Par ailleurs, le Défenseur des Droits a rappelé dans une décision de 2015 (MLD-2014-058) que la mention de civilité (c’est à dire « monsieur » ou « madame ») n’est en aucun cas un élément de l’État-Civil. L’acceptation de la modification de civilité s’impose à toute administration scolaire.", false);

        ajouterLigne(document, ParagraphAlignment.BOTH, "Je demande donc, conformément à ces documents, que sur l’ensemble sur l’ensemble des documents, logiciels informatiques, listes d’appels, cartes scolaires et écrans de connexion :", false);
        ajouterLigne(document, ParagraphAlignment.LEFT, "• Soit indiqué mon prénom d’usage : " + donnees.prenomUsage(), false);
        ajouterLigne(document, ParagraphAlignment.LEFT, "• " + donnees.phraseCiviliteDemandee(), false);
        ajouterLigne(document, ParagraphAlignment.BOTH, "Je reste à votre disposition pour tout renseignement complémentaire, et je vous prie, Madame, Monsieur, de croire en l’expression de mes salutations distinguées.", false);

        ajouterLigne(document, ParagraphAlignment.LEFT, donnees.civiliteEntete() + " " + donnees.prenomUsage() + " " + donnees.nomMajuscules(), false);
    }

    private String ponctuer(String texte) {
        String propre = nettoyer(texte);
        if (propre.isEmpty()) {
            return "";
        }
        if (propre.endsWith(".") || propre.endsWith("!") || propre.endsWith("?")) {
            return propre;
        }
        return propre + ".";
    }
}
