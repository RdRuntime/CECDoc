package com.rdr.cecdoc.service.config;

import com.rdr.cecdoc.model.InstantaneDossier;
import com.rdr.cecdoc.model.InstantaneLettreAdministration;
import com.rdr.cecdoc.model.InstantaneLettreUniversite;
import com.rdr.cecdoc.model.PieceJointe;
import com.rdr.cecdoc.model.PieceJustificative;
import com.rdr.cecdoc.model.TypePieceJointe;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;

public final class PersistanceEtatDossierProperties implements PersistanceEtatDossier {
    private static final String NOM_FICHIER = "cecdoc.conf";
    private static final System.Logger JOURNAL = System.getLogger(PersistanceEtatDossierProperties.class.getName());

    private static final String CLE_CHANGEMENT_PRENOMS = "changementPrenoms";
    private static final String CLE_PRONOM_NEUTRE = "pronomNeutre";
    private static final String CLE_PRENOMS_ETAT_CIVIL = "prenomsEtatCivil";
    private static final String CLE_PRENOMS_CHOISIS = "prenomsUsage";
    private static final String CLE_NOM_FAMILLE = "nomFamille";
    private static final String CLE_DATE_NAISSANCE = "dateNaissance";
    private static final String CLE_LIEU_NAISSANCE = "lieuNaissance";
    private static final String CLE_SEXE_ETAT_CIVIL = "sexeEtatCivil";
    private static final String CLE_ADRESSE = "adresse";
    private static final String CLE_TRIBUNAL = "tribunal";
    private static final String CLE_RECIT = "recit";
    private static final String CLE_VILLE_ACTUELLE = "villeActuelle";
    private static final String CLE_NATIONALITE = "nationalite";
    private static final String CLE_PROFESSION = "profession";
    private static final String CLE_SITUATION_MATRIMONIALE = "situationMatrimoniale";
    private static final String CLE_SITUATION_ENFANTS = "situationEnfants";
    private static final String CLE_PACS_CONTRACTE = "pacsContracte";
    private static final String CLE_EFFACER_APRES_EXPORT = "effacerApresExport";
    private static final String CLE_CONFIRMER_QUITTER_AVEC_DONNEES = "confirmerQuitterAvecDonnees";
    private static final String CLE_MEMORISER_DONNEES_SAISIES = "memoriserDonneesSaisies";
    private static final String CLE_THEME_APPLICATION = "themeApplication";
    private static final String CLE_DOSSIER_SORTIE_PAR_DEFAUT = "dossierSortieParDefaut";
    private static final String CLE_LETTRE_UNIVERSITE_PREFIXE = "lettreUniversite.";
    private static final String CLE_LETTRE_ADMINISTRATION_PREFIXE = "lettreAdministration.";

    private static final String CLE_PIECES_JUSTIFICATIVES_NOMBRE = "piecesJustificatives.count";
    private static final String CLE_PIECES_JUSTIFICATIVES_PREFIXE = "piecesJustificatives.";

    private static final String CLE_PIECES_DETAILLEES_NOMBRE = "piecesJustificativesDetaillees.count";
    private static final String CLE_PIECES_DETAILLEES_PREFIXE = "piecesJustificativesDetaillees.";

    private final Path cheminConfig;

    public PersistanceEtatDossierProperties() {
        this(resoudreCheminConfigParDefaut());
    }

    PersistanceEtatDossierProperties(Path cheminConfig) {
        this.cheminConfig = Objects.requireNonNull(cheminConfig, "cheminConfig");
    }

    @Override
    public Optional<EtatDossierPersistant> charger() {
        if (!Files.isRegularFile(cheminConfig)) {
            return Optional.empty();
        }
        Properties proprietes = new Properties();
        try (BufferedInputStream entree = new BufferedInputStream(Files.newInputStream(cheminConfig))) {
            proprietes.load(entree);
            return Optional.of(lireEtat(proprietes));
        } catch (IOException | RuntimeException ex) {
            return Optional.empty();
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
        } catch (IOException ex) {
            JOURNAL.log(System.Logger.Level.WARNING, "Impossible de sauvegarder le fichier de configuration : " + cheminConfig, ex);
        }
    }

    @Override
    public void effacer() {
        try {
            Files.deleteIfExists(cheminConfig);
        } catch (IOException ex) {
            JOURNAL.log(System.Logger.Level.WARNING, "Impossible d'effacer le fichier de configuration : " + cheminConfig, ex);
        }
    }

    private EtatDossierPersistant lireEtat(Properties proprietes) {
        List<String> piecesJustificatives = lireTitresPieces(proprietes);
        List<PieceJustificative> piecesDetaillees = lirePiecesDetaillees(proprietes);

        InstantaneDossier instantane = new InstantaneDossier(valeurBooleenne(proprietes, CLE_CHANGEMENT_PRENOMS), valeurBooleenne(proprietes, CLE_PRONOM_NEUTRE), valeur(proprietes, CLE_PRENOMS_ETAT_CIVIL), valeur(proprietes, CLE_PRENOMS_CHOISIS), valeur(proprietes, CLE_NOM_FAMILLE), valeur(proprietes, CLE_DATE_NAISSANCE), valeur(proprietes, CLE_LIEU_NAISSANCE), valeur(proprietes, CLE_SEXE_ETAT_CIVIL), valeur(proprietes, CLE_ADRESSE), valeur(proprietes, CLE_TRIBUNAL), valeur(proprietes, CLE_RECIT), valeur(proprietes, CLE_VILLE_ACTUELLE), piecesJustificatives, valeur(proprietes, CLE_NATIONALITE), valeur(proprietes, CLE_PROFESSION), valeur(proprietes, CLE_SITUATION_MATRIMONIALE), valeur(proprietes, CLE_SITUATION_ENFANTS), valeurBooleenne(proprietes, CLE_PACS_CONTRACTE), piecesDetaillees);

        return new EtatDossierPersistant(instantane, valeurBooleenne(proprietes, CLE_EFFACER_APRES_EXPORT), valeurBooleenne(proprietes, CLE_CONFIRMER_QUITTER_AVEC_DONNEES, true), valeurBooleenne(proprietes, CLE_MEMORISER_DONNEES_SAISIES, true), valeur(proprietes, CLE_THEME_APPLICATION), valeur(proprietes, CLE_DOSSIER_SORTIE_PAR_DEFAUT), lireInstantaneLettreUniversite(proprietes), lireInstantaneLettreAdministration(proprietes));
    }

    private Properties ecrireProprietes(EtatDossierPersistant etat) {
        Properties proprietes = new Properties();
        InstantaneDossier instantane = etat.instantane();

        proprietes.setProperty(CLE_CHANGEMENT_PRENOMS, Boolean.toString(instantane.changementPrenoms()));
        proprietes.setProperty(CLE_PRONOM_NEUTRE, Boolean.toString(instantane.pronomNeutre()));
        proprietes.setProperty(CLE_PRENOMS_ETAT_CIVIL, instantane.prenomsEtatCivil());
        proprietes.setProperty(CLE_PRENOMS_CHOISIS, instantane.prenomsUsage());
        proprietes.setProperty(CLE_NOM_FAMILLE, instantane.nomFamille());
        proprietes.setProperty(CLE_DATE_NAISSANCE, instantane.dateNaissance());
        proprietes.setProperty(CLE_LIEU_NAISSANCE, instantane.lieuNaissance());
        proprietes.setProperty(CLE_SEXE_ETAT_CIVIL, instantane.sexeEtatCivil());
        proprietes.setProperty(CLE_ADRESSE, instantane.adresse());
        proprietes.setProperty(CLE_TRIBUNAL, instantane.tribunal());
        proprietes.setProperty(CLE_RECIT, instantane.recit());
        proprietes.setProperty(CLE_VILLE_ACTUELLE, instantane.villeActuelle());
        proprietes.setProperty(CLE_NATIONALITE, instantane.nationalite());
        proprietes.setProperty(CLE_PROFESSION, instantane.profession());
        proprietes.setProperty(CLE_SITUATION_MATRIMONIALE, instantane.situationMatrimoniale());
        proprietes.setProperty(CLE_SITUATION_ENFANTS, instantane.situationEnfants());
        proprietes.setProperty(CLE_PACS_CONTRACTE, Boolean.toString(instantane.pacsContracte()));
        proprietes.setProperty(CLE_EFFACER_APRES_EXPORT, Boolean.toString(etat.effacerApresExport()));
        proprietes.setProperty(CLE_CONFIRMER_QUITTER_AVEC_DONNEES, Boolean.toString(etat.confirmerQuitterAvecDonnees()));
        proprietes.setProperty(CLE_MEMORISER_DONNEES_SAISIES, Boolean.toString(etat.memoriserDonneesSaisies()));
        proprietes.setProperty(CLE_THEME_APPLICATION, etat.themeApplication());
        proprietes.setProperty(CLE_DOSSIER_SORTIE_PAR_DEFAUT, etat.dossierSortieParDefaut());
        ecrireInstantaneLettreUniversite(proprietes, etat.instantaneLettreUniversite());
        ecrireInstantaneLettreAdministration(proprietes, etat.instantaneLettreAdministration());

        List<String> piecesJustificatives = instantane.piecesJustificatives();
        proprietes.setProperty(CLE_PIECES_JUSTIFICATIVES_NOMBRE, Integer.toString(piecesJustificatives.size()));
        for (int i = 0; i < piecesJustificatives.size(); i++) {
            proprietes.setProperty(CLE_PIECES_JUSTIFICATIVES_PREFIXE + i, piecesJustificatives.get(i));
        }

        List<PieceJustificative> piecesDetaillees = instantane.piecesJustificativesDetaillees();
        proprietes.setProperty(CLE_PIECES_DETAILLEES_NOMBRE, Integer.toString(piecesDetaillees.size()));
        for (int i = 0; i < piecesDetaillees.size(); i++) {
            PieceJustificative piece = piecesDetaillees.get(i);
            String base = CLE_PIECES_DETAILLEES_PREFIXE + i + ".";
            proprietes.setProperty(base + "intitule", piece.intitule());
            List<PieceJointe> fichiersJoints = piece.piecesJointes();
            proprietes.setProperty(base + "piecesJointes.count", Integer.toString(fichiersJoints.size()));
            for (int j = 0; j < fichiersJoints.size(); j++) {
                PieceJointe fichierJoint = fichiersJoints.get(j);
                String baseFichier = base + "piecesJointes." + j + ".";
                proprietes.setProperty(baseFichier + "uri", fichierJoint.uri());
                proprietes.setProperty(baseFichier + "nom", fichierJoint.nomAffichage());
                proprietes.setProperty(baseFichier + "type", fichierJoint.type().code());
            }
        }

        return proprietes;
    }

    private static List<String> lireTitresPieces(Properties proprietes) {
        int nombrePieces = valeurEntiere(proprietes, CLE_PIECES_JUSTIFICATIVES_NOMBRE, 0);
        List<String> piecesJustificatives = new ArrayList<>(Math.max(0, nombrePieces));
        for (int i = 0; i < nombrePieces; i++) {
            String intitule = valeur(proprietes, CLE_PIECES_JUSTIFICATIVES_PREFIXE + i);
            if (!intitule.isBlank()) {
                piecesJustificatives.add(intitule);
            }
        }
        return piecesJustificatives;
    }

    private static List<PieceJustificative> lirePiecesDetaillees(Properties proprietes) {
        int nombrePieces = valeurEntiere(proprietes, CLE_PIECES_DETAILLEES_NOMBRE, 0);
        if (nombrePieces <= 0) {
            return List.of();
        }
        List<PieceJustificative> pieces = new ArrayList<>(nombrePieces);
        for (int i = 0; i < nombrePieces; i++) {
            String base = CLE_PIECES_DETAILLEES_PREFIXE + i + ".";
            String intitule = valeur(proprietes, base + "intitule").trim();
            if (intitule.isEmpty()) {
                continue;
            }

            int nombreFichiers = valeurEntiere(proprietes, base + "piecesJointes.count", 0);
            List<PieceJointe> fichiersJoints = new ArrayList<>(Math.max(0, nombreFichiers));
            for (int j = 0; j < nombreFichiers; j++) {
                String baseFichier = base + "piecesJointes." + j + ".";
                String uri = valeur(proprietes, baseFichier + "uri");
                String nom = valeur(proprietes, baseFichier + "nom");
                TypePieceJointe type = TypePieceJointe.depuisCode(valeur(proprietes, baseFichier + "type"));
                if (!uri.isBlank()) {
                    fichiersJoints.add(new PieceJointe(uri, nom, type));
                }
            }

            pieces.add(new PieceJustificative(intitule, fichiersJoints));
        }
        return pieces;
    }

    private static InstantaneLettreUniversite lireInstantaneLettreUniversite(Properties proprietes) {
        String prefixe = CLE_LETTRE_UNIVERSITE_PREFIXE;
        return new InstantaneLettreUniversite(valeur(proprietes, prefixe + "genreActuel"), valeur(proprietes, prefixe + "civiliteSouhaitee"), valeur(proprietes, prefixe + "prenomUsage"), valeur(proprietes, prefixe + "prenomEtatCivil"), valeur(proprietes, prefixe + "nom"), valeur(proprietes, prefixe + "adressePostale"), valeur(proprietes, prefixe + "telephonePortable"), valeur(proprietes, prefixe + "courriel"), valeur(proprietes, prefixe + "ine"), valeur(proprietes, prefixe + "nomUniversite"), valeur(proprietes, prefixe + "explicationParcours"), valeur(proprietes, prefixe + CLE_VILLE_ACTUELLE));
    }

    private static InstantaneLettreAdministration lireInstantaneLettreAdministration(Properties proprietes) {
        String prefixe = CLE_LETTRE_ADMINISTRATION_PREFIXE;
        return new InstantaneLettreAdministration(valeur(proprietes, prefixe + "prenomUsage"), valeur(proprietes, prefixe + CLE_PRENOMS_ETAT_CIVIL), valeur(proprietes, prefixe + "nom"), valeur(proprietes, prefixe + "adressePostale"), valeur(proprietes, prefixe + "telephonePortable"), valeur(proprietes, prefixe + "courriel"), valeur(proprietes, prefixe + "adresseDestinataire"), valeurBooleenne(proprietes, prefixe + "changementPrenom"), valeur(proprietes, prefixe + "prenomNaissance"), valeurBooleenne(proprietes, prefixe + "changementSexe"), valeur(proprietes, prefixe + "sexeAvant"), valeur(proprietes, prefixe + "sexeApres"), valeurBooleenne(proprietes, prefixe + "changementPrenomFaitEnMairie"), valeur(proprietes, prefixe + "numeroDecisionMairie"), valeur(proprietes, prefixe + "dateDecisionMairie"), valeur(proprietes, prefixe + "tribunalCompetent"), valeur(proprietes, prefixe + "numeroJugement"), valeur(proprietes, prefixe + CLE_VILLE_ACTUELLE));
    }

    private static void ecrireInstantaneLettreUniversite(Properties proprietes, InstantaneLettreUniversite instantane) {
        InstantaneLettreUniversite valeurInstantane = instantane == null ? InstantaneLettreUniversite.vide() : instantane;
        String prefixe = CLE_LETTRE_UNIVERSITE_PREFIXE;
        proprietes.setProperty(prefixe + "genreActuel", valeurInstantane.genreActuel());
        proprietes.setProperty(prefixe + "civiliteSouhaitee", valeurInstantane.civiliteSouhaitee());
        proprietes.setProperty(prefixe + "prenomUsage", valeurInstantane.prenomUsage());
        proprietes.setProperty(prefixe + "prenomEtatCivil", valeurInstantane.prenomEtatCivil());
        proprietes.setProperty(prefixe + "nom", valeurInstantane.nom());
        proprietes.setProperty(prefixe + "adressePostale", valeurInstantane.adressePostale());
        proprietes.setProperty(prefixe + "telephonePortable", valeurInstantane.telephonePortable());
        proprietes.setProperty(prefixe + "courriel", valeurInstantane.courriel());
        proprietes.setProperty(prefixe + "ine", valeurInstantane.ine());
        proprietes.setProperty(prefixe + "nomUniversite", valeurInstantane.nomUniversite());
        proprietes.setProperty(prefixe + "explicationParcours", valeurInstantane.explicationParcours());
        proprietes.setProperty(prefixe + CLE_VILLE_ACTUELLE, valeurInstantane.villeActuelle());
    }

    private static void ecrireInstantaneLettreAdministration(Properties proprietes, InstantaneLettreAdministration instantane) {
        InstantaneLettreAdministration valeurInstantane = instantane == null ? InstantaneLettreAdministration.vide() : instantane;
        String prefixe = CLE_LETTRE_ADMINISTRATION_PREFIXE;
        proprietes.setProperty(prefixe + "prenomUsage", valeurInstantane.prenomUsage());
        proprietes.setProperty(prefixe + CLE_PRENOMS_ETAT_CIVIL, valeurInstantane.prenomsEtatCivil());
        proprietes.setProperty(prefixe + "nom", valeurInstantane.nom());
        proprietes.setProperty(prefixe + "adressePostale", valeurInstantane.adressePostale());
        proprietes.setProperty(prefixe + "telephonePortable", valeurInstantane.telephonePortable());
        proprietes.setProperty(prefixe + "courriel", valeurInstantane.courriel());
        proprietes.setProperty(prefixe + "adresseDestinataire", valeurInstantane.adresseDestinataire());
        proprietes.setProperty(prefixe + "changementPrenom", Boolean.toString(valeurInstantane.changementPrenom()));
        proprietes.setProperty(prefixe + "prenomNaissance", valeurInstantane.prenomNaissance());
        proprietes.setProperty(prefixe + "changementSexe", Boolean.toString(valeurInstantane.changementSexe()));
        proprietes.setProperty(prefixe + "sexeAvant", valeurInstantane.sexeAvant());
        proprietes.setProperty(prefixe + "sexeApres", valeurInstantane.sexeApres());
        proprietes.setProperty(prefixe + "changementPrenomFaitEnMairie", Boolean.toString(valeurInstantane.changementPrenomFaitEnMairie()));
        proprietes.setProperty(prefixe + "numeroDecisionMairie", valeurInstantane.numeroDecisionMairie());
        proprietes.setProperty(prefixe + "dateDecisionMairie", valeurInstantane.dateDecisionMairie());
        proprietes.setProperty(prefixe + "tribunalCompetent", valeurInstantane.tribunalCompetent());
        proprietes.setProperty(prefixe + "numeroJugement", valeurInstantane.numeroJugement());
        proprietes.setProperty(prefixe + CLE_VILLE_ACTUELLE, valeurInstantane.villeActuelle());
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
}
