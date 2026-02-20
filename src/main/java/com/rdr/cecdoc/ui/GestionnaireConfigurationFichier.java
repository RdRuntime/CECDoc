package com.rdr.cecdoc.ui;

import com.rdr.cecdoc.service.config.EtatDossierPersistant;
import com.rdr.cecdoc.service.config.PersistanceEtatDossierProperties;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Locale;
import java.util.Objects;
import java.util.Properties;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

final class GestionnaireConfigurationFichier {
    private static final String NOM_FICHIER_CONFIGURATION_LEGACY = "cecdoc.conf";

    @FunctionalInterface
    interface DemandeConfirmation {
        int demander(String message, String titre, int optionType, int messageType);
    }

    private final JFrame proprietaire;
    private final Supplier<Path> dossierSortieParDefaut;
    private final Function<String, File> fichierParDefautDansDossierSortie;

    GestionnaireConfigurationFichier(JFrame proprietaire, Supplier<Path> dossierSortieParDefaut, Function<String, File> fichierParDefautDansDossierSortie) {
        this.proprietaire = Objects.requireNonNull(proprietaire, "proprietaire");
        this.dossierSortieParDefaut = Objects.requireNonNull(dossierSortieParDefaut, "dossierSortieParDefaut");
        this.fichierParDefautDansDossierSortie = Objects.requireNonNull(fichierParDefautDansDossierSortie, "fichierParDefautDansDossierSortie");
    }

    Path choisirFichierImport() {
        JFileChooser selecteur = creerSelecteurFichier("Importer une configuration", false);
        selecteur.setFileHidingEnabled(false);
        selecteur.setFileFilter(new FileNameExtensionFilter("Configuration CECDoc (*.xml, *.conf)", "xml", "conf"));

        int choix = selecteur.showOpenDialog(proprietaire);
        if (choix != JFileChooser.APPROVE_OPTION || selecteur.getSelectedFile() == null) {
            return null;
        }
        Path cheminSelection = selecteur.getSelectedFile().toPath().toAbsolutePath().normalize();
        if (nomFichierInterditPourImport(cheminSelection)) {
            JOptionPane.showMessageDialog(proprietaire, "Import du fichier cecdoc.conf non autorisé.\nUtilisez un fichier XML ou .cecdoc.conf.", "Import de configuration", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        MemoireRepertoireExplorateur.memoriserDepuisSelection(selecteur);
        return cheminSelection;
    }

    Path choisirFichierExport(DemandeConfirmation demandeConfirmation) {
        JFileChooser selecteur = creerSelecteurFichier("Exporter une configuration", false);
        selecteur.setFileFilter(new FileNameExtensionFilter("Configuration XML (*.xml)", "xml"));
        selecteur.setSelectedFile(fichierParDefautDansDossierSortie.apply("cecdoc-configuration.xml"));

        int choix = selecteur.showSaveDialog(proprietaire);
        if (choix != JFileChooser.APPROVE_OPTION || selecteur.getSelectedFile() == null) {
            return null;
        }
        MemoireRepertoireExplorateur.memoriserDepuisSelection(selecteur);

        File cible = SelectionFichierDocument.garantirExtension(selecteur.getSelectedFile(), "xml");
        if (cible == null) {
            return null;
        }
        if (cible.exists()) {
            int validation = demandeConfirmation.demander(
                    "Le fichier existe déjà. Voulez-vous le remplacer ?",
                    "Export de configuration",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );
            if (validation != JOptionPane.YES_OPTION) {
                return null;
            }
        }
        return cible.toPath();
    }

    void sauvegarderConfigurationXml(Path chemin, EtatDossierPersistant etat) throws IOException {
        Objects.requireNonNull(chemin, "chemin");
        Objects.requireNonNull(etat, "etat");
        Path parent = chemin.toAbsolutePath().normalize().getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        Properties proprietes = PersistanceEtatDossierProperties.convertirEtatEnProprietes(etat);
        try (OutputStream sortie = new BufferedOutputStream(Files.newOutputStream(chemin, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE))) {
            proprietes.storeToXML(sortie, "Configuration CECDoc", "UTF-8");
        }
    }

    EtatDossierPersistant lireConfiguration(Path chemin) throws IOException {
        Objects.requireNonNull(chemin, "chemin");
        Path cheminNormalise = chemin.toAbsolutePath().normalize();
        if (nomFichierInterditPourImport(cheminNormalise)) {
            throw new IOException("Import du fichier cecdoc.conf non autorisé.");
        }
        if (!Files.isRegularFile(cheminNormalise) || !Files.isReadable(cheminNormalise)) {
            throw new IOException("Fichier de configuration inaccessible.");
        }
        Properties proprietes = chargerProprietesConfiguration(cheminNormalise);
        return PersistanceEtatDossierProperties.convertirProprietesEnEtat(proprietes);
    }

    private Properties chargerProprietesConfiguration(Path cheminConfiguration) throws IOException {
        String extension = extensionFichier(cheminConfiguration.getFileName() == null ? "" : cheminConfiguration.getFileName().toString());
        if ("xml".equals(extension)) {
            return chargerProprietesXml(cheminConfiguration);
        }
        if ("conf".equals(extension)) {
            return chargerProprietesConf(cheminConfiguration);
        }
        try {
            return chargerProprietesXml(cheminConfiguration);
        } catch (IOException ex) {
            return chargerProprietesConf(cheminConfiguration);
        }
    }

    private Properties chargerProprietesXml(Path cheminConfiguration) throws IOException {
        Properties proprietes = new Properties();
        try (InputStream entree = new BufferedInputStream(Files.newInputStream(cheminConfiguration, StandardOpenOption.READ))) {
            proprietes.loadFromXML(entree);
        }
        return proprietes;
    }

    private Properties chargerProprietesConf(Path cheminConfiguration) throws IOException {
        Properties proprietes = new Properties();
        try (InputStream entree = new BufferedInputStream(Files.newInputStream(cheminConfiguration, StandardOpenOption.READ))) {
            proprietes.load(entree);
        }
        return proprietes;
    }

    private JFileChooser creerSelecteurFichier(String titre, boolean multiselection) {
        JFileChooser selecteur = new JFileChooser();
        MemoireRepertoireExplorateur.appliquerAuSelecteur(selecteur, dossierSortieParDefaut.get());
        selecteur.setDialogTitle(titre);
        selecteur.setMultiSelectionEnabled(multiselection);
        selecteur.setAcceptAllFileFilterUsed(false);
        return selecteur;
    }

    private static String extensionFichier(String nomFichier) {
        if (nomFichier == null || nomFichier.isBlank()) {
            return "";
        }
        int index = nomFichier.lastIndexOf('.');
        if (index < 0 || index >= nomFichier.length() - 1) {
            return "";
        }
        return nomFichier.substring(index + 1).toLowerCase(Locale.ROOT);
    }

    private static boolean nomFichierInterditPourImport(Path chemin) {
        if (chemin == null || chemin.getFileName() == null) {
            return false;
        }
        return NOM_FICHIER_CONFIGURATION_LEGACY.equalsIgnoreCase(chemin.getFileName().toString());
    }
}
