package com.rdr.cecdoc.docx;

import com.rdr.cecdoc.document.modele.ModeleRecoursRefusChangementPrenom;
import com.rdr.cecdoc.model.DonneesRecoursRefusChangementPrenom;

public final class GenerateurRecoursRefusChangementPrenom extends GenerateurLettreAbstrait<DonneesRecoursRefusChangementPrenom> {

    public GenerateurRecoursRefusChangementPrenom() {
        super(new ModeleRecoursRefusChangementPrenom());
    }
}
