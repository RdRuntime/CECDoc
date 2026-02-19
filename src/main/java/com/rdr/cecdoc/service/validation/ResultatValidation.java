package com.rdr.cecdoc.service.validation;

import com.rdr.cecdoc.model.ChampDossier;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;


public final class ResultatValidation {
    private final EnumMap<ChampDossier, String> fieldMessages;
    private final String formMessage;


    public ResultatValidation(Map<ChampDossier, String> fieldMessages, String formMessage) {
        this.fieldMessages = new EnumMap<>(ChampDossier.class);
        if (fieldMessages != null) {
            this.fieldMessages.putAll(fieldMessages);
        }
        this.formMessage = formMessage == null ? "" : formMessage;
    }


    public boolean estValide() {
        return fieldMessages.isEmpty();
    }


    public String messageGlobal() {
        return formMessage;
    }


    public Map<ChampDossier, String> messagesChamps() {
        return Collections.unmodifiableMap(fieldMessages);
    }


    public String messagePour(ChampDossier champ) {
        return fieldMessages.getOrDefault(champ, "");
    }
}
