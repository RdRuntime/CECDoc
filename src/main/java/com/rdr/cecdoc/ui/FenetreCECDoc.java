package com.rdr.cecdoc.ui;

import com.rdr.cecdoc.model.DonneesDossier;
import com.rdr.cecdoc.model.ChampDossier;
import com.rdr.cecdoc.model.InstantaneDossier;
import com.rdr.cecdoc.model.InstantaneLettreAdministration;
import com.rdr.cecdoc.model.InstantaneLettreUniversite;
import com.rdr.cecdoc.model.PieceJointe;
import com.rdr.cecdoc.model.PieceJustificative;
import com.rdr.cecdoc.model.TypePieceJointe;
import com.rdr.cecdoc.service.ServiceApplicationDossier;
import com.rdr.cecdoc.service.FabriqueServices;
import com.rdr.cecdoc.service.config.PersistanceEtatDossier;
import com.rdr.cecdoc.service.config.EtatDossierPersistant;
import com.rdr.cecdoc.util.NormalisationTexte;
import com.rdr.cecdoc.util.ParseursDate;
import com.rdr.cecdoc.service.export.EcritureDocxAtomique;
import com.rdr.cecdoc.service.export.ErreurExportDocument;
import com.rdr.cecdoc.service.export.ServiceGenerationLettreGreffiere;
import com.rdr.cecdoc.service.export.ServicePdfDossierComplet;
import com.rdr.cecdoc.service.validation.ProblemeValidation;
import com.rdr.cecdoc.service.validation.ResultatValidation;
import com.rdr.cecdoc.ui.theme.TokensEtatBouton;
import com.rdr.cecdoc.ui.theme.FabriqueTheme;
import com.rdr.cecdoc.ui.theme.DiffuseurTheme;
import com.rdr.cecdoc.ui.theme.ModeTheme;
import com.rdr.cecdoc.ui.theme.PreferenceThemeApplication;
import com.rdr.cecdoc.ui.theme.StyliseurBoutonTheme;
import com.rdr.cecdoc.ui.theme.TokensTheme;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FocusTraversalPolicy;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.LinearGradientPaint;
import java.awt.RadialGradientPaint;
import java.awt.Image;
import java.awt.GraphicsEnvironment;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.Serial;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JDialog;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.imageio.ImageIO;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.text.JTextComponent;


public class FenetreCECDoc extends FenetreFormulaireAbstraite {
    private static final String NOTIF_ITEM = "Notification de changement de prénoms à l’état-civil";
    private static final String CLE_DESACTIVER_SYSTEM_EXIT = "cecdoc.desactiver.system.exit";
    @Serial
    private static final long serialVersionUID = 1L;
    private static final System.Logger LOGGER = System.getLogger(FenetreCECDoc.class.getName());
    private static final Color COULEUR_TRANS_BLEU = Color.decode("#5BCEFA");
    private static final Color COULEUR_TRANS_ROSE = Color.decode("#F5A9B8");
    private static final Color COULEUR_BLANCHE = Color.decode("#FFFFFF");
    private static final Color COULEUR_NON_BINAIRE_JAUNE = Color.decode("#FCF434");
    private static final Color COULEUR_NON_BINAIRE_VIOLET = Color.decode("#9C59D1");
    private static final Color COULEUR_NON_BINAIRE_NOIR = Color.decode("#000000");
    private static final Color COULEUR_LESBIEN_ORANGE_FONCE = Color.decode("#D52D00");
    private static final Color COULEUR_LESBIEN_ORANGE_CLAIR = Color.decode("#EF7627");
    private static final Color COULEUR_LESBIEN_ORANGE_PALE = Color.decode("#FF9A56");
    private static final Color COULEUR_LESBIEN_ROSE = Color.decode("#D162A4");
    private static final Color COULEUR_LESBIEN_ROSE_FONCE = Color.decode("#B55690");
    private static final Color COULEUR_LESBIEN_PRUNE = Color.decode("#A30262");
    private static final Color COULEUR_RAINBOW_ROUGE = Color.decode("#E40303");
    private static final Color COULEUR_RAINBOW_ORANGE = Color.decode("#FF8C00");
    private static final Color COULEUR_RAINBOW_JAUNE = Color.decode("#FFED00");
    private static final Color COULEUR_RAINBOW_VERT = Color.decode("#008026");
    private static final Color COULEUR_RAINBOW_BLEU = Color.decode("#004DFF");
    private static final Color COULEUR_RAINBOW_VIOLET = Color.decode("#750787");
    private static final Color COULEUR_INTERSEXE_JAUNE = Color.decode("#FFD800");
    private static final Color COULEUR_INTERSEXE_VIOLET = Color.decode("#7902AA");
    private static final Color COULEUR_PAN_ROSE = Color.decode("#FF1B8D");
    private static final Color COULEUR_PAN_JAUNE = Color.decode("#FFD900");
    private static final Color COULEUR_PAN_BLEU = Color.decode("#1BB3FF");
    private static final Color COULEUR_BI_ROSE = Color.decode("#D60270");
    private static final Color COULEUR_BI_VIOLET = Color.decode("#9B4F96");
    private static final Color COULEUR_BI_BLEU = Color.decode("#0038A8");

    private final transient ServiceApplicationDossier serviceApplication;
    private final transient ServiceGenerationLettreGreffiere serviceGenerationLettreGreffiere;
    private final transient DiffuseurTheme diffuseurTheme;

    private transient TokensTheme theme;
    private final transient List<Image> iconesApplication;
    private final transient Icon iconeDialogueApplication;

    private final JCheckBox checkboxChangementPrenoms;
    private final JCheckBox checkboxPronomNeutre;
    private final ChampTexteExemple champPrenomsEtatCivil;
    private final ChampTexteExemple champPrenomsUsage;
    private final JTextField champNomFamille;
    private final JComboBox<String> comboJourNaissance;
    private final JComboBox<String> comboMoisNaissance;
    private final JComboBox<String> comboAnneeNaissance;
    private final JPanel panneauDateNaissance;
    private final JTextField champLieuNaissance;
    private final JComboBox<String> comboSexeEtatCivil;
    private final JTextArea champAdresse;
    private final JTextArea zoneTribunal;
    private final JTextArea zoneRecit;
    private final JTextField champVilleActuelle;
    private final JTextField champNationalite;
    private final JTextField champProfession;
    private final JComboBox<String> comboSituationMatrimoniale;
    private final JComboBox<String> comboSituationEnfants;
    private final JCheckBox checkboxPacs;
    private final JCheckBox checkboxEffacerApresExport;
    private final JButton boutonPieces;
    private final JButton boutonEffacer;
    private final JButton boutonAutresDocuments;
    private final JButton boutonGenerer;
    private final JButton boutonAide;
    private final JButton boutonConfiguration;
    private final JLabel labelMessageFormulaire;

    private final PanneauDegrade panneauRacine;
    private final JPanel panneauCarte;
    private final JLabel labelTitre;
    private final JLabel labelSousTitre;
    private final JLabel labelRaccourcis;

    private JScrollPane scrollAdresse;
    private JScrollPane scrollTribunal;
    private JScrollPane scrollRecit;

    private final transient Map<JComponent, JLabel> labelsErreurs;
    private final transient Map<JComponent, Border> borduresNormales;
    private final transient Map<ChampDossier, JComponent> composantsParChamp;
    private final transient Set<JComponent> composantsInvalides;
    private final transient List<JLabel> labelsSections;
    private final transient List<JLabel> labelsLignes;
    private final transient List<JPanel> separateursSections;
    private final transient Map<JButton, RoleBouton> rolesBoutons;

    private final SurcoucheOccupation surcoucheOccupation;
    private transient SwingWorker<?, ?> tacheActive;

    private transient List<String> piecesActuelles;
    private transient List<String> piecesParDefautSexe;
    private transient List<String> piecesParDefautAvecPrenoms;
    private transient Map<String, List<PieceJointe>> piecesJointesParIntitule;
    private boolean dernierModeChangementPrenoms;
    private boolean confirmerQuitterAvecDonnees;
    private boolean memoriserDonneesSaisies;
    private PreferenceThemeApplication preferenceThemeApplication;
    private transient Path dossierSortieParDefaut;
    private boolean popupsVisibiliteInitialises;
    private transient InstantaneLettreUniversite instantaneLettreUniversite;
    private transient InstantaneLettreAdministration instantaneLettreAdministration;

    private boolean modifie;
    private boolean suspendreSuiviModification;

    private final transient PersistanceEtatDossier persistanceEtat;
    private final transient ExecutorService executricePersistanceEtat;
    private final transient AtomicReference<EtatDossierPersistant> etatPersistantEnAttente;
    private final transient AtomicBoolean ecriturePersistancePlanifiee;


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            installerLookAndFeel();
            FenetreCECDoc form = new FenetreCECDoc();
            form.setVisible(true);
        });
    }


    public FenetreCECDoc() {
        this(FabriqueServices.creerServiceApplication(), FabriqueTheme.creerDiffuseur(), FabriqueServices.creerPersistanceEtat());
    }

    FenetreCECDoc(ServiceApplicationDossier serviceApplication, DiffuseurTheme diffuseurTheme, PersistanceEtatDossier persistanceEtat) {
        super("Requête de changement de sexe à l'état civil");
        this.serviceApplication = Objects.requireNonNull(serviceApplication, "serviceApplication");
        this.serviceGenerationLettreGreffiere = new ServiceGenerationLettreGreffiere(new EcritureDocxAtomique());
        this.diffuseurTheme = Objects.requireNonNull(diffuseurTheme, "diffuseurTheme");
        this.persistanceEtat = Objects.requireNonNull(persistanceEtat, "persistanceEtat");
        this.executricePersistanceEtat = Executors.newSingleThreadExecutor(runnable -> {
            Thread thread = new Thread(runnable, "cecdoc-conf-writher");
            thread.setDaemon(true);
            return thread;
        });
        this.etatPersistantEnAttente = new AtomicReference<>();
        this.ecriturePersistancePlanifiee = new AtomicBoolean(false);
        this.theme = this.diffuseurTheme.themeActuel();
        this.iconesApplication = chargerIconesApplication();
        this.iconeDialogueApplication = creerIconeDialogue(iconesApplication);
        if (!iconesApplication.isEmpty()) {
            setIconImages(iconesApplication);
        }

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        configurerTailleInitialeFenetre();

        checkboxChangementPrenoms = new JCheckBox("Cela concerne aussi un changement de prénoms");
        checkboxPronomNeutre = new JCheckBox("Utiliser le pronom neutre « iel » dans tout le document");
        champPrenomsEtatCivil = new ChampTexteExemple(20, "Ex: Tom, Noé, Max");
        champPrenomsUsage = new ChampTexteExemple(20, "");
        champNomFamille = new ChampTexteExemple(20, "Ex: Dupont");
        comboJourNaissance = creerComboNombre(1, 31);
        comboMoisNaissance = creerComboNombre(1, 12);
        comboAnneeNaissance = creerComboNombreDecroissant(LocalDate.now().getYear(), LocalDate.now().getYear() - 100);
        panneauDateNaissance = creerPanneauDateNaissance();
        champLieuNaissance = new ChampTexteExemple(20, "Ex: Paris");
        comboSexeEtatCivil = new JComboBox<>(new String[]{"Masculin", "Féminin"});
        champAdresse = new ZoneTexteExemple(3, 30, "Ex: 55 Rue du Faubourg Saint-Honoré\n75008 Paris");
        zoneTribunal = new ZoneTexteExemple(4, 30, "Ex: Tribunal judiciaire de Paris\nChambre du Conseil\nParvis du Tribunal de Paris\n75017 PARIS");
        zoneRecit = new ZoneTexteExemple(8, 30, "Décrivez votre parcours, votre identité sociale et les éléments factuels utiles.");
        champVilleActuelle = new ChampTexteExemple(20, "Ex: Paris");
        champNationalite = new ChampTexteExemple(20, "Ex: Française");
        champProfession = new ChampTexteExemple(30, "Ex: Taf / Entreprise");
        comboSituationMatrimoniale = new JComboBox<>(new String[]{"", "Célibataire", "Marié·e", "Divorcé·e", "Veuf / Veuve", "Séparé·e", "En concubinage"});
        comboSituationEnfants = new JComboBox<>(new String[]{"", "Sans enfants", "1 enfant", "2 enfants", "3 enfants ou plus", "Parent isolé·e", "Garde alternée"});
        checkboxPacs = new JCheckBox("A contracté un Pacte civil de solidarité (PACS)");
        checkboxEffacerApresExport = new JCheckBox("Effacer automatiquement après exporter");
        boutonPieces = new JButton("Pièces justificatives");
        boutonEffacer = new JButton("Effacer les données");
        boutonAutresDocuments = new JButton("Autres documents");
        boutonGenerer = new JButton("Générer le document");
        boutonAide = new JButton(new IconeInfo(18));
        boutonAide.setToolTipText("Aide");
        boutonConfiguration = new JButton(new IconeConfiguration(18));
        boutonConfiguration.setToolTipText("Configuration");

        labelTitre = new JLabel("Requête de changement à l'état civil");
        labelSousTitre = new JLabel("Formulaire guidé pour générer un document Word et un dossier PDF complet");
        labelRaccourcis = new JLabel("Raccourcis: Ctrl/Cmd+Entrée générer, Ctrl/Cmd+J pièces, Ctrl/Cmd+I aide, Ctrl/Cmd+, configuration, Ctrl/Cmd+Z annuler");

        labelTitre.setAlignmentX(LEFT_ALIGNMENT);
        labelSousTitre.setAlignmentX(LEFT_ALIGNMENT);
        labelRaccourcis.setAlignmentX(LEFT_ALIGNMENT);
        initialiserAnnulationSaisieTexte();

        labelMessageFormulaire = new JLabel(" ");
        labelsErreurs = new LinkedHashMap<>();
        borduresNormales = new HashMap<>();
        composantsParChamp = new EnumMap<>(ChampDossier.class);
        composantsInvalides = new HashSet<>();
        labelsSections = new ArrayList<>();
        labelsLignes = new ArrayList<>();
        separateursSections = new ArrayList<>();
        rolesBoutons = new LinkedHashMap<>();

        panneauRacine = new PanneauDegrade(new BorderLayout(0, theme.spacing().blockGap() * 2));
        panneauCarte = new JPanel(new GridBagLayout());

        surcoucheOccupation = new SurcoucheOccupation();
        setGlassPane(surcoucheOccupation);

        suspendreSuiviModification = true;

        initialiserPiecesParDefaut();
        piecesActuelles = new ArrayList<>(piecesParDefautSexe);
        piecesJointesParIntitule = new LinkedHashMap<>();
        synchroniserPiecesJointesParIntitule();
        dernierModeChangementPrenoms = checkboxChangementPrenoms.isSelected();
        confirmerQuitterAvecDonnees = true;
        memoriserDonneesSaisies = true;
        preferenceThemeApplication = PreferenceThemeApplication.DEFAUT;
        dossierSortieParDefaut = null;
        instantaneLettreUniversite = InstantaneLettreUniversite.vide();
        instantaneLettreAdministration = InstantaneLettreAdministration.vide();

        initialiserComportementSaisie();
        initialiserMiseEnPage();
        enregistrerComposantsValidation();
        initialiserEcouteurs();
        initialiserMnemoniques();
        initialiserRaccourcisClavier();
        initialiserOrdreFocus();
        initialiserValidationProgressive();
        initialiserSuiviModification();
        initialiserProtectionFermeture();
        initialiserAccessibilite();
        initialiserGestionTheme();
        chargerEtatPersistant();
        initialiserPopupsVisibilite();

        suspendreSuiviModification = false;
        modifie = false;
    }

    private void configurerTailleInitialeFenetre() {
        Rectangle usableBounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();

        int widthMargin = Math.max(24, usableBounds.width / 24);
        int heightMargin = Math.max(24, usableBounds.height / 20);

        int maxWidth = Math.max(320, usableBounds.width - widthMargin);
        int maxHeight = Math.max(360, usableBounds.height - heightMargin);

        int width = Math.min(1060, maxWidth);
        int height = Math.min(960, maxHeight);

        int minWidth = Math.min(760, width);
        int minHeight = Math.min(620, height);

        setResizable(true);
        setMinimumSize(new Dimension(minWidth, minHeight));
        setSize(width, height);
        setLocation(usableBounds.x + Math.max(0, (usableBounds.width - width) / 2), usableBounds.y + Math.max(0, (usableBounds.height - height) / 2));
    }

    private void initialiserAnnulationSaisieTexte() {
        GestionAnnulationTexte.activer(champPrenomsEtatCivil);
        GestionAnnulationTexte.activer(champPrenomsUsage);
        GestionAnnulationTexte.activer(champNomFamille);
        GestionAnnulationTexte.activer(champLieuNaissance);
        GestionAnnulationTexte.activer(champAdresse);
        GestionAnnulationTexte.activer(zoneTribunal);
        GestionAnnulationTexte.activer(zoneRecit);
        GestionAnnulationTexte.activer(champVilleActuelle);
        GestionAnnulationTexte.activer(champNationalite);
        GestionAnnulationTexte.activer(champProfession);
    }

    private static void installerLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            LOGGER.log(System.Logger.Level.WARNING, "Impossible d'appliquer l'apparence système.", ex);
        }
    }

    private List<Image> chargerIconesApplication() {
        try (InputStream in = FenetreCECDoc.class.getResourceAsStream("/cecdoc.png")) {
            if (in == null) {
                return List.of();
            }
            BufferedImage base = ImageIO.read(in);
            if (base == null) {
                return List.of();
            }
            List<Image> icones = new ArrayList<>();
            icones.add(base);
            icones.add(base.getScaledInstance(16, 16, Image.SCALE_SMOOTH));
            icones.add(base.getScaledInstance(24, 24, Image.SCALE_SMOOTH));
            icones.add(base.getScaledInstance(32, 32, Image.SCALE_SMOOTH));
            icones.add(base.getScaledInstance(48, 48, Image.SCALE_SMOOTH));
            icones.add(base.getScaledInstance(64, 64, Image.SCALE_SMOOTH));
            icones.add(base.getScaledInstance(128, 128, Image.SCALE_SMOOTH));
            return List.copyOf(icones);
        } catch (IOException ex) {
            return List.of();
        }
    }

    private Icon creerIconeDialogue(List<Image> icones) {
        if (icones == null || icones.isEmpty()) {
            return null;
        }
        Icon iconeReference = UIManager.getIcon("OptionPane.informationIcon");
        int largeur = iconeReference != null && iconeReference.getIconWidth() > 0 ? iconeReference.getIconWidth() : 32;
        int hauteur = iconeReference != null && iconeReference.getIconHeight() > 0 ? iconeReference.getIconHeight() : largeur;
        return new ImageIcon(icones.get(0).getScaledInstance(largeur, hauteur, Image.SCALE_SMOOTH));
    }

    private void appliquerIconeDialogue(JDialog dialogue) {
        if (dialogue != null && !iconesApplication.isEmpty()) {
            dialogue.setIconImages(iconesApplication);
        }
    }

    private void afficherMessage(String message, String titre, int typeMessage) {
        JOptionPane panneauOption = new JOptionPane(message, typeMessage, JOptionPane.DEFAULT_OPTION);
        panneauOption.getAccessibleContext().setAccessibleName(titre);
        panneauOption.getAccessibleContext().setAccessibleDescription(message);
        if (iconeDialogueApplication != null) {
            panneauOption.setIcon(iconeDialogueApplication);
        }
        JDialog dialogue = panneauOption.createDialog(this, titre);
        afficherDialogueHabille(dialogue);
    }

    private int afficherConfirmation(String message, String titre, int typeOption, int typeMessage) {
        JOptionPane panneauOption = new JOptionPane(message, typeMessage, typeOption);
        panneauOption.getAccessibleContext().setAccessibleName(titre);
        panneauOption.getAccessibleContext().setAccessibleDescription(message);
        if (iconeDialogueApplication != null) {
            panneauOption.setIcon(iconeDialogueApplication);
        }
        JDialog dialogue = panneauOption.createDialog(this, titre);
        afficherDialogueHabille(dialogue);
        Object valeur = panneauOption.getValue();
        if (valeur instanceof Integer choix) {
            return choix;
        }
        return JOptionPane.CLOSED_OPTION;
    }

    private boolean confirmerQuitter() {
        Object[] choixPossibles = {"Annuler", "Valider"};
        JOptionPane panneauOption = new JOptionPane("Quitter ?", JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION, iconeDialogueApplication, choixPossibles, choixPossibles[0]);
        panneauOption.getAccessibleContext().setAccessibleName("Confirmation de fermeture");
        panneauOption.getAccessibleContext().setAccessibleDescription("Demande de confirmer la fermeture de l'application");
        JDialog dialogue = panneauOption.createDialog(this, "Quitter ?");
        afficherDialogueHabille(dialogue);
        return Objects.equals(panneauOption.getValue(), choixPossibles[1]);
    }

    private void afficherDialogueHabille(JDialog dialogue) {
        if (dialogue == null) {
            return;
        }
        appliquerIconeDialogue(dialogue);
        HabillageDialogues.preparerDialogue(dialogue, theme);
        dialogue.pack();
        dialogue.setLocationRelativeTo(this);
        dialogue.setVisible(true);
        dialogue.dispose();
    }

    private void initialiserGestionTheme() {
        diffuseurTheme.ajouterEcouteur(nouveauTheme -> SwingUtilities.invokeLater(() -> appliquerTheme(nouveauTheme)));
        appliquerTheme(theme);
    }

    private void appliquerTheme(TokensTheme nouveauTheme) {
        this.theme = nouveauTheme;

        TokensTheme t = this.theme;

        int screenInset = t.spacing().screenInset();
        int outerInset = Math.max(0, t.spacing().inlineGap() / 2);
        panneauRacine.setBorder(new EmptyBorder(screenInset, screenInset + outerInset, screenInset, screenInset + outerInset));
        panneauCarte.setBackground(t.palette().cardBackground());
        panneauCarte.setBorder(BorderFactory.createCompoundBorder(new LineBorder(t.palette().border(), 1, true), new EmptyBorder(t.spacing().cardInset(), t.spacing().cardInset(), t.spacing().cardInset(), t.spacing().cardInset())));

        labelTitre.setForeground(t.palette().titleText());
        labelTitre.setFont(t.typography().title());
        labelSousTitre.setForeground(t.palette().mutedText());
        labelSousTitre.setFont(t.typography().subtitle());
        labelRaccourcis.setForeground(t.palette().mutedText());
        labelRaccourcis.setFont(t.typography().helper());

        for (JLabel sectionLabel : labelsSections) {
            sectionLabel.setForeground(t.palette().titleText());
            sectionLabel.setFont(t.typography().section());
        }
        for (JLabel rowLabel : labelsLignes) {
            rowLabel.setForeground(t.palette().bodyText());
            rowLabel.setFont(t.typography().label());
        }
        for (JPanel separator : separateursSections) {
            separator.setBackground(t.palette().border());
        }

        labelMessageFormulaire.setForeground(t.palette().error());
        labelMessageFormulaire.setFont(t.typography().message());

        stylerCheckbox(checkboxChangementPrenoms);
        stylerCheckbox(checkboxPronomNeutre);
        stylerCheckbox(checkboxPacs);
        stylerCheckbox(checkboxEffacerApresExport);

        stylerChamp(champPrenomsEtatCivil);
        stylerChamp(champPrenomsUsage);
        stylerChamp(champNomFamille);
        stylerChamp(comboJourNaissance);
        stylerChamp(comboMoisNaissance);
        stylerChamp(comboAnneeNaissance);
        int max = Math.max(1, t.spacing().inlineGap() / 2);
        panneauDateNaissance.setBorder(BorderFactory.createEmptyBorder(max, t.spacing().inlineGap(), max, t.spacing().inlineGap()));
        stylerChamp(champLieuNaissance);
        stylerZoneTexte(champAdresse);
        stylerChamp(champVilleActuelle);
        stylerChamp(champNationalite);
        stylerChamp(champProfession);
        stylerChamp(comboSituationMatrimoniale);
        stylerChamp(comboSituationEnfants);
        stylerChamp(comboSexeEtatCivil);
        stylerZoneTexte(zoneTribunal);
        stylerZoneTexte(zoneRecit);
        stylerAscenseur(scrollAdresse);
        stylerAscenseur(scrollTribunal);
        stylerAscenseur(scrollRecit);

        appliquerRoleBouton(boutonPieces, RoleBouton.SECONDARY);
        appliquerRoleBouton(boutonEffacer, RoleBouton.DANGER);
        appliquerRoleBouton(boutonAutresDocuments, RoleBouton.SECONDARY);
        appliquerRoleBouton(boutonGenerer, RoleBouton.PRIMARY);
        appliquerRoleBouton(boutonConfiguration, RoleBouton.ICON);
        appliquerRoleBouton(boutonAide, RoleBouton.ICON);

        surcoucheOccupation.appliquerTheme(t);
        mettreAJourBorduresNormales();

        effacerErreursValidation();
        revalidate();
        repaint();
    }

    private void initialiserComportementSaisie() {
        champAdresse.setLineWrap(true);
        champAdresse.setWrapStyleWord(true);
        zoneTribunal.setLineWrap(true);
        zoneTribunal.setWrapStyleWord(true);
        zoneRecit.setLineWrap(true);
        zoneRecit.setWrapStyleWord(true);
        champPrenomsUsage.setEnabled(false);
        checkboxEffacerApresExport.setSelected(false);
        initialiserGestionDateNaissance();
        mettreAJourExemplesPrenoms();
    }


    private void mettreAJourExemplesPrenoms() {
        mettreAJourExemplePrenomsEtatCivil();
        mettreAJourExemplePrenomsUsage();
    }


    private void mettreAJourExemplePrenomsEtatCivil() {
        String sexeEtatCivil = Objects.toString(comboSexeEtatCivil.getSelectedItem(), "");
        boolean feminine = "Féminin".equals(sexeEtatCivil);
        boolean changementPrenoms = checkboxChangementPrenoms.isSelected();

        if (changementPrenoms) {
            champPrenomsEtatCivil.setPlaceholder(feminine ? "Ex: Alice, Emma, Agathe" : "Ex: Tom, Noé, Max");
            return;
        }

        champPrenomsEtatCivil.setPlaceholder(feminine ? "Ex: Tom, Noé, Max" : "Ex: Alice, Emma, Agathe");
    }


    private void mettreAJourExemplePrenomsUsage() {
        if (!checkboxChangementPrenoms.isSelected()) {
            champPrenomsUsage.setPlaceholder("");
            return;
        }
        String sexeEtatCivil = Objects.toString(comboSexeEtatCivil.getSelectedItem(), "");
        boolean feminine = "Féminin".equals(sexeEtatCivil);
        champPrenomsUsage.setPlaceholder(feminine ? "Ex: Tom, Noé, Max" : "Ex: Alice, Emma, Agathe");
    }

    private void initialiserGestionDateNaissance() {
        appliquerDateNaissanceParDefaut();
        comboMoisNaissance.addActionListener(e -> ajusterJourNaissance());
        comboAnneeNaissance.addActionListener(e -> ajusterJourNaissance());
    }

    private void appliquerDateNaissanceParDefaut() {
        LocalDate dateDuJour = LocalDate.now();
        comboAnneeNaissance.setSelectedItem(Integer.toString(dateDuJour.getYear()));
        comboMoisNaissance.setSelectedItem("12");
        ajusterJourNaissance();
        comboJourNaissance.setSelectedItem("13");
    }

    private JComboBox<String> creerComboNombre(int minInclusive, int maxInclusive) {
        String[] valeurs = new String[maxInclusive - minInclusive + 1];
        for (int i = minInclusive; i <= maxInclusive; i++) {
            valeurs[i - minInclusive] = Integer.toString(i);
        }
        return new JComboBox<>(valeurs);
    }

    private JComboBox<String> creerComboNombreDecroissant(int borneDepartInclusive, int borneFinInclusive) {
        int taille = borneDepartInclusive - borneFinInclusive + 1;
        String[] valeurs = new String[taille];
        int index = 0;
        for (int valeur = borneDepartInclusive; valeur >= borneFinInclusive; valeur--) {
            valeurs[index++] = Integer.toString(valeur);
        }
        return new JComboBox<>(valeurs);
    }

    private JPanel creerPanneauDateNaissance() {
        JPanel panneau = new JPanel();
        panneau.setOpaque(false);
        panneau.setLayout(new BoxLayout(panneau, BoxLayout.X_AXIS));
        panneau.setBorder(BorderFactory.createEmptyBorder());
        panneau.add(comboJourNaissance);
        panneau.add(Box.createHorizontalStrut(theme.spacing().inlineGap()));
        panneau.add(creerSeparateurDate());
        panneau.add(Box.createHorizontalStrut(theme.spacing().inlineGap()));
        panneau.add(comboMoisNaissance);
        panneau.add(Box.createHorizontalStrut(theme.spacing().inlineGap()));
        panneau.add(creerSeparateurDate());
        panneau.add(Box.createHorizontalStrut(theme.spacing().inlineGap()));
        panneau.add(comboAnneeNaissance);
        panneau.add(Box.createHorizontalGlue());
        return panneau;
    }

    private JLabel creerSeparateurDate() {
        JLabel separateur = new JLabel("/");
        separateur.setForeground(theme.palette().mutedText());
        separateur.setFont(theme.typography().input().deriveFont(Font.BOLD));
        return separateur;
    }

    private void ajusterJourNaissance() {
        int annee = valeurEntiereSelectionnee(comboAnneeNaissance);
        int mois = valeurEntiereSelectionnee(comboMoisNaissance);
        if (annee <= 0 || mois <= 0) {
            return;
        }

        int jourActuel = valeurEntiereSelectionnee(comboJourNaissance);
        int jourMaximum = YearMonth.of(annee, mois).lengthOfMonth();

        comboJourNaissance.removeAllItems();
        for (int jour = 1; jour <= jourMaximum; jour++) {
            comboJourNaissance.addItem(Integer.toString(jour));
        }

        int jourSelectionne = jourActuel <= 0 ? 1 : Math.min(jourActuel, jourMaximum);
        comboJourNaissance.setSelectedItem(Integer.toString(jourSelectionne));
    }

    private int valeurEntiereSelectionnee(JComboBox<String> listeChoix) {
        Object valeurSelectionnee = listeChoix.getSelectedItem();
        if (valeurSelectionnee == null) {
            return -1;
        }
        try {
            return Integer.parseInt(valeurSelectionnee.toString());
        } catch (NumberFormatException ex) {
            return -1;
        }
    }

    private String construireTexteDateNaissance() {
        int jour = valeurEntiereSelectionnee(comboJourNaissance);
        int mois = valeurEntiereSelectionnee(comboMoisNaissance);
        int annee = valeurEntiereSelectionnee(comboAnneeNaissance);
        if (jour <= 0 || mois <= 0 || annee <= 0) {
            return "";
        }
        return String.format(Locale.ROOT, "%02d/%02d/%04d", jour, mois, annee);
    }

    private void chargerEtatPersistant() {
        persistanceEtat.charger().ifPresent(this::appliquerEtatPersistant);
    }

    private void appliquerEtatPersistant(EtatDossierPersistant etatPersistant) {
        checkboxEffacerApresExport.setSelected(etatPersistant.effacerApresExport());
        confirmerQuitterAvecDonnees = etatPersistant.confirmerQuitterAvecDonnees();
        memoriserDonneesSaisies = etatPersistant.memoriserDonneesSaisies();
        preferenceThemeApplication = PreferenceThemeApplication.depuisCode(etatPersistant.themeApplication());
        dossierSortieParDefaut = normaliserDossierSortieParDefaut(etatPersistant.dossierSortieParDefaut());
        instantaneLettreUniversite = etatPersistant.instantaneLettreUniversite();
        instantaneLettreAdministration = etatPersistant.instantaneLettreAdministration();

        if (memoriserDonneesSaisies) {
            appliquerInstantanePersistant(etatPersistant.instantane());
        } else {
            instantaneLettreUniversite = InstantaneLettreUniversite.vide();
            instantaneLettreAdministration = InstantaneLettreAdministration.vide();
        }
        synchroniserInstantanesAvecFormulairePrincipal();

        mettreAJourExemplesPrenoms();
        appliquerThemeSelonConfiguration();
        effacerErreursValidation();
    }

    private void appliquerInstantanePersistant(InstantaneDossier instantane) {
        checkboxChangementPrenoms.setSelected(instantane.changementPrenoms());
        checkboxPronomNeutre.setSelected(instantane.pronomNeutre());
        checkboxPacs.setSelected(instantane.pacsContracte());
        champPrenomsUsage.setEnabled(instantane.changementPrenoms());

        champPrenomsEtatCivil.setText(instantane.prenomsEtatCivil());
        champPrenomsUsage.setText(instantane.prenomsUsage());
        champNomFamille.setText(instantane.nomFamille());
        champLieuNaissance.setText(instantane.lieuNaissance());
        champAdresse.setText(instantane.adresse());
        zoneTribunal.setText(instantane.tribunal());
        zoneRecit.setText(instantane.recit());
        champVilleActuelle.setText(instantane.villeActuelle());
        champNationalite.setText(instantane.nationalite());
        champProfession.setText(instantane.profession());

        appliquerDateNaissanceDepuisTexte(instantane.dateNaissance());
        definirSelectionCombo(comboSexeEtatCivil, instantane.sexeEtatCivil(), "Masculin");
        definirSelectionCombo(comboSituationMatrimoniale, instantane.situationMatrimoniale(), "");
        definirSelectionCombo(comboSituationEnfants, instantane.situationEnfants(), "");

        if (!instantane.piecesJustificativesDetaillees().isEmpty()) {
            appliquerPiecesDepuisDetails(instantane.piecesJustificativesDetaillees(), instantane.changementPrenoms());
        } else if (!instantane.piecesJustificatives().isEmpty()) {
            piecesActuelles = new ArrayList<>(instantane.piecesJustificatives());
            piecesJointesParIntitule = new LinkedHashMap<>();
            synchroniserPiecesJointesParIntitule();
        } else {
            piecesActuelles = new ArrayList<>(piecesParDefautPourMode(instantane.changementPrenoms()));
            piecesJointesParIntitule = new LinkedHashMap<>();
            synchroniserPiecesJointesParIntitule();
        }
        nettoyerPiecesPourMode(instantane.changementPrenoms());
        dernierModeChangementPrenoms = instantane.changementPrenoms();
    }

    private void appliquerThemeSelonConfiguration() {
        if (preferenceThemeApplication != null && preferenceThemeApplication.estForce()) {
            diffuseurTheme.publierMode(preferenceThemeApplication.modeForce());
            return;
        }
        diffuseurTheme.publierChoixPronom(checkboxPronomNeutre.isSelected());
    }

    private void appliquerDateNaissanceDepuisTexte(String texteDateNaissance) {
        if (ParseursDate.dateSaisieValide(texteDateNaissance)) {
            LocalDate dateNaissance = ParseursDate.parserDateSaisie(texteDateNaissance.trim());
            definirSelectionCombo(comboAnneeNaissance, Integer.toString(dateNaissance.getYear()), Integer.toString(LocalDate.now().getYear()));
            definirSelectionCombo(comboMoisNaissance, Integer.toString(dateNaissance.getMonthValue()), "12");
            ajusterJourNaissance();
            definirSelectionCombo(comboJourNaissance, Integer.toString(dateNaissance.getDayOfMonth()), "13");
            return;
        }
        appliquerDateNaissanceParDefaut();
    }

    private void definirSelectionCombo(JComboBox<String> comboBox, String valeur, String valeurSecours) {
        String candidat = valeur == null ? "" : valeur.trim();
        if (!candidat.isEmpty() && comboContient(comboBox, candidat)) {
            comboBox.setSelectedItem(candidat);
            return;
        }
        if (comboContient(comboBox, valeurSecours)) {
            comboBox.setSelectedItem(valeurSecours);
        }
    }

    private boolean comboContient(JComboBox<String> comboBox, String valeur) {
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            if (Objects.equals(comboBox.getItemAt(i), valeur)) {
                return true;
            }
        }
        return false;
    }

    private InstantaneDossier construireInstantanePersistant() {
        if (memoriserDonneesSaisies) {
            return construireInstantane();
        }
        return creerInstantaneVide();
    }

    private InstantaneLettreUniversite construireInstantaneLettreUniversitePersistant() {
        if (memoriserDonneesSaisies) {
            return instantaneLettreUniversite;
        }
        return InstantaneLettreUniversite.vide();
    }

    private InstantaneLettreAdministration construireInstantaneLettreAdministrationPersistant() {
        if (memoriserDonneesSaisies) {
            return instantaneLettreAdministration;
        }
        return InstantaneLettreAdministration.vide();
    }

    private InstantaneDossier creerInstantaneVide() {
        return new InstantaneDossier(false, false, "", "", "", "", "", "", "", "", "", "", List.of(), "", "", "", "", false, List.of());
    }

    private void planifierEcritureEtat() {
        EtatDossierPersistant persistedState = new EtatDossierPersistant(construireInstantanePersistant(), checkboxEffacerApresExport.isSelected(), confirmerQuitterAvecDonnees, memoriserDonneesSaisies, preferenceThemeApplication.code(), dossierSortieParDefaut == null ? "" : dossierSortieParDefaut.toString(), construireInstantaneLettreUniversitePersistant(), construireInstantaneLettreAdministrationPersistant());
        etatPersistantEnAttente.set(persistedState);
        if (ecriturePersistancePlanifiee.compareAndSet(false, true)) {
            executricePersistanceEtat.execute(this::viderEtatEnAttente);
        }
    }

    private void viderEtatEnAttente() {
        try {
            EtatDossierPersistant persistedState = etatPersistantEnAttente.getAndSet(null);
            while (persistedState != null) {
                persistanceEtat.sauvegarder(persistedState);
                persistedState = etatPersistantEnAttente.getAndSet(null);
            }
        } finally {
            ecriturePersistancePlanifiee.set(false);
            if (etatPersistantEnAttente.get() != null && ecriturePersistancePlanifiee.compareAndSet(false, true)) {
                executricePersistanceEtat.execute(this::viderEtatEnAttente);
            }
        }
    }

    private void effacerEtatPersistant() {
        etatPersistantEnAttente.set(null);
        executricePersistanceEtat.execute(persistanceEtat::effacer);
    }

    private void initialiserPiecesParDefaut() {
        piecesParDefautAvecPrenoms = Arrays.asList("Copie intégrale de l’acte de naissance", "Copie de la pièce d’identité (carte d’identité ou passeport)", "Justificatif de domicile", "Attestation de l’employeur confirmant l’usage des prénoms d’usage dans l’environnement professionnel", "Carte étudiante faisant état de l'usage des prénoms d'usage à l'université", "Attestations de proches confirmant la reconnaissance sociale dans le genre revendiqué", "Ordonnance de traitement hormono-substitutif (facultatif)", "Photographies", "Photocopies de courrier reçu au prénom d’usage");
        piecesParDefautSexe = Arrays.asList("Copie intégrale de l’acte de naissance", "Copie de la pièce d’identité (carte d’identité ou passeport)", "Justificatif de domicile", "Attestation de l’employeur confirmant l’usage du sexe d’usage dans l’environnement professionnel", "Carte étudiante faisant état de l'usage du sexe d'usage à l'université", "Attestations de proches confirmant la reconnaissance sociale dans le genre revendiqué", "Ordonnance de traitement hormono-substitutif (facultatif)", "Photographies", "Photocopies de courrier reçu au sexe d’usage", NOTIF_ITEM);
    }

    private boolean listesEgales(List<String> a, List<String> b) {
        if (a == b) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }
        if (a.size() != b.size()) {
            return false;
        }
        for (int i = 0; i < a.size(); i++) {
            if (!Objects.equals(a.get(i), b.get(i))) {
                return false;
            }
        }
        return true;
    }

    private List<String> piecesParDefautPourMode(boolean changementPrenoms) {
        return changementPrenoms ? piecesParDefautAvecPrenoms : piecesParDefautSexe;
    }

    private void nettoyerPiecesPourMode(boolean changementPrenoms) {
        if (changementPrenoms) {
            List<String> filtered = new ArrayList<>();
            for (String s : piecesActuelles) {
                String low = s == null ? "" : s.toLowerCase(Locale.ROOT);
                if (low.startsWith("notification de changement de prénoms")) {
                    continue;
                }
                filtered.add(s);
            }
            piecesActuelles = filtered;
        }
        synchroniserPiecesJointesParIntitule();
    }

    private void basculerPiecesSiModeChange(boolean newMode) {
        List<String> prevDefault = piecesParDefautPourMode(dernierModeChangementPrenoms);
        if (listesEgales(piecesActuelles, prevDefault)) {
            piecesActuelles = new ArrayList<>(piecesParDefautPourMode(newMode));
        }
        nettoyerPiecesPourMode(newMode);
        synchroniserPiecesJointesParIntitule();
        dernierModeChangementPrenoms = newMode;
    }

    private void synchroniserPiecesJointesParIntitule() {
        if (piecesJointesParIntitule == null) {
            piecesJointesParIntitule = new LinkedHashMap<>();
        }
        Map<String, List<PieceJointe>> ordered = new LinkedHashMap<>();
        List<String> titresNormalises = new ArrayList<>();
        for (String piece : piecesActuelles) {
            if (piece == null) {
                continue;
            }
            String titre = piece.trim();
            if (titre.isEmpty()) {
                continue;
            }
            titresNormalises.add(titre);
            ordered.put(titre, new ArrayList<>(piecesJointesParIntitule.getOrDefault(titre, List.of())));
        }
        piecesActuelles = titresNormalises;
        piecesJointesParIntitule = ordered;
    }

    private void appliquerPiecesDepuisDetails(List<PieceJustificative> piecesDetaillees, boolean changementPrenoms) {
        if (piecesDetaillees == null || piecesDetaillees.isEmpty()) {
            piecesActuelles = new ArrayList<>(piecesParDefautPourMode(changementPrenoms));
            piecesJointesParIntitule = new LinkedHashMap<>();
            synchroniserPiecesJointesParIntitule();
            return;
        }

        piecesActuelles = new ArrayList<>();
        Map<String, List<PieceJointe>> map = new LinkedHashMap<>();
        for (PieceJustificative piece : piecesDetaillees) {
            if (piece == null) {
                continue;
            }
            String titre = piece.intitule() == null ? "" : piece.intitule().trim();
            if (titre.isEmpty()) {
                continue;
            }
            piecesActuelles.add(titre);
            map.put(titre, new ArrayList<>(piece.piecesJointes()));
        }
        piecesJointesParIntitule = map;
        synchroniserPiecesJointesParIntitule();
    }

    private List<PieceJustificative> construirePiecesDetaillees() {
        List<PieceJustificative> pieces = new ArrayList<>(piecesActuelles.size());
        for (String titre : piecesActuelles) {
            pieces.add(new PieceJustificative(titre, new ArrayList<>(piecesJointesParIntitule.getOrDefault(titre, List.of()))));
        }
        return pieces;
    }

    private void initialiserMiseEnPage() {
        panneauRacine.add(construireEntete(), BorderLayout.NORTH);
        panneauRacine.add(construireCarteScrollable(), BorderLayout.CENTER);
        setContentPane(panneauRacine);
    }

    private JComponent construireEntete() {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setOpaque(false);

        JPanel topRow = new JPanel(new BorderLayout(theme.spacing().inlineGap(), 0));
        topRow.setOpaque(false);
        topRow.setAlignmentX(LEFT_ALIGNMENT);
        topRow.add(labelTitre, BorderLayout.WEST);

        JPanel panneauActionsEntete = new JPanel();
        panneauActionsEntete.setLayout(new BoxLayout(panneauActionsEntete, BoxLayout.X_AXIS));
        panneauActionsEntete.setOpaque(false);
        panneauActionsEntete.add(boutonConfiguration);
        panneauActionsEntete.add(Box.createHorizontalStrut(Math.max(1, theme.spacing().inlineGap() / 2)));
        panneauActionsEntete.add(boutonAide);

        topRow.add(panneauActionsEntete, BorderLayout.EAST);

        header.add(topRow);
        header.add(Box.createVerticalStrut(Math.max(1, theme.spacing().inlineGap() / 2)));
        header.add(labelSousTitre);
        header.add(Box.createVerticalStrut(theme.spacing().inlineGap()));
        header.add(labelRaccourcis);

        return header;
    }

    private JComponent construireCarteScrollable() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = margesLigne();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        int y = 0;

        y = ajouterTitreSection(panneauCarte, gbc, y, "Type de requête");
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.gridwidth = 2;
        panneauCarte.add(checkboxChangementPrenoms, gbc);
        y++;

        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.gridwidth = 2;
        panneauCarte.add(checkboxPronomNeutre, gbc);
        y++;

        y = ajouterTitreSection(panneauCarte, gbc, y, "Identité");
        y = ajouterLigneLibellee(panneauCarte, gbc, y, "Prénoms à l'état civil", champPrenomsEtatCivil);
        y = ajouterLigneLibellee(panneauCarte, gbc, y, "Prénoms d'usage / demandés", champPrenomsUsage);
        y = ajouterLigneLibellee(panneauCarte, gbc, y, "Nom de famille", champNomFamille);
        y = ajouterLigneLibellee(panneauCarte, gbc, y, "Date de naissance", panneauDateNaissance);
        y = ajouterLigneLibellee(panneauCarte, gbc, y, "Lieu de naissance", champLieuNaissance);
        y = ajouterLigneLibellee(panneauCarte, gbc, y, "Sexe à l'état civil", comboSexeEtatCivil);
        y = ajouterLigneLibellee(panneauCarte, gbc, y, "Nationalité", champNationalite);

        y = ajouterTitreSection(panneauCarte, gbc, y, "Situation personnelle");
        scrollAdresse = creerScrollStyle(champAdresse, 92);
        y = ajouterLigneLibellee(panneauCarte, gbc, y, "Adresse", scrollAdresse);
        y = ajouterLigneLibellee(panneauCarte, gbc, y, "Profession / Employeur", champProfession);
        y = ajouterLigneLibellee(panneauCarte, gbc, y, "Statut matrimonial", comboSituationMatrimoniale);
        y = ajouterLigneLibellee(panneauCarte, gbc, y, "Situation parentale", comboSituationEnfants);

        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.gridwidth = 2;
        panneauCarte.add(checkboxPacs, gbc);
        y++;

        y = ajouterLigneLibellee(panneauCarte, gbc, y, "Ville actuelle (Fait à)", champVilleActuelle);

        y = ajouterTitreSection(panneauCarte, gbc, y, "Contexte juridique et récit");
        scrollTribunal = creerScrollStyle(zoneTribunal, 118);
        y = ajouterLigneLibellee(panneauCarte, gbc, y, "Tribunal judiciaire compétent", scrollTribunal);

        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.gridwidth = 1;
        JLabel narrativeLabel = creerLabel("Récit de vie");
        narrativeLabel.setLabelFor(zoneRecit);
        panneauCarte.add(narrativeLabel, gbc);

        gbc.gridx = 1;
        gbc.weighty = 1;
        scrollRecit = creerScrollStyle(zoneRecit, 190);
        panneauCarte.add(scrollRecit, gbc);
        gbc.weighty = 0;
        y++;

        y = ajouterLigneErreur(panneauCarte, gbc, y, scrollRecit);

        y = ajouterTitreSection(panneauCarte, gbc, y, "Actions");

        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.gridwidth = 2;
        labelMessageFormulaire.setText(" ");
        panneauCarte.add(labelMessageFormulaire, gbc);
        y++;

        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.gridwidth = 2;
        panneauCarte.add(checkboxEffacerApresExport, gbc);
        y++;

        JPanel actions = new JPanel();
        actions.setLayout(new BoxLayout(actions, BoxLayout.X_AXIS));
        actions.setOpaque(false);
        actions.add(boutonPieces);
        actions.add(Box.createHorizontalStrut(theme.spacing().inlineGap()));
        actions.add(boutonEffacer);
        actions.add(Box.createHorizontalGlue());
        actions.add(boutonAutresDocuments);
        actions.add(Box.createHorizontalStrut(theme.spacing().inlineGap()));
        actions.add(boutonGenerer);

        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.gridwidth = 2;
        panneauCarte.add(actions, gbc);
        y++;

        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.weighty = 1;
        panneauCarte.add(Box.createVerticalGlue(), gbc);

        JScrollPane scrollPane = new JScrollPane(panneauCarte);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(18);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setOpaque(false);

        return scrollPane;
    }

    private int ajouterTitreSection(JPanel panel, GridBagConstraints gbc, int y, String title) {
        JLabel sectionLabel = new JLabel(title);
        labelsSections.add(sectionLabel);

        JPanel line = new JPanel(new BorderLayout(theme.spacing().inlineGap(), 0));
        line.setOpaque(false);
        line.add(sectionLabel, BorderLayout.WEST);

        JPanel separator = new JPanel();
        separator.setPreferredSize(new Dimension(1, 1));
        separateursSections.add(separator);
        line.add(separator, BorderLayout.CENTER);

        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.gridwidth = 2;
        gbc.insets = margesSection();
        panel.add(line, gbc);

        gbc.insets = margesLigne();
        return y + 1;
    }

    private int ajouterLigneLibellee(JPanel panel, GridBagConstraints gbc, int y, String label, JComponent component) {
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.gridwidth = 1;
        JLabel rowLabel = creerLabel(label);
        rowLabel.setLabelFor(cibleLibelle(component));
        panel.add(rowLabel, gbc);

        gbc.gridx = 1;
        panel.add(component, gbc);
        y++;

        return ajouterLigneErreur(panel, gbc, y, component);
    }

    private JComponent cibleLibelle(JComponent component) {
        if (component == panneauDateNaissance) {
            return comboJourNaissance;
        }
        if (component instanceof JScrollPane ascenseur && ascenseur.getViewport() != null && ascenseur.getViewport().getView() instanceof JComponent contenu) {
            return contenu;
        }
        return component;
    }

    private int ajouterLigneErreur(JPanel panel, GridBagConstraints gbc, int y, JComponent component) {
        JLabel error = new JLabel(" ");

        gbc.gridx = 1;
        gbc.gridy = y;
        gbc.insets = margesErreurLigne();
        panel.add(error, gbc);
        gbc.insets = margesLigne();

        labelsErreurs.put(component, error);
        borduresNormales.put(component, component.getBorder());

        return y + 1;
    }

    private JLabel creerLabel(String text) {
        JLabel label = new JLabel(text);
        labelsLignes.add(label);
        return label;
    }

    private JScrollPane creerScrollStyle(JTextArea area, int preferredHeight) {
        JScrollPane scrollPane = new JScrollPane(area);
        stylerAscenseur(scrollPane);
        scrollPane.setPreferredSize(new Dimension(300, preferredHeight));
        return scrollPane;
    }

    private void stylerChamp(JComponent component) {
        if (component instanceof JTextComponent textComponent) {
            textComponent.setFont(theme.typography().input());
            textComponent.setBackground(theme.palette().fieldBackground());
            textComponent.setForeground(theme.palette().bodyText());
            textComponent.setCaretColor(theme.palette().focus());
            textComponent.setBorder(BorderFactory.createCompoundBorder(new LineBorder(theme.palette().border(), 1, true), new EmptyBorder(theme.spacing().fieldInsetY(), theme.spacing().fieldInsetX(), theme.spacing().fieldInsetY(), theme.spacing().fieldInsetX())));
        } else if (component instanceof JComboBox<?> comboBox) {
            comboBox.setFont(theme.typography().input());
            comboBox.setBackground(theme.palette().fieldBackground());
            comboBox.setForeground(theme.palette().bodyText());
            comboBox.setBorder(BorderFactory.createEmptyBorder());
        }
    }

    private void stylerZoneTexte(JTextArea area) {
        area.setFont(theme.typography().input());
        area.setBackground(theme.palette().fieldBackground());
        area.setForeground(theme.palette().bodyText());
        area.setCaretColor(theme.palette().focus());
        area.setBorder(new EmptyBorder(theme.spacing().fieldInsetY(), theme.spacing().fieldInsetX(), theme.spacing().fieldInsetY(), theme.spacing().fieldInsetX()));
    }

    private void stylerAscenseur(JScrollPane ascenseur) {
        if (ascenseur == null) {
            return;
        }
        int marge = Math.max(1, theme.spacing().inlineGap() / 4);
        ascenseur.setBorder(BorderFactory.createCompoundBorder(new LineBorder(theme.palette().border(), 1, true), new EmptyBorder(marge, marge, marge, marge)));
        if (ascenseur.getViewport() != null) {
            ascenseur.getViewport().setOpaque(true);
            ascenseur.getViewport().setBackground(theme.palette().fieldBackground());
        }
    }

    private void mettreAJourBorduresNormales() {
        for (Map.Entry<JComponent, Border> entree : borduresNormales.entrySet()) {
            entree.setValue(entree.getKey().getBorder());
        }
    }

    private void stylerCheckbox(JCheckBox checkBox) {
        checkBox.setOpaque(false);
        checkBox.setFont(theme.typography().input());
        checkBox.setForeground(theme.palette().bodyText());
    }

    private void appliquerRoleBouton(JButton button, RoleBouton role) {
        rolesBoutons.put(button, role);
        button.setUI(new BasicButtonUI());
        button.setFocusPainted(false);
        button.setRolloverEnabled(true);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        if (role == RoleBouton.PRIMARY) {
            button.setFont(theme.typography().buttonPrimary());
        } else {
            button.setFont(theme.typography().buttonSecondary());
        }
        if (role == RoleBouton.ICON) {
            button.setOpaque(false);
            button.setContentAreaFilled(false);
            button.setBorderPainted(false);
            button.setBorder(new EmptyBorder(0, 0, 0, 0));
        } else {
            button.setOpaque(true);
            button.setContentAreaFilled(true);
            button.setBorderPainted(true);
            button.setBorder(new EmptyBorder(theme.spacing().buttonInsetY(), theme.spacing().buttonInsetX(), theme.spacing().buttonInsetY(), theme.spacing().buttonInsetX()));
        }
        mettreAJourVisuelBouton(button, role);
        for (javax.swing.event.ChangeListener listener : button.getChangeListeners()) {
            if (listener instanceof EcouteurChangementBoutonTheme) {
                button.removeChangeListener(listener);
            }
        }
        for (java.awt.event.FocusListener listener : button.getFocusListeners()) {
            if (listener instanceof EcouteurFocusBoutonTheme) {
                button.removeFocusListener(listener);
            }
        }
        button.addChangeListener(new EcouteurChangementBoutonTheme(role));
        button.addFocusListener(new EcouteurFocusBoutonTheme(role));
    }

    private void mettreAJourVisuelBouton(JButton button, RoleBouton role) {
        TokensEtatBouton tokens = tokensPourRole(role);
        if (role == RoleBouton.ICON) {
            button.setOpaque(false);
            button.setContentAreaFilled(false);
            button.setBorderPainted(false);
            button.setBackground(new Color(0, 0, 0, 0));
            button.setForeground(tokens.foreground());
            button.setBorder(new EmptyBorder(0, 0, 0, 0));
            return;
        }
        if (!button.isEnabled()) {
            button.setBackground(tokens.disabledBackground());
            button.setForeground(tokens.disabledForeground());
            button.setBorder(BorderFactory.createCompoundBorder(new LineBorder(tokens.border(), 1, true), new EmptyBorder(theme.spacing().buttonInsetY(), theme.spacing().buttonInsetX(), theme.spacing().buttonInsetY(), theme.spacing().buttonInsetX())));
            return;
        }

        if (button.getModel().isPressed()) {
            button.setBackground(tokens.pressedBackground());
        } else if (button.getModel().isRollover()) {
            button.setBackground(tokens.hoverBackground());
        } else {
            button.setBackground(tokens.background());
        }
        button.setForeground(tokens.foreground());

        if (button.isFocusOwner()) {
            button.setBorder(BorderFactory.createCompoundBorder(new LineBorder(tokens.focusRing(), 2, true), new EmptyBorder(Math.max(0, theme.spacing().buttonInsetY() - 1), Math.max(0, theme.spacing().buttonInsetX() - 1), Math.max(0, theme.spacing().buttonInsetY() - 1), Math.max(0, theme.spacing().buttonInsetX() - 1))));
        } else {
            button.setBorder(BorderFactory.createCompoundBorder(new LineBorder(tokens.border(), 1, true), new EmptyBorder(theme.spacing().buttonInsetY(), theme.spacing().buttonInsetX(), theme.spacing().buttonInsetY(), theme.spacing().buttonInsetX())));
        }
    }

    private TokensEtatBouton tokensPourRole(RoleBouton role) {
        return switch (role) {
            case PRIMARY -> theme.palette().primaryButton();
            case SECONDARY -> theme.palette().secondaryButton();
            case DANGER -> theme.palette().dangerButton();
            case ICON -> theme.palette().iconButton();
        };
    }

    private void enregistrerComposantsValidation() {
        composantsParChamp.put(ChampDossier.PRENOMS_ETAT_CIVIL, champPrenomsEtatCivil);
        composantsParChamp.put(ChampDossier.PRENOMS_USAGE, champPrenomsUsage);
        composantsParChamp.put(ChampDossier.NOM_FAMILLE, champNomFamille);
        composantsParChamp.put(ChampDossier.DATE_NAISSANCE, panneauDateNaissance);
        composantsParChamp.put(ChampDossier.LIEU_NAISSANCE, champLieuNaissance);
        composantsParChamp.put(ChampDossier.SEXE_ETAT_CIVIL, comboSexeEtatCivil);
        composantsParChamp.put(ChampDossier.ADRESSE, scrollAdresse);
        composantsParChamp.put(ChampDossier.NATIONALITE, champNationalite);
        composantsParChamp.put(ChampDossier.PROFESSION, champProfession);
        composantsParChamp.put(ChampDossier.SITUATION_MATRIMONIALE, comboSituationMatrimoniale);
        composantsParChamp.put(ChampDossier.SITUATION_ENFANTS, comboSituationEnfants);
        composantsParChamp.put(ChampDossier.VILLE_ACTUELLE, champVilleActuelle);
        composantsParChamp.put(ChampDossier.TRIBUNAL, scrollTribunal);
        composantsParChamp.put(ChampDossier.RECIT, scrollRecit);
    }

    private void initialiserEcouteurs() {
        checkboxChangementPrenoms.addActionListener(e -> {
            boolean actif = checkboxChangementPrenoms.isSelected();
            champPrenomsUsage.setEnabled(actif);
            basculerPiecesSiModeChange(actif);
            mettreAJourExemplesPrenoms();
            if (!actif) {
                effacerErreurChamp(ChampDossier.PRENOMS_USAGE);
            } else {
                validerChampEnLigne(ChampDossier.PRENOMS_USAGE);
            }
            marquerModifie();
        });

        checkboxPronomNeutre.addActionListener(e -> {
            appliquerThemeSelonConfiguration();
            mettreAJourExemplePrenomsUsage();
            marquerModifie();
            afficherPopupsVisibiliteSelonContexte(ContexteAffichagePopup.PRONOM_NEUTRE_BASCULE);
        });

        comboSexeEtatCivil.addActionListener(e -> mettreAJourExemplesPrenoms());

        boutonConfiguration.addActionListener(e -> ouvrirDialogueConfiguration());
        boutonAide.addActionListener(e -> ouvrirDialogueAide());
        boutonPieces.addActionListener(e -> ouvrirDialoguePieces());
        boutonEffacer.addActionListener(e -> confirmerEffacement());
        boutonAutresDocuments.addActionListener(e -> ouvrirDialogueAutresDocuments());
        boutonGenerer.addActionListener(e -> genererDocument());
    }

    private void initialiserMnemoniques() {
        checkboxChangementPrenoms.setMnemonic(KeyEvent.VK_L);
        checkboxPronomNeutre.setMnemonic(KeyEvent.VK_N);
        checkboxPacs.setMnemonic(KeyEvent.VK_P);
        checkboxEffacerApresExport.setMnemonic(KeyEvent.VK_X);
        boutonPieces.setMnemonic(KeyEvent.VK_J);
        boutonEffacer.setMnemonic(KeyEvent.VK_F);
        boutonAutresDocuments.setMnemonic(KeyEvent.VK_O);
        boutonGenerer.setMnemonic(KeyEvent.VK_G);
        boutonAide.setMnemonic(KeyEvent.VK_I);
        boutonConfiguration.setMnemonic(KeyEvent.VK_C);
    }

    private void initialiserRaccourcisClavier() {
        int masqueMenu = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();

        InputMap carteEntrees = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap carteActions = getRootPane().getActionMap();

        carteEntrees.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, masqueMenu), "generer-document");
        carteActions.put("generer-document", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (boutonGenerer.isEnabled()) {
                    boutonGenerer.doClick();
                }
            }
        });

        carteEntrees.put(KeyStroke.getKeyStroke(KeyEvent.VK_J, masqueMenu), "open-piecesJustificatives");
        carteActions.put("open-piecesJustificatives", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (boutonPieces.isEnabled()) {
                    boutonPieces.doClick();
                }
            }
        });

        carteEntrees.put(KeyStroke.getKeyStroke(KeyEvent.VK_I, masqueMenu), "open-help");
        carteEntrees.put(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0), "open-help");
        carteActions.put("open-help", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (boutonAide.isEnabled()) {
                    boutonAide.doClick();
                }
            }
        });

        carteEntrees.put(KeyStroke.getKeyStroke(KeyEvent.VK_COMMA, masqueMenu), "open-configuration");
        carteActions.put("open-configuration", new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                if (boutonConfiguration.isEnabled()) {
                    boutonConfiguration.doClick();
                }
            }
        });

        carteEntrees.put(KeyStroke.getKeyStroke(KeyEvent.VK_L, masqueMenu | InputEvent.SHIFT_DOWN_MASK), "toggle-change-names");
        carteActions.put("toggle-change-names", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkboxChangementPrenoms.doClick();
            }
        });

        carteEntrees.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "effacer-inline-errors");
        carteActions.put("effacer-inline-errors", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                effacerErreursValidation();
            }
        });
    }

    private void initialiserPopupsVisibilite() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                if (popupsVisibiliteInitialises) {
                    return;
                }
                popupsVisibiliteInitialises = true;
                SwingUtilities.invokeLater(() -> afficherPopupsVisibiliteSelonContexte(ContexteAffichagePopup.DEMARRAGE));
            }
        });
    }

    private void afficherPopupsVisibiliteSelonContexte(ContexteAffichagePopup contexte) {
        LocalDate dateCourante = LocalDate.now();
        int jour = dateCourante.getDayOfMonth();
        int mois = dateCourante.getMonthValue();
        ModeTheme modeActif = determinerModeThemeActif();

        if (jour == 17 && mois == 5 && contexte == ContexteAffichagePopup.DEMARRAGE) {
            afficherPopupVisibilite(TypePopupVisibilite.LUTTE_LGBTQIPHOBIES);
            return;
        }
        if (jour == 11 && mois == 10 && contexte == ContexteAffichagePopup.DEMARRAGE) {
            afficherPopupVisibilite(TypePopupVisibilite.COMING_OUT_DAY);
            return;
        }
        if (jour == 24 && mois == 5 && contexte == ContexteAffichagePopup.DEMARRAGE) {
            afficherPopupVisibilite(TypePopupVisibilite.VISIBILITE_PANSEXUELLE);
            return;
        }
        if (jour == 23 && mois == 9 && contexte == ContexteAffichagePopup.DEMARRAGE) {
            afficherPopupVisibilite(TypePopupVisibilite.JOURNEE_BISEXUALITE);
            return;
        }
        if (jour == 31 && mois == 3 && contexte == ContexteAffichagePopup.DEMARRAGE) {
            afficherPopupVisibilite(TypePopupVisibilite.TDOV);
            return;
        }
        if (jour == 14 && mois == 7 && modeActif == ModeTheme.NON_BINAIRE && (contexte == ContexteAffichagePopup.DEMARRAGE || contexte == ContexteAffichagePopup.PRONOM_NEUTRE_BASCULE || contexte == ContexteAffichagePopup.THEME_MODIFIE_DANS_CONFIG)) {
            afficherPopupVisibilite(TypePopupVisibilite.VISIBILITE_NON_BINAIRE);
            return;
        }
        if (jour == 26 && mois == 4 && modeActif == ModeTheme.LESBIEN && (contexte == ContexteAffichagePopup.DEMARRAGE || contexte == ContexteAffichagePopup.THEME_MODIFIE_DANS_CONFIG)) {
            afficherPopupVisibilite(TypePopupVisibilite.VISIBILITE_LESBIENNE);
            return;
        }
        if (jour == 8 && mois == 11 && modeActif == ModeTheme.INTERSEXE && (contexte == ContexteAffichagePopup.DEMARRAGE || contexte == ContexteAffichagePopup.THEME_MODIFIE_DANS_CONFIG)) {
            afficherPopupVisibilite(TypePopupVisibilite.VISIBILITE_INTERSEXE);
        }
    }

    private ModeTheme determinerModeThemeActif() {
        if (preferenceThemeApplication != null && preferenceThemeApplication.estForce()) {
            ModeTheme force = preferenceThemeApplication.modeForce();
            if (force != null) {
                return force;
            }
        }
        return checkboxPronomNeutre.isSelected() ? ModeTheme.NON_BINAIRE : ModeTheme.TRANS;
    }

    private void afficherPopupVisibilite(TypePopupVisibilite typePopupVisibilite) {
        afficherPopupVisibilite(creerConfigurationPopup(typePopupVisibilite));
    }

    int afficherPopupsVisibiliteEnSequencePourTest() {
        int nombrePopups = 0;
        for (TypePopupVisibilite typePopupVisibilite : TypePopupVisibilite.values()) {
            afficherPopupVisibilite(typePopupVisibilite);
            nombrePopups++;
        }
        return nombrePopups;
    }

    private ConfigurationPopupVisibilite creerConfigurationPopup(TypePopupVisibilite typePopupVisibilite) {
        return switch (typePopupVisibilite) {
            case TDOV ->
                    new ConfigurationPopupVisibilite("TDoV – Trans Day of Visibility", "TDoV – Trans Day of Visibility", "C'est le TDoV t'es obligé·e d'être visible bg ! Sois visible ! T'es pas visible ?? Sois visible putain !", FondPopupVisibilite.TRANS, theme.palette().bodyText(), theme.palette().bodyText(), "Tkt frr jsuis un max visible");
            case VISIBILITE_NON_BINAIRE ->
                    new ConfigurationPopupVisibilite("Journée internationale de visibilité des personnes non-binaires", "Journée internationale de visibilité des personnes non-binaires", "RIP la fête nationale ici c'est les blue hair and pronouns qu'on célèbre aujourd'hui !", FondPopupVisibilite.NON_BINAIRE, theme.palette().bodyText(), theme.palette().bodyText(), "Se teindre les cheveux");
            case VISIBILITE_LESBIENNE ->
                    new ConfigurationPopupVisibilite("Journée internationale de visibilité lesbienne", "Journée internationale de visibilité lesbienne", "Bravo les lesbiennes ! Continuez comme ça.", FondPopupVisibilite.LESBIEN, theme.palette().bodyText(), theme.palette().bodyText(), "Bravo à moi, tqt j'ai pas prévu d'arrêter");
            case VISIBILITE_INTERSEXE ->
                    new ConfigurationPopupVisibilite("Journée internationale de visibilité intersexe", "Journée internationale de visibilité intersexe", "La visibilité intersexe n’est pas optionnelle ! 😍\n(Même si t’as dû choisir ce thème dans les réglages)", FondPopupVisibilite.INTERSEXE, COULEUR_BLANCHE, COULEUR_BLANCHE, "Ok pétasse");
            case LUTTE_LGBTQIPHOBIES ->
                    new ConfigurationPopupVisibilite("Journée internationale contre l'homophobie, la transphobie, et la biphobie", "Journée internationale contre l'homophobie, la transphobie, et la biphobie", "Et la biphobie, on oublie pas la biphobie ! Ces trè grave...", FondPopupVisibilite.RAINBOW, COULEUR_BLANCHE, COULEUR_BLANCHE, "Jsuis pas biphobe jte jure");
            case COMING_OUT_DAY ->
                    new ConfigurationPopupVisibilite("Coming out day", "Coming out day", "N'oublie pas de dire à absolument tout le monde que tu es elgéteubé·e aujourd'hui ! Ces trè importan.", FondPopupVisibilite.RAINBOW, COULEUR_BLANCHE, COULEUR_BLANCHE, "Alexa, joue du Diana Ross");
            case VISIBILITE_PANSEXUELLE ->
                    new ConfigurationPopupVisibilite("Journée internationale de la visibilité pansexuelle", "Journée internationale de la visibilité pansexuelle", "C'est la journée spéciale confused slutbag !", FondPopupVisibilite.PANSEXUEL, theme.palette().bodyText(), theme.palette().bodyText(), "Slut-shame les pans");
            case JOURNEE_BISEXUALITE ->
                    new ConfigurationPopupVisibilite("Journée de la bisexualité", "Journée de la bisexualité", "À voile et à vapeur, iels bouffent à tous les rateliers !", FondPopupVisibilite.BISEXUEL, COULEUR_BLANCHE, COULEUR_BLANCHE, "La biphobie est une vraie oppression, source: tkt frr");
        };
    }

    private void afficherPopupVisibilite(ConfigurationPopupVisibilite configurationPopup) {
        JDialog dialogue = new JDialog(this, configurationPopup.titreFenetre(), true);
        appliquerIconeDialogue(dialogue);

        PanneauFondPopup panneauFond = new PanneauFondPopup(configurationPopup.fond());
        panneauFond.setLayout(new BorderLayout(0, theme.spacing().blockGap()));
        panneauFond.setBorder(new EmptyBorder(theme.spacing().cardInset(), theme.spacing().cardInset(), theme.spacing().cardInset(), theme.spacing().cardInset()));

        JLabel labelTitrePopup = new JLabel(configurationPopup.titreAffiche());
        labelTitrePopup.setFont(theme.typography().title().deriveFont(Font.BOLD, theme.typography().title().getSize2D() + 6f));
        labelTitrePopup.setForeground(configurationPopup.couleurTitre());
        labelTitrePopup.setHorizontalAlignment(JLabel.CENTER);

        JLabel labelMessagePopup = new JLabel("<html><div style='text-align:center;width:720px;'>" + echapperHtml(configurationPopup.message()).replace("\n", "<br/>") + "</div></html>");
        labelMessagePopup.setFont(theme.typography().section());
        labelMessagePopup.setForeground(configurationPopup.couleurMessage());
        labelMessagePopup.setHorizontalAlignment(JLabel.CENTER);

        JPanel panneauTexte = new JPanel();
        panneauTexte.setOpaque(false);
        panneauTexte.setLayout(new BoxLayout(panneauTexte, BoxLayout.Y_AXIS));
        labelTitrePopup.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelMessagePopup.setAlignmentX(Component.CENTER_ALIGNMENT);
        panneauTexte.add(Box.createVerticalStrut(Math.max(6, theme.spacing().inlineGap())));
        panneauTexte.add(labelTitrePopup);
        panneauTexte.add(Box.createVerticalStrut(theme.spacing().blockGap()));
        panneauTexte.add(labelMessagePopup);
        panneauTexte.add(Box.createVerticalGlue());

        JButton boutonFermerPopup = new JButton(configurationPopup.libelleBouton());
        StyliseurBoutonTheme.appliquer(boutonFermerPopup, theme.palette().primaryButton(), theme, theme.typography().buttonPrimary());
        boutonFermerPopup.addActionListener(e -> dialogue.dispose());

        JPanel panneauActions = new JPanel();
        panneauActions.setOpaque(false);
        panneauActions.setLayout(new BoxLayout(panneauActions, BoxLayout.X_AXIS));
        panneauActions.setBorder(new EmptyBorder(theme.spacing().inlineGap(), 0, Math.max(theme.spacing().inlineGap(), theme.spacing().buttonInsetY()), 0));
        panneauActions.add(Box.createHorizontalGlue());
        panneauActions.add(boutonFermerPopup);

        panneauFond.add(panneauTexte, BorderLayout.CENTER);
        panneauFond.add(panneauActions, BorderLayout.SOUTH);

        dialogue.setContentPane(panneauFond);
        dialogue.setMinimumSize(new Dimension(780, 460));
        dialogue.pack();
        dialogue.setSize(Math.max(860, dialogue.getWidth()), Math.max(500, dialogue.getHeight()));
        dialogue.setLocationRelativeTo(this);
        dialogue.getRootPane().setDefaultButton(boutonFermerPopup);
        dialogue.setVisible(true);
        dialogue.dispose();
    }

    private String echapperHtml(String texte) {
        if (texte == null || texte.isEmpty()) {
            return "";
        }
        return texte.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    private void initialiserOrdreFocus() {
        List<Component> order = List.of(boutonConfiguration, boutonAide, checkboxChangementPrenoms, checkboxPronomNeutre, champPrenomsEtatCivil, champPrenomsUsage, champNomFamille, comboJourNaissance, comboMoisNaissance, comboAnneeNaissance, champLieuNaissance, comboSexeEtatCivil, champNationalite, champAdresse, champProfession, comboSituationMatrimoniale, comboSituationEnfants, checkboxPacs, champVilleActuelle, zoneTribunal, zoneRecit, boutonPieces, boutonEffacer, boutonAutresDocuments, boutonGenerer);
        setFocusTraversalPolicyProvider(true);
        setFocusTraversalPolicy(new PolitiqueFocusOrdonnee(order));
    }

    private void initialiserValidationProgressive() {
        lierValidationTexte(champPrenomsEtatCivil, ChampDossier.PRENOMS_ETAT_CIVIL);
        lierValidationTexte(champPrenomsUsage, ChampDossier.PRENOMS_USAGE);
        lierValidationTexte(champNomFamille, ChampDossier.NOM_FAMILLE);
        lierValidationTexte(champLieuNaissance, ChampDossier.LIEU_NAISSANCE);
        lierValidationTexte(champAdresse, ChampDossier.ADRESSE);
        lierValidationTexte(champVilleActuelle, ChampDossier.VILLE_ACTUELLE);
        lierValidationTexte(zoneTribunal, ChampDossier.TRIBUNAL);
        lierValidationTexte(zoneRecit, ChampDossier.RECIT);
        lierValidationTexte(champNationalite, ChampDossier.NATIONALITE);
        lierValidationTexte(champProfession, ChampDossier.PROFESSION);

        lierValidationCombo(comboSexeEtatCivil, ChampDossier.SEXE_ETAT_CIVIL);
        lierValidationCombo(comboSituationMatrimoniale, ChampDossier.SITUATION_MATRIMONIALE);
        lierValidationCombo(comboSituationEnfants, ChampDossier.SITUATION_ENFANTS);

        comboJourNaissance.addActionListener(e -> validerChampEnLigne(ChampDossier.DATE_NAISSANCE));
        comboMoisNaissance.addActionListener(e -> validerChampEnLigne(ChampDossier.DATE_NAISSANCE));
        comboAnneeNaissance.addActionListener(e -> validerChampEnLigne(ChampDossier.DATE_NAISSANCE));

        installerFeedbackFocus(champPrenomsEtatCivil);
        installerFeedbackFocus(champPrenomsUsage);
        installerFeedbackFocus(champNomFamille);
        installerFeedbackFocus(panneauDateNaissance);
        installerFeedbackFocus(champLieuNaissance);
        installerFeedbackFocus(champAdresse);
        installerFeedbackFocus(champVilleActuelle);
        installerFeedbackFocus(champNationalite);
        installerFeedbackFocus(champProfession);
        installerFeedbackFocus(zoneTribunal);
        installerFeedbackFocus(zoneRecit);
        installerFeedbackFocus(comboSexeEtatCivil);
        installerFeedbackFocus(comboSituationMatrimoniale);
        installerFeedbackFocus(comboSituationEnfants);
        installerFeedbackFocus(comboJourNaissance);
        installerFeedbackFocus(comboMoisNaissance);
        installerFeedbackFocus(comboAnneeNaissance);
    }

    private void installerFeedbackFocus(JComponent component) {
        if (component == null) {
            return;
        }
        if (component instanceof JComboBox<?>) {
            return;
        }
        component.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (composantsInvalides.contains(component)) {
                    return;
                }
                Border base = borduresNormales.get(component);
                if (base != null) {
                    component.setBorder(BorderFactory.createCompoundBorder(new LineBorder(theme.palette().focus(), 2, true), new EmptyBorder(1, 1, 1, 1)));
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (composantsInvalides.contains(component)) {
                    return;
                }
                Border base = borduresNormales.get(component);
                if (base != null) {
                    component.setBorder(base);
                }
            }
        });
    }

    private void lierValidationTexte(JTextComponent component, ChampDossier champ) {
        component.getDocument().addDocumentListener(new EcouteurDocumentSimple(() -> validerChampEnLigne(champ)));
        component.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                validerChampEnLigne(champ);
            }
        });
    }

    private void lierValidationCombo(JComboBox<String> comboBox, ChampDossier champ) {
        comboBox.addActionListener(e -> validerChampEnLigne(champ));
        comboBox.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                validerChampEnLigne(champ);
            }
        });
    }

    private void initialiserSuiviModification() {
        List<JTextComponent> textInputs = List.of(champPrenomsEtatCivil, champPrenomsUsage, champNomFamille, champLieuNaissance, champAdresse, zoneTribunal, zoneRecit, champVilleActuelle, champNationalite, champProfession);
        for (JTextComponent component : textInputs) {
            component.getDocument().addDocumentListener(new EcouteurDocumentSimple(this::marquerModifie));
        }

        List<JComboBox<String>> combos = List.of(comboJourNaissance, comboMoisNaissance, comboAnneeNaissance, comboSexeEtatCivil, comboSituationMatrimoniale, comboSituationEnfants);
        for (JComboBox<String> combo : combos) {
            combo.addActionListener(e -> marquerModifie());
        }

        List<JCheckBox> checks = List.of(checkboxChangementPrenoms, checkboxPronomNeutre, checkboxPacs, checkboxEffacerApresExport);
        for (JCheckBox check : checks) {
            check.addActionListener(e -> marquerModifie());
        }
    }

    private void initialiserProtectionFermeture() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (tacheActive != null) {
                    afficherMessage("Une opération est en cours. Annulez-la ou attendez la fin avant de fermer.", "Opération en cours", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (!modifie || !aDesDonneesSaisies()) {
                    fermerApplication();
                    return;
                }

                if (!confirmerQuitterAvecDonnees || confirmerQuitter()) {
                    fermerApplication();
                }
            }
        });
    }

    private void fermerApplication() {
        dispose();
        for (Window window : getWindows()) {
            if (window.isDisplayable()) {
                return;
            }
        }
        if (!Boolean.getBoolean(CLE_DESACTIVER_SYSTEM_EXIT)) {
            System.exit(0);
        }
    }

    private void initialiserAccessibilite() {
        getAccessibleContext().setAccessibleName("CECDoc");
        getAccessibleContext().setAccessibleDescription("Fenêtre principale de préparation d'un dossier de changement à l'état civil");

        definirAccessibiliteComposant(checkboxChangementPrenoms, "Option de changement de prénoms", "Active ou désactive la partie changement de prénoms");
        definirAccessibiliteComposant(checkboxPronomNeutre, "Option de pronom neutre", "Utilise le pronom iel dans le document généré");
        definirAccessibiliteComposant(checkboxPacs, "Option PACS", "Indique si un pacte civil de solidarité a été contracté");
        definirAccessibiliteComposant(checkboxEffacerApresExport, "Option d'effacement automatique", "Efface les données du formulaire après génération");

        definirAccessibiliteComposant(champPrenomsEtatCivil, "Prénoms à l'état civil", "Saisir les prénoms inscrits à l'état civil");
        definirAccessibiliteComposant(champPrenomsUsage, "Prénoms d'usage ou demandés", "Saisir les prénoms utilisés ou demandés");
        definirAccessibiliteComposant(champNomFamille, "Nom de famille", "Saisir le nom de famille");
        definirAccessibiliteComposant(champLieuNaissance, "Lieu de naissance", "Saisir le lieu de naissance");
        definirAccessibiliteComposant(champAdresse, "Adresse", "Saisir l'adresse de résidence");
        definirAccessibiliteComposant(champNationalite, "Nationalité", "Saisir la nationalité");
        definirAccessibiliteComposant(champProfession, "Profession", "Saisir la profession ou l'employeur");
        definirAccessibiliteComposant(champVilleActuelle, "Ville actuelle", "Saisir la ville utilisée pour la formule Fait à");
        definirAccessibiliteComposant(zoneTribunal, "Tribunal judiciaire compétent", "Saisir les coordonnées du tribunal");
        definirAccessibiliteComposant(zoneRecit, "Récit de vie", "Saisir le récit personnel à intégrer à la requête");

        definirAccessibiliteComposant(panneauDateNaissance, "Date de naissance", "Sélectionner le jour, le mois et l'année de naissance");
        definirAccessibiliteComposant(comboJourNaissance, "Jour de naissance", "Choisir le jour de naissance");
        definirAccessibiliteComposant(comboMoisNaissance, "Mois de naissance", "Choisir le mois de naissance");
        definirAccessibiliteComposant(comboAnneeNaissance, "Année de naissance", "Choisir l'année de naissance");
        definirAccessibiliteComposant(comboSexeEtatCivil, "Sexe à l'état civil", "Choisir le sexe actuellement inscrit à l'état civil");
        definirAccessibiliteComposant(comboSituationMatrimoniale, "Statut matrimonial", "Choisir la situation matrimoniale");
        definirAccessibiliteComposant(comboSituationEnfants, "Situation parentale", "Choisir la situation parentale");

        definirAccessibiliteComposant(scrollTribunal, "Zone de saisie du tribunal", "Contient le champ de saisie du tribunal compétent");
        definirAccessibiliteComposant(scrollRecit, "Zone de saisie du récit", "Contient le champ de saisie du récit de vie");

        definirAccessibiliteComposant(boutonGenerer, "Générer le document", "Lance la génération de la requête et, si applicable, du PDF");
        definirAccessibiliteComposant(boutonEffacer, "Effacer les données", "Efface les données actuellement saisies");
        definirAccessibiliteComposant(boutonAutresDocuments, "Autres documents", "Ouvre les formulaires de génération de lettres complémentaires");
        definirAccessibiliteComposant(boutonPieces, "Pièces justificatives", "Ouvre la gestion des pièces justificatives et des fichiers attachés");
        definirAccessibiliteComposant(boutonConfiguration, "Configuration de l'application", "Ouvre les paramètres de l'application");
        definirAccessibiliteComposant(boutonAide, "Aide", "Ouvre la fenêtre d'aide");

        comboJourNaissance.setToolTipText("Jour de naissance");
        comboMoisNaissance.setToolTipText("Mois de naissance");
        comboAnneeNaissance.setToolTipText("Année de naissance");
        comboSexeEtatCivil.setToolTipText("Sexe à l'état civil");
        comboSituationMatrimoniale.setToolTipText("Statut matrimonial");
        comboSituationEnfants.setToolTipText("Situation parentale");
        boutonPieces.setToolTipText("Pièces justificatives (Ctrl/Cmd+J)");
        boutonEffacer.setToolTipText("Effacer les données du formulaire");
        boutonAutresDocuments.setToolTipText("Générer d'autres modèles de lettres");
        boutonGenerer.setToolTipText("Générer le document (Ctrl/Cmd+Entrée)");

        labelMessageFormulaire.getAccessibleContext().setAccessibleName("Messages de validation du formulaire");
        labelMessageFormulaire.getAccessibleContext().setAccessibleDescription("Affiche les erreurs ou les informations liées au formulaire");
        surcoucheOccupation.getAccessibleContext().setAccessibleName("Indicateur d'opération en cours");
        surcoucheOccupation.getAccessibleContext().setAccessibleDescription("Indique une opération longue et propose une annulation si disponible");
    }

    private void definirAccessibiliteComposant(JComponent composant, String nom, String description) {
        if (composant == null) {
            return;
        }
        if (nom != null && !nom.isBlank()) {
            composant.getAccessibleContext().setAccessibleName(nom);
        }
        if (description != null && !description.isBlank()) {
            composant.getAccessibleContext().setAccessibleDescription(description);
        }
    }

    private void ouvrirDialogueConfiguration() {
        DialogueConfiguration dialogue = new DialogueConfiguration(this, theme, confirmerQuitterAvecDonnees, memoriserDonneesSaisies, preferenceThemeApplication, dossierSortieParDefaut == null ? "" : dossierSortieParDefaut.toString());
        appliquerIconeDialogue(dialogue);
        dialogue.setVisible(true);
        if (dialogue.estEnregistre()) {
            confirmerQuitterAvecDonnees = dialogue.confirmerQuitterAvecDonnees();
            memoriserDonneesSaisies = dialogue.memoriserDonneesSaisies();
            preferenceThemeApplication = dialogue.preferenceTheme();
            dossierSortieParDefaut = normaliserDossierSortieParDefaut(dialogue.dossierSortieParDefaut());
            appliquerThemeSelonConfiguration();
            planifierEcritureEtat();
            afficherPopupsVisibiliteSelonContexte(ContexteAffichagePopup.THEME_MODIFIE_DANS_CONFIG);
        }
    }

    private void ouvrirDialogueAide() {
        DialogueAide dialogue = new DialogueAide(this, theme);
        appliquerIconeDialogue(dialogue);
        dialogue.setVisible(true);
    }

    private void ouvrirDialoguePieces() {
        nettoyerPiecesPourMode(checkboxChangementPrenoms.isSelected());
        DialoguePiecesJustificatives dialogue = new DialoguePiecesJustificatives(this, new ArrayList<>(piecesActuelles), new LinkedHashMap<>(piecesJointesParIntitule), theme);
        appliquerIconeDialogue(dialogue);
        dialogue.setVisible(true);
        if (dialogue.estEnregistre()) {
            piecesActuelles = dialogue.intitules();
            piecesJointesParIntitule = dialogue.fichiersParIntitule();
            nettoyerPiecesPourMode(checkboxChangementPrenoms.isSelected());
            synchroniserPiecesJointesParIntitule();
            marquerModifie();
        }
    }

    private void ouvrirDialogueAutresDocuments() {
        DialogueAutresDocuments dialogue = new DialogueAutresDocuments(this, theme, iconesApplication);
        appliquerIconeDialogue(dialogue);
        dialogue.setVisible(true);
        if (dialogue.choix() == DialogueAutresDocuments.Choix.LETTRE_UNIVERSITE) {
            ouvrirDialogueLettreUniversite();
            return;
        }
        if (dialogue.choix() == DialogueAutresDocuments.Choix.LETTRE_ADMINISTRATION) {
            ouvrirDialogueLettreAdministration();
        }
    }

    private void ouvrirDialogueLettreUniversite() {
        synchroniserInstantanesAvecFormulairePrincipal();
        DialogueLettreUniversite dialogue = new DialogueLettreUniversite(this, theme, iconesApplication, instantaneLettreUniversite, dossierSortieParDefaut);
        appliquerIconeDialogue(dialogue);
        dialogue.setVisible(true);
        InstantaneLettreUniversite nouvelInstantane = dialogue.instantane();
        if (!Objects.equals(nouvelInstantane, instantaneLettreUniversite)) {
            instantaneLettreUniversite = nouvelInstantane;
            repercuterDonneesDepuisLettreUniversite(nouvelInstantane);
            planifierEcritureEtat();
        }
    }

    private void ouvrirDialogueLettreAdministration() {
        synchroniserInstantanesAvecFormulairePrincipal();
        DialogueLettreAdministration dialogue = new DialogueLettreAdministration(this, theme, iconesApplication, instantaneLettreAdministration, dossierSortieParDefaut);
        appliquerIconeDialogue(dialogue);
        dialogue.setVisible(true);
        InstantaneLettreAdministration nouvelInstantane = dialogue.instantane();
        if (!Objects.equals(nouvelInstantane, instantaneLettreAdministration)) {
            instantaneLettreAdministration = nouvelInstantane;
            repercuterDonneesDepuisLettreAdministration(nouvelInstantane);
            planifierEcritureEtat();
        }
    }

    private void synchroniserInstantanesAvecFormulairePrincipal() {
        String prenomsEtatCivilFormulaire = nettoyerTexte(champPrenomsEtatCivil.getText());
        String prenomsUsageFormulaire = nettoyerTexte(champPrenomsUsage.getText());
        String nomFormulaire = nettoyerTexte(champNomFamille.getText());
        String adresseFormulaire = nettoyerTexte(champAdresse.getText());
        String villeFormulaire = nettoyerTexte(champVilleActuelle.getText());

        String sexeEtatCivilFormulaire = normaliserSexeBinaire(Objects.toString(comboSexeEtatCivil.getSelectedItem(), ""), "Masculin");
        boolean changementSexeEffectif = instantaneLettreAdministration.changementSexe();
        String genreUniversiteSaisi = normaliserGenre(instantaneLettreUniversite.genreActuel(), "");
        boolean pronomNeutreReference = checkboxPronomNeutre.isSelected() || "Non-binaire".equals(genreUniversiteSaisi);

        String sexeEtatCivilReference = sexeEtatCivilFormulaire;
        if (!changementSexeEffectif && ("Masculin".equals(genreUniversiteSaisi) || "Féminin".equals(genreUniversiteSaisi))) {
            sexeEtatCivilReference = sexeOppose(genreUniversiteSaisi);
        }
        if (changementSexeEffectif) {
            sexeEtatCivilReference = normaliserSexeBinaire(instantaneLettreAdministration.sexeAvant(), sexeEtatCivilReference);
        }

        String genreActuelReference;
        if (pronomNeutreReference) {
            genreActuelReference = "Non-binaire";
        } else if (changementSexeEffectif) {
            genreActuelReference = normaliserSexeBinaire(instantaneLettreAdministration.sexeApres(), sexeOppose(sexeEtatCivilReference));
        } else if ("Masculin".equals(genreUniversiteSaisi) || "Féminin".equals(genreUniversiteSaisi)) {
            genreActuelReference = genreUniversiteSaisi;
        } else {
            genreActuelReference = sexeOppose(sexeEtatCivilReference);
        }

        String sexeAvantReference = changementSexeEffectif
                ? normaliserSexeBinaire(instantaneLettreAdministration.sexeAvant(), sexeEtatCivilReference)
                : sexeEtatCivilReference;
        String sexeApresReference = changementSexeEffectif
                ? normaliserSexeBinaire(instantaneLettreAdministration.sexeApres(), sexeOppose(sexeAvantReference))
                : sexeOppose(sexeEtatCivilReference);

        String prenomsEtatCivilPartages = premiereValeurNonVide(prenomsEtatCivilFormulaire, nettoyerTexte(instantaneLettreAdministration.prenomsEtatCivil()), nettoyerTexte(instantaneLettreUniversite.prenomEtatCivil()));
        String prenomEtatCivilPartage = premierPrenom(prenomsEtatCivilPartages);
        String prenomUsagePartage = premierPrenom(premiereValeurNonVide(prenomsUsageFormulaire, nettoyerTexte(instantaneLettreAdministration.prenomUsage()), nettoyerTexte(instantaneLettreUniversite.prenomUsage()), prenomEtatCivilPartage));

        String nomPartage = premiereValeurNonVide(nettoyerTexte(instantaneLettreUniversite.nom()), nettoyerTexte(instantaneLettreAdministration.nom()), nomFormulaire);
        String adresseMultilignePartage = premiereValeurNonVide(nettoyerTexte(instantaneLettreUniversite.adressePostale()), nettoyerTexte(instantaneLettreAdministration.adressePostale()), adresseFormulaire);
        String villePartage = premiereValeurNonVide(nettoyerTexte(instantaneLettreUniversite.villeActuelle()), nettoyerTexte(instantaneLettreAdministration.villeActuelle()), villeFormulaire);
        String telephonePartage = premiereValeurNonVide(nettoyerTexte(instantaneLettreUniversite.telephonePortable()), nettoyerTexte(instantaneLettreAdministration.telephonePortable()));
        String courrielPartage = premiereValeurNonVide(nettoyerTexte(instantaneLettreUniversite.courriel()), nettoyerTexte(instantaneLettreAdministration.courriel()));
        String prenomsNaissancePartages = checkboxChangementPrenoms.isSelected()
                ? premiereValeurNonVide(prenomsEtatCivilPartages, nettoyerTexte(instantaneLettreAdministration.prenomNaissance()))
                : nettoyerTexte(instantaneLettreAdministration.prenomNaissance());

        instantaneLettreUniversite = new InstantaneLettreUniversite(
                genreActuelReference,
                civiliteDepuisGenre(genreActuelReference),
                premiereValeurNonVide(premierPrenom(instantaneLettreUniversite.prenomUsage()), prenomUsagePartage),
                premiereValeurNonVide(premierPrenom(instantaneLettreUniversite.prenomEtatCivil()), prenomEtatCivilPartage),
                premiereValeurNonVide(nettoyerTexte(instantaneLettreUniversite.nom()), nomPartage),
                premiereValeurNonVide(nettoyerTexte(instantaneLettreUniversite.adressePostale()), adresseMultilignePartage),
                premiereValeurNonVide(nettoyerTexte(instantaneLettreUniversite.telephonePortable()), telephonePartage),
                premiereValeurNonVide(nettoyerTexte(instantaneLettreUniversite.courriel()), courrielPartage),
                instantaneLettreUniversite.ine(),
                instantaneLettreUniversite.nomUniversite(),
                instantaneLettreUniversite.explicationParcours(),
                premiereValeurNonVide(nettoyerTexte(instantaneLettreUniversite.villeActuelle()), villePartage)
        );

        instantaneLettreAdministration = new InstantaneLettreAdministration(
                premiereValeurNonVide(premierPrenom(instantaneLettreAdministration.prenomUsage()), prenomUsagePartage),
                premiereValeurNonVide(nettoyerTexte(instantaneLettreAdministration.prenomsEtatCivil()), prenomsEtatCivilPartages),
                premiereValeurNonVide(nettoyerTexte(instantaneLettreAdministration.nom()), nomPartage),
                premiereValeurNonVide(nettoyerTexte(instantaneLettreAdministration.adressePostale()), adresseMultilignePartage),
                premiereValeurNonVide(nettoyerTexte(instantaneLettreAdministration.telephonePortable()), telephonePartage),
                premiereValeurNonVide(nettoyerTexte(instantaneLettreAdministration.courriel()), courrielPartage),
                instantaneLettreAdministration.adresseDestinataire(),
                instantaneLettreAdministration.changementPrenom(),
                prenomsNaissancePartages,
                changementSexeEffectif,
                sexeAvantReference,
                sexeApresReference,
                instantaneLettreAdministration.changementPrenomFaitEnMairie(),
                instantaneLettreAdministration.numeroDecisionMairie(),
                instantaneLettreAdministration.dateDecisionMairie(),
                instantaneLettreAdministration.tribunalCompetent(),
                instantaneLettreAdministration.numeroJugement(),
                premiereValeurNonVide(nettoyerTexte(instantaneLettreAdministration.villeActuelle()), villePartage)
        );

        appliquerChampsPartagesAuFormulairePrincipal(
                premiereValeurNonVide(prenomsEtatCivilFormulaire, prenomsEtatCivilPartages, prenomEtatCivilPartage),
                premiereValeurNonVide(prenomsUsageFormulaire, prenomUsagePartage),
                nomPartage,
                adresseMultilignePartage,
                villePartage
        );
        appliquerContexteSexeEtGenreAuFormulairePrincipal(sexeEtatCivilReference, genreActuelReference, changementSexeEffectif);
    }

    private void repercuterDonneesDepuisLettreUniversite(InstantaneLettreUniversite instantane) {
        String prenomUsage = premierPrenom(instantane.prenomUsage());
        String prenomEtatCivil = premierPrenom(instantane.prenomEtatCivil());
        String nom = nettoyerTexte(instantane.nom());
        String adresseMultiligne = nettoyerTexte(instantane.adressePostale());
        String ville = nettoyerTexte(instantane.villeActuelle());

        instantaneLettreAdministration = new InstantaneLettreAdministration(premiereValeurNonVide(prenomUsage, instantaneLettreAdministration.prenomUsage()), premiereValeurNonVide(instantaneLettreAdministration.prenomsEtatCivil(), prenomEtatCivil), premiereValeurNonVide(nom, instantaneLettreAdministration.nom()), premiereValeurNonVide(instantane.adressePostale(), instantaneLettreAdministration.adressePostale()), premiereValeurNonVide(instantane.telephonePortable(), instantaneLettreAdministration.telephonePortable()), premiereValeurNonVide(instantane.courriel(), instantaneLettreAdministration.courriel()), instantaneLettreAdministration.adresseDestinataire(), instantaneLettreAdministration.changementPrenom(), nettoyerTexte(instantaneLettreAdministration.prenomNaissance()), instantaneLettreAdministration.changementSexe(), instantaneLettreAdministration.sexeAvant(), instantaneLettreAdministration.sexeApres(), instantaneLettreAdministration.changementPrenomFaitEnMairie(), instantaneLettreAdministration.numeroDecisionMairie(), instantaneLettreAdministration.dateDecisionMairie(), instantaneLettreAdministration.tribunalCompetent(), instantaneLettreAdministration.numeroJugement(), premiereValeurNonVide(ville, instantaneLettreAdministration.villeActuelle()));

        appliquerChampsPartagesAuFormulairePrincipal(premiereValeurNonVide(nettoyerTexte(instantaneLettreAdministration.prenomsEtatCivil()), prenomEtatCivil), prenomUsage, nom, adresseMultiligne, ville);
        synchroniserInstantanesAvecFormulairePrincipal();
    }

    private void repercuterDonneesDepuisLettreAdministration(InstantaneLettreAdministration instantane) {
        String prenomUsage = premierPrenom(instantane.prenomUsage());
        String prenomEtatCivil = premierPrenom(instantane.prenomsEtatCivil());
        String nom = nettoyerTexte(instantane.nom());
        String adresseMultiligne = nettoyerTexte(instantane.adressePostale());
        String ville = nettoyerTexte(instantane.villeActuelle());

        instantaneLettreUniversite = new InstantaneLettreUniversite(instantaneLettreUniversite.genreActuel(), instantaneLettreUniversite.civiliteSouhaitee(), premiereValeurNonVide(prenomUsage, instantaneLettreUniversite.prenomUsage()), premiereValeurNonVide(prenomEtatCivil, instantaneLettreUniversite.prenomEtatCivil()), premiereValeurNonVide(nom, instantaneLettreUniversite.nom()), premiereValeurNonVide(instantane.adressePostale(), instantaneLettreUniversite.adressePostale()), premiereValeurNonVide(instantane.telephonePortable(), instantaneLettreUniversite.telephonePortable()), premiereValeurNonVide(instantane.courriel(), instantaneLettreUniversite.courriel()), instantaneLettreUniversite.ine(), instantaneLettreUniversite.nomUniversite(), instantaneLettreUniversite.explicationParcours(), premiereValeurNonVide(ville, instantaneLettreUniversite.villeActuelle()));

        appliquerChampsPartagesAuFormulairePrincipal(premiereValeurNonVide(instantane.prenomsEtatCivil(), prenomEtatCivil), prenomUsage, nom, adresseMultiligne, ville);
        synchroniserInstantanesAvecFormulairePrincipal();
    }

    private void appliquerChampsPartagesAuFormulairePrincipal(String prenomsEtatCivil, String prenomsUsage, String nom, String adresseMultiligne, String ville) {
        boolean suspensionPrecedente = suspendreSuiviModification;
        suspendreSuiviModification = true;
        try {
            if (!nettoyerTexte(prenomsEtatCivil).isBlank()) {
                champPrenomsEtatCivil.setText(nettoyerTexte(prenomsEtatCivil));
            }
            if (!nettoyerTexte(prenomsUsage).isBlank()) {
                champPrenomsUsage.setText(nettoyerTexte(prenomsUsage));
            }
            if (!nettoyerTexte(nom).isBlank()) {
                champNomFamille.setText(nettoyerTexte(nom));
            }
            if (!nettoyerTexte(adresseMultiligne).isBlank()) {
                champAdresse.setText(nettoyerTexte(adresseMultiligne));
            }
            if (!nettoyerTexte(ville).isBlank()) {
                champVilleActuelle.setText(nettoyerTexte(ville));
            }
        } finally {
            suspendreSuiviModification = suspensionPrecedente;
        }
        mettreAJourExemplesPrenoms();
    }

    private void appliquerContexteSexeEtGenreAuFormulairePrincipal(String sexeEtatCivil, String genreActuel, boolean changementSexeEffectif) {
        String sexeReference = normaliserSexeBinaire(sexeEtatCivil, "Masculin");
        String genreReference = normaliserGenre(genreActuel, sexeOppose(sexeReference));
        boolean pronomNeutre = "Non-binaire".equals(genreReference);

        if (checkboxPronomNeutre.isSelected() != pronomNeutre) {
            checkboxPronomNeutre.setSelected(pronomNeutre);
        }
        if (!changementSexeEffectif) {
            definirSelectionCombo(comboSexeEtatCivil, sexeReference, "Masculin");
        }
    }

    private String nettoyerTexte(String valeur) {
        return NormalisationTexte.normaliserTexte(valeur);
    }

    private String premierPrenom(String prenoms) {
        return NormalisationTexte.extrairePremierPrenom(prenoms);
    }

    private String normaliserSexeBinaire(String valeur, String valeurParDefaut) {
        String texte = nettoyerTexte(valeur).toLowerCase(Locale.ROOT);
        if (texte.startsWith("f")) {
            return "Féminin";
        }
        if (texte.startsWith("m")) {
            return "Masculin";
        }
        return "Féminin".equals(valeurParDefaut) ? "Féminin" : "Masculin";
    }

    private String normaliserGenre(String valeur, String valeurParDefaut) {
        String texte = nettoyerTexte(valeur).toLowerCase(Locale.ROOT);
        if (texte.startsWith("n")) {
            return "Non-binaire";
        }
        if (texte.startsWith("f")) {
            return "Féminin";
        }
        if (texte.startsWith("m")) {
            return "Masculin";
        }
        if ("Non-binaire".equals(valeurParDefaut) || "Féminin".equals(valeurParDefaut) || "Masculin".equals(valeurParDefaut)) {
            return valeurParDefaut;
        }
        return "Masculin";
    }

    private String sexeOppose(String sexe) {
        return "Féminin".equals(normaliserSexeBinaire(sexe, "Masculin")) ? "Masculin" : "Féminin";
    }

    private String civiliteDepuisGenre(String genre) {
        return switch (normaliserGenre(genre, "Masculin")) {
            case "Féminin" -> "Madame";
            case "Non-binaire" -> "Mx";
            default -> "Monsieur";
        };
    }

    private String premiereValeurNonVide(String... valeurs) {
        if (valeurs == null) {
            return "";
        }
        for (String valeur : valeurs) {
            String texte = nettoyerTexte(valeur);
            if (!texte.isBlank()) {
                return texte;
            }
        }
        return "";
    }

    private void validerChampEnLigne(ChampDossier champ) {
        if (champ == ChampDossier.PRENOMS_USAGE && !checkboxChangementPrenoms.isSelected()) {
            effacerErreurChamp(champ);
            return;
        }
        ProblemeValidation probleme = serviceApplication.validerChamp(construireInstantane(), champ);
        appliquerProblemeValidation(champ, probleme);
    }

    private void appliquerProblemeValidation(ChampDossier champ, ProblemeValidation probleme) {
        JComponent composant = composantsParChamp.get(champ);
        if (composant == null) {
            return;
        }
        if (probleme == null) {
            effacerErreurComposant(composant);
            return;
        }
        marquerComposantInvalide(composant, probleme.message());
    }

    private InstantaneDossier construireInstantane() {
        nettoyerPiecesPourMode(checkboxChangementPrenoms.isSelected());

        synchroniserPiecesJointesParIntitule();

        return new InstantaneDossier(checkboxChangementPrenoms.isSelected(), checkboxPronomNeutre.isSelected(), champPrenomsEtatCivil.getText(), champPrenomsUsage.getText(), champNomFamille.getText(), construireTexteDateNaissance(), champLieuNaissance.getText(), Objects.toString(comboSexeEtatCivil.getSelectedItem(), ""), champAdresse.getText(), zoneTribunal.getText(), zoneRecit.getText(), champVilleActuelle.getText(), new ArrayList<>(piecesActuelles), champNationalite.getText(), champProfession.getText(), Objects.toString(comboSituationMatrimoniale.getSelectedItem(), ""), Objects.toString(comboSituationEnfants.getSelectedItem(), ""), checkboxPacs.isSelected(), construirePiecesDetaillees());
    }

    private boolean validerAvantGeneration() {
        effacerErreursValidation();

        ResultatValidation choix = serviceApplication.validerAvantGeneration(construireInstantane());
        if (choix.estValide()) {
            return true;
        }

        Component premierInvalide = null;
        for (Map.Entry<ChampDossier, String> entree : choix.messagesChamps().entrySet()) {
            JComponent composant = composantsParChamp.get(entree.getKey());
            if (composant == null) {
                continue;
            }
            if (premierInvalide == null) {
                premierInvalide = composant;
            }
            marquerComposantInvalide(composant, entree.getValue());
        }

        labelMessageFormulaire.setText(choix.messageGlobal());
        if (premierInvalide != null) {
            donnerFocusComposant(premierInvalide);
        }
        return false;
    }

    private void genererDocument() {
        if (tacheActive != null) {
            return;
        }
        if (!validerAvantGeneration()) {
            return;
        }

        DonneesDossier donneesDossier = serviceApplication.construireDonneesDossier(construireInstantane());

        File fichierSortieRequeteDocx = choisirDestinationRequeteDocx();
        if (fichierSortieRequeteDocx == null) {
            return;
        }

        File fichierSortiePdf = null;
        if (aDesPiecesJointesExportables(donneesDossier) && demanderEnregistrementPdf()) {
            fichierSortiePdf = choisirDestinationDossierPdf(fichierSortieRequeteDocx.toPath());
            if (fichierSortiePdf == null) {
                return;
            }
        }

        File fichierSortieLettreGreffiere = null;
        if (demanderGenerationLettreGreffiere()) {
            fichierSortieLettreGreffiere = choisirDestinationLettreGreffiere(fichierSortieRequeteDocx.toPath());
            if (fichierSortieLettreGreffiere == null) {
                return;
            }
        }

        if (!confirmerEcrasementSorties(fichierSortieRequeteDocx, fichierSortiePdf, fichierSortieLettreGreffiere)) {
            return;
        }

        boolean ecrasement = fichierSortieRequeteDocx.exists() || (fichierSortiePdf != null && fichierSortiePdf.exists()) || (fichierSortieLettreGreffiere != null && fichierSortieLettreGreffiere.exists());
        File fichierSortieRequeteDocxFinal = fichierSortieRequeteDocx;
        File fichierSortiePdfFinal = fichierSortiePdf;
        File fichierSortieLettreGreffiereFinal = fichierSortieLettreGreffiere;
        boolean ecrasementFinal = ecrasement;

        SwingWorker<Void, EtapeProgressionGeneration> tacheGeneration = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                if (isCancelled()) {
                    return null;
                }

                publierEtape(5, "Préparation de la génération...");
                Path cheminLettreGreffiere = fichierSortieLettreGreffiereFinal == null ? null : fichierSortieLettreGreffiereFinal.toPath();
                Path cheminPdf = fichierSortiePdfFinal == null ? null : fichierSortiePdfFinal.toPath();
                boolean generationPdfDemandee = cheminPdf != null;

                Path cheminEntetePdf = null;
                Path cheminTemporaireLettreGreffiere = null;
                try {
                    if (generationPdfDemandee) {
                        publierEtape(20, "Préparation de la lettre greffier·e pour le PDF...");
                        if (cheminLettreGreffiere != null) {
                            serviceGenerationLettreGreffiere.exporter(donneesDossier, cheminLettreGreffiere, ecrasementFinal);
                            cheminEntetePdf = cheminLettreGreffiere;
                        } else {
                            cheminTemporaireLettreGreffiere = Files.createTempFile("cecdoc-lettre-greffiere-pdf-", ".docx");
                            serviceGenerationLettreGreffiere.exporter(donneesDossier, cheminTemporaireLettreGreffiere, true);
                            cheminEntetePdf = cheminTemporaireLettreGreffiere;
                        }
                    }

                    publierEtape(generationPdfDemandee ? 55 : 70, generationPdfDemandee ? "Génération de la requête et du PDF..." : "Génération de la requête...");
                    serviceApplication.exporterDocument(donneesDossier, fichierSortieRequeteDocxFinal.toPath(), ecrasementFinal, cheminPdf, cheminEntetePdf);

                    if (!generationPdfDemandee && cheminLettreGreffiere != null) {
                        publierEtape(90, "Génération de la lettre greffier·e...");
                        serviceGenerationLettreGreffiere.exporter(donneesDossier, cheminLettreGreffiere, ecrasementFinal);
                    }
                } finally {
                    if (cheminTemporaireLettreGreffiere != null) {
                        Files.deleteIfExists(cheminTemporaireLettreGreffiere);
                    }
                }
                publierEtape(100, "Finalisation...");
                return null;
            }

            @Override
            protected void process(List<EtapeProgressionGeneration> etapes) {
                if (etapes == null || etapes.isEmpty()) {
                    return;
                }
                EtapeProgressionGeneration etape = etapes.get(etapes.size() - 1);
                surcoucheOccupation.mettreAJourProgression(etape.progression(), etape.message());
            }

            @Override
            protected void done() {
                terminerTache();
                if (isCancelled()) {
                    afficherMessage("La génération a été annulée.", "Génération annulée", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                try {
                    get();
                    StringBuilder messageSucces = new StringBuilder("La requête a été générée avec succès.\nRequête : ").append(fichierSortieRequeteDocxFinal.getAbsolutePath());
                    if (fichierSortiePdfFinal != null) {
                        messageSucces.append("\nPDF : ").append(fichierSortiePdfFinal.getAbsolutePath());
                    }
                    if (fichierSortieLettreGreffiereFinal != null) {
                        messageSucces.append("\nLettre greffier·e : ").append(fichierSortieLettreGreffiereFinal.getAbsolutePath());
                    }
                    afficherMessage(messageSucces.toString(), "Succès", JOptionPane.INFORMATION_MESSAGE);
                    modifie = false;
                    if (checkboxEffacerApresExport.isSelected()) {
                        effacerFormulaire(true);
                        effacerEtatPersistant();
                    }
                } catch (CancellationException ex) {
                    afficherMessage("La génération a été annulée.", "Génération annulée", JOptionPane.INFORMATION_MESSAGE);
                } catch (ExecutionException ex) {
                    Throwable cause = ex.getCause();
                    if (cause instanceof ErreurExportDocument exportException) {
                        afficherMessage(exportException.getUserMessage(), "Échec de génération", JOptionPane.ERROR_MESSAGE);
                    } else {
                        afficherMessage("Une erreur est survenue pendant la génération du document.", "Échec de génération", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    afficherMessage("La génération a été interrompue.", "Génération interrompue", JOptionPane.ERROR_MESSAGE);
                }
            }

            private void publierEtape(int progression, String message) {
                publish(new EtapeProgressionGeneration(progression, message));
            }
        };

        demarrerTache(tacheGeneration, "Génération en cours...", true);
    }

    private File choisirDestinationRequeteDocx() {
        JFileChooser selecteurFichier = new JFileChooser();
        MemoireRepertoireExplorateur.appliquerAuSelecteur(selecteurFichier, dossierSortieParDefaut);
        selecteurFichier.setDialogTitle("Enregistrer la requête");
        selecteurFichier.setSelectedFile(fichierParDefautDansDossierSortie("requete_changement_sexe.docx"));
        selecteurFichier.setAcceptAllFileFilterUsed(false);
        selecteurFichier.setFileFilter(new FileNameExtensionFilter("Requête (*.docx)", "docx"));
        int choix = selecteurFichier.showSaveDialog(this);
        if (choix != JFileChooser.APPROVE_OPTION) {
            return null;
        }
        MemoireRepertoireExplorateur.memoriserDepuisSelection(selecteurFichier);
        return garantirExtension(selecteurFichier.getSelectedFile(), "docx");
    }

    private File choisirDestinationDossierPdf(Path cheminRequeteDocx) {
        JFileChooser selecteurFichier = new JFileChooser();
        MemoireRepertoireExplorateur.appliquerAuSelecteur(selecteurFichier, dossierSortieParDefaut);
        selecteurFichier.setDialogTitle("Enregistrer le dossier PDF");
        selecteurFichier.setSelectedFile(ServicePdfDossierComplet.cheminPdfDossier(cheminRequeteDocx).toFile());
        selecteurFichier.setAcceptAllFileFilterUsed(false);
        selecteurFichier.setFileFilter(new FileNameExtensionFilter("Document PDF (*.pdf)", "pdf"));
        int choix = selecteurFichier.showSaveDialog(this);
        if (choix != JFileChooser.APPROVE_OPTION) {
            return null;
        }
        MemoireRepertoireExplorateur.memoriserDepuisSelection(selecteurFichier);
        return garantirExtension(selecteurFichier.getSelectedFile(), "pdf");
    }

    private boolean demanderEnregistrementPdf() {
        String message = "Des pièces justificatives sont attachées.\nVoulez-vous enregistrer aussi le dossier PDF complet ?";
        int choix = afficherConfirmation(message, "Enregistrement du PDF", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return choix == JOptionPane.YES_OPTION;
    }

    private boolean demanderGenerationLettreGreffiere() {
        String message = "Générer également la lettre pour le/la greffier·e en chef du Tribunal ?";
        int choix = afficherConfirmation(message, "Lettre greffier·e", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return choix == JOptionPane.YES_OPTION;
    }

    private File choisirDestinationLettreGreffiere(Path cheminRequeteDocx) {
        JFileChooser selecteurFichier = new JFileChooser();
        MemoireRepertoireExplorateur.appliquerAuSelecteur(selecteurFichier, dossierSortieParDefaut);
        selecteurFichier.setDialogTitle("Enregistrer la lettre greffier·e");
        selecteurFichier.setSelectedFile(ServiceGenerationLettreGreffiere.cheminParDefaut(cheminRequeteDocx).toFile());
        selecteurFichier.setAcceptAllFileFilterUsed(false);
        selecteurFichier.setFileFilter(new FileNameExtensionFilter("Document Word (*.docx)", "docx"));
        int choix = selecteurFichier.showSaveDialog(this);
        if (choix != JFileChooser.APPROVE_OPTION) {
            return null;
        }
        MemoireRepertoireExplorateur.memoriserDepuisSelection(selecteurFichier);
        return garantirExtension(selecteurFichier.getSelectedFile(), ServiceGenerationLettreGreffiere.extensionParDefaut());
    }

    private boolean confirmerEcrasementSorties(File fichierSortieRequeteDocx, File fichierSortiePdf, File fichierSortieLettreGreffiere) {
        List<String> sortiesExistantes = new ArrayList<>(3);
        if (fichierSortieRequeteDocx != null && fichierSortieRequeteDocx.exists()) {
            sortiesExistantes.add("Requête");
        }
        if (fichierSortiePdf != null && fichierSortiePdf.exists()) {
            sortiesExistantes.add("PDF");
        }
        if (fichierSortieLettreGreffiere != null && fichierSortieLettreGreffiere.exists()) {
            sortiesExistantes.add("Lettre greffier·e");
        }

        if (sortiesExistantes.isEmpty()) {
            return true;
        }

        String message;
        if (sortiesExistantes.size() == 1) {
            message = "Le fichier " + sortiesExistantes.get(0) + " existe déjà. Voulez-vous le remplacer ?";
        } else {
            message = "Des fichiers de sortie existent déjà (" + String.join(", ", sortiesExistantes) + "). Voulez-vous les remplacer ?";
        }

        int choix = afficherConfirmation(message, "Confirmer l'écrasement", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        return choix == JOptionPane.YES_OPTION;
    }

    private boolean aDesPiecesJointesExportables(DonneesDossier donneesDossier) {
        if (donneesDossier == null || donneesDossier.piecesJustificativesDetaillees().isEmpty()) {
            return false;
        }

        for (PieceJustificative piece : donneesDossier.piecesJustificativesDetaillees()) {
            if (piece == null || piece.piecesJointes().isEmpty()) {
                continue;
            }
            for (PieceJointe pieceJointe : piece.piecesJointes()) {
                if (pieceJointe == null) {
                    continue;
                }
                TypePieceJointe type = pieceJointe.type();
                if (type == null || type == TypePieceJointe.INCONNU) {
                    type = TypePieceJointe.depuisNomFichier(pieceJointe.nomVisible());
                }
                if (type.estPrisEnCharge()) {
                    return true;
                }
            }
        }

        return false;
    }

    private static File garantirExtension(File fichierSelectionne, String extensionAttendue) {
        if (fichierSelectionne == null) {
            return null;
        }
        String suffixe = "." + extensionAttendue.toLowerCase(Locale.ROOT);
        if (fichierSelectionne.getName().toLowerCase(Locale.ROOT).endsWith(suffixe)) {
            return fichierSelectionne;
        }
        File parent = fichierSelectionne.getParentFile();
        return parent == null ? new File(fichierSelectionne.getName() + suffixe) : new File(parent, fichierSelectionne.getName() + suffixe);
    }

    private File fichierParDefautDansDossierSortie(String nomFichier) {
        if (dossierSortieParDefaut == null || nomFichier == null || nomFichier.isBlank()) {
            return new File(nomFichier == null ? "" : nomFichier);
        }
        return dossierSortieParDefaut.resolve(nomFichier).toFile();
    }

    private Path normaliserDossierSortieParDefaut(String chemin) {
        String valeur = chemin == null ? "" : chemin.trim();
        if (valeur.isEmpty()) {
            return null;
        }
        try {
            return Path.of(valeur).toAbsolutePath().normalize();
        } catch (RuntimeException ex) {
            return null;
        }
    }

    private void demarrerTache(SwingWorker<?, ?> tacheTravail, String message, boolean annulable) {
        if (tacheActive != null) {
            return;
        }
        tacheActive = tacheTravail;
        afficherOccupation(true, message, annulable);
        tacheTravail.execute();
    }

    private void terminerTache() {
        tacheActive = null;
        afficherOccupation(false, "", false);
    }

    private void afficherOccupation(boolean occupe, String message, boolean annulable) {
        setCursor(occupe ? Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR) : Cursor.getDefaultCursor());
        boutonGenerer.setEnabled(!occupe);
        boutonPieces.setEnabled(!occupe);
        boutonEffacer.setEnabled(!occupe);
        boutonAutresDocuments.setEnabled(!occupe);
        boutonAide.setEnabled(!occupe);
        boutonConfiguration.setEnabled(!occupe);
        for (Map.Entry<JButton, RoleBouton> entry : rolesBoutons.entrySet()) {
            mettreAJourVisuelBouton(entry.getKey(), entry.getValue());
        }

        surcoucheOccupation.configure(message, annulable, () -> {
            SwingWorker<?, ?> tacheEnCours = tacheActive;
            if (tacheEnCours != null) {
                tacheEnCours.cancel(true);
            }
        });
        getGlassPane().setVisible(occupe);
        if (occupe) {
            surcoucheOccupation.requestFocusInWindow();
        }
    }


    @Override
    public void dispose() {
        executricePersistanceEtat.shutdown();
        try {
            if (!executricePersistanceEtat.awaitTermination(1, TimeUnit.SECONDS)) {
                executricePersistanceEtat.shutdownNow();
            }
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            executricePersistanceEtat.shutdownNow();
        }
        super.dispose();
    }

    private void confirmerEffacement() {
        if (!aDesDonneesSaisies()) {
            effacerFormulaire(true);
            effacerEtatPersistant();
            return;
        }

        int choix = afficherConfirmation("Effacer toutes les données saisies du formulaire ?", "Confirmer l'effacement", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (choix == JOptionPane.YES_OPTION) {
            effacerFormulaire(true);
            effacerEtatPersistant();
        }
    }

    private void effacerFormulaire(boolean keepAutoClearPreference) {
        suspendreSuiviModification = true;
        try {
            checkboxChangementPrenoms.setSelected(false);
            checkboxPronomNeutre.setSelected(false);
            checkboxPacs.setSelected(false);
            champPrenomsUsage.setEnabled(false);

            champPrenomsEtatCivil.setText("");
            champPrenomsUsage.setText("");
            champNomFamille.setText("");
            champLieuNaissance.setText("");
            champAdresse.setText("");
            zoneTribunal.setText("");
            zoneRecit.setText("");
            champVilleActuelle.setText("");
            champNationalite.setText("");
            champProfession.setText("");

            appliquerDateNaissanceParDefaut();

            comboSexeEtatCivil.setSelectedItem("Masculin");
            comboSituationMatrimoniale.setSelectedItem("");
            comboSituationEnfants.setSelectedItem("");
            mettreAJourExemplesPrenoms();

            piecesActuelles = new ArrayList<>(piecesParDefautSexe);
            piecesJointesParIntitule = new LinkedHashMap<>();
            synchroniserPiecesJointesParIntitule();
            dernierModeChangementPrenoms = false;
            instantaneLettreUniversite = InstantaneLettreUniversite.vide();
            instantaneLettreAdministration = InstantaneLettreAdministration.vide();

            if (!keepAutoClearPreference) {
                checkboxEffacerApresExport.setSelected(false);
            }
            effacerErreursValidation();
            modifie = false;
            appliquerThemeSelonConfiguration();
        } finally {
            suspendreSuiviModification = false;
        }
    }

    private boolean aDesDonneesSaisies() {
        return aDuTexte(champPrenomsEtatCivil.getText()) || aDuTexte(champPrenomsUsage.getText()) || aDuTexte(champNomFamille.getText()) || aDuTexte(champLieuNaissance.getText()) || aDuTexte(champAdresse.getText()) || aDuTexte(zoneTribunal.getText()) || aDuTexte(zoneRecit.getText()) || aDuTexte(champVilleActuelle.getText()) || aDuTexte(champNationalite.getText()) || aDuTexte(champProfession.getText()) || checkboxChangementPrenoms.isSelected() || checkboxPronomNeutre.isSelected() || checkboxPacs.isSelected() || aDesPiecesJointes();
    }

    private boolean aDesPiecesJointes() {
        return piecesJointesParIntitule != null && piecesJointesParIntitule.values().stream().anyMatch(fichiers -> fichiers != null && !fichiers.isEmpty());
    }

    private static boolean aDuTexte(String valeur) {
        return valeur != null && !valeur.trim().isEmpty();
    }

    private void marquerModifie() {
        if (suspendreSuiviModification) {
            return;
        }
        modifie = true;
        planifierEcritureEtat();
    }

    private void marquerComposantInvalide(JComponent component, String message) {
        if (component == null) {
            return;
        }
        JLabel error = labelsErreurs.get(component);
        if (error != null) {
            error.setText("(!) " + message);
            error.setFont(theme.typography().message());
            error.setForeground(theme.palette().error());
        }

        if (!(component instanceof JComboBox<?>) && component != panneauDateNaissance) {
            component.setBorder(new LineBorder(theme.palette().error(), 1, true));
        }
        composantsInvalides.add(component);
    }

    private void effacerErreurChamp(ChampDossier champ) {
        JComponent component = composantsParChamp.get(champ);
        if (component != null) {
            effacerErreurComposant(component);
        }
    }

    private void effacerErreurComposant(JComponent component) {
        JLabel error = labelsErreurs.get(component);
        if (error != null) {
            error.setText(" ");
        }
        Border border = borduresNormales.get(component);
        if (border != null) {
            component.setBorder(border);
        }
        composantsInvalides.remove(component);
    }

    private void effacerErreursValidation() {
        labelMessageFormulaire.setText(" ");
        composantsInvalides.clear();
        for (Map.Entry<JComponent, JLabel> entry : labelsErreurs.entrySet()) {
            entry.getValue().setText(" ");
            entry.getValue().setFont(theme.typography().message());
            entry.getValue().setForeground(theme.palette().error());
            Border border = borduresNormales.get(entry.getKey());
            if (border != null) {
                entry.getKey().setBorder(border);
            }
        }
    }

    private void donnerFocusComposant(Component component) {
        if (component instanceof JScrollPane scrollPane) {
            JViewport viewport = scrollPane.getViewport();
            if (viewport != null && viewport.getView() != null) {
                viewport.getView().requestFocusInWindow();
            }
            return;
        }
        component.requestFocusInWindow();
    }

    private Insets margesLigne() {
        int vertical = Math.max(1, theme.spacing().inlineGap() - (theme.spacing().inlineGap() / 4));
        int horizontal = theme.spacing().inlineGap();
        return new Insets(vertical, horizontal, vertical, horizontal);
    }

    private Insets margesSection() {
        int top = theme.spacing().blockGap() + Math.max(1, theme.spacing().inlineGap() - (theme.spacing().inlineGap() / 4));
        int horizontal = theme.spacing().inlineGap();
        int bottom = theme.spacing().inlineGap();
        return new Insets(top, horizontal, bottom, horizontal);
    }

    private Insets margesErreurLigne() {
        int horizontal = theme.spacing().inlineGap();
        int bottom = Math.max(1, theme.spacing().inlineGap() / 2);
        return new Insets(0, horizontal, bottom, horizontal);
    }

    private enum RoleBouton {
        PRIMARY, SECONDARY, DANGER, ICON
    }

    private enum ContexteAffichagePopup {
        DEMARRAGE, PRONOM_NEUTRE_BASCULE, THEME_MODIFIE_DANS_CONFIG
    }

    private enum FondPopupVisibilite {
        TRANS, NON_BINAIRE, LESBIEN, INTERSEXE, RAINBOW, PANSEXUEL, BISEXUEL
    }

    private enum TypePopupVisibilite {
        TDOV, VISIBILITE_NON_BINAIRE, VISIBILITE_LESBIENNE, VISIBILITE_INTERSEXE, LUTTE_LGBTQIPHOBIES, COMING_OUT_DAY, VISIBILITE_PANSEXUELLE, JOURNEE_BISEXUALITE
    }

    private record ConfigurationPopupVisibilite(String titreFenetre, String titreAffiche, String message,
                                                FondPopupVisibilite fond, Color couleurTitre, Color couleurMessage,
                                                String libelleBouton) {
    }

    private record EtapeProgressionGeneration(int progression, String message) {
    }

    private final class EcouteurChangementBoutonTheme implements javax.swing.event.ChangeListener {
        private final RoleBouton role;

        private EcouteurChangementBoutonTheme(RoleBouton role) {
            this.role = role;
        }

        @Override
        public void stateChanged(javax.swing.event.ChangeEvent e) {
            if (e.getSource() instanceof JButton button) {
                mettreAJourVisuelBouton(button, role);
            }
        }
    }

    private final class EcouteurFocusBoutonTheme extends FocusAdapter {
        private final RoleBouton role;

        private EcouteurFocusBoutonTheme(RoleBouton role) {
            this.role = role;
        }

        @Override
        public void focusGained(FocusEvent e) {
            if (e.getComponent() instanceof JButton button) {
                mettreAJourVisuelBouton(button, role);
            }
        }

        @Override
        public void focusLost(FocusEvent e) {
            if (e.getComponent() instanceof JButton button) {
                mettreAJourVisuelBouton(button, role);
            }
        }
    }

    private record EcouteurDocumentSimple(Runnable rappel) implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            rappel.run();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            rappel.run();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            rappel.run();
        }
    }

    private final class SurcoucheOccupation extends JPanel {
        @Serial
        private static final long serialVersionUID = 1L;
        private final JLabel labelMessage;
        private final JProgressBar barreProgression;
        private final JButton boutonAnnuler;

        private SurcoucheOccupation() {
            setOpaque(false);
            setFocusCycleRoot(true);
            setFocusable(true);
            setLayout(new GridBagLayout());

            JPanel panneauContenu = new JPanel();
            panneauContenu.setOpaque(true);
            panneauContenu.setLayout(new BoxLayout(panneauContenu, BoxLayout.Y_AXIS));

            labelMessage = new JLabel("Opération en cours...");
            labelMessage.setAlignmentX(CENTER_ALIGNMENT);

            barreProgression = new JProgressBar();
            barreProgression.setIndeterminate(true);
            barreProgression.setAlignmentX(CENTER_ALIGNMENT);
            barreProgression.setMaximumSize(new Dimension(280, 18));

            boutonAnnuler = new JButton("Annuler");
            boutonAnnuler.setAlignmentX(CENTER_ALIGNMENT);
            boutonAnnuler.setMnemonic(KeyEvent.VK_A);

            labelMessage.getAccessibleContext().setAccessibleName("Message d'opération");
            barreProgression.getAccessibleContext().setAccessibleName("Indicateur de progression");
            barreProgression.getAccessibleContext().setAccessibleDescription("Indique qu'une opération longue est en cours");
            boutonAnnuler.getAccessibleContext().setAccessibleName("Annuler l'opération");
            boutonAnnuler.getAccessibleContext().setAccessibleDescription("Annule l'opération en cours lorsque l'option est disponible");

            panneauContenu.add(labelMessage);
            panneauContenu.add(Box.createVerticalStrut(theme.spacing().blockGap()));
            panneauContenu.add(barreProgression);
            panneauContenu.add(Box.createVerticalStrut(theme.spacing().blockGap()));
            panneauContenu.add(boutonAnnuler);

            add(panneauContenu, new GridBagConstraints());

            appliquerTheme(theme);
        }

        private void appliquerTheme(TokensTheme t) {
            if (getComponentCount() == 0 || !(getComponent(0) instanceof JPanel panneauContenu)) {
                return;
            }
            panneauContenu.setBackground(t.palette().busyPanelBackground());
            panneauContenu.setBorder(BorderFactory.createCompoundBorder(new LineBorder(t.palette().busyPanelBorder(), 1, true), new EmptyBorder(14, 16, 14, 16)));

            labelMessage.setFont(t.typography().buttonPrimary());
            labelMessage.setForeground(t.palette().bodyText());

            appliquerRoleBouton(boutonAnnuler, RoleBouton.SECONDARY);
        }

        private void configure(String message, boolean annulable, Runnable actionAnnulation) {
            labelMessage.setText(message == null || message.isBlank() ? "Opération en cours..." : message);
            barreProgression.setMinimum(0);
            barreProgression.setMaximum(100);
            barreProgression.setIndeterminate(false);
            barreProgression.setValue(0);
            boutonAnnuler.setVisible(annulable);
            for (var ecouteur : boutonAnnuler.getActionListeners()) {
                boutonAnnuler.removeActionListener(ecouteur);
            }
            if (annulable && actionAnnulation != null) {
                boutonAnnuler.addActionListener(e -> actionAnnulation.run());
            }
        }

        private void mettreAJourProgression(int progression, String message) {
            int valeur = Math.max(0, Math.min(100, progression));
            barreProgression.setIndeterminate(false);
            barreProgression.setValue(valeur);
            if (message != null && !message.isBlank()) {
                labelMessage.setText(message);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(theme.palette().busyOverlay());
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static final class PolitiqueFocusOrdonnee extends FocusTraversalPolicy {
        private final List<Component> ordre;

        private PolitiqueFocusOrdonnee(List<Component> ordre) {
            this.ordre = new ArrayList<>(ordre);
        }

        @Override
        public Component getComponentAfter(Container root, Component aComponent) {
            return avancer(aComponent, 1);
        }

        @Override
        public Component getComponentBefore(Container root, Component aComponent) {
            return avancer(aComponent, -1);
        }

        @Override
        public Component getFirstComponent(Container root) {
            return premierFocalisable();
        }

        @Override
        public Component getLastComponent(Container root) {
            return dernierFocalisable();
        }

        @Override
        public Component getDefaultComponent(Container root) {
            return premierFocalisable();
        }

        private Component avancer(Component actuel, int delta) {
            int taille = ordre.size();
            if (taille == 0) {
                return actuel;
            }

            int depart = ordre.indexOf(actuel);
            if (depart < 0) {
                depart = 0;
            }

            for (int i = 1; i <= taille; i++) {
                int index = Math.floorMod(depart + (i * delta), taille);
                Component candidat = ordre.get(index);
                if (estCandidatFocalisable(candidat)) {
                    return candidat;
                }
            }
            return actuel;
        }

        private Component premierFocalisable() {
            for (Component composant : ordre) {
                if (estCandidatFocalisable(composant)) {
                    return composant;
                }
            }
            return ordre.isEmpty() ? null : ordre.get(0);
        }

        private Component dernierFocalisable() {
            for (int i = ordre.size() - 1; i >= 0; i--) {
                Component composant = ordre.get(i);
                if (estCandidatFocalisable(composant)) {
                    return composant;
                }
            }
            return ordre.isEmpty() ? null : ordre.get(ordre.size() - 1);
        }

        private boolean estCandidatFocalisable(Component composant) {
            return composant != null && composant.isEnabled() && composant.isVisible() && composant.isFocusable();
        }
    }

    private final class IconeConfiguration implements Icon {
        private final int size;

        private IconeConfiguration(int size) {
            this.size = Math.max(12, size);
        }

        public void paintIcon(Component component, Graphics graphics, int x, int y) {
            Graphics2D g2 = (Graphics2D) graphics.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Color cercle = theme.palette().iconButton().foreground();
            Color glyphe = theme.palette().inverseText();

            int centreX = x + (size / 2);
            int centreY = y + (size / 2);
            int rayonExterieur = Math.max(5, (size / 2) - 3);
            int rayonInterieur = Math.max(2, size / 6);
            int longueurDent = Math.max(2, size / 7);
            int epaisseurDent = Math.max(2, size / 8);

            g2.setColor(cercle);
            g2.fillOval(x, y, size, size);

            g2.setColor(glyphe);
            g2.fillOval(centreX - rayonExterieur, centreY - rayonExterieur, rayonExterieur * 2, rayonExterieur * 2);
            g2.setColor(cercle);
            g2.fillOval(centreX - rayonInterieur, centreY - rayonInterieur, rayonInterieur * 2, rayonInterieur * 2);

            g2.setColor(glyphe);
            for (int i = 0; i < 8; i++) {
                double angle = Math.PI / 4.0 * i;
                int dentX = centreX + (int) Math.round(Math.cos(angle) * (rayonExterieur + 1));
                int dentY = centreY + (int) Math.round(Math.sin(angle) * (rayonExterieur + 1));
                g2.fillRoundRect(dentX - (epaisseurDent / 2), dentY - (epaisseurDent / 2), longueurDent, epaisseurDent, epaisseurDent, epaisseurDent);
            }

            g2.dispose();
        }

        public int getIconWidth() {
            return size;
        }

        public int getIconHeight() {
            return size;
        }
    }

    private final class IconeInfo implements Icon {
        private final int size;

        private IconeInfo(int size) {
            this.size = Math.max(12, size);
        }


        public void paintIcon(Component component, Graphics graphics, int x, int y) {
            Graphics2D g2 = (Graphics2D) graphics.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Color cercle = theme.palette().iconButton().foreground();
            Color glyphe = theme.palette().inverseText();

            g2.setColor(cercle);
            g2.fillOval(x, y, size, size);

            int taillePoint = Math.max(2, size / 6);
            int largeurTrait = Math.max(2, size / 7);
            int centreX = x + (size / 2);
            int hautTrait = y + (size / 3);
            int hauteurTrait = Math.max(4, size / 3);

            g2.setColor(glyphe);
            g2.fillOval(centreX - (taillePoint / 2), y + (size / 5), taillePoint, taillePoint);
            g2.fillRoundRect(centreX - (largeurTrait / 2), hautTrait, largeurTrait, hauteurTrait, largeurTrait, largeurTrait);

            g2.dispose();
        }


        public int getIconWidth() {
            return size;
        }


        public int getIconHeight() {
            return size;
        }
    }

    private final class PanneauFondPopup extends JPanel {
        private final FondPopupVisibilite fond;

        private PanneauFondPopup(FondPopupVisibilite fond) {
            this.fond = fond;
            setOpaque(true);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2.setPaint(creerFond(getWidth(), getHeight()));
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();
        }

        private java.awt.Paint creerFond(int largeur, int hauteur) {
            int largeurEffective = Math.max(1, largeur);
            int hauteurEffective = Math.max(1, hauteur);
            switch (fond) {
                case TRANS -> {
                    return new LinearGradientPaint(0f, 0f, 0f, hauteurEffective, new float[]{0f, 0.25f, 0.5f, 0.75f, 1f}, new Color[]{COULEUR_TRANS_BLEU, COULEUR_TRANS_ROSE, COULEUR_BLANCHE, COULEUR_TRANS_ROSE, COULEUR_TRANS_BLEU});
                }
                case NON_BINAIRE -> {
                    return new LinearGradientPaint(0f, 0f, 0f, hauteurEffective, new float[]{0f, 0.33333334f, 0.6666667f, 1f}, new Color[]{COULEUR_NON_BINAIRE_JAUNE, COULEUR_BLANCHE, COULEUR_NON_BINAIRE_VIOLET, COULEUR_NON_BINAIRE_NOIR});
                }
                case LESBIEN -> {
                    return new LinearGradientPaint(0f, 0f, 0f, hauteurEffective, new float[]{0f, 0.16666667f, 0.33333334f, 0.5f, 0.6666667f, 0.8333333f, 1f}, new Color[]{COULEUR_LESBIEN_ORANGE_FONCE, COULEUR_LESBIEN_ORANGE_CLAIR, COULEUR_LESBIEN_ORANGE_PALE, COULEUR_BLANCHE, COULEUR_LESBIEN_ROSE, COULEUR_LESBIEN_ROSE_FONCE, COULEUR_LESBIEN_PRUNE});
                }
                case INTERSEXE -> {
                    float rayon = Math.max(largeurEffective, hauteurEffective) / 2f;
                    return new RadialGradientPaint(largeurEffective / 2f, hauteurEffective / 2f, rayon, new float[]{0f, 0.5f, 1f}, new Color[]{COULEUR_INTERSEXE_JAUNE, COULEUR_INTERSEXE_VIOLET, COULEUR_INTERSEXE_JAUNE});
                }
                case RAINBOW -> {
                    return new LinearGradientPaint(0f, 0f, 0f, hauteurEffective, new float[]{0f, 0.2f, 0.4f, 0.6f, 0.8f, 1f}, new Color[]{COULEUR_RAINBOW_ROUGE, COULEUR_RAINBOW_ORANGE, COULEUR_RAINBOW_JAUNE, COULEUR_RAINBOW_VERT, COULEUR_RAINBOW_BLEU, COULEUR_RAINBOW_VIOLET});
                }
                case PANSEXUEL -> {
                    return new LinearGradientPaint(0f, 0f, 0f, hauteurEffective, new float[]{0f, 0.5f, 1f}, new Color[]{COULEUR_PAN_ROSE, COULEUR_PAN_JAUNE, COULEUR_PAN_BLEU});
                }
                case BISEXUEL -> {
                    return new LinearGradientPaint(0f, 0f, 0f, hauteurEffective, new float[]{0f, 0.5f, 1f}, new Color[]{COULEUR_BI_ROSE, COULEUR_BI_VIOLET, COULEUR_BI_BLEU});
                }
                default -> {
                    return new GradientPaint(0, 0, theme.palette().backgroundTop(), 0, hauteurEffective, theme.palette().backgroundBottom());
                }
            }
        }
    }

    private final class PanneauDegrade extends JPanel {
        @Serial
        private static final long serialVersionUID = 1L;

        private static final Color LESBIEN_ORANGE_FONCE = Color.decode("#D52D00");
        private static final Color LESBIEN_ORANGE_CLAIR = Color.decode("#EF7627");
        private static final Color LESBIEN_ORANGE_PALE = Color.decode("#FF9A56");
        private static final Color LESBIEN_BLANC = Color.decode("#FFFFFF");
        private static final Color LESBIEN_ROSE = Color.decode("#D162A4");
        private static final Color LESBIEN_ROSE_FONCE = Color.decode("#B55690");
        private static final Color LESBIEN_PRUNE = Color.decode("#A30262");

        private static final Color RAINBOW_ROUGE = Color.decode("#E40303");
        private static final Color RAINBOW_ORANGE = Color.decode("#FF8C00");
        private static final Color RAINBOW_JAUNE = Color.decode("#FFED00");
        private static final Color RAINBOW_VERT = Color.decode("#008026");
        private static final Color RAINBOW_BLEU = Color.decode("#004DFF");
        private static final Color RAINBOW_VIOLET = Color.decode("#750787");

        private PanneauDegrade(LayoutManager layout) {
            super(layout);
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2.setPaint(creerDegrade(getHeight()));
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();
            super.paintComponent(g);
        }

        private java.awt.Paint creerDegrade(int hauteur) {
            int hauteurEffective = Math.max(1, hauteur);
            ModeTheme modeTheme = theme.mode();

            if (modeTheme == ModeTheme.LESBIEN) {
                return new LinearGradientPaint(0f, 0f, 0f, hauteurEffective, new float[]{0f, 0.16666667f, 0.33333334f, 0.5f, 0.6666667f, 0.8333333f, 1f}, new Color[]{LESBIEN_ORANGE_FONCE, LESBIEN_ORANGE_CLAIR, LESBIEN_ORANGE_PALE, LESBIEN_BLANC, LESBIEN_ROSE, LESBIEN_ROSE_FONCE, LESBIEN_PRUNE});
            }

            if (modeTheme == ModeTheme.INTERSEXE) {
                float rayon = Math.max(getWidth(), hauteurEffective) / 2f;
                return new RadialGradientPaint(Math.max(1, getWidth()) / 2f, hauteurEffective / 2f, rayon, new float[]{0f, 0.5f, 1f}, new Color[]{COULEUR_INTERSEXE_JAUNE, COULEUR_INTERSEXE_VIOLET, COULEUR_INTERSEXE_JAUNE});
            }

            if (modeTheme == ModeTheme.RAINBOW) {
                return new LinearGradientPaint(0f, 0f, 0f, hauteurEffective, new float[]{0f, 0.2f, 0.4f, 0.6f, 0.8f, 1f}, new Color[]{RAINBOW_ROUGE, RAINBOW_ORANGE, RAINBOW_JAUNE, RAINBOW_VERT, RAINBOW_BLEU, RAINBOW_VIOLET});
            }

            return new GradientPaint(0, 0, theme.palette().backgroundTop(), 0, hauteurEffective, theme.palette().backgroundBottom());
        }
    }

    private final class ChampTexteExemple extends JTextField {
        @Serial
        private static final long serialVersionUID = 1L;
        private String placeholder;

        private ChampTexteExemple(int columns, String placeholder) {
            super(columns);
            this.placeholder = placeholder == null ? "" : placeholder;
        }


        private void setPlaceholder(String placeholder) {
            this.placeholder = placeholder == null ? "" : placeholder;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (!getText().isEmpty() || isFocusOwner() || placeholder.isEmpty()) {
                return;
            }
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(theme.palette().placeholderText());
            g2.setFont(getFont().deriveFont(Font.ITALIC));
            Insets insets = getInsets();
            int y = insets.top + g2.getFontMetrics().getAscent() + 1;
            g2.drawString(placeholder, insets.left + 2, y);
            g2.dispose();
        }
    }

    private final class ZoneTexteExemple extends JTextArea {
        @Serial
        private static final long serialVersionUID = 1L;
        private final String placeholder;

        private ZoneTexteExemple(int rows, int columns, String placeholder) {
            super(rows, columns);
            this.placeholder = placeholder == null ? "" : placeholder;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (!getText().isEmpty() || isFocusOwner() || placeholder.isEmpty()) {
                return;
            }
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(theme.palette().placeholderText());
            g2.setFont(getFont().deriveFont(Font.ITALIC));
            Insets insets = getInsets();
            int y = insets.top + g2.getFontMetrics().getAscent() + 1;
            for (String line : placeholder.split("\\R")) {
                g2.drawString(line, insets.left + 2, y);
                y += g2.getFontMetrics().getHeight();
            }
            g2.dispose();
        }
    }
}
