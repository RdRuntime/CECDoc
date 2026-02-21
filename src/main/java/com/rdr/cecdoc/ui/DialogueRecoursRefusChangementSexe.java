package com.rdr.cecdoc.ui;

import com.rdr.cecdoc.model.DonneesRecoursRefusChangementSexe;
import com.rdr.cecdoc.model.InstantaneRecoursRefusChangementSexe;
import com.rdr.cecdoc.service.export.EcritureDocxAtomique;
import com.rdr.cecdoc.service.export.ErreurExportDocument;
import com.rdr.cecdoc.service.export.ServiceGenerationRecoursRefusChangementSexe;
import com.rdr.cecdoc.ui.theme.TokensTheme;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

final class DialogueRecoursRefusChangementSexe extends DialogueFormulaireDocumentAbstrait {
    private final transient ServiceGenerationRecoursRefusChangementSexe serviceGenerationRecoursRefusChangementSexe;

    private final ChampTexteExempleDialogue champPrenom;
    private final ChampTexteExempleDialogue champNom;
    private final ZoneTexteExempleDialogue zoneAdressePostale;
    private final JScrollPane ascenseurAdressePostale;
    private final ChampTexteExempleDialogue champTelephonePortable;
    private final ChampTexteExempleDialogue champCourriel;

    private final ChampTexteExempleDialogue champVilleCourAppel;
    private final ZoneTexteExempleDialogue zoneAdresseCourAppel;
    private final JScrollPane ascenseurAdresseCourAppel;
    private final ChampTexteExempleDialogue champVilleRedaction;

    private final JComboBox<String> comboGenreRevendique;
    private final SelecteurDateDialogue selecteurDateNaissance;
    private final ChampTexteExempleDialogue champLieuNaissance;
    private final JComboBox<String> comboQualiteAvocat;
    private final ChampTexteExempleDialogue champNomAvocat;
    private final ChampTexteExempleDialogue champBarreauAvocat;
    private final ChampTexteExempleDialogue champAdresseAvocat;
    private final ChampTexteExempleDialogue champTelephoneAvocat;
    private final ChampTexteExempleDialogue champCourrielAvocat;
    private final ChampTexteExempleDialogue champVilleTribunal;
    private final ChampTexteExempleDialogue champNumeroJugement;
    private final ChampTexteExempleDialogue champPrenomsEtatCivil;
    private final ChampTexteExempleDialogue champPrenomsDemandes;
    private final SelecteurDateDialogue selecteurDateJugement;
    private final ZoneTexteExempleDialogue zoneMotifRefus;
    private final JScrollPane ascenseurMotifRefus;
    private final JCheckBox caseChangementPrenoms;
    private final JLabel labelPerimetreRecours;

    DialogueRecoursRefusChangementSexe(JFrame proprietaire, TokensTheme theme, List<Image> iconesApplication, InstantaneRecoursRefusChangementSexe instantaneInitial, Path dossierSortieParDefaut) {
        super(proprietaire, theme, iconesApplication, dossierSortieParDefaut, "Recours refus changement de sexe", "Recours au tribunal en cas de refus de changement de sexe", "Générer la lettre");
        this.serviceGenerationRecoursRefusChangementSexe = new ServiceGenerationRecoursRefusChangementSexe(new EcritureDocxAtomique());

        champPrenom = creerChampTexte(18, "Ex: Tom, Noé, Max");
        champNom = creerChampTexte(24, "Ex: Dupont");
        zoneAdressePostale = creerZoneTexte(3, 32, "Ex: 12 rue de la Paix\n75000 Paris");
        ascenseurAdressePostale = creerAscenseurZoneTexte(zoneAdressePostale, 92);
        champTelephonePortable = creerChampTexte(20, "Ex: 0613121312");
        champCourriel = creerChampTexte(28, "Ex: prenom.nom@exemple.fr");

        champVilleCourAppel = creerChampTexte(24, "Ex: Paris");
        zoneAdresseCourAppel = creerZoneTexte(3, 32, "Ex: Cour d'appel de Paris\n10 Bd du Palais\n75001 Paris");
        ascenseurAdresseCourAppel = creerAscenseurZoneTexte(zoneAdresseCourAppel, 92);
        champVilleRedaction = creerChampTexte(24, "Ex: Paris");

        comboGenreRevendique = creerCombo("Masculin", "Féminin", "Non-binaire");
        selecteurDateNaissance = creerSelecteurDate();
        champLieuNaissance = creerChampTexte(24, "Ex: Paris");
        comboQualiteAvocat = creerCombo("avocat", "avocate", "avocat·e");
        champNomAvocat = creerChampTexte(24, "Ex: Dupont");
        champBarreauAvocat = creerChampTexte(28, "Ex: Barreau de Paris");
        champAdresseAvocat = creerChampTexte(36, "Ex: 86 rue Laugier 75017 PARIS");
        champTelephoneAvocat = creerChampTexte(20, "Ex: 0613121312");
        champCourrielAvocat = creerChampTexte(28, "Ex: avocat.e@cabinet.fr");
        champVilleTribunal = creerChampTexte(34, "Ex: Tribunal judiciaire de Paris");
        champNumeroJugement = creerChampTexte(24, "Ex: RG 26/12345");
        champPrenomsEtatCivil = creerChampTexte(28, "Ex: Tom, Noé, Max");
        champPrenomsDemandes = creerChampTexte(28, "Ex: Alice, Emma, Agathe");
        selecteurDateJugement = creerSelecteurDate();
        zoneMotifRefus = creerZoneTexte(4, 32, "Ex: exigence de preuves supplémentaires");
        ascenseurMotifRefus = creerAscenseurZoneTexte(zoneMotifRefus, 118);
        caseChangementPrenoms = creerCase("Le recours concerne aussi la mise à jour de mes prénoms");
        labelPerimetreRecours = new JLabel();
        labelPerimetreRecours.setFont(theme.typography().message());
        labelPerimetreRecours.setForeground(theme.palette().bodyText());

        ajouterTitreSection("Identité et contact");
        ajouterLigneChamp("Prénoms", champPrenom);
        ajouterLigneChamp("Nom", champNom);
        ajouterLigneChamp("Adresse postale", ascenseurAdressePostale, ascenseurAdressePostale);
        ajouterLigneChamp("Téléphone portable", champTelephonePortable);
        ajouterLigneChamp("Courriel", champCourriel);

        ajouterTitreSection("Juridiction d'appel");
        ajouterLigneChamp("Ville de la cour d'appel", champVilleCourAppel);
        ajouterLigneChamp("Adresse de la cour d'appel", ascenseurAdresseCourAppel, ascenseurAdresseCourAppel);
        ajouterLigneChamp("Ville de rédaction", champVilleRedaction);

        ajouterTitreSection("Représentation par avocat·e");
        ajouterLigneChamp("Qualité", comboQualiteAvocat);
        ajouterLigneChamp("Nom de l’avocat·e", champNomAvocat);
        ajouterLigneChamp("Barreau", champBarreauAvocat);
        ajouterLigneChamp("Adresse avocat·e", champAdresseAvocat);
        ajouterLigneChamp("Téléphone avocat·e", champTelephoneAvocat);
        ajouterLigneChamp("Courriel avocat·e", champCourrielAvocat);

        ajouterTitreSection("Dossier contesté");
        ajouterLigneChamp("Genre revendiqué", comboGenreRevendique);
        ajouterLigneChamp("Date de naissance", selecteurDateNaissance, selecteurDateNaissance);
        ajouterLigneChamp("Lieu de naissance", champLieuNaissance);
        ajouterLigneChamp("Tribunal judiciaire", champVilleTribunal);
        ajouterLigneChamp("Référence du jugement", champNumeroJugement);
        ajouterLigneChamp("Date du jugement contesté", selecteurDateJugement, selecteurDateJugement);
        ajouterLigneChamp("Motif du refus (résumé)", ascenseurMotifRefus, ascenseurMotifRefus);
        ajouterLignePleine(caseChangementPrenoms);
        ajouterLigneChamp("Prénoms à l'état civil", champPrenomsEtatCivil);
        ajouterLigneChamp("Prénoms demandés", champPrenomsDemandes);
        ajouterLignePleine(labelPerimetreRecours);

        comboGenreRevendique.addActionListener(e -> mettreAJourExemplesSelonGenre());
        caseChangementPrenoms.addActionListener(e -> mettreAJourTextePerimetreRecours());
        mettreAJourExemplesSelonGenre();
        appliquerInstantane(instantaneInitial);
        mettreAJourTextePerimetreRecours();
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
        if (!formatVilleValide(texte(champVilleCourAppel))) {
            signalerErreur(champVilleCourAppel, "La ville de la cour d'appel est obligatoire et doit avoir un format valide.");
            valide = false;
        }
        if (!texteMultiligneValide(texte(zoneAdresseCourAppel), 8, 420)) {
            signalerErreur(ascenseurAdresseCourAppel, "L'adresse de la cour d'appel est obligatoire et doit être suffisamment précise.");
            valide = false;
        }
        if (!formatVilleValide(texte(champVilleRedaction))) {
            signalerErreur(champVilleRedaction, "La ville de rédaction est obligatoire et doit avoir un format valide.");
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
        if (texteSelection(comboQualiteAvocat).isBlank()) {
            signalerErreur(comboQualiteAvocat, "La qualité de l’avocat·e est obligatoire.");
            valide = false;
        }
        if (!formatNomValide(texte(champNomAvocat))) {
            signalerErreur(champNomAvocat, "Le nom de l’avocat·e est obligatoire et doit avoir un format valide.");
            valide = false;
        }
        if (!texteSimpleValide(texte(champBarreauAvocat), 3, 120)) {
            signalerErreur(champBarreauAvocat, "Le barreau est obligatoire.");
            valide = false;
        }
        if (!texteSimpleValide(texte(champAdresseAvocat), 8, 200)) {
            signalerErreur(champAdresseAvocat, "L'adresse de l’avocat·e est obligatoire.");
            valide = false;
        }
        if (!formatTelephoneValide(texte(champTelephoneAvocat))) {
            signalerErreur(champTelephoneAvocat, "Le téléphone de l’avocat·e n'a pas un format valide.");
            valide = false;
        }
        if (!formatCourrielValide(texte(champCourrielAvocat))) {
            signalerErreur(champCourrielAvocat, "Le courriel de l’avocat·e n'a pas un format valide.");
            valide = false;
        }
        if (!texteSimpleValide(texte(champVilleTribunal), 4, 140)) {
            signalerErreur(champVilleTribunal, "Le tribunal judiciaire est obligatoire.");
            valide = false;
        }
        if (!texteSimpleValide(texte(champNumeroJugement), 2, 80)) {
            signalerErreur(champNumeroJugement, "La référence du jugement est obligatoire.");
            valide = false;
        }
        if (!selecteurDateJugement.estDateValide()) {
            signalerErreur(selecteurDateJugement, "La date du jugement est invalide ou future.");
            valide = false;
        }
        if (!texteMultiligneValide(texte(zoneMotifRefus), 8, 1200)) {
            signalerErreur(ascenseurMotifRefus, "Le résumé du motif du refus est obligatoire.");
            valide = false;
        }
        if (caseChangementPrenoms.isSelected()) {
            if (!formatPrenomsMultiplesValide(texte(champPrenomsEtatCivil))) {
                signalerErreur(champPrenomsEtatCivil, "Les prénoms à l'état civil sont obligatoires avec un format valide.");
                valide = false;
            }
            if (!formatPrenomsMultiplesValide(texte(champPrenomsDemandes))) {
                signalerErreur(champPrenomsDemandes, "Les prénoms demandés sont obligatoires avec un format valide.");
                valide = false;
            }
        }
        return valide;
    }

    @Override
    protected String titreChoixDestination() {
        return "Enregistrer le recours en appel";
    }

    @Override
    protected String nomFichierParDefaut() {
        return caseChangementPrenoms.isSelected() ? "recours_refus_changement_sexe_et_prenoms.docx" : "recours_refus_changement_sexe.docx";
    }

    @Override
    protected void genererDocument(Path destination, boolean ecraser) throws ErreurExportDocument {
        serviceGenerationRecoursRefusChangementSexe.exporter(construireDonnees(), destination, ecraser);
    }

    @Override
    protected String messageSuccesGeneration(Path destination) {
        return "Le recours a été généré : " + destination.toAbsolutePath();
    }

    InstantaneRecoursRefusChangementSexe instantane() {
        return new InstantaneRecoursRefusChangementSexe(texte(champPrenom), texte(champNom), texte(zoneAdressePostale), texte(champTelephonePortable), texte(champCourriel), texte(champVilleCourAppel), texte(zoneAdresseCourAppel), texte(champVilleRedaction), dateDuJourTexte(), texteSelection(comboGenreRevendique), selecteurDateNaissance.texteDate(), texte(champLieuNaissance), texteSelection(comboQualiteAvocat), texte(champNomAvocat), texte(champBarreauAvocat), texte(champAdresseAvocat), texte(champTelephoneAvocat), texte(champCourrielAvocat), texte(champVilleTribunal), selecteurDateJugement.texteDate(), texte(zoneMotifRefus), caseChangementPrenoms.isSelected(), texte(champNumeroJugement), texte(champPrenomsEtatCivil), texte(champPrenomsDemandes));
    }

    private DonneesRecoursRefusChangementSexe construireDonnees() {
        InstantaneRecoursRefusChangementSexe valeur = instantane();
        return new DonneesRecoursRefusChangementSexe(valeur.prenom(), valeur.nom(), valeur.adressePostale(), valeur.telephonePortable(), valeur.courriel(), valeur.villeCourAppel(), valeur.adresseCourAppel(), valeur.villeRedaction(), valeur.dateRedaction(), valeur.genreRevendique(), valeur.dateNaissance(), valeur.lieuNaissance(), valeur.qualiteAvocat(), valeur.nomAvocat(), valeur.barreauAvocat(), valeur.adresseAvocat(), valeur.telephoneAvocat(), valeur.courrielAvocat(), valeur.villeTribunal(), valeur.dateJugement(), valeur.motifRefus(), valeur.changementPrenoms(), valeur.numeroJugement(), valeur.prenomsEtatCivil(), valeur.prenomsDemandes());
    }

    private void appliquerInstantane(InstantaneRecoursRefusChangementSexe instantane) {
        InstantaneRecoursRefusChangementSexe valeur = instantane == null ? InstantaneRecoursRefusChangementSexe.vide() : instantane;
        champPrenom.setText(valeur.prenom());
        champNom.setText(valeur.nom());
        zoneAdressePostale.setText(valeur.adressePostale());
        champTelephonePortable.setText(valeur.telephonePortable());
        champCourriel.setText(valeur.courriel());
        champVilleCourAppel.setText(valeur.villeCourAppel());
        zoneAdresseCourAppel.setText(valeur.adresseCourAppel());
        champVilleRedaction.setText(valeur.villeRedaction());
        if (!valeur.genreRevendique().isBlank()) {
            comboGenreRevendique.setSelectedItem(valeur.genreRevendique());
        }
        selecteurDateNaissance.appliquerDateTexte(valeur.dateNaissance());
        champLieuNaissance.setText(valeur.lieuNaissance());
        if (!valeur.qualiteAvocat().isBlank()) {
            comboQualiteAvocat.setSelectedItem(valeur.qualiteAvocat());
        }
        champNomAvocat.setText(valeur.nomAvocat());
        champBarreauAvocat.setText(valeur.barreauAvocat());
        champAdresseAvocat.setText(valeur.adresseAvocat());
        champTelephoneAvocat.setText(valeur.telephoneAvocat());
        champCourrielAvocat.setText(valeur.courrielAvocat());
        champVilleTribunal.setText(valeur.villeTribunal());
        champNumeroJugement.setText(valeur.numeroJugement());
        champPrenomsEtatCivil.setText(valeur.prenomsEtatCivil());
        champPrenomsDemandes.setText(valeur.prenomsDemandes());
        selecteurDateJugement.appliquerDateTexte(valeur.dateJugement());
        zoneMotifRefus.setText(valeur.motifRefus());
        caseChangementPrenoms.setSelected(valeur.changementPrenoms());
        mettreAJourExemplesSelonGenre();
    }

    private void mettreAJourExemplesSelonGenre() {
        String genre = texteSelection(comboGenreRevendique);
        String genreNormalise = normaliserGenre(genre);
        if ("féminin".equals(genreNormalise)) {
            champPrenom.setExemple("Ex: Alice, Emma, Agathe");
        } else if ("masculin".equals(genreNormalise)) {
            champPrenom.setExemple("Ex: Tom, Noé, Max");
        } else {
            champPrenom.setExemple("Ex: Sacha, Charlie, Alex");
        }
        champNom.setExemple("Ex: Dupont");
        if ("féminin".equals(genreNormalise)) {
            champPrenomsDemandes.setExemple("Ex: Alice, Emma, Agathe");
            champPrenomsEtatCivil.setExemple("Ex: Tom, Noé, Max");
        } else if ("masculin".equals(genreNormalise)) {
            champPrenomsDemandes.setExemple("Ex: Tom, Noé, Max");
            champPrenomsEtatCivil.setExemple("Ex: Alice, Emma, Agathe");
        } else {
            champPrenomsDemandes.setExemple("Ex: Sacha, Charlie, Alex");
            champPrenomsEtatCivil.setExemple("Ex: Tom, Noé, Max");
        }
    }

    private void mettreAJourTextePerimetreRecours() {
        String texte = caseChangementPrenoms.isSelected() ? "Le texte généré mentionnera le changement de la mention du sexe, les anciens et nouveaux prénoms." : "Le texte généré mentionnera uniquement le changement de la mention du sexe.";
        labelPerimetreRecours.setText(texte);
    }

    private void configurerAccessibiliteComposants() {
        configurerAccessibilite(champPrenom, "Prénoms", "Saisir les prénoms affichés dans le recours");
        configurerAccessibilite(champNom, "Nom", "Saisir le nom de famille");
        configurerAccessibilite(zoneAdressePostale, "Adresse postale", "Saisir l'adresse postale complète");
        configurerAccessibilite(ascenseurAdressePostale, "Adresse postale", "Zone multi-lignes pour l'adresse postale");
        configurerAccessibilite(champTelephonePortable, "Téléphone portable", "Saisir le numéro de téléphone");
        configurerAccessibilite(champCourriel, "Courriel", "Saisir l'adresse courriel");
        configurerAccessibilite(champVilleCourAppel, "Ville de la cour d'appel", "Saisir la ville de la cour d'appel");
        configurerAccessibilite(zoneAdresseCourAppel, "Adresse de la cour d'appel", "Saisir l'adresse complète de la cour d'appel");
        configurerAccessibilite(ascenseurAdresseCourAppel, "Adresse de la cour d'appel", "Zone multi-lignes pour l'adresse de la cour d'appel");
        configurerAccessibilite(champVilleRedaction, "Ville de rédaction", "Saisir la ville de rédaction de la lettre");
        configurerAccessibilite(comboGenreRevendique, "Genre revendiqué", "Sélectionner le genre revendiqué");
        configurerAccessibilite(selecteurDateNaissance, "Date de naissance", "Sélecteur jour mois année de la date de naissance");
        configurerAccessibilite(champLieuNaissance, "Lieu de naissance", "Saisir le lieu de naissance");
        configurerAccessibilite(comboQualiteAvocat, "Qualité avocat·e", "Sélectionner la qualité de l’avocat·e");
        configurerAccessibilite(champNomAvocat, "Nom de l'avocat", "Saisir le nom de l'avocat ou de l'avocate");
        configurerAccessibilite(champBarreauAvocat, "Barreau avocat·e", "Saisir le barreau de l’avocat·e");
        configurerAccessibilite(champAdresseAvocat, "Adresse avocat·e", "Saisir l’adresse de l’avocat·e");
        configurerAccessibilite(champTelephoneAvocat, "Téléphone avocat·e", "Saisir le téléphone de l’avocat·e");
        configurerAccessibilite(champCourrielAvocat, "Courriel avocat·e", "Saisir le courriel de l’avocat·e");
        configurerAccessibilite(champVilleTribunal, "Tribunal judiciaire", "Saisir le tribunal ayant rendu le jugement");
        configurerAccessibilite(champNumeroJugement, "Référence du jugement", "Saisir la référence du jugement contesté");
        configurerAccessibilite(selecteurDateJugement, "Date du jugement contesté", "Sélecteur jour mois année de la date du jugement");
        configurerAccessibilite(zoneMotifRefus, "Motif du refus", "Saisir un résumé du motif de refus du tribunal");
        configurerAccessibilite(ascenseurMotifRefus, "Motif du refus", "Zone multi-lignes pour le motif de refus");
        configurerAccessibilite(caseChangementPrenoms, "Changement de prénoms", "Indique si le recours inclut aussi la mise à jour des prénoms");
        configurerAccessibilite(champPrenomsEtatCivil, "Prénoms à l'état civil", "Saisir les prénoms à l'état civil");
        configurerAccessibilite(champPrenomsDemandes, "Prénoms demandés", "Saisir les nouveaux prénoms demandés");
    }

    private void configurerAccessibilite(javax.swing.JComponent composant, String nom, String description) {
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
