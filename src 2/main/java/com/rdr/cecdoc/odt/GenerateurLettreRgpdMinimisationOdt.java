package com.rdr.cecdoc.odt;

import com.rdr.cecdoc.document.GenerateurDocumentMultiFormat;
import com.rdr.cecdoc.document.modele.ModeleLettreRgpdMinimisation;
import com.rdr.cecdoc.model.DonneesLettreRgpdMinimisation;
import com.rdr.cecdoc.model.TypeDocumentGenere;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public final class GenerateurLettreRgpdMinimisationOdt {
    private final GenerateurDocumentMultiFormat<DonneesLettreRgpdMinimisation> generateur;

    public GenerateurLettreRgpdMinimisationOdt() {
        this.generateur = new GenerateurDocumentMultiFormat<>(new ModeleLettreRgpdMinimisation(), TypeDocumentGenere.ODT);
    }

    public void generer(DonneesLettreRgpdMinimisation donneesLettreRgpdMinimisation, File destination) throws IOException {
        Objects.requireNonNull(donneesLettreRgpdMinimisation, "donneesLettreRgpdMinimisation");
        Objects.requireNonNull(destination, "destination");
        generateur.generer(donneesLettreRgpdMinimisation, destination);
    }
}
