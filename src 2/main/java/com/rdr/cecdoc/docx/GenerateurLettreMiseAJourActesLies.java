package com.rdr.cecdoc.docx;

import com.rdr.cecdoc.document.modele.ModeleLettreMiseAJourActesLies;
import com.rdr.cecdoc.model.DonneesLettreMiseAJourActesLies;

public final class GenerateurLettreMiseAJourActesLies extends GenerateurLettreAbstrait<DonneesLettreMiseAJourActesLies> {

    public GenerateurLettreMiseAJourActesLies() {
        super(new ModeleLettreMiseAJourActesLies());
    }
}
