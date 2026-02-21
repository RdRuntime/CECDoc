package com.rdr.cecdoc.model;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

public record PieceJointe(String uri, String nomAffichage, TypePieceJointe type) {
    private static final System.Logger JOURNAL = System.getLogger(PieceJointe.class.getName());

    public PieceJointe {
        uri = nettoyer(uri);
        nomAffichage = nettoyer(nomAffichage);
        type = type == null ? TypePieceJointe.INCONNU : type;
    }

    public static PieceJointe depuisChemin(Path chemin) {
        Objects.requireNonNull(chemin, "chemin");
        Path cheminNormalise = chemin.toAbsolutePath().normalize();
        String nomFichier = cheminNormalise.getFileName() == null ? cheminNormalise.toString() : cheminNormalise.getFileName().toString();
        TypePieceJointe typeDetecte = TypePieceJointe.depuisNomFichier(nomFichier);
        return new PieceJointe(cheminNormalise.toUri().toString(), nomFichier, typeDetecte);
    }

    private static String nettoyer(String valeur) {
        return valeur == null ? "" : valeur.trim();
    }

    public Optional<Path> chemin() {
        if (uri.isBlank()) {
            return Optional.empty();
        }
        String uriNormalisee = uri.trim().toLowerCase(Locale.ROOT);
        if (uriNormalisee.startsWith("http://") || uriNormalisee.startsWith("https://")) {
            return Optional.empty();
        }
        URI uriCandidate;
        try {
            uriCandidate = URI.create(uri);
        } catch (RuntimeException ex) {
            uriCandidate = null;
        }
        if (uriCandidate != null && uriCandidate.getScheme() != null && !"file".equalsIgnoreCase(uriCandidate.getScheme())) {
            JOURNAL.log(System.Logger.Level.DEBUG, "Schéma d'URI de pièce jointe non supporté : " + uriCandidate.getScheme());
            return Optional.empty();
        }
        try {
            if (uriCandidate != null && "file".equalsIgnoreCase(uriCandidate.getScheme())) {
                return Optional.of(Path.of(uriCandidate).toAbsolutePath().normalize());
            }
            return Optional.of(Path.of(uri).toAbsolutePath().normalize());
        } catch (RuntimeException ex) {
            JOURNAL.log(System.Logger.Level.DEBUG, "URI de pièce jointe invalide : " + uri, ex);
            return Optional.empty();
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
}
