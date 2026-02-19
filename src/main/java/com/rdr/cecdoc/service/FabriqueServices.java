package com.rdr.cecdoc.service;

import com.rdr.cecdoc.service.config.PersistanceEtatDossier;
import com.rdr.cecdoc.service.config.PersistanceEtatDossierProperties;
import com.rdr.cecdoc.service.export.EcritureDocxAtomique;
import com.rdr.cecdoc.service.export.ServiceExportDocument;
import com.rdr.cecdoc.service.validation.ServiceValidationDossier;

public final class FabriqueServices {
    private FabriqueServices() {
    }

    public static ServiceApplicationDossier creerServiceApplication() {
        ServiceValidationDossier validationService = new ServiceValidationDossier();
        ConvertisseurDonnees formDataMapper = new ConvertisseurDonnees();
        ServiceExportDocument exportService = new ServiceExportDocument(new EcritureDocxAtomique());
        return new OrchestratriceDossier(validationService, formDataMapper, exportService);
    }

    public static PersistanceEtatDossier creerPersistanceEtat() {
        return new PersistanceEtatDossierProperties();
    }
}
