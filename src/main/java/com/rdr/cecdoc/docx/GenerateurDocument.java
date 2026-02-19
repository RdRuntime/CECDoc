package com.rdr.cecdoc.docx;

import com.rdr.cecdoc.model.DonneesDossier;
import com.rdr.cecdoc.util.NormalisationTexte;
import com.rdr.cecdoc.util.ParseursDate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;


public class GenerateurDocument {
    private static final DateTimeFormatter TODAY_FMT = DateTimeFormatter.ofPattern("dd/MM/uuuu");
    private static final DateTimeFormatter DOB_OUT_FMT = DateTimeFormatter.ofPattern("d MMMM uuuu", Locale.FRENCH);
    private static final String NOTIF_PREFIX = "notification de changement de prénoms";


    public void generer(DonneesDossier data, File file) throws IOException {
        Objects.requireNonNull(data, "data");
        Objects.requireNonNull(file, "file");
        try (XWPFDocument document = new XWPFDocument()) {
            addCoverPage(document, data);
            addHeading(document, data);
            addRequestHeader(document, data);
            addApplicantSection(document, data);
            addConsentSection(document, data);
            addFactsSection(document, data);
            addCompetenceSection(document, data);
            addLawSection(document);
            addEnFaitSection(document, data);
            addConclusionSection(document, data);
            addAttachments(document, data);
            try (FileOutputStream out = new FileOutputStream(file)) {
                document.write(out);
            }
        }
    }

    private void addCoverPage(XWPFDocument doc, DonneesDossier data) {
        XWPFParagraph titre = doc.createParagraph();
        titre.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun run = titre.createRun();
        run.setBold(true);
        run.setFontSize(26);
        run.setText(titreRequete(data));

        XWPFParagraph sautPage = doc.createParagraph();
        sautPage.setPageBreak(true);
    }

    private void addHeading(XWPFDocument doc, DonneesDossier data) {
        XWPFParagraph p = doc.createParagraph();
        p.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun r = p.createRun();
        r.setBold(true);
        r.setText("À Mesdames et Messieurs les Présidents");
        r.addBreak();
        r.setText("et Juges de la Chambre du Conseil");
        r.addBreak();
        String[] lines = data.tribunal().split("\\R", -1);
        for (String line : lines) {
            if (!line.isEmpty()) {
                r.setText(line);
                r.addBreak();
            }
        }
        r.addBreak();
    }

    private void addRequestHeader(XWPFDocument doc, DonneesDossier data) {
        XWPFParagraph p = doc.createParagraph();
        p.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun r = p.createRun();
        r.setBold(true);
        r.setText(titreRequete(data));
        r.addBreak();
        XWPFRun r2 = createRun(doc, ParagraphAlignment.CENTER);
        r2.setText("Devant la Chambre du Conseil");
        r2.addBreak();
        r2.setText("(Article 1055-5 à 1055-9 du Code de procédure civile)");
        r2.addBreak();
        r2.setText("(Art. 61-5 à 61-8 du Code civil)");
        r2.addBreak();
        r2.setText("Requête à l'intention de Madame ou Monsieur le Président du tribunal");
        r2.addBreak();
        r2.addBreak();
    }

    private String titreRequete(DonneesDossier data) {
        if (data.changementPrenoms()) {
            return "Requête de changement de la mention de sexe et des prénoms à l'état civil";
        }
        return "Requête de changement de la mention de sexe à l'état civil";
    }

    private void addApplicantSection(XWPFDocument doc, DonneesDossier data) {
        XWPFParagraph title = doc.createParagraph();
        title.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun tRun = title.createRun();
        tRun.setBold(true);
        tRun.setText("À LA DEMANDE DE");
        tRun.addBreak();

        String civilBlock = data.civiliteEtatCivil() + " " + data.nomFamilleMajuscules() + " " + data.prenomsEtatCivil();
        String targetBlock = data.civiliteDemande() + " " + data.nomFamilleMajuscules() + " " + (data.changementPrenoms() ? data.prenomsUsage() : data.prenomsEtatCivil());

        XWPFRun r = createRun(doc, ParagraphAlignment.BOTH);
        r.setText(targetBlock + " (" + civilBlock + " pour l'état civil)");
        r.addBreak();

        String dobLong;
        try {
            dobLong = ParseursDate.parserDateSaisie(data.dateNaissance()).format(DOB_OUT_FMT);
        } catch (Exception ex) {
            dobLong = data.dateNaissance();
        }

        XWPFRun r2 = createRun(doc, ParagraphAlignment.BOTH);
        r2.setText(capFirst(data.adjectifNe()) + " le " + dobLong + " à " + data.lieuNaissance() + ",");
        r2.addBreak();

        if (hasText(data.nationalite())) {
            XWPFRun rNat = createRun(doc, ParagraphAlignment.BOTH);
            rNat.setText("De nationalité " + data.nationalite() + ",");
            rNat.addBreak();
        }

        XWPFRun rAddr = createRun(doc, ParagraphAlignment.BOTH);
        rAddr.setText("Demeurant au " + NormalisationTexte.aplatirLignes(data.adresse()) + ",");
        rAddr.addBreak();

        if (hasText(data.professionExercee())) {
            XWPFRun rProf = createRun(doc, ParagraphAlignment.BOTH);
            rProf.setText(data.professionExercee() + ",");
            rProf.addBreak();
        }

        String civilLine = buildCivilLine(data.situationMatrimoniale(), data.situationEnfants(), data.pacsContracte());
        if (hasText(civilLine)) {
            XWPFRun rCivil = createRun(doc, ParagraphAlignment.BOTH);
            rCivil.setText(civilLine);
            rCivil.addBreak();
        }

        XWPFRun r4 = createRun(doc, ParagraphAlignment.BOTH);
        r4.setText("Faisant état de son consentement libre et éclairé.");
        r4.addBreak();
        r4.addBreak();
    }

    private String buildCivilLine(String marital, String children, boolean pacs) {
        StringBuilder sb = new StringBuilder();
        if (hasText(marital)) sb.append(capFirst(marital.trim()));
        if (hasText(children)) {
            String childrenText = children.trim();
            if (!sb.isEmpty()) {
                sb.append(", ");
                sb.append(lowerFirst(childrenText));
            } else {
                sb.append(capFirst(childrenText));
            }
        }
        if (!sb.isEmpty()) sb.append(", ");
        sb.append(pacs ? "ayant contracté un Pacte Civil de Solidarité," : "n'ayant pas contracté de Pacte Civil de Solidarité,");
        return sb.toString();
    }

    private void addConsentSection(XWPFDocument doc, DonneesDossier data) {
        XWPFParagraph title = doc.createParagraph();
        title.setPageBreak(true);
        title.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun tRun = title.createRun();
        tRun.setBold(true);
        tRun.setText("CONSENTEMENT LIBRE ET ÉCLAIRÉ");
        tRun.addBreak();

        String civilPart = data.prenomsEtatCivil() + " " + data.nomFamilleMajuscules();
        String chosenPart = (data.changementPrenoms() ? data.prenomsUsage() : data.prenomsEtatCivil()) + " " + data.nomFamilleMajuscules();

        XWPFRun r = createRun(doc, ParagraphAlignment.BOTH);
        String consent = "Je " + data.adjectifSousSigne() + " " + chosenPart + " (" + civilPart + " pour l'état civil), " + data.adjectifNe() + " le " + formatDate(data.dateNaissance()) + " à " + data.lieuNaissance() + " demeurant au " + NormalisationTexte.aplatirLignes(data.adresse()) + ", fais état de mon consentement libre et éclairé à la " + (data.changementPrenoms() ? "modification de mes prénoms et de la mention relative à mon sexe" : "modification de la mention relative à mon sexe") + " dans les actes de mon état civil.";
        r.setText(consent);
        r.addBreak();
        r.setText("(À recopier manuscrit ci-dessous par " + data.nomDeclarant() + ", puis signer)");
        r.addBreak();
        r.addBreak();
        for (int i = 0; i < 4; i++) {
            XWPFRun line = createRun(doc, ParagraphAlignment.LEFT);
            line.setText("__________________________________________________________________________________");
        }
        XWPFRun sig = createRun(doc, ParagraphAlignment.RIGHT);
        sig.setText("Signature");
        sig.addBreak();
        sig.addBreak();
    }

    private void addFactsSection(XWPFDocument doc, DonneesDossier data) {
        XWPFParagraph title = doc.createParagraph();
        title.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun tRun = title.createRun();
        tRun.setBold(true);
        tRun.setText("A L'HONNEUR DE VOUS EXPOSER QUE");
        tRun.addBreak();

        XWPFRun factsTitle = createRun(doc, ParagraphAlignment.LEFT);
        factsTitle.setBold(true);
        factsTitle.setText("Les faits");
        factsTitle.addBreak();

        XWPFRun r = createRun(doc, ParagraphAlignment.BOTH);
        String chosenPart = (data.changementPrenoms() ? data.prenomsUsage() : data.prenomsEtatCivil()) + " " + data.nomFamilleMajuscules();
        if (data.pronomNeutre()) {
            if (data.changementPrenoms()) {
                r.setText("« " + chosenPart + " » " + data.adjectifInscrit() + " à sa naissance sur les registres de l'état civil sous les prénoms « " + data.prenomsEtatCivil() + " », se déclare de genre non-binaire et souhaite changer sa mention de sexe à l'état civil ainsi que ses prénoms.");
            } else {
                r.setText("« " + chosenPart + " » " + data.adjectifInscrit() + " à sa naissance sur les registres de l'état civil comme étant de sexe " + data.sexeEtatCivil().toLowerCase(Locale.ROOT) + ", se déclare de genre non-binaire et souhaite changer sa mention de sexe à l'état civil.");
            }
        } else {
            if (data.changementPrenoms()) {
                r.setText("« " + chosenPart + " » " + data.adjectifInscrit() + " à sa naissance sur les registres de l'état civil sous les prénoms « " + data.prenomsEtatCivil() + " » et comme étant de sexe " + data.sexeEtatCivil().toLowerCase(Locale.ROOT) + ", demande une modification de la mention de sexe et de prénoms à l'état civil.");
            } else {
                r.setText("« " + chosenPart + " » " + data.adjectifInscrit() + " à sa naissance sur les registres de l'état civil comme étant de sexe " + data.sexeEtatCivil().toLowerCase(Locale.ROOT) + ", demande une modification de la mention de sexe à l'état civil.");
            }
        }
        r.addBreak();

        String age = data.age();
        if (!age.isEmpty()) {
            String nomGenreDemande = data.nomGenreDemande();
            String enTantQue = "homme".equalsIgnoreCase(nomGenreDemande) ? "en tant qu'homme" : "en tant que " + nomGenreDemande;
            XWPFRun rAge = createRun(doc, ParagraphAlignment.BOTH);
            rAge.setText(capFirst(data.adjectifAge()) + " aujourd'hui de " + age + " ans, " + data.nomRequerant() + " a annoncé son identité de genre à ses proches et vit socialement " + enTantQue + ".");
            rAge.addBreak();
        }

        if (hasText(data.recit())) {
            XWPFRun rN = createRun(doc, ParagraphAlignment.BOTH);
            rN.setText(data.recit());
            rN.addBreak();
        }
        XWPFRun br = createRun(doc, ParagraphAlignment.BOTH);
        br.addBreak();
    }

    private void addCompetenceSection(XWPFDocument doc, DonneesDossier data) {
        XWPFParagraph title = doc.createParagraph();
        title.setPageBreak(true);
        title.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun tRun = title.createRun();
        tRun.setBold(true);
        tRun.setText("Sur la compétence du tribunal");
        tRun.addBreak();

        XWPFRun r = createRun(doc, ParagraphAlignment.BOTH);
        r.setText("Vu l'article 1055-5 du Code de procédure civile : « La demande en modification de la mention du sexe et, le cas échéant, des prénoms, dans les actes de l'état civil, est portée devant le tribunal dans le ressort duquel soit la personne intéressée demeure, soit son acte de naissance a été dressé ou transcrit. »");
        r.addBreak();
        XWPFRun r2 = createRun(doc, ParagraphAlignment.BOTH);
        r2.setText("Le domicile " + data.deNomRequerant() + " demeure à " + NormalisationTexte.aplatirLignes(data.adresse()) + ". Le tribunal indiqué est donc compétent pour entendre la présente affaire.");
        r2.addBreak();
        XWPFRun r3 = createRun(doc, ParagraphAlignment.BOTH);
        r3.setText("Par ailleurs, vu l'article 1055-7 du Code de la procédure civile, la représentation " + data.deNomRequerant() + " par un avocat n'est pas obligatoire : « La demande est formée par requête remise ou adressée au greffe. Le cas échéant, la requête précise si la demande tend également à un changement de prénoms. Le ministère d'avocat n'est pas obligatoire. »");
        r3.addBreak();
        r3.addBreak();
    }

    private void addLawSection(XWPFDocument doc) {
        XWPFParagraph title = doc.createParagraph();
        title.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun tRun = title.createRun();
        tRun.setBold(true);
        tRun.setText("Sur la demande de rectification de la mention de sexe à l'état civil");
        tRun.addBreak();

        XWPFRun p = createRun(doc, ParagraphAlignment.LEFT);
        p.setText("A) En droit :");
        p.addBreak();

        addLawParagraph(doc, "L'article 56 de la loi n° 2016-1547 du 18 novembre 2016 de modernisation de la Justice du XXIème siècle - validé par le Conseil Constitutionnel dans sa décision n° 2016-739 DC du 17 novembre 2016 - vient introduire quatre nouveaux articles dans le Code Civil quant au changement d'état civil pour les personnelles transsexuelles.");
        addLawParagraph(doc, "L'article 61-5 du Code Civil pose le principe que :");
        addLawParagraph(doc, "« Toute personne majeure ou mineur émancipée qui démontre par une réunion suffisante de faits que la mention relative à son sexe dans les actes de l'état civil ne correspondant pas à celui dans lequel elle se présente et dans lequel elle est connue peut en obtenir la modification.\nLes principaux de ces faits, dont la preuve peut être rapportée par tous moyens, peuvent être :\n1° Qu'elle se présente publiquement comme appartenant au sexe revendiqué ;\n2° Qu'elle est connue sous le sexe revendiqué de son entourage familial, amical ou professionnel ;\n3° Qu'elle a obtenu le changement de son prénom afin qu'il corresponde au sexe revendiqué ; »");
        addLawParagraph(doc, "L'article 61-6 dudit code ajoute :");
        addLawParagraph(doc, "« La demande est présentée devant le tribunal de grande instance. Le demandeur fait état de son consentement libre et éclairé à la modification de la mention relative à son sexe dans les actes de l'état civil et produit tous éléments de preuve au soutien de sa demande. Le fait de ne pas avoir subi des traitements médicaux, une opération chirurgicale ou une stérilisation ne peut motiver le refus de faire droit à la demande.\nLe tribunal constate que le demandeur satisfait aux conditions fixées à l'article 61-5 et ordonne la modification de la mention relative au sexe ainsi que, le cas échéant, des prénoms, dans les actes de l'état civil. »");
        addLawParagraph(doc, "Une fois le changement d'état civil accordé l'article 61-7 du Code Civil précise que :");
        addLawParagraph(doc, "« Mention de la décision de modification du sexe et, le cas échéant, des prénoms est portée en marge de l'acte de naissance de l'intéressé, à la requête du procureur de la République, dans les quinze jours suivant la date à laquelle cette décision est passée en force de chose jugée.\nPar dérogation à l'article 61-4, les modifications de prénoms corrélatives à une décision de modification de sexe ne sont portées en marge des actes de l'état civil des conjoints et enfants qu'avec le consentement des intéressés ou de leurs représentants légaux. Les articles 100 et 101 sont applicables aux modifications de sexe. »");
        addLawParagraph(doc, "Enfin, l'article 61-8 du Code civil dispose que :");
        addLawParagraph(doc, "« La modification de la mention du sexe dans les actes de l'état civil est sans effet sur les obligations contractées à l'égard de tiers ni sur les filiations établies avant cette modification. »");
        addLawParagraph(doc, "Ce faisant le changement de sexe à l'état civil est totalement démédicalisé et se fonde désormais uniquement sur la détermination sociale de son sexe par la personne et sa reconnaissance par son entourage comme le précise le circulaire du 10 mai 2017 de présentation des dispositions de l'article 56 de la loi n° 2016-1547 du 18 novembre 2016 de modernisation de la justice du XXIe siècle concernant les procédures judiciaires de changement de prénom et de modification de la mention du sexe à l'état civil, NOR : JUSC1709389C :");
        addLawParagraph(doc, "« L'article 56 crée par ailleurs une procédure de modification de la mention du sexe à l'état civil, simplifiée et démédicalisée sous le contrôle du juge. »");
        addLawParagraph(doc, "Le législateur a en outre pris la peine d'indiquer directement dans la loi que « Le fait de ne pas avoir subi des traitements médicaux, une opération chirurgicale ou une stérilisation ne peut motiver le refus de faire droit à la demande. » (article 61-6 du Code Civil).");
        addLawParagraph(doc, "Cela a été confirmé par la cour d'appel de Montpellier dans l'arrêt du 15 mars 2017 :");
        addLawParagraph(doc, "« La personne ne doit plus établir [...] la réalité du syndrome transsexuel [...] ainsi que le caractère irréversible de la transformation de l'apparence. La reconnaissance sociale, posée par la loi nouvelle du 18 novembre 2016 comme seule condition à la modification de la mention du sexe à l'état civil. »");
        addLawParagraph(doc, "La France a également été condamnée par la Cour Européenne des Droits de l'Homme le 6 avril 2017 :");
        addLawParagraph(doc, "« Le rejet de la demande [...] tendant à la modification de leur état civil au motif qu'ils n'avaient pas établi le caractère irréversible de la transformation de leur apparence, c'est-à-dire démontré avoir subi une opération stérilisante ou un traitement médical entrainant une très forte probabilité de stérilité, s'analyse en un manquement par l'Etat défendeur à son obligation positive de garantir le droit de ces derniers au respect de leur vie privée. Il y a donc, de ce chef, violation de l'article 8 de la Convention à leur égard. »");
        addLawParagraph(doc, "Le caractère facultatif des preuves médicales a d'ailleurs été rappelé par le Défenseur des droits dans sa décision n°2018-122 du 12 avril 2018 :");
        addLawParagraph(doc, "« Décide de prendre acte du dispositif mis en place par le tribunal de grande instance de A. modifiant la notice de pièces jointe aux dossiers de demande de modification de la mention relative au sexe à l’état civil et rendant facultatives les pièces médicales.\nDécide de recommander au ministre de Justice de veiller à ce que les demandeurs soient informés du caractère facultatif de la communication de pièces médicales à leur dossier, et que des instructions soient adressées dans ce sens. »");
        addLawParagraph(doc, "De ce fait, les conditions au changement de la mention de sexe à l'état civil disposées par l'article 61-5 du Code Civil sont les seules à devoir être satisfaites.");
        XWPFRun br = createRun(doc, ParagraphAlignment.BOTH);
        br.addBreak();
    }

    private void addEnFaitSection(XWPFDocument doc, DonneesDossier data) {
        XWPFParagraph title = doc.createParagraph();
        title.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun tRun = title.createRun();
        tRun.setBold(true);
        tRun.setText("EN FAIT");
        tRun.addBreak();

        String honor = data.civiliteDemande();
        String firsts = data.changementPrenoms() ? data.prenomsUsage() : data.prenomsEtatCivil();
        String full = honor + " " + firsts + " " + data.nomFamilleMajuscules();
        String sexeTarget = data.adjectifGenreDemande();
        String sexeCivil = data.adjectifGenreEtatCivil();

        XWPFRun r1 = createRun(doc, ParagraphAlignment.BOTH);
        if (data.pronomNeutre()) {
            r1.setText("Dans les faits, il est établi que " + full + " se présente publiquement comme une personne non-binaire et " + data.pronomQuIel() + " est " + data.adjectifConnu() + " sous cette identité non-binaire par sa famille, son entourage amical, professionnel ou académique ainsi que dans toutes les interactions sociales " + data.pronomQuIel() + " entreprend quotidiennement.");
        } else {
            r1.setText("Dans les faits, il est établi que " + full + " se présente publiquement comme appartenant au sexe " + sexeTarget + " et " + data.pronomQuIel() + " est " + data.adjectifConnu() + " sous cette identité " + (data.sexeDemandeFeminin() ? "féminine" : "masculine") + " par sa famille, son entourage amical, professionnel ou académique ainsi que dans toutes les interactions sociales " + data.pronomQuIel() + " entreprend quotidiennement.");
        }
        r1.addBreak();

        XWPFRun r2 = createRun(doc, ParagraphAlignment.BOTH);
        if (data.pronomNeutre()) {
            r2.setText("Par conséquent, le Tribunal judiciaire ne pourra manquer d’ordonner la modification de la mention relative au sexe sur l'acte de naissance afin qu'elle corresponde à un sexe " + data.sexeDemandeEtatCivil().toLowerCase(Locale.ROOT) + ".");
        } else {
            r2.setText("Par conséquent, le Tribunal judiciaire ne pourra manquer d’ordonner la suppression de la mention « sexe " + sexeCivil + " » pour la remplacer par la mention « sexe " + sexeTarget + " » sur son acte de naissance.");
        }
        r2.addBreak();
        r2.addBreak();
    }

    private void addConclusionSection(XWPFDocument doc, DonneesDossier data) {
        XWPFParagraph title = doc.createParagraph();
        title.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun tRun = title.createRun();
        tRun.setBold(true);
        tRun.setText("EN CONSÉQUENCE DE QUOI");
        tRun.addBreak();

        XWPFRun r = createRun(doc, ParagraphAlignment.BOTH);
        r.setText("Sur la compétence juridictionnelle : Vu les articles 1055-5 à 1055-9 du Code de procédure civile ;");
        r.addBreak();
        XWPFRun r0 = createRun(doc, ParagraphAlignment.BOTH);
        r0.setText("Sur le fond : Vu les articles 9, 61-5 et suivants du Code civil ; Vu l'article 8 de la Convention européenne des droits de l'homme ;");
        r0.addBreak();
        r0.addBreak();

        XWPFRun r1 = createRun(doc, ParagraphAlignment.BOTH);
        r1.setText("Par ces motifs, " + data.nomRequerant() + " requiert qu'il plaise au tribunal de :");
        r1.addBreak();

        XWPFRun bullet1 = createRun(doc, ParagraphAlignment.LEFT);
        if (data.pronomNeutre()) {
            bullet1.setText("– Ordonner que l'acte de naissance de la personne requérante, dressé à " + data.lieuNaissance() + ", soit rectifié afin que la mention relative au sexe à l'état civil corresponde à un sexe " + data.sexeDemandeEtatCivil().toLowerCase(Locale.ROOT) + (data.changementPrenoms() ? " et que les prénoms d'origine soient remplacés par « " + data.prenomsUsage() + " »" : "") + ";");
        } else {
            bullet1.setText("– Ordonner que l'acte de naissance " + (data.sexeDemandeFeminin() ? "de la requérante" : "du requérant") + ", dressé à " + data.lieuNaissance() + ", soit rectifié en ce sens que la mention « sexe " + data.sexeEtatCivil().toLowerCase(Locale.ROOT) + " » soit remplacée par la mention « sexe " + data.sexeDemande().toLowerCase(Locale.ROOT) + " »" + (data.changementPrenoms() ? " et que les prénoms d'origine soient remplacés par « " + data.prenomsUsage() + " »" : "") + ";");
        }

        XWPFRun bullet2 = createRun(doc, ParagraphAlignment.LEFT);
        if (data.changementPrenoms()) {
            bullet2.setText("– Rappeler qu'en vertu de l'article 61-7 du Code civil, la mention de la décision de la modification du sexe et, le cas échéant, des prénoms est portée en marge de l'acte de naissance de l'intéressé dans les quinze jours suivant la date à laquelle cette décision est passée en force de chose jugée ;");
        } else {
            bullet2.setText("– Rappeler qu'en vertu de l'article 61-7 du Code civil, la mention de la décision de la modification du sexe est portée en marge de l'acte de naissance de l'intéressé dans les quinze jours suivant la date à laquelle cette décision est passée en force de chose jugée ;");
        }

        XWPFRun bullet3 = createRun(doc, ParagraphAlignment.LEFT);
        bullet3.setText("– Ordonner qu'aucune expédition des actes d'état civil dans la mention desdites rectifications ne soit délivrée.");

        XWPFRun attestation1 = createRun(doc, ParagraphAlignment.BOTH);
        attestation1.addBreak();
        attestation1.setText(data.nomRequerantMajuscule() + " procédera aux démarches tendant à la reconnaissance de la décision du changement de la mention du sexe" + (data.changementPrenoms() ? " ainsi que des prénoms" : "") + " à l’état civil auprès des autorités locales compétentes, dès que celle-ci aura été prise.");
        attestation1.addBreak();

        XWPFRun attestation2 = createRun(doc, ParagraphAlignment.BOTH);
        attestation2.setText(data.nomRequerantMajuscule() + " atteste sur l’honneur qu’aucune procédure de changement de la mention du sexe" + (data.changementPrenoms() ? " et des prénoms" : "") + " à l’état civil n’est actuellement en cours devant les juridictions françaises et qu’aucune demande de la sorte n’est actuellement examinée par un juge aux affaires familiales.");
        attestation2.addBreak();
        attestation2.addBreak();

        XWPFRun dateLine = createRun(doc, ParagraphAlignment.RIGHT);
        dateLine.setText("Fait à " + data.villeActuelle() + ", le " + LocalDate.now().format(TODAY_FMT));
        dateLine.addBreak();
        dateLine.addBreak();

        XWPFRun signatureLine = createRun(doc, ParagraphAlignment.RIGHT);
        signatureLine.setText("Signature :");
        signatureLine.addBreak();
        signatureLine.addBreak();
    }

    private void addAttachments(XWPFDocument doc, DonneesDossier data) {
        XWPFParagraph title = doc.createParagraph();
        title.setPageBreak(true);
        title.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun tRun = title.createRun();
        tRun.setBold(true);
        tRun.setText("Bordereau des pièces jointes à la requête");
        tRun.addBreak();
        int i = 1;
        for (String item : data.piecesJustificatives()) {
            if (!hasText(item)) continue;
            String low = item.toLowerCase(Locale.ROOT);
            if (data.changementPrenoms() && low.startsWith(NOTIF_PREFIX)) continue;
            XWPFParagraph p = doc.createParagraph();
            p.setAlignment(ParagraphAlignment.LEFT);
            XWPFRun r = p.createRun();
            r.setText("N°" + (i++) + " – " + item);
        }
    }

    private XWPFRun createRun(XWPFDocument doc, ParagraphAlignment alignment) {
        XWPFParagraph paragraph = doc.createParagraph();
        if (alignment != ParagraphAlignment.BOTH) {
            paragraph.setAlignment(alignment);
        }
        return paragraph.createRun();
    }

    private static String capFirst(String s) {
        if (s == null || s.isEmpty()) return s;
        if (s.length() == 1) return s.toUpperCase(Locale.ROOT);
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    private static String lowerFirst(String s) {
        if (s == null || s.isEmpty()) return s;
        if (s.length() == 1) return s.toLowerCase(Locale.ROOT);
        return Character.toLowerCase(s.charAt(0)) + s.substring(1);
    }

    private static String formatDate(String ddmmyyyy) {
        try {
            return ParseursDate.parserDateSaisie(ddmmyyyy).format(DOB_OUT_FMT);
        } catch (Exception e) {
            return ddmmyyyy;
        }
    }

    private static boolean hasText(String s) {
        return s != null && !s.trim().isEmpty();
    }

    private void addLawParagraph(XWPFDocument doc, String text) {
        for (String part : text.split("\\R")) {
            XWPFRun r = createRun(doc, ParagraphAlignment.BOTH);
            r.setText(part);
            r.addBreak();
        }
    }
}
