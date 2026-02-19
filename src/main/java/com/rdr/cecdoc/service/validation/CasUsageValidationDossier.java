package com.rdr.cecdoc.service.validation;

import com.rdr.cecdoc.model.ChampDossier;
import com.rdr.cecdoc.model.InstantaneDossier;


public interface CasUsageValidationDossier {


    ResultatValidation validerAvantGeneration(InstantaneDossier instantane);


    ProblemeValidation validerChamp(InstantaneDossier instantane, ChampDossier champ);
}
