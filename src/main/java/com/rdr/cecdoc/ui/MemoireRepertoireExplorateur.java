package com.rdr.cecdoc.ui;

import javax.swing.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

final class MemoireRepertoireExplorateur {
    private static Path dernierRepertoire;

    private MemoireRepertoireExplorateur() {
    }

    static void appliquerAuSelecteur(JFileChooser selecteur, Path repertoireParDefaut) {
        if (selecteur == null) {
            return;
        }
        Path repertoireInitial = repertoireValide(dernierRepertoire);
        if (repertoireInitial == null) {
            repertoireInitial = repertoireValide(repertoireParDefaut);
        }
        if (repertoireInitial != null) {
            selecteur.setCurrentDirectory(repertoireInitial.toFile());
        }
    }

    static void memoriserDepuisSelection(JFileChooser selecteur) {
        if (selecteur == null) {
            return;
        }
        File selection = selecteur.getSelectedFile();
        if (selection == null) {
            return;
        }
        Path cheminSelection = selection.toPath().toAbsolutePath().normalize();
        Path repertoire = Files.isDirectory(cheminSelection) ? cheminSelection : cheminSelection.getParent();
        Path repertoireValide = repertoireValide(repertoire);
        if (repertoireValide != null) {
            dernierRepertoire = repertoireValide;
        }
    }

    private static Path repertoireValide(Path repertoire) {
        if (repertoire == null) {
            return null;
        }
        Path normalise = repertoire.toAbsolutePath().normalize();
        return Files.isDirectory(normalise) ? normalise : null;
    }
}
