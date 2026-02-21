package com.rdr.cecdoc.service.validation;

import com.rdr.cecdoc.model.ChampDossier;
import com.rdr.cecdoc.model.InstantaneDossier;
import com.rdr.cecdoc.util.ParseursDate;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;

public final class ServiceValidationDossier implements CasUsageValidationDossier {
    private static final String MESSAGE_CHAMP_REQUIS = "Ce champ est nécessaire pour générer le document.";
    private final Map<ChampDossier, Function<InstantaneDossier, ProblemeValidation>> strategiesValidationParChamp;

    public ServiceValidationDossier() {
        Map<ChampDossier, Function<InstantaneDossier, ProblemeValidation>> strategies = new EnumMap<>(ChampDossier.class);
        strategies.put(ChampDossier.PRENOMS_ETAT_CIVIL, instantane -> validerPresence(instantane.prenomsEtatCivil(), ChampDossier.PRENOMS_ETAT_CIVIL, MESSAGE_CHAMP_REQUIS));
        strategies.put(ChampDossier.PRENOMS_USAGE, this::validerPrenomsChoisis);
        strategies.put(ChampDossier.NOM_FAMILLE, instantane -> validerPresence(instantane.nomFamille(), ChampDossier.NOM_FAMILLE, MESSAGE_CHAMP_REQUIS));
        strategies.put(ChampDossier.DATE_NAISSANCE, instantane -> validerDateNaissance(instantane.dateNaissance()));
        strategies.put(ChampDossier.LIEU_NAISSANCE, instantane -> validerPresence(instantane.lieuNaissance(), ChampDossier.LIEU_NAISSANCE, MESSAGE_CHAMP_REQUIS));
        strategies.put(ChampDossier.SEXE_ETAT_CIVIL, instantane -> validerPresence(instantane.sexeEtatCivil(), ChampDossier.SEXE_ETAT_CIVIL, MESSAGE_CHAMP_REQUIS));
        strategies.put(ChampDossier.ADRESSE, instantane -> validerPresence(instantane.adresse(), ChampDossier.ADRESSE, MESSAGE_CHAMP_REQUIS));
        strategies.put(ChampDossier.VILLE_ACTUELLE, instantane -> validerPresence(instantane.villeActuelle(), ChampDossier.VILLE_ACTUELLE, MESSAGE_CHAMP_REQUIS));
        strategies.put(ChampDossier.TRIBUNAL, instantane -> validerPresence(instantane.tribunal(), ChampDossier.TRIBUNAL, MESSAGE_CHAMP_REQUIS));
        strategies.put(ChampDossier.RECIT, instantane -> validerPresence(instantane.recit(), ChampDossier.RECIT, MESSAGE_CHAMP_REQUIS));
        strategies.put(ChampDossier.NATIONALITE, instantane -> validerPresence(instantane.nationalite(), ChampDossier.NATIONALITE, MESSAGE_CHAMP_REQUIS));
        strategies.put(ChampDossier.PROFESSION, instantane -> validerPresence(instantane.profession(), ChampDossier.PROFESSION, MESSAGE_CHAMP_REQUIS));
        strategies.put(ChampDossier.SITUATION_MATRIMONIALE, instantane -> validerPresence(instantane.situationMatrimoniale(), ChampDossier.SITUATION_MATRIMONIALE, MESSAGE_CHAMP_REQUIS));
        strategies.put(ChampDossier.SITUATION_ENFANTS, instantane -> validerPresence(instantane.situationEnfants(), ChampDossier.SITUATION_ENFANTS, MESSAGE_CHAMP_REQUIS));
        strategiesValidationParChamp = Map.copyOf(strategies);
    }

    @Override
    public ResultatValidation validerAvantGeneration(InstantaneDossier instantane) {
        Map<ChampDossier, String> messagesParChamp = new EnumMap<>(ChampDossier.class);
        for (ChampDossier champ : ChampDossier.values()) {
            ProblemeValidation probleme = validerChamp(instantane, champ);
            if (probleme != null && probleme.message() != null && !probleme.message().isBlank()) {
                messagesParChamp.put(probleme.champ(), probleme.message());
            }
        }
        String messageGlobal = messagesParChamp.isEmpty() ? "" : "Certaines informations sont nécessaires avant la génération.";
        return new ResultatValidation(messagesParChamp, messageGlobal);
    }

    @Override
    public ProblemeValidation validerChamp(InstantaneDossier instantane, ChampDossier champ) {
        if (instantane == null || champ == null) {
            return null;
        }
        Function<InstantaneDossier, ProblemeValidation> strategieValidation = strategiesValidationParChamp.get(champ);
        if (strategieValidation == null) {
            return null;
        }
        return strategieValidation.apply(instantane);
    }

    private ProblemeValidation validerPrenomsChoisis(InstantaneDossier instantane) {
        if (!instantane.changementPrenoms()) {
            return null;
        }
        if (instantane.prenomsUsage().isBlank()) {
            return new ProblemeValidation(ChampDossier.PRENOMS_USAGE, "Ce champ est nécessaire pour générer le document quand le changement de prénoms est coché.");
        }
        return null;
    }

    private ProblemeValidation validerDateNaissance(String dateNaissance) {
        if (dateNaissance == null || dateNaissance.isBlank()) {
            return new ProblemeValidation(ChampDossier.DATE_NAISSANCE, MESSAGE_CHAMP_REQUIS);
        }
        if (!ParseursDate.dateSaisieValide(dateNaissance)) {
            return new ProblemeValidation(ChampDossier.DATE_NAISSANCE, "La date de naissance n'est pas valide.");
        }
        if (!ParseursDate.dateSaisieNonFuture(dateNaissance)) {
            return new ProblemeValidation(ChampDossier.DATE_NAISSANCE, "La date de naissance ne peut pas être dans le futur.");
        }
        return null;
    }

    private ProblemeValidation validerPresence(String valeur, ChampDossier champ, String message) {
        if (valeur == null || valeur.isBlank()) {
            return new ProblemeValidation(champ, message);
        }
        return null;
    }
}
