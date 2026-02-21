package com.rdr.cecdoc.service.export;

import org.docx4j.Docx4J;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Locale;
import java.util.Objects;

final class ConvertisseurWordVersPdfDirect implements ServicePdfDossierComplet.ConvertisseurWordVersPdf {
    private final ServicePdfDossierComplet.ConvertisseurWordVersPdf convertisseurSecours;

    ConvertisseurWordVersPdfDirect() {
        this(new ConvertisseurWordVersPdfLibreOffice());
    }

    ConvertisseurWordVersPdfDirect(ServicePdfDossierComplet.ConvertisseurWordVersPdf convertisseurSecours) {
        this.convertisseurSecours = Objects.requireNonNull(convertisseurSecours, "convertisseurSecours");
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

    @Override
    public void convertir(Path fichierSource, Path fichierDestination, String nomFichier) throws ErreurExportDocument {
        Objects.requireNonNull(fichierSource, "fichierSource");
        Objects.requireNonNull(fichierDestination, "fichierDestination");
        String nomEffectif = nomFichier == null || nomFichier.isBlank() ? String.valueOf(fichierSource.getFileName()) : nomFichier;
        if (!Files.isRegularFile(fichierSource) || !Files.isReadable(fichierSource)) {
            throw ErreurExportDocument.attachmentMissing(nomEffectif);
        }
        String extension = extraireExtension(fichierSource, nomEffectif);
        if ("doc".equals(extension) || "odt".equals(extension)) {
            convertisseurSecours.convertir(fichierSource, fichierDestination, nomEffectif);
            return;
        }

        if (!"docx".equals(extension)) {
            throw ErreurExportDocument.attachmentUnsupportedType(nomEffectif);
        }

        convertirDocxDirectement(fichierSource, fichierDestination, nomEffectif);
    }

    private void convertirDocxDirectement(Path fichierSource, Path fichierDestination, String nomFichier) throws ErreurExportDocument {
        try {
            Path repertoireParent = fichierDestination.getParent();
            if (repertoireParent != null) {
                Files.createDirectories(repertoireParent);
            }
        } catch (IOException | RuntimeException ex) {
            throw ErreurExportDocument.wordConversionDirectFailed(nomFichier, ex);
        }
        try (OutputStream sortie = Files.newOutputStream(fichierDestination, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE)) {
            WordprocessingMLPackage documentWord = WordprocessingMLPackage.load(fichierSource.toFile());
            Docx4J.toPDF(documentWord, sortie);
        } catch (IOException | Docx4JException | RuntimeException ex) {
            throw ErreurExportDocument.wordConversionDirectFailed(nomFichier, ex);
        }
    }
}
