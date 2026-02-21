package com.rdr.cecdoc.ui.theme;

import java.util.List;


public final class FabriqueTheme {
    private FabriqueTheme() {
    }


    public static SelecteurTheme creerSelecteur() {
        return new SelecteurThemeStrategie(List.of(new StrategieThemeTrans(), new StrategieThemeNonBinaire(), new StrategieThemeLesbien(), new StrategieThemeIntersexe(), new StrategieThemeRainbow(), new StrategieThemeCommuniste()));
    }


    public static DiffuseurTheme creerDiffuseur() {
        return new DiffuseurTheme(creerSelecteur(), ModeTheme.TRANS);
    }
}
