package com.rdr.cecdoc.odt;

import com.rdr.cecdoc.document.GenerateurDocumentMultiFormat;
import com.rdr.cecdoc.document.modele.ModeleLettreRelanceMairiePrenom;
import com.rdr.cecdoc.model.DonneesLettreRelanceMairiePrenom;
import com.rdr.cecdoc.model.TypeDocumentGenere;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public final class GenerateurLettreRelanceMairiePrenomOdt {
    private final GenerateurDocumentMultiFormat<DonneesLettreRelanceMairiePrenom> generateur;

    public GenerateurLettreRelanceMairiePrenomOdt() {
        this.generateur = new GenerateurDocumentMultiFormat<>(new ModeleLettreRelanceMairiePrenom(), TypeDocumentGenere.ODT);
    }

    public void generer(DonneesLettreRelanceMairiePrenom donneesLettreRelanceMairiePrenom, File destination) throws IOException {
        Objects.requireNonNull(donneesLettreRelanceMairiePrenom, "donneesLettreRelanceMairiePrenom");
        Objects.requireNonNull(destination, "destination");
        generateur.generer(donneesLettreRelanceMairiePrenom, destination);
    }
}
