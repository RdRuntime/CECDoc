package com.rdr.cecdoc.service.export;

import com.rdr.cecdoc.docx.GenerateurLettreMiseAJourActesLies;
import com.rdr.cecdoc.model.DonneesLettreMiseAJourActesLies;
import com.rdr.cecdoc.model.TypeDocumentGenere;
import com.rdr.cecdoc.odt.GenerateurLettreMiseAJourActesLiesOdt;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

public final class ServiceGenerationLettreMiseAJourActesLies extends ServiceGenerationLettreAbstrait<DonneesLettreMiseAJourActesLies> {
    private final GenerateurLettreMiseAJourActesLies generateurDocx;
    private final GenerateurLettreMiseAJourActesLiesOdt generateurOdt;

    public ServiceGenerationLettreMiseAJourActesLies(EcritureDocxAtomique ecritureDocxAtomique) {
        this(ecritureDocxAtomique, new GenerateurLettreMiseAJourActesLies(), new GenerateurLettreMiseAJourActesLiesOdt());
    }

    ServiceGenerationLettreMiseAJourActesLies(EcritureDocxAtomique ecritureDocxAtomique, GenerateurLettreMiseAJourActesLies generateurDocx, GenerateurLettreMiseAJourActesLiesOdt generateurOdt) {
        super(ecritureDocxAtomique);
        this.generateurDocx = Objects.requireNonNull(generateurDocx, "generateurDocx");
        this.generateurOdt = Objects.requireNonNull(generateurOdt, "generateurOdt");
    }

    @Override
    protected void generer(DonneesLettreMiseAJourActesLies donneesLettreMiseAJourActesLies, TypeDocumentGenere typeDocument, Path cheminTemporaire) throws IOException {
        if (typeDocument == TypeDocumentGenere.ODT) {
            generateurOdt.generer(donneesLettreMiseAJourActesLies, cheminTemporaire.toFile());
            return;
        }
        generateurDocx.generer(donneesLettreMiseAJourActesLies, cheminTemporaire.toFile());
    }
}
