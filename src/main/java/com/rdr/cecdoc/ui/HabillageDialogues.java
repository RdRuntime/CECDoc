package com.rdr.cecdoc.ui;

import com.rdr.cecdoc.ui.theme.StyliseurBoutonTheme;
import com.rdr.cecdoc.ui.theme.TokensTheme;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.util.Locale;
import java.util.Objects;

final class HabillageDialogues {
    private HabillageDialogues() {
    }

    static void preparerDialogue(JDialog dialogue, TokensTheme theme) {
        if (dialogue == null || theme == null) {
            return;
        }
        if (dialogue.getContentPane() instanceof JComponent racine) {
            racine.setOpaque(true);
            racine.setBackground(theme.palette().surfaceBackground());
            int marge = theme.spacing().cardInset();
            racine.setBorder(new EmptyBorder(marge, marge, marge, marge));
        }
        appliquerRecursivement(dialogue.getContentPane(), theme);
    }

    private static void appliquerRecursivement(Component composant, TokensTheme theme) {
        if (composant == null) {
            return;
        }

        if (composant instanceof JOptionPane optionPane) {
            optionPane.setBackground(theme.palette().surfaceBackground());
            optionPane.setForeground(theme.palette().bodyText());
            optionPane.setBorder(new EmptyBorder(theme.spacing().blockGap(), theme.spacing().blockGap(), theme.spacing().blockGap(), theme.spacing().blockGap()));
            optionPane.setFont(theme.typography().input());
        } else if (composant instanceof JLabel label) {
            label.setFont(theme.typography().label());
            label.setForeground(theme.palette().bodyText());
        } else if (composant instanceof JButton bouton) {
            styliserBouton(bouton, theme);
        } else if (composant instanceof JTextComponent champTexte) {
            champTexte.setFont(theme.typography().input());
            champTexte.setBackground(theme.palette().fieldBackground());
            champTexte.setForeground(theme.palette().bodyText());
            champTexte.setCaretColor(theme.palette().focus());
            champTexte.setBorder(new CompoundBorder(new LineBorder(theme.palette().border(), 1, true), new EmptyBorder(theme.spacing().fieldInsetY(), theme.spacing().fieldInsetX(), theme.spacing().fieldInsetY(), theme.spacing().fieldInsetX())));
        } else if (composant instanceof JComboBox<?> comboBox) {
            comboBox.setFont(theme.typography().input());
            comboBox.setBackground(theme.palette().fieldBackground());
            comboBox.setForeground(theme.palette().bodyText());
        } else if (composant instanceof JList<?> liste) {
            liste.setFont(theme.typography().input());
            liste.setBackground(theme.palette().fieldBackground());
            liste.setForeground(theme.palette().bodyText());
            liste.setSelectionBackground(theme.palette().focus());
            liste.setSelectionForeground(theme.palette().inverseText());
        } else if (composant instanceof JScrollPane ascenseur) {
            int marge = Math.max(1, theme.spacing().inlineGap() / 4);
            ascenseur.setBorder(new CompoundBorder(new LineBorder(theme.palette().border(), 1, true), new EmptyBorder(marge, marge, marge, marge)));
            if (ascenseur.getViewport() != null) {
                ascenseur.getViewport().setBackground(theme.palette().fieldBackground());
            }
        } else if (composant instanceof JPanel panneau) {
            panneau.setOpaque(true);
            panneau.setBackground(theme.palette().surfaceBackground());
        } else if (composant instanceof JViewport viewport) {
            viewport.setOpaque(true);
            viewport.setBackground(theme.palette().fieldBackground());
        }

        if (composant instanceof Container conteneur) {
            for (Component enfant : conteneur.getComponents()) {
                appliquerRecursivement(enfant, theme);
            }
        }
    }

    private static void styliserBouton(JButton bouton, TokensTheme theme) {
        String texte = Objects.toString(bouton.getText(), "").toLowerCase(Locale.ROOT).trim();
        if (texte.equals("oui") || texte.equals("ok") || texte.equals("yes") || texte.contains("enregistrer") || texte.contains("valider")) {
            StyliseurBoutonTheme.appliquer(bouton, theme.palette().primaryButton(), theme, theme.typography().buttonPrimary());
            return;
        }
        StyliseurBoutonTheme.appliquer(bouton, theme.palette().secondaryButton(), theme, theme.typography().buttonSecondary());
    }
}
