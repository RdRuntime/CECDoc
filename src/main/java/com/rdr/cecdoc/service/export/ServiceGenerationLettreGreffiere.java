package com.rdr.cecdoc.service.export;

import com.rdr.cecdoc.docx.GenerateurLettreGreffiere;
import com.rdr.cecdoc.model.DonneesDossier;
import com.rdr.cecdoc.model.TypeDocumentGenere;
import com.rdr.cecdoc.odt.GenerateurLettreGreffiereOdt;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Objects;

public final class ServiceGenerationLettreGreffiere extends ServiceGenerationLettreAbstrait<DonneesDossier> {
    private final GenerateurLettreGreffiere generateurLettreGreffiereDocx;
    private final GenerateurLettreGreffiereOdt generateurLettreGreffiereOdt;

    public ServiceGenerationLettreGreffiere(EcritureDocxAtomique ecritureDocxAtomique) {
        this(ecritureDocxAtomique, new GenerateurLettreGreffiere(), new GenerateurLettreGreffiereOdt());
    }

    ServiceGenerationLettreGreffiere(EcritureDocxAtomique ecritureDocxAtomique, GenerateurLettreGreffiere generateurLettreGreffiere, GenerateurLettreGreffiereOdt generateurLettreGreffiereOdt) {
        super(ecritureDocxAtomique);
        this.generateurLettreGreffiereDocx = Objects.requireNonNull(generateurLettreGreffiere, "generateurLettreGreffiere");
        this.generateurLettreGreffiereOdt = Objects.requireNonNull(generateurLettreGreffiereOdt, "generateurLettreGreffiereOdt");
    }

    @Override
    protected void generer(DonneesDossier donneesDossier, TypeDocumentGenere typeDocument, Path cheminTemporaire) throws IOException {
        if (typeDocument == TypeDocumentGenere.ODT) {
            generateurLettreGreffiereOdt.generer(donneesDossier, cheminTemporaire.toFile());
            return;
        }
        generateurLettreGreffiereDocx.generer(donneesDossier, cheminTemporaire.toFile());
    }

    public static Path cheminParDefaut(Path cheminRequeteDocx) {
        return cheminParDefaut(cheminRequeteDocx, TypeDocumentGenere.DOCX);
    }

    public static Path cheminParDefaut(Path cheminRequeteDocx, TypeDocumentGenere typeDocument) {
        Objects.requireNonNull(cheminRequeteDocx, "cheminRequeteDocx");
        TypeDocumentGenere typeDocumentEffectif = typeDocument == null ? TypeDocumentGenere.DOCX : typeDocument;
        Path cheminNormalise = cheminRequeteDocx.toAbsolutePath().normalize();
        String nomFichier = cheminNormalise.getFileName() == null ? "requete" : cheminNormalise.getFileName().toString();
        int positionPoint = nomFichier.lastIndexOf('.');
        String racineNom = positionPoint > 0 ? nomFichier.substring(0, positionPoint) : nomFichier;
        String nomSortie = racineNom + "_lettre_greffierE" + typeDocumentEffectif.extension();
        Path repertoireParent = cheminNormalise.getParent();
        return repertoireParent == null ? Path.of(nomSortie) : repertoireParent.resolve(nomSortie);
    }

    public static String extensionParDefaut() {
        return extensionParDefaut(TypeDocumentGenere.DOCX);
    }

    public static String extensionParDefaut(TypeDocumentGenere typeDocument) {
        TypeDocumentGenere typeDocumentEffectif = typeDocument == null ? TypeDocumentGenere.DOCX : typeDocument;
        return typeDocumentEffectif.code().toLowerCase(Locale.ROOT);
    }
}
