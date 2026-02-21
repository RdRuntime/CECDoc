package com.rdr.cecdoc.ui.theme;

import java.awt.*;

public final class StrategieThemeIntersexe implements StrategieTheme {
    private static final Color JAUNE_INTERSEXE = Color.decode("#FFD800");
    private static final Color VIOLET_INTERSEXE = Color.decode("#7902AA");
    private static final Color BLANC = Color.decode("#FFFFFF");

    @Override
    public ModeTheme mode() {
        return ModeTheme.INTERSEXE;
    }

    @Override
    public TokensTheme creer() {
        Color texte = UtilitairesCouleurTheme.assombrir(VIOLET_INTERSEXE, 0.30d);
        Color secondaire = UtilitairesCouleurTheme.assombrir(VIOLET_INTERSEXE, 0.12d);
        Color bordure = UtilitairesCouleurTheme.eclaircir(VIOLET_INTERSEXE, 0.34d);
        Color focusCouleur = VIOLET_INTERSEXE;
        Color erreur = UtilitairesCouleurTheme.assombrir(VIOLET_INTERSEXE, 0.22d);
        Color succes = UtilitairesCouleurTheme.assombrir(VIOLET_INTERSEXE, 0.08d);
        Color superposition = UtilitairesCouleurTheme.avecAlpha(UtilitairesCouleurTheme.assombrir(VIOLET_INTERSEXE, 0.50d), 106);

        TokensEtatBouton principal = new TokensEtatBouton(VIOLET_INTERSEXE, UtilitairesCouleurTheme.assombrir(VIOLET_INTERSEXE, 0.08d), UtilitairesCouleurTheme.assombrir(VIOLET_INTERSEXE, 0.16d), UtilitairesCouleurTheme.eclaircir(VIOLET_INTERSEXE, 0.40d), BLANC, UtilitairesCouleurTheme.eclaircir(BLANC, 0.08d), UtilitairesCouleurTheme.assombrir(VIOLET_INTERSEXE, 0.24d), focusCouleur);

        TokensEtatBouton secondaireBouton = new TokensEtatBouton(JAUNE_INTERSEXE, UtilitairesCouleurTheme.assombrir(JAUNE_INTERSEXE, 0.04d), UtilitairesCouleurTheme.assombrir(JAUNE_INTERSEXE, 0.12d), UtilitairesCouleurTheme.eclaircir(JAUNE_INTERSEXE, 0.30d), UtilitairesCouleurTheme.assombrir(VIOLET_INTERSEXE, 0.62d), UtilitairesCouleurTheme.assombrir(VIOLET_INTERSEXE, 0.44d), UtilitairesCouleurTheme.assombrir(JAUNE_INTERSEXE, 0.22d), focusCouleur);

        TokensEtatBouton dangerBouton = new TokensEtatBouton(UtilitairesCouleurTheme.assombrir(VIOLET_INTERSEXE, 0.18d), UtilitairesCouleurTheme.assombrir(VIOLET_INTERSEXE, 0.26d), UtilitairesCouleurTheme.assombrir(VIOLET_INTERSEXE, 0.34d), UtilitairesCouleurTheme.eclaircir(VIOLET_INTERSEXE, 0.30d), BLANC, UtilitairesCouleurTheme.eclaircir(BLANC, 0.08d), UtilitairesCouleurTheme.assombrir(VIOLET_INTERSEXE, 0.36d), UtilitairesCouleurTheme.assombrir(VIOLET_INTERSEXE, 0.42d));

        TokensEtatBouton icone = new TokensEtatBouton(BLANC, UtilitairesCouleurTheme.eclaircir(JAUNE_INTERSEXE, 0.30d), UtilitairesCouleurTheme.eclaircir(JAUNE_INTERSEXE, 0.20d), UtilitairesCouleurTheme.eclaircir(BLANC, 0.02d), VIOLET_INTERSEXE, UtilitairesCouleurTheme.eclaircir(VIOLET_INTERSEXE, 0.18d), bordure, focusCouleur);

        PaletteTheme palette = new PaletteTheme(JAUNE_INTERSEXE, VIOLET_INTERSEXE, BLANC, BLANC, BLANC, bordure, focusCouleur, texte, texte, secondaire, BLANC, UtilitairesCouleurTheme.assombrir(VIOLET_INTERSEXE, 0.20d), erreur, succes, superposition, BLANC, UtilitairesCouleurTheme.assombrir(VIOLET_INTERSEXE, 0.16d), principal, secondaireBouton, dangerBouton, icone);

        TypographieTheme typographie = FabriqueTypographieTheme.creer(mode());
        EspacementTheme espacement = new EspacementTheme(18, 18, 10, 8, 16, 10, 12, 8);

        return new TokensTheme(ModeTheme.INTERSEXE, palette, typographie, espacement);
    }
}
