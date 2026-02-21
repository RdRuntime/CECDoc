package com.rdr.cecdoc.docx;

import com.rdr.cecdoc.document.modele.ModeleLettreGreffiere;
import com.rdr.cecdoc.model.DonneesDossier;

public final class GenerateurLettreGreffiere extends GenerateurLettreAbstrait<DonneesDossier> {

    public GenerateurLettreGreffiere() {
        super(new ModeleLettreGreffiere());
    }
}
