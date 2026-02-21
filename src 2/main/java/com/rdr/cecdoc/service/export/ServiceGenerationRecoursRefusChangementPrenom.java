package com.rdr.cecdoc.service.export;

import com.rdr.cecdoc.docx.GenerateurRecoursRefusChangementPrenom;
import com.rdr.cecdoc.model.DonneesRecoursRefusChangementPrenom;
import com.rdr.cecdoc.model.TypeDocumentGenere;
import com.rdr.cecdoc.odt.GenerateurRecoursRefusChangementPrenomOdt;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

public final class ServiceGenerationRecoursRefusChangementPrenom extends ServiceGenerationLettreAbstrait<DonneesRecoursRefusChangementPrenom> {
    private final GenerateurRecoursRefusChangementPrenom generateurRecoursRefusChangementPrenomDocx;
    private final GenerateurRecoursRefusChangementPrenomOdt generateurRecoursRefusChangementPrenomOdt;

    public ServiceGenerationRecoursRefusChangementPrenom(EcritureDocxAtomique ecritureDocxAtomique) {
        this(ecritureDocxAtomique, new GenerateurRecoursRefusChangementPrenom(), new GenerateurRecoursRefusChangementPrenomOdt());
    }

    ServiceGenerationRecoursRefusChangementPrenom(EcritureDocxAtomique ecritureDocxAtomique, GenerateurRecoursRefusChangementPrenom generateurRecoursRefusChangementPrenomDocx, GenerateurRecoursRefusChangementPrenomOdt generateurRecoursRefusChangementPrenomOdt) {
        super(ecritureDocxAtomique);
        this.generateurRecoursRefusChangementPrenomDocx = Objects.requireNonNull(generateurRecoursRefusChangementPrenomDocx, "generateurRecoursRefusChangementPrenomDocx");
        this.generateurRecoursRefusChangementPrenomOdt = Objects.requireNonNull(generateurRecoursRefusChangementPrenomOdt, "generateurRecoursRefusChangementPrenomOdt");
    }

    @Override
    protected void generer(DonneesRecoursRefusChangementPrenom donneesRecours, TypeDocumentGenere typeDocument, Path cheminTemporaire) throws IOException {
        if (typeDocument == TypeDocumentGenere.ODT) {
            generateurRecoursRefusChangementPrenomOdt.generer(donneesRecours, cheminTemporaire.toFile());
            return;
        }
        generateurRecoursRefusChangementPrenomDocx.generer(donneesRecours, cheminTemporaire.toFile());
    }
}
