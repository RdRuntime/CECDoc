package com.rdr.cecdoc.service.export;

import java.io.IOException;
import java.nio.file.*;
import java.util.Objects;

public class EcriturePdfAtomique {
    private static final System.Logger LOGGER = System.getLogger(EcriturePdfAtomique.class.getName());
    private final StrategieDeplacement strategieDeplacement;

    public EcriturePdfAtomique() {
        this(EcriturePdfAtomique::deplacementParDefaut);
    }

    EcriturePdfAtomique(StrategieDeplacement strategieDeplacement) {
        this.strategieDeplacement = Objects.requireNonNull(strategieDeplacement, "strategieDeplacement");
    }

    private static Path creerFichierTemporaire(Path repertoirePrefere) throws IOException {
        try {
            return Files.createTempFile(repertoirePrefere, "cecdoc-pdf-", ".tmp");
        } catch (IOException erreurPrincipale) {
            return Files.createTempFile("cecdoc-pdf-", ".tmp");
        }
    }

    private static void deplacementParDefaut(Path cheminSource, Path cheminDestination, boolean ecraser) throws IOException {
        if (ecraser) {
            Files.move(cheminSource, cheminDestination, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
        } else {
            Files.move(cheminSource, cheminDestination, StandardCopyOption.ATOMIC_MOVE);
        }
    }

    public void ecrire(Path cheminDestination, boolean ecraser, EcrivainTemporaire ecrivain) throws ErreurExportDocument {
        Objects.requireNonNull(cheminDestination, "cheminDestination");
        Objects.requireNonNull(ecrivain, "ecrivain");

        Path cible = cheminDestination.toAbsolutePath().normalize();
        Path parent = cible.getParent();
        if (parent == null) {
            throw ErreurExportDocument.ioFailure(new IOException("Destination invalide"));
        }

        Path temporaire = null;
        boolean deplace = false;
        try {
            Files.createDirectories(parent);
            temporaire = creerFichierTemporaire(parent);
            ecrivain.ecrire(temporaire);

            if (!ecraser && Files.exists(cible)) {
                throw ErreurExportDocument.fileAlreadyExists();
            }

            strategieDeplacement.deplacer(temporaire, cible, ecraser);
            deplace = true;
        } catch (FileAlreadyExistsException ex) {
            throw ErreurExportDocument.fileAlreadyExists();
        } catch (AtomicMoveNotSupportedException ex) {
            throw ErreurExportDocument.atomicMoveUnsupported(ex);
        } catch (AccessDeniedException ex) {
            throw ErreurExportDocument.permissionDenied(ex);
        } catch (NoSuchFileException ex) {
            throw ErreurExportDocument.fileLockedOrUnavailable(ex);
        } catch (IOException ex) {
            throw ErreurExportDocument.ioFailure(ex);
        } finally {
            if (!deplace && temporaire != null) {
                try {
                    Files.deleteIfExists(temporaire);
                } catch (IOException ex) {
                    LOGGER.log(System.Logger.Level.WARNING, "Impossible de supprimer le fichier temporaire: " + temporaire, ex);
                }
            }
        }
    }

    @FunctionalInterface
    public interface EcrivainTemporaire {
        void ecrire(Path cheminTemporaire) throws IOException, ErreurExportDocument;
    }

    @FunctionalInterface
    interface StrategieDeplacement {
        void deplacer(Path cheminSource, Path cheminDestination, boolean ecraser) throws IOException;
    }
}
