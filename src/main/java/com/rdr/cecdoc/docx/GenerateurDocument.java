package com.rdr.cecdoc.docx;

import com.rdr.cecdoc.document.GenerateurDocumentMultiFormat;
import com.rdr.cecdoc.document.modele.ModeleRequeteChangementEtatCivil;
import com.rdr.cecdoc.model.DonneesDossier;
import com.rdr.cecdoc.model.TypeDocumentGenere;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class GenerateurDocument {
    private final GenerateurDocumentMultiFormat<DonneesDossier> generateur;

    public GenerateurDocument() {
        this.generateur = new GenerateurDocumentMultiFormat<>(new ModeleRequeteChangementEtatCivil(), TypeDocumentGenere.DOCX);
    }

    public void generer(DonneesDossier data, File file) throws IOException {
        Objects.requireNonNull(data, "data");
        Objects.requireNonNull(file, "file");
        generateur.generer(data, file);
    }
}
