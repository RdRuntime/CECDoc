package com.rdr.cecdoc.service.export;

import com.rdr.cecdoc.model.DonneesDossier;

import java.nio.file.Path;

public interface CasUsageExportDocument {
    default void exporter(DonneesDossier donnees, Path destinationWord, boolean ecraser) throws ErreurExportDocument {
        exporter(donnees, destinationWord, ecraser, null);
    }

    default void exporter(DonneesDossier donnees, Path destinationWord, boolean ecraser, Path destinationPdf, Path documentWordEntetePdf) throws ErreurExportDocument {
        exporter(donnees, destinationWord, ecraser, destinationPdf);
    }

    void exporter(DonneesDossier donnees, Path destinationWord, boolean ecraser, Path destinationPdf) throws ErreurExportDocument;
}
