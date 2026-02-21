package com.rdr.cecdoc.document.rendu;

import com.rdr.cecdoc.document.AlignementTexte;
import com.rdr.cecdoc.document.RedactriceDocument;
import org.odftoolkit.odfdom.dom.element.style.StyleParagraphPropertiesElement;
import org.odftoolkit.simple.TextDocument;
import org.odftoolkit.simple.style.Font;
import org.odftoolkit.simple.style.ParagraphProperties;
import org.odftoolkit.simple.style.StyleTypeDefinitions.FontStyle;
import org.odftoolkit.simple.style.StyleTypeDefinitions.HorizontalAlignmentType;
import org.odftoolkit.simple.text.Paragraph;
import org.odftoolkit.simple.text.list.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public final class RedactriceOdt implements RedactriceDocument {
    private static final System.Logger JOURNAL = System.getLogger(RedactriceOdt.class.getName());
    private final TextDocument document;
    private final java.util.List<org.odftoolkit.simple.text.list.List> listesPucesActives;
    private final java.util.List<ListItem> derniersItemsPuces;

    public RedactriceOdt() throws IOException {
        try {
            this.document = TextDocument.newTextDocument();
            this.listesPucesActives = new ArrayList<>();
            this.derniersItemsPuces = new ArrayList<>();
        } catch (Exception ex) {
            JOURNAL.log(System.Logger.Level.WARNING, "Impossible d'initialiser le document ODT.", ex);
            throw new IOException("Impossible d'initialiser le document ODT.", ex);
        }
    }

    private static boolean contientRetourLigne(String texte) {
        return texte != null && (texte.indexOf('\n') >= 0 || texte.indexOf('\r') >= 0);
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

    @Override
    public void ajouterParagraphe(AlignementTexte alignement, String texte, boolean gras, boolean italique, int taillePolice, boolean sautPageAvant) {
        reinitialiserListesPuces();
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

    @Override
    public void ajouterPuce(AlignementTexte alignement, int niveau, String texte, boolean gras, boolean italique, int taillePolice, boolean sautPageAvant) {
        int profondeurSouhaitee = Math.max(1, niveau);
        if (sautPageAvant) {
            document.addPageBreak();
            reinitialiserListesPuces();
        }
        int profondeur = profondeurSouhaitee;
        if (profondeur > 1 && !parentPuceDisponible(profondeur - 1)) {
            profondeur = 1;
        }
        tronquerListesActives(profondeur);
        while (listesPucesActives.size() < profondeur) {
            int niveauCreation = listesPucesActives.size() + 1;
            org.odftoolkit.simple.text.list.List liste;
            if (niveauCreation == 1) {
                liste = document.addList(decorateurPuce(niveauCreation));
            } else {
                ListItem parentItem = derniersItemsPuces.get(niveauCreation - 2);
                if (parentItem == null) {
                    liste = document.addList(decorateurPuce(1));
                    reinitialiserListesPuces();
                    listesPucesActives.add(liste);
                    derniersItemsPuces.add(null);
                    profondeur = 1;
                    break;
                }
                liste = parentItem.addList(decorateurPuce(niveauCreation));
            }
            listesPucesActives.add(liste);
            derniersItemsPuces.add(null);
        }
        org.odftoolkit.simple.text.list.List listeCible = listesPucesActives.get(profondeur - 1);
        ListItem item = listeCible.addItem(texte == null ? "" : texte);
        while (derniersItemsPuces.size() < profondeur) {
            derniersItemsPuces.add(null);
        }
        derniersItemsPuces.set(profondeur - 1, item);
        for (int index = profondeur; index < derniersItemsPuces.size(); index++) {
            derniersItemsPuces.set(index, null);
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

    @Override
    public void ajouterSautPage() {
        reinitialiserListesPuces();
        document.addPageBreak();
    }

    @Override
    public void enregistrer(File fichierSortie) throws IOException {
        Objects.requireNonNull(fichierSortie, "fichierSortie");
        File parent = fichierSortie.getParentFile();
        if (parent != null && !parent.exists() && !parent.mkdirs() && !parent.isDirectory()) {
            throw new IOException("Impossible de créer le dossier de sortie ODT : " + parent);
        }
        try {
            document.save(fichierSortie);
        } catch (Exception ex) {
            JOURNAL.log(System.Logger.Level.WARNING, "Impossible d'enregistrer le document ODT : " + fichierSortie, ex);
            throw new IOException("Impossible d'enregistrer le document ODT.", ex);
        }
    }

    @Override
    public void close() throws IOException {
        try {
            document.close();
        } catch (Exception ex) {
            JOURNAL.log(System.Logger.Level.WARNING, "Impossible de fermer le document ODT.", ex);
            throw new IOException("Impossible de fermer le document ODT.", ex);
        }
    }

    private void reinitialiserListesPuces() {
        listesPucesActives.clear();
        derniersItemsPuces.clear();
    }

    private boolean parentPuceDisponible(int niveauParent) {
        int index = niveauParent - 1;
        return index >= 0 && index < derniersItemsPuces.size() && derniersItemsPuces.get(index) != null;
    }

    private void tronquerListesActives(int profondeur) {
        if (profondeur < 1) {
            reinitialiserListesPuces();
            return;
        }
        if (listesPucesActives.size() > profondeur) {
            listesPucesActives.subList(profondeur, listesPucesActives.size()).clear();
        }
        if (derniersItemsPuces.size() > profondeur) {
            derniersItemsPuces.subList(profondeur, derniersItemsPuces.size()).clear();
        }
    }

    private ListDecorator decorateurPuce(int niveau) {
        int modulo = Math.floorMod(niveau - 1, 3);
        if (modulo == 1) {
            return new DiscDecorator(document);
        }
        if (modulo == 2) {
            return new SquareDecorator(document);
        }
        return new BulletDecorator(document);
    }
}
