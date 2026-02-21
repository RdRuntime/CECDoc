package com.rdr.cecdoc.ui;

import com.rdr.cecdoc.model.DonneesLettreUniversite;
import com.rdr.cecdoc.model.InstantaneLettreUniversite;
import com.rdr.cecdoc.service.export.EcritureDocxAtomique;
import com.rdr.cecdoc.service.export.ErreurExportDocument;
import com.rdr.cecdoc.service.export.ServiceGenerationLettreUniversite;
import com.rdr.cecdoc.ui.theme.TokensTheme;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;
import java.util.List;

final class DialogueLettreUniversite extends DialogueFormulaireDocumentAbstrait {
    private final transient ServiceGenerationLettreUniversite serviceGenerationLettreUniversite;

    private final JComboBox<String> comboCiviliteSouhaitee;
    private final ChampTexteExempleDialogue champPrenomUsage;
    private final ChampTexteExempleDialogue champPrenomEtatCivil;
    private final ChampTexteExempleDialogue champNom;
    private final ZoneTexteExempleDialogue zoneAdresse;
    private final JScrollPane ascenseurAdresse;
    private final ChampTexteExempleDialogue champTelephonePortable;
    private final ChampTexteExempleDialogue champCourriel;
    private final ChampTexteExempleDialogue champIne;
    private final ZoneTexteExempleDialogue zoneNomAdresseUniversite;
    private final JScrollPane ascenseurNomAdresseUniversite;
    private final ZoneTexteExempleDialogue zoneParcours;
    private final JScrollPane ascenseurParcours;
    private final JComboBox<String> comboGenreActuel;
    private final ChampTexteExempleDialogue champVilleActuelle;

    DialogueLettreUniversite(JFrame proprietaire, TokensTheme theme, List<Image> iconesApplication, InstantaneLettreUniversite instantaneInitial, Path dossierSortieParDefaut) {
        super(proprietaire, theme, iconesApplication, dossierSortieParDefaut, "Lettre université", "Lettre pour faire respecter les prénoms d'usage à l'université", "Générer la lettre");
        this.serviceGenerationLettreUniversite = new ServiceGenerationLettreUniversite(new EcritureDocxAtomique());

        comboGenreActuel = creerCombo("Masculin", "Féminin", "Non-binaire");
        comboCiviliteSouhaitee = creerCombo("Monsieur", "Madame", "Mx");
        champPrenomUsage = creerChampTexte(18, "Ex: Tom");
        champPrenomEtatCivil = creerChampTexte(18, "Ex: Max");
        champNom = creerChampTexte(24, "Ex: Dupont");
        zoneAdresse = creerZoneTexte(3, 32, "Ex: 10 rue de la République\n75000 Paris");
        ascenseurAdresse = creerAscenseurZoneTexte(zoneAdresse, 92);
        champTelephonePortable = creerChampTexte(20, "Ex: 0613121312");
        champCourriel = creerChampTexte(28, "Ex: prenom.nom@exemple.fr");
        champIne = creerChampTexte(20, "Ex: 123456789AA");
        zoneNomAdresseUniversite = creerZoneTexte(4, 32, "Ex: Université Paris Cité,\n45 Rue des Saints-Pères,\n75006 Paris");
        ascenseurNomAdresseUniversite = creerAscenseurZoneTexte(zoneNomAdresseUniversite, 108);
        zoneParcours = creerZoneTexte(5, 32, "Notamment les changements administratifs faits et en cours");
        ascenseurParcours = creerAscenseurZoneTexte(zoneParcours, 132);
        champVilleActuelle = creerChampTexte(20, "Ex: Paris");

        ajouterTitreSection("Identité");
        ajouterLigneChamp("Genre actuel (choisi)", comboGenreActuel);
        ajouterLigneChamp("Civilité souhaitée", comboCiviliteSouhaitee);
        ajouterLigneChamp("Prénom d'usage", champPrenomUsage);
        ajouterLigneChamp("Premier prénom à l'état civil", champPrenomEtatCivil);
        ajouterLigneChamp("Nom", champNom);

        ajouterTitreSection("Coordonnées");
        ajouterLigneChamp("Adresse postale", ascenseurAdresse, ascenseurAdresse);
        ajouterLigneChamp("Téléphone portable", champTelephonePortable);
        ajouterLigneChamp("Courriel", champCourriel);
        ajouterLigneChamp("INE", champIne);

        ajouterTitreSection("Université et contexte");
        ajouterLigneChamp("Nom + adresse de l'université", ascenseurNomAdresseUniversite, ascenseurNomAdresseUniversite);
        ajouterLigneChamp("Parcours et contexte", ascenseurParcours, ascenseurParcours);
        ajouterLigneChamp("Ville actuelle", champVilleActuelle);

        comboGenreActuel.addActionListener(e -> mettreAJourExemplesSelonGenre());
        mettreAJourExemplesSelonGenre();
        appliquerInstantane(instantaneInitial);

        terminerConstruction();
        SwingUtilities.invokeLater(champPrenomUsage::requestFocusInWindow);
    }

    @Override
    protected boolean validerFormulaire() {
        boolean valide = true;
        if (texteSelection(comboGenreActuel).isBlank()) {
            signalerErreur(comboGenreActuel, "Le genre actuel est obligatoire.");
            valide = false;
        }
        if (texteSelection(comboCiviliteSouhaitee).isBlank()) {
            signalerErreur(comboCiviliteSouhaitee, "La civilité souhaitée est obligatoire.");
            valide = false;
        }
        if (!formatPrenomValide(texte(champPrenomUsage))) {
            signalerErreur(champPrenomUsage, "Le prénom d'usage est obligatoire et doit avoir un format valide.");
            valide = false;
        }
        if (!formatPrenomValide(texte(champPrenomEtatCivil))) {
            signalerErreur(champPrenomEtatCivil, "Le prénom à l'état civil est obligatoire et doit avoir un format valide.");
            valide = false;
        }
        if (!formatNomValide(texte(champNom))) {
            signalerErreur(champNom, "Le nom est obligatoire et doit avoir un format valide.");
            valide = false;
        }
        if (!texteMultiligneValide(texte(zoneAdresse), 8, 400)) {
            signalerErreur(ascenseurAdresse, "L'adresse postale est obligatoire et doit être suffisamment précise.");
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
        if (!formatIneValide(texte(champIne))) {
            signalerErreur(champIne, "Le numéro INE est obligatoire et doit contenir uniquement des lettres/chiffres.");
            valide = false;
        }
        if (!texteMultiligneValide(texte(zoneNomAdresseUniversite), 5, 500)) {
            signalerErreur(ascenseurNomAdresseUniversite, "Le nom et l'adresse de l'université sont obligatoires.");
            valide = false;
        }
        if (!texteMultiligneValide(texte(zoneParcours), 10, 1200)) {
            signalerErreur(ascenseurParcours, "Le parcours et les changements en cours doivent être renseignés.");
            valide = false;
        }
        if (!formatVilleValide(texte(champVilleActuelle))) {
            signalerErreur(champVilleActuelle, "La ville actuelle est obligatoire et doit avoir un format valide.");
            valide = false;
        }
        return valide;
    }

    @Override
    protected String titreChoixDestination() {
        return "Enregistrer la lettre université";
    }

    @Override
    protected String nomFichierParDefaut() {
        return "lettre_prenom_usage_universite.docx";
    }

    @Override
    protected void genererDocument(Path destination, boolean ecraser) throws ErreurExportDocument {
        serviceGenerationLettreUniversite.exporter(construireDonnees(), destination, ecraser);
    }

    @Override
    protected String messageSuccesGeneration(Path destination) {
        return "La lettre a été générée : " + destination.toAbsolutePath();
    }

    private DonneesLettreUniversite construireDonnees() {
        return new DonneesLettreUniversite(texteSelection(comboCiviliteSouhaitee), texte(champPrenomUsage), texte(champPrenomEtatCivil), texte(champNom), texte(zoneAdresse), texte(champTelephonePortable), texte(champCourriel), texte(champIne), texte(zoneNomAdresseUniversite), texte(zoneParcours), texteSelection(comboGenreActuel), texte(champVilleActuelle));
    }

    private void mettreAJourExemplesSelonGenre() {
        String genre = texteSelection(comboGenreActuel);
        if (genre.startsWith("F")) {
            champPrenomUsage.setExemple(exemplePrenomSimple("féminin"));
            champPrenomEtatCivil.setExemple(exemplePrenomSimple("féminin"));
            comboCiviliteSouhaitee.setSelectedItem("Madame");
            return;
        }
        if (genre.startsWith("N")) {
            champPrenomUsage.setExemple(exemplePrenomSimple("non-binaire"));
            champPrenomEtatCivil.setExemple(exemplePrenomSimple("non-binaire"));
            comboCiviliteSouhaitee.setSelectedItem("Mx");
            return;
        }
        champPrenomUsage.setExemple(exemplePrenomSimple("masculin"));
        champPrenomEtatCivil.setExemple(exemplePrenomSimple("masculin"));
        comboCiviliteSouhaitee.setSelectedItem("Monsieur");
    }

    InstantaneLettreUniversite instantane() {
        return new InstantaneLettreUniversite(texteSelection(comboGenreActuel), texteSelection(comboCiviliteSouhaitee), texte(champPrenomUsage), texte(champPrenomEtatCivil), texte(champNom), texte(zoneAdresse), texte(champTelephonePortable), texte(champCourriel), texte(champIne), texte(zoneNomAdresseUniversite), texte(zoneParcours), texte(champVilleActuelle));
    }

    private void appliquerInstantane(InstantaneLettreUniversite instantane) {
        InstantaneLettreUniversite valeur = instantane == null ? InstantaneLettreUniversite.vide() : instantane;
        if (!valeur.genreActuel().isBlank()) {
            comboGenreActuel.setSelectedItem(valeur.genreActuel());
        }
        mettreAJourExemplesSelonGenre();
        if (!valeur.civiliteSouhaitee().isBlank()) {
            comboCiviliteSouhaitee.setSelectedItem(valeur.civiliteSouhaitee());
        }
        champPrenomUsage.setText(valeur.prenomUsage());
        champPrenomEtatCivil.setText(valeur.prenomEtatCivil());
        champNom.setText(valeur.nom());
        zoneAdresse.setText(valeur.adressePostale());
        champTelephonePortable.setText(valeur.telephonePortable());
        champCourriel.setText(valeur.courriel());
        champIne.setText(valeur.ine());
        zoneNomAdresseUniversite.setText(valeur.nomUniversite());
        zoneParcours.setText(valeur.explicationParcours());
        champVilleActuelle.setText(valeur.villeActuelle());
    }
}
