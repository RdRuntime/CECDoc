package com.rdr.cecdoc.service;

import com.rdr.cecdoc.model.DonneesDossier;
import com.rdr.cecdoc.model.InstantaneDossier;

import java.util.ArrayList;

public final class ConvertisseurDonnees implements CasUsageConversionDonnees {

    @Override
    public DonneesDossier convertir(InstantaneDossier instantane) {
        if (instantane == null) {
            throw new IllegalArgumentException("instantane");
        }
        return new DonneesDossier(
                instantane.changementPrenoms(),
                instantane.prenomsEtatCivil(),
                instantane.prenomsUsage(),
                instantane.nomFamille(),
                instantane.dateNaissance(),
                instantane.lieuNaissance(),
                instantane.sexeEtatCivil(),
                instantane.adresse(),
                instantane.tribunal(),
                instantane.recit(),
                instantane.villeActuelle(),
                new ArrayList<>(instantane.piecesJustificatives()),
                instantane.nationalite(),
                instantane.profession(),
                instantane.situationMatrimoniale(),
                instantane.situationEnfants(),
                instantane.pacsContracte(),
                instantane.pronomNeutre(),
                new ArrayList<>(instantane.piecesJustificativesDetaillees())
        );
    }
}
