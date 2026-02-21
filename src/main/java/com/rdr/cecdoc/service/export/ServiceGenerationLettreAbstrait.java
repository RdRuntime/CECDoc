package com.rdr.cecdoc.service.export;

import com.rdr.cecdoc.model.TypeDocumentGenere;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

abstract class ServiceGenerationLettreAbstrait<T> {
    private final EcritureDocxAtomique ecritureAtomique;

    ServiceGenerationLettreAbstrait(EcritureDocxAtomique ecritureAtomique) {
        this.ecritureAtomique = Objects.requireNonNull(ecritureAtomique, "ecritureAtomique");
    }

    public final void exporter(T donnees, Path destination, boolean ecraser) throws ErreurExportDocument {
        Objects.requireNonNull(donnees, "donnees");
        Objects.requireNonNull(destination, "destination");

        TypeDocumentGenere typeDocument = TypeDocumentGenere.depuisChemin(destination);
        ecritureAtomique.ecrire(destination, ecraser, cheminTemporaire -> generer(donnees, typeDocument, cheminTemporaire));
    }

    protected abstract void generer(T donnees, TypeDocumentGenere typeDocument, Path cheminTemporaire) throws IOException;
}
