package com.rdr.cecdoc.ui;

import com.rdr.cecdoc.model.DonneesEnfantActe;
import com.rdr.cecdoc.model.DonneesLettreMiseAJourActesLies;
import com.rdr.cecdoc.model.InstantaneEnfantActe;
import com.rdr.cecdoc.model.InstantaneLettreMiseAJourActesLies;
import com.rdr.cecdoc.service.export.EcritureDocxAtomique;
import com.rdr.cecdoc.service.export.ErreurExportDocument;
import com.rdr.cecdoc.service.export.ServiceGenerationLettreMiseAJourActesLies;
import com.rdr.cecdoc.ui.theme.StyliseurBoutonTheme;
import com.rdr.cecdoc.ui.theme.TokensTheme;
import com.rdr.cecdoc.util.ParseursDate;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

final class DialogueLettreMiseAJourActesLies extends DialogueFormulaireDocumentAbstrait {
    private static final int LIMITE_MAX_ENFANTS = 10;
    private static final Pattern MOTIF_ANNEE = Pattern.compile("^\\d{4}$");

    private final transient ServiceGenerationLettreMiseAJourActesLies serviceGenerationLettreMiseAJourActesLies;

    private final ChampTexteExempleDialogue champPrenom;
    private final ChampTexteExempleDialogue champNom;
    private final ZoneTexteExempleDialogue zoneAdressePostale;
    private final JScrollPane ascenseurAdressePostale;
    private final ChampTexteExempleDialogue champTelephonePortable;
    private final ChampTexteExempleDialogue champCourriel;

    private final ChampTexteExempleDialogue champVilleAutoriteDestinataire;
    private final ZoneTexteExempleDialogue zoneAdresseDestinataire;
    private final JScrollPane ascenseurAdresseDestinataire;
    private final ChampTexteExempleDialogue champVilleRedaction;

    private final JComboBox<String> comboGenreAccords;
    private final SelecteurDateDialogue selecteurDateNaissance;
    private final ChampTexteExempleDialogue champLieuNaissance;
    private final JComboBox<String> comboAutoriteDecision;
    private final SelecteurDateDialogue selecteurDateDecision;
    private final SelecteurDateDialogue selecteurDateDecisionDefinitive;
    private final JCheckBox caseChangementPrenoms;
    private final JCheckBox caseChangementSexe;

    private final JCheckBox caseActeNaissanceRequerant;
    private final ChampTexteExempleDialogue champCommuneNaissanceRequerant;

    private final JCheckBox caseConcernePartenaire;
    private final JComboBox<String> comboLienPartenaire;
    private final JComboBox<String> comboGenrePartenaire;
    private final JCheckBox caseActeMariage;
    private final ChampTexteExempleDialogue champCommuneMariage;
    private final SelecteurDateDialogue selecteurDateMariage;
    private final JCheckBox caseActeNaissancePartenaire;
    private final ChampTexteExempleDialogue champPrenomPartenaire;
    private final ChampTexteExempleDialogue champNomPartenaire;
    private final ChampTexteExempleDialogue champCommuneNaissancePartenaire;
    private final ChampTexteExempleDialogue champAnneeNaissancePartenaire;
    private final JCheckBox caseMentionPacs;
    private final ChampTexteExempleDialogue champAutoritePacs;

    private final JComboBox<String> comboOptionLivret;
    private final JPanel panneauEnfants;
    private final JScrollPane ascenseurEnfants;
    private final JButton boutonAjouterEnfant;
    private final JLabel labelAlerteEnfants;
    private final transient List<LigneEnfant> lignesEnfants;

    DialogueLettreMiseAJourActesLies(JFrame proprietaire, TokensTheme theme, List<Image> iconesApplication, InstantaneLettreMiseAJourActesLies instantaneInitial, Path dossierSortieParDefaut) {
        super(proprietaire, theme, iconesApplication, dossierSortieParDefaut, "Mise à jour des actes liés", "Demande de mise à jour des actes d’état civil liés", "Générer la lettre");
        this.serviceGenerationLettreMiseAJourActesLies = new ServiceGenerationLettreMiseAJourActesLies(new EcritureDocxAtomique());
        this.lignesEnfants = new ArrayList<>();

        champPrenom = creerChampTexte(18, "Ex: Tom, Noé, Max");
        champNom = creerChampTexte(24, "Ex: Dupont");
        zoneAdressePostale = creerZoneTexte(3, 34, "Ex: 12 rue de la République\n75000 Paris");
        ascenseurAdressePostale = creerAscenseurZoneTexte(zoneAdressePostale, 92);
        champTelephonePortable = creerChampTexte(20, "Ex: 0613121312");
        champCourriel = creerChampTexte(28, "Ex: prenom.nom@exemple.fr");

        champVilleAutoriteDestinataire = creerChampTexte(24, "Ex: Paris");
        zoneAdresseDestinataire = creerZoneTexte(3, 34, "Ex: Mairie de Paris Centre\n2 rue Eugène Spuller\n75003 Paris");
        ascenseurAdresseDestinataire = creerAscenseurZoneTexte(zoneAdresseDestinataire, 92);
        champVilleRedaction = creerChampTexte(24, "Ex: Paris");

        comboGenreAccords = creerCombo("Masculin", "Féminin", "Non-binaire");
        selecteurDateNaissance = creerSelecteurDate();
        champLieuNaissance = creerChampTexte(24, "Ex: Paris");
        comboAutoriteDecision = creerCombo("Tribunal judiciaire", "Officier·e de l’état civil");
        selecteurDateDecision = creerSelecteurDate();
        selecteurDateDecisionDefinitive = creerSelecteurDate();
        caseChangementPrenoms = creerCase("La décision concerne un changement de prénoms");
        caseChangementSexe = creerCase("La décision concerne la modification de la mention du sexe");

        caseActeNaissanceRequerant = creerCase("Inclure mon acte de naissance");
        champCommuneNaissanceRequerant = creerChampTexte(20, "Ex: Paris");

        caseConcernePartenaire = creerCase("Inclure les actes d’un·e partenaire");
        comboLienPartenaire = creerCombo("Époux·se", "Partenaire de PACS");
        comboGenrePartenaire = creerCombo("Masculin", "Féminin", "Non-binaire");
        caseActeMariage = creerCase("Inclure l’acte de mariage");
        champCommuneMariage = creerChampTexte(20, "Ex: Paris");
        selecteurDateMariage = creerSelecteurDate();
        caseActeNaissancePartenaire = creerCase("Inclure l’acte de naissance du/de la partenaire");
        champPrenomPartenaire = creerChampTexte(14, "Ex: Alex");
        champNomPartenaire = creerChampTexte(16, "Ex: Martin");
        champCommuneNaissancePartenaire = creerChampTexte(20, "Ex: Lyon");
        champAnneeNaissancePartenaire = creerChampTexte(8, "Ex: 1995");
        caseMentionPacs = creerCase("Inclure la mention de PACS");
        champAutoritePacs = creerChampTexte(24, "Ex: Mairie du 12e arrondissement");

        comboOptionLivret = creerCombo("Aucune demande", "Mise à jour du livret de famille", "Établissement d’un nouveau livret");

        panneauEnfants = new JPanel();
        panneauEnfants.setOpaque(false);
        panneauEnfants.setLayout(new BoxLayout(panneauEnfants, BoxLayout.Y_AXIS));
        ascenseurEnfants = new JScrollPane(panneauEnfants);
        ascenseurEnfants.setBorder(new EmptyBorder(0, 0, 0, 0));
        ascenseurEnfants.setPreferredSize(new Dimension(0, 260));
        ascenseurEnfants.getViewport().setOpaque(false);
        ascenseurEnfants.setOpaque(false);
        boutonAjouterEnfant = new JButton("Ajouter un enfant");
        StyliseurBoutonTheme.appliquer(boutonAjouterEnfant, theme().palette().secondaryButton(), theme(), theme().typography().buttonSecondary());
        labelAlerteEnfants = new JLabel(" ");
        labelAlerteEnfants.setFont(theme().typography().helper());
        labelAlerteEnfants.setForeground(theme().palette().error());

        ajouterTitreSection("Identité et contact");
        ajouterLigneChamp("Prénoms", champPrenom);
        ajouterLigneChamp("Nom", champNom);
        ajouterLigneChamp("Adresse postale", ascenseurAdressePostale, ascenseurAdressePostale);
        ajouterLigneChamp("Téléphone portable", champTelephonePortable);
        ajouterLigneChamp("Courriel", champCourriel);

        ajouterTitreSection("Destinataire et décision");
        ajouterLigneChamp("Ville de la mairie", champVilleAutoriteDestinataire);
        ajouterLigneChamp("Adresse du destinataire", ascenseurAdresseDestinataire, ascenseurAdresseDestinataire);
        ajouterLigneChamp("Ville de rédaction", champVilleRedaction);
        ajouterLigneChamp("Genre pour les accords", comboGenreAccords);
        ajouterLigneChamp("Date de naissance", selecteurDateNaissance, selecteurDateNaissance);
        ajouterLigneChamp("Lieu de naissance", champLieuNaissance);
        ajouterLigneChamp("Autorité ayant rendu la décision", comboAutoriteDecision);
        ajouterLigneChamp("Date de la décision", selecteurDateDecision, selecteurDateDecision);
        ajouterLigneChamp("Date à laquelle la décision est définitive", selecteurDateDecisionDefinitive, selecteurDateDecisionDefinitive);
        ajouterLignePleine(caseChangementPrenoms);
        ajouterLignePleine(caseChangementSexe);

        ajouterTitreSection("Actes me concernant");
        ajouterLignePleine(caseActeNaissanceRequerant);
        ajouterLigneChamp("Commune de naissance", champCommuneNaissanceRequerant);

        ajouterTitreSection("Partenaire et PACS");
        ajouterLignePleine(caseConcernePartenaire);
        ajouterLigneChamp("Lien", comboLienPartenaire);
        ajouterLigneChamp("Genre du/de la partenaire", comboGenrePartenaire);
        ajouterLignePleine(caseActeMariage);
        ajouterLigneChamp("Commune du mariage", champCommuneMariage);
        ajouterLigneChamp("Date du mariage", selecteurDateMariage, selecteurDateMariage);
        ajouterLignePleine(caseActeNaissancePartenaire);
        ajouterLigneChamp("Prénom du/de la partenaire", champPrenomPartenaire);
        ajouterLigneChamp("Nom du/de la partenaire", champNomPartenaire);
        ajouterLigneChamp("Commune de naissance du/de la partenaire", champCommuneNaissancePartenaire);
        ajouterLigneChamp("Année de naissance du/de la partenaire", champAnneeNaissancePartenaire);
        ajouterLignePleine(caseMentionPacs);
        ajouterLigneChamp("Commune/autorité dépositaire du PACS", champAutoritePacs);

        ajouterTitreSection("Enfants et livret de famille");
        JPanel carteEnfants = creerCarteEnfants();
        ajouterLignePleine(carteEnfants);
        ajouterLigneChamp("Livret de famille", comboOptionLivret);

        boutonAjouterEnfant.addActionListener(e -> ajouterLigneEnfant(InstantaneEnfantActe.vide()));
        caseConcernePartenaire.addActionListener(e -> mettreAJourEtatPartenaire());
        caseActeMariage.addActionListener(e -> mettreAJourEtatPartenaire());
        caseActeNaissancePartenaire.addActionListener(e -> mettreAJourEtatPartenaire());
        caseMentionPacs.addActionListener(e -> mettreAJourEtatPartenaire());
        comboGenreAccords.addActionListener(e -> mettreAJourExemplesSelonGenre());
        comboGenrePartenaire.addActionListener(e -> mettreAJourExemplePrenomPartenaire());
        comboAutoriteDecision.addActionListener(e -> mettreAJourEtatTypesChangement());

        appliquerInstantane(instantaneInitial);
        mettreAJourEtatTypesChangement();
        mettreAJourEtatPartenaire();
        mettreAJourExemplesSelonGenre();
        configurerAccessibiliteComposants();

        terminerConstruction();
        SwingUtilities.invokeLater(champPrenom::requestFocusInWindow);
    }

    @Override
    protected boolean validerFormulaire() {
        boolean valide = true;
        if (!formatPrenomValide(texte(champPrenom))) {
            signalerErreur(champPrenom, "Les prénoms sont obligatoires et doivent avoir un format valide.");
            valide = false;
        }
        if (!formatNomValide(texte(champNom))) {
            signalerErreur(champNom, "Le nom est obligatoire et doit avoir un format valide.");
            valide = false;
        }
        if (!texteMultiligneValide(texte(zoneAdressePostale), 8, 420)) {
            signalerErreur(ascenseurAdressePostale, "L'adresse postale est obligatoire et doit être suffisamment précise.");
            valide = false;
        }
        if (!formatTelephoneValide(texte(champTelephonePortable))) {
            signalerErreur(champTelephonePortable, "Le numéro de téléphone portable n'a pas un format valide.");
            valide = false;
        }
        if (!formatCourrielValide(texte(champCourriel))) {
            signalerErreur(champCourriel, "Le courriel n'a pas un format valide.");
            valide = false;
        }
        if (!formatVilleValide(texte(champVilleAutoriteDestinataire))) {
            signalerErreur(champVilleAutoriteDestinataire, "La ville de la mairie est obligatoire.");
            valide = false;
        }
        if (!texteMultiligneValide(texte(zoneAdresseDestinataire), 8, 420)) {
            signalerErreur(ascenseurAdresseDestinataire, "L'adresse du destinataire est obligatoire.");
            valide = false;
        }
        if (!formatVilleValide(texte(champVilleRedaction))) {
            signalerErreur(champVilleRedaction, "La ville de rédaction est obligatoire.");
            valide = false;
        }
        if (!selecteurDateNaissance.estDateValide()) {
            signalerErreur(selecteurDateNaissance, "La date de naissance est invalide ou future.");
            valide = false;
        }
        if (!texteSimpleValide(texte(champLieuNaissance), 2, 120)) {
            signalerErreur(champLieuNaissance, "Le lieu de naissance est obligatoire.");
            valide = false;
        }
        if (!selecteurDateDecision.estDateValide()) {
            signalerErreur(selecteurDateDecision, "La date de la décision est invalide ou future.");
            valide = false;
        }
        if (!selecteurDateDecisionDefinitive.estDateValide()) {
            signalerErreur(selecteurDateDecisionDefinitive, "La date de décision définitive est invalide ou future.");
            valide = false;
        }
        if (autoriteDecisionEstOfficierEtatCivil() && caseChangementSexe.isSelected()) {
            signalerErreur(caseChangementSexe, "Une décision de l’officier·e d’état civil ne peut pas concerner la mention du sexe.");
            valide = false;
        }
        if (!caseChangementPrenoms.isSelected() && !caseChangementSexe.isSelected()) {
            signalerErreurGlobale("Sélectionner au moins un type de changement : prénoms et/ou mention du sexe.");
            valide = false;
        }
        if (caseActeNaissanceRequerant.isSelected()) {
            if (!formatVilleValide(texte(champCommuneNaissanceRequerant))) {
                signalerErreur(champCommuneNaissanceRequerant, "La commune de naissance est obligatoire.");
                valide = false;
            }
        }
        if (caseConcernePartenaire.isSelected()) {
            if (texteSelection(comboLienPartenaire).isBlank()) {
                signalerErreur(comboLienPartenaire, "Le lien partenaire est obligatoire.");
                valide = false;
            }
            if (texteSelection(comboGenrePartenaire).isBlank()) {
                signalerErreur(comboGenrePartenaire, "Le genre du/de la partenaire est obligatoire.");
                valide = false;
            }
            if (caseActeMariage.isSelected()) {
                if (!formatVilleValide(texte(champCommuneMariage))) {
                    signalerErreur(champCommuneMariage, "La commune du mariage est obligatoire.");
                    valide = false;
                }
                if (!selecteurDateMariage.estDateValide()) {
                    signalerErreur(selecteurDateMariage, "La date du mariage est invalide ou future.");
                    valide = false;
                }
            }
            if (caseActeNaissancePartenaire.isSelected()) {
                if (!formatPrenomValide(texte(champPrenomPartenaire))) {
                    signalerErreur(champPrenomPartenaire, "Le prénom du/de la partenaire est obligatoire.");
                    valide = false;
                }
                if (!formatNomValide(texte(champNomPartenaire))) {
                    signalerErreur(champNomPartenaire, "Le nom du/de la partenaire est obligatoire.");
                    valide = false;
                }
                if (!formatVilleValide(texte(champCommuneNaissancePartenaire))) {
                    signalerErreur(champCommuneNaissancePartenaire, "La commune de naissance du/de la partenaire est obligatoire.");
                    valide = false;
                }
                if (!formatAnneeValide(texte(champAnneeNaissancePartenaire))) {
                    signalerErreur(champAnneeNaissancePartenaire, "L'année de naissance du/de la partenaire doit contenir 4 chiffres.");
                    valide = false;
                }
            }
            if (caseMentionPacs.isSelected() && !texteSimpleValide(texte(champAutoritePacs), 2, 180)) {
                signalerErreur(champAutoritePacs, "La commune ou l'autorité dépositaire du PACS est obligatoire.");
                valide = false;
            }
            if (!caseActeMariage.isSelected() && !caseActeNaissancePartenaire.isSelected() && !caseMentionPacs.isSelected()) {
                signalerErreurGlobale("Sélectionner au moins un acte partenaire : mariage, naissance partenaire ou PACS.");
                valide = false;
            }
        }

        for (LigneEnfant ligneEnfant : lignesEnfants) {
            if (!formatPrenomValide(texte(ligneEnfant.champPrenom()))) {
                signalerErreur(ligneEnfant.champPrenom(), "Chaque enfant doit avoir un prénom valide.");
                valide = false;
            }
            if (!formatNomValide(texte(ligneEnfant.champNom()))) {
                signalerErreur(ligneEnfant.champNom(), "Chaque enfant doit avoir un nom valide.");
                valide = false;
            }
            if (!formatVilleValide(texte(ligneEnfant.champCommuneNaissance()))) {
                signalerErreur(ligneEnfant.champCommuneNaissance(), "Chaque enfant doit avoir une commune de naissance valide.");
                valide = false;
            }
            if (!ligneEnfant.selecteurDateNaissance().estDateValide()) {
                signalerErreur(ligneEnfant.selecteurDateNaissance(), "Chaque enfant doit avoir une date de naissance valide.");
                valide = false;
            }
            if (texteSelection(ligneEnfant.comboGenre()).isBlank()) {
                signalerErreur(ligneEnfant.comboGenre(), "Chaque enfant doit avoir un genre sélectionné.");
                valide = false;
            }
        }
        if (lignesEnfants.size() > LIMITE_MAX_ENFANTS) {
            signalerErreurGlobale("Pour des raisons de sécurité, le nombre d'enfants est limité à 10.");
            valide = false;
        }

        return valide;
    }

    @Override
    protected String titreChoixDestination() {
        return "Enregistrer la lettre de mise à jour des actes liés";
    }

    @Override
    protected String nomFichierParDefaut() {
        return "lettre_mise_a_jour_actes_lies.docx";
    }

    @Override
    protected void genererDocument(Path destination, boolean ecraser) throws ErreurExportDocument {
        serviceGenerationLettreMiseAJourActesLies.exporter(construireDonnees(), destination, ecraser);
    }

    @Override
    protected String messageSuccesGeneration(Path destination) {
        return "La lettre de mise à jour des actes liés a été générée : " + destination.toAbsolutePath();
    }

    InstantaneLettreMiseAJourActesLies instantane() {
        boolean changementSexeSelectionne = caseChangementSexe.isSelected() && !autoriteDecisionEstOfficierEtatCivil();
        List<InstantaneEnfantActe> enfants = new ArrayList<>();
        for (LigneEnfant ligneEnfant : lignesEnfants) {
            enfants.add(new InstantaneEnfantActe(texte(ligneEnfant.champPrenom()), texte(ligneEnfant.champNom()), texte(ligneEnfant.champCommuneNaissance()), ligneEnfant.selecteurDateNaissance().texteDate(), texteSelection(ligneEnfant.comboGenre())));
        }
        return new InstantaneLettreMiseAJourActesLies(texte(champPrenom), texte(champNom), texte(zoneAdressePostale), texte(champTelephonePortable), texte(champCourriel), "Officier·e de l’état civil", texte(champVilleAutoriteDestinataire), texte(zoneAdresseDestinataire), texte(champVilleRedaction), dateDuJourTexte(), texteSelection(comboGenreAccords), selecteurDateNaissance.texteDate(), texte(champLieuNaissance), texteSelection(comboAutoriteDecision), selecteurDateDecision.texteDate(), selecteurDateDecisionDefinitive.texteDate(), caseChangementPrenoms.isSelected(), changementSexeSelectionne, caseActeNaissanceRequerant.isSelected(), texte(champCommuneNaissanceRequerant), anneeDepuisDate(selecteurDateNaissance.texteDate()), caseConcernePartenaire.isSelected(), texteSelection(comboLienPartenaire), texteSelection(comboGenrePartenaire), caseActeMariage.isSelected(), texte(champCommuneMariage), selecteurDateMariage.texteDate(), caseActeNaissancePartenaire.isSelected(), texte(champPrenomPartenaire), texte(champNomPartenaire), texte(champCommuneNaissancePartenaire), texte(champAnneeNaissancePartenaire), caseMentionPacs.isSelected(), texte(champAutoritePacs), texteSelection(comboOptionLivret), enfants);
    }

    private DonneesLettreMiseAJourActesLies construireDonnees() {
        InstantaneLettreMiseAJourActesLies instantane = instantane();
        List<DonneesEnfantActe> enfants = instantane.enfants().stream().map(enfant -> new DonneesEnfantActe(enfant.prenom(), enfant.nom(), enfant.communeNaissance(), enfant.dateNaissance(), enfant.genre())).toList();
        return new DonneesLettreMiseAJourActesLies(instantane.prenom(), instantane.nom(), instantane.adressePostale(), instantane.telephonePortable(), instantane.courriel(), instantane.villeAutoriteDestinataire(), instantane.adresseDestinataire(), instantane.villeRedaction(), instantane.dateRedaction(), instantane.genreAccords(), instantane.dateNaissance(), instantane.lieuNaissance(), instantane.autoriteDecision(), instantane.dateDecision(), instantane.dateDecisionDefinitive(), instantane.changementPrenoms(), instantane.changementSexe(), instantane.acteNaissanceRequerant(), instantane.communeNaissanceRequerant(), instantane.anneeNaissanceRequerant(), instantane.concernePartenaire(), instantane.lienPartenaire(), instantane.genrePartenaire(), instantane.acteMariage(), instantane.communeMariage(), instantane.dateMariage(), instantane.acteNaissancePartenaire(), instantane.prenomPartenaire(), instantane.nomPartenaire(), instantane.communeNaissancePartenaire(), instantane.anneeNaissancePartenaire(), instantane.mentionPacs(), instantane.autoritePacs(), instantane.optionLivret(), enfants);
    }

    private void appliquerInstantane(InstantaneLettreMiseAJourActesLies instantaneInitial) {
        InstantaneLettreMiseAJourActesLies instantane = instantaneInitial == null ? InstantaneLettreMiseAJourActesLies.vide() : instantaneInitial;
        champPrenom.setText(instantane.prenom());
        champNom.setText(instantane.nom());
        zoneAdressePostale.setText(instantane.adressePostale());
        champTelephonePortable.setText(instantane.telephonePortable());
        champCourriel.setText(instantane.courriel());
        champVilleAutoriteDestinataire.setText(instantane.villeAutoriteDestinataire());
        zoneAdresseDestinataire.setText(instantane.adresseDestinataire());
        champVilleRedaction.setText(instantane.villeRedaction());
        if (!instantane.genreAccords().isBlank()) {
            comboGenreAccords.setSelectedItem(instantane.genreAccords());
        }
        selecteurDateNaissance.appliquerDateTexte(instantane.dateNaissance());
        champLieuNaissance.setText(instantane.lieuNaissance());
        if (!instantane.autoriteDecision().isBlank()) {
            comboAutoriteDecision.setSelectedItem(instantane.autoriteDecision());
        }
        selecteurDateDecision.appliquerDateTexte(instantane.dateDecision());
        selecteurDateDecisionDefinitive.appliquerDateTexte(instantane.dateDecisionDefinitive());
        caseChangementPrenoms.setSelected(instantane.changementPrenoms());
        caseChangementSexe.setSelected(instantane.changementSexe());
        mettreAJourEtatTypesChangement();
        caseActeNaissanceRequerant.setSelected(instantane.acteNaissanceRequerant());
        champCommuneNaissanceRequerant.setText(instantane.communeNaissanceRequerant());
        caseConcernePartenaire.setSelected(instantane.concernePartenaire());
        if (!instantane.lienPartenaire().isBlank()) {
            comboLienPartenaire.setSelectedItem(instantane.lienPartenaire());
        }
        if (!instantane.genrePartenaire().isBlank()) {
            comboGenrePartenaire.setSelectedItem(instantane.genrePartenaire());
        }
        caseActeMariage.setSelected(instantane.acteMariage());
        champCommuneMariage.setText(instantane.communeMariage());
        selecteurDateMariage.appliquerDateTexte(instantane.dateMariage());
        caseActeNaissancePartenaire.setSelected(instantane.acteNaissancePartenaire());
        champPrenomPartenaire.setText(instantane.prenomPartenaire());
        champNomPartenaire.setText(instantane.nomPartenaire());
        champCommuneNaissancePartenaire.setText(instantane.communeNaissancePartenaire());
        champAnneeNaissancePartenaire.setText(instantane.anneeNaissancePartenaire());
        caseMentionPacs.setSelected(instantane.mentionPacs());
        champAutoritePacs.setText(instantane.autoritePacs());
        if (!instantane.optionLivret().isBlank()) {
            comboOptionLivret.setSelectedItem(instantane.optionLivret());
        }
        viderLignesEnfants();
        for (InstantaneEnfantActe enfant : instantane.enfants()) {
            if (lignesEnfants.size() >= LIMITE_MAX_ENFANTS) {
                break;
            }
            ajouterLigneEnfant(enfant);
        }
        mettreAJourAlerteEnfants();
    }

    private JPanel creerCarteEnfants() {
        int espace = Math.max(8, theme().spacing().inlineGap());
        JPanel carte = new JPanel(new BorderLayout(0, espace));
        carte.setOpaque(true);
        carte.setBackground(melangerCouleurs(theme().palette().cardBackground(), theme().palette().surfaceBackground(), 0.62f));
        carte.setBorder(new EmptyBorder(espace, espace, espace, espace));

        JPanel panneauActions = new JPanel();
        panneauActions.setOpaque(false);
        panneauActions.setLayout(new BoxLayout(panneauActions, BoxLayout.X_AXIS));
        panneauActions.add(boutonAjouterEnfant);
        panneauActions.add(Box.createHorizontalStrut(Math.max(6, theme().spacing().inlineGap() / 2)));
        panneauActions.add(labelAlerteEnfants);
        panneauActions.add(Box.createHorizontalGlue());
        carte.add(panneauActions, BorderLayout.NORTH);

        carte.add(ascenseurEnfants, BorderLayout.CENTER);
        return carte;
    }

    private void ajouterLigneEnfant(InstantaneEnfantActe instantaneEnfantActe) {
        if (lignesEnfants.size() >= LIMITE_MAX_ENFANTS) {
            mettreAJourAlerteEnfants();
            return;
        }
        LigneEnfant ligneEnfant = LigneEnfant.creer(instantaneEnfantActe, this, theme(), lignesEnfants.size() + 1);
        ligneEnfant.boutonSupprimer().addActionListener(e -> supprimerLigneEnfant(ligneEnfant));
        lignesEnfants.add(ligneEnfant);
        panneauEnfants.add(ligneEnfant.panneau());
        panneauEnfants.add(Box.createVerticalStrut(Math.max(8, theme().spacing().inlineGap() / 2)));
        mettreAJourAlerteEnfants();
        panneauEnfants.revalidate();
        panneauEnfants.repaint();
    }

    private void supprimerLigneEnfant(LigneEnfant ligneEnfant) {
        int index = lignesEnfants.indexOf(ligneEnfant);
        if (index < 0) {
            return;
        }
        lignesEnfants.remove(index);
        int composantIndex = index * 2;
        if (composantIndex < panneauEnfants.getComponentCount()) {
            panneauEnfants.remove(composantIndex);
        }
        if (composantIndex < panneauEnfants.getComponentCount()) {
            panneauEnfants.remove(composantIndex);
        }
        renumeroterLignesEnfants();
        mettreAJourAlerteEnfants();
        panneauEnfants.revalidate();
        panneauEnfants.repaint();
        effacerErreursValidation();
    }

    private void viderLignesEnfants() {
        lignesEnfants.clear();
        panneauEnfants.removeAll();
    }

    private void renumeroterLignesEnfants() {
        for (int index = 0; index < lignesEnfants.size(); index++) {
            lignesEnfants.get(index).mettreAJourIndice(index + 1);
        }
    }

    private Color melangerCouleurs(Color premier, Color second, float ratioPremier) {
        float ratio = Math.max(0f, Math.min(1f, ratioPremier));
        float inverse = 1f - ratio;
        int rouge = Math.round(premier.getRed() * ratio + second.getRed() * inverse);
        int vert = Math.round(premier.getGreen() * ratio + second.getGreen() * inverse);
        int bleu = Math.round(premier.getBlue() * ratio + second.getBlue() * inverse);
        int alpha = Math.round(premier.getAlpha() * ratio + second.getAlpha() * inverse);
        return new Color(rouge, vert, bleu, alpha);
    }

    private void mettreAJourAlerteEnfants() {
        boutonAjouterEnfant.setEnabled(lignesEnfants.size() < LIMITE_MAX_ENFANTS);
        if (lignesEnfants.size() >= 4) {
            if (lignesEnfants.size() >= LIMITE_MAX_ENFANTS) {
                labelAlerteEnfants.setText("Limite de sécurité atteinte : 10 enfants maximum.");
            } else {
                labelAlerteEnfants.setText("Ça fait beaucoup là, non ?");
            }
        } else {
            labelAlerteEnfants.setText(" ");
        }
    }

    private void mettreAJourExemplesSelonGenre() {
        String genre = normaliserGenre(texteSelection(comboGenreAccords));
        champPrenom.setExemple(exemplePrenoms(genre));
        mettreAJourExemplePrenomPartenaire();
    }

    private void mettreAJourExemplePrenomPartenaire() {
        String genre = normaliserGenre(texteSelection(comboGenrePartenaire));
        champPrenomPartenaire.setExemple(exemplePrenomSimple(genre));
    }

    private void mettreAJourEtatPartenaire() {
        boolean actif = caseConcernePartenaire.isSelected();
        comboLienPartenaire.setEnabled(actif);
        comboGenrePartenaire.setEnabled(actif);
        caseActeMariage.setEnabled(actif);
        caseActeNaissancePartenaire.setEnabled(actif);
        caseMentionPacs.setEnabled(actif);

        boolean mariageActif = actif && caseActeMariage.isSelected();
        champCommuneMariage.setEnabled(mariageActif);
        selecteurDateMariage.setEnabled(mariageActif);

        boolean naissancePartenaireActive = actif && caseActeNaissancePartenaire.isSelected();
        champPrenomPartenaire.setEnabled(naissancePartenaireActive);
        champNomPartenaire.setEnabled(naissancePartenaireActive);
        champCommuneNaissancePartenaire.setEnabled(naissancePartenaireActive);
        champAnneeNaissancePartenaire.setEnabled(naissancePartenaireActive);

        boolean pacsActif = actif && caseMentionPacs.isSelected();
        champAutoritePacs.setEnabled(pacsActif);
    }

    private void mettreAJourEtatTypesChangement() {
        boolean autoriteOfficierEtatCivil = autoriteDecisionEstOfficierEtatCivil();
        if (autoriteOfficierEtatCivil) {
            caseChangementSexe.setSelected(false);
        }
        caseChangementSexe.setEnabled(!autoriteOfficierEtatCivil);
        if (autoriteOfficierEtatCivil) {
            caseChangementSexe.setToolTipText("Impossible avec une décision d’officier·e d’état civil.");
        } else {
            caseChangementSexe.setToolTipText("Indique si la décision concerne la mention du sexe");
        }
    }

    private boolean autoriteDecisionEstOfficierEtatCivil() {
        String autorite = texteSelection(comboAutoriteDecision).toLowerCase(Locale.ROOT);
        return autorite.contains("officier");
    }

    private boolean formatAnneeValide(String annee) {
        return annee != null && MOTIF_ANNEE.matcher(annee.trim()).matches();
    }

    private String anneeDepuisDate(String dateTexte) {
        if (!ParseursDate.dateSaisieValide(dateTexte)) {
            return "";
        }
        return Integer.toString(ParseursDate.parserDateSaisie(dateTexte).getYear());
    }

    private String normaliserGenre(String valeur) {
        String texte = valeur == null ? "" : valeur.trim().toLowerCase(Locale.ROOT);
        if (texte.startsWith("f")) {
            return "féminin";
        }
        if (texte.startsWith("n")) {
            return "non-binaire";
        }
        return "masculin";
    }

    private void configurerAccessibiliteComposants() {
        configurerAccessibilite(champPrenom, "Prénoms", "Saisir les prénoms de la personne requérante");
        configurerAccessibilite(champNom, "Nom", "Saisir le nom de famille");
        configurerAccessibilite(zoneAdressePostale, "Adresse postale", "Saisir l'adresse postale complète");
        configurerAccessibilite(champTelephonePortable, "Téléphone portable", "Saisir le numéro de téléphone portable");
        configurerAccessibilite(champCourriel, "Courriel", "Saisir l'adresse courriel");
        configurerAccessibilite(champVilleAutoriteDestinataire, "Ville de la mairie", "Saisir la ville de la mairie destinataire");
        configurerAccessibilite(zoneAdresseDestinataire, "Adresse du destinataire", "Saisir l'adresse complète du destinataire");
        configurerAccessibilite(champVilleRedaction, "Ville de rédaction", "Saisir la ville de rédaction de la lettre");
        configurerAccessibilite(comboGenreAccords, "Genre pour les accords", "Choisir le genre utilisé pour les accords");
        configurerAccessibilite(selecteurDateNaissance, "Date de naissance", "Sélecteur jour mois année de la date de naissance");
        configurerAccessibilite(champLieuNaissance, "Lieu de naissance", "Saisir le lieu de naissance");
        configurerAccessibilite(comboAutoriteDecision, "Autorité ayant rendu la décision", "Choisir l'autorité qui a rendu la décision");
        configurerAccessibilite(selecteurDateDecision, "Date de décision", "Sélecteur jour mois année de la décision");
        configurerAccessibilite(selecteurDateDecisionDefinitive, "Date de décision définitive", "Sélecteur jour mois année de la décision définitive");
        configurerAccessibilite(caseChangementPrenoms, "Changement de prénoms", "Indique si la décision concerne un changement de prénoms");
        configurerAccessibilite(caseChangementSexe, "Changement de mention du sexe", "Indique si la décision concerne la mention du sexe");
        configurerAccessibilite(caseConcernePartenaire, "Actes partenaire", "Activer les actes liés à un·e partenaire");
        configurerAccessibilite(comboGenrePartenaire, "Genre du/de la partenaire", "Choisir le genre du/de la partenaire pour les accords");
        configurerAccessibilite(boutonAjouterEnfant, "Ajouter un enfant", "Ajouter une ligne d'enfant dans les actes à mettre à jour");
        configurerAccessibilite(comboOptionLivret, "Livret de famille", "Choisir la demande concernant le livret de famille");
    }

    private void configurerAccessibilite(JComponent composant, String nom, String description) {
        if (composant == null) {
            return;
        }
        composant.getAccessibleContext().setAccessibleName(nom);
        composant.getAccessibleContext().setAccessibleDescription(description);
        composant.setToolTipText(description);
    }

    private record LigneEnfant(JPanel panneau, JLabel labelTitre, ChampTexteExempleDialogue champPrenom,
                               ChampTexteExempleDialogue champNom, ChampTexteExempleDialogue champCommuneNaissance,
                               SelecteurDateDialogue selecteurDateNaissance, JComboBox<String> comboGenre,
                               JButton boutonSupprimer) {

        static LigneEnfant creer(InstantaneEnfantActe enfant, DialogueLettreMiseAJourActesLies dialogue, TokensTheme theme, int index) {
            ChampTexteExempleDialogue champPrenom = dialogue.creerChampTexte(12, "Prénom");
            ChampTexteExempleDialogue champNom = dialogue.creerChampTexte(14, "Nom");
            ChampTexteExempleDialogue champCommune = dialogue.creerChampTexte(14, "Commune");
            SelecteurDateDialogue selecteurDateNaissance = dialogue.creerSelecteurDate();
            JComboBox<String> comboGenre = dialogue.creerCombo("Masculin", "Féminin", "Non-binaire");

            JButton boutonSupprimer = new JButton("Retirer");
            StyliseurBoutonTheme.appliquer(boutonSupprimer, theme.palette().secondaryButton(), theme, theme.typography().buttonSecondary());

            champPrenom.setText(enfant.prenom());
            champNom.setText(enfant.nom());
            champCommune.setText(enfant.communeNaissance());
            selecteurDateNaissance.appliquerDateTexte(enfant.dateNaissance());
            if (!enfant.genre().isBlank()) {
                comboGenre.setSelectedItem(enfant.genre());
            }

            JPanel panneau = new JPanel(new GridBagLayout());
            panneau.setOpaque(true);
            panneau.setBackground(dialogue.melangerCouleurs(theme.palette().cardBackground(), theme.palette().surfaceBackground(), 0.78f));
            int margeCarte = Math.max(8, theme.spacing().inlineGap() / 2);
            panneau.setBorder(new EmptyBorder(margeCarte, margeCarte, margeCarte, margeCarte));

            JLabel labelTitre = new JLabel("Enfant " + index);
            labelTitre.setFont(theme.typography().label().deriveFont(Font.BOLD));
            labelTitre.setForeground(theme.palette().titleText());

            GridBagConstraints contraintes = new GridBagConstraints();
            contraintes.gridy = 0;
            contraintes.gridx = 0;
            contraintes.weightx = 1;
            contraintes.anchor = GridBagConstraints.WEST;
            contraintes.fill = GridBagConstraints.HORIZONTAL;
            contraintes.insets = new Insets(0, 0, margeCarte, margeCarte);
            panneau.add(labelTitre, contraintes);

            contraintes.gridx = 1;
            contraintes.weightx = 0;
            contraintes.anchor = GridBagConstraints.EAST;
            contraintes.fill = GridBagConstraints.NONE;
            contraintes.insets = new Insets(0, 0, margeCarte, 0);
            panneau.add(boutonSupprimer, contraintes);

            JPanel grille = new JPanel(new GridBagLayout());
            grille.setOpaque(false);
            contraintes = new GridBagConstraints();
            contraintes.gridy = 0;
            contraintes.insets = new Insets(0, 0, Math.max(4, margeCarte / 2), Math.max(6, margeCarte));
            contraintes.anchor = GridBagConstraints.WEST;
            contraintes.fill = GridBagConstraints.HORIZONTAL;

            JLabel labelPrenom = new JLabel("Prénom");
            labelPrenom.setFont(theme.typography().helper());
            labelPrenom.setForeground(theme.palette().mutedText());
            contraintes.gridx = 0;
            contraintes.weightx = 0.23;
            grille.add(labelPrenom, contraintes);

            JLabel labelNom = new JLabel("Nom");
            labelNom.setFont(theme.typography().helper());
            labelNom.setForeground(theme.palette().mutedText());
            contraintes.gridx = 1;
            contraintes.weightx = 0.23;
            grille.add(labelNom, contraintes);

            JLabel labelCommune = new JLabel("Commune");
            labelCommune.setFont(theme.typography().helper());
            labelCommune.setForeground(theme.palette().mutedText());
            contraintes.gridx = 2;
            contraintes.weightx = 0.24;
            grille.add(labelCommune, contraintes);

            JLabel labelDate = new JLabel("Date de naissance");
            labelDate.setFont(theme.typography().helper());
            labelDate.setForeground(theme.palette().mutedText());
            contraintes.gridx = 3;
            contraintes.weightx = 0.2;
            grille.add(labelDate, contraintes);

            JLabel labelGenre = new JLabel("Genre");
            labelGenre.setFont(theme.typography().helper());
            labelGenre.setForeground(theme.palette().mutedText());
            contraintes.gridx = 4;
            contraintes.weightx = 0.1;
            contraintes.insets = new Insets(0, 0, Math.max(4, margeCarte / 2), 0);
            grille.add(labelGenre, contraintes);

            contraintes.gridy = 1;
            contraintes.insets = new Insets(0, 0, 0, Math.max(6, margeCarte));
            contraintes.gridx = 0;
            contraintes.weightx = 0.26;
            grille.add(champPrenom, contraintes);

            contraintes.gridx = 1;
            contraintes.weightx = 0.26;
            grille.add(champNom, contraintes);

            contraintes.gridx = 2;
            contraintes.weightx = 0.26;
            grille.add(champCommune, contraintes);

            contraintes.gridx = 3;
            contraintes.weightx = 0.14;
            grille.add(selecteurDateNaissance, contraintes);

            contraintes.gridx = 4;
            contraintes.weightx = 0.08;
            contraintes.insets = new Insets(0, 0, 0, 0);
            grille.add(comboGenre, contraintes);

            GridBagConstraints contraintesPanneau = new GridBagConstraints();
            contraintesPanneau.gridx = 0;
            contraintesPanneau.gridy = 1;
            contraintesPanneau.gridwidth = 2;
            contraintesPanneau.weightx = 1;
            contraintesPanneau.fill = GridBagConstraints.HORIZONTAL;
            panneau.add(grille, contraintesPanneau);

            LigneEnfant ligne = new LigneEnfant(panneau, labelTitre, champPrenom, champNom, champCommune, selecteurDateNaissance, comboGenre, boutonSupprimer);

            champPrenom.getAccessibleContext().setAccessibleName("Prénom de l'enfant " + index);
            champNom.getAccessibleContext().setAccessibleName("Nom de l'enfant " + index);
            champCommune.getAccessibleContext().setAccessibleName("Commune de naissance de l'enfant " + index);
            selecteurDateNaissance.getAccessibleContext().setAccessibleName("Date de naissance de l'enfant " + index);
            comboGenre.getAccessibleContext().setAccessibleName("Genre de l'enfant " + index);
            boutonSupprimer.getAccessibleContext().setAccessibleName("Retirer l'enfant " + index);

            return ligne;
        }

        void mettreAJourIndice(int index) {
            labelTitre.setText("Enfant " + index);
            champPrenom.getAccessibleContext().setAccessibleName("Prénom de l'enfant " + index);
            champNom.getAccessibleContext().setAccessibleName("Nom de l'enfant " + index);
            champCommuneNaissance.getAccessibleContext().setAccessibleName("Commune de naissance de l'enfant " + index);
            selecteurDateNaissance.getAccessibleContext().setAccessibleName("Date de naissance de l'enfant " + index);
            comboGenre.getAccessibleContext().setAccessibleName("Genre de l'enfant " + index);
            boutonSupprimer.getAccessibleContext().setAccessibleName("Retirer l'enfant " + index);
        }
    }
}
