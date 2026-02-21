package com.rdr.cecdoc.ui.theme;

import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public final class StrategieThemeCommuniste implements StrategieTheme {
    public static final String CLE_DATE_THEME_COMMUNISTE_TEST = "cecdoc.tests.theme.communiste.date";
    private static final System.Logger JOURNAL = System.getLogger(StrategieThemeCommuniste.class.getName());
    private static final Color ROUGE_CLAIR = Color.decode("#E53935");
    private static final Color ROUGE_FONCE = Color.decode("#8B0000");
    private static final Color NOIR = Color.decode("#000000");
    private static final Color JAUNE = Color.decode("#FFD700");
    private static final Color BLANC = Color.decode("#FFFFFF");

    public static boolean estEditionCommune() {
        LocalDate dateReference = dateReference();
        return dateReference.getDayOfMonth() == 19 && dateReference.getMonthValue() == 3;
    }

    private static LocalDate dateReference() {
        String brute = System.getProperty(CLE_DATE_THEME_COMMUNISTE_TEST, "").trim();
        if (!brute.isBlank()) {
            try {
                return LocalDate.parse(brute);
            } catch (DateTimeParseException ex) {
                JOURNAL.log(System.Logger.Level.DEBUG, "Date de test du thème communiste invalide : " + brute, ex);
            }
        }
        return LocalDate.now();
    }

    @Override
    public ModeTheme mode() {
        return ModeTheme.COMMUNISTE;
    }

    @Override
    public TokensTheme creer() {
        boolean editionCommune = estEditionCommune();
        Color fondHaut = ROUGE_CLAIR;
        Color fondBas = editionCommune ? NOIR : ROUGE_FONCE;
        Color baseSombre = editionCommune ? NOIR : ROUGE_FONCE;
        Color texte = UtilitairesCouleurTheme.eclaircir(baseSombre, 0.10d);
        Color secondaire = UtilitairesCouleurTheme.assombrir(ROUGE_CLAIR, 0.20d);
        Color bordure = UtilitairesCouleurTheme.assombrir(ROUGE_CLAIR, 0.16d);
        Color focusCouleur = JAUNE;
        Color erreur = UtilitairesCouleurTheme.assombrir(baseSombre, 0.08d);
        Color succes = UtilitairesCouleurTheme.assombrir(JAUNE, 0.24d);
        Color superposition = UtilitairesCouleurTheme.avecAlpha(UtilitairesCouleurTheme.assombrir(baseSombre, 0.24d), 108);
        Color carte = UtilitairesCouleurTheme.eclaircir(ROUGE_CLAIR, 0.86d);
        Color champ = UtilitairesCouleurTheme.eclaircir(JAUNE, 0.58d);

        TokensEtatBouton principal = new TokensEtatBouton(baseSombre, UtilitairesCouleurTheme.assombrir(baseSombre, 0.08d), UtilitairesCouleurTheme.assombrir(baseSombre, 0.16d), UtilitairesCouleurTheme.eclaircir(baseSombre, 0.20d), JAUNE, UtilitairesCouleurTheme.eclaircir(JAUNE, 0.08d), UtilitairesCouleurTheme.assombrir(baseSombre, 0.22d), focusCouleur);

        TokensEtatBouton secondaireBouton = new TokensEtatBouton(JAUNE, UtilitairesCouleurTheme.assombrir(JAUNE, 0.06d), UtilitairesCouleurTheme.assombrir(JAUNE, 0.12d), UtilitairesCouleurTheme.eclaircir(JAUNE, 0.24d), UtilitairesCouleurTheme.assombrir(baseSombre, 0.14d), UtilitairesCouleurTheme.assombrir(baseSombre, 0.04d), UtilitairesCouleurTheme.assombrir(JAUNE, 0.24d), focusCouleur);

        TokensEtatBouton dangerBouton = new TokensEtatBouton(UtilitairesCouleurTheme.assombrir(baseSombre, 0.04d), UtilitairesCouleurTheme.assombrir(baseSombre, 0.14d), UtilitairesCouleurTheme.assombrir(baseSombre, 0.22d), UtilitairesCouleurTheme.eclaircir(baseSombre, 0.14d), BLANC, UtilitairesCouleurTheme.eclaircir(BLANC, 0.08d), UtilitairesCouleurTheme.assombrir(baseSombre, 0.22d), focusCouleur);

        TokensEtatBouton iconeBouton = new TokensEtatBouton(JAUNE, UtilitairesCouleurTheme.eclaircir(JAUNE, 0.18d), UtilitairesCouleurTheme.assombrir(JAUNE, 0.06d), UtilitairesCouleurTheme.eclaircir(JAUNE, 0.32d), UtilitairesCouleurTheme.assombrir(baseSombre, 0.14d), UtilitairesCouleurTheme.assombrir(baseSombre, 0.05d), UtilitairesCouleurTheme.assombrir(JAUNE, 0.24d), focusCouleur);

        PaletteTheme palette = new PaletteTheme(fondHaut, fondBas, carte, carte, champ, bordure, focusCouleur, texte, texte, secondaire, BLANC, UtilitairesCouleurTheme.assombrir(ROUGE_CLAIR, 0.34d), erreur, succes, superposition, carte, UtilitairesCouleurTheme.assombrir(baseSombre, 0.12d), principal, secondaireBouton, dangerBouton, iconeBouton);

        TypographieTheme typographie = FabriqueTypographieTheme.creer(mode());
        EspacementTheme espacement = new EspacementTheme(18, 18, 10, 8, 16, 10, 12, 8);
        return new TokensTheme(ModeTheme.COMMUNISTE, palette, typographie, espacement);
    }
}
