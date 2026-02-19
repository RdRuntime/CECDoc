package com.rdr.cecdoc.model;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public record PieceJointe(String uri, String nomAffichage, TypePieceJointe type) {

    public PieceJointe {
        uri = nettoyer(uri);
        nomAffichage = nettoyer(nomAffichage);
        type = type == null ? TypePieceJointe.INCONNU : type;
    }

    public static PieceJointe depuisChemin(Path chemin) {
        Path cheminNormalise = chemin.toAbsolutePath().normalize();
        String nomFichier = cheminNormalise.getFileName() == null ? cheminNormalise.toString() : cheminNormalise.getFileName().toString();
        TypePieceJointe typeDetecte = TypePieceJointe.depuisNomFichier(nomFichier);
        return new PieceJointe(cheminNormalise.toUri().toString(), nomFichier, typeDetecte);
    }

    public Optional<Path> chemin() {
        if (uri.isBlank()) {
            return Optional.empty();
        }
        try {
            return Optional.of(Path.of(URI.create(uri)).toAbsolutePath().normalize());
        } catch (RuntimeException ex) {
            try {
                return Optional.of(Path.of(uri).toAbsolutePath().normalize());
            } catch (RuntimeException ignoree) {
                return Optional.empty();
            }
        }
    }

    public boolean existe() {
        return chemin().map(Files::isRegularFile).orElse(false);
    }

    public String nomVisible() {
        if (!nomAffichage.isBlank()) {
            return nomAffichage;
        }
        return chemin().flatMap(path -> Optional.ofNullable(path.getFileName())).map(Path::toString).orElse(uri);
    }

    private static String nettoyer(String valeur) {
        return valeur == null ? "" : valeur.trim();
    }
}
