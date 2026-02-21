package com.rdr.cecdoc.ui.theme;

import java.awt.*;

public final class StrategieThemeLesbien implements StrategieTheme {
    private static final Color ORANGE_FONCE = Color.decode("#D52D00");
    private static final Color ORANGE_CLAIR = Color.decode("#EF7627");
    private static final Color BLANC = Color.decode("#FFFFFF");
    private static final Color ROSE = Color.decode("#D162A4");
    private static final Color ROSE_FONCE = Color.decode("#B55690");

    @Override
    public ModeTheme mode() {
        return ModeTheme.LESBIEN;
    }

    @Override
    public TokensTheme creer() {
        Color texte = UtilitairesCouleurTheme.assombrir(ROSE_FONCE, 0.58d);
        Color secondaire = UtilitairesCouleurTheme.assombrir(ROSE_FONCE, 0.36d);
        Color bordure = UtilitairesCouleurTheme.eclaircir(ROSE, 0.40d);
        Color focusCouleur = ROSE_FONCE;
        Color erreur = UtilitairesCouleurTheme.assombrir(ORANGE_FONCE, 0.28d);
        Color succes = UtilitairesCouleurTheme.assombrir(ROSE_FONCE, 0.12d);
        Color superposition = UtilitairesCouleurTheme.avecAlpha(UtilitairesCouleurTheme.assombrir(ROSE_FONCE, 0.66d), 110);

        TokensEtatBouton principal = new TokensEtatBouton(ROSE_FONCE, UtilitairesCouleurTheme.assombrir(ROSE_FONCE, 0.10d), UtilitairesCouleurTheme.assombrir(ROSE_FONCE, 0.18d), UtilitairesCouleurTheme.eclaircir(ROSE_FONCE, 0.42d), BLANC, UtilitairesCouleurTheme.eclaircir(BLANC, 0.06d), UtilitairesCouleurTheme.assombrir(ROSE_FONCE, 0.26d), focusCouleur);

        TokensEtatBouton secondaireBouton = new TokensEtatBouton(ORANGE_CLAIR, UtilitairesCouleurTheme.assombrir(ORANGE_CLAIR, 0.06d), UtilitairesCouleurTheme.assombrir(ORANGE_CLAIR, 0.16d), UtilitairesCouleurTheme.eclaircir(ORANGE_CLAIR, 0.36d), UtilitairesCouleurTheme.assombrir(ROSE_FONCE, 0.74d), UtilitairesCouleurTheme.assombrir(ROSE_FONCE, 0.50d), UtilitairesCouleurTheme.assombrir(ORANGE_CLAIR, 0.20d), focusCouleur);

        TokensEtatBouton dangerBouton = new TokensEtatBouton(ORANGE_FONCE, UtilitairesCouleurTheme.assombrir(ORANGE_FONCE, 0.12d), UtilitairesCouleurTheme.assombrir(ORANGE_FONCE, 0.20d), UtilitairesCouleurTheme.eclaircir(ORANGE_FONCE, 0.30d), BLANC, UtilitairesCouleurTheme.eclaircir(BLANC, 0.08d), UtilitairesCouleurTheme.assombrir(ORANGE_FONCE, 0.30d), UtilitairesCouleurTheme.assombrir(ORANGE_FONCE, 0.40d));

        TokensEtatBouton iconeBouton = new TokensEtatBouton(BLANC, UtilitairesCouleurTheme.eclaircir(ROSE, 0.42d), UtilitairesCouleurTheme.eclaircir(ROSE, 0.28d), UtilitairesCouleurTheme.eclaircir(BLANC, 0.02d), ROSE_FONCE, UtilitairesCouleurTheme.eclaircir(ROSE_FONCE, 0.18d), bordure, focusCouleur);

        PaletteTheme palette = new PaletteTheme(ORANGE_FONCE, ROSE, BLANC, BLANC, BLANC, bordure, focusCouleur, texte, texte, secondaire, BLANC, UtilitairesCouleurTheme.assombrir(ROSE_FONCE, 0.30d), erreur, succes, superposition, BLANC, UtilitairesCouleurTheme.assombrir(ROSE_FONCE, 0.18d), principal, secondaireBouton, dangerBouton, iconeBouton);

        TypographieTheme typographie = FabriqueTypographieTheme.creer(mode());
        EspacementTheme espacement = new EspacementTheme(18, 18, 10, 8, 16, 10, 12, 8);

        return new TokensTheme(ModeTheme.LESBIEN, palette, typographie, espacement);
    }
}
