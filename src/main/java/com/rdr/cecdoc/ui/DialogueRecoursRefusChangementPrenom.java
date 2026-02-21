package com.rdr.cecdoc.ui;

import com.rdr.cecdoc.model.DonneesRecoursRefusChangementPrenom;
import com.rdr.cecdoc.model.InstantaneRecoursRefusChangementPrenom;
import com.rdr.cecdoc.service.export.EcritureDocxAtomique;
import com.rdr.cecdoc.service.export.ErreurExportDocument;
import com.rdr.cecdoc.service.export.ServiceGenerationRecoursRefusChangementPrenom;
import com.rdr.cecdoc.ui.theme.TokensTheme;
import com.rdr.cecdoc.util.NormalisationTexte;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

final class DialogueRecoursRefusChangementPrenom extends DialogueFormulaireDocumentAbstrait {
    private final transient ServiceGenerationRecoursRefusChangementPrenom serviceGenerationRecoursRefusChangementPrenom;

    private final ChampTexteExempleDialogue champPrenomsDemandes;
    private final ChampTexteExempleDialogue champPrenomsInscrits;
    private final ChampTexteExempleDialogue champNom;
    private final ZoneTexteExempleDialogue zoneAdressePostale;
    private final JScrollPane ascenseurAdressePostale;
    private final ChampTexteExempleDialogue champTelephonePortable;
    private final ChampTexteExempleDialogue champCourriel;

    private final JComboBox<String> comboQualiteAvocat;
    private final ChampTexteExempleDialogue champNomAvocat;
    private final ChampTexteExempleDialogue champBarreauAvocat;
    private final ChampTexteExempleDialogue champAdresseAvocat;
    private final ChampTexteExempleDialogue champTelephoneAvocat;
    private final ChampTexteExempleDialogue champCourrielAvocat;

    private final ZoneTexteExempleDialogue zoneAdresseTribunal;
    private final JScrollPane ascenseurAdresseTribunal;
    private final ChampTexteExempleDialogue champVilleTribunalJudiciaire;
    private final ChampTexteExempleDialogue champVilleMairie;
    private final ChampTexteExempleDialogue champVilleRedaction;
    private final SelecteurDateDialogue selecteurDateNotificationRefus;
    private final SelecteurDateDialogue selecteurDateRecepisseDepot;
    private final SelecteurDateDialogue selecteurDateDecisionRefus;
    private final ZoneTexteExempleDialogue zoneMotifRefusNotifie;
    private final JScrollPane ascenseurMotifRefusNotifie;
    private final ZoneTexteExempleDialogue zoneRaisonsContestation;
    private final JScrollPane ascenseurRaisonsContestation;

    private final JComboBox<String> comboGenreDemande;
    private final SelecteurDateDialogue selecteurDateNaissance;
    private final ChampTexteExempleDialogue champLieuNaissance;
    private final JCheckBox caseUsageFamilial;
    private final JCheckBox caseUsageAmical;
    private final JCheckBox caseUsageProfessionnel;
    private final JCheckBox caseUsageScolaire;
    private final JCheckBox caseUsageAssociatif;
    private final JPanel panneauUsages;
    private final ZoneTexteExempleDialogue zoneAnecdotesDifficultes;
    private final JScrollPane ascenseurAnecdotesDifficultes;

    DialogueRecoursRefusChangementPrenom(JFrame proprietaire, TokensTheme theme, List<Image> iconesApplication, InstantaneRecoursRefusChangementPrenom instantaneInitial, Path dossierSortieParDefaut) {
        super(proprietaire, theme, iconesApplication, dossierSortieParDefaut, "Recours refus changement de prénoms", "Recours en cas de refus de changement de prénoms", "Générer la lettre");
        this.serviceGenerationRecoursRefusChangementPrenom = new ServiceGenerationRecoursRefusChangementPrenom(new EcritureDocxAtomique());

        champPrenomsDemandes = creerChampTexte(28, "Ex: Alice, Emma, Agathe");
        champPrenomsInscrits = creerChampTexte(28, "Ex: Tom, Noé, Max");
        champNom = creerChampTexte(24, "Ex: Dupont");
        zoneAdressePostale = creerZoneTexte(3, 32, "Ex: 12 rue de la République\n75000 Paris");
        ascenseurAdressePostale = creerAscenseurZoneTexte(zoneAdressePostale, 92);
        champTelephonePortable = creerChampTexte(20, "Ex: 0613121312");
        champCourriel = creerChampTexte(28, "Ex: prenom.nom@exemple.fr");

        comboQualiteAvocat = creerCombo("avocat", "avocate", "avocat·e");
        champNomAvocat = creerChampTexte(24, "Ex: Durand");
        champBarreauAvocat = creerChampTexte(28, "Ex: Barreau de Paris");
        champAdresseAvocat = creerChampTexte(36, "Ex: 86 rue Laugier 75017 PARIS");
        champTelephoneAvocat = creerChampTexte(20, "Ex: 0613121312");
        champCourrielAvocat = creerChampTexte(28, "Ex: avocat.e@cabinet.fr");

        zoneAdresseTribunal = creerZoneTexte(3, 32, "Ex: Tribunal judiciaire de Paris,\nPARVIS ROBERT BADINTER,\n75859 Paris");
        ascenseurAdresseTribunal = creerAscenseurZoneTexte(zoneAdresseTribunal, 92);
        champVilleTribunalJudiciaire = creerChampTexte(24, "Ex: Paris");
        champVilleMairie = creerChampTexte(24, "Ex: Paris");
        champVilleRedaction = creerChampTexte(24, "Ex: Paris");
        selecteurDateNotificationRefus = creerSelecteurDate();
        selecteurDateRecepisseDepot = creerSelecteurDate();
        selecteurDateDecisionRefus = creerSelecteurDate();
        zoneMotifRefusNotifie = creerZoneTexte(3, 32, "Ex: absence d'intérêt légitime retenue par le parquet");
        ascenseurMotifRefusNotifie = creerAscenseurZoneTexte(zoneMotifRefusNotifie, 92);
        zoneRaisonsContestation = creerZoneTexte(4, 32, "Ex:\n- Raison 1\n- Raison 2\n- Raison 3");
        ascenseurRaisonsContestation = creerAscenseurZoneTexte(zoneRaisonsContestation, 120);

        comboGenreDemande = creerCombo("Masculin", "Féminin", "Non-binaire");
        selecteurDateNaissance = creerSelecteurDate();
        champLieuNaissance = creerChampTexte(24, "Ex: Paris");
        caseUsageFamilial = creerCase("familiale");
        caseUsageAmical = creerCase("amicale");
        caseUsageProfessionnel = creerCase("professionnelle");
        caseUsageScolaire = creerCase("scolaire");
        caseUsageAssociatif = creerCase("associative");
        panneauUsages = new JPanel(new FlowLayout(FlowLayout.LEFT, Math.max(4, theme().spacing().inlineGap() / 2), 0));
        panneauUsages.setOpaque(false);
        panneauUsages.add(caseUsageFamilial);
        panneauUsages.add(caseUsageAmical);
        panneauUsages.add(caseUsageProfessionnel);
        panneauUsages.add(caseUsageScolaire);
        panneauUsages.add(caseUsageAssociatif);
        zoneAnecdotesDifficultes = creerZoneTexte(3, 32, "Ex: incidents concrets causés par l'inadéquation des prénoms");
        ascenseurAnecdotesDifficultes = creerAscenseurZoneTexte(zoneAnecdotesDifficultes, 92);

        ajouterTitreSection("Identité et contact");
        ajouterLigneChamp("Prénoms choisis", champPrenomsDemandes);
        ajouterLigneChamp("Prénoms à l'état civil", champPrenomsInscrits);
        ajouterLigneChamp("Nom", champNom);
        ajouterLigneChamp("Adresse postale", ascenseurAdressePostale, ascenseurAdressePostale);
        ajouterLigneChamp("Téléphone portable", champTelephonePortable);
        ajouterLigneChamp("Courriel", champCourriel);

        ajouterTitreSection("Représentation par avocat·e");
        ajouterLigneChamp("Qualité", comboQualiteAvocat);
        ajouterLigneChamp("Nom de l’avocat·e", champNomAvocat);
        ajouterLigneChamp("Barreau", champBarreauAvocat);
        ajouterLigneChamp("Adresse avocat·e", champAdresseAvocat);
        ajouterLigneChamp("Téléphone avocat·e", champTelephoneAvocat);
        ajouterLigneChamp("Courriel avocat·e", champCourrielAvocat);

        ajouterTitreSection("Recours et refus notifié");
        ajouterLigneChamp("Adresse du tribunal", ascenseurAdresseTribunal, ascenseurAdresseTribunal);
        ajouterLigneChamp("Ville du tribunal judiciaire", champVilleTribunalJudiciaire);
        ajouterLigneChamp("Ville de la mairie", champVilleMairie);
        ajouterLigneChamp("Ville de rédaction", champVilleRedaction);
        ajouterLigneChamp("Date de notification du refus", selecteurDateNotificationRefus, selecteurDateNotificationRefus);
        ajouterLigneChamp("Date du récépissé de dépôt", selecteurDateRecepisseDepot, selecteurDateRecepisseDepot);
        ajouterLigneChamp("Date de la décision", selecteurDateDecisionRefus, selecteurDateDecisionRefus);
        ajouterLigneChamp("Motif du refus notifié", ascenseurMotifRefusNotifie, ascenseurMotifRefusNotifie);
        ajouterLigneChamp("Raisons de contestation", ascenseurRaisonsContestation, ascenseurRaisonsContestation);

        ajouterTitreSection("Intérêt légitime");
        ajouterLigneChamp("Genre actuel (choisi)", comboGenreDemande);
        ajouterLigneChamp("Date de naissance", selecteurDateNaissance, selecteurDateNaissance);
        ajouterLigneChamp("Lieu de naissance", champLieuNaissance);
        ajouterLigneChamp("Sphères d'usage du prénom", panneauUsages);
        ajouterLigneChamp("Anecdotes facultatives", ascenseurAnecdotesDifficultes, ascenseurAnecdotesDifficultes);

        comboGenreDemande.addActionListener(e -> mettreAJourExemplesSelonGenre());

        appliquerInstantane(instantaneInitial);
        mettreAJourExemplesSelonGenre();
        configurerAccessibiliteComposants();
        terminerConstruction();
        SwingUtilities.invokeLater(champPrenomsDemandes::requestFocusInWindow);
    }

    @Override
    protected boolean validerFormulaire() {
        boolean valide = true;
        if (!formatPrenomsMultiplesValide(texte(champPrenomsDemandes))) {
            signalerErreur(champPrenomsDemandes, "Les prénoms d'usage doivent être renseignés avec un format valide.");
            valide = false;
        }
        if (!formatPrenomsMultiplesValide(texte(champPrenomsInscrits))) {
            signalerErreur(champPrenomsInscrits, "Les prénoms à l'état civil doivent être renseignés avec un format valide.");
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
        if (!texteMultiligneValide(texte(zoneAdresseTribunal), 8, 420)) {
            signalerErreur(ascenseurAdresseTribunal, "L'adresse du tribunal est obligatoire et doit être suffisamment précise.");
            valide = false;
        }
        if (!formatVilleValide(texte(champVilleTribunalJudiciaire))) {
            signalerErreur(champVilleTribunalJudiciaire, "La ville du tribunal judiciaire est obligatoire.");
            valide = false;
        }
        if (!formatVilleValide(texte(champVilleMairie))) {
            signalerErreur(champVilleMairie, "La ville de la mairie est obligatoire.");
            valide = false;
        }
        if (!formatVilleValide(texte(champVilleRedaction))) {
            signalerErreur(champVilleRedaction, "La ville de rédaction est obligatoire.");
            valide = false;
        }
        if (!selecteurDateNotificationRefus.estDateValide()) {
            signalerErreur(selecteurDateNotificationRefus, "La date de notification du refus est invalide ou future.");
            valide = false;
        }
        if (!selecteurDateRecepisseDepot.estDateValide()) {
            signalerErreur(selecteurDateRecepisseDepot, "La date du récépissé est invalide ou future.");
            valide = false;
        }
        if (!selecteurDateDecisionRefus.estDateValide()) {
            signalerErreur(selecteurDateDecisionRefus, "La date de la décision est invalide ou future.");
            valide = false;
        }
        if (!texteMultiligneValide(texte(zoneMotifRefusNotifie), 8, 1000)) {
            signalerErreur(ascenseurMotifRefusNotifie, "Le motif du refus notifié est obligatoire.");
            valide = false;
        }
        if (!texteMultiligneValide(texte(zoneRaisonsContestation), 8, 2000)) {
            signalerErreur(ascenseurRaisonsContestation, "Les raisons de contestation sont obligatoires.");
            valide = false;
        }
        if (texteSelection(comboGenreDemande).isBlank()) {
            signalerErreur(comboGenreDemande, "Le genre actuel est obligatoire.");
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
        if (!auMoinsUnUsageSelectionne()) {
            signalerErreur(panneauUsages, "Sélectionner au moins une sphère d'usage du prénom.");
            valide = false;
        }
        return valide;
    }

    @Override
    protected String titreChoixDestination() {
        return "Enregistrer le recours";
    }

    @Override
    protected String nomFichierParDefaut() {
        return "recours_refus_changement_prenoms.docx";
    }

    @Override
    protected void genererDocument(Path destination, boolean ecraser) throws ErreurExportDocument {
        serviceGenerationRecoursRefusChangementPrenom.exporter(construireDonnees(), destination, ecraser);
    }

    @Override
    protected String messageSuccesGeneration(Path destination) {
        return "Le recours a été généré : " + destination.toAbsolutePath();
    }

    InstantaneRecoursRefusChangementPrenom instantane() {
        String prenomsDemandes = texte(champPrenomsDemandes);
        String prenomPrincipal = NormalisationTexte.extrairePremierPrenom(prenomsDemandes);
        boolean plusieursInscrits = estPlusieursPrenoms(texte(champPrenomsInscrits));
        boolean plusieursDemandes = estPlusieursPrenoms(prenomsDemandes);
        return new InstantaneRecoursRefusChangementPrenom(prenomPrincipal, texte(champNom), texte(zoneAdressePostale), texte(champTelephonePortable), texte(champCourriel), texte(champVilleMairie), texte(zoneAdresseTribunal), texte(champVilleRedaction), dateDuJourTexte(), texteSelection(comboGenreDemande), selecteurDateNaissance.texteDate(), texte(champLieuNaissance), texte(champPrenomsInscrits), prenomsDemandes, plusieursInscrits, plusieursDemandes, texteSelection(comboQualiteAvocat), texte(champNomAvocat), texte(champBarreauAvocat), texte(champAdresseAvocat), texte(champTelephoneAvocat), texte(champCourrielAvocat), selecteurDateNotificationRefus.texteDate(), texte(champVilleTribunalJudiciaire), texte(zoneMotifRefusNotifie), caseUsageFamilial.isSelected(), caseUsageAmical.isSelected(), caseUsageProfessionnel.isSelected(), caseUsageScolaire.isSelected(), caseUsageAssociatif.isSelected(), texte(zoneAnecdotesDifficultes), texte(zoneRaisonsContestation), selecteurDateRecepisseDepot.texteDate(), selecteurDateDecisionRefus.texteDate());
    }

    private DonneesRecoursRefusChangementPrenom construireDonnees() {
        InstantaneRecoursRefusChangementPrenom valeur = instantane();
        return new DonneesRecoursRefusChangementPrenom(valeur.prenom(), valeur.nom(), valeur.adressePostale(), valeur.telephonePortable(), valeur.courriel(), valeur.villeMairie(), valeur.adresseMairie(), valeur.villeRedaction(), valeur.dateRedaction(), valeur.genreDemande(), valeur.dateNaissance(), valeur.lieuNaissance(), valeur.prenomsInscrits(), valeur.prenomsDemandes(), valeur.plusieursPrenomsInscrits(), valeur.plusieursPrenomsDemandes(), valeur.qualiteAvocat(), valeur.nomAvocat(), valeur.barreauAvocat(), valeur.adresseAvocat(), valeur.telephoneAvocat(), valeur.courrielAvocat(), valeur.dateNotificationRefus(), valeur.villeTribunalJudiciaire(), valeur.motifRefusNotifie(), valeur.usageFamilial(), valeur.usageAmical(), valeur.usageProfessionnel(), valeur.usageScolaire(), valeur.usageAssociatif(), valeur.anecdotesDifficultes(), valeur.raisonsContestation(), valeur.dateRecepisseDepot(), valeur.dateDecisionRefus());
    }

    private void appliquerInstantane(InstantaneRecoursRefusChangementPrenom instantane) {
        InstantaneRecoursRefusChangementPrenom valeur = instantane == null ? InstantaneRecoursRefusChangementPrenom.vide() : instantane;
        champPrenomsDemandes.setText(premiereValeurNonVide(valeur.prenomsDemandes(), valeur.prenom()));
        champPrenomsInscrits.setText(valeur.prenomsInscrits());
        champNom.setText(valeur.nom());
        zoneAdressePostale.setText(valeur.adressePostale());
        champTelephonePortable.setText(valeur.telephonePortable());
        champCourriel.setText(valeur.courriel());
        comboQualiteAvocat.setSelectedItem(premiereValeurNonVide(valeur.qualiteAvocat(), "avocat"));
        champNomAvocat.setText(valeur.nomAvocat());
        champBarreauAvocat.setText(valeur.barreauAvocat());
        champAdresseAvocat.setText(valeur.adresseAvocat());
        champTelephoneAvocat.setText(valeur.telephoneAvocat());
        champCourrielAvocat.setText(valeur.courrielAvocat());
        zoneAdresseTribunal.setText(valeur.adresseMairie());
        champVilleTribunalJudiciaire.setText(valeur.villeTribunalJudiciaire());
        champVilleMairie.setText(valeur.villeMairie());
        champVilleRedaction.setText(valeur.villeRedaction());
        selecteurDateNotificationRefus.appliquerDateTexte(valeur.dateNotificationRefus());
        selecteurDateRecepisseDepot.appliquerDateTexte(valeur.dateRecepisseDepot());
        selecteurDateDecisionRefus.appliquerDateTexte(valeur.dateDecisionRefus());
        zoneMotifRefusNotifie.setText(valeur.motifRefusNotifie());
        zoneRaisonsContestation.setText(valeur.raisonsContestation());
        if (!valeur.genreDemande().isBlank()) {
            comboGenreDemande.setSelectedItem(valeur.genreDemande());
        }
        selecteurDateNaissance.appliquerDateTexte(valeur.dateNaissance());
        champLieuNaissance.setText(valeur.lieuNaissance());
        caseUsageFamilial.setSelected(valeur.usageFamilial());
        caseUsageAmical.setSelected(valeur.usageAmical());
        caseUsageProfessionnel.setSelected(valeur.usageProfessionnel());
        caseUsageScolaire.setSelected(valeur.usageScolaire());
        caseUsageAssociatif.setSelected(valeur.usageAssociatif());
        zoneAnecdotesDifficultes.setText(valeur.anecdotesDifficultes());
    }

    private void mettreAJourExemplesSelonGenre() {
        String genre = normaliserGenre(texteSelection(comboGenreDemande));
        if ("féminin".equals(genre)) {
            champPrenomsDemandes.setExemple("Ex: Alice, Emma, Agathe");
            champPrenomsInscrits.setExemple("Ex: Tom, Noé, Max");
        } else if ("masculin".equals(genre)) {
            champPrenomsDemandes.setExemple("Ex: Tom, Noé, Max");
            champPrenomsInscrits.setExemple("Ex: Alice, Emma, Agathe");
        } else {
            champPrenomsDemandes.setExemple("Ex: Sacha, Charlie, Alex");
            champPrenomsInscrits.setExemple("Ex: Tom, Noé, Max");
        }
    }

    private boolean auMoinsUnUsageSelectionne() {
        return caseUsageFamilial.isSelected() || caseUsageAmical.isSelected() || caseUsageProfessionnel.isSelected() || caseUsageScolaire.isSelected() || caseUsageAssociatif.isSelected();
    }

    private boolean estPlusieursPrenoms(String prenoms) {
        String valeur = prenoms == null ? "" : prenoms.trim();
        return valeur.contains(",") || valeur.contains(" ");
    }

    private String premiereValeurNonVide(String... valeurs) {
        if (valeurs == null) {
            return "";
        }
        for (String valeur : valeurs) {
            if (valeur != null && !valeur.trim().isEmpty()) {
                return valeur.trim();
            }
        }
        return "";
    }

    private void configurerAccessibiliteComposants() {
        configurerAccessibilite(champPrenomsDemandes, "Prénoms choisis", "Saisir les prénoms demandés");
        configurerAccessibilite(champPrenomsInscrits, "Prénoms à l'état civil", "Saisir les prénoms inscrits à l'état civil");
        configurerAccessibilite(champNom, "Nom", "Saisir le nom de famille");
        configurerAccessibilite(zoneAdressePostale, "Adresse postale", "Saisir l'adresse postale complète");
        configurerAccessibilite(ascenseurAdressePostale, "Adresse postale", "Zone multi-lignes pour l'adresse postale");
        configurerAccessibilite(champTelephonePortable, "Téléphone portable", "Saisir le numéro de téléphone portable");
        configurerAccessibilite(champCourriel, "Courriel", "Saisir l'adresse courriel");
        configurerAccessibilite(comboQualiteAvocat, "Qualité de l’avocat·e", "Choisir avocat, avocate ou avocat·e");
        configurerAccessibilite(champNomAvocat, "Nom de l’avocat·e", "Saisir le nom de l’avocat·e");
        configurerAccessibilite(champBarreauAvocat, "Barreau", "Saisir le barreau de rattachement");
        configurerAccessibilite(champAdresseAvocat, "Adresse de l’avocat·e", "Saisir l'adresse de l’avocat·e");
        configurerAccessibilite(champTelephoneAvocat, "Téléphone de l’avocat·e", "Saisir le téléphone de l’avocat·e");
        configurerAccessibilite(champCourrielAvocat, "Courriel de l’avocat·e", "Saisir le courriel de l’avocat·e");
        configurerAccessibilite(zoneAdresseTribunal, "Adresse du tribunal", "Saisir l'adresse du tribunal destinataire");
        configurerAccessibilite(ascenseurAdresseTribunal, "Adresse du tribunal", "Zone multi-lignes pour l'adresse du tribunal");
        configurerAccessibilite(champVilleTribunalJudiciaire, "Ville du tribunal judiciaire", "Saisir la ville du tribunal judiciaire");
        configurerAccessibilite(champVilleMairie, "Ville de la mairie", "Saisir la ville de la mairie qui a transmis le dossier");
        configurerAccessibilite(champVilleRedaction, "Ville de rédaction", "Saisir la ville de rédaction");
        configurerAccessibilite(selecteurDateNotificationRefus, "Date de notification du refus", "Sélecteur jour mois année de la notification du refus");
        configurerAccessibilite(selecteurDateRecepisseDepot, "Date du récépissé de dépôt", "Sélecteur jour mois année du récépissé de dépôt");
        configurerAccessibilite(selecteurDateDecisionRefus, "Date de la décision de refus", "Sélecteur jour mois année de la décision de refus");
        configurerAccessibilite(zoneMotifRefusNotifie, "Motif du refus notifié", "Saisir le motif du refus tel que notifié");
        configurerAccessibilite(ascenseurMotifRefusNotifie, "Motif du refus notifié", "Zone multi-lignes pour le motif du refus");
        configurerAccessibilite(zoneRaisonsContestation, "Raisons de contestation", "Saisir les raisons de contestation une par ligne");
        configurerAccessibilite(ascenseurRaisonsContestation, "Raisons de contestation", "Zone multi-lignes pour les raisons de contestation");
        configurerAccessibilite(comboGenreDemande, "Genre actuel", "Choisir le genre utilisé pour les accords");
        configurerAccessibilite(selecteurDateNaissance, "Date de naissance", "Sélecteur jour mois année de la date de naissance");
        configurerAccessibilite(champLieuNaissance, "Lieu de naissance", "Saisir le lieu de naissance");
        configurerAccessibilite(panneauUsages, "Sphères d'usage du prénom", "Sélectionner les sphères dans lesquelles le prénom d'usage est utilisé");
        configurerAccessibilite(zoneAnecdotesDifficultes, "Anecdotes facultatives", "Saisir des anecdotes facultatives illustrant les difficultés");
        configurerAccessibilite(ascenseurAnecdotesDifficultes, "Anecdotes facultatives", "Zone multi-lignes pour les anecdotes facultatives");
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
