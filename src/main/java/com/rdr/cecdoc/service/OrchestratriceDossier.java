package com.rdr.cecdoc.service;

import com.rdr.cecdoc.model.ChampDossier;
import com.rdr.cecdoc.model.DonneesDossier;
import com.rdr.cecdoc.model.InstantaneDossier;
import com.rdr.cecdoc.service.export.CasUsageExportDocument;
import com.rdr.cecdoc.service.export.ErreurExportDocument;
import com.rdr.cecdoc.service.validation.CasUsageValidationDossier;
import com.rdr.cecdoc.service.validation.ProblemeValidation;
import com.rdr.cecdoc.service.validation.ResultatValidation;

import java.nio.file.Path;
import java.util.Objects;


public final class OrchestratriceDossier implements ServiceApplicationDossier {
    private final CasUsageValidationDossier validationService;
    private final CasUsageConversionDonnees formDataMapper;
    private final CasUsageExportDocument exportService;


    public OrchestratriceDossier(CasUsageValidationDossier validationService, CasUsageConversionDonnees formDataMapper, CasUsageExportDocument exportService) {
        this.validationService = Objects.requireNonNull(validationService, "validationService");
        this.formDataMapper = Objects.requireNonNull(formDataMapper, "formDataMapper");
        this.exportService = Objects.requireNonNull(exportService, "exportService");
    }


    @Override
    public ResultatValidation validerAvantGeneration(InstantaneDossier instantane) {
        return validationService.validerAvantGeneration(instantane);
    }


    @Override
    public ProblemeValidation validerChamp(InstantaneDossier instantane, ChampDossier champ) {
        return validationService.validerChamp(instantane, champ);
    }


    @Override
    public DonneesDossier construireDonneesDossier(InstantaneDossier instantane) {
        return formDataMapper.convertir(instantane);
    }


    @Override
    public void exporterDocument(DonneesDossier data, Path destinationWord, boolean overwrite, Path destinationPdf) throws ErreurExportDocument {
        exportService.exporter(data, destinationWord, overwrite, destinationPdf);
    }


    @Override
    public void exporterDocument(DonneesDossier data, Path destinationWord, boolean overwrite, Path destinationPdf, Path documentWordEntetePdf) throws ErreurExportDocument {
        exportService.exporter(data, destinationWord, overwrite, destinationPdf, documentWordEntetePdf);
    }
}
