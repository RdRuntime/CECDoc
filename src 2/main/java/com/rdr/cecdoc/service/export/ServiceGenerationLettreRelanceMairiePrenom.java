package com.rdr.cecdoc.service.export;

import com.rdr.cecdoc.docx.GenerateurLettreRelanceMairiePrenom;
import com.rdr.cecdoc.model.DonneesLettreRelanceMairiePrenom;
import com.rdr.cecdoc.model.TypeDocumentGenere;
import com.rdr.cecdoc.odt.GenerateurLettreRelanceMairiePrenomOdt;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

public final class ServiceGenerationLettreRelanceMairiePrenom extends ServiceGenerationLettreAbstrait<DonneesLettreRelanceMairiePrenom> {
    private final GenerateurLettreRelanceMairiePrenom generateurLettreRelanceMairiePrenomDocx;
    private final GenerateurLettreRelanceMairiePrenomOdt generateurLettreRelanceMairiePrenomOdt;

    public ServiceGenerationLettreRelanceMairiePrenom(EcritureDocxAtomique ecritureDocxAtomique) {
        this(ecritureDocxAtomique, new GenerateurLettreRelanceMairiePrenom(), new GenerateurLettreRelanceMairiePrenomOdt());
    }

    ServiceGenerationLettreRelanceMairiePrenom(EcritureDocxAtomique ecritureDocxAtomique, GenerateurLettreRelanceMairiePrenom generateurLettreRelanceMairiePrenomDocx, GenerateurLettreRelanceMairiePrenomOdt generateurLettreRelanceMairiePrenomOdt) {
        super(ecritureDocxAtomique);
        this.generateurLettreRelanceMairiePrenomDocx = Objects.requireNonNull(generateurLettreRelanceMairiePrenomDocx, "generateurLettreRelanceMairiePrenomDocx");
        this.generateurLettreRelanceMairiePrenomOdt = Objects.requireNonNull(generateurLettreRelanceMairiePrenomOdt, "generateurLettreRelanceMairiePrenomOdt");
    }

    @Override
    protected void generer(DonneesLettreRelanceMairiePrenom donneesLettreRelanceMairiePrenom, TypeDocumentGenere typeDocument, Path cheminTemporaire) throws IOException {
        if (typeDocument == TypeDocumentGenere.ODT) {
            generateurLettreRelanceMairiePrenomOdt.generer(donneesLettreRelanceMairiePrenom, cheminTemporaire.toFile());
            return;
        }
        generateurLettreRelanceMairiePrenomDocx.generer(donneesLettreRelanceMairiePrenom, cheminTemporaire.toFile());
    }
}
