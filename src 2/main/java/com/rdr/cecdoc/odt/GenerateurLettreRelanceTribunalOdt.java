package com.rdr.cecdoc.odt;

import com.rdr.cecdoc.document.GenerateurDocumentMultiFormat;
import com.rdr.cecdoc.document.modele.ModeleLettreRelanceTribunal;
import com.rdr.cecdoc.model.DonneesLettreRelanceTribunal;
import com.rdr.cecdoc.model.TypeDocumentGenere;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public final class GenerateurLettreRelanceTribunalOdt {
    private final GenerateurDocumentMultiFormat<DonneesLettreRelanceTribunal> generateur;

    public GenerateurLettreRelanceTribunalOdt() {
        this.generateur = new GenerateurDocumentMultiFormat<>(new ModeleLettreRelanceTribunal(), TypeDocumentGenere.ODT);
    }

    public void generer(DonneesLettreRelanceTribunal donneesLettreRelanceTribunal, File destination) throws IOException {
        Objects.requireNonNull(donneesLettreRelanceTribunal, "donneesLettreRelanceTribunal");
        Objects.requireNonNull(destination, "destination");
        generateur.generer(donneesLettreRelanceTribunal, destination);
    }
}
