package com.rdr.cecdoc.service.export;

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

final class FusionneurPdfPdfBox implements ServicePdfDossierComplet.FusionneurPdf {

    @Override
    public void fusionner(List<Path> fichiersSources, Path fichierDestination) throws ErreurExportDocument {
        Objects.requireNonNull(fichiersSources, "fichiersSources");
        Objects.requireNonNull(fichierDestination, "fichierDestination");
        PDFMergerUtility utilitaireFusion = new PDFMergerUtility();
        try {
            if (fichiersSources.isEmpty()) {
                throw new IOException("Aucun fichier PDF source.");
            }
            Path parent = fichierDestination.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            int nombreSourcesValides = 0;
            for (Path cheminSource : fichiersSources) {
                if (cheminSource == null) {
                    continue;
                }
                if (!Files.isRegularFile(cheminSource) || !Files.isReadable(cheminSource)) {
                    throw new IOException("Fichier PDF source introuvable ou illisible : " + cheminSource);
                }
                utilitaireFusion.addSource(cheminSource.toFile());
                nombreSourcesValides++;
            }
            if (nombreSourcesValides == 0) {
                throw new IOException("Aucun fichier PDF source valide.");
            }
            utilitaireFusion.setDestinationFileName(fichierDestination.toString());
            utilitaireFusion.mergeDocuments(MemoryUsageSetting.setupTempFileOnly());
        } catch (IOException ex) {
            throw ErreurExportDocument.pdfMergeFailure(ex);
        }
    }
}
