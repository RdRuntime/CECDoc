package com.rdr.cecdoc.ui;

import com.rdr.cecdoc.service.config.EtatDossierPersistant;
import com.rdr.cecdoc.service.config.PersistanceEtatDossierProperties;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Locale;
import java.util.Objects;
import java.util.Properties;
import java.util.function.Function;
import java.util.function.Supplier;

final class GestionnaireConfigurationFichier {
    private static final String NOM_FICHIER_CONFIGURATION_LEGACY = "cecdoc.conf";
    private static final long TAILLE_MAX_CONFIGURATION_OCTETS = 1024L * 1024L;
    private static final System.Logger JOURNAL = System.getLogger(GestionnaireConfigurationFichier.class.getName());
    private final JFrame proprietaire;
    private final Supplier<Path> dossierSortieParDefaut;
    private final Function<String, File> fichierParDefautDansDossierSortie;

    GestionnaireConfigurationFichier(JFrame proprietaire, Supplier<Path> dossierSortieParDefaut, Function<String, File> fichierParDefautDansDossierSortie) {
        this.proprietaire = proprietaire;
        this.dossierSortieParDefaut = Objects.requireNonNull(dossierSortieParDefaut, "dossierSortieParDefaut");
        this.fichierParDefautDansDossierSortie = Objects.requireNonNull(fichierParDefautDansDossierSortie, "fichierParDefautDansDossierSortie");
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

    Path choisirFichierImport() {
        JFileChooser selecteur = creerSelecteurImport();

        int choix = selecteur.showOpenDialog(proprietaire);
        if (choix != JFileChooser.APPROVE_OPTION || selecteur.getSelectedFile() == null) {
            return null;
        }
        Path cheminSelection = selecteur.getSelectedFile().toPath().toAbsolutePath().normalize();
        if (nomFichierInterditPourImport(cheminSelection)) {
            JOptionPane.showMessageDialog(proprietaire, "Import du fichier cecdoc.conf non autorisé.\nUtilisez un fichier XML (.xml).", "Import de configuration", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        MemoireRepertoireExplorateur.memoriserDepuisSelection(selecteur);
        return cheminSelection;
    }

    JFileChooser creerSelecteurImport() {
        JFileChooser selecteur = creerSelecteurFichier("Importer une configuration", false);
        selecteur.setFileHidingEnabled(true);
        selecteur.setFileFilter(new FileNameExtensionFilter("Configuration CECDoc (*.xml)", "xml"));
        return selecteur;
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
            int validation = demandeConfirmation.demander("Le fichier existe déjà. Voulez-vous le remplacer ?", "Export de configuration", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
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
            proprietes.storeToXML(sortie, "Configuration CECDoc", StandardCharsets.UTF_8);
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
        if (!"xml".equals(extension)) {
            throw new IOException("Seuls les fichiers .xml peuvent être importés.");
        }
        return chargerProprietesXml(cheminConfiguration);
    }

    private Properties chargerProprietesXml(Path cheminConfiguration) throws IOException {
        Properties proprietes = new Properties();
        long taille = tailleFichier(cheminConfiguration);
        if (taille <= 0 || taille > TAILLE_MAX_CONFIGURATION_OCTETS) {
            throw new IOException("Taille du fichier de configuration invalide.");
        }
        try (InputStream entree = new BufferedInputStream(Files.newInputStream(cheminConfiguration, StandardOpenOption.READ))) {
            DocumentBuilderFactory fabrique = DocumentBuilderFactory.newInstance();
            fabrique.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            fabrique.setFeature("http://xml.org/sax/features/external-general-entities", false);
            fabrique.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            fabrique.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            definirAttributXmlSiSupporte(fabrique, XMLConstants.ACCESS_EXTERNAL_DTD, "");
            definirAttributXmlSiSupporte(fabrique, XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
            fabrique.setXIncludeAware(false);
            fabrique.setExpandEntityReferences(false);
            var constructeur = fabrique.newDocumentBuilder();
            EntityResolver resolveurLocal = (publicId, systemId) -> new InputSource(new StringReader(""));
            constructeur.setEntityResolver(resolveurLocal);
            Document document = constructeur.parse(entree);
            NodeList entrees = document.getElementsByTagName("entry");
            for (int index = 0; index < entrees.getLength(); index++) {
                if (!(entrees.item(index) instanceof org.w3c.dom.Element elementEntree)) {
                    continue;
                }
                String cle = elementEntree.getAttribute("key");
                if (cle == null || cle.isBlank()) {
                    continue;
                }
                String valeur = elementEntree.getTextContent();
                proprietes.setProperty(cle, valeur == null ? "" : valeur);
            }
        } catch (SAXException | ParserConfigurationException ex) {
            throw new IOException("XML de configuration invalide.", ex);
        }
        return proprietes;
    }

    private long tailleFichier(Path chemin) throws IOException {
        long taille = Files.size(chemin);
        if (taille < 0) {
            throw new IOException("Taille de fichier invalide.");
        }
        return taille;
    }

    private void definirAttributXmlSiSupporte(DocumentBuilderFactory fabrique, String cle, String valeur) {
        try {
            fabrique.setAttribute(cle, valeur);
        } catch (IllegalArgumentException ex) {
            JOURNAL.log(System.Logger.Level.DEBUG, "Attribut XML non supporté : " + cle, ex);
        }
    }

    private JFileChooser creerSelecteurFichier(String titre, boolean multiselection) {
        JFileChooser selecteur = new JFileChooser();
        MemoireRepertoireExplorateur.appliquerAuSelecteur(selecteur, dossierSortieParDefaut.get());
        selecteur.setDialogTitle(titre);
        selecteur.setMultiSelectionEnabled(multiselection);
        selecteur.setAcceptAllFileFilterUsed(false);
        return selecteur;
    }

    @FunctionalInterface
    interface DemandeConfirmation {
        int demander(String message, String titre, int optionType, int messageType);
    }
}
