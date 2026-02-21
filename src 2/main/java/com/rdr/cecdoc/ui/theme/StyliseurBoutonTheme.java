package com.rdr.cecdoc.ui.theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Objects;


public final class StyliseurBoutonTheme {
    private StyliseurBoutonTheme() {
    }

    public static void appliquer(JButton button, TokensEtatBouton tokens, TokensTheme theme, Font font) {
        JButton nonNullButton = Objects.requireNonNull(button, "button");
        TokensEtatBouton nonNullTokens = Objects.requireNonNull(tokens, "tokens");
        TokensTheme nonNullTheme = Objects.requireNonNull(theme, "theme");

        nonNullButton.setUI(new BasicButtonUI());
        nonNullButton.setOpaque(true);
        nonNullButton.setBorderPainted(true);
        nonNullButton.setFocusPainted(false);
        nonNullButton.setRolloverEnabled(true);
        nonNullButton.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
        nonNullButton.setFont(Objects.requireNonNull(font, "font"));

        detacherEcouteurs(nonNullButton);

        ChangeListener changeListener = new ButtonStateChangeListener(nonNullTokens, nonNullTheme);
        FocusAdapter focusListener = new ButtonStateFocusListener(nonNullTokens, nonNullTheme);

        nonNullButton.addChangeListener(changeListener);
        nonNullButton.addFocusListener(focusListener);

        rafraichir(nonNullButton, nonNullTokens, nonNullTheme);
    }

    private static void detacherEcouteurs(JButton button) {
        for (ChangeListener listener : button.getChangeListeners()) {
            if (listener instanceof ButtonStateChangeListener) {
                button.removeChangeListener(listener);
            }
        }
        for (java.awt.event.FocusListener listener : button.getFocusListeners()) {
            if (listener instanceof ButtonStateFocusListener) {
                button.removeFocusListener(listener);
            }
        }
    }

    private static void rafraichir(JButton button, TokensEtatBouton tokens, TokensTheme theme) {
        if (!button.isEnabled()) {
            button.setBackground(tokens.disabledBackground());
            button.setForeground(tokens.disabledForeground());
            button.setBorder(BorderFactory.createCompoundBorder(new LineBorder(tokens.border(), 1, true), new EmptyBorder(theme.spacing().buttonInsetY(), theme.spacing().buttonInsetX(), theme.spacing().buttonInsetY(), theme.spacing().buttonInsetX())));
            garantirHauteurLisible(button);
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
            int max = Math.max(0, theme.spacing().buttonInsetY() - 1);
            int max1 = Math.max(0, theme.spacing().buttonInsetX() - 1);
            button.setBorder(BorderFactory.createCompoundBorder(new LineBorder(tokens.focusRing(), 2, true), new EmptyBorder(max, max1, max, max1)));
        } else {
            button.setBorder(BorderFactory.createCompoundBorder(new LineBorder(tokens.border(), 1, true), new EmptyBorder(theme.spacing().buttonInsetY(), theme.spacing().buttonInsetX(), theme.spacing().buttonInsetY(), theme.spacing().buttonInsetX())));
        }
        garantirHauteurLisible(button);
    }

    private static void garantirHauteurLisible(JButton button) {
        FontMetrics metriques = button.getFontMetrics(button.getFont());
        if (metriques == null) {
            return;
        }
        Insets insets = button.getInsets();
        int hauteurTexte = metriques.getHeight();
        int hauteurMinimale = hauteurTexte + insets.top + insets.bottom + 4;
        Dimension preferee = button.getPreferredSize();
        int hauteurFinale = Math.max(preferee.height, hauteurMinimale);
        if (hauteurFinale > preferee.height) {
            button.setPreferredSize(new Dimension(preferee.width, hauteurFinale));
        }
        Dimension minimale = button.getMinimumSize();
        if (hauteurFinale > minimale.height) {
            button.setMinimumSize(new Dimension(minimale.width, hauteurFinale));
        }
    }

    private record ButtonStateChangeListener(TokensEtatBouton tokens, TokensTheme theme) implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent event) {
            if (event.getSource() instanceof JButton button) {
                rafraichir(button, tokens, theme);
            }
        }
    }

    private static final class ButtonStateFocusListener extends FocusAdapter {
        private final TokensEtatBouton tokens;
        private final TokensTheme theme;

        private ButtonStateFocusListener(TokensEtatBouton tokens, TokensTheme theme) {
            this.tokens = tokens;
            this.theme = theme;
        }

        @Override
        public void focusGained(FocusEvent event) {
            if (event.getComponent() instanceof JButton button) {
                rafraichir(button, tokens, theme);
            }
        }

        @Override
        public void focusLost(FocusEvent event) {
            if (event.getComponent() instanceof JButton button) {
                rafraichir(button, tokens, theme);
            }
        }
    }
}
