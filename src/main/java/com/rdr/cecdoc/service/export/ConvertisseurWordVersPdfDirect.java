package com.rdr.cecdoc.service.export;

import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Locale;

import org.docx4j.Docx4J;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

final class ConvertisseurWordVersPdfDirect implements ServicePdfDossierComplet.ConvertisseurWordVersPdf {
    private final ServicePdfDossierComplet.ConvertisseurWordVersPdf convertisseurSecours;

    ConvertisseurWordVersPdfDirect() {
        this(new ConvertisseurWordVersPdfLibreOffice());
    }

    ConvertisseurWordVersPdfDirect(ServicePdfDossierComplet.ConvertisseurWordVersPdf convertisseurSecours) {
        this.convertisseurSecours = convertisseurSecours;
    }

    @Override
    public void convertir(Path fichierSource, Path fichierDestination, String nomFichier) throws ErreurExportDocument {
        String extension = extraireExtension(fichierSource, nomFichier);
        if ("doc".equals(extension) || "odt".equals(extension)) {
            convertisseurSecours.convertir(fichierSource, fichierDestination, nomFichier);
            return;
        }

        if (!"docx".equals(extension)) {
            throw ErreurExportDocument.attachmentUnsupportedType(nomFichier);
        }

        convertirDocxDirectement(fichierSource, fichierDestination, nomFichier);
    }

    private void convertirDocxDirectement(Path fichierSource, Path fichierDestination, String nomFichier) throws ErreurExportDocument {
        try (OutputStream sortie = Files.newOutputStream(fichierDestination, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE)) {
            WordprocessingMLPackage documentWord = WordprocessingMLPackage.load(fichierSource.toFile());
            Docx4J.toPDF(documentWord, sortie);
        } catch (Exception ex) {
            throw ErreurExportDocument.wordConversionDirectFailed(nomFichier, ex);
        }
    }

    private static String extraireExtension(Path fichierSource, String nomFichier) {
        String nom = nomFichier;
        if (nom == null || nom.isBlank()) {
            Path nomChemin = fichierSource.getFileName();
            nom = nomChemin == null ? "" : nomChemin.toString();
        }
        int index = nom.lastIndexOf('.');
        if (index < 0 || index >= nom.length() - 1) {
            return "";
        }
        return nom.substring(index + 1).toLowerCase(Locale.ROOT);
    }
}
