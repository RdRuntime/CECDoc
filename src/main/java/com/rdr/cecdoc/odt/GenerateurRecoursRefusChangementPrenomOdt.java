package com.rdr.cecdoc.odt;

import com.rdr.cecdoc.document.GenerateurDocumentMultiFormat;
import com.rdr.cecdoc.document.modele.ModeleRecoursRefusChangementPrenom;
import com.rdr.cecdoc.model.DonneesRecoursRefusChangementPrenom;
import com.rdr.cecdoc.model.TypeDocumentGenere;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public final class GenerateurRecoursRefusChangementPrenomOdt {
    private final GenerateurDocumentMultiFormat<DonneesRecoursRefusChangementPrenom> generateur;

    public GenerateurRecoursRefusChangementPrenomOdt() {
        this.generateur = new GenerateurDocumentMultiFormat<>(new ModeleRecoursRefusChangementPrenom(), TypeDocumentGenere.ODT);
    }

    public void generer(DonneesRecoursRefusChangementPrenom donneesRecours, File destination) throws IOException {
        Objects.requireNonNull(donneesRecours, "donneesRecours");
        Objects.requireNonNull(destination, "destination");
        generateur.generer(donneesRecours, destination);
    }
}
