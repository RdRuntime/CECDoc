package com.rdr.cecdoc.service.validation;

import com.rdr.cecdoc.model.ChampDossier;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public final class ResultatValidation {
    private final EnumMap<ChampDossier, String> messagesParChamp;
    private final String messageGlobal;

    public ResultatValidation(Map<ChampDossier, String> messagesParChamp, String messageGlobal) {
        this.messagesParChamp = new EnumMap<>(ChampDossier.class);
        if (messagesParChamp != null) {
            this.messagesParChamp.putAll(messagesParChamp);
        }
        this.messageGlobal = messageGlobal == null ? "" : messageGlobal;
    }

    public boolean estValide() {
        return messagesParChamp.isEmpty();
    }

    public String messageGlobal() {
        return messageGlobal;
    }

    public Map<ChampDossier, String> messagesChamps() {
        return Collections.unmodifiableMap(messagesParChamp);
    }

    public String messagePour(ChampDossier champ) {
        return messagesParChamp.getOrDefault(champ, "");
    }
}
