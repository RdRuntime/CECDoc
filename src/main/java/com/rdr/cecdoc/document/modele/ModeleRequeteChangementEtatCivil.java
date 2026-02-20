package com.rdr.cecdoc.document.modele;

import com.rdr.cecdoc.document.AlignementTexte;
import com.rdr.cecdoc.document.ModeleDocumentAbstrait;
import com.rdr.cecdoc.document.RedactriceDocument;
import com.rdr.cecdoc.model.DonneesDossier;
import com.rdr.cecdoc.util.NormalisationTexte;
import com.rdr.cecdoc.util.ParseursDate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

public final class ModeleRequeteChangementEtatCivil extends ModeleDocumentAbstrait<DonneesDossier> {
    private static final DateTimeFormatter FORMAT_DATE_JOUR = DateTimeFormatter.ofPattern("dd/MM/uuuu");
    private static final DateTimeFormatter FORMAT_DATE_NAISSANCE = DateTimeFormatter.ofPattern("d MMMM uuuu", Locale.FRENCH);
    private static final String PREFIXE_NOTIFICATION_PRENOMS = "notification de changement de prénoms";

    private static final String[] PARAGRAPHES_DROIT = {
            "L'article 56 de la loi n° 2016-1547 du 18 novembre 2016 de modernisation de la Justice du XXIème siècle - validé par le Conseil Constitutionnel dans sa décision n° 2016-739 DC du 17 novembre 2016 - vient introduire quatre nouveaux articles dans le Code Civil quant au changement d'état civil pour les personnelles transsexuelles.",
            "L'article 61-5 du Code Civil pose le principe que :",
            "« Toute personne majeure ou mineur émancipée qui démontre par une réunion suffisante de faits que la mention relative à son sexe dans les actes de l'état civil ne correspondant pas à celui dans lequel elle se présente et dans lequel elle est connue peut en obtenir la modification.\nLes principaux de ces faits, dont la preuve peut être rapportée par tous moyens, peuvent être :\n1° Qu'elle se présente publiquement comme appartenant au sexe revendiqué ;\n2° Qu'elle est connue sous le sexe revendiqué de son entourage familial, amical ou professionnel ;\n3° Qu'elle a obtenu le changement de son prénom afin qu'il corresponde au sexe revendiqué ; »",
            "L'article 61-6 dudit code ajoute :",
            "« La demande est présentée devant le tribunal de grande instance. Le demandeur fait état de son consentement libre et éclairé à la modification de la mention relative à son sexe dans les actes de l'état civil et produit tous éléments de preuve au soutien de sa demande. Le fait de ne pas avoir subi des traitements médicaux, une opération chirurgicale ou une stérilisation ne peut motiver le refus de faire droit à la demande.\nLe tribunal constate que le demandeur satisfait aux conditions fixées à l'article 61-5 et ordonne la modification de la mention relative au sexe ainsi que, le cas échéant, des prénoms, dans les actes de l'état civil. »",
            "Une fois le changement d'état civil accordé l'article 61-7 du Code Civil précise que :",
            "« Mention de la décision de modification du sexe et, le cas échéant, des prénoms est portée en marge de l'acte de naissance de l'intéressé, à la requête du procureur de la République, dans les quinze jours suivant la date à laquelle cette décision est passée en force de chose jugée.\nPar dérogation à l'article 61-4, les modifications de prénoms corrélatives à une décision de modification de sexe ne sont portées en marge des actes de l'état civil des conjoints et enfants qu'avec le consentement des intéressés ou de leurs représentants légaux. Les articles 100 et 101 sont applicables aux modifications de sexe. »",
            "Enfin, l'article 61-8 du Code civil dispose que :",
            "« La modification de la mention du sexe dans les actes de l'état civil est sans effet sur les obligations contractées à l'égard de tiers ni sur les filiations établies avant cette modification. »",
            "Ce faisant le changement de sexe à l'état civil est totalement démédicalisé et se fonde désormais uniquement sur la détermination sociale de son sexe par la personne et sa reconnaissance par son entourage comme le précise le circulaire du 10 mai 2017 de présentation des dispositions de l'article 56 de la loi n° 2016-1547 du 18 novembre 2016 de modernisation de la justice du XXIe siècle concernant les procédures judiciaires de changement de prénom et de modification de la mention du sexe à l'état civil, NOR : JUSC1709389C :",
            "« L'article 56 crée par ailleurs une procédure de modification de la mention du sexe à l'état civil, simplifiée et démédicalisée sous le contrôle du juge. »",
            "Le législateur a en outre pris la peine d'indiquer directement dans la loi que « Le fait de ne pas avoir subi des traitements médicaux, une opération chirurgicale ou une stérilisation ne peut motiver le refus de faire droit à la demande. » (article 61-6 du Code Civil).",
            "Cela a été confirmé par la cour d'appel de Montpellier dans l'arrêt du 15 mars 2017 :",
            "« La personne ne doit plus établir [...] la réalité du syndrome transsexuel [...] ainsi que le caractère irréversible de la transformation de l'apparence. La reconnaissance sociale, posée par la loi nouvelle du 18 novembre 2016 comme seule condition à la modification de la mention du sexe à l'état civil. »",
            "La France a également été condamnée par la Cour Européenne des Droits de l'Homme le 6 avril 2017 :",
            "« Le rejet de la demande [...] tendant à la modification de leur état civil au motif qu'ils n'avaient pas établi le caractère irréversible de la transformation de leur apparence, c'est-à-dire démontré avoir subi une opération stérilisante ou un traitement médical entrainant une très forte probabilité de stérilité, s'analyse en un manquement par l'Etat défendeur à son obligation positive de garantir le droit de ces derniers au respect de leur vie privée. Il y a donc, de ce chef, violation de l'article 8 de la Convention à leur égard. »",
            "Le caractère facultatif des preuves médicales a d'ailleurs été rappelé par le Défenseur des droits dans sa décision n°2018-122 du 12 avril 2018 :",
            "« Décide de prendre acte du dispositif mis en place par le tribunal de grande instance de A. modifiant la notice de pièces jointe aux dossiers de demande de modification de la mention relative au sexe à l’état civil et rendant facultatives les pièces médicales.\nDécide de recommander au ministre de Justice de veiller à ce que les demandeurs soient informés du caractère facultatif de la communication de pièces médicales à leur dossier, et que des instructions soient adressées dans ce sens. »",
            "De ce fait, les conditions au changement de la mention de sexe à l'état civil disposées par l'article 61-5 du Code Civil sont les seules à devoir être satisfaites."
    };

    @Override
    public void rediger(DonneesDossier data, RedactriceDocument redactrice) {
        Objects.requireNonNull(data, "data");
        Objects.requireNonNull(redactrice, "redactrice");

        ajouterPageTitre(redactrice, data);
        ajouterEntete(redactrice, data);
        ajouterEnTeteRequete(redactrice, data);
        ajouterSectionDemande(redactrice, data);
        ajouterSectionConsentement(redactrice, data);
        ajouterSectionFaits(redactrice, data);
        ajouterSectionCompetence(redactrice, data);
        ajouterSectionDroit(redactrice);
        ajouterSectionEnFait(redactrice, data);
        ajouterSectionConclusion(redactrice, data);
        ajouterPiecesJointes(redactrice, data);
    }

    private void ajouterPageTitre(RedactriceDocument redactrice, DonneesDossier data) {
        ajouterParagraphe(redactrice, AlignementTexte.CENTRE, titreRequete(data), true, false, 26, false);
        redactrice.ajouterSautPage();
    }

    private void ajouterEntete(RedactriceDocument redactrice, DonneesDossier data) {
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "À Mesdames et Messieurs les Présidents", true);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "et Juges de la Chambre du Conseil", true);
        for (String ligne : data.tribunal().split("\\R", -1)) {
            if (!ligne.isEmpty()) {
                ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, ligne, true);
            }
        }
        ajouterParagrapheVide(redactrice, AlignementTexte.GAUCHE);
    }

    private void ajouterEnTeteRequete(RedactriceDocument redactrice, DonneesDossier data) {
        ajouterParagraphe(redactrice, AlignementTexte.CENTRE, titreRequete(data), true);
        ajouterParagraphe(redactrice, AlignementTexte.CENTRE, "Devant la Chambre du Conseil", false);
        ajouterParagraphe(redactrice, AlignementTexte.CENTRE, "(Article 1055-5 à 1055-9 du Code de procédure civile)", false);
        ajouterParagraphe(redactrice, AlignementTexte.CENTRE, "(Art. 61-5 à 61-8 du Code civil)", false);
        ajouterParagraphe(redactrice, AlignementTexte.CENTRE, "Requête à l'intention de Madame ou Monsieur le Président du tribunal", false);
        ajouterParagrapheVide(redactrice, AlignementTexte.CENTRE);
    }

    private String titreRequete(DonneesDossier data) {
        if (data.changementPrenoms()) {
            return "Requête de changement de la mention de sexe et des prénoms à l'état civil";
        }
        return "Requête de changement de la mention de sexe à l'état civil";
    }

    private void ajouterSectionDemande(RedactriceDocument redactrice, DonneesDossier data) {
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "À LA DEMANDE DE", true);

        String blocCivil = data.civiliteEtatCivil() + " " + data.nomFamilleMajuscules() + " " + data.prenomsEtatCivil();
        String blocDemande = data.civiliteDemande() + " " + data.nomFamilleMajuscules() + " " + (data.changementPrenoms() ? data.prenomsUsage() : data.prenomsEtatCivil());
        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, blocDemande + " (" + blocCivil + " pour l'état civil)", false);

        String dateNaissanceLongue = formatterDateNaissance(data.dateNaissance());
        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, capFirst(data.adjectifNe()) + " le " + dateNaissanceLongue + " à " + data.lieuNaissance() + ",", false);

        if (aDuTexte(data.nationalite())) {
            ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "De nationalité " + data.nationalite() + ",", false);
        }

        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "Demeurant au " + NormalisationTexte.aplatirLignes(data.adresse()) + ",", false);

        if (aDuTexte(data.professionExercee())) {
            ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, data.professionExercee() + ",", false);
        }

        String ligneEtatCivil = construireLigneEtatCivil(data.situationMatrimoniale(), data.situationEnfants(), data.pacsContracte());
        if (aDuTexte(ligneEtatCivil)) {
            ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, ligneEtatCivil, false);
        }

        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "Faisant état de son consentement libre et éclairé.", false);
    }

    private String construireLigneEtatCivil(String marital, String children, boolean pacs) {
        StringBuilder sb = new StringBuilder();
        if (aDuTexte(marital)) {
            sb.append(capFirst(marital.trim()));
        }
        if (aDuTexte(children)) {
            String childrenText = children.trim();
            if (!sb.isEmpty()) {
                sb.append(", ");
                sb.append(lowerFirst(childrenText));
            } else {
                sb.append(capFirst(childrenText));
            }
        }
        if (!sb.isEmpty()) {
            sb.append(", ");
        }
        sb.append(pacs ? "ayant contracté un Pacte Civil de Solidarité," : "n'ayant pas contracté de Pacte Civil de Solidarité,");
        return sb.toString();
    }

    private void ajouterSectionConsentement(RedactriceDocument redactrice, DonneesDossier data) {
        ajouterParagraphe(redactrice, AlignementTexte.CENTRE, "CONSENTEMENT LIBRE ET ÉCLAIRÉ", true, false, TAILLE_TEXTE, true);

        String civilPart = data.prenomsEtatCivil() + " " + data.nomFamilleMajuscules();
        String chosenPart = (data.changementPrenoms() ? data.prenomsUsage() : data.prenomsEtatCivil()) + " " + data.nomFamilleMajuscules();
        String consent = "Je " + data.adjectifSousSigne() + " " + chosenPart + " (" + civilPart + " pour l'état civil), " + data.adjectifNe() + " le " + formatDate(data.dateNaissance()) + " à " + data.lieuNaissance() + " demeurant au " + NormalisationTexte.aplatirLignes(data.adresse()) + ", fais état de mon consentement libre et éclairé à la " + (data.changementPrenoms() ? "modification de mes prénoms et de la mention relative à mon sexe" : "modification de la mention relative à mon sexe") + " dans les actes de mon état civil.";
        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, consent, false);
        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "(À recopier manuscrit ci-dessous par " + data.nomDeclarant() + ", puis signer)", false, true);
        for (int i = 0; i < 4; i++) {
            ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "__________________________________________________________________________________", false);
        }
        ajouterParagraphe(redactrice, AlignementTexte.DROITE, "Signature", false);
    }

    private void ajouterSectionFaits(RedactriceDocument redactrice, DonneesDossier data) {
        ajouterParagraphe(redactrice, AlignementTexte.CENTRE, "A L'HONNEUR DE VOUS EXPOSER QUE", true);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "Les faits", true);

        String chosenPart = (data.changementPrenoms() ? data.prenomsUsage() : data.prenomsEtatCivil()) + " " + data.nomFamilleMajuscules();
        if (data.pronomNeutre()) {
            if (data.changementPrenoms()) {
                ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "« " + chosenPart + " » " + data.adjectifInscrit() + " à sa naissance sur les registres de l'état civil sous les prénoms « " + data.prenomsEtatCivil() + " », se déclare de genre non-binaire et souhaite changer sa mention de sexe à l'état civil ainsi que ses prénoms.", false);
            } else {
                ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "« " + chosenPart + " » " + data.adjectifInscrit() + " à sa naissance sur les registres de l'état civil comme étant de sexe " + data.sexeEtatCivil().toLowerCase(Locale.ROOT) + ", se déclare de genre non-binaire et souhaite changer sa mention de sexe à l'état civil.", false);
            }
        } else if (data.changementPrenoms()) {
            ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "« " + chosenPart + " » " + data.adjectifInscrit() + " à sa naissance sur les registres de l'état civil sous les prénoms « " + data.prenomsEtatCivil() + " » et comme étant de sexe " + data.sexeEtatCivil().toLowerCase(Locale.ROOT) + ", demande une modification de la mention de sexe et de prénoms à l'état civil.", false);
        } else {
            ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "« " + chosenPart + " » " + data.adjectifInscrit() + " à sa naissance sur les registres de l'état civil comme étant de sexe " + data.sexeEtatCivil().toLowerCase(Locale.ROOT) + ", demande une modification de la mention de sexe à l'état civil.", false);
        }

        String age = data.age();
        if (!age.isEmpty()) {
            String nomGenreDemande = data.nomGenreDemande();
            String enTantQue = "homme".equalsIgnoreCase(nomGenreDemande) ? "en tant qu'homme" : "en tant que " + nomGenreDemande;
            ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, capFirst(data.adjectifAge()) + " aujourd'hui de " + age + " ans, " + data.nomRequerant() + " a annoncé son identité de genre à ses proches et vit socialement " + enTantQue + ".", false);
        }

        if (aDuTexte(data.recit())) {
            ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, data.recit(), false);
        }
    }

    private void ajouterSectionCompetence(RedactriceDocument redactrice, DonneesDossier data) {
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "Sur la compétence du tribunal", true, false, TAILLE_TEXTE, true);
        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "Vu l'article 1055-5 du Code de procédure civile : « La demande en modification de la mention du sexe et, le cas échéant, des prénoms, dans les actes de l'état civil, est portée devant le tribunal dans le ressort duquel soit la personne intéressée demeure, soit son acte de naissance a été dressé ou transcrit. »", false);
        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "Le domicile " + data.deNomRequerant() + " demeure à " + NormalisationTexte.aplatirLignes(data.adresse()) + ". Le tribunal indiqué est donc compétent pour entendre la présente affaire.", false);
        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "Par ailleurs, vu l'article 1055-7 du Code de la procédure civile, la représentation " + data.deNomRequerant() + " par un avocat n'est pas obligatoire : « La demande est formée par requête remise ou adressée au greffe. Le cas échéant, la requête précise si la demande tend également à un changement de prénoms. Le ministère d'avocat n'est pas obligatoire. »", false);
    }

    private void ajouterSectionDroit(RedactriceDocument redactrice) {
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "Sur la demande de rectification de la mention de sexe à l'état civil", true);
        ajouterParagraphe(redactrice, AlignementTexte.CENTRE, "EN DROIT", true);

        for (String paragraphe : PARAGRAPHES_DROIT) {
            ajouterParagrapheDroit(redactrice, paragraphe);
        }
    }

    private void ajouterParagrapheDroit(RedactriceDocument redactrice, String texte) {
        for (String part : texte.split("\\R")) {
            ajouterParagraphe(redactrice, estEnumerationJuridique(part) ? AlignementTexte.GAUCHE : AlignementTexte.JUSTIFIE, part, false);
        }
    }

    private void ajouterSectionEnFait(RedactriceDocument redactrice, DonneesDossier data) {
        ajouterParagraphe(redactrice, AlignementTexte.CENTRE, "EN FAIT", true);

        String honor = data.civiliteDemande();
        String firsts = data.changementPrenoms() ? data.prenomsUsage() : data.prenomsEtatCivil();
        String full = honor + " " + firsts + " " + data.nomFamilleMajuscules();
        String sexeTarget = data.adjectifGenreDemande();
        String sexeCivil = data.adjectifGenreEtatCivil();

        if (data.pronomNeutre()) {
            ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "Dans les faits, il est établi que " + full + " se présente publiquement comme une personne non-binaire et " + data.pronomQuIel() + " est " + data.adjectifConnu() + " sous cette identité non-binaire par sa famille, son entourage amical, professionnel ou académique ainsi que dans toutes les interactions sociales " + data.pronomQuIel() + " entreprend quotidiennement.", false);
            ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "Par conséquent, le Tribunal judiciaire ne pourra manquer d’ordonner la modification de la mention relative au sexe sur l'acte de naissance afin qu'elle corresponde à un sexe " + data.sexeDemandeEtatCivil().toLowerCase(Locale.ROOT) + ".", false);
        } else {
            ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "Dans les faits, il est établi que " + full + " se présente publiquement comme appartenant au sexe " + sexeTarget + " et " + data.pronomQuIel() + " est " + data.adjectifConnu() + " sous cette identité " + (data.sexeDemandeFeminin() ? "féminine" : "masculine") + " par sa famille, son entourage amical, professionnel ou académique ainsi que dans toutes les interactions sociales " + data.pronomQuIel() + " entreprend quotidiennement.", false);
            ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "Par conséquent, le Tribunal judiciaire ne pourra manquer d’ordonner la suppression de la mention « sexe " + sexeCivil + " » pour la remplacer par la mention « sexe " + sexeTarget + " » sur son acte de naissance.", false);
        }
    }

    private void ajouterSectionConclusion(RedactriceDocument redactrice, DonneesDossier data) {
        ajouterParagraphe(redactrice, AlignementTexte.CENTRE, "EN CONSÉQUENCE DE QUOI", true);
        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "Sur la compétence juridictionnelle : Vu les articles 1055-5 à 1055-9 du Code de procédure civile ;", false);
        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "Sur le fond : Vu les articles 9, 61-5 et suivants du Code civil ; Vu l'article 8 de la Convention européenne des droits de l'homme ;", false);
        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "Par ces motifs, " + data.nomRequerant() + " requiert qu'il plaise au tribunal de :", false);

        if (data.pronomNeutre()) {
            ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "– Ordonner que l'acte de naissance de la personne requérante, dressé à " + data.lieuNaissance() + ", soit rectifié afin que la mention relative au sexe à l'état civil corresponde à un sexe " + data.sexeDemandeEtatCivil().toLowerCase(Locale.ROOT) + (data.changementPrenoms() ? " et que les prénoms d'origine soient remplacés par « " + data.prenomsUsage() + " »" : "") + ";", false);
        } else {
            ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "– Ordonner que l'acte de naissance " + (data.sexeDemandeFeminin() ? "de la requérante" : "du requérant") + ", dressé à " + data.lieuNaissance() + ", soit rectifié en ce sens que la mention « sexe " + data.sexeEtatCivil().toLowerCase(Locale.ROOT) + " » soit remplacée par la mention « sexe " + data.sexeDemande().toLowerCase(Locale.ROOT) + " »" + (data.changementPrenoms() ? " et que les prénoms d'origine soient remplacés par « " + data.prenomsUsage() + " »" : "") + ";", false);
        }

        if (data.changementPrenoms()) {
            ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "– Rappeler qu'en vertu de l'article 61-7 du Code civil, la mention de la décision de la modification du sexe et, le cas échéant, des prénoms est portée en marge de l'acte de naissance de l'intéressé dans les quinze jours suivant la date à laquelle cette décision est passée en force de chose jugée ;", false);
        } else {
            ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "– Rappeler qu'en vertu de l'article 61-7 du Code civil, la mention de la décision de la modification du sexe est portée en marge de l'acte de naissance de l'intéressé dans les quinze jours suivant la date à laquelle cette décision est passée en force de chose jugée ;", false);
        }

        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "– Ordonner qu'aucune expédition des actes d'état civil dans la mention desdites rectifications ne soit délivrée.", false);
        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, data.nomRequerantMajuscule() + " procédera aux démarches tendant à la reconnaissance de la décision du changement de la mention du sexe" + (data.changementPrenoms() ? " ainsi que des prénoms" : "") + " à l’état civil auprès des autorités locales compétentes, dès que celle-ci aura été prise.", false);
        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, data.nomRequerantMajuscule() + " atteste sur l’honneur qu’aucune procédure de changement de la mention du sexe" + (data.changementPrenoms() ? " et des prénoms" : "") + " à l’état civil n’est actuellement en cours devant les juridictions françaises et qu’aucune demande de la sorte n’est actuellement examinée par un juge aux affaires familiales.", false);
        ajouterParagraphe(redactrice, AlignementTexte.DROITE, "Fait à " + data.villeActuelle() + ", le " + LocalDate.now().format(FORMAT_DATE_JOUR), false);
        ajouterParagraphe(redactrice, AlignementTexte.DROITE, "Signature :", false);
    }

    private void ajouterPiecesJointes(RedactriceDocument redactrice, DonneesDossier data) {
        ajouterParagraphe(redactrice, AlignementTexte.CENTRE, "Bordereau des pièces jointes à la requête", true, false, TAILLE_TEXTE, true);
        int index = 1;
        for (String intitule : data.piecesJustificatives()) {
            if (!aDuTexte(intitule)) {
                continue;
            }
            String texte = intitule.toLowerCase(Locale.ROOT);
            if (data.changementPrenoms() && texte.startsWith(PREFIXE_NOTIFICATION_PRENOMS)) {
                continue;
            }
            ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "N°" + index + " – " + intitule, false);
            index++;
        }
    }

    private static String formatterDateNaissance(String ddmmyyyy) {
        try {
            return ParseursDate.parserDateSaisie(ddmmyyyy).format(FORMAT_DATE_NAISSANCE);
        } catch (Exception ex) {
            return ddmmyyyy;
        }
    }

    private static String formatDate(String ddmmyyyy) {
        try {
            return ParseursDate.parserDateSaisie(ddmmyyyy).format(FORMAT_DATE_NAISSANCE);
        } catch (Exception e) {
            return ddmmyyyy;
        }
    }

    private static boolean estEnumerationJuridique(String ligne) {
        if (ligne == null) {
            return false;
        }
        String texte = ligne.trim();
        return texte.matches("^[0-9]+°.*") || texte.startsWith("–") || texte.startsWith("-") || texte.startsWith("•");
    }
}
