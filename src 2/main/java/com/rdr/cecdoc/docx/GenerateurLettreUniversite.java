package com.rdr.cecdoc.docx;

import com.rdr.cecdoc.document.modele.ModeleLettreUniversite;
import com.rdr.cecdoc.model.DonneesLettreUniversite;

public final class GenerateurLettreUniversite extends GenerateurLettreAbstrait<DonneesLettreUniversite> {

    public GenerateurLettreUniversite() {
        super(new ModeleLettreUniversite());
    }
}
