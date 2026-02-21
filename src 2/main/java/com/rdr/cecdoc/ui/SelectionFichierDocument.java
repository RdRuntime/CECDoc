package com.rdr.cecdoc.ui;

import com.rdr.cecdoc.model.TypeDocumentGenere;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.util.Locale;

final class SelectionFichierDocument {
    private SelectionFichierDocument() {
    }

    static TypeDocumentGenere typeEffectif(TypeDocumentGenere typeDocument) {
        return typeDocument == null ? TypeDocumentGenere.DOCX : typeDocument;
    }

    static FileNameExtensionFilter creerFiltre(TypeDocumentGenere typeDocument, String libelleBase) {
        TypeDocumentGenere type = typeEffectif(typeDocument);
        String libelle = libelleBase == null || libelleBase.isBlank() ? "Document" : libelleBase.trim();
        return new FileNameExtensionFilter(libelle + " (*" + type.extension() + ")", type.code());
    }

    static void appliquerFiltresDocuments(JFileChooser selecteur, String libelleBase, TypeDocumentGenere typeParDefaut) {
        if (selecteur == null) {
            return;
        }
        TypeDocumentGenere type = typeEffectif(typeParDefaut);
        FileNameExtensionFilter filtreDocx = creerFiltre(TypeDocumentGenere.DOCX, libelleBase);
        FileNameExtensionFilter filtreOdt = creerFiltre(TypeDocumentGenere.ODT, libelleBase);
        selecteur.resetChoosableFileFilters();
        selecteur.addChoosableFileFilter(filtreDocx);
        selecteur.addChoosableFileFilter(filtreOdt);
        selecteur.setFileFilter(type == TypeDocumentGenere.ODT ? filtreOdt : filtreDocx);
    }

    static String adapterNomFichier(String nomFichier, TypeDocumentGenere typeDocument) {
        TypeDocumentGenere type = typeEffectif(typeDocument);
        String base = nomFichier == null ? "" : nomFichier.trim();
        if (base.isEmpty()) {
            return "document" + type.extension();
        }
        int indexPoint = base.lastIndexOf('.');
        String racine = indexPoint > 0 ? base.substring(0, indexPoint) : base;
        return racine + type.extension();
    }

    static TypeDocumentGenere typeDepuisNomFichier(String nomFichier) {
        if (nomFichier == null || nomFichier.isBlank()) {
            return TypeDocumentGenere.DOCX;
        }
        int indexPoint = nomFichier.lastIndexOf('.');
        if (indexPoint < 0 || indexPoint >= nomFichier.length() - 1) {
            return TypeDocumentGenere.DOCX;
        }
        return TypeDocumentGenere.depuisCode(nomFichier.substring(indexPoint + 1));
    }

    static TypeDocumentGenere typeDepuisSelection(JFileChooser selecteur) {
        if (selecteur == null) {
            return TypeDocumentGenere.DOCX;
        }
        FileFilter filtreSelectionne = selecteur.getFileFilter();
        if (filtreSelectionne instanceof FileNameExtensionFilter filtreNom) {
            for (String extension : filtreNom.getExtensions()) {
                TypeDocumentGenere type = TypeDocumentGenere.depuisCode(extension);
                if (type == TypeDocumentGenere.ODT) {
                    return TypeDocumentGenere.ODT;
                }
                if (type == TypeDocumentGenere.DOCX) {
                    return TypeDocumentGenere.DOCX;
                }
            }
        }
        return typeDepuisCheminFichier(selecteur.getSelectedFile());
    }

    private static TypeDocumentGenere typeDepuisCheminFichier(File fichierSelectionne) {
        if (fichierSelectionne == null) {
            return TypeDocumentGenere.DOCX;
        }
        return typeDepuisNomFichier(fichierSelectionne.getName());
    }

    static File garantirExtension(File fichierSelectionne, TypeDocumentGenere typeDocument) {
        TypeDocumentGenere type = typeEffectif(typeDocument);
        return garantirExtension(fichierSelectionne, type.code());
    }

    static File garantirExtension(File fichierSelectionne, String extensionAttendue) {
        if (fichierSelectionne == null) {
            return null;
        }
        String extensionNormalisee = extensionAttendue == null ? "" : extensionAttendue.trim().toLowerCase(Locale.ROOT);
        if (extensionNormalisee.isEmpty()) {
            return fichierSelectionne;
        }
        String suffixe = "." + extensionNormalisee;
        String nomInitial = fichierSelectionne.getName();
        if (nomInitial.toLowerCase(Locale.ROOT).endsWith(suffixe)) {
            return fichierSelectionne;
        }
        int indexPoint = nomInitial.lastIndexOf('.');
        String racine = indexPoint > 0 ? nomInitial.substring(0, indexPoint) : nomInitial;
        String nomFinal = racine + suffixe;
        File parent = fichierSelectionne.getParentFile();
        return parent == null ? new File(nomFinal) : new File(parent, nomFinal);
    }
}
