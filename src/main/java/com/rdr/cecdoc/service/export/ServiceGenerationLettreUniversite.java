package com.rdr.cecdoc.service.export;

import com.rdr.cecdoc.docx.GenerateurLettreUniversite;
import com.rdr.cecdoc.model.DonneesLettreUniversite;
import com.rdr.cecdoc.model.TypeDocumentGenere;
import com.rdr.cecdoc.odt.GenerateurLettreUniversiteOdt;

import java.nio.file.Path;
import java.io.IOException;
import java.util.Objects;

public final class ServiceGenerationLettreUniversite extends ServiceGenerationLettreAbstrait<DonneesLettreUniversite> {
    private final GenerateurLettreUniversite generateurLettreUniversiteDocx;
    private final GenerateurLettreUniversiteOdt generateurLettreUniversiteOdt;

    public ServiceGenerationLettreUniversite(EcritureDocxAtomique ecritureDocxAtomique) {
        this(ecritureDocxAtomique, new GenerateurLettreUniversite(), new GenerateurLettreUniversiteOdt());
    }

    ServiceGenerationLettreUniversite(EcritureDocxAtomique ecritureDocxAtomique, GenerateurLettreUniversite generateurLettreUniversite, GenerateurLettreUniversiteOdt generateurLettreUniversiteOdt) {
        super(ecritureDocxAtomique);
        this.generateurLettreUniversiteDocx = Objects.requireNonNull(generateurLettreUniversite, "generateurLettreUniversite");
        this.generateurLettreUniversiteOdt = Objects.requireNonNull(generateurLettreUniversiteOdt, "generateurLettreUniversiteOdt");
    }

    @Override
    protected void generer(DonneesLettreUniversite donneesLettreUniversite, TypeDocumentGenere typeDocument, Path cheminTemporaire) throws IOException {
        if (typeDocument == TypeDocumentGenere.ODT) {
            generateurLettreUniversiteOdt.generer(donneesLettreUniversite, cheminTemporaire.toFile());
            return;
        }
        generateurLettreUniversiteDocx.generer(donneesLettreUniversite, cheminTemporaire.toFile());
    }
}
