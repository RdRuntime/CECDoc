package com.rdr.cecdoc.model;

import java.util.List;
import java.util.Objects;

public record PieceJustificative(String intitule, List<PieceJointe> piecesJointes) {

    public PieceJustificative {
        intitule = intitule == null ? "" : intitule.trim();
        piecesJointes = piecesJointes == null ? List.of() : piecesJointes.stream().filter(Objects::nonNull).toList();
    }
}
