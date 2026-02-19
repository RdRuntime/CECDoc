package com.rdr.cecdoc.ui;

import com.rdr.cecdoc.ui.theme.StyliseurBoutonTheme;
import com.rdr.cecdoc.ui.theme.TokensTheme;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.io.Serial;
import java.util.List;
import java.util.Objects;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;

final class DialogueAutresDocuments extends JDialog {
    @Serial
    private static final long serialVersionUID = 1L;

    enum Choix {
        AUCUN, LETTRE_UNIVERSITE, LETTRE_ADMINISTRATION
    }

    private Choix choix;

    DialogueAutresDocuments(JFrame proprietaire, TokensTheme theme, List<Image> iconesApplication) {
        super(proprietaire, "Autres documents", true);
        Objects.requireNonNull(theme, "theme");
        if (iconesApplication != null && !iconesApplication.isEmpty()) {
            setIconImages(iconesApplication);
        }
        choix = Choix.AUCUN;

        JPanel racine = new JPanel(new BorderLayout(0, theme.spacing().blockGap()));
        racine.setBackground(theme.palette().surfaceBackground());
        racine.setBorder(new EmptyBorder(theme.spacing().cardInset(), theme.spacing().cardInset(), theme.spacing().cardInset(), theme.spacing().cardInset()));

        JLabel titre = new JLabel("Choisir un document à générer");
        titre.setFont(theme.typography().section());
        titre.setForeground(theme.palette().titleText());
        racine.add(titre, BorderLayout.NORTH);

        JPanel centre = new JPanel();
        centre.setLayout(new BoxLayout(centre, BoxLayout.Y_AXIS));
        centre.setOpaque(false);

        JButton boutonUniversite = new JButton("Lettre pour faire respecter les prénoms d'usage à l'université");
        JButton boutonAdministration = new JButton("Lettre pour faire mettre à jour mes informations auprès d'une administration");
        JButton boutonAnnuler = new JButton("Annuler");

        StyliseurBoutonTheme.appliquer(boutonUniversite, theme.palette().primaryButton(), theme, theme.typography().buttonPrimary());
        StyliseurBoutonTheme.appliquer(boutonAdministration, theme.palette().secondaryButton(), theme, theme.typography().buttonSecondary());
        StyliseurBoutonTheme.appliquer(boutonAnnuler, theme.palette().secondaryButton(), theme, theme.typography().buttonSecondary());

        boutonUniversite.setAlignmentX(LEFT_ALIGNMENT);
        boutonAdministration.setAlignmentX(LEFT_ALIGNMENT);
        boutonAnnuler.setAlignmentX(LEFT_ALIGNMENT);

        boutonUniversite.addActionListener(e -> {
            choix = Choix.LETTRE_UNIVERSITE;
            dispose();
        });
        boutonAdministration.addActionListener(e -> {
            choix = Choix.LETTRE_ADMINISTRATION;
            dispose();
        });
        boutonAnnuler.addActionListener(e -> {
            choix = Choix.AUCUN;
            dispose();
        });

        centre.add(boutonUniversite);
        centre.add(Box.createVerticalStrut(theme.spacing().inlineGap()));
        centre.add(boutonAdministration);
        centre.add(Box.createVerticalStrut(theme.spacing().blockGap()));
        centre.add(boutonAnnuler);

        racine.add(centre, BorderLayout.CENTER);
        setContentPane(racine);

        getRootPane().registerKeyboardAction(e -> {
            choix = Choix.AUCUN;
            dispose();
        }, KeyStroke.getKeyStroke("ESCAPE"), JComponent.WHEN_IN_FOCUSED_WINDOW);

        setMinimumSize(new Dimension(760, 300));
        pack();
        setLocationRelativeTo(proprietaire);
    }

    Choix choix() {
        return choix;
    }
}
