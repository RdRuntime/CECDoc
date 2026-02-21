package com.rdr.cecdoc.docx;

import com.rdr.cecdoc.document.GenerateurDocumentMultiFormat;
import com.rdr.cecdoc.document.ModeleDocument;
import com.rdr.cecdoc.model.TypeDocumentGenere;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

abstract class GenerateurLettreAbstrait<T> {
    private final GenerateurDocumentMultiFormat<T> generateur;

    protected GenerateurLettreAbstrait(ModeleDocument<T> modeleDocument) {
        this.generateur = new GenerateurDocumentMultiFormat<>(Objects.requireNonNull(modeleDocument, "modeleDocument"), TypeDocumentGenere.DOCX);
    }

    public final void generer(T donnees, File destination) throws IOException {
        Objects.requireNonNull(donnees, "donnees");
        Objects.requireNonNull(destination, "destination");
        generateur.generer(donnees, destination);
    }
}
