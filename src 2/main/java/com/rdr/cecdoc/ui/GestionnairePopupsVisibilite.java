package com.rdr.cecdoc.ui;

import com.rdr.cecdoc.ui.theme.ModeTheme;
import com.rdr.cecdoc.ui.theme.StyliseurBoutonTheme;
import com.rdr.cecdoc.ui.theme.TokensTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.util.Objects;
import java.util.function.Supplier;

final class GestionnairePopupsVisibilite {
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
    private final JFrame proprietaire;
    private final Supplier<TokensTheme> fournisseurTheme;
    private final ApplicationIconeDialogue applicationIconeDialogue;

    GestionnairePopupsVisibilite(JFrame proprietaire, Supplier<TokensTheme> fournisseurTheme, ApplicationIconeDialogue applicationIconeDialogue) {
        this.proprietaire = Objects.requireNonNull(proprietaire, "proprietaire");
        this.fournisseurTheme = Objects.requireNonNull(fournisseurTheme, "fournisseurTheme");
        this.applicationIconeDialogue = Objects.requireNonNull(applicationIconeDialogue, "applicationIconeDialogue");
    }

    void afficherSelonContexte(ContexteAffichagePopup contexte, ModeTheme modeActif) {
        LocalDate dateCourante = LocalDate.now();
        int jour = dateCourante.getDayOfMonth();
        int mois = dateCourante.getMonthValue();

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

    int afficherTousEnSequencePourTest() {
        int nombrePopups = 0;
        for (TypePopupVisibilite typePopupVisibilite : TypePopupVisibilite.values()) {
            afficherPopupVisibilite(typePopupVisibilite);
            nombrePopups++;
        }
        return nombrePopups;
    }

    private void afficherPopupVisibilite(TypePopupVisibilite typePopupVisibilite) {
        afficherPopupVisibilite(creerConfigurationPopup(typePopupVisibilite));
    }

    private ConfigurationPopupVisibilite creerConfigurationPopup(TypePopupVisibilite typePopupVisibilite) {
        TokensTheme theme = fournisseurTheme.get();
        Color couleurTexte = theme.palette().bodyText();
        return switch (typePopupVisibilite) {
            case TDOV ->
                    new ConfigurationPopupVisibilite("TDoV – Trans Day of Visibility", "TDoV – Trans Day of Visibility", "C'est le TDoV t'es obligé·e d'être visible bg ! Sois visible ! T'es pas visible ?? Sois visible putain !", FondPopupVisibilite.TRANS, couleurTexte, couleurTexte, "Tkt frr jsuis un max visible");
            case VISIBILITE_NON_BINAIRE ->
                    new ConfigurationPopupVisibilite("Journée internationale de visibilité des personnes non-binaires", "Journée internationale de visibilité des personnes non-binaires", "RIP la fête nationale ici c'est les blue hair and pronouns qu'on célèbre aujourd'hui !", FondPopupVisibilite.NON_BINAIRE, couleurTexte, couleurTexte, "Se teindre les cheveux");
            case VISIBILITE_LESBIENNE ->
                    new ConfigurationPopupVisibilite("Journée internationale de visibilité lesbienne", "Journée internationale de visibilité lesbienne", "Bravo les lesbiennes ! Continuez comme ça.", FondPopupVisibilite.LESBIEN, couleurTexte, couleurTexte, "Bravo à moi, tqt j'ai pas prévu d'arrêter");
            case VISIBILITE_INTERSEXE ->
                    new ConfigurationPopupVisibilite("Journée internationale de visibilité intersexe", "Journée internationale de visibilité intersexe", "La visibilité intersexe n’est pas optionnelle ! 😍\n(Même si t’as dû choisir ce thème dans les réglages)", FondPopupVisibilite.INTERSEXE, COULEUR_BLANCHE, COULEUR_BLANCHE, "Ok pétasse");
            case LUTTE_LGBTQIPHOBIES ->
                    new ConfigurationPopupVisibilite("Journée internationale contre l'homophobie, la transphobie, et la biphobie", "Journée internationale contre l'homophobie, la transphobie, et la biphobie", "Et la biphobie, on oublie pas la biphobie ! Ces trè grave...", FondPopupVisibilite.RAINBOW, COULEUR_BLANCHE, COULEUR_BLANCHE, "Jsuis pas biphobe jte jure");
            case COMING_OUT_DAY ->
                    new ConfigurationPopupVisibilite("Coming out day", "Coming out day", "N'oublie pas de dire à absolument tout le monde que tu es elgéteubé·e aujourd'hui ! Ces trè importan.", FondPopupVisibilite.RAINBOW, COULEUR_BLANCHE, COULEUR_BLANCHE, "Alexa, joue du Diana Ross");
            case VISIBILITE_PANSEXUELLE ->
                    new ConfigurationPopupVisibilite("Journée internationale de la visibilité pansexuelle", "Journée internationale de la visibilité pansexuelle", "C'est la journée spéciale confused slutbag !", FondPopupVisibilite.PANSEXUEL, couleurTexte, couleurTexte, "Slut-shame les pans");
            case JOURNEE_BISEXUALITE ->
                    new ConfigurationPopupVisibilite("Journée de la bisexualité", "Journée de la bisexualité", "À voile et à vapeur, iels bouffent à tous les rateliers !", FondPopupVisibilite.BISEXUEL, COULEUR_BLANCHE, COULEUR_BLANCHE, "La biphobie est une vraie oppression, source: tkt frr");
        };
    }

    private void afficherPopupVisibilite(ConfigurationPopupVisibilite configurationPopup) {
        TokensTheme theme = fournisseurTheme.get();
        JDialog dialogue = new JDialog(proprietaire, configurationPopup.titreFenetre(), true);
        applicationIconeDialogue.appliquer(dialogue);

        PanneauFondPopup panneauFond = new PanneauFondPopup(configurationPopup.fond(), fournisseurTheme);
        panneauFond.setLayout(new BorderLayout(0, theme.spacing().blockGap()));
        panneauFond.setBorder(new EmptyBorder(theme.spacing().cardInset(), theme.spacing().cardInset(), theme.spacing().cardInset(), theme.spacing().cardInset()));

        JLabel labelTitrePopup = new JLabel(configurationPopup.titreAffiche());
        labelTitrePopup.setFont(theme.typography().title().deriveFont(Font.BOLD, theme.typography().title().getSize2D() + 6f));
        labelTitrePopup.setForeground(configurationPopup.couleurTitre());
        labelTitrePopup.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel labelMessagePopup = new JLabel("<html><div style='text-align:center;width:720px;'>" + echapperHtml(configurationPopup.message()).replace("\n", "<br/>") + "</div></html>");
        labelMessagePopup.setFont(theme.typography().section());
        labelMessagePopup.setForeground(configurationPopup.couleurMessage());
        labelMessagePopup.setHorizontalAlignment(SwingConstants.CENTER);

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
        dialogue.setLocationRelativeTo(proprietaire);
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

    enum ContexteAffichagePopup {
        DEMARRAGE, PRONOM_NEUTRE_BASCULE, THEME_MODIFIE_DANS_CONFIG
    }

    private enum FondPopupVisibilite {
        TRANS, NON_BINAIRE, LESBIEN, INTERSEXE, RAINBOW, PANSEXUEL, BISEXUEL
    }

    private enum TypePopupVisibilite {
        TDOV, VISIBILITE_NON_BINAIRE, VISIBILITE_LESBIENNE, VISIBILITE_INTERSEXE, LUTTE_LGBTQIPHOBIES, COMING_OUT_DAY, VISIBILITE_PANSEXUELLE, JOURNEE_BISEXUALITE
    }

    interface ApplicationIconeDialogue {
        void appliquer(JDialog dialogue);
    }

    private record ConfigurationPopupVisibilite(String titreFenetre, String titreAffiche, String message,
                                                FondPopupVisibilite fond, Color couleurTitre, Color couleurMessage,
                                                String libelleBouton) {
    }

    private static final class PanneauFondPopup extends JPanel {
        private final FondPopupVisibilite fond;
        private final transient Supplier<TokensTheme> fournisseurTheme;

        private PanneauFondPopup(FondPopupVisibilite fond, Supplier<TokensTheme> fournisseurTheme) {
            this.fond = fond;
            this.fournisseurTheme = fournisseurTheme;
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
                    TokensTheme theme = fournisseurTheme.get();
                    return new GradientPaint(0, 0, theme.palette().backgroundTop(), 0, hauteurEffective, theme.palette().backgroundBottom());
                }
            }
        }
    }
}
