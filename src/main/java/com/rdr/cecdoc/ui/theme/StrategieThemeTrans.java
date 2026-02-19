package com.rdr.cecdoc.ui.theme;

import java.awt.Color;


public final class StrategieThemeTrans implements StrategieTheme {
    private static final Color BLEU = Color.decode("#5BCEFA");
    private static final Color ROSE = Color.decode("#F5A9B8");
    private static final Color BLANC = Color.decode("#FFFFFF");


    @Override
    public ModeTheme mode() {
        return ModeTheme.TRANS;
    }


    @Override
    public TokensTheme creer() {
        Color texte = UtilitairesCouleurTheme.assombrir(BLEU, 0.76d);
        Color secondaire = UtilitairesCouleurTheme.assombrir(BLEU, 0.54d);
        Color bordure = UtilitairesCouleurTheme.eclaircir(BLEU, 0.38d);
        Color focusCouleur = UtilitairesCouleurTheme.assombrir(BLEU, 0.48d);
        Color erreur = UtilitairesCouleurTheme.assombrir(ROSE, 0.44d);
        Color succes = UtilitairesCouleurTheme.assombrir(BLEU, 0.36d);
        Color superposition = UtilitairesCouleurTheme.avecAlpha(UtilitairesCouleurTheme.assombrir(BLEU, 0.62d), 102);

        TokensEtatBouton principal = new TokensEtatBouton(BLEU, UtilitairesCouleurTheme.assombrir(BLEU, 0.10d), UtilitairesCouleurTheme.assombrir(BLEU, 0.20d), UtilitairesCouleurTheme.eclaircir(BLEU, 0.46d), BLANC, UtilitairesCouleurTheme.eclaircir(BLANC, 0.08d), UtilitairesCouleurTheme.assombrir(BLEU, 0.22d), focusCouleur);

        TokensEtatBouton secondaireBouton = new TokensEtatBouton(ROSE, UtilitairesCouleurTheme.assombrir(ROSE, 0.05d), UtilitairesCouleurTheme.assombrir(ROSE, 0.14d), UtilitairesCouleurTheme.eclaircir(ROSE, 0.35d), UtilitairesCouleurTheme.assombrir(BLEU, 0.72d), UtilitairesCouleurTheme.assombrir(BLEU, 0.50d), UtilitairesCouleurTheme.assombrir(ROSE, 0.16d), focusCouleur);

        TokensEtatBouton dangerBouton = new TokensEtatBouton(UtilitairesCouleurTheme.assombrir(ROSE, 0.24d), UtilitairesCouleurTheme.assombrir(ROSE, 0.30d), UtilitairesCouleurTheme.assombrir(ROSE, 0.38d), UtilitairesCouleurTheme.eclaircir(ROSE, 0.32d), BLANC, UtilitairesCouleurTheme.eclaircir(BLANC, 0.12d), UtilitairesCouleurTheme.assombrir(ROSE, 0.38d), UtilitairesCouleurTheme.assombrir(ROSE, 0.46d));

        TokensEtatBouton iconeBouton = new TokensEtatBouton(BLANC, UtilitairesCouleurTheme.eclaircir(BLEU, 0.58d), UtilitairesCouleurTheme.eclaircir(BLEU, 0.44d), UtilitairesCouleurTheme.eclaircir(BLEU, 0.66d), UtilitairesCouleurTheme.assombrir(BLEU, 0.58d), UtilitairesCouleurTheme.assombrir(BLEU, 0.38d), bordure, focusCouleur);

        PaletteTheme palette = new PaletteTheme(BLEU, ROSE, BLANC, BLANC, BLANC, bordure, focusCouleur, texte, texte, secondaire, BLANC, UtilitairesCouleurTheme.assombrir(BLEU, 0.40d), erreur, succes, superposition, BLANC, UtilitairesCouleurTheme.assombrir(BLEU, 0.35d), principal, secondaireBouton, dangerBouton, iconeBouton);

        TypographieTheme typographie = FabriqueTypographieTheme.creer(mode());

        EspacementTheme espacement = new EspacementTheme(18, 18, 10, 8, 16, 10, 12, 8);

        return new TokensTheme(ModeTheme.TRANS, palette, typographie, espacement);
    }
}
