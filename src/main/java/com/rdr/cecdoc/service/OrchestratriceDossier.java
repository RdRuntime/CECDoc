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
    private final CasUsageValidationDossier serviceValidation;
    private final CasUsageConversionDonnees convertisseurDonnees;
    private final CasUsageExportDocument serviceExport;

    public OrchestratriceDossier(CasUsageValidationDossier serviceValidation, CasUsageConversionDonnees convertisseurDonnees, CasUsageExportDocument serviceExport) {
        this.serviceValidation = Objects.requireNonNull(serviceValidation, "serviceValidation");
        this.convertisseurDonnees = Objects.requireNonNull(convertisseurDonnees, "convertisseurDonnees");
        this.serviceExport = Objects.requireNonNull(serviceExport, "serviceExport");
    }

    @Override
    public ResultatValidation validerAvantGeneration(InstantaneDossier instantane) {
        return serviceValidation.validerAvantGeneration(instantane);
    }

    @Override
    public ProblemeValidation validerChamp(InstantaneDossier instantane, ChampDossier champ) {
        return serviceValidation.validerChamp(instantane, champ);
    }

    @Override
    public DonneesDossier construireDonneesDossier(InstantaneDossier instantane) {
        return convertisseurDonnees.convertir(instantane);
    }

    @Override
    public void exporterDocument(DonneesDossier donnees, Path destinationWord, boolean ecraser, Path destinationPdf) throws ErreurExportDocument {
        serviceExport.exporter(donnees, destinationWord, ecraser, destinationPdf);
    }

    @Override
    public void exporterDocument(DonneesDossier donnees, Path destinationWord, boolean ecraser, Path destinationPdf, Path documentWordEntetePdf) throws ErreurExportDocument {
        serviceExport.exporter(donnees, destinationWord, ecraser, destinationPdf, documentWordEntetePdf);
    }
}
