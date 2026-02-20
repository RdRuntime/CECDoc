package com.rdr.cecdoc.model;

import java.nio.file.Path;
import java.util.Locale;

public enum TypeDocumentGenere {
    DOCX("docx", ".docx", "Document Word (.docx)"),
    ODT("odt", ".odt", "Document OpenDocument (.odt)");

    private final String code;
    private final String extension;
    private final String libelle;

    TypeDocumentGenere(String code, String extension, String libelle) {
        this.code = code;
        this.extension = extension;
        this.libelle = libelle;
    }

    public String code() {
        return code;
    }

    public String extension() {
        return extension;
    }

    public String libelle() {
        return libelle;
    }

    @Override
    public String toString() {
        return libelle;
    }

    public static TypeDocumentGenere depuisCode(String code) {
        if (code == null || code.isBlank()) {
            return DOCX;
        }
        String codeNormalise = code.trim().toLowerCase(Locale.ROOT);
        for (TypeDocumentGenere type : values()) {
            if (type.code.equals(codeNormalise)) {
                return type;
            }
        }
        return DOCX;
    }

    public static TypeDocumentGenere depuisChemin(Path cheminDocument) {
        Path nom = cheminDocument == null ? null : cheminDocument.getFileName();
        if (nom == null) {
            return DOCX;
        }
        String texteNom = nom.toString();
        int indexPoint = texteNom.lastIndexOf('.');
        if (indexPoint < 0 || indexPoint >= texteNom.length() - 1) {
            return DOCX;
        }
        return depuisCode(texteNom.substring(indexPoint + 1));
    }
}
