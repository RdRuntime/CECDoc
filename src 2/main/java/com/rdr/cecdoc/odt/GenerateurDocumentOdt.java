package com.rdr.cecdoc.odt;

import com.rdr.cecdoc.document.GenerateurDocumentMultiFormat;
import com.rdr.cecdoc.document.modele.ModeleRequeteChangementEtatCivil;
import com.rdr.cecdoc.model.DonneesDossier;
import com.rdr.cecdoc.model.TypeDocumentGenere;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public final class GenerateurDocumentOdt {
    private final GenerateurDocumentMultiFormat<DonneesDossier> generateur;

    public GenerateurDocumentOdt() {
        this.generateur = new GenerateurDocumentMultiFormat<>(new ModeleRequeteChangementEtatCivil(), TypeDocumentGenere.ODT);
    }

    public void generer(DonneesDossier donnees, File fichierSortie) throws IOException {
        Objects.requireNonNull(donnees, "donnees");
        Objects.requireNonNull(fichierSortie, "fichierSortie");
        generateur.generer(donnees, fichierSortie);
    }
}
