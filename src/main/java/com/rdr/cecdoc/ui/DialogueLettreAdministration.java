package com.rdr.cecdoc.ui;

import com.rdr.cecdoc.model.DonneesLettreAdministration;
import com.rdr.cecdoc.model.InstantaneLettreAdministration;
import com.rdr.cecdoc.service.export.EcritureDocxAtomique;
import com.rdr.cecdoc.service.export.ErreurExportDocument;
import com.rdr.cecdoc.service.export.ServiceGenerationLettreAdministration;
import com.rdr.cecdoc.ui.theme.TokensTheme;

import java.awt.Image;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

final class DialogueLettreAdministration extends DialogueFormulaireDocumentAbstrait {
    private final transient ServiceGenerationLettreAdministration serviceGenerationLettreAdministration;

    private final ChampTexteExempleDialogue champPrenomUsage;
    private final ChampTexteExempleDialogue champPrenomsEtatCivil;
    private final ChampTexteExempleDialogue champNom;
    private final ZoneTexteExempleDialogue zoneAdressePostale;
    private final JScrollPane ascenseurAdressePostale;
    private final ChampTexteExempleDialogue champTelephonePortable;
    private final ChampTexteExempleDialogue champCourriel;
    private final ZoneTexteExempleDialogue zoneAdresseDestinataire;
    private final JScrollPane ascenseurAdresseDestinataire;
    private final JCheckBox caseChangementPrenom;
    private final ChampTexteExempleDialogue champPrenomNaissance;
    private final JCheckBox caseChangementSexe;
    private final JComboBox<String> comboSexeAvant;
    private final JComboBox<String> comboSexeApres;
    private final JCheckBox caseDecisionMairie;
    private final ChampTexteExempleDialogue champNumeroDecisionMairie;
    private final SelecteurDateDialogue selecteurDateDecisionMairie;
    private final ChampTexteExempleDialogue champTribunalCompetent;
    private final ChampTexteExempleDialogue champNumeroJugement;
    private final ChampTexteExempleDialogue champVilleActuelle;

    DialogueLettreAdministration(JFrame proprietaire, TokensTheme theme, List<Image> iconesApplication, InstantaneLettreAdministration instantaneInitial, Path dossierSortieParDefaut) {
        super(proprietaire, theme, iconesApplication, dossierSortieParDefaut, "Lettre administration", "Lettre pour mettre à jour mes informations auprès d'une administration", "Générer la lettre");
        this.serviceGenerationLettreAdministration = new ServiceGenerationLettreAdministration(new EcritureDocxAtomique());

        champPrenomUsage = creerChampTexte(20, "Ex: Tom");
        champPrenomsEtatCivil = creerChampTexte(24, "Ex: Tom, Noé, Max");
        champNom = creerChampTexte(24, "Ex: Dupont");
        zoneAdressePostale = creerZoneTexte(3, 32, "Ex: 12 rue de la Paix\n75000 Paris");
        ascenseurAdressePostale = creerAscenseurZoneTexte(zoneAdressePostale, 92);
        champTelephonePortable = creerChampTexte(20, "Ex: 0613121312");
        champCourriel = creerChampTexte(28, "Ex: prenom.nom@exemple.fr");
        zoneAdresseDestinataire = creerZoneTexte(4, 32, "Ex: Service destinataire\nAdresse complète");
        ascenseurAdresseDestinataire = creerAscenseurZoneTexte(zoneAdresseDestinataire, 108);
        caseChangementPrenom = creerCase("Le changement de prénom est effectif");
        champPrenomNaissance = creerChampTexte(24, "Ex: Tom, Noé, Max");
        caseChangementSexe = creerCase("Le changement de sexe à l'état civil est effectif");
        comboSexeAvant = creerCombo("Masculin", "Féminin");
        comboSexeApres = creerCombo("Féminin", "Masculin");
        caseDecisionMairie = creerCase("Le changement de prénom a été acté en mairie");
        champNumeroDecisionMairie = creerChampTexte(20, "Ex: 1234");
        selecteurDateDecisionMairie = creerSelecteurDate();
        champTribunalCompetent = creerChampTexte(30, "Ex: Tribunal judiciaire de Paris");
        champNumeroJugement = creerChampTexte(20, "Ex: RG 24/01234");
        champVilleActuelle = creerChampTexte(20, "Ex: Paris");

        ajouterTitreSection("Identité et coordonnées");
        ajouterLigneChamp("Prénom d'usage", champPrenomUsage);
        ajouterLigneChamp("Prénoms à l'état civil", champPrenomsEtatCivil);
        ajouterLigneChamp("Nom", champNom);
        ajouterLigneChamp("Adresse postale", ascenseurAdressePostale, ascenseurAdressePostale);
        ajouterLigneChamp("Téléphone portable", champTelephonePortable);
        ajouterLigneChamp("Courriel", champCourriel);
        ajouterLigneChamp("Adresse destinataire", ascenseurAdresseDestinataire, ascenseurAdresseDestinataire);

        ajouterTitreSection("Modifications d'état civil");
        ajouterLignePleine(caseChangementPrenom);
        ajouterLigneChamp("Prénoms de naissance", champPrenomNaissance);
        ajouterLignePleine(caseChangementSexe);
        ajouterLigneChamp("Sexe avant modification", comboSexeAvant);
        ajouterLigneChamp("Sexe après modification", comboSexeApres);
        ajouterLignePleine(caseDecisionMairie);
        ajouterLigneChamp("Numéro décision mairie", champNumeroDecisionMairie);
        ajouterLigneChamp("Date décision mairie", selecteurDateDecisionMairie, selecteurDateDecisionMairie);
        ajouterLigneChamp("Tribunal compétent", champTribunalCompetent);
        ajouterLigneChamp("Numéro de jugement", champNumeroJugement);
        ajouterLigneChamp("Ville actuelle", champVilleActuelle);

        caseChangementPrenom.addActionListener(e -> mettreAJourEtatOptions());
        caseChangementSexe.addActionListener(e -> mettreAJourEtatOptions());
        caseDecisionMairie.addActionListener(e -> mettreAJourEtatOptions());
        comboSexeAvant.addActionListener(e -> mettreAJourExemplesPrenoms());
        comboSexeApres.addActionListener(e -> mettreAJourExemplesPrenoms());
        mettreAJourEtatOptions();
        appliquerInstantane(instantaneInitial);

        terminerConstruction();
        SwingUtilities.invokeLater(champPrenomUsage::requestFocusInWindow);
    }

    @Override
    protected boolean validerFormulaire() {
        boolean valide = true;
        if (!formatPrenomValide(texte(champPrenomUsage))) {
            signalerErreur(champPrenomUsage, "Le prénom d'usage est obligatoire et doit avoir un format valide.");
            valide = false;
        }
        if (!formatNomValide(texte(champNom))) {
            signalerErreur(champNom, "Le nom est obligatoire et doit avoir un format valide.");
            valide = false;
        }
        String prenomsEtatCivil = texte(champPrenomsEtatCivil);
        if (!prenomsEtatCivil.isBlank() && !formatPrenomsMultiplesValide(prenomsEtatCivil)) {
            signalerErreur(champPrenomsEtatCivil, "Les prénoms à l'état civil ont un format invalide.");
            valide = false;
        }
        if (!texteMultiligneValide(texte(zoneAdressePostale), 8, 400)) {
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
        if (!texteMultiligneValide(texte(zoneAdresseDestinataire), 8, 500)) {
            signalerErreur(ascenseurAdresseDestinataire, "L'adresse destinataire est obligatoire et doit être suffisamment précise.");
            valide = false;
        }
        if (!formatVilleValide(texte(champVilleActuelle))) {
            signalerErreur(champVilleActuelle, "La ville actuelle est obligatoire et doit avoir un format valide.");
            valide = false;
        }
        if (!caseChangementPrenom.isSelected() && !caseChangementSexe.isSelected()) {
            signalerErreurGlobale("Au moins une modification doit être effective : prénom ou mention de sexe à l'état civil.");
            valide = false;
        }
        boolean uniquementPrenom = caseChangementPrenom.isSelected() && !caseChangementSexe.isSelected();
        if (caseChangementPrenom.isSelected()) {
            if (!formatPrenomsMultiplesValide(texte(champPrenomNaissance))) {
                signalerErreur(champPrenomNaissance, "Les prénoms de naissance sont obligatoires et doivent avoir un format valide.");
                valide = false;
            }
            if (!formatPrenomsMultiplesValide(texte(champPrenomsEtatCivil))) {
                signalerErreur(champPrenomsEtatCivil, "Les prénoms à l'état civil sont obligatoires si le changement de prénom est effectif.");
                valide = false;
            }
        }
        if (uniquementPrenom && !caseDecisionMairie.isSelected()) {
            signalerErreurGlobale("Si seul le changement de prénom est effectif, la décision de mairie doit être renseignée.");
            valide = false;
        }
        if (caseChangementSexe.isSelected()) {
            if (!texteSimpleValide(texte(champTribunalCompetent), 5, 140)) {
                signalerErreur(champTribunalCompetent, "Le tribunal compétent est obligatoire si le changement de sexe est effectif.");
                valide = false;
            }
            if (!formatReferenceValide(texte(champNumeroJugement))) {
                signalerErreur(champNumeroJugement, "Le numéro de jugement est obligatoire si le changement de sexe est effectif.");
                valide = false;
            }
            if (texteSelection(comboSexeAvant).equalsIgnoreCase(texteSelection(comboSexeApres))) {
                signalerErreur(comboSexeApres, "Le sexe avant et le sexe après modification doivent être différents.");
                valide = false;
            }
        }
        if (caseDecisionMairie.isSelected() || uniquementPrenom) {
            if (!formatReferenceValide(texte(champNumeroDecisionMairie))) {
                signalerErreur(champNumeroDecisionMairie, "Le numéro de décision de mairie est obligatoire.");
                valide = false;
            }
            if (!selecteurDateDecisionMairie.estDateValide()) {
                signalerErreur(selecteurDateDecisionMairie, "La date de décision de mairie est invalide.");
                valide = false;
            }
        }
        return valide;
    }

    @Override
    protected String titreChoixDestination() {
        return "Enregistrer la lettre administration";
    }

    @Override
    protected String nomFichierParDefaut() {
        return "lettre_mise_a_jour_administration.docx";
    }

    @Override
    protected void genererDocument(Path destination, boolean ecraser) throws ErreurExportDocument {
        serviceGenerationLettreAdministration.exporter(construireDonnees(), destination, ecraser);
    }

    @Override
    protected String messageSuccesGeneration(Path destination) {
        return "La lettre a été générée : " + destination.toAbsolutePath();
    }

    private DonneesLettreAdministration construireDonnees() {
        return new DonneesLettreAdministration(texte(champPrenomUsage), texte(champPrenomsEtatCivil), texte(champNom), texte(zoneAdressePostale), texte(champTelephonePortable), texte(champCourriel), texte(zoneAdresseDestinataire), caseChangementPrenom.isSelected(), texte(champPrenomNaissance), caseChangementSexe.isSelected(), texteSelection(comboSexeAvant), texteSelection(comboSexeApres), caseDecisionMairie.isSelected(), texte(champNumeroDecisionMairie), selecteurDateDecisionMairie.texteDate(), texte(champTribunalCompetent), texte(champNumeroJugement), texte(champVilleActuelle));
    }

    private void mettreAJourEtatOptions() {
        boolean prenomActif = caseChangementPrenom.isSelected();
        boolean sexeActif = caseChangementSexe.isSelected();
        boolean mairieActive = prenomActif && caseDecisionMairie.isSelected();

        champPrenomNaissance.setEnabled(prenomActif);
        caseDecisionMairie.setEnabled(prenomActif);
        if (!prenomActif) {
            caseDecisionMairie.setSelected(false);
        }
        champNumeroDecisionMairie.setEnabled(mairieActive);
        selecteurDateDecisionMairie.setEnabled(mairieActive);

        comboSexeAvant.setEnabled(sexeActif);
        comboSexeApres.setEnabled(sexeActif);
        champTribunalCompetent.setEnabled(sexeActif);
        champNumeroJugement.setEnabled(sexeActif);
        mettreAJourExemplesPrenoms();
    }

    InstantaneLettreAdministration instantane() {
        return new InstantaneLettreAdministration(texte(champPrenomUsage), texte(champPrenomsEtatCivil), texte(champNom), texte(zoneAdressePostale), texte(champTelephonePortable), texte(champCourriel), texte(zoneAdresseDestinataire), caseChangementPrenom.isSelected(), texte(champPrenomNaissance), caseChangementSexe.isSelected(), texteSelection(comboSexeAvant), texteSelection(comboSexeApres), caseDecisionMairie.isSelected(), texte(champNumeroDecisionMairie), selecteurDateDecisionMairie.texteDate(), texte(champTribunalCompetent), texte(champNumeroJugement), texte(champVilleActuelle));
    }

    private void appliquerInstantane(InstantaneLettreAdministration instantane) {
        InstantaneLettreAdministration valeur = instantane == null ? InstantaneLettreAdministration.vide() : instantane;
        champPrenomUsage.setText(valeur.prenomUsage());
        champPrenomsEtatCivil.setText(valeur.prenomsEtatCivil());
        champNom.setText(valeur.nom());
        zoneAdressePostale.setText(valeur.adressePostale());
        champTelephonePortable.setText(valeur.telephonePortable());
        champCourriel.setText(valeur.courriel());
        zoneAdresseDestinataire.setText(valeur.adresseDestinataire());
        caseChangementPrenom.setSelected(valeur.changementPrenom());
        champPrenomNaissance.setText(valeur.prenomNaissance());
        caseChangementSexe.setSelected(valeur.changementSexe());
        if (!valeur.sexeAvant().isBlank()) {
            comboSexeAvant.setSelectedItem(valeur.sexeAvant());
        }
        if (!valeur.sexeApres().isBlank()) {
            comboSexeApres.setSelectedItem(valeur.sexeApres());
        }
        caseDecisionMairie.setSelected(valeur.changementPrenomFaitEnMairie());
        champNumeroDecisionMairie.setText(valeur.numeroDecisionMairie());
        selecteurDateDecisionMairie.appliquerDateTexte(valeur.dateDecisionMairie());
        champTribunalCompetent.setText(valeur.tribunalCompetent());
        champNumeroJugement.setText(valeur.numeroJugement());
        champVilleActuelle.setText(valeur.villeActuelle());
        mettreAJourEtatOptions();
    }

    private void mettreAJourExemplesPrenoms() {
        String genreEtatCivil = normaliserGenre(texteSelection(comboSexeAvant));
        String genreUsage = caseChangementSexe.isSelected() ? normaliserGenre(texteSelection(comboSexeApres)) : genreOppose(genreEtatCivil);
        champPrenomUsage.setExemple(exemplePrenomSimple(genreUsage));
        champPrenomsEtatCivil.setExemple(exemplePrenoms(genreEtatCivil));
        champPrenomNaissance.setExemple(exemplePrenoms(genreEtatCivil));
    }

    private String normaliserGenre(String valeur) {
        return valeur != null && valeur.toLowerCase(Locale.ROOT).startsWith("f") ? "féminin" : "masculin";
    }

    private String genreOppose(String genre) {
        return "féminin".equals(genre) ? "masculin" : "féminin";
    }
}
