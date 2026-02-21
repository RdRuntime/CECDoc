package com.rdr.cecdoc.ui;

import com.rdr.cecdoc.ui.theme.ModeTheme;
import com.rdr.cecdoc.ui.theme.StyliseurBoutonTheme;
import com.rdr.cecdoc.ui.theme.TokensTheme;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.Serial;
import java.util.List;
import java.util.Objects;
import java.util.Random;

final class DialogueAutresDocuments extends JDialog {
    @Serial
    private static final long serialVersionUID = 1L;
    private final transient TokensTheme theme;
    private Choix choix;

    DialogueAutresDocuments(JFrame proprietaire, TokensTheme theme, List<Image> iconesApplication) {
        super(proprietaire, "Autres documents", true);
        this.theme = Objects.requireNonNull(theme, "theme");
        if (iconesApplication != null && !iconesApplication.isEmpty()) {
            setIconImages(iconesApplication);
        }
        choix = Choix.AUCUN;

        JPanel racine = new PanneauFondThematique(theme);
        racine.setLayout(new BorderLayout(0, theme.spacing().blockGap()));
        racine.setBorder(new EmptyBorder(theme.spacing().cardInset(), theme.spacing().cardInset(), theme.spacing().cardInset(), theme.spacing().cardInset()));

        JPanel carte = new JPanel(new BorderLayout(0, theme.spacing().blockGap()));
        carte.setBackground(theme.palette().cardBackground());
        carte.setBorder(new CompoundBorder(new LineBorder(theme.palette().border(), 1, true), new EmptyBorder(theme.spacing().cardInset(), theme.spacing().cardInset(), theme.spacing().cardInset(), theme.spacing().cardInset())));

        JPanel entete = new JPanel();
        entete.setLayout(new BoxLayout(entete, BoxLayout.Y_AXIS));
        entete.setOpaque(false);

        JLabel titre = new JLabel("Autres documents");
        titre.setFont(theme.typography().section());
        titre.setForeground(theme.palette().titleText());
        JLabel sousTitre = new JLabel("Documents divers pour la transition administrative");
        sousTitre.setFont(theme.typography().helper());
        sousTitre.setForeground(theme.palette().mutedText());
        JLabel aide = new JLabel("Sélectionnez un document à générer");
        aide.setFont(theme.typography().helper());
        aide.setForeground(theme.palette().bodyText());
        entete.add(titre);
        int max = Math.max(2, theme.spacing().inlineGap() / 2);
        entete.add(Box.createVerticalStrut(max));
        entete.add(sousTitre);
        entete.add(Box.createVerticalStrut(max));
        entete.add(aide);
        carte.add(entete, BorderLayout.NORTH);

        List<OptionDocument> options = List.of(new OptionDocument(Choix.LETTRE_UNIVERSITE, "Lettre université", "Faire respecter les prénoms d’usage à l’université", KeyEvent.VK_U, true, true), new OptionDocument(Choix.LETTRE_ADMINISTRATION, "Lettre administration", "Mettre à jour les informations auprès d’une administration", KeyEvent.VK_M, false, true), new OptionDocument(Choix.DOSSIER_CHANGEMENT_PRENOMS, "Demande de changement de prénoms", "Accéder à la page Service-Public officielle", KeyEvent.VK_D, false, true), new OptionDocument(Choix.RECOURS_REFUS_CHANGEMENT_PRENOM, "Recours refus changement de prénom", "Générer un recours en cas de refus en mairie", KeyEvent.VK_R, false, true), new OptionDocument(Choix.RECOURS_REFUS_CHANGEMENT_SEXE, "Recours refus changement de sexe", "Générer un recours en appel au tribunal", KeyEvent.VK_S, false, true), new OptionDocument(Choix.LETTRE_RELANCE_TRIBUNAL, "Relance tribunal judiciaire", "Générer une lettre de relance et suivi de dossier", KeyEvent.VK_T, false, true), new OptionDocument(Choix.LETTRE_RELANCE_MAIRIE_PRENOM, "Relance mairie changement de prénoms", "Générer une relance et accusé de réception", KeyEvent.VK_Y, false, true), new OptionDocument(Choix.LETTRE_MISE_A_JOUR_ACTES_LIES, "Mise à jour des actes liés", "Générer une demande de mise à jour des actes d’état civil liés", KeyEvent.VK_L, false, true), new OptionDocument(Choix.LETTRE_RGPD_MINIMISATION, "Lettre RGPD / minimisation", "Exercer les droits de protection des données", KeyEvent.VK_G, false, true));

        JPanel listeTuiles = new JPanel(new GridBagLayout());
        listeTuiles.setOpaque(false);
        GridBagConstraints contraintes = new GridBagConstraints();
        contraintes.gridx = 0;
        contraintes.weightx = 1;
        contraintes.fill = GridBagConstraints.HORIZONTAL;
        contraintes.insets = new Insets(0, 0, Math.max(8, theme.spacing().inlineGap()), 0);
        JButton boutonParDefaut = null;
        for (int index = 0; index < options.size(); index++) {
            OptionDocument option = options.get(index);
            JPanel tuile = creherTuileOption(theme, option);
            contraintes.gridy = index;
            listeTuiles.add(tuile, contraintes);
            if (boutonParDefaut == null && option.disponible()) {
                boutonParDefaut = boutonActionParTuile(tuile);
            }
        }
        contraintes.gridy = options.size();
        contraintes.weighty = 1;
        contraintes.fill = GridBagConstraints.VERTICAL;
        contraintes.insets = new Insets(0, 0, 0, 0);
        listeTuiles.add(Box.createVerticalGlue(), contraintes);

        JScrollPane ascenseur = new JScrollPane(listeTuiles);
        ascenseur.setOpaque(false);
        ascenseur.getViewport().setOpaque(false);
        ascenseur.setBorder(new EmptyBorder(0, 0, 0, 0));
        ascenseur.getVerticalScrollBar().setUnitIncrement(16);
        carte.add(ascenseur, BorderLayout.CENTER);

        JButton boutonAnnuler = creherBouton(theme, "Fermer", KeyEvent.VK_F, "Fermer", "Ferme la fenêtre sans ouvrir de formulaire", false);
        boutonAnnuler.addActionListener(e -> {
            choix = Choix.AUCUN;
            dispose();
        });

        JPanel actions = new JPanel();
        actions.setLayout(new BoxLayout(actions, BoxLayout.X_AXIS));
        actions.setOpaque(false);
        actions.add(Box.createHorizontalGlue());
        actions.add(boutonAnnuler);
        carte.add(actions, BorderLayout.SOUTH);

        racine.add(carte, BorderLayout.CENTER);
        setContentPane(racine);

        getRootPane().registerKeyboardAction(e -> {
            choix = Choix.AUCUN;
            dispose();
        }, KeyStroke.getKeyStroke("ESCAPE"), JComponent.WHEN_IN_FOCUSED_WINDOW);

        if (boutonParDefaut != null) {
            getRootPane().setDefaultButton(boutonParDefaut);
        }
        getAccessibleContext().setAccessibleName("Choix de document complémentaire");
        getAccessibleContext().setAccessibleDescription("Permet de choisir un document complémentaire disponible ou en préparation");
        configurerTailleInitiale();
        setLocationRelativeTo(proprietaire);
    }

    Choix choix() {
        return choix;
    }

    private JPanel creherTuileOption(TokensTheme theme, OptionDocument option) {
        JPanel tuile = new JPanel(new BorderLayout(Math.max(8, theme.spacing().inlineGap()), 0));
        tuile.setOpaque(true);
        tuile.setBackground(melanger(theme.palette().cardBackground(), theme.palette().surfaceBackground(), 0.82f));
        tuile.setBorder(new CompoundBorder(new LineBorder(theme.palette().border(), 1, true), new EmptyBorder(theme.spacing().inlineGap(), theme.spacing().inlineGap(), theme.spacing().inlineGap(), theme.spacing().inlineGap())));

        JLabel titre = new JLabel(option.titre());
        titre.setFont(theme.typography().label().deriveFont(Font.BOLD));
        titre.setForeground(theme.palette().titleText());
        JLabel description = new JLabel(option.description());
        description.setFont(theme.typography().helper());
        description.setForeground(option.disponible() ? theme.palette().mutedText() : theme.palette().error());

        JPanel blocTexte = new JPanel();
        blocTexte.setOpaque(false);
        blocTexte.setLayout(new BoxLayout(blocTexte, BoxLayout.Y_AXIS));
        blocTexte.add(titre);
        blocTexte.add(Box.createVerticalStrut(Math.max(2, theme.spacing().inlineGap() / 3)));
        blocTexte.add(description);
        tuile.add(blocTexte, BorderLayout.CENTER);

        String texteAction = option.disponible() ? "Ouvrir" : "Bientôt";
        String nomAccessible = option.disponible() ? option.titre() : option.titre() + " indisponible";
        JButton bouton = creherBouton(theme, texteAction, option.mnemonic(), nomAccessible, option.description(), option.primaire());
        bouton.setPreferredSize(new Dimension(136, bouton.getPreferredSize().height));
        bouton.addActionListener(e -> {
            if (!option.disponible()) {
                afficherInformationIndisponible(option.titre());
                return;
            }
            choix = option.choix();
            dispose();
        });
        tuile.add(bouton, BorderLayout.EAST);
        tuile.putClientProperty("actionButton", bouton);
        return tuile;
    }

    private JButton boutonActionParTuile(JPanel tuile) {
        Object valeur = tuile.getClientProperty("actionButton");
        if (valeur instanceof JButton bouton) {
            return bouton;
        }
        return null;
    }

    private void afficherInformationIndisponible(String fonctionnalite) {
        String message = "La fonctionnalité \"" + fonctionnalite + "\" est en préparation.";
        JOptionPane optionPane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION);
        optionPane.getAccessibleContext().setAccessibleName("Fonction en préparation");
        optionPane.getAccessibleContext().setAccessibleDescription(message);
        JDialog dialogue = optionPane.createDialog(this, "Fonction en préparation");
        HabillageDialogues.preparerDialogue(dialogue, theme);
        dialogue.pack();
        dialogue.setLocationRelativeTo(this);
        dialogue.setVisible(true);
        dialogue.dispose();
    }

    private JButton creherBouton(TokensTheme theme, String texte, int mnemonic, String nomAccessible, String descriptionAccessible, boolean primaire) {
        JButton bouton = new JButton(texte);
        bouton.setMnemonic(mnemonic);
        bouton.setAlignmentX(LEFT_ALIGNMENT);
        bouton.getAccessibleContext().setAccessibleName(nomAccessible);
        bouton.getAccessibleContext().setAccessibleDescription(descriptionAccessible);
        bouton.setToolTipText(descriptionAccessible);
        StyliseurBoutonTheme.appliquer(bouton, primaire ? theme.palette().primaryButton() : theme.palette().secondaryButton(), theme, primaire ? theme.typography().buttonPrimary() : theme.typography().buttonSecondary());
        return bouton;
    }

    private Color melanger(Color premier, Color second, float ratioPremier) {
        float ratio = Math.max(0f, Math.min(1f, ratioPremier));
        float inverse = 1f - ratio;
        int rouge = Math.round(premier.getRed() * ratio + second.getRed() * inverse);
        int vert = Math.round(premier.getGreen() * ratio + second.getGreen() * inverse);
        int bleu = Math.round(premier.getBlue() * ratio + second.getBlue() * inverse);
        int alpha = Math.round(premier.getAlpha() * ratio + second.getAlpha() * inverse);
        return new Color(rouge, vert, bleu, alpha);
    }

    private void configurerTailleInitiale() {
        GraphicsConfiguration configuration = getGraphicsConfiguration();
        Rectangle zoneEcran = configuration == null ? new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()) : configuration.getBounds();
        Insets marges = configuration == null ? new Insets(0, 0, 0, 0) : Toolkit.getDefaultToolkit().getScreenInsets(configuration);
        int largeurMax = Math.max(920, zoneEcran.width - marges.left - marges.right - 64);
        int hauteurMax = Math.max(865, zoneEcran.height - marges.top - marges.bottom - 64);
        int largeur = Math.min(700, largeurMax);
        int hauteur = Math.min(865, hauteurMax);
        setSize(new Dimension(largeur, hauteur));
        setMinimumSize(new Dimension(Math.min(900, largeur), Math.min(760, hauteur)));
    }

    enum Choix {
        AUCUN, LETTRE_UNIVERSITE, LETTRE_ADMINISTRATION, DOSSIER_CHANGEMENT_PRENOMS, RECOURS_REFUS_CHANGEMENT_PRENOM, RECOURS_REFUS_CHANGEMENT_SEXE, LETTRE_RELANCE_TRIBUNAL, LETTRE_RELANCE_MAIRIE_PRENOM, LETTRE_MISE_A_JOUR_ACTES_LIES, LETTRE_RGPD_MINIMISATION
    }

    private record OptionDocument(Choix choix, String titre, String description, int mnemonic, boolean primaire,
                                  boolean disponible) {
    }

    private static final class PanneauFondThematique extends JPanel {
        @Serial
        private static final long serialVersionUID = 1L;

        private final transient TokensTheme theme;

        private PanneauFondThematique(TokensTheme theme) {
            this.theme = theme;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            int hauteur = Math.max(1, getHeight());
            g2.setPaint(new GradientPaint(0, 0, theme.palette().backgroundTop(), 0, hauteur, theme.palette().backgroundBottom()));
            g2.fillRect(0, 0, getWidth(), getHeight());
            Color halo = theme.palette().focus();
            Color haloTransparent = new Color(halo.getRed(), halo.getGreen(), halo.getBlue(), 36);
            int diametre = Math.max(220, Math.min(getWidth(), getHeight()) / 2);
            g2.setColor(haloTransparent);
            g2.fillOval(-diametre / 3, -diametre / 4, diametre, diametre);
            g2.fillOval(getWidth() - (diametre * 2 / 3), getHeight() - (diametre * 2 / 3), diametre, diametre);
            if (theme.mode() == ModeTheme.COMMUNISTE) {
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                Color symboleJaune = Color.decode("#FFD700");
                Random aleatoire = new Random(0xCEC0D0CL + (long) getWidth() * 131 + (long) getHeight() * 17);
                int largeur = Math.max(1, getWidth());
                int hauteurTotale = Math.max(1, getHeight());
                int nombreSymbolesCibles = Math.max(360, (largeur * hauteurTotale) / 1625);
                int colonnes = Math.max(1, (int) Math.ceil(Math.sqrt((double) nombreSymbolesCibles * largeur / hauteurTotale)));
                int lignes = Math.max(1, (int) Math.ceil((double) nombreSymbolesCibles / colonnes));
                double pasX = (double) largeur / colonnes;
                double pasY = (double) hauteurTotale / lignes;
                float taille = (float) Math.max(62.0d, Math.min(94.0d, Math.min(pasX * 0.95d, pasY * 0.95d)));
                Font police = theme.typography().helper().deriveFont(Font.BOLD, taille);
                FontMetrics mesures = g2.getFontMetrics(police);
                int largeurSymbole = Math.max(1, mesures.stringWidth("☭"));
                int hauteurSymbole = Math.max(1, mesures.getAscent() + mesures.getDescent());
                if (largeurSymbole > pasX || hauteurSymbole > pasY) {
                    double ratio = Math.min(pasX / largeurSymbole, pasY / hauteurSymbole) * 0.92d;
                    float tailleAjustee = (float) Math.max(22.0d, taille * Math.max(0.1d, ratio));
                    police = theme.typography().helper().deriveFont(Font.BOLD, tailleAjustee);
                    mesures = g2.getFontMetrics(police);
                    largeurSymbole = Math.max(1, mesures.stringWidth("☭"));
                    hauteurSymbole = Math.max(1, mesures.getAscent() + mesures.getDescent());
                }
                g2.setFont(police);
                for (int ligne = 0; ligne < lignes; ligne++) {
                    for (int colonne = 0; colonne < colonnes; colonne++) {
                        int xCell = (int) Math.round(colonne * pasX);
                        int yCell = (int) Math.round(ligne * pasY);
                        int xCellFin = (int) Math.round((colonne + 1) * pasX);
                        int yCellFin = (int) Math.round((ligne + 1) * pasY);
                        int largeurCellule = Math.max(1, xCellFin - xCell);
                        int hauteurCellule = Math.max(1, yCellFin - yCell);
                        int margeX = Math.max(0, largeurCellule - largeurSymbole);
                        int margeY = Math.max(0, hauteurCellule - hauteurSymbole);
                        int jitterX = margeX == 0 ? 0 : aleatoire.nextInt(margeX + 1);
                        int jitterY = margeY == 0 ? 0 : aleatoire.nextInt(margeY + 1);
                        int x = xCell + jitterX;
                        int yTop = yCell + jitterY;
                        if (x + largeurSymbole > largeur) {
                            x = Math.max(0, largeur - largeurSymbole);
                        }
                        if (yTop + hauteurSymbole > hauteurTotale) {
                            yTop = Math.max(0, hauteurTotale - hauteurSymbole);
                        }
                        int alpha = 80 + aleatoire.nextInt(70);
                        int y = yTop + mesures.getAscent();
                        g2.setColor(new Color(symboleJaune.getRed(), symboleJaune.getGreen(), symboleJaune.getBlue(), alpha));
                        g2.drawString("☭", x, y);
                    }
                }
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
