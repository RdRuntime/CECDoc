package com.rdr.cecdoc.service.export;

import com.rdr.cecdoc.docx.GenerateurDocument;
import com.rdr.cecdoc.model.TypeDocumentGenere;
import com.rdr.cecdoc.model.DonneesDossier;
import com.rdr.cecdoc.odt.GenerateurDocumentOdt;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

public final class ServiceExportDocument implements CasUsageExportDocument {
    private final EcritureDocxAtomique ecritureDocxAtomique;
    private final GenerateurDocument generateurDocument;
    private final GenerateurDocumentOdt generateurDocumentOdt;
    private final ServicePdfDossierComplet servicePdfDossierComplet;

    public ServiceExportDocument(EcritureDocxAtomique ecritureDocxAtomique) {
        this(ecritureDocxAtomique, new GenerateurDocument(), new GenerateurDocumentOdt(), new ServicePdfDossierComplet(new EcriturePdfAtomique()));
    }

    ServiceExportDocument(EcritureDocxAtomique ecritureDocxAtomique, GenerateurDocument generateurDocument, GenerateurDocumentOdt generateurDocumentOdt, ServicePdfDossierComplet servicePdfDossierComplet) {
        this.ecritureDocxAtomique = Objects.requireNonNull(ecritureDocxAtomique, "ecritureDocxAtomique");
        this.generateurDocument = Objects.requireNonNull(generateurDocument, "generateurDocument");
        this.generateurDocumentOdt = Objects.requireNonNull(generateurDocumentOdt, "generateurDocumentOdt");
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
        TypeDocumentGenere typeDocument = typeDocumentDepuisChemin(cheminDocxDestination);

        ecritureDocxAtomique.ecrire(cheminDocxDestination, ecraser, cheminTemporaire -> genererDocument(typeDocument, donneesDossier, cheminTemporaire));
        if (cheminPdfDestination != null) {
            servicePdfDossierComplet.exporter(donneesDossier, cheminDocxDestination, cheminPdfDestination, documentWordEntetePdf, ecraser);
        }
    }

    private void genererDocument(TypeDocumentGenere typeDocument, DonneesDossier donneesDossier, Path cheminTemporaire) throws IOException {
        if (typeDocument == TypeDocumentGenere.ODT) {
            generateurDocumentOdt.generer(donneesDossier, cheminTemporaire.toFile());
            return;
        }
        generateurDocument.generer(donneesDossier, cheminTemporaire.toFile());
    }

    private static TypeDocumentGenere typeDocumentDepuisChemin(Path cheminDocument) {
        return TypeDocumentGenere.depuisChemin(cheminDocument);
    }
}
