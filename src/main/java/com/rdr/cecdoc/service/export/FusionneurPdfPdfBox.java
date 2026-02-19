package com.rdr.cecdoc.service.export;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;

final class FusionneurPdfPdfBox implements ServicePdfDossierComplet.FusionneurPdf {

    @Override
    public void fusionner(List<Path> fichiersSources, Path fichierDestination) throws ErreurExportDocument {
        PDFMergerUtility utilitaireFusion = new PDFMergerUtility();
        try {
            for (Path cheminSource : fichiersSources) {
                utilitaireFusion.addSource(cheminSource.toFile());
            }
            utilitaireFusion.setDestinationFileName(fichierDestination.toString());
            utilitaireFusion.mergeDocuments(MemoryUsageSetting.setupTempFileOnly());
        } catch (IOException ex) {
            throw ErreurExportDocument.pdfMergeFailure(ex);
        }
    }
}
