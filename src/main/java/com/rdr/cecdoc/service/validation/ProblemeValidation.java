package com.rdr.cecdoc.service.validation;

import com.rdr.cecdoc.model.ChampDossier;

public record ProblemeValidation(ChampDossier champ, String message) {
}
