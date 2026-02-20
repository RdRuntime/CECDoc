package com.rdr.cecdoc.ui;

import com.rdr.cecdoc.model.TypeDocumentGenere;
import com.rdr.cecdoc.service.export.ErreurExportDocument;
import com.rdr.cecdoc.ui.theme.StyliseurBoutonTheme;
import com.rdr.cecdoc.ui.theme.TokensTheme;
import com.rdr.cecdoc.util.ParseursDate;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.Serial;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingWorker;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

abstract class DialogueFormulaireDocumentAbstrait extends JDialog {
    @Serial
    private static final long serialVersionUID = 1L;
    private static final Pattern MOTIF_COURRIEL = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    private static final Pattern MOTIF_TELEPHONE = Pattern.compile("^(?:0[1-9](?:[ .-]?\\d{2}){4}|\\+33[ .-]?[1-9](?:[ .-]?\\d{2}){4})$");
    private static final Pattern MOTIF_PRENOM = Pattern.compile("^[\\p{L}][\\p{L}'’\\-\\s]{0,59}$");
    private static final Pattern MOTIF_NOM = Pattern.compile("^[\\p{L}][\\p{L}'’\\-\\s]{0,79}$");
    private static final Pattern MOTIF_VILLE = Pattern.compile("^[\\p{L}][\\p{L}'’\\-\\s]{0,79}$");
    private static final Pattern MOTIF_INE = Pattern.compile("^[A-Za-z0-9]{5,20}$");
    private static final Pattern MOTIF_REFERENCE = Pattern.compile("^[\\p{L}0-9 .:/#°\\-]{2,80}$");

    private final transient TokensTheme theme;
    private final JLabel labelMessageValidation;
    private final JPanel panneauFormulaire;
    private final transient Map<JComponent, Border> borduresNormales;
    private final transient List<String> messagesErreurs;
    private final JButton boutonAnnuler;
    private final JButton boutonGenerer;
    private final transient Path dossierSortieParDefaut;
    private final transient GridBagConstraints contraintes;
    private int ligneCourante;
    private transient SwingWorker<Void, Void> tacheActive;

    protected DialogueFormulaireDocumentAbstrait(JFrame proprietaire, TokensTheme theme, List<Image> iconesApplication, Path dossierSortieParDefaut, String titreFenetre, String titreFormulaire, String libelleBoutonGenerer) {
        super(proprietaire, titreFenetre, true);
        this.theme = Objects.requireNonNull(theme, "theme");
        this.dossierSortieParDefaut = dossierSortieParDefaut;
        this.borduresNormales = new LinkedHashMap<>();
        this.messagesErreurs = new ArrayList<>();
        if (iconesApplication != null && !iconesApplication.isEmpty()) {
            setIconImages(iconesApplication);
        }

        JPanel panneauRacine = new JPanel(new BorderLayout(0, this.theme.spacing().blockGap()));
        panneauRacine.setBorder(new EmptyBorder(this.theme.spacing().cardInset(), this.theme.spacing().cardInset(), this.theme.spacing().cardInset(), this.theme.spacing().cardInset()));
        panneauRacine.setBackground(this.theme.palette().surfaceBackground());

        JLabel labelTitre = new JLabel(titreFormulaire);
        labelTitre.setFont(this.theme.typography().section());
        labelTitre.setForeground(this.theme.palette().titleText());
        panneauRacine.add(labelTitre, BorderLayout.NORTH);

        panneauFormulaire = new JPanel(new GridBagLayout());
        panneauFormulaire.setOpaque(false);
        contraintes = new GridBagConstraints();
        contraintes.insets = new Insets(Math.max(1, this.theme.spacing().inlineGap() / 2), this.theme.spacing().inlineGap(), Math.max(1, this.theme.spacing().inlineGap() / 2), this.theme.spacing().inlineGap());
        contraintes.gridy = 0;
        contraintes.weightx = 0;
        contraintes.weighty = 0;
        contraintes.anchor = GridBagConstraints.WEST;
        contraintes.fill = GridBagConstraints.HORIZONTAL;
        ligneCourante = 0;

        JScrollPane ascenseur = new JScrollPane(panneauFormulaire);
        ascenseur.setBorder(new EmptyBorder(0, 0, 0, 0));
        ascenseur.getVerticalScrollBar().setUnitIncrement(16);
        ascenseur.getViewport().setOpaque(false);
        ascenseur.setOpaque(false);
        panneauRacine.add(ascenseur, BorderLayout.CENTER);

        labelMessageValidation = new JLabel(" ");
        labelMessageValidation.setFont(this.theme.typography().message());
        labelMessageValidation.setForeground(this.theme.palette().error());

        JPanel panneauActions = new JPanel();
        panneauActions.setLayout(new BoxLayout(panneauActions, BoxLayout.X_AXIS));
        panneauActions.setOpaque(false);

        boutonAnnuler = new JButton("Annuler");
        boutonGenerer = new JButton(libelleBoutonGenerer);
        StyliseurBoutonTheme.appliquer(boutonAnnuler, this.theme.palette().secondaryButton(), this.theme, this.theme.typography().buttonSecondary());
        StyliseurBoutonTheme.appliquer(boutonGenerer, this.theme.palette().primaryButton(), this.theme, this.theme.typography().buttonPrimary());
        boutonAnnuler.setMnemonic(KeyEvent.VK_A);
        boutonGenerer.setMnemonic(KeyEvent.VK_G);
        boutonAnnuler.getAccessibleContext().setAccessibleName("Annuler");
        boutonAnnuler.getAccessibleContext().setAccessibleDescription("Ferme la fenêtre sans générer de document");
        boutonGenerer.getAccessibleContext().setAccessibleName(libelleBoutonGenerer);
        boutonGenerer.getAccessibleContext().setAccessibleDescription("Valide le formulaire puis lance la génération du document");

        boutonAnnuler.addActionListener(e -> {
            if (tacheActive == null) {
                dispose();
            }
        });
        boutonGenerer.addActionListener(e -> lancerGeneration());

        panneauActions.add(Box.createHorizontalGlue());
        panneauActions.add(boutonAnnuler);
        panneauActions.add(Box.createHorizontalStrut(this.theme.spacing().inlineGap()));
        panneauActions.add(boutonGenerer);

        JPanel panneauSud = new JPanel(new BorderLayout(0, this.theme.spacing().inlineGap()));
        panneauSud.setOpaque(false);
        panneauSud.add(labelMessageValidation, BorderLayout.NORTH);
        panneauSud.add(panneauActions, BorderLayout.SOUTH);
        panneauRacine.add(panneauSud, BorderLayout.SOUTH);

        setContentPane(panneauRacine);
        setMinimumSize(new Dimension(860, 620));
        getRootPane().setDefaultButton(boutonGenerer);
        getAccessibleContext().setAccessibleName(titreFenetre);
        getAccessibleContext().setAccessibleDescription("Fenêtre de formulaire de génération documentaire");
        getRootPane().registerKeyboardAction(e -> {
            if (tacheActive == null) {
                dispose();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    protected final TokensTheme theme() {
        return theme;
    }

    protected final ChampTexteExempleDialogue creerChampTexte(int colonnes, String exemple) {
        ChampTexteExempleDialogue champ = new ChampTexteExempleDialogue(colonnes, exemple, theme.palette().placeholderText());
        styliserChampTexte(champ);
        GestionAnnulationTexte.activer(champ);
        connecterEffacementErreurs(champ);
        return champ;
    }

    protected final ZoneTexteExempleDialogue creerZoneTexte(int lignes, int colonnes, String exemple) {
        ZoneTexteExempleDialogue zone = new ZoneTexteExempleDialogue(lignes, colonnes, exemple, theme.palette().placeholderText());
        zone.setLineWrap(true);
        zone.setWrapStyleWord(true);
        styliserZoneTexte(zone);
        GestionAnnulationTexte.activer(zone);
        connecterEffacementErreurs(zone);
        return zone;
    }

    protected final JScrollPane creerAscenseurZoneTexte(JTextArea zoneTexte, int hauteurMinimale) {
        JScrollPane ascenseur = new JScrollPane(zoneTexte);
        int marge = Math.max(1, theme.spacing().inlineGap() / 4);
        ascenseur.setBorder(new CompoundBorder(new LineBorder(theme.palette().border(), 1, true), new EmptyBorder(marge, marge, marge, marge)));
        ascenseur.setPreferredSize(new Dimension(0, Math.max(hauteurMinimale, 72)));
        ascenseur.getViewport().setOpaque(true);
        ascenseur.getViewport().setBackground(theme.palette().fieldBackground());
        ascenseur.getVerticalScrollBar().setUnitIncrement(16);
        connecterEffacementErreurs(ascenseur);
        return ascenseur;
    }

    protected final JComboBox<String> creerCombo(String... options) {
        JComboBox<String> combo = new JComboBox<>(options == null ? new String[0] : options);
        combo.setFont(theme.typography().input());
        combo.setBackground(theme.palette().fieldBackground());
        combo.setForeground(theme.palette().bodyText());
        combo.setBorder(new EmptyBorder(0, 0, 0, 0));
        connecterEffacementErreurs(combo);
        return combo;
    }

    protected final JCheckBox creerCase(String texte) {
        JCheckBox caseCoche = new JCheckBox(texte);
        caseCoche.setOpaque(false);
        caseCoche.setFont(theme.typography().input());
        caseCoche.setForeground(theme.palette().bodyText());
        connecterEffacementErreurs(caseCoche);
        return caseCoche;
    }

    protected final void ajouterTitreSection(String texte) {
        JLabel labelSection = new JLabel(texte);
        labelSection.setFont(theme.typography().section());
        labelSection.setForeground(theme.palette().titleText());

        contraintes.gridx = 0;
        contraintes.gridy = ligneCourante++;
        contraintes.gridwidth = 2;
        contraintes.weightx = 1;
        contraintes.fill = GridBagConstraints.HORIZONTAL;
        int max = Math.max(1, theme.spacing().inlineGap() / 2);
        contraintes.insets = new Insets(theme.spacing().blockGap(), theme.spacing().inlineGap(), max, theme.spacing().inlineGap());
        panneauFormulaire.add(labelSection, contraintes);

        contraintes.insets = new Insets(max, theme.spacing().inlineGap(), max, theme.spacing().inlineGap());
    }

    protected final void ajouterLigneChamp(String libelle, JComponent composant) {
        ajouterLigneChamp(libelle, composant, composant);
    }

    protected final void ajouterLigneChamp(String libelle, JComponent composantAffiche, JComponent composantValidation) {
        JLabel label = new JLabel(libelle);
        label.setFont(theme.typography().label());
        label.setForeground(theme.palette().bodyText());
        label.setLabelFor(cibleLibelle(composantAffiche));

        contraintes.gridx = 0;
        contraintes.gridy = ligneCourante;
        contraintes.gridwidth = 1;
        contraintes.weightx = 0.35;
        contraintes.fill = GridBagConstraints.HORIZONTAL;
        panneauFormulaire.add(label, contraintes);

        contraintes.gridx = 1;
        contraintes.weightx = 0.65;
        panneauFormulaire.add(composantAffiche, contraintes);
        ligneCourante++;

        enregistrerBordureNormale(composantValidation);
    }

    protected final void ajouterLignePleine(JComponent composant) {
        contraintes.gridx = 0;
        contraintes.gridy = ligneCourante++;
        contraintes.gridwidth = 2;
        contraintes.weightx = 1;
        contraintes.fill = GridBagConstraints.HORIZONTAL;
        panneauFormulaire.add(composant, contraintes);
        contraintes.gridwidth = 1;
    }

    protected final void terminerConstruction() {
        contraintes.gridx = 0;
        contraintes.gridy = ligneCourante;
        contraintes.gridwidth = 2;
        contraintes.weightx = 1;
        contraintes.weighty = 1;
        contraintes.fill = GridBagConstraints.VERTICAL;
        panneauFormulaire.add(Box.createVerticalGlue(), contraintes);

        pack();
        setSize(Math.max(900, getWidth()), Math.max(660, getHeight()));
        setLocationRelativeTo(getOwner());
    }

    protected final void signalerErreur(JComponent composant, String message) {
        if (composant != null) {
            composant.setBorder(new LineBorder(theme.palette().error(), 1, true));
        }
        String texte = message == null ? "" : message.trim();
        if (!texte.isEmpty() && !messagesErreurs.contains(texte)) {
            messagesErreurs.add(texte);
        }
        afficherMessagesErreurs();
    }

    protected final void signalerErreurGlobale(String message) {
        String texte = message == null ? "" : message.trim();
        if (!texte.isEmpty() && !messagesErreurs.contains(texte)) {
            messagesErreurs.add(texte);
        }
        afficherMessagesErreurs();
    }

    protected final void effacerErreursValidation() {
        messagesErreurs.clear();
        labelMessageValidation.setText(" ");
        for (Map.Entry<JComponent, Border> entree : borduresNormales.entrySet()) {
            entree.getKey().setBorder(entree.getValue());
        }
    }

    protected final String texte(JTextComponent composant) {
        return composant == null ? "" : Objects.toString(composant.getText(), "").trim();
    }

    protected final String texteSelection(JComboBox<String> composant) {
        return composant == null ? "" : Objects.toString(composant.getSelectedItem(), "").trim();
    }

    protected final boolean formatCourrielValide(String courriel) {
        return courriel != null && MOTIF_COURRIEL.matcher(courriel.trim()).matches();
    }

    protected final boolean formatTelephoneValide(String telephone) {
        return telephone != null && MOTIF_TELEPHONE.matcher(telephone.trim()).matches();
    }

    protected final boolean formatPrenomValide(String prenom) {
        return prenom != null && MOTIF_PRENOM.matcher(prenom.trim()).matches();
    }

    protected final boolean formatPrenomsMultiplesValide(String prenoms) {
        if (prenoms == null || prenoms.isBlank()) {
            return false;
        }
        String[] elements = prenoms.split(",");
        for (String element : elements) {
            if (!formatPrenomValide(element)) {
                return false;
            }
        }
        return true;
    }

    protected final boolean formatNomValide(String nom) {
        return nom != null && MOTIF_NOM.matcher(nom.trim()).matches();
    }

    protected final boolean formatVilleValide(String ville) {
        return ville != null && MOTIF_VILLE.matcher(ville.trim()).matches();
    }

    protected final boolean formatIneValide(String ine) {
        return ine != null && MOTIF_INE.matcher(ine.trim()).matches();
    }

    protected final boolean formatReferenceValide(String reference) {
        return reference != null && MOTIF_REFERENCE.matcher(reference.trim()).matches();
    }

    protected final boolean texteSimpleValide(String texte, int min, int max) {
        if (texte == null) {
            return false;
        }
        String valeur = texte.trim();
        return valeur.length() >= min && valeur.length() <= max;
    }

    protected final boolean texteMultiligneValide(String texte, int min, int max) {
        if (texte == null) {
            return false;
        }
        String valeur = texte.replaceAll("\\s+", " ").trim();
        return valeur.length() >= min && valeur.length() <= max;
    }

    protected final String exemplePrenomSimple(String genre) {
        if (genre == null || genre.isBlank()) {
            return "Ex: Tom";
        }
        String normalise = genre.trim().toLowerCase(Locale.ROOT);
        if (normalise.startsWith("f")) {
            return "Ex: Alice";
        }
        if (normalise.startsWith("n")) {
            return "Ex: Sacha";
        }
        return "Ex: Tom";
    }

    protected final String exemplePrenoms(String genre) {
        if (genre == null || genre.isBlank()) {
            return "Ex: Tom, Noé, Max";
        }
        String normalise = genre.trim().toLowerCase(Locale.ROOT);
        if (normalise.startsWith("f")) {
            return "Ex: Alice, Emma, Agathe";
        }
        if (normalise.startsWith("n")) {
            return "Ex: Sacha, Charlie, Alex";
        }
        return "Ex: Tom, Noé, Max";
    }

    protected final SelecteurDateDialogue creerSelecteurDate() {
        int anneeCourante = LocalDate.now().getYear();
        return new SelecteurDateDialogue(Math.max(anneeCourante + 10, 2100), 1900, anneeCourante);
    }

    private void lancerGeneration() {
        if (tacheActive != null) {
            return;
        }
        effacerErreursValidation();
        if (!validerFormulaire()) {
            afficherMessagesErreurs();
            return;
        }

        File fichierDestination = choisirDestinationDocument();
        if (fichierDestination == null) {
            return;
        }

        boolean ecraser = fichierDestination.exists();
        if (ecraser) {
            int choix = afficherConfirmation("Le fichier existe déjà. Voulez-vous le remplacer ?", "Confirmer l'écrasement");
            if (choix != JOptionPane.YES_OPTION) {
                return;
            }
        }

        Path cheminDestination = fichierDestination.toPath();
        tacheActive = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                genererDocument(cheminDestination, ecraser);
                return null;
            }

            @Override
            protected void done() {
                tacheActive = null;
                appliquerEtatOccupation(false);
                if (isCancelled()) {
                    return;
                }
                try {
                    get();
                    afficherMessage(messageSuccesGeneration(cheminDestination), "Succès", JOptionPane.INFORMATION_MESSAGE);
                } catch (CancellationException ex) {
                    afficherMessage("La génération a été annulée.", "Annulée", JOptionPane.INFORMATION_MESSAGE);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    afficherMessage("La génération a été interrompue.", "Erreur", JOptionPane.ERROR_MESSAGE);
                } catch (ExecutionException ex) {
                    Throwable cause = ex.getCause();
                    if (cause instanceof ErreurExportDocument erreurExportDocument) {
                        afficherMessage(erreurExportDocument.getUserMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                    } else {
                        afficherMessage("Une erreur est survenue pendant la génération.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        };
        appliquerEtatOccupation(true);
        tacheActive.execute();
    }

    private void appliquerEtatOccupation(boolean occupe) {
        setCursor(occupe ? Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR) : Cursor.getDefaultCursor());
        boutonAnnuler.setEnabled(!occupe);
        boutonGenerer.setEnabled(!occupe);
    }

    private File choisirDestinationDocument() {
        JFileChooser selecteur = new JFileChooser();
        MemoireRepertoireExplorateur.appliquerAuSelecteur(selecteur, dossierSortieParDefaut);
        selecteur.setDialogTitle(titreChoixDestination());
        TypeDocumentGenere typeParDefaut = SelectionFichierDocument.typeDepuisNomFichier(nomFichierParDefaut());
        String nomParDefaut = SelectionFichierDocument.adapterNomFichier(nomFichierParDefaut(), typeParDefaut);
        selecteur.setSelectedFile(fichierParDefautDansDossierSortie(nomParDefaut));
        selecteur.setAcceptAllFileFilterUsed(false);
        SelectionFichierDocument.appliquerFiltresDocuments(selecteur, "Document", typeParDefaut);
        int choix = selecteur.showSaveDialog(this);
        if (choix != JFileChooser.APPROVE_OPTION) {
            return null;
        }
        MemoireRepertoireExplorateur.memoriserDepuisSelection(selecteur);
        TypeDocumentGenere typeChoisi = SelectionFichierDocument.typeDepuisSelection(selecteur);
        return SelectionFichierDocument.garantirExtension(selecteur.getSelectedFile(), typeChoisi);
    }

    private int afficherConfirmation(String message, String titre) {
        JOptionPane optionPane = new JOptionPane(message, JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION);
        optionPane.getAccessibleContext().setAccessibleName(titre);
        optionPane.getAccessibleContext().setAccessibleDescription(message);
        JDialog dialogue = optionPane.createDialog(this, titre);
        HabillageDialogues.preparerDialogue(dialogue, theme);
        dialogue.pack();
        dialogue.setLocationRelativeTo(this);
        dialogue.setVisible(true);
        Object valeur = optionPane.getValue();
        if (valeur instanceof Integer entier) {
            return entier;
        }
        return JOptionPane.CLOSED_OPTION;
    }

    private void afficherMessage(String message, String titre, int type) {
        JOptionPane optionPane = new JOptionPane(message, type, JOptionPane.DEFAULT_OPTION);
        optionPane.getAccessibleContext().setAccessibleName(titre);
        optionPane.getAccessibleContext().setAccessibleDescription(message);
        JDialog dialogue = optionPane.createDialog(this, titre);
        HabillageDialogues.preparerDialogue(dialogue, theme);
        dialogue.pack();
        dialogue.setLocationRelativeTo(this);
        dialogue.setVisible(true);
        dialogue.dispose();
    }

    private void styliserChampTexte(JTextComponent composant) {
        composant.setFont(theme.typography().input());
        composant.setBackground(theme.palette().fieldBackground());
        composant.setForeground(theme.palette().bodyText());
        composant.setCaretColor(theme.palette().focus());
        composant.setBorder(new CompoundBorder(new LineBorder(theme.palette().border(), 1, true), new EmptyBorder(theme.spacing().fieldInsetY(), theme.spacing().fieldInsetX(), theme.spacing().fieldInsetY(), theme.spacing().fieldInsetX())));
    }

    private void styliserZoneTexte(JTextArea composant) {
        composant.setFont(theme.typography().input());
        composant.setBackground(theme.palette().fieldBackground());
        composant.setForeground(theme.palette().bodyText());
        composant.setCaretColor(theme.palette().focus());
        composant.setBorder(new EmptyBorder(theme.spacing().fieldInsetY(), theme.spacing().fieldInsetX(), theme.spacing().fieldInsetY(), theme.spacing().fieldInsetX()));
    }

    private void connecterEffacementErreurs(JComponent composant) {
        if (composant instanceof JTextComponent champTexte) {
            champTexte.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    effacerErreursValidation();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    effacerErreursValidation();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    effacerErreursValidation();
                }
            });
            return;
        }
        if (composant instanceof JComboBox<?> listeChoix) {
            listeChoix.addActionListener(e -> effacerErreursValidation());
            return;
        }
        if (composant instanceof JCheckBox caseCoche) {
            caseCoche.addActionListener(e -> effacerErreursValidation());
            return;
        }
        if (composant instanceof JScrollPane ascenseur && ascenseur.getViewport() != null && ascenseur.getViewport().getView() instanceof JComponent contenu) {
            connecterEffacementErreurs(contenu);
        }
    }

    private JComponent cibleLibelle(JComponent composant) {
        if (composant instanceof JScrollPane ascenseur && ascenseur.getViewport() != null && ascenseur.getViewport().getView() instanceof JComponent contenu) {
            return contenu;
        }
        return composant;
    }

    private void enregistrerBordureNormale(JComponent composant) {
        if (composant == null || borduresNormales.containsKey(composant)) {
            return;
        }
        borduresNormales.put(composant, composant.getBorder());
    }

    private void afficherMessagesErreurs() {
        if (messagesErreurs.isEmpty()) {
            labelMessageValidation.setText(" ");
            return;
        }
        StringBuilder texte = new StringBuilder("<html>");
        for (int i = 0; i < messagesErreurs.size(); i++) {
            if (i > 0) {
                texte.append("<br/>");
            }
            texte.append(echapperHtml(messagesErreurs.get(i)));
        }
        texte.append("</html>");
        labelMessageValidation.setText(texte.toString());
    }

    private String echapperHtml(String texte) {
        if (texte == null || texte.isBlank()) {
            return "";
        }
        return texte.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    private File fichierParDefautDansDossierSortie(String nomFichier) {
        if (dossierSortieParDefaut == null || nomFichier == null || nomFichier.isBlank()) {
            return new File(nomFichier == null ? "" : nomFichier);
        }
        return dossierSortieParDefaut.resolve(nomFichier).toFile();
    }

    protected abstract boolean validerFormulaire();

    protected abstract String titreChoixDestination();

    protected abstract String nomFichierParDefaut();

    protected abstract void genererDocument(Path destination, boolean ecraser) throws ErreurExportDocument;

    protected abstract String messageSuccesGeneration(Path destination);

    protected final class SelecteurDateDialogue extends JPanel {
        @Serial
        private static final long serialVersionUID = 1L;

        private final JComboBox<String> comboJour;
        private final JComboBox<String> comboMois;
        private final JComboBox<String> comboAnnee;

        private SelecteurDateDialogue(int anneeMax, int anneeMin, int anneeParDefaut) {
            setOpaque(false);
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

            comboJour = creerComboNombres(1, 31);
            comboMois = creerComboNombres(1, 12);
            comboAnnee = creerComboNombresDecroissants(anneeMax, anneeMin);

            comboMois.addActionListener(e -> ajusterJour());
            comboAnnee.addActionListener(e -> ajusterJour());

            add(comboJour);
            int max = Math.max(4, theme.spacing().inlineGap() / 3);
            add(Box.createHorizontalStrut(max));
            add(new JLabel("/"));
            add(Box.createHorizontalStrut(max));
            add(comboMois);
            add(Box.createHorizontalStrut(max));
            add(new JLabel("/"));
            add(Box.createHorizontalStrut(max));
            add(comboAnnee);

            definirDateParDefaut(anneeParDefaut);
            connecterEffacementErreurs(this);
        }

        void appliquerDateTexte(String texteDate) {
            if (ParseursDate.dateSaisieValide(texteDate)) {
                LocalDate date = ParseursDate.parserDateSaisie(texteDate.trim());
                comboAnnee.setSelectedItem(Integer.toString(date.getYear()));
                comboMois.setSelectedItem(Integer.toString(date.getMonthValue()));
                ajusterJour();
                comboJour.setSelectedItem(Integer.toString(date.getDayOfMonth()));
                return;
            }
            definirDateParDefaut(LocalDate.now().getYear());
        }

        String texteDate() {
            int jour = valeurEntiereSelectionnee(comboJour);
            int mois = valeurEntiereSelectionnee(comboMois);
            int annee = valeurEntiereSelectionnee(comboAnnee);
            if (jour <= 0 || mois <= 0 || annee <= 0) {
                return "";
            }
            return String.format(Locale.ROOT, "%02d/%02d/%04d", jour, mois, annee);
        }

        boolean estDateValide() {
            return ParseursDate.dateSaisieValide(texteDate());
        }

        @Override
        public void setEnabled(boolean actif) {
            super.setEnabled(actif);
            comboJour.setEnabled(actif);
            comboMois.setEnabled(actif);
            comboAnnee.setEnabled(actif);
        }

        private void definirDateParDefaut(int anneeParDefaut) {
            comboAnnee.setSelectedItem(Integer.toString(anneeParDefaut));
            comboMois.setSelectedItem("12");
            ajusterJour();
            comboJour.setSelectedItem("13");
        }

        private void ajusterJour() {
            int annee = valeurEntiereSelectionnee(comboAnnee);
            int mois = valeurEntiereSelectionnee(comboMois);
            if (annee <= 0 || mois <= 0) {
                return;
            }
            int jourActuel = valeurEntiereSelectionnee(comboJour);
            int jourMax = YearMonth.of(annee, mois).lengthOfMonth();
            comboJour.removeAllItems();
            for (int jour = 1; jour <= jourMax; jour++) {
                comboJour.addItem(Integer.toString(jour));
            }
            int jourSelectionne = jourActuel <= 0 ? 13 : Math.min(jourActuel, jourMax);
            comboJour.setSelectedItem(Integer.toString(jourSelectionne));
        }
    }

    private JComboBox<String> creerComboNombres(int debut, int fin) {
        List<String> valeurs = new ArrayList<>();
        for (int valeur = debut; valeur <= fin; valeur++) {
            valeurs.add(Integer.toString(valeur));
        }
        return creerCombo(valeurs.toArray(new String[0]));
    }

    private JComboBox<String> creerComboNombresDecroissants(int debut, int fin) {
        List<String> valeurs = new ArrayList<>();
        for (int valeur = debut; valeur >= fin; valeur--) {
            valeurs.add(Integer.toString(valeur));
        }
        return creerCombo(valeurs.toArray(new String[0]));
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

    protected static final class ChampTexteExempleDialogue extends JTextField {
        @Serial
        private static final long serialVersionUID = 1L;
        private String exemple;
        private final Color couleurExemple;

        private ChampTexteExempleDialogue(int colonnes, String exemple, Color couleurExemple) {
            super(colonnes);
            this.exemple = exemple == null ? "" : exemple;
            this.couleurExemple = couleurExemple;
        }

        void setExemple(String exemple) {
            this.exemple = exemple == null ? "" : exemple;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (!getText().isEmpty() || isFocusOwner() || exemple.isEmpty()) {
                return;
            }
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(couleurExemple);
            g2.setFont(getFont().deriveFont(Font.ITALIC));
            Insets insets = getInsets();
            int y = insets.top + g2.getFontMetrics().getAscent() + 1;
            g2.drawString(exemple, insets.left + 2, y);
            g2.dispose();
        }
    }

    protected static final class ZoneTexteExempleDialogue extends JTextArea {
        @Serial
        private static final long serialVersionUID = 1L;
        private final String exemple;
        private final Color couleurExemple;

        private ZoneTexteExempleDialogue(int lignes, int colonnes, String exemple, Color couleurExemple) {
            super(lignes, colonnes);
            this.exemple = exemple == null ? "" : exemple;
            this.couleurExemple = couleurExemple;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (!getText().isEmpty() || isFocusOwner() || exemple.isEmpty()) {
                return;
            }
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(couleurExemple);
            g2.setFont(getFont().deriveFont(Font.ITALIC));
            Insets insets = getInsets();
            int y = insets.top + g2.getFontMetrics().getAscent() + 1;
            for (String ligne : exemple.split("\\R")) {
                g2.drawString(ligne, insets.left + 2, y);
                y += g2.getFontMetrics().getHeight();
            }
            g2.dispose();
        }
    }
}
