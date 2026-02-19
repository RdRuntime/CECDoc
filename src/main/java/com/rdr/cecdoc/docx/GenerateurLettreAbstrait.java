package com.rdr.cecdoc.docx;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

abstract class GenerateurLettreAbstrait {
    protected static final String POLICE = "Calibri";
    protected static final int TAILLE_POLICE = 11;

    protected final void ecrireDocument(File destination, RedactionDocument redactionDocument) throws IOException {
        Objects.requireNonNull(destination, "destination");
        Objects.requireNonNull(redactionDocument, "redactionDocument");
        try (XWPFDocument document = new XWPFDocument()) {
            redactionDocument.rediger(document);
            try (FileOutputStream sortie = new FileOutputStream(destination)) {
                document.write(sortie);
            }
        }
    }

    protected final void ajouterLigne(XWPFDocument document, ParagraphAlignment alignement, String texte, boolean gras) {
        XWPFParagraph paragraphe = document.createParagraph();
        paragraphe.setAlignment(alignement == null ? ParagraphAlignment.LEFT : alignement);
        XWPFRun run = paragraphe.createRun();
        run.setFontFamily(POLICE);
        run.setFontSize(TAILLE_POLICE);
        run.setBold(gras);
        run.setText(texte == null ? "" : texte);
    }

    protected final void ajouterParagrapheVide(XWPFDocument document, ParagraphAlignment alignement) {
        ajouterLigne(document, alignement, "", false);
    }

    protected final void ajouterBlocMultiligne(XWPFDocument document, ParagraphAlignment alignement, String texte, boolean gras) {
        if (texte == null || texte.isBlank()) {
            ajouterLigne(document, alignement, "", gras);
            return;
        }
        String[] lignes = texte.split("\\R");
        boolean auMoinsUne = false;
        for (String ligne : lignes) {
            String candidate = ligne == null ? "" : ligne.trim();
            if (candidate.isEmpty()) {
                continue;
            }
            ajouterLigne(document, alignement, candidate, gras);
            auMoinsUne = true;
        }
        if (!auMoinsUne) {
            ajouterLigne(document, alignement, "", gras);
        }
    }

    protected static String nettoyer(String valeur) {
        return valeur == null ? "" : valeur.trim();
    }

    @FunctionalInterface
    protected interface RedactionDocument {
        void rediger(XWPFDocument document) throws IOException;
    }
}
