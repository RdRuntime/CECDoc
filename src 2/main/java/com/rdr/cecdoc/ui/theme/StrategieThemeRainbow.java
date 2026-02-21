package com.rdr.cecdoc.ui.theme;

import java.awt.*;

public final class StrategieThemeRainbow implements StrategieTheme {
    private static final Color ROUGE = Color.decode("#E40303");
    private static final Color JAUNE = Color.decode("#FFED00");
    private static final Color VERT = Color.decode("#008026");
    private static final Color BLEU = Color.decode("#004DFF");
    private static final Color VIOLET = Color.decode("#750787");
    private static final Color BLANC = Color.decode("#FFFFFF");

    @Override
    public ModeTheme mode() {
        return ModeTheme.RAINBOW;
    }

    @Override
    public TokensTheme creer() {
        Color texte = UtilitairesCouleurTheme.assombrir(BLEU, 0.55d);
        Color secondaire = UtilitairesCouleurTheme.assombrir(BLEU, 0.32d);
        Color bordure = UtilitairesCouleurTheme.eclaircir(BLEU, 0.45d);
        Color focusCouleur = BLEU;
        Color erreur = UtilitairesCouleurTheme.assombrir(ROUGE, 0.22d);
        Color succes = UtilitairesCouleurTheme.assombrir(VERT, 0.08d);
        Color superposition = UtilitairesCouleurTheme.avecAlpha(UtilitairesCouleurTheme.assombrir(VIOLET, 0.56d), 104);

        TokensEtatBouton principal = new TokensEtatBouton(BLEU, UtilitairesCouleurTheme.assombrir(BLEU, 0.08d), UtilitairesCouleurTheme.assombrir(BLEU, 0.16d), UtilitairesCouleurTheme.eclaircir(BLEU, 0.40d), BLANC, UtilitairesCouleurTheme.eclaircir(BLANC, 0.08d), UtilitairesCouleurTheme.assombrir(BLEU, 0.22d), focusCouleur);

        TokensEtatBouton secondaireBouton = new TokensEtatBouton(VIOLET, UtilitairesCouleurTheme.assombrir(VIOLET, 0.06d), UtilitairesCouleurTheme.assombrir(VIOLET, 0.15d), UtilitairesCouleurTheme.eclaircir(VIOLET, 0.38d), BLANC, UtilitairesCouleurTheme.eclaircir(BLANC, 0.08d), UtilitairesCouleurTheme.assombrir(VIOLET, 0.24d), focusCouleur);

        TokensEtatBouton dangerBouton = new TokensEtatBouton(ROUGE, UtilitairesCouleurTheme.assombrir(ROUGE, 0.10d), UtilitairesCouleurTheme.assombrir(ROUGE, 0.18d), UtilitairesCouleurTheme.eclaircir(ROUGE, 0.30d), BLANC, UtilitairesCouleurTheme.eclaircir(BLANC, 0.10d), UtilitairesCouleurTheme.assombrir(ROUGE, 0.24d), UtilitairesCouleurTheme.assombrir(ROUGE, 0.34d));

        TokensEtatBouton iconeBouton = new TokensEtatBouton(JAUNE, UtilitairesCouleurTheme.eclaircir(JAUNE, 0.22d), UtilitairesCouleurTheme.assombrir(JAUNE, 0.10d), UtilitairesCouleurTheme.eclaircir(JAUNE, 0.34d), UtilitairesCouleurTheme.assombrir(VIOLET, 0.48d), UtilitairesCouleurTheme.assombrir(VIOLET, 0.26d), bordure, focusCouleur);

        PaletteTheme palette = new PaletteTheme(ROUGE, VIOLET, BLANC, BLANC, BLANC, bordure, focusCouleur, texte, texte, secondaire, BLANC, UtilitairesCouleurTheme.assombrir(BLEU, 0.24d), erreur, succes, superposition, BLANC, UtilitairesCouleurTheme.assombrir(VIOLET, 0.18d), principal, secondaireBouton, dangerBouton, iconeBouton);

        TypographieTheme typographie = FabriqueTypographieTheme.creer(mode());
        EspacementTheme espacement = new EspacementTheme(18, 18, 10, 8, 16, 10, 12, 8);

        return new TokensTheme(ModeTheme.RAINBOW, palette, typographie, espacement);
    }
}
