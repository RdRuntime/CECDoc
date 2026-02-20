package com.rdr.cecdoc.document.rendu;

import com.rdr.cecdoc.document.AlignementTexte;
import com.rdr.cecdoc.document.RedactriceDocument;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public final class RedactriceDocx implements RedactriceDocument {
    private final XWPFDocument document;

    public RedactriceDocx() {
        this.document = new XWPFDocument();
    }

    @Override
    public void ajouterParagraphe(AlignementTexte alignement, String texte, boolean gras, boolean italique, int taillePolice, boolean sautPageAvant) {
        XWPFParagraph paragraphe = document.createParagraph();
        paragraphe.setAlignment(convertirAlignement(alignement));
        paragraphe.setPageBreak(sautPageAvant);
        XWPFRun run = paragraphe.createRun();
        run.setBold(gras);
        run.setItalic(italique);
        run.setFontFamily("Calibri");
        run.setFontSize(taillePolice <= 0 ? 11 : taillePolice);
        run.setText(texte == null ? "" : texte);
    }

    @Override
    public void ajouterSautPage() {
        XWPFParagraph paragraphe = document.createParagraph();
        paragraphe.setPageBreak(true);
    }

    @Override
    public void enregistrer(File fichierSortie) throws IOException {
        Objects.requireNonNull(fichierSortie, "fichierSortie");
        try (FileOutputStream sortie = new FileOutputStream(fichierSortie)) {
            document.write(sortie);
        }
    }

    @Override
    public void close() throws IOException {
        document.close();
    }

    private static ParagraphAlignment convertirAlignement(AlignementTexte alignement) {
        if (alignement == null) {
            return ParagraphAlignment.LEFT;
        }
        return switch (alignement) {
            case CENTRE -> ParagraphAlignment.CENTER;
            case DROITE -> ParagraphAlignment.RIGHT;
            case JUSTIFIE -> ParagraphAlignment.BOTH;
            case GAUCHE -> ParagraphAlignment.LEFT;
        };
    }
}
