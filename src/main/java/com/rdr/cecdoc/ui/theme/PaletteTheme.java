package com.rdr.cecdoc.ui.theme;

import java.awt.Color;


public record PaletteTheme(Color backgroundTop, Color backgroundBottom, Color cardBackground, Color surfaceBackground,
                           Color fieldBackground, Color border, Color focus, Color titleText, Color bodyText,
                           Color mutedText, Color inverseText, Color placeholderText, Color error, Color success,
                           Color busyOverlay, Color busyPanelBackground, Color busyPanelBorder,
                           TokensEtatBouton primaryButton, TokensEtatBouton secondaryButton,
                           TokensEtatBouton dangerButton, TokensEtatBouton iconButton) {
}
