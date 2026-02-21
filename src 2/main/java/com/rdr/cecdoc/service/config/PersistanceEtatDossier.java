package com.rdr.cecdoc.service.config;

import java.util.Optional;

public interface PersistanceEtatDossier {
    Optional<EtatDossierPersistant> charger();

    void sauvegarder(EtatDossierPersistant state);

    void effacer();

    default boolean consommerSignalConfigurationObsoleteSupprimee() {
        return false;
    }
}
