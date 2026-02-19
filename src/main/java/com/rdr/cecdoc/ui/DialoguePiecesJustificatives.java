package com.rdr.cecdoc.ui;

import com.rdr.cecdoc.model.PieceJointe;
import com.rdr.cecdoc.model.TypePieceJointe;
import com.rdr.cecdoc.ui.theme.StyliseurBoutonTheme;
import com.rdr.cecdoc.ui.theme.TokensTheme;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.Serial;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

public final class DialoguePiecesJustificatives extends JDialog {
    @Serial
    private static final long serialVersionUID = 1L;

    private final DefaultListModel<String> modeleIntitules;
    private final JList<String> listeIntitules;
    private final DefaultListModel<PieceJointe> modeleFichiers;
    private final JList<PieceJointe> listeFichiers;
    private final Map<String, List<PieceJointe>> fichiersParIntitule;
    private final transient TokensTheme theme;
    private final transient Icon iconeDialogue;

    private final JLabel labelIntituleSelectionne;
    private final JLabel labelAlerteFichier;

    private final JButton boutonAjouterFichier;
    private final JButton boutonRetirerFichier;
    private final JButton boutonMonterFichier;
    private final JButton boutonDescendreFichier;
    private final JButton boutonRemplacerFichier;

    private boolean enregistre;

    public DialoguePiecesJustificatives(JFrame proprietaire, List<String> intitulesInitiaux, Map<String, List<PieceJointe>> fichiersInitiauxParIntitule, TokensTheme theme) {
        super(proprietaire, "Pièces justificatives", true);
        this.theme = Objects.requireNonNull(theme, "theme");

        this.modeleIntitules = new DefaultListModel<>();
        this.modeleFichiers = new DefaultListModel<>();
        this.listeIntitules = new JList<>(modeleIntitules);
        this.listeFichiers = new JList<>(modeleFichiers);
        this.fichiersParIntitule = new LinkedHashMap<>();

        if (intitulesInitiaux != null) {
            for (String intitule : intitulesInitiaux) {
                if (intitule == null) {
                    continue;
                }
                String intituleNettoye = intitule.trim();
                if (intituleNettoye.isEmpty()) {
                    continue;
                }
                if (!fichiersParIntitule.containsKey(intituleNettoye)) {
                    modeleIntitules.addElement(intituleNettoye);
                    fichiersParIntitule.put(intituleNettoye, copierListe(fichiersInitiauxParIntitule == null ? null : fichiersInitiauxParIntitule.get(intituleNettoye)));
                }
            }
        }

        if (fichiersInitiauxParIntitule != null && !fichiersInitiauxParIntitule.isEmpty()) {
            for (Map.Entry<String, List<PieceJointe>> entree : fichiersInitiauxParIntitule.entrySet()) {
                String intitule = entree.getKey() == null ? "" : entree.getKey().trim();
                if (intitule.isEmpty() || fichiersParIntitule.containsKey(intitule)) {
                    continue;
                }
                modeleIntitules.addElement(intitule);
                fichiersParIntitule.put(intitule, copierListe(entree.getValue()));
            }
        }

        if (proprietaire != null && !proprietaire.getIconImages().isEmpty()) {
            setIconImages(proprietaire.getIconImages());
        }
        this.iconeDialogue = creerIconeDialogue(proprietaire == null ? List.of() : proprietaire.getIconImages());

        this.labelIntituleSelectionne = new JLabel("Sélectionnez un intitulé");
        this.labelAlerteFichier = new JLabel(" ");

        this.boutonAjouterFichier = new JButton("Ajouter un fichier…");
        this.boutonRetirerFichier = new JButton("Retirer");
        this.boutonMonterFichier = new JButton("Monter");
        this.boutonDescendreFichier = new JButton("Descendre");
        this.boutonRemplacerFichier = new JButton("Remplacer…");

        initialiserAccessibilite();
        construireInterface();
        setSize(980, 520);
        setLocationRelativeTo(proprietaire);
    }

    private void construireInterface() {
        JPanel racine = new JPanel(new BorderLayout(theme.spacing().inlineGap(), theme.spacing().inlineGap()));
        racine.setBorder(new EmptyBorder(theme.spacing().cardInset(), theme.spacing().cardInset(), theme.spacing().cardInset(), theme.spacing().cardInset()));
        racine.setBackground(theme.palette().surfaceBackground());

        JLabel titre = new JLabel("Personnaliser les pièces justificatives");
        titre.setFont(theme.typography().section());
        titre.setForeground(theme.palette().titleText());
        racine.add(titre, BorderLayout.NORTH);

        JPanel centre = new JPanel(new BorderLayout(theme.spacing().inlineGap(), 0));
        centre.setOpaque(false);
        centre.add(construirePanneauIntitules(), BorderLayout.WEST);
        centre.add(construirePanneauFichiers(), BorderLayout.CENTER);
        racine.add(centre, BorderLayout.CENTER);

        JPanel bas = new JPanel();
        bas.setLayout(new BoxLayout(bas, BoxLayout.X_AXIS));
        bas.setOpaque(false);

        JButton boutonEnregistrer = new JButton("Enregistrer");
        JButton boutonAnnuler = new JButton("Annuler");

        boutonEnregistrer.setMnemonic(KeyEvent.VK_E);
        boutonAnnuler.setMnemonic(KeyEvent.VK_A);
        boutonEnregistrer.getAccessibleContext().setAccessibleName("Enregistrer la configuration des pièces");
        boutonEnregistrer.getAccessibleContext().setAccessibleDescription("Valide les intitulés et les fichiers attachés");
        boutonAnnuler.getAccessibleContext().setAccessibleName("Annuler les modifications des pièces");
        boutonAnnuler.getAccessibleContext().setAccessibleDescription("Ferme la fenêtre sans enregistrer les modifications");

        StyliseurBoutonTheme.appliquer(boutonEnregistrer, theme.palette().primaryButton(), theme, theme.typography().buttonPrimary());
        StyliseurBoutonTheme.appliquer(boutonAnnuler, theme.palette().secondaryButton(), theme, theme.typography().buttonSecondary());

        bas.add(Box.createHorizontalGlue());
        bas.add(boutonAnnuler);
        bas.add(Box.createHorizontalStrut(theme.spacing().inlineGap()));
        bas.add(boutonEnregistrer);

        racine.add(bas, BorderLayout.SOUTH);
        setContentPane(racine);

        getRootPane().setDefaultButton(boutonEnregistrer);
        getRootPane().registerKeyboardAction(e -> annuler(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);

        listeIntitules.addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                chargerFichiersPourIntituleSelectionne();
            }
        });
        listeFichiers.addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                mettreAJourEtatActions();
                mettreAJourAlerteFichier();
            }
        });

        boutonEnregistrer.addActionListener(e -> enregistrer());
        boutonAnnuler.addActionListener(e -> annuler());

        if (!modeleIntitules.isEmpty()) {
            listeIntitules.setSelectedIndex(0);
        } else {
            mettreAJourEtatActions();
        }

        SwingUtilities.invokeLater(() -> {
            if (!modeleIntitules.isEmpty()) {
                listeIntitules.requestFocusInWindow();
            }
        });
    }

    private void initialiserAccessibilite() {
        getAccessibleContext().setAccessibleName("Pièces justificatives");
        getAccessibleContext().setAccessibleDescription("Fenêtre de gestion des intitulés et des fichiers justificatifs attachés");

        listeIntitules.getAccessibleContext().setAccessibleName("Liste des intitulés de pièces justificatives");
        listeIntitules.getAccessibleContext().setAccessibleDescription("Liste des catégories de pièces justificatives, parcourable au clavier");
        listeIntitules.setToolTipText("Intitulés des pièces justificatives");

        listeFichiers.getAccessibleContext().setAccessibleName("Liste des fichiers attachés");
        listeFichiers.getAccessibleContext().setAccessibleDescription("Liste ordonnée des fichiers attachés à l'intitulé sélectionné");
        listeFichiers.setToolTipText("Fichiers attachés à l'intitulé sélectionné");

        labelIntituleSelectionne.getAccessibleContext().setAccessibleName("Intitulé sélectionné");
        labelAlerteFichier.getAccessibleContext().setAccessibleName("Alerte sur le fichier sélectionné");

        boutonAjouterFichier.setMnemonic(KeyEvent.VK_F);
        boutonRetirerFichier.setMnemonic(KeyEvent.VK_R);
        boutonMonterFichier.setMnemonic(KeyEvent.VK_U);
        boutonDescendreFichier.setMnemonic(KeyEvent.VK_D);
        boutonRemplacerFichier.setMnemonic(KeyEvent.VK_P);

        definirAccessibiliteBouton(boutonAjouterFichier, "Ajouter un fichier justificatif", "Ajoute un ou plusieurs fichiers à l'intitulé sélectionné");
        definirAccessibiliteBouton(boutonRetirerFichier, "Retirer le fichier sélectionné", "Retire le fichier sélectionné de la liste");
        definirAccessibiliteBouton(boutonMonterFichier, "Monter le fichier sélectionné", "Déplace le fichier sélectionné vers le haut");
        definirAccessibiliteBouton(boutonDescendreFichier, "Descendre le fichier sélectionné", "Déplace le fichier sélectionné vers le bas");
        definirAccessibiliteBouton(boutonRemplacerFichier, "Remplacer le fichier sélectionné", "Remplace le fichier sélectionné par un autre fichier");

        boutonAjouterFichier.setToolTipText("Ajouter des fichiers à l'intitulé");
        boutonRetirerFichier.setToolTipText("Retirer le fichier sélectionné");
        boutonMonterFichier.setToolTipText("Monter le fichier sélectionné");
        boutonDescendreFichier.setToolTipText("Descendre le fichier sélectionné");
        boutonRemplacerFichier.setToolTipText("Remplacer le fichier sélectionné");
    }

    private void definirAccessibiliteBouton(JButton bouton, String nom, String description) {
        bouton.getAccessibleContext().setAccessibleName(nom);
        bouton.getAccessibleContext().setAccessibleDescription(description);
    }

    private JPanel construirePanneauIntitules() {
        JPanel panneau = new JPanel(new BorderLayout(theme.spacing().inlineGap(), theme.spacing().inlineGap()));
        panneau.setOpaque(false);
        panneau.setPreferredSize(new java.awt.Dimension(360, 360));

        JLabel label = new JLabel("Intitulés");
        label.setFont(theme.typography().section());
        label.setForeground(theme.palette().titleText());
        label.setLabelFor(listeIntitules);
        label.setDisplayedMnemonic(KeyEvent.VK_I);
        panneau.add(label, BorderLayout.NORTH);

        listeIntitules.setFont(theme.typography().input());
        listeIntitules.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listeIntitules.setBackground(theme.palette().fieldBackground());
        listeIntitules.setForeground(theme.palette().bodyText());

        JScrollPane ascenseur = new JScrollPane(listeIntitules);
        int marge = Math.max(1, theme.spacing().inlineGap() / 2);
        ascenseur.setBorder(BorderFactory.createCompoundBorder(new LineBorder(theme.palette().border(), 1, true), new EmptyBorder(marge, marge, marge, marge)));
        panneau.add(ascenseur, BorderLayout.CENTER);

        JPanel boutons = new JPanel();
        boutons.setLayout(new BoxLayout(boutons, BoxLayout.X_AXIS));
        boutons.setOpaque(false);

        JButton boutonAjouter = new JButton("Ajouter");
        JButton boutonModifier = new JButton("Modifier");
        JButton boutonSupprimer = new JButton("Supprimer");

        boutonAjouter.setMnemonic(KeyEvent.VK_A);
        boutonModifier.setMnemonic(KeyEvent.VK_M);
        boutonSupprimer.setMnemonic(KeyEvent.VK_S);
        boutonAjouter.getAccessibleContext().setAccessibleName("Ajouter un intitulé");
        boutonModifier.getAccessibleContext().setAccessibleName("Modifier l'intitulé sélectionné");
        boutonSupprimer.getAccessibleContext().setAccessibleName("Supprimer l'intitulé sélectionné");
        boutonAjouter.setToolTipText("Ajouter un nouvel intitulé");
        boutonModifier.setToolTipText("Modifier l'intitulé sélectionné");
        boutonSupprimer.setToolTipText("Supprimer l'intitulé sélectionné");

        StyliseurBoutonTheme.appliquer(boutonAjouter, theme.palette().secondaryButton(), theme, theme.typography().buttonSecondary());
        StyliseurBoutonTheme.appliquer(boutonModifier, theme.palette().secondaryButton(), theme, theme.typography().buttonSecondary());
        StyliseurBoutonTheme.appliquer(boutonSupprimer, theme.palette().dangerButton(), theme, theme.typography().buttonSecondary());

        boutons.add(boutonAjouter);
        boutons.add(Box.createHorizontalStrut(theme.spacing().inlineGap()));
        boutons.add(boutonModifier);
        boutons.add(Box.createHorizontalStrut(theme.spacing().inlineGap()));
        boutons.add(boutonSupprimer);

        panneau.add(boutons, BorderLayout.SOUTH);

        boutonAjouter.addActionListener(e -> ajouterIntitule());
        boutonModifier.addActionListener(e -> modifierIntitule());
        boutonSupprimer.addActionListener(e -> supprimerIntitule());

        return panneau;
    }

    private JPanel construirePanneauFichiers() {
        JPanel panneau = new JPanel(new BorderLayout(theme.spacing().inlineGap(), theme.spacing().inlineGap()));
        panneau.setOpaque(false);

        JPanel entete = new JPanel();
        entete.setLayout(new BoxLayout(entete, BoxLayout.Y_AXIS));
        entete.setOpaque(false);

        JLabel section = new JLabel("Fichiers attachés");
        section.setFont(theme.typography().section());
        section.setForeground(theme.palette().titleText());
        section.setLabelFor(listeFichiers);
        section.setDisplayedMnemonic(KeyEvent.VK_H);

        labelIntituleSelectionne.setFont(theme.typography().input());
        labelIntituleSelectionne.setForeground(theme.palette().bodyText());

        entete.add(section);
        entete.add(Box.createVerticalStrut(Math.max(1, theme.spacing().inlineGap() / 3)));
        entete.add(labelIntituleSelectionne);

        panneau.add(entete, BorderLayout.NORTH);

        listeFichiers.setFont(theme.typography().input());
        listeFichiers.setBackground(theme.palette().fieldBackground());
        listeFichiers.setForeground(theme.palette().bodyText());
        listeFichiers.setCellRenderer(new RenduFichierJoint());

        JScrollPane ascenseur = new JScrollPane(listeFichiers);
        int marge = Math.max(1, theme.spacing().inlineGap() / 2);
        ascenseur.setBorder(BorderFactory.createCompoundBorder(new LineBorder(theme.palette().border(), 1, true), new EmptyBorder(marge, marge, marge, marge)));
        panneau.add(ascenseur, BorderLayout.CENTER);

        JPanel actions = new JPanel();
        actions.setLayout(new BoxLayout(actions, BoxLayout.X_AXIS));
        actions.setOpaque(false);

        StyliseurBoutonTheme.appliquer(boutonAjouterFichier, theme.palette().secondaryButton(), theme, theme.typography().buttonSecondary());
        StyliseurBoutonTheme.appliquer(boutonRetirerFichier, theme.palette().dangerButton(), theme, theme.typography().buttonSecondary());
        StyliseurBoutonTheme.appliquer(boutonMonterFichier, theme.palette().secondaryButton(), theme, theme.typography().buttonSecondary());
        StyliseurBoutonTheme.appliquer(boutonDescendreFichier, theme.palette().secondaryButton(), theme, theme.typography().buttonSecondary());
        StyliseurBoutonTheme.appliquer(boutonRemplacerFichier, theme.palette().secondaryButton(), theme, theme.typography().buttonSecondary());

        actions.add(boutonAjouterFichier);
        actions.add(Box.createHorizontalStrut(theme.spacing().inlineGap()));
        actions.add(boutonRetirerFichier);
        actions.add(Box.createHorizontalStrut(theme.spacing().inlineGap()));
        actions.add(boutonMonterFichier);
        actions.add(Box.createHorizontalStrut(theme.spacing().inlineGap()));
        actions.add(boutonDescendreFichier);
        actions.add(Box.createHorizontalStrut(theme.spacing().inlineGap()));
        actions.add(boutonRemplacerFichier);

        JPanel pied = new JPanel();
        pied.setLayout(new BoxLayout(pied, BoxLayout.Y_AXIS));
        pied.setOpaque(false);
        labelAlerteFichier.setFont(theme.typography().message());
        labelAlerteFichier.setForeground(theme.palette().error());
        pied.add(labelAlerteFichier);
        pied.add(Box.createVerticalStrut(marge));
        pied.add(actions);

        panneau.add(pied, BorderLayout.SOUTH);

        boutonAjouterFichier.addActionListener(e -> ajouterFichiers());
        boutonRetirerFichier.addActionListener(e -> retirerFichier());
        boutonMonterFichier.addActionListener(e -> monterFichier());
        boutonDescendreFichier.addActionListener(e -> descendreFichier());
        boutonRemplacerFichier.addActionListener(e -> remplacerFichier());

        return panneau;
    }

    private void ajouterIntitule() {
        String saisie = montrerDialogueSaisie("Nouvelle pièce :", "");
        if (saisie == null) {
            return;
        }
        String intituleNettoye = saisie.trim();
        if (intituleNettoye.isEmpty() || fichiersParIntitule.containsKey(intituleNettoye)) {
            return;
        }
        modeleIntitules.addElement(intituleNettoye);
        fichiersParIntitule.put(intituleNettoye, new ArrayList<>());
        listeIntitules.setSelectedValue(intituleNettoye, true);
    }

    private void modifierIntitule() {
        int index = listeIntitules.getSelectedIndex();
        if (index < 0) {
            return;
        }
        String intituleActuel = modeleIntitules.get(index);
        String saisie = montrerDialogueSaisie("Modifier la pièce :", intituleActuel);
        if (saisie == null) {
            return;
        }
        String intituleNettoye = saisie.trim();
        if (intituleNettoye.isEmpty()) {
            return;
        }
        if (!intituleActuel.equals(intituleNettoye) && fichiersParIntitule.containsKey(intituleNettoye)) {
            return;
        }

        List<PieceJointe> pieces = fichiersParIntitule.remove(intituleActuel);
        fichiersParIntitule.put(intituleNettoye, pieces == null ? new ArrayList<>() : pieces);
        modeleIntitules.set(index, intituleNettoye);
        listeIntitules.setSelectedIndex(index);
    }

    private void supprimerIntitule() {
        int index = listeIntitules.getSelectedIndex();
        if (index < 0) {
            return;
        }
        String intitule = modeleIntitules.get(index);
        fichiersParIntitule.remove(intitule);
        modeleIntitules.remove(index);

        if (!modeleIntitules.isEmpty()) {
            int suivant = Math.min(index, modeleIntitules.size() - 1);
            listeIntitules.setSelectedIndex(suivant);
        } else {
            modeleFichiers.clear();
            labelIntituleSelectionne.setText("Sélectionnez un intitulé");
            mettreAJourEtatActions();
        }
    }

    private void ajouterFichiers() {
        String intitule = listeIntitules.getSelectedValue();
        if (intitule == null) {
            return;
        }

        JFileChooser selecteur = new JFileChooser();
        MemoireRepertoireExplorateur.appliquerAuSelecteur(selecteur, null);
        selecteur.setDialogTitle("Ajouter un fichier justificatif");
        selecteur.setMultiSelectionEnabled(true);
        selecteur.setAcceptAllFileFilterUsed(false);
        selecteur.setFileFilter(new FileNameExtensionFilter("Documents pris en charge", "pdf", "doc", "docx", "jpg", "jpeg", "png"));
        selecteur.getAccessibleContext().setAccessibleName("Sélecteur de pièces justificatives");
        selecteur.getAccessibleContext().setAccessibleDescription("Permet de sélectionner des fichiers PDF, Word ou image à joindre au dossier");
        int statut = selecteur.showOpenDialog(this);
        if (statut != JFileChooser.APPROVE_OPTION) {
            return;
        }
        MemoireRepertoireExplorateur.memoriserDepuisSelection(selecteur);

        List<String> formatsIgnores = new ArrayList<>();
        List<String> doublonsIgnores = new ArrayList<>();

        for (File fichier : selecteur.getSelectedFiles()) {
            TypePieceJointe type = TypePieceJointe.depuisNomFichier(fichier.getName());
            if (!type.estPrisEnCharge()) {
                formatsIgnores.add(fichier.getName());
                continue;
            }
            PieceJointe piece = PieceJointe.depuisChemin(fichier.toPath());
            if (contientUri(modeleFichiers, piece.uri())) {
                doublonsIgnores.add(piece.nomVisible());
                continue;
            }
            modeleFichiers.addElement(piece);
        }

        synchroniserFichiersSelectionnes();
        mettreAJourEtatActions();
        mettreAJourAlerteFichier();

        if (!formatsIgnores.isEmpty()) {
            afficherMessage("Formats non pris en charge ignorés : " + String.join(", ", formatsIgnores), "Ajout partiel", JOptionPane.WARNING_MESSAGE);
        }
        if (!doublonsIgnores.isEmpty()) {
            afficherMessage("Fichiers déjà attachés ignorés : " + String.join(", ", doublonsIgnores), "Ajout partiel", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void retirerFichier() {
        int index = listeFichiers.getSelectedIndex();
        if (index < 0) {
            return;
        }
        modeleFichiers.remove(index);
        if (!modeleFichiers.isEmpty()) {
            listeFichiers.setSelectedIndex(Math.min(index, modeleFichiers.size() - 1));
        }
        synchroniserFichiersSelectionnes();
        mettreAJourEtatActions();
        mettreAJourAlerteFichier();
    }

    private void monterFichier() {
        int index = listeFichiers.getSelectedIndex();
        if (index <= 0) {
            return;
        }
        PieceJointe piece = modeleFichiers.get(index);
        modeleFichiers.remove(index);
        modeleFichiers.add(index - 1, piece);
        listeFichiers.setSelectedIndex(index - 1);
        synchroniserFichiersSelectionnes();
    }

    private void descendreFichier() {
        int index = listeFichiers.getSelectedIndex();
        if (index < 0 || index >= modeleFichiers.size() - 1) {
            return;
        }
        PieceJointe piece = modeleFichiers.get(index);
        modeleFichiers.remove(index);
        modeleFichiers.add(index + 1, piece);
        listeFichiers.setSelectedIndex(index + 1);
        synchroniserFichiersSelectionnes();
    }

    private void remplacerFichier() {
        int index = listeFichiers.getSelectedIndex();
        if (index < 0) {
            return;
        }

        JFileChooser selecteur = new JFileChooser();
        MemoireRepertoireExplorateur.appliquerAuSelecteur(selecteur, null);
        selecteur.setDialogTitle("Remplacer le fichier");
        selecteur.setMultiSelectionEnabled(false);
        selecteur.setAcceptAllFileFilterUsed(false);
        selecteur.setFileFilter(new FileNameExtensionFilter("Documents pris en charge", "pdf", "doc", "docx", "jpg", "jpeg", "png"));
        selecteur.getAccessibleContext().setAccessibleName("Sélecteur de pièces justificatives");
        selecteur.getAccessibleContext().setAccessibleDescription("Permet de sélectionner des fichiers PDF, Word ou image à joindre au dossier");
        int statut = selecteur.showOpenDialog(this);
        if (statut != JFileChooser.APPROVE_OPTION) {
            return;
        }
        MemoireRepertoireExplorateur.memoriserDepuisSelection(selecteur);

        File fichier = selecteur.getSelectedFile();
        TypePieceJointe type = TypePieceJointe.depuisNomFichier(fichier.getName());
        if (!type.estPrisEnCharge()) {
            afficherMessage("Le format de ce fichier n'est pas pris en charge.", "Format non pris en charge", JOptionPane.WARNING_MESSAGE);
            return;
        }

        PieceJointe remplacement = PieceJointe.depuisChemin(fichier.toPath());
        modeleFichiers.set(index, remplacement);
        listeFichiers.setSelectedIndex(index);
        synchroniserFichiersSelectionnes();
        mettreAJourAlerteFichier();
    }

    private void chargerFichiersPourIntituleSelectionne() {
        modeleFichiers.clear();
        String intitule = listeIntitules.getSelectedValue();
        if (intitule == null) {
            labelIntituleSelectionne.setText("Sélectionnez un intitulé");
            mettreAJourEtatActions();
            mettreAJourAlerteFichier();
            return;
        }

        labelIntituleSelectionne.setText(intitule);
        List<PieceJointe> pieces = fichiersParIntitule.getOrDefault(intitule, List.of());
        for (PieceJointe piece : pieces) {
            modeleFichiers.addElement(piece);
        }
        mettreAJourEtatActions();
        mettreAJourAlerteFichier();
    }

    private void synchroniserFichiersSelectionnes() {
        String intitule = listeIntitules.getSelectedValue();
        if (intitule == null) {
            return;
        }
        List<PieceJointe> pieces = new ArrayList<>(modeleFichiers.size());
        for (int i = 0; i < modeleFichiers.size(); i++) {
            pieces.add(modeleFichiers.get(i));
        }
        fichiersParIntitule.put(intitule, pieces);
    }

    private void mettreAJourEtatActions() {
        boolean intituleSelectionne = listeIntitules.getSelectedIndex() >= 0;
        int indexFichier = listeFichiers.getSelectedIndex();
        int total = modeleFichiers.size();

        boutonAjouterFichier.setEnabled(intituleSelectionne);
        boutonRetirerFichier.setEnabled(intituleSelectionne && indexFichier >= 0);
        boutonRemplacerFichier.setEnabled(intituleSelectionne && indexFichier >= 0);
        boutonMonterFichier.setEnabled(intituleSelectionne && indexFichier > 0);
        boutonDescendreFichier.setEnabled(intituleSelectionne && indexFichier >= 0 && indexFichier < total - 1);
    }

    private void mettreAJourAlerteFichier() {
        int index = listeFichiers.getSelectedIndex();
        if (index < 0) {
            labelAlerteFichier.setText(" ");
            return;
        }

        PieceJointe piece = modeleFichiers.get(index);
        if (piece.existe()) {
            labelAlerteFichier.setText(" ");
            return;
        }
        labelAlerteFichier.setText("Fichier introuvable. Vous pouvez le retirer ou le remplacer.");
    }

    private void enregistrer() {
        enregistre = true;
        dispose();
    }

    private void annuler() {
        enregistre = false;
        dispose();
    }

    private void afficherMessage(String message, String titre, int typeMessage) {
        JOptionPane optionPane = new JOptionPane(message, typeMessage, JOptionPane.DEFAULT_OPTION);
        optionPane.getAccessibleContext().setAccessibleName(titre);
        optionPane.getAccessibleContext().setAccessibleDescription(message);
        if (iconeDialogue != null) {
            optionPane.setIcon(iconeDialogue);
        }
        JDialog dialogue = optionPane.createDialog(this, titre);
        afficherDialogueHabille(dialogue);
    }

    private void afficherDialogueHabille(JDialog dialogue) {
        if (dialogue == null) {
            return;
        }
        if (!getIconImages().isEmpty()) {
            dialogue.setIconImages(getIconImages());
        }
        HabillageDialogues.preparerDialogue(dialogue, theme);
        dialogue.pack();
        dialogue.setLocationRelativeTo(this);
        dialogue.setVisible(true);
        dialogue.dispose();
    }

    private Icon creerIconeDialogue(List<Image> icones) {
        if (icones == null || icones.isEmpty()) {
            return null;
        }
        Icon reference = UIManager.getIcon("OptionPane.questionIcon");
        int largeur = reference != null && reference.getIconWidth() > 0 ? reference.getIconWidth() : 32;
        int hauteur = reference != null && reference.getIconHeight() > 0 ? reference.getIconHeight() : largeur;
        return new ImageIcon(icones.get(0).getScaledInstance(largeur, hauteur, Image.SCALE_SMOOTH));
    }

    private String montrerDialogueSaisie(String message, String valeurInitiale) {
        JOptionPane optionPane = new JOptionPane(message, JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
        optionPane.getAccessibleContext().setAccessibleName("Saisie d'intitulé de pièce justificative");
        optionPane.getAccessibleContext().setAccessibleDescription(message);
        if (iconeDialogue != null) {
            optionPane.setIcon(iconeDialogue);
        }
        optionPane.setWantsInput(true);
        optionPane.setInitialSelectionValue(valeurInitiale == null ? "" : valeurInitiale);

        JDialog dialogue = optionPane.createDialog(this, "Pièces justificatives");
        afficherDialogueHabille(dialogue);

        Object valeur = optionPane.getValue();
        if (!(valeur instanceof Integer selection) || selection != JOptionPane.OK_OPTION) {
            return null;
        }

        Object saisie = optionPane.getInputValue();
        if (saisie == JOptionPane.UNINITIALIZED_VALUE) {
            return null;
        }
        return saisie == null ? null : saisie.toString();
    }

    public boolean estEnregistre() {
        return enregistre;
    }

    public List<String> intitules() {
        List<String> resultat = new ArrayList<>();
        for (int i = 0; i < modeleIntitules.getSize(); i++) {
            resultat.add(modeleIntitules.get(i));
        }
        return resultat;
    }

    public Map<String, List<PieceJointe>> fichiersParIntitule() {
        Map<String, List<PieceJointe>> resultat = new LinkedHashMap<>();
        for (int i = 0; i < modeleIntitules.size(); i++) {
            String intitule = modeleIntitules.get(i);
            resultat.put(intitule, copierListe(fichiersParIntitule.get(intitule)));
        }
        return resultat;
    }

    private static List<PieceJointe> copierListe(List<PieceJointe> source) {
        if (source == null || source.isEmpty()) {
            return new ArrayList<>();
        }
        return new ArrayList<>(source);
    }

    private static boolean contientUri(DefaultListModel<PieceJointe> modele, String uri) {
        for (int i = 0; i < modele.size(); i++) {
            if (Objects.equals(uri, modele.get(i).uri())) {
                return true;
            }
        }
        return false;
    }

    private final class RenduFichierJoint extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> liste, Object valeur, int index, boolean selectionne, boolean celluleFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(liste, valeur, index, selectionne, celluleFocus);
            if (!(valeur instanceof PieceJointe piece)) {
                return label;
            }

            String nom = tronquer(piece.nomVisible(), 72);
            String prefixe = switch (piece.type()) {
                case PDF -> "PDF";
                case WORD -> "DOC";
                case IMAGE -> "IMG";
                case INCONNU -> "FICHIER";
            };
            String texte = "[" + prefixe + "] " + nom;
            if (!piece.existe()) {
                texte = "[MANQUANT] " + texte;
            }
            label.setText(texte);

            if (!selectionne && !piece.existe()) {
                label.setForeground(theme.palette().error());
            } else if (!selectionne) {
                label.setForeground(theme.palette().bodyText());
            }
            return label;
        }

        private String tronquer(String texte, int longueurMax) {
            if (texte == null || texte.length() <= longueurMax) {
                return texte;
            }
            if (longueurMax <= 1) {
                return "…";
            }
            return texte.substring(0, longueurMax - 1) + "…";
        }
    }
}
