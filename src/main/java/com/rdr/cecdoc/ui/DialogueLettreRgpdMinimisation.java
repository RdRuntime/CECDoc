package com.rdr.cecdoc.ui;

import com.rdr.cecdoc.model.DonneesLettreRgpdMinimisation;
import com.rdr.cecdoc.model.InstantaneLettreRgpdMinimisation;
import com.rdr.cecdoc.service.export.EcritureDocxAtomique;
import com.rdr.cecdoc.service.export.ErreurExportDocument;
import com.rdr.cecdoc.service.export.ServiceGenerationLettreRgpdMinimisation;
import com.rdr.cecdoc.ui.theme.TokensTheme;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

final class DialogueLettreRgpdMinimisation extends DialogueFormulaireDocumentAbstrait {
    private final transient ServiceGenerationLettreRgpdMinimisation serviceGenerationLettreRgpdMinimisation;

    private final ChampTexteExempleDialogue champPrenomsEtatCivil;
    private final ChampTexteExempleDialogue champPrenomsConnusOrganisme;
    private final ChampTexteExempleDialogue champNom;
    private final ZoneTexteExempleDialogue zoneAdressePostale;
    private final JScrollPane ascenseurAdressePostale;
    private final ChampTexteExempleDialogue champTelephonePortable;
    private final ChampTexteExempleDialogue champCourriel;

    private final ZoneTexteExempleDialogue zoneNomAdresseOrganisme;
    private final JScrollPane ascenseurNomAdresseOrganisme;
    private final ChampTexteExempleDialogue champVilleRedaction;

    private final JComboBox<String> comboGenreDemande;
    private final SelecteurDateDialogue selecteurDateNaissance;
    private final ChampTexteExempleDialogue champLieuNaissance;
    private final JComboBox<String> comboSexeEtatCivil;
    private final JComboBox<String> comboCiviliteAffichage;
    private final JCheckBox caseChampsCiviliteGenrePresents;

    DialogueLettreRgpdMinimisation(JFrame proprietaire, TokensTheme theme, List<Image> iconesApplication, InstantaneLettreRgpdMinimisation instantaneInitial, Path dossierSortieParDefaut) {
        super(proprietaire, theme, iconesApplication, dossierSortieParDefaut, "Lettre RGPD / minimisation", "Lettre « RGPD / minimisation » aux organismes", "Générer la lettre");
        this.serviceGenerationLettreRgpdMinimisation = new ServiceGenerationLettreRgpdMinimisation(new EcritureDocxAtomique());

        champPrenomsEtatCivil = creerChampTexte(22, "Ex: Tom, Noé, Max");
        champPrenomsConnusOrganisme = creerChampTexte(22, "Ex: Tom, Noé, Max");
        champNom = creerChampTexte(24, "Ex: Dupont");
        zoneAdressePostale = creerZoneTexte(3, 32, "Ex: 12 rue de la République\n75000 Paris");
        ascenseurAdressePostale = creerAscenseurZoneTexte(zoneAdressePostale, 92);
        champTelephonePortable = creerChampTexte(20, "Ex: 0613121312");
        champCourriel = creerChampTexte(28, "Ex: prenom.nom@exemple.fr");

        zoneNomAdresseOrganisme = creerZoneTexte(4, 32, "Ex: Organisme concerné\nService conformité\nAdresse complète");
        ascenseurNomAdresseOrganisme = creerAscenseurZoneTexte(zoneNomAdresseOrganisme, 108);
        champVilleRedaction = creerChampTexte(24, "Ex: Paris");

        comboGenreDemande = creerCombo("Masculin", "Féminin", "Non-binaire");
        selecteurDateNaissance = creerSelecteurDate();
        champLieuNaissance = creerChampTexte(24, "Ex: Paris");
        comboSexeEtatCivil = creerCombo("Masculin", "Féminin");
        comboCiviliteAffichage = creerCombo("Masculin", "Féminin", "Pas de civilité");
        caseChampsCiviliteGenrePresents = creerCase("Les champs civilité / genre / mention d’affichage sont présents dans les interfaces");

        ajouterTitreSection("Identité et contact");
        ajouterLigneChamp("Prénoms", champPrenomsEtatCivil);
        ajouterLigneChamp("Prénoms connus de l'organisme", champPrenomsConnusOrganisme);
        ajouterLigneChamp("Nom", champNom);
        ajouterLigneChamp("Adresse postale", ascenseurAdressePostale, ascenseurAdressePostale);
        ajouterLigneChamp("Téléphone portable", champTelephonePortable);
        ajouterLigneChamp("Courriel", champCourriel);

        ajouterTitreSection("Organisme destinataire");
        ajouterLigneChamp("Nom et adresse de l’organisme", ascenseurNomAdresseOrganisme, ascenseurNomAdresseOrganisme);
        ajouterLigneChamp("Ville de rédaction", champVilleRedaction);

        ajouterTitreSection("Paramètres de la demande");
        ajouterLigneChamp("Genre (accords)", comboGenreDemande);
        ajouterLigneChamp("Date de naissance", selecteurDateNaissance, selecteurDateNaissance);
        ajouterLigneChamp("Lieu de naissance", champLieuNaissance);
        ajouterLigneChamp("Sexe à l'état civil", comboSexeEtatCivil);
        ajouterLigneChamp("Civilité / mention d'affichage", comboCiviliteAffichage);
        ajouterLignePleine(caseChampsCiviliteGenrePresents);

        comboGenreDemande.addActionListener(e -> mettreAJourExemplesSelonGenre());

        appliquerInstantane(instantaneInitial);
        mettreAJourExemplesSelonGenre();
        configurerAccessibiliteComposants();

        terminerConstruction();
        SwingUtilities.invokeLater(champPrenomsEtatCivil::requestFocusInWindow);
    }

    @Override
    protected boolean validerFormulaire() {
        boolean valide = true;
        if (!formatPrenomValide(texte(champPrenomsEtatCivil))) {
            signalerErreur(champPrenomsEtatCivil, "Les prénoms à l'état civil sont obligatoires et doivent avoir un format valide.");
            valide = false;
        }
        String prenomsConnus = texte(champPrenomsConnusOrganisme);
        if (!prenomsConnus.isBlank() && !formatPrenomValide(prenomsConnus)) {
            signalerErreur(champPrenomsConnusOrganisme, "Les prénoms connus de l'organisme doivent avoir un format valide.");
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
        if (!texteMultiligneValide(texte(zoneNomAdresseOrganisme), 8, 800)) {
            signalerErreur(ascenseurNomAdresseOrganisme, "Le nom et l'adresse de l'organisme sont obligatoires.");
            valide = false;
        }
        if (!formatVilleValide(texte(champVilleRedaction))) {
            signalerErreur(champVilleRedaction, "La ville de rédaction est obligatoire et doit avoir un format valide.");
            valide = false;
        }
        if (texteSelection(comboGenreDemande).isBlank()) {
            signalerErreur(comboGenreDemande, "Le genre pour les accords est obligatoire.");
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
        if (texteSelection(comboSexeEtatCivil).isBlank()) {
            signalerErreur(comboSexeEtatCivil, "Le sexe à l'état civil est obligatoire.");
            valide = false;
        }
        if (texteSelection(comboCiviliteAffichage).isBlank()) {
            signalerErreur(comboCiviliteAffichage, "La civilité ou mention d'affichage est obligatoire.");
            valide = false;
        }
        return valide;
    }

    @Override
    protected String titreChoixDestination() {
        return "Enregistrer la lettre RGPD";
    }

    @Override
    protected String nomFichierParDefaut() {
        return "lettre_rgpd_minimisation.docx";
    }

    @Override
    protected void genererDocument(Path destination, boolean ecraser) throws ErreurExportDocument {
        serviceGenerationLettreRgpdMinimisation.exporter(construireDonnees(), destination, ecraser);
    }

    @Override
    protected String messageSuccesGeneration(Path destination) {
        return "La lettre RGPD a été générée : " + destination.toAbsolutePath();
    }

    InstantaneLettreRgpdMinimisation instantane() {
        return new InstantaneLettreRgpdMinimisation(texte(champPrenomsEtatCivil), texte(champPrenomsConnusOrganisme), texte(champNom), texte(zoneAdressePostale), texte(champTelephonePortable), texte(champCourriel), texte(zoneNomAdresseOrganisme), texte(champVilleRedaction), dateDuJourTexte(), texteSelection(comboGenreDemande), selecteurDateNaissance.texteDate(), texte(champLieuNaissance), texteSelection(comboSexeEtatCivil), texteSelection(comboCiviliteAffichage), caseChampsCiviliteGenrePresents.isSelected());
    }

    private DonneesLettreRgpdMinimisation construireDonnees() {
        InstantaneLettreRgpdMinimisation valeur = instantane();
        return new DonneesLettreRgpdMinimisation(valeur.prenomsEtatCivil(), valeur.prenomsConnusOrganisme(), valeur.nom(), valeur.adressePostale(), valeur.telephonePortable(), valeur.courriel(), valeur.nomAdresseOrganisme(), valeur.villeRedaction(), valeur.dateRedaction(), valeur.genreDemande(), valeur.dateNaissance(), valeur.lieuNaissance(), valeur.sexeEtatCivil(), valeur.civiliteAffichage(), valeur.champsCiviliteGenrePresents());
    }

    private void appliquerInstantane(InstantaneLettreRgpdMinimisation instantane) {
        InstantaneLettreRgpdMinimisation valeur = instantane == null ? InstantaneLettreRgpdMinimisation.vide() : instantane;
        champPrenomsEtatCivil.setText(valeur.prenomsEtatCivil());
        champPrenomsConnusOrganisme.setText(valeur.prenomsConnusOrganisme());
        champNom.setText(valeur.nom());
        zoneAdressePostale.setText(valeur.adressePostale());
        champTelephonePortable.setText(valeur.telephonePortable());
        champCourriel.setText(valeur.courriel());
        zoneNomAdresseOrganisme.setText(valeur.nomAdresseOrganisme());
        champVilleRedaction.setText(valeur.villeRedaction());
        if (!valeur.genreDemande().isBlank()) {
            comboGenreDemande.setSelectedItem(valeur.genreDemande());
        }
        selecteurDateNaissance.appliquerDateTexte(valeur.dateNaissance());
        champLieuNaissance.setText(valeur.lieuNaissance());
        if (!valeur.sexeEtatCivil().isBlank()) {
            comboSexeEtatCivil.setSelectedItem(valeur.sexeEtatCivil());
        }
        if (!valeur.civiliteAffichage().isBlank()) {
            comboCiviliteAffichage.setSelectedItem(valeur.civiliteAffichage());
        }
        caseChampsCiviliteGenrePresents.setSelected(valeur.champsCiviliteGenrePresents());
    }

    private void mettreAJourExemplesSelonGenre() {
        String genre = normaliserGenre(texteSelection(comboGenreDemande));
        champPrenomsEtatCivil.setExemple(exemplePrenoms(genre));
        champPrenomsConnusOrganisme.setExemple(exemplePrenoms(genre));
        champNom.setExemple("Ex: Dupont");
    }

    private void configurerAccessibiliteComposants() {
        configurerAccessibilite(champPrenomsEtatCivil, "Prénoms à l'état civil", "Saisir les prénoms actuels à l'état civil");
        configurerAccessibilite(champPrenomsConnusOrganisme, "Prénoms connus de l'organisme", "Saisir les prénoms figurant dans les fichiers de l'organisme si différents");
        configurerAccessibilite(champNom, "Nom", "Saisir le nom de famille");
        configurerAccessibilite(zoneAdressePostale, "Adresse postale", "Saisir l'adresse postale complète");
        configurerAccessibilite(ascenseurAdressePostale, "Adresse postale", "Zone multi-lignes pour l'adresse postale");
        configurerAccessibilite(champTelephonePortable, "Téléphone portable", "Saisir le numéro de téléphone portable");
        configurerAccessibilite(champCourriel, "Courriel", "Saisir l'adresse courriel");
        configurerAccessibilite(zoneNomAdresseOrganisme, "Nom et adresse de l'organisme", "Saisir l'organisme destinataire et son adresse complète");
        configurerAccessibilite(ascenseurNomAdresseOrganisme, "Nom et adresse de l'organisme", "Zone multi-lignes pour le destinataire");
        configurerAccessibilite(champVilleRedaction, "Ville de rédaction", "Saisir la ville de rédaction");
        configurerAccessibilite(comboGenreDemande, "Genre pour les accords", "Sélectionner le genre grammatical utilisé dans la lettre");
        configurerAccessibilite(selecteurDateNaissance, "Date de naissance", "Sélecteur jour mois année de la date de naissance");
        configurerAccessibilite(champLieuNaissance, "Lieu de naissance", "Saisir le lieu de naissance");
        configurerAccessibilite(comboSexeEtatCivil, "Sexe à l'état civil", "Sélectionner la mention de sexe à l'état civil");
        configurerAccessibilite(comboCiviliteAffichage, "Civilité et mention d'affichage", "Sélectionner la civilité ou mention d'affichage demandée");
        configurerAccessibilite(caseChampsCiviliteGenrePresents, "Présence des champs civilité et genre", "Indique si ces champs sont présents dans les interfaces de l'organisme");
    }

    private void configurerAccessibilite(JComponent composant, String nom, String description) {
        if (composant == null) {
            return;
        }
        composant.getAccessibleContext().setAccessibleName(nom);
        composant.getAccessibleContext().setAccessibleDescription(description);
        composant.setToolTipText(description);
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
}
