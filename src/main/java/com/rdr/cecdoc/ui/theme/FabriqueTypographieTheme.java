package com.rdr.cecdoc.ui.theme;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;


public final class FabriqueTypographieTheme {
    private static final Set<String> AVAILABLE_FAMILIES = Arrays.stream(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames(Locale.ROOT)).map(name -> name.toLowerCase(Locale.ROOT)).collect(Collectors.toUnmodifiableSet());

    private FabriqueTypographieTheme() {
    }


    public static TypographieTheme creer(ModeTheme mode) {
        if (mode == ModeTheme.NON_BINAIRE) {
            return createNonBinaryTypography();
        }
        return createTransTypography();
    }

    private static TypographieTheme createTransTypography() {
        Font title = pick(Font.BOLD, 30, "Avenir Next", "Futura", "Montserrat", "Gill Sans MT", "Trebuchet MS", "SansSerif");
        Font subtitle = pick(Font.PLAIN, 16, "Avenir Next", "Noto Sans", "Segoe UI", "Helvetica Neue", "SansSerif");
        Font section = pick(Font.BOLD, 17, "Avenir Next", "Futura", "Montserrat", "Gill Sans MT", "SansSerif");
        Font label = pick(Font.PLAIN, 13, "Noto Sans", "Segoe UI", "Helvetica Neue", "Avenir Next", "SansSerif");
        Font input = pick(Font.PLAIN, 14, "Noto Sans", "Segoe UI", "Helvetica Neue", "Avenir Next", "SansSerif");
        Font helper = pick(Font.PLAIN, 12, "Noto Sans", "Segoe UI", "Helvetica Neue", "SansSerif");
        Font message = pick(Font.PLAIN, 12, "Noto Sans", "Segoe UI", "Helvetica Neue", "SansSerif");
        Font buttonPrimary = pick(Font.BOLD, 14, "Avenir Next", "Montserrat", "Segoe UI", "SansSerif");
        Font buttonSecondary = pick(Font.BOLD, 13, "Avenir Next", "Noto Sans", "Segoe UI", "SansSerif");
        return new TypographieTheme(title, subtitle, section, label, input, helper, message, buttonPrimary, buttonSecondary);
    }

    private static TypographieTheme createNonBinaryTypography() {
        return createTransTypography();
    }

    private static Font pick(int style, int size, String... preferredFamilies) {
        for (String family : preferredFamilies) {
            if (family != null && isAvailable(family)) {
                return new Font(family, style, size);
            }
        }
        return new Font("SansSerif", style, size);
    }

    private static boolean isAvailable(String family) {
        return AVAILABLE_FAMILIES.contains(family.toLowerCase(Locale.ROOT));
    }
}
