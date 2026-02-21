package com.rdr.cecdoc.odt;

import com.rdr.cecdoc.document.GenerateurDocumentMultiFormat;
import com.rdr.cecdoc.document.modele.ModeleRecoursRefusChangementSexe;
import com.rdr.cecdoc.model.DonneesRecoursRefusChangementSexe;
import com.rdr.cecdoc.model.TypeDocumentGenere;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public final class GenerateurRecoursRefusChangementSexeOdt {
    private final GenerateurDocumentMultiFormat<DonneesRecoursRefusChangementSexe> generateur;

    public GenerateurRecoursRefusChangementSexeOdt() {
        this.generateur = new GenerateurDocumentMultiFormat<>(new ModeleRecoursRefusChangementSexe(), TypeDocumentGenere.ODT);
    }

    public void generer(DonneesRecoursRefusChangementSexe donneesRecours, File destination) throws IOException {
        Objects.requireNonNull(donneesRecours, "donneesRecours");
        Objects.requireNonNull(destination, "destination");
        generateur.generer(donneesRecours, destination);
    }
}
