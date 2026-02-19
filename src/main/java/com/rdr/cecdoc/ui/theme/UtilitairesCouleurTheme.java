package com.rdr.cecdoc.ui.theme;

import java.awt.Color;


public final class UtilitairesCouleurTheme {
    private UtilitairesCouleurTheme() {
    }


    public static Color avecAlpha(Color base, int alpha) {
        return new Color(base.getRed(), base.getGreen(), base.getBlue(), clamp(alpha));
    }


    public static Color eclaircir(Color base, double ratio) {
        double clamped = Math.max(0d, Math.min(1d, ratio));
        int r = (int) Math.round(base.getRed() + ((255 - base.getRed()) * clamped));
        int g = (int) Math.round(base.getGreen() + ((255 - base.getGreen()) * clamped));
        int b = (int) Math.round(base.getBlue() + ((255 - base.getBlue()) * clamped));
        return new Color(clamp(r), clamp(g), clamp(b), base.getAlpha());
    }


    public static Color assombrir(Color base, double ratio) {
        double clamped = Math.max(0d, Math.min(1d, ratio));
        int r = (int) Math.round(base.getRed() * (1d - clamped));
        int g = (int) Math.round(base.getGreen() * (1d - clamped));
        int b = (int) Math.round(base.getBlue() * (1d - clamped));
        return new Color(clamp(r), clamp(g), clamp(b), base.getAlpha());
    }

    private static int clamp(int value) {
        return Math.max(0, Math.min(255, value));
    }
}
