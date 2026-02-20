package com.rdr.cecdoc.document.modele;

import com.rdr.cecdoc.document.AlignementTexte;
import com.rdr.cecdoc.document.ModeleDocumentAbstrait;
import com.rdr.cecdoc.document.RedactriceDocument;
import com.rdr.cecdoc.model.DonneesLettreUniversite;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

public final class ModeleLettreUniversite extends ModeleDocumentAbstrait<DonneesLettreUniversite> {
    private static final DateTimeFormatter FORMAT_DATE = DateTimeFormatter.ofPattern("dd/MM/uuuu", Locale.ROOT);

    @Override
    public void rediger(DonneesLettreUniversite donnees, RedactriceDocument redactrice) {
        Objects.requireNonNull(donnees, "donnees");
        Objects.requireNonNull(redactrice, "redactrice");

        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, donnees.identiteEntete(), false);
        ajouterBlocMultiligne(redactrice, AlignementTexte.GAUCHE, donnees.adressePostale(), false);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "Tél : " + donnees.telephonePortable(), false);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "Courriel : " + donnees.courriel(), false);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "INE : " + donnees.ine(), false);
        ajouterParagrapheVide(redactrice, AlignementTexte.GAUCHE);

        ajouterBlocMultiligne(redactrice, AlignementTexte.DROITE, donnees.nomAdresseUniversite(), false);
        ajouterParagrapheVide(redactrice, AlignementTexte.DROITE);
        ajouterParagraphe(redactrice, AlignementTexte.DROITE, "Fait à " + donnees.villeActuelle() + ", le " + LocalDate.now().format(FORMAT_DATE), false);
        ajouterParagraphe(redactrice, AlignementTexte.DROITE, "Sous toutes réserves", false);
        ajouterParagraphe(redactrice, AlignementTexte.CENTRE, "Objet : Changement de mon prénom d'usage à l'université dans le cadre d'une transition de genre", true);
        ajouterParagrapheVide(redactrice, AlignementTexte.GAUCHE);

        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "Madame, Monsieur,", false);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "Je vous écris pour vous faire part de ma situation,", false);

        String premierParagraphe = "Je suis une personne transgenre (cf. personne dont le sexe assigné ne correspond pas au genre), " + ponctuer(donnees.explicationParcoursMilieuPhrase());
        if (donnees.genreNonBinaire()) {
            ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, premierParagraphe, false);
        } else {
            ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, premierParagraphe + " Mon marqueur de sexe à l’état civil est toujours " + donnees.marqueurEtatCivilOppose() + ".", false);
        }

        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "Mon prénom à l’état-civil est toujours " + donnees.prenomEtatCivil() + ", néanmoins, celui-ci n’est pas mon prénom d’usage, qui est " + donnees.prenomUsage() + ".", false);
        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "La lettre de Madame la Ministre de l’enseignement supérieur en date du 17 avril 2019, ayant pour objet « Recommandations pour favoriser l’inclusion des personnes transgenres dans la vie étudiante et dans les établissements d’enseignement supérieur et de recherche » indique :", false);
        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "« Dans cette optique, et en lien avec la politique gouvernement de prévention des violences sexistes et sexuelles et de lutte contre la haine et les discriminations anti-LGBT, le ministère invite l’ensemble des établissements d’enseignement supérieur et de recherche à faciliter l’utilisation du prénom d’usage sur les documents et pièces internes à l’établissement pour les personnes transgenres, tout au long de leur scolarité ou de leur carrière professionnelle. » (Page 2)", false);
        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "Par ailleurs, le Défenseur des Droits a rappelé dans une décision de 2015 (MLD-2014-058) que la mention de civilité (c’est à dire « monsieur » ou « madame ») n’est en aucun cas un élément de l’État-Civil. L’acceptation de la modification de civilité s’impose à toute administration scolaire.", false);
        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "Je demande donc, conformément à ces documents, que sur l’ensemble sur l’ensemble des documents, logiciels informatiques, listes d’appels, cartes scolaires et écrans de connexion :", false);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "• Soit indiqué mon prénom d’usage : " + donnees.prenomUsage(), false);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "• " + donnees.phraseCiviliteDemandee(), false);
        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "Je reste à votre disposition pour tout renseignement complémentaire, et je vous prie, Madame, Monsieur, de croire en l’expression de mes salutations distinguées.", false);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, donnees.civiliteEntete() + " " + donnees.prenomUsage() + " " + donnees.nomMajuscules(), false);
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
