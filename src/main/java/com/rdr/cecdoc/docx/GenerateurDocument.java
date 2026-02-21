package com.rdr.cecdoc.docx;

import com.rdr.cecdoc.document.modele.ModeleRequeteChangementEtatCivil;
import com.rdr.cecdoc.model.DonneesDossier;

public class GenerateurDocument extends GenerateurLettreAbstrait<DonneesDossier> {

    public GenerateurDocument() {
        super(new ModeleRequeteChangementEtatCivil());
    }
}
