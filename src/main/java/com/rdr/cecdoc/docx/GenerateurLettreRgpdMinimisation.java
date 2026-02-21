package com.rdr.cecdoc.docx;

import com.rdr.cecdoc.document.modele.ModeleLettreRgpdMinimisation;
import com.rdr.cecdoc.model.DonneesLettreRgpdMinimisation;

public final class GenerateurLettreRgpdMinimisation extends GenerateurLettreAbstrait<DonneesLettreRgpdMinimisation> {

    public GenerateurLettreRgpdMinimisation() {
        super(new ModeleLettreRgpdMinimisation());
    }
}
