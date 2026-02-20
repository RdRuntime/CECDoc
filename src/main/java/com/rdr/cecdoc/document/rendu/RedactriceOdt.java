package com.rdr.cecdoc.document.rendu;

import com.rdr.cecdoc.document.AlignementTexte;
import com.rdr.cecdoc.document.RedactriceDocument;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Objects;

import org.odftoolkit.odfdom.dom.element.style.StyleParagraphPropertiesElement;
import org.odftoolkit.simple.TextDocument;
import org.odftoolkit.simple.style.Font;
import org.odftoolkit.simple.style.ParagraphProperties;
import org.odftoolkit.simple.style.StyleTypeDefinitions.FontStyle;
import org.odftoolkit.simple.style.StyleTypeDefinitions.HorizontalAlignmentType;
import org.odftoolkit.simple.text.Paragraph;

public final class RedactriceOdt implements RedactriceDocument {
    private final TextDocument document;

    public RedactriceOdt() throws IOException {
        try {
            this.document = TextDocument.newTextDocument();
        } catch (Exception ex) {
            throw new IOException("Impossible d'initialiser le document ODT.", ex);
        }
    }

    @Override
    public void ajouterParagraphe(AlignementTexte alignement, String texte, boolean gras, boolean italique, int taillePolice, boolean sautPageAvant) {
        String contenu = texte == null ? "" : texte;
        if (!contientRetourLigne(contenu)) {
            ajouterParagrapheSimple(alignement, contenu, gras, italique, taillePolice, sautPageAvant);
            return;
        }

        String[] lignes = contenu.split("\\R", -1);
        boolean premier = true;
        for (String ligne : lignes) {
            ajouterParagrapheSimple(alignement, ligne == null ? "" : ligne, gras, italique, taillePolice, premier && sautPageAvant);
            premier = false;
        }
    }

    private void ajouterParagrapheSimple(AlignementTexte alignement, String texte, boolean gras, boolean italique, int taillePolice, boolean sautPageAvant) {
        if (sautPageAvant) {
            document.addPageBreak();
        }
        Paragraph paragraphe = document.addParagraph(texte == null ? "" : texte);
        HorizontalAlignmentType alignementHorizontal = convertirAlignement(alignement);
        paragraphe.setHorizontalAlignment(alignementHorizontal);
        paragraphe.getStyleHandler().setHorizontalAlignment(alignementHorizontal);
        ParagraphProperties proprietesParagraphe = paragraphe.getStyleHandler().getParagraphPropertiesForWrite();
        proprietesParagraphe.setMarginTop(0);
        proprietesParagraphe.setMarginBottom(0);
        proprietesParagraphe.setMarginLeft(0);
        proprietesParagraphe.setMarginRight(0);
        proprietesParagraphe.setTextIndent(0);
        var styleAutomatique = paragraphe.getOdfElement().getOrCreateUnqiueAutomaticStyle();
        if (alignementHorizontal == HorizontalAlignmentType.JUSTIFY) {
            styleAutomatique.setProperty(StyleParagraphPropertiesElement.TextAlignLast, "left");
            styleAutomatique.setProperty(StyleParagraphPropertiesElement.JustifySingleWord, "false");
        } else {
            styleAutomatique.removeProperty(StyleParagraphPropertiesElement.TextAlignLast);
            styleAutomatique.removeProperty(StyleParagraphPropertiesElement.JustifySingleWord);
        }
        paragraphe.setFont(new Font("Calibri", determinerStyle(gras, italique), taillePolice <= 0 ? 11 : taillePolice, Locale.FRENCH));
    }

    private static boolean contientRetourLigne(String texte) {
        return texte != null && (texte.indexOf('\n') >= 0 || texte.indexOf('\r') >= 0);
    }

    @Override
    public void ajouterSautPage() {
        document.addPageBreak();
    }

    @Override
    public void enregistrer(File fichierSortie) throws IOException {
        Objects.requireNonNull(fichierSortie, "fichierSortie");
        try {
            document.save(fichierSortie);
        } catch (Exception ex) {
            throw new IOException("Impossible d'enregistrer le document ODT.", ex);
        }
    }

    @Override
    public void close() throws IOException {
        try {
            document.close();
        } catch (Exception ex) {
            throw new IOException("Impossible de fermer le document ODT.", ex);
        }
    }

    private static FontStyle determinerStyle(boolean gras, boolean italique) {
        if (gras && italique) {
            return FontStyle.BOLDITALIC;
        }
        if (gras) {
            return FontStyle.BOLD;
        }
        if (italique) {
            return FontStyle.ITALIC;
        }
        return FontStyle.REGULAR;
    }

    private static HorizontalAlignmentType convertirAlignement(AlignementTexte alignement) {
        if (alignement == null) {
            return HorizontalAlignmentType.LEFT;
        }
        return switch (alignement) {
            case CENTRE -> HorizontalAlignmentType.CENTER;
            case DROITE -> HorizontalAlignmentType.RIGHT;
            case JUSTIFIE -> HorizontalAlignmentType.JUSTIFY;
            case GAUCHE -> HorizontalAlignmentType.LEFT;
        };
    }
}
