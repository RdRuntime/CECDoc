package com.rdr.cecdoc.odt;

import com.rdr.cecdoc.document.GenerateurDocumentMultiFormat;
import com.rdr.cecdoc.document.modele.ModeleLettreMiseAJourActesLies;
import com.rdr.cecdoc.model.DonneesLettreMiseAJourActesLies;
import com.rdr.cecdoc.model.TypeDocumentGenere;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public final class GenerateurLettreMiseAJourActesLiesOdt {
    private final GenerateurDocumentMultiFormat<DonneesLettreMiseAJourActesLies> generateur;

    public GenerateurLettreMiseAJourActesLiesOdt() {
        this.generateur = new GenerateurDocumentMultiFormat<>(new ModeleLettreMiseAJourActesLies(), TypeDocumentGenere.ODT);
    }

    public void generer(DonneesLettreMiseAJourActesLies donneesLettreMiseAJourActesLies, File destination) throws IOException {
        Objects.requireNonNull(donneesLettreMiseAJourActesLies, "donneesLettreMiseAJourActesLies");
        Objects.requireNonNull(destination, "destination");
        generateur.generer(donneesLettreMiseAJourActesLies, destination);
    }
}
