package com.rdr.cecdoc.service.export;

import com.rdr.cecdoc.docx.GenerateurLettreUniversite;
import com.rdr.cecdoc.model.DonneesLettreUniversite;

import java.nio.file.Path;
import java.util.Objects;

public final class ServiceGenerationLettreUniversite {
    private final EcritureDocxAtomique ecritureDocxAtomique;
    private final GenerateurLettreUniversite generateurLettreUniversite;

    public ServiceGenerationLettreUniversite(EcritureDocxAtomique ecritureDocxAtomique) {
        this(ecritureDocxAtomique, new GenerateurLettreUniversite());
    }

    ServiceGenerationLettreUniversite(EcritureDocxAtomique ecritureDocxAtomique, GenerateurLettreUniversite generateurLettreUniversite) {
        this.ecritureDocxAtomique = Objects.requireNonNull(ecritureDocxAtomique, "ecritureDocxAtomique");
        this.generateurLettreUniversite = Objects.requireNonNull(generateurLettreUniversite, "generateurLettreUniversite");
    }

    public void exporter(DonneesLettreUniversite donneesLettreUniversite, Path destination, boolean ecraser) throws ErreurExportDocument {
        Objects.requireNonNull(donneesLettreUniversite, "donneesLettreUniversite");
        Objects.requireNonNull(destination, "destination");

        ecritureDocxAtomique.ecrire(destination, ecraser, cheminTemporaire -> generateurLettreUniversite.generer(donneesLettreUniversite, cheminTemporaire.toFile()));
    }

}
