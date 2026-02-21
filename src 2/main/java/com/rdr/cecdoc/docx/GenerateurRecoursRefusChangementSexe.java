package com.rdr.cecdoc.docx;

import com.rdr.cecdoc.document.modele.ModeleRecoursRefusChangementSexe;
import com.rdr.cecdoc.model.DonneesRecoursRefusChangementSexe;

public final class GenerateurRecoursRefusChangementSexe extends GenerateurLettreAbstrait<DonneesRecoursRefusChangementSexe> {

    public GenerateurRecoursRefusChangementSexe() {
        super(new ModeleRecoursRefusChangementSexe());
    }
}
