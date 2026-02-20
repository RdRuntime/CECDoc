package com.rdr.cecdoc.docx;

import com.rdr.cecdoc.document.GenerateurDocumentMultiFormat;
import com.rdr.cecdoc.document.modele.ModeleLettreAdministration;
import com.rdr.cecdoc.model.DonneesLettreAdministration;
import com.rdr.cecdoc.model.TypeDocumentGenere;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public final class GenerateurLettreAdministration {
    private final GenerateurDocumentMultiFormat<DonneesLettreAdministration> generateur;

    public GenerateurLettreAdministration() {
        this.generateur = new GenerateurDocumentMultiFormat<>(new ModeleLettreAdministration(), TypeDocumentGenere.DOCX);
    }

    public void generer(DonneesLettreAdministration donneesLettreAdministration, File destination) throws IOException {
        Objects.requireNonNull(donneesLettreAdministration, "donneesLettreAdministration");
        Objects.requireNonNull(destination, "destination");
        generateur.generer(donneesLettreAdministration, destination);
    }
}
