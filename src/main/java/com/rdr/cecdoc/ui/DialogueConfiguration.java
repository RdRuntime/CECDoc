package com.rdr.cecdoc.ui;

import com.rdr.cecdoc.ui.theme.StyliseurBoutonTheme;
import com.rdr.cecdoc.ui.theme.TokensTheme;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.io.Serial;
import java.nio.file.Path;
import java.util.Objects;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public final class DialogueConfiguration extends JDialog {
    @Serial
    private static final long serialVersionUID = 1L;

    private final transient TokensTheme theme;
    private final JCheckBox checkboxConfirmerQuitter;
    private final JCheckBox checkboxMemoriserSaisie;
    private final JTextField champDossierSortie;
    private final JButton boutonChoisirDossier;
    private final JButton boutonViderDossier;

    private boolean enregistre;

    public DialogueConfiguration(JFrame proprietaire, TokensTheme theme, boolean confirmerQuitterAvecDonnees, boolean memoriserDonneesSaisies, String dossierSortieParDefaut) {
        super(proprietaire, "Configuration", true);
        this.theme = Objects.requireNonNull(theme, "theme");

        if (proprietaire != null && !proprietaire.getIconImages().isEmpty()) {
            setIconImages(proprietaire.getIconImages());
        }

        checkboxConfirmerQuitter = new JCheckBox("Demander une confirmation avant de quitter");
        checkboxConfirmerQuitter.setSelected(confirmerQuitterAvecDonnees);

        checkboxMemoriserSaisie = new JCheckBox("Se souvenir des informations saisies à la prochaine ouverture (fichier de configuration)");
        checkboxMemoriserSaisie.setSelected(memoriserDonneesSaisies);

        champDossierSortie = new JTextField();
        GestionAnnulationTexte.activer(champDossierSortie);
        champDossierSortie.setText(dossierSortieParDefaut == null ? "" : dossierSortieParDefaut.trim());
        boutonChoisirDossier = new JButton("Parcourir…");
        boutonViderDossier = new JButton("Aucun");

        construireInterface();

        pack();
        setMinimumSize(new Dimension(680, getHeight()));
        setLocationRelativeTo(proprietaire);
    }

    private void construireInterface() {
        JPanel racine = new JPanel(new BorderLayout(0, theme.spacing().blockGap()));
        racine.setBorder(new EmptyBorder(theme.spacing().cardInset(), theme.spacing().cardInset(), theme.spacing().cardInset(), theme.spacing().cardInset()));
        racine.setBackground(theme.palette().surfaceBackground());

        JLabel titre = new JLabel("Paramètres de l'application");
        titre.setFont(theme.typography().section());
        titre.setForeground(theme.palette().titleText());
        racine.add(titre, BorderLayout.NORTH);

        JPanel centre = new JPanel();
        centre.setLayout(new BoxLayout(centre, BoxLayout.Y_AXIS));
        centre.setOpaque(false);

        styliserCheckbox(checkboxConfirmerQuitter);
        styliserCheckbox(checkboxMemoriserSaisie);

        checkboxConfirmerQuitter.setMnemonic(KeyEvent.VK_Q);
        checkboxMemoriserSaisie.setMnemonic(KeyEvent.VK_S);

        centre.add(checkboxConfirmerQuitter);
        centre.add(Box.createVerticalStrut(theme.spacing().inlineGap()));
        centre.add(checkboxMemoriserSaisie);
        centre.add(Box.createVerticalStrut(theme.spacing().inlineGap()));
        int max = Math.max(2, theme.spacing().inlineGap() / 2);

        JPanel panneauDossier = new JPanel();
        panneauDossier.setLayout(new BoxLayout(panneauDossier, BoxLayout.Y_AXIS));
        panneauDossier.setOpaque(false);
        panneauDossier.setAlignmentX(LEFT_ALIGNMENT);

        JLabel labelDossier = new JLabel("Dossier de sortie par défaut");
        labelDossier.setFont(theme.typography().label());
        labelDossier.setForeground(theme.palette().bodyText());
        labelDossier.setLabelFor(champDossierSortie);
        labelDossier.setDisplayedMnemonic(KeyEvent.VK_D);
        labelDossier.setAlignmentX(LEFT_ALIGNMENT);

        champDossierSortie.setFont(theme.typography().input());
        champDossierSortie.setBackground(theme.palette().fieldBackground());
        champDossierSortie.setForeground(theme.palette().bodyText());
        champDossierSortie.setEditable(false);
        champDossierSortie.setMaximumSize(new Dimension(Integer.MAX_VALUE, champDossierSortie.getPreferredSize().height));
        champDossierSortie.setAlignmentX(LEFT_ALIGNMENT);
        champDossierSortie.setToolTipText("Utiliser Parcourir… pour choisir un dossier");

        boutonChoisirDossier.setMnemonic(KeyEvent.VK_O);
        StyliseurBoutonTheme.appliquer(boutonChoisirDossier, theme.palette().secondaryButton(), theme, theme.typography().buttonSecondary());
        boutonChoisirDossier.setAlignmentX(LEFT_ALIGNMENT);
        boutonChoisirDossier.addActionListener(e -> choisirDossierSortie());
        boutonViderDossier.setMnemonic(KeyEvent.VK_U);
        StyliseurBoutonTheme.appliquer(boutonViderDossier, theme.palette().secondaryButton(), theme, theme.typography().buttonSecondary());
        boutonViderDossier.setAlignmentX(LEFT_ALIGNMENT);
        boutonViderDossier.addActionListener(e -> champDossierSortie.setText(""));

        JPanel panneauActionsDossier = new JPanel();
        panneauActionsDossier.setLayout(new BoxLayout(panneauActionsDossier, BoxLayout.X_AXIS));
        panneauActionsDossier.setOpaque(false);
        panneauActionsDossier.setAlignmentX(LEFT_ALIGNMENT);
        panneauActionsDossier.add(boutonChoisirDossier);
        panneauActionsDossier.add(Box.createHorizontalStrut(theme.spacing().inlineGap()));
        panneauActionsDossier.add(boutonViderDossier);

        panneauDossier.add(labelDossier);
        panneauDossier.add(Box.createVerticalStrut(max));
        panneauDossier.add(champDossierSortie);
        panneauDossier.add(Box.createVerticalStrut(max));
        panneauDossier.add(panneauActionsDossier);
        centre.add(panneauDossier);
        centre.add(Box.createVerticalStrut(theme.spacing().inlineGap()));

        racine.add(centre, BorderLayout.CENTER);

        JPanel panneauActions = new JPanel();
        panneauActions.setLayout(new BoxLayout(panneauActions, BoxLayout.X_AXIS));
        panneauActions.setOpaque(false);

        JButton boutonAnnuler = new JButton("Annuler");
        JButton boutonEnregistrer = new JButton("Enregistrer");

        boutonAnnuler.setMnemonic(KeyEvent.VK_A);
        boutonEnregistrer.setMnemonic(KeyEvent.VK_E);

        StyliseurBoutonTheme.appliquer(boutonAnnuler, theme.palette().secondaryButton(), theme, theme.typography().buttonSecondary());
        StyliseurBoutonTheme.appliquer(boutonEnregistrer, theme.palette().primaryButton(), theme, theme.typography().buttonPrimary());

        boutonAnnuler.addActionListener(e -> {
            enregistre = false;
            dispose();
        });
        boutonEnregistrer.addActionListener(e -> {
            enregistre = true;
            dispose();
        });

        panneauActions.add(Box.createHorizontalGlue());
        panneauActions.add(boutonAnnuler);
        panneauActions.add(Box.createHorizontalStrut(theme.spacing().inlineGap()));
        panneauActions.add(boutonEnregistrer);

        racine.add(panneauActions, BorderLayout.SOUTH);

        setContentPane(racine);

        getRootPane().setDefaultButton(boutonEnregistrer);
        getRootPane().registerKeyboardAction(e -> {
            enregistre = false;
            dispose();
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);

        getAccessibleContext().setAccessibleName("Configuration de l'application");
        getAccessibleContext().setAccessibleDescription("Fenêtre de configuration des préférences de CECDoc");
        checkboxConfirmerQuitter.getAccessibleContext().setAccessibleName("Confirmation avant fermeture");
        checkboxConfirmerQuitter.getAccessibleContext().setAccessibleDescription("Demande une confirmation avant de quitter l'application");
        checkboxMemoriserSaisie.getAccessibleContext().setAccessibleName("Mémorisation des informations saisies");
        checkboxMemoriserSaisie.getAccessibleContext().setAccessibleDescription("Conserve les informations saisies pour la prochaine ouverture");
        champDossierSortie.getAccessibleContext().setAccessibleName("Dossier de sortie par défaut");
        champDossierSortie.getAccessibleContext().setAccessibleDescription("Chemin du dossier proposé par défaut pour enregistrer les documents générés");
        boutonChoisirDossier.getAccessibleContext().setAccessibleName("Choisir le dossier de sortie");
        boutonViderDossier.getAccessibleContext().setAccessibleName("Retirer le dossier de sortie par défaut");
        boutonAnnuler.getAccessibleContext().setAccessibleName("Annuler les modifications");
        boutonEnregistrer.getAccessibleContext().setAccessibleName("Enregistrer les modifications");

        SwingUtilities.invokeLater(checkboxConfirmerQuitter::requestFocusInWindow);
    }

    private void choisirDossierSortie() {
        JFileChooser selecteur = new JFileChooser();
        selecteur.setDialogTitle("Choisir le dossier de sortie par défaut");
        selecteur.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        selecteur.setMultiSelectionEnabled(false);
        selecteur.setAcceptAllFileFilterUsed(false);

        Path repertoireSaisi = null;
        String valeur = champDossierSortie.getText() == null ? "" : champDossierSortie.getText().trim();
        if (!valeur.isEmpty()) {
            try {
                Path dossier = Path.of(valeur).toAbsolutePath().normalize();
                if (dossier.toFile().isDirectory()) {
                    repertoireSaisi = dossier;
                    selecteur.setSelectedFile(dossier.toFile());
                }
            } catch (RuntimeException ex) {
                champDossierSortie.requestFocusInWindow();
            }
        }
        MemoireRepertoireExplorateur.appliquerAuSelecteur(selecteur, repertoireSaisi);

        int choix = selecteur.showOpenDialog(this);
        if (choix == JFileChooser.APPROVE_OPTION && selecteur.getSelectedFile() != null) {
            MemoireRepertoireExplorateur.memoriserDepuisSelection(selecteur);
            champDossierSortie.setText(selecteur.getSelectedFile().getAbsolutePath());
        }
    }

    private void styliserCheckbox(JCheckBox checkbox) {
        checkbox.setOpaque(false);
        checkbox.setFont(theme.typography().input());
        checkbox.setForeground(theme.palette().bodyText());
    }

    public boolean estEnregistre() {
        return enregistre;
    }

    public boolean confirmerQuitterAvecDonnees() {
        return checkboxConfirmerQuitter.isSelected();
    }

    public boolean memoriserDonneesSaisies() {
        return checkboxMemoriserSaisie.isSelected();
    }

    public String dossierSortieParDefaut() {
        return champDossierSortie.getText() == null ? "" : champDossierSortie.getText().trim();
    }
}
