package com.rdr.cecdoc.service.export;

import com.rdr.cecdoc.docx.GenerateurLettreAdministration;
import com.rdr.cecdoc.model.DonneesLettreAdministration;

import java.nio.file.Path;
import java.util.Objects;

public final class ServiceGenerationLettreAdministration {
    private final EcritureDocxAtomique ecritureDocxAtomique;
    private final GenerateurLettreAdministration generateurLettreAdministration;

    public ServiceGenerationLettreAdministration(EcritureDocxAtomique ecritureDocxAtomique) {
        this(ecritureDocxAtomique, new GenerateurLettreAdministration());
    }

    ServiceGenerationLettreAdministration(EcritureDocxAtomique ecritureDocxAtomique, GenerateurLettreAdministration generateurLettreAdministration) {
        this.ecritureDocxAtomique = Objects.requireNonNull(ecritureDocxAtomique, "ecritureDocxAtomique");
        this.generateurLettreAdministration = Objects.requireNonNull(generateurLettreAdministration, "generateurLettreAdministration");
    }

    public void exporter(DonneesLettreAdministration donneesLettreAdministration, Path destination, boolean ecraser) throws ErreurExportDocument {
        Objects.requireNonNull(donneesLettreAdministration, "donneesLettreAdministration");
        Objects.requireNonNull(destination, "destination");

        ecritureDocxAtomique.ecrire(destination, ecraser, cheminTemporaire -> generateurLettreAdministration.generer(donneesLettreAdministration, cheminTemporaire.toFile()));
    }

}
