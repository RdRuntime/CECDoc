package com.rdr.cecdoc.docx;

import com.rdr.cecdoc.document.modele.ModeleLettreRelanceTribunal;
import com.rdr.cecdoc.model.DonneesLettreRelanceTribunal;

public final class GenerateurLettreRelanceTribunal extends GenerateurLettreAbstrait<DonneesLettreRelanceTribunal> {

    public GenerateurLettreRelanceTribunal() {
        super(new ModeleLettreRelanceTribunal());
    }
}
