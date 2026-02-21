package com.rdr.cecdoc.ui;

import com.rdr.cecdoc.model.DonneesLettreRelanceTribunal;
import com.rdr.cecdoc.model.InstantaneLettreRelanceTribunal;
import com.rdr.cecdoc.service.export.EcritureDocxAtomique;
import com.rdr.cecdoc.service.export.ErreurExportDocument;
import com.rdr.cecdoc.service.export.ServiceGenerationLettreRelanceTribunal;
import com.rdr.cecdoc.ui.theme.TokensTheme;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

final class DialogueLettreRelanceTribunal extends DialogueFormulaireDocumentAbstrait {
    private final transient ServiceGenerationLettreRelanceTribunal serviceGenerationLettreRelanceTribunal;

    private final ChampTexteExempleDialogue champPrenom;
    private final ChampTexteExempleDialogue champNom;
    private final ZoneTexteExempleDialogue zoneAdressePostale;
    private final JScrollPane ascenseurAdressePostale;
    private final ChampTexteExempleDialogue champTelephonePortable;
    private final ChampTexteExempleDialogue champCourriel;

    private final ZoneTexteExempleDialogue zoneAdresseTribunal;
    private final JScrollPane ascenseurAdresseTribunal;
    private final ChampTexteExempleDialogue champVilleRedaction;
    private final SelecteurDateDialogue selecteurDateDepotEnvoi;
    private final JComboBox<String> comboInformationAttendue;

    private final JComboBox<String> comboGenreRevendique;
    private final SelecteurDateDialogue selecteurDateNaissance;
    private final ChampTexteExempleDialogue champLieuNaissance;
    private final ChampTexteExempleDialogue champReferenceDossier;
    private final JCheckBox caseChangementPrenoms;
    private final ChampTexteExempleDialogue champPrenomsEtatCivil;

    DialogueLettreRelanceTribunal(JFrame proprietaire, TokensTheme theme, List<Image> iconesApplication, InstantaneLettreRelanceTribunal instantaneInitial, Path dossierSortieParDefaut) {
        super(proprietaire, theme, iconesApplication, dossierSortieParDefaut, "Relance tribunal judiciaire", "Lettre de relance / suivi au tribunal judiciaire", "Générer la lettre");
        this.serviceGenerationLettreRelanceTribunal = new ServiceGenerationLettreRelanceTribunal(new EcritureDocxAtomique());

        champPrenom = creerChampTexte(18, "Ex: Tom, Noé, Max");
        champNom = creerChampTexte(24, "Ex: Dupont");
        zoneAdressePostale = creerZoneTexte(3, 32, "Ex: 12 rue de la République\n75000 Paris");
        ascenseurAdressePostale = creerAscenseurZoneTexte(zoneAdressePostale, 92);
        champTelephonePortable = creerChampTexte(20, "Ex: 0613121312");
        champCourriel = creerChampTexte(28, "Ex: prenom.nom@exemple.fr");

        zoneAdresseTribunal = creerZoneTexte(3, 32, "Ex: Tribunal judiciaire de Paris,\nPARVIS ROBERT BADINTER,\n75859 Paris");
        ascenseurAdresseTribunal = creerAscenseurZoneTexte(zoneAdresseTribunal, 92);
        champVilleRedaction = creerChampTexte(24, "Ex: Paris");
        selecteurDateDepotEnvoi = creerSelecteurDate();
        comboInformationAttendue = creerCombo("accusé d’enregistrement", "date d’audience", "information sur l’instruction");

        comboGenreRevendique = creerCombo("Masculin", "Féminin", "Non-binaire");
        selecteurDateNaissance = creerSelecteurDate();
        champLieuNaissance = creerChampTexte(24, "Ex: Paris");
        champReferenceDossier = creerChampTexte(24, "Ex: RG 24/01234");
        caseChangementPrenoms = creerCase("La requête concerne aussi un changement de prénoms");
        champPrenomsEtatCivil = creerChampTexte(28, "Ex: Alice, Emma, Agathe");

        ajouterTitreSection("Identité et contact");
        ajouterLigneChamp("Prénoms", champPrenom);
        ajouterLigneChamp("Nom", champNom);
        ajouterLigneChamp("Adresse postale", ascenseurAdressePostale, ascenseurAdressePostale);
        ajouterLigneChamp("Téléphone portable", champTelephonePortable);
        ajouterLigneChamp("Courriel", champCourriel);

        ajouterTitreSection("Suivi du dossier");
        ajouterLigneChamp("Adresse du tribunal", ascenseurAdresseTribunal, ascenseurAdresseTribunal);
        ajouterLigneChamp("Ville de rédaction", champVilleRedaction);
        ajouterLigneChamp("Date de dépôt/envoi", selecteurDateDepotEnvoi, selecteurDateDepotEnvoi);
        ajouterLigneChamp("Information attendue", comboInformationAttendue);

        ajouterTitreSection("Éléments de référence");
        ajouterLigneChamp("Genre revendiqué", comboGenreRevendique);
        ajouterLigneChamp("Date de naissance", selecteurDateNaissance, selecteurDateNaissance);
        ajouterLigneChamp("Lieu de naissance", champLieuNaissance);
        ajouterLigneChamp("Référence de dossier", champReferenceDossier);
        ajouterLignePleine(caseChangementPrenoms);
        ajouterLigneChamp("Prénoms à l'état civil", champPrenomsEtatCivil);

        comboGenreRevendique.addActionListener(e -> mettreAJourExemplesSelonGenre());
        caseChangementPrenoms.addActionListener(e -> mettreAJourEtatChangementPrenoms());

        appliquerInstantane(instantaneInitial);
        mettreAJourExemplesSelonGenre();
        mettreAJourEtatChangementPrenoms();
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
        if (!texteMultiligneValide(texte(zoneAdresseTribunal), 8, 420)) {
            signalerErreur(ascenseurAdresseTribunal, "L'adresse du tribunal est obligatoire et doit être suffisamment précise.");
            valide = false;
        }
        if (!formatVilleValide(texte(champVilleRedaction))) {
            signalerErreur(champVilleRedaction, "La ville de rédaction est obligatoire et doit avoir un format valide.");
            valide = false;
        }
        if (!selecteurDateDepotEnvoi.estDateValide()) {
            signalerErreur(selecteurDateDepotEnvoi, "La date de dépôt/envoi est invalide ou future.");
            valide = false;
        }
        if (texteSelection(comboInformationAttendue).isBlank()) {
            signalerErreur(comboInformationAttendue, "L'information attendue est obligatoire.");
            valide = false;
        }
        if (texteSelection(comboGenreRevendique).isBlank()) {
            signalerErreur(comboGenreRevendique, "Le genre revendiqué est obligatoire.");
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
        if (caseChangementPrenoms.isSelected() && !formatPrenomValide(texte(champPrenomsEtatCivil))) {
            signalerErreur(champPrenomsEtatCivil, "Les prénoms à l'état civil sont obligatoires et doivent avoir un format valide.");
            valide = false;
        }
        return valide;
    }

    @Override
    protected String titreChoixDestination() {
        return "Enregistrer la relance";
    }

    @Override
    protected String nomFichierParDefaut() {
        return "lettre_relance_tribunal.docx";
    }

    @Override
    protected void genererDocument(Path destination, boolean ecraser) throws ErreurExportDocument {
        serviceGenerationLettreRelanceTribunal.exporter(construireDonnees(), destination, ecraser);
    }

    @Override
    protected String messageSuccesGeneration(Path destination) {
        return "La lettre de relance a été générée : " + destination.toAbsolutePath();
    }

    InstantaneLettreRelanceTribunal instantane() {
        return new InstantaneLettreRelanceTribunal(texte(champPrenom), texte(champNom), texte(zoneAdressePostale), texte(champTelephonePortable), texte(champCourriel), texte(zoneAdresseTribunal), texte(champVilleRedaction), dateDuJourTexte(), selecteurDateDepotEnvoi.texteDate(), texteSelection(comboInformationAttendue), texteSelection(comboGenreRevendique), selecteurDateNaissance.texteDate(), texte(champLieuNaissance), texte(champReferenceDossier), caseChangementPrenoms.isSelected(), texte(champPrenomsEtatCivil));
    }

    private DonneesLettreRelanceTribunal construireDonnees() {
        InstantaneLettreRelanceTribunal valeur = instantane();
        return new DonneesLettreRelanceTribunal(valeur.prenom(), valeur.nom(), valeur.adressePostale(), valeur.telephonePortable(), valeur.courriel(), valeur.adresseTribunal(), valeur.villeRedaction(), valeur.dateRedaction(), valeur.dateDepotEnvoi(), valeur.informationAttendue(), valeur.genreRevendique(), valeur.dateNaissance(), valeur.lieuNaissance(), valeur.referenceDossier(), valeur.changementPrenoms(), valeur.prenomsEtatCivil());
    }

    private void appliquerInstantane(InstantaneLettreRelanceTribunal instantane) {
        InstantaneLettreRelanceTribunal valeur = instantane == null ? InstantaneLettreRelanceTribunal.vide() : instantane;
        champPrenom.setText(valeur.prenom());
        champNom.setText(valeur.nom());
        zoneAdressePostale.setText(valeur.adressePostale());
        champTelephonePortable.setText(valeur.telephonePortable());
        champCourriel.setText(valeur.courriel());
        zoneAdresseTribunal.setText(valeur.adresseTribunal());
        champVilleRedaction.setText(valeur.villeRedaction());
        selecteurDateDepotEnvoi.appliquerDateTexte(valeur.dateDepotEnvoi());
        if (!valeur.informationAttendue().isBlank()) {
            comboInformationAttendue.setSelectedItem(valeur.informationAttendue());
        }
        if (!valeur.genreRevendique().isBlank()) {
            comboGenreRevendique.setSelectedItem(valeur.genreRevendique());
        }
        selecteurDateNaissance.appliquerDateTexte(valeur.dateNaissance());
        champLieuNaissance.setText(valeur.lieuNaissance());
        champReferenceDossier.setText(valeur.referenceDossier());
        caseChangementPrenoms.setSelected(valeur.changementPrenoms());
        champPrenomsEtatCivil.setText(valeur.prenomsEtatCivil());
        mettreAJourEtatChangementPrenoms();
    }

    private void mettreAJourExemplesSelonGenre() {
        String genre = normaliserGenre(texteSelection(comboGenreRevendique));
        if ("féminin".equals(genre)) {
            champPrenom.setExemple("Ex: Alice, Emma, Agathe");
        } else if ("masculin".equals(genre)) {
            champPrenom.setExemple("Ex: Tom, Noé, Max");
        } else {
            champPrenom.setExemple("Ex: Sacha, Charlie, Alex");
        }
        champNom.setExemple("Ex: Dupont");
        champPrenomsEtatCivil.setExemple(exemplePrenomsEtatCivilDepuisGenre(genre));
    }

    private void mettreAJourEtatChangementPrenoms() {
        boolean actif = caseChangementPrenoms.isSelected();
        champPrenomsEtatCivil.setEnabled(actif);
    }

    private String exemplePrenomsEtatCivilDepuisGenre(String genre) {
        String normalise = normaliserGenre(genre);
        if ("féminin".equals(normalise)) {
            return "Ex: Tom, Noé, Max";
        }
        return "Ex: Alice, Emma, Agathe";
    }

    private void configurerAccessibiliteComposants() {
        configurerAccessibilite(champPrenom, "Prénoms", "Saisir les prénoms de la personne requérante");
        configurerAccessibilite(champNom, "Nom", "Saisir le nom de famille");
        configurerAccessibilite(zoneAdressePostale, "Adresse postale", "Saisir l'adresse postale complète");
        configurerAccessibilite(ascenseurAdressePostale, "Adresse postale", "Zone multi-lignes pour l'adresse postale");
        configurerAccessibilite(champTelephonePortable, "Téléphone portable", "Saisir le numéro de téléphone portable");
        configurerAccessibilite(champCourriel, "Courriel", "Saisir l'adresse courriel");
        configurerAccessibilite(zoneAdresseTribunal, "Adresse du tribunal", "Saisir l'adresse du greffe destinataire");
        configurerAccessibilite(ascenseurAdresseTribunal, "Adresse du tribunal", "Zone multi-lignes pour l'adresse du tribunal");
        configurerAccessibilite(champVilleRedaction, "Ville de rédaction", "Saisir la ville de rédaction");
        configurerAccessibilite(selecteurDateDepotEnvoi, "Date de dépôt ou d'envoi", "Sélecteur jour mois année de la date de dépôt ou d'envoi");
        configurerAccessibilite(comboInformationAttendue, "Information attendue", "Sélectionner l'information attendue du tribunal");
        configurerAccessibilite(comboGenreRevendique, "Genre revendiqué", "Sélectionner le genre pour les accords de la lettre");
        configurerAccessibilite(selecteurDateNaissance, "Date de naissance", "Sélecteur jour mois année de la date de naissance");
        configurerAccessibilite(champLieuNaissance, "Lieu de naissance", "Saisir le lieu de naissance");
        configurerAccessibilite(champReferenceDossier, "Référence de dossier", "Saisir la référence du dossier si elle est connue");
        configurerAccessibilite(caseChangementPrenoms, "Changement de prénoms", "Indique si la requête concerne aussi un changement de prénoms");
        configurerAccessibilite(champPrenomsEtatCivil, "Prénoms à l'état civil", "Saisir les prénoms à l'état civil si le changement de prénoms est concerné");
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
