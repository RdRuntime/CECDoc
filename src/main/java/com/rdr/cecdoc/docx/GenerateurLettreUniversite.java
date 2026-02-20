package com.rdr.cecdoc.docx;

import com.rdr.cecdoc.document.GenerateurDocumentMultiFormat;
import com.rdr.cecdoc.document.modele.ModeleLettreUniversite;
import com.rdr.cecdoc.model.DonneesLettreUniversite;
import com.rdr.cecdoc.model.TypeDocumentGenere;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public final class GenerateurLettreUniversite {
    private final GenerateurDocumentMultiFormat<DonneesLettreUniversite> generateur;

    public GenerateurLettreUniversite() {
        this.generateur = new GenerateurDocumentMultiFormat<>(new ModeleLettreUniversite(), TypeDocumentGenere.DOCX);
    }

    public void generer(DonneesLettreUniversite donneesLettreUniversite, File destination) throws IOException {
        Objects.requireNonNull(donneesLettreUniversite, "donneesLettreUniversite");
        Objects.requireNonNull(destination, "destination");
        generateur.generer(donneesLettreUniversite, destination);
    }
}
