package com.rdr.cecdoc.ui.theme;

import java.awt.Color;


public final class StrategieThemeNonBinaire implements StrategieTheme {
    private static final Color JAUNE = Color.decode("#FCF434");
    private static final Color BLANC = Color.decode("#FFFFFF");
    private static final Color VIOLET = Color.decode("#9C59D1");
    private static final Color NOIR = Color.decode("#000000");


    @Override
    public ModeTheme mode() {
        return ModeTheme.NON_BINAIRE;
    }


    @Override
    public TokensTheme creer() {
        Color texte = UtilitairesCouleurTheme.eclaircir(NOIR, 0.10d);
        Color secondaire = UtilitairesCouleurTheme.eclaircir(NOIR, 0.34d);
        Color bordure = UtilitairesCouleurTheme.eclaircir(VIOLET, 0.46d);
        Color focusCouleur = VIOLET;
        Color erreur = UtilitairesCouleurTheme.assombrir(NOIR, 0.10d);
        Color succes = UtilitairesCouleurTheme.assombrir(VIOLET, 0.12d);
        Color superposition = UtilitairesCouleurTheme.avecAlpha(NOIR, 122);

        TokensEtatBouton principal = new TokensEtatBouton(VIOLET, UtilitairesCouleurTheme.assombrir(VIOLET, 0.10d), UtilitairesCouleurTheme.assombrir(VIOLET, 0.20d), UtilitairesCouleurTheme.eclaircir(VIOLET, 0.44d), BLANC, UtilitairesCouleurTheme.eclaircir(BLANC, 0.10d), UtilitairesCouleurTheme.assombrir(VIOLET, 0.24d), VIOLET);

        TokensEtatBouton secondaireBouton = new TokensEtatBouton(JAUNE, UtilitairesCouleurTheme.assombrir(JAUNE, 0.04d), UtilitairesCouleurTheme.assombrir(JAUNE, 0.12d), UtilitairesCouleurTheme.eclaircir(JAUNE, 0.36d), NOIR, UtilitairesCouleurTheme.eclaircir(NOIR, 0.28d), UtilitairesCouleurTheme.assombrir(JAUNE, 0.22d), NOIR);

        TokensEtatBouton dangerBouton = new TokensEtatBouton(NOIR, UtilitairesCouleurTheme.eclaircir(NOIR, 0.12d), UtilitairesCouleurTheme.eclaircir(NOIR, 0.22d), UtilitairesCouleurTheme.eclaircir(NOIR, 0.56d), BLANC, UtilitairesCouleurTheme.eclaircir(BLANC, 0.08d), NOIR, NOIR);

        TokensEtatBouton iconeBouton = new TokensEtatBouton(BLANC, UtilitairesCouleurTheme.eclaircir(JAUNE, 0.30d), UtilitairesCouleurTheme.eclaircir(JAUNE, 0.16d), UtilitairesCouleurTheme.eclaircir(BLANC, 0.02d), VIOLET, UtilitairesCouleurTheme.eclaircir(VIOLET, 0.22d), bordure, VIOLET);

        PaletteTheme palette = new PaletteTheme(JAUNE, VIOLET, BLANC, BLANC, BLANC, bordure, focusCouleur, NOIR, texte, secondaire, BLANC, UtilitairesCouleurTheme.eclaircir(NOIR, 0.52d), erreur, succes, superposition, BLANC, UtilitairesCouleurTheme.assombrir(VIOLET, 0.18d), principal, secondaireBouton, dangerBouton, iconeBouton);

        TypographieTheme typographie = FabriqueTypographieTheme.creer(mode());

        EspacementTheme espacement = new EspacementTheme(18, 18, 10, 8, 16, 10, 12, 8);

        return new TokensTheme(ModeTheme.NON_BINAIRE, palette, typographie, espacement);
    }
}
