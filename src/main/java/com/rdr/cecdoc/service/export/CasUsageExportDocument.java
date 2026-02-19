package com.rdr.cecdoc.service.export;

import com.rdr.cecdoc.model.DonneesDossier;

import java.nio.file.Path;


public interface CasUsageExportDocument {


    default void exporter(DonneesDossier data, Path destination, boolean overwrite) throws ErreurExportDocument {
        exporter(data, destination, overwrite, null);
    }


    default void exporter(DonneesDossier data, Path destinationWord, boolean overwrite, Path destinationPdf, Path documentWordEntetePdf) throws ErreurExportDocument {
        exporter(data, destinationWord, overwrite, destinationPdf);
    }


    void exporter(DonneesDossier data, Path destinationWord, boolean overwrite, Path destinationPdf) throws ErreurExportDocument;
}
