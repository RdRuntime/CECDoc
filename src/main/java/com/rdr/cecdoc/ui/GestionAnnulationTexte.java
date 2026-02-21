package com.rdr.cecdoc.ui;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

final class GestionAnnulationTexte {
    private static final String CLE_GESTIONNAIRE = "cecdoc.undo.manager";
    private static final String ACTION_ANNULER = "cecdoc-annuler-saisie";
    private static final String ACTION_RETABLIR = "cecdoc-retablir-saisie";

    private GestionAnnulationTexte() {
    }

    static void activer(JTextComponent composantTexte) {
        if (composantTexte == null || composantTexte.getClientProperty(CLE_GESTIONNAIRE) != null) {
            return;
        }

        UndoManager gestionnaire = new UndoManager();
        composantTexte.getDocument().addUndoableEditListener(event -> gestionnaire.addEdit(event.getEdit()));
        composantTexte.putClientProperty(CLE_GESTIONNAIRE, gestionnaire);

        int masqueRaccourci = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();
        InputMap carteEntrees = composantTexte.getInputMap(JComponent.WHEN_FOCUSED);
        ActionMap carteActions = composantTexte.getActionMap();

        carteEntrees.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, masqueRaccourci), ACTION_ANNULER);
        carteEntrees.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, masqueRaccourci | InputEvent.SHIFT_DOWN_MASK), ACTION_RETABLIR);
        carteEntrees.put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, masqueRaccourci), ACTION_RETABLIR);

        carteActions.put(ACTION_ANNULER, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!composantTexte.isEditable()) {
                    return;
                }
                try {
                    if (gestionnaire.canUndo()) {
                        gestionnaire.undo();
                    }
                } catch (CannotUndoException ex) {
                    Toolkit.getDefaultToolkit().beep();
                }
            }
        });

        carteActions.put(ACTION_RETABLIR, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!composantTexte.isEditable()) {
                    return;
                }
                try {
                    if (gestionnaire.canRedo()) {
                        gestionnaire.redo();
                    }
                } catch (CannotRedoException ex) {
                    Toolkit.getDefaultToolkit().beep();
                }
            }
        });
    }
}
