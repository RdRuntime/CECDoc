package com.rdr.cecdoc.document;

import com.rdr.cecdoc.model.TypeDocumentGenere;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public final class GenerateurDocumentMultiFormat<T> {
    private final ModeleDocument<T> modeleDocument;
    private final TypeDocumentGenere typeDocument;

    public GenerateurDocumentMultiFormat(ModeleDocument<T> modeleDocument, TypeDocumentGenere typeDocument) {
        this.modeleDocument = Objects.requireNonNull(modeleDocument, "modeleDocument");
        this.typeDocument = typeDocument == null ? TypeDocumentGenere.DOCX : typeDocument;
    }

    public void generer(T donnees, File fichierSortie) throws IOException {
        Objects.requireNonNull(donnees, "donnees");
        Objects.requireNonNull(fichierSortie, "fichierSortie");
        try (RedactriceDocument redactrice = FabriqueRedactriceDocument.creer(typeDocument)) {
            modeleDocument.rediger(donnees, redactrice);
            redactrice.enregistrer(fichierSortie);
        }
    }
}
