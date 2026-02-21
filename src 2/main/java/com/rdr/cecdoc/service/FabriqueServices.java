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
        ServiceValidationDossier serviceValidation = new ServiceValidationDossier();
        ConvertisseurDonnees convertisseurDonnees = new ConvertisseurDonnees();
        ServiceExportDocument serviceExport = new ServiceExportDocument(new EcritureDocxAtomique());
        return new OrchestratriceDossier(serviceValidation, convertisseurDonnees, serviceExport);
    }

    public static PersistanceEtatDossier creerPersistanceEtat() {
        return new PersistanceEtatDossierProperties();
    }
}
