package com.rdr.cecdoc.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Locale;


public final class ParseursDate {
    private static final DateTimeFormatter INPUT_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/uuuu", Locale.ROOT).withResolverStyle(ResolverStyle.STRICT);

    private ParseursDate() {
    }

    public static LocalDate parserDateSaisie(String text) {
        return LocalDate.parse(text, INPUT_DATE_FORMATTER);
    }

    public static boolean dateSaisieValide(String text) {
        if (text == null || text.isBlank()) {
            return false;
        }
        try {
            parserDateSaisie(text.trim());
            return true;
        } catch (DateTimeParseException ex) {
            return false;
        }
    }

    public static boolean dateSaisieNonFuture(String text) {
        if (!dateSaisieValide(text)) {
            return false;
        }
        LocalDate date = parserDateSaisie(text.trim());
        return !date.isAfter(LocalDate.now());
    }
}
