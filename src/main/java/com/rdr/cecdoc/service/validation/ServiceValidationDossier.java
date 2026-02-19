package com.rdr.cecdoc.service.validation;

import com.rdr.cecdoc.model.ChampDossier;
import com.rdr.cecdoc.model.InstantaneDossier;
import com.rdr.cecdoc.util.ParseursDate;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;


public final class ServiceValidationDossier implements CasUsageValidationDossier {
    private static final String REQUIRED_BASE = "Ce champ est nécessaire pour générer le document.";

    private final Map<ChampDossier, Function<InstantaneDossier, ProblemeValidation>> fieldStrategies;


    public ServiceValidationDossier() {
        Map<ChampDossier, Function<InstantaneDossier, ProblemeValidation>> strategies = new EnumMap<>(ChampDossier.class);
        strategies.put(ChampDossier.PRENOMS_ETAT_CIVIL, instantane -> require(instantane.prenomsEtatCivil(), ChampDossier.PRENOMS_ETAT_CIVIL, REQUIRED_BASE));
        strategies.put(ChampDossier.PRENOMS_USAGE, this::validateChosenFirstNames);
        strategies.put(ChampDossier.NOM_FAMILLE, instantane -> require(instantane.nomFamille(), ChampDossier.NOM_FAMILLE, REQUIRED_BASE));
        strategies.put(ChampDossier.DATE_NAISSANCE, instantane -> validateBirthDate(instantane.dateNaissance()));
        strategies.put(ChampDossier.LIEU_NAISSANCE, instantane -> require(instantane.lieuNaissance(), ChampDossier.LIEU_NAISSANCE, REQUIRED_BASE));
        strategies.put(ChampDossier.SEXE_ETAT_CIVIL, instantane -> require(instantane.sexeEtatCivil(), ChampDossier.SEXE_ETAT_CIVIL, REQUIRED_BASE));
        strategies.put(ChampDossier.ADRESSE, instantane -> require(instantane.adresse(), ChampDossier.ADRESSE, REQUIRED_BASE));
        strategies.put(ChampDossier.VILLE_ACTUELLE, instantane -> require(instantane.villeActuelle(), ChampDossier.VILLE_ACTUELLE, REQUIRED_BASE));
        strategies.put(ChampDossier.TRIBUNAL, instantane -> require(instantane.tribunal(), ChampDossier.TRIBUNAL, REQUIRED_BASE));
        strategies.put(ChampDossier.RECIT, instantane -> require(instantane.recit(), ChampDossier.RECIT, REQUIRED_BASE));
        strategies.put(ChampDossier.NATIONALITE, instantane -> require(instantane.nationalite(), ChampDossier.NATIONALITE, REQUIRED_BASE));
        strategies.put(ChampDossier.PROFESSION, instantane -> require(instantane.profession(), ChampDossier.PROFESSION, REQUIRED_BASE));
        strategies.put(ChampDossier.SITUATION_MATRIMONIALE, instantane -> require(instantane.situationMatrimoniale(), ChampDossier.SITUATION_MATRIMONIALE, REQUIRED_BASE));
        strategies.put(ChampDossier.SITUATION_ENFANTS, instantane -> require(instantane.situationEnfants(), ChampDossier.SITUATION_ENFANTS, REQUIRED_BASE));
        this.fieldStrategies = Map.copyOf(strategies);
    }


    @Override
    public ResultatValidation validerAvantGeneration(InstantaneDossier instantane) {
        Map<ChampDossier, String> issues = new EnumMap<>(ChampDossier.class);
        for (ChampDossier champ : ChampDossier.values()) {
            ProblemeValidation issue = validerChamp(instantane, champ);
            if (issue != null && issue.message() != null && !issue.message().isBlank()) {
                issues.put(issue.champ(), issue.message());
            }
        }
        String formMessage = issues.isEmpty() ? "" : "Certaines informations sont nécessaires avant la génération.";
        return new ResultatValidation(issues, formMessage);
    }


    @Override
    public ProblemeValidation validerChamp(InstantaneDossier instantane, ChampDossier champ) {
        if (instantane == null || champ == null) {
            return null;
        }
        Function<InstantaneDossier, ProblemeValidation> strategy = fieldStrategies.get(champ);
        if (strategy == null) {
            return null;
        }
        return strategy.apply(instantane);
    }

    private ProblemeValidation validateChosenFirstNames(InstantaneDossier instantane) {
        if (!instantane.changementPrenoms()) {
            return null;
        }
        if (instantane.prenomsUsage().isBlank()) {
            return new ProblemeValidation(ChampDossier.PRENOMS_USAGE, "Ce champ est nécessaire pour générer le document quand le changement de prénoms est coché.");
        }
        return null;
    }

    private ProblemeValidation validateBirthDate(String dateNaissance) {
        if (dateNaissance == null || dateNaissance.isBlank()) {
            return new ProblemeValidation(ChampDossier.DATE_NAISSANCE, REQUIRED_BASE);
        }
        if (!ParseursDate.dateSaisieValide(dateNaissance)) {
            return new ProblemeValidation(ChampDossier.DATE_NAISSANCE, "La date de naissance n'est pas valide.");
        }
        return null;
    }

    private ProblemeValidation require(String value, ChampDossier champ, String message) {
        if (value == null || value.isBlank()) {
            return new ProblemeValidation(champ, message);
        }
        return null;
    }
}
