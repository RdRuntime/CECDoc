package com.rdr.cecdoc.service.export;

import com.rdr.cecdoc.docx.GenerateurLettreAdministration;
import com.rdr.cecdoc.model.DonneesLettreAdministration;
import com.rdr.cecdoc.model.TypeDocumentGenere;
import com.rdr.cecdoc.odt.GenerateurLettreAdministrationOdt;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

public final class ServiceGenerationLettreAdministration extends ServiceGenerationLettreAbstrait<DonneesLettreAdministration> {
    private final GenerateurLettreAdministration generateurLettreAdministrationDocx;
    private final GenerateurLettreAdministrationOdt generateurLettreAdministrationOdt;

    public ServiceGenerationLettreAdministration(EcritureDocxAtomique ecritureDocxAtomique) {
        this(ecritureDocxAtomique, new GenerateurLettreAdministration(), new GenerateurLettreAdministrationOdt());
    }

    ServiceGenerationLettreAdministration(EcritureDocxAtomique ecritureDocxAtomique, GenerateurLettreAdministration generateurLettreAdministration, GenerateurLettreAdministrationOdt generateurLettreAdministrationOdt) {
        super(ecritureDocxAtomique);
        this.generateurLettreAdministrationDocx = Objects.requireNonNull(generateurLettreAdministration, "generateurLettreAdministration");
        this.generateurLettreAdministrationOdt = Objects.requireNonNull(generateurLettreAdministrationOdt, "generateurLettreAdministrationOdt");
    }

    @Override
    protected void generer(DonneesLettreAdministration donneesLettreAdministration, TypeDocumentGenere typeDocument, Path cheminTemporaire) throws IOException {
        if (typeDocument == TypeDocumentGenere.ODT) {
            generateurLettreAdministrationOdt.generer(donneesLettreAdministration, cheminTemporaire.toFile());
            return;
        }
        generateurLettreAdministrationDocx.generer(donneesLettreAdministration, cheminTemporaire.toFile());
    }
}
