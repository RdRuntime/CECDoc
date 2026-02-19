package com.rdr.cecdoc.service.export;

import java.nio.file.Path;

import org.jodconverter.core.office.OfficeException;
import org.jodconverter.local.LocalConverter;
import org.jodconverter.local.office.LocalOfficeManager;

final class ConvertisseurWordVersPdfLibreOffice implements ServicePdfDossierComplet.ConvertisseurWordVersPdf {

    @Override
    public void convertir(Path fichierSource, Path fichierDestination, String nomFichier) throws ErreurExportDocument {
        LocalOfficeManager gestionnaireBureautique = null;
        try {
            gestionnaireBureautique = LocalOfficeManager.builder().build();
            gestionnaireBureautique.start();
            LocalConverter.make(gestionnaireBureautique).convert(fichierSource.toFile()).to(fichierDestination.toFile()).execute();
        } catch (OfficeException | RuntimeException ex) {
            throw ErreurExportDocument.wordConversionDependencyMissing(nomFichier, ex);
        } finally {
            if (gestionnaireBureautique != null) {
                try {
                    gestionnaireBureautique.stop();
                } catch (OfficeException ex) {
                    System.getLogger(ConvertisseurWordVersPdfLibreOffice.class.getName()).log(System.Logger.Level.WARNING, "Impossible d'arrêter proprement LibreOffice.", ex);
                }
            }
        }
    }
}
