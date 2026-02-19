package com.rdr.cecdoc.service;

import com.rdr.cecdoc.model.DonneesDossier;
import com.rdr.cecdoc.model.InstantaneDossier;


public interface CasUsageConversionDonnees {


    DonneesDossier convertir(InstantaneDossier instantane);
}
