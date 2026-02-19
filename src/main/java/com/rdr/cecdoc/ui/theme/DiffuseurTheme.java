package com.rdr.cecdoc.ui.theme;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;


public final class DiffuseurTheme {
    private final SelecteurTheme selector;
    private final List<EcouteurTheme> listeners;
    private volatile TokensTheme themeActuel;


    public DiffuseurTheme(SelecteurTheme selector, ModeTheme initialMode) {
        this.selector = Objects.requireNonNull(selector, "selector");
        this.listeners = new CopyOnWriteArrayList<>();
        this.themeActuel = selector.selectionner(initialMode);
    }


    public TokensTheme themeActuel() {
        return themeActuel;
    }


    public void publierMode(ModeTheme mode) {
        TokensTheme next = selector.selectionner(mode);
        themeActuel = next;
        for (EcouteurTheme listener : listeners) {
            listener.themeChange(next);
        }
    }


    public void publierChoixPronom(boolean pronomNeutre) {
        TokensTheme next = selector.selectionnerPourPronom(pronomNeutre);
        themeActuel = next;
        for (EcouteurTheme listener : listeners) {
            listener.themeChange(next);
        }
    }


    public void ajouterEcouteur(EcouteurTheme listener) {
        listeners.add(Objects.requireNonNull(listener, "listener"));
    }


    public void retirerEcouteur(EcouteurTheme listener) {
        listeners.remove(listener);
    }
}
