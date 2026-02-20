package com.rdr.cecdoc.docx;

import com.rdr.cecdoc.document.GenerateurDocumentMultiFormat;
import com.rdr.cecdoc.document.modele.ModeleLettreGreffiere;
import com.rdr.cecdoc.model.DonneesDossier;
import com.rdr.cecdoc.model.TypeDocumentGenere;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public final class GenerateurLettreGreffiere {
    private final GenerateurDocumentMultiFormat<DonneesDossier> generateur;

    public GenerateurLettreGreffiere() {
        this.generateur = new GenerateurDocumentMultiFormat<>(new ModeleLettreGreffiere(), TypeDocumentGenere.DOCX);
    }

    public void generer(DonneesDossier donneesDossier, File destination) throws IOException {
        Objects.requireNonNull(donneesDossier, "donneesDossier");
        Objects.requireNonNull(destination, "destination");
        generateur.generer(donneesDossier, destination);
    }
}
