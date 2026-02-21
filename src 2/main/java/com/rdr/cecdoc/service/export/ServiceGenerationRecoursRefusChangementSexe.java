package com.rdr.cecdoc.service.export;

import com.rdr.cecdoc.docx.GenerateurRecoursRefusChangementSexe;
import com.rdr.cecdoc.model.DonneesRecoursRefusChangementSexe;
import com.rdr.cecdoc.model.TypeDocumentGenere;
import com.rdr.cecdoc.odt.GenerateurRecoursRefusChangementSexeOdt;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

public final class ServiceGenerationRecoursRefusChangementSexe extends ServiceGenerationLettreAbstrait<DonneesRecoursRefusChangementSexe> {
    private final GenerateurRecoursRefusChangementSexe generateurRecoursRefusChangementSexeDocx;
    private final GenerateurRecoursRefusChangementSexeOdt generateurRecoursRefusChangementSexeOdt;

    public ServiceGenerationRecoursRefusChangementSexe(EcritureDocxAtomique ecritureDocxAtomique) {
        this(ecritureDocxAtomique, new GenerateurRecoursRefusChangementSexe(), new GenerateurRecoursRefusChangementSexeOdt());
    }

    ServiceGenerationRecoursRefusChangementSexe(EcritureDocxAtomique ecritureDocxAtomique, GenerateurRecoursRefusChangementSexe generateurRecoursRefusChangementSexeDocx, GenerateurRecoursRefusChangementSexeOdt generateurRecoursRefusChangementSexeOdt) {
        super(ecritureDocxAtomique);
        this.generateurRecoursRefusChangementSexeDocx = Objects.requireNonNull(generateurRecoursRefusChangementSexeDocx, "generateurRecoursRefusChangementSexeDocx");
        this.generateurRecoursRefusChangementSexeOdt = Objects.requireNonNull(generateurRecoursRefusChangementSexeOdt, "generateurRecoursRefusChangementSexeOdt");
    }

    @Override
    protected void generer(DonneesRecoursRefusChangementSexe donneesRecours, TypeDocumentGenere typeDocument, Path cheminTemporaire) throws IOException {
        if (typeDocument == TypeDocumentGenere.ODT) {
            generateurRecoursRefusChangementSexeOdt.generer(donneesRecours, cheminTemporaire.toFile());
            return;
        }
        generateurRecoursRefusChangementSexeDocx.generer(donneesRecours, cheminTemporaire.toFile());
    }
}
