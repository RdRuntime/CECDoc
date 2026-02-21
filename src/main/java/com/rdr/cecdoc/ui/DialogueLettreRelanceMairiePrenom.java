package com.rdr.cecdoc.ui;

import com.rdr.cecdoc.model.DonneesLettreRelanceMairiePrenom;
import com.rdr.cecdoc.model.InstantaneLettreRelanceMairiePrenom;
import com.rdr.cecdoc.service.export.EcritureDocxAtomique;
import com.rdr.cecdoc.service.export.ErreurExportDocument;
import com.rdr.cecdoc.service.export.ServiceGenerationLettreRelanceMairiePrenom;
import com.rdr.cecdoc.ui.theme.TokensTheme;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

final class DialogueLettreRelanceMairiePrenom extends DialogueFormulaireDocumentAbstrait {
    private final transient ServiceGenerationLettreRelanceMairiePrenom serviceGenerationLettreRelanceMairiePrenom;

    private final ChampTexteExempleDialogue champPrenomsEtatCivil;
    private final ChampTexteExempleDialogue champPrenomsDemandes;
    private final ChampTexteExempleDialogue champNom;
    private final ZoneTexteExempleDialogue zoneAdressePostale;
    private final JScrollPane ascenseurAdressePostale;
    private final ChampTexteExempleDialogue champTelephonePortable;
    private final ChampTexteExempleDialogue champCourriel;

    private final ZoneTexteExempleDialogue zoneAdresseMairie;
    private final JScrollPane ascenseurAdresseMairie;
    private final ChampTexteExempleDialogue champVilleRedaction;
    private final SelecteurDateDialogue selecteurDateDemande;

    private final JComboBox<String> comboGenreActuel;
    private final SelecteurDateDialogue selecteurDateNaissance;
    private final ChampTexteExempleDialogue champLieuNaissance;
    private final ChampTexteExempleDialogue champReferenceDossier;

    DialogueLettreRelanceMairiePrenom(JFrame proprietaire, TokensTheme theme, List<Image> iconesApplication, InstantaneLettreRelanceMairiePrenom instantaneInitial, Path dossierSortieParDefaut) {
        super(proprietaire, theme, iconesApplication, dossierSortieParDefaut, "Relance mairie changement de prénoms", "Lettre de relance / suivi à la mairie pour un changement de prénoms", "Générer la lettre");
        this.serviceGenerationLettreRelanceMairiePrenom = new ServiceGenerationLettreRelanceMairiePrenom(new EcritureDocxAtomique());

        champPrenomsEtatCivil = creerChampTexte(24, "Ex: Tom, Noé, Max");
        champPrenomsDemandes = creerChampTexte(24, "Ex: Alice, Emma, Agathe");
        champNom = creerChampTexte(24, "Ex: Dupont");
        zoneAdressePostale = creerZoneTexte(3, 32, "Ex: 12 rue de la République\n75000 Paris");
        ascenseurAdressePostale = creerAscenseurZoneTexte(zoneAdressePostale, 92);
        champTelephonePortable = creerChampTexte(20, "Ex: 0613121312");
        champCourriel = creerChampTexte(28, "Ex: prenom.nom@exemple.fr");

        zoneAdresseMairie = creerZoneTexte(3, 32, "Ex: Mairie de Paris\nPlace de l'Hôtel de Ville\n75004 Paris");
        ascenseurAdresseMairie = creerAscenseurZoneTexte(zoneAdresseMairie, 92);
        champVilleRedaction = creerChampTexte(24, "Ex: Paris");
        selecteurDateDemande = creerSelecteurDate();

        comboGenreActuel = creerCombo("Masculin", "Féminin", "Non-binaire");
        selecteurDateNaissance = creerSelecteurDate();
        champLieuNaissance = creerChampTexte(24, "Ex: Paris");
        champReferenceDossier = creerChampTexte(24, "Ex: Dossier 2026-001");

        ajouterTitreSection("Identité et contact");
        ajouterLigneChamp("Prénoms à l'état civil", champPrenomsEtatCivil);
        ajouterLigneChamp("Prénoms demandés", champPrenomsDemandes);
        ajouterLigneChamp("Nom", champNom);
        ajouterLigneChamp("Adresse postale", ascenseurAdressePostale, ascenseurAdressePostale);
        ajouterLigneChamp("Téléphone portable", champTelephonePortable);
        ajouterLigneChamp("Courriel", champCourriel);

        ajouterTitreSection("Mairie et suivi");
        ajouterLigneChamp("Adresse de la mairie", ascenseurAdresseMairie, ascenseurAdresseMairie);
        ajouterLigneChamp("Ville de rédaction", champVilleRedaction);
        ajouterLigneChamp("Date de dépôt/envoi de la demande", selecteurDateDemande, selecteurDateDemande);

        ajouterTitreSection("Éléments d’identification");
        ajouterLigneChamp("Genre actuel (choisi)", comboGenreActuel);
        ajouterLigneChamp("Date de naissance", selecteurDateNaissance, selecteurDateNaissance);
        ajouterLigneChamp("Lieu de naissance", champLieuNaissance);
        ajouterLigneChamp("Référence de dossier", champReferenceDossier);

        comboGenreActuel.addActionListener(e -> mettreAJourExemplesSelonGenre());

        appliquerInstantane(instantaneInitial);
        mettreAJourExemplesSelonGenre();
        configurerAccessibiliteComposants();

        terminerConstruction();
        SwingUtilities.invokeLater(champPrenomsDemandes::requestFocusInWindow);
    }

    @Override
    protected boolean validerFormulaire() {
        boolean valide = true;
        if (!formatPrenomValide(texte(champPrenomsEtatCivil))) {
            signalerErreur(champPrenomsEtatCivil, "Les prénoms à l'état civil sont obligatoires et doivent avoir un format valide.");
            valide = false;
        }
        if (!formatPrenomValide(texte(champPrenomsDemandes))) {
            signalerErreur(champPrenomsDemandes, "Les prénoms demandés sont obligatoires et doivent avoir un format valide.");
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
        if (!texteMultiligneValide(texte(zoneAdresseMairie), 8, 420)) {
            signalerErreur(ascenseurAdresseMairie, "L'adresse de la mairie est obligatoire et doit être suffisamment précise.");
            valide = false;
        }
        if (!formatVilleValide(texte(champVilleRedaction))) {
            signalerErreur(champVilleRedaction, "La ville de rédaction est obligatoire et doit avoir un format valide.");
            valide = false;
        }
        if (!selecteurDateDemande.estDateValide()) {
            signalerErreur(selecteurDateDemande, "La date de dépôt/envoi de la demande est invalide ou future.");
            valide = false;
        }
        if (texteSelection(comboGenreActuel).isBlank()) {
            signalerErreur(comboGenreActuel, "Le genre actuel est obligatoire.");
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
        if (!texte(champReferenceDossier).isBlank() && !formatReferenceValide(texte(champReferenceDossier))) {
            signalerErreur(champReferenceDossier, "La référence de dossier n'a pas un format valide.");
            valide = false;
        }
        return valide;
    }

    @Override
    protected String titreChoixDestination() {
        return "Enregistrer la relance mairie";
    }

    @Override
    protected String nomFichierParDefaut() {
        return "lettre_relance_mairie_prenoms.docx";
    }

    @Override
    protected void genererDocument(Path destination, boolean ecraser) throws ErreurExportDocument {
        serviceGenerationLettreRelanceMairiePrenom.exporter(construireDonnees(), destination, ecraser);
    }

    @Override
    protected String messageSuccesGeneration(Path destination) {
        return "La lettre de relance a été générée : " + destination.toAbsolutePath();
    }

    InstantaneLettreRelanceMairiePrenom instantane() {
        return new InstantaneLettreRelanceMairiePrenom(texte(champPrenomsEtatCivil), texte(champPrenomsDemandes), texte(champNom), texte(zoneAdressePostale), texte(champTelephonePortable), texte(champCourriel), texte(zoneAdresseMairie), texte(champVilleRedaction), dateDuJourTexte(), selecteurDateDemande.texteDate(), texteSelection(comboGenreActuel), selecteurDateNaissance.texteDate(), texte(champLieuNaissance), texte(champReferenceDossier));
    }

    private DonneesLettreRelanceMairiePrenom construireDonnees() {
        InstantaneLettreRelanceMairiePrenom valeur = instantane();
        return new DonneesLettreRelanceMairiePrenom(valeur.prenomsEtatCivil(), valeur.prenomsDemandes(), valeur.nom(), valeur.adressePostale(), valeur.telephonePortable(), valeur.courriel(), valeur.adresseMairie(), valeur.villeRedaction(), valeur.dateRedaction(), valeur.dateDemande(), valeur.genreActuel(), valeur.dateNaissance(), valeur.lieuNaissance(), valeur.referenceDossier());
    }

    private void appliquerInstantane(InstantaneLettreRelanceMairiePrenom instantane) {
        InstantaneLettreRelanceMairiePrenom valeur = instantane == null ? InstantaneLettreRelanceMairiePrenom.vide() : instantane;
        champPrenomsEtatCivil.setText(valeur.prenomsEtatCivil());
        champPrenomsDemandes.setText(valeur.prenomsDemandes());
        champNom.setText(valeur.nom());
        zoneAdressePostale.setText(valeur.adressePostale());
        champTelephonePortable.setText(valeur.telephonePortable());
        champCourriel.setText(valeur.courriel());
        zoneAdresseMairie.setText(valeur.adresseMairie());
        champVilleRedaction.setText(valeur.villeRedaction());
        selecteurDateDemande.appliquerDateTexte(valeur.dateDemande());
        if (!valeur.genreActuel().isBlank()) {
            comboGenreActuel.setSelectedItem(valeur.genreActuel());
        }
        selecteurDateNaissance.appliquerDateTexte(valeur.dateNaissance());
        champLieuNaissance.setText(valeur.lieuNaissance());
        champReferenceDossier.setText(valeur.referenceDossier());
    }

    private void mettreAJourExemplesSelonGenre() {
        String genre = normaliserGenre(texteSelection(comboGenreActuel));
        champPrenomsDemandes.setExemple(exemplePrenoms(genre));
        champPrenomsEtatCivil.setExemple(exemplePrenomsEtatCivilDepuisGenre(genre));
        champNom.setExemple("Ex: Dupont");
    }

    private String exemplePrenomsEtatCivilDepuisGenre(String genre) {
        if ("féminin".equals(genre)) {
            return "Ex: Tom, Noé, Max";
        }
        if ("masculin".equals(genre)) {
            return "Ex: Alice, Emma, Agathe";
        }
        return "Ex: Tom, Noé, Max";
    }

    private void configurerAccessibiliteComposants() {
        configurerAccessibilite(champPrenomsEtatCivil, "Prénoms à l'état civil", "Saisir les prénoms à l'état civil");
        configurerAccessibilite(champPrenomsDemandes, "Prénoms demandés", "Saisir les prénoms demandés");
        configurerAccessibilite(champNom, "Nom", "Saisir le nom de famille");
        configurerAccessibilite(zoneAdressePostale, "Adresse postale", "Saisir l'adresse postale complète");
        configurerAccessibilite(ascenseurAdressePostale, "Adresse postale", "Zone multi-lignes pour l'adresse postale");
        configurerAccessibilite(champTelephonePortable, "Téléphone portable", "Saisir le numéro de téléphone portable");
        configurerAccessibilite(champCourriel, "Courriel", "Saisir l'adresse courriel");
        configurerAccessibilite(zoneAdresseMairie, "Adresse de la mairie", "Saisir l'adresse de la mairie destinataire");
        configurerAccessibilite(ascenseurAdresseMairie, "Adresse de la mairie", "Zone multi-lignes pour l'adresse de la mairie");
        configurerAccessibilite(champVilleRedaction, "Ville de rédaction", "Saisir la ville de rédaction");
        configurerAccessibilite(selecteurDateDemande, "Date de dépôt ou d'envoi", "Sélecteur jour mois année de la date de dépôt ou d'envoi");
        configurerAccessibilite(comboGenreActuel, "Genre actuel choisi", "Sélectionner le genre actuel choisi pour les accords de la lettre");
        configurerAccessibilite(selecteurDateNaissance, "Date de naissance", "Sélecteur jour mois année de la date de naissance");
        configurerAccessibilite(champLieuNaissance, "Lieu de naissance", "Saisir le lieu de naissance");
        configurerAccessibilite(champReferenceDossier, "Référence de dossier", "Saisir la référence du dossier si elle est connue");
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
