package com.rdr.cecdoc.model;

import java.util.Locale;

public enum TypePieceJointe {
    PDF("pdf"), WORD("word"), IMAGE("image"), INCONNU("inconnu");

    private final String code;

    TypePieceJointe(String code) {
        this.code = code;
    }

    public String code() {
        return code;
    }

    public boolean estPrisEnCharge() {
        return this == PDF || this == WORD || this == IMAGE;
    }

    public static TypePieceJointe depuisCode(String code) {
        if (code == null) {
            return INCONNU;
        }
        String codeNormalise = code.trim().toLowerCase(Locale.ROOT);
        for (TypePieceJointe type : values()) {
            if (type.code.equals(codeNormalise)) {
                return type;
            }
        }
        return INCONNU;
    }

    public static TypePieceJointe depuisNomFichier(String nomFichier) {
        if (nomFichier == null) {
            return INCONNU;
        }
        String nomNormalise = nomFichier.trim().toLowerCase(Locale.ROOT);
        int indexExtension = nomNormalise.lastIndexOf('.');
        if (indexExtension < 0 || indexExtension >= nomNormalise.length() - 1) {
            return INCONNU;
        }
        String extension = nomNormalise.substring(indexExtension + 1);
        return switch (extension) {
            case "pdf" -> PDF;
            case "doc", "docx" -> WORD;
            case "jpg", "jpeg", "png" -> IMAGE;
            default -> INCONNU;
        };
    }
}
