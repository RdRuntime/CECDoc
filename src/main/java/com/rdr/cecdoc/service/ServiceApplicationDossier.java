package com.rdr.cecdoc.service;

import com.rdr.cecdoc.model.ChampDossier;
import com.rdr.cecdoc.model.DonneesDossier;
import com.rdr.cecdoc.model.InstantaneDossier;
import com.rdr.cecdoc.service.export.ErreurExportDocument;
import com.rdr.cecdoc.service.validation.ProblemeValidation;
import com.rdr.cecdoc.service.validation.ResultatValidation;

import java.nio.file.Path;

public interface ServiceApplicationDossier {
    ResultatValidation validerAvantGeneration(InstantaneDossier instantane);

    ProblemeValidation validerChamp(InstantaneDossier instantane, ChampDossier champ);

    DonneesDossier construireDonneesDossier(InstantaneDossier instantane);

    default void exporterDocument(DonneesDossier donnees, Path destinationWord, boolean ecraser, Path destinationPdf, Path documentWordEntetePdf) throws ErreurExportDocument {
        exporterDocument(donnees, destinationWord, ecraser, destinationPdf);
    }

    void exporterDocument(DonneesDossier donnees, Path destinationWord, boolean ecraser, Path destinationPdf) throws ErreurExportDocument;
}
