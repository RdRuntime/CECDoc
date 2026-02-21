package com.rdr.cecdoc.service.export;

import com.rdr.cecdoc.docx.GenerateurLettreRelanceTribunal;
import com.rdr.cecdoc.model.DonneesLettreRelanceTribunal;
import com.rdr.cecdoc.model.TypeDocumentGenere;
import com.rdr.cecdoc.odt.GenerateurLettreRelanceTribunalOdt;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

public final class ServiceGenerationLettreRelanceTribunal extends ServiceGenerationLettreAbstrait<DonneesLettreRelanceTribunal> {
    private final GenerateurLettreRelanceTribunal generateurLettreRelanceTribunalDocx;
    private final GenerateurLettreRelanceTribunalOdt generateurLettreRelanceTribunalOdt;

    public ServiceGenerationLettreRelanceTribunal(EcritureDocxAtomique ecritureDocxAtomique) {
        this(ecritureDocxAtomique, new GenerateurLettreRelanceTribunal(), new GenerateurLettreRelanceTribunalOdt());
    }

    ServiceGenerationLettreRelanceTribunal(EcritureDocxAtomique ecritureDocxAtomique, GenerateurLettreRelanceTribunal generateurLettreRelanceTribunalDocx, GenerateurLettreRelanceTribunalOdt generateurLettreRelanceTribunalOdt) {
        super(ecritureDocxAtomique);
        this.generateurLettreRelanceTribunalDocx = Objects.requireNonNull(generateurLettreRelanceTribunalDocx, "generateurLettreRelanceTribunalDocx");
        this.generateurLettreRelanceTribunalOdt = Objects.requireNonNull(generateurLettreRelanceTribunalOdt, "generateurLettreRelanceTribunalOdt");
    }

    @Override
    protected void generer(DonneesLettreRelanceTribunal donneesLettreRelanceTribunal, TypeDocumentGenere typeDocument, Path cheminTemporaire) throws IOException {
        if (typeDocument == TypeDocumentGenere.ODT) {
            generateurLettreRelanceTribunalOdt.generer(donneesLettreRelanceTribunal, cheminTemporaire.toFile());
            return;
        }
        generateurLettreRelanceTribunalDocx.generer(donneesLettreRelanceTribunal, cheminTemporaire.toFile());
    }
}
