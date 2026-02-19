package com.rdr.cecdoc.ui;

import java.io.Serial;

import javax.swing.JFrame;

abstract class FenetreFormulaireAbstraite extends JFrame {
    @Serial
    private static final long serialVersionUID = 1L;

    protected FenetreFormulaireAbstraite(String titre) {
        super(titre);
    }
}
