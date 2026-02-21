package com.rdr.cecdoc.docx;

import com.rdr.cecdoc.document.modele.ModeleLettreAdministration;
import com.rdr.cecdoc.model.DonneesLettreAdministration;

public final class GenerateurLettreAdministration extends GenerateurLettreAbstrait<DonneesLettreAdministration> {

    public GenerateurLettreAdministration() {
        super(new ModeleLettreAdministration());
    }
}
