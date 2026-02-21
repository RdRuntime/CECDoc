package com.rdr.cecdoc.document.rendu;

import com.rdr.cecdoc.document.AlignementTexte;
import com.rdr.cecdoc.document.RedactriceDocument;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAbstractNum;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLvl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STNumberFormat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Objects;

public final class RedactriceDocx implements RedactriceDocument {
    private final XWPFDocument document;
    private BigInteger identifiantNumerotationPuces;

    public RedactriceDocx() {
        this.document = new XWPFDocument();
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

    @Override
    public void ajouterParagraphe(AlignementTexte alignement, String texte, boolean gras, boolean italique, int taillePolice, boolean sautPageAvant) {
        XWPFParagraph paragraphe = creerParagraphe(alignement, sautPageAvant);
        ajouterRunTexte(paragraphe, texte, gras, italique, taillePolice);
    }

    @Override
    public void ajouterPuce(AlignementTexte alignement, int niveau, String texte, boolean gras, boolean italique, int taillePolice, boolean sautPageAvant) {
        int profondeur = Math.max(1, Math.min(9, niveau));
        XWPFParagraph paragraphe = creerParagraphe(alignement, sautPageAvant);
        paragraphe.setNumID(obtenirIdentifiantNumerotationPuces());
        paragraphe.setNumILvl(BigInteger.valueOf(profondeur - 1L));
        paragraphe.setIndentationLeft(720 + ((profondeur - 1) * 360));
        paragraphe.setIndentationHanging(360);
        ajouterRunTexte(paragraphe, texte, gras, italique, taillePolice);
    }

    private XWPFParagraph creerParagraphe(AlignementTexte alignement, boolean sautPageAvant) {
        XWPFParagraph paragraphe = document.createParagraph();
        paragraphe.setAlignment(convertirAlignement(alignement));
        paragraphe.setPageBreak(sautPageAvant);
        return paragraphe;
    }

    private void ajouterRunTexte(XWPFParagraph paragraphe, String texte, boolean gras, boolean italique, int taillePolice) {
        XWPFRun run = paragraphe.createRun();
        run.setBold(gras);
        run.setItalic(italique);
        run.setFontFamily("Calibri");
        run.setFontSize(taillePolice <= 0 ? 11 : taillePolice);
        run.setText(texte == null ? "" : texte);
    }

    private BigInteger obtenirIdentifiantNumerotationPuces() {
        if (identifiantNumerotationPuces != null) {
            return identifiantNumerotationPuces;
        }
        XWPFNumbering numbering = document.getNumbering();
        if (numbering == null) {
            numbering = document.createNumbering();
        }
        CTAbstractNum abstractNum = CTAbstractNum.Factory.newInstance();
        BigInteger abstractNumId = BigInteger.valueOf(numbering.getAbstractNums().size() + 1L);
        abstractNum.setAbstractNumId(abstractNumId);
        for (int niveau = 0; niveau < 9; niveau++) {
            CTLvl lvl = abstractNum.addNewLvl();
            lvl.setIlvl(BigInteger.valueOf(niveau));
            lvl.addNewNumFmt().setVal(STNumberFormat.BULLET);
            lvl.addNewLvlText().setVal(symbolePucePourNiveau(niveau));
            lvl.addNewStart().setVal(BigInteger.ONE);
        }
        BigInteger nouvelAbstractNumId = numbering.addAbstractNum(new XWPFAbstractNum(abstractNum));
        identifiantNumerotationPuces = numbering.addNum(nouvelAbstractNumId);
        return identifiantNumerotationPuces;
    }

    private String symbolePucePourNiveau(int niveau) {
        int modulo = niveau % 3;
        if (modulo == 1) {
            return "◦";
        }
        if (modulo == 2) {
            return "▪";
        }
        return "•";
    }

    @Override
    public void ajouterSautPage() {
        XWPFParagraph paragraphe = document.createParagraph();
        paragraphe.setPageBreak(true);
    }

    @Override
    public void enregistrer(File fichierSortie) throws IOException {
        Objects.requireNonNull(fichierSortie, "fichierSortie");
        File parent = fichierSortie.getParentFile();
        if (parent != null && !parent.exists() && !parent.mkdirs() && !parent.isDirectory()) {
            throw new IOException("Impossible de créer le dossier de sortie DOCX : " + parent);
        }
        try (FileOutputStream sortie = new FileOutputStream(fichierSortie)) {
            document.write(sortie);
        }
    }

    @Override
    public void close() throws IOException {
        document.close();
    }
}
