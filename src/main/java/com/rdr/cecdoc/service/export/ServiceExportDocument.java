package com.rdr.cecdoc.service.export;

import com.rdr.cecdoc.docx.GenerateurDocument;
import com.rdr.cecdoc.model.DonneesDossier;

import java.nio.file.Path;
import java.util.Objects;

public final class ServiceExportDocument implements CasUsageExportDocument {
    private final EcritureDocxAtomique ecritureDocxAtomique;
    private final GenerateurDocument generateurDocument;
    private final ServicePdfDossierComplet servicePdfDossierComplet;

    public ServiceExportDocument(EcritureDocxAtomique ecritureDocxAtomique) {
        this(ecritureDocxAtomique, new GenerateurDocument(), new ServicePdfDossierComplet(new EcriturePdfAtomique()));
    }

    ServiceExportDocument(EcritureDocxAtomique ecritureDocxAtomique, GenerateurDocument generateurDocument, ServicePdfDossierComplet servicePdfDossierComplet) {
        this.ecritureDocxAtomique = Objects.requireNonNull(ecritureDocxAtomique, "ecritureDocxAtomique");
        this.generateurDocument = Objects.requireNonNull(generateurDocument, "generateurDocument");
        this.servicePdfDossierComplet = Objects.requireNonNull(servicePdfDossierComplet, "servicePdfDossierComplet");
    }

    @Override
    public void exporter(DonneesDossier donneesDossier, Path cheminDocxDestination, boolean ecraser, Path cheminPdfDestination) throws ErreurExportDocument {
        exporter(donneesDossier, cheminDocxDestination, ecraser, cheminPdfDestination, null);
    }

    @Override
    public void exporter(DonneesDossier donneesDossier, Path cheminDocxDestination, boolean ecraser, Path cheminPdfDestination, Path documentWordEntetePdf) throws ErreurExportDocument {
        Objects.requireNonNull(donneesDossier, "donneesDossier");
        Objects.requireNonNull(cheminDocxDestination, "cheminDocxDestination");

        ecritureDocxAtomique.ecrire(cheminDocxDestination, ecraser, cheminTemporaire -> generateurDocument.generer(donneesDossier, cheminTemporaire.toFile()));
        if (cheminPdfDestination != null) {
            servicePdfDossierComplet.exporter(donneesDossier, cheminDocxDestination, cheminPdfDestination, documentWordEntetePdf, ecraser);
        }
    }
}
