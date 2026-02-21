package com.rdr.cecdoc.service.config;

import com.rdr.cecdoc.model.*;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public final class PersistanceEtatDossierProperties implements PersistanceEtatDossier {
    private static final String NOM_FICHIER = ".cecdoc.conf";
    private static final String NOM_FICHIER_LEGACY = "cecdoc.conf";
    private static final long TAILLE_MAX_CONFIGURATION_OCTETS = 1024L * 1024L;
    private static final int LIMITE_MAX_PIECES = 500;
    private static final int LIMITE_MAX_FICHIERS_PAR_PIECE = 500;
    private static final int LIMITE_MAX_ENFANTS = 10;
    private static final System.Logger JOURNAL = System.getLogger(PersistanceEtatDossierProperties.class.getName());

    private static final String CLE_SCHEMA_FORMAT = "conf.format";
    private static final String CLE_SCHEMA_VERSION = "conf.version";
    private static final String VALEUR_SCHEMA_FORMAT = "CECDOC";
    private static final String VALEUR_SCHEMA_VERSION = "2";

    private static final String PREF_APP = "app.";
    private static final String PREF_DOSSIER = "dossier.";
    private static final String PREF_LETTRE_UNIVERSITE = "form.universite.";
    private static final String PREF_LETTRE_ADMINISTRATION = "form.administration.";
    private static final String PREF_LETTRE_RELANCE_MAIRIE_PRENOM = "form.relanceMairiePrenom.";
    private static final String PREF_LETTRE_RGPD = "form.rgpd.";
    private static final String PREF_LETTRE_RELANCE_TRIBUNAL = "form.relanceTribunal.";
    private static final String PREF_LETTRE_MISE_A_JOUR_ACTES = "form.miseAJourActes.";
    private static final String PREF_RECOURS_PRENOM = "form.recoursRefusPrenom.";
    private static final String PREF_RECOURS_SEXE = "form.recoursRefusSexe.";

    private static final String CLE_PIECES_TITRES_NOMBRE = PREF_DOSSIER + "piecesTitres.count";
    private static final String CLE_PIECES_TITRES_PREFIXE = PREF_DOSSIER + "piecesTitres.";
    private static final String CLE_PIECES_DETAILLEES_NOMBRE = PREF_DOSSIER + "piecesDetaillees.count";
    private static final String CLE_PIECES_DETAILLEES_PREFIXE = PREF_DOSSIER + "piecesDetaillees.";
    private static final String CLE_ENFANTS_NOMBRE = PREF_LETTRE_MISE_A_JOUR_ACTES + "enfants.count";
    private static final String CLE_ENFANTS_PREFIXE = PREF_LETTRE_MISE_A_JOUR_ACTES + "enfants.";

    private final Path cheminConfig;
    private final Path cheminConfigLegacy;
    private final AtomicBoolean configurationObsoleteSupprimee;

    public PersistanceEtatDossierProperties() {
        this(resoudreCheminConfigParDefaut());
    }

    PersistanceEtatDossierProperties(Path cheminConfig) {
        this.cheminConfig = Objects.requireNonNull(cheminConfig, "cheminConfig");
        this.cheminConfigLegacy = resoudreCheminConfigLegacy(cheminConfig);
        this.configurationObsoleteSupprimee = new AtomicBoolean(false);
    }

    public static Properties convertirEtatEnProprietes(EtatDossierPersistant etat) {
        Objects.requireNonNull(etat, "etat");
        return ecrireProprietes(etat);
    }

    public static EtatDossierPersistant convertirProprietesEnEtat(Properties proprietes) {
        Objects.requireNonNull(proprietes, "proprietes");
        if (!estSchemaCompatible(proprietes)) {
            throw new IllegalArgumentException("Format de configuration obsolète.");
        }
        return lireEtat(proprietes);
    }

    private static boolean estSchemaCompatible(Properties proprietes) {
        String format = valeur(proprietes, CLE_SCHEMA_FORMAT);
        String version = valeur(proprietes, CLE_SCHEMA_VERSION);
        return VALEUR_SCHEMA_FORMAT.equals(format) && VALEUR_SCHEMA_VERSION.equals(version);
    }

    private static EtatDossierPersistant lireEtat(Properties proprietes) {
        InstantaneDossier instantaneDossier = lireInstantaneDossier(proprietes);
        return new EtatDossierPersistant(instantaneDossier, valeurBooleenne(proprietes, PREF_APP + "effacerApresExport"), valeurBooleenne(proprietes, PREF_APP + "confirmerQuitterAvecDonnees", true), valeurBooleenne(proprietes, PREF_APP + "memoriserDonneesSaisies", true), valeur(proprietes, PREF_APP + "themeApplication"), valeur(proprietes, PREF_APP + "dossierSortieParDefaut"), lireInstantaneLettreUniversite(proprietes), lireInstantaneLettreAdministration(proprietes), lireInstantaneLettreRelanceMairiePrenom(proprietes), lireInstantaneLettreRgpdMinimisation(proprietes), lireInstantaneLettreRelanceTribunal(proprietes), lireInstantaneLettreMiseAJourActesLies(proprietes), lireInstantaneRecoursRefusChangementPrenom(proprietes), lireInstantaneRecoursRefusChangementSexe(proprietes));
    }

    private static InstantaneDossier lireInstantaneDossier(Properties proprietes) {
        List<String> piecesTitres = lireTitresPieces(proprietes);
        List<PieceJustificative> piecesDetaillees = lirePiecesDetaillees(proprietes);
        return new InstantaneDossier(valeurBooleenne(proprietes, PREF_DOSSIER + "changementPrenoms"), valeurBooleenne(proprietes, PREF_DOSSIER + "pronomNeutre"), valeur(proprietes, PREF_DOSSIER + "prenomsEtatCivil"), valeur(proprietes, PREF_DOSSIER + "prenomsUsage"), valeur(proprietes, PREF_DOSSIER + "nomFamille"), valeur(proprietes, PREF_DOSSIER + "dateNaissance"), valeur(proprietes, PREF_DOSSIER + "lieuNaissance"), valeur(proprietes, PREF_DOSSIER + "sexeEtatCivil"), valeur(proprietes, PREF_DOSSIER + "adresse"), valeur(proprietes, PREF_DOSSIER + "tribunal"), valeur(proprietes, PREF_DOSSIER + "recit"), valeur(proprietes, PREF_DOSSIER + "villeActuelle"), piecesTitres, valeur(proprietes, PREF_DOSSIER + "nationalite"), valeur(proprietes, PREF_DOSSIER + "profession"), valeur(proprietes, PREF_DOSSIER + "situationMatrimoniale"), valeur(proprietes, PREF_DOSSIER + "situationEnfants"), valeurBooleenne(proprietes, PREF_DOSSIER + "pacsContracte"), piecesDetaillees);
    }

    private static Properties ecrireProprietes(EtatDossierPersistant etat) {
        Properties proprietes = new Properties();
        proprietes.setProperty(CLE_SCHEMA_FORMAT, VALEUR_SCHEMA_FORMAT);
        proprietes.setProperty(CLE_SCHEMA_VERSION, VALEUR_SCHEMA_VERSION);

        ecrireEtatApplication(proprietes, etat);
        ecrireInstantaneDossier(proprietes, etat.instantane());
        ecrireInstantaneLettreUniversite(proprietes, etat.instantaneLettreUniversite());
        ecrireInstantaneLettreAdministration(proprietes, etat.instantaneLettreAdministration());
        ecrireInstantaneLettreRelanceMairiePrenom(proprietes, etat.instantaneLettreRelanceMairiePrenom());
        ecrireInstantaneLettreRgpdMinimisation(proprietes, etat.instantaneLettreRgpdMinimisation());
        ecrireInstantaneLettreRelanceTribunal(proprietes, etat.instantaneLettreRelanceTribunal());
        ecrireInstantaneLettreMiseAJourActesLies(proprietes, etat.instantaneLettreMiseAJourActesLies());
        ecrireInstantaneRecoursRefusChangementPrenom(proprietes, etat.instantaneRecoursRefusChangementPrenom());
        ecrireInstantaneRecoursRefusChangementSexe(proprietes, etat.instantaneRecoursRefusChangementSexe());

        return proprietes;
    }

    private static void ecrireEtatApplication(Properties proprietes, EtatDossierPersistant etat) {
        proprietes.setProperty(PREF_APP + "effacerApresExport", Boolean.toString(etat.effacerApresExport()));
        proprietes.setProperty(PREF_APP + "confirmerQuitterAvecDonnees", Boolean.toString(etat.confirmerQuitterAvecDonnees()));
        proprietes.setProperty(PREF_APP + "memoriserDonneesSaisies", Boolean.toString(etat.memoriserDonneesSaisies()));
        proprietes.setProperty(PREF_APP + "themeApplication", etat.themeApplication());
        proprietes.setProperty(PREF_APP + "dossierSortieParDefaut", etat.dossierSortieParDefaut());
    }

    private static void ecrireInstantaneDossier(Properties proprietes, InstantaneDossier instantane) {
        proprietes.setProperty(PREF_DOSSIER + "changementPrenoms", Boolean.toString(instantane.changementPrenoms()));
        proprietes.setProperty(PREF_DOSSIER + "pronomNeutre", Boolean.toString(instantane.pronomNeutre()));
        proprietes.setProperty(PREF_DOSSIER + "prenomsEtatCivil", instantane.prenomsEtatCivil());
        proprietes.setProperty(PREF_DOSSIER + "prenomsUsage", instantane.prenomsUsage());
        proprietes.setProperty(PREF_DOSSIER + "nomFamille", instantane.nomFamille());
        proprietes.setProperty(PREF_DOSSIER + "dateNaissance", instantane.dateNaissance());
        proprietes.setProperty(PREF_DOSSIER + "lieuNaissance", instantane.lieuNaissance());
        proprietes.setProperty(PREF_DOSSIER + "sexeEtatCivil", instantane.sexeEtatCivil());
        proprietes.setProperty(PREF_DOSSIER + "adresse", instantane.adresse());
        proprietes.setProperty(PREF_DOSSIER + "tribunal", instantane.tribunal());
        proprietes.setProperty(PREF_DOSSIER + "recit", instantane.recit());
        proprietes.setProperty(PREF_DOSSIER + "villeActuelle", instantane.villeActuelle());
        proprietes.setProperty(PREF_DOSSIER + "nationalite", instantane.nationalite());
        proprietes.setProperty(PREF_DOSSIER + "profession", instantane.profession());
        proprietes.setProperty(PREF_DOSSIER + "situationMatrimoniale", instantane.situationMatrimoniale());
        proprietes.setProperty(PREF_DOSSIER + "situationEnfants", instantane.situationEnfants());
        proprietes.setProperty(PREF_DOSSIER + "pacsContracte", Boolean.toString(instantane.pacsContracte()));

        List<String> titresPieces = instantane.piecesJustificatives();
        proprietes.setProperty(CLE_PIECES_TITRES_NOMBRE, Integer.toString(titresPieces.size()));
        for (int i = 0; i < titresPieces.size(); i++) {
            proprietes.setProperty(CLE_PIECES_TITRES_PREFIXE + i, titresPieces.get(i));
        }

        List<PieceJustificative> detailsPieces = instantane.piecesJustificativesDetaillees();
        proprietes.setProperty(CLE_PIECES_DETAILLEES_NOMBRE, Integer.toString(detailsPieces.size()));
        for (int i = 0; i < detailsPieces.size(); i++) {
            PieceJustificative piece = detailsPieces.get(i);
            String basePiece = CLE_PIECES_DETAILLEES_PREFIXE + i + ".";
            proprietes.setProperty(basePiece + "intitule", piece.intitule());
            List<PieceJointe> fichiersJoints = piece.piecesJointes();
            proprietes.setProperty(basePiece + "piecesJointes.count", Integer.toString(fichiersJoints.size()));
            for (int j = 0; j < fichiersJoints.size(); j++) {
                PieceJointe fichierJoint = fichiersJoints.get(j);
                String baseFichier = basePiece + "piecesJointes." + j + ".";
                proprietes.setProperty(baseFichier + "uri", fichierJoint.uri());
                proprietes.setProperty(baseFichier + "nom", fichierJoint.nomAffichage());
                proprietes.setProperty(baseFichier + "type", fichierJoint.type().code());
            }
        }
    }

    private static List<String> lireTitresPieces(Properties proprietes) {
        int nombre = bornerValeur(valeurEntiere(proprietes, CLE_PIECES_TITRES_NOMBRE, 0), LIMITE_MAX_PIECES);
        List<String> titres = new ArrayList<>(nombre);
        for (int i = 0; i < nombre; i++) {
            String titre = valeur(proprietes, CLE_PIECES_TITRES_PREFIXE + i);
            if (!titre.isBlank()) {
                titres.add(titre);
            }
        }
        return titres;
    }

    private static List<PieceJustificative> lirePiecesDetaillees(Properties proprietes) {
        int nombre = bornerValeur(valeurEntiere(proprietes, CLE_PIECES_DETAILLEES_NOMBRE, 0), LIMITE_MAX_PIECES);
        List<PieceJustificative> pieces = new ArrayList<>(nombre);
        for (int i = 0; i < nombre; i++) {
            String basePiece = CLE_PIECES_DETAILLEES_PREFIXE + i + ".";
            String intitule = valeur(proprietes, basePiece + "intitule");
            if (intitule.isBlank()) {
                continue;
            }
            int nombreFichiers = bornerValeur(valeurEntiere(proprietes, basePiece + "piecesJointes.count", 0), LIMITE_MAX_FICHIERS_PAR_PIECE);
            List<PieceJointe> fichiers = new ArrayList<>(nombreFichiers);
            for (int j = 0; j < nombreFichiers; j++) {
                String baseFichier = basePiece + "piecesJointes." + j + ".";
                String uri = valeur(proprietes, baseFichier + "uri");
                if (uri.isBlank()) {
                    continue;
                }
                fichiers.add(new PieceJointe(uri, valeur(proprietes, baseFichier + "nom"), TypePieceJointe.depuisCode(valeur(proprietes, baseFichier + "type"))));
            }
            pieces.add(new PieceJustificative(intitule, fichiers));
        }
        return pieces;
    }

    private static InstantaneLettreUniversite lireInstantaneLettreUniversite(Properties proprietes) {
        return new InstantaneLettreUniversite(valeur(proprietes, PREF_LETTRE_UNIVERSITE + "genreActuel"), valeur(proprietes, PREF_LETTRE_UNIVERSITE + "civiliteSouhaitee"), valeur(proprietes, PREF_LETTRE_UNIVERSITE + "prenomUsage"), valeur(proprietes, PREF_LETTRE_UNIVERSITE + "prenomEtatCivil"), valeur(proprietes, PREF_LETTRE_UNIVERSITE + "nom"), valeur(proprietes, PREF_LETTRE_UNIVERSITE + "adressePostale"), valeur(proprietes, PREF_LETTRE_UNIVERSITE + "telephonePortable"), valeur(proprietes, PREF_LETTRE_UNIVERSITE + "courriel"), valeur(proprietes, PREF_LETTRE_UNIVERSITE + "ine"), valeur(proprietes, PREF_LETTRE_UNIVERSITE + "nomUniversite"), valeur(proprietes, PREF_LETTRE_UNIVERSITE + "explicationParcours"), valeur(proprietes, PREF_LETTRE_UNIVERSITE + "villeActuelle"));
    }

    private static void ecrireInstantaneLettreUniversite(Properties proprietes, InstantaneLettreUniversite instantane) {
        InstantaneLettreUniversite valeurInstantane = instantane == null ? InstantaneLettreUniversite.vide() : instantane;
        proprietes.setProperty(PREF_LETTRE_UNIVERSITE + "genreActuel", valeurInstantane.genreActuel());
        proprietes.setProperty(PREF_LETTRE_UNIVERSITE + "civiliteSouhaitee", valeurInstantane.civiliteSouhaitee());
        proprietes.setProperty(PREF_LETTRE_UNIVERSITE + "prenomUsage", valeurInstantane.prenomUsage());
        proprietes.setProperty(PREF_LETTRE_UNIVERSITE + "prenomEtatCivil", valeurInstantane.prenomEtatCivil());
        proprietes.setProperty(PREF_LETTRE_UNIVERSITE + "nom", valeurInstantane.nom());
        proprietes.setProperty(PREF_LETTRE_UNIVERSITE + "adressePostale", valeurInstantane.adressePostale());
        proprietes.setProperty(PREF_LETTRE_UNIVERSITE + "telephonePortable", valeurInstantane.telephonePortable());
        proprietes.setProperty(PREF_LETTRE_UNIVERSITE + "courriel", valeurInstantane.courriel());
        proprietes.setProperty(PREF_LETTRE_UNIVERSITE + "ine", valeurInstantane.ine());
        proprietes.setProperty(PREF_LETTRE_UNIVERSITE + "nomUniversite", valeurInstantane.nomUniversite());
        proprietes.setProperty(PREF_LETTRE_UNIVERSITE + "explicationParcours", valeurInstantane.explicationParcours());
        proprietes.setProperty(PREF_LETTRE_UNIVERSITE + "villeActuelle", valeurInstantane.villeActuelle());
    }

    private static InstantaneLettreAdministration lireInstantaneLettreAdministration(Properties proprietes) {
        return new InstantaneLettreAdministration(valeur(proprietes, PREF_LETTRE_ADMINISTRATION + "prenomUsage"), valeur(proprietes, PREF_LETTRE_ADMINISTRATION + "prenomsEtatCivil"), valeur(proprietes, PREF_LETTRE_ADMINISTRATION + "nom"), valeur(proprietes, PREF_LETTRE_ADMINISTRATION + "adressePostale"), valeur(proprietes, PREF_LETTRE_ADMINISTRATION + "telephonePortable"), valeur(proprietes, PREF_LETTRE_ADMINISTRATION + "courriel"), valeur(proprietes, PREF_LETTRE_ADMINISTRATION + "adresseDestinataire"), valeurBooleenne(proprietes, PREF_LETTRE_ADMINISTRATION + "changementPrenom"), valeur(proprietes, PREF_LETTRE_ADMINISTRATION + "prenomNaissance"), valeurBooleenne(proprietes, PREF_LETTRE_ADMINISTRATION + "changementSexe"), valeur(proprietes, PREF_LETTRE_ADMINISTRATION + "sexeAvant"), valeur(proprietes, PREF_LETTRE_ADMINISTRATION + "sexeApres"), valeurBooleenne(proprietes, PREF_LETTRE_ADMINISTRATION + "changementPrenomFaitEnMairie"), valeur(proprietes, PREF_LETTRE_ADMINISTRATION + "numeroDecisionMairie"), valeur(proprietes, PREF_LETTRE_ADMINISTRATION + "dateDecisionMairie"), valeur(proprietes, PREF_LETTRE_ADMINISTRATION + "tribunalCompetent"), valeur(proprietes, PREF_LETTRE_ADMINISTRATION + "numeroJugement"), valeur(proprietes, PREF_LETTRE_ADMINISTRATION + "villeActuelle"));
    }

    private static void ecrireInstantaneLettreAdministration(Properties proprietes, InstantaneLettreAdministration instantane) {
        InstantaneLettreAdministration valeurInstantane = instantane == null ? InstantaneLettreAdministration.vide() : instantane;
        proprietes.setProperty(PREF_LETTRE_ADMINISTRATION + "prenomUsage", valeurInstantane.prenomUsage());
        proprietes.setProperty(PREF_LETTRE_ADMINISTRATION + "prenomsEtatCivil", valeurInstantane.prenomsEtatCivil());
        proprietes.setProperty(PREF_LETTRE_ADMINISTRATION + "nom", valeurInstantane.nom());
        proprietes.setProperty(PREF_LETTRE_ADMINISTRATION + "adressePostale", valeurInstantane.adressePostale());
        proprietes.setProperty(PREF_LETTRE_ADMINISTRATION + "telephonePortable", valeurInstantane.telephonePortable());
        proprietes.setProperty(PREF_LETTRE_ADMINISTRATION + "courriel", valeurInstantane.courriel());
        proprietes.setProperty(PREF_LETTRE_ADMINISTRATION + "adresseDestinataire", valeurInstantane.adresseDestinataire());
        proprietes.setProperty(PREF_LETTRE_ADMINISTRATION + "changementPrenom", Boolean.toString(valeurInstantane.changementPrenom()));
        proprietes.setProperty(PREF_LETTRE_ADMINISTRATION + "prenomNaissance", valeurInstantane.prenomNaissance());
        proprietes.setProperty(PREF_LETTRE_ADMINISTRATION + "changementSexe", Boolean.toString(valeurInstantane.changementSexe()));
        proprietes.setProperty(PREF_LETTRE_ADMINISTRATION + "sexeAvant", valeurInstantane.sexeAvant());
        proprietes.setProperty(PREF_LETTRE_ADMINISTRATION + "sexeApres", valeurInstantane.sexeApres());
        proprietes.setProperty(PREF_LETTRE_ADMINISTRATION + "changementPrenomFaitEnMairie", Boolean.toString(valeurInstantane.changementPrenomFaitEnMairie()));
        proprietes.setProperty(PREF_LETTRE_ADMINISTRATION + "numeroDecisionMairie", valeurInstantane.numeroDecisionMairie());
        proprietes.setProperty(PREF_LETTRE_ADMINISTRATION + "dateDecisionMairie", valeurInstantane.dateDecisionMairie());
        proprietes.setProperty(PREF_LETTRE_ADMINISTRATION + "tribunalCompetent", valeurInstantane.tribunalCompetent());
        proprietes.setProperty(PREF_LETTRE_ADMINISTRATION + "numeroJugement", valeurInstantane.numeroJugement());
        proprietes.setProperty(PREF_LETTRE_ADMINISTRATION + "villeActuelle", valeurInstantane.villeActuelle());
    }

    private static InstantaneLettreRelanceMairiePrenom lireInstantaneLettreRelanceMairiePrenom(Properties proprietes) {
        return new InstantaneLettreRelanceMairiePrenom(valeur(proprietes, PREF_LETTRE_RELANCE_MAIRIE_PRENOM + "prenomsEtatCivil"), valeur(proprietes, PREF_LETTRE_RELANCE_MAIRIE_PRENOM + "prenomsDemandes"), valeur(proprietes, PREF_LETTRE_RELANCE_MAIRIE_PRENOM + "nom"), valeur(proprietes, PREF_LETTRE_RELANCE_MAIRIE_PRENOM + "adressePostale"), valeur(proprietes, PREF_LETTRE_RELANCE_MAIRIE_PRENOM + "telephonePortable"), valeur(proprietes, PREF_LETTRE_RELANCE_MAIRIE_PRENOM + "courriel"), valeur(proprietes, PREF_LETTRE_RELANCE_MAIRIE_PRENOM + "adresseMairie"), valeur(proprietes, PREF_LETTRE_RELANCE_MAIRIE_PRENOM + "villeRedaction"), valeur(proprietes, PREF_LETTRE_RELANCE_MAIRIE_PRENOM + "dateRedaction"), valeur(proprietes, PREF_LETTRE_RELANCE_MAIRIE_PRENOM + "dateDemande"), valeur(proprietes, PREF_LETTRE_RELANCE_MAIRIE_PRENOM + "genreActuel"), valeur(proprietes, PREF_LETTRE_RELANCE_MAIRIE_PRENOM + "dateNaissance"), valeur(proprietes, PREF_LETTRE_RELANCE_MAIRIE_PRENOM + "lieuNaissance"), valeur(proprietes, PREF_LETTRE_RELANCE_MAIRIE_PRENOM + "referenceDossier"));
    }

    private static void ecrireInstantaneLettreRelanceMairiePrenom(Properties proprietes, InstantaneLettreRelanceMairiePrenom instantane) {
        InstantaneLettreRelanceMairiePrenom valeurInstantane = instantane == null ? InstantaneLettreRelanceMairiePrenom.vide() : instantane;
        proprietes.setProperty(PREF_LETTRE_RELANCE_MAIRIE_PRENOM + "prenomsEtatCivil", valeurInstantane.prenomsEtatCivil());
        proprietes.setProperty(PREF_LETTRE_RELANCE_MAIRIE_PRENOM + "prenomsDemandes", valeurInstantane.prenomsDemandes());
        proprietes.setProperty(PREF_LETTRE_RELANCE_MAIRIE_PRENOM + "nom", valeurInstantane.nom());
        proprietes.setProperty(PREF_LETTRE_RELANCE_MAIRIE_PRENOM + "adressePostale", valeurInstantane.adressePostale());
        proprietes.setProperty(PREF_LETTRE_RELANCE_MAIRIE_PRENOM + "telephonePortable", valeurInstantane.telephonePortable());
        proprietes.setProperty(PREF_LETTRE_RELANCE_MAIRIE_PRENOM + "courriel", valeurInstantane.courriel());
        proprietes.setProperty(PREF_LETTRE_RELANCE_MAIRIE_PRENOM + "adresseMairie", valeurInstantane.adresseMairie());
        proprietes.setProperty(PREF_LETTRE_RELANCE_MAIRIE_PRENOM + "villeRedaction", valeurInstantane.villeRedaction());
        proprietes.setProperty(PREF_LETTRE_RELANCE_MAIRIE_PRENOM + "dateRedaction", valeurInstantane.dateRedaction());
        proprietes.setProperty(PREF_LETTRE_RELANCE_MAIRIE_PRENOM + "dateDemande", valeurInstantane.dateDemande());
        proprietes.setProperty(PREF_LETTRE_RELANCE_MAIRIE_PRENOM + "genreActuel", valeurInstantane.genreActuel());
        proprietes.setProperty(PREF_LETTRE_RELANCE_MAIRIE_PRENOM + "dateNaissance", valeurInstantane.dateNaissance());
        proprietes.setProperty(PREF_LETTRE_RELANCE_MAIRIE_PRENOM + "lieuNaissance", valeurInstantane.lieuNaissance());
        proprietes.setProperty(PREF_LETTRE_RELANCE_MAIRIE_PRENOM + "referenceDossier", valeurInstantane.referenceDossier());
    }

    private static InstantaneLettreRgpdMinimisation lireInstantaneLettreRgpdMinimisation(Properties proprietes) {
        String prenomsEtatCivil = valeur(proprietes, PREF_LETTRE_RGPD + "prenomsEtatCivil");
        if (prenomsEtatCivil.isBlank()) {
            prenomsEtatCivil = valeur(proprietes, PREF_LETTRE_RGPD + "prenomEtatCivil");
        }
        return new InstantaneLettreRgpdMinimisation(prenomsEtatCivil, valeur(proprietes, PREF_LETTRE_RGPD + "prenomsConnusOrganisme"), valeur(proprietes, PREF_LETTRE_RGPD + "nom"), valeur(proprietes, PREF_LETTRE_RGPD + "adressePostale"), valeur(proprietes, PREF_LETTRE_RGPD + "telephonePortable"), valeur(proprietes, PREF_LETTRE_RGPD + "courriel"), valeur(proprietes, PREF_LETTRE_RGPD + "nomAdresseOrganisme"), valeur(proprietes, PREF_LETTRE_RGPD + "villeRedaction"), valeur(proprietes, PREF_LETTRE_RGPD + "dateRedaction"), valeur(proprietes, PREF_LETTRE_RGPD + "genreDemande"), valeur(proprietes, PREF_LETTRE_RGPD + "dateNaissance"), valeur(proprietes, PREF_LETTRE_RGPD + "lieuNaissance"), valeur(proprietes, PREF_LETTRE_RGPD + "sexeEtatCivil"), valeur(proprietes, PREF_LETTRE_RGPD + "civiliteAffichage"), valeurBooleenne(proprietes, PREF_LETTRE_RGPD + "champsCiviliteGenrePresents", true));
    }

    private static void ecrireInstantaneLettreRgpdMinimisation(Properties proprietes, InstantaneLettreRgpdMinimisation instantane) {
        InstantaneLettreRgpdMinimisation valeurInstantane = instantane == null ? InstantaneLettreRgpdMinimisation.vide() : instantane;
        proprietes.setProperty(PREF_LETTRE_RGPD + "prenomsEtatCivil", valeurInstantane.prenomsEtatCivil());
        proprietes.setProperty(PREF_LETTRE_RGPD + "prenomEtatCivil", valeurInstantane.prenomsEtatCivil());
        proprietes.setProperty(PREF_LETTRE_RGPD + "prenomsConnusOrganisme", valeurInstantane.prenomsConnusOrganisme());
        proprietes.setProperty(PREF_LETTRE_RGPD + "nom", valeurInstantane.nom());
        proprietes.setProperty(PREF_LETTRE_RGPD + "adressePostale", valeurInstantane.adressePostale());
        proprietes.setProperty(PREF_LETTRE_RGPD + "telephonePortable", valeurInstantane.telephonePortable());
        proprietes.setProperty(PREF_LETTRE_RGPD + "courriel", valeurInstantane.courriel());
        proprietes.setProperty(PREF_LETTRE_RGPD + "nomAdresseOrganisme", valeurInstantane.nomAdresseOrganisme());
        proprietes.setProperty(PREF_LETTRE_RGPD + "villeRedaction", valeurInstantane.villeRedaction());
        proprietes.setProperty(PREF_LETTRE_RGPD + "dateRedaction", valeurInstantane.dateRedaction());
        proprietes.setProperty(PREF_LETTRE_RGPD + "genreDemande", valeurInstantane.genreDemande());
        proprietes.setProperty(PREF_LETTRE_RGPD + "dateNaissance", valeurInstantane.dateNaissance());
        proprietes.setProperty(PREF_LETTRE_RGPD + "lieuNaissance", valeurInstantane.lieuNaissance());
        proprietes.setProperty(PREF_LETTRE_RGPD + "sexeEtatCivil", valeurInstantane.sexeEtatCivil());
        proprietes.setProperty(PREF_LETTRE_RGPD + "civiliteAffichage", valeurInstantane.civiliteAffichage());
        proprietes.setProperty(PREF_LETTRE_RGPD + "champsCiviliteGenrePresents", Boolean.toString(valeurInstantane.champsCiviliteGenrePresents()));
    }

    private static InstantaneLettreRelanceTribunal lireInstantaneLettreRelanceTribunal(Properties proprietes) {
        return new InstantaneLettreRelanceTribunal(valeur(proprietes, PREF_LETTRE_RELANCE_TRIBUNAL + "prenom"), valeur(proprietes, PREF_LETTRE_RELANCE_TRIBUNAL + "nom"), valeur(proprietes, PREF_LETTRE_RELANCE_TRIBUNAL + "adressePostale"), valeur(proprietes, PREF_LETTRE_RELANCE_TRIBUNAL + "telephonePortable"), valeur(proprietes, PREF_LETTRE_RELANCE_TRIBUNAL + "courriel"), valeur(proprietes, PREF_LETTRE_RELANCE_TRIBUNAL + "adresseTribunal"), valeur(proprietes, PREF_LETTRE_RELANCE_TRIBUNAL + "villeRedaction"), valeur(proprietes, PREF_LETTRE_RELANCE_TRIBUNAL + "dateRedaction"), valeur(proprietes, PREF_LETTRE_RELANCE_TRIBUNAL + "dateDepotEnvoi"), valeur(proprietes, PREF_LETTRE_RELANCE_TRIBUNAL + "informationAttendue"), valeur(proprietes, PREF_LETTRE_RELANCE_TRIBUNAL + "genreRevendique"), valeur(proprietes, PREF_LETTRE_RELANCE_TRIBUNAL + "dateNaissance"), valeur(proprietes, PREF_LETTRE_RELANCE_TRIBUNAL + "lieuNaissance"), valeur(proprietes, PREF_LETTRE_RELANCE_TRIBUNAL + "referenceDossier"), valeurBooleenne(proprietes, PREF_LETTRE_RELANCE_TRIBUNAL + "changementPrenoms"), valeur(proprietes, PREF_LETTRE_RELANCE_TRIBUNAL + "prenomsEtatCivil"));
    }

    private static void ecrireInstantaneLettreRelanceTribunal(Properties proprietes, InstantaneLettreRelanceTribunal instantane) {
        InstantaneLettreRelanceTribunal valeurInstantane = instantane == null ? InstantaneLettreRelanceTribunal.vide() : instantane;
        proprietes.setProperty(PREF_LETTRE_RELANCE_TRIBUNAL + "prenom", valeurInstantane.prenom());
        proprietes.setProperty(PREF_LETTRE_RELANCE_TRIBUNAL + "nom", valeurInstantane.nom());
        proprietes.setProperty(PREF_LETTRE_RELANCE_TRIBUNAL + "adressePostale", valeurInstantane.adressePostale());
        proprietes.setProperty(PREF_LETTRE_RELANCE_TRIBUNAL + "telephonePortable", valeurInstantane.telephonePortable());
        proprietes.setProperty(PREF_LETTRE_RELANCE_TRIBUNAL + "courriel", valeurInstantane.courriel());
        proprietes.setProperty(PREF_LETTRE_RELANCE_TRIBUNAL + "adresseTribunal", valeurInstantane.adresseTribunal());
        proprietes.setProperty(PREF_LETTRE_RELANCE_TRIBUNAL + "villeRedaction", valeurInstantane.villeRedaction());
        proprietes.setProperty(PREF_LETTRE_RELANCE_TRIBUNAL + "dateRedaction", valeurInstantane.dateRedaction());
        proprietes.setProperty(PREF_LETTRE_RELANCE_TRIBUNAL + "dateDepotEnvoi", valeurInstantane.dateDepotEnvoi());
        proprietes.setProperty(PREF_LETTRE_RELANCE_TRIBUNAL + "informationAttendue", valeurInstantane.informationAttendue());
        proprietes.setProperty(PREF_LETTRE_RELANCE_TRIBUNAL + "genreRevendique", valeurInstantane.genreRevendique());
        proprietes.setProperty(PREF_LETTRE_RELANCE_TRIBUNAL + "dateNaissance", valeurInstantane.dateNaissance());
        proprietes.setProperty(PREF_LETTRE_RELANCE_TRIBUNAL + "lieuNaissance", valeurInstantane.lieuNaissance());
        proprietes.setProperty(PREF_LETTRE_RELANCE_TRIBUNAL + "referenceDossier", valeurInstantane.referenceDossier());
        proprietes.setProperty(PREF_LETTRE_RELANCE_TRIBUNAL + "changementPrenoms", Boolean.toString(valeurInstantane.changementPrenoms()));
        proprietes.setProperty(PREF_LETTRE_RELANCE_TRIBUNAL + "prenomsEtatCivil", valeurInstantane.prenomsEtatCivil());
    }

    private static InstantaneLettreMiseAJourActesLies lireInstantaneLettreMiseAJourActesLies(Properties proprietes) {
        return new InstantaneLettreMiseAJourActesLies(valeur(proprietes, PREF_LETTRE_MISE_A_JOUR_ACTES + "prenom"), valeur(proprietes, PREF_LETTRE_MISE_A_JOUR_ACTES + "nom"), valeur(proprietes, PREF_LETTRE_MISE_A_JOUR_ACTES + "adressePostale"), valeur(proprietes, PREF_LETTRE_MISE_A_JOUR_ACTES + "telephonePortable"), valeur(proprietes, PREF_LETTRE_MISE_A_JOUR_ACTES + "courriel"), valeur(proprietes, PREF_LETTRE_MISE_A_JOUR_ACTES + "typeDestinataire"), valeur(proprietes, PREF_LETTRE_MISE_A_JOUR_ACTES + "villeAutoriteDestinataire"), valeur(proprietes, PREF_LETTRE_MISE_A_JOUR_ACTES + "adresseDestinataire"), valeur(proprietes, PREF_LETTRE_MISE_A_JOUR_ACTES + "villeRedaction"), valeur(proprietes, PREF_LETTRE_MISE_A_JOUR_ACTES + "dateRedaction"), valeur(proprietes, PREF_LETTRE_MISE_A_JOUR_ACTES + "genreAccords"), valeur(proprietes, PREF_LETTRE_MISE_A_JOUR_ACTES + "dateNaissance"), valeur(proprietes, PREF_LETTRE_MISE_A_JOUR_ACTES + "lieuNaissance"), valeur(proprietes, PREF_LETTRE_MISE_A_JOUR_ACTES + "autoriteDecision"), valeur(proprietes, PREF_LETTRE_MISE_A_JOUR_ACTES + "dateDecision"), valeur(proprietes, PREF_LETTRE_MISE_A_JOUR_ACTES + "dateDecisionDefinitive"), valeurBooleenne(proprietes, PREF_LETTRE_MISE_A_JOUR_ACTES + "changementPrenoms"), valeurBooleenne(proprietes, PREF_LETTRE_MISE_A_JOUR_ACTES + "changementSexe"), valeurBooleenne(proprietes, PREF_LETTRE_MISE_A_JOUR_ACTES + "acteNaissanceRequerant"), valeur(proprietes, PREF_LETTRE_MISE_A_JOUR_ACTES + "communeNaissanceRequerant"), valeur(proprietes, PREF_LETTRE_MISE_A_JOUR_ACTES + "anneeNaissanceRequerant"), valeurBooleenne(proprietes, PREF_LETTRE_MISE_A_JOUR_ACTES + "concernePartenaire"), valeur(proprietes, PREF_LETTRE_MISE_A_JOUR_ACTES + "lienPartenaire"), valeur(proprietes, PREF_LETTRE_MISE_A_JOUR_ACTES + "genrePartenaire"), valeurBooleenne(proprietes, PREF_LETTRE_MISE_A_JOUR_ACTES + "acteMariage"), valeur(proprietes, PREF_LETTRE_MISE_A_JOUR_ACTES + "communeMariage"), valeur(proprietes, PREF_LETTRE_MISE_A_JOUR_ACTES + "dateMariage"), valeurBooleenne(proprietes, PREF_LETTRE_MISE_A_JOUR_ACTES + "acteNaissancePartenaire"), valeur(proprietes, PREF_LETTRE_MISE_A_JOUR_ACTES + "prenomPartenaire"), valeur(proprietes, PREF_LETTRE_MISE_A_JOUR_ACTES + "nomPartenaire"), valeur(proprietes, PREF_LETTRE_MISE_A_JOUR_ACTES + "communeNaissancePartenaire"), valeur(proprietes, PREF_LETTRE_MISE_A_JOUR_ACTES + "anneeNaissancePartenaire"), valeurBooleenne(proprietes, PREF_LETTRE_MISE_A_JOUR_ACTES + "mentionPacs"), valeur(proprietes, PREF_LETTRE_MISE_A_JOUR_ACTES + "autoritePacs"), valeur(proprietes, PREF_LETTRE_MISE_A_JOUR_ACTES + "optionLivret"), lireInstantaneEnfants(proprietes));
    }

    private static void ecrireInstantaneLettreMiseAJourActesLies(Properties proprietes, InstantaneLettreMiseAJourActesLies instantane) {
        InstantaneLettreMiseAJourActesLies valeurInstantane = instantane == null ? InstantaneLettreMiseAJourActesLies.vide() : instantane;
        proprietes.setProperty(PREF_LETTRE_MISE_A_JOUR_ACTES + "prenom", valeurInstantane.prenom());
        proprietes.setProperty(PREF_LETTRE_MISE_A_JOUR_ACTES + "nom", valeurInstantane.nom());
        proprietes.setProperty(PREF_LETTRE_MISE_A_JOUR_ACTES + "adressePostale", valeurInstantane.adressePostale());
        proprietes.setProperty(PREF_LETTRE_MISE_A_JOUR_ACTES + "telephonePortable", valeurInstantane.telephonePortable());
        proprietes.setProperty(PREF_LETTRE_MISE_A_JOUR_ACTES + "courriel", valeurInstantane.courriel());
        proprietes.setProperty(PREF_LETTRE_MISE_A_JOUR_ACTES + "typeDestinataire", valeurInstantane.typeDestinataire());
        proprietes.setProperty(PREF_LETTRE_MISE_A_JOUR_ACTES + "villeAutoriteDestinataire", valeurInstantane.villeAutoriteDestinataire());
        proprietes.setProperty(PREF_LETTRE_MISE_A_JOUR_ACTES + "adresseDestinataire", valeurInstantane.adresseDestinataire());
        proprietes.setProperty(PREF_LETTRE_MISE_A_JOUR_ACTES + "villeRedaction", valeurInstantane.villeRedaction());
        proprietes.setProperty(PREF_LETTRE_MISE_A_JOUR_ACTES + "dateRedaction", valeurInstantane.dateRedaction());
        proprietes.setProperty(PREF_LETTRE_MISE_A_JOUR_ACTES + "genreAccords", valeurInstantane.genreAccords());
        proprietes.setProperty(PREF_LETTRE_MISE_A_JOUR_ACTES + "dateNaissance", valeurInstantane.dateNaissance());
        proprietes.setProperty(PREF_LETTRE_MISE_A_JOUR_ACTES + "lieuNaissance", valeurInstantane.lieuNaissance());
        proprietes.setProperty(PREF_LETTRE_MISE_A_JOUR_ACTES + "autoriteDecision", valeurInstantane.autoriteDecision());
        proprietes.setProperty(PREF_LETTRE_MISE_A_JOUR_ACTES + "dateDecision", valeurInstantane.dateDecision());
        proprietes.setProperty(PREF_LETTRE_MISE_A_JOUR_ACTES + "dateDecisionDefinitive", valeurInstantane.dateDecisionDefinitive());
        proprietes.setProperty(PREF_LETTRE_MISE_A_JOUR_ACTES + "changementPrenoms", Boolean.toString(valeurInstantane.changementPrenoms()));
        proprietes.setProperty(PREF_LETTRE_MISE_A_JOUR_ACTES + "changementSexe", Boolean.toString(valeurInstantane.changementSexe()));
        proprietes.setProperty(PREF_LETTRE_MISE_A_JOUR_ACTES + "acteNaissanceRequerant", Boolean.toString(valeurInstantane.acteNaissanceRequerant()));
        proprietes.setProperty(PREF_LETTRE_MISE_A_JOUR_ACTES + "communeNaissanceRequerant", valeurInstantane.communeNaissanceRequerant());
        proprietes.setProperty(PREF_LETTRE_MISE_A_JOUR_ACTES + "anneeNaissanceRequerant", valeurInstantane.anneeNaissanceRequerant());
        proprietes.setProperty(PREF_LETTRE_MISE_A_JOUR_ACTES + "concernePartenaire", Boolean.toString(valeurInstantane.concernePartenaire()));
        proprietes.setProperty(PREF_LETTRE_MISE_A_JOUR_ACTES + "lienPartenaire", valeurInstantane.lienPartenaire());
        proprietes.setProperty(PREF_LETTRE_MISE_A_JOUR_ACTES + "genrePartenaire", valeurInstantane.genrePartenaire());
        proprietes.setProperty(PREF_LETTRE_MISE_A_JOUR_ACTES + "acteMariage", Boolean.toString(valeurInstantane.acteMariage()));
        proprietes.setProperty(PREF_LETTRE_MISE_A_JOUR_ACTES + "communeMariage", valeurInstantane.communeMariage());
        proprietes.setProperty(PREF_LETTRE_MISE_A_JOUR_ACTES + "dateMariage", valeurInstantane.dateMariage());
        proprietes.setProperty(PREF_LETTRE_MISE_A_JOUR_ACTES + "acteNaissancePartenaire", Boolean.toString(valeurInstantane.acteNaissancePartenaire()));
        proprietes.setProperty(PREF_LETTRE_MISE_A_JOUR_ACTES + "prenomPartenaire", valeurInstantane.prenomPartenaire());
        proprietes.setProperty(PREF_LETTRE_MISE_A_JOUR_ACTES + "nomPartenaire", valeurInstantane.nomPartenaire());
        proprietes.setProperty(PREF_LETTRE_MISE_A_JOUR_ACTES + "communeNaissancePartenaire", valeurInstantane.communeNaissancePartenaire());
        proprietes.setProperty(PREF_LETTRE_MISE_A_JOUR_ACTES + "anneeNaissancePartenaire", valeurInstantane.anneeNaissancePartenaire());
        proprietes.setProperty(PREF_LETTRE_MISE_A_JOUR_ACTES + "mentionPacs", Boolean.toString(valeurInstantane.mentionPacs()));
        proprietes.setProperty(PREF_LETTRE_MISE_A_JOUR_ACTES + "autoritePacs", valeurInstantane.autoritePacs());
        proprietes.setProperty(PREF_LETTRE_MISE_A_JOUR_ACTES + "optionLivret", valeurInstantane.optionLivret());

        List<InstantaneEnfantActe> enfants = valeurInstantane.enfants();
        proprietes.setProperty(CLE_ENFANTS_NOMBRE, Integer.toString(enfants.size()));
        for (int i = 0; i < enfants.size(); i++) {
            InstantaneEnfantActe enfant = enfants.get(i);
            String baseEnfant = CLE_ENFANTS_PREFIXE + i + ".";
            proprietes.setProperty(baseEnfant + "prenom", enfant.prenom());
            proprietes.setProperty(baseEnfant + "nom", enfant.nom());
            proprietes.setProperty(baseEnfant + "communeNaissance", enfant.communeNaissance());
            proprietes.setProperty(baseEnfant + "dateNaissance", enfant.dateNaissance());
            proprietes.setProperty(baseEnfant + "genre", enfant.genre());
        }
    }

    private static List<InstantaneEnfantActe> lireInstantaneEnfants(Properties proprietes) {
        int nombreEnfants = bornerValeur(valeurEntiere(proprietes, CLE_ENFANTS_NOMBRE, 0), LIMITE_MAX_ENFANTS);
        List<InstantaneEnfantActe> enfants = new ArrayList<>(nombreEnfants);
        for (int i = 0; i < nombreEnfants; i++) {
            String baseEnfant = CLE_ENFANTS_PREFIXE + i + ".";
            InstantaneEnfantActe enfant = new InstantaneEnfantActe(valeur(proprietes, baseEnfant + "prenom"), valeur(proprietes, baseEnfant + "nom"), valeur(proprietes, baseEnfant + "communeNaissance"), valeur(proprietes, baseEnfant + "dateNaissance"), valeur(proprietes, baseEnfant + "genre"));
            if (!enfant.prenom().isBlank() || !enfant.nom().isBlank() || !enfant.communeNaissance().isBlank() || !enfant.dateNaissance().isBlank()) {
                enfants.add(enfant);
            }
        }
        return enfants;
    }

    private static InstantaneRecoursRefusChangementPrenom lireInstantaneRecoursRefusChangementPrenom(Properties proprietes) {
        return new InstantaneRecoursRefusChangementPrenom(valeur(proprietes, PREF_RECOURS_PRENOM + "prenom"), valeur(proprietes, PREF_RECOURS_PRENOM + "nom"), valeur(proprietes, PREF_RECOURS_PRENOM + "adressePostale"), valeur(proprietes, PREF_RECOURS_PRENOM + "telephonePortable"), valeur(proprietes, PREF_RECOURS_PRENOM + "courriel"), valeur(proprietes, PREF_RECOURS_PRENOM + "villeMairie"), valeur(proprietes, PREF_RECOURS_PRENOM + "adresseMairie"), valeur(proprietes, PREF_RECOURS_PRENOM + "villeRedaction"), valeur(proprietes, PREF_RECOURS_PRENOM + "dateRedaction"), valeur(proprietes, PREF_RECOURS_PRENOM + "genreDemande"), valeur(proprietes, PREF_RECOURS_PRENOM + "dateNaissance"), valeur(proprietes, PREF_RECOURS_PRENOM + "lieuNaissance"), valeur(proprietes, PREF_RECOURS_PRENOM + "prenomsInscrits"), valeur(proprietes, PREF_RECOURS_PRENOM + "prenomsDemandes"), valeurBooleenne(proprietes, PREF_RECOURS_PRENOM + "plusieursPrenomsInscrits"), valeurBooleenne(proprietes, PREF_RECOURS_PRENOM + "plusieursPrenomsDemandes"), valeur(proprietes, PREF_RECOURS_PRENOM + "qualiteAvocat"), valeur(proprietes, PREF_RECOURS_PRENOM + "nomAvocat"), valeur(proprietes, PREF_RECOURS_PRENOM + "barreauAvocat"), valeur(proprietes, PREF_RECOURS_PRENOM + "adresseAvocat"), valeur(proprietes, PREF_RECOURS_PRENOM + "telephoneAvocat"), valeur(proprietes, PREF_RECOURS_PRENOM + "courrielAvocat"), valeur(proprietes, PREF_RECOURS_PRENOM + "dateNotificationRefus"), valeur(proprietes, PREF_RECOURS_PRENOM + "villeTribunalJudiciaire"), valeur(proprietes, PREF_RECOURS_PRENOM + "motifRefusNotifie"), valeurBooleenne(proprietes, PREF_RECOURS_PRENOM + "usageFamilial"), valeurBooleenne(proprietes, PREF_RECOURS_PRENOM + "usageAmical"), valeurBooleenne(proprietes, PREF_RECOURS_PRENOM + "usageProfessionnel"), valeurBooleenne(proprietes, PREF_RECOURS_PRENOM + "usageScolaire"), valeurBooleenne(proprietes, PREF_RECOURS_PRENOM + "usageAssociatif"), valeur(proprietes, PREF_RECOURS_PRENOM + "anecdotesDifficultes"), valeur(proprietes, PREF_RECOURS_PRENOM + "raisonsContestation"), valeur(proprietes, PREF_RECOURS_PRENOM + "dateRecepisseDepot"), valeur(proprietes, PREF_RECOURS_PRENOM + "dateDecisionRefus"));
    }

    private static void ecrireInstantaneRecoursRefusChangementPrenom(Properties proprietes, InstantaneRecoursRefusChangementPrenom instantane) {
        InstantaneRecoursRefusChangementPrenom valeurInstantane = instantane == null ? InstantaneRecoursRefusChangementPrenom.vide() : instantane;
        proprietes.setProperty(PREF_RECOURS_PRENOM + "prenom", valeurInstantane.prenom());
        proprietes.setProperty(PREF_RECOURS_PRENOM + "nom", valeurInstantane.nom());
        proprietes.setProperty(PREF_RECOURS_PRENOM + "adressePostale", valeurInstantane.adressePostale());
        proprietes.setProperty(PREF_RECOURS_PRENOM + "telephonePortable", valeurInstantane.telephonePortable());
        proprietes.setProperty(PREF_RECOURS_PRENOM + "courriel", valeurInstantane.courriel());
        proprietes.setProperty(PREF_RECOURS_PRENOM + "villeMairie", valeurInstantane.villeMairie());
        proprietes.setProperty(PREF_RECOURS_PRENOM + "adresseMairie", valeurInstantane.adresseMairie());
        proprietes.setProperty(PREF_RECOURS_PRENOM + "villeRedaction", valeurInstantane.villeRedaction());
        proprietes.setProperty(PREF_RECOURS_PRENOM + "dateRedaction", valeurInstantane.dateRedaction());
        proprietes.setProperty(PREF_RECOURS_PRENOM + "genreDemande", valeurInstantane.genreDemande());
        proprietes.setProperty(PREF_RECOURS_PRENOM + "dateNaissance", valeurInstantane.dateNaissance());
        proprietes.setProperty(PREF_RECOURS_PRENOM + "lieuNaissance", valeurInstantane.lieuNaissance());
        proprietes.setProperty(PREF_RECOURS_PRENOM + "prenomsInscrits", valeurInstantane.prenomsInscrits());
        proprietes.setProperty(PREF_RECOURS_PRENOM + "prenomsDemandes", valeurInstantane.prenomsDemandes());
        proprietes.setProperty(PREF_RECOURS_PRENOM + "plusieursPrenomsInscrits", Boolean.toString(valeurInstantane.plusieursPrenomsInscrits()));
        proprietes.setProperty(PREF_RECOURS_PRENOM + "plusieursPrenomsDemandes", Boolean.toString(valeurInstantane.plusieursPrenomsDemandes()));
        proprietes.setProperty(PREF_RECOURS_PRENOM + "qualiteAvocat", valeurInstantane.qualiteAvocat());
        proprietes.setProperty(PREF_RECOURS_PRENOM + "nomAvocat", valeurInstantane.nomAvocat());
        proprietes.setProperty(PREF_RECOURS_PRENOM + "barreauAvocat", valeurInstantane.barreauAvocat());
        proprietes.setProperty(PREF_RECOURS_PRENOM + "adresseAvocat", valeurInstantane.adresseAvocat());
        proprietes.setProperty(PREF_RECOURS_PRENOM + "telephoneAvocat", valeurInstantane.telephoneAvocat());
        proprietes.setProperty(PREF_RECOURS_PRENOM + "courrielAvocat", valeurInstantane.courrielAvocat());
        proprietes.setProperty(PREF_RECOURS_PRENOM + "dateNotificationRefus", valeurInstantane.dateNotificationRefus());
        proprietes.setProperty(PREF_RECOURS_PRENOM + "villeTribunalJudiciaire", valeurInstantane.villeTribunalJudiciaire());
        proprietes.setProperty(PREF_RECOURS_PRENOM + "motifRefusNotifie", valeurInstantane.motifRefusNotifie());
        proprietes.setProperty(PREF_RECOURS_PRENOM + "usageFamilial", Boolean.toString(valeurInstantane.usageFamilial()));
        proprietes.setProperty(PREF_RECOURS_PRENOM + "usageAmical", Boolean.toString(valeurInstantane.usageAmical()));
        proprietes.setProperty(PREF_RECOURS_PRENOM + "usageProfessionnel", Boolean.toString(valeurInstantane.usageProfessionnel()));
        proprietes.setProperty(PREF_RECOURS_PRENOM + "usageScolaire", Boolean.toString(valeurInstantane.usageScolaire()));
        proprietes.setProperty(PREF_RECOURS_PRENOM + "usageAssociatif", Boolean.toString(valeurInstantane.usageAssociatif()));
        proprietes.setProperty(PREF_RECOURS_PRENOM + "anecdotesDifficultes", valeurInstantane.anecdotesDifficultes());
        proprietes.setProperty(PREF_RECOURS_PRENOM + "raisonsContestation", valeurInstantane.raisonsContestation());
        proprietes.setProperty(PREF_RECOURS_PRENOM + "dateRecepisseDepot", valeurInstantane.dateRecepisseDepot());
        proprietes.setProperty(PREF_RECOURS_PRENOM + "dateDecisionRefus", valeurInstantane.dateDecisionRefus());
    }

    private static InstantaneRecoursRefusChangementSexe lireInstantaneRecoursRefusChangementSexe(Properties proprietes) {
        return new InstantaneRecoursRefusChangementSexe(valeur(proprietes, PREF_RECOURS_SEXE + "prenom"), valeur(proprietes, PREF_RECOURS_SEXE + "nom"), valeur(proprietes, PREF_RECOURS_SEXE + "adressePostale"), valeur(proprietes, PREF_RECOURS_SEXE + "telephonePortable"), valeur(proprietes, PREF_RECOURS_SEXE + "courriel"), valeur(proprietes, PREF_RECOURS_SEXE + "villeCourAppel"), valeur(proprietes, PREF_RECOURS_SEXE + "adresseCourAppel"), valeur(proprietes, PREF_RECOURS_SEXE + "villeRedaction"), valeur(proprietes, PREF_RECOURS_SEXE + "dateRedaction"), valeur(proprietes, PREF_RECOURS_SEXE + "genreRevendique"), valeur(proprietes, PREF_RECOURS_SEXE + "dateNaissance"), valeur(proprietes, PREF_RECOURS_SEXE + "lieuNaissance"), valeur(proprietes, PREF_RECOURS_SEXE + "qualiteAvocat"), valeur(proprietes, PREF_RECOURS_SEXE + "nomAvocat"), valeur(proprietes, PREF_RECOURS_SEXE + "barreauAvocat"), valeur(proprietes, PREF_RECOURS_SEXE + "adresseAvocat"), valeur(proprietes, PREF_RECOURS_SEXE + "telephoneAvocat"), valeur(proprietes, PREF_RECOURS_SEXE + "courrielAvocat"), valeur(proprietes, PREF_RECOURS_SEXE + "villeTribunal"), valeur(proprietes, PREF_RECOURS_SEXE + "dateJugement"), valeur(proprietes, PREF_RECOURS_SEXE + "motifRefus"), valeurBooleenne(proprietes, PREF_RECOURS_SEXE + "changementPrenoms"), valeur(proprietes, PREF_RECOURS_SEXE + "numeroJugement"), valeur(proprietes, PREF_RECOURS_SEXE + "prenomsEtatCivil"), valeur(proprietes, PREF_RECOURS_SEXE + "prenomsDemandes"));
    }

    private static void ecrireInstantaneRecoursRefusChangementSexe(Properties proprietes, InstantaneRecoursRefusChangementSexe instantane) {
        InstantaneRecoursRefusChangementSexe valeurInstantane = instantane == null ? InstantaneRecoursRefusChangementSexe.vide() : instantane;
        proprietes.setProperty(PREF_RECOURS_SEXE + "prenom", valeurInstantane.prenom());
        proprietes.setProperty(PREF_RECOURS_SEXE + "nom", valeurInstantane.nom());
        proprietes.setProperty(PREF_RECOURS_SEXE + "adressePostale", valeurInstantane.adressePostale());
        proprietes.setProperty(PREF_RECOURS_SEXE + "telephonePortable", valeurInstantane.telephonePortable());
        proprietes.setProperty(PREF_RECOURS_SEXE + "courriel", valeurInstantane.courriel());
        proprietes.setProperty(PREF_RECOURS_SEXE + "villeCourAppel", valeurInstantane.villeCourAppel());
        proprietes.setProperty(PREF_RECOURS_SEXE + "adresseCourAppel", valeurInstantane.adresseCourAppel());
        proprietes.setProperty(PREF_RECOURS_SEXE + "villeRedaction", valeurInstantane.villeRedaction());
        proprietes.setProperty(PREF_RECOURS_SEXE + "dateRedaction", valeurInstantane.dateRedaction());
        proprietes.setProperty(PREF_RECOURS_SEXE + "genreRevendique", valeurInstantane.genreRevendique());
        proprietes.setProperty(PREF_RECOURS_SEXE + "dateNaissance", valeurInstantane.dateNaissance());
        proprietes.setProperty(PREF_RECOURS_SEXE + "lieuNaissance", valeurInstantane.lieuNaissance());
        proprietes.setProperty(PREF_RECOURS_SEXE + "qualiteAvocat", valeurInstantane.qualiteAvocat());
        proprietes.setProperty(PREF_RECOURS_SEXE + "nomAvocat", valeurInstantane.nomAvocat());
        proprietes.setProperty(PREF_RECOURS_SEXE + "barreauAvocat", valeurInstantane.barreauAvocat());
        proprietes.setProperty(PREF_RECOURS_SEXE + "adresseAvocat", valeurInstantane.adresseAvocat());
        proprietes.setProperty(PREF_RECOURS_SEXE + "telephoneAvocat", valeurInstantane.telephoneAvocat());
        proprietes.setProperty(PREF_RECOURS_SEXE + "courrielAvocat", valeurInstantane.courrielAvocat());
        proprietes.setProperty(PREF_RECOURS_SEXE + "villeTribunal", valeurInstantane.villeTribunal());
        proprietes.setProperty(PREF_RECOURS_SEXE + "dateJugement", valeurInstantane.dateJugement());
        proprietes.setProperty(PREF_RECOURS_SEXE + "motifRefus", valeurInstantane.motifRefus());
        proprietes.setProperty(PREF_RECOURS_SEXE + "changementPrenoms", Boolean.toString(valeurInstantane.changementPrenoms()));
        proprietes.setProperty(PREF_RECOURS_SEXE + "numeroJugement", valeurInstantane.numeroJugement());
        proprietes.setProperty(PREF_RECOURS_SEXE + "prenomsEtatCivil", valeurInstantane.prenomsEtatCivil());
        proprietes.setProperty(PREF_RECOURS_SEXE + "prenomsDemandes", valeurInstantane.prenomsDemandes());
    }

    private static String valeur(Properties proprietes, String cle) {
        return proprietes.getProperty(cle, "");
    }

    private static boolean valeurBooleenne(Properties proprietes, String cle) {
        return valeurBooleenne(proprietes, cle, false);
    }

    private static boolean valeurBooleenne(Properties proprietes, String cle, boolean valeurParDefaut) {
        String texte = valeur(proprietes, cle);
        if (texte.isBlank()) {
            return valeurParDefaut;
        }
        return Boolean.parseBoolean(texte);
    }

    private static int valeurEntiere(Properties proprietes, String cle, int valeurParDefaut) {
        String texte = valeur(proprietes, cle);
        if (texte.isBlank()) {
            return valeurParDefaut;
        }
        try {
            return Integer.parseInt(texte);
        } catch (NumberFormatException ex) {
            return valeurParDefaut;
        }
    }

    private static int bornerValeur(int valeur, int maximum) {
        if (valeur < 0) {
            return 0;
        }
        return Math.min(valeur, maximum);
    }

    private static Path resoudreCheminConfigParDefaut() {
        Path repertoireSecours = Path.of(System.getProperty("user.dir", ".")).toAbsolutePath().normalize();
        try {
            Path source = Path.of(PersistanceEtatDossierProperties.class.getProtectionDomain().getCodeSource().getLocation().toURI()).toAbsolutePath().normalize();
            Path repertoireExecutable = Files.isDirectory(source) ? source : source.getParent();
            if (repertoireExecutable == null) {
                repertoireExecutable = repertoireSecours;
            }
            return repertoireExecutable.resolve(NOM_FICHIER);
        } catch (URISyntaxException | IllegalArgumentException | SecurityException ex) {
            return repertoireSecours.resolve(NOM_FICHIER);
        }
    }

    private static Path resoudreCheminConfigLegacy(Path cheminConfig) {
        if (cheminConfig == null) {
            return null;
        }
        Path parent = cheminConfig.toAbsolutePath().normalize().getParent();
        if (parent == null) {
            return null;
        }
        Path legacy = parent.resolve(NOM_FICHIER_LEGACY);
        return legacy.equals(cheminConfig.toAbsolutePath().normalize()) ? null : legacy;
    }

    private static void marquerFichierCacheSousWindows(Path chemin) {
        String nomOs = System.getProperty("os.name", "").toLowerCase();
        if (!nomOs.contains("win")) {
            return;
        }
        try {
            Files.setAttribute(chemin, "dos:hidden", Boolean.TRUE);
        } catch (IOException | UnsupportedOperationException | SecurityException ex) {
            JOURNAL.log(System.Logger.Level.DEBUG, "Impossible de marquer le fichier de configuration comme caché : " + chemin, ex);
        }
    }

    @Override
    public Optional<EtatDossierPersistant> charger() {
        configurationObsoleteSupprimee.set(false);
        supprimerLegacySiPresent();
        if (!Files.isRegularFile(cheminConfig)) {
            return Optional.empty();
        }
        try {
            long taille = Files.size(cheminConfig);
            if (taille <= 0 || taille > TAILLE_MAX_CONFIGURATION_OCTETS) {
                supprimerConfigurationObsolete(cheminConfig);
                return Optional.empty();
            }
        } catch (IOException ex) {
            JOURNAL.log(System.Logger.Level.DEBUG, "Impossible de lire la taille de la configuration : " + cheminConfig, ex);
            return Optional.empty();
        }

        Properties proprietes = new Properties();
        try (BufferedInputStream entree = new BufferedInputStream(Files.newInputStream(cheminConfig))) {
            proprietes.load(entree);
        } catch (IOException ex) {
            JOURNAL.log(System.Logger.Level.DEBUG, "Impossible de charger la configuration : " + cheminConfig, ex);
            return Optional.empty();
        }

        if (!estSchemaCompatible(proprietes)) {
            supprimerConfigurationObsolete(cheminConfig);
            return Optional.empty();
        }

        try {
            return Optional.of(lireEtat(proprietes));
        } catch (RuntimeException ex) {
            JOURNAL.log(System.Logger.Level.WARNING, "Configuration persistée invalide, chargement ignoré : " + cheminConfig, ex);
            return Optional.empty();
        }
    }

    private void supprimerLegacySiPresent() {
        if (cheminConfigLegacy == null || !Files.exists(cheminConfigLegacy)) {
            return;
        }
        supprimerConfigurationObsolete(cheminConfigLegacy);
    }

    private void supprimerConfigurationObsolete(Path chemin) {
        try {
            Files.deleteIfExists(chemin);
            configurationObsoleteSupprimee.set(true);
        } catch (IOException ex) {
            JOURNAL.log(System.Logger.Level.WARNING, "Impossible de supprimer la configuration obsolète : " + chemin, ex);
        }
    }

    @Override
    public void sauvegarder(EtatDossierPersistant etat) {
        Objects.requireNonNull(etat, "etat");
        Properties proprietes = ecrireProprietes(etat);
        try {
            Path parent = cheminConfig.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            try (BufferedOutputStream sortie = new BufferedOutputStream(Files.newOutputStream(cheminConfig, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE))) {
                proprietes.store(sortie, null);
            }
            marquerFichierCacheSousWindows(cheminConfig);
        } catch (IOException ex) {
            JOURNAL.log(System.Logger.Level.WARNING, "Impossible de sauvegarder le fichier de configuration : " + cheminConfig, ex);
        }
    }

    @Override
    public void effacer() {
        try {
            Files.deleteIfExists(cheminConfig);
            if (cheminConfigLegacy != null) {
                Files.deleteIfExists(cheminConfigLegacy);
            }
        } catch (IOException ex) {
            JOURNAL.log(System.Logger.Level.WARNING, "Impossible d'effacer le fichier de configuration : " + cheminConfig, ex);
        }
    }

    @Override
    public boolean consommerSignalConfigurationObsoleteSupprimee() {
        return configurationObsoleteSupprimee.getAndSet(false);
    }
}
