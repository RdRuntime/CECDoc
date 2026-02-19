package com.rdr.cecdoc.service.export;

import com.rdr.cecdoc.model.DonneesDossier;
import com.rdr.cecdoc.model.PieceJointe;
import com.rdr.cecdoc.model.PieceJustificative;
import com.rdr.cecdoc.model.TypePieceJointe;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public final class ServicePdfDossierComplet {
    public interface ConvertisseurWordVersPdf {
        void convertir(Path fichierSource, Path fichierDestination, String nomFichier) throws ErreurExportDocument;
    }

    public interface ConvertisseurImageVersPdf {
        void convertir(Path fichierSource, Path fichierDestination, String nomFichier) throws ErreurExportDocument;
    }

    public interface FusionneurPdf {
        void fusionner(List<Path> fichiersSources, Path fichierDestination) throws ErreurExportDocument;
    }

    private static final System.Logger LOGGER = System.getLogger(ServicePdfDossierComplet.class.getName());

    private final EcriturePdfAtomique ecriturePdfAtomique;
    private final ConvertisseurWordVersPdf convertisseurWordVersPdf;
    private final ConvertisseurImageVersPdf convertisseurImageVersPdf;
    private final FusionneurPdf fusionneurPdf;

    public ServicePdfDossierComplet(EcriturePdfAtomique ecriturePdfAtomique) {
        this(ecriturePdfAtomique, new ConvertisseurWordVersPdfDirect(), new ConvertisseurImageVersPdfPdfBox(), new FusionneurPdfPdfBox());
    }

    ServicePdfDossierComplet(EcriturePdfAtomique ecriturePdfAtomique, ConvertisseurWordVersPdf convertisseurWordVersPdf, ConvertisseurImageVersPdf convertisseurImageVersPdf, FusionneurPdf fusionneurPdf) {
        this.ecriturePdfAtomique = Objects.requireNonNull(ecriturePdfAtomique, "ecriturePdfAtomique");
        this.convertisseurWordVersPdf = Objects.requireNonNull(convertisseurWordVersPdf, "convertisseurWordVersPdf");
        this.convertisseurImageVersPdf = Objects.requireNonNull(convertisseurImageVersPdf, "convertisseurImageVersPdf");
        this.fusionneurPdf = Objects.requireNonNull(fusionneurPdf, "fusionneurPdf");
    }

    public void exporter(DonneesDossier donnees, Path documentWord, boolean ecraser) throws ErreurExportDocument {
        Path destinationPdf = cheminPdfDossier(documentWord);
        exporter(donnees, documentWord, destinationPdf, null, ecraser);
    }

    public void exporter(DonneesDossier donnees, Path documentWord, Path destinationPdf, boolean ecraser) throws ErreurExportDocument {
        exporter(donnees, documentWord, destinationPdf, null, ecraser);
    }

    public void exporter(DonneesDossier donnees, Path documentWord, Path destinationPdf, Path documentWordEntetePdf, boolean ecraser) throws ErreurExportDocument {
        Objects.requireNonNull(donnees, "donnees");
        Objects.requireNonNull(documentWord, "documentWord");
        Objects.requireNonNull(destinationPdf, "destinationPdf");
        Path documentWordNormalise = documentWord.toAbsolutePath().normalize();
        Path documentWordEnteteNormalise = documentWordEntetePdf == null ? null : documentWordEntetePdf.toAbsolutePath().normalize();

        ecriturePdfAtomique.ecrire(destinationPdf, ecraser, pdfTemporaire -> assemblerPdfFinal(donnees, documentWordNormalise, documentWordEnteteNormalise, pdfTemporaire));
    }

    public static Path cheminPdfDossier(Path documentWord) {
        Objects.requireNonNull(documentWord, "documentWord");
        Path cheminNormalise = documentWord.toAbsolutePath().normalize();
        String nomFichier = cheminNormalise.getFileName() == null ? "dossier" : cheminNormalise.getFileName().toString();
        int indexPoint = nomFichier.lastIndexOf('.');
        String base = indexPoint > 0 ? nomFichier.substring(0, indexPoint) : nomFichier;
        String nomSortie = base + "_dossier_complet.pdf";
        Path parent = cheminNormalise.getParent();
        return parent == null ? Path.of(nomSortie) : parent.resolve(nomSortie);
    }

    private void assemblerPdfFinal(DonneesDossier donnees, Path documentWord, Path documentWordEntetePdf, Path destinationTemporairePdf) throws ErreurExportDocument {
        Path repertoireTemporaire = creerEspaceTemporaire();
        try {
            List<Path> fichiersSources = new ArrayList<>();
            int sequence = 0;

            if (documentWordEntetePdf != null) {
                if (!Files.isRegularFile(documentWordEntetePdf)) {
                    throw ErreurExportDocument.ioFailure(new IOException("Document d'entête PDF introuvable: " + documentWordEntetePdf));
                }
                Path pdfEntete = repertoireTemporaire.resolve(String.format("%03d-entete.pdf", sequence++));
                convertisseurWordVersPdf.convertir(documentWordEntetePdf, pdfEntete, documentWordEntetePdf.getFileName() == null ? documentWordEntetePdf.toString() : documentWordEntetePdf.getFileName().toString());
                fichiersSources.add(pdfEntete);
            }

            Path pdfRequete = repertoireTemporaire.resolve(String.format("%03d-requete.pdf", sequence++));
            convertisseurWordVersPdf.convertir(documentWord, pdfRequete, documentWord.getFileName() == null ? documentWord.toString() : documentWord.getFileName().toString());
            fichiersSources.add(pdfRequete);

            for (PieceJustificative piece : donnees.piecesJustificativesDetaillees()) {
                if (piece == null || piece.piecesJointes().isEmpty()) {
                    continue;
                }
                for (PieceJointe fichierJoint : piece.piecesJointes()) {
                    Path cheminSource = fichierJoint.chemin().orElseThrow(() -> ErreurExportDocument.attachmentInvalidPath(fichierJoint.nomVisible()));
                    if (!Files.isRegularFile(cheminSource)) {
                        throw ErreurExportDocument.attachmentMissing(fichierJoint.nomVisible());
                    }

                    TypePieceJointe type = fichierJoint.type() == TypePieceJointe.INCONNU ? TypePieceJointe.depuisNomFichier(fichierJoint.nomVisible()) : fichierJoint.type();

                    switch (type) {
                        case PDF -> fichiersSources.add(cheminSource);
                        case WORD -> {
                            Path pdfConverti = repertoireTemporaire.resolve(String.format("%03d-%s.pdf", sequence++, normaliserNomFichier(fichierJoint.nomVisible())));
                            convertisseurWordVersPdf.convertir(cheminSource, pdfConverti, fichierJoint.nomVisible());
                            fichiersSources.add(pdfConverti);
                        }
                        case IMAGE -> {
                            Path pdfConverti = repertoireTemporaire.resolve(String.format("%03d-%s.pdf", sequence++, normaliserNomFichier(fichierJoint.nomVisible())));
                            convertisseurImageVersPdf.convertir(cheminSource, pdfConverti, fichierJoint.nomVisible());
                            fichiersSources.add(pdfConverti);
                        }
                        case INCONNU -> throw ErreurExportDocument.attachmentUnsupportedType(fichierJoint.nomVisible());
                    }
                }
            }

            if (fichiersSources.isEmpty()) {
                throw ErreurExportDocument.pdfMergeFailure(new IOException("Aucun document à fusionner"));
            }
            fusionneurPdf.fusionner(fichiersSources, destinationTemporairePdf);
        } finally {
            supprimerRecursivementSilencieusement(repertoireTemporaire);
        }
    }

    private static String normaliserNomFichier(String valeur) {
        if (valeur == null || valeur.isBlank()) {
            return "piece";
        }
        return valeur.replaceAll("[^A-Za-z0-9._-]", "_");
    }

    private static Path creerEspaceTemporaire() throws ErreurExportDocument {
        try {
            return Files.createTempDirectory("cecdoc-pdf-");
        } catch (IOException ex) {
            throw ErreurExportDocument.ioFailure(ex);
        }
    }

    private static void supprimerRecursivementSilencieusement(Path repertoire) {
        if (repertoire == null || !Files.exists(repertoire)) {
            return;
        }
        try (Stream<Path> parcours = Files.walk(repertoire)) {
            parcours.sorted(Comparator.reverseOrder()).forEach(chemin -> {
                try {
                    Files.deleteIfExists(chemin);
                } catch (IOException ex) {
                    LOGGER.log(System.Logger.Level.WARNING, "Impossible de supprimer l'artefact PDF temporaire: " + chemin, ex);
                }
            });
        } catch (IOException ex) {
            LOGGER.log(System.Logger.Level.WARNING, "Impossible de parcourir le répertoire PDF temporaire: " + repertoire, ex);
        }
    }
}
