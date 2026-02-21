package com.rdr.cecdoc.docx;

import com.rdr.cecdoc.document.modele.ModeleLettreRelanceMairiePrenom;
import com.rdr.cecdoc.model.DonneesLettreRelanceMairiePrenom;

public final class GenerateurLettreRelanceMairiePrenom extends GenerateurLettreAbstrait<DonneesLettreRelanceMairiePrenom> {

    public GenerateurLettreRelanceMairiePrenom() {
        super(new ModeleLettreRelanceMairiePrenom());
    }
}
