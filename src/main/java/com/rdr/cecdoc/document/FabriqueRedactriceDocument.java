package com.rdr.cecdoc.document;

import com.rdr.cecdoc.document.rendu.RedactriceDocx;
import com.rdr.cecdoc.document.rendu.RedactriceOdt;
import com.rdr.cecdoc.model.TypeDocumentGenere;

import java.io.IOException;

final class FabriqueRedactriceDocument {
    private FabriqueRedactriceDocument() {
    }

    static RedactriceDocument creer(TypeDocumentGenere typeDocumentGenere) throws IOException {
        TypeDocumentGenere type = typeDocumentGenere == null ? TypeDocumentGenere.DOCX : typeDocumentGenere;
        if (type == TypeDocumentGenere.ODT) {
            return new RedactriceOdt();
        }
        return new RedactriceDocx();
    }
}
