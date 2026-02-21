package com.rdr.cecdoc.ui;

import javax.swing.*;
import java.io.Serial;

abstract class FenetreFormulaireAbstraite extends JFrame {
    @Serial
    private static final long serialVersionUID = 1L;

    protected FenetreFormulaireAbstraite(String titre) {
        super(titre);
    }
}
