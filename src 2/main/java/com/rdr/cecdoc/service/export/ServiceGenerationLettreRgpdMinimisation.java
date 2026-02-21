package com.rdr.cecdoc.service.export;

import com.rdr.cecdoc.docx.GenerateurLettreRgpdMinimisation;
import com.rdr.cecdoc.model.DonneesLettreRgpdMinimisation;
import com.rdr.cecdoc.model.TypeDocumentGenere;
import com.rdr.cecdoc.odt.GenerateurLettreRgpdMinimisationOdt;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

public final class ServiceGenerationLettreRgpdMinimisation extends ServiceGenerationLettreAbstrait<DonneesLettreRgpdMinimisation> {
    private final GenerateurLettreRgpdMinimisation generateurLettreRgpdMinimisationDocx;
    private final GenerateurLettreRgpdMinimisationOdt generateurLettreRgpdMinimisationOdt;

    public ServiceGenerationLettreRgpdMinimisation(EcritureDocxAtomique ecritureDocxAtomique) {
        this(ecritureDocxAtomique, new GenerateurLettreRgpdMinimisation(), new GenerateurLettreRgpdMinimisationOdt());
    }

    ServiceGenerationLettreRgpdMinimisation(EcritureDocxAtomique ecritureDocxAtomique, GenerateurLettreRgpdMinimisation generateurLettreRgpdMinimisationDocx, GenerateurLettreRgpdMinimisationOdt generateurLettreRgpdMinimisationOdt) {
        super(ecritureDocxAtomique);
        this.generateurLettreRgpdMinimisationDocx = Objects.requireNonNull(generateurLettreRgpdMinimisationDocx, "generateurLettreRgpdMinimisationDocx");
        this.generateurLettreRgpdMinimisationOdt = Objects.requireNonNull(generateurLettreRgpdMinimisationOdt, "generateurLettreRgpdMinimisationOdt");
    }

    @Override
    protected void generer(DonneesLettreRgpdMinimisation donneesLettreRgpdMinimisation, TypeDocumentGenere typeDocument, Path cheminTemporaire) throws IOException {
        if (typeDocument == TypeDocumentGenere.ODT) {
            generateurLettreRgpdMinimisationOdt.generer(donneesLettreRgpdMinimisation, cheminTemporaire.toFile());
            return;
        }
        generateurLettreRgpdMinimisationDocx.generer(donneesLettreRgpdMinimisation, cheminTemporaire.toFile());
    }
}
