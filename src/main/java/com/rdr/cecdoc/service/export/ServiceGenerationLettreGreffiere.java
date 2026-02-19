package com.rdr.cecdoc.service.export;

import com.rdr.cecdoc.docx.GenerateurLettreGreffiere;
import com.rdr.cecdoc.model.DonneesDossier;

import java.nio.file.Path;
import java.util.Locale;
import java.util.Objects;

public final class ServiceGenerationLettreGreffiere {
    private final EcritureDocxAtomique ecritureDocxAtomique;
    private final GenerateurLettreGreffiere generateurLettreGreffiere;

    public ServiceGenerationLettreGreffiere(EcritureDocxAtomique ecritureDocxAtomique) {
        this(ecritureDocxAtomique, new GenerateurLettreGreffiere());
    }

    ServiceGenerationLettreGreffiere(EcritureDocxAtomique ecritureDocxAtomique, GenerateurLettreGreffiere generateurLettreGreffiere) {
        this.ecritureDocxAtomique = Objects.requireNonNull(ecritureDocxAtomique, "ecritureDocxAtomique");
        this.generateurLettreGreffiere = Objects.requireNonNull(generateurLettreGreffiere, "generateurLettreGreffiere");
    }

    public void exporter(DonneesDossier donneesDossier, Path destinationLettre, boolean ecraser) throws ErreurExportDocument {
        Objects.requireNonNull(donneesDossier, "donneesDossier");
        Objects.requireNonNull(destinationLettre, "destinationLettre");

        ecritureDocxAtomique.ecrire(destinationLettre, ecraser, cheminTemporaire -> generateurLettreGreffiere.generer(donneesDossier, cheminTemporaire.toFile()));
    }

    public static Path cheminParDefaut(Path cheminRequeteDocx) {
        Objects.requireNonNull(cheminRequeteDocx, "cheminRequeteDocx");
        Path cheminNormalise = cheminRequeteDocx.toAbsolutePath().normalize();
        String nomFichier = cheminNormalise.getFileName() == null ? "requete" : cheminNormalise.getFileName().toString();
        int positionPoint = nomFichier.lastIndexOf('.');
        String racineNom = positionPoint > 0 ? nomFichier.substring(0, positionPoint) : nomFichier;
        String nomSortie = racineNom + "_lettre_greffierE.docx";
        Path repertoireParent = cheminNormalise.getParent();
        return repertoireParent == null ? Path.of(nomSortie) : repertoireParent.resolve(nomSortie);
    }

    public static String extensionParDefaut() {
        return "docx".toLowerCase(Locale.ROOT);
    }
}
