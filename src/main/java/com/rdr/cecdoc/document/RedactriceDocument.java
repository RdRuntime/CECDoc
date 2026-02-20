package com.rdr.cecdoc.document;

import java.io.File;
import java.io.IOException;

public interface RedactriceDocument extends AutoCloseable {
    void ajouterParagraphe(AlignementTexte alignement, String texte, boolean gras, boolean italique, int taillePolice, boolean sautPageAvant);

    void ajouterSautPage();

    void enregistrer(File fichierSortie) throws IOException;

    @Override
    void close() throws IOException;
}
